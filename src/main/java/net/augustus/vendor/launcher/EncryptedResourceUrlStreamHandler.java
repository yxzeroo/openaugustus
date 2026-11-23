package net.augustus.vendor.launcher;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class EncryptedResourceUrlStreamHandler extends URLStreamHandler {
   public final EncryptedClassLoader classLoader;

   @Override
   public URLConnection openConnection(URL url) throws IOException {
      return new EncryptedResourceUrlConnection(this.classLoader, url);
   }

   public EncryptedResourceUrlStreamHandler(EncryptedClassLoader encryptedClassLoader) {
      this.classLoader = encryptedClassLoader;
   }
}
