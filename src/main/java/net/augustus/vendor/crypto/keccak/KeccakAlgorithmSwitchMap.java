package net.augustus.vendor.crypto.keccak;

public class KeccakAlgorithmSwitchMap {
   public static final int[] ORDINAL_MAP = new int[Sha3Algorithm.values().length];

   static {
      try {
         ORDINAL_MAP[Sha3Algorithm.SHA3_224.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[Sha3Algorithm.SHA3_256.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[Sha3Algorithm.SHA3_384.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         ORDINAL_MAP[Sha3Algorithm.SHA3_512.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }
   }
}
