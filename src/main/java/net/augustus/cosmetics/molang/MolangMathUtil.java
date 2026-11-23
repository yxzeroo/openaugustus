package net.augustus.cosmetics.molang;

public class MolangMathUtil {
   public static int clamp2(int n, long l, int n2, int n3) {
      int[] flowState = MolangInterpolations.getFlowState();
      return n < n2 ? n2 : (n > n3 ? n3 : n);
   }

   public static float clamp(float f, float f2, long l, float f3) {
      int[] flowState = MolangInterpolations.getFlowState();
      return f < f2 ? f2 : (f > f3 ? f3 : f);
   }

   public static double clamp(double d, double d2, double d3) {
      int[] flowState = MolangInterpolations.getFlowState();
      return d < d2 ? d2 : (d > d3 ? d3 : d);
   }
}
