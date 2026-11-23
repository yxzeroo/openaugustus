package net.augustus.modules.modes.clicking.legacy;

import net.minecraft.util.MovingObjectPosition;
import net.augustus.ClientManagers;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.ActionRateAccumulator;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.math.RandomNumberGenerator;
import java.util.function.BiFunction;
import net.augustus.modules.Module;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.EntityLivingBase;

public class LegacyAdvancedClickMode extends AbstractLegacyClickMode {
   private final RandomizedRangeSettings advancedCPS;
   private final RandomizedRangeSettings advancedCPSRecalculateDelay;
   private final BooleanValue advancedSpikes;
   private final StringValue advancedSpikeDelay;
   private final RandomizedRangeSettings advancedSpikeDuration;
   private final StringValue advancedSpikeMultiplier;
   private final BooleanValue advancedDrops;
   private final StringValue advancedDropDelay;
   private final RandomizedRangeSettings advancedDropDuration;
   private final StringValue advancedDropMultiplier;
   private final BooleanValue advancedReduceHitsInAir;
   private final RandomizedRangeSettings advancedReduceRecalculateDelay;
   private final RandomizedRangeSettings advancedReduceMultiplier;
   private final BooleanValue advancedReduceHitsOnWall;
   private final RandomizedRangeSettings advancedReduceRecalculateDelayOnWall;
   private final RandomizedRangeSettings advancedReduceMultiplierOnWall;
   private final ActionRateAccumulator actionRateAccumulator;
   private final TimeHelper cpsRecalcTimer;
   private final TimeHelper dropTimer;
   private final TimeHelper spikeTimer;
   private final TimeHelper airReduceTimer;
   private final TimeHelper wallReduceTimer;
   private long nextSpikeDelayMs;
   private long nextDropDelayMs;

