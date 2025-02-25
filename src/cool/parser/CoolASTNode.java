package cool.parser;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import cool.parser.CoolParser.*;
import cool.structures.Scope;
import cool.structures.Scope.ClassScope;
import cool.structures.Symbol.ArgSymbol;
import cool.structures.Symbol.AttrSymbol;
import cool.structures.Symbol.ClassSymbol;
import cool.structures.Symbol.IdSymbol;
import cool.structures.Symbol.LocalSymbol;
import cool.structures.Symbol.MethodSymbol;

public abstract class CoolASTNode {
    ParserRuleContext ctx;

    CoolASTNode(ParserRuleContext ctx) {
        this.ctx = ctx;
    }

    public <T> T accept(CoolASTVisitor<T> visitor) {
        return null;
    }

    public ParserRuleContext getContext() {
        return ctx;
    }

    public static class CoolProgram extends CoolASTNode {
        private Token startToken;
        public final List<CoolClass> classes;

        public CoolProgram(ProgramContext ctx, Token start, List<CoolClass> classes) {
            super(ctx);
            this.startToken = start;
            this.classes = classes;
        }

        public Token getStart() {
            return startToken;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitProgram(this);
        }
    };

    public static class CoolClass extends CoolASTNode {
        Token nameToken;
        Token parentClassToken;
        public final String name;
        public final String parentClass;
        public final List<CoolAttribute> attributes;
        public final List<CoolMethod> methods;

        ClassSymbol symbol;
        ClassSymbol parentSymbol;

        CoolClass(ClassContext ctx, Token name, Token parentClass, 
                  List<CoolAttribute> attributes, List<CoolMethod> methods) {
            super(ctx);
            this.parentClassToken = parentClass;
            this.nameToken = name;
            this.name = name.getText();
            this.parentClass = parentClass != null ? parentClass.getText() : null;
            this.attributes = attributes;
            this.methods = methods;
        }

        public Token getNameToken() {
            return nameToken;
        }

        public Token getParentClassToken() {
            return parentClassToken;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitClass(this);
        }

        public ClassSymbol getSymbol() {
            return symbol;
        }

        public void setSymbol(ClassSymbol symbol) {
            this.symbol = symbol;
        }

        public ClassSymbol getParentSymbol() {
            return parentSymbol;
        }

        public void setParentSymbol(ClassSymbol parentSymbol) {
            this.parentSymbol = parentSymbol;
        }
    }

    public static class CoolAttribute extends CoolASTNode {
        Token nameToken;
        Token typeToken;
        public final String name;
        public final String type;
        public final CoolExpr init;
        AttrSymbol symbol;
        ClassScope scope;

        CoolAttribute(AttributeContext ctx, Token name, Token type, CoolExpr init) {
            super(ctx);
            this.nameToken = name;
            this.name = name.getText();
            this.typeToken = type;
            this.type = type.getText();
            this.init = init;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitAttribute(this);
        }

        public Token getNameToken() {
            return nameToken;
        }

        public Token getTypeToken() {
            return typeToken;
        }

        public ClassScope getScope() {
            return scope;
        }

        public void setScope(ClassScope scope) {
            this.scope = scope;
        }

        public AttrSymbol getSymbol() {
            return symbol;
        }

        public void setSymbol(AttrSymbol symbol) {
            this.symbol = symbol;
        }
    }

    public static class CoolMethod extends CoolASTNode {
        Token nameToken;
        Token typeToken;
        public final String name;
        public final List<CoolFormalArg> formalArgs;
        public final String type;
        public final CoolExpr body;
        MethodSymbol symbol;
        ClassScope scope;

        CoolMethod(MethodContext ctx, Token name, List<CoolFormalArg> args, Token type, 
                   CoolExpr body) {
            super(ctx);
            this.nameToken = name;
            this.name = name.getText();
            this.formalArgs = args;
            this.typeToken = type;
            this.type = type.getText();
            this.body = body;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitMethod(this);
        }

        public Token getNameToken() {
            return nameToken;
        }

        public Token getTypeToken() {
            return typeToken;
        }

        public ClassScope getScope() {
            return scope;
        }

        public void setScope(ClassScope scope) {
            this.scope = scope;
        }

        public MethodSymbol getSymbol() {
            return symbol;
        }

