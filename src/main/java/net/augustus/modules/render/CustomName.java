package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.settings.TextInputSetting;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.Augustus;
import net.augustus.settings.SettingChangeListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleansSetting;

public class CustomName extends Module implements LoadWorldListener {
   public final BooleansSetting modes;
   public final BooleansSetting nameFormatting;
   public final TextInputSetting customName;
   public final String pcUserName;
   public String displayName2;

   public CustomName() {
       super("CustomName", "KundenspezifischerName", new Color(63, 21, 21), Categorys.RENDER);
      BooleansSetting booleansSetting = new BooleansSetting(this, "Modes", "AugustusName", new String[]{"AugustusName", "IngameName", "PCName", "Custom"});
      SettingChangeListener listener = () -> this.refreshCustomName();
      SettingChangeListener listener2 = listener;
      this.modes = (BooleansSetting)booleansSetting.withChangeListener(listener2);
      BooleansSetting booleansSetting2 = new BooleansSetting(
         this, "Name-Formatting", "Xanax", new String[]{"Xanax", "Xanax2", "Centaurus", "Centaurus2", "Skid", "Simple"}
      );
      SettingChangeListener listener3 = () -> this.refreshCustomName();
      SettingChangeListener listener4 = listener3;
      this.nameFormatting = (BooleansSetting)booleansSetting2.withChangeListener(listener4);
      TextInputSetting textInputSetting = new TextInputSetting(this, "Custom name", "Example Name");
      SettingVisibilityCondition condition = () -> this.isCustomNameVisible();
      this.customName = (TextInputSetting)textInputSetting.withVisibility(condition);
      this.pcUserName = System.getProperty("user.name");
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 17;
      this.toggleEventSubscription(b);
   }

   public void refreshCustomName() {
      String selectedValue = this.modes.getSelectedValue();
      switch (selectedValue) {
         case "AugustusName":
            this.displayName2 = Augustus.getInstance().getUsername();
            break;
         case "IngameName":
            this.displayName2 = mc.getSession().getUsername();
            break;
         case "PCName":
            this.displayName2 = this.pcUserName;
            break;
         case "Custom":
            this.displayName2 = this.customName.getValue();
      }
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.refreshCustomName();
   }

   private boolean isCustomNameVisible() {
      return this.modes.getSelectedValue().equals("Custom");
   }

}
