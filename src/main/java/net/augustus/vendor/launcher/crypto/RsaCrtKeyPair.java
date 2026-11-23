package net.augustus.vendor.launcher.crypto;


import net.augustus.vendor.launcher.crypto.math.ShadedBigInteger;
public class RsaCrtKeyPair {
   private final ShadedBigInteger privateExponent;
   private final ShadedBigInteger crtCoefficient;
   private final ShadedBigInteger primeExponentP;
   private final ShadedBigInteger primeP;
   private final ShadedBigInteger primeQ;
   private final ShadedBigInteger primeExponentQ;
   private final ShadedBigInteger modulus;
   private final ShadedBigInteger publicExponent;

   public ShadedBigInteger getPrivateExponent() {
      return this.privateExponent;
   }

   public ShadedBigInteger getPublicExponent() {
      return this.publicExponent;
   }

   public ShadedBigInteger getPrimeP() {
      return this.primeP;
   }

   public ShadedBigInteger getPrimeExponentQ() {
      return this.primeExponentQ;
   }

   public ShadedBigInteger getModulus() {
      return this.modulus;
   }

   public RsaCrtKeyPair(ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2, ShadedBigInteger shadedBigInteger3, ShadedBigInteger shadedBigInteger4, ShadedBigInteger shadedBigInteger5) {
       super();
      this.modulus = shadedBigInteger;
      this.publicExponent = shadedBigInteger2;
      this.privateExponent = shadedBigInteger3;
      this.primeP = shadedBigInteger4;
      this.primeQ = shadedBigInteger5;
      ShadedBigInteger shadedBigInteger6 = shadedBigInteger4.subtract(ShadedBigInteger.ONE);
      this.primeExponentP = shadedBigInteger3.mod(shadedBigInteger6);
      shadedBigInteger6 = shadedBigInteger5.subtract(ShadedBigInteger.ONE);
      this.primeExponentQ = shadedBigInteger3.mod(shadedBigInteger6);
      this.crtCoefficient = shadedBigInteger5.modInverse(shadedBigInteger4);
   }

   public ShadedBigInteger getPrimeExponentP() {
      return this.primeExponentP;
   }

   public ShadedBigInteger getCrtCoefficient() {
      return this.crtCoefficient;
   }

   public ShadedBigInteger getPrimeQ() {
      return this.primeQ;
   }
}
