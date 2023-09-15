
public class GinsengVisitor extends JavaParserBaseVisitor<Double> {
    


	@Override public Double visitSwitchBlockStatementGroup(JavaParser.SwitchBlockStatementGroupContext ctx) {
        System.out.println("Saw a switch block statement group!");
        return visitChildren(ctx);
    }


}
