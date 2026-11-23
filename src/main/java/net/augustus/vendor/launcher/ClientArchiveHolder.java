package net.augustus.vendor.launcher;

import net.augustus.vendor.launcher.crypto.AesCipherUtil;
import net.augustus.vendor.launcher.crypto.Sha256Digest;
import net.augustus.vendor.launcher.crypto.keccak.Sha3Type;
import net.augustus.vendor.launcher.net.LauncherClientHandler;
import net.augustus.vendor.launcher.util.Base64Codec;
import net.augustus.vendor.launcher.util.Base64Encoder;
import com.sun.management.OperatingSystemMXBean;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ClientArchiveHolder extends Thread {
   private HashMap<String, byte[]> clientEntries;
   public static final boolean DEBUG_ENABLED = false;
   public static byte[] encryptedArchiveKey;
   private HashMap<String, byte[]> resourceEntries;
   private static ClientArchiveHolder instance;
   public static byte[] archiveBytes;
   public static String clientName;
   public static long lastEntryTimestamp;

   public static Object[] concatArrays(Object[] array, long l, Object[] array2) {
      Object[] result = Arrays.copyOf(array, array.length + array2.length);
      System.arraycopy(array2, 0, result, array.length, array2.length);
      return result;
   }

   private void loadAndLaunchClient() throws Exception {
      instance = this;
      this.setNativeLibraryPath(new File(System.getenv("APPDATA") + "/ElectricLauncher/data/data/" + clientName + "/natives"));
      this.resourceEntries = readZipEntries(
         Files.readAllBytes(new File(System.getenv("APPDATA") + "/ElectricLauncher/data/data/" + clientName + "/resources.jar").toPath())
      );
      this.clientEntries = readZipEntries(archiveBytes);
      String mac = "00-00-00-00-00-00";
      ((OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
      FileStore store = FileSystems.getDefault().getFileStores().iterator().next();
      Base64Encoder encoder = Base64Codec.getRfc46482();
      String hwidPrefix = System.getenv("PROCESSOR_LEVEL")
         + System.getenv("NUMBER_OF_PROCESSORS")
         + System.getenv("PROCESSOR_IDENTIFIER")
         + File.listRoots()[0].getTotalSpace()
         + System.getenv("PROCESSOR_REVISION");
      String hwidFull = hwidPrefix
         + System.getenv("PROCESSOR_LEVEL")
         + System.getenv("PROCESSOR_ARCHITECTURE")
         + System.getenv("HOME")
         + System.getenv("NUMBER_OF_PROCESSORS")
         + System.getenv("TMP")
         + System.getenv("PROCESSOR_REVISION")
         + mac
         + System.getenv("COMPUTERNAME")
         + System.getenv("APPDATA")
         + File.listRoots()[0].getTotalSpace()
         + System.getenv("DISPLAY")
         + System.getenv("PROCESSOR_IDENTIFIER")
         + System.getenv("USERNAME")
         + System.getenv("HOMEPATH")
         + encoder.encodeToString(store.toString().getBytes(StandardCharsets.UTF_8))
         + encoder.encodeToString(store.getAttribute("volume:vsn").toString().getBytes(StandardCharsets.UTF_8));
      byte[] key1 = AesCipherUtil.encrypt(
         encryptedArchiveKey,
         new Sha3Hash(Sha3Type.SHA3_256)
            .digest(new Sha256Digest().digest(hwidFull.getBytes(StandardCharsets.UTF_8)))
      );
      EncryptedClassLoader.setClassDecryptionKey(AesCipherUtil.encrypt(
         key1,
         new Sha3Hash(Sha3Type.SHA3_256).digest(LauncherClientHandler.getInstance().sessionToken)
      ));
      EncryptedClassLoader.setSessionAuthToken(LauncherClientHandler.getInstance().sessionToken);
      Class<?> mainClass = new EncryptedClassLoader().findClass("StartMinecraftDirectory");
      mainClass.getMethod("main", String[].class).invoke(null, (Object)new String[0]);
   }

   public HashMap getResourceEntries() {
      return this.resourceEntries;
   }

   public HashMap getClientEntries() {
      return this.clientEntries;
   }

   private void setNativeLibraryPath(File file) throws NoSuchFieldException, IllegalAccessException {
      System.setProperty("java.library.path", file.getAbsolutePath());
      Field sysPaths = ClassLoader.class.getDeclaredField("sys_paths");
      sysPaths.setAccessible(true);
      sysPaths.set(null, null);
   }

   public ClientArchiveHolder() {
      this.clientEntries = new HashMap<>();
      this.resourceEntries = new HashMap<>();
   }

   private static HashMap readZipEntries(byte[] bytes) throws IOException {
      HashMap<String, byte[]> map = new HashMap<>();
      ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bytes));
      ZipEntry lastEntry = null;
      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null) {
         byte[] data = readAllBytes(zis, zis.available());
         if (!entry.getName().endsWith("/")) {
            map.put(entry.getName(), data);
            lastEntry = entry;
         }
      }
      lastEntryTimestamp = lastEntry.getTime();
      zis.close();
      return map;
   }

   @Override
   public void run() {
      try {
         this.loadAndLaunchClient();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static ClientArchiveHolder getInstance() {
      return instance;
   }

   private static byte[] readAllBytes(InputStream in, int n2) throws IOException {
      byte[] buf = new byte[n2];
      int capacity = n2;
      int nread = 0;
      while (true) {
         int n;
         while ((n = in.read(buf, nread, capacity - nread)) > 0) {
            nread += n;
         }
         if (n < 0 || (n = in.read()) < 0) {
            break;
         }
         if (capacity <= Integer.MAX_VALUE - 8 - capacity) {
            capacity = Math.max(capacity << 1, 8192);
         } else {
            if (capacity == Integer.MAX_VALUE - 8) {
               throw new RuntimeException();
            }
            capacity = Integer.MAX_VALUE - 8;
         }
         buf = Arrays.copyOf(buf, capacity);
         buf[nread++] = (byte)n;
      }
      return capacity == nread ? buf : Arrays.copyOf(buf, nread);
   }



}
