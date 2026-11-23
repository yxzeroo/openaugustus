package net.augustus.modules.modes.clicking;

import net.minecraft.util.MovingObjectPosition;
import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.modules.combat.helper.ClickModuleContext;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.options.AdvancedClickHurtTimeOption;
import net.augustus.utils.ActionRateAccumulator;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.modules.Module;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;

public class AdvancedClickPatternMode extends AbstractClickPatternMode {
   private final RandomizedRangeSettings advancedCPS;
   private final RandomizedRangeSettings advancedCPSRecalculateDelay;
   private final BooleanValue spikes;
   private final StringValue spikeDelay;
   private final RandomizedRangeSettings spikeDuration;
   private final StringValue spikeMultiplier;
   private final BooleanValue drops;
   private final StringValue dropDelay;
   private final RandomizedRangeSettings dropDuration;
   private final StringValue dropMultiplier;
   private final BooleanValue reduceHitsInAir;
   private final RandomizedRangeSettings reduceRecalculateDelay;
   private final RandomizedRangeSettings reduceMultiplier;
   private final BooleanValue reduceHitsOnWall;
   private final RandomizedRangeSettings reduceRecalculateDelayOnWall;
   private final RandomizedRangeSettings reduceMultiplierOnWall;
   private final MultiOptionSetting hurttimesToHit;
   private final ActionRateAccumulator actionRateAccumulator;
   private final TimeHelper cpsRecalcTimer;
   private final TimeHelper dropTimer;
   private final TimeHelper spikeTimer;
   private final TimeHelper airReduceTimer;
   private final TimeHelper wallReduceTimer;
   private final AdvancedClickHurtTimeOption[] hurtTimeOptions;
   private long nextSpikeDelayMs;
   private long nextDropDelayMs;

