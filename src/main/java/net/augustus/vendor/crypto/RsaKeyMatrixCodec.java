package net.augustus.vendor.crypto;


public class RsaKeyMatrixCodec {
   public static final int[] BASE64_DECODE_TABLE = new int[128];
   public static final char[] BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

   private static byte[] decodeBase64(String str, long l) {
      Object obj = null;
      int n = 0;
      int n2 = 0;
      char ch = '\u0000';
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      boolean flag = false;
      n2 = str.length();
      if (str.charAt(n2 - 1) == '=') {
         n2--;
      }

      if (n2 > 0 && str.charAt(n2 - 1) == '=') {
         n2--;
      }

      n = n2 * 3 / 4;
      obj = new byte[n];
      n3 = 0;
      n4 = 0;

      while (n4 < n2) {
         n5 = 0;

         for (int i = 0; i < 4 && n4 < n2; n4++) {
            ch = str.charAt(n4);
            n5 |= BASE64_DECODE_TABLE[ch] << 18 - 6 * i;
            i++;
         }

         ((byte[])obj)[n3++] = (byte)(n5 >>> 16 & 0xFF);
         if (n3 < n) {
            ((byte[])obj)[n3++] = (byte)(n5 >>> 8 & 0xFF);
         }

         if (n3 < n) {
            ((byte[])obj)[n3++] = (byte)(n5 & 0xFF);
         }
      }

      return (byte[])obj;
   }

   public static int[] readVarInt(byte[] bytes, int n) {
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      n3 = 0;
      n4 = 0;

      do {
         n2 = bytes[n++] & 255;
         n3 |= (n2 & 127) << n4;
         n4 += 7;
      } while ((n2 & 128) != 0);

      return new int[]{n3, n};
   }

   public static boolean isRectangularMatrix(int[][] intsArr, long l) {
      boolean flag = false;
      int n = 0;
      if (intsArr.length <= 1) {
         return true;
      } else {
         n = intsArr[0].length;

         for (int i = 1; i < ((int[][])intsArr).length; i++) {
            if (intsArr[i].length != n) {
               return false;
            }
         }

         return true;
      }
   }

   public static int writeVarInt(byte[] bytes, int n, long l, int n2) {

      while ((n2 & -128) != 0) {
         bytes[n++] = (byte)(n2 & 127 | 128);
         n2 >>>= 7;
      }

      bytes[n++] = (byte)(n2 & 127);
      return n;
   }

   static {
      int n = 0;

      while (n < BASE64_ALPHABET.length) {
         BASE64_DECODE_TABLE[BASE64_ALPHABET[n]] = n++;
      }
   }

   public static String encodeBase64(long l, byte[] bytes, int n, int n2, char ch) {
      int n3 = 0;
      boolean flag = false;
      Object obj = null;
      obj = new StringBuilder((n2 + 2) / 3 * 4);

      for (int i = n; i < n + n2; i += 3) {
         n3 = (bytes[i] & 255) << 16;
         if (i + 1 < n + n2) {
            n3 |= (((byte[])bytes)[i + 1] & 255) << 8;
         }

         if (i + 2 < n + n2) {
            n3 |= ((byte[])bytes)[i + 2] & 255;
         }

         ((StringBuilder)obj).append(BASE64_ALPHABET[n3 >>> 18 & 63]);
         ((StringBuilder)obj).append(BASE64_ALPHABET[n3 >>> 12 & 63]);
         if (i + 1 >= n + n2) {
            ((StringBuilder)obj).append('=');
         } else {
            ((StringBuilder)obj).append(BASE64_ALPHABET[n3 >>> 6 & 63]);
         }

         if (i + 2 >= n + n2) {
            ((StringBuilder)obj).append('=');
         } else {
            ((StringBuilder)obj).append(BASE64_ALPHABET[n3 & 63]);
         }
      }

      return ((StringBuilder)obj).toString();
   }

}
