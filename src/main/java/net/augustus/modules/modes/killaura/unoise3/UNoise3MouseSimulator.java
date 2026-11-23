package net.augustus.modules.modes.killaura.unoise3;

import net.augustus.settings.NoiseLayerBuilder;
import net.augustus.settings.NoiseLayerSetting;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.VarianceNoiseSetting;
import net.augustus.utils.math.MathStatisticsUtil;
import net.augustus.utils.math.MutableDoubleRef;
import net.augustus.utils.math.Vec2d;
import net.augustus.utils.rotation.RotationOvershootController;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.minecraft.util.MathHelper;

public class UNoise3MouseSimulator {
   public Runnable onGoalReached;
   private double baseAcceleration;
   private double prevYawVelocity;
   private double lastYawVelocity;
   private RandomizedRangeSettings uNoise3ResyncRate;
   public double postDecelerationFactor;
   private double prevPitchVelocity;
   private RotationOvershootController horizontalOvershoot;
   public double highSpeedThresholdDeg;
   private static final int MAX_BINARY_SEARCH_ITERS = 100;
   private RandomizedRangeSettings uNoise3VerticalSmoothingValue;
   private RotationOvershootController verticalOvershoot;
   private float verticalDisturbanceOffset;
   private static final double MIN_SEARCH_ACCEL = 0.001;
   public boolean disableNoiseNearTarget;
   private double lastPitchVelocity;
   private double cappedAcceleration;
   private RandomizedRangeSettings uNoise3HorizontalSmoothingValue;
   public double degreesPerStep;
   public int resyncStepThreshold;
   public Vec2d currentVelocity;
   private static final double SUBSTEPS_PER_TICK = 3.0;
   private boolean accelerationCapped;
   private BooleanValue uNoise3AdditionalSmoothing;
   private double currentResyncRate;
   public double preDecelerationFactor;
   public Runnable onResyncTriggered;
   private static final double BINARY_SEARCH_EPSILON = 1.0E-9;
   private VarianceNoiseSetting disturbance;
   public double postHighSpeedDecelFactor;

   public ArrayList simulateSimpleAxis(double d, double d2, double d3, double d4) {
      MouseTrajectoryConfig mouseTrajectoryConfig = new MouseTrajectoryConfig();
      mouseTrajectoryConfig.targetDistance = d;
      mouseTrajectoryConfig.targetVelocity = 0.0;
      mouseTrajectoryConfig.maxAcceleration = d3;
      mouseTrajectoryConfig.deceleration = mouseTrajectoryConfig.maxAcceleration;
      mouseTrajectoryConfig.reverseDeceleration = d4;
      mouseTrajectoryConfig.currentVelocity = d2;
      return this.simulateTrajectory(mouseTrajectoryConfig);
   }