   public AdvancedClickPatternMode(Module module, DoubleValue doubleValue) {
       super(module, doubleValue, "Advanced");
      this.actionRateAccumulator = new ActionRateAccumulator(20, 0.0);
      this.cpsRecalcTimer = new TimeHelper();
      this.dropTimer = new TimeHelper();
      this.spikeTimer = new TimeHelper();
      this.airReduceTimer = new TimeHelper();
      this.wallReduceTimer = new TimeHelper();
      this.hurtTimeOptions = new AdvancedClickHurtTimeOption[11];
      this.nextSpikeDelayMs = 2000L;
      this.nextDropDelayMs = 2000L;
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(module, "AdvancedCPS", 20.0, 20.0, 0.0, 40.0, 0, doubleValue)).withoutNotInARow();
      String str = "CPS";
      randomizedRangeSettings = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.advancedCPS = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition);
      randomizedRangeSettings = (new RandomizedRangeSettings(module, "AdvancedCPSRecalculateDelay", 200.0, 600.0, 0.0, 4000.0, 0, doubleValue)).withoutNotInARow();
      String str2 = "CPSRecalculateDelay";
      randomizedRangeSettings = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str2);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.advancedCPSRecalculateDelay = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition2);
      BooleanValue booleanValue = new BooleanValue(module, "Spikes", false, doubleValue);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.spikes = (BooleanValue)booleanValue.withVisibility(condition3);
      StringValue stringValue = new StringValue(module, "SpikeDelay", 15000.0, 0.0, 20000.0, 0, doubleValue);
      SettingVisibilityCondition condition4 = () -> this.isSpikeDelayVisible();
      this.spikeDelay = stringValue.visibleWhen(condition4);
      randomizedRangeSettings = (new RandomizedRangeSettings(module, "SpikeDuration", 500.0, 1000.0, 0.0, 2000.0, 0, doubleValue)).withoutNotInARow();
      SettingVisibilityCondition condition5 = () -> this.isSpikeDurationVisible();
      this.spikeDuration = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition5);
      StringValue stringValue2 = new StringValue(module, "SpikeMultiplier", 1.5, 1.0, 3.0, 2, doubleValue);
      SettingVisibilityCondition condition6 = () -> this.isSpikeMultiplierVisible();
      this.spikeMultiplier = stringValue2.visibleWhen(condition6);
      BooleanValue booleanValue2 = new BooleanValue(module, "Drops", false, doubleValue);
      SettingVisibilityCondition condition7 = () -> this.isSelected();
      this.drops = (BooleanValue)booleanValue2.withVisibility(condition7);
      StringValue stringValue3 = new StringValue(module, "DropDelay", 7000.0, 0.0, 20000.0, 0, doubleValue);
      SettingVisibilityCondition condition8 = () -> this.isDropDelayVisible();
      this.dropDelay = stringValue3.visibleWhen(condition8);
      randomizedRangeSettings = (new RandomizedRangeSettings(module, "DropDuration", 500.0, 1000.0, 0.0, 2000.0, 0, doubleValue)).withoutNotInARow();
      SettingVisibilityCondition condition9 = () -> this.isDropDurationVisible();
      this.dropDuration = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition9);
      StringValue stringValue4 = new StringValue(module, "DropMultiplier", 0.5, 0.0, 1.0, 2, doubleValue);
      SettingVisibilityCondition condition10 = () -> this.isDropMultiplierVisible();
      this.dropMultiplier = stringValue4.visibleWhen(condition10);
      BooleanValue booleanValue3 = new BooleanValue(module, "ReduceHitsInAir", true, doubleValue);
      SettingVisibilityCondition condition11 = () -> this.isSelected();
      this.reduceHitsInAir = (BooleanValue)booleanValue3.withVisibility(condition11);
      randomizedRangeSettings = (new RandomizedRangeSettings(module, "ReduceRecalculateDelay", 100.0, 200.0, 0.0, 1000.0, 0, doubleValue)).withoutNotInARow();
      SettingVisibilityCondition condition12 = () -> this.isAirReduceDelayVisible();
      this.reduceRecalculateDelay = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition12);
      randomizedRangeSettings = (new RandomizedRangeSettings(module, "ReduceMultiplier", 0.4, 0.4, 0.0, 1.0, 2, doubleValue)).withoutNotInARow();
      SettingVisibilityCondition condition13 = () -> this.isAirReduceMultiplierVisible();
      this.reduceMultiplier = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition13);
      BooleanValue booleanValue4 = new BooleanValue(module, "ReduceHitsOnWall", false, doubleValue);
      SettingVisibilityCondition condition14 = () -> this.isSelected();
      this.reduceHitsOnWall = (BooleanValue)booleanValue4.withVisibility(condition14);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(module, "ReduceRecalculateDelayOnWall", 100.0, 200.0, 0.0, 1000.0, 0, doubleValue)).withoutNotInARow();
      SettingVisibilityCondition condition15 = () -> this.isWallReduceDelayVisible();
      this.reduceRecalculateDelayOnWall = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition15);
      RandomizedRangeSettings randomizedRangeSettings3 = (new RandomizedRangeSettings(module, "ReduceMultiplierOnWall", 0.4, 0.4, 0.0, 1.0, 2, doubleValue)).withoutNotInARow();
      SettingVisibilityCondition condition16 = () -> {
         return this.isSelected() && this.reduceHitsOnWall.getValue();
      };
      this.reduceMultiplierOnWall = (RandomizedRangeSettings)randomizedRangeSettings3.withVisibility(condition16);
      this.initHurtTimeOptions();
      MultiOptionSetting multiOptionSetting = new MultiOptionSetting(module, "HurttimesToHit", this.hurtTimeOptions, doubleValue);
      SettingVisibilityCondition condition17 = () -> this.isSelected();
      this.hurttimesToHit = multiOptionSetting.visibleWhen(condition17);
   }

   @Override
   public int computeClicks(EventClick event) {
      long l;
      TimeHelper cpsRecalcTimer2 = this.cpsRecalcTimer;
      long lastIntValue = (long)this.advancedCPSRecalculateDelay.getLastIntValue();
      if (cpsRecalcTimer2.reached2(lastIntValue)) {
         this.advancedCPS.nextSecureValue();
         this.advancedCPSRecalculateDelay.nextValue();
         this.cpsRecalcTimer.reset();
      }

      int lastIntValue2 = this.advancedCPS.getLastIntValue();
      if (this.spikes.getValue()) {
         int value = (int)this.spikeDelay.getValue();
         long nextSpikeDelayMs2 = this.nextSpikeDelayMs;
         if (this.spikeTimer.reached2(nextSpikeDelayMs2)) {
            lastIntValue2 = (int)Math.round((double)this.advancedCPS.getLastIntValue() * this.spikeMultiplier.getValue());
            cpsRecalcTimer2 = this.spikeTimer;
            l = this.nextSpikeDelayMs;
            RandomNumberGenerator instance = DefaultRandomGenerator.instance;
            int minValue = (int)this.spikeDuration.getMinValue();
            int maxValue = (int)this.spikeDuration.getMaxValue();
            int n = maxValue;
            int n2 = minValue;
            long l2 = l + (long)instance.nextSecureInt(n2, n);
            if (cpsRecalcTimer2.reached2(l2)) {
               RandomNumberGenerator instance2 = DefaultRandomGenerator.instance;
               int n3 = Math.max(0, value - 100);
               minValue = value + 100;
               int n4 = minValue;
               int n5 = n3;
               this.nextSpikeDelayMs = (long)instance2.nextSecureInt(n5, n4);
               this.spikeTimer.reset();
            }
         }
      }

      if (this.drops.getValue()) {
         int value2 = (int)this.dropDelay.getValue();
         long nextDropDelayMs2 = this.nextDropDelayMs;
         if (this.dropTimer.reached2(nextDropDelayMs2)) {
            lastIntValue2 = (int)Math.round((double)this.advancedCPS.getLastIntValue() * this.dropMultiplier.getValue());
            cpsRecalcTimer2 = this.dropTimer;
            l = this.nextDropDelayMs;
            RandomNumberGenerator instance3 = DefaultRandomGenerator.instance;
            int minValue2 = (int)this.dropDuration.getMinValue();
            int maxValue2 = (int)this.dropDuration.getMaxValue();
            int n6 = maxValue2;
            int n7 = minValue2;
            long l3 = l + (long)instance3.nextSecureInt(n7, n6);
            if (cpsRecalcTimer2.reached2(l3)) {
               RandomNumberGenerator instance4 = DefaultRandomGenerator.instance;
               int n8 = Math.max(0, value2 - 100);
               minValue2 = value2 + 100;
               int n9 = minValue2;
               int n10 = n8;
               this.nextDropDelayMs = (long)instance4.nextSecureInt(n10, n9);
               this.dropTimer.reset();
            }
         }
      }

      if (this.reduceHitsInAir.getValue() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
         cpsRecalcTimer2 = this.airReduceTimer;
         long lastIntValue3 = (long)this.reduceRecalculateDelay.getLastIntValue();
         if (cpsRecalcTimer2.reached2(lastIntValue3)) {
            this.reduceRecalculateDelay.nextValue();
            this.reduceMultiplier.nextValue();
            this.airReduceTimer.reset();
         }

         lastIntValue2 = (int)Math.round((double)this.advancedCPS.getLastIntValue() * this.reduceMultiplier.getLastValue());
      }

      if (this.reduceHitsOnWall.getValue() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
         cpsRecalcTimer2 = this.wallReduceTimer;
         long lastIntValue4 = (long)this.reduceRecalculateDelayOnWall.getLastIntValue();
         if (cpsRecalcTimer2.reached2(lastIntValue4)) {
            this.reduceRecalculateDelayOnWall.nextValue();
            this.reduceMultiplierOnWall.nextValue();
            this.wallReduceTimer.reset();
         }

         lastIntValue2 = (int)Math.round((double)this.advancedCPS.getLastIntValue() * this.reduceMultiplierOnWall.getLastValue());
      }

      if (ClientManagers.moduleManager.smartClicking.isSuppressingClicks()) {
         return 0;
      } else {
         int n11 = ((ClickModuleContext)this.parentModule).getTarget() != null ? ((ClickModuleContext)this.parentModule).getTarget().hurtTime : 11;
         if (n11 < 11 && n11 > -1 && !this.hurtTimeOptions[n11].selected) {
            return 0;
         } else {
            ClickModuleContext parentModule2 = (ClickModuleContext)this.parentModule;
            int n12 = this.actionRateAccumulator.accumulate(lastIntValue2);
            return parentModule2.applyHitReduction(n12);
         }
      }
   }

   private void initHurtTimeOptions() {

      for (int i = 0; i < this.hurtTimeOptions.length; i++) {
         this.hurtTimeOptions[i] = new AdvancedClickHurtTimeOption(i);
      }
   }

   @Override
   public void onTick2() {
   }

   @Override
   public void updateEventSubscriptions() {
   }

   @Override
   public void onEnable() {
      this.nextSpikeDelayMs = 2000L;
      this.nextDropDelayMs = 2000L;
   }

   @Override
   public void onDisable() {
   }

   private boolean isWallReduceDelayVisible() {
      return this.isSelected() && this.reduceHitsOnWall.getValue();
   }

   private boolean isAirReduceMultiplierVisible() {
      return this.isSelected() && this.reduceHitsInAir.getValue();
   }

   private boolean isAirReduceDelayVisible() {
      return this.isSelected() && this.reduceHitsInAir.getValue();
   }

   private boolean isDropMultiplierVisible() {
      return this.isSelected() && this.drops.getValue();
   }

   private boolean isDropDurationVisible() {
      return this.isSelected() && this.drops.getValue();
   }

   private boolean isDropDelayVisible() {
      return this.isSelected() && this.drops.getValue();
   }

   private boolean isSpikeMultiplierVisible() {
      return this.isSelected() && this.spikes.getValue();
   }

   private boolean isSpikeDurationVisible() {
      return this.isSelected() && this.spikes.getValue();
   }

   private boolean isSpikeDelayVisible() {
      return this.isSelected() && this.spikes.getValue();
   }

}
