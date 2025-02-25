package cool.compiler;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.stringtemplate.v4.ST;

import cool.codegen.CoolCodegenVisitor;
import cool.lexer.CoolLexer;
import cool.parser.CoolASTConstructionVisitor;
import cool.parser.CoolASTNode;
import cool.parser.CoolParser;
import cool.parser.CoolParser.ProgramContext;
import cool.structures.ClassDefinitionPassVisitor;
import cool.structures.DefinitionPassVisitor;
import cool.structures.FeatureDefinitionPassVisitor;
import cool.structures.HierarchyResolutionPassVisitor;
import cool.structures.ResolutionPassVisitor;
import cool.structures.SymbolTable;

import java.io.*;


public class Compiler {
    // Annotates class nodes with the names of files where they are defined.
    public static ParseTreeProperty<String> fileNames = new ParseTreeProperty<>();

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("No file(s) given");
            return;
        }
        
        CoolLexer lexer = null;
        CommonTokenStream tokenStream = null;
        CoolParser parser = null;
        ParserRuleContext globalTree = null;
        
        // True if any lexical or syntax errors occur.
        boolean lexicalSyntaxErrors = false;
        
        // Parse each input file and build one big parse tree out of
        // individual parse trees.
        for (var fileName : args) {
            var input = CharStreams.fromFileName(fileName);
            
            // Lexer
            if (lexer == null)
                lexer = new CoolLexer(input);
            else
                lexer.setInputStream(input);

            // Token stream
            if (tokenStream == null)
                tokenStream = new CommonTokenStream(lexer);
            else
                tokenStream.setTokenSource(lexer);
                
            /*
            // Test lexer only.
            tokenStream.fill();
            List<Token> tokens = tokenStream.getTokens();
            tokens.stream().forEach(token -> {
                var text = token.getText();
                var name = CoolLexer.VOCABULARY.getSymbolicName(token.getType());
                
                System.out.println(text + " : " + name);
                //System.out.println(token);
            });
            */
            
            // Parser
            if (parser == null)
                parser = new CoolParser(tokenStream);
            else
                parser.setTokenStream(tokenStream);
            
            // Customized error listener, for including file names in error
            // messages.
            var errorListener = new BaseErrorListener() {
                public boolean errors = false;
                
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer,
                                        Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg,
                                        RecognitionException e) {
                    String newMsg = "\"" + new File(fileName).getName() + "\", line " +
                                        line + ":" + (charPositionInLine + 1) + ", ";
                    
                    Token token = (Token)offendingSymbol;
                    if (token.getType() == CoolLexer.ERROR)
                        newMsg += "Lexical error: " + token.getText();
                    else
                        newMsg += "Syntax error: " + msg;
                    
                    System.err.println(newMsg);
                    errors = true;
                }
            };
            
            parser.removeErrorListeners();
            parser.addErrorListener(errorListener);
            
            // Actual parsing
            var tree = parser.program();
            if (globalTree == null)
                globalTree = tree;
            else {
                // Add the current parse tree's children to the global tree.
                for (int i = 0; i < tree.getChildCount(); i++)
                    globalTree.addAnyChild(tree.getChild(i));
                
                // Add the current parse tree's class nodes to the global tree's class nodes.
                ProgramContext program = (ProgramContext)globalTree;
                program.classes.addAll(tree.classes);
            }
                    
            // Annotate class nodes with file names, to be used later
            // in semantic error messages.
            for (int i = 0; i < tree.getChildCount(); i++) {
                var child = tree.getChild(i);
                // The only ParserRuleContext children of the program node
                // are class nodes.
                if (child instanceof ParserRuleContext)
                    fileNames.put(child, fileName);
            }
            
            // Record any lexical or syntax errors.
            lexicalSyntaxErrors |= errorListener.errors;
        }

        // Stop before semantic analysis phase, in case errors occurred.
        if (lexicalSyntaxErrors) {
            System.err.println("Compilation halted");
            return;
        }

        var astVisitor = new CoolASTConstructionVisitor();
        CoolASTNode ast = astVisitor.visit(globalTree);

        // System.out.println(new CoolPrintVisitior().visit(ast));
        
        // Populate global scope.
        SymbolTable.defineBasicClasses();
        
        ClassDefinitionPassVisitor classDefinitionVisitor = new ClassDefinitionPassVisitor();
        HierarchyResolutionPassVisitor hierarchyResolutionVisitor = new HierarchyResolutionPassVisitor();
        FeatureDefinitionPassVisitor featureDefinitionVisitor = new FeatureDefinitionPassVisitor();
        DefinitionPassVisitor definitionVisitor = new DefinitionPassVisitor();
        ResolutionPassVisitor resolutionVisitor = new ResolutionPassVisitor();

        /*
         * Symbol resolution is done in 5 passes:
         * 1. define the class symbols and scopes
         * 2. resolve parent classes, set class scopes' parents and check for cycles
         * 3. define class features in their own scope and check for redefinition;
         * 4a. check for redefinition of inherited attributes and incorrect method overloads
         * 4b. define everything else
         * 5. resolve everything else
         */
        
        classDefinitionVisitor.visit(ast);
        hierarchyResolutionVisitor.visit(ast);
        
        if (SymbolTable.hasSemanticErrors()) {
            System.err.println("Compilation halted");
            return;
        }

        featureDefinitionVisitor.visit(ast);
        definitionVisitor.visit(ast);
        resolutionVisitor.visit(ast);

        if (SymbolTable.hasSemanticErrors()) {
            System.err.println("Compilation halted");
            return;
        }

        CoolCodegenVisitor codegenVisitor = new CoolCodegenVisitor(
            resolutionVisitor.allIntegers,
            resolutionVisitor.allStrings,
            hierarchyResolutionVisitor.classes
        );
        ST asm = codegenVisitor.visit(ast);
        System.out.println(asm.render());
    }
}
