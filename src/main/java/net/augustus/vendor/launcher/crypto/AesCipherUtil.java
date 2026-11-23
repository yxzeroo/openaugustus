package net.augustus.vendor.launcher.crypto;


import net.augustus.vendor.launcher.AesCrypto;
public class AesCipherUtil {
   private static final char[] HEX_CHARS;
   private static final long a = 31075419399341L;

   static {
      HEX_CHARS = "0123456789ABCDEF".toCharArray();
   }

   public static byte[] encrypt(byte[] bytes, byte[] bytes2) {
      if (bytes == null) {
         return null;
      } else {
         AesCrypto aesCrypto = new AesCrypto();
         aesCrypto.init(true, bytes2);
         return aesCrypto.decrypt(bytes, (byte)0, bytes.length);
      }
   }

   public static byte[] decrypt(byte[] bytes, byte[] bytes2) {
      AesCrypto aesCrypto = new AesCrypto();
      aesCrypto.init(false, bytes2);
      return aesCrypto.encrypt(bytes, (byte)0, bytes.length);
   }

   public static synchronized byte[] encrypt(byte[] bytes, byte[] bytes2, AesCrypto aesCrypto) {
      if (bytes == null) {
         return null;
      } else {
         aesCrypto.init(true, bytes2);
         byte[] bytes3 = aesCrypto.decrypt(bytes, (byte)0, bytes.length);
         aesCrypto.resetIv();
         return bytes3;
      }
   }

   public static byte[] concatByteArrays(long l, byte[][] bytesArr) {
      l = a ^ l;
      int n = 0;

      for (byte[] bytes : bytesArr) {
         if (l > 0L && bytes != null) {
            n += bytes.length;
         }
      }

      byte[] bytes2 = new byte[n];
      int n2 = 0;

      for (byte[] bytes3 : bytesArr) {
         if (l > 0L && bytes3 != null && bytes3.length != 0) {
            System.arraycopy(bytes3, 0, bytes2, n2, bytes3.length);
            n2 += bytes3.length;
         }
      }

      return bytes2;
   }

   public static String bytesToHex(byte[] bytes) {
      char[] chars = new char[bytes.length * 2];

      for (int i = 0; i < bytes.length; i++) {
         int n = bytes[i] & 255;
         chars[i * 2] = HEX_CHARS[n >>> 4];
         chars[i * 2 + 1] = HEX_CHARS[n & 15];
      }

      return new String(chars);
   }

   public static synchronized byte[] decrypt(byte[] bytes, byte[] bytes2, AesCrypto aesCrypto) {
      aesCrypto.init(false, bytes2);
      byte[] bytes3 = aesCrypto.encrypt(bytes, (byte)0, bytes.length);
      aesCrypto.resetIv();
      return bytes3;
   }

}
