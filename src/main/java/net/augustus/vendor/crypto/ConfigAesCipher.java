package net.augustus.vendor.crypto;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import net.augustus.utils.TimeHelper;

public class ConfigAesCipher {
   private byte[] iv;
   private boolean useCbc;
   private SecretKeySpec secretKey;

   public void setIv(byte[] bytes) {
      this.iv = bytes;
      this.useCbc = true;
   }

   public byte[] encrypt(byte[] bytes, int n, int n2) {

      try {
         if (this.secretKey == null) {
            return null;
         } else {
            Cipher cipher;
            if (this.useCbc) {
               cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
               IvParameterSpec ivParameterSpec = new IvParameterSpec(this.iv);
               cipher.init(1, this.secretKey, ivParameterSpec);
            } else {
               cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
               cipher.init(1, this.secretKey);
            }

            return cipher.doFinal(bytes, n, n2);
         }
      } catch (Exception e) {
         return null;
      }
   }

   public void initKey(boolean flag, byte[] bytes) {
      this.useCbc = flag;
      if (bytes.length != 16 && bytes.length != 24 && bytes.length != 32) {
         throw new IllegalArgumentException("Schlüssellänge muss 16, 24 oder 32 Bytes sein");
      } else {
         this.secretKey = new SecretKeySpec(bytes, "AES");
         if (flag) {
            this.iv = new byte[16];
            new SecureRandom().nextBytes(this.iv);
         }
      }
   }

   public byte[] decrypt(byte[] bytes, int n, int n2) {

      try {
         if (this.secretKey == null) {
            return null;
         } else {
            Cipher cipher;
            if (this.useCbc) {
               cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
               IvParameterSpec ivParameterSpec = new IvParameterSpec(this.iv);
               cipher.init(2, this.secretKey, ivParameterSpec);
            } else {
               cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
               cipher.init(2, this.secretKey);
            }

            return cipher.doFinal(bytes, n, n2);
         }
      } catch (Exception e) {
         return null;
      }
   }

   public byte[] decrypt(byte[] bytes) {
      int length = bytes.length;
      byte b = 0;
      return this.decrypt(bytes, b, length);
   }

   public byte[] getIv() {
      return this.iv;
   }
}
