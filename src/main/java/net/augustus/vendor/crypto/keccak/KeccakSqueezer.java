package net.augustus.vendor.crypto.keccak;


public class KeccakSqueezer {
   private final Sha3Algorithm algorithm;

   public void extractLaneBytes(long[][] longsArr, byte[] bytes, int n, int n2, int n3) {
      byte b = 0;
      boolean flag = false;
      long l = 0L;
      byte b2 = 0;
      int n4 = 0;
      b2 = 8;
      n4 = n / 8;
      l = longsArr[n2][n3];

      for (int i = b2 - 1; i >= 0; i--) {
         b = (byte)((int)(l & 255L));
         bytes[n4 + (b2 - 1 - i)] = b;
         l >>= 8;
      }
   }

   private void squeezeBlock(long[][] longsArr, byte[] bytes, int n, int n2) {
      int n3 = 0;
      boolean flag = false;
      boolean flag2 = false;
      int n4 = 0;
      n3 = n;
      n4 = n + n2;

      for (int i = 0; i < 5; i++) {
         for (int j = 0; j < 5; j++) {
            if (n3 == n4) {
               return;
            }

            if (n3 % 8 == 0 && n2 - n3 >= 64) {
               this.extractLaneBytes(longsArr, bytes, n3, j, i);
               n3 += 64;
            } else {
               n3 = this.extractLaneBits(longsArr, bytes, n3, n4, j, i);
            }
         }
      }
   }

   public byte[] squeeze(long[][] longsArr, byte[] bytes) {
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      n = this.algorithm.getOutputBitLength();
      n3 = 0;

      do {
         n2 = Math.min(KeccakParameters.getRateBits(this.algorithm), n - n3);
         this.squeezeBlock(longsArr, bytes, n3, n2);
         n3 += KeccakParameters.getRateBits(this.algorithm);
      } while (n3 < n);

      return bytes;
   }

   public KeccakSqueezer(Sha3Algorithm sha3Algorithm) {
      this.algorithm = sha3Algorithm;
   }

   private void setOutputBit(byte[] bytes, int n) {
      int n2 = 0;
      byte b = 0;
      n2 = n / 8;
      b = (byte)(n % 8);
      bytes[n2] = (byte)(bytes[n2] + (byte)(1 << b));
   }

   private boolean isStateBitSet(long[][] longsArr, int n, int n2, int n3) {
      return (longsArr[n][n2] & 1L << n3) != 0L;
   }

   public int extractLaneBits(long[][] longsArr, byte[] bytes, int n, int n2, int n3, int n4) {
      int n5 = 0;
      boolean flag = false;
      n5 = n;

      for (int i = 0; i < 64; i++) {
         if (n5 == n2) {
            break;
         }

         KeccakSqueezer keccakSqueezer = this;
         if (keccakSqueezer.isStateBitSet(longsArr, n3, n4, i)) {
            keccakSqueezer = this;
            keccakSqueezer.setOutputBit(bytes, n5);
         }

         n5++;
      }

      return n5;
   }




}
