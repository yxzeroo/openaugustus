package net.optifine.expr;

import net.optifine.shaders.uniform.Smoother;

public class FunctionFloat implements IExpressionFloat {
   private FunctionType type;
   private IExpression[] arguments;
   private int smoothId = -1;

   public FunctionFloat(FunctionType functionType, IExpression[] expressions) {
      this.type = functionType;
      this.arguments = expressions;
   }

   @Override
   public float eval() {
      IExpression[] arguments2 = this.arguments;
      switch (FunctionFloat.FunctionTypeSwitchMap.ORDINAL_MAP[this.type.ordinal()]) {
         case 1:
            IExpression expression = arguments2[0];
            if (!(expression instanceof ConstantFloat)) {
               float f = evalFloat(arguments2, 0);
               float f2 = arguments2.length > 1 ? evalFloat(arguments2, 1) : 1.0F;
               float f3 = arguments2.length > 2 ? evalFloat(arguments2, 2) : f2;
               if (this.smoothId < 0) {
                  this.smoothId = Smoother.getNextId();
               }

               return Smoother.getSmoothValue(this.smoothId, f, f2, f3);
            }
         default:
            return this.type.evalFloat(this.arguments);
      }
   }

   @Override
   public ExpressionType getFloatValue() {
      return ExpressionType.FLOAT;
   }

   private static float evalFloat(IExpression[] expressions, int n) {
      IExpressionFloat expressionFloat = (IExpressionFloat)expressions[n];
      return expressionFloat.eval();
   }

   @Override
   public String toString() {
      return "" + this.type + "()";
   }


   public static class FunctionTypeSwitchMap {
      public static final int[] ORDINAL_MAP = new int[FunctionType.values().length];

      static {
         try {
            ORDINAL_MAP[FunctionType.SMOOTH.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }
      }
   }
}
