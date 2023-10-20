import java.util.List;

public class StringEqualsVisitor extends JavaParserBaseVisitor<Void> {

    // hook into the antlr visitor system to catch using == with strings
    @Override
    public Void visitExpression(JavaParser.ExpressionContext expr) {
       
        if (expr.EQUAL() != null) {
            System.out.println("Found an == operator!");
        } else if (expr.NOTEQUAL() != null) {
            System.out.println("Found a != operator!");
        }
        
        // recurse (in order to get switches that may be nested themselves)
        return visitChildren(expr);
    }
}

