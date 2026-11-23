package net.augustus.modules.movement;

import java.awt.Color;
import java.util.function.BiFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.MotionListener;
import net.augustus.settings.Setting;
import net.augustus.settings.ModeSetting;
import net.augustus.modules.modes.highjump.MotionModifierHighJumpMode;
import net.augustus.modules.modes.highjump.DamageHighJumpMode;
import net.augustus.modules.modes.highjump.VanillaHighJumpMode;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.settings.CustomSubModeFactory;
import net.augustus.events.EventMotion;

public class HighJump extends Module implements MotionListener {
   public final ModeSetting modes;
   private int jumpTicks;

   public HighJump() {
       super("HighJump", "Hochsprung", new Color(131, 184, 222), Categorys.MOVEMENT);
      BiFunction[] biFunctions = new BiFunction[]{VanillaHighJumpMode.factory(), DamageHighJumpMode.factory()};
      ModeSetting modeSetting = new ModeSetting(this, "Modes", "Vanilla", biFunctions);
      CustomSubModeFactory customSubModeFactory = MotionModifierHighJumpMode.factory();
      this.modes = modeSetting.enableCustomModes(customSubModeFactory);
      this.jumpTicks = 0;
   }

   public void onEnable() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      this.jumpTicks = 0;
      if (mc.thePlayer != null && this.modes.getSelectedValue().equals("Damage")) {
         PlayerUtil.damageSelfShort();
      }
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 18;
      this.toggleEventSubscription(b);
   }

   public void onMotion(EventMotion event) {
      String selectedValue = this.modes.getSelectedValue();
      this.setSuffix(selectedValue);
   }

}
