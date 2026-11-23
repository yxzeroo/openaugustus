package net.augustus.discord;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.augustus.utils.TimeHelper;

public class DiscordRpcCallbackThread extends Thread {
   public final DiscordRpcManager discordRpcManager;
   public final DiscordEventHandlers handlers;

   public DiscordRpcCallbackThread(DiscordRpcManager discordRpcManager2, String str, DiscordEventHandlers discordEventHandlers) {
      super(str);
      this.discordRpcManager = discordRpcManager2;
      this.handlers = discordEventHandlers;
   }

   @Override
   public void finalize() {
      boolean flag = false;
      DiscordRpcManager.setConnected(this.discordRpcManager, flag);
      DiscordRPC.discordShutdown();
   }

   @Override
   public void run() {

      while (true) {
         if (!DiscordRpcManager.isConnected(this.discordRpcManager)) {
            TimeHelper connectTimer = DiscordRpcManager.getConnectTimer(this.discordRpcManager);
            long l = 10000L;
            if (connectTimer.reached2(l)) {
               return;
            }
         }

         DiscordRPC.discordRunCallbacks();
         DiscordRPC.discordUpdateEventHandlers(this.handlers);

         try {
            Thread.sleep(10L);
         } catch (InterruptedException e) {
         }
      }
   }

}
