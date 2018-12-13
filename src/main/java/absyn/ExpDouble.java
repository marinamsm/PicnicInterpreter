package absyn;

import env.Env;
import env.Table;
import interpret.Value;
import interpret.ValueDouble;
import io.vavr.collection.List;
import io.vavr.collection.Tree;
import types.DOUBLE;
import types.Type;

public class ExpDouble extends Exp {
    public final double value;

    public ExpDouble(Loc loc, String value) {
        super(loc);
        this.value = new Double(value);
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of(annotateType("ExpDouble: " + value));
    }

    @Override
    protected Type semantic_(Env env) {
        return DOUBLE.T;
    }

    @Override
    public Value eval(Table<Value> memory, List<Fun> functions) {
        return new ValueDouble(value);
    }
}
