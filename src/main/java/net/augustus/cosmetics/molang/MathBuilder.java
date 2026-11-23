package net.augustus.cosmetics.molang;

import net.augustus.cosmetics.molang.ast.BinaryExpression;
import net.augustus.cosmetics.molang.ast.ConstantExpression;
import net.augustus.cosmetics.molang.ast.GroupExpression;
import net.augustus.cosmetics.molang.ast.LogicalNotExpression;
import net.augustus.cosmetics.molang.ast.MolangValue;
import net.augustus.cosmetics.molang.ast.MolangVariable;
import net.augustus.cosmetics.molang.ast.NegateExpression;
import net.augustus.cosmetics.molang.ast.TernaryExpression;
import net.augustus.cosmetics.molang.functions.AbsFunction;
import net.augustus.cosmetics.molang.functions.CeilFunction;
import net.augustus.cosmetics.molang.functions.ClampFunction;
import net.augustus.cosmetics.molang.functions.CosFunction;
import net.augustus.cosmetics.molang.functions.ExpFunction;
import net.augustus.cosmetics.molang.functions.FloorFunction;
import net.augustus.cosmetics.molang.functions.LerpFunction;
import net.augustus.cosmetics.molang.functions.LerpRotateFunction;
import net.augustus.cosmetics.molang.functions.LnFunction;
import net.augustus.cosmetics.molang.functions.MaxFunction;
import net.augustus.cosmetics.molang.functions.MinFunction;
import net.augustus.cosmetics.molang.functions.ModFunction;
import net.augustus.cosmetics.molang.functions.MolangFunction;
import net.augustus.cosmetics.molang.functions.PowFunction;
import net.augustus.cosmetics.molang.functions.RandomFunction;
import net.augustus.cosmetics.molang.functions.RoundFunction;
import net.augustus.cosmetics.molang.functions.SinFunction;
import net.augustus.cosmetics.molang.functions.SqrtFunction;
import net.augustus.cosmetics.molang.functions.TruncFunction;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathBuilder {
   public Map functions;
   public Map variables;

   public boolean isOperator2(Object obj) {
      if (obj instanceof String) {
         String str = (String)obj;
         if (this.isOperator(str)) {
            return true;
         }
      }

      return false;
   }

   public boolean isOperator(String str) {
      return MolangOperation.OPERATORS.contains(str) || str.equals("?") || str.equals(":");
   }

   public boolean isVariable(Object obj) {
      if (obj instanceof String) {
         String str = (String)obj;
         if (!this.isDecimal(str)) {
            String str2 = (String)obj;
            if (!this.isOperator(str2)) {
               return true;
            }
         }
      }

      return false;
   }

   public MolangValue createFunction(String str, List list) throws Exception {
      if (str.equals("!")) {
         return new LogicalNotExpression(this.parseSymbols(list));
      } else if (str.startsWith("!") && str.length() > 1) {
         String str2 = str.substring(1);
         return new LogicalNotExpression(this.createFunction(str2, list));
      } else if (str.equals("-")) {
         return new NegateExpression(this.parseSymbols(list));
      } else if (str.startsWith("-") && str.length() > 1) {
         String str3 = str.substring(1);
         return new NegateExpression(this.createFunction(str3, list));
      } else if (!this.functions.containsKey(str)) {
         throw new Exception("Function '" + str + "' couldn't be found!");
      } else {
         ArrayList list2 = new ArrayList();
         ArrayList list3 = new ArrayList();

         for (Object obj : list) {
            if (obj.equals(",")) {
               list2.add(this.parseSymbols(list3));
               list3.clear();
            } else {
               list3.add(obj);
            }
         }

         if (!list3.isEmpty()) {
            list2.add(this.parseSymbols(list3));
         }

         Class clazz = (Class)this.functions.get(str);
         Constructor constructor = clazz.getConstructor(MolangValue[].class, String.class);
         return (MolangFunction)constructor.newInstance(list2.toArray(new MolangValue[list2.size()]), str);
      }
   }

   public boolean isDecimal(String str) {
      return str.matches("^-?\\d+(\\.\\d+)?$");
   }

   public MolangValue valueFromObject(Object obj) throws Exception {
      if (obj instanceof String) {
         String str = (String)obj;
         if (str.startsWith("!")) {
            String str2 = str.substring(1);
            return new LogicalNotExpression(this.valueFromObject((Object)str2));
         }

         if (this.isDecimal(str)) {
            return new ConstantExpression(Double.parseDouble(str));
         }

         if (this.isVariable(str)) {
            if (str.startsWith("-")) {
               str = str.substring(1);
               MolangVariable variable = this.getVariable(str);
               if (variable != null) {
                  return new NegateExpression(variable);
               }
            } else {
               MolangVariable variable2 = this.getVariable(str);
               if (variable2 != null) {
                  return variable2;
               }
            }
         }
      } else if (obj instanceof List) {
         List list = (List)obj;
         return new GroupExpression(this.parseSymbols(list));
      }

      throw new Exception("Given object couldn't be converted to value! " + obj);
   }

   public MathBuilder() {
       super();
      this.variables = new HashMap();
      this.functions = new HashMap();
      MolangVariable molangVariable = new MolangVariable("PI", 3.141592653589793);
      this.register(molangVariable);
      MolangVariable molangVariable2 = new MolangVariable("E", 2.718281828459045);
      this.register(molangVariable2);
      this.functions.put("floor", FloorFunction.class);
      this.functions.put("round", RoundFunction.class);
      this.functions.put("ceil", CeilFunction.class);
      this.functions.put("trunc", TruncFunction.class);
      this.functions.put("clamp", ClampFunction.class);
      this.functions.put("max", MaxFunction.class);
      this.functions.put("min", MinFunction.class);
      this.functions.put("abs", AbsFunction.class);
      this.functions.put("cos", CosFunction.class);
      this.functions.put("sin", SinFunction.class);
      this.functions.put("exp", ExpFunction.class);
      this.functions.put("ln", LnFunction.class);
      this.functions.put("sqrt", SqrtFunction.class);
      this.functions.put("mod", ModFunction.class);
      this.functions.put("pow", PowFunction.class);
      this.functions.put("lerp", LerpFunction.class);
      this.functions.put("lerprotate", LerpRotateFunction.class);
      this.functions.put("random", RandomFunction.class);
   }

   public int seekNextOperator(List list, int n) {
      int n2 = n;

      for (int i = list.size(); n2 < i; n2++) {
         Object obj = list.get(n2);
         if (this.isOperator2(obj)) {
            return n2;
         }
      }

      return -1;
   }

   public MolangVariable getVariable(String str) {
      return (MolangVariable)this.variables.get(str);
   }

   public int seekLastOperator(List list) {
      int n = list.size() - 1;
      return this.seekPreviousOperator(list, n);
   }

   public MolangValue tryTernary(List list) throws Exception {
      int n = -1;
      int n2 = 0;
      int n3 = -1;
      int n4 = 0;
      int size = list.size();

      for (int i = 0; i < size; i++) {
         Object obj = list.get(i);
         if (obj instanceof String) {
            if (obj.equals("?")) {
               if (n == -1) {
                  n = i;
               }

               n2++;
            } else if (obj.equals(":")) {
               if (n4 + 1 == n2 && n3 == -1) {
                  n3 = i;
               }

               n4++;
            }
         }
      }

      if (n2 == n4 && n > 0 && n + 1 < n3 && n3 < size - 1) {
         List list2 = list.subList(0, n);
         MolangValue molangValue = this.parseSymbols(list2);
         List list3 = list.subList(n + 1, n3);
         MolangValue molangValue2 = this.parseSymbols(list3);
         List list4 = list.subList(n3 + 1, size);
         return new TernaryExpression(molangValue, molangValue2, this.parseSymbols(list4));
      } else {
         return null;
      }
   }

   public List breakdown(String[] strings) {
      ArrayList list = new ArrayList();
      String str = "";
      int length = strings.length;

      for (int i = 0; i < length; i++) {
         String str2;
         boolean flag;
         exitBlock: {
            str2 = strings[i];
            if (i > 0) {
               String str3 = strings[i - 1] + str2;
               if (this.isOperator(str3)) {
                  flag = true;
                  break exitBlock;
               }
            }

            flag = false;
         }

         boolean flag2 = flag;
         if (!this.isOperator(str2) && !flag2 && !str2.equals(",")) {
            if (!str2.equals("(")) {
               str = str + str2;
            } else {
               if (!str.isEmpty()) {
                  list.add(str);
                  str = "";
               }

               int n = 1;

               for (int j = i + 1; j < length; j++) {
                  String str4 = strings[j];
                  if (str4.equals("(")) {
                     n++;
                  } else if (str4.equals(")")) {
                     n--;
                  }

                  if (n == 0) {
                     String[] strings2 = str.split("(?!^)");
                     list.add(this.breakdown(strings2));
                     i = j;
                     str = "";
                     break;
                  }

                  str = str + str4;
               }
            }
         } else {
            if (str2.equals("-")) {
               boolean flag3;
               exitBlock2: {
                  int size = list.size();
                  flag3 = size == 0 && str.isEmpty();
                  if (size > 0) {
                     Object obj = list.get(size - 1);
                     Object obj2 = obj;
                     if ((this.isOperator2(obj2) || list.get(size - 1).equals(",")) && str.isEmpty()) {
                        flag = true;
                        break exitBlock2;
                     }
                  }

                  flag = false;
               }

               boolean flag4 = flag;
               if (flag3 || flag4) {
                  str = str + str2;
                  continue;
               }
            }

            if (flag2) {
               str2 = strings[i - 1] + str2;
               str = str.substring(0, str.length() - 1);
            }

            if (!str.isEmpty()) {
               list.add(str);
               str = "";
            }

            list.add(str2);
         }
      }

      if (!str.isEmpty()) {
         list.add(str);
      }

      return list;
   }

   public MolangOperation operationForOperator(String str) throws Exception {
      MolangOperation[] values = MolangOperation.values();

      for (MolangOperation molangOperation : values) {
         if (molangOperation.sign.equals(str)) {
            return molangOperation;
         }
      }

      throw new Exception("There is no such operator '" + str + "'!");
   }

   public String[] breakdownChars(String str) throws Exception {
      if (!str.matches("^[\\w\\d\\s_+-/*%^&|<>=!?:.,()]+$")) {
         throw new Exception("Given expression '" + str + "' contains illegal characters!");
      } else {
         str = str.replaceAll("\\s+", "");
         String[] strings = str.split("(?!^)");
         int n = 0;
         int n2 = 0;

         for (String str2 : strings) {
            if (str2.equals("(")) {
               n++;
            } else if (str2.equals(")")) {
               n2++;
            }
         }

         if (n != n2) {
            throw new Exception("Given expression '" + str + "' has more uneven amount of parenthesis, there are " + n + " open and " + n2 + " closed!");
         } else {
            return strings;
         }
      }
   }

   public int seekFirstOperator(List list) {
      byte b = 0;
      return this.seekNextOperator(list, b);
   }

   public MolangValue parseSymbols(List list) throws Exception {
      MolangValue molangValue = this.tryTernary(list);
      if (molangValue != null) {
         return molangValue;
      } else {
         int size = list.size();
         if (size == 1) {
            Object obj = list.get(0);
            return this.valueFromObject(obj);
         } else {
            if (size == 2) {
               Object obj2 = list.get(0);
               Object obj3 = list.get(1);
               if ((this.isVariable(obj2) || obj2.equals("-")) && obj3 instanceof List) {
                  String str = (String)obj2;
                  List list2 = (List)obj3;
                  String str2 = str;
                  return this.createFunction(str2, list2);
               }
            }

            int n;
            int n2 = n = this.seekLastOperator(list);

            while (n != -1) {
               int n3 = n - 1;
               int n4 = this.seekPreviousOperator(list, n3);
               if (n4 != -1) {
                  String str3 = (String)list.get(n4);
                  String str4 = str3;
                  MolangOperation molangOperation = this.operationForOperator(str4);
                  str3 = (String)list.get(n);
                  String str5 = str3;
                  MolangOperation molangOperation2 = this.operationForOperator(str5);
                  if (molangOperation2.value > molangOperation.value) {
                     List list3 = list.subList(0, n4);
                     MolangValue molangValue2 = this.parseSymbols(list3);
                     List list4 = list.subList(n4 + 1, size);
                     MolangValue molangValue3 = this.parseSymbols(list4);
                     return new BinaryExpression(molangOperation, molangValue2, molangValue3);
                  }

                  if (molangOperation.value > molangOperation2.value) {
                     str3 = (String)list.get(n2);
                     String str6 = str3;
                     MolangOperation molangOperation3 = this.operationForOperator(str6);
                     if (molangOperation3.value < molangOperation.value) {
                        List list5 = list.subList(0, n2);
                        MolangValue molangValue4 = this.parseSymbols(list5);
                        List list6 = list.subList(n2 + 1, size);
                        MolangValue molangValue5 = this.parseSymbols(list6);
                        return new BinaryExpression(molangOperation3, molangValue4, molangValue5);
                     }

                     List list7 = list.subList(0, n);
                     MolangValue molangValue6 = this.parseSymbols(list7);
                     List list8 = list.subList(n + 1, size);
                     MolangValue molangValue7 = this.parseSymbols(list8);
                     return new BinaryExpression(molangOperation2, molangValue6, molangValue7);
                  }
               }

               n = n4;
            }

            String str7 = (String)list.get(n2);
            String str8 = str7;
            MolangOperation molangOperation4 = this.operationForOperator(str8);
            List list9 = list.subList(0, n2);
            MolangValue molangValue8 = this.parseSymbols(list9);
            List list10 = list.subList(n2 + 1, size);
            return new BinaryExpression(molangOperation4, molangValue8, this.parseSymbols(list10));
         }
      }
   }

   public MolangValue parse(String str) throws Exception {
      String[] strings = this.breakdownChars(str);
      List list = this.breakdown(strings);
      return this.parseSymbols(list);
   }

   public int seekPreviousOperator(List list, int n) {

      for (int i = n; i >= 0; i--) {
         Object obj = list.get(i);
         if (this.isOperator2(obj)) {
            return i;
         }
      }

      return -1;
   }

   public void register(MolangVariable molangVariable) {
      this.variables.put(molangVariable.getName(), molangVariable);
   }




}
