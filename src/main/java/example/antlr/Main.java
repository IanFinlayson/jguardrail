package example.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class Main {

    public static void main(String... args) {
        System.out.println("Running the example...");

        ExprLexer lexer = new ExprLexer(CharStreams.fromString("100+2*6"));

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);
        ParseTree parseTree = parser.expr();

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new MyListener(), parseTree);
    }
}
