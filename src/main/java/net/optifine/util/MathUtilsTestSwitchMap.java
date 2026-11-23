package net.optifine.util;

public class MathUtilsTestSwitchMap {
   public static final int[] ORDINAL_MAP = new int[MathUtilsTestOperation.values().length];

   static {
      try {
         ORDINAL_MAP[MathUtilsTestOperation.SIN.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[MathUtilsTestOperation.COS.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[MathUtilsTestOperation.ASIN.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         ORDINAL_MAP[MathUtilsTestOperation.ACOS.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }
   }
}
