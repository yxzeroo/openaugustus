package net.augustus.modules.movement;

import java.awt.Color;
import java.util.function.BiFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.events.listeners.MotionListener;
import net.augustus.settings.ModeSetting;
import net.augustus.modules.modes.spider.BoundingBoxSpiderMode;
import net.augustus.modules.modes.spider.JumpSpiderMode;
import net.augustus.modules.modes.spider.VerusSpiderMode;
import net.augustus.modules.modes.spider.MotionSpiderMode;
import net.augustus.modules.modes.spider.PositionSpiderMode;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.EventMotion;
import net.augustus.settings.DoubleValue;

public class Spider extends Module implements TickListener, MotionListener {
   public final ModeSetting mode;
   public final DoubleValue timer;

   public Spider() {
       super("Spider", "Spinne", new Color(168, 127, 50), Categorys.MOVEMENT);
      BiFunction[] biFunctions = new BiFunction[]{
         MotionSpiderMode.factory(),
         JumpSpiderMode.factory(),
         PositionSpiderMode.factory(),
         VerusSpiderMode.factory(),
         BoundingBoxSpiderMode.factory()
      };
      this.mode = new ModeSetting(this, "Mode", "Jump", biFunctions);
      this.timer = new DoubleValue(this, "Timer", 1.0, 0.1, 2.0, 2);
   }

   public void onEnable() {
   }

   public void onDisable() {
      mc.getTimer().timerSpeed = 1.0F;
   }

   public void updateEventSubscriptions() {
      this.toggleEventSubscription((byte)40);
      this.toggleEventSubscription((byte)18);
   }

   public void onMotion(EventMotion event) {
      if (this.canClimbWall()) {
         mc.getTimer().timerSpeed = (float)Math.max(0.1, this.timer.getValue());
      } else {
         mc.getTimer().timerSpeed = 1.0F;
      }
   }

   public void onTick2() {
      this.setSuffix(this.mode.getSelectedValue());
   }

   private boolean canClimbWall() {
      if (mc.thePlayer != null && mc.thePlayer.isCollidedHorizontally) {
         if (this.isLicenseTimestampValid() && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava()) {
            return true;
         }
      }

      return false;
   }

   private boolean isLicenseTimestampValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
      boolean flag = false;
      if (timestampBytes != null) {
         long l = 0L;

         for (int i = 0; i < 8; i++) {
            l <<= 8;
            l |= (long)(timestampBytes[i] & 255);
         }

         long l2 = System.currentTimeMillis() - l;
         String str = "518400000";
         long l3 = 0L;
         int n = 0;
         int length = str.length();

         while (n < length) {
            int n2 = Character.digit(str.charAt(n++), 10);
            l3 *= 10L;
            l3 -= (long)n2;
         }

         l3 = -l3;
         flag = l2 <= l3;
      }

      return flag;
   }

}
