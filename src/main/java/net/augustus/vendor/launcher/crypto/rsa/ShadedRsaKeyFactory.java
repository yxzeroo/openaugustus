package net.augustus.vendor.launcher.crypto.rsa;

import net.augustus.vendor.launcher.crypto.math.ShadedBigInteger;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;
import sun.security.action.GetPropertyAction;

public class ShadedRsaKeyFactory extends KeyFactorySpi {
   public static final int MIN_MODLEN = 0;
   public static final int MAX_MODLEN = 0;
   private static final Class<?> rsaPublicKeySpecClass = ShadedRsaPublicKeySpec.class;
   public static final int MAX_MODLEN_RESTRICT_EXP = 0;
   private static final Class<?> rsaPrivateCrtKeySpecClass = RSAPrivateCrtKeySpec.class;
   public static final int MAX_RESTRICTED_EXPLEN = 0;
   private final ShadedRsaKeyType type;
   private static final boolean restrictExpLen = "true".equalsIgnoreCase(AccessController.doPrivileged(new GetPropertyAction("restrictRSAExponent", "true")));
   private static final Class<?> pkcs8KeySpecClass = PKCS8EncodedKeySpec.class;
   private static final Class<?> x509KeySpecClass = X509EncodedKeySpec.class;
   private static final Class<?> rsaPrivateKeySpecClass = RSAPrivateKeySpec.class;
   private static final long a = 104335090443071L;

   public ShadedRsaKeyFactory(int n, char ch, int n2) {
      this.type = ShadedRsaKeyType.RSA;
   }


   public PublicKey generatePublic(long l, KeySpec keySpec) throws GeneralSecurityException {
      l = a ^ l;
      long l2 = l ^ 124371634728459L;
      long l3 = l ^ 8891092774022L;
      if (keySpec instanceof X509EncodedKeySpec) {
         X509EncodedKeySpec x509EncodedKeySpec = (X509EncodedKeySpec)keySpec;
         ShadedRsaPublicKeyImpl shadedRsaPublicKeyImpl = new ShadedRsaPublicKeyImpl(l3, x509EncodedKeySpec.getEncoded());
         checkKeyAlgo(shadedRsaPublicKeyImpl, this.type.getKeyAlgo());
         return shadedRsaPublicKeyImpl;
      } else if (keySpec instanceof ShadedRsaPublicKeySpec) {
         ShadedRsaPublicKeySpec shadedRsaPublicKeySpec = (ShadedRsaPublicKeySpec)keySpec;

         try {
            ShadedRsaKeyType type2 = this.type;
            AlgorithmParameterSpec params = shadedRsaPublicKeySpec.getParams();
            return new ShadedRsaPublicKeyImpl(ShadedRsaUtil.createAlgorithmId(type2, params), shadedRsaPublicKeySpec.getModulus(), shadedRsaPublicKeySpec.getPublicExponent(), l2);
         } catch (ProviderException e) {
            throw new InvalidKeySpecException(e);
         }
      } else {
         throw new InvalidKeySpecException("Only RSAPublicKeySpec and X509EncodedKeySpec supported for RSA public keys");
      }
   }

   public static void checkRSAProviderKeyLengths(int n, ShadedBigInteger shadedBigInteger) throws InvalidKeyException {
      checkKeyLengths(n + 7 & -8, shadedBigInteger, (short)512, 2147483647);
   }

   @Override
   public <T extends KeySpec> T engineGetKeySpec(Key key, Class<T> clazz) throws InvalidKeySpecException {
      return null;
   }

   public ShadedRsaKeyFactory(ShadedRsaKeyType shadedRsaKeyType) {
      this.type = shadedRsaKeyType;
   }

   public static void checkKeyLengths(int n, ShadedBigInteger shadedBigInteger, int n2, int n3) throws InvalidKeyException {
      if (n2 > 0 && n < n2) {
         throw new InvalidKeyException("RSA keys must be at least " + n2 + " bits long");
      } else {
         int n4 = Math.min(n3, 16384);
         if (n > n4) {
            throw new InvalidKeyException("RSA keys must be no longer than " + n4 + " bits");
         } else if (restrictExpLen && shadedBigInteger != null && n > 3072 && shadedBigInteger.bitLength() > 64) {
            throw new InvalidKeyException("RSA exponents can be no longer than 64 bits  if modulus is greater than 3072 bits");
         }
      }
   }

   public static ShadedRsaKeyFactory getInstance(ShadedRsaKeyType shadedRsaKeyType) {
      return new ShadedRsaKeyFactory(shadedRsaKeyType);
   }

   @Override
   public Key engineTranslateKey(Key key) throws InvalidKeyException {
      return null;
   }

   @Override
   public PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
      long l2 = (a ^ 94568921859109L) ^ 99366112174318L;

      try {
         return this.generatePublic(l2, keySpec);
      } catch (InvalidKeySpecException e) {
         throw e;
      } catch (GeneralSecurityException e2) {
         throw new InvalidKeySpecException(e2);
      }
   }

   @Override
   public PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
      return null;
   }

   private static void checkKeyAlgo(Key key, String str) throws InvalidKeyException {
      String algorithm = key.getAlgorithm();
      if (algorithm == null || !algorithm.equalsIgnoreCase(str)) {
         throw new InvalidKeyException("Expected a " + str + " key, but got " + algorithm);
      }
   }

}
