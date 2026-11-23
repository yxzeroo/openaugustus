package net.augustus.render.waveycape;

public class CapeMathHelper {
   public static float lerp2(float f, float f2, float f3) {
      return f2 + f * (f3 - f2);
   }

   public static float fastInvSqrt(float f) {
      float f2 = 0.5F * f;
      int n = Float.floatToIntBits(f);
      n = 1597463007 - (n >> 1);
      f = Float.intBitsToFloat(n);
      return f * (1.5F - f2 * f * f);
   }

   public static float fastInvCubeRoot(float f) {
      int n = Float.floatToIntBits(f);
      n = 1419967116 - n / 3;
      float f2 = Float.intBitsToFloat(n);
      f2 = 0.6666667F * f2 + 0.33333334F * f2 * f2 * f;
      return 0.6666667F * f2 + 0.33333334F * f2 * f2 * f;
   }

   public static double lerp(double d, double d2, double d3) {
      return d2 + d * (d3 - d2);
   }
}
