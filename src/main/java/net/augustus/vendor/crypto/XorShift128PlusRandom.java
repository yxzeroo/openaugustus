package net.augustus.vendor.crypto;


public class XorShift128PlusRandom {
   private static long MIX_MULTIPLIER_B;
   private static long MIX_MULTIPLIER_A;
   private long state0 = System.nanoTime() ^ Runtime.getRuntime().freeMemory();
   private long state1;

   public void nextBytes(byte[] bytes, long l) {
      int n = 0;
      int n2 = 0;
      long l2 = 0L;
      long l3 = 0L;
      boolean flag = false;
      long l4 = 0L;
      n = 0;
      n2 = bytes.length;

      while (n < n2) {
         l4 = this.state0;
         l3 = this.state1;
         l3 ^= l3 << 23;
         l2 = l4 + l3;
         this.state0 = l3 ^ l4 ^ l3 >>> 18 ^ l4 >>> 5;
         this.state1 = l4;
         l2 ^= l2 >>> 33;
         l2 *= 2685821657736338717L;
         l2 ^= l2 >>> 33;
         l2 *= 6364136223846793005L;
         l2 ^= l2 >>> 33;

         for (int i = 0; i < 8 && n < n2; n++) {
            bytes[n] = (byte)((int)(l2 >> i * 8));
            i++;
         }
      }
   }

   public long nextValue(int n, long l, int n2) {
      long l2 = 0L;
      int n3 = 0;
      int n4 = 0;
      boolean flag = false;
      long l3 = 0L;
      long l4 = 0L;
      l4 = this.state0;
      l3 = this.state1;
      l3 ^= l3 << 23;
      l2 = l4 + l3;
      this.state0 = l3 ^ l4 ^ l3 >>> 18 ^ l4 >>> 5;
      this.state1 = l4;
      l2 ^= l2 >>> 33;
      l2 *= 2685821657736338717L;
      l2 ^= l2 >>> 33;
      l2 *= 6364136223846793005L;
      l2 ^= l2 >>> 33;
      if (n == 3) {
         return (long)((double)(l2 >>> 11) * 1.110223E-16F);
      } else if (n == 0) {
         return l2;
      } else if (n != 2) {
         return n == 1 ? (long)((int)l2) : l2;
      } else if (n2 <= 0) {
         throw new IllegalArgumentException("!");
      } else if ((n2 & -n2) == n2) {
         return (long)((int)((long)n2 * (long)((int)(l2 >>> 1)) >>> 31));
      } else {
         do {
            n3 = (int)(l2 >>> 1);
            n4 = n3 % n2;
         } while (n3 - n4 + (n2 - 1) < 0);

         return (long)n4;
      }
   }

   public XorShift128PlusRandom(short s, int n, int n2) {
      this.state1 = System.currentTimeMillis() ^ Runtime.getRuntime().totalMemory();

      for (int i = 0; i < 15; i++) {
         this.state0 = this.state0 ^ System.nanoTime();
         this.state0 = (this.state0 ^ this.state0 >>> 30) * 2685821657736338717L ^ (this.state0 << i) + (long)i;
         Object obj = new Object();
         this.state1 = this.state1 ^ (long)obj.hashCode() ^ System.nanoTime();
         this.state1 = (this.state1 ^ this.state1 >>> 27) * 6364136223846793005L ^ Thread.currentThread().getId() << i;
         this.state0 = this.state0 ^ this.state1;
         this.state1 = this.state1 ^ this.state0;
      }

      for (int j = 0; j < 20; j++) {
         long state02 = this.state0;
         long state12 = this.state1;
         state12 ^= state12 << 23;
         this.state0 = state12 ^ state02 ^ state12 >>> 18 ^ state02 >>> 5;
         this.state1 = state02;
      }
   }

}