   public MouseSimulationDelta simulateSingleSubstep(double d, double d2, double d3, int n) {
      int[] controlFlowState = UNoise3SettingContext.getControlFlowState();
      if (Math.signum(d3) != Math.signum(d) && d != 0.0) {
         d3 = 0.0;
      } else {
         d3 = Math.abs(d3);
      }

      double baseAcceleration2 = this.baseAcceleration;
      double baseAcceleration3 = this.baseAcceleration;
      double d4 = this.currentVelocity.y;
      ArrayList list = this.simulateSimpleAxis(d2, d4, baseAcceleration2, baseAcceleration2);
      int size = list.size();
      MouseSimulationStep mouseSimulationStep = new MouseSimulationStep(0.0, 0.0, 0.0);
      MouseTrajectoryConfig mouseTrajectoryConfig = new MouseTrajectoryConfig(d, this.currentVelocity.x, d3, baseAcceleration3, baseAcceleration3, baseAcceleration3);
      MouseTrajectoryConfig mouseTrajectoryConfig2 = this.solveAccelerationForSteps(size, mouseTrajectoryConfig);
      ArrayList list2 = this.simulateTrajectory(mouseTrajectoryConfig2);
      ArrayList list3 = list;
      if (list2.size() > size || mouseTrajectoryConfig2.maxAcceleration > baseAcceleration3) {
         MouseTrajectoryConfig mouseTrajectoryConfig3 = new MouseTrajectoryConfig(d, this.currentVelocity.x, d3, baseAcceleration3, baseAcceleration3, baseAcceleration3);
         list2 = this.simulateTrajectory(mouseTrajectoryConfig3);
         int size2 = list2.size();
         MouseTrajectoryConfig mouseTrajectoryConfig4 = new MouseTrajectoryConfig(d2, this.currentVelocity.y, 0.0, baseAcceleration2, baseAcceleration2, baseAcceleration2);
         int n2 = size2;
         MouseTrajectoryConfig mouseTrajectoryConfig5 = this.solveAccelerationForSteps(n2, mouseTrajectoryConfig4);
         list3 = this.simulateTrajectory(mouseTrajectoryConfig5);
      }

      if (this.resyncStepThreshold >= list2.size() || this.resyncStepThreshold >= list3.size()) {
         if (this.onResyncTriggered != null) {
            this.onResyncTriggered.run();
         }

         this.resyncStepThreshold = -1;
      }

      if (this.resyncStepThreshold == -1) {
         this.resyncStepThreshold = (int)((double)Math.max(list2.size(), list3.size()) * this.currentResyncRate);
         boolean flag = true;
         this.currentResyncRate = this.uNoise3ResyncRate.nextValue(flag);
      }

      byte b = 0;
      double velocity = this.getStepOrFallback(list2, b, mouseSimulationStep).velocity;
      byte b2 = 0;
      double velocity2 = this.getStepOrFallback(list3, b2, mouseSimulationStep).velocity;
      if (this.uNoise3AdditionalSmoothing.getValue()) {
         if (Math.abs(this.prevYawVelocity) > Math.abs(this.lastYawVelocity)
            && Math.abs(velocity) > Math.abs(this.lastYawVelocity)
            && Math.signum(this.prevYawVelocity) == Math.signum(this.lastYawVelocity)
            && Math.signum(this.lastYawVelocity) == Math.signum(velocity)) {
            boolean flag2 = true;
            velocity = MathStatisticsUtil.lerp(this.lastYawVelocity, velocity, 1.0 - this.uNoise3HorizontalSmoothingValue.nextValue(flag2) / 100.0);
         }

         if (Math.abs(this.prevPitchVelocity) > Math.abs(this.lastPitchVelocity)
            && Math.abs(velocity2) > Math.abs(this.lastPitchVelocity)
            && Math.signum(this.prevPitchVelocity) == Math.signum(this.lastPitchVelocity)
            && Math.signum(this.lastPitchVelocity) == Math.signum(velocity2)) {
            boolean flag3 = true;
            velocity2 = MathStatisticsUtil.lerp(this.lastPitchVelocity, velocity2, 1.0 - this.uNoise3VerticalSmoothingValue.nextValue(flag3) / 100.0);
         }
      }

      this.prevYawVelocity = this.lastYawVelocity;
      this.prevPitchVelocity = this.lastPitchVelocity;
      this.lastYawVelocity = velocity;
      this.lastPitchVelocity = velocity2;
      this.disturbance.stepLayers();
      double d5 = this.disturbance.sample();
      float f = this.verticalDisturbanceOffset;
      boolean flag4 = false;
      double d6 = this.disturbance.sampleWithOffset(flag4, f);
      byte b3 = 0;
      byte b4 = 0;
      Vec2d vec2d = new Vec2d(this.getStepOrFallback(list2, b3, mouseSimulationStep).position, this.getStepOrFallback(list3, b4, mouseSimulationStep).position);
      double d7 = vec2d.x;
      double d8 = this.horizontalOvershoot.computeOvershootMultiplier(d7);
      double d9 = vec2d.y;
      double d10 = this.verticalOvershoot.computeOvershootMultiplier(d9);
      if (this.horizontalOvershoot.shouldApplyToMouseSpeed()) {
         velocity = (velocity - this.currentVelocity.x) * d8 + this.currentVelocity.x;
      }

      if (this.verticalOvershoot.shouldApplyToMouseSpeed()) {
         velocity2 = (velocity2 - this.currentVelocity.y) * d10 + this.currentVelocity.y;
      }

      boolean flag5 = list2.size() < 2 && list3.size() < 2;
      if (n >= list2.size() && n >= list3.size() && this.disableNoiseNearTarget) {
         d6 = 0.0;
         d5 = 0.0;
         d10 = 1.0;
         d8 = 1.0;
      }

      Vec2d vec2d2 = new Vec2d(vec2d.x * d8 + vec2d.y * d5, vec2d.y * d10 + vec2d.x * d6);
      this.currentVelocity = new Vec2d(velocity, velocity2);
      return new MouseSimulationDelta(vec2d2, vec2d, flag5);
   }