        public void setSymbol(MethodSymbol symbol) {
            this.symbol = symbol;
        }
    }

    public static class CoolFormalArg extends CoolASTNode {
        private Token nameToken;
        private Token typeToken;
        public final String name;
        public final String type;
        ArgSymbol symbol;

        CoolFormalArg(FormalContext ctx, Token name, Token type) {
            super(ctx);
            this.nameToken = name;
            this.typeToken = type;
            this.name = name.getText();
            this.type = type.getText();
        }

        public Token getNameToken() {
            return nameToken;
        }

        public Token getTypeToken() {
            return typeToken;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitFormalArg(this);
        }

        public ArgSymbol getSymbol() {
            return symbol;
        }

        public void setSymbol(ArgSymbol symbol) {
            this.symbol = symbol;
        }
    }

    public static class CoolExpr extends CoolASTNode {
        CoolExpr(ExprContext ctx) {
            super(ctx);
        }
        // apart from a variable as an expression, a variable can be the lhs of an assignment
        CoolExpr(VarContext ctx) {
            super(ctx);
        }

        public Token getToken() {
            return ctx.getStart();
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitExpr(this);
        }
    }

    public static class CoolVar extends CoolExpr {
        public final String name;
        ClassScope scope;
        IdSymbol symbol;

        CoolVar(VarContext ctx, String name) {
            super(ctx);
            this.name = name;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitVar(this);
        }

        public ClassScope getScope() {
            return scope;
        }

        public void setScope(ClassScope scope) {
            this.scope = scope;
        }

        public IdSymbol getSymbol() {
            return symbol;
        }

        public void setSymbol(IdSymbol symbol) {
            this.symbol = symbol;
        }
    }

    public static class CoolLiteral extends CoolExpr {
        public final String value;

        CoolLiteral(LiteralContext ctx, String value) {
            super(ctx);
            this.value = value;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitLiteral(this);
        }
    }

    public static class CoolBinaryOperation extends CoolExpr {
        private Token opToken;
        public final String op;
        public final CoolExpr lhs;
        public final CoolExpr rhs;

        CoolBinaryOperation(BinaryOpContext ctx, Token op, CoolExpr lhs, CoolExpr rhs) {
            super(ctx);
            this.opToken = op;
            this.op = op.getText();
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public Token getOpToken() {
            return opToken;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitBinaryOperation(this);
        }
    }

    public static class CoolUnaryOperation extends CoolExpr {
        public final String op;
        public final CoolExpr operand;

        CoolUnaryOperation(UnaryOpContext ctx, String op, CoolExpr operand) {
            super(ctx);
            this.op = op;
            this.operand = operand;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitUnaryOperation(this);
        }
    }

    public static class CoolNewExpr extends CoolExpr {
        private Token typeToken;
        public final String type;
        Scope scope;

        CoolNewExpr(NewExprContext ctx, Token type) {
            super(ctx);
            this.typeToken = type;
            this.type = type.getText();
        }

        public Token getTypeToken() {
            return typeToken;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitNewExpr(this);
        }

        public Scope getScope() {
            return scope;
        }

        public void setScope(Scope scope) {
            this.scope = scope;
        }
    }

    public static class CoolAssignment extends CoolExpr {
        public final CoolVar variable;
        public final CoolExpr value;

        CoolAssignment(AssigmentContext ctx, CoolVar variable, CoolExpr value) {
            super(ctx);
            this.variable = variable;
            this.value = value;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitAssignment(this);
        }
    }

    public static class CoolExplicitDispatch extends CoolExpr {
        private Token staticClassToken;
        private Token methodToken;
        public final CoolExpr object;
        public final String staticClass;
        public final String method;
        public final List<CoolExpr> params;
        private Scope scope;
        private MethodSymbol symbol;

        CoolExplicitDispatch(ExplDispatchContext ctx, CoolExpr object, 
                             Token staticClass, Token method, List<CoolExpr> params) {
            super(ctx);
            this.staticClassToken = staticClass;
            this.methodToken = method;
            this.object = object;
            this.staticClass = (staticClass != null) ? staticClass.getText() : null;
            this.method = method.getText();
            this.params = params;
        }

