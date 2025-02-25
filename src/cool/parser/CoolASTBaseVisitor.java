package cool.parser;

import cool.parser.CoolASTNode.*;

// A base implementation for the AST visitor that visits nothing recursively (recursion is opt-in)
public class CoolASTBaseVisitor<T> implements CoolASTVisitor<T> {
    public T visit(CoolASTNode node) {
        return node.accept(this);
    };

    public T visitProgram(CoolProgram program) {
        return null;
    };

    public T visitClass(CoolClass cclass) {
        return null;
    };

    public T visitAttribute(CoolAttribute attr) {
        return null;
    };

    public T visitMethod(CoolMethod method) {
        return null;
    };

    public T visitFormalArg(CoolFormalArg arg) {
        return null;
    };

    public T visitExpr(CoolExpr expr) {
        return null;
    };

    public T visitVar(CoolVar variable) {
        return null;
    };

    public T visitLiteral(CoolLiteral lit) {
        return null;
    };

    public T visitBinaryOperation(CoolBinaryOperation operation) {
        return null;
    };

    public T visitUnaryOperation(CoolUnaryOperation operation) {
        return null;
    };

    public T visitNewExpr(CoolNewExpr newexpr) {
        return null;
    };

    public T visitAssignment(CoolAssignment assignment) {
        return null;
    };

    public T visitExplicitDispatch(CoolExplicitDispatch dispatch) {
        return null;
    };

    public T visitImplicitDispatch(CoolImplicitDispatch dispatch) {
        return null;
    };

    public T visitIf(CoolIf cif) {
        return null;
    };

    public T visitWhile(CoolWhile cwhile) {
        return null;
    };

    public T visitLet(CoolLet let) {
        return null;
    };

    public T visitLocalBinding(CoolLocalBinding binding) {
        return null;
    };

    public T visitCase(CoolCase ccase) {
        return null;
    };

    public T visitCaseBranch(CoolCaseBranch branch) {
        return null;
    };

    public T visitBlock(CoolBlock block) {
        return null;
    };
}
