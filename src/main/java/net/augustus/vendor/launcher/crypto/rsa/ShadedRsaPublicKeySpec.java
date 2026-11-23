package net.augustus.vendor.launcher.crypto.rsa;

import net.augustus.vendor.launcher.crypto.math.ShadedBigInteger;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

public class ShadedRsaPublicKeySpec implements KeySpec {
   private final AlgorithmParameterSpec params;
   private final ShadedBigInteger modulus;
   private final ShadedBigInteger publicExponent;

   public AlgorithmParameterSpec getParams() {
      return this.params;
   }

   public ShadedRsaPublicKeySpec(ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2) {
      this(shadedBigInteger, shadedBigInteger2, null);
   }

   public ShadedBigInteger getModulus() {
      return this.modulus;
   }

   public ShadedRsaPublicKeySpec(ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2, AlgorithmParameterSpec algorithmParameterSpec) {
      this.modulus = shadedBigInteger;
      this.publicExponent = shadedBigInteger2;
      this.params = algorithmParameterSpec;
   }

   public ShadedBigInteger getPublicExponent() {
      return this.publicExponent;
   }
}
