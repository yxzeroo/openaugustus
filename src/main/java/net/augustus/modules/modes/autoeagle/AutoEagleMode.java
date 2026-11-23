package net.augustus.modules.modes.autoeagle;

import net.augustus.modules.modes.AbstractSubMode;
import net.augustus.modules.legit.AutoEagle;
import net.augustus.settings.DoubleValue;

public abstract class AutoEagleMode extends AbstractSubMode {

   public AutoEagleMode(AutoEagle autoEagle, DoubleValue doubleValue, String str) {
      super(autoEagle, doubleValue, str);
   }



}
