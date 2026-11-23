package net.augustus.vendor.launcher.crypto.rsa;

import java.security.InvalidKeyException;
import java.security.ProviderException;

public enum ShadedRsaKeyType {
   RSA("RSA"),
   PSS("RSASSA-PSS");

   private static final ShadedRsaKeyType[] VALUES = new ShadedRsaKeyType[]{RSA, ShadedRsaKeyType.PSS};
   private final String keyAlgo;
   private static final long a = 11597550550925L;

   private ShadedRsaKeyType(String str) {
      this.keyAlgo = str;
   }

   public static ShadedRsaKeyType lookup(long l, String str) throws InvalidKeyException, ProviderException {
      l = a ^ l;
      long l2 = (l ^ 21692695020514L) >>> 32;
      int n = (int)((l ^ 21692695020514L) << 32 >>> 32);
      if (str == null) {
         throw new InvalidKeyException("Null key algorithm");
      } else {
         for (ShadedRsaKeyType shadedRsaKeyType : values(l2, n)) {
            if (shadedRsaKeyType.getKeyAlgo().equalsIgnoreCase(str)) {
               return shadedRsaKeyType;
            }
         }

         throw new ProviderException("Unsupported algorithm " + str);
      }
   }

   public static ShadedRsaKeyType[] values(long l, int n) {
      return (ShadedRsaKeyType[])VALUES.clone();
   }

   public String getKeyAlgo() {
      return this.keyAlgo;
   }


}
