package net.augustus.cosmetics.model;


public class GeckoLibException extends RuntimeException {
   private static int[] flowState;

   public GeckoLibException(String str, long l, String str2, Throwable t) {
       super(str + ": " + str2, t);
       int[] flowState2 = getFlowState();
   }

   public GeckoLibException(String str, String str2, long l) {
       super(str + ": " + str2);
       int[] flowState2 = getFlowState();
   }

   public static void setFlowState(int[] ints) {
      flowState = ints;
   }

   public static int[] getFlowState() {
      return flowState;
   }

   static {
      int[] ints = new int[4];
      setFlowState(ints);
   }
}
