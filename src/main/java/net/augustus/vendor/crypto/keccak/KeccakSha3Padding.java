package net.augustus.vendor.crypto.keccak;


public class KeccakSha3Padding {
   private final Sha3Algorithm algorithm;

   private int getMinimumPaddedBits(int n) {
      return this.getSuffixEndBit(n) + 2;
   }

   public KeccakSha3Padding(Sha3Algorithm sha3Algorithm) {
      this.algorithm = sha3Algorithm;
   }

   private int getZeroPadBitCount(int n) {
      int n2 = 0;
      int[] squeezeTrace = KeccakSqueezer.getSqueezeTrace();
      n2 = this.getMinimumPaddedBits(n);
      return n2 % KeccakParameters.getRateBits(this.algorithm) == 0 ? 0 : KeccakParameters.getRateBits(this.algorithm) - n2 % KeccakParameters.getRateBits(this.algorithm);
   }

   private void setBit(byte[] bytes, int n) {
      byte b = 0;
      KeccakSqueezer.getSqueezeTrace();
      int n2 = 0;
      n2 = n / 8;
      b = (byte)(n % 8);
      bytes[n2] = (byte)(bytes[n2] + (byte)(1 << b));
   }

   private int getSuffixEndBit(int n) {
      return n + "01".length();
   }

   public void pad(byte[] bytes, int n) {
      int n2 = 0;
      KeccakSqueezer.getSqueezeTrace();
      int n3 = 0;
      long l = 0L;
      l = 2912375107686255831L ^ (long)"㎈囟鑳ᑹ뒳\uf3f3逷耲グ킸쨀刦䍊\uf32bല灯㔱겷\uf353꿢鷔夊権№".hashCode();
      KeccakSha3Padding keccakSha3Padding = this;
      n3 = keccakSha3Padding.getSuffixEndBit(n);
      l = l - -4830414927752726335L ^ 2001636306314098790L;
      keccakSha3Padding = this;
      n2 = keccakSha3Padding.getZeroPadBitCount(n);
      l = l + -6857074186809647137L ^ 5092128663208213150L;
      keccakSha3Padding = this;
      byte[] bytes2 = bytes;
      keccakSha3Padding.setBit(bytes2, n3);
      l = l - 9022152212829634951L ^ 3099126017912787631L;
      keccakSha3Padding = this;
      byte[] bytes3 = bytes;
      int n4 = n3 + 1 + n2;
      byte[] bytes4 = bytes3;
      keccakSha3Padding.setBit(bytes4, n4);
      l = (l ^ 412547650036461400L) + 5552296087332069878L;
   }

}
