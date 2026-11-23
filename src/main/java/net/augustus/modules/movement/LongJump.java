package net.augustus.modules.movement;

import java.awt.Color;
import java.util.function.BiFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.events.listeners.MotionListener;
import net.augustus.settings.Setting;
import net.augustus.settings.ModeSetting;
import net.augustus.modules.modes.longjump.OldCubeCraftLongJumpMode;
import net.augustus.modules.modes.longjump.IntaveBoatLongJumpMode;
import net.augustus.modules.modes.longjump.MotionModifierLongJumpMode;
import net.augustus.modules.modes.longjump.OldZoneCraftLongJumpMode;
import net.augustus.modules.modes.longjump.MinemenLongJumpMode;
import net.augustus.modules.modes.longjump.NcpLongJumpMode;
import net.augustus.settings.CustomSubModeFactory;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.events.EventMotion;
import net.augustus.settings.BooleanValue;

public class LongJump extends Module implements MotionListener {
   private final ModeSetting modes;
   private final BooleanValue autoDisable;
   private int groundTicks;
   public boolean pendingJump;

   public LongJump() {
       super("LongJump", "Weitsprung", new Color(243, 45, 134), Categorys.MOVEMENT);
      BiFunction[] biFunctions = new BiFunction[]{
         NcpLongJumpMode.factory(),
         OldCubeCraftLongJumpMode.factory(),
         OldZoneCraftLongJumpMode.factory(),
         MinemenLongJumpMode.factory(),
         IntaveBoatLongJumpMode.factory()
      };
      this.modes = new ModeSetting(this, "Modes", "NCP", biFunctions).enableCustomModes(MotionModifierLongJumpMode.factory());
      this.autoDisable = (BooleanValue)new BooleanValue(this, "AutoDisable", true).withVisibility(() -> this.isAutoDisableVisible());
      this.groundTicks = 0;
   }

   @Override
   public void onEnable() {
      this.groundTicks = 0;
      this.pendingJump = true;
   }

   @Override
   public void onDisable() {
      mc.getTimer().timerSpeed = 1.0F;
   }

   @Override
   public void updateEventSubscriptions() {
      this.toggleEventSubscription((byte)18);
   }

   @Override
   public void onMotion(EventMotion event) {
      this.setSuffix(this.modes.getSelectedValue());
      if (!this.modes.getSelectedValue().equals("IntaveBoat") && mc.thePlayer.onGround) {
         if (this.groundTicks > 0) {
            if (this.isLicenseTimestampValid() && this.autoDisable.getValue()) {
               this.setToggled(false);
               return;
            }
         }

         this.groundTicks++;
         if (this.groundTicks % 2 == 0) {
            this.pendingJump = true;
         }
      }
   }

   private boolean isAutoDisableVisible() {
      return !this.modes.getSelectedValue().equals("IntaveBoat");
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
