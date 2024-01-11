package net.ianfinlayson.jguardrail;

// this  checker looks for constructors that are erroneously given a
// return (typically but not necessarily void).  So what we actually
// are looking for is a method with the same name as the class, which
// likely was intended to be a constructor instead

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
            Warnings.warn(Warnings.VOID_CONSTRUCTOR, "regular method with name matching class name", method.getStart().getLine());
        }

        return null;
    }
}

