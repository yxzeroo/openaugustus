package net.augustus.modules.modes.killaura.unoise3;

import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.interfaces.MC;

public class UNoise3RandomForceTicker implements MC {
   public int interpolationDuration;
   private RandomizedRangeSettings interpolationDurationSetting;
   public int elapsedTicks;
   private BooleanValue syncInterpolationSetting;
   public int lastRegenTick;
   private RandomizedRangeSettings regenDelaySetting;
   private int ticksUntilRegen;

   private boolean isInterpolationDurationVisible() {
      return !this.syncInterpolationSetting.getValue();
   }

   public void initSettings(UNoise3SettingContext uNoise3SettingContext, DoubleValue doubleValue, SettingVisibilityCondition condition) {
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext.getModule(), doubleValue.getName() + "_setDelay", 1.0, 5.0, 1.0, 20.0, 0, doubleValue);
      String str = "Regeneration delay";
      this.regenDelaySetting = (RandomizedRangeSettings)((RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str)).withoutNotInARow().withVisibility(condition);
      BooleanValue booleanValue = new BooleanValue(uNoise3SettingContext.getModule(), doubleValue.getName() + "_syncInterpolation", false, doubleValue);
      String str2 = "Synchronize interpolation with regeneration";
      this.syncInterpolationSetting = (BooleanValue)((BooleanValue)booleanValue.withDisplayName(str2)).withVisibility(condition);
      randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext.getModule(), doubleValue.getName() + "_interpolationDuration", 1.0, 5.0, 1.0, 20.0, 0, doubleValue);
      String str3 = "Interpolation Duration";
      randomizedRangeSettings = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str3);
      SettingVisibilityCondition condition2 = () -> this.isInterpolationDurationVisible();
      this.interpolationDurationSetting = (RandomizedRangeSettings)((RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition2)).withoutNotInARow().withVisibility(condition);
   }

   public void tick() {
      if (this.ticksUntilRegen == 0) {
         this.lastRegenTick = mc.thePlayer.ticksExisted;
         boolean flag = true;
         this.ticksUntilRegen = this.regenDelaySetting.nextInt(flag);
         this.elapsedTicks = 0;
         boolean flag2 = true;
         this.interpolationDuration = this.interpolationDurationSetting.nextInt(flag2);
         if (this.syncInterpolationSetting.getValue()) {
            this.interpolationDuration = this.ticksUntilRegen;
         }
      }

      this.ticksUntilRegen--;
      this.elapsedTicks++;
   }





}
