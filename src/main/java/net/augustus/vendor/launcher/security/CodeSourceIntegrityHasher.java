package net.augustus.vendor.launcher.security;

import net.augustus.vendor.launcher.crypto.Sha256Digest;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.CodeSource;

public class CodeSourceIntegrityHasher {

   public static byte[] computeCodeSourceHash(Sha256Digest sha256Digest) {
      byte[] result = new byte[32];
      try {
         CodeSource codeSource = CodeSourceIntegrityHasher.class.getProtectionDomain().getCodeSource();
         if (codeSource == null) {
            return result;
         }
         URL location = codeSource.getLocation();
         if (location == null) {
            return result;
         }
         byte[] bytes = Files.readAllBytes(Paths.get(location.toURI()));
         return sha256Digest.digest(bytes);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }



}