        CoolExplicitDispatch(ImplDispatchContext ctx, CoolExpr object, 
                             Token staticClass, Token method, List<CoolExpr> params) {
            super(ctx);
            this.staticClassToken = staticClass;
            this.methodToken = method;
            this.object = object;
            this.staticClass = (staticClass != null) ? staticClass.getText() : null;
            this.method = method.getText();
            this.params = params;
        }

        public Token getStaticClassToken() {
            return staticClassToken;
        }

        public Token getMethodToken() {
            return methodToken;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitExplicitDispatch(this);
        }

        public Scope getScope() {
            return scope;
        }

        public void setScope(Scope scope) {
            this.scope = scope;
        }

        public MethodSymbol getSymbol() {
            return symbol;
        }

        public void setSymbol(MethodSymbol symbol) {
            this.symbol = symbol;
        }
    }

    public static class CoolImplicitDispatch extends CoolExplicitDispatch {
        CoolImplicitDispatch(ImplDispatchContext ctx, Token method, List<CoolExpr> params) {
            super(ctx, null, null, method, params);
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitImplicitDispatch(this);
        }
    }

    public static class CoolIf extends CoolExpr {
        public final CoolExpr cond;
        public final CoolExpr thenBranch;
        public final CoolExpr elseBranch;

        CoolIf(IfContext ctx, CoolExpr cond, CoolExpr thenBranch, CoolExpr elseBranch) {
            super(ctx);
            this.cond = cond;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitIf(this);
        }
    }

    public static class CoolWhile extends CoolExpr {
        public final CoolExpr cond;
        public final CoolExpr body;

        CoolWhile(LoopContext ctx, CoolExpr cond, CoolExpr body) {
            super(ctx);
            this.cond = cond;
            this.body = body;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitWhile(this);
        }
    }

    public static class CoolLet extends CoolExpr {
        public final List<CoolLocalBinding> bindings;
        public final CoolExpr body;

        CoolLet(LetContext ctx, List<CoolLocalBinding> bindings, CoolExpr body) {
            super(ctx);
            this.bindings = bindings;
            this.body = body;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitLet(this);
        }
    }

    public static class CoolLocalBinding extends CoolASTNode {
        private Token typeToken;
        public final String name;
        public final String type;
        public final CoolExpr init;
        LocalSymbol symbol;
        Scope scope;

        CoolLocalBinding(VardefContext ctx, Token name, Token type, CoolExpr init) {
            super(ctx);
            this.typeToken = type;
            this.name = name.getText();
            this.type = type.getText();
            this.init = init;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitLocalBinding(this);
        }

        public Token getTypeToken() {
            return typeToken;
        }

        public Token getNameToken() {
            return ctx.getStart();
        }

        public LocalSymbol getSymbol() {
            return symbol;
        }

        public void setSymbol(LocalSymbol symbol) {
            this.symbol = symbol;
        }

        public Scope getScope() {
            return scope;
        }

        public void setScope(Scope scope) {
            this.scope = scope;
        }
    }

    public static class CoolCase extends CoolExpr {
        public final CoolExpr expr;
        public final List<CoolCaseBranch> branches;
        
        CoolCase(CaseContext ctx, CoolExpr expr, List<CoolCaseBranch> branches) {
            super(ctx);
            this.expr = expr;
            this.branches = branches;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitCase(this);
        }
    }

    public static class CoolCaseBranch extends CoolASTNode {
        private Token nameToken;
        private Token typeToken;
        public final String name;
        public final String type;
        public final CoolExpr body;
        LocalSymbol symbol;

        CoolCaseBranch(CaseBranchContext ctx, Token name, Token type, CoolExpr body) {
            super(ctx);
            this.nameToken = name;
            this.typeToken = type;
            this.name = name.getText();
            this.type = type.getText();
            this.body = body;
        }

        public Token getNameToken() {
            return nameToken;
        }

        public Token getTypeToken() {
            return typeToken;
        }

        public LocalSymbol getSymbol() {
            return symbol;
        }

        public void setSymbol(LocalSymbol symbol) {
            this.symbol = symbol;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitCaseBranch(this);
        }
    }

    public static class CoolBlock extends CoolExpr {
        public final List<CoolExpr> exprs;

        CoolBlock(BlockContext ctx, List<CoolExpr> exprs) {
            super(ctx);
            this.exprs = exprs;
        }

        public <T> T accept(CoolASTVisitor<T> visitor) {
            return visitor.visitBlock(this);
        }
    }
}