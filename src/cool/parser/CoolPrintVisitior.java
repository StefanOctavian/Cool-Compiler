package cool.parser;

public class CoolPrintVisitior extends CoolASTBaseVisitor<String> {
    String indent = "";

    private void incIndent() {
        indent += "  ";
    }

    private void decIndent() {
        indent = indent.substring(2);
    }

    class IndentBuilder implements AutoCloseable {
        private StringBuilder builder;

        public IndentBuilder(String str) {
            builder = new StringBuilder(indent);
            builder.append(str);
            incIndent();
        }
        public IndentBuilder() { this(""); }

        public void append(String str, boolean newline) {
            builder.append(str + (newline ? "\n" : ""));
        }
        public void append(String str) { this.append(str, false); }

        public void appendIndent(String str, boolean newline) {
            builder.append(indent);
            this.append(str, newline);
        }
        public void appendIndent(String str) { this.appendIndent(str, true); }

        public String toString() { return builder.toString(); }

        public void close() {
            decIndent();
        }
    };

    @Override
    public String visitProgram(CoolASTNode.CoolProgram program) {
        try (var builder = new IndentBuilder("program\n")) {
            for (var cclass : program.classes)
                builder.append(visitClass(cclass));
            return builder.toString();
        }
    }

    @Override
    public String visitClass(CoolASTNode.CoolClass cclass) {
        try (var builder = new IndentBuilder("class\n")) {
            builder.appendIndent(cclass.name);
            if (cclass.parentClass != null)
                builder.appendIndent(cclass.parentClass);

            for (var attr : cclass.attributes)
                builder.append(visitAttribute(attr));
            for (var method : cclass.methods)
                builder.append(visitMethod(method));

            return builder.toString();
        }
    }

    @Override
    public String visitAttribute(CoolASTNode.CoolAttribute attr) {
        try (var builder = new IndentBuilder("attribute\n")) {
            builder.appendIndent(attr.name);
            builder.appendIndent(attr.type);
            if (attr.init != null)
                builder.append(visitExpr(attr.init));
            return builder.toString();
        }
    }

    @Override
    public String visitMethod(CoolASTNode.CoolMethod method) {
        try (var builder = new IndentBuilder("method\n")) {
            builder.appendIndent(method.name);
            for (var arg : method.formalArgs)
                builder.append(visitFormalArg(arg));
            builder.appendIndent(method.type);
            builder.append(visitExpr(method.body));
            return builder.toString();
        }
    }

    @Override
    public String visitFormalArg(CoolASTNode.CoolFormalArg arg) {
        try (var builder = new IndentBuilder("formal\n")) {
            builder.appendIndent(arg.name);
            builder.appendIndent(arg.type);
            return builder.toString();
        }
    }

    @Override
    public String visitExpr(CoolASTNode.CoolExpr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitVar(CoolASTNode.CoolVar variable) {
        return indent + variable.name + "\n";
    }

    @Override
    public String visitLiteral(CoolASTNode.CoolLiteral lit) {
        return indent + lit.value + "\n";
    }

    @Override
    public String visitBinaryOperation(CoolASTNode.CoolBinaryOperation operation) {
        try (var builder = new IndentBuilder(operation.op + "\n")) {
            builder.append(visitExpr(operation.lhs));
            builder.append(visitExpr(operation.rhs));
            return builder.toString();
        }
    }

    @Override
    public String visitUnaryOperation(CoolASTNode.CoolUnaryOperation operation) {
        try (var builder = new IndentBuilder(operation.op + "\n")) {
            builder.append(visitExpr(operation.operand));
            return builder.toString();
        }
    }

    @Override
    public String visitNewExpr(CoolASTNode.CoolNewExpr newexpr) {
        try (var builder = new IndentBuilder("new\n")) {
            builder.appendIndent(newexpr.type);
            return builder.toString();
        }
    }

    @Override
    public String visitAssignment(CoolASTNode.CoolAssignment assignment) {
        try (var builder = new IndentBuilder("<-\n")) {
            builder.append(visitVar(assignment.variable));
            builder.append(visitExpr(assignment.value));
            return builder.toString();
        }
    }

    @Override
    public String visitExplicitDispatch(CoolASTNode.CoolExplicitDispatch dispatch) {
        try (var builder = new IndentBuilder(".\n")) {
            builder.append(visitExpr(dispatch.object));
            if (dispatch.staticClass != null)
                builder.appendIndent(dispatch.staticClass);
            builder.appendIndent(dispatch.method);
            for (var param : dispatch.params)
                builder.append(visitExpr(param));
            return builder.toString();
        }
    }

    @Override
    public String visitImplicitDispatch(CoolASTNode.CoolImplicitDispatch dispatch) {
        try (var builder = new IndentBuilder("implicit dispatch\n")) {
            builder.appendIndent(dispatch.method);
            for (var param : dispatch.params)
                builder.append(visitExpr(param));
            return builder.toString();
        }
    }

    @Override
    public String visitIf(CoolASTNode.CoolIf cif) {
        try (var builder = new IndentBuilder("if\n")) {
            builder.append(visitExpr(cif.cond));
            builder.append(visitExpr(cif.thenBranch));
            builder.append(visitExpr(cif.elseBranch));
            return builder.toString();
        }
    }

    @Override
    public String visitWhile(CoolASTNode.CoolWhile cwhile) {
        try (var builder = new IndentBuilder("while\n")) {
            builder.append(visitExpr(cwhile.cond));
            builder.append(visitExpr(cwhile.body));
            return builder.toString();
        }
    }

    @Override
    public String visitLet(CoolASTNode.CoolLet let) {
        try (var builder = new IndentBuilder("let\n")) {
            for (var binding : let.bindings)
                builder.append(visitLocalBinding(binding));
            builder.append(visitExpr(let.body));
            return builder.toString();
        }
    }

    @Override
    public String visitLocalBinding(CoolASTNode.CoolLocalBinding binding) {
        try (var builder = new IndentBuilder("local\n")) {
            builder.appendIndent(binding.name);
            builder.appendIndent(binding.type);
            if (binding.init != null)
                builder.append(visitExpr(binding.init));
            return builder.toString();
        }
    }

    @Override
    public String visitCase(CoolASTNode.CoolCase ccase) {
        try (var builder = new IndentBuilder("case\n")) {
            builder.append(visitExpr(ccase.expr));
            for (var branch : ccase.branches)
                builder.append(visitCaseBranch(branch));
            return builder.toString();
        }
    }

    @Override
    public String visitCaseBranch(CoolASTNode.CoolCaseBranch branch) {
        try (var builder = new IndentBuilder("case branch\n")) {
            builder.appendIndent(branch.name);
            builder.appendIndent(branch.type);
            builder.append(visitExpr(branch.body));
            return builder.toString();
        }
    }

    @Override
    public String visitBlock(CoolASTNode.CoolBlock block) {
        try (var builder = new IndentBuilder("block\n")) {
            for (var expr : block.exprs)
                builder.append(visitExpr(expr));
            return builder.toString();
        }
    }
}
