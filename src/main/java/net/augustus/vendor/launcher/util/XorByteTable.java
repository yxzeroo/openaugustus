package net.augustus.vendor.launcher.util;

public class XorByteTable {
   public static byte[] TABLE;
   private static int[] controlFlowMarker;

   public void populate() {
      TABLE = new byte[256];
      for (int i = 0; i < 256; i++) {
         TABLE[i] = (byte)(i ^ 1);
      }
   }

   public static void setControlFlowMarker(int[] ints) {
      controlFlowMarker = ints;
   }

   public static int[] getControlFlowMarker() {
      return controlFlowMarker;
   }

   static {
      setControlFlowMarker(new int[1]);
   }
}
