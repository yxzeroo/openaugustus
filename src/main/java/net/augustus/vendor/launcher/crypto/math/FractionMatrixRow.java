package net.augustus.vendor.launcher.crypto.math;

public class FractionMatrixRow {
   private RationalFraction[] elements;
   private int leadingZeroCount;
   private boolean dirty;
   private static int flowState;

   public int getLeadingZeroCount() {
      if (this.dirty) {
         for (int i = 0; i < this.elements.length; i++) {
            if (!this.elements[i].getNumerator().equals(ShadedBigInteger.ZERO)) {
               this.leadingZeroCount = i;
               return i;
            }
         }

         this.leadingZeroCount = this.elements.length;
      }

      return this.leadingZeroCount;
   }

   public RationalFraction[] getElements() {
      return this.elements;
   }

   public void setElement(long l, RationalFraction rationalFraction, int n) {
      this.dirty = true;
      this.elements[n] = rationalFraction;
   }

   public FractionMatrixRow subtract(FractionMatrixRow fractionMatrixRow) {
      if (fractionMatrixRow.elements.length != this.elements.length) {
         throw new UnsupportedOperationException("Two matrices with an unequal length can not be subtracted!");
      } else {
         RationalFraction[] rationalFractions = new RationalFraction[this.elements.length];

         for (int i = 0; i < this.elements.length; i++) {
            rationalFractions[i] = this.elements[i].subtract2(fractionMatrixRow.elements[i]);
         }

         return new FractionMatrixRow(rationalFractions);
      }
   }

   public FractionMatrixRow(RationalFraction[] rationalFractions) {
      this.dirty = true;
      this.elements = rationalFractions;
   }

   public static void setFlowState(int n) {
      flowState = n;
   }

   public static int getFlowState() {
      return flowState;
   }

   public static int getInvertedFlowMarker() {
      return getFlowState() == 0 ? 82 : 0;
   }

   static {
      setFlowState(4);
   }

}
