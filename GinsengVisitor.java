import java.util.List;

public class GinsengVisitor extends JavaParserBaseVisitor<Double> {
    

    // returns true if a break was found
    public boolean checkForBreak(JavaParser.StatementContext stmt) {
        // if there is no statement, it could be a declaration or sth
        if (stmt == null) {
            return false;
        }
        if (stmt.BREAK() != null) {
            return true;
        } else {
            return false;
        }
    }

	@Override
    public Double visitSwitchBlockStatementGroup(JavaParser.SwitchBlockStatementGroupContext ctx) {
        System.out.println("Saw a switch block statement group!");
		
		for (JavaParser.BlockStatementContext stmt: ctx.blockStatement()) {
            // TODO what if it's in a nested {}??
            if (checkForBreak(stmt.statement())) {
                System.out.println("\tYES! a break!");
            } else {
                System.out.println("\t...");
            }
        }
        
        return visitChildren(ctx);
    }


}
