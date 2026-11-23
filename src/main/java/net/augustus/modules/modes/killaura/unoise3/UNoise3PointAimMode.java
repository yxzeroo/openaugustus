package net.augustus.modules.modes.killaura.unoise3;

import net.augustus.settings.VarianceNoiseSetting;
import net.augustus.utils.math.MathStatisticsUtil;
import net.augustus.settings.DoubleValue;
import net.minecraft.util.Vec3;

public class UNoise3PointAimMode extends UNoise3AimMode {
   private VarianceNoiseSetting x;
   private VarianceNoiseSetting z;

   public UNoise3PointAimMode(UNoise3SettingContext uNoise3SettingContext2, boolean flag) {
       super(uNoise3SettingContext2, "Point", UNoise3AimConstraintType.NONE, true, flag);
      DoubleValue settingsGroup2 = this.getSettingsGroup();
      uNoise3SettingContext2 = uNoise3SettingContext2.withParentGroup(settingsGroup2);
      VarianceNoiseSetting varianceNoiseSetting = new VarianceNoiseSetting(uNoise3SettingContext2, "x", true);
      String str = "x";
      varianceNoiseSetting = varianceNoiseSetting.named(str);
      double d = 100.0;
      double d2 = 0.0;
      double d3 = 100.0;
      this.x = varianceNoiseSetting.withVariance(d3, d2, d);
      varianceNoiseSetting = new VarianceNoiseSetting(uNoise3SettingContext2, "z", true);
      String str2 = "z";
      varianceNoiseSetting = varianceNoiseSetting.named(str2);
      double d4 = 100.0;
      double d5 = 0.0;
      double d6 = 100.0;
      this.z = varianceNoiseSetting.withVariance(d6, d5, d4);
      this.x.addDefaultLayer();
      this.z.addDefaultLayer();
   }

   @Override
   public double computeTargetAngle(UNoise3AimContext uNoise3AimContext) {
      double d = 0.5;
      double d2 = 0.5;
      double d3 = MathStatisticsUtil.lerp(uNoise3AimContext.targetBoundingBox.minX, uNoise3AimContext.targetBoundingBox.maxX, this.x.sampleWithVariance(d2, d));
      double d4 = 0.5;
      double d5 = 0.5;
      Vec3 vec = new Vec3(d3, 0.0, MathStatisticsUtil.lerp(uNoise3AimContext.targetBoundingBox.minZ, uNoise3AimContext.targetBoundingBox.maxZ, this.z.sampleWithVariance(d5, d4)));
      Vec3 vec2 = vec.subtract(uNoise3AimContext.playerEyePos);
      return Math.atan2(vec2.zCoord, vec2.xCoord) / Math.PI * 180.0 - 90.0;
   }

   @Override
   public void stepNoise() {
      this.x.stepLayers();
      this.z.stepLayers();
   }
}
