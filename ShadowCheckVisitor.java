import java.util.List;
import java.util.Stack;
import java.util.ArrayList;

// this checker looks for instance variables being shadowed by
// local variable declarations in constructors or methods

// we basically treewalk each class to look for two things:
// 1. all the instance vars to remember their names
// 2. all local variables to make sure they aren't named the same (shadowing)

// to make this work with multiple classes, we maintain a stack of scopes
// when we see a new class, we push a new frame to add to
// when we leave a class, we pop the frame off
// this handles multiple classes whether nested or sequential

// we enforce this being done in two passes to ensure that it works
// no matter where the instance vars are declared (we can't assume
// they are all at the top)

public class ShadowCheckVisitor extends JavaParserBaseVisitor<Void> {

    private Stack<List<String>> instances = new Stack<>();
    private int pass = 1;
    
    @Override
    public Void visitClassDeclaration(JavaParser.ClassDeclarationContext theClass) {
        // push a stack frame
        instances.push(new ArrayList<String>());
        
        // do pass 1 (collecting inst vars)
        pass = 1;
        visit(theClass.classBody());
        
        // do pass 2 (check for shadows)
        pass = 2;
        visit(theClass.classBody());
        
        // pop the stack and return
        instances.pop();
        return null;
    }

    // this is for pass 1, a field being declared
    @Override
    public Void visitFieldDeclaration(JavaParser.FieldDeclarationContext decl) {
        if (pass != 1) return null;

        List<JavaParser.VariableDeclaratorContext> vars = decl.variableDeclarators().variableDeclarator();
        for (JavaParser.VariableDeclaratorContext var : vars) {
            String name = var.variableDeclaratorId().identifier().IDENTIFIER().getText();
            instances.peek().add(name);
        }

        return null;
    }

    // this is for pass 2, a local variable being declared
	@Override
    public Void visitLocalVariableDeclaration(JavaParser.LocalVariableDeclarationContext decl) {
        if (pass != 2) return null;

        List<JavaParser.VariableDeclaratorContext> vars = decl.variableDeclarators().variableDeclarator();
        for (JavaParser.VariableDeclaratorContext var : vars) {
            String name = var.variableDeclaratorId().identifier().IDENTIFIER().getText();

            // check if we've seen this one
            for (String instance : instances.peek()) {
                if (name.equals(instance)) {
                    Warnings.warn(Warnings.SHADOW_VARIABLE, "local variable '" + name + "' shadowing instance variable with same name",
                            decl.getStart().getLine());
                }
            }
        }

        return null;
    }
}

