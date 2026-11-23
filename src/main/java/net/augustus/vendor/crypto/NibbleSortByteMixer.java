package net.augustus.vendor.crypto;

public class NibbleSortByteMixer {
   private static final byte CASE_ZERO_XOR_MASK = -91;
   private static final byte FINAL_OFFSET_KEY = 119;
   private static final byte[] NIBBLE_XOR_TABLE = new byte[]{26, 43, 60, 77, 94, 111, 112, -127, -110, -93, -76, -59, -42, -25, -8, 9};
   private static final byte CASE_ONE_REPLACEMENT = 60;

   public static byte[] mix(byte[] bytes) {
      byte[] bytes2 = (byte[])bytes.clone();

      for (int i = 0; i < bytes2.length - 1; i += 2) {
         if ((bytes2[i] & 15) > (bytes2[i + 1] & 15)) {
            byte b = bytes2[i];
            bytes2[i] = bytes2[i + 1];
            bytes2[i + 1] = b;
         }
      }

      for (int j = 0; j < bytes2.length; ++j) {
         byte b2 = bytes2[j];
         switch (j % 3) {
            case 0:
               b2 = (byte)(~(b2 ^ CASE_ZERO_XOR_MASK));
               break;
            case 1:
               b2 = CASE_ONE_REPLACEMENT;
               break;
            default:
               b2 = (byte)(b2 ^ NIBBLE_XOR_TABLE[b2 & 15]);
               break;
         }

         bytes2[j] = (byte)(b2 ^ (FINAL_OFFSET_KEY + j));
      }

      return BitPermuteBlockScrambler.scramble(bytes2);
   }
}
