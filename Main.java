
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Main {

    public static void main(String... args) {
        // get input file
        if (args.length != 1) {
            System.out.println("Please pass the Java file as input.");
            return;
        }

        // set up streams
        JavaLexer lexer = null;
        try {
            lexer = new JavaLexer(CharStreams.fromFileName(args[0]));
        } catch (Exception e) {
            System.out.println("Could not open '" + args[0] + "' for reading.");
            return;
        }
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        
        // do the parsing
        ParseTree tree = parser.compilationUnit();

        // visit the tree to execute it
        GinsengVisitor visitor = new GinsengVisitor();
        visitor.visit(tree);
    }
}

