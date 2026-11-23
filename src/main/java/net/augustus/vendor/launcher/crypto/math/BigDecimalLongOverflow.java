package net.augustus.vendor.launcher.crypto.math;

public class BigDecimalLongOverflow {
   private static final ShadedBigInteger LONGMIN;
   private static final ShadedBigInteger LONGMAX;

   public static void check(ShadedBigDecimal shadedBigDecimal) {
      int[] flowState = ShadedNumber.getFlowState();
      ShadedBigInteger inflatedValue = ShadedBigDecimal.getInflatedValue(shadedBigDecimal);
      ShadedBigInteger shadedBigInteger = LONGMIN;
      if (inflatedValue.compareTo2(shadedBigInteger) >= 0) {
         ShadedBigInteger shadedBigInteger2 = LONGMAX;
         if (inflatedValue.compareTo2(shadedBigInteger2) <= 0) {
            return;
         }
      }

      throw new ArithmeticException("Overflow");
   }

   private BigDecimalLongOverflow() {
   }

   static {
      long[] longs = new long[]{Long.MIN_VALUE, Long.MAX_VALUE};
      LONGMIN = ShadedBigInteger.valueOf(longs[0]);
      LONGMAX = ShadedBigInteger.valueOf(longs[1]);
   }

}
