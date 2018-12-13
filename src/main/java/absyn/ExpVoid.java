package absyn;

import env.Env;
import env.Table;
import interpret.Value;
import interpret.ValueVoid;
import io.vavr.collection.List;
import io.vavr.collection.Tree;
import types.VOID;
import types.Type;

public class ExpVoid extends Exp {
    public final String value;

    public ExpVoid(Loc loc) {
        super(loc);
        this.value = null;
    }

    @Override
    public Tree.Node<String> toTree() {
        return Tree.of(annotateType("ExpVoid: " + value));
    }

    @Override
    protected Type semantic_(Env env) {
        return VOID.T;
    }

    @Override
    public Value eval(Table<Value> memory, List<Fun> functions) {
        return null;
    }
}
