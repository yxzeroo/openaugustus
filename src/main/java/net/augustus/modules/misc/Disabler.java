package net.augustus.modules.misc;

import net.minecraft.util.MovingObjectPosition;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.events.listeners.MotionListener;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.events.EventClick;
import net.augustus.settings.RangeSetting;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.events.EventPacketDelay;
import net.augustus.settings.options.DisablerEntityActionOption;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.settings.options.DisablerDiggingActionOption;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.events.EventSwing;
import net.augustus.settings.options.DisablerOutgoingPacketOption;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.Augustus;
import net.augustus.settings.options.DisablerIncomingPacketOption;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.options.DisablerPingSpoofPacketOption;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.events.EventMotion;
import net.augustus.events.listeners.SwingListener;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Disabler extends Module implements UpdateListener, RenderListener, PacketListener, TickListener, MotionListener, ClickListener, SwingListener, PacketDelayListener {
   private final DoubleValue customPacketCanceling;
   private final DoubleValue incomingPackets;
   private final MultiOptionSetting incomingPacketsToCancel;
   private final DoubleValue outgoingPackets;
   private final MultiOptionSetting outGoingPacketsToCancel;
   private final DoubleValue detailedPackets;
   private final MultiOptionSetting entityActionsToCancel;
   private final MultiOptionSetting diggingPacket;
   private final BooleanValue pingSpoof;
   private final MultiOptionSetting pingSpoofPackets;
   private final RangeSetting delay;
   private final DoubleValue place;
   private final BooleanValue place1;
   private final BooleanValue notWhileAttack1;
   private final BooleanValue notWhileScaffold1;
   private final BooleanValue onlyWhileMove1;
   private final BooleanValue onlyWhileSpeedModule1;
   private final BooleanValue place2;
   private final BooleanValue notWhileAttack2;
   private final BooleanValue notWhileScaffold2;
   private final BooleanValue onlyWhileMove2;
   private final BooleanValue onlyWhileSpeedModule2;
   private final BooleanValue place3;
   private final BooleanValue notWhileAttack3;
   private final BooleanValue notWhileScaffold3;
   private final BooleanValue onlyWhileMove3;
   private final BooleanValue onlyWhileSpeedModule3;
   private final BooleanValue royalPixels;
   private final BooleanValue spectate;
   private final BooleanValue ride;
   private final BooleanValue karhuClicking;
   private final BooleanValue mmcReachSemi;
   private final BooleanValue onGroundTrue;
   private final BooleanValue tp;
   private final RandomizedRangeSettings tpDelay;
   private final RandomizedRangeSettings tpy;
   private final RandomizedRangeSettings tpxz;
   public final BooleanValue test;
   public final BooleanValue resourcePack;
   private final BooleanValue hover;
   private final BooleanValue grimSpec;
   private final BooleanValue grimSpecAlways;
   private final BooleanValue minibloxMoveFix;
   private final BooleanValue sneak;
   private final BooleansSetting sneakType;
   private final StringValue sneakDelay;
   private final CopyOnWriteArrayList spoofedPackets;
   private final HashMap spoofedPacketTimestamps;
   private int transactionCounter;
   private int sneakTickCounter;
   private boolean cancelingTransactions;
   private final CopyOnWriteArrayList heldTransactionPackets;
   private final TimeHelper unusedTimerA;
   private boolean attackedThisTick;
   private boolean pendingSwingDigPackets;
   private final TimeHelper positionJitterTimer;
   private final TimeHelper unusedTimerB;
   private final CopyOnWriteArrayList unusedPacketList;
   private boolean unusedStateFlag;

   public Disabler() {
       super("Disabler", "Deaktivierer", new Color(73, 127, 163), Categorys.MISC);
      this.customPacketCanceling = new DoubleValue(this, "CustomPacketCanceling");
      this.incomingPackets = new DoubleValue(this, "IncomingPackets", this.customPacketCanceling);
      this.incomingPacketsToCancel = new DisablerIncomingPacketCancelSetting(this, this, "IncomingPacketsToCancel", DisablerIncomingPacketOption.values(), this.incomingPackets);
      this.outgoingPackets = new DoubleValue(this, "OutgoingPackets", this.customPacketCanceling);
      this.outGoingPacketsToCancel = new DisablerOutgoingPacketCancelSetting(this, this, "OutGoingPacketsToCancel", DisablerOutgoingPacketOption.values(), this.outgoingPackets);
      this.detailedPackets = new DoubleValue(this, "DetailedPackets", this.customPacketCanceling);
      this.entityActionsToCancel = new MultiOptionSetting(this, "EntityActionsToCancel", DisablerEntityActionOption.values(), this.detailedPackets);
      this.diggingPacket = new MultiOptionSetting(this, "DiggingPacket", DisablerDiggingActionOption.values(), this.detailedPackets);
      this.pingSpoof = new BooleanValue(this, "PingSpoof", false);
      MultiOptionSetting multiOptionSetting = new MultiOptionSetting(this, "PingSpoofPackets", DisablerPingSpoofPacketOption.values());
      BooleanValue pingSpoof2 = this.pingSpoof;
      this.pingSpoof.getClass();
      BooleanValue booleanValue = pingSpoof2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.pingSpoofPackets = multiOptionSetting.visibleWhen(condition);
      RangeSetting rangeSetting = new RangeSetting(this, "Delay", 1000.0, 1500.0, 1.0, 10000.0, 0);
      pingSpoof2 = this.pingSpoof;
      this.pingSpoof.getClass();
      BooleanValue booleanValue2 = pingSpoof2;
      SettingVisibilityCondition condition2 = () -> booleanValue2.getValue();
      this.delay = rangeSetting.visibleWhen(condition2);
      this.place = new DoubleValue(this, "Place");
      this.place1 = new BooleanValue(this, "Place1", false, this.place);
      BooleanValue booleanValue3 = new BooleanValue(this, "NotWhileAttack1", true, this.place);
      String str = "NotWhileAttack";
      BooleanValue booleanValue4 = (BooleanValue)booleanValue3.withDisplayName(str);
      BooleanValue place12 = this.place1;
      this.place1.getClass();
      BooleanValue booleanValue5 = place12;
      SettingVisibilityCondition condition3 = () -> booleanValue5.getValue();
      this.notWhileAttack1 = (BooleanValue)booleanValue4.withVisibility(condition3);
      BooleanValue booleanValue6 = new BooleanValue(this, "NotWhileScaffold1", true, this.place);
      String str2 = "NotWhileScaffold";
      BooleanValue booleanValue7 = (BooleanValue)booleanValue6.withDisplayName(str2);
      BooleanValue place13 = this.place1;
      this.place1.getClass();
      BooleanValue booleanValue8 = place13;
      SettingVisibilityCondition condition4 = () -> booleanValue8.getValue();
      this.notWhileScaffold1 = (BooleanValue)booleanValue7.withVisibility(condition4);
      BooleanValue booleanValue9 = new BooleanValue(this, "OnlyWhileMove1", true, this.place);
      String str3 = "OnlyWhileMove";
      BooleanValue booleanValue10 = (BooleanValue)booleanValue9.withDisplayName(str3);
      BooleanValue place14 = this.place1;
      this.place1.getClass();
      BooleanValue booleanValue11 = place14;
      SettingVisibilityCondition condition5 = () -> booleanValue11.getValue();
      this.onlyWhileMove1 = (BooleanValue)booleanValue10.withVisibility(condition5);
      BooleanValue booleanValue12 = new BooleanValue(this, "OnlyWhileSpeedModule1", true, this.place);
      String str4 = "OnlyWhileSpeedModule";
      BooleanValue booleanValue13 = (BooleanValue)booleanValue12.withDisplayName(str4);
      BooleanValue place15 = this.place1;
      this.place1.getClass();
      BooleanValue booleanValue14 = place15;
      SettingVisibilityCondition condition6 = () -> booleanValue14.getValue();
      this.onlyWhileSpeedModule1 = (BooleanValue)booleanValue13.withVisibility(condition6);
      this.place2 = new BooleanValue(this, "Place2", false, this.place);
      BooleanValue booleanValue15 = new BooleanValue(this, "NotWhileAttack2", true, this.place);
      String str5 = "NotWhileAttack";
      BooleanValue booleanValue16 = (BooleanValue)booleanValue15.withDisplayName(str5);
      BooleanValue place22 = this.place2;
      this.place2.getClass();
      BooleanValue booleanValue17 = place22;
      SettingVisibilityCondition condition7 = () -> booleanValue17.getValue();
      this.notWhileAttack2 = (BooleanValue)booleanValue16.withVisibility(condition7);
      BooleanValue booleanValue18 = new BooleanValue(this, "NotWhileScaffold2", true, this.place);
      String str6 = "NotWhileScaffold";
      BooleanValue booleanValue19 = (BooleanValue)booleanValue18.withDisplayName(str6);
      BooleanValue place23 = this.place2;
      this.place2.getClass();
      BooleanValue booleanValue20 = place23;
      SettingVisibilityCondition condition8 = () -> booleanValue20.getValue();
      this.notWhileScaffold2 = (BooleanValue)booleanValue19.withVisibility(condition8);
      BooleanValue booleanValue21 = new BooleanValue(this, "OnlyWhileMove2", true, this.place);
      String str7 = "OnlyWhileMove";
      BooleanValue booleanValue22 = (BooleanValue)booleanValue21.withDisplayName(str7);
      BooleanValue place24 = this.place2;
      this.place2.getClass();
      BooleanValue booleanValue23 = place24;
      SettingVisibilityCondition condition9 = () -> booleanValue23.getValue();
      this.onlyWhileMove2 = (BooleanValue)booleanValue22.withVisibility(condition9);
      BooleanValue booleanValue24 = new BooleanValue(this, "OnlyWhileSpeedModule2", true, this.place);
      String str8 = "OnlyWhileSpeedModule";
      BooleanValue booleanValue25 = (BooleanValue)booleanValue24.withDisplayName(str8);
      BooleanValue place25 = this.place2;
      this.place2.getClass();
      BooleanValue booleanValue26 = place25;
      SettingVisibilityCondition condition10 = () -> booleanValue26.getValue();
      this.onlyWhileSpeedModule2 = (BooleanValue)booleanValue25.withVisibility(condition10);
      this.place3 = new BooleanValue(this, "Place3", false, this.place);
      BooleanValue booleanValue27 = new BooleanValue(this, "NotWhileAttack3", true, this.place);
      String str9 = "NotWhileAttack";
      BooleanValue booleanValue28 = (BooleanValue)booleanValue27.withDisplayName(str9);
      BooleanValue place32 = this.place3;
      this.place3.getClass();
      BooleanValue booleanValue29 = place32;
      SettingVisibilityCondition condition11 = () -> booleanValue29.getValue();
      this.notWhileAttack3 = (BooleanValue)booleanValue28.withVisibility(condition11);
      BooleanValue booleanValue30 = new BooleanValue(this, "NotWhileScaffold3", true, this.place);
      String str10 = "NotWhileScaffold";
      BooleanValue booleanValue31 = (BooleanValue)booleanValue30.withDisplayName(str10);
      BooleanValue place33 = this.place3;
      this.place3.getClass();
      BooleanValue booleanValue32 = place33;
      SettingVisibilityCondition condition12 = () -> booleanValue32.getValue();
      this.notWhileScaffold3 = (BooleanValue)booleanValue31.withVisibility(condition12);
      BooleanValue booleanValue33 = new BooleanValue(this, "OnlyWhileMove3", true, this.place);
      String str11 = "OnlyWhileMove";
      BooleanValue booleanValue34 = (BooleanValue)booleanValue33.withDisplayName(str11);
      BooleanValue place34 = this.place3;
      this.place3.getClass();
      BooleanValue booleanValue35 = place34;
      SettingVisibilityCondition condition13 = () -> booleanValue35.getValue();
      this.onlyWhileMove3 = (BooleanValue)booleanValue34.withVisibility(condition13);
      BooleanValue booleanValue36 = new BooleanValue(this, "OnlyWhileSpeedModule3", true, this.place);
      String str12 = "OnlyWhileSpeedModule";
      BooleanValue booleanValue37 = (BooleanValue)booleanValue36.withDisplayName(str12);
      BooleanValue place35 = this.place3;
      this.place3.getClass();
      BooleanValue booleanValue38 = place35;
      SettingVisibilityCondition condition14 = () -> booleanValue38.getValue();
      this.onlyWhileSpeedModule3 = (BooleanValue)booleanValue37.withVisibility(condition14);
      this.royalPixels = new BooleanValue(this, "RoyalPixels", false);
      this.spectate = new BooleanValue(this, "Spectate", false);
      this.ride = new BooleanValue(this, "Ride", false);
      this.karhuClicking = new BooleanValue(this, "KarhuClicking", false);
      this.mmcReachSemi = new BooleanValue(this, "MMCReachSemi", false);
      this.onGroundTrue = new BooleanValue(this, "OnGroundTrue", false);
      this.tp = new BooleanValue(this, "Tp", false);
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "TPDelay", 1000.0, 1500.0, 1.0, 10000.0, 0)).withoutNotInARow();
      BooleanValue tp2 = this.tp;
      this.tp.getClass();
      BooleanValue booleanValue39 = tp2;
      SettingVisibilityCondition condition15 = () -> booleanValue39.getValue();
      this.tpDelay = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition15);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this, "TPY", 1024.0, 2048.0, 1024.0, 2048.0, 0)).withoutNotInARow();
      BooleanValue tp3 = this.tp;
      this.tp.getClass();
      BooleanValue booleanValue40 = tp3;
      SettingVisibilityCondition condition16 = () -> booleanValue40.getValue();
      this.tpy = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition16);
      RandomizedRangeSettings randomizedRangeSettings3 = (new RandomizedRangeSettings(this, "TPXZ", 1024.0, 2048.0, 1024.0, 2048.0, 0)).withoutNotInARow();
      BooleanValue tp4 = this.tp;
      this.tp.getClass();
      BooleanValue booleanValue41 = tp4;
      SettingVisibilityCondition condition17 = () -> booleanValue41.getValue();
      this.tpxz = (RandomizedRangeSettings)randomizedRangeSettings3.withVisibility(condition17);
      this.test = new BooleanValue(this, "Test", false);
      this.resourcePack = new BooleanValue(this, "ResourcePack", false);
      this.hover = new BooleanValue(this, "Hover", false);
      this.grimSpec = new BooleanValue(this, "GrimSpec", false);
      BooleanValue booleanValue42 = new BooleanValue(this, "grimSpecAlways", false);
      BooleanValue grimSpec2 = this.grimSpec;
      this.grimSpec.getClass();
      BooleanValue booleanValue43 = grimSpec2;
      SettingVisibilityCondition condition18 = () -> booleanValue43.getValue();
      this.grimSpecAlways = (BooleanValue)booleanValue42.withVisibility(condition18);
      this.minibloxMoveFix = new BooleanValue(this, "MinibloxMoveFix", false);
      this.sneak = new BooleanValue(this, "Sneak", false);
      BooleansSetting booleansSetting = new BooleansSetting(this, "SneakType", "StartStop", new String[]{"StartStop", "Start", "Stop"});
      BooleanValue sneak2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue44 = sneak2;
      SettingVisibilityCondition condition19 = () -> booleanValue44.getValue();
      this.sneakType = (BooleansSetting)booleansSetting.withVisibility(condition19);
      StringValue stringValue = new StringValue(this, "SneakDelay", 10.0, 1.0, 40.0, 0);
      BooleanValue sneak3 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue45 = sneak3;
      SettingVisibilityCondition condition20 = () -> booleanValue45.getValue();
      this.sneakDelay = stringValue.visibleWhen(condition20);
      this.spoofedPackets = new CopyOnWriteArrayList();
      this.spoofedPacketTimestamps = new HashMap();
      this.transactionCounter = 0;
      this.sneakTickCounter = 0;
      this.cancelingTransactions = false;
      this.heldTransactionPackets = new CopyOnWriteArrayList();
      this.unusedTimerA = new TimeHelper();
      this.attackedThisTick = false;
      this.pendingSwingDigPackets = false;
      this.positionJitterTimer = new TimeHelper();
      this.unusedTimerB = new TimeHelper();
      this.unusedPacketList = new CopyOnWriteArrayList();
      this.unusedStateFlag = false;

      for (DisablerIncomingPacketOption option : DisablerIncomingPacketOption.values()) {
         DoubleValue doubleValue = new DoubleValue(this, DisablerIncomingPacketOption.getOptionName(option), this.incomingPackets);
         DisablerIncomingPacketOption option2 = option;
         SettingVisibilityCondition condition21 = () -> DisablerIncomingPacketOption.isOptionSelected(option2);
         DoubleValue doubleValue2 = doubleValue.visibleWhen(condition21);
         StringValue stringValue2 = new StringValue(this, DisablerIncomingPacketOption.getOptionName(option) + "Interval", 0.0, 0.0, 20.0, 0, doubleValue2);
         String str13 = "Interval";
         StringValue stringValue3 = stringValue2.named(str13);
         DisablerIncomingPacketOption.setIntervalSetting(option, stringValue3);
         BooleanValue booleanValue46 = new BooleanValue(this, DisablerIncomingPacketOption.getOptionName(option) + "InvertInterval", false, doubleValue2);
         String str14 = "InvertInterval";
         BooleanValue booleanValue47 = (BooleanValue)booleanValue46.withDisplayName(str14);
         DisablerIncomingPacketOption.setInvertIntervalSetting(option, booleanValue47);
         BooleanValue booleanValue48 = new BooleanValue(this, DisablerIncomingPacketOption.getOptionName(option) + "Always", true, doubleValue2);
         String str15 = "Always";
         BooleanValue booleanValue49 = (BooleanValue)booleanValue48.withDisplayName(str15);
         DisablerIncomingPacketOption.setAlwaysSetting(option, booleanValue49);
         RangeSetting rangeSetting2 = new RangeSetting(
            this, DisablerIncomingPacketOption.getOptionName(option) + "StartTick", DisablerIncomingPacketOption.getOptionName(option) + "EndTick", 0.0, 100.0, 0.0, 100.0, 0, doubleValue2
         );
         String str16 = "EndTick";
         String str17 = "StartTick";
         RangeSetting rangeSetting3 = rangeSetting2.withBoundNames(str17, str16);
         DisablerIncomingPacketOption option3 = option;
         SettingVisibilityCondition condition22 = () -> isIncomingTickRangeVisible(option3);
         RangeSetting rangeSetting4 = rangeSetting3.visibleWhen(condition22);
         DisablerIncomingPacketOption.setTickRangeSetting(option, rangeSetting4);
         BooleansSetting booleansSetting2 = new BooleansSetting(
            this, DisablerIncomingPacketOption.getOptionName(option) + "CancelCondition", "Always", new String[]{"Always", "OnlyOnGround", "OnlyInAir"}, doubleValue2
         );
         String str18 = "CancelCondition";
         BooleansSetting booleansSetting3 = (BooleansSetting)booleansSetting2.withDisplayName(str18);
         DisablerIncomingPacketOption.setCancelConditionSetting(option, booleansSetting3);
      }

      for (DisablerOutgoingPacketOption option4 : DisablerOutgoingPacketOption.values()) {
         DoubleValue doubleValue3 = new DoubleValue(this, DisablerOutgoingPacketOption.getOptionName(option4), this.outgoingPackets);
         DisablerOutgoingPacketOption option5 = option4;
         SettingVisibilityCondition condition23 = () -> DisablerOutgoingPacketOption.isOptionSelected(option5);
         DoubleValue doubleValue4 = doubleValue3.visibleWhen(condition23);
         StringValue stringValue4 = new StringValue(this, DisablerOutgoingPacketOption.getOptionName(option4) + "Interval", 0.0, 0.0, 20.0, 0, doubleValue4);
         String str19 = "Interval";
         StringValue stringValue5 = stringValue4.named(str19);
         DisablerOutgoingPacketOption.setIntervalSetting(option4, stringValue5);
         BooleanValue booleanValue50 = new BooleanValue(this, DisablerOutgoingPacketOption.getOptionName(option4) + "InvertInterval", false, doubleValue4);
         String str20 = "InvertInterval";
         BooleanValue booleanValue51 = (BooleanValue)booleanValue50.withDisplayName(str20);
         DisablerOutgoingPacketOption.setInvertIntervalSetting(option4, booleanValue51);
         BooleanValue booleanValue52 = new BooleanValue(this, DisablerOutgoingPacketOption.getOptionName(option4) + "Always", true, doubleValue4);
         String str21 = "Always";
         BooleanValue booleanValue53 = (BooleanValue)booleanValue52.withDisplayName(str21);
         DisablerOutgoingPacketOption.setAlwaysSetting(option4, booleanValue53);
         RangeSetting rangeSetting5 = new RangeSetting(
            this, DisablerOutgoingPacketOption.getOptionName(option4) + "StartTick", DisablerOutgoingPacketOption.getOptionName(option4) + "EndTick", 0.0, 100.0, 0.0, 100.0, 0, doubleValue4
         );
         String str22 = "EndTick";
         String str23 = "StartTick";
         RangeSetting rangeSetting6 = rangeSetting5.withBoundNames(str23, str22);
         DisablerOutgoingPacketOption option6 = option4;
         SettingVisibilityCondition condition24 = () -> isOutgoingTickRangeVisible(option6);
         RangeSetting rangeSetting7 = rangeSetting6.visibleWhen(condition24);
         DisablerOutgoingPacketOption.setTickRangeSetting(option4, rangeSetting7);
         BooleansSetting booleansSetting4 = new BooleansSetting(
            this, DisablerOutgoingPacketOption.getOptionName(option4) + "CancelCondition", "Always", new String[]{"Always", "OnlyOnGround", "OnlyInAir"}, doubleValue4
         );
         String str24 = "CancelCondition";
         BooleansSetting booleansSetting5 = (BooleansSetting)booleansSetting4.withDisplayName(str24);
         DisablerOutgoingPacketOption.setCancelConditionSetting(option4, booleansSetting5);
      }

      for (DisablerEntityActionOption option7 : DisablerEntityActionOption.values()) {
         DoubleValue doubleValue5 = new DoubleValue(this, DisablerEntityActionOption.getOptionName(option7), this.detailedPackets);
         SettingVisibilityCondition condition25 = () -> DisablerEntityActionOption.isOptionSelected(option7);
         DoubleValue doubleValue6 = doubleValue5.visibleWhen(condition25);
         StringValue stringValue6 = new StringValue(this, DisablerEntityActionOption.getOptionName(option7) + "Interval", 0.0, 0.0, 20.0, 0, doubleValue6);
         String str25 = "Interval";
         StringValue stringValue7 = stringValue6.named(str25);
         DisablerEntityActionOption.setIntervalSetting(option7, stringValue7);
         BooleanValue booleanValue54 = new BooleanValue(this, DisablerEntityActionOption.getOptionName(option7) + "InvertInterval", false, doubleValue6);
         String str26 = "InvertInterval";
         BooleanValue booleanValue55 = (BooleanValue)booleanValue54.withDisplayName(str26);
         DisablerEntityActionOption.setInvertIntervalSetting(option7, booleanValue55);
         BooleanValue booleanValue56 = new BooleanValue(this, DisablerEntityActionOption.getOptionName(option7) + "Always", true, doubleValue6);
         String str27 = "Always";
         BooleanValue booleanValue57 = (BooleanValue)booleanValue56.withDisplayName(str27);
         DisablerEntityActionOption.setAlwaysSetting(option7, booleanValue57);
         RangeSetting rangeSetting8 = new RangeSetting(
            this, DisablerEntityActionOption.getOptionName(option7) + "StartTick", DisablerEntityActionOption.getOptionName(option7) + "EndTick", 0.0, 100.0, 0.0, 100.0, 0, doubleValue6
         );
         String str28 = "EndTick";
         String str29 = "StartTick";
         RangeSetting rangeSetting9 = rangeSetting8.withBoundNames(str29, str28);
         SettingVisibilityCondition condition26 = () -> isEntityActionTickRangeVisible(option7);
         RangeSetting rangeSetting10 = rangeSetting9.visibleWhen(condition26);
         DisablerEntityActionOption.setTickRangeSetting(option7, rangeSetting10);
         BooleansSetting booleansSetting6 = new BooleansSetting(
            this, DisablerEntityActionOption.getOptionName(option7) + "CancelCondition", "Always", new String[]{"Always", "OnlyOnGround", "OnlyInAir"}, doubleValue6
         );
         String str30 = "CancelCondition";
         BooleansSetting booleansSetting7 = (BooleansSetting)booleansSetting6.withDisplayName(str30);
         DisablerEntityActionOption.setCancelConditionSetting(option7, booleansSetting7);
      }

      for (DisablerDiggingActionOption option8 : DisablerDiggingActionOption.values()) {
         DoubleValue doubleValue7 = new DoubleValue(this, DisablerDiggingActionOption.getOptionName(option8), this.detailedPackets);
         SettingVisibilityCondition condition27 = () -> DisablerDiggingActionOption.isOptionSelected(option8);
         DoubleValue doubleValue8 = doubleValue7.visibleWhen(condition27);
         StringValue stringValue8 = new StringValue(this, DisablerDiggingActionOption.getOptionName(option8) + "Interval", 0.0, 0.0, 20.0, 0, doubleValue8);
         String str31 = "Interval";
         StringValue stringValue9 = stringValue8.named(str31);
         DisablerDiggingActionOption.setIntervalSetting(option8, stringValue9);
         BooleanValue booleanValue58 = new BooleanValue(this, DisablerDiggingActionOption.getOptionName(option8) + "InvertInterval", false, doubleValue8);
         String str32 = "InvertInterval";
         BooleanValue booleanValue59 = (BooleanValue)booleanValue58.withDisplayName(str32);
         DisablerDiggingActionOption.setInvertIntervalSetting(option8, booleanValue59);
         BooleanValue booleanValue60 = new BooleanValue(this, DisablerDiggingActionOption.getOptionName(option8) + "Always", true, doubleValue8);
         String str33 = "Always";
         BooleanValue booleanValue61 = (BooleanValue)booleanValue60.withDisplayName(str33);
         DisablerDiggingActionOption.setAlwaysSetting(option8, booleanValue61);
         RangeSetting rangeSetting11 = new RangeSetting(
            this, DisablerDiggingActionOption.getOptionName(option8) + "StartTick", DisablerDiggingActionOption.getOptionName(option8) + "EndTick", 0.0, 100.0, 0.0, 100.0, 0, doubleValue8
         );
         String str34 = "EndTick";
         String str35 = "StartTick";
         RangeSetting rangeSetting12 = rangeSetting11.withBoundNames(str35, str34);
         SettingVisibilityCondition condition28 = () -> isDiggingTickRangeVisible(option8);
         RangeSetting rangeSetting13 = rangeSetting12.visibleWhen(condition28);
         DisablerDiggingActionOption.setTickRangeSetting(option8, rangeSetting13);
         BooleansSetting booleansSetting8 = new BooleansSetting(
            this, DisablerDiggingActionOption.getOptionName(option8) + "CancelCondition", "Always", new String[]{"Always", "OnlyOnGround", "OnlyInAir"}, doubleValue8
         );
         String str36 = "CancelCondition";
         BooleansSetting booleansSetting9 = (BooleansSetting)booleansSetting8.withDisplayName(str36);
         DisablerDiggingActionOption.setCancelConditionSetting(option8, booleansSetting9);
      }
   }

   public static void noopHook() {
   }

   @Override
   public void onEnable() {
      this.spoofedPackets.clear();
      this.spoofedPacketTimestamps.clear();
      this.tpDelay.nextValue();
      this.tpy.nextValue();
      this.tpxz.nextValue();
      this.positionJitterTimer.reset();
      this.cancelingTransactions = false;
   }

   @Override
   public void onDisable() {
      if (this.pingSpoof.getValue() && mc.thePlayer != null && mc.theWorld != null && !this.spoofedPackets.isEmpty()) {
         Iterator it = this.spoofedPackets.iterator();

         while (it.hasNext()) {
            Packet packet = (Packet)it.next();

            try {
               mc.thePlayer.sendQueue.addToSendQueueUnchecked(packet);
               it.remove();
               this.spoofedPacketTimestamps.remove(packet);
            } catch (Exception e) {
            }
         }
      }

      this.spoofedPackets.clear();
      if (!this.heldTransactionPackets.isEmpty()
         && mc.thePlayer != null
         && mc.theWorld != null
         && mc.thePlayer.sendQueue != null
         && mc.getNetHandler() != null
         && mc.getNetHandler().getNetworkManager() != null
         && mc.getNetHandler().getNetworkManager().getNetHandler() != null) {
         this.heldTransactionPackets.forEach(obj -> {
            try {
               ((Packet)obj).processPacket(mc.getNetHandler().getNetworkManager().getNetHandler());
            } catch (Throwable e2) {
            }
         });
         this.heldTransactionPackets.clear();
      }
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 43;
      this.toggleEventSubscription(b);
      byte b2 = 32;
      this.toggleEventSubscription(b2);
      byte b3 = 26;
      this.toggleEventSubscription(b3);
      byte b4 = 40;
      this.toggleEventSubscription(b4);
      byte b5 = 18;
      this.toggleEventSubscription(b5);
      byte b6 = 7;
      this.toggleEventSubscription(b6);
      byte b7 = 39;
      this.toggleEventSubscription(b7);
      byte b8 = 25;
      this.toggleEventSubscription(b8);
   }

   @Override
   public void onPacketDelay(EventPacketDelay event) {
      if (this.mmcReachSemi.getValue()) {
         short s = 500;
         event.setOutgoingDelayMs(s);
      }
   }

   @Override
   public void onPacketDelay2(EventPacketDelay event) {
   }

   @Override
   public void onClick(EventClick event) {
      if (this.test.getValue() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
         Entity entityHit = mc.objectMouseOver.entityHit;
         if (entityHit instanceof EntityBoat || entityHit instanceof EntityMinecart) {
            if (mc.thePlayer.ticksExisted % 6 == 0) {
               byte b = 1;
               event.addLeftClicks(b);
            }

            float f = entityHit instanceof EntityBoat ? ((EntityBoat)entityHit).getDamageTaken() : ((EntityMinecart)entityHit).getDamage();
            float attributeValue = (float)mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
            float modifierForCreature = EnchantmentHelper.getModifierForCreature(mc.thePlayer.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
            if (attributeValue > 0.0F || modifierForCreature > 0.0F) {
               if (mc.thePlayer.fallDistance > 0.0F && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater()) {
                  mc.thePlayer.isPotionActive(Potion.blindness);
               }

               attributeValue += modifierForCreature;
               if (f > 40.0F - attributeValue * 10.0F) {
                  byte b2 = 1;
                  event.addLeftClicks(b2);
                  byte b3 = 1;
                  event.addRightClicks(b3);
               }
            }
         }
      }
   }

   @Override
   public void onSwing(EventSwing event) {
      if (this.test.getValue() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
         Entity entityHit = mc.objectMouseOver.entityHit;
         if (entityHit instanceof EntityBoat || entityHit instanceof EntityMinecart) {
            if (mc.thePlayer.ticksExisted % 6 == 0) {
               boolean flag = true;
               event.setCancelClientSwing(flag);
            }

            float f = entityHit instanceof EntityBoat ? ((EntityBoat)entityHit).getDamageTaken() : ((EntityMinecart)entityHit).getDamage();
            float attributeValue = (float)mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
            float modifierForCreature = EnchantmentHelper.getModifierForCreature(mc.thePlayer.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
            if (attributeValue > 0.0F || modifierForCreature > 0.0F) {
               if (mc.thePlayer.fallDistance > 0.0F && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater()) {
                  mc.thePlayer.isPotionActive(Potion.blindness);
               }

               attributeValue += modifierForCreature;
               if (f > 40.0F - attributeValue * 10.0F) {
                  boolean flag2 = true;
                  event.setCancelClientSwing(flag2);
               }
            }
         }
      }
   }

   @Override
   public void onMotion(EventMotion event) {
      if (this.sneak.getValue()) {
         this.sneakTickCounter++;
         if (this.sneakTickCounter % (int)Math.max(this.sneakDelay.getValue(), 1.0) == 0) {
            if (this.sneakType.getSelectedValue().contains("Start")) {
               mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            }

            if (this.sneakType.getSelectedValue().contains("Stop")) {
               mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
         }
      }

      if (this.hover.getValue() && mc.thePlayer.ticksExisted % 2 == 0 && mc.thePlayer.onGround && event.getY() % 1.0 == 0.0) {
         double d = Math.random() * 1.0E-4;
         double d2 = event.getY() + d;
         event.setY(d2);
         boolean flag = false;
         event.setOnGround(flag);
      }

      if (this.tp.getValue()) {
         TimeHelper positionJitterTimer2 = this.positionJitterTimer;
         long lastIntValue = (long)this.tpDelay.getLastIntValue();
         if (positionJitterTimer2.reached2(lastIntValue)) {
            this.positionJitterTimer.reset();
            double y = event.getY();
            int lastIntValue2 = this.tpy.getLastIntValue();
            byte b = 100;
            byte b2 = 0;
            double d3 = y + (double)(lastIntValue2 * DefaultRandomGenerator.instance.nextInt2(b2, b) % 2) == 0.0 ? -1.0 : 1.0;
            event.setY(d3);
            y = event.getX();
            double d4 = this.tpxz.nextValue();
            byte b3 = 100;
            byte b4 = 0;
            double d5 = y + d4 * (double)DefaultRandomGenerator.instance.nextInt2(b4, b3) % 2.0 == 0.0 ? -1.0 : 1.0;
            event.setX(d5);
            y = event.getZ();
            double d6 = this.tpxz.nextValue();
            byte b5 = 100;
            byte b6 = 0;
            double d7 = y + d6 * (double)DefaultRandomGenerator.instance.nextInt2(b6, b5) % 2.0 == 0.0 ? -1.0 : 1.0;
            event.setZ(d7);
            this.tpDelay.nextValue();
            this.tpy.nextValue();
            return;
         }
      }
   }

   @Override
   public void onPacket4(EventPacket event) {
      Packet packet = event.getPacket();
      if (this.shouldCancelIncomingPacket(packet)) {
         event.cancel();
      }
   }

   @Override
   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (this.grimSpec.getValue()
         && mc.thePlayer != null
         && (packet instanceof S08PacketPlayerPosLook && mc.thePlayer.capabilities != null && mc.thePlayer.capabilities.isFlying || this.grimSpecAlways.getValue())) {
         this.cancelingTransactions = true;
      }

      if (packet instanceof S32PacketConfirmTransaction && this.cancelingTransactions && this.grimSpec.getValue()) {
         event.cancel();
         this.heldTransactionPackets.add(packet);
      }
   }

   @Override
   public void onPacket2(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)packet).getAction() == net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK) {
         this.attackedThisTick = true;
      }

      if (this.pingSpoof.getValue()) {
         Class clazz = packet.getClass();

         for (DisablerPingSpoofPacketOption option : DisablerPingSpoofPacketOption.values()) {
            if (clazz.equals(DisablerPingSpoofPacketOption.getPacketClass(option)) && DisablerPingSpoofPacketOption.isOptionSelected(option)) {
               this.spoofedPackets.add(packet);
               this.spoofedPacketTimestamps.put(packet, System.currentTimeMillis());
               event.cancel();
               break;
            }
         }
      }

      if (packet instanceof C03PacketPlayer && this.spectate.getValue()) {
         mc.thePlayer.sendQueue.addToSendQueue(new C18PacketSpectate(mc.thePlayer.getUniqueID()));
      }

      if (packet instanceof C03PacketPlayer && this.ride.getValue()) {
         mc.thePlayer.sendQueue.addToSendQueueUnchecked(packet);
         mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C0CPacketInput(mc.thePlayer.moveStrafing, mc.thePlayer.moveForward, mc.thePlayer.movementInput.jump, mc.thePlayer.movementInput.sneak));
         event.cancel();
      }

      if (this.karhuClicking.getValue()) {
         if (packet instanceof C0APacketAnimation && mc.objectMouseOver != null) {
            if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
               this.sendDuplicateDigPackets();
            } else if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
               this.pendingSwingDigPackets = true;
            }
         }

         if (packet instanceof C03PacketPlayer && this.pendingSwingDigPackets) {
            this.sendDuplicateDigPackets();
            this.pendingSwingDigPackets = false;
         }
      }

      if (this.onGroundTrue.getValue() && packet instanceof C03PacketPlayer) {
         C03PacketPlayer packet2 = (C03PacketPlayer)packet;
         packet2.setOnGround(true);
      }
   }

   private void sendDuplicateDigPackets() {
      mc.thePlayer
         .sendQueue
         .addToSendQueue(
            new C07PacketPlayerDigging(net.minecraft.network.play.client.C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, BlockPos.ORIGIN, EnumFacing.UP)
         );
      mc.thePlayer
         .sendQueue
         .addToSendQueue(
            new C07PacketPlayerDigging(net.minecraft.network.play.client.C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, BlockPos.ORIGIN, EnumFacing.UP)
         );
   }

   @Override
   public void onPacket5(EventPacket event) {
      Packet packet = event.getPacket();
      if (this.shouldCancelOutgoingPacket(packet)) {
         event.cancel();
      }

      if (packet instanceof C0BPacketEntityAction) {
         C0BPacketEntityAction packet2 = (C0BPacketEntityAction)packet;

         for (DisablerEntityActionOption option : DisablerEntityActionOption.values()) {
            if (packet2.getAction() == DisablerEntityActionOption.getAction(option) && DisablerEntityActionOption.isOptionSelected(option)) {
               DisablerEntityActionOption.incrementPacketCount(option);
               if (DisablerEntityActionOption.getCancelConditionSetting(option).getSelectedValue().equals("Always")
                  || DisablerEntityActionOption.getCancelConditionSetting(option).getSelectedValue().equals("OnlyOnGround") && mc.thePlayer.onGround
                  || DisablerEntityActionOption.getCancelConditionSetting(option).getSelectedValue().equals("OnlyInAir") && !mc.thePlayer.onGround) {
                  StringValue intervalSetting = DisablerEntityActionOption.getIntervalSetting(option);
                  BooleanValue invertIntervalSetting = DisablerEntityActionOption.getInvertIntervalSetting(option);
                  int packetCount = DisablerEntityActionOption.getPacketCount(option);
                  BooleanValue booleanValue = invertIntervalSetting;
                  StringValue stringValue = intervalSetting;
                  if (this.matchesCancelModulo(stringValue, booleanValue, packetCount)) {
                     if (!DisablerEntityActionOption.getAlwaysSetting(option).getValue()) {
                        if (!((double)mc.thePlayer.ticksExisted > DisablerEntityActionOption.getTickRangeSetting(option).getMinValue())) {
                           break;
                        }

                        double ticksExisted = (double)mc.thePlayer.ticksExisted;
                        RangeSetting tickRangeSetting = DisablerEntityActionOption.getTickRangeSetting(option);
                        if (!(ticksExisted < tickRangeSetting.getMaxValue())) {
                           break;
                        }
                     }

                     event.cancel();
                  }
               }
               break;
            }
         }
      }

      if (packet instanceof C07PacketPlayerDigging) {
         C07PacketPlayerDigging packet3 = (C07PacketPlayerDigging)packet;

         for (DisablerDiggingActionOption option2 : DisablerDiggingActionOption.values()) {
            if (packet3.getStatus() == DisablerDiggingActionOption.getAction(option2) && DisablerDiggingActionOption.isOptionSelected(option2)) {
               DisablerDiggingActionOption.incrementPacketCount(option2);
               if (DisablerDiggingActionOption.getCancelConditionSetting(option2).getSelectedValue().equals("Always")
                  || DisablerDiggingActionOption.getCancelConditionSetting(option2).getSelectedValue().equals("OnlyOnGround") && mc.thePlayer.onGround
                  || DisablerDiggingActionOption.getCancelConditionSetting(option2).getSelectedValue().equals("OnlyInAir") && !mc.thePlayer.onGround) {
                  StringValue intervalSetting2 = DisablerDiggingActionOption.getIntervalSetting(option2);
                  BooleanValue invertIntervalSetting2 = DisablerDiggingActionOption.getInvertIntervalSetting(option2);
                  int packetCount2 = DisablerDiggingActionOption.getPacketCount(option2);
                  BooleanValue booleanValue2 = invertIntervalSetting2;
                  StringValue stringValue2 = intervalSetting2;
                  if (this.matchesCancelModulo(stringValue2, booleanValue2, packetCount2)) {
                     if (!DisablerDiggingActionOption.getAlwaysSetting(option2).getValue()) {
                        if (!((double)mc.thePlayer.ticksExisted > DisablerDiggingActionOption.getTickRangeSetting(option2).getMinValue())) {
                           break;
                        }

                        double ticksExisted2 = (double)mc.thePlayer.ticksExisted;
                        RangeSetting tickRangeSetting2 = DisablerDiggingActionOption.getTickRangeSetting(option2);
                        if (!(ticksExisted2 < tickRangeSetting2.getMaxValue())) {
                           break;
                        }
                     }

                     event.cancel();
                  }
               }
               break;
            }
         }
      }

      if (this.mmcReachSemi.getValue() && packet instanceof C0FPacketConfirmTransaction) {
         this.transactionCounter++;
         if (this.transactionCounter % 2 == 0) {
            event.cancel();
         }
      }
   }

   @Override
   public void onUpdate() {
      if (this.minibloxMoveFix.getValue()) {
         mc.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput(mc.thePlayer.moveStrafing, mc.thePlayer.moveForward, mc.thePlayer.movementInput.jump, mc.thePlayer.movementInput.sneak));
      }
   }

   @Override
   public void onUpdate2() {
      if (this.royalPixels.getValue()) {
         C13PacketPlayerAbilities packet = new C13PacketPlayerAbilities();
         packet.setAllowFlying(true);
         packet.setFlying(true);
         mc.thePlayer.sendQueue.addToSendQueue(packet);
      }

      BooleanValue onlyWhileSpeedModule12 = this.onlyWhileSpeedModule1;
      BooleanValue onlyWhileMove12 = this.onlyWhileMove1;
      BooleanValue notWhileScaffold12 = this.notWhileScaffold1;
      BooleanValue notWhileAttack12 = this.notWhileAttack1;
      BooleanValue place12 = this.place1;
      if (this.shouldSendPlacePacket(place12, notWhileAttack12, notWhileScaffold12, onlyWhileMove12, onlyWhileSpeedModule12)) {
         mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0.0F, 0.5F, 0.0F));
         mc.thePlayer
            .sendQueue
            .addToSendQueueUnchecked(
               new C08PacketPlayerBlockPlacement(
                  new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.5, mc.thePlayer.posZ),
                  1,
                  new ItemStack(Blocks.stone.getItem(mc.theWorld, new BlockPos(-1, -1, -1))),
                  0.0F,
                  0.94F,
                  0.0F
               )
            );
      }

      BooleanValue onlyWhileSpeedModule22 = this.onlyWhileSpeedModule2;
      BooleanValue onlyWhileMove22 = this.onlyWhileMove2;
      BooleanValue notWhileScaffold22 = this.notWhileScaffold2;
      BooleanValue notWhileAttack22 = this.notWhileAttack2;
      BooleanValue place22 = this.place2;
      if (this.shouldSendPlacePacket(place22, notWhileAttack22, notWhileScaffold22, onlyWhileMove22, onlyWhileSpeedModule22)) {
         mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
      }

      BooleanValue onlyWhileSpeedModule32 = this.onlyWhileSpeedModule3;
      BooleanValue onlyWhileMove32 = this.onlyWhileMove3;
      BooleanValue notWhileScaffold32 = this.notWhileScaffold3;
      BooleanValue notWhileAttack32 = this.notWhileAttack3;
      BooleanValue place32 = this.place3;
      if (this.shouldSendPlacePacket(place32, notWhileAttack32, notWhileScaffold32, onlyWhileMove32, onlyWhileSpeedModule32)) {
         mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0.0F, 0.5F, 0.0F));
      }

      if (this.resourcePack.getValue()) {
         mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C19PacketResourcePackStatus("", net.minecraft.network.play.client.C19PacketResourcePackStatus.Action.ACCEPTED));
      }

      this.attackedThisTick = false;
   }

   public boolean shouldSendPlacePacket(BooleanValue booleanValue, BooleanValue booleanValue2, BooleanValue booleanValue3, BooleanValue booleanValue4, BooleanValue booleanValue5) {
      return booleanValue.getValue()
         && mc.thePlayer != null
         && mc.theWorld != null
         && (!booleanValue2.getValue() || !this.attackedThisTick)
         && (!Augustus.getInstance().getModuleManager().scaffold.isToggled() || !booleanValue3.getValue())
         && (MoveUtil.isMoving() || !booleanValue4.getValue())
         && (Augustus.getInstance().getModuleManager().speed.isToggled() || !booleanValue5.getValue());
   }

   public boolean shouldCancelIncomingPacket(Packet packet) {
      if (mc.thePlayer == null) {
         return false;
      } else {
         Class clazz = packet.getClass();

         for (DisablerIncomingPacketOption option : DisablerIncomingPacketOption.values()) {
            if (clazz.equals(DisablerIncomingPacketOption.getPacketClass(option))) {
               DisablerIncomingPacketOption.incrementPacketCount(option);
               if (DisablerIncomingPacketOption.isOptionSelected(option)
                  && (
                     DisablerIncomingPacketOption.getCancelConditionSetting(option).getSelectedValue().equals("Always")
                        || DisablerIncomingPacketOption.getCancelConditionSetting(option).getSelectedValue().equals("OnlyOnGround") && mc.thePlayer.onGround
                        || DisablerIncomingPacketOption.getCancelConditionSetting(option).getSelectedValue().equals("OnlyInAir") && !mc.thePlayer.onGround
                  )) {
                  StringValue intervalSetting = DisablerIncomingPacketOption.getIntervalSetting(option);
                  BooleanValue invertIntervalSetting = DisablerIncomingPacketOption.getInvertIntervalSetting(option);
                  int packetCount = DisablerIncomingPacketOption.getPacketCount(option);
                  BooleanValue booleanValue = invertIntervalSetting;
                  StringValue stringValue = intervalSetting;
                  if (this.matchesCancelModulo(stringValue, booleanValue, packetCount)) {
                     if (DisablerIncomingPacketOption.getAlwaysSetting(option).getValue()) {
                        return true;
                     }

                     if ((double)mc.thePlayer.ticksExisted > DisablerIncomingPacketOption.getTickRangeSetting(option).getMinValue()) {
                        double ticksExisted = (double)mc.thePlayer.ticksExisted;
                        RangeSetting tickRangeSetting = DisablerIncomingPacketOption.getTickRangeSetting(option);
                        if (ticksExisted < tickRangeSetting.getMaxValue()) {
                           return true;
                        }
                     }
                  }
               }

               return false;
            }
         }

         return false;
      }
   }

   public boolean shouldCancelOutgoingPacket(Packet packet) {
      Class clazz = packet.getClass();

      for (DisablerOutgoingPacketOption option : DisablerOutgoingPacketOption.values()) {
         if (clazz.equals(DisablerOutgoingPacketOption.getPacketClass(option))) {
            DisablerOutgoingPacketOption.incrementPacketCount(option);
            if (DisablerOutgoingPacketOption.isOptionSelected(option)
               && (
                  DisablerOutgoingPacketOption.getCancelConditionSetting(option).getSelectedValue().equals("Always")
                     || DisablerOutgoingPacketOption.getCancelConditionSetting(option).getSelectedValue().equals("OnlyOnGround") && mc.thePlayer.onGround
                     || DisablerOutgoingPacketOption.getCancelConditionSetting(option).getSelectedValue().equals("OnlyInAir") && !mc.thePlayer.onGround
               )) {
               StringValue intervalSetting = DisablerOutgoingPacketOption.getIntervalSetting(option);
               BooleanValue invertIntervalSetting = DisablerOutgoingPacketOption.getInvertIntervalSetting(option);
               int packetCount = DisablerOutgoingPacketOption.getPacketCount(option);
               BooleanValue booleanValue = invertIntervalSetting;
               StringValue stringValue = intervalSetting;
               if (this.matchesCancelModulo(stringValue, booleanValue, packetCount)) {
                  if (DisablerOutgoingPacketOption.getAlwaysSetting(option).getValue()) {
                     return true;
                  }

                  if ((double)mc.thePlayer.ticksExisted > DisablerOutgoingPacketOption.getTickRangeSetting(option).getMinValue()) {
                     double ticksExisted = (double)mc.thePlayer.ticksExisted;
                     RangeSetting tickRangeSetting = DisablerOutgoingPacketOption.getTickRangeSetting(option);
                     if (ticksExisted < tickRangeSetting.getMaxValue()) {
                        return true;
                     }
                  }
               }
            }

            return false;
         }
      }

      return false;
   }

   @Override
   public void onRender(float f) {
      if (this.pingSpoof.getValue()) {
         if (!this.spoofedPackets.isEmpty()) {
            Iterator it = this.spoofedPackets.iterator();
            RandomNumberGenerator instance = DefaultRandomGenerator.instance;
            double minValue = this.delay.getMinValue();
            double maxValue = this.delay.getMaxValue();
            double d = minValue;
            long l = instance.nextLong(d, maxValue);
            long l2 = System.currentTimeMillis();

            while (it.hasNext()) {
               Packet packet = (Packet)it.next();

               try {
                  long l3 = (Long)this.spoofedPacketTimestamps.get(packet);
                  if (l3 + l < l2) {
                     mc.thePlayer.sendQueue.addToSendQueueUnchecked(packet);
                     this.spoofedPacketTimestamps.remove(packet);
                     it.remove();
                  }
               } catch (Exception e) {
               }
            }
         }
      } else if (!this.spoofedPackets.isEmpty()) {
         this.spoofedPackets.forEach(obj -> mc.thePlayer.sendQueue.addToSendQueueUnchecked((Packet)obj));
         this.spoofedPackets.clear();
      }
   }

   private boolean matchesCancelModulo(StringValue stringValue, BooleanValue booleanValue, int n) {
      return booleanValue.getValue()
         ? stringValue.getValue() <= 0.0 || (double)n % stringValue.getValue() != 0.0
         : stringValue.getValue() <= 0.0 || (double)n % stringValue.getValue() == 0.0;
   }

   private static boolean isDiggingTickRangeVisible(DisablerDiggingActionOption option) {
      return !DisablerDiggingActionOption.getAlwaysSetting(option).getValue();
   }

   private static boolean isEntityActionTickRangeVisible(DisablerEntityActionOption option) {
      return !DisablerEntityActionOption.getAlwaysSetting(option).getValue();
   }

   private static boolean isOutgoingTickRangeVisible(DisablerOutgoingPacketOption option) {
      return !DisablerOutgoingPacketOption.getAlwaysSetting(option).getValue();
   }

   private static boolean isIncomingTickRangeVisible(DisablerIncomingPacketOption option) {
      return !DisablerIncomingPacketOption.getAlwaysSetting(option).getValue();
   }

}
