package net.augustus.modules.movement;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.ClientManagers;
import net.augustus.settings.Setting;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;

public class NoJumpDelay extends Module implements UpdateListener {
   private final BooleanValue whileScaffold;
   private final StringValue chance;

   public NoJumpDelay() {
       super("NoJumpDelay", "KeineSprungVerzögerung", new Color(235, 64, 52), Categorys.MOVEMENT);
      this.whileScaffold = new BooleanValue(this, "WhileScaffold", true);
      this.chance = new StringValue(this, "Chance", 100.0, 0.0, 100.0, 0);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 43;
      this.toggleEventSubscription(b);
   }

   public void onUpdate2() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if ((!ClientManagers.moduleManager.scaffold.isToggled() || this.whileScaffold.getValue()) && Math.random() <= this.chance.getValue() / 100.0) {
         mc.thePlayer.setJumpTicks(0);
      }
   }

}
