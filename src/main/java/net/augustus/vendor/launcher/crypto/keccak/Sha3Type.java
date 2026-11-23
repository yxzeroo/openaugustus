package net.augustus.vendor.launcher.crypto.keccak;

public enum Sha3Type {
   SHA3_224(224),
   SHA3_256(256),
   SHA3_384(384),
   SHA3_512(512);

   private final int digestBitLength;


   private Sha3Type(int n) {
      this.digestBitLength = n;
   }

   public static Sha3Type[] values(long l) {
      return values();
   }

   public int getDigestBitLength() {
      return this.digestBitLength;
   }


}
