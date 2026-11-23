package net.augustus.vendor.launcher.crypto.rsa;

import net.augustus.vendor.launcher.crypto.asn1.Asn1DerInputStream;
import net.augustus.vendor.launcher.crypto.asn1.Asn1DerOutputStream;
import net.augustus.vendor.launcher.crypto.asn1.Asn1DerValue;
import net.augustus.vendor.launcher.crypto.math.ShadedBigInteger;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.security.AccessController;
import java.security.InvalidKeyException;
import java.security.KeyRep;
import java.security.ProviderException;
import java.security.KeyRep.Type;
import java.security.spec.AlgorithmParameterSpec;
import sun.security.action.GetPropertyAction;
import sun.security.util.BitArray;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X509Key;

public final class ShadedRsaPublicKeyImpl extends X509Key implements ShadedRsaPublicKey {
   private ShadedBigInteger publicExponent;
   private static final ShadedBigInteger THREE;
   private AlgorithmParameterSpec keyParams;
   private ShadedBigInteger modulus;
   private static final long serialVersionUID;
   private static final long a = 10266322325464L;

   public static ShadedRsaPublicKeyImpl newKey(long l, byte[] bytes) throws InvalidKeyException {
      l = a ^ l;
      long l2 = l ^ 127903219500137L;
      return new ShadedRsaPublicKeyImpl(l2, bytes);
   }

   public ShadedRsaPublicKeyImpl(AlgorithmId algorithmId, ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2, long l) throws InvalidKeyException {
      super();
      l = a ^ l;
      long l2 = l ^ 73085932475315L;
      checkRSAProviderKeyLengths(shadedBigInteger.bitLength(), shadedBigInteger2);
      checkExponentRange(shadedBigInteger, shadedBigInteger2);
      this.modulus = shadedBigInteger;
      this.publicExponent = shadedBigInteger2;
      this.keyParams = ShadedRsaUtil.getParamSpec(algorithmId, l2);
      this.algid = algorithmId;

      try {
         Asn1DerOutputStream asn1DerOutputStream = new Asn1DerOutputStream();
         asn1DerOutputStream.putInteger3(shadedBigInteger);
         asn1DerOutputStream.putInteger3(shadedBigInteger2);
         byte[] bytes = (new Asn1DerValue((byte)48, asn1DerOutputStream.toByteArray())).toByteArray();
         this.setKey(new BitArray(bytes.length * 8, bytes));
      } catch (IOException e) {
         throw new InvalidKeyException(e);
      }
   }

   public static ShadedRsaPublicKey newKey(ShadedRsaKeyType shadedRsaKeyType, AlgorithmParameterSpec algorithmParameterSpec, ShadedBigInteger shadedBigInteger, long l, ShadedBigInteger shadedBigInteger2) throws InvalidKeyException {
      l = a ^ l;
      long l2 = l ^ 115977884150682L;
      AlgorithmId algorithmId = ShadedRsaUtil.createAlgorithmId(shadedRsaKeyType, algorithmParameterSpec);
      return new ShadedRsaPublicKeyImpl(algorithmId, shadedBigInteger, shadedBigInteger2, l2);
   }

   @Override
   public String toString() {
      return "Sun "
         + this.getAlgorithm()
         + " public key, "
         + this.modulus.bitLength()
         + " bits\n  params: "
         + this.keyParams
         + "\n  modulus: "
         + this.modulus
         + "\n  public exponent: "
         + this.publicExponent;
   }

   static {
      long[] longs = new long[]{3L, 2644735423591199609L};
      serialVersionUID = longs[1];
      THREE = ShadedBigInteger.valueOf(longs[0]);
   }

   @Override
   public ShadedBigInteger getModulus(long l) {
      return this.modulus;
   }

   @Override
   public void parseKeyBits() throws InvalidKeyException {

      try {
         Asn1DerValue derValue = new Asn1DerInputStream(this.getKey().toByteArray()).getDerValue();
         if (derValue.tag != 48) {
            throw new IOException("Not a SEQUENCE");
         } else {
            Asn1DerInputStream data = derValue.data;
            this.modulus = data.getPositiveBigInteger();
            this.publicExponent = data.getPositiveBigInteger();
            if (derValue.data.available() != 0) {
               throw new IOException("Extra data available");
            }
         }
      } catch (IOException e) {
         throw new InvalidKeyException("Invalid RSA public key", e);
      }
   }

   public ShadedRsaPublicKeyImpl(long l, byte[] bytes) throws InvalidKeyException {
      super();
      l = a ^ l;
      long l2 = l ^ 65366122139966L;
      if (bytes != null && bytes.length != 0) {
         this.decode(bytes);
         checkRSAProviderKeyLengths(this.modulus.bitLength(), this.publicExponent);
         ShadedBigInteger publicExponent2 = this.publicExponent;
         checkExponentRange(this.modulus, publicExponent2);

         try {
            this.keyParams = ShadedRsaUtil.getParamSpec(this.algid, l2);
         } catch (ProviderException e) {
            throw new InvalidKeyException(e);
         }
      } else {
         throw new InvalidKeyException("Missing key encoding");
      }
   }

   @Override
   public AlgorithmParameterSpec getParams(long l) {
      return this.keyParams;
   }

   @Override
   public String getAlgorithm() {
      return this.algid.getName();
   }

   public static void checkRSAProviderKeyLengths(int n, ShadedBigInteger shadedBigInteger) throws InvalidKeyException {
      checkKeyLengths(n + 7 & -8, shadedBigInteger, (short)512, Integer.MAX_VALUE);
   }

   @Override
   public ShadedBigInteger getPublicExponent(long l) {
      return this.publicExponent;
   }

   public Object writeReplace(long l) throws ObjectStreamException {
      return new KeyRep(Type.PUBLIC, this.getAlgorithm(), this.getFormat(), this.getEncoded());
   }

   public static void checkKeyLengths(int n, ShadedBigInteger shadedBigInteger, int n2, int n3) throws InvalidKeyException {
      if (n2 > 0 && n < n2) {
         throw new InvalidKeyException("RSA keys must be at least " + n2 + " bits long");
      } else {
         int n4 = Math.min(n3, 16384);
         if (n > n4) {
            throw new InvalidKeyException("RSA keys must be no longer than " + n4 + " bits");
         } else if ("true".equalsIgnoreCase(AccessController.doPrivileged(new GetPropertyAction("sun.security.rsa.restrictRSAExponent", "true")))
            && shadedBigInteger != null
            && n > 3072
            && shadedBigInteger.bitLength() > 64) {
            throw new InvalidKeyException("RSA exponents can be no longer than 64 bits  if modulus is greater than 3072 bits");
         }
      }
   }

   public static void checkExponentRange(ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2) throws InvalidKeyException {
      if (shadedBigInteger2.compareTo2(shadedBigInteger) >= 0) {
         throw new InvalidKeyException("exponent is larger than modulus");
      } else {
         if (shadedBigInteger2.compareTo2(THREE) < 0) {
            throw new InvalidKeyException("exponent is smaller than 3");
         }
      }
   }

}
