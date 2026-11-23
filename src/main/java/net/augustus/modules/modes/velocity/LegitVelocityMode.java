package net.augustus.modules.modes.velocity;

import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.settings.IntervalNoiseSetting;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.ChatUtil;
import net.augustus.utils.TickCounter;
import net.augustus.utils.combat.CombatTargetUtil;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.math.DoubleSampleWindow;
import net.augustus.utils.rotation.RotationUtil;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Velocity;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

public class LegitVelocityMode extends VelocityMode implements MoveInputListener {
   private final BooleanValue jumpInInv;
   public final BooleansSetting legitJumpDelayMode;
   private final RandomizedRangeSettings jumpInterval;
   private final RandomizedRangeSettings tickDelayBetweenJumps;
   public final StringValue jumpChanceLegit;
   public final IntervalNoiseSetting dynamicChance;
   private final DoubleValue legitJumpFailGroup;
   private final BooleanValue legitFailJump;
   private final RandomizedRangeSettings failJumpDelay;
   private final RandomizedRangeSettings inReachDistance;
   private final RandomizedRangeSettings targetInReachTickOffset;
   public final BooleanValue checkFallDamageLegit;
   public final StringValue fallDistanceLegit;
   public final StringValue fallDistanceBufferSizeLegit;
   public final BooleanValue notWhileSpeedLegit;
   public final BooleanValue notWhileJumpBoostLegit;
   public final BooleanValue debug;
   private int hitsSinceLastJump;
   private int targetInReachTicks;
   private int successfulJumpCount;
   private DoubleSampleWindow fallDistanceHistory;
   private final TickCounter tickCounter;

