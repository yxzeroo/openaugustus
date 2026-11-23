package net.augustus.vendor.launcher.crypto.keccak;

public class KeccakRho {
   private static final int[][] ROTATION_OFFSETS;

   static {
      long[] longs = new long[]{
         699963335763820558L,
         -3650161918821269443L,
         1101140498417975352L,
         8018028424880717868L,
         8368080689140072475L,
         -809488566634676216L,
         2360251349976743942L,
         4362748962331951143L,
         2980205097193046052L,
         -407476574007328758L,
         -4792587180486688706L,
         -7397000132681531347L,
         -4899025824520339435L,
         3250054392002379794L,
         5486275289858179100L,
         -4990419309785251797L,
         -4742535761922883569L,
         1320466873153749047L,
         5124638777247006745L,
         -237163184937500631L,
         -6596424422049972204L
      };
      ROTATION_OFFSETS = new int[][]{
         {0, (int)longs[8], 3, (int)longs[19], (int)longs[13]},
         {1, (int)longs[3], (int)longs[9], (int)longs[11], 2},
         {(int)longs[10], (int)longs[6], (int)longs[15], (int)longs[16], (int)longs[1]},
         {(int)longs[14], (int)longs[17], (int)longs[18], (int)longs[12], (int)longs[2]},
         {(int)longs[4], (int)longs[20], (int)longs[7], (int)longs[5], (int)longs[0]}
      };
   }

   public void apply(long[][] longsArr) {
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
}
