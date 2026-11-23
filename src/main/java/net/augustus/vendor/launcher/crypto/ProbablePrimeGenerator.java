package net.augustus.vendor.launcher.crypto;

import net.augustus.vendor.launcher.crypto.math.ShadedBigInteger;
import java.util.Random;

public class ProbablePrimeGenerator {
   private final Random random;
   private static final ShadedBigInteger TWO;

   private boolean isProbablePrime(ShadedBigInteger shadedBigInteger, int n) {
      if (shadedBigInteger.compareTo2(ShadedBigInteger.ONE) <= 0) {
         return false;
      } else {
         if (shadedBigInteger.equals(TWO)) {
            return true;
         } else {
            ShadedBigInteger shadedBigInteger4 = shadedBigInteger.mod(TWO);
            if (shadedBigInteger4.equals(ShadedBigInteger.ZERO)) {
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
                  if (shadedBigInteger4.equals(ShadedBigInteger.ZERO)) {
                     return false;
                  }
               }

               ShadedBigInteger shadedBigInteger6 = shadedBigInteger.subtract(ShadedBigInteger.ONE);
               int n2 = 0;
               ShadedBigInteger shadedBigInteger7 = shadedBigInteger6;

               while (true) {
                  shadedBigInteger4 = shadedBigInteger7.mod(TWO);
                  if (!shadedBigInteger4.equals(ShadedBigInteger.ZERO)) {
                     for (int i = 0; i < n; i++) {
                        while (true) {
                           shadedBigInteger4 = new ShadedBigInteger(shadedBigInteger.bitLength(), this.random);
                           ShadedBigInteger shadedBigInteger9 = shadedBigInteger4.mod(shadedBigInteger);
                           if (shadedBigInteger9.compareTo2(ShadedBigInteger.ONE) > 0) {
                              if (shadedBigInteger9.compareTo2(shadedBigInteger6) < 0) {
                                 ShadedBigInteger shadedBigInteger10 = shadedBigInteger9.modPow(shadedBigInteger7, shadedBigInteger);
                                 if (!shadedBigInteger10.equals(ShadedBigInteger.ONE) && !shadedBigInteger10.equals(shadedBigInteger6)) {
                                    boolean flag = false;

                                    for (int j = 0; j < n2 - 1; j++) {
                                       shadedBigInteger10 = shadedBigInteger10.modPow(TWO, shadedBigInteger);
                                       if (shadedBigInteger10.equals(ShadedBigInteger.ONE)) {
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

                  shadedBigInteger7 = shadedBigInteger7.divide(TWO);
                  n2++;
               }
            }
         }
      }
   }

   public void nextEntropyBytes(byte[] bytes) {
      this.random.nextBytes(bytes);

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
      ShadedBigInteger shadedBigInteger = this.generateRandomBigInteger(n);
      if (!shadedBigInteger.testBit((byte)0)) {
         shadedBigInteger = shadedBigInteger.add(ShadedBigInteger.ONE);
      }

      while (!this.isProbablePrime(shadedBigInteger, n2)) {
         shadedBigInteger = shadedBigInteger.add(TWO);
      }

      return shadedBigInteger;
   }
}
