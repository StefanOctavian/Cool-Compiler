// Generated from d:/Stefan/Facultate/Anul IV/CPL/teme/tema3/src/cool/parser/CoolParser.g4 by ANTLR 4.13.1

    package cool.parser;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CoolParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CoolParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CoolParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(CoolParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#class}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass(CoolParser.ClassContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#vardef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVardef(CoolParser.VardefContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#method}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethod(CoolParser.MethodContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute(CoolParser.AttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#formal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormal(CoolParser.FormalContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall(CoolParser.CallContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#caseBranch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseBranch(CoolParser.CaseBranchContext ctx);
	/**
	 * Visit a parse tree produced by {@link CoolParser#var}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar(CoolParser.VarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code newExpr}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewExpr(CoolParser.NewExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binaryOp}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryOp(CoolParser.BinaryOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code exprvar}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprvar(CoolParser.ExprvarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code implDispatch}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplDispatch(CoolParser.ImplDispatchContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryOp}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOp(CoolParser.UnaryOpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code precedence}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrecedence(CoolParser.PrecedenceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code literal}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(CoolParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assigment}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssigment(CoolParser.AssigmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code loop}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoop(CoolParser.LoopContext ctx);
	/**
	 * Visit a parse tree produced by the {@code explDispatch}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplDispatch(CoolParser.ExplDispatchContext ctx);
	/**
	 * Visit a parse tree produced by the {@code block}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(CoolParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code let}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLet(CoolParser.LetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code if}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf(CoolParser.IfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code case}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCase(CoolParser.CaseContext ctx);
}