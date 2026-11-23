package net.augustus.vendor.launcher.crypto.math;

public class FractionMatrix {
   public FractionMatrixRow[] rows;

   public void sortRows() {
      MatrixRowHeapSort.sort(this.rows);
   }

   public FractionMatrix(FractionMatrixRow[] fractionMatrixRows) {
      this.rows = fractionMatrixRows;
   }
}
