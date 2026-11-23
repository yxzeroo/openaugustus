package net.augustus.vendor.crypto.rsa;

import net.augustus.vendor.crypto.CustomBigInteger;
import java.util.Random;

public class ProbablePrimeGenerator {
   private static final CustomBigInteger TWO;
   private final Random random;

   public ProbablePrimeGenerator() {
      long l = System.currentTimeMillis();
      l ^= Thread.currentThread().getId();
      l ^= Runtime.getRuntime().freeMemory();
      l ^= System.nanoTime();
      this.random = new Random(l);
   }

   public CustomBigInteger generateRandomBitLength(int n) {
      int n2 = 0;
      Object obj = null;
      int n3 = 0;
      n3 = (n + 7) / 8;
      obj = new byte[n3];
      this.nextEntropyBytes((byte[])obj);
      if (n > 0) {
         n2 = 8 * n3 - n;
         ((byte[])obj)[0] = (byte)(((byte[])obj)[0] & (byte)(255 >>> n2));
      }

      ((byte[])obj)[0] = (byte)(((byte[])obj)[0] | (byte)(1 << 8 - (8 * n3 - n + 1) % 8));
      return new CustomBigInteger(1, (byte[])obj);
   }

   public CustomBigInteger generateProbablePrime(int n, int n2) {
      Object obj = null;
      ProbablePrimeGenerator probablePrimeGenerator = this;
      obj = probablePrimeGenerator.generateRandomBitLength(n);
      if (!((CustomBigInteger)obj).testBit((byte)0)) {
         obj = ((CustomBigInteger)obj).add2(CustomBigInteger.ONE);
      }

      while (true) {
         probablePrimeGenerator = this;
         if (probablePrimeGenerator.isProbablePrime((CustomBigInteger)obj, n2)) {
            return (CustomBigInteger)obj;
         }

         obj = ((CustomBigInteger)obj).add2(TWO);
      }
   }

   private boolean isProbablePrime(CustomBigInteger customBigInteger, int n) {
      boolean flag = false;
      Object obj = null;
      boolean flag2 = false;
      Object obj2 = null;
      boolean flag3 = false;
      boolean flag4 = false;
      Object obj3 = null;
      Object obj4 = null;
      Object obj5 = null;
      Object obj6 = null;
      Object obj7 = null;
      int n2 = 0;
      CustomBigInteger customBigInteger2 = customBigInteger;
      if (customBigInteger2.compareTo2(CustomBigInteger.ONE) <= 0) {
         return false;
      } else {
         customBigInteger2 = customBigInteger;
         if (customBigInteger2.equals(TWO)) {
            return true;
         } else {
            customBigInteger2 = customBigInteger;
            customBigInteger2 = customBigInteger2.mod(TWO);
            if (customBigInteger2.equals(CustomBigInteger.ZERO)) {
               return false;
            } else {
               obj5 = new CustomBigInteger[]{
                  CustomBigInteger.valueOf(3L),
                  CustomBigInteger.valueOf(5L),
                  CustomBigInteger.valueOf(7L),
                  CustomBigInteger.valueOf(11L),
                  CustomBigInteger.valueOf(13L),
                  CustomBigInteger.valueOf(17L),
                  CustomBigInteger.valueOf(19L),
                  CustomBigInteger.valueOf(23L),
                  CustomBigInteger.valueOf(29L)
               };
               obj7 = (CustomBigInteger[])obj5;
               n2 = ((CustomBigInteger[])obj7).length;

               for (int i = 0; i < n2; i++) {
                  obj6 = ((CustomBigInteger[])obj7)[i];
                  customBigInteger2 = customBigInteger;
                  if (customBigInteger2.equals((CustomBigInteger)obj6)) {
                     return true;
                  }

                  customBigInteger2 = customBigInteger;
                  customBigInteger2 = customBigInteger2.mod((CustomBigInteger)obj6);
                  if (customBigInteger2.equals(CustomBigInteger.ZERO)) {
                     return false;
                  }
               }

               customBigInteger2 = customBigInteger;
               obj2 = customBigInteger2.subtract(CustomBigInteger.ONE);
               n2 = 0;
               obj4 = (CustomBigInteger)obj2;

               while (true) {
                  customBigInteger2 = (CustomBigInteger)obj4;
                  customBigInteger2 = customBigInteger2.mod(TWO);
                  if (!customBigInteger2.equals(CustomBigInteger.ZERO)) {
                     for (int j = 0; j < n; j++) {
                        while (true) {
                           customBigInteger2 = new CustomBigInteger(customBigInteger.bitLength(), this.random);
                           obj = customBigInteger2.mod(customBigInteger);
                           customBigInteger2 = (CustomBigInteger)obj;
                           if (customBigInteger2.compareTo2(CustomBigInteger.ONE) > 0) {
                              customBigInteger2 = (CustomBigInteger)obj;
                              if (customBigInteger2.compareTo2((CustomBigInteger)obj2) < 0) {
                                 customBigInteger2 = (CustomBigInteger)obj;
                                 obj3 = customBigInteger2.modPow((CustomBigInteger)obj4, customBigInteger);
                                 customBigInteger2 = (CustomBigInteger)obj3;
                                 if (!customBigInteger2.equals(CustomBigInteger.ONE)) {
                                    customBigInteger2 = (CustomBigInteger)obj3;
                                    if (!customBigInteger2.equals((CustomBigInteger)obj2)) {
                                       flag2 = false;

                                       for (int k = 0; k < n2 - 1; k++) {
                                          customBigInteger2 = (CustomBigInteger)obj3;
                                          obj3 = customBigInteger2.modPow(TWO, customBigInteger);
                                          customBigInteger2 = (CustomBigInteger)obj3;
                                          if (customBigInteger2.equals(CustomBigInteger.ONE)) {
                                             return false;
                                          }

                                          customBigInteger2 = (CustomBigInteger)obj3;
                                          if (customBigInteger2.equals((CustomBigInteger)obj2)) {
                                             flag2 = true;
                                             break;
                                          }
                                       }

                                       if (!flag2) {
                                          return false;
                                       }
                                    }
                                 }
                                 break;
                              }
                           }
                        }
                     }

                     return true;
                  }

                  customBigInteger2 = (CustomBigInteger)obj4;
                  obj4 = customBigInteger2.divide(TWO);
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
      TWO = CustomBigInteger.valueOf(2L);
   }

}