   public double getEffectiveAcceleration() {
      int[] controlFlowState = UNoise3SettingContext.getControlFlowState();
      return this.accelerationCapped ? Math.min(this.baseAcceleration, this.cappedAcceleration) : this.baseAcceleration;
   }

   public MouseTrajectoryConfig solveAccelerationForSteps(int n, MouseTrajectoryConfig mouseTrajectoryConfig) {
      double d = 0.001;
      UNoise3SettingContext.getControlFlowState();
      double maxAcceleration = mouseTrajectoryConfig.maxAcceleration;

      for (int i = 0; i < 100; i++) {
         double d2 = 0.5 * (d + maxAcceleration);
         MouseTrajectoryConfig mouseTrajectoryConfig2 = mouseTrajectoryConfig.copy();
         mouseTrajectoryConfig2.maxAcceleration = mouseTrajectoryConfig2.deceleration = d2;
         ArrayList list = this.simulateTrajectory(mouseTrajectoryConfig2);
         int size = list.size();
         if (size < n) {
            maxAcceleration = d2;
         } else {
            if (size <= n) {
               MouseTrajectoryConfig mouseTrajectoryConfig3 = mouseTrajectoryConfig.copy();
               mouseTrajectoryConfig3.maxAcceleration = mouseTrajectoryConfig3.deceleration = d2;
               return mouseTrajectoryConfig3;
            }

            d = d2;
         }

         if (Math.abs(maxAcceleration - d) < 1.0E-9) {
            break;
         }
      }

      MouseTrajectoryConfig mouseTrajectoryConfig4 = mouseTrajectoryConfig.copy();
      mouseTrajectoryConfig4.maxAcceleration = mouseTrajectoryConfig4.deceleration = 0.5 * (d + maxAcceleration);
      return mouseTrajectoryConfig4;
   }

   public void clearAccelerationCap() {
      this.accelerationCapped = false;
   }

