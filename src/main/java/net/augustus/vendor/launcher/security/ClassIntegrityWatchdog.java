package net.augustus.vendor.launcher.security;

import net.augustus.vendor.launcher.ClientArchiveHolder;
import net.augustus.vendor.launcher.ElectricLauncherController;
import net.augustus.vendor.launcher.EncryptedClassLoader;
import net.augustus.vendor.launcher.crypto.Sha256Digest;
import net.augustus.vendor.launcher.net.LauncherClientHandler;
import net.augustus.vendor.launcher.net.LauncherNetworkClient;
import net.augustus.vendor.launcher.packet.C2SRemoteClassRequestPacket;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClassIntegrityWatchdog {
   private static ClassLoader expectedParentClassLoader;
   private static WeakReference<Object> controllerRef;
   private static volatile boolean started;
   private static final Map<String, byte[]> CLASS_HASHES;
   private static WeakReference<Object> clientHandlerRef;
   private static Sha256Digest baselineHasher;
   private static Sha256Digest verifyHasher;
   private static final ScheduledExecutorService WATCHDOG_EXECUTOR;

   private static boolean verifyClassHash(String str) {
      byte[] expected = CLASS_HASHES.get(str);
      if (expected == null) {
         return true;
      }
      try {
         InputStream is = ClassLoader.getSystemResourceAsStream(str.replace('.', '/') + ".class");
         if (is == null) {
            return false;
         }
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         byte[] buf = new byte[8192];
         int n;
         while ((n = is.read(buf)) != -1) {
            baos.write(buf, 0, n);
         }
         is.close();
         byte[] bytes = baos.toByteArray();
         byte[] hash = verifyHasher.digest(bytes);
         return Arrays.equals(expected, hash);
      } catch (Throwable t) {
         return false;
      }
   }

   public static boolean matchesRegisteredInstance(String str, Object obj2) {
      WeakReference<Object> ref;
      if (str.equals("2")) {
         ref = controllerRef;
      } else if (str.equals("1")) {
         ref = clientHandlerRef;
      } else {
         return false;
      }
      if (ref == null) {
         return false;
      }
      Object obj = ref.get();
      if (obj == null) {
         return false;
      }
      return obj == obj2;
   }

   public static Map getClassHashes() {
      return new HashMap<>(CLASS_HASHES);
   }

   private static void checkRuntimeIntegrity() {
      ClassLoader parent = ClassIntegrityWatchdog.class.getClassLoader().getParent();
      if (parent != expectedParentClassLoader) {
         LauncherClientHandler.getInstance().sendPacket(new C2SRemoteClassRequestPacket(LauncherClientHandler.getInstance().sessionToken, "net/c/"));
         return;
      }
      ElectricLauncherController controller = ElectricLauncherController.getInstance();
      if (controller != null && !matchesRegisteredInstance("2", controller)) {
         LauncherClientHandler.getInstance().sendPacket(new C2SRemoteClassRequestPacket(LauncherClientHandler.getInstance().sessionToken, "net/d/"));
         return;
      }
      LauncherClientHandler client = LauncherClientHandler.getInstance();
      if (client != null && !matchesRegisteredInstance("1", client)) {
         LauncherClientHandler.getInstance().sendPacket(new C2SRemoteClassRequestPacket(LauncherClientHandler.getInstance().sessionToken, "net/e"));
      }
   }

   private static Thread createDaemonThread(Runnable runnable) {
      Thread thread = new Thread(runnable);
      thread.setDaemon(true);
      return thread;
   }

   private static void recordClassHash(String str) {
      try {
         InputStream is = ClassIntegrityWatchdog.class.getClassLoader().getResourceAsStream(str.replace('.', '/') + ".class");
         if (is == null) {
            return;
         }
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         byte[] buf = new byte[8192];
         int n;
         while ((n = is.read(buf)) != -1) {
            baos.write(buf, 0, n);
         }
         is.close();
         byte[] bytes = baos.toByteArray();
         byte[] hash = baselineHasher.digest(bytes);
         CLASS_HASHES.put(str, hash);
      } catch (Throwable t) {
      }
   }

   public static void start() {
      if (started) {
         return;
      }
      recordClassHash(ElectricLauncherController.class.getName());
      recordClassHash(ClientArchiveHolder.class.getName());
      recordClassHash(EncryptedClassLoader.class.getName());
      recordClassHash(LauncherClientHandler.class.getName());
      recordClassHash(ClassIntegrityWatchdog.class.getName());
      recordClassHash(Sha256Digest.class.getName());
      recordClassHash(CodeSourceIntegrityHasher.class.getName());
      expectedParentClassLoader = ClassIntegrityWatchdog.class.getClassLoader().getParent();
      WATCHDOG_EXECUTOR.scheduleAtFixedRate(() -> runWatchdogTick(), 5L, 5L, TimeUnit.SECONDS);
      started = true;
   }

   private static void runWatchdogTick() {
      try {
         checkRuntimeIntegrity();
      } catch (Throwable t) {
         LauncherClientHandler.getInstance().sendPacket(
            new C2SRemoteClassRequestPacket(LauncherClientHandler.getInstance().sessionToken, "net/f/")
         );
         LauncherNetworkClient.getInstance().interrupt();
      }
   }

   public static void registerInstance(String str, Object obj) {
      if (str.equals("2")) {
         controllerRef = new WeakReference<>(obj);
      } else if (str.equals("1")) {
         clientHandlerRef = new WeakReference<>(obj);
      }
   }



   static {
      WATCHDOG_EXECUTOR = Executors.newSingleThreadScheduledExecutor(ClassIntegrityWatchdog::createDaemonThread);
      CLASS_HASHES = new HashMap<>();
      controllerRef = null;
      clientHandlerRef = null;
      expectedParentClassLoader = null;
      baselineHasher = new Sha256Digest();
      verifyHasher = new Sha256Digest();
      started = false;
   }
}
