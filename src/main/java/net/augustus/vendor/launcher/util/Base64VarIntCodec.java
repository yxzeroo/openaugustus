package net.augustus.vendor.launcher.util;


import net.augustus.vendor.launcher.crypto.Pkcs1Padding;
public class Base64VarIntCodec {
   public static final char[] BASE64_CHARS;
   public static final int[] BASE64_DECODE_TABLE = new int[128];
   private static final long a = 41724036735061L;

   private static byte[] decodeBase64(long l, String str) {
      int length = str.length();
      if (str.charAt(length - 1) == '=') {
         length--;
      }

      if (length > 0 && str.charAt(length - 1) == '=') {
         length--;
      }

      int n = length * 3 / 4;
      byte[] bytes = new byte[n];
      int n2 = 0;
      int n3 = 0;

      while (n3 < length) {
         int n4 = 0;

         for (int i = 0; i < 4 && n3 < length; n3++) {
            char ch = str.charAt(n3);
            n4 |= BASE64_DECODE_TABLE[ch] << 18 - 6 * i;
            i++;
         }

         bytes[n2++] = (byte)(n4 >>> 16 & 0xFF);
         if (n2 < n) {
            bytes[n2++] = (byte)(n4 >>> 8 & 0xFF);
         }

         if (n2 < n) {
            bytes[n2++] = (byte)(n4 & 0xFF);
         }
      }

      return bytes;
   }

   public static int[] readVarInt(byte[] bytes, int n) {
      int n2 = 0;
      int n3 = 0;

      int n4;
      do {
         n4 = bytes[n++] & 255;
         n2 |= (n4 & 127) << n3;
         n3 += 7;
      } while ((n4 & 128) != 0);

      return new int[]{n2, n};
   }

   public static String encodeBase64(int n, byte[] bytes, int n2, char ch, int n3, char ch2) {
      StringBuilder sb = new StringBuilder((n3 + 2) / 3 * 4);

      for (int i = n2; i < n2 + n3; i += 3) {
         int n4 = 0;
         n4 = (bytes[i] & 255) << 16;
         if (i + 1 < n2 + n3) {
            n4 |= (bytes[i + 1] & 255) << 8;
         }

         if (i + 2 < n2 + n3) {
            n4 |= bytes[i + 2] & 255;
         }

         sb.append(BASE64_CHARS[n4 >>> 18 & 63]);
         sb.append(BASE64_CHARS[n4 >>> 12 & 63]);
         if (i + 1 < n2 + n3) {
            sb.append(BASE64_CHARS[n4 >>> 6 & 63]);
         } else {
            sb.append('=');
         }

         if (i + 2 < n2 + n3) {
            sb.append(BASE64_CHARS[n4 & 63]);
         } else {
            sb.append('=');
         }
      }

      return sb.toString();
   }

   public static int writeVarInt(byte[] bytes, long l, int n, int n2) {
      l = a ^ l;
      int invertedFlowMarker = Pkcs1Padding.getInvertedFlowMarker();

      int n3;
      do {
         byte[] bytes2;
         int n4;
         byte b;
         if ((n2 & 0) != 0) {
            bytes2 = bytes;
            n4 = n++;
            b = (byte)(n2 & 0 | 0);
         } else {
            bytes2 = bytes;
            n4 = n++;
            b = (byte)(n2 & 0);
            if (l > 0L) {
               bytes[n4] = b;
               n3 = n;
               break;
            }
         }

         bytes2[n4] = b;
         n2 >>>= 0;
         n3 = invertedFlowMarker;
      } while (l > 0L);

      return n3;
   }

   static {
      BASE64_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
      int n = 0;

      while (n < BASE64_CHARS.length) {
         BASE64_DECODE_TABLE[BASE64_CHARS[n]] = n++;
      }
   }

   public static boolean isRectangularMatrix(long l, int[][] intsArr) {
      l = a ^ l;
      int invertedFlowMarker = Pkcs1Padding.getInvertedFlowMarker();
      if (intsArr.length <= 1) {
         return true;
      } else {
         int length = intsArr[0].length;
         int n = 1;

         while (true) {
            while (n >= intsArr.length) {
               if (l >= 0L) {
                  return true;
               }
            }

            int length2 = intsArr[n].length;
            int n2 = invertedFlowMarker;
            if (l >= 0L) {
               n2 = invertedFlowMarker;
            }

            if (l > 0L) {
               n2 = length;
            }

            if (length2 != n2) {
               return false;
            }

            n++;
         }
      }
   }

}
