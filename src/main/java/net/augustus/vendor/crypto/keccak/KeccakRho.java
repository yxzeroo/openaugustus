package net.augustus.vendor.crypto.keccak;

public class KeccakRho {
   private static final int[][] ROTATION_OFFSETS;

   public void applyRho(long[][] longsArr) {
      longsArr[0][0] = Long.rotateLeft(longsArr[0][0], ROTATION_OFFSETS[0][0]);
      longsArr[0][1] = Long.rotateLeft(longsArr[0][1], ROTATION_OFFSETS[0][1]);
      longsArr[0][2] = Long.rotateLeft(longsArr[0][2], ROTATION_OFFSETS[0][2]);
      longsArr[0][3] = Long.rotateLeft(longsArr[0][3], ROTATION_OFFSETS[0][3]);
      longsArr[0][4] = Long.rotateLeft(longsArr[0][4], ROTATION_OFFSETS[0][4]);
      longsArr[1][0] = Long.rotateLeft(longsArr[1][0], ROTATION_OFFSETS[1][0]);
      longsArr[1][1] = Long.rotateLeft(longsArr[1][1], ROTATION_OFFSETS[1][1]);
      longsArr[1][2] = Long.rotateLeft(longsArr[1][2], ROTATION_OFFSETS[1][2]);
      longsArr[1][3] = Long.rotateLeft(longsArr[1][3], ROTATION_OFFSETS[1][3]);
      longsArr[1][4] = Long.rotateLeft(longsArr[1][4], ROTATION_OFFSETS[1][4]);
      longsArr[2][0] = Long.rotateLeft(longsArr[2][0], ROTATION_OFFSETS[2][0]);
      longsArr[2][1] = Long.rotateLeft(longsArr[2][1], ROTATION_OFFSETS[2][1]);
      longsArr[2][2] = Long.rotateLeft(longsArr[2][2], ROTATION_OFFSETS[2][2]);
      longsArr[2][3] = Long.rotateLeft(longsArr[2][3], ROTATION_OFFSETS[2][3]);
      longsArr[2][4] = Long.rotateLeft(longsArr[2][4], ROTATION_OFFSETS[2][4]);
      longsArr[3][0] = Long.rotateLeft(longsArr[3][0], ROTATION_OFFSETS[3][0]);
      longsArr[3][1] = Long.rotateLeft(longsArr[3][1], ROTATION_OFFSETS[3][1]);
      longsArr[3][2] = Long.rotateLeft(longsArr[3][2], ROTATION_OFFSETS[3][2]);
      longsArr[3][3] = Long.rotateLeft(longsArr[3][3], ROTATION_OFFSETS[3][3]);
      longsArr[3][4] = Long.rotateLeft(longsArr[3][4], ROTATION_OFFSETS[3][4]);
      longsArr[4][0] = Long.rotateLeft(longsArr[4][0], ROTATION_OFFSETS[4][0]);
      longsArr[4][1] = Long.rotateLeft(longsArr[4][1], ROTATION_OFFSETS[4][1]);
      longsArr[4][2] = Long.rotateLeft(longsArr[4][2], ROTATION_OFFSETS[4][2]);
      longsArr[4][3] = Long.rotateLeft(longsArr[4][3], ROTATION_OFFSETS[4][3]);
      longsArr[4][4] = Long.rotateLeft(longsArr[4][4], ROTATION_OFFSETS[4][4]);
   }

   static {
      long[] longs = new long[]{
         -6594325845194571758L,
         7303752621704085518L,
         4819063115540856887L,
         -9175038429115711461L,
         7607477904124936217L,
         -1873099460431577067L,
         -3249801461378318292L,
         3595977805415841833L,
         1719400684194889789L,
         -3613915551643992019L,
         3763232988792356920L,
         3051319829945384968L,
         -8861036827553824729L,
         6814242073558908956L,
         -1368498286993866690L,
         -7046674390674571244L,
         -3657064790259400661L,
         1619558847934890020L,
         2658998048041992207L,
         -8239243447060398070L,
         -4677692295603027962L
      };
      ROTATION_OFFSETS = new int[][]{
         {0, (int)longs[17], 3, (int)longs[7], (int)longs[0]},
         {1, (int)longs[6], (int)longs[19], (int)longs[9], 2},
         {(int)longs[14], (int)longs[20], (int)longs[16], (int)longs[18], (int)longs[8]},
         {(int)longs[13], (int)longs[2], (int)longs[4], (int)longs[5], (int)longs[10]},
         {(int)longs[3], (int)longs[15], (int)longs[12], (int)longs[11], (int)longs[1]}
      };
   }

}
