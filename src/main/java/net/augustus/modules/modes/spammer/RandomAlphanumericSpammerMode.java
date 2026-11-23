package net.augustus.modules.modes.spammer;

import net.augustus.utils.TextFormatUtil;
import net.augustus.utils.math.DefaultRandomGenerator;
import java.util.function.BiFunction;
import net.augustus.modules.fun.Spammer;
import net.augustus.settings.DoubleValue;

public class RandomAlphanumericSpammerMode extends SpammerRandomMode {

   public RandomAlphanumericSpammerMode(Spammer spammer, DoubleValue doubleValue) {
      super(spammer, doubleValue, "RandomAlphanumeric");
   }

   @Override
   public String generateRandomSuffix() {
      byte b = 15;
      byte b2 = 8;
      int n = DefaultRandomGenerator.instance.nextInt2(b2, b);
      StringBuilder sb = new StringBuilder();
      char[] chars = TextFormatUtil.ALPHANUMERIC_CHARS.toCharArray();
      int length = chars.length;

      for (int i = 0; i < n; i++) {
         int n2 = length - 1;
         byte b3 = 0;
         sb.append(chars[DefaultRandomGenerator.instance.nextInt2(b3, n2)]);
      }

      return sb.toString();
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
      return (obj, obj2) -> new RandomAlphanumericSpammerMode((Spammer)obj, (DoubleValue)obj2);
   }

}
