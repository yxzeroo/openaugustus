package net.augustus.vendor.launcher.crypto;


public class Pkcs1Padding {
   private static int flowState;

   public static byte[] unpad(byte[] bytes) {
      int invertedFlowMarker = getInvertedFlowMarker();
      if (bytes.length >= 11 && bytes[0] == 0 && bytes[1] == 2) {
         int n = -1;

         for (int i = 2; i < bytes.length; i++) {
            if (bytes[i] == 0) {
               n = i;
               break;
            }
         }

         if (n < 10) {
            throw new IllegalArgumentException("Invalid PKCS#1 padding");
         } else {
            int n2 = bytes.length - n - 1;
            byte[] bytes2 = new byte[n2];
            System.arraycopy(bytes, n + 1, bytes2, 0, n2);
            return bytes2;
         }
      } else {
         throw new IllegalArgumentException("Invalid PKCS#1 padding");
      }
   }

   public static byte[] pad(byte[] bytes, int n) {
      int flowState2 = getFlowState();
      if (bytes.length > n - 11) {
         throw new IllegalArgumentException("Data too large for block size");
      } else {
         byte[] bytes2 = new byte[n];
         bytes2[0] = 0;
         bytes2[1] = 2;
         EntropyXorShiftRandom entropyXorShiftRandom = new EntropyXorShiftRandom();
         int n2 = n - bytes.length - 3;

         for (int i = 0; i < n2; i++) {
            byte b;
            do {
               byte[] bytes3 = new byte[1];
               entropyXorShiftRandom.nextBytes(bytes3);
               b = bytes3[0];
            } while (b == 0);

            bytes2[i + 2] = b;
         }

         bytes2[n2 + 2] = 0;
         System.arraycopy(bytes, 0, bytes2, n2 + 3, bytes.length);
         return bytes2;
      }
   }

   public static void setFlowState(int n) {
      flowState = n;
   }

   public static int getFlowState() {
      return flowState;
   }

   public static int getInvertedFlowMarker() {
      int flowState2 = getFlowState();
      return flowState2 == 0 ? 98 : 0;
   }

   static {
      setFlowState(0);
   }

}
