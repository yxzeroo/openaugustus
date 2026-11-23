package net.augustus.vendor.launcher.crypto;


import net.augustus.vendor.launcher.AesCrypto;
public class AesCipherUtil {
   private static final char[] HEX_CHARS;
   private static final long a = 31075419399341L;

   static {
      String str = "0123456789ABCDEF";
      HEX_CHARS = str.toCharArray();
   }

   public static byte[] encrypt(byte[] bytes, byte[] bytes2) {
      if (bytes == null) {
         return null;
      } else {
         AesCrypto aesCrypto = new AesCrypto();
         boolean flag = true;
         aesCrypto.init(flag, bytes2);
         int length = bytes.length;
         byte b = 0;
         return aesCrypto.decrypt(bytes, b, length);
      }
   }

   public static byte[] decrypt(byte[] bytes, byte[] bytes2) {
      AesCrypto aesCrypto = new AesCrypto();
      boolean flag = false;
      aesCrypto.init(flag, bytes2);
      int length = bytes.length;
      byte b = 0;
      return aesCrypto.encrypt(bytes, b, length);
   }

   public static synchronized byte[] encrypt(byte[] bytes, byte[] bytes2, AesCrypto aesCrypto) {
      int[] flowState = Sha256Digest.getFlowState();
      if (bytes == null) {
         return null;
      } else {
         boolean flag = true;
         aesCrypto.init(flag, bytes2);
         int length = bytes.length;
         byte b = 0;
         byte[] bytes3 = aesCrypto.decrypt(bytes, b, length);
         aesCrypto.resetIv();
         return bytes3;
      }
   }

   public static byte[] concatByteArrays(long l, byte[][] bytesArr) {
      l = a ^ l;
      int n = 0;
      Sha256Digest.getFlowState();

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
      Sha256Digest.getFlowState();

      for (int i = 0; i < bytes.length; i++) {
         int n = bytes[i] & 255;
         chars[i * 2] = HEX_CHARS[n >>> 4];
         chars[i * 2 + 1] = HEX_CHARS[n & 15];
      }

      return new String(chars);
   }

   public static synchronized byte[] decrypt(byte[] bytes, byte[] bytes2, AesCrypto aesCrypto) {
      boolean flag = false;
      aesCrypto.init(flag, bytes2);
      int length = bytes.length;
      byte b = 0;
      byte[] bytes3 = aesCrypto.encrypt(bytes, b, length);
      aesCrypto.resetIv();
      return bytes3;
   }

}
