package net.ianfinlayson.jguardrail;

// this checker looks for control structures with no bodies, which can happen
// when one accidentally puts a ; at the end of a loop or if statement:
//
// for (int i = 0; i < 10; i++);
// {
//     // code
// }

// we also look for bodies that are only a single statment without the { }
// marking the block -- this one is less WRONG per se, but a widely agreed
// upon style issue that may lead to mistakes down the line

import java.util.List;

public class ControlStructureVisitor extends JavaParserBaseVisitor<Void> {

    // check if a given statement is empty
    boolean isEmptyStatement(JavaParser.StatementContext stmt) {
        // if it literally is just a semi-colon
        return stmt.getStart().getText().equals(";");
    }
    
    // check if given statement is "raw" i.e. not in any kind of { block }
    boolean isRawStatement(JavaParser.StatementContext stmt) {
        // if there is no block, it's just a straight-up nested statment
        return stmt.block() == null;
    }

    @Override
    public Void visitStatement(JavaParser.StatementContext statement) {
        // check for the types of control structures we are looking for
        if (statement.FOR() != null || statement.WHILE() != null || statement.IF() != null || statement.DO() != null) {

            List<JavaParser.StatementContext> stmts = statement.statement();
            
            if (stmts.size() == 0 || (stmts.size() == 1 && isEmptyStatement(stmts.get(0)))) {
                Warnings.warn(Warnings.EMPTY_CONTROL, "control statement has no body", statement.getStart().getLine());
            }
            
            else if (stmts.size() == 1 && isRawStatement(stmts.get(0))) {
                Warnings.warn(Warnings.MISSING_BRACES, "missing curly braces on control block", statement.getStart().getLine());
            }
        }

        return visitChildren(statement);
    }
}

