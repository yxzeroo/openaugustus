package net.augustus.vendor.launcher.crypto;

import net.augustus.vendor.launcher.crypto.math.ShadedBigInteger;
import java.util.Random;

public class ProbablePrimeGenerator {
   private final Random random;
   private static final ShadedBigInteger TWO;

   private boolean isProbablePrime(ShadedBigInteger shadedBigInteger, int n) {
      int invertedFlowMarker = Pkcs1Padding.getInvertedFlowMarker();
      ShadedBigInteger one = ShadedBigInteger.ONE;
      if (shadedBigInteger.compareTo2(one) <= 0) {
         return false;
      } else {
         ShadedBigInteger shadedBigInteger2 = TWO;
         if (shadedBigInteger.equals(shadedBigInteger2)) {
            return true;
         } else {
            ShadedBigInteger shadedBigInteger3 = TWO;
            ShadedBigInteger shadedBigInteger4 = shadedBigInteger.mod(shadedBigInteger3);
            ShadedBigInteger zero = ShadedBigInteger.ZERO;
            if (shadedBigInteger4.equals(zero)) {
               return false;
            } else {
               ShadedBigInteger[] shadedBigIntegers = new ShadedBigInteger[]{
                  ShadedBigInteger.valueOf(3L),
                  ShadedBigInteger.valueOf(5L),
                  ShadedBigInteger.valueOf(7L),
                  ShadedBigInteger.valueOf(11L),
                  ShadedBigInteger.valueOf(13L),
                  ShadedBigInteger.valueOf(17L),
                  ShadedBigInteger.valueOf(19L),
                  ShadedBigInteger.valueOf(23L),
                  ShadedBigInteger.valueOf(29L)
               };

               for (ShadedBigInteger shadedBigInteger5 : shadedBigIntegers) {
                  if (shadedBigInteger.equals(shadedBigInteger5)) {
                     return true;
                  }

                  shadedBigInteger4 = shadedBigInteger.mod(shadedBigInteger5);
                  ShadedBigInteger zero2 = ShadedBigInteger.ZERO;
                  if (shadedBigInteger4.equals(zero2)) {
                     return false;
                  }
               }

               ShadedBigInteger one2 = ShadedBigInteger.ONE;
               ShadedBigInteger shadedBigInteger6 = shadedBigInteger.subtract(one2);
               int n2 = 0;
               ShadedBigInteger shadedBigInteger7 = shadedBigInteger6;

               while (true) {
                  ShadedBigInteger shadedBigInteger8 = TWO;
                  shadedBigInteger4 = shadedBigInteger7.mod(shadedBigInteger8);
                  ShadedBigInteger zero3 = ShadedBigInteger.ZERO;
                  if (!shadedBigInteger4.equals(zero3)) {
                     for (int i = 0; i < n; i++) {
                        while (true) {
                           shadedBigInteger4 = new ShadedBigInteger(shadedBigInteger.bitLength(), this.random);
                           ShadedBigInteger shadedBigInteger9 = shadedBigInteger4.mod(shadedBigInteger);
                           ShadedBigInteger one3 = ShadedBigInteger.ONE;
                           if (shadedBigInteger9.compareTo2(one3) > 0) {
                              if (shadedBigInteger9.compareTo2(shadedBigInteger6) < 0) {
                                 ShadedBigInteger shadedBigInteger10 = shadedBigInteger9.modPow(shadedBigInteger7, shadedBigInteger);
                                 ShadedBigInteger one4 = ShadedBigInteger.ONE;
                                 if (!shadedBigInteger10.equals(one4) && !shadedBigInteger10.equals(shadedBigInteger6)) {
                                    boolean flag = false;

                                    for (int j = 0; j < n2 - 1; j++) {
                                       ShadedBigInteger shadedBigInteger11 = TWO;
                                       shadedBigInteger10 = shadedBigInteger10.modPow(shadedBigInteger11, shadedBigInteger);
                                       ShadedBigInteger one5 = ShadedBigInteger.ONE;
                                       if (shadedBigInteger10.equals(one5)) {
                                          return false;
                                       }

                                       if (shadedBigInteger10.equals(shadedBigInteger6)) {
                                          flag = true;
                                          break;
                                       }
                                    }

                                    if (!flag) {
                                       return false;
                                    }
                                 }
                                 break;
                              }
                           }
                        }
                     }

                     return true;
                  }

                  ShadedBigInteger shadedBigInteger12 = TWO;
                  shadedBigInteger7 = shadedBigInteger7.divide(shadedBigInteger12);
                  n2++;
               }
            }
         }
      }
   }

   public void nextEntropyBytes(byte[] bytes) {
      this.random.nextBytes(bytes);
      Pkcs1Padding.getInvertedFlowMarker();

      for (int i = 0; i < bytes.length; i++) {
         bytes[i] ^= (byte)((int)(System.nanoTime() & 255L));
         if (i % 5 == 0) {
            long l = System.nanoTime() ^ Thread.currentThread().getId() << 8;
            bytes[i] ^= (byte)((int)(l & 255L));
         }

         if (i % 10 == 0) {
            try {
               Thread.sleep(0L, 100);
            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
            }
         }
      }
   }

   static {
      TWO = ShadedBigInteger.valueOf(2L);
   }

   public ProbablePrimeGenerator() {
      long l = System.currentTimeMillis();
      l ^= Thread.currentThread().getId();
      l ^= Runtime.getRuntime().freeMemory();
      l ^= System.nanoTime();
      this.random = new Random(l);
   }

   public ShadedBigInteger generateRandomBigInteger(int n) {
      int n2 = (n + 7) / 8;
      Pkcs1Padding.getFlowState();
      byte[] bytes = new byte[n2];
      this.nextEntropyBytes(bytes);
      if (n > 0) {
         int n3 = 8 * n2 - n;
         bytes[0] &= (byte)(255 >>> n3);
      }

      bytes[0] |= (byte)(1 << 8 - (8 * n2 - n + 1) % 8);
      return new ShadedBigInteger(1, bytes);
   }

   public ShadedBigInteger generateProbablePrime(int n, int n2) {
      int invertedFlowMarker = Pkcs1Padding.getInvertedFlowMarker();
      ShadedBigInteger shadedBigInteger = this.generateRandomBigInteger(n);
      byte b = 0;
      if (!shadedBigInteger.testBit(b)) {
         ShadedBigInteger one = ShadedBigInteger.ONE;
         shadedBigInteger = shadedBigInteger.add(one);
      }

      while (!this.isProbablePrime(shadedBigInteger, n2)) {
         ShadedBigInteger shadedBigInteger2 = TWO;
         shadedBigInteger = shadedBigInteger.add(shadedBigInteger2);
      }

      return shadedBigInteger;
   }
}
