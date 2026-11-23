package net.augustus.render;

public class CubeFaceAxisSwitchMap {
   public static final int[] ORDINAL_MAP = new int[CubeFaceAxis.values().length];

   static {
      try {
         ORDINAL_MAP[CubeFaceAxis.Z.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[CubeFaceAxis.X.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[CubeFaceAxis.Y.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }
   }
}
