package net.augustus.modules.modes.stealer;

import net.augustus.modules.modes.killaura.unoise3.MouseSimulationDelta;
import net.augustus.modules.modes.killaura.unoise3.MouseSimulationStepCallback;
import net.augustus.modules.modes.killaura.unoise3.UNoise3MouseSimulator;
import net.augustus.modules.modes.killaura.unoise3.UNoise3SettingContext;
import net.augustus.settings.IntervalNoiseSetting;
import net.augustus.settings.NoiseLayerBuilder;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.math.MutableDoubleRef;
import net.augustus.utils.math.Vec2d;
import net.augustus.utils.player.ContainerUtil;
import java.util.ArrayList;
import java.util.List;
import net.augustus.modules.player.Stealer;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class StealerMouseSimulationMode extends AbstractStealerMouseMode {
   private Vec2d actualCursorPos;
   private Vec2d baseCursorPos;
   private UNoise3MouseSimulator uNoise3MouseSimulator;
   private IntervalNoiseSetting uNoise3Aimspeed;
   private IntervalNoiseSetting uNoise3DecelerationFactorPre;
   private IntervalNoiseSetting uNoise3DecelerationFactorPost;
   private IntervalNoiseSetting uNoise3DecelerationFactorPostHighspeed;
   private StringValue uNoise3highspeed;
   private RandomizedRangeSettings uNoise3resyncdelay;
   private int resyncCooldownSteps;
   private boolean resyncPending;
   private boolean resyncQueued;
   private double cachedAimSpeed;
   private boolean resyncEnabled;
   private int resyncCount;
   private int stepCount;

   public StealerMouseSimulationMode(Stealer stealer, DoubleValue doubleValue) {
      super(stealer, doubleValue, "Mouse Simulation");
      this.resyncEnabled = true;
      this.resyncCount = 0;
      this.stepCount = 0;
      IntervalNoiseSetting intervalNoiseSetting = new IntervalNoiseSetting(stealer, "UNoise3Aimspeed", doubleValue, true);
      String str = "Aim speed";
      IntervalNoiseSetting intervalNoiseSetting2 = intervalNoiseSetting.named(str);
      byte b = 1;
      double d = 20.0;
      double d2 = 15.0;
      double d3 = 100.0;
      double d4 = 1.0;
      this.uNoise3Aimspeed = intervalNoiseSetting2.withInterval(d4, d3, d2, d, b);
      IntervalNoiseSetting intervalNoiseSetting3 = new IntervalNoiseSetting(stealer, "UNoise3DecelerationFactorPre", doubleValue, false);
      String str2 = "Decelerationfactor Pre";
      IntervalNoiseSetting intervalNoiseSetting4 = intervalNoiseSetting3.named(str2);
      byte b2 = 2;
      double d5 = 1.0;
      double d6 = 1.0;
      double d7 = 2.0;
      double d8 = 0.5;
      this.uNoise3DecelerationFactorPre = intervalNoiseSetting4.withInterval(d8, d7, d6, d5, b2);
      IntervalNoiseSetting intervalNoiseSetting5 = new IntervalNoiseSetting(stealer, "UNoise3DecelerationFactorPost", doubleValue, false);
      String str3 = "Decelerationfactor Post";
      IntervalNoiseSetting intervalNoiseSetting6 = intervalNoiseSetting5.named(str3);
      byte b3 = 2;
      double d9 = 1.0;
      double d10 = 1.0;
      double d11 = 8.0;
      double d12 = 0.5;
      this.uNoise3DecelerationFactorPost = intervalNoiseSetting6.withInterval(d12, d11, d10, d9, b3);
      IntervalNoiseSetting intervalNoiseSetting7 = new IntervalNoiseSetting(stealer, "UNoise3DecelerationFactorPostHighspeed", doubleValue, false);
      String str4 = "Decelerationfactor Post at highspeed";
      IntervalNoiseSetting intervalNoiseSetting8 = intervalNoiseSetting7.named(str4);
      byte b4 = 2;
      double d13 = 2.0;
      double d14 = 2.0;
      double d15 = 8.0;
      double d16 = 0.5;
      this.uNoise3DecelerationFactorPostHighspeed = intervalNoiseSetting8.withInterval(d16, d15, d14, d13, b4);
      StringValue stringValue = new StringValue(stealer, "UNoise3highspeed", 10.0, 5.0, 20.0, 1, doubleValue);
      String str5 = "How many slots per second is high speed?";
      this.uNoise3highspeed = stringValue.named(str5);
      UNoise3SettingContext uNoise3SettingContext = new UNoise3SettingContext(stealer, doubleValue, () -> this.isSelected());
      this.uNoise3MouseSimulator = new UNoise3MouseSimulator(uNoise3SettingContext);
      ArrayList layers = this.uNoise3Aimspeed.getLayers();
      NoiseLayerBuilder noiseLayerBuilder = this.uNoise3Aimspeed.newLayerBuilder();
      float f = 16.6F;
      layers.add(noiseLayerBuilder.withStepDeltaMs(f).build());
      layers = this.uNoise3DecelerationFactorPre.getLayers();
      NoiseLayerBuilder noiseLayerBuilder2 = this.uNoise3DecelerationFactorPre.newLayerBuilder();
      float f2 = 16.6F;
      layers.add(noiseLayerBuilder2.withStepDeltaMs(f2).build());
      layers = this.uNoise3DecelerationFactorPost.getLayers();
      NoiseLayerBuilder noiseLayerBuilder3 = this.uNoise3DecelerationFactorPost.newLayerBuilder();
      float f3 = 16.6F;
      layers.add(noiseLayerBuilder3.withStepDeltaMs(f3).build());
      layers = this.uNoise3DecelerationFactorPostHighspeed.getLayers();
      NoiseLayerBuilder noiseLayerBuilder4 = this.uNoise3DecelerationFactorPostHighspeed.newLayerBuilder();
      float f4 = 16.6F;
      layers.add(noiseLayerBuilder4.withStepDeltaMs(f4).build());
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(stealer, "UNoise3resyncdelay", 0.0, 0.0, 0.0, 1000.0, 0, doubleValue);
      String str6 = "Resync delay";
      this.uNoise3resyncdelay = ((RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str6)).withoutNotInARow();
      this.baseCursorPos = new Vec2d(0.0, 0.0);
      this.actualCursorPos = new Vec2d(0.0, 0.0);
      IntervalNoiseSetting uNoise3Aimspeed2 = this.uNoise3Aimspeed;
      SettingVisibilityCondition condition = () -> this.isSelected();
      SettingVisibilityCondition condition2 = condition;
      uNoise3Aimspeed2.visibleWhen(condition2);
      IntervalNoiseSetting intervalNoiseSetting9 = this.uNoise3DecelerationFactorPre;
      condition = () -> this.isSelected();
      SettingVisibilityCondition condition3 = condition;
      intervalNoiseSetting9.visibleWhen(condition3);
      IntervalNoiseSetting intervalNoiseSetting10 = this.uNoise3DecelerationFactorPost;
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      SettingVisibilityCondition condition5 = condition4;
      intervalNoiseSetting10.visibleWhen(condition5);
      IntervalNoiseSetting intervalNoiseSetting11 = this.uNoise3DecelerationFactorPostHighspeed;
      condition4 = () -> this.isSelected();
      SettingVisibilityCondition condition6 = condition4;
      intervalNoiseSetting11.visibleWhen(condition6);
      StringValue uNoise3highspeed2 = this.uNoise3highspeed;
      SettingVisibilityCondition condition7 = () -> this.isSelected();
      uNoise3highspeed2.visibleWhen(condition7);
      RandomizedRangeSettings uNoise3resyncdelay2 = this.uNoise3resyncdelay;
      SettingVisibilityCondition condition8 = () -> this.isSelected();
      uNoise3resyncdelay2.withVisibility(condition8);
      this.uNoise3MouseSimulator.degreesPerStep = 1.0;
      this.uNoise3MouseSimulator.onGoalReached = () -> this.refreshSimulatorParameters();
      this.uNoise3MouseSimulator.onResyncTriggered = () -> this.requestResync();
   }

   @Override
   public Vec2d computeNextCursorPos(Container container, List list) {
      Slot slot = (Slot)list.get(0);
      double slotCenterX = ContainerUtil.getSlotCenterX(slot);
      double slotCenterY = ContainerUtil.getSlotCenterY(container, slot);
      MutableDoubleRef mutableDoubleRef = new MutableDoubleRef(slotCenterX - this.baseCursorPos.x);
      MutableDoubleRef mutableDoubleRef2 = new MutableDoubleRef(slotCenterY - this.baseCursorPos.y);
      this.processQueuedResync();
      UNoise3MouseSimulator uNoise3MouseSimulator2 = this.uNoise3MouseSimulator;
      MouseSimulationStepCallback callback = (n, d, d2) -> {
         this.tickResyncCooldown();
      };
      byte b = 1;
      double d3 = 0.0;
      MouseSimulationDelta mouseSimulationDelta = uNoise3MouseSimulator2.simulateTickSubsteps(mutableDoubleRef, mutableDoubleRef2, d3, b, callback);
      Vec2d noisyDelta = mouseSimulationDelta.noisyDelta;
      this.actualCursorPos = this.actualCursorPos.add(noisyDelta);
      Vec2d baseDelta = mouseSimulationDelta.baseDelta;
      this.baseCursorPos = this.baseCursorPos.add(baseDelta);
      this.uNoise3Aimspeed.stepLayers();
      this.uNoise3DecelerationFactorPre.stepLayers();
      this.uNoise3DecelerationFactorPost.stepLayers();
      this.uNoise3DecelerationFactorPostHighspeed.stepLayers();
      return this.actualCursorPos;
   }

   private void performResync() {
      boolean flag = true;
      this.resyncCooldownSteps = (int)((double)this.uNoise3resyncdelay.nextInt(flag) / 16.6);
      this.resyncQueued = false;
      this.actualCursorPos = this.baseCursorPos;
      this.resyncPending = false;
   }

   private void tickResyncCooldown() {
      if (this.resyncCooldownSteps > 0) {
         this.resyncCooldownSteps--;
      }
   }

   private void processQueuedResync() {
      if (this.resyncCooldownSteps == 0 && this.resyncQueued) {
         this.performResync();
      }
   }

   @Override
   public void resetCursorPosition() {
      ScaledResolution sr = new ScaledResolution(mc);
      this.actualCursorPos = this.baseCursorPos = new Vec2d(sr.getScaledWidth_double() / 2.0, sr.getScaledHeight_double() / 2.0);
      UNoise3MouseSimulator uNoise3MouseSimulator2 = this.uNoise3MouseSimulator;
      double d = this.uNoise3Aimspeed.sample();
      boolean flag = false;
      double d2 = d;
      uNoise3MouseSimulator2.setAcceleration(d2, flag);
      this.uNoise3MouseSimulator.preDecelerationFactor = this.uNoise3DecelerationFactorPre.sample();
      this.uNoise3MouseSimulator.postDecelerationFactor = this.uNoise3DecelerationFactorPost.sample();
      this.uNoise3MouseSimulator.postHighSpeedDecelFactor = this.uNoise3DecelerationFactorPostHighspeed.sample();
      this.uNoise3MouseSimulator.highSpeedThresholdDeg = this.uNoise3highspeed.getValue() * 19.0 / 60.0;
      this.uNoise3MouseSimulator.currentVelocity = new Vec2d(0.0, 0.0);
   }

   private void requestResync() {
      if (this.resyncCooldownSteps > 0) {
         this.resyncQueued = true;
      } else {
         this.performResync();
      }
   }

   @Override
   public void onTargetSlotChanged() {
      this.uNoise3MouseSimulator.onGoalReached.run();
   }

   @Override
   public void updateEventSubscriptions() {
   }

   @Override
   public void onEnable() {
      this.resetCursorPosition();
   }

   @Override
   public void onDisable() {
   }

   private void refreshSimulatorParameters() {
      this.requestResync();
      UNoise3MouseSimulator uNoise3MouseSimulator2 = this.uNoise3MouseSimulator;
      double d = this.uNoise3Aimspeed.sample();
      boolean flag = false;
      double d2 = d;
      uNoise3MouseSimulator2.setAcceleration(d2, flag);
      this.uNoise3MouseSimulator.clearAccelerationCap();
      this.uNoise3MouseSimulator.preDecelerationFactor = this.uNoise3DecelerationFactorPre.sample();
      this.uNoise3MouseSimulator.postDecelerationFactor = this.uNoise3DecelerationFactorPost.sample();
      this.uNoise3MouseSimulator.postHighSpeedDecelFactor = this.uNoise3DecelerationFactorPostHighspeed.sample();
      this.uNoise3MouseSimulator.highSpeedThresholdDeg = this.uNoise3highspeed.getValue() * 19.0 / 60.0;
   }

}
