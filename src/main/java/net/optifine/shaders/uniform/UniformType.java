package net.optifine.shaders.uniform;

import net.optifine.expr.IExpressionFloatArray;
import net.optifine.expr.ExpressionType;
import net.optifine.expr.IExpression;
import net.optifine.expr.IExpressionBool;
import net.optifine.expr.IExpressionFloat;

public enum UniformType {
   BOOL,
   INT,
   FLOAT,
   VEC2,
   VEC3,
   VEC4;

public static UniformType parse(String str) {
      UniformType[] values = values();

      for (int i = 0; i < values.length; i++) {
         UniformType uniformType = values[i];
         if (uniformType.name().toLowerCase().equals(str)) {
            return uniformType;
         }
      }

      return null;
   }

   private void updateUniformBool(IExpressionBool expressionBool, ShaderUniform1i shaderUniform1i) {
      shaderUniform1i.setValue(expressionBool.eval() ? 1 : 0);
   }

   public void updateUniform(IExpression expression, ShaderUniformBase shaderUniformBase) {
      switch (UniformType.UniformTypeSwitchMap.ORDINAL_MAP[this.ordinal()]) {
         case 1:
            this.updateUniformBool((IExpressionBool)expression, (ShaderUniform1i)shaderUniformBase);
            return;
         case 2:
            this.updateUniformInt((IExpressionFloat)expression, (ShaderUniform1i)shaderUniformBase);
            return;
         case 3:
            this.updateUniformFloat((IExpressionFloat)expression, (ShaderUniform1f)shaderUniformBase);
            return;
         case 4:
            this.updateUniformFloat2((IExpressionFloatArray)expression, (ShaderUniform2f)shaderUniformBase);
            return;
         case 5:
            this.updateUniformFloat3((IExpressionFloatArray)expression, (ShaderUniform3f)shaderUniformBase);
            return;
         case 6:
            this.updateUniformFloat4((IExpressionFloatArray)expression, (ShaderUniform4f)shaderUniformBase);
            return;
         default:
            throw new RuntimeException("Unknown uniform type: " + this);
      }
   }

   public boolean matchesExpressionType(ExpressionType expressionType) {
      switch (UniformType.UniformTypeSwitchMap.ORDINAL_MAP[this.ordinal()]) {
         case 1:
            return expressionType == ExpressionType.BOOL;
         case 2:
            return expressionType == ExpressionType.FLOAT;
         case 3:
            return expressionType == ExpressionType.FLOAT;
         case 4:
         case 5:
         case 6:
            return expressionType == ExpressionType.FLOAT_ARRAY;
         default:
            throw new RuntimeException("Unknown uniform type: " + this);
      }
   }

   private void updateUniformInt(IExpressionFloat expressionFloat, ShaderUniform1i shaderUniform1i) {
      shaderUniform1i.setValue((int)expressionFloat.eval());
   }

   private void updateUniformFloat2(IExpressionFloatArray expressionFloatArray, ShaderUniform2f shaderUniform2f) {
      float[] floats = expressionFloatArray.eval();
      if (floats.length != 2) {
         throw new RuntimeException("Value length is not 2, length: " + floats.length);
      } else {
         shaderUniform2f.setValue(floats[0], floats[1]);
      }
   }

   private void updateUniformFloat4(IExpressionFloatArray expressionFloatArray, ShaderUniform4f shaderUniform4f) {
      float[] floats = expressionFloatArray.eval();
      if (floats.length != 4) {
         throw new RuntimeException("Value length is not 4, length: " + floats.length);
      } else {
         shaderUniform4f.setValue(floats[0], floats[1], floats[2], floats[3]);
      }
   }

   private void updateUniformFloat(IExpressionFloat expressionFloat, ShaderUniform1f shaderUniform1f) {
      shaderUniform1f.setValue(expressionFloat.eval());
   }

   public ShaderUniformBase makeShaderUniform(String str) {
      switch (UniformType.UniformTypeSwitchMap.ORDINAL_MAP[this.ordinal()]) {
         case 1:
            return new ShaderUniform1i(str);
         case 2:
            return new ShaderUniform1i(str);
         case 3:
            return new ShaderUniform1f(str);
         case 4:
            return new ShaderUniform2f(str);
         case 5:
            return new ShaderUniform3f(str);
         case 6:
            return new ShaderUniform4f(str);
         default:
            throw new RuntimeException("Unknown uniform type: " + this);
      }
   }

   private void updateUniformFloat3(IExpressionFloatArray expressionFloatArray, ShaderUniform3f shaderUniform3f) {
      float[] floats = expressionFloatArray.eval();
      if (floats.length != 3) {
         throw new RuntimeException("Value length is not 3, length: " + floats.length);
      } else {
         shaderUniform3f.setValue(floats[0], floats[1], floats[2]);
      }
   }


   public static class UniformTypeSwitchMap {
      public static final int[] ORDINAL_MAP = new int[UniformType.values().length];

      static {
         try {
            ORDINAL_MAP[UniformType.BOOL.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }

         try {
            ORDINAL_MAP[UniformType.INT.ordinal()] = 2;
         } catch (NoSuchFieldError e2) {
         }

         try {
            ORDINAL_MAP[UniformType.FLOAT.ordinal()] = 3;
         } catch (NoSuchFieldError e3) {
         }

         try {
            ORDINAL_MAP[UniformType.VEC2.ordinal()] = 4;
         } catch (NoSuchFieldError e4) {
         }

         try {
            ORDINAL_MAP[UniformType.VEC3.ordinal()] = 5;
         } catch (NoSuchFieldError e5) {
         }

         try {
            ORDINAL_MAP[UniformType.VEC4.ordinal()] = 6;
         } catch (NoSuchFieldError e6) {
         }
      }
   }
}
