package net.augustus.vendor.crypto.math;

import net.augustus.vendor.crypto.CustomBigInteger;
import java.util.Random;

public class PrimeBitSieve {
   private int length;
   private static PrimeBitSieve smallSieve;
   private long[] bits;

   private static int unitIndex(int n) {
      return n >>> 6;
   }

   private void sieveSingle(int n, int n2, int n3) {

      while (n2 < n) {
         this.set(n2);
         n2 += n3;
      }
   }

   private boolean get(int n) {
      int n2 = 0;
      long l = 0L;
      l = 1403035457723525380L ^ (long)"쇭\uecd5ꐸ憾뇈ᰃ熗❶\ue55a쀏槺瀳䡐\ud98c❗晫\ue9b8ﲞ켨ꚥ\u0a52픭鲉鿘ꆰ࢝\u200d".hashCode();
      n2 = unitIndex(n);
      l = l - -6622336172733027145L ^ -2191481261975394830L;
      boolean flag;
      if ((this.bits[n2] & bit(n)) == 0L) {
         l = (l ^ -3335985469781827115L) + 7201928683282548318L;
         flag = false;
      } else {
         l = l - 5413189809096719188L ^ -9014418570746730849L;
         flag = true;
      }

      return flag;
   }

   private void set(int n) {
      int n2 = 0;
      n2 = unitIndex(n);
      this.bits[n2] = this.bits[n2] | bit(n);
   }

   static {
      smallSieve = new PrimeBitSieve();
   }

   public CustomBigInteger retrieve(CustomBigInteger customBigInteger, int n, Random rand) {
      long l = 0L;
      Object obj = null;
      int n2 = 0;
      byte b = 0;
      int n3 = 0;
      long l2 = 0L;
      l2 = 5903054056774736943L ^ (long)"ꍄ⩚\uf153蔖\ue5b9箖镳\uf606\ue95d咾蜡脠㰰햸鉛튝ꉾ⽣뤷짟㸡樊徻㊱".hashCode();
      b = 1;
      l2 = l2 - -6282686622510913435L ^ -3336793127296361118L;
      n3 = 0;
      l2 = l2 + 6153882969193286112L ^ 7769447826484764430L;

      while (n3 < ((long[])this.bits).length) {
         l2 = l2 + 6162684763081749246L ^ 8823898963736192848L;
         l = ~this.bits[n3];
         l2 = (l2 ^ 2505456776208881297L) - 2665681469134821166L;
         n2 = 0;
         l2 = l2 - 4085209889135502162L ^ -8703669979872059339L;

         while (n2 < 64) {
            l2 = (l2 ^ 5943257931109738733L) - 7270939858571326220L;
            if ((l & 1L) != 1L) {
               l2 = l2 + 2276322269527848160L ^ -1900309811281537354L;
            } else {
               l2 = l2 + 120271549378631993L ^ 5647143142673624201L;
               CustomBigInteger customBigInteger2 = customBigInteger;
               obj = customBigInteger2.add2(CustomBigInteger.valueOf((long)b));
               l2 = l2 + -7620482389353213670L ^ 9112964045003031263L;
               customBigInteger2 = (CustomBigInteger)obj;
               if (customBigInteger2.primeToCertainty(n, rand)) {
                  l2 = (l2 ^ -2718505282163047679L) - 2685270582744089189L;
                  return (CustomBigInteger)obj;
               }

               l2 = l2 + -6190216060548665968L ^ -7218125866780372077L;
            }

            l >>>= 1;
            l2 = (l2 ^ 6313568728455129820L) + -3797883910222028869L;
            b += 2;
            l2 = l2 - -5851095505358025998L ^ -7947558734575209810L;
            n2++;
            l2 = (l2 ^ 686097245616831167L) - 8877989788917482836L;
            l2 = l2 + 4058951552071244569L ^ -9212702135619102650L;
         }

         l2 = (l2 ^ 4013217413656079338L) + 3070961842197609286L;
         n3++;
         l2 = (l2 ^ -5725217166662425712L) + -1470967413639054303L;
         l2 = (l2 ^ -453911021796227897L) + 2376847363657892402L;
      }

      l2 = (l2 ^ 3083930767579803666L) + 7821307554824108401L;
      return null;
   }

   public PrimeBitSieve(CustomBigInteger customBigInteger, int n) {
      super();
      this.bits = new long[unitIndex(n - 1) + 1];
      this.length = n;
      int n2 = 0;
      int length2 = smallSieve.length;
      int n3 = smallSieve.sieveSearch(length2, n2);
      int n4 = n3 * 2 + 1;
      ShadedMutableBigInteger shadedMutableBigInteger = new ShadedMutableBigInteger(customBigInteger);
      ShadedMutableBigInteger shadedMutableBigInteger2 = new ShadedMutableBigInteger();

      do {
         n2 = shadedMutableBigInteger.divideOneWord(n4, shadedMutableBigInteger2);
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

   private int sieveSearch(int n, int n2) {
      int n3 = 0;
      if (n2 < n) {
         n3 = n2;

         while (this.get(n3)) {
            if (++n3 >= n - 1) {
               return -1;
            }
         }

         return n3;
      } else {
         return -1;
      }
   }

   private static long bit(int n) {
      return 1L << (n & 63);
   }

   private PrimeBitSieve() {
      super();
      this.length = 9600;
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

}
