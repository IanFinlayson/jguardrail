package net.ianfinlayson.jguardrail;

// this checker looks for places in code where we divide two integer literals that
// do not actually divide evenly.  For example if we have something like
// var1 = var2 * (1/2);
// this of course results in a 0 value since the division is truncated

import java.util.List;

public class IntDivideVisitor extends JavaParserBaseVisitor<Void> {
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
                    Warnings.warn(Warnings.INT_DIVIDE, "division between integer constants produces 0 value", expr.getStart().getLine());
                } else if ((lhs % rhs) != 0) {
                    Warnings.warn(Warnings.INT_DIVIDE, "division between integer constants truncates value", expr.getStart().getLine());
                }
            } catch (NullPointerException e) {
                // not the droids we're looking for...
            }
        }

        return visitChildren(expr);
    }
}

