package absyn;

import env.Env;
import env.VarEntry;
import io.vavr.collection.Tree;
import types.Type;

public class ExpLet extends Exp {

   public final String variable;
   public final Exp init;
   public final Exp body;

   public ExpLet(Loc loc, String variable, Exp init, Exp body) {
      super(loc);
      this.variable = variable;
      this.init = init;
      this.body = body;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpLet"),
                     Tree.of(variable),
                     init.toTree(),
                     body.toTree());
   }

   @Override
   protected Type semantic_(Env env) {
      System.out.println("init: " + init.toString());
      System.out.println("body: " + body.toString());
      System.out.println("variable: " + variable);
      // check the initialization expression
      init.semantic(env);
      // save the current environment
      env.venv.beginScope();
      // add the initialization expression to environemnt
      env.venv.put(variable, new VarEntry(init.type));
      // check the body
      body.semantic(env);
      // restores the environment
      env.venv.endScope();
      // final type
      return body.type;
   }

}
