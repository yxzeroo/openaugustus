package net.augustus.settings;

import net.augustus.modules.Module;

public class NoiseLayerBuilder {
   private boolean useDynamicIntervals;
   private String namePrefix;
   private String noiseType = "Sinus";
   private Module parentModule;
   private double frequency;
   private double rateOfChange;
   private DoubleValue parentGroup;
   private float stepDeltaMs;
   private int layerIndex;
   private double strength = 0.0;
   private SettingVisibilityCondition strengthVisibility;

   public NoiseLayerBuilder(Module module, DoubleValue doubleValue, String str, int n, SettingVisibilityCondition condition) {
      this.rateOfChange = 1.0;
      this.frequency = 1.0;
      this.stepDeltaMs = 50.0F;
      this.useDynamicIntervals = false;
      this.parentModule = module;
      this.parentGroup = doubleValue;
      this.namePrefix = str;
      this.layerIndex = n;
      this.strengthVisibility = condition;
   }

   public NoiseLayerSetting build() {
      NoiseLayerSetting noiseLayerSetting = new NoiseLayerSetting(this.parentModule, this.parentGroup, this.namePrefix + "_layer_" + this.layerIndex, this.strengthVisibility, this.layerIndex, this.useDynamicIntervals);
      String noiseType2 = this.noiseType;
      noiseLayerSetting.setNoiseType(noiseType2);
      double strength2 = this.strength;
      noiseLayerSetting.setStrength(strength2);
      float stepDeltaMs2 = this.stepDeltaMs;
      noiseLayerSetting.setStepDeltaMs(stepDeltaMs2);
      if (this.useDynamicIntervals) {
         IntervalNoiseSetting frequency2 = noiseLayerSetting.getFrequency();
         double frequency3 = this.frequency;
         double frequency4 = this.frequency;
         frequency2 = frequency2.withRangeValues(frequency4, frequency3);
         byte b = 1;
         frequency2.enableSharpness(b);
         frequency2 = noiseLayerSetting.getRateofchange();
         double rateOfChange2 = this.rateOfChange;
         double rateOfChange3 = this.rateOfChange;
         frequency2 = frequency2.withRangeValues(rateOfChange3, rateOfChange2);
         byte b2 = 1;
         frequency2.enableSharpness(b2);
      } else {
         double frequency5 = this.frequency;
         noiseLayerSetting.setFrequency(frequency5);
         double rateOfChange4 = this.rateOfChange;
         noiseLayerSetting.setRateOfChange(rateOfChange4);
      }

      return noiseLayerSetting;
   }

   public NoiseLayerBuilder withDynamicIntervals(boolean flag) {
      this.useDynamicIntervals = flag;
      return this;
   }

   public NoiseLayerBuilder withStrength(double d) {
      this.strength = d;
      return this;
   }

   public NoiseLayerBuilder withFrequency(double d) {
      this.frequency = d;
      return this;
   }

   public NoiseLayerBuilder withStepDeltaMs(float f) {
      this.stepDeltaMs = f;
      return this;
   }

   public NoiseLayerBuilder withRateOfChange(double d) {
      this.rateOfChange = d;
      return this;
   }





}
