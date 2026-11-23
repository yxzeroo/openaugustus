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
      ModeSetting modeSetting = new ModeSetting(this, "Modes", "NCP", biFunctions);
      CustomSubModeFactory customSubModeFactory = MotionModifierLongJumpMode.factory();
      this.modes = modeSetting.enableCustomModes(customSubModeFactory);
      BooleanValue booleanValue = new BooleanValue(this, "AutoDisable", true);
      SettingVisibilityCondition condition = () -> this.isAutoDisableVisible();
      this.autoDisable = (BooleanValue)booleanValue.withVisibility(condition);
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
      byte b = 18;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onMotion(EventMotion event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      String selectedValue = this.modes.getSelectedValue();
      this.setSuffix(selectedValue);
      if (!this.modes.getSelectedValue().equals("IntaveBoat") && mc.thePlayer.onGround) {
         if (this.groundTicks > 0) {
            if (this.isLicenseTimestampValid() && this.autoDisable.getValue()) {
               boolean flag = false;
               this.setToggled(flag);
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
      Setting[] cachedSettings = Fly.getCachedSettings();
      return !this.modes.getSelectedValue().equals("IntaveBoat");
   }

   private boolean isLicenseTimestampValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
      Fly.getCachedSettings();
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
