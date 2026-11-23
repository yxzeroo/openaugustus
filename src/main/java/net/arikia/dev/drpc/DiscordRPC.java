package net.arikia.dev.drpc;

import com.sun.jna.Library;
import com.sun.jna.Native;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class DiscordRPC {
   private static final String DLL_VERSION = "3.4.0";
   private static final String LIB_VERSION = "1.6.2";

   public static void discordInitialize(String applicationId, DiscordEventHandlers handlers, boolean autoRegister) {
      DiscordRPC.DLL.INSTANCE.Discord_Initialize(applicationId, handlers, autoRegister ? 1 : 0, null);
   }

   public static void discordRegister(String applicationId, String command) {
      DiscordRPC.DLL.INSTANCE.Discord_Register(applicationId, command);
   }

   public static void discordInitialize(String applicationId, DiscordEventHandlers handlers, boolean autoRegister, String steamId) {
      DiscordRPC.DLL.INSTANCE.Discord_Initialize(applicationId, handlers, autoRegister ? 1 : 0, steamId);
   }

   public static void discordRegisterSteam(String applicationId, String steamId) {
      DiscordRPC.DLL.INSTANCE.Discord_RegisterSteamGame(applicationId, steamId);
   }

   public static void discordUpdateEventHandlers(DiscordEventHandlers handlers) {
      DiscordRPC.DLL.INSTANCE.Discord_UpdateHandlers(handlers);
   }

   public static void discordShutdown() {
      DiscordRPC.DLL.INSTANCE.Discord_Shutdown();
   }

   public static void discordRunCallbacks() {
      DiscordRPC.DLL.INSTANCE.Discord_RunCallbacks();
   }

   public static void discordUpdatePresence(DiscordRichPresence presence) {
      DiscordRPC.DLL.INSTANCE.Discord_UpdatePresence(presence);
   }

   public static void discordClearPresence() {
      DiscordRPC.DLL.INSTANCE.Discord_ClearPresence();
   }

   public static void discordRespond(String userId, DiscordRPC.DiscordReply reply) {
      DiscordRPC.DLL.INSTANCE.Discord_Respond(userId, reply.reply);
   }

   private static void loadDLL() {
      String name = System.mapLibraryName("discord-rpc");
      OSUtil osUtil = new OSUtil();
      String dir;
      if (osUtil.isMac()) {
         File homeDir = new File(System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support" + File.separator);
         dir = "darwin";
         String tempPath = homeDir + File.separator + "discord-rpc" + File.separator + name;
      } else {
         if (!osUtil.isWindows()) {
            String finalPath = "/linux/" + name;

            try {
               File f = File.createTempFile("drpc", name);

               try (
                  InputStream in = DiscordRPC.class.getResourceAsStream(finalPath);
                  OutputStream out = openOutputStream(f);
               ) {
                  if (in == null) {
                     throw new FileNotFoundException("Native Linux .so library missing. Please open an issue. https://github.com/Vatuu/discord-rpc");
                  }

                  copyFile(in, out);
                  f.deleteOnExit();
                  System.load(f.getAbsolutePath());
                  out.close();
                  in.close();
               }

               return;
            } catch (IOException e) {
               e.printStackTrace();
               System.out.println("Fatal Discord RPC exception occurred. Discord RPC will be unavailable for this session.");
               return;
            }
         }

         File homeDir = new File(System.getenv("TEMP"));
         boolean is64bit = System.getProperty("sun.arch.data.model").equals("64");
         dir = is64bit ? "win-x64" : "win-x86";
         String tempPath = homeDir + File.separator + "discord-rpc" + File.separator + name;
      }

      String finalPath = "/" + dir + "/" + name;

      try {
         Path tempDirectoryPath = Files.createTempDirectory("drpc");
         File f = new File(tempDirectoryPath + File.separator + name);

         try (
            InputStream in = DiscordRPC.class.getResourceAsStream(finalPath);
            OutputStream out = openOutputStream(f);
         ) {
            copyFile(in, out);
            tempDirectoryPath.toFile().deleteOnExit();
            f.deleteOnExit();
            out.close();
            in.close();
         } catch (IOException e2) {
            e2.printStackTrace();
         }

         System.load(f.getAbsolutePath());
      } catch (Exception e3) {
         e3.printStackTrace();
      }
   }

   private static void copyFile(InputStream input, OutputStream output) throws IOException {
      byte[] buffer = new byte[4096];

      int n;
      while (-1 != (n = input.read(buffer))) {
         output.write(buffer, 0, n);
      }
   }

   private static FileOutputStream openOutputStream(File file) throws IOException {
      if (file.exists()) {
         if (file.isDirectory()) {
            throw new IOException("File '" + file + "' exists but is a directory");
         }

         if (!file.canWrite()) {
            throw new IOException("File '" + file + "' cannot be written to");
         }
      } else {
         File parent = file.getParentFile();
         if (parent != null && !parent.mkdirs() && !parent.isDirectory()) {
            throw new IOException("Directory '" + parent + "' could not be created");
         }
      }

      return new FileOutputStream(file);
   }

   static {
      loadDLL();
   }

   private interface DLL extends Library {
      DiscordRPC.DLL INSTANCE = (DiscordRPC.DLL)Native.loadLibrary("discord-rpc", DiscordRPC.DLL.class);

      void Discord_Initialize(String str, DiscordEventHandlers discordEventHandlers, int n, String str2);

      void Discord_Register(String str, String str2);

      void Discord_RegisterSteamGame(String str, String str2);

      void Discord_UpdateHandlers(DiscordEventHandlers discordEventHandlers);

      void Discord_Shutdown();

      void Discord_RunCallbacks();

      void Discord_UpdatePresence(DiscordRichPresence discordRichPresence);

      void Discord_ClearPresence();

      void Discord_Respond(String str, int n);
   }

   public static enum DiscordReply {
      NO(0),
      YES(1),
      IGNORE(2);

      public final int reply;

      private DiscordReply(int reply) {
         this.reply = reply;
      }
   }
}
