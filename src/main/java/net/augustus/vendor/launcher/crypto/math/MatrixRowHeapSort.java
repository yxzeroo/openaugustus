package net.augustus.vendor.launcher.crypto.math;


public class MatrixRowHeapSort {

   public static void sort(FractionMatrixRow[] fractionMatrixRows) {
      int length = fractionMatrixRows.length;

      for (int i = length / 2 - 1; i >= 0; i--) {
         heapify(fractionMatrixRows, length, i);
      }

      for (int j = length - 1; j > 0; j--) {
         FractionMatrixRow fractionMatrixRow = fractionMatrixRows[0];
         fractionMatrixRows[0] = fractionMatrixRows[j];
         fractionMatrixRows[j] = fractionMatrixRow;
         heapify(fractionMatrixRows, j, (byte)0);
      }
   }

   public static void heapify(FractionMatrixRow[] fractionMatrixRows, int n, int n2) {
      int n3 = n2;
      int n4 = 2 * n2 + 1;
      int n5 = 2 * n2 + 2;
      if (n4 < n) {
         if (fractionMatrixRows[n4].getLeadingZeroCount() > fractionMatrixRows[n2].getLeadingZeroCount()) {
            n3 = n4;
         }
      }

      if (n5 < n) {
         if (fractionMatrixRows[n5].getLeadingZeroCount() > fractionMatrixRows[n3].getLeadingZeroCount()) {
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
      int n5 = 2 * n2 + 1;
      int n6 = 2 * n2 + 2;
      if (n5 < n) {
         if (rationalFractionsArr[n5][n3].compareTo(rationalFractionsArr[n2][n3]) == 1) {
            n4 = n5;
         }
      }

      if (n6 < n) {
         if (rationalFractionsArr[n6][n3].compareTo(rationalFractionsArr[n4][n3]) == 1) {
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
