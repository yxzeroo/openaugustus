package net.augustus.vendor.launcher.crypto.keccak;

public class KeccakTheta {
   private final long[] columnParity = new long[5];
   private final long[] thetaEffect = new long[5];

   private void computeColumnParity(long[][] longsArr) {

      for (int i = 0; i < 5; i++) {
         this.columnParity[i] = longsArr[i][0] ^ longsArr[i][1] ^ longsArr[i][2] ^ longsArr[i][3] ^ longsArr[i][4];
      }
   }

   private void applyThetaEffect(long[][] longsArr) {

      for (int i = 0; i < 5; i++) {
         for (int j = 0; j < 5; j++) {
            longsArr[i][j] = longsArr[i][j] ^ this.thetaEffect[i];
         }
      }
   }

   private void computeThetaEffect() {
      this.thetaEffect[0] = this.columnParity[4] ^ Long.rotateLeft(this.columnParity[1], 1);
      this.thetaEffect[1] = this.columnParity[0] ^ Long.rotateLeft(this.columnParity[2], 1);
      this.thetaEffect[2] = this.columnParity[1] ^ Long.rotateLeft(this.columnParity[3], 1);
      this.thetaEffect[3] = this.columnParity[2] ^ Long.rotateLeft(this.columnParity[4], 1);
      this.thetaEffect[4] = this.columnParity[3] ^ Long.rotateLeft(this.columnParity[0], 1);
   }

   public void apply(long[][] longsArr) {
      this.computeColumnParity(longsArr);
      this.computeThetaEffect();
      this.applyThetaEffect(longsArr);
   }




}
