package absyn;

import env.Env;
import io.vavr.collection.Tree;
import types.DOUBLE;
import types.Type;

public class TyDouble extends Ty {

    public TyDouble(Loc loc) {
        super(loc);
    }

    @java.lang.Override
    public Tree.Node<String> toTree() {
        return Tree.of("double");
    }

    @Override
    public Type semantic(Env env) {
        return DOUBLE.T;
    }
}
