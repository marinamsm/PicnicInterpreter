package absyn;

import env.Env;
import env.Table;
import interpret.Value;
import interpret.ValueBool;
import interpret.ValueDouble;
import interpret.ValueInt;
import io.vavr.collection.List;
import io.vavr.collection.Tree;
import types.BOOL;
import types.INT;
import types.Type;

import static error.ErrorHelper.fatal;
import static error.ErrorHelper.typeMismatch;

public class ExpBin extends Exp {

   public final BinOp op;
   public final Exp left;
   public final Exp right;

   public ExpBin(Loc loc, BinOp op, Exp left, Exp right) {
      super(loc);
      this.op = op;
      this.left = left;
      this.right = right;
   }

   @Override
   public Tree.Node<String> toTree() {
      return Tree.of(annotateType("ExpBin: " + op),
                     left.toTree(),
                     right.toTree());
   }

   @Override
   protected Type semantic_(Env env) {
      left.semantic(env);
      right.semantic(env);
      switch (op) {
         // arithmetic operations
         case PLUS:
         case MINUS:
         case TIMES:
         case DIV:
         case POWER:
            if (! left.type.is(INT.T))
               throw typeMismatch(left.loc, left.type, INT.T);
            if (! right.type.is(INT.T))
               throw typeMismatch(right.loc, right.type, INT.T);
            return INT.T;

         // relational operations
         case LT:
         case LE:
         case GT:
         case GE:
            if (! left.type.is(INT.T))
               throw typeMismatch(left.loc, left.type, INT.T);
            if (! right.type.is(INT.T))
               throw typeMismatch(right.loc, right.type, INT.T);
            return BOOL.T;

         case EQ:
         case NE:
            if (left.type.is(right.type))
               return BOOL.T;
            if (right.type.is(left.type))
               return BOOL.T;
            throw typeMismatch(right.loc, right.type, left.type);

         // logical operations
         case AND:
         case OR:
            if (! left.type.is(BOOL.T))
               throw typeMismatch(left.loc, left.type, BOOL.T);
            if (! right.type.is(BOOL.T))
               throw typeMismatch(right.loc, right.type, BOOL.T);
            return BOOL.T;

         default:
            throw fatal("incomplete semantic analisys of binary operation");
      }
   }

   @Override
   public Value eval(Table<Value> memory, List<Fun> functions) {
      Value x = left.eval(memory, functions);
      Value y = right.eval(memory, functions);
      switch (op) {
         case PLUS: return new ValueDouble(((ValueDouble)x).value + ((ValueDouble)y).value);
         case MINUS: return new ValueInt(((ValueInt)x).value - ((ValueInt)y).value);
         case TIMES: return new ValueInt(((ValueInt)x).value * ((ValueInt)y).value);
         case DIV: return new ValueInt(((ValueInt)x).value / ((ValueInt)y).value);
         case POWER: return new ValueInt(Math.round(Math.pow(((ValueInt)x).value, ((ValueInt)y).value)));

         case AND: return new ValueBool(((ValueBool)x).value && ((ValueBool)y).value);
         case OR: return new ValueBool(((ValueBool)x).value || ((ValueBool)y).value);

         case LT: return new ValueBool(((ValueInt)x).value < ((ValueInt)y).value);
         case LE: return new ValueBool(((ValueInt)x).value <= ((ValueInt)y).value);
         case GT: return new ValueBool(((ValueInt)x).value > ((ValueInt)y).value);
         case GE: return new ValueBool(((ValueInt)x).value >= ((ValueInt)y).value);
         case NE: return new ValueBool(((ValueInt)x).value != ((ValueInt)y).value);
         case EQ: return new ValueBool(((ValueInt)x).value == ((ValueInt)y).value);
      }
      return super.eval(memory, functions);
   }
}