   public LegitVelocityMode(Velocity velocity, DoubleValue doubleValue) {
       super(velocity, doubleValue, "Legit");
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "JumpInInv", false, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.jumpInInv = (BooleanValue)booleanValue.withVisibility(condition);
      BooleansSetting booleansSetting = new BooleansSetting(
         this.parentModule, "LegitJumpDelayMode", "IntervalDelay", new String[]{"IntervalDelay", "Chance", "DynamicChance"}, this.modeSetting
      );
      String str = "JumpDelayMode";
      booleansSetting = (BooleansSetting)booleansSetting.withDisplayName(str);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.legitJumpDelayMode = (BooleansSetting)booleansSetting.withVisibility(condition2);
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(this.parentModule, "JumpInterval", 0.0, 0.0, 0.0, 5.0, 0, this.modeSetting);
      SettingVisibilityCondition condition3 = () -> this.isJumpIntervalVisible();
      RandomizedRangeSettings randomizedRangeSettings2 = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition3);
      boolean flag = false;
      double d = 30.0;
      double d2 = 0.0;
      double d3 = 5.0;
      double d4 = 0.0;
      double d5 = 3.0;
      double d6 = 0.0;
      RandomizedRangeSettings randomizedRangeSettings3 = randomizedRangeSettings2.withOutliers(d6, d5, d4, d3, d2, d, flag);
      boolean flag2 = false;
      double d7 = 5.0;
      double d8 = 0.5;
      double d9 = 1.0;
      this.jumpInterval = randomizedRangeSettings3.withNormalDistribution(d9, d8, d7, flag2);
      RandomizedRangeSettings randomizedRangeSettings4 = (new RandomizedRangeSettings(this.parentModule, "TickDelayBetweenJumps", 10.0, 10.0, 0.0, 100.0, 0, this.modeSetting)).withoutNotInARow();
      SettingVisibilityCondition condition4 = () -> this.isTickDelayBetweenJumpsVisible();
      this.tickDelayBetweenJumps = (RandomizedRangeSettings)randomizedRangeSettings4.withVisibility(condition4);
      StringValue stringValue = new StringValue(this.parentModule, "JumpChanceLegit", 50.0, 0.0, 100.0, 0, this.modeSetting);
      String str2 = "JumpChance";
      StringValue stringValue2 = stringValue.named(str2);
      SettingVisibilityCondition condition5 = () -> this.isJumpChanceVisible();
      this.jumpChanceLegit = stringValue2.visibleWhen(condition5);
      IntervalNoiseSetting intervalNoiseSetting = new IntervalNoiseSetting(this.parentModule, "Dynamic Chance", this.modeSetting, false);
      byte b = 1;
      double d10 = 69.0;
      double d11 = 67.0;
      double d12 = 100.0;
      double d13 = 0.0;
      IntervalNoiseSetting intervalNoiseSetting2 = intervalNoiseSetting.withInterval(d13, d12, d11, d10, b);
      SettingVisibilityCondition condition6 = () -> this.isDynamicChanceVisible();
      SettingVisibilityCondition condition7 = condition6;
      this.dynamicChance = intervalNoiseSetting2.visibleWhen(condition7);
      DoubleValue doubleValue2 = new DoubleValue(this.parentModule, "LegitJumpFailGroup", this.modeSetting);
      SettingVisibilityCondition condition8 = () -> this.isSelected();
      DoubleValue doubleValue3 = doubleValue2.visibleWhen(condition8);
      String str3 = "Fail Jump";
      this.legitJumpFailGroup = (DoubleValue)doubleValue3.withDisplayName(str3);
      BooleanValue booleanValue2 = new BooleanValue(this.parentModule, "LegitFailJump", false, this.legitJumpFailGroup);
      SettingVisibilityCondition condition9 = () -> this.isSelected();
      BooleanValue booleanValue3 = (BooleanValue)booleanValue2.withVisibility(condition9);
      String str4 = "Fail jump";
      this.legitFailJump = (BooleanValue)booleanValue3.withDisplayName(str4);
      RandomizedRangeSettings randomizedRangeSettings5 = new RandomizedRangeSettings(this.parentModule, "FailJumpDelay", 10.0, 20.0, 0.0, 100.0, 0, this.legitJumpFailGroup);
      BooleanValue legitFailJump2 = this.legitFailJump;
      this.legitFailJump.getClass();
      BooleanValue booleanValue4 = legitFailJump2;
      SettingVisibilityCondition condition10 = () -> booleanValue4.getValue();
      this.failJumpDelay = (RandomizedRangeSettings)randomizedRangeSettings5.withVisibility(condition10);
      RandomizedRangeSettings randomizedRangeSettings6 = (new RandomizedRangeSettings(this.parentModule, "In reach distance", 3.0, 3.0, 2.0, 4.0, 2, this.legitJumpFailGroup)).withoutNotInARow();
      BooleanValue legitFailJump3 = this.legitFailJump;
      this.legitFailJump.getClass();
      BooleanValue booleanValue5 = legitFailJump3;
      SettingVisibilityCondition condition11 = () -> booleanValue5.getValue();
      this.inReachDistance = (RandomizedRangeSettings)randomizedRangeSettings6.withVisibility(condition11);
      RandomizedRangeSettings randomizedRangeSettings7 = new RandomizedRangeSettings(this.parentModule, "Target in reach tick offset", 0.0, 0.0, 0.0, 20.0, 0, this.legitJumpFailGroup);
      BooleanValue legitFailJump4 = this.legitFailJump;
      this.legitFailJump.getClass();
      BooleanValue booleanValue6 = legitFailJump4;
      SettingVisibilityCondition condition12 = () -> booleanValue6.getValue();
      this.targetInReachTickOffset = (RandomizedRangeSettings)randomizedRangeSettings7.withVisibility(condition12);
      BooleanValue booleanValue7 = new BooleanValue(this.parentModule, "CheckFallDamageLegit", true, this.modeSetting);
      String str5 = "CheckFallDamage";
      BooleanValue booleanValue8 = (BooleanValue)booleanValue7.withDisplayName(str5);
      SettingVisibilityCondition condition13 = () -> this.isSelected();
      this.checkFallDamageLegit = (BooleanValue)booleanValue8.withVisibility(condition13);
      StringValue stringValue3 = new StringValue(this.parentModule, "FallDistanceLegit", 3.0, 1.0, 10.0, 2, this.modeSetting);
      String str6 = "FallDistance";
      StringValue stringValue4 = stringValue3.named(str6);
      SettingVisibilityCondition condition14 = () -> this.isFallDistanceVisible();
      this.fallDistanceLegit = stringValue4.visibleWhen(condition14);
      StringValue stringValue5 = new StringValue(this.parentModule, "FallDistanceBufferSizeLegit", 2.0, 1.0, 5.0, 0, this.modeSetting);
      String str7 = "FallDistanceBufferSize";
      StringValue stringValue6 = stringValue5.named(str7);
      SettingVisibilityCondition condition15 = () -> this.isFallDistanceBufferVisible();
      this.fallDistanceBufferSizeLegit = stringValue6.visibleWhen(condition15);
      BooleanValue booleanValue9 = new BooleanValue(this.parentModule, "NotWhileSpeedLegit", true, this.modeSetting);
      String str8 = "NotWhileSpeed";
      BooleanValue booleanValue10 = (BooleanValue)booleanValue9.withDisplayName(str8);
      SettingVisibilityCondition condition16 = () -> this.isSelected();
      this.notWhileSpeedLegit = (BooleanValue)booleanValue10.withVisibility(condition16);
      BooleanValue booleanValue11 = new BooleanValue(this.parentModule, "NotWhileJumpBoostLegit", true, this.modeSetting);
      String str9 = "NotWhileJumpBoost";
      BooleanValue booleanValue12 = (BooleanValue)booleanValue11.withDisplayName(str9);
      SettingVisibilityCondition condition17 = () -> this.isSelected();
      this.notWhileJumpBoostLegit = (BooleanValue)booleanValue12.withVisibility(condition17);
      BooleanValue booleanValue13 = new BooleanValue(this.parentModule, "Debug", false, this.modeSetting);
      String str10 = "Debug";
      BooleanValue booleanValue14 = (BooleanValue)booleanValue13.withDisplayName(str10);
      SettingVisibilityCondition condition18 = () -> this.isSelected();
      this.debug = (BooleanValue)booleanValue14.withVisibility(condition18);
      this.hitsSinceLastJump = 0;
      this.targetInReachTicks = 0;
      this.successfulJumpCount = 0;
      this.fallDistanceHistory = new DoubleSampleWindow(5);
      this.tickCounter = new TickCounter();
      this.dynamicChance.addDefaultLayer();
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 21;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
      this.resetJumpDelays();
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new LegitVelocityMode((Velocity)obj, (DoubleValue)obj2);
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
   }

   @Override
   public void onMoveInput2(EventMoveInput event) {
      int maxSize = this.fallDistanceHistory.getMaxSize();
      boolean flag = true;
      if (maxSize != (int)this.fallDistanceBufferSizeLegit.getValue(flag)) {
         boolean flag2 = true;
         this.fallDistanceHistory = new DoubleSampleWindow((int)this.fallDistanceBufferSizeLegit.getValue(flag2));
      }

      this.tickCounter.increment();
      if (!this.canApplyVelocity()) {
         double fallDistance = (double)mc.thePlayer.fallDistance;
         this.fallDistanceHistory.add(fallDistance);
      } else {
         if (this.debug.getValue()
            && !mc.thePlayer.onGround
            && mc.thePlayer.hurtTime >= 9
            && (!this.checkFallDamageLegit.getValue() || this.fallDistanceHistory.max() < this.fallDistanceLegit.getValue())) {
            Velocity parentModule2 = (Velocity)this.parentModule;
            String str = "Currently in air, not jumping even though i got hit tE=" + mc.thePlayer.ticksExisted;
            parentModule2.sendChatMessage2(str);
         }

         if (this.canPerformLegitJump()) {
            if (mc.thePlayer.hurtTime >= 9) {
               if (this.shouldJumpThisHit()) {
                  this.successfulJumpCount++;
                  if (this.debug.getValue()) {
                     Velocity parentModule3 = (Velocity)this.parentModule;
                     String str2 = "Jumped! tE=" + mc.thePlayer.ticksExisted;
                     parentModule3.sendChatMessage2(str2);
                  }

                  boolean flag3 = true;
                  event.setJump(flag3);
                  this.resetJumpDelays();
                  this.hitsSinceLastJump++;
                  return;
               }

               if (this.debug.getValue()) {
                  Velocity parentModule4 = (Velocity)this.parentModule;
                  String str3 = "No jump because of randomization tE=" + mc.thePlayer.ticksExisted;
                  parentModule4.sendChatMessage2(str3);
               }

               this.hitsSinceLastJump++;
            } else if (mc.thePlayer.hurtTime == 0) {
               if (CombatTargetUtil.getActiveTarget() != null) {
                  EntityLivingBase activeTarget = CombatTargetUtil.getActiveTarget();
                  EntityPlayerSP thePlayer = mc.thePlayer;
                  if (RotationUtil.getNearestPointFromEntity(activeTarget, thePlayer).distanceTo(activeTarget.getPositionEyes(1.0F)) <= this.inReachDistance.getLastValue()) {
                     this.targetInReachTicks++;
                     if (this.targetInReachTicks >= this.targetInReachTickOffset.getLastIntValue() && this.failJumpDelay.getLastIntValue() <= this.successfulJumpCount) {
                        if (this.debug.getValue()) {
                           Velocity parentModule5 = (Velocity)this.parentModule;
                           String str4 = "Jumping too early... tE=" + mc.thePlayer.ticksExisted;
                           parentModule5.sendChatMessage2(str4);
                        }

                        this.successfulJumpCount = 0;
                        this.failJumpDelay.nextInt();
                        boolean flag4 = true;
                        event.setJump(flag4);
                     }
                  } else {
                     this.targetInReachTicks = 0;
                  }
               } else {
                  this.targetInReachTicks = 0;
               }
            }
         } else {
            this.targetInReachTicks = 0;
         }

         double fallDistance2 = (double)mc.thePlayer.fallDistance;
         this.fallDistanceHistory.add(fallDistance2);
      }
   }

   private boolean canPerformLegitJump() {
      return mc.thePlayer.isSprinting()
         && (mc.isNotInGui() || this.jumpInInv.getValue())
         && (this.fallDistanceHistory.max() < this.fallDistanceLegit.getValue() || !this.checkFallDamageLegit.getValue())
         && !mc.thePlayer.isBurning()
         && mc.thePlayer.onGround
         && this.passesPotionChecks();
   }

   private boolean shouldJumpThisHit() {
      String selectedValue = this.legitJumpDelayMode.getSelectedValue();
      switch (selectedValue) {
         case "IntervalDelay":
            TickCounter tickCounter2 = this.tickCounter;
            int lastIntValue = this.tickDelayBetweenJumps.getLastIntValue();
            return tickCounter2.hasReached2(lastIntValue) && this.hitsSinceLastJump > this.jumpInterval.getLastIntValue();
         case "Chance":
            byte b = 99;
            byte b2 = 0;
            return (double)DefaultRandomGenerator.instance.nextInt2(b2, b) < this.jumpChanceLegit.getValue();
         case "DynamicChance":
            this.dynamicChance.update();
            double d = this.dynamicChance.sample();
            float f = 99.0F;
            float f2 = 0.0F;
            boolean flag = (double)DefaultRandomGenerator.instance.nextFloat(f2, f) < d;
            if (this.debug.getValue()) {
               ChatUtil.sendChat3("Chance: " + d + " jump=" + flag);
            }

            return flag;
         default:
            return false;
      }
   }

   private void resetJumpDelays() {
      this.tickDelayBetweenJumps.nextValue();
      this.jumpInterval.nextValue();
      this.tickCounter.reset();
      this.hitsSinceLastJump = 0;
   }

   private boolean passesPotionChecks() {
      return (!this.notWhileSpeedLegit.getValue() || !mc.thePlayer.isPotionActive(Potion.moveSpeed)) && (!this.notWhileJumpBoostLegit.getValue() || !mc.thePlayer.isPotionActive(Potion.jump));
   }

   private boolean isFallDistanceBufferVisible() {
      return this.isSelected() && this.checkFallDamageLegit.getValue();
   }

   private boolean isFallDistanceVisible() {
      return this.isSelected() && this.checkFallDamageLegit.getValue();
   }

   private boolean isDynamicChanceVisible() {
      return this.isSelected() && this.legitJumpDelayMode.getSelectedValue().equals("DynamicChance");
   }

   private boolean isJumpChanceVisible() {
      return this.isSelected() && this.legitJumpDelayMode.getSelectedValue().equals("Chance");
   }

   private boolean isTickDelayBetweenJumpsVisible() {
      return this.isSelected() && this.legitJumpDelayMode.getSelectedValue().equals("IntervalDelay");
   }

   private boolean isJumpIntervalVisible() {
      return this.isSelected() && this.legitJumpDelayMode.getSelectedValue().equals("IntervalDelay");
   }

}
