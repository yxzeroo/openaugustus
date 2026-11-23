package net.augustus.vendor.crypto.keccak;


public class KeccakTheta {
   private final long[] columnParity = new long[5];
   private final long[] parityEffect = new long[5];

   public void applyTheta(long[][] longsArr) {
      KeccakTheta keccakTheta = this;
      keccakTheta.computeColumnParity(longsArr);
      this.computeParityEffect();
      keccakTheta = this;
      keccakTheta.xorParityEffect(longsArr);
   }

   private void xorParityEffect(long[][] longsArr) {
      boolean flag = false;
      boolean flag2 = false;

      for (int i = 0; i < 5; i++) {
         for (int j = 0; j < 5; j++) {
            longsArr[i][j] = longsArr[i][j] ^ this.parityEffect[i];
         }
      }
   }

   private void computeColumnParity(long[][] longsArr) {
      boolean flag = false;

      for (int i = 0; i < 5; i++) {
         this.columnParity[i] = longsArr[i][0] ^ longsArr[i][1] ^ longsArr[i][2] ^ longsArr[i][3] ^ longsArr[i][4];
      }
   }

   private void computeParityEffect() {
      this.parityEffect[0] = this.columnParity[4] ^ Long.rotateLeft(this.columnParity[1], 1);
      this.parityEffect[1] = this.columnParity[0] ^ Long.rotateLeft(this.columnParity[2], 1);
      this.parityEffect[2] = this.columnParity[1] ^ Long.rotateLeft(this.columnParity[3], 1);
      this.parityEffect[3] = this.columnParity[2] ^ Long.rotateLeft(this.columnParity[4], 1);
      this.parityEffect[4] = this.columnParity[3] ^ Long.rotateLeft(this.columnParity[0], 1);
   }





}
