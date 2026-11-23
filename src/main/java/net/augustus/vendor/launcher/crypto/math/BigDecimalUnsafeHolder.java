package net.augustus.vendor.launcher.crypto.math;

import sun.misc.Unsafe;

public class BigDecimalUnsafeHolder {
   private static final Unsafe unsafe;
   private static final long intValOffset;
   private static final long intCompactOffset;

   public static void setIntValVolatile(ShadedBigDecimal shadedBigDecimal, ShadedBigInteger shadedBigInteger) {
      unsafe.putObjectVolatile(shadedBigDecimal, intValOffset, shadedBigInteger);
   }

   static {
      try {
         unsafe = Unsafe.getUnsafe();
         intCompactOffset = unsafe.objectFieldOffset(ShadedBigDecimal.class.getDeclaredField("413"));
         intValOffset = unsafe.objectFieldOffset(ShadedBigDecimal.class.getDeclaredField("44"));
      } catch (Exception e) {
         throw new ExceptionInInitializerError(e);
      }
   }

   private BigDecimalUnsafeHolder() {
   }

   public static void setIntCompactVolatile(ShadedBigDecimal shadedBigDecimal, long l) {
      unsafe.putLongVolatile(shadedBigDecimal, intCompactOffset, l);
   }
}
