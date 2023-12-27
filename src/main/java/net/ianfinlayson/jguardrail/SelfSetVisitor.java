package net.ianfinlayson.jguardrail;

// here we are checking for code that sets an identifier to itself
// this most often happens in code when in a constructor with code
// like this:
// public Thing(int var) {
//     var = var;
// }
// here we meant to assign the instance var to the constructor parameter

public class SelfSetVisitor extends JavaParserBaseVisitor<Void> {
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

