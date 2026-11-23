package net.optifine.shaders.uniform;

public class ShaderParameterFloatSwitchMap {
   public static final int[] ORDINAL_MAP = new int[ShaderParameterFloat.values().length];

   static {
      try {
         ORDINAL_MAP[ShaderParameterFloat.BIOME.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[ShaderParameterFloat.TEMPERATURE.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[ShaderParameterFloat.RAINFALL.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }
   }
}
