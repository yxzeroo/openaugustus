package net.augustus.vendor.launcher.crypto;

public class EntropyXorShiftRandom {
   private long state0;
   private long state1;
   private static final long MULTIPLIER_PRIMARY = 0L;
   private static final long MULTIPLIER_SECONDARY = 0L;
   private static final long SEED_MIX_MULTIPLIER = 0L;

   public int nextInt(int n) {
      byte b = 2;
      return (int)this.nextValue(b, n);
   }

   public long nextValue(int n, int n2) {
      long state02 = this.state0;
      long state12 = this.state1;
      state12 ^= state12 << 23;
      long l = state02 + state12;
      long l2 = state12 ^ state02 ^ state12 >>> 18 ^ state02 >>> 5;
      this.state0 = l2;
      this.state1 = state02;
      l ^= l >>> 33;
      l *= 2685821657736338717L;
      l ^= l >>> 33;
      Sha256Digest.getFlowState();
      l *= 6364136223846793005L;
      l ^= l >>> 33;
      switch (n) {
         case 0:
            return l;
         case 1:
            return (long)((int)l);
         case 2:
            if (n2 <= 0) {
               throw new IllegalArgumentException("Bound must be positive");
            } else {
               if ((n2 & -n2) == n2) {
                  return (long)((int)((long)n2 * (long)((int)(l >>> 1)) >>> 31));
               }

               int n3;
               int n4;
               do {
                  n3 = (int)(l >>> 1);
                  n4 = n3 % n2;
               } while (n3 - n4 + (n2 - 1) < 0);

               return (long)n4;
            }
         case 3:
            return (long)((double)(l >>> 11) * 1.1102230246251565E-16);
         default:
            return l;
      }
   }

   public boolean nextBoolean() {
      int[] flowState = Sha256Digest.getFlowState();
      byte b = 0;
      byte b2 = 0;
      return (this.nextValue(b2, b) & 1L) != 0L;
   }

   public double nextDouble() {
      byte b = 0;
      byte b2 = 3;
      return (double)this.nextValue(b2, b);
   }

   public void nextBytes(byte[] bytes) {
      this.fillRandomBytes(bytes);
   }

   public EntropyXorShiftRandom() {
      long l = System.nanoTime();
      long l2 = System.currentTimeMillis();
      long l3 = Runtime.getRuntime().freeMemory();
      long l4 = Runtime.getRuntime().totalMemory();
      long id = Thread.currentThread().getId();
      long l5 = l ^ l3 ^ id;
      long l6 = l2 ^ l4 ^ id << 17;
      l5 ^= l5 >>> 33;
      l5 *= 2685821657736338717L;
      Sha256Digest.getFlowState();
      l5 ^= l5 >>> 33;
      l5 *= 6364136223846793005L;
      l5 ^= l5 >>> 33;
      l6 ^= l6 >>> 33;
      l6 *= 2685821657736338717L;
      l6 ^= l6 >>> 33;
      l6 *= 6364136223846793005L;
      l6 ^= l6 >>> 33;

      for (int i = 0; i < 10; i++) {
         long l7 = System.nanoTime();
         l5 ^= l7;
         l5 ^= l5 >>> 33;
         l5 *= 2685821657736338717L;
         l5 ^= l5 >>> 33;
         l5 *= 6364136223846793005L;
         l5 ^= l5 >>> 33;

         for (int j = 0; j < i * 100; j++) {
            l6 += l6 << 5;
         }

         l6 ^= System.nanoTime();
         l6 ^= l6 >>> 33;
         l6 *= 2685821657736338717L;
         l6 ^= l6 >>> 33;
         l6 *= 6364136223846793005L;
         l6 ^= l6 >>> 33;
      }

      Object[] array = new Object[5];

      for (int k = 0; k < array.length; k++) {
         array[k] = new Object();
         l5 ^= (long)array[k].hashCode();
         l6 ^= (long)(array[k].hashCode() << k * 7);
      }

      StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

      for (StackTraceElement stackTraceElement : stackTrace) {
         l5 ^= (long)stackTraceElement.hashCode();
         l6 ^= (long)(stackTraceElement.getLineNumber() * 31);
      }

      l5 = (l5 ^ l5 >>> 30) * 1442695040888963407L;
      l5 = (l5 ^ l5 >>> 27) * 6364136223846793005L;
      l5 ^= l5 >>> 31;
      l6 = (l6 ^ l6 >>> 30) * 1442695040888963407L;
      l6 = (l6 ^ l6 >>> 27) * 6364136223846793005L;
      l6 ^= l6 >>> 31;
      this.state0 = l5;
      this.state1 = l6;

      for (int m = 0; m < 20; m++) {
         long state02 = this.state0;
         long state12 = this.state1;
         state12 ^= state12 << 23;
         long l8 = state12 ^ state02 ^ state12 >>> 18 ^ state02 >>> 5;
         this.state0 = l8;
         this.state1 = state02;
      }
   }

   public long nextLong() {
      byte b = 0;
      byte b2 = 0;
      return this.nextValue(b2, b);
   }

   public int nextInt() {
      byte b = 0;
      byte b2 = 1;
      return (int)this.nextValue(b2, b);
   }

   public void fillRandomBytes(byte[] bytes) {
      int n = 0;
      Sha256Digest.getFlowState();
      int length = bytes.length;

      while (n < length) {
         long state02 = this.state0;
         long state12 = this.state1;
         state12 ^= state12 << 23;
         long l = state02 + state12;
         long l2 = state12 ^ state02 ^ state12 >>> 18 ^ state02 >>> 5;
         this.state0 = l2;
         this.state1 = state02;
         l ^= l >>> 33;
         l *= 2685821657736338717L;
         l ^= l >>> 33;
         l *= 6364136223846793005L;
         l ^= l >>> 33;

         for (int i = 0; i < 8 && n < length; n++) {
            bytes[n] = (byte)((int)(l & 255L));
            l >>= 8;
            i++;
         }
      }
   }

}
