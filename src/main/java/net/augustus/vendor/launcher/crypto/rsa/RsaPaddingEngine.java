package net.augustus.vendor.launcher.crypto.rsa;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.MGF1ParameterSpec;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.PSource.PSpecified;
import sun.security.jca.JCAUtil;

public final class RsaPaddingEngine {
   public static final int PAD_BLOCKTYPE_2 = 0;
   private final int type;
   private final int maxDataSize;
   private Mgf1MaskGenerator mgf;
   public static final int PAD_NONE = 0;
   public static final int PAD_BLOCKTYPE_1 = 1;
   public static final int PAD_OAEP_MGF1 = 0;
   private SecureRandom secureRandom;
   private MessageDigest md;
   private static final Map<String, byte[]> emptyHashes = Collections.synchronizedMap(new HashMap<>());
   private byte[] lHash;
   private final int paddedSize;

   public static RsaPaddingEngine getInstance(int n, int n2) throws InvalidKeyException, InvalidAlgorithmParameterException {
      return new RsaPaddingEngine(n, n2, (SecureRandom)null, (OAEPParameterSpec)null);
   }

   public byte[] unpad(byte[] bytes, int n, int n2) throws BadPaddingException {
      byte[] bytes2 = RsaPublicKeyCipher.convert(bytes, n, n2);
      return this.unpad(bytes2);
   }

   public byte[] pad(byte[] bytes) throws BadPaddingException {
      int[] flowState = RsaPublicKeyCipher.getFlowState();
      if (bytes.length > this.maxDataSize) {
         throw new BadPaddingException("Data must be shorter than " + (this.maxDataSize + 1) + " bytes but received " + bytes.length + " bytes.");
      } else {
         switch (this.type) {
            case 1:
            case 2:
               return this.padV15(bytes);
            case 3:
               return bytes;
            case 4:
               return this.padOAEP(bytes);
            default:
               throw new AssertionError();
         }
      }
   }

   public byte[] pad(byte[] bytes, int n, int n2) throws BadPaddingException {
      byte[] bytes2 = RsaPublicKeyCipher.convert(bytes, n, n2);
      return this.pad(bytes2);
   }

   private byte[] unpadOAEP(byte[] bytes) throws BadPaddingException {
      byte[] bytes2 = bytes;
      RsaPublicKeyCipher.getFlowState();
      boolean flag = false;
      int length = this.lHash.length;
      if (bytes[0] != 0) {
         flag = true;
      }

      byte b = 1;
      int n = length + 1;
      int n2 = bytes.length - n;
      this.mgf.generateAndXor(bytes, n, n2, length, bytes, b);
      this.mgf.generateAndXor(bytes, b, length, n2, bytes, n);

      for (int i = 0; i < length; i++) {
         if (this.lHash[i] != bytes2[n + i]) {
            flag = true;
         }
      }

      int n3 = n + length;
      int n4 = -1;

      for (int j = n3; j < bytes2.length; j++) {
         byte b2 = bytes2[j];
         if (n4 == -1 && b2 != 0) {
            if (b2 == 1) {
               n4 = j;
            } else {
               flag = true;
            }
         }
      }

      if (n4 == -1) {
         flag = true;
         n4 = bytes2.length - 1;
      }

      int n5 = n4 + 1;
      byte[] bytes3 = new byte[n5 - n3];
      System.arraycopy(bytes2, n3, bytes3, 0, bytes3.length);
      byte[] bytes4 = new byte[bytes2.length - n5];
      System.arraycopy(bytes2, n5, bytes4, 0, bytes4.length);
      BadPaddingException ex = new BadPaddingException("Decryption error");
      if (flag) {
         throw ex;
      } else {
         return bytes4;
      }
   }

   public static RsaPaddingEngine getInstance(int n, int n2, SecureRandom rand) throws InvalidKeyException, InvalidAlgorithmParameterException {
      return new RsaPaddingEngine(n, n2, rand, (OAEPParameterSpec)null);
   }

   public int getMaxDataSize(long l) {
      return this.maxDataSize;
   }


   private byte[] padV15(byte[] bytes) throws BadPaddingException {
      byte[] bytes2 = new byte[this.paddedSize];
      System.arraycopy(bytes, 0, bytes2, this.paddedSize - bytes.length, bytes.length);
      int n = this.paddedSize - 3 - bytes.length;
      RsaPublicKeyCipher.getFlowState();
      byte b = 0;
      int n2 = b + 1;
      bytes2[b] = 0;
      bytes2[n2++] = (byte)this.type;
      if (this.type == 1) {
         while (n-- > 0) {
            bytes2[n2++] = -1;
         }
      } else {
         if (this.secureRandom == null) {
            SecureRandom secureRandom2 = JCAUtil.getSecureRandom();
            this.secureRandom = secureRandom2;
         }

         byte[] bytes3 = new byte[64];
         int n3 = -1;

         while (n-- > 0) {
            int n4;
            do {
               if (n3 < 0) {
                  this.secureRandom.nextBytes(bytes3);
                  n3 = bytes3.length - 1;
               }

               n4 = bytes3[n3--] & 255;
            } while (n4 == 0);

            bytes2[n2++] = (byte)n4;
         }
      }

      return bytes2;
   }

   public static RsaPaddingEngine getInstance(int n, int n2, SecureRandom rand, OAEPParameterSpec oaepParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
      return new RsaPaddingEngine(n, n2, rand, oaepParameterSpec);
   }

