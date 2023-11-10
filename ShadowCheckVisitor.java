import java.util.List;
import java.util.ArrayList;

public class ShadowCheckVisitor extends JavaParserBaseVisitor<Void> {
    // we need to treewalk for two things:
    // 1. all the instance vars to remember their names
    // 2. all local variables to make sure they aren't named the same (shadowing)

    private ArrayList<String> instances = new ArrayList<>();


    // this is for part 1, a field being declared
    @Override
    public Void visitFieldDeclaration(JavaParser.FieldDeclarationContext decl) {

        List<JavaParser.VariableDeclaratorContext> vars = decl.variableDeclarators().variableDeclarator();
        for (JavaParser.VariableDeclaratorContext var : vars) {
            String name = var.variableDeclaratorId().identifier().IDENTIFIER().getText();
            instances.add(name);
        }

        return null;
    }

    // this is for part 2, a local variable being declared
	@Override
    public Void visitLocalVariableDeclaration(JavaParser.LocalVariableDeclarationContext decl) {
        List<JavaParser.VariableDeclaratorContext> vars = decl.variableDeclarators().variableDeclarator();
        for (JavaParser.VariableDeclaratorContext var : vars) {
            String name = var.variableDeclaratorId().identifier().IDENTIFIER().getText();

            // check if we've seen this one
            for (String instance : instances) {
                if (name.equals(instance)) {
                    Warnings.warn("local variable '" + name + "' shadowing instance variable with same name",
                            decl.getStart().getLine());
                }
            }
        }

        return null;
    }
}

