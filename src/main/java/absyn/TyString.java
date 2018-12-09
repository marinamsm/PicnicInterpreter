package absyn;

import env.Env;
import io.vavr.collection.Tree;
import types.STRING;
import types.Type;

public class TyString extends Ty {

    public TyString(Loc loc) {
        super(loc);
    }

    @java.lang.Override
    public Tree.Node<String> toTree() {
        return Tree.of("string");
    }

    @Override
    public Type semantic(Env env) {
        return STRING.T;
    }
}
