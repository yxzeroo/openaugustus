package net.augustus.vendor.launcher.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipArchiveExtractor {
   private static int unusedCounter;

   public static void extractZipBytes(byte[] bytes, String str) {

      try {
         File file = new File(str);
         if (!file.exists()) {
            file.mkdir();
         }

         ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(bytes));

         for (ZipEntry i = zipInputStream.getNextEntry(); i != null; i = zipInputStream.getNextEntry()) {
            String str2 = str + File.separator + i.getName();
            if (!i.isDirectory()) {
               extractEntryToFile(zipInputStream, str2);
            } else {
               File file2 = new File(str2);
               file2.mkdirs();
            }

            zipInputStream.closeEntry();
         }

         zipInputStream.close();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   private static void extractEntryToFile(ZipInputStream zipInputStream, String str) throws IOException {
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(str));
      byte[] bytes = new byte[4096];
      int n = 0;

      while ((n = zipInputStream.read(bytes)) != -1) {
         bufferedOutputStream.write(bytes, 0, n);
      }

      bufferedOutputStream.close();
   }

}
