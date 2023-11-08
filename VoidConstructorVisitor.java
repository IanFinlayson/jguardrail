import java.util.Stack;

public class VoidConstructorVisitor extends JavaParserBaseVisitor<Void> {
    // we keep track of the name of the class we are in to make sure
    // we don't use it for method names (which indicates sth that should be
    // a constructor!)
    //
    // we use a stack so this works for nested classes
    private Stack<String> className = new Stack<>();

    // look for class declarations so that we can remember what class we are in
    @Override
    public Void visitClassDeclaration(JavaParser.ClassDeclarationContext theClass) {
        className.push(theClass.identifier().IDENTIFIER().getText());
        visit(theClass.classBody());
        className.pop();
        return null;
    }

    // catch method declarations crucially for this, it does NOT match constructors
    // which are separate in the grammar
    @Override
    public Void visitMethodDeclaration(JavaParser.MethodDeclarationContext method) {
        // get the name of the method
        String name = method.identifier().IDENTIFIER().getText();

        if (name.equals(className.peek())) {
            System.out.println("Warning: regular method with name matching class name on line: " + method.getStart().getLine());
        }

        return null;
    }
}

