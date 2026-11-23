package net.augustus.modules.misc;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.Augustus;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.TimeHelper;

public class AquaMessenger extends Module implements TickListener {
   public boolean pendingConnectWarning;
   private final TimeHelper timeHelper;

   public AquaMessenger() {
       super("AquaMessenger", "WasserBote", new Color(52, 235, 195), Categorys.MISC);
      this.pendingConnectWarning = false;
      this.timeHelper = new TimeHelper();
   }

   public void onEnable() {
      this.pendingConnectWarning = true;
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
   }

   public void onTick() {
      long l = 10000L;
      if (this.timeHelper.reached2(l) && !Augustus.getInstance().getUsername().isEmpty()) {
         if (this.pendingConnectWarning) {
            String str = "Attention: chat messages are unsecure!";
            this.sendChatMessage2(str);
            this.timeHelper.reset();
            this.pendingConnectWarning = false;
         }
      }
   }

}
