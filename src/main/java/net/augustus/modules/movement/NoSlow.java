package net.augustus.modules.movement;

import java.awt.Color;
import java.util.ArrayList;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.ClickListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.events.listeners.SprintListener;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.MotionListener;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.events.EventClick;
import net.augustus.settings.Setting;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.events.EventPacketDelay;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.events.EventSlowDown;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.utils.world.BlockUtil;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.SlowDownListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.events.EventMoveInput;
import net.augustus.events.EventAttackEntity;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.AttackEntityListener;
import net.augustus.settings.options.NoSlowBowOption;
import net.augustus.events.EventSprint;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.options.NoSlowRestItemOption;
import net.augustus.events.EventMotion;
import net.augustus.settings.options.NoSlowSwordOption;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlow
   extends Module
   implements ClickListener,
   MoveInputListener,
   SlowDownListener,
   UpdateListener,
   MotionListener,
   PacketListener,
   AttackEntityListener,
   SprintListener,
   PacketDelayListener,
   TickListener {
   public final BooleanValue onlyWhileMove;
   public final BooleanValue startSlow;
   public final BooleanValue enableIntervals;
   public final BooleanValue switchOnlyEmptySlot;
   public final BooleanValue switchGomme;
   public final BooleansSetting switchMode;
   public final BooleansSetting slotMode;
   public final BooleansSetting spamMode;
   public final BooleansSetting mode;
   private final DoubleValue sword;
   private final MultiOptionSetting sword2;
   public final StringValue swordForward;
   public final StringValue swordStrafe;
   public final BooleansSetting swordSlowdownInterval;
   public final StringValue swordSlowdownIntervalAir;
   public final StringValue swordSlowdownIntervalGround;
   public final StringValue timerSword;
   public final StringValue swordSneakForward;
   public final StringValue swordSneakStrafe;
   public final StringValue swordMaxBlinkTicks;
   public final BooleanValue swordNullItem;
   public final BooleanValue swordEditFacing;
   public final StringValue swordEnumFacingID;
   public final BooleanValue swordCustomBlockPos;
   public final RandomizedRangeSettings swordCustomBlockPosX;
   public final RandomizedRangeSettings swordCustomBlockPosY;
   public final RandomizedRangeSettings swordCustomBlockPosZ;
   public final BooleanValue swordCustomFacing;
   public final RandomizedRangeSettings swordCustomFacingX;
   public final RandomizedRangeSettings swordCustomFacingY;
   public final RandomizedRangeSettings swordCustomFacingZ;
   private final DoubleValue bow;
   public final MultiOptionSetting bow2;
   public final StringValue bowForward;
   public final StringValue bowStrafe;
   public final BooleansSetting bowSlowdownInterval;
   public final StringValue bowSlowdownIntervalAir;
   public final StringValue bowSlowdownIntervalGround;
   public final StringValue timerBow;
   public final StringValue bowSneakForward;
   public final StringValue bowSneakStrafe;
   public final BooleanValue bowNullItem;
   public final BooleanValue bowEditFacing;
   public final StringValue bowEnumFacingID;
   public final BooleanValue bowCustomBlockPos;
   public final RandomizedRangeSettings bowCustomBlockPosX;
   public final RandomizedRangeSettings bowCustomBlockPosY;
   public final RandomizedRangeSettings bowCustomBlockPosZ;
   public final BooleanValue bowCustomFacing;
   public final RandomizedRangeSettings bowCustomFacingX;
   public final RandomizedRangeSettings bowCustomFacingY;
   public final RandomizedRangeSettings bowCustomFacingZ;
   private final DoubleValue rest;
   public final MultiOptionSetting rest2;
   public final StringValue restForward;
   public final StringValue restStrafe;
   public final BooleansSetting restSlowdownInterval;
   public final StringValue restSlowdownIntervalAir;
   public final StringValue restSlowdownIntervalGround;
   public final StringValue timerRest;
   public final StringValue restSneakForward;
   public final StringValue restSneakStrafe;
   public final BooleanValue restNullItem;
   public final BooleanValue restEditFacing;
   public final StringValue restEnumFacingID;
   public final BooleanValue restCustomBlockPos;
   public final RandomizedRangeSettings restCustomBlockPosX;
   public final RandomizedRangeSettings restCustomBlockPosY;
   public final RandomizedRangeSettings restCustomBlockPosZ;
   public final BooleanValue restCustomFacing;
   public final RandomizedRangeSettings restCustomFacingX;
   public final RandomizedRangeSettings restCustomFacingY;
   public final RandomizedRangeSettings restCustomFacingZ;
   private final BooleanValue inputForwardLegit;
   private final StringValue inputForward;
   private final BooleanValue inputStrafeLegit;
   private final StringValue inputStrafe;
   private final BooleanValue inputJumpLegit;
   private final BooleanValue inputJump;
   private final BooleanValue inputSneakLegit;
   private final BooleanValue inputSneak;
   private final TimeHelper timeHelper;
   private int bugBypassState;
   private ItemStack lastUsedItemStack;
   private boolean shouldSendUnToggleRelease;
   private boolean sneakInput;
   private boolean lastSneakInput;
   private float forwardInput;
   private float lastForwardInput;
   private int swordBlinkTicks;
   private int usingTicks;
   private int airUsingTicks;
   private int groundUsingTicks;
   private boolean grimRestItemActive;
   private transient boolean blink2Delaying;
   private transient int blink2Ticks;
   private boolean timerModified;
   public boolean pendingCustomSwitchPlace;

   public NoSlow() {
       super("NoSlow", "NichtLangsam", new Color(10, 40, 53), Categorys.MOVEMENT);
      this.onlyWhileMove = new BooleanValue(this, "OnlyWhileMove", true);
      this.startSlow = new BooleanValue(this, "StartSlow", false);
      this.enableIntervals = new BooleanValue(this, "EnableIntervals", false);
      this.switchOnlyEmptySlot = new BooleanValue(this, "SwitchOnlyEmptySlot", false);
      this.switchGomme = new BooleanValue(this, "SwitchGomme", false);
      this.switchMode = new BooleansSetting(
         this, "SwitchMode", "PreAttack", new String[]{"PreAttack", "PostAttack", "PrePosition", "PostPosition", "PrePostPosition"}
      );
      this.slotMode = new BooleansSetting(this, "SlotMode", "PreAttack", new String[]{"PreAttack", "PostAttack", "PrePosition", "PostPosition"});
      this.spamMode = new BooleansSetting(this, "SpamMode", "PostPosition", new String[]{"PreAttack", "PostAttack", "PrePosition", "PostPosition"});
      this.mode = new BooleansSetting(this, "Mode", "Legit", new String[]{"Legit", "LegitMotion", "AllDirection", "AllDirectionMotion"});
      this.sword = new DoubleValue(this, "Sword");
      this.sword2 = new MultiOptionSetting(this, "Sword", NoSlowSwordOption.values(), this.sword);
      StringValue stringValue = new StringValue(this, "SwordForward", 0.2, 0.2, 1.0, 2, this.sword);
      SettingVisibilityCondition condition = NoSlowSwordOption.SLOWDOWN::isSelected;
      this.swordForward = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(this, "SwordStrafe", 0.2, 0.2, 1.0, 2, this.sword);
      SettingVisibilityCondition condition2 = NoSlowSwordOption.SLOWDOWN::isSelected;
      this.swordStrafe = stringValue2.visibleWhen(condition2);
      BooleansSetting booleansSetting = new BooleansSetting(
         this, "SwordSlowdownInterval", "None", new String[]{"None", "TicksToNextSlowdownApply", "TicksToNextDelayApply"}, this.sword
      );
      String str = "SlowdownInterval";
      BooleansSetting booleansSetting2 = (BooleansSetting)booleansSetting.withDisplayName(str);
      SettingVisibilityCondition condition3 = () -> {
         Setting[] cachedSettings = Fly.getCachedSettings();
         return NoSlowSwordOption.SLOWDOWN.isSelected() && this.enableIntervals.getValue();
      };
      this.swordSlowdownInterval = (BooleansSetting)booleansSetting2.withVisibility(condition3);
      StringValue stringValue3 = new StringValue(this, "SwordSlowdownIntervalAir", 0.0, 1.0, 10.0, 0, this.sword);
      String str2 = "SlowdownIntervalAir";
      StringValue stringValue4 = stringValue3.named(str2);
      SettingVisibilityCondition condition4 = () -> {
         Setting[] cachedSettings2 = Fly.getCachedSettings();
         return !this.swordSlowdownInterval.getSelectedValue().equals("None") && NoSlowSwordOption.SLOWDOWN.isSelected() && this.enableIntervals.getValue();
      };
      this.swordSlowdownIntervalAir = stringValue4.visibleWhen(condition4);
      StringValue stringValue5 = new StringValue(this, "SwordSlowdownIntervalGround", 0.0, 1.0, 10.0, 0, this.sword);
      String str3 = "SlowdownIntervalGround";
      StringValue stringValue6 = stringValue5.named(str3);
      SettingVisibilityCondition condition5 = () -> this.isSwordSlowdownIntervalGroundVisible();
      this.swordSlowdownIntervalGround = stringValue6.visibleWhen(condition5);
      StringValue stringValue7 = new StringValue(this, "TimerSword", 0.2, 0.1, 2.0, 2, this.sword);
      SettingVisibilityCondition condition6 = NoSlowSwordOption.TIMER::isSelected;
      this.timerSword = stringValue7.visibleWhen(condition6);
      StringValue stringValue8 = new StringValue(this, "SwordSneakForward", 0.3, 0.3, 1.0, 2, this.sword);
      SettingVisibilityCondition condition7 = NoSlowSwordOption.SNEAK::isSelected;
      this.swordSneakForward = stringValue8.visibleWhen(condition7);
      StringValue stringValue9 = new StringValue(this, "SwordSneakStrafe", 0.3, 0.3, 1.0, 2, this.sword);
      SettingVisibilityCondition condition8 = NoSlowSwordOption.SNEAK::isSelected;
      this.swordSneakStrafe = stringValue9.visibleWhen(condition8);
      StringValue stringValue10 = new StringValue(this, "SwordMaxBlinkTicks", 1.0, 0.0, 20.0, 0, this.sword);
      SettingVisibilityCondition condition9 = NoSlowSwordOption.BLINK2::isSelected;
      this.swordMaxBlinkTicks = stringValue10.visibleWhen(condition9);
      BooleanValue booleanValue = new BooleanValue(this, "SwordNullItem", false);
      SettingVisibilityCondition condition10 = NoSlowSwordOption.INVALID_PACKET::isSelected;
      this.swordNullItem = (BooleanValue)booleanValue.withVisibility(condition10);
      BooleanValue booleanValue2 = new BooleanValue(this, "SwordEditFacing", false);
      SettingVisibilityCondition condition11 = NoSlowSwordOption.INVALID_PACKET::isSelected;
      this.swordEditFacing = (BooleanValue)booleanValue2.withVisibility(condition11);
      StringValue stringValue11 = new StringValue(this, "SwordEnumFacingID", 0.0, 0.0, 5.0, 0);
      SettingVisibilityCondition condition12 = () -> this.isSwordEnumFacingIdVisible();
      this.swordEnumFacingID = stringValue11.visibleWhen(condition12);
      BooleanValue booleanValue3 = new BooleanValue(this, "SwordCustomBlockPos", true);
      SettingVisibilityCondition condition13 = NoSlowSwordOption.INVALID_PACKET::isSelected;
      this.swordCustomBlockPos = (BooleanValue)booleanValue3.withVisibility(condition13);
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "SwordCustomBlockPosX", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition14 = () -> this.isSwordCustomBlockPosXVisible();
      this.swordCustomBlockPosX = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition14);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this, "SwordCustomBlockPosY", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition15 = () -> this.isSwordCustomBlockPosYVisible();
      this.swordCustomBlockPosY = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition15);
      RandomizedRangeSettings randomizedRangeSettings3 = (new RandomizedRangeSettings(this, "SwordCustomBlockPosZ", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition16 = () -> this.isSwordCustomBlockPosZVisible();
      this.swordCustomBlockPosZ = (RandomizedRangeSettings)randomizedRangeSettings3.withVisibility(condition16);
      BooleanValue booleanValue4 = new BooleanValue(this, "SwordCustomFacing", false);
      SettingVisibilityCondition condition17 = NoSlowSwordOption.INVALID_PACKET::isSelected;
      this.swordCustomFacing = (BooleanValue)booleanValue4.withVisibility(condition17);
      RandomizedRangeSettings randomizedRangeSettings4 = (new RandomizedRangeSettings(this, "SwordCustomFacingX", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition18 = () -> this.isSwordCustomFacingXVisible();
      this.swordCustomFacingX = (RandomizedRangeSettings)randomizedRangeSettings4.withVisibility(condition18);
      RandomizedRangeSettings randomizedRangeSettings5 = (new RandomizedRangeSettings(this, "SwordCustomFacingY", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition19 = () -> this.isSwordCustomFacingYVisible();
      this.swordCustomFacingY = (RandomizedRangeSettings)randomizedRangeSettings5.withVisibility(condition19);
      RandomizedRangeSettings randomizedRangeSettings6 = (new RandomizedRangeSettings(this, "SwordCustomFacingZ", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition20 = () -> this.isSwordCustomFacingZVisible();
      this.swordCustomFacingZ = (RandomizedRangeSettings)randomizedRangeSettings6.withVisibility(condition20);
      this.bow = new DoubleValue(this, "Bow");
      this.bow2 = new MultiOptionSetting(this, "Bow", NoSlowBowOption.values(), this.bow);
      StringValue stringValue12 = new StringValue(this, "BowForward", 0.2, 0.2, 1.0, 2, this.bow);
      SettingVisibilityCondition condition21 = NoSlowBowOption.SLOWDOWN::isSelected;
      this.bowForward = stringValue12.visibleWhen(condition21);
      StringValue stringValue13 = new StringValue(this, "BowStrafe", 0.2, 0.2, 1.0, 2, this.bow);
      SettingVisibilityCondition condition22 = NoSlowBowOption.SLOWDOWN::isSelected;
      this.bowStrafe = stringValue13.visibleWhen(condition22);
      BooleansSetting booleansSetting3 = new BooleansSetting(
         this, "BowSlowdownInterval", "None", new String[]{"None", "TicksToNextSlowdownApply", "TicksToNextDelayApply"}, this.bow
      );
      String str4 = "SlowdownInterval";
      BooleansSetting booleansSetting4 = (BooleansSetting)booleansSetting3.withDisplayName(str4);
      SettingVisibilityCondition condition23 = () -> this.isBowSlowdownIntervalVisible();
      this.bowSlowdownInterval = (BooleansSetting)booleansSetting4.withVisibility(condition23);
      StringValue stringValue14 = new StringValue(this, "BowSlowdownIntervalAir", 0.0, 1.0, 10.0, 0, this.bow);
      String str5 = "SlowdownIntervalAir";
      StringValue stringValue15 = stringValue14.named(str5);
      SettingVisibilityCondition condition24 = () -> this.isBowSlowdownIntervalAirVisible();
      this.bowSlowdownIntervalAir = stringValue15.visibleWhen(condition24);
      StringValue stringValue16 = new StringValue(this, "BowSlowdownIntervalGround", 0.0, 1.0, 10.0, 0, this.bow);
      String str6 = "SlowdownIntervalGround";
      StringValue stringValue17 = stringValue16.named(str6);
      SettingVisibilityCondition condition25 = () -> this.isBowSlowdownIntervalGroundVisible();
      this.bowSlowdownIntervalGround = stringValue17.visibleWhen(condition25);
      StringValue stringValue18 = new StringValue(this, "TimerBow", 0.2, 0.1, 2.0, 2, this.bow);
      SettingVisibilityCondition condition26 = NoSlowBowOption.TIMER::isSelected;
      this.timerBow = stringValue18.visibleWhen(condition26);
      StringValue stringValue19 = new StringValue(this, "BowSneakForward", 0.3, 0.3, 1.0, 2, this.bow);
      SettingVisibilityCondition condition27 = NoSlowBowOption.SNEAK::isSelected;
      this.bowSneakForward = stringValue19.visibleWhen(condition27);
      Fly.getCachedSettings();
      StringValue stringValue20 = new StringValue(this, "BowSneakStrafe", 0.3, 0.3, 1.0, 2, this.bow);
      SettingVisibilityCondition condition28 = NoSlowBowOption.SNEAK::isSelected;
      this.bowSneakStrafe = stringValue20.visibleWhen(condition28);
      BooleanValue booleanValue5 = new BooleanValue(this, "BowNullItem", false);
      SettingVisibilityCondition condition29 = NoSlowBowOption.INVALID_PACKET::isSelected;
      this.bowNullItem = (BooleanValue)booleanValue5.withVisibility(condition29);
      BooleanValue booleanValue6 = new BooleanValue(this, "BowEditFacing", false);
      SettingVisibilityCondition condition30 = NoSlowBowOption.INVALID_PACKET::isSelected;
      this.bowEditFacing = (BooleanValue)booleanValue6.withVisibility(condition30);
      stringValue20 = new StringValue(this, "BowEnumFacingID", 0.0, 0.0, 5.0, 0);
      SettingVisibilityCondition condition31 = () -> this.isBowEnumFacingIdVisible();
      this.bowEnumFacingID = stringValue20.visibleWhen(condition31);
      BooleanValue booleanValue7 = new BooleanValue(this, "BowCustomBlockPos", true);
      SettingVisibilityCondition condition32 = NoSlowBowOption.INVALID_PACKET::isSelected;
      this.bowCustomBlockPos = (BooleanValue)booleanValue7.withVisibility(condition32);
      RandomizedRangeSettings randomizedRangeSettings7 = (new RandomizedRangeSettings(this, "BowCustomBlockPosX", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition33 = () -> this.isBowCustomBlockPosXVisible();
      this.bowCustomBlockPosX = (RandomizedRangeSettings)randomizedRangeSettings7.withVisibility(condition33);
      RandomizedRangeSettings randomizedRangeSettings8 = (new RandomizedRangeSettings(this, "BowCustomBlockPosY", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition34 = () -> this.isBowCustomBlockPosYVisible();
      this.bowCustomBlockPosY = (RandomizedRangeSettings)randomizedRangeSettings8.withVisibility(condition34);
      RandomizedRangeSettings randomizedRangeSettings9 = (new RandomizedRangeSettings(this, "BowCustomBlockPosZ", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition35 = () -> this.isBowCustomBlockPosZVisible();
      this.bowCustomBlockPosZ = (RandomizedRangeSettings)randomizedRangeSettings9.withVisibility(condition35);
      BooleanValue booleanValue8 = new BooleanValue(this, "BowCustomFacing", false);
      SettingVisibilityCondition condition36 = NoSlowBowOption.INVALID_PACKET::isSelected;
      this.bowCustomFacing = (BooleanValue)booleanValue8.withVisibility(condition36);
      RandomizedRangeSettings randomizedRangeSettings10 = (new RandomizedRangeSettings(this, "BowCustomFacingX", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition37 = () -> this.isBowCustomFacingXVisible();
      this.bowCustomFacingX = (RandomizedRangeSettings)randomizedRangeSettings10.withVisibility(condition37);
      RandomizedRangeSettings randomizedRangeSettings11 = (new RandomizedRangeSettings(this, "BowCustomFacingY", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition38 = () -> this.isBowCustomFacingYVisible();
      this.bowCustomFacingY = (RandomizedRangeSettings)randomizedRangeSettings11.withVisibility(condition38);
      RandomizedRangeSettings randomizedRangeSettings12 = (new RandomizedRangeSettings(this, "BowCustomFacingZ", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition39 = () -> this.isBowCustomFacingZVisible();
      this.bowCustomFacingZ = (RandomizedRangeSettings)randomizedRangeSettings12.withVisibility(condition39);
      this.rest = new DoubleValue(this, "Rest");
      this.rest2 = new MultiOptionSetting(this, "Rest", NoSlowRestItemOption.values(), this.rest);
      stringValue20 = new StringValue(this, "RestForward", 0.2, 0.2, 1.0, 2, this.rest);
      SettingVisibilityCondition condition40 = NoSlowRestItemOption.SLOWDOWN::isSelected;
      this.restForward = stringValue20.visibleWhen(condition40);
      stringValue20 = new StringValue(this, "RestStrafe", 0.2, 0.2, 1.0, 2, this.rest);
      SettingVisibilityCondition condition41 = NoSlowRestItemOption.SLOWDOWN::isSelected;
      this.restStrafe = stringValue20.visibleWhen(condition41);
      BooleansSetting booleansSetting5 = new BooleansSetting(
         this, "RestSlowdownInterval", "None", new String[]{"None", "TicksToNextSlowdownApply", "TicksToNextDelayApply"}, this.rest
      );
      String str7 = "SlowdownInterval";
      BooleansSetting booleansSetting6 = (BooleansSetting)booleansSetting5.withDisplayName(str7);
      SettingVisibilityCondition condition42 = () -> {
         Setting[] cachedSettings3 = Fly.getCachedSettings();
         return NoSlowRestItemOption.SLOWDOWN.isSelected() && this.enableIntervals.getValue();
      };
      this.restSlowdownInterval = (BooleansSetting)booleansSetting6.withVisibility(condition42);
      stringValue20 = new StringValue(this, "RestSlowdownIntervalAir", 0.0, 1.0, 10.0, 0, this.rest);
      String str8 = "SlowdownIntervalAir";
      stringValue20 = stringValue20.named(str8);
      SettingVisibilityCondition condition43 = () -> this.isRestSlowdownIntervalAirVisible();
      this.restSlowdownIntervalAir = stringValue20.visibleWhen(condition43);
      stringValue20 = new StringValue(this, "RestSlowdownIntervalGround", 0.0, 1.0, 10.0, 0, this.rest);
      String str9 = "SlowdownIntervalGround";
      stringValue20 = stringValue20.named(str9);
      SettingVisibilityCondition condition44 = () -> this.isRestSlowdownIntervalGroundVisible();
      this.restSlowdownIntervalGround = stringValue20.visibleWhen(condition44);
      stringValue20 = new StringValue(this, "TimerRest", 0.2, 0.1, 2.0, 2, this.rest);
      SettingVisibilityCondition condition45 = NoSlowRestItemOption.TIMER::isSelected;
      this.timerRest = stringValue20.visibleWhen(condition45);
      stringValue20 = new StringValue(this, "RestSneakForward", 0.3, 0.3, 1.0, 2, this.rest);
      SettingVisibilityCondition condition46 = NoSlowRestItemOption.SNEAK::isSelected;
      this.restSneakForward = stringValue20.visibleWhen(condition46);
      stringValue20 = new StringValue(this, "RestSneakStrafe", 0.3, 0.3, 1.0, 2, this.rest);
      SettingVisibilityCondition condition47 = NoSlowRestItemOption.SNEAK::isSelected;
      this.restSneakStrafe = stringValue20.visibleWhen(condition47);
      BooleanValue booleanValue9 = new BooleanValue(this, "RestNullItem", false);
      SettingVisibilityCondition condition48 = NoSlowRestItemOption.INVALID_PACKET::isSelected;
      this.restNullItem = (BooleanValue)booleanValue9.withVisibility(condition48);
      BooleanValue booleanValue10 = new BooleanValue(this, "RestEditFacing", false);
      SettingVisibilityCondition condition49 = NoSlowRestItemOption.INVALID_PACKET::isSelected;
      this.restEditFacing = (BooleanValue)booleanValue10.withVisibility(condition49);
      stringValue20 = new StringValue(this, "RestEnumFacingID", 0.0, 0.0, 5.0, 0);
      SettingVisibilityCondition condition50 = () -> this.isRestEnumFacingIdVisible();
      this.restEnumFacingID = stringValue20.visibleWhen(condition50);
      BooleanValue booleanValue11 = new BooleanValue(this, "RestCustomBlockPos", true);
      SettingVisibilityCondition condition51 = NoSlowRestItemOption.INVALID_PACKET::isSelected;
      this.restCustomBlockPos = (BooleanValue)booleanValue11.withVisibility(condition51);
      RandomizedRangeSettings randomizedRangeSettings13 = (new RandomizedRangeSettings(this, "RestCustomBlockPosX", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition52 = () -> this.isRestCustomBlockPosXVisible();
      this.restCustomBlockPosX = (RandomizedRangeSettings)randomizedRangeSettings13.withVisibility(condition52);
      RandomizedRangeSettings randomizedRangeSettings14 = (new RandomizedRangeSettings(this, "RestCustomBlockPosY", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition53 = () -> this.isRestCustomBlockPosYVisible();
      this.restCustomBlockPosY = (RandomizedRangeSettings)randomizedRangeSettings14.withVisibility(condition53);
      RandomizedRangeSettings randomizedRangeSettings15 = (new RandomizedRangeSettings(this, "RestCustomBlockPosZ", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition54 = () -> {
         Setting[] cachedSettings4 = Fly.getCachedSettings();
         return NoSlowRestItemOption.INVALID_PACKET.isSelected() && this.restCustomBlockPos.getValue();
      };
      this.restCustomBlockPosZ = (RandomizedRangeSettings)randomizedRangeSettings15.withVisibility(condition54);
      BooleanValue booleanValue12 = new BooleanValue(this, "RestCustomFacing", false);
      SettingVisibilityCondition condition55 = NoSlowRestItemOption.INVALID_PACKET::isSelected;
      this.restCustomFacing = (BooleanValue)booleanValue12.withVisibility(condition55);
      RandomizedRangeSettings randomizedRangeSettings16 = (new RandomizedRangeSettings(this, "RestCustomFacingX", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition56 = () -> this.isRestCustomFacingXVisible();
      this.restCustomFacingX = (RandomizedRangeSettings)randomizedRangeSettings16.withVisibility(condition56);
      RandomizedRangeSettings randomizedRangeSettings17 = (new RandomizedRangeSettings(this, "RestCustomFacingY", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition57 = () -> this.isRestCustomFacingYVisible();
      this.restCustomFacingY = (RandomizedRangeSettings)randomizedRangeSettings17.withVisibility(condition57);
      RandomizedRangeSettings randomizedRangeSettings18 = (new RandomizedRangeSettings(this, "RestCustomFacingZ", 0.0, 0.0, -10.0, 10.0, 8)).withoutNotInARow();
      SettingVisibilityCondition condition58 = () -> this.isRestCustomFacingZVisible();
      this.restCustomFacingZ = (RandomizedRangeSettings)randomizedRangeSettings18.withVisibility(condition58);
      BooleanValue booleanValue13 = new BooleanValue(this, "InputForwardLegit", false);
      SettingVisibilityCondition condition59 = () -> {
         Setting[] cachedSettings5 = Fly.getCachedSettings();
         return NoSlowSwordOption.INPUT.isSelected() || NoSlowBowOption.INPUT.isSelected() || NoSlowRestItemOption.INPUT.isSelected();
      };
      this.inputForwardLegit = (BooleanValue)booleanValue13.withVisibility(condition59);
      stringValue20 = new StringValue(this, "InputForward", 0.0, -1.0, 1.0, 2);
      SettingVisibilityCondition condition60 = () -> this.isInputForwardVisible();
      this.inputForward = stringValue20.visibleWhen(condition60);
      BooleanValue booleanValue14 = new BooleanValue(this, "InputStrafeLegit", false);
      SettingVisibilityCondition condition61 = () -> {
         Setting[] cachedSettings6 = Fly.getCachedSettings();
         return NoSlowSwordOption.INPUT.isSelected() || NoSlowBowOption.INPUT.isSelected() || NoSlowRestItemOption.INPUT.isSelected();
      };
      this.inputStrafeLegit = (BooleanValue)booleanValue14.withVisibility(condition61);
      stringValue20 = new StringValue(this, "InputStrafe", 0.0, -1.0, 1.0, 2);
      SettingVisibilityCondition condition62 = () -> this.isInputStrafeVisible();
      this.inputStrafe = stringValue20.visibleWhen(condition62);
      BooleanValue booleanValue15 = new BooleanValue(this, "InputJumpLegit", false);
      SettingVisibilityCondition condition63 = () -> {
         Setting[] cachedSettings7 = Fly.getCachedSettings();
         return NoSlowSwordOption.INPUT.isSelected() || NoSlowBowOption.INPUT.isSelected() || NoSlowRestItemOption.INPUT.isSelected();
      };
      this.inputJumpLegit = (BooleanValue)booleanValue15.withVisibility(condition63);
      BooleanValue booleanValue16 = new BooleanValue(this, "InputJump", false);
      SettingVisibilityCondition condition64 = () -> {
         Setting[] cachedSettings8 = Fly.getCachedSettings();
         return this.inputJumpLegit.isVisible() && !this.inputJumpLegit.getValue();
      };
      this.inputJump = (BooleanValue)booleanValue16.withVisibility(condition64);
      BooleanValue booleanValue17 = new BooleanValue(this, "InputSneakLegit", false);
      SettingVisibilityCondition condition65 = () -> {
         Setting[] cachedSettings9 = Fly.getCachedSettings();
         return NoSlowSwordOption.INPUT.isSelected() || NoSlowBowOption.INPUT.isSelected() || NoSlowRestItemOption.INPUT.isSelected();
      };
      this.inputSneakLegit = (BooleanValue)booleanValue17.withVisibility(condition65);
      BooleanValue booleanValue18 = new BooleanValue(this, "InputSneak", false);
      SettingVisibilityCondition condition66 = () -> this.isInputSneakVisible();
      this.inputSneak = (BooleanValue)booleanValue18.withVisibility(condition66);
      this.timeHelper = new TimeHelper();
      this.bugBypassState = 0;
      this.lastUsedItemStack = null;
      this.shouldSendUnToggleRelease = true;
      this.swordBlinkTicks = 0;
      this.usingTicks = 0;
      this.airUsingTicks = 0;
      this.groundUsingTicks = 0;
      this.grimRestItemActive = false;
      this.blink2Delaying = false;
      this.blink2Ticks = 0;
      this.timerModified = false;
      this.pendingCustomSwitchPlace = false;
   }

   @Override
   public void onEnable() {
      this.blink2Delaying = false;
   }

   @Override
   public void onDisable() {
      if (mc != null && mc.getTimer() != null) {
         mc.getTimer().timerSpeed = 1.0F;
      }
      this.grimRestItemActive = false;
   }

   @Override
   public void updateEventSubscriptions() {
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      byte b = 18;
      byte b2 = 36;
      this.toggleEventSubscription(b2, b);
      this.onAttackEntity();
      this.onAttackEntity();
   }

   @Override
   public void onTick() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      this.usingTicks++;
      if (mc.thePlayer.onGround) {
         this.groundUsingTicks++;
      } else {
         this.airUsingTicks++;
      }

      ItemStack currentHeldItem = this.getCurrentHeldItem();
      if (this.shouldSkipNoSlow(currentHeldItem)) {
         this.airUsingTicks = 0;
         this.groundUsingTicks = 0;
         this.usingTicks = 0;
      }
   }

   @Override
   public void onPacket2(EventPacket event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      Packet packet = event.getPacket();
      if (packet instanceof C08PacketPlayerBlockPlacement && !mc.thePlayer.isUsingItem()) {
         C08PacketPlayerBlockPlacement packet2 = (C08PacketPlayerBlockPlacement)packet;
         if (packet2.getPlacedBlockDirection() == 255) {
            ItemStack stack = packet2.getStack();
            if (this.isSwitchCustomItem(stack) && mc.thePlayer.airTicks < 2) {
               if (mc.thePlayer.onGround) {
                  mc.thePlayer.jump();
               }

               this.pendingCustomSwitchPlace = true;
            }
         }
      }

      if (packet instanceof C03PacketPlayer) {
         ItemStack currentHeldItem = this.getCurrentHeldItem();
         if (this.shouldSkipNoSlow(currentHeldItem)) {
            this.bugBypassState = 0;
            return;
         }

         Item item = currentHeldItem.getItem();
         C03PacketPlayer packet3 = (C03PacketPlayer)packet;
         if (this.isSwordItem(item)) {
            if (NoSlowSwordOption.ON_GROUND.isSelected()) {
               packet3.setOnGround(true);
            }
         } else if (this.isBowItem(item)) {
            if (NoSlowBowOption.ON_GROUND.isSelected()) {
               packet3.setOnGround(true);
            }
         } else {
            if (this.isConsumableRestItem(item, currentHeldItem) && NoSlowRestItemOption.ON_GROUND.isSelected()) {
               packet3.setOnGround(true);
            }
         }
      }

      if (NoSlowRestItemOption.GRIM.isSelected() && (packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C07PacketPlayerDigging)) {
         ItemStack currentHeldItem2 = this.getCurrentHeldItem();
         boolean flag = false;
         if (!this.shouldSkipNoSlow(currentHeldItem2, flag)) {
            Item item2 = currentHeldItem2.getItem();
            Item item3 = item2;
            if (this.isConsumableRestItem(item3, currentHeldItem2)) {
               if (packet instanceof C08PacketPlayerBlockPlacement) {
                  mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                  mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                  event.cancel();
                  KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                  this.grimRestItemActive = true;
               } else if (this.grimRestItemActive && ((C07PacketPlayerDigging)packet).getStatus() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                  this.grimRestItemActive = false;
                  event.cancel();
               }
            }
         }
      }

      if (packet instanceof C08PacketPlayerBlockPlacement) {
         C08PacketPlayerBlockPlacement packet4 = (C08PacketPlayerBlockPlacement)packet;
         if (packet4.getPlacedBlockDirection() == 255) {
            ItemStack stack2 = packet4.getStack();
            if (!this.shouldSkipNoSlow(stack2)) {
               Item item4 = stack2.getItem();
               if (item4 != null) {
                  if (this.isSwordItem(item4)) {
                     if (!NoSlowSwordOption.INVALID_PACKET.isSelected()) {
                        return;
                     }

                     if (this.swordNullItem.getValue()) {
                        packet4.setStack(null);
                     }

                     if (this.swordEditFacing.getValue()) {
                        packet4.setPlacedBlockDirection((int)this.swordEnumFacingID.getValue());
                     }

                     if (this.swordCustomBlockPos.getValue()) {
                        packet4.setPosition(new BlockPos(this.swordCustomBlockPosX.nextValue(), this.swordCustomBlockPosY.nextValue(), this.swordCustomBlockPosZ.nextValue()));
                     }

                     if (this.swordCustomFacing.getValue()) {
                        packet4.setFacingX((float)this.swordCustomFacingX.nextValue());
                        packet4.setFacingY((float)this.swordCustomFacingY.nextValue());
                        packet4.setFacingZ((float)this.swordCustomFacingZ.nextValue());
                     }
                  } else if (this.isBowItem(item4)) {
                     if (!NoSlowBowOption.INVALID_PACKET.isSelected()) {
                        return;
                     }

                     if (this.bowNullItem.getValue()) {
                        packet4.setStack(null);
                     }

                     if (this.bowEditFacing.getValue()) {
                        packet4.setPlacedBlockDirection((int)this.bowEnumFacingID.getValue());
                     }

                     if (this.bowCustomBlockPos.getValue()) {
                        packet4.setPosition(new BlockPos(this.bowCustomBlockPosX.nextValue(), this.bowCustomBlockPosY.nextValue(), this.bowCustomBlockPosZ.nextValue()));
                     }

                     if (this.bowCustomFacing.getValue()) {
                        packet4.setFacingX((float)this.bowCustomFacingX.nextValue());
                        packet4.setFacingY((float)this.bowCustomFacingY.nextValue());
                        packet4.setFacingZ((float)this.bowCustomFacingZ.nextValue());
                     }
                  } else {
                     if (this.isConsumableRestItem(item4, stack2)) {
                        if (!NoSlowRestItemOption.INVALID_PACKET.isSelected()) {
                           return;
                        }

                        if (this.restNullItem.getValue()) {
                           packet4.setStack(null);
                        }

                        if (this.restEditFacing.getValue()) {
                           packet4.setPlacedBlockDirection((int)this.restEnumFacingID.getValue());
                        }

                        if (this.restCustomBlockPos.getValue()) {
                           packet4.setPosition(new BlockPos(this.restCustomBlockPosX.nextValue(), this.restCustomBlockPosY.nextValue(), this.restCustomBlockPosZ.nextValue()));
                        }

                        if (this.restCustomFacing.getValue()) {
                           packet4.setFacingX((float)this.restCustomFacingX.nextValue());
                           packet4.setFacingY((float)this.restCustomFacingY.nextValue());
                           packet4.setFacingZ((float)this.restCustomFacingZ.nextValue());
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void onPacketDelay(EventPacketDelay event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      Packet packet = event.getPacket();
      if (NoSlowSwordOption.BLINK.isSelected()) {
         if (this.swordBlinkTicks > 0) {
            boolean flag = true;
            event.setDelayed(flag);
         }

         if (packet instanceof C03PacketPlayer) {
            EntityPlayerSP thePlayer = mc.thePlayer;
            if (thePlayer != null && thePlayer.isBlocking()) {
               this.swordBlinkTicks++;
            } else {
               this.swordBlinkTicks = 0;
            }
         }

         if (this.swordBlinkTicks > 3) {
            this.swordBlinkTicks = 0;
         }
      }

      if (NoSlowSwordOption.BLINK2.isSelected()) {
         exitBlock:
         if (packet instanceof C08PacketPlayerBlockPlacement || packet instanceof C07PacketPlayerDigging || packet instanceof C03PacketPlayer) {
            ItemStack currentHeldItem = this.getCurrentHeldItem();
            if (currentHeldItem != null) {
               Item item = currentHeldItem.getItem();
               if (this.isSwordItem(item)) {
                  if (packet instanceof C08PacketPlayerBlockPlacement) {
                     C08PacketPlayerBlockPlacement packet2 = (C08PacketPlayerBlockPlacement)packet;
                     if (packet2.getPlacedBlockDirection() == 255
                        && packet2.getPosition().equals(C08PacketPlayerBlockPlacement.INVALID_BLOCK_POS)
                        && packet2.getPlacedBlockOffsetX() == 0.0F
                        && packet2.getPlacedBlockOffsetY() == 0.0F
                        && packet2.getPlacedBlockOffsetZ() == 0.0F) {
                        this.blink2Delaying = false;
                        this.blink2Ticks = 0;
                     }
                  }

                  if (packet instanceof C07PacketPlayerDigging) {
                     C07PacketPlayerDigging packet3 = (C07PacketPlayerDigging)packet;
                     if (packet3.getStatus() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM && packet3.getPosition().equals(BlockPos.ORIGIN) && packet3.getFacing() == EnumFacing.DOWN) {
                        this.blink2Delaying = true;
                        this.blink2Ticks = 0;
                     }
                  }

                  if (packet instanceof C03PacketPlayer) {
                     if ((double)this.blink2Ticks >= this.swordMaxBlinkTicks.getValue() && this.blink2Delaying) {
                        this.blink2Delaying = false;
                        this.blink2Ticks = 0;
                     }

                     this.blink2Ticks++;
                  }
                  break exitBlock;
               }
            }

            if (this.blink2Delaying) {
               this.blink2Delaying = false;
            }
         }

         if (this.blink2Delaying) {
            boolean flag2 = true;
            event.setDelayed(flag2);
         }
      }
   }

   @Override
   public void onSprint3(EventSprint event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      ItemStack currentHeldItem = this.getCurrentHeldItem();
      if (this.shouldSkipNoSlow(currentHeldItem)) {
         this.timeHelper.reset();
      } else {
         long l = 400L;
         if (this.timeHelper.reached2(l) || !this.startSlow.getValue()) {
            Item item = currentHeldItem.getItem();
            if (this.mode.getSelectedValue().equals("AllDirection")) {
               if ((!this.isSwordItem(item) || !NoSlowSwordOption.SPRINT.isSelected()) && (!this.isBowItem(item) || !NoSlowBowOption.SPRINT.isSelected())) {
                  if (!this.isConsumableRestItem(item, currentHeldItem) || !NoSlowRestItemOption.SPRINT.isSelected()) {
                     return;
                  }
               }

               boolean flag = true;
               event.setForceSprint(flag);
            }
         }
      }
   }

   @Override
   public void onSprint2(EventSprint event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      ItemStack currentHeldItem = this.getCurrentHeldItem();
      if (this.shouldSkipNoSlow(currentHeldItem)) {
         this.timeHelper.reset();
      } else {
         long l = 400L;
         if (this.timeHelper.reached2(l) || !this.startSlow.getValue()) {
            Item item = currentHeldItem.getItem();
            if (!this.mode.getSelectedValue().equals("AllDirectionMotion")) {
               if (!this.mode.getSelectedValue().equals("LegitMotion")) {
                  return;
               }

               if (!this.canLegitSprint()) {
                  return;
               }
            }

            if ((!this.isSwordItem(item) || !NoSlowSwordOption.SPRINT.isSelected()) && (!this.isBowItem(item) || !NoSlowBowOption.SPRINT.isSelected())) {
               if (!this.isConsumableRestItem(item, currentHeldItem) || !NoSlowRestItemOption.SPRINT.isSelected()) {
                  return;
               }
            }

            boolean flag = true;
            event.setMotionSprint(flag);
         }
      }
   }

   @Override
   public void onSprint(EventSprint event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      ItemStack currentHeldItem = this.getCurrentHeldItem();
      if (this.shouldSkipNoSlow(currentHeldItem)) {
         this.timeHelper.reset();
      } else {
         long l = 400L;
         if (this.timeHelper.reached2(l) || !this.startSlow.getValue()) {
            Item item = currentHeldItem.getItem();
            if (MoveUtil.isMoving()) {
               if (!this.mode.getSelectedValue().equals("AllDirectionMotion")) {
                  if (!this.mode.getSelectedValue().equals("LegitMotion")) {
                     return;
                  }

                  if (!this.canLegitSprint()) {
                     return;
                  }
               }

               if ((!this.isSwordItem(item) || !NoSlowSwordOption.SPRINT.isSelected()) && (!this.isBowItem(item) || !NoSlowBowOption.SPRINT.isSelected())) {
                  if (!this.isConsumableRestItem(item, currentHeldItem) || !NoSlowRestItemOption.SPRINT.isSelected()) {
                     return;
                  }
               }

               boolean flag = true;
               event.setOmniJumpSprint(flag);
            }
         }
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      this.lastForwardInput = this.forwardInput;
      Fly.getCachedSettings();
      this.lastSneakInput = this.sneakInput;
      this.forwardInput = event.getMoveForward();
      this.sneakInput = event.isSneak();
      ItemStack currentHeldItem = this.getCurrentHeldItem();
      if (this.shouldSkipNoSlow(currentHeldItem)) {
         this.timeHelper.reset();
      } else {
         long l = 400L;
         if (this.timeHelper.reached2(l) || !this.startSlow.getValue()) {
            Item item = currentHeldItem.getItem();
            if (this.isSwordItem(item)) {
               if (NoSlowSwordOption.SNEAK.isSelected()) {
                  boolean flag = true;
                  event.setSneak(flag);
                  double value = this.swordSneakForward.getValue();
                  event.setSneakForwardMultiplier(value);
                  double value2 = this.swordSneakStrafe.getValue();
                  event.setSneakStrafeMultiplier(value2);
               }
            } else if (this.isBowItem(item)) {
               if (NoSlowBowOption.SNEAK.isSelected()) {
                  boolean flag2 = true;
                  event.setSneak(flag2);
                  double value3 = this.bowSneakForward.getValue();
                  event.setSneakForwardMultiplier(value3);
                  double value4 = this.bowSneakStrafe.getValue();
                  event.setSneakStrafeMultiplier(value4);
               }
            } else {
               if (this.isConsumableRestItem(item, currentHeldItem) && NoSlowRestItemOption.SNEAK.isSelected()) {
                  boolean flag3 = true;
                  event.setSneak(flag3);
                  double value5 = this.restSneakForward.getValue();
                  event.setSneakForwardMultiplier(value5);
                  double value6 = this.restSneakStrafe.getValue();
                  event.setSneakStrafeMultiplier(value6);
               }
            }
         }
      }
   }

   @Override
   public void onSlowDown(EventSlowDown event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      ItemStack currentHeldItem = this.getCurrentHeldItem();
      if (this.shouldSkipNoSlow(currentHeldItem)) {
         this.timeHelper.reset();
      } else {
         long l = 400L;
         if (this.timeHelper.reached2(l) || !this.startSlow.getValue()) {
            Item item = currentHeldItem.getItem();
            if (this.isSwordItem(item)) {
               if (this.mode.getSelectedValue().equals("Legit")) {
                  boolean isSelected = NoSlowSwordOption.SPRINT.isSelected();
                  event.setAllowSprint(isSelected);
               }

               if (NoSlowSwordOption.SLOWDOWN.isSelected()) {
                  StringValue stringValue = this.swordSlowdownIntervalGround;
                  StringValue stringValue2 = this.swordSlowdownIntervalAir;
                  BooleansSetting swordSlowdownInterval2 = this.swordSlowdownInterval;
                  if (this.shouldApplySlowdownByInterval(swordSlowdownInterval2, stringValue2, stringValue)) {
                     if (this.switchOnlyEmptySlot.getValue() && NoSlowSwordOption.SWITCH.isSelected()) {
                        int currentItem = mc.thePlayer.inventory.currentItem;
                        if (this.findSwitchHotbarSlot(currentItem) < 0) {
                           return;
                        }
                     }

                     float value = (float)this.swordForward.getValue();
                     event.setForwardMultiplier(value);
                     float value2 = (float)this.swordStrafe.getValue();
                     event.setStrafeMultiplier(value2);
                  }
               }
            } else if (this.isBowItem(item)) {
               if (this.mode.getSelectedValue().equals("Legit")) {
                  boolean isSelected2 = NoSlowBowOption.SPRINT.isSelected();
                  event.setAllowSprint(isSelected2);
               }

               if (NoSlowBowOption.SLOWDOWN.isSelected()) {
                  StringValue stringValue3 = this.bowSlowdownIntervalGround;
                  StringValue bowSlowdownIntervalAir2 = this.bowSlowdownIntervalAir;
                  BooleansSetting bowSlowdownInterval2 = this.bowSlowdownInterval;
                  if (this.shouldApplySlowdownByInterval(bowSlowdownInterval2, bowSlowdownIntervalAir2, stringValue3)) {
                     if (this.switchOnlyEmptySlot.getValue() && NoSlowBowOption.SWITCH.isSelected()) {
                        int currentItem2 = mc.thePlayer.inventory.currentItem;
                        if (this.findSwitchHotbarSlot(currentItem2) < 0) {
                           return;
                        }
                     }

                     float value3 = (float)this.bowForward.getValue();
                     event.setForwardMultiplier(value3);
                     float value4 = (float)this.bowStrafe.getValue();
                     event.setStrafeMultiplier(value4);
                  }
               }
            } else {
               if (this.isConsumableRestItem(item, currentHeldItem)) {
                  if (NoSlowRestItemOption.UN_TOGGLE.isSelected() && this.shouldSendUnToggleRelease) {
                     mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
                     this.shouldSendUnToggleRelease = false;
                  }

                  if (this.mode.getSelectedValue().equals("Legit")) {
                     boolean isSelected3 = NoSlowRestItemOption.SPRINT.isSelected();
                     event.setAllowSprint(isSelected3);
                  }

                  if (NoSlowRestItemOption.SLOWDOWN.isSelected()) {
                     StringValue stringValue4 = this.restSlowdownIntervalGround;
                     StringValue stringValue5 = this.restSlowdownIntervalAir;
                     BooleansSetting restSlowdownInterval2 = this.restSlowdownInterval;
                     if (this.shouldApplySlowdownByInterval(restSlowdownInterval2, stringValue5, stringValue4)) {
                        if (this.switchOnlyEmptySlot.getValue() && NoSlowRestItemOption.SWITCH.isSelected()) {
                           int currentItem3 = mc.thePlayer.inventory.currentItem;
                           if (this.findSwitchHotbarSlot(currentItem3) < 0) {
                              return;
                           }
                        }

                        float value5 = (float)this.restForward.getValue();
                        event.setForwardMultiplier(value5);
                        float value6 = (float)this.restStrafe.getValue();
                        event.setStrafeMultiplier(value6);
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void onMotion(EventMotion event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (NoSlowBowOption.SWITCH_CUSTOM.isSelected() || NoSlowRestItemOption.SWITCH_CUSTOM.isSelected()) {
         if (mc.thePlayer.airTicks == 2 && this.pendingCustomSwitchPlace) {
            this.pendingCustomSwitchPlace = false;
            mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
         } else if (mc.thePlayer.isUsingItem()) {
            ItemStack currentHeldItem = this.getCurrentHeldItem();
            if (this.isSwitchCustomItem(currentHeldItem)) {
               double d = event.getY() + 1.0E-14;
               event.setY(d);
            }
         }
      }

      ItemStack currentHeldItem2 = this.getCurrentHeldItem();
      if (this.shouldSkipNoSlow(currentHeldItem2)) {
         this.grimRestItemActive = false;
      } else {
         Item item = currentHeldItem2.getItem();
         if (this.isSwordItem(item)) {
            if (NoSlowSwordOption.INPUT.isSelected()) {
               this.sendCustomInputPacket();
            }

            if (MoveUtil.isMoving() && mc.thePlayer.ticksExisted % 5 == 0 && NoSlowSwordOption.INVALID_DELAY.isSelected()) {
               mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 1, null, 0.0F, 0.0F, 0.0F));
            }

            if (NoSlowSwordOption.TOGGLE.isSelected()) {
               mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }

            if (NoSlowSwordOption.SWITCH.isSelected() && (this.switchMode.getSelectedValue().equals("PrePosition") || this.switchMode.getSelectedValue().equals("PrePostPosition"))) {
               int currentItem = mc.thePlayer.inventory.currentItem;
               int n = this.findSwitchHotbarSlot(currentItem);
               if (n >= 0) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n));
                  if (this.switchMode.getSelectedValue().equals("PrePosition")) {
                     mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                  }
               }
            }

            if (NoSlowSwordOption.SLOT.isSelected() && this.slotMode.getSelectedValue().equals("PrePosition")) {
               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }

            if (NoSlowSwordOption.SPAM.isSelected() && this.spamMode.getSelectedValue().equals("PrePosition")) {
               mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            }
         } else if (this.isBowItem(item)) {
            if (NoSlowBowOption.INPUT.isSelected()) {
               this.sendCustomInputPacket();
            }

            if (NoSlowBowOption.TOGGLE.isSelected()) {
               mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }

            if (NoSlowBowOption.SWITCH.isSelected() && (this.switchMode.getSelectedValue().equals("PrePosition") || this.switchMode.getSelectedValue().equals("PrePostPosition"))) {
               int currentItem2 = mc.thePlayer.inventory.currentItem;
               int n2 = this.findSwitchHotbarSlot(currentItem2);
               if (n2 >= 0) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n2));
                  if (this.switchMode.getSelectedValue().equals("PrePosition")) {
                     mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                  }
               }
            }

            if (NoSlowBowOption.PLACE.isSelected()) {
               mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), EnumFacing.UP.getIndex(), null, 0.0F, 0.0F, 0.0F));
            }

            if (NoSlowBowOption.SLOT.isSelected() && this.slotMode.getSelectedValue().equals("PrePosition")) {
               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }

            if (NoSlowBowOption.SPAM.isSelected() && this.spamMode.getSelectedValue().equals("PrePosition")) {
               mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            }
         } else {
            if (this.isConsumableRestItem(item, currentHeldItem2)) {
               if (NoSlowRestItemOption.INPUT.isSelected()) {
                  this.sendCustomInputPacket();
               }

               if (NoSlowRestItemOption.TOGGLE.isSelected()) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
               }

               if (NoSlowRestItemOption.SWITCH.isSelected() && (this.switchMode.getSelectedValue().equals("PrePosition") || this.switchMode.getSelectedValue().equals("PrePostPosition"))) {
                  int currentItem3 = mc.thePlayer.inventory.currentItem;
                  int n3 = this.findSwitchHotbarSlot(currentItem3);
                  if (n3 >= 0 && !ClientManagers.moduleManager.scaffold.isToggled()) {
                     mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n3));
                     if (this.switchMode.getSelectedValue().equals("PrePosition")) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                     }
                  }
               }

               if (NoSlowRestItemOption.SLOT.isSelected() && this.slotMode.getSelectedValue().equals("PrePosition")) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
               }

               if (NoSlowRestItemOption.SPAM.isSelected() && this.spamMode.getSelectedValue().equals("PrePosition")) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
               }

               if (NoSlowRestItemOption.SPAM2.isSelected() && MoveUtil.isMoving() && mc.thePlayer.ticksExisted % 5 == 0) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 1, null, 0.0F, 0.0F, 0.0F));
               }
            }
         }
      }
   }

   private boolean shouldApplySlowdownByInterval(BooleansSetting booleansSetting, StringValue stringValue, StringValue stringValue2) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.enableIntervals.getValue() && !booleansSetting.getSelectedValue().equalsIgnoreCase("None")) {
         if (stringValue.getValue() == stringValue2.getValue()) {
            if (stringValue.getValue() == 0.0) {
               return true;
            } else {
               int n = (int)(stringValue.getValue() + 1.0);
               return booleansSetting.getSelectedValue().equalsIgnoreCase("TicksToNextSlowdownApply") == (this.usingTicks % n == 0);
            }
         } else if (mc.thePlayer.onGround) {
            if (stringValue2.getValue() == 0.0) {
               return true;
            } else {
               int n2 = (int)(stringValue2.getValue() + 1.0);
               return booleansSetting.getSelectedValue().equalsIgnoreCase("TicksToNextSlowdownApply") == (this.groundUsingTicks % n2 == 0);
            }
         } else if (stringValue.getValue() == 0.0) {
            return true;
         } else {
            int n3 = (int)(stringValue.getValue() + 1.0);
            return booleansSetting.getSelectedValue().equalsIgnoreCase("TicksToNextSlowdownApply") == (this.airUsingTicks % n3 == 0);
         }
      } else {
         return true;
      }
   }

   private int findSwitchHotbarSlot(int n) {
      ArrayList list = new ArrayList();
      Fly.getCachedSettings();

      for (int i = 36; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (stack == null && i != n + 36) {
            list.add(i - 36);
         }
      }

      if (!list.isEmpty()) {
         RandomNumberGenerator instance = DefaultRandomGenerator.instance;
         int n2 = list.size() - 1;
         byte b = 0;
         return (Integer)list.get(instance.nextInt2(b, n2));
      } else if (this.switchOnlyEmptySlot.getValue()) {
         return -1;
      } else if (this.switchGomme.getValue()) {
         int n3 = n == 0 ? 1 : -1;
         return n + n3;
      } else {
         byte b2 = 8;
         byte b3 = 0;
         int n4 = DefaultRandomGenerator.instance.nextInt2(b3, b2);
         if (n4 == n) {
            this.findSwitchHotbarSlot(n);
         }

         return n4;
      }
   }

   private boolean canLegitSprint() {
      float f = 0.8F;
      Fly.getCachedSettings();
      boolean isSprinting = mc.thePlayer.isSprinting();
      boolean flag = (float)mc.thePlayer.getFoodStats().getFoodLevel() > 6.0F || mc.thePlayer.capabilities.allowFlying;
      if (mc.thePlayer.onGround
         && !this.lastSneakInput
         && this.lastForwardInput < f
         && this.forwardInput >= f
         && !isSprinting
         && this.isLicenseTimestampValid()
         && flag
         && !mc.thePlayer.isPotionActive(Potion.blindness)) {
         isSprinting = true;
      }

      if (!isSprinting && this.forwardInput >= f && flag && !mc.thePlayer.isPotionActive(Potion.blindness)) {
         isSprinting = true;
      }

      if (isSprinting && (this.forwardInput < f || mc.thePlayer.isCollidedHorizontally && !flag)) {
         isSprinting = false;
      }

      return isSprinting;
   }

   @Override
   public void onMotion() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      ItemStack currentHeldItem = this.getCurrentHeldItem();
      if (this.shouldSkipNoSlow(currentHeldItem)) {
         this.shouldSendUnToggleRelease = true;
      } else {
         Item item = currentHeldItem.getItem();
         if (this.isSwordItem(item)) {
            if (NoSlowSwordOption.TOGGLE.isSelected()) {
               mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            }

            if (NoSlowSwordOption.SWITCH.isSelected() && this.switchMode.getSelectedValue().equals("PostPosition")) {
               int currentItem = mc.thePlayer.inventory.currentItem;
               int n = this.findSwitchHotbarSlot(currentItem);
               if (n >= 0 && !ClientManagers.moduleManager.scaffold.isToggled()) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n));
                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
               }
            }

            if (NoSlowSwordOption.SLOT.isSelected() && this.slotMode.getSelectedValue().equals("PostPosition")) {
               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }

            if (NoSlowSwordOption.SPAM.isSelected() && this.spamMode.getSelectedValue().equals("PostPosition")) {
               mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            }
         } else if (this.isBowItem(item)) {
            if (NoSlowBowOption.TOGGLE.isSelected()) {
               mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            }

            if (NoSlowBowOption.SWITCH.isSelected() && this.switchMode.getSelectedValue().equals("PostPosition")) {
               int currentItem2 = mc.thePlayer.inventory.currentItem;
               int n2 = this.findSwitchHotbarSlot(currentItem2);
               if (n2 >= 0 && !ClientManagers.moduleManager.scaffold.isToggled()) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n2));
                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
               }
            }

            if (NoSlowBowOption.SLOT.isSelected() && this.slotMode.getSelectedValue().equals("PostPosition")) {
               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }

            if (NoSlowBowOption.SPAM.isSelected() && this.spamMode.getSelectedValue().equals("PostPosition")) {
               mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            }
         } else {
            if (this.isConsumableRestItem(item, currentHeldItem)) {
               if (NoSlowRestItemOption.TOGGLE.isSelected()) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
               }

               if (NoSlowRestItemOption.SWITCH.isSelected() && this.switchMode.getSelectedValue().equals("PostPosition")) {
                  int currentItem3 = mc.thePlayer.inventory.currentItem;
                  int n3 = this.findSwitchHotbarSlot(currentItem3);
                  if (n3 >= 0 && !ClientManagers.moduleManager.scaffold.isToggled()) {
                     mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n3));
                     mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                  }
               }

               if (NoSlowRestItemOption.SLOT.isSelected() && this.slotMode.getSelectedValue().equals("PostPosition")) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
               }

               if (NoSlowBowOption.SPAM.isSelected() && this.spamMode.getSelectedValue().equals("PostPosition")) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
               }
            }
         }
      }
   }

   @Override
   public void onUpdate2() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      ItemStack currentHeldItem = this.getCurrentHeldItem();
      if (this.shouldSkipNoSlow(currentHeldItem)) {
         if (this.timerModified) {
            this.timerModified = false;
            mc.getTimer().timerSpeed = 1.0F;
         }
      } else {
         Item item = currentHeldItem.getItem();
         if (this.isSwordItem(item)) {
            if (NoSlowSwordOption.TIMER.isSelected()) {
               mc.getTimer().timerSpeed = (float)this.timerSword.getValue();
               this.timerModified = true;
            } else {
               mc.getTimer().timerSpeed = 1.0F;
            }
         } else if (this.isBowItem(item)) {
            if (NoSlowBowOption.TIMER.isSelected()) {
               mc.getTimer().timerSpeed = (float)this.timerBow.getValue();
               this.timerModified = true;
            } else {
               mc.getTimer().timerSpeed = 1.0F;
            }
         } else {
            if (this.isConsumableRestItem(item, currentHeldItem)) {
               if (NoSlowRestItemOption.TIMER.isSelected()) {
                  mc.getTimer().timerSpeed = (float)this.timerRest.getValue();
                  this.timerModified = true;
               } else {
                  mc.getTimer().timerSpeed = 1.0F;
               }
            }
         }
      }
   }

   @Override
   public void onTick2() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      ItemStack currentHeldItem = this.getCurrentHeldItem();
      if (!this.shouldSkipNoSlow(currentHeldItem)) {
         Item item = currentHeldItem.getItem();
         if (item instanceof ItemSword && NoSlowSwordOption.GRIM.isSelected()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
         }

         if (this.isSwordItem(item)) {
            if (NoSlowSwordOption.SWITCH.isSelected() && this.switchMode.getSelectedValue().equals("PostAttack")) {
               int currentItem = mc.thePlayer.inventory.currentItem;
               int n = this.findSwitchHotbarSlot(currentItem);
               if (n >= 0 && !ClientManagers.moduleManager.scaffold.isToggled()) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n));
                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
               }
            }

            if (NoSlowSwordOption.SLOT.isSelected() && this.slotMode.getSelectedValue().equals("PostAttack")) {
               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }

            if (NoSlowSwordOption.SPAM.isSelected() && this.spamMode.getSelectedValue().equals("PostAttack")) {
               mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            }
         } else if (this.isBowItem(item)) {
            if (NoSlowBowOption.SWITCH.isSelected() && this.switchMode.getSelectedValue().equals("PostAttack")) {
               int currentItem2 = mc.thePlayer.inventory.currentItem;
               int n2 = this.findSwitchHotbarSlot(currentItem2);
               if (n2 >= 0 && !ClientManagers.moduleManager.scaffold.isToggled()) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n2));
                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
               }
            }

            if (NoSlowBowOption.SLOT.isSelected() && this.slotMode.getSelectedValue().equals("PostAttack")) {
               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }

            if (NoSlowBowOption.SPAM.isSelected() && this.spamMode.getSelectedValue().equals("PostAttack")) {
               mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            }
         } else {
            if (this.isConsumableRestItem(item, currentHeldItem)) {
               if (NoSlowRestItemOption.SWITCH.isSelected() && this.switchMode.getSelectedValue().equals("PostAttack")) {
                  int currentItem3 = mc.thePlayer.inventory.currentItem;
                  int n3 = this.findSwitchHotbarSlot(currentItem3);
                  if (n3 >= 0 && !ClientManagers.moduleManager.scaffold.isToggled()) {
                     mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n3));
                     mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                  }
               }

               if (NoSlowRestItemOption.SLOT.isSelected() && this.slotMode.getSelectedValue().equals("PostAttack")) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
               }

               if (NoSlowRestItemOption.SPAM.isSelected() && this.spamMode.getSelectedValue().equals("PostAttack")) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
               }
            }
         }
      }
   }

   @Override
   public void onClick(EventClick event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      ItemStack currentHeldItem = this.getCurrentHeldItem();
      if (this.shouldSkipNoSlow(currentHeldItem)) {
         this.bugBypassState = 0;
      } else {
         Item item = currentHeldItem.getItem();
         if (this.lastUsedItemStack != null && !this.lastUsedItemStack.equals(currentHeldItem)) {
            this.bugBypassState = 0;
         }

         if (this.isSwordItem(item)) {
            if (NoSlowSwordOption.GRIM.isSelected()) {
               mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 8));
               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
               mc.playerController.currentPlayerItem = mc.thePlayer.inventory.currentItem;
            }

            if (NoSlowSwordOption.SWITCH.isSelected() && (this.switchMode.getSelectedValue().equals("PreAttack") || this.switchMode.getSelectedValue().equals("PrePostPosition"))) {
               int currentItem = mc.thePlayer.inventory.currentItem;
               int n = this.findSwitchHotbarSlot(currentItem);
               if (n >= 0) {
                  if (this.switchMode.getSelectedValue().equals("PreAttack")) {
                     mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n));
                  }

                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                  mc.playerController.currentPlayerItem = mc.thePlayer.inventory.currentItem;
               }
            }

            if (NoSlowSwordOption.SLOT.isSelected() && this.slotMode.getSelectedValue().equals("PreAttack")) {
               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }

            if (NoSlowSwordOption.SPAM.isSelected() && this.spamMode.getSelectedValue().equals("PreAttack")) {
               mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            }

            if (NoSlowSwordOption.BUG.isSelected()) {
               boolean flag = false;
               event.setAllowRightClick(flag);
               if (this.bugBypassState != 1) {
                  mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                  mc.thePlayer.stopUsingItem();
                  mc.thePlayer.closeScreen();
                  event.cancel();
                  this.bugBypassState = 1;
               }
            }
         } else if (this.isBowItem(item)) {
            if (NoSlowBowOption.SWITCH.isSelected() && (this.switchMode.getSelectedValue().equals("PreAttack") || this.switchMode.getSelectedValue().equals("PrePostPosition"))) {
               int currentItem2 = mc.thePlayer.inventory.currentItem;
               int n2 = this.findSwitchHotbarSlot(currentItem2);
               if (n2 >= 0) {
                  if (this.switchMode.getSelectedValue().equals("PreAttack")) {
                     mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n2));
                  }

                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                  mc.playerController.currentPlayerItem = mc.thePlayer.inventory.currentItem;
               }
            }

            if (NoSlowBowOption.SLOT.isSelected() && this.slotMode.getSelectedValue().equals("PreAttack")) {
               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }

            if (NoSlowBowOption.SPAM.isSelected() && this.spamMode.getSelectedValue().equals("PreAttack")) {
               mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            }
         } else {
            if (this.isConsumableRestItem(item, currentHeldItem)) {
               if (NoSlowRestItemOption.SWITCH.isSelected() && (this.switchMode.getSelectedValue().equals("PreAttack") || this.switchMode.getSelectedValue().equals("PrePostPosition"))) {
                  int currentItem3 = mc.thePlayer.inventory.currentItem;
                  int n3 = this.findSwitchHotbarSlot(currentItem3);
                  if (n3 >= 0 && !ClientManagers.moduleManager.scaffold.isToggled()) {
                     if (this.switchMode.getSelectedValue().equals("PreAttack")) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(n3));
                     }

                     mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                     mc.playerController.currentPlayerItem = mc.thePlayer.inventory.currentItem;
                  }
               }

               if (NoSlowRestItemOption.SLOT.isSelected() && this.slotMode.getSelectedValue().equals("PreAttack")) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
               }

               if (NoSlowRestItemOption.SPAM.isSelected() && this.spamMode.getSelectedValue().equals("PreAttack")) {
                  mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
               }

               if (NoSlowRestItemOption.BUG.isSelected()) {
                  boolean flag2 = false;
                  event.setAllowRightClick(flag2);
                  if (this.bugBypassState != 3) {
                     mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                     mc.thePlayer.stopUsingItem();
                     mc.thePlayer.closeScreen();
                     event.cancel();
                     this.bugBypassState = 3;
                  }
               }
            }
         }

         this.lastUsedItemStack = currentHeldItem;
      }
   }

   private boolean shouldSkipNoSlow(ItemStack stack) {
      boolean flag = true;
      return this.shouldSkipNoSlow(stack, flag);
   }

   private boolean shouldSkipNoSlow(ItemStack stack, boolean flag) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return stack == null
         || !mc.thePlayer.isUsingItem() && flag
         || mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F && this.onlyWhileMove.getValue()
         || ClientManagers.moduleManager.airStuck.isToggled()
         || BlockUtil.isScaffoldActiveAndValid();
   }

   private ItemStack getCurrentHeldItem() {
      return mc.thePlayer.getCurrentEquippedItem();
   }

   private void sendCustomInputPacket() {
      mc.thePlayer
         .sendQueue
         .addToSendQueue(
            new C0CPacketInput(
               this.inputStrafeLegit.getValue() ? mc.thePlayer.moveStrafing : (float)this.inputStrafe.getValue(),
               this.inputForwardLegit.getValue() ? mc.thePlayer.moveForward : (float)this.inputForward.getValue(),
               this.inputJumpLegit.getValue() ? mc.thePlayer.movementInput.jump : this.inputJump.getValue(),
               this.inputSneakLegit.getValue() ? mc.thePlayer.movementInput.sneak : this.inputSneak.getValue()
            )
         );
   }

   @Override
   public void onAttackEntity(EventAttackEntity event) {
   }

   private boolean isSwitchCustomItem(ItemStack stack) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (stack == null) {
         return false;
      } else {
         Item item = stack.getItem();
         if (!this.isBowItem(item) || !NoSlowBowOption.SWITCH_CUSTOM.isSelected()) {
            if (!this.isConsumableRestItem(item, stack) || !NoSlowRestItemOption.SWITCH_CUSTOM.isSelected()) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean isConsumableRestItem(Item item, ItemStack stack) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return (item instanceof ItemFood || item instanceof ItemPotion && !ItemPotion.isSplash(stack.getMetadata()) || item instanceof ItemBucketMilk)
         && this.isLicenseTimestampValid();
   }

   private boolean isBowItem(Item item) {
      return item instanceof ItemBow;
   }

   private boolean isSwordItem(Item item) {
      return item instanceof ItemSword;
   }

   private boolean isInputSneakVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.inputSneakLegit.isVisible() && !this.inputSneakLegit.getValue();
   }

   private boolean isInputStrafeVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.inputStrafeLegit.isVisible() && !this.inputStrafeLegit.getValue();
   }

   private boolean isInputForwardVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.inputForwardLegit.isVisible() && !this.inputForwardLegit.getValue();
   }

   private boolean isRestCustomFacingZVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowRestItemOption.INVALID_PACKET.isSelected() && this.restCustomFacing.getValue();
   }

   private boolean isRestCustomFacingYVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowRestItemOption.INVALID_PACKET.isSelected() && this.restCustomFacing.getValue();
   }

   private boolean isRestCustomFacingXVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowRestItemOption.INVALID_PACKET.isSelected() && this.restCustomFacing.getValue();
   }

   private boolean isRestCustomBlockPosYVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowRestItemOption.INVALID_PACKET.isSelected() && this.restCustomBlockPos.getValue();
   }

   private boolean isRestCustomBlockPosXVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowRestItemOption.INVALID_PACKET.isSelected() && this.restCustomBlockPos.getValue();
   }

   private boolean isRestEnumFacingIdVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowRestItemOption.INVALID_PACKET.isSelected() && this.restEditFacing.getValue();
   }

   private boolean isRestSlowdownIntervalGroundVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return !this.restSlowdownInterval.getSelectedValue().equals("None") && NoSlowRestItemOption.SLOWDOWN.isSelected() && this.enableIntervals.getValue();
   }

   private boolean isRestSlowdownIntervalAirVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return !this.restSlowdownInterval.getSelectedValue().equals("None") && NoSlowRestItemOption.SLOWDOWN.isSelected() && this.enableIntervals.getValue();
   }

   private boolean isBowCustomFacingZVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowBowOption.INVALID_PACKET.isSelected() && this.bowCustomFacing.getValue();
   }

   private boolean isBowCustomFacingYVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowBowOption.INVALID_PACKET.isSelected() && this.bowCustomFacing.getValue();
   }

   private boolean isBowCustomFacingXVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowBowOption.INVALID_PACKET.isSelected() && this.bowCustomFacing.getValue();
   }

   private boolean isBowCustomBlockPosZVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowBowOption.INVALID_PACKET.isSelected() && this.bowCustomBlockPos.getValue();
   }

   private boolean isBowCustomBlockPosYVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowBowOption.INVALID_PACKET.isSelected() && this.bowCustomBlockPos.getValue();
   }

   private boolean isBowCustomBlockPosXVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowBowOption.INVALID_PACKET.isSelected() && this.bowCustomBlockPos.getValue();
   }

   private boolean isBowEnumFacingIdVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowBowOption.INVALID_PACKET.isSelected() && this.bowEditFacing.getValue();
   }

   private boolean isBowSlowdownIntervalGroundVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return !this.bowSlowdownInterval.getSelectedValue().equals("None") && NoSlowBowOption.SLOWDOWN.isSelected() && this.enableIntervals.getValue();
   }

   private boolean isBowSlowdownIntervalAirVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return !this.bowSlowdownInterval.getSelectedValue().equals("None") && NoSlowBowOption.SLOWDOWN.isSelected() && this.enableIntervals.getValue();
   }

   private boolean isBowSlowdownIntervalVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowBowOption.SLOWDOWN.isSelected() && this.enableIntervals.getValue();
   }

   private boolean isSwordCustomFacingZVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowSwordOption.INVALID_PACKET.isSelected() && this.swordCustomFacing.getValue();
   }

   private boolean isSwordCustomFacingYVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowSwordOption.INVALID_PACKET.isSelected() && this.swordCustomFacing.getValue();
   }

   private boolean isSwordCustomFacingXVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowSwordOption.INVALID_PACKET.isSelected() && this.swordCustomFacing.getValue();
   }

   private boolean isSwordCustomBlockPosZVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowSwordOption.INVALID_PACKET.isSelected() && this.swordCustomBlockPos.getValue();
   }

   private boolean isSwordCustomBlockPosYVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowSwordOption.INVALID_PACKET.isSelected() && this.swordCustomBlockPos.getValue();
   }

   private boolean isSwordCustomBlockPosXVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowSwordOption.INVALID_PACKET.isSelected() && this.swordCustomBlockPos.getValue();
   }

   private boolean isSwordEnumFacingIdVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return NoSlowSwordOption.INVALID_PACKET.isSelected() && this.swordEditFacing.getValue();
   }

   private boolean isSwordSlowdownIntervalGroundVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return !this.swordSlowdownInterval.getSelectedValue().equals("None") && NoSlowSwordOption.SLOWDOWN.isSelected() && this.enableIntervals.getValue();
   }

   private boolean isLicenseTimestampValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
      Fly.getCachedSettings();
      boolean flag = false;
      if (timestampBytes != null) {
         long l = 0L;

         for (int i = 0; i < 8; i++) {
            l <<= 8;
            l |= (long)(timestampBytes[i] & 255);
         }

         long l2 = System.currentTimeMillis() - l;
         String str = "518400000";
         long l3 = 0L;
         int n = 0;
         int length = str.length();

         while (n < length) {
            int n2 = Character.digit(str.charAt(n++), 10);
            l3 *= 10L;
            l3 -= (long)n2;
         }

         l3 = -l3;
         flag = l2 <= l3;
      }

      return flag;
   }

}
