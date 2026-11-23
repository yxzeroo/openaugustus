package net.augustus.modules.modes.clicking.legacy;

import net.augustus.settings.RandomizedRangeSettings;
import java.util.function.BiFunction;
import net.augustus.modules.Module;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.EntityLivingBase;

public class LegacyBasicClickMode extends AbstractLegacyClickMode {
   private final RandomizedRangeSettings basicDelay;
   private final TimeHelper timeHelper;

   public LegacyBasicClickMode(Module module, DoubleValue doubleValue) {
       super(module, doubleValue, "Basic");
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(this.parentModule, "BasicDelay", 50.0, 100.0, 0.0, 400.0, 0, this.modeSetting);
      String str = "Delay";
      this.basicDelay = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str);
      this.timeHelper = new TimeHelper();
   }

   @Override
   public int computeClicks(EntityLivingBase entity) {
      TimeHelper timeHelper2 = this.timeHelper;
      long lastIntValue = (long)this.basicDelay.getLastIntValue();
      boolean flag = timeHelper2.reached2(lastIntValue);
      if (flag) {
         this.timeHelper.reset();
      }

      return flag ? 1 : 0;
   }

   @Override
   public void onClickPerformed(EntityLivingBase entity) {
      this.basicDelay.nextValue();
   }

   @Override
   public void onTickUpdate(long l) {
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
      return (obj, obj2) -> new LegacyBasicClickMode((Module)obj, (DoubleValue)obj2);
   }

}