   public UNoise3MouseSimulator(UNoise3SettingContext uNoise3SettingContext) {
       super();
      this.currentVelocity = new Vec2d(0.0, 0.0);
      this.baseAcceleration = 3.0;
      this.preDecelerationFactor = 1.0;
      this.postDecelerationFactor = 1.0;
      this.postHighSpeedDecelFactor = 1.0;
      UNoise3SettingContext.getControlFlowState();
      this.highSpeedThresholdDeg = 10.0;
      this.accelerationCapped = false;
      this.cappedAcceleration = this.baseAcceleration;
      this.onGoalReached = null;
      this.onResyncTriggered = null;
      this.resyncStepThreshold = -1;
      this.currentResyncRate = 0.5;
      this.disableNoiseNearTarget = false;
      this.verticalDisturbanceOffset = (float)ThreadLocalRandom.current().nextDouble(-200.0, 200.0);
      this.degreesPerStep = 0.15;
      VarianceNoiseSetting varianceNoiseSetting = new VarianceNoiseSetting(uNoise3SettingContext, "disturbance", false);
      byte b = 2;
      double d = 0.5;
      double d2 = 0.0;
      double d3 = 0.2;
      varianceNoiseSetting = varianceNoiseSetting.withStrength(d3, d2, d, b);
      String str = "Disturbance";
      this.disturbance = varianceNoiseSetting.named(str);
      VarianceNoiseSetting disturbance2 = this.disturbance;
      NoiseLayerBuilder noiseLayerBuilder = this.disturbance.newLayerBuilder();
      float f = 16.6F;
      NoiseLayerSetting noiseLayerSetting = noiseLayerBuilder.withStepDeltaMs(f).build();
      disturbance2.addLayer(noiseLayerSetting);
      VarianceNoiseSetting disturbance3 = this.disturbance;
      NoiseLayerBuilder noiseLayerBuilder2 = this.disturbance.newLayerBuilder();
      float f2 = 16.6F;
      NoiseLayerSetting noiseLayerSetting2 = noiseLayerBuilder2.withStepDeltaMs(f2).build();
      disturbance3.addLayer(noiseLayerSetting2);
      VarianceNoiseSetting disturbance4 = this.disturbance;
      NoiseLayerBuilder noiseLayerBuilder3 = this.disturbance.newLayerBuilder();
      float f3 = 16.6F;
      NoiseLayerSetting noiseLayerSetting3 = noiseLayerBuilder3.withStepDeltaMs(f3).build();
      disturbance4.addLayer(noiseLayerSetting3);
      DoubleValue doubleValue = new DoubleValue(uNoise3SettingContext.getModule(), uNoise3SettingContext.getParentGroup().getName() + "_overshoot", uNoise3SettingContext.getParentGroup());
      String str2 = "Overshoot";
      DoubleValue doubleValue2 = (DoubleValue)doubleValue.withDisplayName(str2);
      SettingVisibilityCondition visibilityCondition = uNoise3SettingContext.getVisibilityCondition();
      DoubleValue doubleValue3 = doubleValue2.visibleWhen(visibilityCondition);
      this.horizontalOvershoot = new RotationOvershootController(uNoise3SettingContext.withParentGroup(doubleValue3), "Horizontal");
      this.verticalOvershoot = new RotationOvershootController(uNoise3SettingContext.withParentGroup(doubleValue3), "Vertical");
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext.getModule(), "UNoise3ResyncRate", 0.4, 0.6, 0.0, 1.0, 2, uNoise3SettingContext.getParentGroup());
      String str3 = "Resync rate";
      this.uNoise3ResyncRate = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str3);
      disturbance4 = this.disturbance;
      SettingVisibilityCondition visibilityCondition2 = uNoise3SettingContext.getVisibilityCondition();
      SettingVisibilityCondition condition = visibilityCondition2;
      disturbance4.withNoiseVisibility(condition);
      RandomizedRangeSettings uNoise3ResyncRate2 = this.uNoise3ResyncRate;
      SettingVisibilityCondition visibilityCondition3 = uNoise3SettingContext.getVisibilityCondition();
      uNoise3ResyncRate2.withVisibility(visibilityCondition3);
      DoubleValue doubleValue4 = new DoubleValue(uNoise3SettingContext.getModule(), "UNoise3AdditionalSmoothingGroup", uNoise3SettingContext.getParentGroup());
      String str4 = "Additional Smoothing";
      DoubleValue doubleValue5 = (DoubleValue)doubleValue4.withDisplayName(str4);
      SettingVisibilityCondition visibilityCondition4 = uNoise3SettingContext.getVisibilityCondition();
      DoubleValue doubleValue6 = doubleValue5.visibleWhen(visibilityCondition4);
      BooleanValue booleanValue = new BooleanValue(uNoise3SettingContext.getModule(), "UNoise3AdditionalSmoothing", false, doubleValue6);
      String str5 = "Additional Smoothing";
      this.uNoise3AdditionalSmoothing = (BooleanValue)booleanValue.withDisplayName(str5);
      RandomizedRangeSettings randomizedRangeSettings2 = new RandomizedRangeSettings(uNoise3SettingContext.getModule(), "UNoise3HorizontalSmoothingValue", 50.0, 50.0, 0.0, 100.0, 0, doubleValue6);
      BooleanValue booleanValue2 = this.uNoise3AdditionalSmoothing;
      this.uNoise3AdditionalSmoothing.getClass();
      BooleanValue booleanValue3 = booleanValue2;
      SettingVisibilityCondition condition2 = () -> booleanValue3.getValue();
      RandomizedRangeSettings randomizedRangeSettings3 = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition2);
      String str6 = "Horizontal smoothing";
      this.uNoise3HorizontalSmoothingValue = (RandomizedRangeSettings)randomizedRangeSettings3.withDisplayNamePrefix(str6);
      RandomizedRangeSettings randomizedRangeSettings4 = new RandomizedRangeSettings(uNoise3SettingContext.getModule(), "UNoise3VerticalSmoothingValue", 50.0, 50.0, 0.0, 100.0, 0, doubleValue6);
      BooleanValue booleanValue4 = this.uNoise3AdditionalSmoothing;
      this.uNoise3AdditionalSmoothing.getClass();
      BooleanValue booleanValue5 = booleanValue4;
      SettingVisibilityCondition condition3 = () -> booleanValue5.getValue();
      RandomizedRangeSettings randomizedRangeSettings5 = (RandomizedRangeSettings)randomizedRangeSettings4.withVisibility(condition3);
      String str7 = "Vertical smoothing";
      this.uNoise3VerticalSmoothingValue = (RandomizedRangeSettings)randomizedRangeSettings5.withDisplayNamePrefix(str7);
   }

   public void setAcceleration(double d, boolean flag) {
      int[] controlFlowState = UNoise3SettingContext.getControlFlowState();
      if (flag) {
         this.cappedAcceleration = d;
         this.accelerationCapped = true;
      } else {
         this.baseAcceleration = d;
      }
   }

   public MouseSimulationDelta simulateTickSubsteps(MutableDoubleRef mutableDoubleRef, MutableDoubleRef mutableDoubleRef2, double d, int n, MouseSimulationStepCallback callback) {
      Vec2d vec2d = new Vec2d(0.0, 0.0);
      UNoise3SettingContext.getControlFlowState();
      Vec2d vec2d2 = new Vec2d(0.0, 0.0);
      boolean flag = false;

      for (int i = 0; i < n; i++) {
         double d2 = mutableDoubleRef.getValue() / this.degreesPerStep - vec2d2.x;
         double d3 = mutableDoubleRef2.getValue() / this.degreesPerStep - vec2d2.y;
         int n2 = n - i;
         double d4 = d3;
         double d5 = d2;
         MouseSimulationDelta mouseSimulationDelta = this.simulateSingleSubstep(d5, d4, d, n2);
         Vec2d noisyDelta = mouseSimulationDelta.noisyDelta;
         vec2d = vec2d.add(noisyDelta);
         Vec2d baseDelta = mouseSimulationDelta.baseDelta;
         vec2d2 = vec2d2.add(baseDelta);
         flag |= mouseSimulationDelta.reachedGoal;
         double d6 = vec2d.y;
         double d7 = vec2d.x;
         callback.onStep(i, d7, d6);
      }

      if (flag) {
         this.onGoalReached.run();
         this.resyncStepThreshold = -1;
      }

      return new MouseSimulationDelta(vec2d, vec2d2);
   }

   public ArrayList simulateTrajectory(MouseTrajectoryConfig mouseTrajectoryConfig) {
      ArrayList list = new ArrayList();
      UNoise3SettingContext.getControlFlowState();
      double d = 0.0;
      double targetDistance = mouseTrajectoryConfig.targetDistance;
      double d2 = targetDistance;

      for (int i = 0; i < 1000 && d2 != 0.0; i++) {
         double d3 = mouseTrajectoryConfig.targetVelocity + Math.sqrt(2.0 * mouseTrajectoryConfig.deceleration * this.preDecelerationFactor * Math.abs(d2));
         double d4 = d3 * Math.signum(d2) - mouseTrajectoryConfig.currentVelocity;
         double maxAcceleration = mouseTrajectoryConfig.maxAcceleration;
         if (Math.signum(d2) != Math.signum(mouseTrajectoryConfig.currentVelocity)) {
            double postDecelerationFactor2 = this.postDecelerationFactor;
            if (Math.abs(mouseTrajectoryConfig.currentVelocity) > this.highSpeedThresholdDeg / this.degreesPerStep / 3.0) {
               postDecelerationFactor2 = this.postHighSpeedDecelFactor;
            }

            maxAcceleration = mouseTrajectoryConfig.reverseDeceleration * postDecelerationFactor2;
         } else if (Math.signum(d4) != Math.signum(d2)) {
            maxAcceleration = Double.POSITIVE_INFINITY;
         }

         double d5 = MathHelper.clamp_double(d4, -maxAcceleration, maxAcceleration);
         mouseTrajectoryConfig.currentVelocity = mouseTrajectoryConfig.currentVelocity + d5 * mouseTrajectoryConfig.stepSize;
         d2 -= mouseTrajectoryConfig.currentVelocity * mouseTrajectoryConfig.stepSize;
         d += mouseTrajectoryConfig.currentVelocity * mouseTrajectoryConfig.stepSize;
         if (Math.signum(d2) != Math.signum(mouseTrajectoryConfig.targetDistance)) {
            mouseTrajectoryConfig.currentVelocity = mouseTrajectoryConfig.targetVelocity * Math.signum(mouseTrajectoryConfig.targetDistance);
            d2 = 0.0;
            d = targetDistance;
         }

         list.add(new MouseSimulationStep(mouseTrajectoryConfig.currentVelocity, d, d5));
         if (Math.abs(d2) > 180.0 / this.degreesPerStep) {
            d2 = (Math.abs(d2) - 180.0 / this.degreesPerStep) * -Math.signum(d2);
         }
      }

      return list;
   }

   private MouseSimulationStep getStepOrFallback(ArrayList list, int n, MouseSimulationStep mouseSimulationStep) {
      int[] controlFlowState = UNoise3SettingContext.getControlFlowState();
      if (list.isEmpty()) {
         return mouseSimulationStep;
      } else if (n >= list.size()) {
         return (MouseSimulationStep)list.get(list.size() - 1);
      } else {
         return n < 0 ? mouseSimulationStep : (MouseSimulationStep)list.get(n);
      }
   }

   public boolean isAccelerationCapped() {
      return this.accelerationCapped;
   }

}
