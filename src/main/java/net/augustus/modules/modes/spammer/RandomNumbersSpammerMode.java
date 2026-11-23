package net.augustus.modules.modes.spammer;

import net.augustus.utils.math.DefaultRandomGenerator;
import java.util.function.BiFunction;
import net.augustus.modules.fun.Spammer;
import net.augustus.settings.DoubleValue;

public class RandomNumbersSpammerMode extends SpammerRandomMode {

   public RandomNumbersSpammerMode(Spammer spammer, DoubleValue doubleValue) {
      super(spammer, doubleValue, "RandomNumbers");
   }

   @Override
   public String generateRandomSuffix() {
      long l = 100000000000000L;
      long l2 = 1000000000L;
      return String.valueOf(DefaultRandomGenerator.instance.nextLong2(l2, l));
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

   public static BiFunction factory() {
      return (obj, obj2) -> new RandomNumbersSpammerMode((Spammer)obj, (DoubleValue)obj2);
   }

}
