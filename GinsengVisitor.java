import java.util.List;

public class GinsengVisitor extends JavaParserBaseVisitor<Double> {


    // returns true if a break was found
    public boolean checkForBreak(JavaParser.StatementContext stmt) {
        if (stmt == null) {
            // if there is no statement, it could be a declaration or sth else
            return false;
        } else if (stmt.BREAK() != null) {
            // we found a break!
            return true;
        } else if (stmt.block() != null) {
            // it's a nested one, such as arises from { } in the case. recurse
            for (JavaParser.BlockStatementContext block : stmt.block().blockStatement()) {
                if (checkForBreak(block.statement())) {
                    return true;
                }
            }
        }
        
        return false;
    }

	@Override
    public Double visitSwitchBlockStatementGroup(JavaParser.SwitchBlockStatementGroupContext ctx) {
		boolean break_found = false;
		for (JavaParser.BlockStatementContext stmt : ctx.blockStatement()) {
            if (checkForBreak(stmt.statement())) {
                break_found = true;
            }
        }

        if (!break_found) {
            System.out.println("Switch case on line " + ctx.getStart().getLine() + " missing break.");
        }

        return visitChildren(ctx);
    }

// TODO don't do this for the last case!!!

}
