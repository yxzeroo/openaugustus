package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.NightVisionListener;
import net.augustus.events.EventNightVision;
import net.augustus.settings.SettingChangeListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.minecraft.client.settings.GameSettings;

public class FullBright extends Module implements NightVisionListener {
   public final BooleansSetting mode;
   public final BooleanValue other;
   private float savedGamma;

   public FullBright() {
       super("FullBright", "VolleHelligkeit", new Color(188, 213, 25), Categorys.RENDER);
      BooleansSetting booleansSetting = new BooleansSetting(this, "Mode", "NightVision", new String[]{"NightVision", "Gamma"});
      SettingChangeListener listener = () -> this.onModeChanged();
      SettingChangeListener listener2 = listener;
      this.mode = (BooleansSetting)booleansSetting.withChangeListener(listener2);
      BooleanValue booleanValue = new BooleanValue(this, "Other", true);
      SettingVisibilityCondition condition = () -> this.isOtherNightVisionVisible();
      this.other = (BooleanValue)booleanValue.withVisibility(condition);
      this.savedGamma = 1.0F;
   }

   @Override
   public void onEnable() {
      this.savedGamma = mc.gameSettings.gammaSetting;
      if (this.mode.getSelectedValue().equals("Gamma")) {
         mc.gameSettings.gammaSetting = 10.0F;
      }
   }

   @Override
   public void onDisable() {
      ((GameSettings)null).gammaSetting = Math.min(this.savedGamma, 1.0F);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 22;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onNightVision(EventNightVision event) {
      if (!this.mode.getSelectedValue().equals("Gamma")) {
         boolean flag = true;
         event.setEnabled(flag);
      }
   }

   @Override
   public void onNightVision2(EventNightVision event) {
      if (!this.other.getValue()) {
         boolean flag = true;
         event.setEnabled(flag);
      }
   }

   private boolean isOtherNightVisionVisible() {
      return this.mode.getSelectedValue().equals("NightVision");
   }

   private void onModeChanged() {
      if (this.isToggled()) {
         if (this.mode.getSelectedValue().equals("Gamma")) {
            mc.gameSettings.gammaSetting = 10.0F;
         } else if (mc.gameSettings.gammaSetting > 1.0F) {
            mc.gameSettings.gammaSetting = Math.min(this.savedGamma, 1.0F);
         }
      } else if (mc.gameSettings.gammaSetting > 1.0F) {
         mc.gameSettings.gammaSetting = Math.min(this.savedGamma, 1.0F);
      }
   }

}
