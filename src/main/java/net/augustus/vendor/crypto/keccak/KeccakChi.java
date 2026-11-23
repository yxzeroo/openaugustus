package net.augustus.vendor.crypto.keccak;

public class KeccakChi {

   public void applyChi(long[][] longsArr) {
      boolean flag = false;
      Object obj = null;
      obj = new long[5][5];

      for (int i = 0; i < ((long[][])longsArr).length; i++) {
         System.arraycopy(longsArr[i], 0, ((long[][])obj)[i], 0, longsArr[i].length);
      }

      for (int j = 0; j < 5; j++) {
         longsArr[0][j] = ((long[][])obj)[0][j] ^ ~((long[][])obj)[1][j] & ((long[][])obj)[2][j];
         longsArr[1][j] = ((long[][])obj)[1][j] ^ ~((long[][])obj)[2][j] & ((long[][])obj)[3][j];
         longsArr[2][j] = ((long[][])obj)[2][j] ^ ~((long[][])obj)[3][j] & ((long[][])obj)[4][j];
         longsArr[3][j] = ((long[][])obj)[3][j] ^ ~((long[][])obj)[4][j] & ((long[][])obj)[0][j];
         longsArr[4][j] = ((long[][])obj)[4][j] ^ ~((long[][])obj)[0][j] & ((long[][])obj)[1][j];
      }
   }

}
