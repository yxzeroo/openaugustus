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
   private static int[] flowState;
   private static final long e = 6790944702066589703L;

   public RsaPublicKeyCipher() {
      byte b = 0;
      this.bufOfs = b;
      byte[] bytes = new byte[0];
      this.buffer = bytes;
   }

   private int update(byte[] bytes, int n, int n2) {
      int[] flowState2 = getFlowState();
      if (n2 != 0 && bytes != null) {
         if (n2 > this.buffer.length - this.bufOfs) {
            int n3 = this.buffer.length + 1;
            this.bufOfs = n3;
         } else {
            System.arraycopy(bytes, n, this.buffer, this.bufOfs, n2);
            int n4 = this.bufOfs + n2;
            this.bufOfs = n4;
         }
      }

      return this.bufOfs;
   }

   public static int getByteLength(BigInteger bigInt) {
      int n = bigInt.bitLength();
      return n + (int)e >> 3;
   }

   static {
      setFlowState(new int[3]);
   }

   private static BigInteger parseMsg(byte[] bytes, BigInteger bigInt) throws BadPaddingException {
      int[] flowState2 = getFlowState();
      BigInteger bigInt2 = new BigInteger(1, bytes);
      if (bigInt2.compareTo(bigInt) >= 0) {
         throw new BadPaddingException("Message is larger than modulus");
      } else {
         return bigInt2;
      }
   }

   private static byte[] rsaCrypt(byte[] bytes, BigInteger bigInt, BigInteger bigInt2) throws BadPaddingException {
      BigInteger bigInt3 = parseMsg(bytes, bigInt);
      BigInteger bigInt4 = bigInt3.modPow(bigInt2, bigInt);
      int byteLength = getByteLength(bigInt);
      int n = byteLength;
      return toByteArray(bigInt4, n);
   }

   private static byte[] toByteArray(BigInteger bigInt, int n) {
      byte[] bytes = bigInt.toByteArray();
      getFlowState();
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
      int[] flowState2 = getFlowState();
      if (n == 0 && n2 == bytes.length) {
         return bytes;
      } else {
         byte[] bytes2 = new byte[n2];
         System.arraycopy(bytes, n, bytes2, 0, n2);
         return bytes2;
      }
   }

   public byte[] decrypt(RSAPublicKey rsaPublicKey, byte[] bytes) {
      int[] flowState2 = getFlowState();

      try {
         byte[] bytes2 = new byte[bytes.length];
         this.buffer = bytes2;
         int byteLength = getByteLength(rsaPublicKey.getModulus());
         RsaPaddingEngine instance = RsaPaddingEngine.getInstance(1, byteLength, secureRandom);
         this.padding = instance;
         int length = bytes.length;
         byte b = 0;
         this.update(bytes, b, length);
         if (this.bufOfs > this.buffer.length) {
            throw new IllegalBlockSizeException("Data must not be longer than " + this.buffer.length + " bytes");
         } else {
            BigInteger publicExponent = rsaPublicKey.getPublicExponent();
            BigInteger modulus = rsaPublicKey.getModulus();
            RsaPaddingEngine padding2 = this.padding;
            int bufOfs2 = this.bufOfs;
            byte b2 = 0;
            byte[] bytes3 = rsaCrypt(convert(this.buffer, b2, bufOfs2), modulus, publicExponent);
            return padding2.unpad(bytes3);
         }
      } catch (InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e2) {
         throw new RuntimeException(e2);
      }
   }

   public static void setFlowState(int[] ints) {
      flowState = ints;
   }

   public static int[] getFlowState() {
      return flowState;
   }

}
