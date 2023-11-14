import java.util.ArrayList;
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
    private ArrayList<InstVar> uninitializedVars = new ArrayList<>();
    private int numConstructors = 0;
    
    // we make two passes: one to peep the inst vars and another for constructors
    // could be that constructors come before inst. vars
    private int pass = 1;

    @Override
    public Void visitClassDeclaration(JavaParser.ClassDeclarationContext theClass) {
        // before entering the class, clear the list of vars
        uninitializedVars.clear();
        
        // run through the class members to find all the instance variables first
        visit(theClass.classBody());

        // then do it again on pass 2 to get the constructors
        pass++;
        visit(theClass.classBody());

        // now we know what was missed
        for (InstVar v : uninitializedVars) {
            if (v.consInitCount < numConstructors) {
                Warnings.warn("instance variable " + v.name + " was not initialized inline nor in all constructors", v.line);
            }
        }
        return null;
    }

    // this is called for each instance variable being declared
    @Override
    public Void visitFieldDeclaration(JavaParser.FieldDeclarationContext decl) {
        // only for pass 1
        if (pass != 1) return null;

        List<JavaParser.VariableDeclaratorContext> vars = decl.variableDeclarators().variableDeclarator();
        for (JavaParser.VariableDeclaratorContext var : vars) {
            // get the name
            String name = var.variableDeclaratorId().identifier().IDENTIFIER().getText();

            // if it doesn't contain an assign, throw it in the list
            if (var.ASSIGN() == null) {
                uninitializedVars.add(new InstVar(name, decl.getStart().getLine()));
            }
        }
        return null;
    }

    // this is called for each constructor

	@Override
    public Void visitConstructorDeclaration(JavaParser.ConstructorDeclarationContext cons) {
        // only for pass 2
        if (pass != 2) return null;

        // we've seen another constructor
        numConstructors++;

        // go through each line of the constructor and check if an inst var is written
		for (JavaParser.BlockStatementContext blk : cons.block().blockStatement()) {
            // try to see it as an assignment statement
            try {
                    // TODO
                    System.out.println("Saw a stmt");

            } catch (NullPointerException e) {
                // ignore this, not an assignment
            }
        }



        return null;
    }

}

