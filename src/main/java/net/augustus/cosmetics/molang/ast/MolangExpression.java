package net.augustus.cosmetics.molang.ast;

import net.augustus.cosmetics.molang.MolangOperation;
import net.augustus.cosmetics.molang.MolangParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public abstract class MolangExpression implements MolangValue {
   public MolangParser context;

   public static boolean isConstant(MolangExpression molangExpression, long l) {
      if (molangExpression instanceof MolangValueExpression) {
         MolangValueExpression molangValueExpression = (MolangValueExpression)molangExpression;
         return molangValueExpression.value instanceof ConstantExpression;
      } else {
         return false;
      }
   }

   public MolangExpression(MolangParser molangParser) {
      this.context = molangParser;
   }

   public static boolean isOne(MolangExpression molangExpression) {
      double d = 1.0;
      return isExpressionConstant(molangExpression, d);
   }

   public static boolean isExpressionConstant(MolangExpression molangExpression, double d) {
      if (!(molangExpression instanceof MolangValueExpression)) {
         return false;
      } else {
         MolangValueExpression molangValueExpression = (MolangValueExpression)molangExpression;
         return molangValueExpression.value instanceof ConstantExpression && MolangOperation.equals(molangValueExpression.value.get(), d);
      }
   }

   public JsonElement toJson() {
      return new JsonPrimitive(this.toString());
   }

   public static boolean isZero(MolangExpression molangExpression) {
      double d = 0.0;
      return isExpressionConstant(molangExpression, d);
   }



}
