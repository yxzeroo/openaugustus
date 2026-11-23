package net.augustus.vendor.launcher.crypto.rsa;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Mgf1MaskGenerator {
   private final MessageDigest md;

   public String getName() {
      return "MGF1" + this.md.getAlgorithm();
   }

   public Mgf1MaskGenerator(String str) throws NoSuchAlgorithmException {
      this.md = MessageDigest.getInstance(str);
   }

   public void generateAndXor(byte[] bytes, int n, int n2, int n3, byte[] bytes2, int n4) throws RuntimeException {
      byte[] bytes3 = new byte[4];
      byte[] bytes4 = new byte[this.md.getDigestLength()];

      while (n3 > 0) {
         this.md.update(bytes, n, n2);
         this.md.update(bytes3);

         try {
            this.md.digest(bytes4, 0, bytes4.length);
         } catch (DigestException e) {
            throw new RuntimeException(e.toString());
         }

         for (int i = 0; i < bytes4.length && n3 > 0; n3--) {
            int n5 = n4++;
            bytes2[n5] ^= bytes4[i++];
         }

         if (n3 > 0) {
            int n6 = bytes3.length - 1;

            while (++bytes3[n6] == 0 && n6 > 0) {
               n6--;
            }
         }
      }
   }

}
