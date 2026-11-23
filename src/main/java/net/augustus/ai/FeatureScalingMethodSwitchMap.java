package net.augustus.ai;

public class FeatureScalingMethodSwitchMap {
   public static final int[] ORDINAL_MAP = new int[FeatureScalingMethod.values().length];

   static {
      try {
         ORDINAL_MAP[FeatureScalingMethod.MINMAX.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[FeatureScalingMethod.DIVIDE.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[FeatureScalingMethod.CLAMP.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         ORDINAL_MAP[FeatureScalingMethod.IDENTITY.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }
   }
}
