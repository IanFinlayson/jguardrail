package net.ianfinlayson.jguardrail;

// this checker scans for the instance variables of a class and makes
// sure each one is either (a) initialized inline or (b) initialized
// in every constructor of the class.  This is perhaps arguable since
// (unlike some languages) Java has defined default values.  But many
// student bugs come from not initializing variables properly

import java.util.ArrayList;
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

// we need to check for instance variable declarations
// we keep a list of those that have not been init'd inline
// we also check the constructors to see if they do it or not
public class InstVarInitVisitor extends JavaParserBaseVisitor<Void> {
    // we store these for each instance var
    class InstVar {
        public String name;
        public int line;
        public int consInitCount;

        public InstVar(String name, int line) {
            this.name = name;
            this.line = line;
            this.consInitCount = 0;
        }
    }

    // would be ironic if we forgot to init these lol
    private Stack<ArrayList<InstVar>> uninitializedVars = new Stack<>();
    private Stack<Integer> numConstructors = new Stack<>();

    // we make two passes: one to peep the inst vars and another for constructors
    // this is since it could be that constructors come before inst. vars
    private int pass = 1;

    // mark a variable as being set to sth in a constructor
    private void markWrite(String name) {
        for (int i = 0; i < uninitializedVars.peek().size(); i++) {
            if (uninitializedVars.peek().get(i).name.equals(name)) {
                uninitializedVars.peek().get(i).consInitCount++;
            }
        }
    }

    @Override
    public Void visitClassDeclaration(JavaParser.ClassDeclarationContext theClass) {
        // push new lists of vars, and num constructors -- stack is used here so nested
        // classes are handled correctly
        uninitializedVars.push(new ArrayList<>());
        numConstructors.push(0);

        // run through the class members to find all the instance variables first
        visit(theClass.classBody());

        // then do it again on pass 2 to get the constructors
        pass++;
        visit(theClass.classBody());

        // now we know what was missed
        for (InstVar v : uninitializedVars.peek()) {
            if (v.consInitCount < numConstructors.peek() || numConstructors.peek() == 0) {
                Warnings.warn(Warnings.UNINITIALIZED, "instance variable " + v.name + " was not initialized inline nor in all constructors", v.line);
            }
        }

        // pop this set of things
        uninitializedVars.pop();
        numConstructors.pop();

        return null;
    }

    // this is called for each instance variable being declared
    @Override
    public Void visitFieldDeclaration(JavaParser.FieldDeclarationContext decl) {
        // only for pass 1
        if (pass != 1) return null;
        if (uninitializedVars.empty()) return null;

        List<JavaParser.VariableDeclaratorContext> vars = decl.variableDeclarators().variableDeclarator();
        for (JavaParser.VariableDeclaratorContext var : vars) {
            // get the name
            String name = var.variableDeclaratorId().identifier().IDENTIFIER().getText();
            
            // if it doesn't contain an assign, throw it in the list
            if (var.ASSIGN() == null) {
                uninitializedVars.peek().add(new InstVar(name, decl.getStart().getLine()));
            }
        }
        return null;
    }

    // called for each statement inside a constructor, this way we can catch assignments that take
    // place inside ifs or switches or what-have-you
    public void handleStatement(JavaParser.StatementContext stmt, Set<String> varsWritten) {
        // if it's an if, recurse on sub-statement
        if (stmt.IF() != null) {
            for (JavaParser.StatementContext sub : stmt.statement()) {
                handleStatement(sub, varsWritten);
            }
            return;
        }

        // if it's a switch also do
        if (stmt.SWITCH() != null) {
            for (JavaParser.SwitchBlockStatementGroupContext swblk : stmt.switchBlockStatementGroup()) {
                for (JavaParser.BlockStatementContext blk : swblk.blockStatement()) {
                    handleStatement(blk.statement(), varsWritten);
                }
            }
            return;
        }

        // if it's a block, go through each statement in it
        if (stmt.block() != null) {
		    for (JavaParser.BlockStatementContext blk : stmt.block().blockStatement()) {
                handleStatement(blk.statement(), varsWritten);
            }
            return;
        }

        // otherwise look for the assignment statement here
        try {
            for (JavaParser.ExpressionContext expr : stmt.expression()) {
                if (expr.ASSIGN() != null) {
                    // one way that these can be written to is straight up by name
                    try {
                        String lhs = expr.expression(0).primary().identifier().IDENTIFIER().getText();
                        varsWritten.add(lhs);
                    } catch (NullPointerException e) {}

                    // the other way is as this. then a variable name
                    try {
                        if ((expr.expression(0).DOT() != null) && (expr.expression(0).expression(0).primary().THIS() != null)) {
                            String lhs = expr.expression(0).identifier().IDENTIFIER().getText();
                            varsWritten.add(lhs);
                        }
                    } catch (NullPointerException e) {}
                }
            }
        } catch (NullPointerException e) {}
    }

    // this is called for each constructor
    @Override
    public Void visitConstructorDeclaration(JavaParser.ConstructorDeclarationContext cons) {
        // only for pass 2
        if (pass != 2) return null;

        // we've seen another constructor
        int count = numConstructors.pop();
        numConstructors.push(count + 1);

        // keep a set of vars written in this constructor -- we mark them only at end so
        // that even if a inst var is written multiple times in a constructor, it will be
        // only counted as once
        Set<String> varsWritten = new HashSet<>();

        // go through each line of the constructor and check if an inst var is written
        for (JavaParser.BlockStatementContext blk : cons.block().blockStatement()) {
            handleStatement(blk.statement(), varsWritten);
        }

        // mark each var written as written
        for (String varname : varsWritten) {
            markWrite(varname);
        }

        return null;
    }
}

