package absyn;

import env.Env;
import io.vavr.collection.Tree;
import types.Type;


public class ExpAssign extends Exp {
    public final String variable;
    public final Exp expression;

    public ExpAssign(Loc loc, String variable, Exp expression) {
        super(loc);
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of(annotateType("ExpAssign"),
                Tree.of(variable),
                expression.toTree());
    }

    @Override
    protected Type semantic_(Env env) {
        // COMPLETE
        return expression.type;
    }
}