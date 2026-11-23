package net.augustus.utils.rotation;

import net.augustus.modules.modes.killaura.unoise3.UNoise3SettingContext;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;

public class RotationSilenceController {
   private BooleanValue keepticking;
   private BooleanValue synchronizesilence;
   private boolean silentActive;
   private RandomizedRangeSettings silencestrength;
   private RandomizedRangeSettings nonsilentinterval;
   private double currentDecreaseFactor;
   private RandomizedRangeSettings silentinterval;
   private RandomizedRangeSettings silencedecreasefactor;
   private BooleanValue allowforsilence;
   private int remainingIntervalTicks;
   private BooleanValue shutup;

   public void reset() {
      double d = 0.0;
      this.silencestrength.setLastValue(d);
      boolean flag = true;
      this.remainingIntervalTicks = this.nonsilentinterval.nextInt(flag);
      if (this.remainingIntervalTicks <= 1) {
         this.remainingIntervalTicks = 2;
      }
   }

   public boolean shouldSilenceOffTarget(boolean flag) {
      return this.synchronizesilence != null && this.synchronizesilence.getValue() ? flag : this.shutup.getValue();
   }

   private boolean areSilenceSettingsVisible(boolean flag) {
      return flag || this.allowforsilence.getValue() && !this.synchronizesilence.getValue();
   }

   public boolean shouldKeepTicking(boolean flag) {
      return this.synchronizesilence != null && this.synchronizesilence.getValue() ? flag : this.keepticking.getValue();
   }

   public double computeSilenceFactor(double d) {
      if (this.allowforsilence != null && !this.allowforsilence.getValue()) {
         return 0.0;
      } else if (this.allowforsilence != null && this.synchronizesilence.getValue()) {
         return d;
      } else {
         if (this.remainingIntervalTicks > 0) {
            this.remainingIntervalTicks--;
         }

         if (this.remainingIntervalTicks == 0) {
            this.silentActive = !this.silentActive;
            boolean flag = false;
            if (this.silentActive) {
               flag = true;
            } else {
               boolean flag2 = true;
               this.remainingIntervalTicks = this.nonsilentinterval.nextInt(flag2);
               if (this.remainingIntervalTicks == 0) {
                  flag = true;
                  this.silentActive = true;
               }
            }

            if (flag) {
               boolean flag3 = true;
               this.remainingIntervalTicks = this.silentinterval.nextInt(flag3);
               boolean flag4 = true;
               this.currentDecreaseFactor = this.silencedecreasefactor.nextValue(flag4);
               boolean flag5 = true;
               this.silencestrength.nextValue(flag5);
            }
         }

         return !this.silentActive ? 0.0 : (1.0 - (double)this.remainingIntervalTicks / (double)this.silentinterval.getLastIntValue() * this.currentDecreaseFactor) * this.silencestrength.getLastValue();
      }
   }

   public RotationSilenceController(UNoise3SettingContext uNoise3SettingContext, boolean flag) {
       super();
      DoubleValue doubleValue = new DoubleValue(uNoise3SettingContext.getModule(), uNoise3SettingContext.getParentGroup().getName() + "_silence", uNoise3SettingContext.getParentGroup());
      String str = "Silence";
      DoubleValue doubleValue2 = (DoubleValue)doubleValue.withDisplayName(str);
      if (!flag) {
         BooleanValue booleanValue = new BooleanValue(uNoise3SettingContext.getModule(), uNoise3SettingContext.getParentGroup().getName() + "_allowforsilence", false, doubleValue2);
         String str2 = "Enable Silence";
         this.allowforsilence = (BooleanValue)booleanValue.withDisplayName(str2);
         booleanValue = new BooleanValue(uNoise3SettingContext.getModule(), uNoise3SettingContext.getParentGroup().getName() + "_synchronizesilence", true, doubleValue2);
         String str3 = "Synchronize Silence";
         booleanValue = (BooleanValue)booleanValue.withDisplayName(str3);
         BooleanValue allowforsilence2 = this.allowforsilence;
         this.allowforsilence.getClass();
         BooleanValue booleanValue2 = allowforsilence2;
         SettingVisibilityCondition condition = () -> booleanValue2.getValue();
         this.synchronizesilence = (BooleanValue)booleanValue.withVisibility(condition);
      }

      SettingVisibilityCondition condition2 = () -> this.areSilenceSettingsVisible(flag);
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext.getModule(), uNoise3SettingContext.getParentGroup().getName() + "_silentinterval", 1.0, 1.0, 1.0, 20.0, 0, doubleValue2);
      String str4 = "Silent Interval Length";
      this.silentinterval = (RandomizedRangeSettings)((RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str4)).withVisibility(condition2);
      randomizedRangeSettings = (RandomizedRangeSettings)(new RandomizedRangeSettings(uNoise3SettingContext.getModule(), uNoise3SettingContext.getParentGroup().getName() + "_nonsilentinterval", 3.0, 5.0, 0.0, 40.0, 0, doubleValue2))
         .withVisibility(condition2);
      String str5 = "Loud interval";
      this.nonsilentinterval = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str5);
      randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext.getModule(), uNoise3SettingContext.getParentGroup().getName() + "_silencestrength", 1.0, 1.0, 0.0, 1.0, 2, doubleValue2);
      String str6 = "Silence Strength";
      this.silencestrength = ((RandomizedRangeSettings)((RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str6)).withVisibility(condition2)).withoutNotInARow();
      randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext.getModule(), uNoise3SettingContext.getParentGroup().getName() + "_silencedecreasefactor", 0.0, 0.0, 0.0, 1.0, 2, doubleValue2);
      String str7 = "Silence Decrease Factor";
      this.silencedecreasefactor = ((RandomizedRangeSettings)((RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str7)).withVisibility(condition2)).withoutNotInARow();
      BooleanValue booleanValue3 = new BooleanValue(uNoise3SettingContext.getModule(), uNoise3SettingContext.getParentGroup().getName() + "_keepticking", true, doubleValue2);
      String str8 = "Keep Ticking";
      this.keepticking = (BooleanValue)((BooleanValue)booleanValue3.withDisplayName(str8)).withVisibility(condition2);
      BooleanValue booleanValue4 = new BooleanValue(uNoise3SettingContext.getModule(), uNoise3SettingContext.getParentGroup().getName() + "_shutup", false, doubleValue2);
      String str9 = "Be silent even if not on target";
      this.shutup = (BooleanValue)((BooleanValue)booleanValue4.withDisplayName(str9)).withVisibility(condition2);
   }

}
