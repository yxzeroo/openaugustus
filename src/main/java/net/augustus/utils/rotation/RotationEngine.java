package net.augustus.utils.rotation;

import net.augustus.ClientManagers;
import net.augustus.modules.modes.killaura.unoise3.MouseSimulationDelta;
import net.augustus.modules.modes.killaura.unoise3.MouseSimulationStepCallback;
import net.augustus.modules.modes.killaura.unoise3.UNoise3MouseSimulator;
import net.augustus.modules.modes.killaura.unoise3.UNoise3SettingContext;
import net.augustus.settings.IntervalNoiseSetting;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.AntiSamplerGuard;
import net.augustus.utils.math.MutableDoubleRef;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import net.augustus.modules.Module;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class RotationEngine {
   private RandomizedRangeSettings uNoise3ResyncUpdateDelay;
   private double curveDeltaYaw;
   private StringValue uNoise3RemoveNoiseChance;
   private final BooleanValue rotationEngineRandomizeOnlyWhileRotating;
   private IntervalNoiseSetting uNoise3SpeedAtInitialRotation;
   private float previousTargetYaw;
   private int lastAdvancedTick;
   private final RandomizedRangeSettings rotationEnginePitchAcceleration;
   private final StringValue rotationEngineInterval;
   private double curveStartYaw;
   private double largeRotationSpeed;
   private final Supplier targetChangedSupplier;
   private StringValue uNoise3LargeRotation;
   private double actualYaw;
   private final RandomizedRangeSettings rotationEngineYawSpeed;
   private double simulatedYaw;
   private final StringValue rotationEngineAmplitude;
   private RandomizedRangeSettings uNoise3RecalcRemoveNoise;
   private final BooleanValue rotationEngineBetterAcceleration;
   private int resyncDelayTicks;
   private RandomizedRangeSettings uNoise3resyncdelay;
   private IntervalNoiseSetting uNoise3Aimspeed;
   private boolean resyncQueued;
   private float previousTargetPitch;
   private int removeNoiseRecalcTicks;
   private IntervalNoiseSetting uNoise3DecelerationFactorPre;
   private int curveIntervalTicks;
   private boolean resyncPending;
   private final RandomizedRangeSettings rotationEnginePitchSpeed;
   private StringValue uNoise3ResyncChanceAtAimgoal;
   private final RandomizedRangeSettings rotationEngineYawAcceleration;
   private final BooleansSetting rotationEngineMode;
   private boolean shouldResyncAtGoal;
   private double actualPitch;
   private IntervalNoiseSetting uNoise3DecelerationFactorPostHighspeed;
   private UNoise3MouseSimulator uNoise3MouseSimulator;
   private double curveDeltaPitch;
   private StringValue uNoise3highspeed;
   private IntervalNoiseSetting uNoise3DecelerationFactorPost;
   private double curveStartPitch;
   private IntervalNoiseSetting uNoise3SpeedAtLargeRotation;
   private final StringValue rotationEngineFrequency;
   private double simulatedPitch;
   private int resyncRecalcTicks;
   private final RandomizedRangeSettings rotationEngineRandomStrength;

   private boolean isIntervalVisible() {
      return this.rotationEngineMode.getSelectedValue().equals("Curve");
   }

   private void scheduleResync() {
      if (this.resyncDelayTicks > 0) {
         this.resyncQueued = true;
      } else {
         this.triggerResync();
      }
   }

   private boolean isFrequencyVisible() {
      return this.rotationEngineMode.getSelectedValue().equals("Curve");
   }

   public Rotation rotateTowards(float f, float f2, Rotation rotation) {
      float pitch = rotation.pitch;
      float yaw = rotation.yaw;
      return this.rotateTowards(f, f2, yaw, pitch);
   }

   public Rotation rotateTowards(Rotation rotation, Rotation rotation2) {
      float pitch = rotation2.pitch;
      float yaw = rotation2.yaw;
      float pitch2 = rotation.pitch;
      float yaw2 = rotation.yaw;
      return this.rotateTowards(yaw2, pitch2, yaw, pitch);
   }

   private boolean isRandomizeOnlyWhileRotatingVisible(SettingVisibilityCondition condition) {
      return !this.rotationEngineMode.getSelectedValue().equals("Curve") && !condition.isVisible();
   }

   public Rotation computeCurvedRotation(float f, float f2, float f3, float f4, float f5, float f6, int n, float f7, float f8) {
      double d;
      exitBlock: {
         f = RotationUtil.updateAngle(f3, f, 180.0F);
         f2 = RotationUtil.updateAngle(f4, f2, 180.0F);
         d = computeMaxLinearStep(f, f2, f3, f4, f5, f6);
         double d2 = (double)f - this.curveStartYaw;
         double d3 = (double)f2 - this.curveStartPitch;
         if (this.curveIntervalTicks > 0 && haveSameSign(d2, this.curveDeltaYaw)) {
            double curveDeltaPitch2 = this.curveDeltaPitch;
            if (haveSameSign(d3, curveDeltaPitch2)) {
               break exitBlock;
            }
         }

         this.curveIntervalTicks = n;
         this.curveStartYaw = (double)f3;
         this.curveStartPitch = (double)f4;
         this.curveDeltaYaw = (double)(f - f3);
         this.curveDeltaPitch = (double)(f2 - f4);
      }

      this.curveIntervalTicks--;
      double d4 = distance2D(this.curveStartYaw, this.curveStartPitch, (double)f3, (double)f4);
      double[] doubles = sampleSinusoidalArcPoint(this.curveStartYaw, this.curveStartPitch, (double)f, (double)f2, d + d4, (double)f7, (double)f8);
      return new Rotation((float)doubles[0], (float)doubles[1]);
   }

   public RotationEngine(Module module, DoubleValue doubleValue, Supplier supplier) {
       super();
      this.previousTargetYaw = Float.NaN;
      this.previousTargetPitch = 0.0F;
      this.curveIntervalTicks = 0;
      BooleansSetting booleansSetting = new BooleansSetting(
         module, "RotationEngineMode", "Independent", new String[]{"Independent", "Linear", "Curve", "Advanced"}, doubleValue
      );
      String str = "Mode";
      this.rotationEngineMode = (BooleansSetting)booleansSetting.withDisplayName(str);
      SettingVisibilityCondition condition = () -> this.isAdvancedMode();
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(module, "RotationEngineYawSpeed", 15.0, 20.0, 0.0, 180.0, 1, doubleValue);
      String str2 = "YawSpeed";
      RandomizedRangeSettings randomizedRangeSettings2 = ((RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str2)).withoutNotInARow();
      SettingVisibilityCondition condition2 = () -> {
         return !condition.isVisible();
      };
      this.rotationEngineYawSpeed = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition2);
      RandomizedRangeSettings randomizedRangeSettings3 = new RandomizedRangeSettings(module, "RotationEnginePitchSpeed", 15.0, 20.0, 0.0, 180.0, 1, doubleValue);
      String str3 = "PitchSpeed";
      RandomizedRangeSettings randomizedRangeSettings4 = ((RandomizedRangeSettings)randomizedRangeSettings3.withDisplayNamePrefix(str3)).withoutNotInARow();
      SettingVisibilityCondition condition3 = () -> {
         return !condition.isVisible();
      };
      this.rotationEnginePitchSpeed = (RandomizedRangeSettings)randomizedRangeSettings4.withVisibility(condition3);
      RandomizedRangeSettings randomizedRangeSettings5 = new RandomizedRangeSettings(module, "RotationEngineYawAcceleration", 5.0, 10.0, 0.0, 180.0, 1, doubleValue);
      String str4 = "YawAcceleration";
      RandomizedRangeSettings randomizedRangeSettings6 = ((RandomizedRangeSettings)randomizedRangeSettings5.withDisplayNamePrefix(str4)).withoutNotInARow();
      SettingVisibilityCondition condition4 = () -> {
         return !condition.isVisible();
      };
      this.rotationEngineYawAcceleration = (RandomizedRangeSettings)randomizedRangeSettings6.withVisibility(condition4);
      RandomizedRangeSettings randomizedRangeSettings7 = new RandomizedRangeSettings(module, "RotationEnginePitchAcceleration", 5.0, 10.0, 0.0, 180.0, 1, doubleValue);
      String str5 = "PitchAcceleration";
      RandomizedRangeSettings randomizedRangeSettings8 = ((RandomizedRangeSettings)randomizedRangeSettings7.withDisplayNamePrefix(str5)).withoutNotInARow();
      SettingVisibilityCondition condition5 = () -> {
         return !condition.isVisible();
      };
      this.rotationEnginePitchAcceleration = (RandomizedRangeSettings)randomizedRangeSettings8.withVisibility(condition5);
      RandomizedRangeSettings randomizedRangeSettings9 = (new RandomizedRangeSettings(module, "RotationEngineRandomStrength", 0.0, 0.5, 0.0, 4.0, 2, doubleValue)).withoutNotInARow();
      String str6 = "RandomStrength";
      RandomizedRangeSettings randomizedRangeSettings10 = (RandomizedRangeSettings)randomizedRangeSettings9.withDisplayNamePrefix(str6);
      SettingVisibilityCondition condition6 = () -> {
         return !this.rotationEngineMode.getSelectedValue().equals("Curve") && !condition.isVisible();
      };
      this.rotationEngineRandomStrength = (RandomizedRangeSettings)randomizedRangeSettings10.withVisibility(condition6);
      BooleanValue booleanValue = new BooleanValue(module, "RotationEngineRandomizeOnlyWhileRotating", true, doubleValue);
      String str7 = "RandomizeOnlyAtRotating";
      BooleanValue booleanValue2 = (BooleanValue)booleanValue.withDisplayName(str7);
      SettingVisibilityCondition condition7 = () -> this.isRandomizeOnlyWhileRotatingVisible(condition);
      this.rotationEngineRandomizeOnlyWhileRotating = (BooleanValue)booleanValue2.withVisibility(condition7);
      StringValue stringValue = new StringValue(module, "RotationEngineInterval", 7.0, 1.0, 20.0, 0, doubleValue);
      String str8 = "Interval";
      StringValue stringValue2 = stringValue.named(str8);
      SettingVisibilityCondition condition8 = () -> this.isIntervalVisible();
      this.rotationEngineInterval = stringValue2.visibleWhen(condition8);
      StringValue stringValue3 = new StringValue(module, "RotationEngineAmplitude", 1.0, 0.1, 5.0, 2, doubleValue);
      String str9 = "Amplitude";
      StringValue stringValue4 = stringValue3.named(str9);
      SettingVisibilityCondition condition9 = () -> this.isAmplitudeVisible();
      this.rotationEngineAmplitude = stringValue4.visibleWhen(condition9);
      StringValue stringValue5 = new StringValue(module, "RotationEngineFrequency", 1.0, 1.0, 8.0, 0, doubleValue);
      String str10 = "Frequency";
      StringValue stringValue6 = stringValue5.named(str10);
      SettingVisibilityCondition condition10 = () -> this.isFrequencyVisible();
      this.rotationEngineFrequency = stringValue6.visibleWhen(condition10);
      BooleanValue booleanValue3 = new BooleanValue(module, "RotationEngineBetterAcceleration", false, doubleValue);
      String str11 = "BetterAcceleration";
      BooleanValue booleanValue4 = (BooleanValue)booleanValue3.withDisplayName(str11);
      SettingVisibilityCondition condition11 = () -> {
         return !condition.isVisible();
      };
      this.rotationEngineBetterAcceleration = (BooleanValue)booleanValue4.withVisibility(condition11);
      UNoise3SettingContext uNoise3SettingContext = new UNoise3SettingContext(module, doubleValue, condition);
      IntervalNoiseSetting intervalNoiseSetting = new IntervalNoiseSetting(uNoise3SettingContext.getModule(), "UNoise3Aimspeed", doubleValue, true);
      String str12 = "Aim speed";
      IntervalNoiseSetting intervalNoiseSetting2 = intervalNoiseSetting.named(str12);
      byte b = 1;
      double d = 20.0;
      double d2 = 15.0;
      double d3 = 100.0;
      double d4 = 1.0;
      IntervalNoiseSetting intervalNoiseSetting3 = intervalNoiseSetting2.withInterval(d4, d3, d2, d, b);
      this.uNoise3Aimspeed = intervalNoiseSetting3.visibleWhen(condition);
      IntervalNoiseSetting intervalNoiseSetting4 = new IntervalNoiseSetting(uNoise3SettingContext.getModule(), "UNoise3DecelerationFactorPre", doubleValue, false);
      String str13 = "Decelerationfactor Pre";
      IntervalNoiseSetting intervalNoiseSetting5 = intervalNoiseSetting4.named(str13);
      byte b2 = 2;
      double d5 = 1.0;
      double d6 = 1.0;
      double d7 = 2.0;
      double d8 = 0.5;
      IntervalNoiseSetting intervalNoiseSetting6 = intervalNoiseSetting5.withInterval(d8, d7, d6, d5, b2);
      this.uNoise3DecelerationFactorPre = intervalNoiseSetting6.visibleWhen(condition);
      IntervalNoiseSetting intervalNoiseSetting7 = new IntervalNoiseSetting(uNoise3SettingContext.getModule(), "UNoise3DecelerationFactorPost", doubleValue, false);
      String str14 = "Decelerationfactor Post";
      IntervalNoiseSetting intervalNoiseSetting8 = intervalNoiseSetting7.named(str14);
      byte b3 = 2;
      double d9 = 1.0;
      double d10 = 1.0;
      double d11 = 8.0;
      double d12 = 0.5;
      IntervalNoiseSetting intervalNoiseSetting9 = intervalNoiseSetting8.withInterval(d12, d11, d10, d9, b3);
      this.uNoise3DecelerationFactorPost = intervalNoiseSetting9.visibleWhen(condition);
      IntervalNoiseSetting intervalNoiseSetting10 = new IntervalNoiseSetting(uNoise3SettingContext.getModule(), "UNoise3DecelerationFactorPostHighspeed", doubleValue, false);
      String str15 = "Decelerationfactor Post at highspeed";
      IntervalNoiseSetting intervalNoiseSetting11 = intervalNoiseSetting10.named(str15);
      byte b4 = 2;
      double d13 = 2.0;
      double d14 = 2.0;
      double d15 = 8.0;
      double d16 = 0.5;
      IntervalNoiseSetting intervalNoiseSetting12 = intervalNoiseSetting11.withInterval(d16, d15, d14, d13, b4);
      this.uNoise3DecelerationFactorPostHighspeed = intervalNoiseSetting12.visibleWhen(condition);
      StringValue stringValue7 = new StringValue(uNoise3SettingContext.getModule(), "UNoise3highspeed", 10.0, 5.0, 20.0, 1, doubleValue);
      String str16 = "How many degrees per tick is high speed?";
      this.uNoise3highspeed = stringValue7.named(str16).visibleWhen(condition);
      IntervalNoiseSetting intervalNoiseSetting13 = new IntervalNoiseSetting(uNoise3SettingContext.getModule(), "UNoise3SpeedAtLargeRotation", doubleValue, true);
      String str17 = "Speed at large rotation";
      IntervalNoiseSetting intervalNoiseSetting14 = intervalNoiseSetting13.named(str17);
      byte b5 = 1;
      double d17 = 40.0;
      double d18 = 30.0;
      double d19 = 100.0;
      double d20 = 1.0;
      IntervalNoiseSetting intervalNoiseSetting15 = intervalNoiseSetting14.withInterval(d20, d19, d18, d17, b5);
      this.uNoise3SpeedAtLargeRotation = intervalNoiseSetting15.visibleWhen(condition);
      IntervalNoiseSetting intervalNoiseSetting16 = new IntervalNoiseSetting(uNoise3SettingContext.getModule(), "UNoise3SpeedAtInitialRotation", doubleValue, true);
      String str18 = "Speed at initial rotation";
      IntervalNoiseSetting intervalNoiseSetting17 = intervalNoiseSetting16.named(str18);
      byte b6 = 1;
      double d21 = 40.0;
      double d22 = 30.0;
      double d23 = 100.0;
      double d24 = 1.0;
      IntervalNoiseSetting intervalNoiseSetting18 = intervalNoiseSetting17.withInterval(d24, d23, d22, d21, b6);
      this.uNoise3SpeedAtInitialRotation = intervalNoiseSetting18.visibleWhen(condition);
      StringValue stringValue8 = new StringValue(uNoise3SettingContext.getModule(), "UNoise3LargeRotation", 30.0, 20.0, 180.0, 1, doubleValue);
      String str19 = "How many degrees difference do count as large rotation?";
      this.uNoise3LargeRotation = stringValue8.named(str19).visibleWhen(condition);
      this.uNoise3MouseSimulator = new UNoise3MouseSimulator(uNoise3SettingContext.withParentGroup(doubleValue));
      this.uNoise3Aimspeed.addDefaultLayer();
      this.uNoise3DecelerationFactorPre.addDefaultLayer();
      this.uNoise3DecelerationFactorPost.addDefaultLayer();
      this.uNoise3SpeedAtLargeRotation.addDefaultLayer();
      this.uNoise3SpeedAtInitialRotation.addDefaultLayer();
      this.uNoise3DecelerationFactorPostHighspeed.addDefaultLayer();
      StringValue stringValue9 = new StringValue(uNoise3SettingContext.getModule(), "UNoise3ResyncChanceAtAimgoal", 0.0, 0.0, 100.0, 0, doubleValue);
      String str20 = "Resync at aimgoal";
      this.uNoise3ResyncChanceAtAimgoal = stringValue9.named(str20).visibleWhen(condition);
      RandomizedRangeSettings randomizedRangeSettings11 = new RandomizedRangeSettings(uNoise3SettingContext.getModule(), "UNoise3ResyncUpdateDelay", 1.0, 1.0, 1.0, 5.0, 0, doubleValue);
      String str21 = "Resync recalculate delay";
      this.uNoise3ResyncUpdateDelay = (RandomizedRangeSettings)((RandomizedRangeSettings)randomizedRangeSettings11.withDisplayNamePrefix(str21)).withVisibility(condition);
      StringValue stringValue10 = new StringValue(uNoise3SettingContext.getModule(), "UNoise3RemoveNoiseChance", 0.0, 0.0, 100.0, 0, doubleValue);
      String str22 = "Apply no noise when near target chance";
      this.uNoise3RemoveNoiseChance = stringValue10.named(str22).visibleWhen(condition);
      RandomizedRangeSettings randomizedRangeSettings12 = new RandomizedRangeSettings(uNoise3SettingContext.getModule(), "UNoise3RecalcRemoveNoise", 1.0, 5.0, 1.0, 8.0, 0, doubleValue);
      String str23 = "No noise recalc delay";
      this.uNoise3RecalcRemoveNoise = (RandomizedRangeSettings)((RandomizedRangeSettings)randomizedRangeSettings12.withDisplayNamePrefix(str23)).withVisibility(condition);
      RandomizedRangeSettings randomizedRangeSettings13 = new RandomizedRangeSettings(uNoise3SettingContext.getModule(), "UNoise3resyncdelay", 0.0, 0.0, 0.0, 1000.0, 0, doubleValue);
      String str24 = "Resync delay";
      this.uNoise3resyncdelay = (RandomizedRangeSettings)((RandomizedRangeSettings)randomizedRangeSettings13.withDisplayNamePrefix(str24)).withoutNotInARow().withVisibility(condition);
      this.uNoise3MouseSimulator.onGoalReached = () -> this.onAimGoalReached();
      this.uNoise3MouseSimulator.onResyncTriggered = () -> this.scheduleResync();
      this.targetChangedSupplier = supplier;
   }

   public void resetAdvancedState(double d, double d2) {
      this.simulatedYaw = d;
      this.simulatedPitch = d2;
      this.actualYaw = d;
      this.actualPitch = d2;
      UNoise3MouseSimulator uNoise3MouseSimulator2 = this.uNoise3MouseSimulator;
      double d3 = this.uNoise3SpeedAtInitialRotation.sample() * 20.0;
      boolean flag = false;
      double d4 = d3;
      uNoise3MouseSimulator2.setAcceleration(d4, flag);
      this.uNoise3MouseSimulator.preDecelerationFactor = this.uNoise3DecelerationFactorPre.sample();
      this.uNoise3MouseSimulator.postDecelerationFactor = this.uNoise3DecelerationFactorPost.sample();
      this.uNoise3MouseSimulator.postHighSpeedDecelFactor = this.uNoise3DecelerationFactorPostHighspeed.sample();
      this.uNoise3MouseSimulator.highSpeedThresholdDeg = this.uNoise3highspeed.getValue();
      this.largeRotationSpeed = this.uNoise3SpeedAtLargeRotation.sample() * 20.0;
   }

   private boolean isAdvancedMode() {
      return this.rotationEngineMode.getSelectedValue().equals("Advanced");
   }

   public Rotation computeIndependentRotation(float f, float f2, float f3, float f4, float f5, float f6) {
      f = RotationUtil.updateAngle(f3, f, f5);
      f2 = RotationUtil.updateAngle(f4, f2, f6);
      return new Rotation(f, f2);
   }

   private static double[] sampleSinusoidalArcPoint(double d, double d2, double d3, double d4, double d5, double d6, double d7) {
      double d8 = d3 - d;
      double d9 = d4 - d2;
      double d10 = Math.sqrt(d8 * d8 + d9 * d9);
      d5 = Math.min(d5, d10);
      double d11 = d5 / d10;
      double d12 = d + d11 * d8;
      double d13 = d2 + d11 * d9;
      double d14 = -d9 / d10;
      double d15 = d8 / d10;
      double d16 = d6 * Math.sin(d7 * d11 * Math.PI);
      double d17 = d12 + d16 * d14;
      double d18 = d13 + d16 * d15;
      return new double[]{d17, d18};
   }

   public static boolean haveSameSign(double d, double d2) {
      return d > 0.0 && d2 > 0.0 || d < 0.0 && d2 < 0.0;
   }

   private void onAimGoalReached() {
      boolean shouldResyncAtGoal2 = this.shouldResyncAtGoal;
      if (shouldResyncAtGoal2) {
         this.scheduleResync();
      }

      UNoise3MouseSimulator uNoise3MouseSimulator2 = this.uNoise3MouseSimulator;
      double d = this.uNoise3Aimspeed.sample() * 20.0;
      boolean flag = false;
      double d2 = d;
      uNoise3MouseSimulator2.setAcceleration(d2, flag);
      this.uNoise3MouseSimulator.clearAccelerationCap();
      this.uNoise3MouseSimulator.preDecelerationFactor = this.uNoise3DecelerationFactorPre.sample();
      this.uNoise3MouseSimulator.postDecelerationFactor = this.uNoise3DecelerationFactorPost.sample();
      this.uNoise3MouseSimulator.postHighSpeedDecelFactor = this.uNoise3DecelerationFactorPostHighspeed.sample();
      this.uNoise3MouseSimulator.highSpeedThresholdDeg = this.uNoise3highspeed.getValue();
      this.largeRotationSpeed = this.uNoise3SpeedAtLargeRotation.sample() * 20.0;
   }

   private static double computeMaxLinearStep(float f, float f2, float f3, float f4, float f5, float f6) {
      float f7 = f - f3;
      float f8 = f2 - f4;
      float f9 = Math.min(Math.abs(f7), f5);
      float f10 = Math.min(Math.abs(f8), f6);
      float f11 = f4 + f10 * (float)(f8 > 0.0F ? 1 : -1);
      float f12 = f3 + f9 * (float)(f7 > 0.0F ? 1 : -1);
      double d = interpolateXAtY((double)f3, (double)f4, (double)f, (double)f2, (double)f11);
      double d2 = interpolateYAtX((double)f3, (double)f4, (double)f, (double)f2, (double)f12);
      double d3 = distance2D((double)f3, (double)f4, d, (double)f11);
      double d4 = distance2D((double)f3, (double)f4, (double)f12, d2);
      return Math.min(d3, d4);
   }

   private void updatePreviousTarget(float f, float f2, boolean flag) {
      if (flag || (Boolean)this.targetChangedSupplier.get() || !Float.isFinite(this.previousTargetYaw)) {
         this.previousTargetYaw = f;
         this.previousTargetPitch = f2;
      }
   }

   private Rotation applyRandomJitter(Rotation rotation, float f, float f2) {
      Rotation rotation2 = new Rotation(rotation);
      if ((double)Math.abs(rotation2.yaw - f) > 0.16 || (double)Math.abs(rotation2.pitch - f2) > 0.16 || !this.rotationEngineRandomizeOnlyWhileRotating.getValue()) {
         boolean flag = true;
         rotation2.yaw = rotation2.yaw + (float)this.rotationEngineRandomStrength.nextValue(flag) * (float)(ThreadLocalRandom.current().nextBoolean() ? 1 : -1);
         boolean flag2 = true;
         rotation2.pitch = rotation2.pitch + (float)this.rotationEngineRandomStrength.nextValue(flag2) * (float)(ThreadLocalRandom.current().nextBoolean() ? 1 : -1);
      }

      return rotation2;
   }

   public Rotation rotateTowards(float f, float f2, float f3, float f4) {
      f = RotationUtil.updateAngle(f3, f, 180.0F);
      f2 = RotationUtil.updateAngle(f4, f2, 180.0F);
      boolean flag = false;
      this.updatePreviousTarget(f, f2, flag);
      boolean isGuardActive = AntiSamplerGuard.isGuardActive(1);
      Rotation rotation = this.computeTurnSpeeds(f, f2, f3, f4, isGuardActive);
      Rotation rotation2 = null;
      String selectedValue = this.rotationEngineMode.getSelectedValue();
      if (isGuardActive) {
         selectedValue = "Linear";
      }

      switch (selectedValue) {
         case "Independent":
            float pitch = rotation.pitch;
            float yaw = rotation.yaw;
            rotation2 = this.computeIndependentRotation(f, f2, f3, f4, yaw, pitch);
            rotation2 = this.applyRandomJitter(rotation2, f, f2);
            break;
         case "Linear":
            float pitch2 = rotation.pitch;
            float yaw2 = rotation.yaw;
            rotation2 = this.computeLinearRotation(f, f2, f3, f4, yaw2, pitch2);
            rotation2 = this.applyRandomJitter(rotation2, f, f2);
            break;
         case "Curve":
            Rotation rotation3;
            if (isGuardActive) {
               float pitch3 = rotation.pitch;
               float yaw3 = rotation.yaw;
               rotation3 = this.computeLinearRotation(f, f2, f3, f4, yaw3, pitch3);
            } else {
               float yaw4 = rotation.yaw;
               float pitch4 = rotation.pitch;
               boolean flag2 = true;
               int value = (int)this.rotationEngineInterval.getValue(flag2);
               boolean flag3 = true;
               float value2 = (float)this.rotationEngineAmplitude.getValue(flag3);
               boolean flag4 = true;
               float value3 = (float)this.rotationEngineFrequency.getValue(flag4);
               float f5 = value2;
               int n = value;
               float f6 = pitch4;
               float f7 = yaw4;
               rotation3 = this.computeCurvedRotation(f, f2, f3, f4, f7, f6, n, f5, value3);
            }

            rotation2 = rotation3;
            break;
         case "Advanced":
            if (this.lastAdvancedTick + 1 != Minecraft.getMinecraft().thePlayer.ticksExisted) {
               double d = MathHelper.wrapAngleTo180_double((double)f3);
               double d2 = (double)f4;
               double d3 = d;
               this.resetAdvancedState(d3, d2);
            }

            if (EntityBoundsUtil.getShortestAngleDistance(MathHelper.wrapAngleTo180_double((double)f3), this.actualYaw) > 0.15) {
               this.actualYaw = this.simulatedYaw = MathHelper.wrapAngleTo180_double((double)f3);
            }

            if (Math.abs((double)f4 - this.actualPitch) > 0.15) {
               this.actualPitch = (double)f4;
               this.simulatedYaw = (double)f4;
            }

            if (this.resyncRecalcTicks == 0) {
               double d4 = Math.random();
               boolean flag5 = true;
               this.shouldResyncAtGoal = d4 <= this.uNoise3ResyncChanceAtAimgoal.getValue(flag5) / 100.0;
               boolean flag6 = true;
               this.resyncRecalcTicks = this.uNoise3ResyncUpdateDelay.nextInt(flag6);
            } else if (this.resyncRecalcTicks > 0) {
               this.resyncRecalcTicks--;
            }

            if (this.removeNoiseRecalcTicks == 0) {
               UNoise3MouseSimulator uNoise3MouseSimulator2 = this.uNoise3MouseSimulator;
               double d5 = Math.random();
               boolean flag7 = true;
               uNoise3MouseSimulator2.disableNoiseNearTarget = d5 <= this.uNoise3RemoveNoiseChance.getValue(flag7) / 100.0;
               boolean flag8 = true;
               this.removeNoiseRecalcTicks = this.uNoise3RecalcRemoveNoise.nextInt(flag8);
            } else if (this.removeNoiseRecalcTicks > 0) {
               this.removeNoiseRecalcTicks--;
            }

            double d6 = MathHelper.wrapAngleTo180_double((double)f);
            MutableDoubleRef mutableDoubleRef = new MutableDoubleRef(EntityBoundsUtil.getShortestSignedAngleDelta(this.simulatedYaw, d6));
            MutableDoubleRef mutableDoubleRef2 = new MutableDoubleRef((double)f2 - this.simulatedPitch);
            double d7 = 0.0;
            double d8 = Math.sqrt(mutableDoubleRef.getValue() * mutableDoubleRef.getValue() + mutableDoubleRef2.getValue() * mutableDoubleRef2.getValue());
            boolean flag9 = true;
            if (d8 > this.uNoise3LargeRotation.getValue(flag9)) {
               boolean flag10 = false;
               double largeRotationSpeed2 = this.largeRotationSpeed;
               this.uNoise3MouseSimulator.setAcceleration(largeRotationSpeed2, flag10);
            }

            UNoise3MouseSimulator uNoise3MouseSimulator3 = this.uNoise3MouseSimulator;
            double d9 = d7 / 0.15;
            float var3f = f2;
            MouseSimulationStepCallback callback = (n2, d10, d11) -> this.onMouseSimulationSubstep(mutableDoubleRef, d6, mutableDoubleRef2, var3f, n2, d10, d11);
            byte b = 3;
            double d12 = d9;
            MouseSimulationDelta mouseSimulationDelta = uNoise3MouseSimulator3.simulateTickSubsteps(mutableDoubleRef, mutableDoubleRef2, d12, b, callback);
            this.uNoise3Aimspeed.stepLayers();
            this.uNoise3SpeedAtLargeRotation.stepLayers();
            this.uNoise3SpeedAtInitialRotation.stepLayers();
            this.uNoise3DecelerationFactorPre.stepLayers();
            this.uNoise3DecelerationFactorPost.stepLayers();
            this.uNoise3DecelerationFactorPostHighspeed.stepLayers();
            double d13 = mouseSimulationDelta.noisyDelta.x * 0.15;
            double d14 = mouseSimulationDelta.noisyDelta.y * 0.15;
            this.simulatedYaw = MathHelper.wrapAngleTo180_double(this.simulatedYaw + mouseSimulationDelta.baseDelta.x * 0.15);
            this.simulatedPitch = MathHelper.clamp_double(this.simulatedPitch + mouseSimulationDelta.baseDelta.y * 0.15, -90.0, 90.0);
            this.actualYaw = MathHelper.wrapAngleTo180_double(this.actualYaw + d13);
            this.actualPitch = MathHelper.clamp_double(this.actualPitch + d14, -90.0, 90.0);
            rotation2 = new Rotation((float)this.actualYaw, (float)this.actualPitch);
      }

      boolean flag11 = true;
      this.updatePreviousTarget(f, f2, flag11);
      return rotation2 == null ? new Rotation(f3, f4) : RotationUtil.applyGcdFix(rotation2.yaw, rotation2.pitch, f3, f4);
   }

   private static double distance2D(double d, double d2, double d3, double d4) {
      return Math.sqrt(Math.pow(d3 - d, 2.0) + Math.pow(d4 - d2, 2.0));
   }

   public Rotation computeLinearRotation(float f, float f2, float f3, float f4, float f5, float f6) {
      f = RotationUtil.updateAngle(f3, f, 180.0F);
      f2 = RotationUtil.updateAngle(f4, f2, 180.0F);
      double d = computeMaxLinearStep(f, f2, f3, f4, f5, f6);
      double[] doubles = stepAlongLine((double)f3, (double)f4, (double)f, (double)f2, d);
      return new Rotation((float)doubles[0], (float)doubles[1]);
   }

   private void onMouseSimulationSubstep(MutableDoubleRef mutableDoubleRef, double d, MutableDoubleRef mutableDoubleRef2, float f, int n, double d2, double d3) {
      this.tickResyncTimer();
      this.processQueuedResync();
      if (this.resyncPending) {
         this.simulatedYaw = this.actualYaw;
         this.simulatedPitch = this.actualPitch;
         double d4 = EntityBoundsUtil.getShortestSignedAngleDelta(this.simulatedYaw, d);
         mutableDoubleRef.setValue(d4);
         double d5 = (double)f - this.simulatedPitch;
         mutableDoubleRef2.setValue(d5);
         this.resyncPending = false;
      }
   }

   private static double interpolateYAtX(double d, double d2, double d3, double d4, double d5) {
      double d6 = (d4 - d2) / (d3 - d);
      return d6 * (d5 - d) + d2;
   }

   public float computeAcceleratedSpeed(RandomizedRangeSettings randomizedRangeSettings, float f, float f2, float f3, float f4, float f5, boolean flag, boolean flag2) {
      float f6 = (float)randomizedRangeSettings.nextDouble() + (float)(flag2 ? 15 : 0);
      double d = randomizedRangeSettings.getMinValue() + (double)(flag2 ? 15 : 0);
      double d2 = d + (randomizedRangeSettings.getMaxValue() - randomizedRangeSettings.getMinValue()) / 2.0;
      float f7 = f - f2;
      double d3 = flag ? (double)Math.abs(f4 - f) : 0.0;
      double d4 = (double)Math.abs(f7 / Math.max(f3, 1.0F));
      double d5 = Math.abs(((double)f3 - d3) / d2);
      return MathHelper.clamp_float(Math.abs(f3) + (d4 > d5 ? f6 : -f6), 0.0F, f5);
   }

   private void processQueuedResync() {
      if (this.resyncDelayTicks == 0 && this.resyncQueued) {
         this.triggerResync();
      }
   }

   private static double[] stepAlongLine(double d, double d2, double d3, double d4, double d5) {
      double d6 = d3 - d;
      double d7 = d4 - d2;
      double d8 = Math.sqrt(d6 * d6 + d7 * d7);
      d5 = Math.min(d5, d8);
      double d9 = d5 / d8;
      double d10 = d + d9 * d6;
      double d11 = d2 + d9 * d7;
      return new double[]{d10, d11};
   }

   public RotationEngine(Module module, DoubleValue doubleValue) {
       this(module, doubleValue, () -> false);
   }

   private void triggerResync() {
      boolean flag = true;
      this.resyncDelayTicks = (int)((double)this.uNoise3resyncdelay.nextInt(flag) / 16.6);
      this.resyncQueued = false;
      this.resyncPending = true;
   }

   private boolean isAmplitudeVisible() {
      return this.rotationEngineMode.getSelectedValue().equals("Curve");
   }

   private static double interpolateXAtY(double d, double d2, double d3, double d4, double d5) {
      double d6 = (d4 - d2) / (d3 - d);
      return (d5 - d2) / d6 + d;
   }

   private Rotation computeTurnSpeeds(float f, float f2, float f3, float f4, boolean flag) {
      Rotation lastTickRotation = ClientManagers.rotationController.getLastTickRotation();
      Rotation rotation = ClientManagers.rotationController.getPreviousRotation().absDelta(lastTickRotation);
      RandomizedRangeSettings randomizedRangeSettings = this.rotationEngineYawAcceleration;
      float yaw = rotation.yaw;
      float previousTargetYaw2 = this.previousTargetYaw;
      float f5 = (float)this.rotationEngineYawSpeed.nextValue();
      boolean value = this.rotationEngineBetterAcceleration.getValue();
      boolean flag2 = value;
      float f6 = f5;
      float f7 = previousTargetYaw2;
      float f8 = yaw;
      RandomizedRangeSettings randomizedRangeSettings2 = randomizedRangeSettings;
      float f9 = this.computeAcceleratedSpeed(randomizedRangeSettings2, f, f3, f8, f7, f6, flag2, flag);
      RandomizedRangeSettings randomizedRangeSettings3 = this.rotationEnginePitchAcceleration;
      yaw = rotation.pitch;
      float previousTargetPitch2 = this.previousTargetPitch;
      f5 = (float)this.rotationEnginePitchSpeed.nextValue();
      value = this.rotationEngineBetterAcceleration.getValue();
      boolean flag3 = value;
      float f10 = f5;
      float f11 = previousTargetPitch2;
      float f12 = yaw;
      RandomizedRangeSettings randomizedRangeSettings4 = randomizedRangeSettings3;
      float f13 = this.computeAcceleratedSpeed(randomizedRangeSettings4, f2, f4, f12, f11, f10, flag3, flag);
      return new Rotation(f9, f13);
   }

   private void tickResyncTimer() {
      if (this.resyncDelayTicks > 0) {
         this.resyncDelayTicks--;
      }
   }





}