   public LegacyAdvancedClickMode(Module module, DoubleValue doubleValue) {
       super(module, doubleValue, "Advanced");
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this.parentModule, "AdvancedCPS", 20.0, 20.0, 0.0, 40.0, 0, this.modeSetting)).withoutNotInARow();
      String str = "CPS";
      this.advancedCPS = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this.parentModule, "AdvancedCPSRecalculateDelay", 200.0, 600.0, 0.0, 4000.0, 0, this.modeSetting)).withoutNotInARow();
      String str2 = "CPSRecalculateDelay";
      this.advancedCPSRecalculateDelay = (RandomizedRangeSettings)randomizedRangeSettings2.withDisplayNamePrefix(str2);
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "AdvancedSpikes", false, this.modeSetting);
      String str3 = "Spikes";
      this.advancedSpikes = (BooleanValue)booleanValue.withDisplayName(str3);
      StringValue stringValue = new StringValue(this.parentModule, "AdvancedSpikeDelay", 15000.0, 0.0, 20000.0, 0, this.modeSetting);
      String str4 = "SpikeDelay";
      StringValue stringValue2 = stringValue.named(str4);
      BooleanValue advancedSpikes2 = this.advancedSpikes;
      this.advancedSpikes.getClass();
      BooleanValue booleanValue2 = advancedSpikes2;
      SettingVisibilityCondition condition = () -> booleanValue2.getValue();
      this.advancedSpikeDelay = stringValue2.visibleWhen(condition);
      RandomizedRangeSettings randomizedRangeSettings3 = (new RandomizedRangeSettings(this.parentModule, "AdvancedSpikeDuration", 500.0, 1000.0, 0.0, 2000.0, 0, this.modeSetting)).withoutNotInARow();
      String str5 = "SpikeDuration";
      RandomizedRangeSettings randomizedRangeSettings4 = (RandomizedRangeSettings)randomizedRangeSettings3.withDisplayNamePrefix(str5);
      advancedSpikes2 = this.advancedSpikes;
      this.advancedSpikes.getClass();
      BooleanValue booleanValue3 = advancedSpikes2;
      SettingVisibilityCondition condition2 = () -> booleanValue3.getValue();
      this.advancedSpikeDuration = (RandomizedRangeSettings)randomizedRangeSettings4.withVisibility(condition2);
      StringValue stringValue3 = new StringValue(this.parentModule, "AdvancedSpikeMultiplier", 1.5, 1.0, 3.0, 2, this.modeSetting);
      String str6 = "SpikeMultiplier";
      StringValue stringValue4 = stringValue3.named(str6);
      advancedSpikes2 = this.advancedSpikes;
      this.advancedSpikes.getClass();
      BooleanValue booleanValue4 = advancedSpikes2;
      SettingVisibilityCondition condition3 = () -> booleanValue4.getValue();
      this.advancedSpikeMultiplier = stringValue4.visibleWhen(condition3);
      BooleanValue booleanValue5 = new BooleanValue(this.parentModule, "AdvancedDrops", false, this.modeSetting);
      String str7 = "Drops";
      this.advancedDrops = (BooleanValue)booleanValue5.withDisplayName(str7);
      StringValue stringValue5 = new StringValue(this.parentModule, "AdvancedDropDelay", 7000.0, 0.0, 20000.0, 0, this.modeSetting);
      String str8 = "DropDelay";
      StringValue stringValue6 = stringValue5.named(str8);
      advancedSpikes2 = this.advancedDrops;
      this.advancedDrops.getClass();
      BooleanValue booleanValue6 = advancedSpikes2;
      SettingVisibilityCondition condition4 = () -> booleanValue6.getValue();
      this.advancedDropDelay = stringValue6.visibleWhen(condition4);
      RandomizedRangeSettings randomizedRangeSettings5 = new RandomizedRangeSettings(this.parentModule, "AdvancedDropDuration", 500.0, 1000.0, 0.0, 2000.0, 0, this.modeSetting);
      String str9 = "DropDuration";
      RandomizedRangeSettings randomizedRangeSettings6 = ((RandomizedRangeSettings)randomizedRangeSettings5.withDisplayNamePrefix(str9)).withoutNotInARow();
      advancedSpikes2 = this.advancedDrops;
      this.advancedDrops.getClass();
      BooleanValue booleanValue7 = advancedSpikes2;
      SettingVisibilityCondition condition5 = () -> booleanValue7.getValue();
      this.advancedDropDuration = (RandomizedRangeSettings)randomizedRangeSettings6.withVisibility(condition5);
      StringValue stringValue7 = new StringValue(this.parentModule, "AdvancedDropMultiplier", 0.5, 0.0, 1.0, 2, this.modeSetting);
      String str10 = "DropMultiplier";
      StringValue stringValue8 = stringValue7.named(str10);
      advancedSpikes2 = this.advancedDrops;
      this.advancedDrops.getClass();
      BooleanValue booleanValue8 = advancedSpikes2;
      SettingVisibilityCondition condition6 = () -> booleanValue8.getValue();
      this.advancedDropMultiplier = stringValue8.visibleWhen(condition6);
      BooleanValue booleanValue9 = new BooleanValue(this.parentModule, "AdvancedReduceHitsInAir", true, this.modeSetting);
      String str11 = "ReduceHitsInAir";
      this.advancedReduceHitsInAir = (BooleanValue)booleanValue9.withDisplayName(str11);
      RandomizedRangeSettings randomizedRangeSettings7 = (new RandomizedRangeSettings(this.parentModule, "AdvancedReduceRecalculateDelay", 100.0, 200.0, 0.0, 1000.0, 0, this.modeSetting)).withoutNotInARow();
      String str12 = "ReduceRecalculateDelay";
      RandomizedRangeSettings randomizedRangeSettings8 = (RandomizedRangeSettings)randomizedRangeSettings7.withDisplayNamePrefix(str12);
      advancedSpikes2 = this.advancedReduceHitsInAir;
      this.advancedReduceHitsInAir.getClass();
      BooleanValue booleanValue10 = advancedSpikes2;
      SettingVisibilityCondition condition7 = () -> booleanValue10.getValue();
      this.advancedReduceRecalculateDelay = (RandomizedRangeSettings)randomizedRangeSettings8.withVisibility(condition7);
      RandomizedRangeSettings randomizedRangeSettings9 = (new RandomizedRangeSettings(this.parentModule, "AdvancedReduceMultiplier", 0.4, 0.4, 0.0, 1.0, 2, this.modeSetting)).withoutNotInARow();
      String str13 = "ReduceMultiplier";
      RandomizedRangeSettings randomizedRangeSettings10 = (RandomizedRangeSettings)randomizedRangeSettings9.withDisplayNamePrefix(str13);
      advancedSpikes2 = this.advancedReduceHitsInAir;
      this.advancedReduceHitsInAir.getClass();
      BooleanValue booleanValue11 = advancedSpikes2;
      SettingVisibilityCondition condition8 = () -> booleanValue11.getValue();
      this.advancedReduceMultiplier = (RandomizedRangeSettings)randomizedRangeSettings10.withVisibility(condition8);
      BooleanValue booleanValue12 = new BooleanValue(this.parentModule, "AdvancedReduceHitsOnWall", false, this.modeSetting);
      String str14 = "ReduceHitsOnWall";
      this.advancedReduceHitsOnWall = (BooleanValue)booleanValue12.withDisplayName(str14);
      RandomizedRangeSettings randomizedRangeSettings11 = (new RandomizedRangeSettings(this.parentModule, "AdvancedReduceRecalculateDelayOnWall", 100.0, 200.0, 0.0, 1000.0, 0, this.modeSetting))
         .withoutNotInARow();
      String str15 = "ReduceRecalculateDelayOnWall";
      RandomizedRangeSettings randomizedRangeSettings12 = (RandomizedRangeSettings)randomizedRangeSettings11.withDisplayNamePrefix(str15);
      BooleanValue booleanValue13 = this.advancedReduceHitsOnWall;
      this.advancedReduceHitsOnWall.getClass();
      BooleanValue booleanValue14 = booleanValue13;
      SettingVisibilityCondition condition9 = () -> booleanValue14.getValue();
      this.advancedReduceRecalculateDelayOnWall = (RandomizedRangeSettings)randomizedRangeSettings12.withVisibility(condition9);
      RandomizedRangeSettings randomizedRangeSettings13 = (new RandomizedRangeSettings(this.parentModule, "AdvancedReduceMultiplierOnWall", 0.4, 0.4, 0.0, 1.0, 2, this.modeSetting)).withoutNotInARow();
      String str16 = "ReduceMultiplierOnWall";
      RandomizedRangeSettings randomizedRangeSettings14 = (RandomizedRangeSettings)randomizedRangeSettings13.withDisplayNamePrefix(str16);
      booleanValue13 = this.advancedReduceHitsOnWall;
      this.advancedReduceHitsOnWall.getClass();
      BooleanValue booleanValue15 = booleanValue13;
      SettingVisibilityCondition condition10 = () -> booleanValue15.getValue();
      this.advancedReduceMultiplierOnWall = (RandomizedRangeSettings)randomizedRangeSettings14.withVisibility(condition10);
      this.actionRateAccumulator = new ActionRateAccumulator(20, 0.0);
      this.cpsRecalcTimer = new TimeHelper();
      this.dropTimer = new TimeHelper();
      this.spikeTimer = new TimeHelper();
      this.airReduceTimer = new TimeHelper();
      this.wallReduceTimer = new TimeHelper();
      this.nextSpikeDelayMs = 0L;
      this.nextDropDelayMs = 0L;
   }

   @Override
   public int computeClicks(EntityLivingBase entity) {
      long l;
      TimeHelper cpsRecalcTimer2 = this.cpsRecalcTimer;
      long lastIntValue = (long)this.advancedCPSRecalculateDelay.getLastIntValue();
      if (cpsRecalcTimer2.reached2(lastIntValue)) {
         this.advancedCPS.nextSecureValue();
         this.advancedCPSRecalculateDelay.nextValue();
         this.cpsRecalcTimer.reset();
      }

      int lastIntValue2 = this.advancedCPS.getLastIntValue();
      if (this.advancedSpikes.getValue()) {
         int value = (int)this.advancedSpikeDelay.getValue();
         long nextSpikeDelayMs2 = this.nextSpikeDelayMs;
         if (this.spikeTimer.reached2(nextSpikeDelayMs2)) {
            lastIntValue2 = (int)Math.round((double)this.advancedCPS.getLastIntValue() * this.advancedSpikeMultiplier.getValue());
            cpsRecalcTimer2 = this.spikeTimer;
            l = this.nextSpikeDelayMs;
            RandomNumberGenerator instance = DefaultRandomGenerator.instance;
            int minValue = (int)this.advancedSpikeDuration.getMinValue();
            int maxValue = (int)this.advancedSpikeDuration.getMaxValue();
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

      if (this.advancedDrops.getValue()) {
         int value2 = (int)this.advancedDropDelay.getValue();
         long nextDropDelayMs2 = this.nextDropDelayMs;
         if (this.dropTimer.reached2(nextDropDelayMs2)) {
            lastIntValue2 = (int)Math.round((double)this.advancedCPS.getLastIntValue() * this.advancedDropMultiplier.getValue());
            cpsRecalcTimer2 = this.dropTimer;
            l = this.nextDropDelayMs;
            RandomNumberGenerator instance3 = DefaultRandomGenerator.instance;
            int minValue2 = (int)this.advancedDropDuration.getMinValue();
            int maxValue2 = (int)this.advancedDropDuration.getMaxValue();
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

      if (this.advancedReduceHitsInAir.getValue() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
         cpsRecalcTimer2 = this.airReduceTimer;
         long lastIntValue3 = (long)this.advancedReduceRecalculateDelay.getLastIntValue();
         if (cpsRecalcTimer2.reached2(lastIntValue3)) {
            this.advancedReduceRecalculateDelay.nextValue();
            this.advancedReduceMultiplier.nextValue();
            this.airReduceTimer.reset();
         }

         lastIntValue2 = (int)Math.round((double)this.advancedCPS.getLastIntValue() * this.advancedReduceMultiplier.getLastValue());
      }

      if (this.advancedReduceHitsOnWall.getValue() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
         cpsRecalcTimer2 = this.wallReduceTimer;
         long lastIntValue4 = (long)this.advancedReduceRecalculateDelayOnWall.getLastIntValue();
         if (cpsRecalcTimer2.reached2(lastIntValue4)) {
            this.advancedReduceRecalculateDelayOnWall.nextValue();
            this.advancedReduceMultiplierOnWall.nextValue();
            this.wallReduceTimer.reset();
         }

         lastIntValue2 = (int)Math.round((double)this.advancedCPS.getLastIntValue() * this.advancedReduceMultiplierOnWall.getLastValue());
      }

      return ClientManagers.moduleManager.smartClicking.isSuppressingClicks() ? 0 : this.actionRateAccumulator.accumulate(lastIntValue2);
   }

   @Override
   public void onClickPerformed(EntityLivingBase entity) {
   }

   @Override
   public void onTickUpdate(long l) {
   }

   @Override
   public void updateEventSubscriptions() {
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new LegacyAdvancedClickMode((Module)obj, (DoubleValue)obj2);
   }

}
