package net.augustus.modules.modes.killaura.unoise3;

import net.augustus.utils.rotation.HitboxAngleUtil;
import net.augustus.settings.BooleanValue;
import net.minecraft.util.MathHelper;

public class UNoise3NearestToAimGoalMode extends UNoise3AimMode {
   private BooleanValue expandbb;

   @Override
   public double computeTargetAngle(UNoise3AimContext uNoise3AimContext) {
      int[] controlFlowState = UNoise3CircleAimMode.getControlFlowState();
      boolean value = this.expandbb.getValue();
      double[] effectiveAngleBounds = this.getEffectiveAngleBounds(uNoise3AimContext, value);
      if (uNoise3AimContext.vertical) {
         double lastGoalAngle = uNoise3AimContext.lastGoalAngle;
         double d = HitboxAngleUtil.inverseLerpRange(effectiveAngleBounds, lastGoalAngle);
         if (!(d < 0.0) && !(d > 1.0)) {
            return uNoise3AimContext.lastGoalAngle;
         } else {
            double d2 = Math.abs(effectiveAngleBounds[0] - uNoise3AimContext.lastGoalAngle);
            double d3 = Math.abs(effectiveAngleBounds[1] - uNoise3AimContext.lastGoalAngle);
            return d2 > d3 ? effectiveAngleBounds[1] : effectiveAngleBounds[0];
         }
      } else {
         double normalizedYawPosition = HitboxAngleUtil.getNormalizedYawPosition(effectiveAngleBounds[0], effectiveAngleBounds[1], uNoise3AimContext.lastGoalAngle);
         if (!(normalizedYawPosition < 0.0) && !(normalizedYawPosition > 1.0)) {
            return uNoise3AimContext.lastGoalAngle;
         } else {
            double d4 = HitboxAngleUtil.getShortestAngularDistance(MathHelper.wrapAngleTo180_double(effectiveAngleBounds[0]), uNoise3AimContext.lastGoalAngle);
            double d5 = HitboxAngleUtil.getShortestAngularDistance(MathHelper.wrapAngleTo180_double(effectiveAngleBounds[1]), uNoise3AimContext.lastGoalAngle);
            return d4 > d5 ? effectiveAngleBounds[1] : effectiveAngleBounds[0];
         }
      }
   }

   public UNoise3NearestToAimGoalMode(UNoise3SettingContext uNoise3SettingContext2, boolean flag) {
       super(uNoise3SettingContext2, "NearestVec to current aimgoal", UNoise3AimConstraintType.SURFACE_LIMITER, true, flag);
      BooleanValue booleanValue = new BooleanValue(uNoise3SettingContext2.getModule(), this.getSettingsGroup().getName() + "_expandbb", false, this.getSettingsGroup());
      String str = "Expand bounding box";
      this.expandbb = (BooleanValue)booleanValue.withDisplayName(str);
   }

}
