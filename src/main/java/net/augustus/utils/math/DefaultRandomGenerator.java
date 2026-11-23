package net.augustus.utils.math;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public class DefaultRandomGenerator implements RandomNumberGenerator {
   public static RandomNumberGenerator instance = new DefaultRandomGenerator();

   @Override
   public float nextSecureFloat(double d, double d2) {
      if (d == d2) {
         return (float)d;
      } else {
         if (d > d2) {
            double d3 = d;
            d = d2;
            d2 = d3;
         }

         SecureRandom rand = new SecureRandom();
         float f = (float)(d2 - d);
         return (float)(d + (double)(rand.nextFloat() * f));
      }
   }

   @Override
   public float nextFloat2(double d, double d2) {
      float f = (float)d;
      float f2 = (float)d2;
      float f3 = f;
      return this.nextFloat(f3, f2);
   }

   @Override
   public double nextRandomSin() {
      double d = Math.PI * 2;
      double d2 = 0.0;
      return Math.sin(instance.nextDouble(d2, d));
   }

   @Override
   public int nextInt2(int n, int n2) {
      if (n2 == Integer.MAX_VALUE) {
         n2--;
      }

      if (n == Integer.MAX_VALUE) {
         n--;
      }

      if (n == n2) {
         return n;
      } else {
         if (n > n2) {
            int n3 = n;
            n = n2;
            n2 = n3;
         }

         return ThreadLocalRandom.current().nextInt(n, n2 + 1);
      }
   }

   @Override
   public long nextLong(double d, double d2) {
      long l;
      l = (long)d;
      long l2 = (long)d2;
      long l3 = l2;
      long l4 = l;
      return this.nextLong2(l4, l3);
   }


   @Override
   public int nextInt(double d, double d2) {
      int n = (int)d;
      int n2 = (int)d2;
      int n3 = n;
      return this.nextInt2(n3, n2);
   }

   @Override
   public long nextLong2(long l, long l2) {
      if (l == l2) {
         return l;
      } else {
         if (l > l2) {
            long l3 = l;
            l = l2;
            l2 = l3;
         }

         return ThreadLocalRandom.current().nextLong(l, l2 + 1L);
      }
   }

   @Override
   public double nextDouble(double d, double d2) {
      if (d == d2) {
         return d;
      } else {
         if (d > d2) {
            double d3 = d;
            d = d2;
            d2 = d3;
         }

         return ThreadLocalRandom.current().nextDouble(d, d2);
      }
   }

   @Override
   public float nextFloat(float f, float f2) {
      if (f == f2) {
         return f;
      } else {
         if (f > f2) {
            float f3 = f;
            f = f2;
            f2 = f3;
         }

         return (float)ThreadLocalRandom.current().nextDouble((double)f, (double)f2);
      }
   }

   @Override
   public double nextSecureIntAsDouble(double d, double d2) {
      int n = (int)d;
      int n2 = (int)d2;
      int n3 = n2;
      int n4 = n;
      return (double)this.nextSecureInt(n4, n3);
   }

   @Override
   public int nextSecureInt(int n, int n2) {
      if (n == n2) {
         return n;
      } else {
         if (n > n2) {
            int n3 = n;
            n = n2;
            n2 = n3;
         }

         SecureRandom rand = new SecureRandom();
         int n4 = n2 - n;
         return n + rand.nextInt(n4 + 1);
      }
   }

   @Override
   public double nextSecureDouble(double d, double d2) {
      if (d == d2) {
         return d;
      } else {
         if (d > d2) {
            double d3 = d;
            d = d2;
            d2 = d3;
         }

         SecureRandom rand = new SecureRandom();
         double d4 = d2 - d;
         return d + rand.nextDouble() * d4;
      }
   }


}
