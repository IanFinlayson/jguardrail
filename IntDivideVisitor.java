import java.util.List;

public class IntDivideVisitor extends JavaParserBaseVisitor<Void> {


    // hook into the antlr visitor system to catch dividing two integers by each other that are not
    // actually divisble -- especially things like (1/2)
    @Override
    public Void visitExpression(JavaParser.ExpressionContext expr) {

        if (expr.DIV() != null) {
            // try to get the integers we are dividing, if this produces NPE,
            // one or both was not an integer literal
            try {
                int lhs = Integer.parseInt(expr.expression(0).primary().literal().integerLiteral().DECIMAL_LITERAL().getText());
                int rhs = Integer.parseInt(expr.expression(1).primary().literal().integerLiteral().DECIMAL_LITERAL().getText());

                // if lhs < rhs, it's 0 (like 1/2)
                // otherwise, if it doesn't divide clean (like 9/5) tell them that too
                // if it divides evenly I still don't know why you're doing this but carry on
                if (lhs < rhs) {
                    System.out.println("Warning: division between integer constants produces 0 value on line: " + expr.getStart().getLine());
                } else if ((lhs % rhs) != 0) {
                    System.out.println("Waning: division between integer constants truncates value on line: " + expr.getStart().getLine());
                }
            } catch (NullPointerException e) {
                // not the droids we're looking for...
            }
        }

        return visitChildren(expr);
    }
}

