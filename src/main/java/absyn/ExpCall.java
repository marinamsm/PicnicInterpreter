package absyn;

import env.Entry;
import env.Env;
import env.FunEntry;
import env.Table;
import interpret.*;
import io.vavr.collection.List;
import io.vavr.collection.Tree;
import io.vavr.control.Option;
import io.vavr.render.ToTree;
import types.*;

import static error.ErrorHelper.*;


public class ExpCall extends Exp {

   public final String name;
   public final List<Exp> arguments;

   public ExpCall(Loc loc, String name, List<Exp> arguments) {
      super(loc);
      this.name = name;
      this.arguments = arguments;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpCall: " + name),
                     arguments.map(ToTree::toTree));
   }

   @Override
   protected Type semantic_(Env env) {
      Entry entry = env.venv.get(name);
      if (entry == null)
         throw undefined(loc, "function", name);
      if (!(entry instanceof FunEntry))
         throw notAFunction(loc, name);
      FunEntry fentry = (FunEntry) entry;
      if (arguments.size() < fentry.formals.size())
         throw tooFewArguments(loc, name);
      if (arguments.size() > fentry.formals.size())
         throw tooMuchArguments(loc, name);
      fentry.formals.zipWith(arguments,
                             (f, a) -> {
                                if (!a.semantic(env).is(f))
                                   throw typeMismatch(a.loc, a.type, f);
                                return 0;
                             });
      return fentry.result;
   }

   @Override
   public Value eval(Table<Value> memory, List<Fun> functions) {
      List<Value> args = arguments.map(a -> a.eval(memory, functions));
      Option<Fun> option = functions.find(fun -> fun.name.id == name);
      if (option.isEmpty())
         return applyPrimitive(name, args);
      Fun f = option.get();
      memory.beginScope();
      f.parameters.zipWith(args, (p, v) -> {
         memory.put(p.id, v);
         return null; });
      Value x = f.body.eval(memory, functions);
      memory.endScope();
      return x;
   }

   private Value applyPrimitive(String name, List<Value> arguments) {
      switch (name) {
         case "print":
            System.out.println(((ValueString)arguments.get(0)).value);
            return new ValueVoid();
         case "printint":
            System.out.println(((ValueInt)arguments.get(0)).value);
             return new ValueVoid();
         case "printdouble":
            System.out.println(((ValueDouble)arguments.get(0)).value);
            return new ValueVoid();
         case "printbool":
            System.out.println(((ValueBool)arguments.get(0)).value);
            return new ValueVoid();
         case "cast2double":
            return new ValueDouble(((ValueDouble)arguments.get(0)).value);
         case "round": {
             double d = ((ValueDouble) arguments.get(0)).value;
             return new ValueInt(Math.round(d));
         }
         case "ceil": {
             double d = ((ValueDouble) arguments.get(0)).value;
             long i = (long) Math.ceil(d + 0.5d);
             return new ValueInt(i);
         }
         case "floor": {
             double d = ((ValueDouble) arguments.get(0)).value;
             long i = (long) Math.floor(d - 0.5d);
             return new ValueInt(i);
         }
          case "size": {
              String str = ((ValueString) arguments.get(0)).value;
              long i = str.length();
              return new ValueInt(i);
          }
          case "char": {
              long index = ((ValueInt) arguments.get(0)).value;
              String str = ((ValueString) arguments.get(1)).value;
              String ch = Character.toString(str.charAt((int)index));
              return new ValueString(ch);
          }
          case "substr": {
              long start = ((ValueInt) arguments.get(0)).value;
              long end = ((ValueInt) arguments.get(1)).value;
              String str = ((ValueString) arguments.get(2)).value;
              String sub = str.substring((int)start, (int)end);
              return new ValueString(sub);
          }
          case "concat": {
              String str1 = ((ValueString) arguments.get(0)).value;
              String str2 = ((ValueString) arguments.get(1)).value;
              return new ValueString(str1 + str2);
          }
          case "return": {
              return new ValueInt(((ValueInt)arguments.get(0)).value);
          }
         default:
            fatal("unknown primitive function");
            return new ValueInt(0L);
      }
   }


}
