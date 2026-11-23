package net.augustus.vendor.launcher.crypto.math;

import java.util.Random;

public class PrimeBitSieve {
   private long[] bits;
   private int length;
   private static PrimeBitSieve smallSieve;
   private static final long e = -1L;

   public ShadedBigInteger retrieve(ShadedBigInteger shadedBigInteger, int n, Random rand) {
      int n2 = 1;

      for (int i = 0; i < this.bits.length; i++) {
         long l = this.bits[i] ^ e;

         for (int j = 0; j < 64; j++) {
            if ((l & 1L) == 1L) {
               ShadedBigInteger shadedBigInteger4 = shadedBigInteger.add(ShadedBigInteger.valueOf((long)n2));
               if (shadedBigInteger4.primeToCertainty(n, rand)) {
                  return shadedBigInteger4;
               }
            }

            l >>>= 1;
            n2 += 2;
         }
      }

      return null;
   }

   private void set(int n) {
      int n2 = unitIndex(n);
      this.bits[n2] = this.bits[n2] | bit(n);
   }

   private void sieveSingle(int n, int n2, int n3) {

      while (n2 < n) {
         this.set(n2);
         n2 += n3;
      }
   }

   private static long bit(int n) {
      return 1L << (n & 63);
   }

   private boolean get(int n) {
      int n2 = unitIndex(n);
      return (this.bits[n2] & bit(n)) != 0L;
   }

   private int sieveSearch(int n, int n2) {
      if (n2 >= n) {
         return -1;
      } else {
         int n3 = n2;

         do {
            if (!this.get(n3)) {
               return n3;
            }
         } while (++n3 < n - 1);

         return -1;
      }
   }

   static {
      smallSieve = new PrimeBitSieve();
   }

   private PrimeBitSieve() {
      super();
      this.length = (short)9600;
      this.bits = new long[unitIndex(this.length - 1) + 1];
      this.set((byte)0);
      int n = 1;
      int n2 = 3;

      do {
         int n3 = n + n2;
         this.sieveSingle(this.length, n3, n2);
         int n5 = n + 1;
         n = this.sieveSearch(this.length, n5);
         n2 = 2 * n + 1;
      } while (n > 0 && n2 < this.length);
   }

   private static int unitIndex(int n) {
      return n >>> 6;
   }

   public PrimeBitSieve(ShadedBigInteger shadedBigInteger, int n) {
      super();
      this.bits = new long[unitIndex(n - 1) + 1];
      this.length = n;
      int n2 = 0;
      int length2 = smallSieve.length;
      int n3 = smallSieve.sieveSearch(length2, n2);
      int n4 = n3 * 2 + 1;
      MutableBigInt mutableBigInt = new MutableBigInt(shadedBigInteger);
      MutableBigInt mutableBigInt2 = new MutableBigInt();

      do {
         n2 = mutableBigInt.divideOneWord(n4, mutableBigInt2);
         n2 = n4 - n2;
         if (n2 % 2 == 0) {
            n2 += n4;
         }

         this.sieveSingle(n, (n2 - 1) / 2, n4);
         int n7 = n3 + 1;
         int length3 = smallSieve.length;
         n3 = smallSieve.sieveSearch(length3, n7);
         n4 = n3 * 2 + 1;
      } while (n3 > 0);
   }
}
