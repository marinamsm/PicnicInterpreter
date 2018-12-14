package env;

import io.vavr.collection.List;
import types.*;

public class Env {

  public Table<Type> tenv;
  public Table<Entry> venv;

  public Env() {
    tenv = new Table<Type>();
    venv = new Table<Entry>();
    put(venv, "print", new FunEntry(List.of(STRING.T), VOID.T) );
    put(venv, "printint", new FunEntry(List.of(INT.T), VOID.T));
    put(venv, "printdouble", new FunEntry(List.of(DOUBLE.T), VOID.T) );
    put(venv, "printbool", new FunEntry(List.of(BOOL.T), VOID.T) );
    put(venv, "cast2double", new FunEntry(List.of(INT.T), DOUBLE.T));
    put(venv, "round", new FunEntry(List.of(DOUBLE.T), INT.T));
    put(venv, "ceil", new FunEntry(List.of(DOUBLE.T), INT.T));
    put(venv, "floor", new FunEntry(List.of(DOUBLE.T), INT.T));
    put(venv, "size", new FunEntry(List.of(STRING.T), INT.T));
    put(venv, "char", new FunEntry(List.of(INT.T, STRING.T), STRING.T));
    put(venv, "substr", new FunEntry(List.of(INT.T, INT.T, STRING.T), STRING.T));
    put(venv, "concat", new FunEntry(List.of(STRING.T, STRING.T), STRING.T));
    put(venv, "sumdouble", new FunEntry(List.of(DOUBLE.T, DOUBLE.T), DOUBLE.T));
    put(venv, "sumint", new FunEntry(List.of(INT.T, INT.T), INT.T));
    put(venv, "return", new FunEntry(List.of(INT.T), INT.T));
  }

  private static <E> void put(Table<E> table, String name, E value) {
    table.put(name.intern(), value);
  }

   @Override
   public String toString() {
      return "Env{" +
         "tenv=" + tenv +
         ", venv=" + venv +
         '}';
   }

}
