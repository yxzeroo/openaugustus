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
      ProbablePrimeGenerator probablePrimeGenerator = this;
      byte[] bytes = (byte[])obj;
      probablePrimeGenerator.nextEntropyBytes(bytes);
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
      CustomBigInteger customBigInteger = (CustomBigInteger)obj;
      byte b = 0;
      if (!customBigInteger.testBit(b)) {
         CustomBigInteger customBigInteger2 = (CustomBigInteger)obj;
         CustomBigInteger one = CustomBigInteger.ONE;
         obj = customBigInteger2.add2(one);
      }

      while (true) {
         probablePrimeGenerator = this;
         CustomBigInteger customBigInteger3 = (CustomBigInteger)obj;
         if (probablePrimeGenerator.isProbablePrime(customBigInteger3, n2)) {
            return (CustomBigInteger)obj;
         }

         CustomBigInteger customBigInteger4 = (CustomBigInteger)obj;
         CustomBigInteger customBigInteger5 = TWO;
         obj = customBigInteger4.add2(customBigInteger5);
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
      CustomBigInteger one = CustomBigInteger.ONE;
      if (customBigInteger2.compareTo2(one) <= 0) {
         return false;
      } else {
         customBigInteger2 = customBigInteger;
         CustomBigInteger customBigInteger3 = TWO;
         if (customBigInteger2.equals(customBigInteger3)) {
            return true;
         } else {
            customBigInteger2 = customBigInteger;
            CustomBigInteger customBigInteger4 = TWO;
            customBigInteger2 = customBigInteger2.mod(customBigInteger4);
            CustomBigInteger zero = CustomBigInteger.ZERO;
            if (customBigInteger2.equals(zero)) {
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
                  CustomBigInteger customBigInteger5 = (CustomBigInteger)obj6;
                  if (customBigInteger2.equals(customBigInteger5)) {
                     return true;
                  }

                  customBigInteger2 = customBigInteger;
                  CustomBigInteger customBigInteger6 = (CustomBigInteger)obj6;
                  CustomBigInteger customBigInteger7 = customBigInteger6;
                  customBigInteger2 = customBigInteger2.mod(customBigInteger7);
                  CustomBigInteger zero2 = CustomBigInteger.ZERO;
                  if (customBigInteger2.equals(zero2)) {
                     return false;
                  }
               }

               customBigInteger2 = customBigInteger;
               CustomBigInteger one2 = CustomBigInteger.ONE;
               obj2 = customBigInteger2.subtract(one2);
               n2 = 0;
               obj4 = (CustomBigInteger)obj2;

               while (true) {
                  customBigInteger2 = (CustomBigInteger)obj4;
                  CustomBigInteger customBigInteger8 = TWO;
                  customBigInteger2 = customBigInteger2.mod(customBigInteger8);
                  CustomBigInteger zero3 = CustomBigInteger.ZERO;
                  if (!customBigInteger2.equals(zero3)) {
                     for (int j = 0; j < n; j++) {
                        while (true) {
                           customBigInteger2 = new CustomBigInteger(customBigInteger.bitLength(), this.random);
                           CustomBigInteger customBigInteger9 = customBigInteger;
                           CustomBigInteger customBigInteger10 = customBigInteger9;
                           obj = customBigInteger2.mod(customBigInteger10);
                           customBigInteger2 = (CustomBigInteger)obj;
                           CustomBigInteger one3 = CustomBigInteger.ONE;
                           if (customBigInteger2.compareTo2(one3) > 0) {
                              customBigInteger2 = (CustomBigInteger)obj;
                              CustomBigInteger customBigInteger11 = (CustomBigInteger)obj2;
                              if (customBigInteger2.compareTo2(customBigInteger11) < 0) {
                                 customBigInteger2 = (CustomBigInteger)obj;
                                 CustomBigInteger customBigInteger12 = (CustomBigInteger)obj4;
                                 CustomBigInteger customBigInteger13 = customBigInteger;
                                 CustomBigInteger customBigInteger14 = customBigInteger12;
                                 obj3 = customBigInteger2.modPow(customBigInteger14, customBigInteger13);
                                 customBigInteger2 = (CustomBigInteger)obj3;
                                 CustomBigInteger one4 = CustomBigInteger.ONE;
                                 if (!customBigInteger2.equals(one4)) {
                                    customBigInteger2 = (CustomBigInteger)obj3;
                                    CustomBigInteger customBigInteger15 = (CustomBigInteger)obj2;
                                    if (!customBigInteger2.equals(customBigInteger15)) {
                                       flag2 = false;

                                       for (int k = 0; k < n2 - 1; k++) {
                                          customBigInteger2 = (CustomBigInteger)obj3;
                                          CustomBigInteger customBigInteger16 = customBigInteger;
                                          CustomBigInteger customBigInteger17 = TWO;
                                          obj3 = customBigInteger2.modPow(customBigInteger17, customBigInteger16);
                                          customBigInteger2 = (CustomBigInteger)obj3;
                                          CustomBigInteger one5 = CustomBigInteger.ONE;
                                          if (customBigInteger2.equals(one5)) {
                                             return false;
                                          }

                                          customBigInteger2 = (CustomBigInteger)obj3;
                                          CustomBigInteger customBigInteger18 = (CustomBigInteger)obj2;
                                          if (customBigInteger2.equals(customBigInteger18)) {
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
                  CustomBigInteger customBigInteger19 = TWO;
                  obj4 = customBigInteger2.divide(customBigInteger19);
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
