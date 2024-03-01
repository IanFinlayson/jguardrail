package net.ianfinlayson.jguardrail;

// main driver class for this system
// we basically take every .java file given in argv and apply every checker to each

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Main {
    public static void main(String[] args) {
        boolean warned = false;

        // for each input file given to us
        for (int i = 0; i < args.length; i++) {
            // if it does not end in .java, skip it
            if (!args[i].contains(".java")) {
                continue;
            }

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

            // do not not do the default thing on errors (printing them out)
            // instead, add our own whose job it is to simply record if there are errors
            // if there are, jguardrail will not do any analysis (leaving them for javac)
            lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);
            parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
            
            ErrorListener el = new ErrorListener();
            lexer.addErrorListener(el);
            parser.addErrorListener(el);

            // do the parsing
            ParseTree tree = parser.compilationUnit();
            if (tree == null) {
                System.out.println("Couldn't parse at all!");
            }
            
            // if there were any errors, we bail now
            if (el.errorsExist()) {
                System.exit(0);
            }

            // we make a list of all the checks we have
            JavaParserBaseVisitor checkers [] = {
                new SwitchCheckVisitor(),
                new StringEqualsVisitor(),
                new IntDivideVisitor(),
                new MethodNameVisitor(),
                new ShadowCheckVisitor(),
                new SelfSetVisitor(),
                new InstVarInitVisitor(),
                new ControlStructureVisitor()
            };

            // run all the checkers
            for (JavaParserBaseVisitor checker : checkers) {
                // there have been some cases of a checker barfing up
                // exceptions, confusing students.  So we ignore any
                // errors not caught within the checker itself, but
                // do log them so I can investigate
                try {
                    checker.visit(tree);
                } catch (Exception e) {
                    Warnings.fail();
                }
            }

            // print the warnings accrued
            if (Warnings.printWarnings()) {
                warned = true;
            }
        }
    
        // use the exit code to indicate if warnings were given
        if (warned) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }
}

