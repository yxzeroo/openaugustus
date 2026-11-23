package net.augustus.vendor.crypto.keccak;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class KeccakSpongeAbsorber {
   private final KeccakPermutation permutation;
   private final Sha3Algorithm algorithm;

   private void absorbBlock(long[][] longsArr, byte[] bytes, int n, int n2) {
      int n3 = 0;
      int n4 = 0;
      boolean flag = false;
      boolean flag2 = false;
      n4 = n;
      n3 = n2;

      for (int i = 0; i < 5; i++) {
         for (int j = 0; j < 5; j++) {
            if (n4 % 8 != 0 || n3 < 64) {
               this.xorRemainingBits(longsArr, bytes, n4, n3, j, i);
               return;
            }

            this.xorLaneBytes(longsArr, bytes, n4, j, i);
            n4 += 64;
            n3 -= 64;
         }
      }
   }

   public KeccakSpongeAbsorber(Sha3Algorithm sha3Algorithm) {
      this.algorithm = sha3Algorithm;
      this.permutation = new KeccakPermutation();
   }

   private boolean getBit(byte[] bytes, int n) {
      int n2 = 0;
      int n3 = 0;
      n3 = n / 8;
      n2 = n % 8;
      return 0 != (bytes[n3] & 1 << n2);
   }

   public void absorb(byte[] bytes, long[][] longsArr) {
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      n2 = bytes.length << 3;
      n3 = 0;

      do {
         n = Math.min(KeccakParameters.getRateBits(this.algorithm), n2 - n3);
         this.absorbBlock(longsArr, bytes, n3, n);
         this.permutation.permute(longsArr);
         n3 += KeccakParameters.getRateBits(this.algorithm);
      } while (n3 < n2);
   }

   private int readRateBlock(InputStream in, byte[] bytes) throws IOException {
      boolean flag = false;
      int n = 0;
      n = 0;

      for (int i = in.read(bytes); i > 0; i = in.read(bytes, n, bytes.length - n)) {
         n += i;
      }

      if (n < bytes.length) {
         Arrays.fill(bytes, n, bytes.length, (byte)0);
      }

      return n * 8;
   }

   public int absorbStream(InputStream in, long[][] longsArr, byte[] bytes) throws IOException {
      int n = 0;
      KeccakSpongeAbsorber keccakSpongeAbsorber = this;
      InputStream in2 = in;
      n = keccakSpongeAbsorber.readRateBlock(in2, bytes);

      while (n == KeccakParameters.getRateBits(this.algorithm)) {
         keccakSpongeAbsorber = this;
         keccakSpongeAbsorber.absorbBlock(longsArr, bytes, (byte)0, n);
         this.permutation.permute(longsArr);
         keccakSpongeAbsorber = this;
         in2 = in;
         n = keccakSpongeAbsorber.readRateBlock(in2, bytes);
      }

      return n;
   }

   private void xorRemainingBits(long[][] longsArr, byte[] bytes, int n, int n2, int n3, int n4) {
      int n5 = 0;
      int n6 = 0;
      int n7 = 0;
      boolean flag = false;
      int n8 = 0;
      n6 = n + n2;
      n5 = 0;
      n8 = n3;
      n7 = n4;

      for (int i = n; i < n6; i++) {
         if (this.getBit(bytes, i)) {
            longsArr[n8][n7] = longsArr[n8][n7] ^ 1L << n5;
         }

         if (++n5 == 64) {
            n8++;
            n5 = 0;
         }

         if (n3 == 5) {
            n7++;
            n8 = 0;
         }
      }
   }

   private void xorLaneBytes(long[][] longsArr, byte[] bytes, int n, int n2, int n3) {
      boolean flag = false;
      int n4 = 0;
      long l = 0L;
      byte b = 0;
      b = 8;
      n4 = n / 8;
      l = 0L;

      for (int i = b - 1; i >= 0; i--) {
         l <<= 8;
         l += (long)Byte.toUnsignedInt(bytes[n4 + i]);
      }

      longsArr[n2][n3] = longsArr[n2][n3] ^ l;
   }

}
