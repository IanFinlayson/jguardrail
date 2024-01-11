package net.ianfinlayson.jguardrail;

// this  checker looks for method names that indicate likely issues these are:
//
// 1. methods with the same name as the class they are in.  these are more than
// likely meant to be constructors but were mistakenly given a return type
//
// 2. methods named "tostring" which were likely meant to be "toString" instead

import java.util.Stack;

public class MethodNameVisitor extends JavaParserBaseVisitor<Void> {
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

    // catch method declarations so we can check their name
    @Override
    public Void visitMethodDeclaration(JavaParser.MethodDeclarationContext method) {
        // get the name of the method
        String name = method.identifier().IDENTIFIER().getText();

        if (name.equals(className.peek())) {
            Warnings.warn(Warnings.VOID_CONSTRUCTOR, "regular method with name matching class name, constructors have no return types", method.getStart().getLine());
        }
        
        else if (name.equals("tostring")) {
            Warnings.warn(Warnings.TOSTRING, "method named 'tostring', did you mean 'toString' instead?", method.getStart().getLine());
        }

        return null;
    }
}

