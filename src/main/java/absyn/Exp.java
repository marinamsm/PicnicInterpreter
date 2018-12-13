package absyn;

import env.Env;
import env.Table;
import interpret.Value;
import interpret.ValueDouble;
import interpret.ValueInt;
import io.vavr.collection.List;
import types.Type;

public abstract class Exp extends AST {

   // Type of the expression, calculated by the semantic analyser
   public Type type;

   public Exp(Loc loc) {
      super(loc);
   }

   // Obtain the type of the expression as a string prefixed by
   // the given text. Will be needed when drawing its AST with
   // annotated types.
   protected String annotateType(String text) {
      final String theType = type == null ? "" : "\n<" + type + ">";
      return text + theType;
   }

   // Do semantic analysis of the expression and assigns its type.
   public Type semantic(Env env) {
      type = semantic_(env);
      return type;
   }

   // Type check the expression and obtains its type.
   // Should be defined in the concrete subclasses.
   protected abstract Type semantic_(Env env);

   // TODO: abstract
   public Value eval(Table<Value> memory, List<Fun> functions) {
      return new ValueInt(0L);
   }
}
