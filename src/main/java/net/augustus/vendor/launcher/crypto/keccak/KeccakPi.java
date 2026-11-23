package net.augustus.vendor.launcher.crypto.keccak;

public class KeccakPi {
   public void apply(long[][] longsArr) {
      long[][] longsArr2 = new long[5][5];

      for (int i = 0; i < longsArr.length; i++) {
         System.arraycopy(longsArr[i], 0, longsArr2[i], 0, longsArr[i].length);
      }

      longsArr[0][0] = longsArr2[0][0];
      longsArr[1][3] = longsArr2[0][1];
      longsArr[2][1] = longsArr2[0][2];
      longsArr[3][4] = longsArr2[0][3];
      longsArr[4][2] = longsArr2[0][4];
      longsArr[0][2] = longsArr2[1][0];
      longsArr[1][0] = longsArr2[1][1];
      longsArr[2][3] = longsArr2[1][2];
      longsArr[3][1] = longsArr2[1][3];
      longsArr[4][4] = longsArr2[1][4];
      longsArr[0][4] = longsArr2[2][0];
      longsArr[1][2] = longsArr2[2][1];
      longsArr[2][0] = longsArr2[2][2];
      longsArr[3][3] = longsArr2[2][3];
      longsArr[4][1] = longsArr2[2][4];
      longsArr[0][1] = longsArr2[3][0];
      longsArr[1][4] = longsArr2[3][1];
      longsArr[2][2] = longsArr2[3][2];
      longsArr[3][0] = longsArr2[3][3];
      longsArr[4][3] = longsArr2[3][4];
      longsArr[0][3] = longsArr2[4][0];
      longsArr[1][1] = longsArr2[4][1];
      longsArr[2][4] = longsArr2[4][2];
      longsArr[3][2] = longsArr2[4][3];
      longsArr[4][0] = longsArr2[4][4];
   }
}
