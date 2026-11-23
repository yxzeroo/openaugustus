package net.augustus.modules.modes.killaura.unoise3;

import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.rotation.HitboxAngleUtil;
import net.augustus.settings.BooleanValue;
import net.minecraft.util.MathHelper;

public class UNoise3NearestVecAimMode extends UNoise3AimMode {
   private RandomizedRangeSettings nearedgethreshold;
   private BooleanValue disablewhennearedge;
   private BooleanValue expandbb;

   @Override
   public double sampleWeight(UNoise3AimContext uNoise3AimContext) {
      double d = super.sampleWeight(uNoise3AimContext);
      UNoise3CircleAimMode.getControlFlowState();
      double d2 = this.computeTargetAngle(uNoise3AimContext);
      return Double.isFinite(d2) ? d : 0.0;
   }

   public UNoise3NearestVecAimMode(UNoise3SettingContext uNoise3SettingContext2, boolean flag) {
       super(uNoise3SettingContext2, "NearestVec", UNoise3AimConstraintType.SURFACE_LIMITER, true, flag);
      BooleanValue booleanValue = new BooleanValue(uNoise3SettingContext2.getModule(), this.getSettingsGroup().getName() + "_expandbb", false, this.getSettingsGroup());
      String str = "Expand bounding box";
      this.expandbb = (BooleanValue)booleanValue.withDisplayName(str);
      booleanValue = new BooleanValue(uNoise3SettingContext2.getModule(), this.getSettingsGroup().getName() + "_disablewhennearedge", false, this.getSettingsGroup());
      String str2 = "Disable when near edge";
      this.disablewhennearedge = (BooleanValue)booleanValue.withDisplayName(str2);
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext2.getModule(), this.getSettingsGroup().getName() + "_nearedgethreshold", 0.1, 0.2, 0.0, 0.5, 3, this.getSettingsGroup());
      String str3 = "Near edge threshold";
      RandomizedRangeSettings randomizedRangeSettings2 = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str3);
      BooleanValue disablewhennearedge2 = this.disablewhennearedge;
      this.disablewhennearedge.getClass();
      BooleanValue booleanValue2 = disablewhennearedge2;
      SettingVisibilityCondition condition = () -> booleanValue2.getValue();
      this.nearedgethreshold = ((RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition)).withoutNotInARow();
   }

   @Override
   public double computeTargetAngle(UNoise3AimContext uNoise3AimContext) {
      int[] controlFlowState = UNoise3CircleAimMode.getControlFlowState();
      boolean value = this.expandbb.getValue();
      double[] effectiveAngleBounds = this.getEffectiveAngleBounds(uNoise3AimContext, value);
      if (uNoise3AimContext.vertical) {
         double currentAngle = uNoise3AimContext.currentAngle;
         double d = HitboxAngleUtil.inverseLerpRange(effectiveAngleBounds, currentAngle);
         if (this.disablewhennearedge.getValue()) {
            if (d < 0.0 || d > 1.0) {
               this.nearedgethreshold.nextValue();
               return Double.NaN;
            }

            if (d < this.nearedgethreshold.getLastValue() || d > 1.0 - this.nearedgethreshold.getLastValue()) {
               this.nearedgethreshold.nextValue();
               return Double.NaN;
            }
         }

         if (!(d < 0.0) && !(d > 1.0)) {
            return uNoise3AimContext.currentAngle;
         } else {
            double d2 = Math.abs(effectiveAngleBounds[0] - uNoise3AimContext.currentAngle);
            double d3 = Math.abs(effectiveAngleBounds[1] - uNoise3AimContext.currentAngle);
            return d2 > d3 ? effectiveAngleBounds[1] : effectiveAngleBounds[0];
         }
      } else {
         double normalizedYawPosition = HitboxAngleUtil.getNormalizedYawPosition(effectiveAngleBounds[0], effectiveAngleBounds[1], uNoise3AimContext.currentAngle);
         if (this.disablewhennearedge.getValue()) {
            if (normalizedYawPosition < 0.0 || normalizedYawPosition > 1.0) {
               this.nearedgethreshold.nextValue();
               return Double.NaN;
            }

            if (normalizedYawPosition < this.nearedgethreshold.getLastValue() || normalizedYawPosition > 1.0 - this.nearedgethreshold.getLastValue()) {
               this.nearedgethreshold.nextValue();
               return Double.NaN;
            }
         }

         if (!(normalizedYawPosition < 0.0) && !(normalizedYawPosition > 1.0)) {
            return uNoise3AimContext.currentAngle;
         } else {
            double d4 = HitboxAngleUtil.getShortestAngularDistance(MathHelper.wrapAngleTo180_double(effectiveAngleBounds[0]), uNoise3AimContext.currentAngle);
            double d5 = HitboxAngleUtil.getShortestAngularDistance(MathHelper.wrapAngleTo180_double(effectiveAngleBounds[1]), uNoise3AimContext.currentAngle);
            return d4 > d5 ? effectiveAngleBounds[1] : effectiveAngleBounds[0];
         }
      }
   }

}
