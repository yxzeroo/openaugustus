package net.augustus.vendor.launcher.crypto.rsa;

import net.augustus.vendor.launcher.crypto.math.ShadedBigInteger;
import java.security.spec.AlgorithmParameterSpec;

public interface ShadedRsaKey {
   default AlgorithmParameterSpec getParams(long l) {
      return null;
   }

   ShadedBigInteger getModulus(long l);
}
