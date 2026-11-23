package net.augustus.utils.rotation;

import net.augustus.modules.modes.killaura.unoise3.UNoise3SettingContext;
import net.augustus.settings.IntervalNoiseSetting;
import net.augustus.settings.NoiseLayerBuilder;
import net.augustus.settings.NoiseLayerSetting;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.math.StatisticalSampleBuffer;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class RotationOvershootController {
   private BooleanValue applymisscalccorrectly;
   private RandomizedRangeSettings maxmissdegrees;
   private StringValue buffersize;
   private StatisticalSampleBuffer missDeltaBuffer;
   private RandomizedRangeSettings suppressiontime;
   private int suppressionTicks;
   public BooleanValue applyovershoottomousespeed;
   private IntervalNoiseSetting missstrength;

   public boolean shouldApplyToMouseSpeed() {
      return this.applyovershoottomousespeed.getValue();
   }

   public double computeOvershootMultiplier(double d) {
      this.missstrength.stepLayers();
      if (this.missDeltaBuffer == null || (double)this.missDeltaBuffer.getMaxSize() != this.buffersize.getValue()) {
         this.missDeltaBuffer = new StatisticalSampleBuffer((int)this.buffersize.getValue());
      }

      if (this.suppressionTicks > 0) {
         this.suppressionTicks--;
         return 1.0;
      } else {
         double d2 = this.missstrength.sample() + 1.0;
         double d3 = d2 * d;
         double d4 = (d - d3) * 0.15;
         this.missDeltaBuffer.add(d4);
         if ((this.applymisscalccorrectly.getValue() ? Math.abs(this.missDeltaBuffer.sum()) : this.missDeltaBuffer.sum()) >= this.maxmissdegrees.getLastValue()) {
            this.missDeltaBuffer.clear();
            boolean flag = true;
            this.maxmissdegrees.nextValue(flag);
            boolean flag2 = true;
            this.suppressionTicks = this.suppressiontime.nextInt(flag2);
            if (this.suppressionTicks > 0) {
               return 1.0;
            }
         }

         return d2;
      }
   }

   public RotationOvershootController(UNoise3SettingContext uNoise3SettingContext, String str) {
       super();
      DoubleValue doubleValue = new DoubleValue(uNoise3SettingContext.getModule(), uNoise3SettingContext.getParentGroup().getName() + "_" + str, uNoise3SettingContext.getParentGroup());
      String str2 = "Over / Undershoot " + str;
      DoubleValue doubleValue2 = (DoubleValue)doubleValue.withDisplayName(str2);
      SettingVisibilityCondition visibilityCondition = uNoise3SettingContext.getVisibilityCondition();
      doubleValue2.visibleWhen(visibilityCondition);
      uNoise3SettingContext = uNoise3SettingContext.withParentGroup(doubleValue2);
      IntervalNoiseSetting intervalNoiseSetting = new IntervalNoiseSetting(uNoise3SettingContext.getModule(), doubleValue2.getName() + "_missstrength", doubleValue2, false);
      String str3 = "Miss Strength";
      this.missstrength = intervalNoiseSetting.named(str3);
      byte b = 2;
      double d = 0.0;
      double d2 = 0.0;
      double d3 = 1.0;
      double d4 = -1.0;
      this.missstrength.withInterval(d4, d3, d2, d, b);
      IntervalNoiseSetting missstrength2 = this.missstrength;
      NoiseLayerBuilder noiseLayerBuilder = this.missstrength.newLayerBuilder();
      float f = 16.6F;
      NoiseLayerSetting noiseLayerSetting = noiseLayerBuilder.withStepDeltaMs(f).build();
      missstrength2.addLayer(noiseLayerSetting);
      IntervalNoiseSetting missstrength3 = this.missstrength;
      NoiseLayerBuilder noiseLayerBuilder2 = this.missstrength.newLayerBuilder();
      float f2 = 16.6F;
      NoiseLayerSetting noiseLayerSetting2 = noiseLayerBuilder2.withStepDeltaMs(f2).build();
      missstrength3.addLayer(noiseLayerSetting2);
      byte b2 = 1;
      this.missstrength.enableSharpness(b2);
      StringValue stringValue = new StringValue(uNoise3SettingContext.getModule(), doubleValue2.getName() + "_buffersize", 10.0, 2.0, 30.0, 0, doubleValue2);
      String str4 = "Buffersize";
      this.buffersize = stringValue.named(str4);
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext.getModule(), doubleValue2.getName() + "_maxmissdegrees", 5.0, 6.0, 0.1, 30.0, 2, doubleValue2);
      String str5 = "Max degree by which it misses";
      this.maxmissdegrees = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str5);
      randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext.getModule(), doubleValue2.getName() + "_suppressiontime", 0.0, 0.0, 0.0, 40.0, 0, doubleValue2);
      String str6 = "Suppression time";
      this.suppressiontime = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str6);
      BooleanValue booleanValue = new BooleanValue(uNoise3SettingContext.getModule(), doubleValue2.getName() + "_applymisscalccorrectly", false, doubleValue2);
      String str7 = "Apply misscalculation as intended";
      this.applymisscalccorrectly = (BooleanValue)booleanValue.withDisplayName(str7);
      BooleanValue booleanValue2 = new BooleanValue(uNoise3SettingContext.getModule(), doubleValue2.getName() + "_applyovershoottomousespeed", false, doubleValue2);
      String str8 = "Apply overshoot to mousespeed";
      this.applyovershoottomousespeed = (BooleanValue)booleanValue2.withDisplayName(str8);
   }




}
