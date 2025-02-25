package cool.codegen;

import cool.lexer.CoolLexer;
import cool.parser.CoolASTBaseRecursiveVisitor;
import cool.parser.CoolASTNode.*;
import cool.structures.SymbolTable;
import cool.structures.Symbol.ArgSymbol;
import cool.structures.Symbol.AttrSymbol;
import cool.structures.Symbol.ClassSymbol;
import cool.structures.Symbol.IdSymbol;
import cool.structures.Symbol.LocalSymbol;
import cool.structures.Symbol.VarSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.misc.Pair;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class CoolCodegenVisitor extends CoolASTBaseRecursiveVisitor<ST> {
    private List<ObjectLayout> allIntegers = new ArrayList<>();
    private List<StringLiteral> allStrings = new ArrayList<>();
    private Map<String, Integer> stringLabels = new HashMap<>();
    private Map<String, Integer> intLabels = new HashMap<>();
    private Map<ClassSymbol, ClassData> classesData = new LinkedHashMap<>();

    private static Map<ClassSymbol, String> defaultValues = new HashMap<>();
    static {
        defaultValues.put(SymbolTable.Int, "int_const0");
        defaultValues.put(SymbolTable.String, "str_const0");
        defaultValues.put(SymbolTable.Bool, "bool_const0");
    }

    private CoolTempLocationsVisitor tempLocVisitor = new CoolTempLocationsVisitor();
    private int tempLoc = 0;
    private int dispIndex = 0;
    private int branchIndex = 0;
    private int caseIndex = 0;

    private class StringLiteral extends ObjectLayout {
        public final String value;
        @SuppressWarnings("unused")
        public final String template = "stringObject";

        public StringLiteral(String value) {
            super(SymbolTable.String, List.of("int_const" + intLabels.get("" + value.length())));
            this.value = value.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"");
        }

        @Override
        public int getMemsize() {
            return (int)Math.ceil((value.length() + 1) / 4.0) + 4;
        }
    };

    private class ClassData {
        private ClassSymbol classSymbol;

        @SuppressWarnings("unused")
        public final ObjectLayout protObj;
        public final Map<String, Pair<String, Integer>> methods;
        public final List<IdSymbol> attributes;
        
        public ClassData(ClassSymbol classSymbol) {
            this.classSymbol = classSymbol;
            this.methods = classSymbol.classScope.getMethods();
            this.attributes = classSymbol.classScope.getAttributes();
            this.protObj = computeProtObj();
        }

        @SuppressWarnings("unused")
        public String getName() {
            return classSymbol.getName();
        }

        @SuppressWarnings("unused")
        public String getNameLabel() {
            return stringLabels.get(classSymbol.getName()).toString();
        }

        @SuppressWarnings("unused")
        public List<String> getDispTab() {
            return methods.entrySet().stream()
                .map(e -> e.getValue().a + "." + e.getKey())
                .collect(Collectors.toList());
        }

        @SuppressWarnings("unused")
        public String getParent() {
            return (classSymbol.getParent() != null) ? classSymbol.getParent().getName() : null;
        }

        private ObjectLayout computeProtObj() {
            if (classSymbol == SymbolTable.String)
                return new StringLiteral("");

            List<String> attrs = new ArrayList<>();
            for (var attr : attributes) {
                attrs.add(defaultValues.getOrDefault(attr.getClassType(), "0"));
            }
            if (classSymbol == SymbolTable.Int || classSymbol == SymbolTable.Bool)
                attrs.add("0");

            return new ObjectLayout(classSymbol, attrs);
        }
    }

    private STGroup dataTemplates = new STGroupFile("cool/codegen/data.stg", '$', '$');
    private STGroup codeTemplates = new STGroupFile("cool/codegen/text.stg", '$', '$');

    public CoolCodegenVisitor(Set<String> integers, Set<String> allStrings, 
                              List<ClassSymbol> classes) {
        Set<String> allIntegers = allStrings.stream().map(s -> "" + s.length())
            .collect(Collectors.toCollection(LinkedHashSet::new));
        allIntegers.addAll(integers);
        int label;

        label = 0;
        for (String s : allIntegers) {
            this.allIntegers.add(new ObjectLayout(SymbolTable.Int, List.of(s)));
            intLabels.put(s, label++);
        }

        label = 0;
        for (String s : allStrings) {
            this.allStrings.add(new StringLiteral(s));
            stringLabels.put(s, label++);
        }

        this.classesData = classes.stream().collect(
            Collectors.toMap(c -> c, ClassData::new, (a, b) -> a, LinkedHashMap::new));
    }

    @Override
    public ST visitProgram(CoolProgram program) {
        ST dataTemplate = dataTemplates.getInstanceOf("data");
        dataTemplate.add("intTag", SymbolTable.Int.getOrder());
        dataTemplate.add("stringTag", SymbolTable.String.getOrder());
        dataTemplate.add("boolTag", SymbolTable.Bool.getOrder());
        dataTemplate.add("intConsts", allIntegers);
        dataTemplate.add("stringConsts", allStrings);
        dataTemplate.add("classes", classesData.values());

        List<ST> classesCode = program.classes.stream().map(this::visitClass).toList();

        ST codeTemplate = codeTemplates.getInstanceOf("code");
        codeTemplate.add("classes", classesCode);

        ST all = new ST("$data$\n$code$", '$', '$');
        all.add("data", dataTemplate);
        all.add("code", codeTemplate);

        return all;
    }

    @Override
    public ST visitClass(CoolClass cclass) {
        ClassSymbol parentSymbol = cclass.getSymbol().getParent();
        String parent = parentSymbol != null && parentSymbol != SymbolTable.Object ?
            parentSymbol.getName() : null;  // just a little optimization for the Object case
        boolean emptyInit = (parent == null) && cclass.attributes.stream().allMatch(a -> a.init == null);

        tempLoc = 0;
        List<ST> attrInits = cclass.attributes.stream().map(this::visitAttribute).toList();

        tempLoc = 0;
        List<ST> methodsCode = cclass.methods.stream().map(this::visitMethod).toList();

        ST classCode = codeTemplates.getInstanceOf("class");
        classCode.add("name", cclass.name);
        classCode.add("parent", parent);
        classCode.add("emptyInit", emptyInit);  // another little optimization
        classCode.add("attrInits", attrInits);
        classCode.add("methods", methodsCode);

        int ntemp = tempLocVisitor.visit(cclass);
        if (ntemp > 0) classCode.add("tempsize", ntemp * 4);

        return classCode;
    }

    private int getVarLocation(VarSymbol varSymbol) {
        if (varSymbol.store == AttrSymbol.attrStore) {
            return 12 + varSymbol.getIndex() * 4;
        } else if (varSymbol.store == ArgSymbol.argStore) {
            return 12 + varSymbol.getIndex() * 4;
        }
        return varSymbol.getIndex();
    }

    @Override
    public ST visitAttribute(CoolAttribute attr) {
        if (attr.init == null)
            return new ST("");

        ST attrInit = codeTemplates.getInstanceOf("assign");
        attrInit.add("e_code", visitExpr(attr.init));
        attrInit.add("store", attr.getSymbol().store);
        attrInit.add("loc", getVarLocation(attr.getSymbol()));
        return attrInit;
    }

    @Override
    public ST visitMethod(CoolMethod method) {
        ST methodCode = codeTemplates.getInstanceOf("method");
        ClassSymbol classSymbol = method.getScope().classSymbol;

        methodCode.add("name", classSymbol.getName() + "." + method.name);
        methodCode.add("body", visitExpr(method.body));
        methodCode.add("headersize", 12 + method.formalArgs.size() * 4);

        int ntemp = tempLocVisitor.visit(method);
        if (ntemp > 0) methodCode.add("tempsize", ntemp * 4);

        return methodCode;
    }

    @Override
    public ST visitExplicitDispatch(CoolExplicitDispatch dispatch) {
        // unfortunately, I can't check whether this is an explicit dispatch on 'self'
        // so, this optimization will only apply to the implicit dispatch case
        boolean isSelf = dispatch.object == null;

        int argssize = dispatch.params.size() * 4;
        List<ST> argsCode = new ArrayList<>();
        for (int i = 0; i < dispatch.params.size(); ++i) {
            ST argCode = codeTemplates.getInstanceOf("pushArg");
            argCode.add("arg_code", visitExpr(dispatch.params.get(i)));
            argCode.add("loc", (i + 1) * 4);
            argsCode.add(argCode);
        }

        ST dispatchCode = codeTemplates.getInstanceOf("dispatch" + (isSelf ? "Self" : ""));
        dispatchCode.add("methodOffset", dispatch.getSymbol().index * 4);
        dispatchCode.add("args_code", argsCode);
        if (argssize > 0) dispatchCode.add("argssize", argssize);
        if (isSelf) return dispatchCode;

        dispatchCode.add("obj_code", visitExpr(dispatch.object));
        dispatchCode.add("dispIndex", dispIndex++);
        dispatchCode.add("fileNameLabel", stringLabels.get(SymbolTable.getFileName(dispatch.getContext())));
        dispatchCode.add("lineNo", dispatch.getToken().getLine());

        if (dispatch.staticClass != null) {
            dispatchCode.add("staticClass", dispatch.staticClass);
        }
        return dispatchCode;
    }

    @Override
    public ST visitBlock(CoolBlock block) {
        ST blockCode = codeTemplates.getInstanceOf("block");
        List<ST> exprsCode = block.exprs.stream().map(this::visitExpr).toList();
        blockCode.add("exprs_code", exprsCode);
        return blockCode;
    }

    @Override
    public ST visitAssignment(CoolAssignment assignment) {
        ST assignCode = codeTemplates.getInstanceOf("assign");
        assignCode.add("e_code", visitExpr(assignment.value));

        // the variable in an assignment cannot be 'self'
        VarSymbol varSymbol = (VarSymbol)assignment.variable.getSymbol();
        assignCode.add("store", varSymbol.store);
        assignCode.add("loc", getVarLocation(varSymbol));

        return assignCode;
    }

    @Override
    public ST visitLet(CoolLet let) {
        ST letCode = codeTemplates.getInstanceOf("let");
        List<ST> bindingsCode = new ArrayList<>();

        for (int i = 0; i < let.bindings.size(); ++i) {
            var binding = let.bindings.get(i);
            ST bindingCode = codeTemplates.getInstanceOf("assign");
            if (binding.init == null) {
                var defVal = defaultValues.getOrDefault(binding.getSymbol().getClassType(), "");
                ST literalCode = codeTemplates.getInstanceOf("literal");
                ST voidCode = codeTemplates.getInstanceOf("void");
                literalCode.add("value", defVal);
                bindingCode.add("e_code", defVal.equals("") ? voidCode : literalCode);
            } else {
                bindingCode.add("e_code", visitExpr(binding.init));
            }
            binding.getSymbol().setIndex(tempLoc += 4);
            bindingCode.add("store", LocalSymbol.localStore);
            bindingCode.add("loc", tempLoc);
            bindingsCode.add(bindingCode);
        }
        letCode.add("bindings_code", bindingsCode);
        letCode.add("body_code", visitExpr(let.body));
        tempLoc -= 4 * let.bindings.size();
        return letCode;
    }

    @Override
    public ST visitNewExpr(CoolNewExpr newexpr) {
        if (!newexpr.type.equals("SELF_TYPE")) {
            ST newCode = codeTemplates.getInstanceOf("new");
            newCode.add("className", newexpr.type);
            return newCode;
        }
        ST newCode = codeTemplates.getInstanceOf("newSelfType");
        newCode.add("temp", tempLoc + 4);  // reusable temp location
        return newCode;
    }
    
    @Override
    public ST visitIf(CoolIf cif) {
        ST ifCode = codeTemplates.getInstanceOf("if");
        ifCode.add("cond_code", visitExpr(cif.cond));
        ifCode.add("then_code", visitExpr(cif.thenBranch));
        ifCode.add("else_code", visitExpr(cif.elseBranch));
        ifCode.add("branchIndex", branchIndex++);
        return ifCode;
    }

    @Override
    public ST visitUnaryOperation(CoolUnaryOperation operation) {
        ST unaryCode = null;
        if (operation.getToken().getType() == CoolLexer.ISVOID) {
            unaryCode = codeTemplates.getInstanceOf("isvoid");
            unaryCode.add("branchIndex", branchIndex++);
        } else if (operation.getToken().getType() == CoolLexer.NOT) {
            unaryCode = codeTemplates.getInstanceOf("not");
            unaryCode.add("branchIndex", branchIndex++);
        } else if (operation.getToken().getType() == CoolLexer.NEG) {
            unaryCode = codeTemplates.getInstanceOf("neg");
        }  
        unaryCode.add("e_code", visitExpr(operation.operand));
        return unaryCode;
    }

    @Override
    public ST visitBinaryOperation(CoolBinaryOperation operation) {
        ST binopCode;
        Map<Integer, String> arithOps = Map.of(
            CoolLexer.PLUS, "add", 
            CoolLexer.SUB, "sub",
            CoolLexer.MUL, "mul",
            CoolLexer.DIV, "div"
        );
        int op = operation.getOpToken().getType();
        if (arithOps.keySet().contains(op)) {
            binopCode = codeTemplates.getInstanceOf("arithop");
            binopCode.add("op", arithOps.get(op));
        } else if (op == CoolLexer.EQ) {
            binopCode = codeTemplates.getInstanceOf("equal");
            binopCode.add("branchIndex", branchIndex++);
        } else /* LT, LEQ */ {
            binopCode = codeTemplates.getInstanceOf("comp");
            binopCode.add("rel", op == CoolLexer.LT ? "lt" : "leq");
            binopCode.add("branchIndex", branchIndex++);
        }

        binopCode.add("e1_code", visitExpr(operation.lhs));
        binopCode.add("temp", tempLoc += 4);
        binopCode.add("e2_code", visitExpr(operation.rhs));
        tempLoc -= 4;
        return binopCode;
    }

    @Override
    public ST visitWhile(CoolWhile cwhile) {
        ST whileCode = codeTemplates.getInstanceOf("while");
        whileCode.add("cond_code", visitExpr(cwhile.cond));
        whileCode.add("body_code", visitExpr(cwhile.body));
        whileCode.add("branchIndex", branchIndex++);
        return whileCode;
    }

    @Override
    public ST visitCase(CoolCase ccase) {
        ST caseCode = codeTemplates.getInstanceOf("case");
        caseCode.add("obj_code", visitExpr(ccase.expr));
        caseCode.add("temp", tempLoc += 4);
        caseCode.add("caseIndex", caseIndex);

        List<ST> branchesCode = new ArrayList<>();
        List<CoolCaseBranch> branches = ccase.branches.stream().sorted((b1, b2) ->
            b2.getSymbol().getClassType().getOrder() - b1.getSymbol().getClassType().getOrder())
            .collect(Collectors.toList()); 
        for (int i = 0; i < branches.size(); ++i) {
            ST branchCode = visitCaseBranch(branches.get(i));
            branchCode.add("i0", i);
            branchCode.add("i1", i + 1);
            branchesCode.add(branchCode);
        }
        ST noCaseBranch = codeTemplates.getInstanceOf("noCaseBranch");
        noCaseBranch.add("objtemp", tempLoc);
        noCaseBranch.add("caseIndex", caseIndex);
        noCaseBranch.add("i0", branches.size());
        branchesCode.add(noCaseBranch);

        tempLoc -= 4;
        caseIndex += 1;
        caseCode.add("cases_code", branchesCode);
        caseCode.add("fileNameLabel", stringLabels.get(SymbolTable.getFileName(ccase.getContext())));
        caseCode.add("lineNo", ccase.getToken().getLine());
        return caseCode;
    }

    public ST visitCaseBranch(CoolCaseBranch branch) {
        ST branchCode = codeTemplates.getInstanceOf("caseBranch");
        int inftag = branch.getSymbol().getClassType().getOrder();
        int suptag = inftag + branch.getSymbol().getClassType().children.size();
        branch.getSymbol().setIndex(tempLoc);  // all case variables are refs to the object
        branchCode.add("inftag", inftag);
        branchCode.add("suptag", suptag);
        branchCode.add("case_code", visitExpr(branch.body));
        branchCode.add("caseIndex", caseIndex);
        return branchCode;
    }

    @Override
    public ST visitVar(CoolVar variable) {
        boolean isSelf = variable.getSymbol().getName().equals("");
        ST readCode = codeTemplates.getInstanceOf("read" + (isSelf ? "Self" : ""));

        if (!isSelf) {
            VarSymbol varSymbol = (VarSymbol)variable.getSymbol();
            readCode.add("store", varSymbol.store);
            readCode.add("loc", getVarLocation(varSymbol));
        }
        return readCode;
    }

    @Override
    public ST visitLiteral(CoolLiteral lit) {
        ST litCode = codeTemplates.getInstanceOf("literal");
        if (lit.getToken().getType() == CoolLexer.INTEGER) {
            litCode.add("value", "int_const" + intLabels.get(lit.getToken().getText()));
        } else if (lit.getToken().getType() == CoolLexer.STRING) {
            litCode.add("value", "str_const" + stringLabels.get(lit.getToken().getText()));
        } else if (lit.getToken().getType() == CoolLexer.TRUE) {
            litCode.add("value", "bool_const1");
        } else if (lit.getToken().getType() == CoolLexer.FALSE) {
            litCode.add("value", "bool_const0");
        } 
        return litCode;
    }
}
