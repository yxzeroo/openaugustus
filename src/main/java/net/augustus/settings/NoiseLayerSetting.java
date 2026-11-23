package net.augustus.settings;

import net.augustus.utils.math.SimplexNoise;
import java.util.concurrent.ThreadLocalRandom;
import net.augustus.modules.Module;

public class NoiseLayerSetting extends Setting {
   private IntervalNoiseSetting frequency;
   private float stepDeltaMs;
   private float phaseOffset;
   private StringValue strength;
   private float phasePosition;
   private IntervalNoiseSetting rateofchange;
   private boolean useDynamicIntervals;
   private StringValue frequency2;
   private StringValue rateOfChange;
   private BooleansSetting type;

   public float getPhaseOffset() {
      return this.phaseOffset;
   }

   public IntervalNoiseSetting getFrequency() {
      return this.frequency;
   }

   public IntervalNoiseSetting getRateofchange() {
      return this.rateofchange;
   }

   public double getEffectiveStrength() {
      if (!this.strength.isVisible()) {
         return 1.0;
      } else {
         boolean flag = true;
         return this.strength.getValue(flag);
      }
   }

   private boolean isSinusModeSelected() {
      return this.type.getSelectedValue().equals("Sinus");
   }

   public void setNoiseType(String str) {
      this.type.setSelectedValue(str);
   }

   @Override
   public void resetToDefault() {
   }

   private boolean isNoiseModeSelected() {
      return this.type.getSelectedValue().equals("Noise");
   }

   public void setPhaseOffset(float f) {
      this.phaseOffset = f;
   }

   public void advanceByMillis(long l) {
      if (this.useDynamicIntervals) {
         if (this.type.getSelectedValue().equals("Noise")) {
            this.rateofchange.stepLayers();
            this.phasePosition = this.phasePosition + (float)this.rateofchange.sample() * (float)l / 4000.0F;
         } else {
            this.frequency.stepLayers();
            this.phasePosition = this.phasePosition + (float)this.frequency.sample() * (float)l / 1000.0F;
         }
      } else {
         if (this.type.getSelectedValue().equals("Noise")) {
            boolean flag = true;
            this.phasePosition = this.phasePosition + (float)this.rateOfChange.getValue(flag) * (float)l / 4000.0F;
         } else {
            boolean flag2 = true;
            this.phasePosition = this.phasePosition + (float)this.frequency2.getValue(flag2) * ((float)l / 1000.0F);
            this.phasePosition %= 1.0F;
         }
      }
   }

   public void step() {
      if (this.useDynamicIntervals) {
         if (this.type.getSelectedValue().equals("Noise")) {
            this.rateofchange.stepLayers();
            this.phasePosition = this.phasePosition + (float)this.rateofchange.sample() * this.stepDeltaMs / 4000.0F;
         } else {
            this.frequency.stepLayers();
            this.phasePosition = this.phasePosition + (float)this.frequency.sample() * this.stepDeltaMs / 1000.0F;
         }
      } else {
         if (this.type.getSelectedValue().equals("Noise")) {
            boolean flag = true;
            this.phasePosition = this.phasePosition + (float)this.rateOfChange.getValue(flag) * this.stepDeltaMs / 4000.0F;
         } else {
            boolean flag2 = true;
            this.phasePosition = this.phasePosition + (float)this.frequency2.getValue(flag2) * (this.stepDeltaMs / 1000.0F);
            this.phasePosition %= 1.0F;
         }
      }
   }

   public double sampleAt(float f) {
      return this.type.getSelectedValue().equals("Noise")
         ? (double)SimplexNoise.noise(this.phasePosition, f)
         : Math.sin((double)(this.phasePosition * 2.0F) * Math.PI + (double)f);
   }

