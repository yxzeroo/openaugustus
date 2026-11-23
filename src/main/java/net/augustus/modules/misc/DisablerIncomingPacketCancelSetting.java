package net.augustus.modules.misc;

import net.augustus.settings.MultiOptionSetting;
import net.augustus.settings.SelectableOption;
import net.augustus.modules.Module;
import net.augustus.settings.DoubleValue;

public class DisablerIncomingPacketCancelSetting extends MultiOptionSetting {
   public final Disabler disabler;

   public DisablerIncomingPacketCancelSetting(Disabler disabler2, Module module, String str, SelectableOption[] selectableOptions, DoubleValue doubleValue) {
       super(module, str, selectableOptions, doubleValue);
       this.disabler = disabler2;
   }

   @Override
   public String getDisplayName() {
      return "PacketsToCancel";
   }
}
