package net.augustus.modules.modes.killaura.unoise3;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.RenderListener;
import net.augustus.render.RenderUtil;
import net.augustus.settings.IntervalNoiseSetting;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.AntiSamplerGuard;
import net.augustus.utils.ChatUtil;
import net.augustus.utils.math.MutableDoubleRef;
import net.augustus.utils.math.Vec2d;
import net.augustus.utils.rotation.DynamicAimController;
import net.augustus.utils.rotation.EntityBoundsUtil;
import net.augustus.utils.rotation.HitboxAngleUtil;
import net.augustus.utils.rotation.Rotation;
import java.util.ArrayList;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.interfaces.MC;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class UNoise3RotationEngine implements RenderListener, MC {
   private BooleanValue uNoise3DebugPrediction;
   private IntervalNoiseSetting uNoise3SpeedAtInitialRotation;
   private EntityLivingBase lastTarget;
   private Vec3 prevPredictedTargetPos;
   private RandomizedRangeSettings uNoise3resyncdelay;
   private StringValue uNoise3highspeed;
   private StringValue uNoise3RemoveNoiseChance;
   private IntervalNoiseSetting uNoise3DecelerationFactorPostHighspeed;
   private UNoise3SettingContext uNoise3SettingContext;
   public static ArrayList ENGINES = new ArrayList();
   private IntervalNoiseSetting uNoise3SpeedAtLargeRotation;
   private double actualPitch;
   private Vec3 predictedTargetPos;
   private Vec3 prevPredictedPlayerPos;
   private RandomizedRangeSettings uNoise3ResyncUpdateDelay;
   private boolean resyncPending;
   private BooleansSetting pointSelectionMode;
   private double baseYaw;
   private int resyncCooldownSubsteps;
   private DynamicAimController dynamicAimController;
   private StringValue uNoise3LargeRotation;
   private IntervalNoiseSetting uNoise3DecelerationFactorPre;
   private RandomizedRangeSettings uNoise3RecalcRemoveNoise;
   private Vec3 predictedPlayerPos;
   private Vec2d cachedAimGoal;
   private StringValue uNoise3ResyncChanceAtAimgoal;
   private IntervalNoiseSetting uNoise3Aimspeed;
   private int noNoiseRecalcTicks;
   private UNoise3MouseSimulator uNoise3MouseSimulator;
   private IntervalNoiseSetting uNoise3DecelerationFactorPost;
   private double basePitch;
   private UNoise3PointSelector uNoise3PointSelector;
   private double largeRotationSpeed;
   public UNoise3PredictionScheduler uNoise3PredictionScheduler;
   private boolean resyncQueued;
   private boolean resyncAtGoalEnabled;
   public boolean suppressTrailLogging;
   private int lastUpdateTick;
   private int remainingLookaheadTicks;
   private int resyncChanceRecalcTicks;
   private double actualYaw;

   private Vec2d resolveAimGoal(
      EntityLivingBase entity, EntityPlayerSP player, float f, float f2, double d, double d2, UNoise3PredictionEntry[] uNoise3PredictionEntrys, UNoise3PredictionEntry[] uNoise3PredictionEntrys2
   ) {
      int[] controlFlowState = UNoise3SettingContext.getControlFlowState();
      if (this.cachedAimGoal != null && this.uNoise3PredictionScheduler.lookaheadEndTick >= player.ticksExisted && this.remainingLookaheadTicks > 0) {
         this.remainingLookaheadTicks--;
         return this.cachedAimGoal;
      } else {
         Vec2d vec2d;
         if (this.pointSelectionMode.getSelectedValue().equals("Prediction")) {
            double d3 = (uNoise3PredictionEntrys[0].angularVelocity + uNoise3PredictionEntrys[1].angularVelocity) / 2.0;
            double d4 = (uNoise3PredictionEntrys2[0].angularVelocity + uNoise3PredictionEntrys2[1].angularVelocity) / 2.0;
            UNoise3PointSelector uNoise3PointSelector2 = this.uNoise3PointSelector;
            UNoise3PredictionScheduler scheduler = this.uNoise3PredictionScheduler;
            Vec3 predictedPlayerPos2 = uNoise3PredictionEntrys[0].predictedPlayerPos;
            Vec3 predictedTargetPos2 = uNoise3PredictionEntrys[0].predictedTargetPos;
            Vec3 predictedPlayerPos3 = uNoise3PredictionEntrys2[0].predictedPlayerPos;
            Vec3 predictedTargetPos3 = uNoise3PredictionEntrys2[0].predictedTargetPos;
            double d5 = (double)f;
            double d6 = (double)f2;
            byte b = 4;
            double d7 = this.uNoise3PredictionScheduler.uNoise3MovementFilter.predictMinDistance(player, entity, b);
            double d8 = d6;
            double d9 = d5;
            Vec3 vec = predictedTargetPos3;
            Vec3 vec2 = predictedPlayerPos3;
            Vec3 vec3 = predictedTargetPos2;
            Vec3 vec4 = predictedPlayerPos2;
            UNoise3PredictionScheduler scheduler2 = scheduler;
            vec2d = uNoise3PointSelector2.selectTargetAngles(scheduler2, player, entity, vec4, vec3, vec2, vec, d9, d8, d, d2, d7, d3, d4);
         } else {
            vec2d = new Vec2d(d, d2);
            this.uNoise3PointSelector.resetState(d, d2);
         }

         this.cachedAimGoal = vec2d;
         this.remainingLookaheadTicks = this.uNoise3PredictionScheduler.lookaheadTicks;
         this.uNoise3MouseSimulator.clearAccelerationCap();
         return vec2d;
      }
   }

   private boolean isPointSelectionVisible() {
      return this.pointSelectionMode.getSelectedValue().equals("Prediction");
   }

   private void processQueuedResync() {
      int[] controlFlowState = UNoise3SettingContext.getControlFlowState();
      if (this.resyncCooldownSubsteps == 0 && this.resyncQueued) {
         this.triggerResync();
      }
   }

   private void requestResync() {
      int[] controlFlowState = UNoise3SettingContext.getControlFlowState();
      if (this.resyncCooldownSubsteps > 0) {
         this.resyncQueued = true;
      } else {
         this.triggerResync();
      }
   }

   private boolean isPredictionVisible() {
      return this.pointSelectionMode.getSelectedValue().equals("Prediction");
   }

   private void triggerResync() {
      boolean flag = true;
      this.resyncCooldownSubsteps = (int)((double)this.uNoise3resyncdelay.nextInt(flag) / 16.6);
      this.resyncQueued = false;
      this.resyncPending = true;
   }

   public UNoise3RotationEngine(UNoise3SettingContext uNoise3SettingContext2, SettingVisibilityCondition condition) {
       super();
      UNoise3SettingContext.getControlFlowState();
      this.resyncPending = false;
      this.lastUpdateTick = -1;
      this.lastTarget = null;
      this.resyncAtGoalEnabled = true;
      this.resyncChanceRecalcTicks = 0;
      this.noNoiseRecalcTicks = 0;
      this.suppressTrailLogging = false;
      this.cachedAimGoal = null;
      this.remainingLookaheadTicks = 0;
      if (AntiSamplerGuard.isGuardActive(1)) {
         this.dynamicAimController = new DynamicAimController();
      }

      this.uNoise3SettingContext = uNoise3SettingContext2;
      DoubleValue doubleValue = (new DoubleValue(uNoise3SettingContext2.getModule(), "Mouse", uNoise3SettingContext2.getParentGroup())).visibleWhen(condition);
      this.pointSelectionMode = (BooleansSetting)(new BooleansSetting(
            uNoise3SettingContext2.getModule(), "Point selection mode", "Prediction", new String[]{"Provided", "Prediction"}, uNoise3SettingContext2.getParentGroup()
         ))
         .withVisibility(condition);
      DoubleValue doubleValue2 = new DoubleValue(uNoise3SettingContext2.getModule(), "Point selection", uNoise3SettingContext2.getParentGroup());
      SettingVisibilityCondition condition2 = () -> this.isPointSelectionVisible();
      DoubleValue doubleValue3 = doubleValue2.visibleWhen(condition2).visibleWhen(condition);
      doubleValue2 = new DoubleValue(uNoise3SettingContext2.getModule(), "Prediction", uNoise3SettingContext2.getParentGroup());
      SettingVisibilityCondition condition3 = () -> this.isPredictionVisible();
      DoubleValue doubleValue4 = doubleValue2.visibleWhen(condition3).visibleWhen(condition);
      this.uNoise3PredictionScheduler = new UNoise3PredictionScheduler(uNoise3SettingContext2.withParentGroup(doubleValue4));
      this.uNoise3PointSelector = new UNoise3PointSelector(uNoise3SettingContext2.withParentGroup(doubleValue3), this.uNoise3PredictionScheduler);
      IntervalNoiseSetting intervalNoiseSetting = new IntervalNoiseSetting(uNoise3SettingContext2.getModule(), "UNoise3Aimspeed", doubleValue, true);
      String str = "Aim speed";
      IntervalNoiseSetting intervalNoiseSetting2 = intervalNoiseSetting.named(str);
      byte b = 1;
      double d = 20.0;
      double d2 = 15.0;
      double d3 = 100.0;
      double d4 = 1.0;
      this.uNoise3Aimspeed = intervalNoiseSetting2.withInterval(d4, d3, d2, d, b);
      IntervalNoiseSetting intervalNoiseSetting3 = new IntervalNoiseSetting(uNoise3SettingContext2.getModule(), "UNoise3DecelerationFactorPre", doubleValue, false);
      String str2 = "Decelerationfactor Pre";
      IntervalNoiseSetting intervalNoiseSetting4 = intervalNoiseSetting3.named(str2);
      byte b2 = 2;
      double d5 = 1.0;
      double d6 = 1.0;
      double d7 = 2.0;
      double d8 = 0.5;
      this.uNoise3DecelerationFactorPre = intervalNoiseSetting4.withInterval(d8, d7, d6, d5, b2);
      IntervalNoiseSetting intervalNoiseSetting5 = new IntervalNoiseSetting(uNoise3SettingContext2.getModule(), "UNoise3DecelerationFactorPost", doubleValue, false);
      String str3 = "Decelerationfactor Post";
      IntervalNoiseSetting intervalNoiseSetting6 = intervalNoiseSetting5.named(str3);
      byte b3 = 2;
      double d9 = 1.0;
      double d10 = 1.0;
      double d11 = 8.0;
      double d12 = 0.5;
      this.uNoise3DecelerationFactorPost = intervalNoiseSetting6.withInterval(d12, d11, d10, d9, b3);
      IntervalNoiseSetting intervalNoiseSetting7 = new IntervalNoiseSetting(uNoise3SettingContext2.getModule(), "UNoise3DecelerationFactorPostHighspeed", doubleValue, false);
      String str4 = "Decelerationfactor Post at highspeed";
      IntervalNoiseSetting intervalNoiseSetting8 = intervalNoiseSetting7.named(str4);
      byte b4 = 2;
      double d13 = 2.0;
      double d14 = 2.0;
      double d15 = 8.0;
      double d16 = 0.5;
      this.uNoise3DecelerationFactorPostHighspeed = intervalNoiseSetting8.withInterval(d16, d15, d14, d13, b4);
      StringValue stringValue = new StringValue(uNoise3SettingContext2.getModule(), "UNoise3highspeed", 10.0, 5.0, 20.0, 1, doubleValue);
      String str5 = "How many degrees per tick is high speed?";
      this.uNoise3highspeed = stringValue.named(str5);
      IntervalNoiseSetting intervalNoiseSetting9 = new IntervalNoiseSetting(uNoise3SettingContext2.getModule(), "UNoise3SpeedAtLargeRotation", doubleValue, true);
      String str6 = "Speed at large rotation";
      IntervalNoiseSetting intervalNoiseSetting10 = intervalNoiseSetting9.named(str6);
      byte b5 = 1;
      double d17 = 40.0;
      double d18 = 30.0;
      double d19 = 100.0;
      double d20 = 1.0;
      this.uNoise3SpeedAtLargeRotation = intervalNoiseSetting10.withInterval(d20, d19, d18, d17, b5);
      IntervalNoiseSetting intervalNoiseSetting11 = new IntervalNoiseSetting(uNoise3SettingContext2.getModule(), "UNoise3SpeedAtInitialRotation", doubleValue, true);
      String str7 = "Speed at initial rotation";
      IntervalNoiseSetting intervalNoiseSetting12 = intervalNoiseSetting11.named(str7);
      byte b6 = 1;
      double d21 = 40.0;
      double d22 = 30.0;
      double d23 = 100.0;
      double d24 = 1.0;
      this.uNoise3SpeedAtInitialRotation = intervalNoiseSetting12.withInterval(d24, d23, d22, d21, b6);
      StringValue stringValue2 = new StringValue(uNoise3SettingContext2.getModule(), "UNoise3LargeRotation", 30.0, 1.0, 180.0, 1, doubleValue);
      String str8 = "How many degrees difference do count as large rotation?";
      this.uNoise3LargeRotation = stringValue2.named(str8);
      this.uNoise3MouseSimulator = new UNoise3MouseSimulator(uNoise3SettingContext2.withParentGroup(doubleValue));
      this.uNoise3Aimspeed.addDefaultLayer();
      this.uNoise3DecelerationFactorPre.addDefaultLayer();
      this.uNoise3DecelerationFactorPost.addDefaultLayer();
      this.uNoise3SpeedAtLargeRotation.addDefaultLayer();
      this.uNoise3SpeedAtInitialRotation.addDefaultLayer();
      this.uNoise3DecelerationFactorPostHighspeed.addDefaultLayer();
      StringValue stringValue3 = new StringValue(uNoise3SettingContext2.getModule(), "UNoise3ResyncChanceAtAimgoal", 0.0, 0.0, 100.0, 0, doubleValue);
      String str9 = "Resync at aimgoal";
      this.uNoise3ResyncChanceAtAimgoal = stringValue3.named(str9);
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(uNoise3SettingContext2.getModule(), "UNoise3ResyncUpdateDelay", 1.0, 1.0, 1.0, 5.0, 0, doubleValue);
      String str10 = "Resync recalculate delay";
      this.uNoise3ResyncUpdateDelay = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str10);
      StringValue stringValue4 = new StringValue(uNoise3SettingContext2.getModule(), "UNoise3RemoveNoiseChance", 0.0, 0.0, 100.0, 0, doubleValue);
      String str11 = "Apply no noise when near target chance";
      this.uNoise3RemoveNoiseChance = stringValue4.named(str11);
      RandomizedRangeSettings randomizedRangeSettings2 = new RandomizedRangeSettings(uNoise3SettingContext2.getModule(), "UNoise3RecalcRemoveNoise", 1.0, 5.0, 1.0, 8.0, 0, doubleValue);
      String str12 = "No noise recalc delay";
      this.uNoise3RecalcRemoveNoise = (RandomizedRangeSettings)randomizedRangeSettings2.withDisplayNamePrefix(str12);
      RandomizedRangeSettings randomizedRangeSettings3 = new RandomizedRangeSettings(uNoise3SettingContext2.getModule(), "UNoise3resyncdelay", 0.0, 0.0, 0.0, 1000.0, 0, doubleValue);
      String str13 = "Resync delay";
      this.uNoise3resyncdelay = ((RandomizedRangeSettings)randomizedRangeSettings3.withDisplayNamePrefix(str13)).withoutNotInARow();
      BooleanValue booleanValue = new BooleanValue(uNoise3SettingContext2.getModule(), "UNoise3DebugPrediction", false, doubleValue4);
      String str14 = "Debug prediction";
      this.uNoise3DebugPrediction = (BooleanValue)booleanValue.withDisplayName(str14);
      this.uNoise3MouseSimulator.onGoalReached = () -> {
         int[] controlFlowState = UNoise3SettingContext.getControlFlowState();
         boolean flag = true;
         if (!this.resyncAtGoalEnabled) {
            flag = false;
         }

         if (flag) {
            this.requestResync();
         }

         UNoise3MouseSimulator uNoise3MouseSimulator2 = this.uNoise3MouseSimulator;
         double d25 = this.uNoise3Aimspeed.sample() * 20.0;
         boolean flag2 = false;
         double d26 = d25;
         uNoise3MouseSimulator2.setAcceleration(d26, flag2);
         this.uNoise3MouseSimulator.clearAccelerationCap();
         this.uNoise3MouseSimulator.preDecelerationFactor = this.uNoise3DecelerationFactorPre.sample();
         this.uNoise3MouseSimulator.postDecelerationFactor = this.uNoise3DecelerationFactorPost.sample();
         this.uNoise3MouseSimulator.postHighSpeedDecelFactor = this.uNoise3DecelerationFactorPostHighspeed.sample();
         this.uNoise3MouseSimulator.highSpeedThresholdDeg = this.uNoise3highspeed.getValue();
         this.largeRotationSpeed = this.uNoise3SpeedAtLargeRotation.sample() * 20.0;
         if (this.remainingLookaheadTicks > 0) {
            this.uNoise3PredictionScheduler.forceRecompute = true;
         }

         this.remainingLookaheadTicks = 0;
         this.cachedAimGoal = null;
      };
      this.uNoise3MouseSimulator.onResyncTriggered = () -> this.requestResync();
      ENGINES.add(this);
   }

   public static void syncAllEngines(double d, double d2) {
      int[] controlFlowState = UNoise3SettingContext.getControlFlowState();

      for (UNoise3RotationEngine uNoise3RotationEngine : (Iterable<UNoise3RotationEngine>)(ENGINES)) {
         uNoise3RotationEngine.syncAngles(d, d2);
      }
   }

   public Rotation computeRotation(EntityLivingBase entity, EntityPlayerSP player, float f, float f2, double d, double d2, Vec3 vec, Vec3 vec2) {
      int[] controlFlowState = UNoise3SettingContext.getControlFlowState();
      if (AntiSamplerGuard.isGuardActive(6)) {
         return new Rotation(f, f2);
      } else {
         if (this.dynamicAimController == null
            && (
               AntiSamplerGuard.isGuardActive(1)
                  || Augustus.getInstance().getUsername().equals("Tom")
                  || Augustus.getInstance().getUsername().equals("karuz")
            )) {
            this.dynamicAimController = new DynamicAimController();
         }

         if (this.dynamicAimController != null) {
            this.lastTarget = entity;
            this.dynamicAimController.minReactionTime = 2;
            this.dynamicAimController.maxReactionTime = 3;
            this.dynamicAimController.attentionWindow = 3;
            this.dynamicAimController.superCorrectionDelay = 0;
            this.dynamicAimController.superCorrectionProbability = 0.0;
            this.dynamicAimController.additionalInaccuracy = 0.0;
            this.dynamicAimController.aimSpeed = 6.0;
            this.dynamicAimController.preDecelerationFactor = 1.0;
            this.dynamicAimController.postDecelerationFactor = 1.0;
            this.dynamicAimController.additionalAccuracy = false;
            this.dynamicAimController.applyRandomJitter = false;
            return this.dynamicAimController.computeRotation(entity, player, f, f2, d, d2, vec, vec2);
         } else {
            f = MathHelper.wrapAngleTo180_float(f);
            this.uNoise3PredictionScheduler.uNoise3MovementFilter.player = player;
            this.uNoise3PointSelector.player = player;
            boolean flag = HitboxAngleUtil.getShortestAngularDistance((double)f, this.actualYaw) > 1.0 || Math.abs(this.actualPitch - (double)f2) > 1.0;
            if (this.lastTarget != entity || this.lastUpdateTick + 1 != player.ticksExisted && (!mc.ranMultipleTicks || ClientManagers.moduleManager.rotationHandler.rotationMode.getSelectedValue().equals("Tick")) || flag) {
               double d3 = (double)f;
               double d4 = (double)f2;
               double d5 = d3;
               this.initializeForNewTarget(d5, d4);
               double d6 = (double)f;
               double d7 = (double)f2;
               double d8 = d6;
               this.uNoise3PointSelector.resetState(d8, d7);
               int ticksExisted = player.ticksExisted;
               this.uNoise3PredictionScheduler.resetSnapshotTick(ticksExisted);
               double d9 = MathHelper.wrapAngleTo180_double((double)player.prevReportedYaw);
               double d10 = MathHelper.wrapAngleTo180_double((double)player.lastReportedYaw);
               double lastReportedPitch = (double)player.lastReportedPitch;
               double prevReportedPitch = (double)player.prevReportedPitch;
               this.uNoise3MouseSimulator.currentVelocity = new Vec2d(EntityBoundsUtil.getShortestSignedAngleDelta(d9, d10) / 0.15 / 3.0, (lastReportedPitch - prevReportedPitch) / 0.15 / 3.0);
               this.uNoise3MouseSimulator.resyncStepThreshold = -1;
            }

            this.lastTarget = entity;
            this.lastUpdateTick = player.ticksExisted;
            if (!Double.isFinite(this.actualYaw)) {
               this.actualYaw = (double)f;
               ChatUtil.sendChat3("Fixed flaw (1)");
            }

            if (!Double.isFinite(this.actualPitch)) {
               this.actualPitch = (double)f2;
               ChatUtil.sendChat3("Fixed flaw (2)");
            }

            if (!Double.isFinite(this.baseYaw)) {
               this.baseYaw = this.actualYaw;
               ChatUtil.sendChat3("Fixed flaw (3)");
            }

            if (!Double.isFinite(this.basePitch)) {
               this.basePitch = this.actualPitch;
               ChatUtil.sendChat3("Fixed flaw (4)");
            }

            if (this.resyncChanceRecalcTicks == 0) {
               double d11 = Math.random();
               boolean flag2 = true;
               this.resyncAtGoalEnabled = d11 <= this.uNoise3ResyncChanceAtAimgoal.getValue(flag2) / 100.0;
               boolean flag3 = true;
               this.resyncChanceRecalcTicks = this.uNoise3ResyncUpdateDelay.nextInt(flag3);
            } else if (this.resyncChanceRecalcTicks > 0) {
               this.resyncChanceRecalcTicks--;
            }

            if (this.noNoiseRecalcTicks == 0) {
               UNoise3MouseSimulator uNoise3MouseSimulator2 = this.uNoise3MouseSimulator;
               double d12 = Math.random();
               boolean flag4 = true;
               uNoise3MouseSimulator2.disableNoiseNearTarget = d12 <= this.uNoise3RemoveNoiseChance.getValue(flag4) / 100.0;
               boolean flag5 = true;
               this.noNoiseRecalcTicks = this.uNoise3RecalcRemoveNoise.nextInt(flag5);
            } else if (this.noNoiseRecalcTicks > 0) {
               this.noNoiseRecalcTicks--;
            }

            UNoise3PredictionEntry[] uNoise3PredictionEntrys = null;
            UNoise3PredictionEntry[] uNoise3PredictionEntrys2 = null;
            Vec3 interpolatedPos = player.getInterpolatedPos(1.0F);
            Vec3 positionEyes = player.getPositionEyes(1.0F);
            Vec3 interpolatedPos2 = entity.getInterpolatedPos(1.0F);
            this.uNoise3PredictionScheduler.updatePredictions(player, entity);
            if (this.uNoise3PredictionScheduler.yawPredictions == null || !this.uNoise3PredictionScheduler.yawPredictions[0].tooClose && !this.uNoise3PredictionScheduler.yawPredictions[0].filtered) {
               uNoise3PredictionEntrys = this.uNoise3PredictionScheduler.yawPredictions;
               uNoise3PredictionEntrys2 = this.uNoise3PredictionScheduler.pitchPredictions;
            }

            if (uNoise3PredictionEntrys == null) {
               uNoise3PredictionEntrys = new UNoise3PredictionEntry[]{
                  new UNoise3PredictionEntry(interpolatedPos, interpolatedPos2, 0.0, 0.0, this.uNoise3PointSelector.lastOutputYaw, this.uNoise3PointSelector.lastOutputYaw, 1),
                  new UNoise3PredictionEntry(interpolatedPos, interpolatedPos2, 0.0, 0.0, this.uNoise3PointSelector.lastOutputYaw, this.uNoise3PointSelector.lastOutputYaw, 1)
               };
            }

            if (uNoise3PredictionEntrys2 == null) {
               uNoise3PredictionEntrys2 = new UNoise3PredictionEntry[]{
                  new UNoise3PredictionEntry(interpolatedPos, interpolatedPos2, 0.0, 0.0, this.uNoise3PointSelector.lastOutputPitch, this.uNoise3PointSelector.lastOutputPitch, 1),
                  new UNoise3PredictionEntry(interpolatedPos, interpolatedPos2, 0.0, 0.0, this.uNoise3PointSelector.lastOutputPitch, this.uNoise3PointSelector.lastOutputPitch, 1)
               };
            }

            this.prevPredictedPlayerPos = this.predictedPlayerPos;
            this.prevPredictedTargetPos = this.predictedTargetPos;
            this.predictedTargetPos = uNoise3PredictionEntrys2[0].predictedTargetPos;
            this.predictedPlayerPos = uNoise3PredictionEntrys2[0].predictedPlayerPos;
            Vec2d vec2d = this.resolveAimGoal(entity, player, f, f2, d, d2, uNoise3PredictionEntrys, uNoise3PredictionEntrys2);
            MutableDoubleRef mutableDoubleRef = new MutableDoubleRef(EntityBoundsUtil.getShortestSignedAngleDelta(this.baseYaw, MathHelper.wrapAngleTo180_double(vec2d.x)));
            MutableDoubleRef mutableDoubleRef2 = new MutableDoubleRef(vec2d.y - this.basePitch);
            double d13 = (uNoise3PredictionEntrys[0].angularVelocity + uNoise3PredictionEntrys[1].angularVelocity) / 2.0;
            double d14 = Math.sqrt(mutableDoubleRef.getValue() * mutableDoubleRef.getValue() + mutableDoubleRef2.getValue() * mutableDoubleRef2.getValue());
            boolean flag6 = true;
            if (d14 > this.uNoise3LargeRotation.getValue(flag6)) {
               boolean flag7 = false;
               double largeRotationSpeed2 = this.largeRotationSpeed;
               this.uNoise3MouseSimulator.setAcceleration(largeRotationSpeed2, flag7);
            }

            if (!this.uNoise3MouseSimulator.isAccelerationCapped() && this.uNoise3PredictionScheduler.lookaheadTicks > 0 && this.remainingLookaheadTicks >= 0 && this.pointSelectionMode.getSelectedValue().equals("Prediction")) {
               double effectiveAcceleration = this.uNoise3MouseSimulator.getEffectiveAcceleration();
               double value = mutableDoubleRef.getValue();
               double d15;
               if (Math.signum(d13) != Math.signum(value) && value != 0.0) {
                  d15 = 0.0;
               } else {
                  d15 = Math.abs(d13);
               }

               MouseTrajectoryConfig mouseTrajectoryConfig = new MouseTrajectoryConfig(mutableDoubleRef.getValue() / 0.15, this.uNoise3MouseSimulator.currentVelocity.x, d15 / 0.15, effectiveAcceleration, effectiveAcceleration, effectiveAcceleration);
               MouseTrajectoryConfig mouseTrajectoryConfig2 = new MouseTrajectoryConfig(mutableDoubleRef2.getValue() / 0.15, this.uNoise3MouseSimulator.currentVelocity.y, 0.0, effectiveAcceleration, effectiveAcceleration, effectiveAcceleration);
               int n = this.remainingLookaheadTicks * 3 + 3;
               double maxAcceleration = this.uNoise3MouseSimulator.solveAccelerationForSteps(n, mouseTrajectoryConfig).maxAcceleration;
               int n2 = this.remainingLookaheadTicks * 3 + 3;
               double maxAcceleration2 = this.uNoise3MouseSimulator.solveAccelerationForSteps(n2, mouseTrajectoryConfig2).maxAcceleration;
               UNoise3MouseSimulator uNoise3MouseSimulator3 = this.uNoise3MouseSimulator;
               double d16 = Math.max(maxAcceleration, maxAcceleration2);
               boolean flag8 = true;
               double d17 = d16;
               uNoise3MouseSimulator3.setAcceleration(d17, flag8);
            }

            UNoise3MouseSimulator uNoise3MouseSimulator4 = this.uNoise3MouseSimulator;
            double d18 = d13 / 0.15;
            MouseSimulationStepCallback callback = (n3, d19, d20) -> {
               this.tickResyncCooldown();
               UNoise3SettingContext.getControlFlowState();
               this.processQueuedResync();
               if (!this.suppressTrailLogging) {
                  ClientManagers.moduleManager.line.rotationLineTimestamps.add((long)((double)System.currentTimeMillis() - (1.0 - (double)(n3 + 1) / 3.0) * 50.0));
                  ClientManagers.moduleManager.line.rotationLineEyePositions.add(mc.thePlayer.getPositionEyes((float)(n3 + 1) / 3.0F));
                  ClientManagers.moduleManager
                     .line
                     .rotationLineAngles
                     .add(
                        new float[]{
                           (float)MathHelper.wrapAngleTo180_double(this.actualYaw + d19 * 0.15),
                           MathHelper.clamp_float((float)(this.actualPitch + d20 * 0.15), -90.0F, 90.0F)
                        }
                     );

                  while (
                     !ClientManagers.moduleManager.line.rotationLineTimestamps.isEmpty()
                        && (double)(System.currentTimeMillis() - (Long)ClientManagers.moduleManager.line.rotationLineTimestamps.get(0)) > ClientManagers.moduleManager.line.auraLineTime.getValue()
                  ) {
                     ClientManagers.moduleManager.line.rotationLineTimestamps.remove(0);
                     ClientManagers.moduleManager.line.rotationLineAngles.remove(0);
                     ClientManagers.moduleManager.line.rotationLineEyePositions.remove(0);
                  }
               }

               if (this.resyncPending) {
                  this.baseYaw = this.actualYaw;
                  this.basePitch = this.actualPitch;
                  double d21 = EntityBoundsUtil.getShortestSignedAngleDelta(this.baseYaw, MathHelper.wrapAngleTo180_double(vec2d.x));
                  mutableDoubleRef.setValue(d21);
                  double d22 = vec2d.y - this.basePitch;
                  mutableDoubleRef2.setValue(d22);
                  this.resyncPending = false;
               }
            };
            byte b = 3;
            double d23 = d18;
            MouseSimulationDelta mouseSimulationDelta = uNoise3MouseSimulator4.simulateTickSubsteps(mutableDoubleRef, mutableDoubleRef2, d23, b, callback);
            Augustus.getInstance().getClass();
            this.uNoise3Aimspeed.stepLayers();
            this.uNoise3SpeedAtLargeRotation.stepLayers();
            this.uNoise3SpeedAtInitialRotation.stepLayers();
            this.uNoise3DecelerationFactorPre.stepLayers();
            this.uNoise3DecelerationFactorPost.stepLayers();
            this.uNoise3DecelerationFactorPostHighspeed.stepLayers();
            double d24 = mouseSimulationDelta.noisyDelta.x * 0.15;
            double d25 = mouseSimulationDelta.noisyDelta.y * 0.15;
            this.baseYaw = MathHelper.wrapAngleTo180_double(this.baseYaw + mouseSimulationDelta.baseDelta.x * 0.15);
            this.basePitch = MathHelper.clamp_double(this.basePitch + mouseSimulationDelta.baseDelta.y * 0.15, -90.0, 90.0);
            this.actualYaw = MathHelper.wrapAngleTo180_double(this.actualYaw + d24);
            this.actualPitch = MathHelper.clamp_double(this.actualPitch + d25, -90.0, 90.0);
            return new Rotation((float)this.actualYaw, (float)this.actualPitch);
         }
      }
   }

   public void initializeForNewTarget(double d, double d2) {
      this.baseYaw = d;
      this.basePitch = d2;
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
      this.cachedAimGoal = null;
   }

   @Override
   public void onRender(float f) {
      int[] controlFlowState = UNoise3SettingContext.getControlFlowState();
      if (this.uNoise3DebugPrediction.getValue()) {
         Vec3 vec = null;
         Vec3 vec2 = null;
         if (this.lastTarget != null && AntiSamplerGuard.isGuardActive(1)) {
            vec = this.lastTarget.getInterpolatedPos(f);
            vec2 = mc.thePlayer.getInterpolatedPos(f);
         } else if (this.prevPredictedTargetPos != null && this.prevPredictedPlayerPos != null && this.predictedTargetPos != null && this.lastTarget != null && this.predictedPlayerPos != null && mc.thePlayer != null) {
            vec = this.prevPredictedTargetPos.lerp(this.predictedTargetPos, (double)f);
            vec2 = this.prevPredictedPlayerPos.lerp(this.predictedPlayerPos, (double)f);
         }

         if (vec != null) {
            RenderUtil.begin3DRender();
            GL11.glPushMatrix();
            GL11.glLineWidth(3.0F);
            GL11.glTranslated(-mc.getRenderManager().getRenderPosX(), -mc.getRenderManager().getRenderPosY(), -mc.getRenderManager().getRenderPosZ());
            GlStateManager.color(1.0F, 0.0F, 0.0F, 1.0F);
            boolean flag = true;
            RenderUtil.drawOutlinedBoundingBox(EntityBoundsUtil.getEntityBoundingBoxAt(this.lastTarget, vec, flag));
            GlStateManager.color(0.0F, 0.0F, 1.0F, 1.0F);
            boolean flag2 = true;
            RenderUtil.drawOutlinedBoundingBox(EntityBoundsUtil.getEntityBoundingBoxAt(mc.thePlayer, vec2, flag2));
            GL11.glPopMatrix();
            RenderUtil.end3DRender();
         }
      }
   }

   public int estimateStepsToTarget(double d, double d2, double d3, double d4) {
      UNoise3MouseSimulator uNoise3MouseSimulator2 = this.uNoise3MouseSimulator;
      MouseTrajectoryConfig mouseTrajectoryConfig = new MouseTrajectoryConfig(d, d2, d3, d4, d4, 6.0);
      return uNoise3MouseSimulator2.simulateTrajectory(mouseTrajectoryConfig).size();
   }

   private void tickResyncCooldown() {
      int[] controlFlowState = UNoise3SettingContext.getControlFlowState();
      if (this.resyncCooldownSubsteps > 0) {
         this.resyncCooldownSubsteps--;
      }
   }

   public void syncAngles(double d, double d2) {
      d = MathHelper.wrapAngleTo180_double(d);
      d2 = MathHelper.clamp_double(d2, -90.0, 90.0);
      this.uNoise3PointSelector.resetState(d, d2);
      this.baseYaw = d;
      this.basePitch = d2;
      this.actualYaw = d;
      this.actualPitch = d2;
   }

}
