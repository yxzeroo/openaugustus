package net.augustus.modules.legit;

import java.awt.Color;
import java.util.function.BiFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.ClickListener;
import net.augustus.settings.WeaponFilterSettings;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.events.listeners.MouseMoveListener;
import net.augustus.utils.simulation.EntityMovementPredictor;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.settings.ModeSetting;
import net.augustus.modules.combat.helper.ClickModuleContext;
import net.augustus.settings.options.MoveHelperModeOption;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.modules.modes.clicking.SimpleClickPatternMode;
import net.augustus.modules.modes.clicking.DistributionClickPatternMode;
import net.augustus.modules.modes.clicking.SimpleV2ClickPatternMode;
import net.augustus.modules.modes.clicking.SmartClickPatternMode;
import net.augustus.modules.modes.clicking.DeviationClickPatternMode;
import net.augustus.modules.modes.clicking.RecordedClickPatternMode;
import net.augustus.modules.modes.clicking.AbstractClickPatternMode;
import net.augustus.modules.modes.clicking.AdvancedClickPatternMode;
import net.augustus.modules.modes.clicking.DragClickPatternMode;
import net.augustus.utils.rotation.RayTraceUtil;
import net.augustus.events.EventMouseMove;
import net.augustus.modules.combat.helper.ClickSpeedMonitor;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class AutoClicker extends Module implements ClickModuleContext, LoadWorldListener, ClickListener, MouseMoveListener, TickListener, PacketListener {
   public final ModeSetting mode;
   private final StringValue cpsLimiter;
   private final BooleanValue cpsLimiterIntave;
   private final BooleansSetting timing;
   private final DoubleValue timingValues;
   private final RandomizedRangeSettings prediction;
   private final RandomizedRangeSettings loseDelay;
   private final RandomizedRangeSettings endurance;
   private final RandomizedRangeSettings enduranceReduction;
   private final RandomizedRangeSettings delayClicksAfterClosingGuiScreen;
   private final BooleanValue reduceCPSIfYouCanNotHitATargett;
   public final BooleanValue critFix;
   public final BooleanValue critFixPacketCheck;
   private final BooleanValue onlyOnTarget;
   public final BooleanValue perfectHit;
   private final BooleanValue noLoopHit;
   private final BooleanValue attackFriends;
   private final BooleanValue longClick;
   private final BooleanValue breakBlocks;
   private final StringValue breakTickDelay;
   public final BooleanValue onlyOnClick;
   private final WeaponFilterSettings weaponFilterSettings;
   public boolean ignoreClickDelay;
   public EntityLivingBase currentTarget;
   private EntityLivingBase lastTarget;
   private final TimeHelper timeHelper;
   public final ClickSpeedMonitor clickSpeedMonitor;
   private int ticksSinceEntityHover;
   public boolean critConfirmed;
   private boolean firstClickPending;
   private boolean wasAttackKeyDown;
   private long lastTickTimeMs;
   private int enduranceTicks;
   private boolean enduranceExhausted;

   public AutoClicker() {
       super("AutoClicker", "SelbstständigerClicker", new Color(82, 186, 74), Categorys.LEGIT);
      BiFunction[] biFunctions = new BiFunction[]{
         (obj, obj2) -> new DeviationClickPatternMode((Module)obj, (DoubleValue)obj2),
         (obj3, obj4) -> new DragClickPatternMode((Module)obj3, (DoubleValue)obj4),
         (obj5, obj6) -> new SimpleClickPatternMode((Module)obj5, (DoubleValue)obj6),
         (obj7, obj8) -> new SimpleV2ClickPatternMode((Module)obj7, (DoubleValue)obj8),
         (obj9, obj10) -> new AdvancedClickPatternMode((Module)obj9, (DoubleValue)obj10),
         (obj11, obj12) -> new RecordedClickPatternMode((Module)obj11, (DoubleValue)obj12),
         (obj13, obj14) -> new SmartClickPatternMode((Module)obj13, (DoubleValue)obj14),
         (obj15, obj16) -> new DistributionClickPatternMode((Module)obj15, (DoubleValue)obj16)
      };
      this.mode = new ModeSetting(this, "Mode", "Smart", biFunctions);
      this.cpsLimiter = new StringValue(this, "CPSLimiter", 20.0, 5.0, 40.0, 0);
      this.cpsLimiterIntave = new BooleanValue(this, "CPSLimiterIntave", false);
      this.timing = new BooleansSetting(this, "Timing", "Always", new String[]{"Always", "Other"});
      DoubleValue doubleValue = new DoubleValue(this, "TimingValues", true);
      SettingVisibilityCondition condition = () -> this.isTimingValuesVisible();
      this.timingValues = doubleValue.visibleWhen(condition);
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "Prediction", 3.0, 5.0, 1.0, 10.0, 0, this.timingValues)).withoutNotInARow();
      SettingVisibilityCondition condition2 = () -> this.isPredictionVisible();
      this.prediction = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition2);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this, "LoseDelay", 3.0, 5.0, 1.0, 10.0, 0, this.timingValues)).withoutNotInARow();
      SettingVisibilityCondition condition3 = () -> this.isLoseDelayVisible();
      this.loseDelay = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition3);
      RandomizedRangeSettings randomizedRangeSettings3 = (new RandomizedRangeSettings(this, "Endurance", 90.0, 110.0, 1.0, 400.0, 0, this.timingValues)).withoutNotInARow();
      SettingVisibilityCondition condition4 = () -> {
         return this.timing.getSelectedValue().equals("Other");
      };
      this.endurance = (RandomizedRangeSettings)randomizedRangeSettings3.withVisibility(condition4);
      RandomizedRangeSettings randomizedRangeSettings4 = (new RandomizedRangeSettings(this, "EnduranceReduction", 5.0, 10.0, 1.0, 20.0, 0, this.timingValues)).withoutNotInARow();
      SettingVisibilityCondition condition5 = () -> this.isEnduranceReductionVisible();
      this.enduranceReduction = (RandomizedRangeSettings)randomizedRangeSettings4.withVisibility(condition5);
      this.delayClicksAfterClosingGuiScreen = (new RandomizedRangeSettings(this, "DelayClicksAfterClosingGuiScreen", 3.0, 5.0, 1.0, 20.0, 0)).withoutNotInARow();
      BooleanValue booleanValue = new BooleanValue(this, "ReduceCPSIfYouCanNotHitATargett", false);
      String str = "ReduceCPSIfYouCanNotHitATarget";
      this.reduceCPSIfYouCanNotHitATargett = (BooleanValue)booleanValue.withDisplayName(str);
      this.critFix = new BooleanValue(this, "CritFix", true);
      BooleanValue booleanValue2 = new BooleanValue(this, "CritFixPacketCheck", true);
      BooleanValue critFix2 = this.critFix;
      this.critFix.getClass();
      BooleanValue booleanValue3 = critFix2;
      SettingVisibilityCondition condition6 = () -> booleanValue3.getValue();
      this.critFixPacketCheck = (BooleanValue)booleanValue2.withVisibility(condition6);
      this.onlyOnTarget = new BooleanValue(this, "OnlyOnTarget", false);
      this.perfectHit = new BooleanValue(this, "PerfectHit", false);
      this.noLoopHit = new BooleanValue(this, "NoLoopHit", false);
      this.attackFriends = new BooleanValue(this, "AttackFriends", false);
      BooleanValue booleanValue4 = new BooleanValue(this, "LongClick", false);
      SettingVisibilityCondition condition7 = () -> this.isLongClickVisible();
      this.longClick = (BooleanValue)booleanValue4.withVisibility(condition7);
      this.breakBlocks = (BooleanValue)(new BooleanValue(this, "BreakBlocks", false)).markVisualOnly();
      StringValue stringValue = new StringValue(this, "BreakTickDelay", 5.0, 0.0, 20.0, 0);
      critFix2 = this.breakBlocks;
      this.breakBlocks.getClass();
      BooleanValue booleanValue5 = critFix2;
      SettingVisibilityCondition condition8 = () -> booleanValue5.getValue();
      this.breakTickDelay = (StringValue)stringValue.visibleWhen(condition8).markVisualOnly();
      this.onlyOnClick = (BooleanValue)(new BooleanValue(this, "OnlyOnClick", true)).markVisualOnly();
      this.weaponFilterSettings = new WeaponFilterSettings(this);
      this.ignoreClickDelay = false;
      this.currentTarget = null;
      this.lastTarget = null;
      this.timeHelper = new TimeHelper();
      this.clickSpeedMonitor = new ClickSpeedMonitor(60);
      this.ticksSinceEntityHover = 0;
      this.critConfirmed = false;
      this.firstClickPending = false;
      this.wasAttackKeyDown = false;
      this.lastTickTimeMs = 0L;
      this.enduranceTicks = 0;
      this.enduranceExhausted = false;
   }

   public AutoClicker(boolean flag) {
       super("AutoClicker");
      BiFunction[] biFunctions = new BiFunction[]{
         (obj, obj2) -> new DeviationClickPatternMode((Module)obj, (DoubleValue)obj2),
         (obj3, obj4) -> new DragClickPatternMode((Module)obj3, (DoubleValue)obj4),
         (obj5, obj6) -> new SimpleClickPatternMode((Module)obj5, (DoubleValue)obj6),
         (obj7, obj8) -> new SimpleV2ClickPatternMode((Module)obj7, (DoubleValue)obj8),
         (obj9, obj10) -> new AdvancedClickPatternMode((Module)obj9, (DoubleValue)obj10),
         (obj11, obj12) -> new RecordedClickPatternMode((Module)obj11, (DoubleValue)obj12),
         (obj13, obj14) -> new SmartClickPatternMode((Module)obj13, (DoubleValue)obj14),
         (obj15, obj16) -> new DistributionClickPatternMode((Module)obj15, (DoubleValue)obj16)
      };
      this.mode = new ModeSetting(this, "Mode", "Smart", biFunctions);
      this.cpsLimiter = new StringValue(this, "CPSLimiter", 20.0, 5.0, 40.0, 0);
      this.cpsLimiterIntave = new BooleanValue(this, "CPSLimiterIntave", false);
      this.timing = new BooleansSetting(this, "Timing", "Always", new String[]{"Always", "Other"});
      DoubleValue doubleValue = new DoubleValue(this, "TimingValues", true);
      SettingVisibilityCondition condition = () -> this.isTimingValuesVisible();
      this.timingValues = doubleValue.visibleWhen(condition);
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "Prediction", 3.0, 5.0, 1.0, 10.0, 0, this.timingValues)).withoutNotInARow();
      SettingVisibilityCondition condition2 = () -> this.isPredictionVisible();
      this.prediction = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition2);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this, "LoseDelay", 3.0, 5.0, 1.0, 10.0, 0, this.timingValues)).withoutNotInARow();
      SettingVisibilityCondition condition3 = () -> this.isLoseDelayVisible();
      this.loseDelay = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition3);
      RandomizedRangeSettings randomizedRangeSettings3 = (new RandomizedRangeSettings(this, "Endurance", 90.0, 110.0, 1.0, 400.0, 0, this.timingValues)).withoutNotInARow();
      SettingVisibilityCondition condition4 = () -> {
         return this.timing.getSelectedValue().equals("Other");
      };
      this.endurance = (RandomizedRangeSettings)randomizedRangeSettings3.withVisibility(condition4);
      RandomizedRangeSettings randomizedRangeSettings4 = (new RandomizedRangeSettings(this, "EnduranceReduction", 5.0, 10.0, 1.0, 20.0, 0, this.timingValues)).withoutNotInARow();
      SettingVisibilityCondition condition5 = () -> this.isEnduranceReductionVisible();
      this.enduranceReduction = (RandomizedRangeSettings)randomizedRangeSettings4.withVisibility(condition5);
      this.delayClicksAfterClosingGuiScreen = (new RandomizedRangeSettings(this, "DelayClicksAfterClosingGuiScreen", 3.0, 5.0, 1.0, 20.0, 0)).withoutNotInARow();
      BooleanValue booleanValue = new BooleanValue(this, "ReduceCPSIfYouCanNotHitATargett", false);
      String str = "ReduceCPSIfYouCanNotHitATarget";
      this.reduceCPSIfYouCanNotHitATargett = (BooleanValue)booleanValue.withDisplayName(str);
      this.critFix = new BooleanValue(this, "CritFix", true);
      BooleanValue booleanValue2 = new BooleanValue(this, "CritFixPacketCheck", true);
      BooleanValue critFix2 = this.critFix;
      this.critFix.getClass();
      BooleanValue booleanValue3 = critFix2;
      SettingVisibilityCondition condition6 = () -> booleanValue3.getValue();
      this.critFixPacketCheck = (BooleanValue)booleanValue2.withVisibility(condition6);
      this.onlyOnTarget = new BooleanValue(this, "OnlyOnTarget", false);
      this.perfectHit = new BooleanValue(this, "PerfectHit", false);
      this.noLoopHit = new BooleanValue(this, "NoLoopHit", false);
      this.attackFriends = new BooleanValue(this, "AttackFriends", false);
      BooleanValue booleanValue4 = new BooleanValue(this, "LongClick", false);
      SettingVisibilityCondition condition7 = () -> this.isLongClickVisible();
      this.longClick = (BooleanValue)booleanValue4.withVisibility(condition7);
      this.breakBlocks = (BooleanValue)(new BooleanValue(this, "BreakBlocks", false)).markVisualOnly();
      StringValue stringValue = new StringValue(this, "BreakTickDelay", 5.0, 0.0, 20.0, 0);
      critFix2 = this.breakBlocks;
      this.breakBlocks.getClass();
      BooleanValue booleanValue5 = critFix2;
      SettingVisibilityCondition condition8 = () -> booleanValue5.getValue();
      this.breakTickDelay = (StringValue)stringValue.visibleWhen(condition8).markVisualOnly();
      this.onlyOnClick = (BooleanValue)(new BooleanValue(this, "OnlyOnClick", true)).markVisualOnly();
      this.weaponFilterSettings = new WeaponFilterSettings(this);
      this.ignoreClickDelay = false;
      this.currentTarget = null;
      this.lastTarget = null;
      this.timeHelper = new TimeHelper();
      this.clickSpeedMonitor = new ClickSpeedMonitor(60);
      this.ticksSinceEntityHover = 0;
      this.critConfirmed = false;
      this.firstClickPending = false;
      this.wasAttackKeyDown = false;
      this.lastTickTimeMs = 0L;
      this.enduranceTicks = 0;
      this.enduranceExhausted = false;
   }

   @Override
   public void onEnable() {
      this.ticksSinceEntityHover = 0;
      this.critConfirmed = false;
      this.firstClickPending = true;
      this.lastTickTimeMs = System.currentTimeMillis();
      this.delayClicksAfterClosingGuiScreen.nextValue();
   }

   @Override
   public void onDisable() {
      this.currentTarget = null;
      this.lastTarget = null;
      this.enduranceTicks = 0;
      this.enduranceExhausted = false;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 7;
      this.toggleEventSubscription(b);
      byte b2 = 17;
      this.toggleEventSubscription(b2);
      byte b3 = 19;
      this.toggleEventSubscription(b3);
      byte b4 = 40;
      this.toggleEventSubscription(b4);
      byte b5 = 26;
      this.toggleEventSubscription(b5);
   }

   @Override
   public void onPacket(EventPacket event) {
      if (this.critFix.getValue() && this.critFixPacketCheck.getValue()) {
         Packet packet = event.getPacket();
         if (packet instanceof S0BPacketAnimation && mc.theWorld != null) {
            S0BPacketAnimation packet2 = (S0BPacketAnimation)packet;
            if (packet2.getAnimationType() != 4) {
               return;
            }

            Entity entityByID = mc.theWorld.getEntityByID(packet2.getEntityID());
            if (this.lastTarget != null && entityByID != null && entityByID.getEntityId() == this.lastTarget.getEntityId()) {
               this.critConfirmed = true;
            }
         }
      }
   }

   @Override
   public void onTick3() {
      int n = (int)Math.abs(this.lastTickTimeMs - System.currentTimeMillis());
      this.lastTickTimeMs = System.currentTimeMillis();
      AbstractClickPatternMode abstractSubMode = (AbstractClickPatternMode)this.mode.getAbstractSubMode();
      if (abstractSubMode instanceof RecordedClickPatternMode) {
         ((RecordedClickPatternMode)abstractSubMode).setTickDurationMs(n);
      }
   }

   @Override
   public void onMouseMove(EventMouseMove event) {
      AbstractClickPatternMode abstractSubMode = (AbstractClickPatternMode)this.mode.getAbstractSubMode();
      if (abstractSubMode instanceof RecordedClickPatternMode) {
         ((RecordedClickPatternMode)abstractSubMode).onMouseMove(event);
      }
   }

   @Override
   public void onClick(EventClick event) {
      this.currentTarget = this.resolveCurrentTarget();
      if (this.currentTarget != null) {
         this.lastTarget = this.currentTarget;
         this.timeHelper.reset();
      }

      long l = 2000L;
      if (this.timeHelper.reached2(l)) {
         this.lastTarget = null;
      }

      if (this.onlyOnClick.getValue() && (mc.gameSettings.keyBindAttack.isKeyDown() || mc.gameSettings.keyBindAttack.getPressTime() > 0) && !this.wasAttackKeyDown) {
         this.firstClickPending = true;
      }

      exitBlock: {
         this.wasAttackKeyDown = mc.gameSettings.keyBindAttack.isKeyDown() || mc.gameSettings.keyBindAttack.getPressTime() > 0;
         if (this.canClick()) {
            int lastIntValue = this.prediction.getLastIntValue();
            double d = 3.0;
            if (this.checkTimingAndEndurance(d, lastIntValue)) {
               if (!this.shouldBreakBlock()) {
                  this.enduranceTicks++;
                  if (!mc.thePlayer.isUsingItem()) {
                     int n = this.computeClickCount();
                     this.applyClicks(event, n);
                  }

                  boolean value = this.longClick.getValue();
                  event.setDelayBlockBreak(value);
                  if (!this.longClick.getValue()) {
                     boolean flag = false;
                     event.setLeftMouseDown(flag);
                  }

                  if (this.isInstantClickMode()) {
                     boolean flag2 = true;
                     event.setOverrideBlockBreak(flag2);
                     boolean ignoreClickDelay2 = this.ignoreClickDelay;
                     event.setBreakingBlock(ignoreClickDelay2);
                  }
               } else {
                  AbstractClickPatternMode abstractSubMode = (AbstractClickPatternMode)this.mode.getAbstractSubMode();
                  if (abstractSubMode instanceof DeviationClickPatternMode) {
                     DeviationClickPatternMode mode2 = (DeviationClickPatternMode)abstractSubMode;
                     mode2.resetNextClickTime();
                  } else if (abstractSubMode instanceof RecordedClickPatternMode) {
                     ((RecordedClickPatternMode)abstractSubMode).resetTiming();
                  }

                  boolean flag3 = true;
                  event.setLeftMouseDown(flag3);
               }
               break exitBlock;
            }
         }

         this.recoverEndurance();
         AbstractClickPatternMode abstractSubMode2 = (AbstractClickPatternMode)this.mode.getAbstractSubMode();
         if (abstractSubMode2 instanceof DeviationClickPatternMode) {
            DeviationClickPatternMode mode3 = (DeviationClickPatternMode)abstractSubMode2;
            mode3.resetNextClickTime();
         } else if (abstractSubMode2 instanceof RecordedClickPatternMode) {
            ((RecordedClickPatternMode)abstractSubMode2).resetTiming();
         }

         if (mc.currentScreen != null && mc.previousScreen != null) {
            this.delayClicksAfterClosingGuiScreen.nextValue();
         }
      }

      this.ticksSinceEntityHover++;
      if (this.mode.getAbstractSubMode() != null) {
         ((AbstractClickPatternMode)this.mode.getAbstractSubMode()).onTick2();
      }
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.currentTarget = null;
      this.lastTarget = null;
   }

   private void recoverEndurance() {
      boolean flag = true;
      this.enduranceTicks = Math.max(this.enduranceTicks - this.enduranceReduction.nextInt(flag), 0);
      if (this.enduranceTicks == 0) {
         this.enduranceExhausted = false;
      }
   }

   private int computeClickCount() {
      if (this.currentTarget != null && this.currentTarget.hurtTime == 10) {
         this.critConfirmed = false;
      }

      int modeClickCount = this.getModeClickCount();
      if (this.firstClickPending) {
         if (modeClickCount <= 0 && !this.onlyOnClick.getValue()) {
            modeClickCount = 1;
         }

         this.firstClickPending = false;
      }

      if (this.noLoopHit.getValue() && mc.remainingTicksInFrame != 0 && modeClickCount > 0) {
         modeClickCount = 0;
      }

      if (this.perfectHit.getValue()
         && mc.objectMouseOver != null
         && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY
         && mc.objectMouseOver.entityHit instanceof EntityLivingBase
         && !this.shouldReduceCpsForTarget()) {
         EntityLivingBase entityHit = (EntityLivingBase)mc.objectMouseOver.entityHit;
         if (entityHit.hurtTime == 0 || entityHit.hurtTime == 1) {
            modeClickCount = 1;
         }
      }

      if (ClientManagers.moduleManager.moveHelper.isToggled() && MoveHelperModeOption.HIT_SELECTION.isSelected() && ClientManagers.moduleManager.moveHelper.shouldHitSelect()) {
         modeClickCount = 0;
      } else if (modeClickCount == 0
         && ClientManagers.moduleManager.moveHelper.isToggled()
         && MoveHelperModeOption.HIT_SELECTION.isSelected()
         && !ClientManagers.moduleManager.moveHelper.shouldHitSelect()
         && ClientManagers.moduleManager.moveHelper.isPreviousShouldHitSelect()) {
         modeClickCount = 1;
      } else if (modeClickCount == 0 && this.critFix.getValue() && !this.critConfirmed && this.canCriticalHit()) {
         modeClickCount = 1;
      } else if (this.onlyOnTarget.getValue() && (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY)) {
         modeClickCount = 0;
      }

      if (this.isTargetingFriend()) {
         modeClickCount = 0;
      }

      int n = (int)((double)modeClickCount + EventClick.recentClicksBuffer.sum());
      if (this.cpsLimiterIntave.getValue() ? this.clickSpeedMonitor.isExceedsCpsLimit() : (double)n > this.cpsLimiter.getValue()) {
         modeClickCount = 0;
      }

      return modeClickCount;
   }

   private int getModeClickCount() {
      AbstractClickPatternMode abstractSubMode = (AbstractClickPatternMode)this.mode.getAbstractSubMode();
      if (abstractSubMode == null) {
         return 0;
      } else {
         Object obj = null;
         return abstractSubMode.computeClicks((EventClick)obj);
      }
   }

   private boolean isTargetingFriend() {
      if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
         Entity entityHit = mc.objectMouseOver.entityHit;
         if (ClientManagers.moduleManager.midClick.isFriend(entityHit) && !ClientManagers.moduleManager.midClick.autoUnfriendTriggered && !this.attackFriends.getValue()) {
            return true;
         }
      }

      return false;
   }

   private boolean canCriticalHit() {
      return mc.objectMouseOver != null
         && (
            ClientManagers.moduleManager.criticals.isToggled()
               || mc.thePlayer.fallDistance > 0.0F
                  && !mc.thePlayer.onGround
                  && !mc.thePlayer.isOnLadder()
                  && !mc.thePlayer.isInWater()
                  && !mc.thePlayer.isPotionActive(Potion.blindness)
                  && mc.thePlayer.ridingEntity == null
                  && mc.objectMouseOver.entityHit instanceof EntityLivingBase
         );
   }

   private boolean shouldBreakBlock() {
      return this.breakBlocks.getValue() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && (double)this.ticksSinceEntityHover > this.breakTickDelay.getValue();
   }

   private boolean shouldReduceCpsForTarget() {
      return this.reduceCPSIfYouCanNotHitATargett.getValue()
         && this.currentTarget != null
         && this.currentTarget.hurtTime == 0
         && System.currentTimeMillis() - this.currentTarget.lastDamageAttackTime > 75L
         && System.currentTimeMillis() - this.currentTarget.lastDamageAttackTime < 225L;
   }

   private void applyClicks(EventClick event, int n) {
      if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
         this.ticksSinceEntityHover = 0;
      }

      if (this.canCriticalHit() && n > 0) {
         this.critConfirmed = true;
         boolean flag = true;
         event.setCriticalHit(flag);
      }

      mc.gameSettings.keyBindAttack.setPressTime(0);
      event.addLeftClicks(n);
   }

   private boolean canClick() {
      return this.weaponFilterSettings.isHeldItemAllowed()
         && (!this.onlyOnClick.getValue() || mc.gameSettings.keyBindAttack.isKeyDown() || mc.gameSettings.keyBindAttack.isPressed())
         && mc.isNotInGui()
         && mc.getTicksWithoutScreen() >= this.delayClicksAfterClosingGuiScreen.getLastIntValue();
   }

   private boolean checkTimingAndEndurance(double d, int n) {
      if (!this.timing.getSelectedValue().equals("Other")) {
         return true;
      } else if (this.enduranceTicks <= this.endurance.getLastIntValue() && !this.enduranceExhausted) {
         if (mc.ticksLookingAtEntity == 1) {
            this.loseDelay.nextValue();
            this.prediction.nextValue();
         }

         if ((mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) && mc.ticksNotLookingAtEntity > this.loseDelay.getLastIntValue()) {
            if (this.enduranceTicks > this.endurance.getLastIntValue() / 2) {
               return false;
            } else {
               MovingObjectPosition hit = RayTraceUtil.rayTrace(
                  1.0F, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, 10.0, 6.0, 1.0
               );
               if (hit != null && hit.entityHit instanceof EntityLivingBase) {
                  EntityLivingBase entityHit = (EntityLivingBase)hit.entityHit;

                  for (int i = 1; i < n; i++) {
                     Vec3 vec = EntityMovementPredictor.extrapolateLinearHorizontal((EntityLivingBase)hit.entityHit, i);
                     AxisAlignedBB box = RotationUtil.createEntityBoxAt(entityHit, vec);
                     double[] doubles = PlayerUtil.predictPlayerPosition(i - 1, mc.thePlayer);
                     Vec3 vec2 = new Vec3(doubles[0], doubles[1] + (double)mc.thePlayer.getEyeHeight(), doubles[2]);
                     Vec3 vec3 = RotationUtil.clampPointToBox(vec2, box);
                     if (vec2.distanceTo(vec3) < d) {
                        return true;
                     }
                  }

                  return false;
               } else {
                  return false;
               }
            }
         } else {
            return true;
         }
      } else {
         if (!this.enduranceExhausted) {
            this.endurance.nextValue();
         }

         this.enduranceExhausted = true;
         return false;
      }
   }

   private EntityLivingBase resolveCurrentTarget() {
      if (ClientManagers.moduleManager.aimBot.isToggled() && ClientManagers.moduleManager.aimBot.target != null) {
         return ClientManagers.moduleManager.aimBot.target;
      } else {
         return mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit instanceof EntityLivingBase
            ? (EntityLivingBase)mc.objectMouseOver.entityHit
            : null;
      }
   }

   private boolean isInstantClickMode() {
      return ((AbstractClickPatternMode)this.mode.getAbstractSubMode()).simulatesMousePress();
   }

   @Override
   public EntityLivingBase getTarget() {
      return this.currentTarget;
   }

   @Override
   public int applyHitReduction(int n) {
      if (!this.shouldReduceCpsForTarget()) {
         return n;
      } else {
         if (ClientManagers.moduleManager.debug.devToggleF != null && ClientManagers.moduleManager.debug.devToggleF.getValue() && ClientManagers.moduleManager.debug.isToggled()) {
            String str = "Reduce hits";
            ClientManagers.moduleManager.debug.sendChatMessage2(str);
         }

         return 0;
      }
   }

   @Override
   public void setHoldingAttack(boolean flag) {
      this.ignoreClickDelay = flag;
   }

   @Override
   public boolean hasLandedCrit() {
      return this.critConfirmed;
   }

   @Override
   public boolean isCritFixEnabled() {
      return this.critFix.getValue();
   }

   private boolean isLongClickVisible() {
      return !this.isInstantClickMode();
   }

   private boolean isEnduranceReductionVisible() {
      return this.timing.getSelectedValue().equals("Other");
   }

   private boolean isLoseDelayVisible() {
      return this.timing.getSelectedValue().equals("Other");
   }

   private boolean isPredictionVisible() {
      return this.timing.getSelectedValue().equals("Other");
   }

   private boolean isTimingValuesVisible() {
      return !this.timing.getSelectedValue().equals("Always");
   }

}
