package net.augustus.vendor.launcher.crypto.math;

import sun.misc.Unsafe;

public class BigIntegerUnsafeHolder {
   private static final Unsafe unsafe;
   private static final long magOffset;
   private static final long signumOffset;

   private BigIntegerUnsafeHolder() {
   }

   public static void putMag(ShadedBigInteger shadedBigInteger, int[] ints) {
      unsafe.putObjectVolatile(shadedBigInteger, magOffset, ints);
   }

   public static void putSign(ShadedBigInteger shadedBigInteger, int n) {
      unsafe.putIntVolatile(shadedBigInteger, signumOffset, n);
   }

   static {
      try {
         unsafe = Unsafe.getUnsafe();
         signumOffset = unsafe.objectFieldOffset(ShadedBigInteger.class.getDeclaredField("41w"));
         magOffset = unsafe.objectFieldOffset(ShadedBigInteger.class.getDeclaredField("47"));
      } catch (Exception e) {
         throw new ExceptionInInitializerError(e);
      }
   }
}
