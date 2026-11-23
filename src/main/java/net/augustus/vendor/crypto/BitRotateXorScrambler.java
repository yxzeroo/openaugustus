package net.augustus.vendor.crypto;

public class BitRotateXorScrambler {
   private static final byte INITIAL_XOR_KEY = -98;
   private static final int HIGH_BYTE_ROTATE_LEFT = 3;
   private static final byte POSITION_XOR_BASE = 93;
   private static final byte FINAL_XOR_KEY = -61;
   private static final int LOW_BYTE_ROTATE_RIGHT = 2;

   public static byte[] scramble(byte[] bytes) {
      byte[] bytes2 = (byte[])bytes.clone();

      for (int i = 0; i < bytes2.length; i++) {
         byte b = bytes2[i];
         b = (byte)(b ^ -98);
         b = (byte)(b ^ 93 - (byte)(i % 16));
         if ((b & 255) > 128) {
            b = (byte)(b << 3 | b >>> 5);
         } else {
            b = (byte)(b >>> 2 | b << 6);
         }

         b = (byte)(b ^ -61);
         bytes2[i] = b;
      }

      return NibbleSortByteMixer.mix(bytes2);
   }


}
