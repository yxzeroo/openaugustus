package net.augustus.vendor.launcher.crypto.math;

public class FractionMatrixRow {
   private RationalFraction[] elements;
   private int leadingZeroCount;
   private boolean dirty;
   private static int flowState;

   public int getLeadingZeroCount() {
      int flowState2 = getFlowState();
      if (this.dirty) {
         for (int i = 0; i < this.elements.length; i++) {
            ShadedBigInteger numerator = this.elements[i].getNumerator();
            ShadedBigInteger zero = ShadedBigInteger.ZERO;
            if (!numerator.equals(zero)) {
               this.leadingZeroCount = i;
               return i;
            }
         }

         int length = this.elements.length;
         this.leadingZeroCount = length;
      }

      return this.leadingZeroCount;
   }

   public RationalFraction[] getElements() {
      return this.elements;
   }

   public void setElement(long l, RationalFraction rationalFraction, int n) {
      boolean flag = true;
      this.dirty = flag;
      this.elements[n] = rationalFraction;
   }

   public FractionMatrixRow subtract(FractionMatrixRow fractionMatrixRow) {
      int flowState2 = getFlowState();
      if (fractionMatrixRow.elements.length != this.elements.length) {
         throw new UnsupportedOperationException("Two matrices with an unequal length can not be subtracted!");
      } else {
         RationalFraction[] rationalFractions = new RationalFraction[this.elements.length];

         for (int i = 0; i < this.elements.length; i++) {
            RationalFraction rationalFraction = this.elements[i];
            RationalFraction rationalFraction2 = fractionMatrixRow.elements[i];
            RationalFraction rationalFraction3 = rationalFraction2;
            rationalFractions[i] = rationalFraction.subtract2(rationalFraction3);
         }

         return new FractionMatrixRow(rationalFractions);
      }
   }

   public FractionMatrixRow(RationalFraction[] rationalFractions) {
      boolean flag = true;
      this.dirty = flag;
      this.elements = rationalFractions;
   }

   public static void setFlowState(int n) {
      flowState = n;
   }

   public static int getFlowState() {
      return flowState;
   }

   public static int getInvertedFlowMarker() {
      int flowState2 = getFlowState();
      return flowState2 == 0 ? 82 : 0;
   }

   static {
      setFlowState(4);
   }

}
