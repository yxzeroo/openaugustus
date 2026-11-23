package net.augustus.modules.fun;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventKey;
import net.augustus.settings.Setting;
import net.augustus.settings.KeybindSetting;
import net.augustus.settings.TextInputSetting;
import net.augustus.settings.DynamicSettingList;
import net.augustus.events.listeners.KeyListener;
import net.augustus.settings.DoubleValue;

public class Hotkeys extends Module implements KeyListener {
   private final DynamicSettingList hotkeys;

   public Hotkeys() {
       super("Hotkeys", "HeißeSchlüssel", new Color(150, 255, 211), Categorys.FUN);
      this.hotkeys = new DynamicSettingList(this, "Hotkeys", n -> this.createHotkeyEntry(n), 0, 50);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 14;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onKey2(EventKey event) {

      for (DoubleValue doubleValue : (Iterable<DoubleValue>)(this.hotkeys.getEntries())) {
         KeybindSetting keybindSetting = null;
         TextInputSetting textInputSetting = null;

         for (Setting setting : (Iterable<Setting>)(doubleValue.getChildren())) {
            if (setting instanceof KeybindSetting) {
               keybindSetting = (KeybindSetting)setting;
            } else if (setting instanceof TextInputSetting) {
               textInputSetting = (TextInputSetting)setting;
            }
         }

         if (keybindSetting != null && textInputSetting != null && event.getKeyCode() == keybindSetting.getKeyCode() && mc.thePlayer != null) {
            mc.thePlayer.sendChatMessage(textInputSetting.getValue());
         }
      }
   }

   private DoubleValue createHotkeyEntry(int n) {
      DoubleValue doubleValue = new DoubleValue(this, "Hotkey_" + n, true);
      new KeybindSetting(this, "Key_" + n, doubleValue);
      new TextInputSetting(this, "Text_" + n, "", doubleValue);
      return doubleValue;
   }

}
