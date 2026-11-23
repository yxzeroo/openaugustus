package net.augustus.vendor.launcher.crypto.rsa;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class RsaPublicKeyCipher {
   private byte[] buffer;
   public static final SecureRandom secureRandom = new SecureRandom();
   private int bufOfs;
   private RsaPaddingEngine padding;
   private static final long e = 6790944702066589703L;

   public RsaPublicKeyCipher() {
      this.bufOfs = (byte)0;
      this.buffer = new byte[0];
   }

   private int update(byte[] bytes, int n, int n2) {
      if (n2 != 0 && bytes != null) {
         if (n2 > this.buffer.length - this.bufOfs) {
            this.bufOfs = this.buffer.length + 1;
         } else {
            System.arraycopy(bytes, n, this.buffer, this.bufOfs, n2);
            this.bufOfs = this.bufOfs + n2;
         }
      }

      return this.bufOfs;
   }

   public static int getByteLength(BigInteger bigInt) {
      return bigInt.bitLength() + (int)e >> 3;
   }


   private static BigInteger parseMsg(byte[] bytes, BigInteger bigInt) throws BadPaddingException {
      BigInteger bigInt2 = new BigInteger(1, bytes);
      if (bigInt2.compareTo(bigInt) >= 0) {
         throw new BadPaddingException("Message is larger than modulus");
      } else {
         return bigInt2;
      }
   }

   private static byte[] rsaCrypt(byte[] bytes, BigInteger bigInt, BigInteger bigInt2) throws BadPaddingException {
      return toByteArray(parseMsg(bytes, bigInt).modPow(bigInt2, bigInt), getByteLength(bigInt));
   }

   private static byte[] toByteArray(BigInteger bigInt, int n) {
      byte[] bytes = bigInt.toByteArray();
      int length = bytes.length;
      if (length == n) {
         return bytes;
      } else if (length == n + 1 && bytes[0] == 0) {
         byte[] bytes2 = new byte[n];
         System.arraycopy(bytes, 1, bytes2, 0, n);
         return bytes2;
      } else {
         assert length < n;

         byte[] bytes3 = new byte[n];
         System.arraycopy(bytes, 0, bytes3, n - length, length);
         return bytes3;
      }
   }

   public static byte[] convert(byte[] bytes, int n, int n2) {
      if (n == 0 && n2 == bytes.length) {
         return bytes;
      } else {
         byte[] bytes2 = new byte[n2];
         System.arraycopy(bytes, n, bytes2, 0, n2);
         return bytes2;
      }
   }

   public byte[] decrypt(RSAPublicKey rsaPublicKey, byte[] bytes) {

      try {
         this.buffer = new byte[bytes.length];
         this.padding = RsaPaddingEngine.getInstance(1, getByteLength(rsaPublicKey.getModulus()), secureRandom);
         this.update(bytes, (byte)0, bytes.length);
         if (this.bufOfs > this.buffer.length) {
            throw new IllegalBlockSizeException("Data must not be longer than " + this.buffer.length + " bytes");
         } else {
            BigInteger publicExponent = rsaPublicKey.getPublicExponent();
            BigInteger modulus = rsaPublicKey.getModulus();
            RsaPaddingEngine padding2 = this.padding;
            int bufOfs2 = this.bufOfs;
            return padding2.unpad(rsaCrypt(convert(this.buffer, (byte)0, bufOfs2), modulus, publicExponent));
         }
      } catch (InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e2) {
         throw new RuntimeException(e2);
      }
   }



}
