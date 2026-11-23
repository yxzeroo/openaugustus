package net.augustus.modules.modes.spammer;

import java.util.function.BiFunction;
import net.augustus.modules.fun.Spammer;
import net.augustus.settings.DoubleValue;

public class NoneSpammerRandomMode extends SpammerRandomMode {

   public NoneSpammerRandomMode(Spammer spammer, DoubleValue doubleValue) {
      super(spammer, doubleValue, "None");
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new NoneSpammerRandomMode((Spammer)obj, (DoubleValue)obj2);
   }

   @Override
   public String generateRandomSuffix() {
      return "";
   }

   @Override
   public void updateEventSubscriptions() {
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

}
