package net.augustus.modules.misc;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.viaversion.ViaAugustus;
import net.augustus.settings.SettingChangeListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.StringValue;

public class ProtocolVersion extends Module {
   public final StringValue protocolVersionVal;

   public ProtocolVersion() {
       super("ProtocolVersion", "ProtokollVersion", true, Color.RED, Categorys.MISC);
      StringValue stringValue = new StringValue(this, "ProtocolVersionVal", 47.0, 0.0, 0.0, 0);
      SettingVisibilityCondition condition = () -> false;
      StringValue stringValue2 = stringValue.visibleWhen(condition);
      SettingChangeListener listener = () -> this.applySelectedProtocolVersion();
      SettingChangeListener listener2 = listener;
      this.protocolVersionVal = (StringValue)stringValue2.withChangeListener(listener2);
      boolean flag = true;
      this.setToggled(flag);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
   }

   private void applySelectedProtocolVersion() {

      for (com.viaversion.viaversion.api.protocol.version.ProtocolVersion protocolVersion : com.viaversion.viaversion.api.protocol.version.ProtocolVersion.getReversedProtocols()) {
         if ((double)protocolVersion.getVersion() == this.protocolVersionVal.getValue()) {
            ViaAugustus.targetProtocolVersion = protocolVersion;
            return;
         }
      }
   }

}
