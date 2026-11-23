package net.augustus.vendor.launcher.crypto;

import net.augustus.vendor.launcher.crypto.math.ShadedBigInteger;
import net.augustus.vendor.launcher.util.Base64Codec;
import net.augustus.vendor.launcher.util.Base64Decoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class HybridRsaCipher {
   private static final byte MODE_DIRECT_RSA = 1;
   private static int DEFAULT_KEY_BITS;
   private static int MAX_DIRECT_DATA_LENGTH;
   private static final ShadedBigInteger PUBLIC_EXPONENT;
   public static final ShadedBigInteger TWO;
   private static int PRIME_CERTAINTY;
   private static byte MODE_HYBRID;

   private static byte[] encryptSymmetricKey(byte[] bytes, ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2) throws Exception {
      int n = (shadedBigInteger.bitLength() + 7) / 8;

      try {
         return bigIntegerToBytes(bytesToBigInteger(Pkcs1Padding.pad(bytes, n)).modPow(shadedBigInteger2, shadedBigInteger), n);
      } catch (Exception e) {
         throw new Exception("Symmetric key encryption failed: " + e.getMessage());
      }
   }

   private static ShadedBigInteger bytesToBigInteger(byte[] bytes) {
      return new ShadedBigInteger(1, bytes);
   }

   public static String exportKeyPairToBase64(RsaCrtKeyPair rsaCrtKeyPair) {

      try {
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         writeBigInteger(byteArrayOutputStream, rsaCrtKeyPair.getModulus());
         writeBigInteger(byteArrayOutputStream, rsaCrtKeyPair.getPublicExponent());
         writeBigInteger(byteArrayOutputStream, rsaCrtKeyPair.getPrivateExponent());
         writeBigInteger(byteArrayOutputStream, rsaCrtKeyPair.getPrimeP());
         writeBigInteger(byteArrayOutputStream, rsaCrtKeyPair.getPrimeQ());
         byte[] bytes = byteArrayOutputStream.toByteArray();
         byteArrayOutputStream.close();
         return Base64Codec.getRfc46482().encodeToString(bytes);
      } catch (Exception e) {
         throw new RuntimeException("Failed to export key pair: " + e.getMessage());
      }
   }

   public static byte[] exportPublicKeyToBytes(RsaCrtKeyPair rsaCrtKeyPair) {

      try {
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         writeBigInteger(byteArrayOutputStream, rsaCrtKeyPair.getModulus());
         writeBigInteger(byteArrayOutputStream, rsaCrtKeyPair.getPublicExponent());
         byte[] bytes = byteArrayOutputStream.toByteArray();
         byteArrayOutputStream.close();
         return bytes;
      } catch (Exception e) {
         throw new RuntimeException("Failed to export public key: " + e.getMessage());
      }
   }

   public static byte[] encryptWithPrivateKey(byte[] bytes, RsaCrtKeyPair rsaCrtKeyPair) throws Exception {
      int n = (rsaCrtKeyPair.getModulus().bitLength() + 7) / 8;
      if (bytes.length > 245) {
         throw new Exception("Data to encrypt with private key too long");
      } else {
         try {
            ShadedBigInteger shadedBigInteger = bytesToBigInteger(Pkcs1Padding.pad(bytes, n));
            ShadedBigInteger shadedBigInteger3 = shadedBigInteger.modPow(rsaCrtKeyPair.getPrimeExponentP(), rsaCrtKeyPair.getPrimeP());
            ShadedBigInteger shadedBigInteger5 = shadedBigInteger.modPow(rsaCrtKeyPair.getPrimeExponentQ(), rsaCrtKeyPair.getPrimeQ());
            ShadedBigInteger crtCoefficient = rsaCrtKeyPair.getCrtCoefficient();
            crtCoefficient = crtCoefficient.multiply(shadedBigInteger3.subtract(shadedBigInteger5).mod(rsaCrtKeyPair.getPrimeP()));
            ShadedBigInteger shadedBigInteger13 = shadedBigInteger5.add(crtCoefficient.mod(rsaCrtKeyPair.getPrimeP()).multiply(rsaCrtKeyPair.getPrimeQ()));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(1);
            byteArrayOutputStream.write(bigIntegerToBytes(shadedBigInteger13, n));
            return byteArrayOutputStream.toByteArray();
         } catch (Exception e) {
            throw new Exception("Private key encryption failed: " + e.getMessage());
         }
      }
   }

   public static byte[] decrypt(byte[] bytes, ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2) throws Exception {
      return decryptWithPublicKey(bytes, shadedBigInteger, shadedBigInteger2);
   }

   public static byte[] encryptWithPublicKey(byte[] bytes, ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2) {
      int n = (shadedBigInteger.bitLength() + 7) / 8;
      byte[] bytes2 = Pkcs1Padding.pad(bytes, n);
      if (bytes2 == null) {
         return new byte[1];
      } else {
         byte[] bytes3 = bigIntegerToBytes(bytesToBigInteger(bytes2).modPow(shadedBigInteger2, shadedBigInteger), n);
         ByteBuffer buf = ByteBuffer.allocate(1 + bytes3.length);
         buf.put((byte)1);
         buf.put(bytes3);
         return buf.array();
      }
   }

   private static byte[] bigIntegerToBytes(ShadedBigInteger shadedBigInteger, int n) {
      byte[] bytes = shadedBigInteger.toByteArray();
      if (bytes[0] == 0 && bytes.length > n) {
         byte[] bytes2 = new byte[bytes.length - 1];
         System.arraycopy(bytes, 1, bytes2, 0, bytes2.length);
         bytes = bytes2;
      }

      if (bytes.length < n) {
         byte[] bytes3 = new byte[n];
         System.arraycopy(bytes, 0, bytes3, n - bytes.length, bytes.length);
         bytes = bytes3;
      }

      return bytes;
   }

   public static ShadedBigInteger[] importPublicKeyFromBase64(String str) {

      try {
         ByteBuffer buf = ByteBuffer.wrap(Base64Codec.getRfc4648().decode3(str));
         ShadedBigInteger shadedBigInteger = readBigInteger(buf);
         ShadedBigInteger shadedBigInteger2 = readBigInteger(buf);
         return new ShadedBigInteger[]{shadedBigInteger, shadedBigInteger2};
      } catch (Exception e) {
         throw new RuntimeException("Failed to import public key: " + e.getMessage());
      }
   }

   public static byte[] decryptWithPrivateKey(byte[] bytes, RsaCrtKeyPair rsaCrtKeyPair) throws Exception {
      if (bytes.length < 2) {
         throw new Exception("Invalid encrypted data format");
      } else {
         byte b = bytes[0];
         if (b == 1) {
            byte[] bytes2 = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, bytes2, 0, bytes2.length);
            int n = (rsaCrtKeyPair.getModulus().bitLength() + 7) / 8;

            try {
               ShadedBigInteger shadedBigInteger = bytesToBigInteger(bytes2);
               ShadedBigInteger shadedBigInteger3 = shadedBigInteger.modPow(rsaCrtKeyPair.getPrimeExponentP(), rsaCrtKeyPair.getPrimeP());
               ShadedBigInteger shadedBigInteger5 = shadedBigInteger.modPow(rsaCrtKeyPair.getPrimeExponentQ(), rsaCrtKeyPair.getPrimeQ());
               ShadedBigInteger crtCoefficient = rsaCrtKeyPair.getCrtCoefficient();
               crtCoefficient = crtCoefficient.multiply(shadedBigInteger3.subtract(shadedBigInteger5).mod(rsaCrtKeyPair.getPrimeP()));
               return Pkcs1Padding.unpad(bigIntegerToBytes(shadedBigInteger5.add(crtCoefficient.mod(rsaCrtKeyPair.getPrimeP()).multiply(rsaCrtKeyPair.getPrimeQ())), n));
            } catch (Exception e) {
               throw new Exception("Decryption failed: " + e.getMessage());
            }
         } else if (b == 2) {
            ByteBuffer buf = ByteBuffer.wrap(bytes);
            buf.get();
            byte[] bytes4 = new byte[(buf.get() & 255) << 8 | buf.get() & 255];
            buf.get(bytes4);
            byte[] bytes5 = new byte[buf.remaining()];
            buf.get(bytes5);
            byte[] bytes6 = new byte[bytes4.length + 1];
            bytes6[0] = 1;
            System.arraycopy(bytes4, 0, bytes6, 1, bytes4.length);
            return SymmetricStreamCipher.decrypt(bytes5, decryptWithPrivateKey(bytes6, rsaCrtKeyPair));
         } else {
            throw new Exception("Unknown encryption mode: " + b);
         }
      }
   }

   public static byte[] exportKeyPairToBytes(RsaCrtKeyPair rsaCrtKeyPair) {

      try {
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         writeBigInteger(byteArrayOutputStream, rsaCrtKeyPair.getModulus());
         writeBigInteger(byteArrayOutputStream, rsaCrtKeyPair.getPublicExponent());
         writeBigInteger(byteArrayOutputStream, rsaCrtKeyPair.getPrivateExponent());
         writeBigInteger(byteArrayOutputStream, rsaCrtKeyPair.getPrimeP());
         writeBigInteger(byteArrayOutputStream, rsaCrtKeyPair.getPrimeQ());
         byte[] bytes = byteArrayOutputStream.toByteArray();
         byteArrayOutputStream.close();
         return bytes;
      } catch (Exception e) {
         throw new RuntimeException("Failed to export key pair: " + e.getMessage());
      }
   }

   public static byte[] encrypt(byte[] bytes, ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2) throws Exception {
      return encryptWithPublicKey(bytes, shadedBigInteger, shadedBigInteger2);
   }

   public static boolean verifySignature(byte[] bytes, byte[] bytes2, ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2) throws Exception {

      try {
         byte[] bytes3 = decrypt(bytes, shadedBigInteger, shadedBigInteger2);
         if (bytes3.length != bytes2.length) {
            return false;
         } else {
            for (int i = 0; i < bytes3.length; i++) {
               if (bytes3[i] != bytes2[i]) {
                  return false;
               }
            }

            return true;
         }
      } catch (Exception e) {
         return false;
      }
   }

   public static RsaCrtKeyPair importKeyPairFromBase64(String str) {

      try {
         ByteBuffer buf = ByteBuffer.wrap(Base64Codec.getRfc4648().decode3(str));
         ShadedBigInteger shadedBigInteger = readBigInteger(buf);
         ShadedBigInteger shadedBigInteger2 = readBigInteger(buf);
         ShadedBigInteger shadedBigInteger3 = readBigInteger(buf);
         ShadedBigInteger shadedBigInteger4 = readBigInteger(buf);
         ShadedBigInteger shadedBigInteger5 = readBigInteger(buf);
         return new RsaCrtKeyPair(shadedBigInteger, shadedBigInteger2, shadedBigInteger3, shadedBigInteger4, shadedBigInteger5);
      } catch (Exception e) {
         throw new RuntimeException("Failed to import key pair: " + e.getMessage());
      }
   }

   private static ShadedBigInteger readBigInteger(ByteBuffer buf) {
      byte[] bytes = new byte[(buf.get() & 255) << 24 | (buf.get() & 255) << 16 | (buf.get() & 255) << 8 | buf.get() & 255];
      buf.get(bytes);
      return new ShadedBigInteger(bytes);
   }

   static {
      long[] longs = new long[]{2L, 65537L};
      TWO = ShadedBigInteger.valueOf(longs[0]);
      PUBLIC_EXPONENT = ShadedBigInteger.valueOf(longs[1]);
   }

   public static byte[] sign(byte[] bytes, RsaCrtKeyPair rsaCrtKeyPair) throws Exception {
      return encryptWithPrivateKey(bytes, rsaCrtKeyPair);
   }

   public static ShadedBigInteger[] importPublicKeyFromBytes(byte[] bytes) {

      try {
         ByteBuffer buf = ByteBuffer.wrap(bytes);
         ShadedBigInteger shadedBigInteger = readBigInteger(buf);
         ShadedBigInteger shadedBigInteger2 = readBigInteger(buf);
         return new ShadedBigInteger[]{shadedBigInteger, shadedBigInteger2};
      } catch (Exception e) {
         throw new RuntimeException("Failed to import public key: " + e.getMessage());
      }
   }

   public static byte[] decrypt(byte[] bytes, RsaCrtKeyPair rsaCrtKeyPair) throws Exception {
      return decryptWithPrivateKey(bytes, rsaCrtKeyPair);
   }

   public static String exportPublicKeyToBase64(RsaCrtKeyPair rsaCrtKeyPair) {

      try {
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         writeBigInteger(byteArrayOutputStream, rsaCrtKeyPair.getModulus());
         writeBigInteger(byteArrayOutputStream, rsaCrtKeyPair.getPublicExponent());
         byte[] bytes = byteArrayOutputStream.toByteArray();
         byteArrayOutputStream.close();
         return Base64Codec.getRfc46482().encodeToString(bytes);
      } catch (Exception e) {
         throw new RuntimeException("Failed to export public key: " + e.getMessage());
      }
   }

   public static RsaCrtKeyPair importKeyPairFromBytes(byte[] bytes) {

      try {
         ByteBuffer buf = ByteBuffer.wrap(bytes);
         ShadedBigInteger shadedBigInteger = readBigInteger(buf);
         ShadedBigInteger shadedBigInteger2 = readBigInteger(buf);
         ShadedBigInteger shadedBigInteger3 = readBigInteger(buf);
         ShadedBigInteger shadedBigInteger4 = readBigInteger(buf);
         ShadedBigInteger shadedBigInteger5 = readBigInteger(buf);
         return new RsaCrtKeyPair(shadedBigInteger, shadedBigInteger2, shadedBigInteger3, shadedBigInteger4, shadedBigInteger5);
      } catch (Exception e) {
         throw new RuntimeException("Failed to import key pair: " + e.getMessage());
      }
   }

   private static void writeBigInteger(ByteArrayOutputStream byteArrayOutputStream, ShadedBigInteger shadedBigInteger) throws IOException {
      byte[] bytes = shadedBigInteger.toByteArray();
      byteArrayOutputStream.write(bytes.length >>> 24);
      byteArrayOutputStream.write(bytes.length >>> 16);
      byteArrayOutputStream.write(bytes.length >>> 8);
      byteArrayOutputStream.write(bytes.length);
      byteArrayOutputStream.write(bytes);
   }

   public static byte[] decryptWithPublicKey(byte[] bytes, ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2) throws Exception {
      if (bytes.length < 2) {
         throw new Exception("Invalid encrypted data format");
      } else {
         if (bytes[0] == 1) {
            byte[] bytes2 = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, bytes2, 0, bytes2.length);
            int n = (shadedBigInteger.bitLength() + 7) / 8;
            return Pkcs1Padding.unpad(bigIntegerToBytes(bytesToBigInteger(bytes2).modPow(shadedBigInteger2, shadedBigInteger), n));
         } else {
            throw new Exception("Hybrid encryption not supported for public key decryption");
         }
      }
   }

}
