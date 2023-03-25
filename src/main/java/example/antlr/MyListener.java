package example.antlr;


public class MyListener extends ExprBaseListener {
    @Override
    public void enterExpr(ExprParser.ExprContext ctx) {
        System.out.println(ctx.getText());
    }
}
