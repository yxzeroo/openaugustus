package net.augustus.modules.combat;

import net.minecraft.util.MovingObjectPosition;
import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.events.EventOrientCamera;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.utils.combat.CombatOptimizationUtil;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.utils.combat.CombatTargetUtil;
import net.augustus.events.EventMoveInput;
import net.augustus.utils.TickCounter;
import net.augustus.events.listeners.OrientCameraListener;
import net.augustus.events.EventAttackEntity;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.AttackEntityListener;
import net.augustus.utils.UnaryFunction;
import net.augustus.settings.options.MoreKbModeOption;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class MoreKB extends Module implements LoadWorldListener, MoveInputListener, TickListener, OrientCameraListener, PacketListener, AttackEntityListener {
   private final MultiOptionSetting modes;
   private final DoubleValue modeSettings;
   private BooleanValue hideSneakAnimation;
   private final StringValue fov;
   private final BooleanValue ignoreEatingTargets;
   private final BooleanValue onlyAtSprinting;
   private final BooleanValue dontSprintResetIfYouAlreadyDidLegit;
   private final BooleanValue waitForDamage;
   private final DoubleValue timing;
   private final RandomizedRangeSettings sprintResetTime;
   private final RandomizedRangeSettings minimalSprintResetDelay;
   private final DoubleValue duration;
   private final RandomizedRangeSettings sprintResetTicks;
   private final DoubleValue doubleReset;
   private final BooleanValue doubleSprintReset;
   private final RandomizedRangeSettings doubleResetTiming;
   private final RandomizedRangeSettings doubleResetDelay;
   private final BooleanValue noDoubleSprintresetWhenReducingNeeded;
   private final DoubleValue fail;
   private final BooleanValue failSprintReset;
   private final RandomizedRangeSettings failDelay;
   private final RandomizedRangeSettings failDuration;
   private final BooleanValue onlyWhileKBItem;
   private final BooleanValue onlyWhenNotClicking;
   private final TickCounter tickCounter;
   private final TimeHelper timeHelper;
   private int remainingFailHits;
   private int remainingResetTicks;
   private int hitsSinceLastFail;
   private int ticksSinceHit;
   private boolean pendingSprintReset;
   private EntityLivingBase target;
   private boolean resettingSprint;
   private int ticksSinceLegitSneak;
   private boolean doubleResetPerformed;
   private boolean sentStartSprintingPacket;
   private boolean restartedSprintBeforeHit;
   private boolean legitResetDetected;
   private MoreKbModeOption activeMode;
   private int minDelayTicks;

   public MoreKB() {
       super("MoreKB", "MehrRückstoß", new Color(252, 73, 3), Categorys.COMBAT);
      this.modes = new MultiOptionSetting(this, "Modes", MoreKbModeOption.values());
      this.modeSettings = new DoubleValue(this, "ModeSettings", true);
      this.fov = new StringValue(this, "FOV", 180.0, 20.0, 180.0, 0);
      this.ignoreEatingTargets = new BooleanValue(this, "IgnoreEatingTargets", false);
      this.onlyAtSprinting = new BooleanValue(this, "OnlyAtSprinting", true);
      this.dontSprintResetIfYouAlreadyDidLegit = new BooleanValue(this, "DontSprintResetIfYouAlreadyDidLegit", false);
      this.waitForDamage = new BooleanValue(this, "WaitForDamage", false);
      this.timing = new DoubleValue(this, "Timing", true);
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "SprintResetTime", 3.0, 5.0, 0.0, 10.0, 0, this.timing)).withMaxInARow();
      double d = 30.0;
      double d2 = 15.0;
      double d3 = 10.0;
      double d4 = 0.0;
      double d5 = 7.0;
      double d6 = 1.0;
      randomizedRangeSettings = randomizedRangeSettings.withOutliers(d6, d5, d4, d3, d2, d);
      boolean flag = false;
      double d7 = 5.0;
      double d8 = 0.5;
      double d9 = 1.0;
      this.sprintResetTime = randomizedRangeSettings.withNormalDistribution(d9, d8, d7, flag);
      this.minimalSprintResetDelay = new RandomizedRangeSettings(this, "MinimalSprintResetDelay", 0.0, 0.0, 0.0, 20.0, 0, this.timing);
      this.duration = new DoubleValue(this, "Duration", true);
      randomizedRangeSettings = (new RandomizedRangeSettings(this, "SprintResetTicks", 1.0, 1.0, 1.0, 5.0, 0, this.duration)).withMaxInARow();
      double d10 = 3.0;
      double d11 = 0.0;
      double d12 = 5.0;
      double d13 = 1.0;
      double d14 = 2.0;
      double d15 = 2.0;
      randomizedRangeSettings = randomizedRangeSettings.withOutliers(d15, d14, d13, d12, d11, d10);
      String str = "SprintResetDuration";
      randomizedRangeSettings = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str);
      boolean flag2 = false;
      double d16 = 4.0;
      double d17 = 0.5;
      double d18 = 1.0;
      this.sprintResetTicks = randomizedRangeSettings.withNormalDistribution(d18, d17, d16, flag2);
      this.doubleReset = new DoubleValue(this, "DoubleReset", true);
      this.doubleSprintReset = new BooleanValue(this, "DoubleSprintReset", true, this.doubleReset);
      randomizedRangeSettings = new RandomizedRangeSettings(this, "DoubleResetTiming", 10.0, 14.0, 0.0, 20.0, 0, this.doubleReset);
      boolean flag3 = false;
      double d19 = 5.0;
      double d20 = 0.5;
      double d21 = 1.0;
      randomizedRangeSettings = randomizedRangeSettings.withNormalDistribution(d21, d20, d19, flag3);
      BooleanValue doubleSprintReset2 = this.doubleSprintReset;
      this.doubleSprintReset.getClass();
      BooleanValue booleanValue = doubleSprintReset2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.doubleResetTiming = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition);
      randomizedRangeSettings = (new RandomizedRangeSettings(this, "DoubleResetDelay", 10.0, 15.0, 0.0, 30.0, 0, this.doubleReset)).withoutNotInARow();
      double d22 = 15.0;
      double d23 = 10.0;
      double d24 = 30.0;
      double d25 = 0.0;
      double d26 = 20.0;
      double d27 = 5.0;
      randomizedRangeSettings = randomizedRangeSettings.withOutliers(d27, d26, d25, d24, d23, d22);
      doubleSprintReset2 = this.doubleSprintReset;
      this.doubleSprintReset.getClass();
      BooleanValue booleanValue2 = doubleSprintReset2;
      SettingVisibilityCondition condition2 = () -> booleanValue2.getValue();
      this.doubleResetDelay = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition2);
      this.noDoubleSprintresetWhenReducingNeeded = new BooleanValue(this, "No double sprintreset when reducing needed", false, this.doubleReset);
      this.fail = new DoubleValue(this, "Fail", true);
      this.failSprintReset = new BooleanValue(this, "FailSprintReset", true, this.fail);
      RandomizedRangeSettings randomizedRangeSettings2 = new RandomizedRangeSettings(this, "FailDelay", 15.0, 18.0, 0.0, 150.0, 0, this.fail);
      String str2 = "FailInterval";
      RandomizedRangeSettings randomizedRangeSettings3 = ((RandomizedRangeSettings)randomizedRangeSettings2.withDisplayNamePrefix(str2)).withoutNotInARow();
      BooleanValue failSprintReset2 = this.failSprintReset;
      this.failSprintReset.getClass();
      BooleanValue booleanValue3 = failSprintReset2;
      SettingVisibilityCondition condition3 = () -> booleanValue3.getValue();
      this.failDelay = (RandomizedRangeSettings)randomizedRangeSettings3.withVisibility(condition3);
      RandomizedRangeSettings randomizedRangeSettings4 = new RandomizedRangeSettings(this, "FailDuration", 1.0, 2.0, 1.0, 10.0, 0, this.fail);
      boolean flag4 = false;
      double d28 = 5.0;
      double d29 = 0.5;
      double d30 = 1.0;
      RandomizedRangeSettings randomizedRangeSettings5 = randomizedRangeSettings4.withNormalDistribution(d30, d29, d28, flag4);
      BooleanValue failSprintReset3 = this.failSprintReset;
      this.failSprintReset.getClass();
      BooleanValue booleanValue4 = failSprintReset3;
      SettingVisibilityCondition condition4 = () -> booleanValue4.getValue();
      this.failDuration = (RandomizedRangeSettings)randomizedRangeSettings5.withVisibility(condition4);
      this.onlyWhileKBItem = new BooleanValue(this, "OnlyWhileKBItem", false);
      BooleanValue booleanValue5 = new BooleanValue(this, "OnlyWhenNotClicking", true);
      BooleanValue onlyAtSprinting2 = this.onlyAtSprinting;
      this.onlyAtSprinting.getClass();
      BooleanValue booleanValue6 = onlyAtSprinting2;
      SettingVisibilityCondition condition5 = () -> booleanValue6.getValue();
      this.onlyWhenNotClicking = (BooleanValue)booleanValue5.withVisibility(condition5);
      this.tickCounter = new TickCounter();
      this.timeHelper = new TimeHelper();
      this.remainingFailHits = 0;
      this.remainingResetTicks = 0;
      this.hitsSinceLastFail = 0;
      this.ticksSinceHit = 0;
      this.pendingSprintReset = true;
      this.resettingSprint = false;
      this.ticksSinceLegitSneak = 0;
      this.doubleResetPerformed = false;
      this.sentStartSprintingPacket = false;
      this.restartedSprintBeforeHit = false;
      this.legitResetDetected = false;
      this.minDelayTicks = 0;

      for (MoreKbModeOption moreKbModeOption : MoreKbModeOption.values()) {
         DoubleValue modeSettings2 = this.modeSettings;
         MoreKbModeOption.initOptionSettings(moreKbModeOption, this, modeSettings2);
         if (moreKbModeOption == MoreKbModeOption.LEGIT_SNEAK) {
            this.hideSneakAnimation = new BooleanValue(this, "HideSneakAnimation", true, MoreKbModeOption.getSettingsGroup(moreKbModeOption));
         }
      }

      String str3 = "Only sprintresets if the player is holding an item with knockback enchantment";
      this.onlyWhileKBItem.setDescription(str3);
   }

   public static void initialize() {
   }

   @Override
   public void onEnable() {
      this.pendingSprintReset = true;
      this.resettingSprint = false;
      this.restartedSprintBeforeHit = false;
      this.sentStartSprintingPacket = false;
      this.doubleResetDelay.nextValue();
      this.doubleResetTiming.nextValue();
      this.legitResetDetected = false;
      this.activeMode = this.selectWeightedMode();
   }

   @Override
   public void onDisable() {
      this.target = null;
   }

   @Override
   public void updateEventSubscriptions() {
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (event.isSneak() || event.getMoveForward() != 1.0F) {
         this.legitResetDetected = true;
      }

      this.ticksSinceLegitSneak++;
      if (this.activeMode == MoreKbModeOption.LEGIT_SNEAK && this.remainingResetTicks > 0) {
         this.remainingResetTicks--;
         boolean flag = true;
         event.setSneak(flag);
         this.resettingSprint = true;
         this.ticksSinceLegitSneak = 0;
      }
   }

   @Override
   public void onTick2() {
      String name2 = this.activeMode.getName();
      this.setSuffix(name2);
      EntityLivingBase entity = this.findTarget();
      if (entity != null) {
         this.target = entity;
      }

      if (this.target != null && this.target.hurtTime == 10) {
         this.legitResetDetected = false;
      }

      if (mc.thePlayer.isBlocking()) {
         this.legitResetDetected = true;
      }

      if (this.legitResetDetected && this.dontSprintResetIfYouAlreadyDidLegit.getValue()) {
         this.doubleResetPerformed = false;
         this.pendingSprintReset = false;
         this.remainingResetTicks = 0;
      }

      if (mc.thePlayer.hurtTime == 10) {
         this.timeHelper.reset();
      }

      if (this.target != null && this.target.hurtTime == 10 && !this.isBacktrackTooFar() && this.isTargetAllowed()) {
         this.activeMode = this.selectWeightedMode();
         this.sentStartSprintingPacket = false;
         double d = (double)RotationUtil.getEntityLookAngleToPlayer(this.target);
         if (d <= this.fov.getValue() && (!this.onlyWhileKBItem.getValue() || EnchantmentHelper.getKnockbackModifier(mc.thePlayer) > 0)) {
            this.pendingSprintReset = !this.shouldFailSprintReset();
         }

         this.ticksSinceHit = 0;
         this.target = null;
         this.tickCounter.increment();
         if (this.remainingFailHits <= 0) {
            this.hitsSinceLastFail++;
         }

         this.doubleResetPerformed = false;
      }

      int n;
      exitBlock: {
         if (this.waitForDamage.getValue()) {
            long l = 500L;
            if (this.timeHelper.reached2(l)) {
               n = -1;
               break exitBlock;
            }
         }

         n = this.ticksSinceHit;
      }

      boolean isServerSprintState = mc.thePlayer.isServerSprintState();
      if (this.onlyWhenNotClicking.getValue()) {
         isServerSprintState = mc.thePlayer.isSprinting();
      }

      if (this.pendingSprintReset && n == this.sprintResetTime.getLastIntValue() && (!this.onlyAtSprinting.getValue() || isServerSprintState)) {
         this.sprintResetTime.nextSecureValue();
         this.remainingResetTicks = (int)this.sprintResetTicks.nextSecureValue();
         this.pendingSprintReset = false;
         if (ClientManagers.moduleManager.debug.isToggled() && ClientManagers.moduleManager.debug.devToggleA != null && ClientManagers.moduleManager.debug.devToggleA.getValue()) {
            String str = "Sprint rest count: " + n + " " + this.remainingResetTicks;
            ClientManagers.moduleManager.debug.sendChatMessage2(str);
         }
      }

      this.resettingSprint = this.remainingResetTicks > 0;
      if (this.remainingResetTicks > 0 && this.activeMode != MoreKbModeOption.LEGIT_SNEAK) {
         this.remainingResetTicks--;
         boolean flag = false;
         this.executeSprintReset(flag);
      }

      if ((!this.legitResetDetected || !this.dontSprintResetIfYouAlreadyDidLegit.getValue()) && this.doubleSprintReset.getValue() && !this.resettingSprint && n == this.doubleResetTiming.getLastIntValue()) {
         TickCounter tickCounter2 = this.tickCounter;
         int lastIntValue = this.doubleResetDelay.getLastIntValue();
         exitBlock2:
         if (tickCounter2.hasReached2(lastIntValue) && !this.doubleResetPerformed) {
            if (this.noDoubleSprintresetWhenReducingNeeded.getValue()) {
               if (this.target == null) {
                  break exitBlock2;
               }

               float rotationYaw = mc.thePlayer.rotationYaw;
               if (!CombatOptimizationUtil.shouldReduceSprintKnockback(this.target, rotationYaw)) {
                  break exitBlock2;
               }
            }

            boolean flag2 = true;
            this.executeSprintReset(flag2);
            this.doubleResetDelay.nextValue();
            this.doubleResetTiming.nextValue();
            this.tickCounter.reset();
            this.doubleResetPerformed = true;
         }
      }

      this.ticksSinceHit++;
   }

   private EntityLivingBase findTarget() {
      EntityLivingBase entity = null;
      if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
         entity = (EntityLivingBase)mc.objectMouseOver.entityHit;
      }

      return CombatTargetUtil.getActiveTargetOrDefault(entity);
   }

   private void executeSprintReset(boolean flag) {
      if (!flag && this.minDelayTicks < this.minimalSprintResetDelay.getLastIntValue()) {
         this.minDelayTicks++;
      } else {
         this.minimalSprintResetDelay.nextInt();
         UnaryFunction sprintResetAction = MoreKbModeOption.getSprintResetAction(this.activeMode);
         Boolean bool = flag;
         boolean flag2 = (Boolean)sprintResetAction.apply(bool);
         if (flag2) {
            this.remainingResetTicks = 0;
         }
      }
   }

   private boolean shouldFailSprintReset() {
      if (this.remainingFailHits > 0 && this.restartedSprintBeforeHit) {
         this.remainingFailHits++;
      }

      this.remainingFailHits--;
      boolean flag = this.failSprintReset.getValue() && this.hitsSinceLastFail >= this.failDelay.getLastIntValue();
      if (flag) {
         this.remainingFailHits = (int)this.failDuration.nextValue();
         this.hitsSinceLastFail = 0;
         this.failDelay.nextSecureValue();
      }

      return this.remainingFailHits > 0;
   }

   private boolean isBacktrackTooFar() {
      return ClientManagers.moduleManager.backTrack.isToggled()
         && ClientManagers.moduleManager.backTrack.improve.getValue()
         && ClientManagers.moduleManager.backTrack.isBacktracking()
         && mc.thePlayer.getDistance(this.target.realPosX, this.target.realPosY, this.target.realPosZ) > 3.0;
   }

   private boolean isTargetAllowed() {
      return !this.ignoreEatingTargets.getValue() || !this.target.isEating();
   }

   @Override
   public void onOrientCamera(EventOrientCamera event) {
      if (this.ticksSinceLegitSneak < 3 && this.hideSneakAnimation.getValue()) {
         float f = 1.62F;
         event.setEyeHeight(f);
      }
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.target = null;
   }

   @Override
   public void onAttackEntity(EventAttackEntity event) {
      Entity target2 = event.getTarget();
      if (target2 instanceof EntityLivingBase) {
         EntityLivingBase entity = (EntityLivingBase)target2;
         if (entity.hurtTime < 3) {
            this.restartedSprintBeforeHit = this.sentStartSprintingPacket;
            this.sentStartSprintingPacket = false;
         }
      }
   }

   @Override
   public void onPacket2(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof C0BPacketEntityAction) {
         C0BPacketEntityAction packet2 = (C0BPacketEntityAction)packet;
         C0BPacketEntityAction.Action action = packet2.getAction();
         if (action == C0BPacketEntityAction.Action.START_SPRINTING) {
            this.sentStartSprintingPacket = true;
         }
      }
   }

   private MoreKbModeOption selectWeightedMode() {
      int n = 0;
      MoreKbModeOption[] values = MoreKbModeOption.values();

      for (MoreKbModeOption moreKbModeOption : values) {
         if (moreKbModeOption.isSelected()) {
            StringValue chanceSetting = MoreKbModeOption.getChanceSetting(moreKbModeOption);
            boolean flag = true;
            int value = (int)chanceSetting.getValue(flag);
            n += value;
         }
      }

      int n2 = n - 1;
      byte b = 0;
      int n3 = DefaultRandomGenerator.instance.nextInt2(b, n2);
      n = 0;

      for (MoreKbModeOption moreKbModeOption2 : MoreKbModeOption.values()) {
         if (moreKbModeOption2.isSelected()) {
            StringValue chanceSetting2 = MoreKbModeOption.getChanceSetting(moreKbModeOption2);
            boolean flag2 = true;
            int value2 = (int)chanceSetting2.getValue(flag2);
            if (n3 >= n && n3 < n + value2) {
               return moreKbModeOption2;
            }

            n += value2;
         }
      }

      return MoreKbModeOption.LEGIT;
   }

}
