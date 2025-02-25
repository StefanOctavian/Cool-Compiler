package cool.structures;

import java.io.File;

import org.antlr.v4.runtime.*;

import cool.compiler.Compiler;
import cool.parser.CoolParser;
import cool.structures.Symbol.ArgSymbol;
import cool.structures.Symbol.ClassSymbol;
import cool.structures.Symbol.MethodSymbol;

public class SymbolTable {
    public static Scope globals;
    public static ClassSymbol Int;
    public static ClassSymbol Bool;
    public static ClassSymbol String;
    public static ClassSymbol Object;
    public static ClassSymbol IO;

    private static boolean semanticErrors;

    public static void defineBasicClasses() {
        globals = new Scope(null);
        semanticErrors = false;

        Int = new ClassSymbol("Int");
        String = new ClassSymbol("String");
        Bool = new ClassSymbol("Bool");
        Object = new ClassSymbol("Object");
        IO = new ClassSymbol("IO");
        defineStringMethods();
        defineObjectMethods();
        defineIOMethods();

        globals.add(Object);
        Object.getScope().setParent(globals);
        for (var cls : new ClassSymbol[] { Int, String, Bool, IO }) {
            globals.add(cls);
            cls.getScope().setParent(Object.getScope());
        }
    }

    private static void defineStringMethods() {
        MethodSymbol length = new MethodSymbol("length", String.getScope()).setType(Int);
        MethodSymbol concat = new MethodSymbol("concat", String.getScope())
                .addArg(new ArgSymbol("s", 0).setType(String))
                .setType(String);
        MethodSymbol substr = new MethodSymbol("substr", String.getScope())
                .addArg(new ArgSymbol("i", 0).setType(Int))
                .addArg(new ArgSymbol("l", 1).setType(Int))
                .setType(String);

        String.getScope().add(length, MethodSymbol.namespace);
        String.getScope().add(concat, MethodSymbol.namespace);
        String.getScope().add(substr, MethodSymbol.namespace);
    }

    private static void defineObjectMethods() {
        MethodSymbol abort = new MethodSymbol("abort", Object.getScope()).setType(Object);
        MethodSymbol type_name = new MethodSymbol("type_name", Object.getScope()).setType(String);
        MethodSymbol copy = new MethodSymbol("copy", Object.getScope()).setSelfType();

        Object.getScope().add(abort, MethodSymbol.namespace);
        Object.getScope().add(type_name, MethodSymbol.namespace);
        Object.getScope().add(copy, MethodSymbol.namespace);
    }

    public static void defineIOMethods() {
        MethodSymbol out_string = new MethodSymbol("out_string", IO.getScope())
                .addArg(new ArgSymbol("x", 0).setType(String))
                .setSelfType();
        MethodSymbol out_int = new MethodSymbol("out_int", IO.getScope())
                .addArg(new ArgSymbol("x", 0).setType(Int))
                .setSelfType();
        MethodSymbol in_string = new MethodSymbol("in_string", IO.getScope()).setType(String);
        MethodSymbol in_int = new MethodSymbol("in_int", IO.getScope()).setType(Int);

        IO.getScope().add(out_string, MethodSymbol.namespace);
        IO.getScope().add(out_int, MethodSymbol.namespace);
        IO.getScope().add(in_string, MethodSymbol.namespace);
        IO.getScope().add(in_int, MethodSymbol.namespace);
    }

    public static String getFileName(ParserRuleContext ctx) {
        while (!(ctx.getParent() instanceof CoolParser.ProgramContext))
            ctx = ctx.getParent();

        return new File(Compiler.fileNames.get(ctx)).getName();
    }

    /**
     * Displays a semantic error message.
     * 
     * @param ctx  Used to determine the enclosing class context of this error,
     *             which knows the file name in which the class was defined.
     * @param info Used for line and column information.
     * @param str  The error message.
     */
    public static void error(ParserRuleContext ctx, Token info, String str) {
        String message = "\"" + getFileName(ctx)
                + "\", line " + info.getLine()
                + ":" + (info.getCharPositionInLine() + 1)
                + ", Semantic error: " + str;

        System.err.println(message);

        semanticErrors = true;
    }

    public static void error(String str) {
        String message = "Semantic error: " + str;

        System.err.println(message);

        semanticErrors = true;
    }

    public static boolean hasSemanticErrors() {
        return semanticErrors;
    }
}
