package net.augustus.vendor.crypto;

public class RsaCrtKeyPair {
   private final CustomBigInteger primeExponentP;
   private final CustomBigInteger crtCoefficient;
   private final CustomBigInteger modulus;
   private final CustomBigInteger publicExponent;
   private final CustomBigInteger privateExponent;
   private final CustomBigInteger primeExponentQ;
   private final CustomBigInteger primeQ;
   private final CustomBigInteger primeP;

   public RsaCrtKeyPair(CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2, CustomBigInteger customBigInteger3, CustomBigInteger customBigInteger4, CustomBigInteger customBigInteger5) {
       super();
      this.modulus = customBigInteger;
      this.publicExponent = customBigInteger2;
      this.privateExponent = customBigInteger3;
      this.primeP = customBigInteger4;
      this.primeQ = customBigInteger5;
      CustomBigInteger one = CustomBigInteger.ONE;
      CustomBigInteger customBigInteger6 = customBigInteger4.subtract(one);
      CustomBigInteger customBigInteger7 = customBigInteger6;
      this.primeExponentP = customBigInteger3.mod(customBigInteger7);
      CustomBigInteger one2 = CustomBigInteger.ONE;
      customBigInteger6 = customBigInteger5.subtract(one2);
      CustomBigInteger customBigInteger8 = customBigInteger6;
      this.primeExponentQ = customBigInteger3.mod(customBigInteger8);
      this.crtCoefficient = customBigInteger5.modInverse(customBigInteger4);
   }

   public CustomBigInteger getModulus() {
      return this.modulus;
   }

   public CustomBigInteger getPrivateExponent() {
      return this.privateExponent;
   }

   public CustomBigInteger getPrimeExponentP() {
      return this.primeExponentP;
   }

   public CustomBigInteger getPrimeP() {
      return this.primeP;
   }

   public CustomBigInteger getPublicExponent() {
      return this.publicExponent;
   }

   public CustomBigInteger getPrimeExponentQ() {
      return this.primeExponentQ;
   }

   public CustomBigInteger getPrimeQ() {
      return this.primeQ;
   }

   public CustomBigInteger getCrtCoefficient() {
      return this.crtCoefficient;
   }
}
