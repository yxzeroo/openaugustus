package net.augustus.cosmetics.model;

public enum GeoPlayState {
   STOP,

   CONTINUE;

private static int[] flowState;

   static {
      if (getFlowState() == null) {
         setFlowState(new int[3]);
      }
   }



   public static void setFlowState(int[] ints) {
      flowState = ints;
   }

   public static int[] getFlowState() {
      return flowState;
   }
}
