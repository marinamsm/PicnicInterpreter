package absyn;

import env.Entry;
import env.Env;
import env.FunEntry;
import env.VarEntry;
import io.vavr.collection.List;
import io.vavr.collection.Tree;
import io.vavr.render.ToTree;
import types.INT;
import types.Type;

import static error.ErrorHelper.*;

public class Fun extends AST {

   public final TypeId name;
   public final List<TypeId> parameters;
   public final Exp body;

   public Fun(Loc loc, TypeId name, List<TypeId> parameters, Exp body) {
      super(loc);
      this.name = name;
      this.parameters = parameters == null ? List.empty() : parameters;
      this.body = body;
   }

   @java.lang.Override
   public Tree.Node<String> toTree() {
      return Tree.of("Fun",
                     name.toTree(),
                     Tree.of("Parameters",
                             parameters.map(ToTree::toTree)),
                     body.toTree());
   }

   // Semantic analysis of mutual recursive function declarations
   // is done in two phases.

   // First the signature of each function declarations (type of
   // formal parameters and type of result) is calculated and the
   // function name is added to the environment.

   // Then the body of each function is checked using the extended
   // environment.

   // Add the signature of the function to the environment
   public void checkSignature(Env env) {
      // verify that parameter names are unique
      List<String> parameterNames = List.empty();
      for (TypeId p : parameters) {
         if (parameterNames.contains(p.id))
            throw sameParameter(p.loc, p.id);
         parameterNames = parameterNames.append(p.id);
      }
      // check each parameters
      List<Type> formals = parameters.map(p -> p.semantic(env));
      // check the result type
      Type result = name.semantic(env);
      if (name.id.equals("main") && !(formals.isEmpty()))
          throw tooMuchArguments(name.loc, name.id);
      if (name.id.equals("main") && !(result.is(INT.T)))
          throw typeMismatch(name.loc, result, INT.T);
      // add function name to the environment
      env.venv.put(name.id, new FunEntry(formals, result));
   }

   // Check the function body
   public void checkBody(Env env) {
      // retrieve the function signature from the environment
      Entry entry = env.venv.get(name.id);
      if (! (entry instanceof FunEntry))
         fatal("'%s' should be a function", entry);
      FunEntry f = (FunEntry) entry;
      // save the current environment
      env.venv.beginScope();
      // add the parameters to the environment
      List<TypeId> ps = parameters;
      List<Type> fs = f.formals;
      while (! ps.isEmpty() && ! fs.isEmpty()) {
         env.venv.put(ps.head().id, new VarEntry(fs.head()));
         ps = ps.tail();
         fs = fs.tail();
      }
      // check the body
      body.semantic(env);
      if (! body.type.is(f.result))
         throw typeMismatch(body.loc, body.type, f.result);
      // restore the current environment
      env.venv.endScope();
   }

}
