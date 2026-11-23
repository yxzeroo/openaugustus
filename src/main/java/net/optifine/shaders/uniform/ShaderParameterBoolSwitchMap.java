package net.optifine.shaders.uniform;

public class ShaderParameterBoolSwitchMap {
   public static final int[] ORDINAL_MAP = new int[ShaderParameterBool.values().length];

   static {
      try {
         ORDINAL_MAP[ShaderParameterBool.IS_ALIVE.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[ShaderParameterBool.IS_BURNING.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[ShaderParameterBool.IS_CHILD.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         ORDINAL_MAP[ShaderParameterBool.IS_HURT.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }

      try {
         ORDINAL_MAP[ShaderParameterBool.IS_IN_LAVA.ordinal()] = 5;
      } catch (NoSuchFieldError e5) {
      }

      try {
         ORDINAL_MAP[ShaderParameterBool.IS_IN_WATER.ordinal()] = 6;
      } catch (NoSuchFieldError e6) {
      }

      try {
         ORDINAL_MAP[ShaderParameterBool.IS_INVISIBLE.ordinal()] = 7;
      } catch (NoSuchFieldError e7) {
      }

      try {
         ORDINAL_MAP[ShaderParameterBool.IS_ON_GROUND.ordinal()] = 8;
      } catch (NoSuchFieldError e8) {
      }

      try {
         ORDINAL_MAP[ShaderParameterBool.IS_RIDDEN.ordinal()] = 9;
      } catch (NoSuchFieldError e9) {
      }

      try {
         ORDINAL_MAP[ShaderParameterBool.IS_RIDING.ordinal()] = 10;
      } catch (NoSuchFieldError e10) {
      }

      try {
         ORDINAL_MAP[ShaderParameterBool.IS_SNEAKING.ordinal()] = 11;
      } catch (NoSuchFieldError e11) {
      }

      try {
         ORDINAL_MAP[ShaderParameterBool.IS_SPRINTING.ordinal()] = 12;
      } catch (NoSuchFieldError e12) {
      }

      try {
         ORDINAL_MAP[ShaderParameterBool.IS_WET.ordinal()] = 13;
      } catch (NoSuchFieldError e13) {
      }
   }
}
