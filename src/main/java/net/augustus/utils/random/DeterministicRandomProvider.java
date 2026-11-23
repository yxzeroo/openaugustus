package net.augustus.utils.random;


import net.augustus.utils.math.RandomNumberGenerator;
public class DeterministicRandomProvider implements RandomNumberGenerator {
   @Override
   public int nextInt2(int n, int n2) {
      return n;
   }

   @Override
   public float nextSecureFloat(double d, double d2) {
      return (float)d2;
   }

   @Override
   public float nextFloat(float f, float f2) {
      return f2;
   }

   @Override
   public double nextDouble(double d, double d2) {
      return d2;
   }

   @Override
   public long nextLong2(long l, long l2) {
      return l2;
   }

   @Override
   public double nextRandomSin() {
      return 0.5;
   }

   @Override
   public long nextLong(double d, double d2) {
      return (long)d;
   }

   @Override
   public int nextSecureInt(int n, int n2) {
      return n;
   }

   @Override
   public double nextSecureIntAsDouble(double d, double d2) {
      return d;
   }

   @Override
   public double nextSecureDouble(double d, double d2) {
      return d2;
   }

   @Override
   public float nextFloat2(double d, double d2) {
      return (float)d;
   }

   @Override
   public int nextInt(double d, double d2) {
      return (int)d2;
   }
}
