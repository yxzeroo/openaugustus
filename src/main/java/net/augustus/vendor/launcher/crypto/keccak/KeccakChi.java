package net.augustus.vendor.launcher.crypto.keccak;


public class KeccakChi {

   public void apply(long[][] longsArr) {
      long[][] longsArr2 = new long[5][5];

      for (int i = 0; i < longsArr.length; i++) {
         System.arraycopy(longsArr[i], 0, longsArr2[i], 0, longsArr[i].length);
      }

      for (int j = 0; j < 5; j++) {
         longsArr[0][j] = longsArr2[0][j] ^ ~longsArr2[1][j] & longsArr2[2][j];
         longsArr[1][j] = longsArr2[1][j] ^ ~longsArr2[2][j] & longsArr2[3][j];
         longsArr[2][j] = longsArr2[2][j] ^ ~longsArr2[3][j] & longsArr2[4][j];
         longsArr[3][j] = longsArr2[3][j] ^ ~longsArr2[4][j] & longsArr2[0][j];
         longsArr[4][j] = longsArr2[4][j] ^ ~longsArr2[0][j] & longsArr2[1][j];
      }
   }

}
