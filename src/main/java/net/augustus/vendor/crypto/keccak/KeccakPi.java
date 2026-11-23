package net.augustus.vendor.crypto.keccak;

public class KeccakPi {

   public void applyPi(long[][] longsArr) {
      Object obj = null;
      boolean flag = false;
      obj = new long[5][5];

      for (int i = 0; i < ((long[][])longsArr).length; i++) {
         System.arraycopy(longsArr[i], 0, ((long[][])obj)[i], 0, longsArr[i].length);
      }

      longsArr[0][0] = ((long[][])obj)[0][0];
      longsArr[1][3] = ((long[][])obj)[0][1];
      longsArr[2][1] = ((long[][])obj)[0][2];
      longsArr[3][4] = ((long[][])obj)[0][3];
      longsArr[4][2] = ((long[][])obj)[0][4];
      longsArr[0][2] = ((long[][])obj)[1][0];
      longsArr[1][0] = ((long[][])obj)[1][1];
      longsArr[2][3] = ((long[][])obj)[1][2];
      longsArr[3][1] = ((long[][])obj)[1][3];
      longsArr[4][4] = ((long[][])obj)[1][4];
      longsArr[0][4] = ((long[][])obj)[2][0];
      longsArr[1][2] = ((long[][])obj)[2][1];
      longsArr[2][0] = ((long[][])obj)[2][2];
      longsArr[3][3] = ((long[][])obj)[2][3];
      longsArr[4][1] = ((long[][])obj)[2][4];
      longsArr[0][1] = ((long[][])obj)[3][0];
      longsArr[1][4] = ((long[][])obj)[3][1];
      longsArr[2][2] = ((long[][])obj)[3][2];
      longsArr[3][0] = ((long[][])obj)[3][3];
      longsArr[4][3] = ((long[][])obj)[3][4];
      longsArr[0][3] = ((long[][])obj)[4][0];
      longsArr[1][1] = ((long[][])obj)[4][1];
      longsArr[2][4] = ((long[][])obj)[4][2];
      longsArr[3][2] = ((long[][])obj)[4][3];
      longsArr[4][0] = ((long[][])obj)[4][4];
   }

}
