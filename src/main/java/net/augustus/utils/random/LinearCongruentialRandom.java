package net.augustus.utils.random;

public class LinearCongruentialRandom {
   private final int increment;
   private final int multiplier;
   private long seed;
   private final int modulus;

   public long nextLong(int n, char ch, char ch2) {
      long maxvalue = Long.MAX_VALUE;
      this.seed = (this.seed * 1103515245L + 12345L) % maxvalue;
      return this.seed;
   }

   public int nextIntInRange(long l, int n, int n2) {
      int n3 = n2 - n + 1;
      this.seed = (this.seed * 1103515245L + 12345L) % (long)n3;
      if (this.seed < 0L) {
         this.seed *= -1L;
      }

      return n + (int)this.seed;
   }

   public long nextLongInRange(long l, long l2, long l3) {
      long l4 = l2 - l + 1L;
      this.seed = (this.seed * 1103515245L + 12345L) % l4;
      if (this.seed < 0L) {
         this.seed *= -1L;
      }

      return l + this.seed;
   }

   public LinearCongruentialRandom(short s, long l, long l2) {
      this.multiplier = 1103515245;
      this.increment = 12345;
      this.modulus = Integer.MAX_VALUE;
      this.seed = l;
   }
}
