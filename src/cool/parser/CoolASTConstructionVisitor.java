package cool.parser;

import java.util.List;

import org.antlr.v4.runtime.Token;

import java.util.ArrayList;

import cool.parser.CoolParser.*;
import cool.parser.CoolASTNode.*;

public class CoolASTConstructionVisitor extends CoolParserBaseVisitor<CoolASTNode> {
    @Override
    public CoolASTNode visitProgram(ProgramContext ctx) {
        List<CoolClass> classes = new ArrayList<>();

        // System.out.println("Classes: " + ctx.children.stream().map(cl -> cl.name).toList());
        for (var cclass : ctx.classes) {
            classes.add((CoolClass)visit(cclass));
        }
        return new CoolProgram(ctx, ctx.getStart(), classes);
    }

    @Override
    public CoolASTNode visitClass(ClassContext ctx) {
        List<CoolAttribute> attributes = new ArrayList<>();
        List<CoolMethod> methods = new ArrayList<>();

        for (var attr : ctx.attributes) {
            CoolAttribute attribute = (CoolAttribute)visit(attr);
            attributes.add(attribute);
        }
        for (var method : ctx.methods) {
            methods.add((CoolMethod)visit(method));
        }
        return new CoolClass(ctx, ctx.name, ctx.base, attributes, methods);
    }

    @Override
    public CoolASTNode visitAttribute(AttributeContext ctx) {
        ExprContext exprCtx = ctx.vardef().expr();
        CoolExpr init = exprCtx != null ? (CoolExpr)visit(exprCtx) : null;
        return new CoolAttribute(ctx, ctx.getStart(), ctx.vardef().TYPE().getSymbol(), init);
    }

    @Override
    public CoolASTNode visitMethod(MethodContext ctx) {
        List<CoolFormalArg> args = new ArrayList<>();
        CoolExpr body = (CoolExpr)visit(ctx.expr());

        for (var arg : ctx.formal()) {
            args.add((CoolFormalArg)visit(arg));
        }
        return new CoolMethod(ctx, ctx.getStart(), args, ctx.TYPE().getSymbol(), body);
    }

    @Override
    public CoolASTNode visitFormal(FormalContext ctx) {
        return new CoolFormalArg(ctx, ctx.getStart(), ctx.TYPE().getSymbol());
    }

    @Override
    public CoolASTNode visitVar(VarContext ctx) {
        return new CoolVar(ctx, ctx.ID().getText());
    }

    @Override
    public CoolASTNode visitLiteral(LiteralContext ctx) {
        return new CoolLiteral(ctx, ctx.getText());
    }

    @Override
    public CoolASTNode visitBinaryOp(BinaryOpContext ctx) {
        CoolExpr lhs = (CoolExpr)visit(ctx.expr(0));
        CoolExpr rhs = (CoolExpr)visit(ctx.expr(1));
        return new CoolBinaryOperation(ctx, ctx.op, lhs, rhs);
    }

    @Override
    public CoolASTNode visitUnaryOp(UnaryOpContext ctx) {
        CoolExpr operand = (CoolExpr)visit(ctx.expr());
        return new CoolUnaryOperation(ctx, ctx.op.getText(), operand);
    }

    @Override
    public CoolASTNode visitNewExpr(NewExprContext ctx) {
        return new CoolNewExpr(ctx, ctx.TYPE().getSymbol());
    }

    @Override
    public CoolASTNode visitAssigment(AssigmentContext ctx) {
        CoolVar variable = (CoolVar)visit(ctx.var());
        CoolExpr value = (CoolExpr)visit(ctx.expr());
        return new CoolAssignment(ctx, variable, value);
    }

    private List<CoolExpr> paramList(CallContext ctx) {
        List<CoolExpr> params = new ArrayList<>();

        for (var param : ctx.expr()) {
            params.add((CoolExpr)visit(param));
        }
        return params;
    }

    @Override
    public CoolASTNode visitExplDispatch(ExplDispatchContext ctx) {
        Token staticType = ctx.TYPE() != null ? ctx.TYPE().getSymbol() : null;
        CoolExpr object = (CoolExpr)visit(ctx.expr());

        return new CoolExplicitDispatch(
            ctx, object, staticType, 
            ctx.call().ID().getSymbol(), paramList(ctx.call())
        );
    }

    @Override
    public CoolASTNode visitImplDispatch(ImplDispatchContext ctx) {
        return new CoolImplicitDispatch(ctx, ctx.getStart(), paramList(ctx.call()));
    }

    @Override
    public CoolASTNode visitIf(IfContext ctx) {
        CoolExpr cond = (CoolExpr)visit(ctx.cond);
        CoolExpr thenBranch = (CoolExpr)visit(ctx.thenBranch);
        CoolExpr elseBranch = (CoolExpr)visit(ctx.elseBranch);
        return new CoolIf(ctx, cond, thenBranch, elseBranch);
    }

    @Override
    public CoolASTNode visitLoop(LoopContext ctx) {
        CoolExpr cond = (CoolExpr)visit(ctx.cond);
        CoolExpr body = (CoolExpr)visit(ctx.body);
        return new CoolWhile(ctx, cond, body);
    }

    @Override
    public CoolASTNode visitLet(LetContext ctx) {
        List<CoolLocalBinding> bindings = new ArrayList<>();
        CoolExpr body = (CoolExpr)visit(ctx.expr());

        for (var local : ctx.vardef()) {
            bindings.add((CoolLocalBinding)visit(local));
        }

        return new CoolLet(ctx, bindings, body);
    }

    @Override
    public CoolASTNode visitVardef(VardefContext ctx) {
        ExprContext valueCtx = ctx.expr();
        CoolExpr value = null;
        if (valueCtx != null)
            value = (CoolExpr)visit(ctx.expr());
        return new CoolLocalBinding(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(), value);
    }

    @Override
    public CoolASTNode visitCase(CaseContext ctx) {
        CoolExpr expr = (CoolExpr)visit(ctx.expr());
        List<CoolCaseBranch> branches = new ArrayList<>();

        for (var branch : ctx.caseBranch()) {
            branches.add((CoolCaseBranch)visit(branch));
        }

        return new CoolCase(ctx, expr, branches);
    }

    @Override
    public CoolASTNode visitCaseBranch(CaseBranchContext ctx) {
        CoolExpr body = (CoolExpr)visit(ctx.expr());
        return new CoolCaseBranch(ctx, ctx.ID().getSymbol(), ctx.TYPE().getSymbol(), body);
    }

    @Override
    public CoolASTNode visitBlock(BlockContext ctx) {
        List<CoolExpr> exprs = new ArrayList<>();

        for (var expr : ctx.expr()) {
            exprs.add((CoolExpr)visit(expr));
        }
        return new CoolBlock(ctx, exprs);
    }

    @Override
    public CoolASTNode visitPrecedence(PrecedenceContext ctx) {
        return visit(ctx.expr());
    }
}
