package absyn;

import env.Env;
import io.vavr.collection.Tree;
import types.BOOL;
import types.Type;

public class TyBool extends Ty {

   public TyBool(Loc loc) {
      super(loc);
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of("bool");
   }

   @Override
   public Type semantic(Env env) {
      return BOOL.T;
   }
}
