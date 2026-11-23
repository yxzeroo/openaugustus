package net.augustus.vendor.crypto.rsa;

import net.augustus.vendor.crypto.CustomBase64;
import net.augustus.vendor.crypto.CustomBigInteger;
import net.augustus.vendor.crypto.HybridSymmetricCipher;
import net.augustus.vendor.crypto.Pkcs1Padding;
import net.augustus.vendor.crypto.RsaCrtKeyPair;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class HybridRsaCipher {
   private static  int HYBRID_AES_KEY_SIZE;
   private static final CustomBigInteger DEFAULT_PUBLIC_EXPONENT;
   private static  byte MODE_HYBRID;
   private static  int MAX_DIRECT_BLOCK_SIZE;
   private static  int DEFAULT_KEY_BIT_SIZE;
   public static final CustomBigInteger TWO;
   private static final byte MODE_DIRECT = 1;

   public static byte[] exportPublicKeyBytes(RsaCrtKeyPair rsaCrtKeyPair) {

      try {
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         CustomBigInteger modulus = rsaCrtKeyPair.getModulus();
         writeBigInteger(byteArrayOutputStream, modulus);
         CustomBigInteger publicExponent = rsaCrtKeyPair.getPublicExponent();
         writeBigInteger(byteArrayOutputStream, publicExponent);
         byte[] bytes = byteArrayOutputStream.toByteArray();
         byteArrayOutputStream.close();
         return bytes;
      } catch (Exception e) {
         throw new RuntimeException("Failed to export public key: " + e.getMessage());
      }
   }

   private static byte[] encryptSymmetricKey(byte[] bytes, CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2) throws Exception {
      int n = (customBigInteger.bitLength() + 7) / 8;

      try {
         byte[] bytes2 = Pkcs1Padding.pad(bytes, n);
         CustomBigInteger customBigInteger3 = bytesToPositiveBigInteger(bytes2);
         CustomBigInteger customBigInteger4 = customBigInteger3.modPow(customBigInteger2, customBigInteger);
         return bigIntegerToFixedBytes(customBigInteger4, n);
      } catch (Exception e) {
         throw new Exception("Symmetric key encryption failed: " + e.getMessage());
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
               CustomBigInteger customBigInteger = bytesToPositiveBigInteger(bytes2);
               CustomBigInteger primeP = rsaCrtKeyPair.getPrimeP();
               CustomBigInteger primeExponentP = rsaCrtKeyPair.getPrimeExponentP();
               CustomBigInteger customBigInteger2 = customBigInteger.modPow(primeExponentP, primeP);
               CustomBigInteger primeQ = rsaCrtKeyPair.getPrimeQ();
               CustomBigInteger primeExponentQ = rsaCrtKeyPair.getPrimeExponentQ();
               CustomBigInteger customBigInteger3 = customBigInteger.modPow(primeExponentQ, primeQ);
               CustomBigInteger crtCoefficient = rsaCrtKeyPair.getCrtCoefficient();
               CustomBigInteger customBigInteger4 = customBigInteger2.subtract(customBigInteger3);
               CustomBigInteger primeP2 = rsaCrtKeyPair.getPrimeP();
               CustomBigInteger customBigInteger5 = primeP2;
               CustomBigInteger customBigInteger6 = customBigInteger4.mod(customBigInteger5);
               CustomBigInteger customBigInteger7 = crtCoefficient.multiply2(customBigInteger6);
               CustomBigInteger primeP3 = rsaCrtKeyPair.getPrimeP();
               CustomBigInteger customBigInteger8 = primeP3;
               CustomBigInteger customBigInteger9 = customBigInteger7.mod(customBigInteger8);
               CustomBigInteger primeQ2 = rsaCrtKeyPair.getPrimeQ();
               CustomBigInteger customBigInteger10 = customBigInteger9.multiply2(primeQ2);
               CustomBigInteger customBigInteger11 = customBigInteger3.add2(customBigInteger10);
               byte[] bytes3 = bigIntegerToFixedBytes(customBigInteger11, n);
               return Pkcs1Padding.unpad(bytes3);
            } catch (Exception e) {
               throw new Exception("Decryption failed: " + e.getMessage());
            }
         } else if (b == 2) {
            ByteBuffer buf = ByteBuffer.wrap(bytes);
            buf.get();
            int n2 = (buf.get() & 255) << 8 | buf.get() & 255;
            byte[] bytes4 = new byte[n2];
            buf.get(bytes4);
            byte[] bytes5 = new byte[buf.remaining()];
            buf.get(bytes5);
            byte[] bytes6 = new byte[bytes4.length + 1];
            bytes6[0] = 1;
            System.arraycopy(bytes4, 0, bytes6, 1, bytes4.length);
            byte[] bytes7 = decryptWithPrivateKey(bytes6, rsaCrtKeyPair);
            return HybridSymmetricCipher.decrypt(bytes5, bytes7);
         } else {
            throw new Exception("Unknown encryption mode: " + b);
         }
      }
   }

   public static boolean verifySignature(byte[] bytes, byte[] bytes2, CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2) throws Exception {

      try {
         byte[] bytes3 = decrypt(bytes, customBigInteger, customBigInteger2);
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

   public static CustomBigInteger[] importPublicKeyBase64(String str) {

      try {
         ByteBuffer buf = ByteBuffer.wrap(CustomBase64.getRfc46482().decode2(str));
         CustomBigInteger customBigInteger = readBigInteger(buf);
         CustomBigInteger customBigInteger2 = readBigInteger(buf);
         return new CustomBigInteger[]{customBigInteger, customBigInteger2};
      } catch (Exception e) {
         throw new RuntimeException("Failed to import public key: " + e.getMessage());
      }
   }

   public static byte[] exportKeyPairBytes(RsaCrtKeyPair rsaCrtKeyPair) {

      try {
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         CustomBigInteger modulus = rsaCrtKeyPair.getModulus();
         writeBigInteger(byteArrayOutputStream, modulus);
         CustomBigInteger publicExponent = rsaCrtKeyPair.getPublicExponent();
         writeBigInteger(byteArrayOutputStream, publicExponent);
         CustomBigInteger privateExponent = rsaCrtKeyPair.getPrivateExponent();
         writeBigInteger(byteArrayOutputStream, privateExponent);
         CustomBigInteger primeP = rsaCrtKeyPair.getPrimeP();
         writeBigInteger(byteArrayOutputStream, primeP);
         CustomBigInteger primeQ = rsaCrtKeyPair.getPrimeQ();
         writeBigInteger(byteArrayOutputStream, primeQ);
         byte[] bytes = byteArrayOutputStream.toByteArray();
         byteArrayOutputStream.close();
         return bytes;
      } catch (Exception e) {
         throw new RuntimeException("Failed to export key pair: " + e.getMessage());
      }
   }

   private static byte[] bigIntegerToFixedBytes(CustomBigInteger customBigInteger, int n) {
      Object obj = null;
      Object obj2 = null;
      obj2 = customBigInteger.toByteArray();
      if (((byte[])obj2)[0] == 0 && ((byte[])obj2).length > n) {
         obj = new byte[((byte[])obj2).length - 1];
         System.arraycopy((byte[])obj2, 1, (byte[])obj, 0, ((byte[])obj).length);
         obj2 = (byte[])obj;
      }

      if (((byte[])obj2).length < n) {
         obj = new byte[n];
         System.arraycopy((byte[])obj2, 0, (byte[])obj, n - ((byte[])obj2).length, ((byte[])obj2).length);
         obj2 = (byte[])obj;
      }

      return (byte[])obj2;
   }

   public static String exportPublicKeyBase64(RsaCrtKeyPair rsaCrtKeyPair) {

      try {
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         CustomBigInteger modulus = rsaCrtKeyPair.getModulus();
         writeBigInteger(byteArrayOutputStream, modulus);
         CustomBigInteger publicExponent = rsaCrtKeyPair.getPublicExponent();
         writeBigInteger(byteArrayOutputStream, publicExponent);
         byte[] bytes = byteArrayOutputStream.toByteArray();
         byteArrayOutputStream.close();
         return CustomBase64.getRfc4648().encodeToString(bytes);
      } catch (Exception e) {
         throw new RuntimeException("Failed to export public key: " + e.getMessage());
      }
   }

   private static CustomBigInteger bytesToPositiveBigInteger(byte[] bytes) {
      return new CustomBigInteger(1, bytes);
   }

   public static byte[] decrypt(byte[] bytes, CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2) throws Exception {
      return decryptWithPublicKey(bytes, customBigInteger, customBigInteger2);
   }

   public static RsaCrtKeyPair importKeyPairBase64(String str) {

      try {
         ByteBuffer buf = ByteBuffer.wrap(CustomBase64.getRfc46482().decode2(str));
         CustomBigInteger customBigInteger = readBigInteger(buf);
         CustomBigInteger customBigInteger2 = readBigInteger(buf);
         CustomBigInteger customBigInteger3 = readBigInteger(buf);
         CustomBigInteger customBigInteger4 = readBigInteger(buf);
         CustomBigInteger customBigInteger5 = readBigInteger(buf);
         return new RsaCrtKeyPair(customBigInteger, customBigInteger2, customBigInteger3, customBigInteger4, customBigInteger5);
      } catch (Exception e) {
         throw new RuntimeException("Failed to import key pair: " + e.getMessage());
      }
   }

   private static void writeBigInteger(ByteArrayOutputStream byteArrayOutputStream, CustomBigInteger customBigInteger) throws IOException {
      Object obj = null;
      obj = customBigInteger.toByteArray();
      byteArrayOutputStream.write(((byte[])obj).length >>> 24);
      byteArrayOutputStream.write(((byte[])obj).length >>> 16);
      byteArrayOutputStream.write(((byte[])obj).length >>> 8);
      byteArrayOutputStream.write(((byte[])obj).length);
      byteArrayOutputStream.write((byte[])obj);
   }

   public static byte[] encrypt(byte[] bytes, CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2) throws Exception {
      return encryptWithPublicKey(bytes, customBigInteger, customBigInteger2);
   }

   public static RsaCrtKeyPair importKeyPairBytes(byte[] bytes) {

      try {
         ByteBuffer buf = ByteBuffer.wrap(bytes);
         CustomBigInteger customBigInteger = readBigInteger(buf);
         CustomBigInteger customBigInteger2 = readBigInteger(buf);
         CustomBigInteger customBigInteger3 = readBigInteger(buf);
         CustomBigInteger customBigInteger4 = readBigInteger(buf);
         CustomBigInteger customBigInteger5 = readBigInteger(buf);
         return new RsaCrtKeyPair(customBigInteger, customBigInteger2, customBigInteger3, customBigInteger4, customBigInteger5);
      } catch (Exception e) {
         throw new RuntimeException("Failed to import key pair: " + e.getMessage());
      }
   }

   public static byte[] encryptWithPublicKey(byte[] bytes, CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      Object obj5 = null;
      int n = 0;
      n = (customBigInteger.bitLength() + 7) / 8;
      obj5 = Pkcs1Padding.pad(bytes, n);
      if ((byte[])obj5 != null) {
         byte[] bytes2 = (byte[])obj5;
         obj3 = bytesToPositiveBigInteger(bytes2);
         CustomBigInteger customBigInteger3 = (CustomBigInteger)obj3;
         CustomBigInteger customBigInteger4 = customBigInteger2;
         CustomBigInteger customBigInteger5 = customBigInteger;
         CustomBigInteger customBigInteger6 = customBigInteger4;
         obj4 = customBigInteger3.modPow(customBigInteger6, customBigInteger5);
         obj = bigIntegerToFixedBytes((CustomBigInteger)obj4, n);
         obj2 = ByteBuffer.allocate(1 + ((byte[])obj).length);
         ((ByteBuffer)obj2).put((byte)1);
         ((ByteBuffer)obj2).put((byte[])obj);
         return ((ByteBuffer)obj2).array();
      } else {
         return new byte[1];
      }
   }

   private static CustomBigInteger readBigInteger(ByteBuffer buf) {
      Object obj = null;
      int n = 0;
      n = (buf.get() & 255) << 24 | (buf.get() & 255) << 16 | (buf.get() & 255) << 8 | buf.get() & 255;
      obj = new byte[n];
      buf.get((byte[])obj);
      return new CustomBigInteger((byte[])obj);
   }

   public static String exportKeyPairBase64(RsaCrtKeyPair rsaCrtKeyPair) {

      try {
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         CustomBigInteger modulus = rsaCrtKeyPair.getModulus();
         writeBigInteger(byteArrayOutputStream, modulus);
         CustomBigInteger publicExponent = rsaCrtKeyPair.getPublicExponent();
         writeBigInteger(byteArrayOutputStream, publicExponent);
         CustomBigInteger privateExponent = rsaCrtKeyPair.getPrivateExponent();
         writeBigInteger(byteArrayOutputStream, privateExponent);
         CustomBigInteger primeP = rsaCrtKeyPair.getPrimeP();
         writeBigInteger(byteArrayOutputStream, primeP);
         CustomBigInteger primeQ = rsaCrtKeyPair.getPrimeQ();
         writeBigInteger(byteArrayOutputStream, primeQ);
         byte[] bytes = byteArrayOutputStream.toByteArray();
         byteArrayOutputStream.close();
         return CustomBase64.getRfc4648().encodeToString(bytes);
      } catch (Exception e) {
         throw new RuntimeException("Failed to export key pair: " + e.getMessage());
      }
   }

   public static byte[] sign(byte[] bytes, RsaCrtKeyPair rsaCrtKeyPair) throws Exception {
      return encryptWithPrivateKey(bytes, rsaCrtKeyPair);
   }

   static {
      TWO = CustomBigInteger.valueOf(2L);
      DEFAULT_PUBLIC_EXPONENT = CustomBigInteger.valueOf(65537L);
   }

   public static CustomBigInteger[] importPublicKeyBytes(byte[] bytes) {

      try {
         ByteBuffer buf = ByteBuffer.wrap(bytes);
         CustomBigInteger customBigInteger = readBigInteger(buf);
         CustomBigInteger customBigInteger2 = readBigInteger(buf);
         return new CustomBigInteger[]{customBigInteger, customBigInteger2};
      } catch (Exception e) {
         throw new RuntimeException("Failed to import public key: " + e.getMessage());
      }
   }

   public static byte[] encryptWithPrivateKey(byte[] bytes, RsaCrtKeyPair rsaCrtKeyPair) throws Exception {
      int n = (rsaCrtKeyPair.getModulus().bitLength() + 7) / 8;
      if (bytes.length > 245) {
         throw new Exception("Data to encrypt with private key too long");
      } else {
         try {
            byte[] bytes2 = Pkcs1Padding.pad(bytes, n);
            CustomBigInteger customBigInteger = bytesToPositiveBigInteger(bytes2);
            CustomBigInteger primeP = rsaCrtKeyPair.getPrimeP();
            CustomBigInteger primeExponentP = rsaCrtKeyPair.getPrimeExponentP();
            CustomBigInteger customBigInteger2 = customBigInteger.modPow(primeExponentP, primeP);
            CustomBigInteger primeQ = rsaCrtKeyPair.getPrimeQ();
            CustomBigInteger primeExponentQ = rsaCrtKeyPair.getPrimeExponentQ();
            CustomBigInteger customBigInteger3 = customBigInteger.modPow(primeExponentQ, primeQ);
            CustomBigInteger crtCoefficient = rsaCrtKeyPair.getCrtCoefficient();
            CustomBigInteger customBigInteger4 = customBigInteger2.subtract(customBigInteger3);
            CustomBigInteger primeP2 = rsaCrtKeyPair.getPrimeP();
            CustomBigInteger customBigInteger5 = primeP2;
            CustomBigInteger customBigInteger6 = customBigInteger4.mod(customBigInteger5);
            CustomBigInteger customBigInteger7 = crtCoefficient.multiply2(customBigInteger6);
            CustomBigInteger primeP3 = rsaCrtKeyPair.getPrimeP();
            CustomBigInteger customBigInteger8 = primeP3;
            CustomBigInteger customBigInteger9 = customBigInteger7.mod(customBigInteger8);
            CustomBigInteger primeQ2 = rsaCrtKeyPair.getPrimeQ();
            CustomBigInteger customBigInteger10 = customBigInteger9.multiply2(primeQ2);
            CustomBigInteger customBigInteger11 = customBigInteger3.add2(customBigInteger10);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(1);
            byte[] bytes3 = bigIntegerToFixedBytes(customBigInteger11, n);
            byteArrayOutputStream.write(bytes3);
            return byteArrayOutputStream.toByteArray();
         } catch (Exception e) {
            throw new Exception("Private key encryption failed: " + e.getMessage());
         }
      }
   }

   public static byte[] decrypt(byte[] bytes, RsaCrtKeyPair rsaCrtKeyPair) throws Exception {
      return decryptWithPrivateKey(bytes, rsaCrtKeyPair);
   }

   public static byte[] decryptWithPublicKey(byte[] bytes, CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2) throws Exception {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      byte b = 0;
      Object obj4 = null;
      int n = 0;
      if (bytes.length >= 2) {
         b = bytes[0];
         if (b != 1) {
            throw new Exception("Hybrid encryption not supported for public key decryption");
         } else {
            obj = new byte[((byte[])bytes).length - 1];
            System.arraycopy(bytes, 1, (byte[])obj, 0, ((byte[])obj).length);
            n = (customBigInteger.bitLength() + 7) / 8;
            byte[] bytes2 = (byte[])obj;
            obj3 = bytesToPositiveBigInteger(bytes2);
            CustomBigInteger customBigInteger3 = (CustomBigInteger)obj3;
            CustomBigInteger customBigInteger4 = customBigInteger2;
            CustomBigInteger customBigInteger5 = customBigInteger;
            CustomBigInteger customBigInteger6 = customBigInteger4;
            obj2 = customBigInteger3.modPow(customBigInteger6, customBigInteger5);
            obj4 = bigIntegerToFixedBytes((CustomBigInteger)obj2, n);
            byte[] bytes3 = (byte[])obj4;
            return Pkcs1Padding.unpad(bytes3);
         }
      } else {
         throw new Exception("Invalid encrypted data format");
      }
   }

}
