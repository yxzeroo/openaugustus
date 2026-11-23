package net.augustus.vendor.launcher.crypto;

import java.nio.charset.StandardCharsets;

public class IteratedSha256KeyDeriver {
   private static byte[] SALT = new byte[]{12, -38, 34, -75, -28, -64, -4, -85};

   public static byte[] deriveKey(Sha256Digest sha256Digest, String str) {
      byte[] bytes = str.getBytes(StandardCharsets.UTF_8);

      for (int i = 0; i < 50; i++) {
         bytes = sha256Digest.digest(bytes);
      }

      return bytes;
   }

}
