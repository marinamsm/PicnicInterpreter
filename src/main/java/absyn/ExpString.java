package absyn;

import env.Env;
import env.Table;
import interpret.Value;
import interpret.ValueString;
import io.vavr.collection.List;
import io.vavr.collection.Tree;
import types.STRING;
import types.Type;

public class ExpString extends Exp {
    public final String value;

    public ExpString(Loc loc, String value) {
        super(loc);
        this.value = value;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of(annotateType("ExpString: " + value));
    }

    @Override
    protected Type semantic_(Env env) {
        // COMPLETE
        return STRING.T;
    }

    @Override
    public Value eval(Table<Value> memory, List<Fun> functions) {
        return new ValueString(value);
    }
}
