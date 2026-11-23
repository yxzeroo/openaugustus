package net.augustus.modules.player;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.settings.RangeSetting;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;

public class AutoRespawn extends Module implements TickListener {
   private final TimeHelper deathScreenTimer;
   private final TimeHelper respawnCooldownTimer;
   public final RangeSetting delay;

   public AutoRespawn() {
       super("AutoRespawn", "AutomatischerRespawn", new Color(82, 222, 124), Categorys.PLAYER);
      this.deathScreenTimer = new TimeHelper();
      this.respawnCooldownTimer = new TimeHelper();
      this.delay = new RangeSetting(this, "Delay", 1000.0, 2000.0, 0.0, 5000.0, 0);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
   }

   public void onTick2() {
      if (mc.currentScreen instanceof GuiGameOver) {
         if (mc.thePlayer.isDead) {
            TimeHelper deathScreenTimer2 = this.deathScreenTimer;
            RandomNumberGenerator instance = DefaultRandomGenerator.instance;
            double minValue = this.delay.getMinValue();
            double maxValue = this.delay.getMaxValue();
            double d = maxValue;
            double d2 = minValue;
            long l = (long)instance.nextInt(d2, d);
            if (deathScreenTimer2.reached2(l)) {
               long l2 = 10000L;
               if (this.respawnCooldownTimer.reached2(l2)) {
                  mc.thePlayer.respawnPlayer();
                  GuiScreen screen = (GuiScreen)null;
                  mc.displayGuiScreen(screen);
                  this.deathScreenTimer.reset();
                  this.respawnCooldownTimer.reset();
               }
            }
         }
      } else {
         this.deathScreenTimer.reset();
      }
   }

}
