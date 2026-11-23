package net.optifine.expr;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestExpressions {
   public static void main(String[] args) throws Exception {
      ExpressionParser expressionParser = new ExpressionParser((IExpressionResolver)null);

      while (true) {
         try {
            InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String str = reader.readLine();
            if (str.length() <= 0) {
               return;
            }

            IExpression expression = expressionParser.parse(str);
            if (expression instanceof IExpressionFloat) {
               IExpressionFloat expressionFloat = (IExpressionFloat)expression;
               float f = expressionFloat.eval();
               System.out.println("" + f);
            }

            if (expression instanceof IExpressionBool) {
               IExpressionBool expressionBool = (IExpressionBool)expression;
               boolean flag = expressionBool.eval();
               System.out.println("" + flag);
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }
}
