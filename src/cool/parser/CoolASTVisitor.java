package cool.parser;

import cool.parser.CoolASTNode.*;

public interface CoolASTVisitor<T> {
    T visit(CoolASTNode node);
    T visitProgram(CoolProgram program);
    T visitClass(CoolClass cclass);
    T visitAttribute(CoolAttribute attr);
    T visitMethod(CoolMethod method);
    T visitFormalArg(CoolFormalArg arg);
    T visitExpr(CoolExpr expr);
    T visitVar(CoolVar variable);
    T visitLiteral(CoolLiteral lit);
    T visitBinaryOperation(CoolBinaryOperation operation);
    T visitUnaryOperation(CoolUnaryOperation operation);
    T visitNewExpr(CoolNewExpr newexpr);
    T visitAssignment(CoolAssignment assignment);
    T visitExplicitDispatch(CoolExplicitDispatch dispatch);
    T visitImplicitDispatch(CoolImplicitDispatch dispatch);
    T visitIf(CoolIf cif);
    T visitWhile(CoolWhile cwhile);
    T visitLet(CoolLet let);
    T visitLocalBinding(CoolLocalBinding binding);
    T visitCase(CoolCase ccase);
    T visitCaseBranch(CoolCaseBranch branch);
    T visitBlock(CoolBlock block);
}
