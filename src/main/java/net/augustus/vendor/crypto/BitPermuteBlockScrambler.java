package net.augustus.vendor.crypto;

public class BitPermuteBlockScrambler {
   private static final byte SECONDARY_XOR_BASE = 17;
   private static final int BLOCK_SIZE = 4;
   private static final int[] BIT_PERMUTATION = new int[]{3, 0, 4, 1, 5, 2, 6, 7};
   private static final byte PRIMARY_XOR_MASK = -18;

   public static byte[] scramble(byte[] bytes) {
      byte[] bytes2 = (byte[])bytes.clone();

      for (int i = 0; i < bytes2.length; i += 4) {
         int n = i;
         int n2 = Math.min(i + 4 - 1, bytes2.length - 1);

         while (n < n2) {
            byte b = bytes2[n];
            bytes2[n] = bytes2[n2];
            bytes2[n2] = b;
            n++;
            n2--;
         }
      }

      for (int j = 0; j < bytes2.length; j++) {
         byte b2 = bytes2[j];
         byte b3 = 0;

         for (int k = 0; k < 8; k++) {
            if ((b2 >> k & 1) != 0) {
               b3 = (byte)(b3 | 1 << BIT_PERMUTATION[k]);
            }
         }

         b3 = (byte)(b3 ^ -18);
         b3 = (byte)(b3 ^ 17 + (byte)(b2 & 15));
         bytes2[j] = b3;
      }

      return bytes2;
   }
}
