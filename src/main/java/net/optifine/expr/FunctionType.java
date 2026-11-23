package net.optifine.expr;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MathHelper;
import net.optifine.config.Config;
import net.optifine.shaders.uniform.Smoother;
import net.optifine.util.MathUtils;

public enum FunctionType {
   PLUS(10, ExpressionType.FLOAT, "+", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   MINUS(10, ExpressionType.FLOAT, "-", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   MUL(11, ExpressionType.FLOAT, "*", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   DIV(11, ExpressionType.FLOAT, "/", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   MOD(11, ExpressionType.FLOAT, "%", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   NEG(12, ExpressionType.FLOAT, "neg", new ExpressionType[]{ExpressionType.FLOAT}),
   PI(ExpressionType.FLOAT, "pi", new ExpressionType[0]),
   SIN(ExpressionType.FLOAT, "sin", new ExpressionType[]{ExpressionType.FLOAT}),
   COS(ExpressionType.FLOAT, "cos", new ExpressionType[]{ExpressionType.FLOAT}),
   ASIN(ExpressionType.FLOAT, "asin", new ExpressionType[]{ExpressionType.FLOAT}),
   ACOS(ExpressionType.FLOAT, "acos", new ExpressionType[]{ExpressionType.FLOAT}),
   TAN(ExpressionType.FLOAT, "tan", new ExpressionType[]{ExpressionType.FLOAT}),
   ATAN(ExpressionType.FLOAT, "atan", new ExpressionType[]{ExpressionType.FLOAT}),
   ATAN2(ExpressionType.FLOAT, "atan2", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   TORAD(ExpressionType.FLOAT, "torad", new ExpressionType[]{ExpressionType.FLOAT}),
   TODEG(ExpressionType.FLOAT, "todeg", new ExpressionType[]{ExpressionType.FLOAT}),
   MIN(ExpressionType.FLOAT, "min", new ParametersVariable().first(new ExpressionType[]{ExpressionType.FLOAT}).repeat(new ExpressionType[]{ExpressionType.FLOAT})),
   MAX(ExpressionType.FLOAT, "max", new ParametersVariable().first(new ExpressionType[]{ExpressionType.FLOAT}).repeat(new ExpressionType[]{ExpressionType.FLOAT})),
   CLAMP(ExpressionType.FLOAT, "clamp", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT}),
   ABS(ExpressionType.FLOAT, "abs", new ExpressionType[]{ExpressionType.FLOAT}),
   FLOOR(ExpressionType.FLOAT, "floor", new ExpressionType[]{ExpressionType.FLOAT}),
   CEIL(ExpressionType.FLOAT, "ceil", new ExpressionType[]{ExpressionType.FLOAT}),
   EXP(ExpressionType.FLOAT, "exp", new ExpressionType[]{ExpressionType.FLOAT}),
   FRAC(ExpressionType.FLOAT, "frac", new ExpressionType[]{ExpressionType.FLOAT}),
   LOG(ExpressionType.FLOAT, "log", new ExpressionType[]{ExpressionType.FLOAT}),
   POW(ExpressionType.FLOAT, "pow", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   RANDOM(ExpressionType.FLOAT, "random", new ExpressionType[0]),
   ROUND(ExpressionType.FLOAT, "round", new ExpressionType[]{ExpressionType.FLOAT}),
   SIGNUM(ExpressionType.FLOAT, "signum", new ExpressionType[]{ExpressionType.FLOAT}),
   SQRT(ExpressionType.FLOAT, "sqrt", new ExpressionType[]{ExpressionType.FLOAT}),
   FMOD(ExpressionType.FLOAT, "fmod", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   TIME(ExpressionType.FLOAT, "time", new ExpressionType[0]),
   IF(ExpressionType.FLOAT, "if", new ParametersVariable()
         .first(new ExpressionType[]{ExpressionType.BOOL, ExpressionType.FLOAT})
         .repeat(new ExpressionType[]{ExpressionType.BOOL, ExpressionType.FLOAT})
         .last(new ExpressionType[]{ExpressionType.FLOAT})),
   NOT(12, ExpressionType.BOOL, "!", new ExpressionType[]{ExpressionType.BOOL}),
   AND(3, ExpressionType.BOOL, "&&", new ExpressionType[]{ExpressionType.BOOL, ExpressionType.BOOL}),
   OR(2, ExpressionType.BOOL, "||", new ExpressionType[]{ExpressionType.BOOL, ExpressionType.BOOL}),
   GREATER(8, ExpressionType.BOOL, ">", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   GREATER_OR_EQUAL(8, ExpressionType.BOOL, ">=", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   SMALLER(8, ExpressionType.BOOL, "<", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   SMALLER_OR_EQUAL(8, ExpressionType.BOOL, "<=", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   EQUAL(7, ExpressionType.BOOL, "==", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   NOT_EQUAL(7, ExpressionType.BOOL, "!=", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   BETWEEN(7, ExpressionType.BOOL, "between", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT}),
   EQUALS(7, ExpressionType.BOOL, "equals", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT}),
   IN(ExpressionType.BOOL, "in", new ParametersVariable()
         .first(new ExpressionType[]{ExpressionType.FLOAT})
         .repeat(new ExpressionType[]{ExpressionType.FLOAT})
         .last(new ExpressionType[]{ExpressionType.FLOAT})),
   SMOOTH(ExpressionType.FLOAT, "smooth", new ParametersVariable().first(new ExpressionType[]{ExpressionType.FLOAT}).repeat(new ExpressionType[]{ExpressionType.FLOAT}).maxCount(4)),
   TRUE(ExpressionType.BOOL, "true", new ExpressionType[0]),
   FALSE(ExpressionType.BOOL, "false", new ExpressionType[0]),
   VEC2(ExpressionType.FLOAT_ARRAY, "vec2", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   VEC3(ExpressionType.FLOAT_ARRAY, "vec3", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT}),
   VEC4(ExpressionType.FLOAT_ARRAY, "vec4", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT});

private int precedence;
   private ExpressionType expressionType;
   private String name;
   private IParameters parameters;
   public static FunctionType[] VALUES = values();
   private static final Map mapSmooth = new HashMap();
public int getParameterCount(IExpression[] expressions) {
      return this.parameters.getParameterTypes(expressions).length;
   }

   public ExpressionType getExpressionType() {
      return this.expressionType;
   }

   public IParameters getParameters() {
      return this.parameters;
   }

   private static float evalFloat(IExpression[] expressions, int n) {
      IExpressionFloat expressionFloat = (IExpressionFloat)expressions[n];
      return expressionFloat.eval();
   }

   private float getMax(IExpression[] expressions) {
      if (expressions.length == 2) {
         return Math.max(evalFloat(expressions, 0), evalFloat(expressions, 1));
      } else {
         float f = evalFloat(expressions, 0);

         for (int i = 1; i < expressions.length; i++) {
            float f2 = evalFloat(expressions, i);
            if (f2 > f) {
               f = f2;
            }
         }

         return f;
      }
   }

   public int getPrecedence() {
      return this.precedence;
   }

   public static FunctionType parse(String str) {
      for (int i = 0; i < VALUES.length; i++) {
         FunctionType functionType = VALUES[i];
         if (functionType.getName().equals(str)) {
            return functionType;
         }
      }

      return null;
   }

   private FunctionType(int n, ExpressionType expressionType2, String str, IParameters parameters2) {
      this.precedence = n;
      this.expressionType = expressionType2;
      this.name = str;
      this.parameters = parameters2;
   }

   private float getMin(IExpression[] expressions) {
      if (expressions.length == 2) {
         return Math.min(evalFloat(expressions, 0), evalFloat(expressions, 1));
      } else {
         float f = evalFloat(expressions, 0);

         for (int i = 1; i < expressions.length; i++) {
            float f2 = evalFloat(expressions, i);
            if (f2 < f) {
               f = f2;
            }
         }

         return f;
      }
   }

   private FunctionType(ExpressionType expressionType2, String str, IParameters parameters2) {
      this(0, expressionType2, str, parameters2);
   }

   public float evalFloat(IExpression[] expressions) {
      switch (FunctionType.FunctionTypeSwitchMap.ORDINAL_MAP[this.ordinal()]) {
         case 1:
            return evalFloat(expressions, 0) + evalFloat(expressions, 1);
         case 2:
            return evalFloat(expressions, 0) - evalFloat(expressions, 1);
         case 3:
            return evalFloat(expressions, 0) * evalFloat(expressions, 1);
         case 4:
            return evalFloat(expressions, 0) / evalFloat(expressions, 1);
         case 5:
            float f = evalFloat(expressions, 0);
            float f2 = evalFloat(expressions, 1);
            return f - f2 * (float)((int)(f / f2));
         case 6:
            return -evalFloat(expressions, 0);
         case 7:
            return (float) Math.PI;
         case 8:
            return MathHelper.sin(evalFloat(expressions, 0));
         case 9:
            return MathHelper.cos(evalFloat(expressions, 0));
         case 10:
            return MathUtils.asin(evalFloat(expressions, 0));
         case 11:
            return MathUtils.acos(evalFloat(expressions, 0));
         case 12:
            return (float)Math.tan((double)evalFloat(expressions, 0));
         case 13:
            return (float)Math.atan((double)evalFloat(expressions, 0));
         case 14:
            return (float)MathHelper.atan2((double)evalFloat(expressions, 0), (double)evalFloat(expressions, 1));
         case 15:
            return MathUtils.toRad(evalFloat(expressions, 0));
         case 16:
            return MathUtils.toDeg(evalFloat(expressions, 0));
         case 17:
            return this.getMin(expressions);
         case 18:
            return this.getMax(expressions);
         case 19:
            return MathHelper.clamp_float(evalFloat(expressions, 0), evalFloat(expressions, 1), evalFloat(expressions, 2));
         case 20:
            return MathHelper.abs(evalFloat(expressions, 0));
         case 21:
            return (float)Math.exp((double)evalFloat(expressions, 0));
         case 22:
            return (float)MathHelper.floor_float(evalFloat(expressions, 0));
         case 23:
            return (float)MathHelper.ceiling_float_int(evalFloat(expressions, 0));
         case 24:
            return (float)MathHelper.frac((double)evalFloat(expressions, 0));
         case 25:
            return (float)Math.log((double)evalFloat(expressions, 0));
         case 26:
            return (float)Math.pow((double)evalFloat(expressions, 0), (double)evalFloat(expressions, 1));
         case 27:
            return (float)Math.random();
         case 28:
            return (float)Math.round(evalFloat(expressions, 0));
         case 29:
            return Math.signum(evalFloat(expressions, 0));
         case 30:
            return MathHelper.sqrt_float(evalFloat(expressions, 0));
         case 31:
            float f3 = evalFloat(expressions, 0);
            float f4 = evalFloat(expressions, 1);
            return f3 - f4 * (float)MathHelper.floor_float(f3 / f4);
         case 32:
            Minecraft minecraft = Minecraft.getMinecraft();
            WorldClient theWorld = minecraft.theWorld;
            if (theWorld == null) {
               return 0.0F;
            }

            return (float)(theWorld.getTotalWorldTime() % 24000L) + Config.renderPartialTicks;
         case 33:
            int n = (expressions.length - 1) / 2;

            for (int i = 0; i < n; i++) {
               int n2 = i * 2;
               if (evalBool(expressions, n2)) {
                  return evalFloat(expressions, n2 + 1);
               }
            }

            return evalFloat(expressions, n * 2);
         case 34:
            int n3 = (int)evalFloat(expressions, 0);
            float f5 = evalFloat(expressions, 1);
            float f6 = expressions.length > 2 ? evalFloat(expressions, 2) : 1.0F;
            float f7 = expressions.length > 3 ? evalFloat(expressions, 3) : f6;
            return Smoother.getSmoothValue(n3, f5, f6, f7);
         default:
            Config.warn("Unknown function type: " + this);
            return 0.0F;
      }
   }

   public String getName() {
      return this.name;
   }

   public ExpressionType[] getParameterTypes(IExpression[] expressions) {
      return this.parameters.getParameterTypes(expressions);
   }

   private FunctionType(ExpressionType expressionType2, String str, ExpressionType[] expressionTypes) {
      this(0, expressionType2, str, expressionTypes);
   }

   public float[] evalFloatArray(IExpression[] expressions) {
      switch (FunctionType.FunctionTypeSwitchMap.ORDINAL_MAP[this.ordinal()]) {
         case 49:
            return new float[]{evalFloat(expressions, 0), evalFloat(expressions, 1)};
         case 50:
            return new float[]{evalFloat(expressions, 0), evalFloat(expressions, 1), evalFloat(expressions, 2)};
         case 51:
            return new float[]{evalFloat(expressions, 0), evalFloat(expressions, 1), evalFloat(expressions, 2), evalFloat(expressions, 3)};
         default:
            Config.warn("Unknown function type: " + this);
            return null;
      }
   }

   public boolean evalBool(IExpression[] expressions) {
      switch (FunctionType.FunctionTypeSwitchMap.ORDINAL_MAP[this.ordinal()]) {
         case 35:
            return true;
         case 36:
            return false;
         case 37:
            return !evalBool(expressions, 0);
         case 38:
            return evalBool(expressions, 0) && evalBool(expressions, 1);
         case 39:
            return evalBool(expressions, 0) || evalBool(expressions, 1);
         case 40:
            return evalFloat(expressions, 0) > evalFloat(expressions, 1);
         case 41:
            return evalFloat(expressions, 0) >= evalFloat(expressions, 1);
         case 42:
            return evalFloat(expressions, 0) < evalFloat(expressions, 1);
         case 43:
            return evalFloat(expressions, 0) <= evalFloat(expressions, 1);
         case 44:
            return evalFloat(expressions, 0) == evalFloat(expressions, 1);
         case 45:
            return evalFloat(expressions, 0) != evalFloat(expressions, 1);
         case 46:
            float f = evalFloat(expressions, 0);
            return f >= evalFloat(expressions, 1) && f <= evalFloat(expressions, 2);
         case 47:
            float f2 = evalFloat(expressions, 0) - evalFloat(expressions, 1);
            float f3 = evalFloat(expressions, 2);
            return Math.abs(f2) <= f3;
         case 48:
            float f4 = evalFloat(expressions, 0);

            for (int i = 1; i < expressions.length; i++) {
               float f5 = evalFloat(expressions, i);
               if (f4 == f5) {
                  return true;
               }
            }

            return false;
         default:
            Config.warn("Unknown function type: " + this);
            return false;
      }
   }

   private FunctionType(int n, ExpressionType expressionType2, String str, ExpressionType[] expressionTypes) {
      this(n, expressionType2, str, new Parameters(expressionTypes));
   }

   private static boolean evalBool(IExpression[] expressions, int n) {
      IExpressionBool expressionBool = (IExpressionBool)expressions[n];
      return expressionBool.eval();
   }


   public static class FunctionTypeSwitchMap {
      public static final int[] ORDINAL_MAP = new int[FunctionType.values().length];

      static {
         try {
            ORDINAL_MAP[FunctionType.PLUS.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }

         try {
            ORDINAL_MAP[FunctionType.MINUS.ordinal()] = 2;
         } catch (NoSuchFieldError e2) {
         }

         try {
            ORDINAL_MAP[FunctionType.MUL.ordinal()] = 3;
         } catch (NoSuchFieldError e3) {
         }

         try {
            ORDINAL_MAP[FunctionType.DIV.ordinal()] = 4;
         } catch (NoSuchFieldError e4) {
         }

         try {
            ORDINAL_MAP[FunctionType.MOD.ordinal()] = 5;
         } catch (NoSuchFieldError e5) {
         }

         try {
            ORDINAL_MAP[FunctionType.NEG.ordinal()] = 6;
         } catch (NoSuchFieldError e6) {
         }

         try {
            ORDINAL_MAP[FunctionType.PI.ordinal()] = 7;
         } catch (NoSuchFieldError e7) {
         }

         try {
            ORDINAL_MAP[FunctionType.SIN.ordinal()] = 8;
         } catch (NoSuchFieldError e8) {
         }

         try {
            ORDINAL_MAP[FunctionType.COS.ordinal()] = 9;
         } catch (NoSuchFieldError e9) {
         }

         try {
            ORDINAL_MAP[FunctionType.ASIN.ordinal()] = 10;
         } catch (NoSuchFieldError e10) {
         }

         try {
            ORDINAL_MAP[FunctionType.ACOS.ordinal()] = 11;
         } catch (NoSuchFieldError e11) {
         }

         try {
            ORDINAL_MAP[FunctionType.TAN.ordinal()] = 12;
         } catch (NoSuchFieldError e12) {
         }

         try {
            ORDINAL_MAP[FunctionType.ATAN.ordinal()] = 13;
         } catch (NoSuchFieldError e13) {
         }

         try {
            ORDINAL_MAP[FunctionType.ATAN2.ordinal()] = 14;
         } catch (NoSuchFieldError e14) {
         }

         try {
            ORDINAL_MAP[FunctionType.TORAD.ordinal()] = 15;
         } catch (NoSuchFieldError e15) {
         }

         try {
            ORDINAL_MAP[FunctionType.TODEG.ordinal()] = 16;
         } catch (NoSuchFieldError e16) {
         }

         try {
            ORDINAL_MAP[FunctionType.MIN.ordinal()] = 17;
         } catch (NoSuchFieldError e17) {
         }

         try {
            ORDINAL_MAP[FunctionType.MAX.ordinal()] = 18;
         } catch (NoSuchFieldError e18) {
         }

         try {
            ORDINAL_MAP[FunctionType.CLAMP.ordinal()] = 19;
         } catch (NoSuchFieldError e19) {
         }

         try {
            ORDINAL_MAP[FunctionType.ABS.ordinal()] = 20;
         } catch (NoSuchFieldError e20) {
         }

         try {
            ORDINAL_MAP[FunctionType.EXP.ordinal()] = 21;
         } catch (NoSuchFieldError e21) {
         }

         try {
            ORDINAL_MAP[FunctionType.FLOOR.ordinal()] = 22;
         } catch (NoSuchFieldError e22) {
         }

         try {
            ORDINAL_MAP[FunctionType.CEIL.ordinal()] = 23;
         } catch (NoSuchFieldError e23) {
         }

         try {
            ORDINAL_MAP[FunctionType.FRAC.ordinal()] = 24;
         } catch (NoSuchFieldError e24) {
         }

         try {
            ORDINAL_MAP[FunctionType.LOG.ordinal()] = 25;
         } catch (NoSuchFieldError e25) {
         }

         try {
            ORDINAL_MAP[FunctionType.POW.ordinal()] = 26;
         } catch (NoSuchFieldError e26) {
         }

         try {
            ORDINAL_MAP[FunctionType.RANDOM.ordinal()] = 27;
         } catch (NoSuchFieldError e27) {
         }

         try {
            ORDINAL_MAP[FunctionType.ROUND.ordinal()] = 28;
         } catch (NoSuchFieldError e28) {
         }

         try {
            ORDINAL_MAP[FunctionType.SIGNUM.ordinal()] = 29;
         } catch (NoSuchFieldError e29) {
         }

         try {
            ORDINAL_MAP[FunctionType.SQRT.ordinal()] = 30;
         } catch (NoSuchFieldError e30) {
         }

         try {
            ORDINAL_MAP[FunctionType.FMOD.ordinal()] = 31;
         } catch (NoSuchFieldError e31) {
         }

         try {
            ORDINAL_MAP[FunctionType.TIME.ordinal()] = 32;
         } catch (NoSuchFieldError e32) {
         }

         try {
            ORDINAL_MAP[FunctionType.IF.ordinal()] = 33;
         } catch (NoSuchFieldError e33) {
         }

         try {
            ORDINAL_MAP[FunctionType.SMOOTH.ordinal()] = 34;
         } catch (NoSuchFieldError e34) {
         }

         try {
            ORDINAL_MAP[FunctionType.TRUE.ordinal()] = 35;
         } catch (NoSuchFieldError e35) {
         }

         try {
            ORDINAL_MAP[FunctionType.FALSE.ordinal()] = 36;
         } catch (NoSuchFieldError e36) {
         }

         try {
            ORDINAL_MAP[FunctionType.NOT.ordinal()] = 37;
         } catch (NoSuchFieldError e37) {
         }

         try {
            ORDINAL_MAP[FunctionType.AND.ordinal()] = 38;
         } catch (NoSuchFieldError e38) {
         }

         try {
            ORDINAL_MAP[FunctionType.OR.ordinal()] = 39;
         } catch (NoSuchFieldError e39) {
         }

         try {
            ORDINAL_MAP[FunctionType.GREATER.ordinal()] = 40;
         } catch (NoSuchFieldError e40) {
         }

         try {
            ORDINAL_MAP[FunctionType.GREATER_OR_EQUAL.ordinal()] = 41;
         } catch (NoSuchFieldError e41) {
         }

         try {
            ORDINAL_MAP[FunctionType.SMALLER.ordinal()] = 42;
         } catch (NoSuchFieldError e42) {
         }

         try {
            ORDINAL_MAP[FunctionType.SMALLER_OR_EQUAL.ordinal()] = 43;
         } catch (NoSuchFieldError e43) {
         }

         try {
            ORDINAL_MAP[FunctionType.EQUAL.ordinal()] = 44;
         } catch (NoSuchFieldError e44) {
         }

         try {
            ORDINAL_MAP[FunctionType.NOT_EQUAL.ordinal()] = 45;
         } catch (NoSuchFieldError e45) {
         }

         try {
            ORDINAL_MAP[FunctionType.BETWEEN.ordinal()] = 46;
         } catch (NoSuchFieldError e46) {
         }

         try {
            ORDINAL_MAP[FunctionType.EQUALS.ordinal()] = 47;
         } catch (NoSuchFieldError e47) {
         }

         try {
            ORDINAL_MAP[FunctionType.IN.ordinal()] = 48;
         } catch (NoSuchFieldError e48) {
         }

         try {
            ORDINAL_MAP[FunctionType.VEC2.ordinal()] = 49;
         } catch (NoSuchFieldError e49) {
         }

         try {
            ORDINAL_MAP[FunctionType.VEC3.ordinal()] = 50;
         } catch (NoSuchFieldError e50) {
         }

         try {
            ORDINAL_MAP[FunctionType.VEC4.ordinal()] = 51;
         } catch (NoSuchFieldError e51) {
         }
      }
   }
}
