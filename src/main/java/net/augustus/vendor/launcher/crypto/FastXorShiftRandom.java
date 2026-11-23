package net.augustus.vendor.launcher.crypto;


public class FastXorShiftRandom {
   private long state1;
   private static final long MULTIPLIER_PRIMARY = 0L;
   private static final long MULTIPLIER_SECONDARY = 0L;
   private long state0;

   public void nextBytes(long l, byte[] bytes) {
      int n = 0;
      int length = bytes.length;

      while (n < length) {
         long state02 = this.state0;
         long state12 = this.state1;
         state12 ^= state12 << 23;
         long l2 = state02 + state12;
         this.state0 = state12 ^ state02 ^ state12 >>> 18 ^ state02 >>> 5;
         this.state1 = state02;
         l2 ^= l2 >>> 33;
         l2 *= 2685821657736338717L;
         l2 ^= l2 >>> 33;
         l2 *= 6364136223846793005L;
         l2 ^= l2 >>> 33;

         for (int i = 0; i < 8 && n < length; n++) {
            bytes[n] = (byte)((int)(l2 >> i * 8));
            i++;
         }
      }
   }

   public long nextValue(int n, int n2, int n3, int n4) {
      long state02 = this.state0;
      long state12 = this.state1;
      state12 ^= state12 << 23;
      long l = state02 + state12;
      this.state0 = state12 ^ state02 ^ state12 >>> 18 ^ state02 >>> 5;
      this.state1 = state02;
      l ^= l >>> 33;
      l *= 2685821657736338717L;
      l ^= l >>> 33;
      l *= 6364136223846793005L;
      l ^= l >>> 33;
      switch (n) {
         case 0:
            return l;
         case 1:
            return (long)((int)l);
         case 2:
            if (n2 <= 0) {
               throw new IllegalArgumentException("!");
            } else {
               if ((n2 & -n2) == n2) {
                  return (long)((int)((long)n2 * (long)((int)(l >>> 1)) >>> 31));
               }

               int n5;
               int n6;
               do {
                  n5 = (int)(l >>> 1);
                  n6 = n5 % n2;
               } while (n5 - n6 + (n2 - 1) < 0);

               return (long)n6;
            }
         case 3:
            return (long)((double)(l >>> 11) * 1.1102230246251565E-16);
         default:
            return l;
      }
   }

   public FastXorShiftRandom(long l) {
      this.state0 = System.nanoTime() ^ Runtime.getRuntime().freeMemory();
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