   public NoiseLayerSetting(Module module, DoubleValue doubleValue, String str, SettingVisibilityCondition condition, int n, boolean flag) {
       super(module, doubleValue, str, false);
      this.phaseOffset = (float)ThreadLocalRandom.current().nextDouble();
      DoubleValue doubleValue2 = new DoubleValue(module, str + "_layer_" + n, doubleValue);
      String str2 = "Layer " + n;
      DoubleValue doubleValue3 = (DoubleValue)doubleValue2.withDisplayName(str2);
      BooleansSetting booleansSetting = new BooleansSetting(module, str + "_type", "Noise", new String[]{"Noise", "Sinus"}, doubleValue3);
      String str3 = "Type";
      this.type = (BooleansSetting)booleansSetting.withDisplayName(str3);
      StringValue stringValue = new StringValue(module, str + "_strength", 0.0, 0.1, 100.0, 0, doubleValue3);
      String str4 = "Strength";
      this.strength = stringValue.named(str4).visibleWhen(condition);
      if (flag) {
         IntervalNoiseSetting intervalNoiseSetting = new IntervalNoiseSetting(module, str + "_rateofchange", doubleValue3, false);
         String str5 = "Rate of change";
         this.rateofchange = intervalNoiseSetting.named(str5);
         IntervalNoiseSetting intervalNoiseSetting2 = new IntervalNoiseSetting(module, str + "_frequency", doubleValue3, false);
         String str6 = "Frequency";
         this.frequency = intervalNoiseSetting2.named(str6);
         byte b = 2;
         double d = 1.0;
         double d2 = 1.0;
         double d3 = 25.0;
         double d4 = 0.1;
         this.frequency.withInterval(d4, d3, d2, d, b);
         byte b2 = 2;
         double d5 = 1.0;
         double d6 = 1.0;
         double d7 = 25.0;
         double d8 = 0.1;
         this.rateofchange.withInterval(d8, d7, d6, d5, b2);
         this.rateofchange.addDefaultLayer();
         this.frequency.addDefaultLayer();
         IntervalNoiseSetting rateofchange2 = this.rateofchange;
         SettingVisibilityCondition condition2 = () -> {
            return this.type.getSelectedValue().equals("Noise");
         };
         SettingVisibilityCondition condition3 = condition2;
         rateofchange2.visibleWhen(condition3);
         IntervalNoiseSetting frequency3 = this.frequency;
         condition2 = () -> this.isDynamicSinusModeSelected();
         SettingVisibilityCondition condition4 = condition2;
         frequency3.visibleWhen(condition4);
         this.useDynamicIntervals = true;
      } else {
         StringValue stringValue2 = new StringValue(module, str + "_rateOfChange", 1.0, 0.1, 25.0, 2, doubleValue3);
         String str7 = "Rate of change";
         StringValue stringValue3 = stringValue2.named(str7);
         SettingVisibilityCondition condition5 = () -> this.isNoiseModeSelected();
         this.rateOfChange = stringValue3.visibleWhen(condition5);
         StringValue stringValue4 = new StringValue(module, str + "_frequency", 1.0, 0.1, 10.0, 2, doubleValue3);
         String str8 = "Frequency";
         StringValue stringValue5 = stringValue4.named(str8);
         SettingVisibilityCondition condition6 = () -> this.isSinusModeSelected();
         this.frequency2 = stringValue5.visibleWhen(condition6);
      }
   }

   public void setFrequency(double d) {
      this.frequency2.setValue(d);
   }

   public void setStrength(double d) {
      this.strength.setValue(d);
   }

   public void setPhasePosition(float f) {
      this.phasePosition = f;
   }

   public double sample() {
      float phaseOffset2 = this.phaseOffset;
      return this.sampleAt(phaseOffset2);
   }

   public void setRateOfChange(double d) {
      this.rateOfChange.setValue(d);
   }

   @Override
   public void renderTooltip() {
   }

   public void setStepDeltaMs(float f) {
      this.stepDeltaMs = f;
   }

   private boolean isDynamicSinusModeSelected() {
      return this.type.getSelectedValue().equals("Sinus");
   }


}
