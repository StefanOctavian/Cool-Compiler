// Generated from d:/Stefan/Facultate/Anul IV/CPL/teme/tema3/src/cool/parser/CoolParser.g4 by ANTLR 4.13.1

    package cool.parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CoolParser}.
 */
public interface CoolParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CoolParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(CoolParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link CoolParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(CoolParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link CoolParser#class}.
	 * @param ctx the parse tree
	 */
	void enterClass(CoolParser.ClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link CoolParser#class}.
	 * @param ctx the parse tree
	 */
	void exitClass(CoolParser.ClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link CoolParser#vardef}.
	 * @param ctx the parse tree
	 */
	void enterVardef(CoolParser.VardefContext ctx);
	/**
	 * Exit a parse tree produced by {@link CoolParser#vardef}.
	 * @param ctx the parse tree
	 */
	void exitVardef(CoolParser.VardefContext ctx);
	/**
	 * Enter a parse tree produced by {@link CoolParser#method}.
	 * @param ctx the parse tree
	 */
	void enterMethod(CoolParser.MethodContext ctx);
	/**
	 * Exit a parse tree produced by {@link CoolParser#method}.
	 * @param ctx the parse tree
	 */
	void exitMethod(CoolParser.MethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link CoolParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(CoolParser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link CoolParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(CoolParser.AttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link CoolParser#formal}.
	 * @param ctx the parse tree
	 */
	void enterFormal(CoolParser.FormalContext ctx);
	/**
	 * Exit a parse tree produced by {@link CoolParser#formal}.
	 * @param ctx the parse tree
	 */
	void exitFormal(CoolParser.FormalContext ctx);
	/**
	 * Enter a parse tree produced by {@link CoolParser#call}.
	 * @param ctx the parse tree
	 */
	void enterCall(CoolParser.CallContext ctx);
	/**
	 * Exit a parse tree produced by {@link CoolParser#call}.
	 * @param ctx the parse tree
	 */
	void exitCall(CoolParser.CallContext ctx);
	/**
	 * Enter a parse tree produced by {@link CoolParser#caseBranch}.
	 * @param ctx the parse tree
	 */
	void enterCaseBranch(CoolParser.CaseBranchContext ctx);
	/**
	 * Exit a parse tree produced by {@link CoolParser#caseBranch}.
	 * @param ctx the parse tree
	 */
	void exitCaseBranch(CoolParser.CaseBranchContext ctx);
	/**
	 * Enter a parse tree produced by {@link CoolParser#var}.
	 * @param ctx the parse tree
	 */
	void enterVar(CoolParser.VarContext ctx);
	/**
	 * Exit a parse tree produced by {@link CoolParser#var}.
	 * @param ctx the parse tree
	 */
	void exitVar(CoolParser.VarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code newExpr}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNewExpr(CoolParser.NewExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code newExpr}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNewExpr(CoolParser.NewExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryOp}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBinaryOp(CoolParser.BinaryOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryOp}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBinaryOp(CoolParser.BinaryOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprvar}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExprvar(CoolParser.ExprvarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprvar}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExprvar(CoolParser.ExprvarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code implDispatch}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterImplDispatch(CoolParser.ImplDispatchContext ctx);
	/**
	 * Exit a parse tree produced by the {@code implDispatch}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitImplDispatch(CoolParser.ImplDispatchContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryOp}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOp(CoolParser.UnaryOpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryOp}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOp(CoolParser.UnaryOpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code precedence}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPrecedence(CoolParser.PrecedenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code precedence}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPrecedence(CoolParser.PrecedenceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code literal}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(CoolParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code literal}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(CoolParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assigment}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAssigment(CoolParser.AssigmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assigment}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAssigment(CoolParser.AssigmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code loop}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLoop(CoolParser.LoopContext ctx);
	/**
	 * Exit a parse tree produced by the {@code loop}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLoop(CoolParser.LoopContext ctx);
	/**
	 * Enter a parse tree produced by the {@code explDispatch}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExplDispatch(CoolParser.ExplDispatchContext ctx);
	/**
	 * Exit a parse tree produced by the {@code explDispatch}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExplDispatch(CoolParser.ExplDispatchContext ctx);
	/**
	 * Enter a parse tree produced by the {@code block}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBlock(CoolParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by the {@code block}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBlock(CoolParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code let}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLet(CoolParser.LetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code let}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLet(CoolParser.LetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code if}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIf(CoolParser.IfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code if}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIf(CoolParser.IfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code case}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCase(CoolParser.CaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code case}
	 * labeled alternative in {@link CoolParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCase(CoolParser.CaseContext ctx);
}