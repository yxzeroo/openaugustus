package net.augustus.vendor;

import net.augustus.Augustus;
import net.augustus.utils.ChatUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import net.minecraft.network.PacketThreadUtil;

public class AquaMessengerClient {
   private static BufferedReader reader;
   private static boolean running = true;
   private static Socket socket;
   private static PrintWriter writer;
   private static boolean enabled = false;

   private static synchronized boolean isEnabled() {
      return enabled;
   }

   public static void startClient(String str) {

      try {
         new Thread(null, () -> {

            while (running) {
               if (isEnabled()) {
                  try {
                     socket = new Socket("162.212.154.28", 12777);
                     reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     writer = new PrintWriter(socket.getOutputStream(), true);
                     writer.println(str);
                     writer.println(Augustus.getInstance().getUsername());

                     String str2;
                     while (running && (str2 = reader.readLine()) != null) {
                        if (str2 != null && !str2.equals("msg")) {
                           String var15f = str2;
                           PacketThreadUtil.runOnMainThreadImmediate(() -> {
                              handleIncomingMessage(var15f);
                           });
                        }
                     }
                  } catch (IOException e) {
                  } finally {
                     closeConnection();
                  }
               }

               try {
                  Thread.sleep(1000L);
               } catch (InterruptedException e2) {
               }
            }
         }, "IRC", 0L).start();
      } catch (Throwable e3) {
         closeConnection();
         return;
      }

      closeConnection();
   }

   private static void closeConnection() {

      try {
         if (socket != null) {
            socket.close();
            socket = null;
         }

         if (reader != null) {
            reader.close();
            reader = null;
         }

         if (writer != null) {
            writer.close();
            writer = null;
         }
      } catch (Throwable e) {
         e.printStackTrace();
      }
   }

   public static void shutdown() {
      running = false;
   }

   private static void handleIncomingMessage(String str) {
      String[] strings = str.split(":", 3);
      if (strings.length > 2) {
         String str2 = strings[2].trim();
         if (str2.contains("Userinfo:") || str2.contains("Aqua") && str2.contains("ESound")) {
            return;
         }

         ChatUtil.sendFormattedChat("AquaMessenger", str2);
      }
   }

   public static void setConnectionEnabled(boolean flag) {
      setEnabled(flag);
      closeConnection();
   }

   static {
      Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
   }

   private static synchronized void setEnabled(boolean flag) {
      enabled = flag;
   }

   public static void sendMessage(String str) {
      if (writer != null) {
         String str2 = Augustus.getInstance().getName() + " [" + Augustus.getInstance().getUsername() + "] : " + str;
         writer.println(str2);
         ChatUtil.sendFormattedChat("AquaMessenger", str2);
      } else {
         String str3 = "You are not connected to the AquaIRC (enable AquaMessenger)";
         ChatUtil.sendFormattedChat("AquaMessenger", str3);
      }
   }



}
