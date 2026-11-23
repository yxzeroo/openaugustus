package net.augustus.modules.modes.killaura.unoise3;

import net.augustus.settings.IntervalNoiseSetting;
import net.augustus.settings.NoiseLayerSetting;
import net.augustus.settings.Setting;
import net.augustus.settings.VarianceNoiseSetting;
import net.augustus.utils.math.MathStatisticsUtil;
import net.augustus.utils.rotation.HitboxAngleUtil;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class UNoise3CircleAimMode extends UNoise3AimMode {
   private double currentPhaseRadians;
   public IntervalNoiseSetting radius;
   public IntervalNoiseSetting angleChange;
   public VarianceNoiseSetting center;
   private static long SHARED_SEED = ThreadLocalRandom.current().nextLong();
   private static ArrayList INSTANCES = new ArrayList();
   private static int[] controlFlowState;

   public static void clearInstances() {
      INSTANCES.clear();
   }

   @Override
   public void stepNoise() {
      this.radius.stepLayers();
      this.angleChange.stepLayers();
      this.center.stepLayers();
      double d = this.angleChange.sample() / 180.0 * 3.141592653589793;
      this.currentPhaseRadians += d;
   }

   public UNoise3CircleAimMode(UNoise3SettingContext uNoise3SettingContext2, boolean flag) {
       super(uNoise3SettingContext2, "Circle", UNoise3AimConstraintType.SURFACE_LIMITER, true, flag);
      INSTANCES.add(this);
      IntervalNoiseSetting intervalNoiseSetting = new IntervalNoiseSetting(uNoise3SettingContext2.getModule(), this.getSettingsGroup().getName() + "_radius", this.getSettingsGroup(), false);
      String str = "Radius";
      this.radius = intervalNoiseSetting.named(str);
      byte b = 2;
      double d = 0.4;
      double d2 = 0.15;
      double d3 = 0.5;
      double d4 = 0.01;
      this.radius.withInterval(d4, d3, d2, d, b);
      this.radius.addDefaultLayer();
      IntervalNoiseSetting intervalNoiseSetting2 = new IntervalNoiseSetting(uNoise3SettingContext2.getModule(), this.getSettingsGroup().getName() + "_angleChange", this.getSettingsGroup(), false);
      String str2 = "Angle rate of change";
      this.angleChange = intervalNoiseSetting2.named(str2);
      byte b2 = 2;
      double d5 = 0.4;
      double d6 = 0.2;
      double d7 = 180.0;
      double d8 = 0.01;
      this.angleChange.withInterval(d8, d7, d6, d5, b2);
      this.angleChange.addDefaultLayer();
      getControlFlowState();
      VarianceNoiseSetting varianceNoiseSetting = new VarianceNoiseSetting(uNoise3SettingContext2.getModule(), this.getSettingsGroup().getName() + "_center", this.getSettingsGroup(), false);
      String str3 = "Center";
      this.center = varianceNoiseSetting.named(str3);
      double d9 = 100.0;
      double d10 = 0.0;
      double d11 = 0.0;
      this.center.withVariance(d11, d10, d9);
      this.center.addDefaultLayer();
      Random rand = new Random(SHARED_SEED);

      for (NoiseLayerSetting noiseLayerSetting : (Iterable<NoiseLayerSetting>)(this.radius.getLayers())) {
         float f = rand.nextFloat() * 200.0F - 100.0F;
         noiseLayerSetting.setPhaseOffset(f);
      }

      for (NoiseLayerSetting noiseLayerSetting2 : (Iterable<NoiseLayerSetting>)(this.angleChange.getLayers())) {
         float f2 = rand.nextFloat() * 200.0F - 100.0F;
         noiseLayerSetting2.setPhaseOffset(f2);
      }

      for (NoiseLayerSetting noiseLayerSetting3 : (Iterable<NoiseLayerSetting>)(this.center.getLayers())) {
         float f3 = rand.nextFloat() * 200.0F - 100.0F;
         noiseLayerSetting3.setPhaseOffset(f3);
      }

      Setting.getControlFlowMarker();
   }

   public static void randomizeAllPhases() {
      float f = (float)ThreadLocalRandom.current().nextDouble(-5.0, 5.0);
      getControlFlowState();
      float f2 = (float)ThreadLocalRandom.current().nextDouble(-3.141592653589793, 3.141592653589793);

      for (UNoise3CircleAimMode uNoise3CircleAimMode : (Iterable<UNoise3CircleAimMode>)(INSTANCES)) {
         uNoise3CircleAimMode.radius.setPhasePosition(f);
         uNoise3CircleAimMode.angleChange.setPhasePosition(f);
         uNoise3CircleAimMode.center.setPhasePosition(f);
         uNoise3CircleAimMode.currentPhaseRadians = (double)f2;
      }
   }

   static {
      setControlFlowState((int[])null);
   }

   @Override
   public double computeTargetAngle(UNoise3AimContext uNoise3AimContext) {
      double d = this.radius.sample();
      double d2 = 0.5;
      double d3 = 0.0;
      double d4 = this.center.sampleWithVariance(d3, d2);
      if (uNoise3AimContext.vertical) {
         boolean flag = true;
         double[] effectiveAngleBounds = this.getEffectiveAngleBounds(uNoise3AimContext, flag);
         double d5 = MathStatisticsUtil.lerp(0.5 - d, 0.5 + d, Math.sin(this.currentPhaseRadians) * 0.5 + 0.5) - d4;
         return HitboxAngleUtil.lerpRange(effectiveAngleBounds, d5);
      } else {
         boolean flag2 = true;
         double[] effectiveAngleBounds2 = this.getEffectiveAngleBounds(uNoise3AimContext, flag2);
         double d6 = MathStatisticsUtil.lerp(0.5 - d, 0.5 + d, Math.cos(this.currentPhaseRadians) * 0.5 + 0.5) - d4;
         return HitboxAngleUtil.lerpRange(effectiveAngleBounds2, d6);
      }
   }

   public static void setControlFlowState(int[] ints) {
      controlFlowState = ints;
   }

   public static int[] getControlFlowState() {
      return controlFlowState;
   }

}
