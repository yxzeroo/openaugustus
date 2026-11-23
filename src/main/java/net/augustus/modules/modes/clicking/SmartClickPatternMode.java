package net.augustus.modules.modes.clicking;

import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.modules.combat.helper.ClickModuleContext;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.ActionRateAccumulator;
import net.augustus.utils.TickCounter;
import net.augustus.modules.Module;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;

public class SmartClickPatternMode extends AbstractClickPatternMode {
   private final BooleansSetting smartMode;
   private final BooleansSetting smartClickMode;
   private final BooleansSetting smartDoubleClickModeS;
   private final DoubleValue cpsSettings;
   private final DoubleValue inAir;
   private final RandomizedRangeSettings smartCPSInAir;
   private final RandomizedRangeSettings smartCPSInAirRecalculateTickDelay;
   private final RandomizedRangeSettings smartDCPSInAir;
   private final RandomizedRangeSettings dcpsInAirRecalculateTickDelay;
   private final DoubleValue onWall;
   private final RandomizedRangeSettings smartCPSOnWall;
   private final RandomizedRangeSettings smartCPSOnWallRecalculateTickDelay;
   private final RandomizedRangeSettings smartDCPSOnWall;
   private final RandomizedRangeSettings dcpsOnWallRecalculateTickDelay;
   private final DoubleValue onFirstHit;
   private final RandomizedRangeSettings smartCPSFirstHit;
   private final RandomizedRangeSettings smartCPSFirstHitRecalculateTickDelay;
   private final RandomizedRangeSettings smartDCPSFirstHit;
   private final RandomizedRangeSettings smartDCPSFirstHitRecalculateTickDelay;
   private final DoubleValue onTarget;
   private final RandomizedRangeSettings smartCPS;
   private final RandomizedRangeSettings cpsRecalculateTickDelay;
   private final RandomizedRangeSettings smartDCPS;
   private final RandomizedRangeSettings dcpsRecalculateTickDelay;
   private final TickCounter[] hurtTimeRecalcCounters;
   private final ActionRateAccumulator[] separateRateAccumulators;
   private final RandomizedRangeSettings[] hurtTimeRangeSettings;
   private final TickCounter airCpsRecalcCounter;
   private final TickCounter airDcpsRecalcCounter;
   private final TickCounter wallCpsRecalcCounter;
   private final TickCounter wallDcpsRecalcCounter;
   private final TickCounter firstHitCpsRecalcCounter;
   private final TickCounter firstHitDcpsRecalcCounter;
   private final TickCounter targetCpsRecalcCounter;
   private final TickCounter targetDcpsRecalcCounter;
   private final ActionRateAccumulator combinedClickAccumulator;
   private final ActionRateAccumulator combinedDoubleClickAccumulator;
   private int consecutiveTargetClickTicks;
   private int lastTargetHurtTime;

