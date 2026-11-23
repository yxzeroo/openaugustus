package net.augustus.vendor.launcher.crypto.keccak;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class KeccakAbsorber {
   private final Sha3Type sha3Type;
   private final KeccakPermutation permutation;

   private void xorRemainingBits(long[][] longsArr, byte[] bytes, int n, int n2, int n3, int n4) {
      int n5 = n + n2;
      int n6 = 0;
      int n7 = n3;
      int n8 = n4;

      for (int i = n; i < n5; i++) {
         if (this.isBitSet(bytes, i)) {
            longsArr[n7][n8] = longsArr[n7][n8] ^ 1L << n6;
         }

         if (++n6 == 64) {
            n7++;
            n6 = 0;
         }

         if (n3 == 5) {
            n8++;
            n7 = 0;
         }
      }
   }

   public int absorbStream(InputStream in, long[][] longsArr, byte[] bytes) throws IOException {

      int n;
      for (n = this.readBlockBits(in, bytes); n == KeccakParameters.getRate(this.sha3Type); n = this.readBlockBits(in, bytes)) {
         byte b = 0;
         this.xorBlock(longsArr, bytes, b, n);
         this.permutation.permute(longsArr);
      }

      return n;
   }

   public KeccakAbsorber(Sha3Type sha3Type2) {
      this.sha3Type = sha3Type2;
      this.permutation = new KeccakPermutation();
   }

   public void absorb(byte[] bytes, long[][] longsArr) {
      int n = bytes.length << 3;
      int n2 = 0;

      do {
         int n3 = Math.min(KeccakParameters.getRate(this.sha3Type), n - n2);
         this.xorBlock(longsArr, bytes, n2, n3);
         this.permutation.permute(longsArr);
         n2 += KeccakParameters.getRate(this.sha3Type);
      } while (n2 < n);
   }

   private boolean isBitSet(byte[] bytes, int n) {
      int n2 = n / 8;
      int n3 = n % 8;
      return 0 != (bytes[n2] & 1 << n3);
   }

   private void xorBlock(long[][] longsArr, byte[] bytes, int n, int n2) {
      int n3 = n;
      int n4 = n2;

      for (int i = 0; i < 5; i++) {
         for (int j = 0; j < 5; j++) {
            if (n3 % 8 != 0 || n4 < 64) {
               this.xorRemainingBits(longsArr, bytes, n3, n4, j, i);
               return;
            }

            this.xorLane(longsArr, bytes, n3, j, i);
            n3 += 64;
            n4 -= 64;
         }
      }
   }

   private void xorLane(long[][] longsArr, byte[] bytes, int n, int n2, int n3) {
      byte b = 8;
      int n4 = n / 8;
      long l = 0L;

      for (int i = b - 1; i >= 0; i--) {
         l <<= 8;
         l += (long)Byte.toUnsignedInt(bytes[n4 + i]);
      }

      longsArr[n2][n3] = longsArr[n2][n3] ^ l;
   }

   private int readBlockBits(InputStream in, byte[] bytes) throws IOException {
      int n = 0;

      for (int i = in.read(bytes); i > 0; i = in.read(bytes, n, bytes.length - n)) {
         n += i;
      }

      if (n < bytes.length) {
         Arrays.fill(bytes, n, bytes.length, (byte)0);
      }

      return n * 8;
   }
}
