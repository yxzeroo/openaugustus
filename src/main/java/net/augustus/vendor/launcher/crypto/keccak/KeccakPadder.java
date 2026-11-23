package net.augustus.vendor.launcher.crypto.keccak;

public class KeccakPadder {
   private final Sha3Type sha3Type;

   private int getPaddingStartBit(int n) {
      return n + "01".length();
   }

   private int computeZeroPaddingBits(int n) {
      int minPaddedBitLength = this.getMinPaddedBitLength(n);
      return minPaddedBitLength % KeccakParameters.getRate(this.sha3Type) != 0 ? KeccakParameters.getRate(this.sha3Type) - minPaddedBitLength % KeccakParameters.getRate(this.sha3Type) : 0;
   }

   private int getMinPaddedBitLength(int n) {
      return this.getPaddingStartBit(n) + 2;
   }

   public KeccakPadder(Sha3Type sha3Type2) {
      this.sha3Type = sha3Type2;
   }

   private void setBit(byte[] bytes, int n) {
      int n2 = n / 8;
      byte b = (byte)(n % 8);
      bytes[n2] += (byte)(1 << b);
   }

   public void pad(byte[] bytes, int n) {
      int paddingStartBit = this.getPaddingStartBit(n);
      int n2 = this.computeZeroPaddingBits(n);
      this.setBit(bytes, paddingStartBit);
      int n3 = paddingStartBit + 1 + n2;
      this.setBit(bytes, n3);
   }


}