   public SmartClickPatternMode(Module module, DoubleValue doubleValue) {
       super(module, doubleValue, "Smart");
      this.hurtTimeRecalcCounters = new TickCounter[22];
      this.separateRateAccumulators = new ActionRateAccumulator[28];
      this.hurtTimeRangeSettings = new RandomizedRangeSettings[44];
      this.airCpsRecalcCounter = new TickCounter();
      this.airDcpsRecalcCounter = new TickCounter();
      this.wallCpsRecalcCounter = new TickCounter();
      this.wallDcpsRecalcCounter = new TickCounter();
      this.firstHitCpsRecalcCounter = new TickCounter();
      this.firstHitDcpsRecalcCounter = new TickCounter();
      this.targetCpsRecalcCounter = new TickCounter();
      this.targetDcpsRecalcCounter = new TickCounter();
      this.combinedClickAccumulator = new ActionRateAccumulator(20, 0.0);
      this.combinedDoubleClickAccumulator = new ActionRateAccumulator(20, 5.0);
      this.consecutiveTargetClickTicks = 0;
      this.lastTargetHurtTime = 0;
      BooleansSetting booleansSetting = new BooleansSetting(module, "SmartMode", "Recommended", new String[]{"Recommended", "Overengineered"}, doubleValue);
      String str = "Mode";
      booleansSetting = (BooleansSetting)booleansSetting.withDisplayName(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.smartMode = (BooleansSetting)booleansSetting.withVisibility(condition);
      booleansSetting = new BooleansSetting(module, "SmartClickMode", "Combined", new String[]{"Combined", "Default", "Separate"}, doubleValue);
      String str2 = "ClickMode";
      booleansSetting = (BooleansSetting)booleansSetting.withDisplayName(str2);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.smartClickMode = (BooleansSetting)booleansSetting.withVisibility(condition2);
      booleansSetting = new BooleansSetting(module, "SmartDoubleClickModeS", "Default", new String[]{"Combined", "Default", "Separate"}, doubleValue);
      String str3 = "DoubleClickMode";
      booleansSetting = (BooleansSetting)booleansSetting.withDisplayName(str3);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.smartDoubleClickModeS = (BooleansSetting)booleansSetting.withVisibility(condition3);
      DoubleValue doubleValue2 = new DoubleValue(module, "CPS Settings", true, doubleValue);
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      this.cpsSettings = doubleValue2.visibleWhen(condition4);
      DoubleValue doubleValue3 = new DoubleValue(module, "In Air", true, this.cpsSettings);
      SettingVisibilityCondition condition5 = () -> this.isSelected();
      this.inAir = doubleValue3.visibleWhen(condition5);
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(module, "SmartCPSInAir", 4.0, 8.0, 0.0, 20.0, 0, this.inAir);
      String str4 = "CPSInAir";
      RandomizedRangeSettings randomizedRangeSettings2 = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str4);
      SettingVisibilityCondition condition6 = () -> this.isSelected();
      this.smartCPSInAir = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition6);
      RandomizedRangeSettings randomizedRangeSettings3 = new RandomizedRangeSettings(module, "SmartCPSInAirRecalculateTickDelay", 3.0, 8.0, 0.0, 60.0, 0, this.inAir);
      String str5 = "CPSInAirRecalculateTickDelay";
      RandomizedRangeSettings randomizedRangeSettings4 = ((RandomizedRangeSettings)randomizedRangeSettings3.withDisplayNamePrefix(str5)).withoutNotInARow();
      SettingVisibilityCondition condition7 = () -> this.isSelected();
      this.smartCPSInAirRecalculateTickDelay = (RandomizedRangeSettings)randomizedRangeSettings4.withVisibility(condition7);
      RandomizedRangeSettings randomizedRangeSettings5 = new RandomizedRangeSettings(module, "SmartDCPSInAir", 0.0, 0.0, 0.0, 20.0, 0, this.inAir);
      String str6 = "DoubleClicksPerSecondInAir";
      RandomizedRangeSettings randomizedRangeSettings6 = (RandomizedRangeSettings)randomizedRangeSettings5.withDisplayNamePrefix(str6);
      SettingVisibilityCondition condition8 = () -> this.isSelected();
      this.smartDCPSInAir = (RandomizedRangeSettings)randomizedRangeSettings6.withVisibility(condition8);
      RandomizedRangeSettings randomizedRangeSettings7 = (new RandomizedRangeSettings(module, "DCPSInAirRecalculateTickDelay", 3.0, 8.0, 1.0, 40.0, 0, this.inAir)).withoutNotInARow();
      SettingVisibilityCondition condition9 = () -> this.isSelected();
      this.dcpsInAirRecalculateTickDelay = (RandomizedRangeSettings)randomizedRangeSettings7.withVisibility(condition9);
      DoubleValue doubleValue4 = new DoubleValue(module, "On wall", true, this.cpsSettings);
      SettingVisibilityCondition condition10 = () -> this.isSelected();
      this.onWall = doubleValue4.visibleWhen(condition10);
      RandomizedRangeSettings randomizedRangeSettings8 = new RandomizedRangeSettings(module, "SmartCPSOnWall", 8.0, 12.0, 0.0, 20.0, 0, this.onWall);
      String str7 = "CPSOnWall";
      RandomizedRangeSettings randomizedRangeSettings9 = (RandomizedRangeSettings)randomizedRangeSettings8.withDisplayNamePrefix(str7);
      SettingVisibilityCondition condition11 = () -> this.isSelected();
      this.smartCPSOnWall = (RandomizedRangeSettings)randomizedRangeSettings9.withVisibility(condition11);
      RandomizedRangeSettings randomizedRangeSettings10 = new RandomizedRangeSettings(module, "SmartCPSOnWallRecalculateTickDelay", 3.0, 8.0, 0.0, 60.0, 0, this.onWall);
      String str8 = "CPSOnWallRecalculateTickDelay";
      RandomizedRangeSettings randomizedRangeSettings11 = ((RandomizedRangeSettings)randomizedRangeSettings10.withDisplayNamePrefix(str8)).withoutNotInARow();
      SettingVisibilityCondition condition12 = () -> this.isSelected();
      this.smartCPSOnWallRecalculateTickDelay = (RandomizedRangeSettings)randomizedRangeSettings11.withVisibility(condition12);
      RandomizedRangeSettings randomizedRangeSettings12 = new RandomizedRangeSettings(module, "SmartDCPSOnWall", 2.0, 4.0, 0.0, 20.0, 0, this.onWall);
      String str9 = "DCPSOnWall";
      RandomizedRangeSettings randomizedRangeSettings13 = (RandomizedRangeSettings)randomizedRangeSettings12.withDisplayNamePrefix(str9);
      SettingVisibilityCondition condition13 = () -> this.isSelected();
      this.smartDCPSOnWall = (RandomizedRangeSettings)randomizedRangeSettings13.withVisibility(condition13);
      RandomizedRangeSettings randomizedRangeSettings14 = (new RandomizedRangeSettings(module, "DCPSOnWallRecalculateTickDelay", 3.0, 8.0, 1.0, 40.0, 0, this.onWall)).withoutNotInARow();
      SettingVisibilityCondition condition14 = () -> this.isSelected();
      this.dcpsOnWallRecalculateTickDelay = (RandomizedRangeSettings)randomizedRangeSettings14.withVisibility(condition14);
      DoubleValue doubleValue5 = new DoubleValue(module, "On first Hit", true, this.cpsSettings);
      SettingVisibilityCondition condition15 = () -> this.isSelected();
      this.onFirstHit = doubleValue5.visibleWhen(condition15);
      RandomizedRangeSettings randomizedRangeSettings15 = new RandomizedRangeSettings(module, "SmartCPSFirstHit", 20.0, 20.0, 0.0, 40.0, 0, this.onFirstHit);
      String str10 = "CPSFirstHit";
      RandomizedRangeSettings randomizedRangeSettings16 = (RandomizedRangeSettings)randomizedRangeSettings15.withDisplayNamePrefix(str10);
      SettingVisibilityCondition condition16 = () -> this.isSelected();
      this.smartCPSFirstHit = (RandomizedRangeSettings)randomizedRangeSettings16.withVisibility(condition16);
      RandomizedRangeSettings randomizedRangeSettings17 = new RandomizedRangeSettings(module, "SmartCPSFirstHitRecalculateTickDelay", 3.0, 8.0, 0.0, 60.0, 0, this.onFirstHit);
      String str11 = "CPSFirstHitRecalculateTickDelay";
      RandomizedRangeSettings randomizedRangeSettings18 = ((RandomizedRangeSettings)randomizedRangeSettings17.withDisplayNamePrefix(str11)).withoutNotInARow();
      SettingVisibilityCondition condition17 = () -> this.isSelected();
      this.smartCPSFirstHitRecalculateTickDelay = (RandomizedRangeSettings)randomizedRangeSettings18.withVisibility(condition17);
      RandomizedRangeSettings randomizedRangeSettings19 = new RandomizedRangeSettings(module, "SmartDCPSFirstHit", 4.0, 5.0, 0.0, 20.0, 0, this.onFirstHit);
      String str12 = "DoubleClicksPerSecondFirstHit";
      randomizedRangeSettings19 = (RandomizedRangeSettings)randomizedRangeSettings19.withDisplayNamePrefix(str12);
      SettingVisibilityCondition condition18 = () -> this.isFirstHitDcpsVisible();
      this.smartDCPSFirstHit = (RandomizedRangeSettings)randomizedRangeSettings19.withVisibility(condition18);
      randomizedRangeSettings19 = new RandomizedRangeSettings(module, "SmartDCPSFirstHitRecalculateTickDelay", 3.0, 8.0, 1.0, 40.0, 0, this.onFirstHit);
      String str13 = "DCPSFirstHitRecalculateTickDelay";
      randomizedRangeSettings19 = ((RandomizedRangeSettings)randomizedRangeSettings19.withDisplayNamePrefix(str13)).withoutNotInARow();
      SettingVisibilityCondition condition19 = () -> this.isFirstHitDcpsRecalcDelayVisible();
      this.smartDCPSFirstHitRecalculateTickDelay = (RandomizedRangeSettings)randomizedRangeSettings19.withVisibility(condition19);
      DoubleValue doubleValue6 = new DoubleValue(module, "On Target", true, this.cpsSettings);
      SettingVisibilityCondition condition20 = () -> this.isOnTargetGroupVisible();
      this.onTarget = doubleValue6.visibleWhen(condition20);
      randomizedRangeSettings19 = new RandomizedRangeSettings(module, "SmartCPS", 17.0, 19.0, 1.0, 40.0, 0, this.onTarget);
      String str14 = "CPS";
      randomizedRangeSettings19 = (RandomizedRangeSettings)randomizedRangeSettings19.withDisplayNamePrefix(str14);
      SettingVisibilityCondition condition21 = () -> {
         return this.isSelected() && this.smartMode.getSelectedValue().equals("Recommended");
      };
      this.smartCPS = (RandomizedRangeSettings)randomizedRangeSettings19.withVisibility(condition21);
      randomizedRangeSettings19 = (new RandomizedRangeSettings(module, "CPSRecalculateTickDelay", 3.0, 8.0, 1.0, 40.0, 0, this.onTarget)).withoutNotInARow();
      SettingVisibilityCondition condition22 = () -> this.isRecommendedCpsRecalcDelayVisible();
      this.cpsRecalculateTickDelay = (RandomizedRangeSettings)randomizedRangeSettings19.withVisibility(condition22);
      randomizedRangeSettings19 = new RandomizedRangeSettings(module, "SmartDCPS", 4.0, 5.0, 0.0, 20.0, 0, this.onTarget);
      String str15 = "DoubleClicksPerSecond";
      randomizedRangeSettings19 = (RandomizedRangeSettings)randomizedRangeSettings19.withDisplayNamePrefix(str15);
      SettingVisibilityCondition condition23 = () -> this.isRecommendedDcpsVisible();
      this.smartDCPS = (RandomizedRangeSettings)randomizedRangeSettings19.withVisibility(condition23);
      randomizedRangeSettings19 = (new RandomizedRangeSettings(module, "DCPSRecalculateTickDelay", 3.0, 8.0, 1.0, 40.0, 0, this.onTarget)).withoutNotInARow();
      SettingVisibilityCondition condition24 = () -> this.isRecommendedDcpsRecalcDelayVisible();
      this.dcpsRecalculateTickDelay = (RandomizedRangeSettings)randomizedRangeSettings19.withVisibility(condition24);
      this.initHurtTimeSettings(module);
   }

   @Override
   public int computeClicks(EventClick event) {
      int n = 0;
      MovingObjectPosition objectMouseOver = mc.objectMouseOver;
      boolean flag = objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && objectMouseOver.entityHit instanceof EntityLivingBase;
      this.updateRecalcTimers();
      EntityLivingBase target = ((ClickModuleContext)this.parentModule).getTarget();
      if (flag) {
         int n2 = target != null ? target.hurtTime : 0;
         if (ClientManagers.moduleManager.smartClicking.isSuppressingClicks()) {
            n = 0;
         } else if (n2 != this.lastTargetHurtTime) {
            ActionRateAccumulator clickAccumulator = this.getClickAccumulator(n2);
            ClickModuleContext parentModule2 = (ClickModuleContext)this.parentModule;
            int n3 = n2 * 4;
            int cpsForHurtTimeIndex = this.getCpsForHurtTimeIndex(n3);
            int n4 = parentModule2.applyHitReduction(cpsForHurtTimeIndex);
            n = clickAccumulator.accumulate(n4);
            if (n > 0) {
               int n5 = n2 + 11;
               ActionRateAccumulator doubleClickAccumulator = this.getDoubleClickAccumulator(n5);
               ClickModuleContext parentModule3 = (ClickModuleContext)this.parentModule;
               int n6 = n2 * 4 + 2;
               int dcpsForHurtTimeIndex = this.getDcpsForHurtTimeIndex(n6);
               int n7 = parentModule3.applyHitReduction(dcpsForHurtTimeIndex);
               n += doubleClickAccumulator.accumulate(n7);
            }
         } else {
            TickCounter tickCounter = this.firstHitCpsRecalcCounter;
            int lastIntValue = this.smartCPSFirstHitRecalculateTickDelay.getLastIntValue();
            if (tickCounter.hasReached2(lastIntValue)) {
               this.firstHitCpsRecalcCounter.reset();
               this.smartCPSFirstHitRecalculateTickDelay.nextSecureValue();
               this.smartCPSFirstHit.nextSecureValue();
            }

            tickCounter = this.firstHitDcpsRecalcCounter;
            int lastIntValue2 = this.smartDCPSFirstHitRecalculateTickDelay.getLastIntValue();
            if (tickCounter.hasReached2(lastIntValue2)) {
               this.firstHitDcpsRecalcCounter.reset();
               this.smartDCPSFirstHitRecalculateTickDelay.nextSecureValue();
               this.smartDCPSFirstHit.nextSecureValue();
            }

            byte b = 22;
            ActionRateAccumulator clickAccumulator2 = this.getClickAccumulator(b);
            ClickModuleContext parentModule4 = (ClickModuleContext)this.parentModule;
            int lastIntValue3 = this.smartCPSFirstHit.getLastIntValue();
            int n8 = parentModule4.applyHitReduction(lastIntValue3);
            n = clickAccumulator2.accumulate(n8);
            if (n > 0) {
               byte b2 = 23;
               ActionRateAccumulator doubleClickAccumulator2 = this.getDoubleClickAccumulator(b2);
               ClickModuleContext parentModule5 = (ClickModuleContext)this.parentModule;
               int n9 = this.smartMode.getSelectedValue().equals("Recommended") ? this.smartDCPS.getLastIntValue() : this.smartDCPSFirstHit.getLastIntValue();
               int n10 = parentModule5.applyHitReduction(n9);
               n += doubleClickAccumulator2.accumulate(n10);
            }
         }

         this.consecutiveTargetClickTicks = n > 0 ? ++this.consecutiveTargetClickTicks : 0;
      } else if (objectMouseOver != null && objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.MISS) {
         TickCounter wallCpsRecalcCounter2 = this.wallCpsRecalcCounter;
         int lastIntValue4 = this.smartCPSOnWallRecalculateTickDelay.getLastIntValue();
         if (wallCpsRecalcCounter2.hasReached2(lastIntValue4)) {
            this.wallCpsRecalcCounter.reset();
            this.smartCPSOnWallRecalculateTickDelay.nextSecureValue();
            this.smartCPSOnWall.nextSecureValue();
         }

         wallCpsRecalcCounter2 = this.wallDcpsRecalcCounter;
         int lastIntValue5 = this.dcpsOnWallRecalculateTickDelay.getLastIntValue();
         if (wallCpsRecalcCounter2.hasReached2(lastIntValue5)) {
            this.wallDcpsRecalcCounter.reset();
            this.dcpsOnWallRecalculateTickDelay.nextSecureValue();
            this.smartDCPSOnWall.nextSecureValue();
         }

         byte b3 = 26;
         ActionRateAccumulator clickAccumulator3 = this.getClickAccumulator(b3);
         int lastIntValue6 = this.smartCPSOnWall.getLastIntValue();
         n = clickAccumulator3.accumulate(lastIntValue6);
         if (n > 0) {
            byte b4 = 27;
            ActionRateAccumulator doubleClickAccumulator3 = this.getDoubleClickAccumulator(b4);
            int lastIntValue7 = this.smartDCPSOnWall.getLastIntValue();
            n += doubleClickAccumulator3.accumulate(lastIntValue7);
         }
      } else {
         TickCounter airCpsRecalcCounter2 = this.airCpsRecalcCounter;
         int lastIntValue8 = this.smartCPSInAirRecalculateTickDelay.getLastIntValue();
         if (airCpsRecalcCounter2.hasReached2(lastIntValue8)) {
            this.airCpsRecalcCounter.reset();
            this.smartCPSInAirRecalculateTickDelay.nextSecureValue();
            this.smartCPSInAir.nextSecureValue();
         }

         airCpsRecalcCounter2 = this.airDcpsRecalcCounter;
         int lastIntValue9 = this.dcpsInAirRecalculateTickDelay.getLastIntValue();
         if (airCpsRecalcCounter2.hasReached2(lastIntValue9)) {
            this.airDcpsRecalcCounter.reset();
            this.dcpsInAirRecalculateTickDelay.nextSecureValue();
            this.smartDCPSInAir.nextSecureValue();
         }

         byte b5 = 24;
         ActionRateAccumulator clickAccumulator4 = this.getClickAccumulator(b5);
         int lastIntValue10 = this.smartCPSInAir.getLastIntValue();
         n = clickAccumulator4.accumulate(lastIntValue10);
         if (n > 0) {
            byte b6 = 25;
            ActionRateAccumulator doubleClickAccumulator4 = this.getDoubleClickAccumulator(b6);
            int lastIntValue11 = this.smartDCPSInAir.getLastIntValue();
            n += doubleClickAccumulator4.accumulate(lastIntValue11);
         }
      }

      if (target != null) {
         this.lastTargetHurtTime = target.hurtTime;
      }

      if (ClientManagers.moduleManager.smartClicking.isSuppressingClicks()) {
         n = 0;
      }

      return n;
   }

   private int getCpsForHurtTimeIndex(int n) {
      return this.smartMode.getSelectedValue().equals("Recommended") ? this.smartCPS.getLastIntValue() : this.hurtTimeRangeSettings[n].getLastIntValue();
   }

   private int getDcpsForHurtTimeIndex(int n) {
      return this.smartMode.getSelectedValue().equals("Recommended") ? this.smartDCPS.getLastIntValue() : this.hurtTimeRangeSettings[n].getLastIntValue();
   }

   private ActionRateAccumulator getClickAccumulator(int n) {
      String selectedValue = this.smartClickMode.getSelectedValue();
      switch (selectedValue) {
         case "Combined":
            return this.combinedClickAccumulator;
         case "Default":
            return n < 22 ? this.combinedClickAccumulator : this.separateRateAccumulators[n];
         case "Separate":
            return this.separateRateAccumulators[n];
         default:
            return this.combinedClickAccumulator;
      }
   }

   private ActionRateAccumulator getDoubleClickAccumulator(int n) {
      String selectedValue = this.smartDoubleClickModeS.getSelectedValue();
      switch (selectedValue) {
         case "Combined":
            return this.combinedDoubleClickAccumulator;
         case "Default":
            return n < 22 ? this.combinedDoubleClickAccumulator : this.separateRateAccumulators[n];
         case "Separate":
            return this.separateRateAccumulators[n];
         default:
            return this.combinedDoubleClickAccumulator;
      }
   }

   private void updateRecalcTimers() {
      if (this.smartMode.getSelectedValue().equals("Recommended")) {
         TickCounter targetCpsRecalcCounter2 = this.targetCpsRecalcCounter;
         int lastIntValue = this.cpsRecalculateTickDelay.getLastIntValue();
         if (targetCpsRecalcCounter2.hasReached2(lastIntValue)) {
            this.targetCpsRecalcCounter.reset();
            this.cpsRecalculateTickDelay.nextSecureValue();
            this.smartCPS.nextSecureValue();
         }

         targetCpsRecalcCounter2 = this.targetDcpsRecalcCounter;
         int lastIntValue2 = this.dcpsRecalculateTickDelay.getLastIntValue();
         if (targetCpsRecalcCounter2.hasReached2(lastIntValue2)) {
            this.targetDcpsRecalcCounter.reset();
            this.dcpsRecalculateTickDelay.nextSecureValue();
            this.smartDCPS.nextSecureValue();
         }
      } else {
         for (int i = 0; i < 22; i++) {
            int n = i * 2;
            TickCounter tickCounter = this.hurtTimeRecalcCounters[i];
            int lastIntValue3 = this.hurtTimeRangeSettings[n].getLastIntValue();
            if (tickCounter.hasReached2(lastIntValue3)) {
               this.hurtTimeRecalcCounters[i].reset();
               this.hurtTimeRangeSettings[n + 1].nextSecureValue();
               this.hurtTimeRangeSettings[n].nextSecureValue();
            }
         }
      }
   }

   @Override
   public void onTick2() {
      TickCounter[] hurtTimeRecalcCounters2 = this.hurtTimeRecalcCounters;
      int length = hurtTimeRecalcCounters2.length;

      for (int i = 0; i < length; i++) {
         TickCounter tickCounter = hurtTimeRecalcCounters2[i];
         tickCounter.increment();
      }

      this.airCpsRecalcCounter.increment();
      this.airDcpsRecalcCounter.increment();
      this.wallCpsRecalcCounter.increment();
      this.wallDcpsRecalcCounter.increment();
      this.firstHitCpsRecalcCounter.increment();
      this.firstHitDcpsRecalcCounter.increment();
      this.targetCpsRecalcCounter.increment();
      this.targetDcpsRecalcCounter.increment();
   }

   @Override
   public void updateEventSubscriptions() {
   }

   @Override
   public void onEnable() {
      this.consecutiveTargetClickTicks = 0;
      this.lastTargetHurtTime = 0;
   }

   @Override
   public void onDisable() {
      this.consecutiveTargetClickTicks = 0;
      this.lastTargetHurtTime = 0;
   }

   private void initHurtTimeSettings(Module module) {

      for (int i = 0; i < this.hurtTimeRecalcCounters.length; i++) {
         this.hurtTimeRecalcCounters[i] = new TickCounter();
      }

      for (int j = 0; j < this.separateRateAccumulators.length; j++) {
         this.separateRateAccumulators[j] = new ActionRateAccumulator(20, (double)j * 1.2 % 20.0);
      }

      for (int k = 0; k < 11; k++) {
         DoubleValue doubleValue = new DoubleValue(module, "HurtTime " + k, this.cpsSettings);
         SettingVisibilityCondition condition = () -> this.isHurtTimeGroupVisible();
         DoubleValue doubleValue2 = doubleValue.visibleWhen(condition);
         RandomizedRangeSettings[] hurtTimeRangeSettings2 = this.hurtTimeRangeSettings;
         int n = k * 4;
         RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(module, "SmartCPSHurt" + k, 17.0, 19.0, 1.0, 40.0, 0, doubleValue2);
         String str = "CPS";
         randomizedRangeSettings = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str);
         SettingVisibilityCondition condition2 = () -> this.isHurtCpsVisible();
         hurtTimeRangeSettings2[n] = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition2);
         RandomizedRangeSettings[] hurtTimeRangeSettings3 = this.hurtTimeRangeSettings;
         n = k * 4 + 1;
         randomizedRangeSettings = new RandomizedRangeSettings(module, "Hurt" + k + "SmartCPSRecalculateTickDelay", 3.0, 8.0, 1.0, 40.0, 0, doubleValue2);
         String str2 = "CPSRecalculateTickDelay";
         randomizedRangeSettings = ((RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str2)).withoutNotInARow();
         SettingVisibilityCondition condition3 = () -> this.isHurtCpsRecalcDelayVisible();
         hurtTimeRangeSettings3[n] = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition3);
         RandomizedRangeSettings[] hurtTimeRangeSettings4 = this.hurtTimeRangeSettings;
         n = k * 4 + 2;
         randomizedRangeSettings = new RandomizedRangeSettings(module, "SmartDCPSHurt" + k, 4.0, 5.0, 0.0, 20.0, 0, doubleValue2);
         String str3 = "DoubleClicksPerSecond";
         randomizedRangeSettings = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str3);
         SettingVisibilityCondition condition4 = () -> this.isHurtDcpsVisible();
         hurtTimeRangeSettings4[n] = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition4);
         RandomizedRangeSettings[] hurtTimeRangeSettings5 = this.hurtTimeRangeSettings;
         n = k * 4 + 3;
         randomizedRangeSettings = new RandomizedRangeSettings(module, "DCPSRecalculateTickDelayHurt" + k, 3.0, 8.0, 1.0, 40.0, 0, doubleValue2);
         String str4 = "DoubleClicksPerSecondRecalculateTickDelay";
         randomizedRangeSettings = ((RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str4)).withoutNotInARow();
         SettingVisibilityCondition condition5 = () -> this.isHurtDcpsRecalcDelayVisible();
         hurtTimeRangeSettings5[n] = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition5);
      }
   }

   private boolean isHurtDcpsRecalcDelayVisible() {
      return this.isSelected() && this.smartMode.getSelectedValue().equals("Overengineered");
   }

   private boolean isHurtDcpsVisible() {
      return this.isSelected() && this.smartMode.getSelectedValue().equals("Overengineered");
   }

   private boolean isHurtCpsRecalcDelayVisible() {
      return this.isSelected() && this.smartMode.getSelectedValue().equals("Overengineered");
   }

   private boolean isHurtCpsVisible() {
      return this.isSelected() && this.smartMode.getSelectedValue().equals("Overengineered");
   }

   private boolean isHurtTimeGroupVisible() {
      return this.isSelected() && this.smartMode.getSelectedValue().equals("Overengineered");
   }

   private boolean isRecommendedDcpsRecalcDelayVisible() {
      return this.isSelected() && this.smartMode.getSelectedValue().equals("Recommended");
   }

   private boolean isRecommendedDcpsVisible() {
      return this.isSelected() && this.smartMode.getSelectedValue().equals("Recommended");
   }

   private boolean isRecommendedCpsRecalcDelayVisible() {
      return this.isSelected() && this.smartMode.getSelectedValue().equals("Recommended");
   }

   private boolean isOnTargetGroupVisible() {
      return this.isSelected() && this.smartMode.getSelectedValue().equals("Recommended");
   }

   private boolean isFirstHitDcpsRecalcDelayVisible() {
      return this.isSelected() && this.smartMode.getSelectedValue().equals("Overengineered");
   }

   private boolean isFirstHitDcpsVisible() {
      return this.isSelected() && this.smartMode.getSelectedValue().equals("Overengineered");
   }

}
