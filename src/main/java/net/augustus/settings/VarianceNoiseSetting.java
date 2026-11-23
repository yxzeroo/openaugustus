package net.augustus.settings;

import net.augustus.modules.modes.killaura.unoise3.UNoise3SettingContext;
import net.augustus.modules.Module;

public class VarianceNoiseSetting extends AbstractNoiseSetting {
   private StringValue varianceSetting;
   private StringValue strengthSetting;
   private Exception creationTrace;

   @Override
   public Setting withDisplayName(String str) {
      return this.named(str);
   }

   public VarianceNoiseSetting named(String str) {
      super.withDisplayName(str);
      this.noisegroup.withDisplayName(str);
      if (this.strengthSetting != null) {
         this.strengthSetting.named(str);
      }

      return this;
   }

   public VarianceNoiseSetting(Module module, String str, DoubleValue doubleValue, boolean flag) {
       super(module, str, doubleValue, flag);
      this.creationTrace = new Exception();
   }

   @Override
   public void renderTooltip() {
   }

   public VarianceNoiseSetting(UNoise3SettingContext uNoise3SettingContext, boolean flag) {
       super(uNoise3SettingContext.getModule(), uNoise3SettingContext.getParentGroup().getName() + "_noise", uNoise3SettingContext.getParentGroup(), flag);
      this.creationTrace = new Exception();
   }

   public VarianceNoiseSetting withVariance(double d, double d2, double d3) {
      StringValue stringValue = new StringValue(this.getParentModule(), this.getName() + "_variance", d, d2, d3, 0, this.noisegroup);
      String str = "Variance";
      this.varianceSetting = stringValue.named(str);
      return this;
   }

   public VarianceNoiseSetting(UNoise3SettingContext uNoise3SettingContext, String str, boolean flag) {
       super(uNoise3SettingContext.getModule(), uNoise3SettingContext.getParentGroup().getName() + "_" + str, uNoise3SettingContext.getParentGroup(), flag);
      this.creationTrace = new Exception();
   }

   public VarianceNoiseSetting withStrength(double d, double d2, double d3, int n) {
      StringValue stringValue = new StringValue(this.getParentModule(), this.getName() + "_strength", d, d2, d3, n, this.noisegroup);
      String displayName2 = this.getDisplayName();
      this.strengthSetting = stringValue.named(displayName2);
      return this;
   }

   public double sampleCentered(double d, double d2) {
      boolean flag = true;
      return this.sample(flag) * d2 + d;
   }

   @Override
   public void resetToDefault() {
   }

   public double sampleUnitRange() {
      boolean flag = true;
      return this.sample(flag) * 0.5 + 0.5;
   }

   @Override
   public double sample() {
      float f = 0.0F;
      boolean normalized2 = this.normalized;
      return this.sampleWithOffset(normalized2, f);
   }

   public double sample(boolean flag) {
      float f = 0.0F;
      return this.sampleWithOffset(flag, f);
   }

   public double sampleWithVariance(double d, double d2) {
      boolean flag = true;
      double d3 = this.sample(flag) * d2;
      boolean flag2 = true;
      return d3 * this.varianceSetting.getValue(flag2) / this.varianceSetting.getMaxValue() + d;
   }

   public double sampleWithOffset(boolean flag, float f) {
      if (this.strengthSetting == null && !flag) {
         this.creationTrace.printStackTrace();
         return 0.0;
      } else {
         double d = 0.0;
         double d2 = 0.0;

         for (NoiseLayerSetting noiseLayerSetting : (Iterable<NoiseLayerSetting>)(this.layers)) {
            double effectiveStrength = noiseLayerSetting.getEffectiveStrength();
            if (effectiveStrength != 0.0) {
               float f2 = noiseLayerSetting.getPhaseOffset() + f;
               d += noiseLayerSetting.sampleAt(f2) * effectiveStrength;
               d2 += effectiveStrength;
            }
         }

         if (d2 == 0.0) {
            d2 = 1.0;
         }

         double d3 = d / d2;
         if (!flag) {
            boolean flag2 = true;
            d3 *= this.strengthSetting.getValue(flag2);
         }

         return d3;
      }
   }


}
