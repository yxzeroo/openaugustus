package net.augustus.vendor.launcher.crypto;

import java.util.Arrays;

public class SymmetricStreamCipher {
   private static final int BLOCK_SIZE = 0;

   public static byte[] decrypt(byte[] bytes, byte[] bytes2) {
      int flowState = Pkcs1Padding.getFlowState();
      if (bytes.length < 32) {
         throw new IllegalArgumentException("Invalid encrypted data");
      } else {
         byte[] bytes3 = new byte[16];
         System.arraycopy(bytes, 0, bytes3, 0, 16);
         int n = bytes.length - 16;
         byte[] bytes4 = new byte[n];
         System.arraycopy(bytes, 16, bytes4, 0, n);
         byte[] bytes5 = new byte[n];
         generateKeystream(bytes2, bytes3, bytes5);
         byte[] bytes6 = new byte[n];

         for (int i = 0; i < n; i++) {
            bytes6[i] = (byte)(bytes4[i] ^ bytes5[i]);
         }

         int n2 = bytes6[bytes6.length - 1] & 255;
         if (n2 > 0 && n2 <= 16) {
            boolean flag = true;

            for (int j = bytes6.length - n2; j < bytes6.length; j++) {
               if ((bytes6[j] & 255) != n2) {
                  flag = false;
                  break;
               }
            }

            if (flag) {
               byte[] bytes7 = new byte[bytes6.length - n2];
               System.arraycopy(bytes6, 0, bytes7, 0, bytes7.length);
               return bytes7;
            }
         }

         return bytes6;
      }
   }

   private static void generateKeystream(byte[] bytes, byte[] bytes2, byte[] bytes3) {
      byte[] bytes4 = Arrays.copyOf(bytes2, bytes2.length);
      Pkcs1Padding.getInvertedFlowMarker();

      for (int i = 0; i < bytes3.length; i += 16) {
         for (int j = 0; j < 16; j++) {
            bytes4[j] ^= bytes[j % bytes.length];
         }

         for (int k = 0; k < 20; k++) {
            for (int m = 0; m < 16; m++) {
               int n = (m + 1) % 16;
               bytes4[m] = (byte)((bytes4[m] << 1 | (bytes4[n] & 128) >>> 7) & 0xFF);
               bytes4[m] ^= bytes[(m + k) % bytes.length];
            }
         }

         int n2 = Math.min(16, bytes3.length - i);
         System.arraycopy(bytes4, 0, bytes3, i, n2);
      }
   }

   public static byte[] encrypt(byte[] bytes, byte[] bytes2) {
      int flowState = Pkcs1Padding.getFlowState();
      if (bytes2.length < 16) {
         throw new IllegalArgumentException("Key must be at least 16 bytes");
      } else {
         byte[] bytes3 = new byte[16];
         (new EntropyXorShiftRandom()).nextBytes(bytes3);
         int n = 16 - bytes.length % 16;
         if (n == 0) {
            n = 16;
         }

         byte[] bytes4 = new byte[bytes.length + n];
         System.arraycopy(bytes, 0, bytes4, 0, bytes.length);

         for (int i = 0; i < n; i++) {
            bytes4[bytes.length + i] = (byte)n;
         }

         byte[] bytes5 = new byte[bytes3.length + bytes4.length];
         System.arraycopy(bytes3, 0, bytes5, 0, bytes3.length);
         byte[] bytes6 = new byte[bytes4.length];
         generateKeystream(bytes2, bytes3, bytes6);

         for (int j = 0; j < bytes4.length; j++) {
            bytes5[bytes3.length + j] = (byte)(bytes4[j] ^ bytes6[j]);
         }

         return bytes5;
      }
   }

   public static byte[] generateRandomKey(int n) {
      int flowState = Pkcs1Padding.getFlowState();
      if (n < 16) {
         n = 16;
      }

      byte[] bytes = new byte[n];
      (new ProbablePrimeGenerator()).nextEntropyBytes(bytes);
      return bytes;
   }


}
