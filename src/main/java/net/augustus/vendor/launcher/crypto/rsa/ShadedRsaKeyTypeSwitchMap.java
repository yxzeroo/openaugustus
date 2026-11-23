package net.augustus.vendor.launcher.crypto.rsa;

public class ShadedRsaKeyTypeSwitchMap {
   public static final int[] ORDINAL_MAP;

   static {
      long l = 139276311673812L ^ 119680822354719L;
      long l2 = (l ^ 24902666645615L) >>> 32;
      int n = (int)((l ^ 24902666645615L) << 32 >>> 32);
      ORDINAL_MAP = new int[ShadedRsaKeyType.values(l2, n).length];

      try {
         ORDINAL_MAP[ShadedRsaKeyType.RSA.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[ShadedRsaKeyType.PSS.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }
   }
}
