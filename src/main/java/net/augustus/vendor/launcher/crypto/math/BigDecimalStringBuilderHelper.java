package net.augustus.vendor.launcher.crypto.math;

public class BigDecimalStringBuilderHelper {
   public static final char[] DIGIT_ONES = new char[]{
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9',
      '0',
      '1',
      '2',
      '3',
      '4',
      '5',
      '6',
      '7',
      '8',
      '9'
   };
   public static final char[] DIGIT_TENS = new char[]{
      '0',
      '0',
      '0',
      '0',
      '0',
      '0',
      '0',
      '0',
      '0',
      '0',
      '1',
      '1',
      '1',
      '1',
      '1',
      '1',
      '1',
      '1',
      '1',
      '1',
      '2',
      '2',
      '2',
      '2',
      '2',
      '2',
      '2',
      '2',
      '2',
      '2',
      '3',
      '3',
      '3',
      '3',
      '3',
      '3',
      '3',
      '3',
      '3',
      '3',
      '4',
      '4',
      '4',
      '4',
      '4',
      '4',
      '4',
      '4',
      '4',
      '4',
      '5',
      '5',
      '5',
      '5',
      '5',
      '5',
      '5',
      '5',
      '5',
      '5',
      '6',
      '6',
      '6',
      '6',
      '6',
      '6',
      '6',
      '6',
      '6',
      '6',
      '7',
      '7',
      '7',
      '7',
      '7',
      '7',
      '7',
      '7',
      '7',
      '7',
      '8',
      '8',
      '8',
      '8',
      '8',
      '8',
      '8',
      '8',
      '8',
      '8',
      '9',
      '9',
      '9',
      '9',
      '9',
      '9',
      '9',
      '9',
      '9',
      '9'
   };
   public static final boolean assertionsDisabled = !ShadedBigDecimal.class.desiredAssertionStatus();
   public final StringBuilder sb = new StringBuilder();
   public final char[] cmpCharArray = new char[19];

   public BigDecimalStringBuilderHelper() {
   }

   public StringBuilder getStringBuilder() {
      this.sb.setLength(0);
      return this.sb;
   }

   public char[] getCmpCharArray() {
      return this.cmpCharArray;
   }

   public int putIntCompact(long l) {
      int[] flowState = ShadedNumber.getFlowState();
      if (!assertionsDisabled && l < 0L) {
         throw new AssertionError();
      } else {
         int length = this.cmpCharArray.length;

         while (l > 2147483647L) {
            long l2 = l / 100L;
            int n = (int)(l - l2 * 100L);
            l = l2;
            length--;
            this.cmpCharArray[length] = DIGIT_ONES[n];
            length--;
            this.cmpCharArray[length] = DIGIT_TENS[n];
         }

         int n2 = (int)l;

         while (n2 >= 100) {
            int n3 = n2 / 100;
            int n4 = n2 - n3 * 100;
            n2 = n3;
            length--;
            this.cmpCharArray[length] = DIGIT_ONES[n4];
            length--;
            this.cmpCharArray[length] = DIGIT_TENS[n4];
         }

         length--;
         this.cmpCharArray[length] = DIGIT_ONES[n2];
         if (n2 >= 10) {
            length--;
            this.cmpCharArray[length] = DIGIT_TENS[n2];
         }

         return length;
      }
   }
}
