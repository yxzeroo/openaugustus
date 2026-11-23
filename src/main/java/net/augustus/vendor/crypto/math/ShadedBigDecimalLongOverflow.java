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
      int[] opaqueStateTag = CustomBigInteger.getOpaqueStateTag();
      obj = ShadedBigDecimal.accessInflated(shadedBigDecimal);
      CustomBigInteger customBigInteger = (CustomBigInteger)obj;
      CustomBigInteger customBigInteger2 = LONGMIN;
      if (customBigInteger.compareTo2(customBigInteger2) >= 0) {
         customBigInteger = (CustomBigInteger)obj;
         CustomBigInteger customBigInteger3 = LONGMAX;
         if (customBigInteger.compareTo2(customBigInteger3) <= 0) {
            return;
         }
      }

      throw new ArithmeticException("Overflow");
   }

   private ShadedBigDecimalLongOverflow() {
   }

}
