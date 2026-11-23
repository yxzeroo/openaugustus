package net.augustus.vendor.crypto.math;



import net.augustus.vendor.crypto.CustomBigInteger;
public class ShadedBigDecimalLongOverflow {
   private static final CustomBigInteger LONGMAX;
   private static final CustomBigInteger LONGMIN;

   static {
      LONGMIN = CustomBigInteger.valueOf(Long.MIN_VALUE);
      LONGMAX = CustomBigInteger.valueOf(Long.MAX_VALUE);
   }

   public static void check(ShadedBigDecimal shadedBigDecimal) {
      Object obj = null;
      obj = ShadedBigDecimal.accessInflated(shadedBigDecimal);
      CustomBigInteger customBigInteger = (CustomBigInteger)obj;
      if (customBigInteger.compareTo2(LONGMIN) >= 0) {
         customBigInteger = (CustomBigInteger)obj;
         if (customBigInteger.compareTo2(LONGMAX) <= 0) {
            return;
         }
      }

      throw new ArithmeticException("Overflow");
   }

   private ShadedBigDecimalLongOverflow() {
   }

}
