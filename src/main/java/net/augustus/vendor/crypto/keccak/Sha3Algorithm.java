package net.augustus.vendor.crypto.keccak;

public enum Sha3Algorithm {
   SHA3_224(224),
   SHA3_256(256),
   SHA3_384(384),
   SHA3_512(512);

   private final int outputBitLength;


   public int getOutputBitLength() {
      return this.outputBitLength;
   }

   private Sha3Algorithm(int n) {
      this.outputBitLength = n;
   }


}
