package cool.codegen;

import cool.parser.CoolASTBaseVisitor;
import cool.parser.CoolASTNode.*;

public class CoolTempLocationsVisitor extends CoolASTBaseVisitor<Integer> {
    @Override
    public Integer visitClass(CoolClass cclass) {
        return cclass.attributes.stream().mapToInt(this::visitAttribute).max().orElse(0);
    }

    @Override
    public Integer visitAttribute(CoolAttribute attr) {
        if (attr.init == null) return 0;
        return visitExpr(attr.init);
    }

    @Override
    public Integer visitMethod(CoolMethod method) {
        return visitExpr(method.body);
    }

    @Override
    public Integer visitExpr(CoolExpr expr) {
        return expr.accept(this);
    }

    @Override
    public Integer visitExplicitDispatch(CoolExplicitDispatch dispatch) {
        return dispatch.params.stream().mapToInt(this::visitExpr).max().orElse(0);
    }

    @Override
    public Integer visitImplicitDispatch(CoolImplicitDispatch dispatch) {
        return visitExplicitDispatch(dispatch);
    }

    @Override
    public Integer visitBlock(CoolBlock block) {
        return block.exprs.stream().mapToInt(this::visitExpr).max().orElse(0);
    }

    @Override
    public Integer visitAssignment(CoolAssignment assignment) {
        return visitExpr(assignment.value);
    }

    @Override
    public Integer visitLet(CoolLet let) {
        return Math.max(let.bindings.size() + visitExpr(let.body), 
            let.bindings.stream().mapToInt(this::visitLocalBinding).max().orElse(0));
    }

    @Override
    public Integer visitLocalBinding(CoolLocalBinding binding) {
        if (binding.init == null) return 0;
        return visitExpr(binding.init);
    }

    @Override
    public Integer visitBinaryOperation(CoolBinaryOperation operation) {
        return Math.max(visitExpr(operation.lhs), 1 + visitExpr(operation.rhs));
    }

    @Override
    public Integer visitUnaryOperation(CoolUnaryOperation operation) {
        return visitExpr(operation.operand);
    }

    @Override
    public Integer visitNewExpr(CoolNewExpr newexpr) {
        return newexpr.type.equals("SELF_TYPE") ? 1 : 0;
    }

    @Override
    public Integer visitIf(CoolIf cif) {
        return Math.max(
            Math.max(visitExpr(cif.cond), visitExpr(cif.thenBranch)),
            visit(cif.elseBranch)
        );
    }

    @Override
    public Integer visitWhile(CoolWhile cwhile) {
        return Math.max(visitExpr(cwhile.cond), visitExpr(cwhile.body));
    }

    @Override
    public Integer visitCase(CoolCase ccase) {
        return Math.max(
            visitExpr(ccase.expr),
            1 + ccase.branches.stream().mapToInt(this::visitCaseBranch).max().orElse(0)
        );
    }

    @Override
    public Integer visitCaseBranch(CoolCaseBranch branch) {
        return visitExpr(branch.body);
    }

    @Override
    public Integer visitLiteral(CoolLiteral _lit) {
        return 0;
    }

    @Override
    public Integer visitVar(CoolVar _variable) {
        return 0;
    }
}
