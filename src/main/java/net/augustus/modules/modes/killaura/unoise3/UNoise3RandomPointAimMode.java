package net.augustus.modules.modes.killaura.unoise3;

import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.math.MathStatisticsUtil;
import net.augustus.utils.rotation.HitboxAngleUtil;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;

public class UNoise3RandomPointAimMode extends UNoise3AimMode {
   private int nextUpdateTick;
   private UNoise3RandomForceTicker localTicker;
   private BooleanValue synchronize;
   private UNoise3RandomForceTicker globalTicker;
   private double targetPointRatio;
   private double prevPointRatio;

   @Override
   public double computeTargetAngle(UNoise3AimContext uNoise3AimContext) {
      UNoise3RandomForceTicker localTicker2 = this.localTicker;
      localTicker2.tick();
      if (this.synchronize.getValue()) {
         localTicker2 = this.globalTicker;
      }

      if (this.nextUpdateTick <= localTicker2.lastRegenTick || this.nextUpdateTick > mc.thePlayer.ticksExisted) {
         this.prevPointRatio = this.targetPointRatio;
         this.targetPointRatio = Math.random();
         this.nextUpdateTick = localTicker2.lastRegenTick + 1;
      }

      boolean flag = true;
      double[] effectiveAngleBounds = this.getEffectiveAngleBounds(uNoise3AimContext, flag);
      double d = MathStatisticsUtil.lerp(this.prevPointRatio, this.targetPointRatio, Math.min((double)localTicker2.elapsedTicks / (double)localTicker2.interpolationDuration, 1.0));
      return HitboxAngleUtil.lerpRange(effectiveAngleBounds, d);
   }

   public UNoise3RandomPointAimMode(UNoise3SettingContext uNoise3SettingContext2, UNoise3RandomForceTicker uNoise3RandomForceTicker, boolean flag) {
       super(uNoise3SettingContext2, "Randompoint", UNoise3AimConstraintType.SURFACE_LIMITER, true, flag);
      this.globalTicker = uNoise3RandomForceTicker;
      BooleanValue booleanValue = new BooleanValue(uNoise3SettingContext2.getModule(), this.getSettingsGroup().getName() + "_synchronize", false, this.getSettingsGroup());
      String str = "Synchronize with global ticker";
      this.synchronize = (BooleanValue)booleanValue.withDisplayName(str);
      this.localTicker = new UNoise3RandomForceTicker();
      UNoise3RandomForceTicker localTicker2 = this.localTicker;
      DoubleValue settingsGroup2 = this.getSettingsGroup();
      SettingVisibilityCondition condition = () -> {
         return !this.synchronize.getValue();
      };
      DoubleValue doubleValue = settingsGroup2;
      localTicker2.initSettings(uNoise3SettingContext2, doubleValue, condition);
   }

}
