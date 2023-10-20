import java.util.List;

public class StringEqualsVisitor extends JavaParserBaseVisitor<Void> {


    // hook into the antlr visitor system to catch using == with strings
    @Override
    public Void visitExpression(JavaParser.ExpressionContext expr) {
       
        if ((expr.EQUAL() != null) || (expr.NOTEQUAL() != null)) {
            JavaParser.ExpressionContext lhs = expr.expression(0), rhs = expr.expression(1);

            // if either side is a string literal, flag it
            if ((lhs.primary() != null && lhs.primary().literal() != null && lhs.primary().literal().STRING_LITERAL() != null) ||
                (rhs.primary() != null && rhs.primary().literal() != null && rhs.primary().literal().STRING_LITERAL() != null)) {

                System.out.println("Warning: using the == operator between string literals on line " + expr.getStart().getLine());
            }
        }
        
        // recurse (in order to get switches that may be nested themselves)
        return visitChildren(expr);
    }
}

