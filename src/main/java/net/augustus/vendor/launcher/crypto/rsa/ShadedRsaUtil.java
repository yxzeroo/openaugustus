package net.augustus.vendor.launcher.crypto.rsa;

import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PSSParameterSpec;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;

public class ShadedRsaUtil {
   private static final long a = 356642568725L;

   public static AlgorithmId createAlgorithmId(ShadedRsaKeyType shadedRsaKeyType, AlgorithmParameterSpec algorithmParameterSpec) throws ProviderException {
      checkParamsAgainstType(shadedRsaKeyType, algorithmParameterSpec);
      Object obj = null;
      AlgorithmParameters algorithmParameters = null;

      try {
         switch (ShadedRsaKeyTypeSwitchMap.ORDINAL_MAP[shadedRsaKeyType.ordinal()]) {
            case 1:
               obj = AlgorithmId.RSAEncryption_oid;
               break;
            case 2:
               if (algorithmParameterSpec != null) {
                  algorithmParameters = AlgorithmParameters.getInstance(shadedRsaKeyType.getKeyAlgo());
                  algorithmParameters.init(algorithmParameterSpec);
               }

               obj = AlgorithmId.RSASSA_PSS_oid;
               break;
            default:
               throw new ProviderException("Unsupported RSA algorithm " + shadedRsaKeyType);
         }

         AlgorithmId algorithmId;
         if (algorithmParameters == null) {
            algorithmId = new AlgorithmId((ObjectIdentifier)obj);
         } else {
            algorithmId = new AlgorithmId((ObjectIdentifier)obj, algorithmParameters);
         }

         return algorithmId;
      } catch (NoSuchAlgorithmException | InvalidParameterSpecException e) {
         throw new ProviderException(e);
      }
   }

   public static AlgorithmParameterSpec getParamSpec2(long l, AlgorithmParameters algorithmParameters) throws ProviderException {
      l = a ^ l;
      long l2 = l ^ 73708771735226L;
      if (algorithmParameters == null) {
         return null;
      } else {
         try {
            String algorithm = algorithmParameters.getAlgorithm();
            ShadedRsaKeyType shadedRsaKeyType = ShadedRsaKeyType.lookup(l2, algorithm);
            switch (ShadedRsaKeyTypeSwitchMap.ORDINAL_MAP[shadedRsaKeyType.ordinal()]) {
               case 1:
                  StringBuilder sb = new StringBuilder().append("No params accepted for ");
                  throw new ProviderException(sb.append(shadedRsaKeyType.getKeyAlgo()).toString());
               case 2:
                  return algorithmParameters.getParameterSpec(PSSParameterSpec.class);
               default:
                  throw new ProviderException("Unsupported RSA algorithm: " + algorithm);
            }
         } catch (ProviderException e) {
            throw e;
         } catch (Exception e2) {
            throw new ProviderException(e2);
         }
      }
   }

   public static AlgorithmParameterSpec getParamSpec(AlgorithmId algorithmId, long l) throws ProviderException {
      l = a ^ l;
      long l2 = l ^ 114922043028698L;
      if (algorithmId == null) {
         throw new ProviderException("AlgorithmId should not be null");
      } else {
         return getParamSpec2(l2, algorithmId.getParameters());
      }
   }

   public static void checkParamsAgainstType(ShadedRsaKeyType shadedRsaKeyType, AlgorithmParameterSpec algorithmParameterSpec) throws ProviderException {
      switch (ShadedRsaKeyTypeSwitchMap.ORDINAL_MAP[shadedRsaKeyType.ordinal()]) {
         case 1:
            if (algorithmParameterSpec != null) {
               StringBuilder sb = new StringBuilder().append("null params expected for ");
               throw new ProviderException(sb.append(shadedRsaKeyType.getKeyAlgo()).toString());
            }
            break;
         case 2:
            if (algorithmParameterSpec != null && !(algorithmParameterSpec instanceof PSSParameterSpec)) {
               StringBuilder sb2 = new StringBuilder().append("PSSParmeterSpec expected for ");
               throw new ProviderException(sb2.append(shadedRsaKeyType.getKeyAlgo()).toString());
            }
            break;
         default:
            throw new ProviderException("Unsupported RSA algorithm " + shadedRsaKeyType);
      }
   }


}
