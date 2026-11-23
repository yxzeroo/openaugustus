package net.augustus.vendor.crypto;


import net.augustus.vendor.crypto.aes.PaddedAesCipher;
public class VendorAesHelper {
   private static final char[] HEX_CHARS;
   private static int guardFlag;

   public static String bytesToHex(short s, short s2, byte[] bytes, int n) {
      char[] chars = new char[bytes.length * 2];

      for (int i = 0; i < bytes.length; i++) {
         int n2 = bytes[i] & 255;
         chars[i * 2] = HEX_CHARS[n2 >>> 4];
         chars[i * 2 + 1] = HEX_CHARS[n2 & 15];
      }

      return new String(chars);
   }

   public static synchronized byte[] decrypt(byte[] bytes, byte[] bytes2) {
      PaddedAesCipher paddedAesCipher = new PaddedAesCipher();
      paddedAesCipher.init(true, bytes2);
      return paddedAesCipher.decrypt(bytes, 0, bytes.length);
   }

   public static synchronized byte[] encryptWithCipher(byte[] bytes, byte[] bytes2, PaddedAesCipher paddedAesCipher) {
      paddedAesCipher.init(false, bytes2);
      byte[] bytes3 = paddedAesCipher.encrypt(bytes, 0, bytes.length);
      paddedAesCipher.resetIv();
      return bytes3;
   }

   public static synchronized byte[] decryptWithCipher(byte[] bytes, byte[] bytes2, PaddedAesCipher paddedAesCipher) {
      paddedAesCipher.init(true, bytes2);
      byte[] bytes3 = paddedAesCipher.decrypt(bytes, 0, bytes.length);
      paddedAesCipher.resetIv();
      return bytes3;
   }

   public static synchronized byte[] encrypt(byte[] bytes, byte[] bytes2) {
      PaddedAesCipher paddedAesCipher = new PaddedAesCipher();
      paddedAesCipher.init(false, bytes2);
      return paddedAesCipher.encrypt(bytes, 0, bytes.length);
   }

   public static void setGuardFlag(int n) {
      guardFlag = n;
   }

   public static int getGuardFlag() {
      return guardFlag;
   }

   public static int getInvertedGuardFlag() {
      int guardFlag2 = getGuardFlag();
      return guardFlag2 == 0 ? 19 : 0;
   }

   static {
      setGuardFlag(0);
      HEX_CHARS = "0123456789ABCDEF".toCharArray();
   }
}
