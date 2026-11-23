package net.augustus.vendor.launcher.crypto.keccak;

public class Sha3TypeSwitchMap {
   public static final int[] ORDINAL_MAP;

   static {
      ORDINAL_MAP = new int[Sha3Type.values((85769596696145L ^ 114852672393575L) ^ 617402426584L).length];

      try {
         ORDINAL_MAP[Sha3Type.SHA3_224.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[Sha3Type.SHA3_256.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[Sha3Type.SHA3_384.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         ORDINAL_MAP[Sha3Type.SHA3_512.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }
   }
}
