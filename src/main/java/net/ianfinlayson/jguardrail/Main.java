package net.ianfinlayson.jguardrail;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Main {

    public static void main(String[] args) {
        // for each input file given to us
        for (int i = 0; i < args.length; i++) {
            // set up streams
            JavaLexer lexer = null;
            try {
                lexer = new JavaLexer(CharStreams.fromFileName(args[i]));
                Warnings.setupWarnings(args[i]);
            } catch (Exception e) {
                System.out.println("Could not open '" + args[i] + "' for reading.");
                return;
            }
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokens);
            
            // ignore warnings from the parse itself (these will be caught by real compiler)
            lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);
            parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
            
            // do the parsing
            ParseTree tree = parser.compilationUnit();
            if (tree == null) {
                System.out.println("Couldn't parse at all!");
            }

            // we make a list of all the checks we have
            JavaParserBaseVisitor checkers [] = {
                new SwitchCheckVisitor(),
                new StringEqualsVisitor(),
                new IntDivideVisitor(),
                new VoidConstructorVisitor(),
                new ShadowCheckVisitor(),
                new SelfSetVisitor(),
                new InstVarInitVisitor()
            };

            // run all the checkers
            for (JavaParserBaseVisitor checker : checkers) {
                checker.visit(tree);
            }
            
            // print the warnings accrued
            Warnings.printWarnings();
        }
    }
}

