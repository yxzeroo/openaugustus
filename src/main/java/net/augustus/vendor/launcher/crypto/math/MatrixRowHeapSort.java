package net.augustus.vendor.launcher.crypto.math;


import net.augustus.vendor.launcher.crypto.Sha256Digest;
public class MatrixRowHeapSort {

   public static void sort(FractionMatrixRow[] fractionMatrixRows) {
      int length = fractionMatrixRows.length;
      Sha256Digest.getFlowState();

      for (int i = length / 2 - 1; i >= 0; i--) {
         heapify(fractionMatrixRows, length, i);
      }

      for (int j = length - 1; j > 0; j--) {
         FractionMatrixRow fractionMatrixRow = fractionMatrixRows[0];
         fractionMatrixRows[0] = fractionMatrixRows[j];
         fractionMatrixRows[j] = fractionMatrixRow;
         byte b = 0;
         heapify(fractionMatrixRows, j, b);
      }
   }

   public static void heapify(FractionMatrixRow[] fractionMatrixRows, int n, int n2) {
      int n3 = n2;
      int n4 = 2 * n2 + 1;
      Sha256Digest.getFlowState();
      int n5 = 2 * n2 + 2;
      if (n4 < n) {
         FractionMatrixRow fractionMatrixRow = fractionMatrixRows[n4];
         int leadingZeroCount = fractionMatrixRow.getLeadingZeroCount();
         FractionMatrixRow fractionMatrixRow2 = fractionMatrixRows[n2];
         if (leadingZeroCount > fractionMatrixRow2.getLeadingZeroCount()) {
            n3 = n4;
         }
      }

      if (n5 < n) {
         FractionMatrixRow fractionMatrixRow3 = fractionMatrixRows[n5];
         int leadingZeroCount2 = fractionMatrixRow3.getLeadingZeroCount();
         FractionMatrixRow fractionMatrixRow4 = fractionMatrixRows[n3];
         if (leadingZeroCount2 > fractionMatrixRow4.getLeadingZeroCount()) {
            n3 = n5;
         }
      }

      if (n3 != n2) {
         FractionMatrixRow fractionMatrixRow5 = fractionMatrixRows[n2];
         fractionMatrixRows[n2] = fractionMatrixRows[n3];
         fractionMatrixRows[n3] = fractionMatrixRow5;
         heapify(fractionMatrixRows, n, n3);
      }
   }

   public static void heapifyByColumn(RationalFraction[][] rationalFractionsArr, int n, int n2, int n3) {
      int n4 = n2;
      Sha256Digest.getFlowState();
      int n5 = 2 * n2 + 1;
      int n6 = 2 * n2 + 2;
      if (n5 < n) {
         RationalFraction rationalFraction = rationalFractionsArr[n5][n3];
         RationalFraction rationalFraction2 = rationalFractionsArr[n2][n3];
         if (rationalFraction.compareTo(rationalFraction2) == 1) {
            n4 = n5;
         }
      }

      if (n6 < n) {
         RationalFraction rationalFraction3 = rationalFractionsArr[n6][n3];
         RationalFraction rationalFraction4 = rationalFractionsArr[n4][n3];
         if (rationalFraction3.compareTo(rationalFraction4) == 1) {
            n4 = n6;
         }
      }

      if (n4 != n2) {
         RationalFraction[] rationalFractions = rationalFractionsArr[n2];
         rationalFractionsArr[n2] = rationalFractionsArr[n4];
         rationalFractionsArr[n4] = rationalFractions;
         heapifyByColumn(rationalFractionsArr, n, n4, n3);
      }
   }
}
