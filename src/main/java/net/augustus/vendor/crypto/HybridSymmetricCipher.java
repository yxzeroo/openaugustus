package net.augustus.vendor.crypto;

import net.augustus.vendor.crypto.rsa.ProbablePrimeGenerator;
import java.util.Arrays;

public class HybridSymmetricCipher {
   private static int BLOCK_SIZE;

   public static byte[] encrypt(byte[] bytes, byte[] bytes2) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      int n = 0;
      boolean flag = false;
      boolean flag2 = false;
      Object obj4 = null;
      if (bytes2.length < 16) {
         throw new IllegalArgumentException("Key must be at least 16 bytes");
      } else {
         obj4 = new byte[16];
         CustomSecureRandom customSecureRandom = new CustomSecureRandom();
         byte[] bytes3 = (byte[])obj4;
         customSecureRandom.nextBytes(bytes3);
         n = 16 - bytes.length % 16;
         if (n == 0) {
            n = 16;
         }

         obj = new byte[((byte[])bytes).length + n];
         System.arraycopy(bytes, 0, (byte[])obj, 0, bytes.length);

         for (int i = 0; i < n; i++) {
            ((byte[])obj)[((byte[])bytes).length + i] = (byte)n;
         }

         obj3 = new byte[((byte[])obj4).length + ((byte[])obj).length];
         System.arraycopy((byte[])obj4, 0, (byte[])obj3, 0, ((byte[])obj4).length);
         obj2 = new byte[((byte[])obj).length];
         byte[] bytes4 = bytes2;
         byte[] bytes5 = (byte[])obj4;
         byte[] bytes6 = (byte[])obj2;
         byte[] bytes7 = bytes5;
         generateKeystream(bytes4, bytes7, bytes6);

         for (int j = 0; j < ((byte[])obj).length; j++) {
            ((byte[])obj3)[((byte[])obj4).length + j] = (byte)(((byte[])obj)[j] ^ ((byte[])obj2)[j]);
         }

         return (byte[])obj3;
      }
   }

   private static void generateKeystream(byte[] bytes, byte[] bytes2, byte[] bytes3) {
      boolean flag = false;
      boolean flag2 = false;
      int n = 0;
      int n2 = 0;
      Object obj = null;
      obj = Arrays.copyOf(bytes2, bytes2.length);

      for (int i = 0; i < ((byte[])bytes3).length; i += 16) {
         for (int j = 0; j < 16; j++) {
            ((byte[])obj)[j] = (byte)(((byte[])obj)[j] ^ bytes[j % ((byte[])bytes).length]);
         }

         for (int k = 0; k < 20; k++) {
            for (int m = 0; m < 16; m++) {
               n = (m + 1) % 16;
               ((byte[])obj)[m] = (byte)((((byte[])obj)[m] << 1 | (((byte[])obj)[n] & 128) >>> 7) & 0xFF);
               ((byte[])obj)[m] = (byte)(((byte[])obj)[m] ^ bytes[(m + k) % ((byte[])bytes).length]);
            }
         }

         n2 = Math.min(16, bytes3.length - i);
         System.arraycopy((byte[])obj, 0, bytes3, i, n2);
      }
   }

   public static byte[] generateKey(int n) {
      Object obj = null;
      if (n < 16) {
         n = 16;
      }

      obj = new byte[n];
      ProbablePrimeGenerator probablePrimeGenerator = new ProbablePrimeGenerator();
      byte[] bytes = (byte[])obj;
      probablePrimeGenerator.nextEntropyBytes(bytes);
      return (byte[])obj;
   }

   public static byte[] decrypt(byte[] bytes, byte[] bytes2) {
      int n = 0;
      Object obj = null;
      Object obj2 = null;
      int n2 = 0;
      Object obj3 = null;
      Object obj4 = null;
      boolean flag = false;
      Object obj5 = null;
      boolean flag2 = false;
      if (bytes.length < 32) {
         throw new IllegalArgumentException("Invalid encrypted data");
      } else {
         obj3 = new byte[16];
         System.arraycopy(bytes, 0, (byte[])obj3, 0, 16);
         n = bytes.length - 16;
         obj4 = new byte[n];
         System.arraycopy(bytes, 16, (byte[])obj4, 0, n);
         obj5 = new byte[n];
         byte[] bytes3 = bytes2;
         byte[] bytes4 = (byte[])obj3;
         byte[] bytes5 = (byte[])obj5;
         byte[] bytes6 = bytes4;
         generateKeystream(bytes3, bytes6, bytes5);
         obj = new byte[n];

         for (int i = 0; i < n; i++) {
            ((byte[])obj)[i] = (byte)(((byte[])obj4)[i] ^ ((byte[])obj5)[i]);
         }

         n2 = ((byte[])obj)[((byte[])obj).length - 1] & 255;
         if (n2 > 0 && n2 <= 16) {
            flag = true;

            for (int j = ((byte[])obj).length - n2; j < ((byte[])obj).length; j++) {
               if ((((byte[])obj)[j] & 255) != n2) {
                  flag = false;
                  break;
               }
            }

            if (flag) {
               obj2 = new byte[((byte[])obj).length - n2];
               System.arraycopy((byte[])obj, 0, (byte[])obj2, 0, ((byte[])obj2).length);
               return (byte[])obj2;
            }
         }

         return (byte[])obj;
      }
   }

}
