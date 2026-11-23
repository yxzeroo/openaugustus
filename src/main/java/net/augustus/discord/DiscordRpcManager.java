package net.augustus.discord;

import net.augustus.Augustus;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.DiscordEventHandlers.Builder;
import net.augustus.utils.TimeHelper;

public class DiscordRpcManager {
   private boolean started;
   private boolean connected;
   public static String discordUserTag = "";
   private final TimeHelper timeHelper = new TimeHelper();
   private Thread callbackThread;
   private long startTimestamp;

   private static void syncDiscordUser(DiscordUser discordUser, long l) {
   }

   private void onReady(DiscordUser discordUser) {
      this.connected = true;
      if (discordUser != null) {
         discordUserTag = discordUser.userId + "###" + discordUser.username;
      }
   }

   public DiscordRpcManager() {
      this.connected = false;
      this.startTimestamp = System.currentTimeMillis();
      this.started = false;
      this.callbackThread = null;
   }

   public static boolean setConnected(DiscordRpcManager discordRpcManager, boolean flag) {
      return discordRpcManager.connected = flag;
   }

   public void shutdown() {
      if (this.connected) {
         this.connected = false;
         DiscordRPC.discordShutdown();
      }
   }

   public static TimeHelper getConnectTimer(DiscordRpcManager discordRpcManager) {
      return discordRpcManager.timeHelper;
   }

   public void start() {
      Object obj = null;
      Augustus.getInstance().getClass();
      this.started = true;
      if (!DiscordRPC.isAvailable()) {
         return;
      }
      if (!this.connected) {
         if (this.callbackThread != null) {
            this.callbackThread.interrupt();
         }

         this.timeHelper.reset();
         try {
            obj = new Builder().setReadyEventHandler(discordUser -> this.onReady(discordUser)).setErroredEventHandler((n, str) -> {
               System.err.println("Failed to load");
               this.connected = false;
            }).setDisconnectedEventHandler((n2, str2) -> {
               System.err.println("Disconnected!");
               this.connected = false;
            }).build();
            DiscordRPC.discordInitialize("944603218623733780", (DiscordEventHandlers)obj, true);
            if (!DiscordRPC.isAvailable()) {
               return;
            }
            this.callbackThread = new DiscordRpcCallbackThread(this, "Discord RPC Callback", (DiscordEventHandlers)obj);
            this.callbackThread.start();
         } catch (Throwable t) {
            this.connected = false;
         }
      }
   }

   public void updatePresence(String str, String str2) {
      Object obj = null;
      long l = 0L;
      l = -3221083442777101553L ^ (long)"\ue9ff瀞ؠ屛䢗랞℈瓓✇㠛멘罁垨烋렂ǩ띀賋\uf425臦㾢攉ࠀᑃ얢ꩉ铉ᒌ".hashCode();
      if (Augustus.getInstance().getModuleManager() == null) {
         l = (l ^ -8886614603018153483L) - -278448968043319355L;
      } else {
         l = (l ^ 5910076217030982124L) - -4697037063422838039L;
         if (!Augustus.getInstance().getModuleManager().noDiscordRPC.isToggled()) {
            l = (l ^ 6043383642565646527L) - -2976129328545735945L;
            if (!this.connected) {
               l = (l ^ -5167270007570758572L) - -3659298407330510090L;
            } else {
               l = l + 2238172087338308131L ^ 1002503232089553301L;
               obj = new net.arikia.dev.drpc.DiscordRichPresence.Builder(str2);
               l = l + -7339647169158021892L ^ 1342490735996400016L;
               ((net.arikia.dev.drpc.DiscordRichPresence.Builder)obj).setBigImage("large", "");
               l = l + 6134326050270533796L ^ 3123665649588462121L;
               ((net.arikia.dev.drpc.DiscordRichPresence.Builder)obj).setDetails(str);
               l = (l ^ -1099530656731637397L) - -3735658415963314970L;
               ((net.arikia.dev.drpc.DiscordRichPresence.Builder)obj).setStartTimestamps(this.startTimestamp);
               l = (l ^ 7913410136111398804L) + 8151475807151477842L;
               DiscordRPC.discordUpdatePresence(((net.arikia.dev.drpc.DiscordRichPresence.Builder)obj).build());
               l = (l ^ 8359487505767773903L) - -39404645582175467L;
            }

            return;
         }

         l = l + 3007169018997661377L ^ -5220177909943597906L;
      }
   }


   public static boolean isConnected(DiscordRpcManager discordRpcManager) {
      return discordRpcManager.connected;
   }

}
