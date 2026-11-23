package net.augustus.vendor.launcher.crypto.keccak;

public class KeccakSqueezer {
   private final Sha3Type sha3Type;
   private static final long e = 255L;

   private void setOutputBit(byte[] bytes, int n) {
      int n2 = n / 8;
      byte b = (byte)(n % 8);
      bytes[n2] += (byte)(1 << b);
   }

   public int extractBits(long[][] longsArr, byte[] bytes, int n, int n2, int n3, int n4) {
      int n5 = n;

      for (int i = 0; i < 64 && n5 != n2; i++) {
         if (this.isStateBitSet(longsArr, n3, n4, i)) {
            this.setOutputBit(bytes, n5);
         }

         n5++;
      }

      return n5;
   }

   public void extractLane(long[][] longsArr, byte[] bytes, int n, int n2, int n3) {
      byte b = 8;
      int n4 = n / 8;
      long l = longsArr[n2][n3];

      for (int i = b - 1; i >= 0; i--) {
         bytes[n4 + (b - 1 - i)] = (byte)((int)(l & e));
         l >>= 8;
      }
   }

   public byte[] squeeze(long[][] longsArr, byte[] bytes) {
      int digestBitLength = this.sha3Type.getDigestBitLength();
      int n = 0;

      do {
         this.extractBlock(longsArr, bytes, n, Math.min(KeccakParameters.getRate(this.sha3Type), digestBitLength - n));
         n += KeccakParameters.getRate(this.sha3Type);
      } while (n < digestBitLength);

      return bytes;
   }

   private boolean isStateBitSet(long[][] longsArr, int n, int n2, int n3) {
      return (longsArr[n][n2] & 1L << n3) != 0L;
   }

   public KeccakSqueezer(Sha3Type sha3Type2) {
      this.sha3Type = sha3Type2;
   }

   private void extractBlock(long[][] longsArr, byte[] bytes, int n, int n2) {
      int n3 = n;
      int n4 = n + n2;

      for (int i = 0; i < 5; i++) {
         for (int j = 0; j < 5; j++) {
            if (n3 == n4) {
               return;
            }

            if (n3 % 8 == 0 && n2 - n3 >= 64) {
               this.extractLane(longsArr, bytes, n3, j, i);
               n3 += 64;
            } else {
               n3 = this.extractBits(longsArr, bytes, n3, n4, j, i);
            }
         }
      }
   }




}
