
public class SelfSetVisitor extends JavaParserBaseVisitor<Void> {
    // we need to visit assignment statements to see if we are setting
    // something equal to itself


	@Override
    public Void visitExpression(JavaParser.ExpressionContext expr) {
        if(expr.ASSIGN() != null) {
            // try to grab them both as identifiers, if this doesn't work then
            // they weren't just ids, and we can bail
            try {
                String lhs = expr.expression(0).primary().identifier().IDENTIFIER().getText();
                String rhs = expr.expression(1).primary().identifier().IDENTIFIER().getText();
                
                if (lhs.equals(rhs)) {
                    Warnings.warn(Warnings.SELF_ASSIGN, "setting a variable to itself has no effect", expr.getStart().getLine());
                }

            } catch (NullPointerException e) {
                // yeah move on...
            }
        }

        return null;
    }

}

