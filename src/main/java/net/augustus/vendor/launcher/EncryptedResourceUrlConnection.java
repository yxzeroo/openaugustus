package net.augustus.vendor.launcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class EncryptedResourceUrlConnection extends URLConnection {
   public final EncryptedClassLoader classLoader;

   public EncryptedResourceUrlConnection(EncryptedClassLoader encryptedClassLoader, URL url2) {
      super(url2);
      this.classLoader = encryptedClassLoader;
   }

   @Override
   public InputStream getInputStream() throws IOException {
      return this.classLoader.getResourceAsStream(this.url.getPath());
   }

   @Override
   public void connect() throws IOException {
   }
}