   public byte[] unpad(byte[] bytes) throws BadPaddingException {
      int[] flowState = RsaPublicKeyCipher.getFlowState();
      if (bytes.length != this.paddedSize) {
         throw new BadPaddingException("Decryption error.The padded array length (" + bytes.length + ") is not the specified padded size (" + this.paddedSize + ")");
      } else {
         switch (this.type) {
            case 1:
            case 2:
               return this.unpadV15(bytes);
            case 3:
               return bytes;
            case 4:
               return this.unpadOAEP(bytes);
            default:
               throw new AssertionError();
         }
      }
   }

   private byte[] unpadV15(byte[] bytes) throws BadPaddingException {
      byte b = 0;
      boolean flag = false;
      RsaPublicKeyCipher.getFlowState();
      int n = b + 1;
      if (bytes[b] != 0) {
         flag = true;
      }

      if (bytes[n++] != this.type) {
         flag = true;
      }

      int n2 = 0;

      while (n < bytes.length) {
         int n3 = bytes[n++] & 255;
         if (n3 == 0 && n2 == 0) {
            n2 = n;
         }

         if (n == bytes.length && n2 == 0) {
            flag = true;
         }

         if (this.type == 1 && n3 != 255 && n2 == 0) {
            flag = true;
         }
      }

      int n4 = bytes.length - n2;
      if (n4 > this.maxDataSize) {
         flag = true;
      }

      byte[] bytes2 = new byte[n2];
      System.arraycopy(bytes, 0, bytes2, 0, n2);
      byte[] bytes3 = new byte[n4];
      System.arraycopy(bytes, n2, bytes3, 0, n4);
      BadPaddingException ex = new BadPaddingException("Decryption error");
      if (flag) {
         throw ex;
      } else {
         return bytes3;
      }
   }

   private RsaPaddingEngine(int n, int n2, SecureRandom rand, OAEPParameterSpec oaepParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
       super();
      this.type = n;
      RsaPublicKeyCipher.getFlowState();
      this.paddedSize = n2;
      this.secureRandom = rand;
      if (n2 < 64) {
         throw new InvalidKeyException("Padded size must be at least 64");
      } else {
         switch (n) {
            case 1:
            case 2:
               this.maxDataSize = n2 - 11;
               break;
            case 3:
               this.maxDataSize = n2;
               break;
            case 4:
               String str = "SHA-1";
               String str2 = str;
               byte[] bytes = null;

               try {
                  if (oaepParameterSpec != null) {
                     str = oaepParameterSpec.getDigestAlgorithm();
                     String mgfAlgorithm = oaepParameterSpec.getMGFAlgorithm();
                     if (!mgfAlgorithm.equalsIgnoreCase("MGF1")) {
                        throw new InvalidAlgorithmParameterException("Unsupported MGF algo: " + mgfAlgorithm);
                     }

                     str2 = ((MGF1ParameterSpec)oaepParameterSpec.getMGFParameters()).getDigestAlgorithm();
                     PSource pSource = oaepParameterSpec.getPSource();
                     String algorithm = pSource.getAlgorithm();
                     if (!algorithm.equalsIgnoreCase("PSpecified")) {
                        throw new InvalidAlgorithmParameterException("Unsupported pSource algo: " + algorithm);
                     }

                     bytes = ((PSpecified)pSource).getValue();
                  }

                  MessageDigest instance = MessageDigest.getInstance(str);
                  this.md = instance;
                  Mgf1MaskGenerator mgf1MaskGenerator = new Mgf1MaskGenerator(str2);
                  this.mgf = mgf1MaskGenerator;
               } catch (NoSuchAlgorithmException e) {
                  throw new InvalidKeyException("Digest not available", e);
               }

               byte[] initialHash = getInitialHash(this.md, bytes);
               this.lHash = initialHash;
               int length = this.lHash.length;
               this.maxDataSize = n2 - 2 - 2 * length;
               if (this.maxDataSize <= 0) {
                  StringBuilder sb = new StringBuilder().append("Key is too short for encryption using OAEPPadding with ").append(str).append(" and ");
                  throw new InvalidKeyException(sb.append(this.mgf.getName()).toString());
               }
               break;
            default:
               throw new InvalidKeyException("Invalid padding: " + n);
         }
      }
   }

   private byte[] padOAEP(byte[] bytes) throws BadPaddingException {
      int[] flowState = RsaPublicKeyCipher.getFlowState();
      if (this.secureRandom == null) {
         SecureRandom secureRandom2 = JCAUtil.getSecureRandom();
         this.secureRandom = secureRandom2;
      }

      int length = this.lHash.length;
      byte[] bytes2 = new byte[length];
      this.secureRandom.nextBytes(bytes2);
      byte[] bytes3 = new byte[this.paddedSize];
      byte b = 1;
      System.arraycopy(bytes2, 0, bytes3, b, length);
      int n = length + 1;
      int n2 = bytes3.length - n;
      int n3 = this.paddedSize - bytes.length;
      System.arraycopy(this.lHash, 0, bytes3, n, length);
      bytes3[n3 - 1] = 1;
      System.arraycopy(bytes, 0, bytes3, n3, bytes.length);
      this.mgf.generateAndXor(bytes3, b, length, n2, bytes3, n);
      this.mgf.generateAndXor(bytes3, n, n2, length, bytes3, b);
      return bytes3;
   }

   private static byte[] getInitialHash(MessageDigest messageDigest, byte[] bytes) {
      int[] flowState = RsaPublicKeyCipher.getFlowState();
      byte[] bytes2;
      if (bytes != null && bytes.length != 0) {
         bytes2 = messageDigest.digest(bytes);
      } else {
         String algorithm = messageDigest.getAlgorithm();
         bytes2 = emptyHashes.get(algorithm);
         if (bytes2 == null) {
            bytes2 = messageDigest.digest();
            emptyHashes.put(algorithm, bytes2);
         }
      }

      return bytes2;
   }

}
