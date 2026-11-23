package net.augustus.modules.latency;

import java.awt.Color;
import java.util.Optional;
import java.util.function.ToDoubleFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.EventGameLoop;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.events.EventRenderEntity;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.render.RenderPassTask;
import net.augustus.utils.network.PacketFlushMarker;
import net.augustus.utils.simulation.EntityMovementPredictor;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.events.EventClick;
import net.augustus.utils.input.MouseInputUtil;
import net.augustus.settings.RangeSetting;
import net.augustus.settings.ColorSetting;
import net.augustus.events.EventPacketDelay;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.utils.combat.EntityTargetFilter;
import net.augustus.events.EventLoadWorld;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.events.listeners.GameLoopListener;
import net.augustus.utils.world.BlockUtil;
import net.augustus.utils.network.ServerPingUtil;
import net.augustus.utils.combat.TargetFilterPresets;
import net.augustus.utils.combat.EntityTargetSelector;
import net.augustus.utils.combat.CombatTargetUtil;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.events.listeners.RenderEntityListener;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.viaversion.packet.C0APacketSwingHand;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.TargetSelectionSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class BackTrack extends Module implements LoadWorldListener, PacketDelayListener, RenderListener, RenderEntityListener, GameLoopListener, TickListener, ClickListener {
   private final BooleansSetting mode;
   private final RandomizedRangeSettings maxHitRange;
   private final StringValue threshold;
   private final BooleanValue cancelHitsIfServerCancelsHit;
   private final RandomizedRangeSettings classicBackTrackDelay;
   private final BooleanValue onlyWhenNeed;
   private final BooleanValue dynamicClassic;
   private final RangeSetting backTrackTime;
   private final RandomizedRangeSettings delayBetweenBacktrack;
   private final BooleanValue calculateWithOwnPing;
   private final BooleanValue forceDelay;
   private final BooleanValue predict;
   private final BooleanValue dynamic;
   private final RandomizedRangeSettings maxLagPackets;
   private final StringValue delayBetweenLags;
   public final BooleansSetting countMode;
   private final BooleanValue calculateWithOwnPingLagTrack;
   private final StringValue lagPackets;
   private final RandomizedRangeSettings pingDelay;
   private final StringValue delayBetweenBacktrackPing;
   private final BooleanValue calculateWithOwnPingPing;
   private final BooleanValue dynamicPing;
   private final BooleanValue checkStartDistance;
   private final StringValue startDistance;
   private final BooleansSetting abortCriterion;
   private final StringValue stopOnAttackRange;
   private final BooleanValue fixPacketOrder;
   private final StringValue stopOnAttackMaxHurtTime;
   private final BooleanValue stopBackTrackAtHurtTime;
   private final StringValue continueWithBackTrackAtHurtTime;
   private final BooleanValue notWhileScaffold;
   public final BooleanValue improve;
   private final BooleanValue onlyKillAura;
   private final TargetSelectionSettings targetSelection;
   private final StringValue preAimRange;
   private final BooleanValue clickCheck;
   private final DoubleValue targetESPs;
   public final BooleansSetting targetESP;
   private final ColorSetting classicColor;
   private final StringValue lineWidthTarget;
   private final BooleanValue smoothRendering;
   public final BlurShadowSettings target;
   private final DoubleValue playerESPs;
   public final BooleansSetting playerESP;
   private final BooleanValue onlyInThirdPerson;
   private final ColorSetting classicColor2;
   private final StringValue lineWidthMe;
   public final BlurShadowSettings player;
   private EntityLivingBase currentTarget;
   private boolean wasDelayingIncoming;
   private boolean attackedTarget;
   private boolean backtracking;
   private boolean receivedVelocity;
   private boolean waitingForHurtTime;
   private boolean wasBacktracking;
   private final TimeHelper backtrackDurationTimer;
   private final TimeHelper betweenBacktracksTimer;
   private int currentDelayMs;
   private final double[] predictedTargetPos;
   private final TimeHelper betweenLagsTimer;
   private int lagPacketCount;
   private int effectiveLagPackets;
   private boolean incomingReady;
   private boolean outgoingReady;
   private boolean attackSentInLag;
   private boolean movePacketSentAfterAttack;
   private boolean lagTrackActive;
   private int gommeLagTicks;
   private boolean gommeDelaying;
   private boolean gommeAttacked;
   private boolean pingModeActive;
   private boolean classicBacktrackActive;

   public BackTrack() {
       super("BackTrack", "Zurückverfolgung", new Color(110, 186, 9), Categorys.LATENCY);
      this.mode = new BooleansSetting(this, "Mode", "BackTrack", new String[]{"BackTrack", "LagTrack", "Gomme", "Ping", "ClassicBackTrack"});
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(this, "MaxHitRange", 6.0, 6.0, 3.0, 10.0, 2);
      String str = "MaxBackTrackRangeMax";
      String str2 = "MaxBackTrackRangeMin";
      this.maxHitRange = randomizedRangeSettings.withRangeNames(str2, str).withoutNotInARow();
      this.threshold = new StringValue(this, "Threshold", 0.125, 0.0, 0.2, 3);
      this.cancelHitsIfServerCancelsHit = new BooleanValue(this, "CancelHitsIfServerCancelsHit", true);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this, "ClassicBackTrackDelay", 200.0, 250.0, 0.0, 5000.0, 0)).withoutNotInARow();
      String str3 = "MaxBackTrackDelay";
      String str4 = "MinBackTrackDelay";
      RandomizedRangeSettings randomizedRangeSettings3 = randomizedRangeSettings2.withRangeNames(str4, str3);
      SettingVisibilityCondition condition = () -> this.isClassicDelayVisible();
      this.classicBackTrackDelay = (RandomizedRangeSettings)randomizedRangeSettings3.withVisibility(condition);
      BooleanValue booleanValue = new BooleanValue(this, "OnlyWhenNeed", false);
      SettingVisibilityCondition condition2 = () -> {
         return this.mode.getSelectedValue().equals("ClassicBackTrack");
      };
      this.onlyWhenNeed = (BooleanValue)booleanValue.withVisibility(condition2);
      BooleanValue booleanValue2 = new BooleanValue(this, "DynamicClassic", false);
      String str5 = "Dynamic";
      BooleanValue booleanValue3 = (BooleanValue)booleanValue2.withDisplayName(str5);
      SettingVisibilityCondition condition3 = () -> this.isClassicDynamicVisible();
      this.dynamicClassic = (BooleanValue)booleanValue3.withVisibility(condition3);
      RangeSetting rangeSetting = new RangeSetting(this, "BackTrackTime", 200.0, 250.0, 0.0, 5000.0, 0);
      String str6 = "MaxBackTrackDelay";
      String str7 = "MinBackTrackDelay";
      RangeSetting rangeSetting2 = rangeSetting.withBoundNames(str7, str6);
      SettingVisibilityCondition condition4 = () -> this.isBackTrackTimeVisible();
      this.backTrackTime = rangeSetting2.visibleWhen(condition4);
      RandomizedRangeSettings randomizedRangeSettings4 = (new RandomizedRangeSettings(this, "DelayBetweenBacktrack", 50.0, 150.0, 0.0, 5000.0, 0)).withoutNotInARow();
      String str8 = "DelayBetweenBacktracks";
      RandomizedRangeSettings randomizedRangeSettings5 = (RandomizedRangeSettings)randomizedRangeSettings4.withDisplayNamePrefix(str8);
      SettingVisibilityCondition condition5 = () -> this.isDelayBetweenBacktracksVisible();
      this.delayBetweenBacktrack = (RandomizedRangeSettings)randomizedRangeSettings5.withVisibility(condition5);
      BooleanValue booleanValue4 = new BooleanValue(this, "CalculateWithOwnPing", false);
      String str9 = "Subtract ping from Delay";
      BooleanValue booleanValue5 = (BooleanValue)booleanValue4.withDisplayName(str9);
      SettingVisibilityCondition condition6 = () -> this.isSubtractPingVisible();
      this.calculateWithOwnPing = (BooleanValue)booleanValue5.withVisibility(condition6);
      BooleanValue booleanValue6 = new BooleanValue(this, "ForceDelay", false);
      SettingVisibilityCondition condition7 = () -> this.isForceDelayVisible();
      this.forceDelay = (BooleanValue)booleanValue6.withVisibility(condition7);
      BooleanValue booleanValue7 = new BooleanValue(this, "Predict", true);
      SettingVisibilityCondition condition8 = () -> this.isBackTrackPredictVisible();
      this.predict = (BooleanValue)booleanValue7.withVisibility(condition8);
      BooleanValue booleanValue8 = new BooleanValue(this, "Dynamic", false);
      SettingVisibilityCondition condition9 = () -> this.isBackTrackDynamicVisible();
      this.dynamic = (BooleanValue)booleanValue8.withVisibility(condition9);
      RandomizedRangeSettings randomizedRangeSettings6 = new RandomizedRangeSettings(this, "MaxLagPackets", 2.0, 2.0, 0.0, 10.0, 0);
      SettingVisibilityCondition condition10 = () -> this.isLagTrackMaxPacketsVisible();
      this.maxLagPackets = (RandomizedRangeSettings)randomizedRangeSettings6.withVisibility(condition10);
      StringValue stringValue = new StringValue(this, "DelayBetweenLags", 0.0, 0.0, 4000.0, 0);
      SettingVisibilityCondition condition11 = () -> this.isDelayBetweenLagsVisible();
      this.delayBetweenLags = stringValue.visibleWhen(condition11);
      BooleansSetting booleansSetting = new BooleansSetting(this, "CountMode", "Tick", new String[]{"Tick", "MovePacket"});
      SettingVisibilityCondition condition12 = () -> this.isLagTrackCountModeVisible();
      this.countMode = (BooleansSetting)booleansSetting.withVisibility(condition12);
      BooleanValue booleanValue9 = new BooleanValue(this, "CalculateWithOwnPingLagTrack", false);
      String str10 = "Subtract ping from Delay";
      BooleanValue booleanValue10 = (BooleanValue)booleanValue9.withDisplayName(str10);
      SettingVisibilityCondition condition13 = () -> this.isLagTrackSubtractPingVisible();
      this.calculateWithOwnPingLagTrack = (BooleanValue)booleanValue10.withVisibility(condition13);
      StringValue stringValue2 = new StringValue(this, "LagPackets", 10.0, 1.0, 10.0, 0);
      String str11 = "MaxLagPackets";
      StringValue stringValue3 = stringValue2.named(str11);
      SettingVisibilityCondition condition14 = () -> this.isGommeLagPacketsVisible();
      this.lagPackets = stringValue3.visibleWhen(condition14);
      RandomizedRangeSettings randomizedRangeSettings7 = (new RandomizedRangeSettings(this, "PingDelay", 150.0, 150.0, 0.0, 2000.0, 0)).withoutNotInARow();
      SettingVisibilityCondition condition15 = () -> this.isPingDelayVisible();
      this.pingDelay = (RandomizedRangeSettings)randomizedRangeSettings7.withVisibility(condition15);
      StringValue stringValue4 = new StringValue(this, "DelayBetweenBacktrackPing", 1200.0, 0.0, 5000.0, 0);
      String str12 = "DelayBetweenBacktracks";
      StringValue stringValue5 = stringValue4.named(str12);
      SettingVisibilityCondition condition16 = () -> this.isDelayBetweenPingBacktracksVisible();
      this.delayBetweenBacktrackPing = stringValue5.visibleWhen(condition16);
      BooleanValue booleanValue11 = new BooleanValue(this, "CalculateWithOwnPingPing", false);
      String str13 = "Subtract own ping from Delay";
      BooleanValue booleanValue12 = (BooleanValue)booleanValue11.withDisplayName(str13);
      SettingVisibilityCondition condition17 = () -> this.isSubtractPingInPingModeVisible();
      this.calculateWithOwnPingPing = (BooleanValue)booleanValue12.withVisibility(condition17);
      BooleanValue booleanValue13 = new BooleanValue(this, "DynamicPing", false);
      String str14 = "Dynamic";
      BooleanValue booleanValue14 = (BooleanValue)booleanValue13.withDisplayName(str14);
      SettingVisibilityCondition condition18 = () -> this.isDynamicPingVisible();
      this.dynamicPing = (BooleanValue)booleanValue14.withVisibility(condition18);
      BooleanValue booleanValue15 = new BooleanValue(this, "CheckStartDistance", false);
      SettingVisibilityCondition condition19 = () -> this.isCheckStartDistanceVisible();
      this.checkStartDistance = (BooleanValue)booleanValue15.withVisibility(condition19);
      StringValue stringValue6 = new StringValue(this, "StartDistance", 3.0, 0.0, 6.0, 2);
      SettingVisibilityCondition condition20 = () -> {
         return this.checkStartDistance.getValue() && !this.mode.getSelectedValue().equals("ClassicBackTrack");
      };
      this.startDistance = stringValue6.visibleWhen(condition20);
      BooleansSetting booleansSetting2 = new BooleansSetting(this, "AbortCriterion", "None", new String[]{"None", "OnAttack", "OnRange"});
      SettingVisibilityCondition condition21 = () -> {
         return !this.mode.getSelectedValue().equals("ClassicBackTrack");
      };
      this.abortCriterion = (BooleansSetting)booleansSetting2.withVisibility(condition21);
      StringValue stringValue7 = new StringValue(this, "StopOnAttackRange", 2.95, 1.0, 6.0, 2);
      SettingVisibilityCondition condition22 = () -> this.isStopOnAttackRangeVisible();
      this.stopOnAttackRange = stringValue7.visibleWhen(condition22);
      BooleanValue booleanValue16 = new BooleanValue(this, "FixPacketOrder", true);
      SettingVisibilityCondition condition23 = () -> this.isFixPacketOrderVisible();
      this.fixPacketOrder = (BooleanValue)booleanValue16.withVisibility(condition23);
      StringValue stringValue8 = new StringValue(this, "StopOnAttackMaxHurtTime", 3.0, 2.0, 10.0, 0);
      SettingVisibilityCondition condition24 = () -> this.isStopOnAttackMaxHurtTimeVisible();
      this.stopOnAttackMaxHurtTime = stringValue8.visibleWhen(condition24);
      BooleanValue booleanValue17 = new BooleanValue(this, "StopBackTrackAtHurtTime", false);
      SettingVisibilityCondition condition25 = () -> this.isStopAtHurtTimeVisible();
      this.stopBackTrackAtHurtTime = (BooleanValue)booleanValue17.withVisibility(condition25);
      StringValue stringValue9 = new StringValue(this, "ContinueWithBackTrackAtHurtTime", 0.0, 0.0, 10.0, 0);
      BooleanValue booleanValue18 = this.stopBackTrackAtHurtTime;
      this.stopBackTrackAtHurtTime.getClass();
      BooleanValue booleanValue19 = booleanValue18;
      SettingVisibilityCondition condition26 = () -> booleanValue19.getValue();
      StringValue stringValue10 = stringValue9.visibleWhen(condition26);
      SettingVisibilityCondition condition27 = () -> {
         return !this.mode.getSelectedValue().equals("ClassicBackTrack") && this.stopBackTrackAtHurtTime.getValue();
      };
      this.continueWithBackTrackAtHurtTime = stringValue10.visibleWhen(condition27);
      this.notWhileScaffold = new BooleanValue(this, "NotWhileScaffold", true);
      this.improve = new BooleanValue(this, "Improve", false);
      this.onlyKillAura = (BooleanValue)(new BooleanValue(this, "OnlyKillAura", true)).markVisualOnly();
      TargetSelectionSettings targetSelectionSettings = (new TargetSelectionSettings(this, TargetFilterPresets.createCombatFilter())).withSorting();
      SettingVisibilityCondition condition28 = () -> {
         return !this.onlyKillAura.getValue();
      };
      this.targetSelection = targetSelectionSettings.withVisibility(condition28);
      StringValue stringValue11 = new StringValue(this, "PreAimRange", 4.0, 0.0, 15.0, 1);
      SettingVisibilityCondition condition29 = () -> this.isPreAimRangeVisible();
      this.preAimRange = (StringValue)stringValue11.visibleWhen(condition29).markVisualOnly();
      this.clickCheck = (BooleanValue)(new BooleanValue(this, "ClickCheck", false)).markVisualOnly();
      this.targetESPs = (DoubleValue)(new DoubleValue(this, "TargetESPs")).markVisualOnly();
      this.targetESP = (BooleansSetting)(new BooleansSetting(
            this, "TargetESP", "Classic", new String[]{"None", "Classic", "PlayerModel", "PlayerModelArmor"}, this.targetESPs
         ))
         .markVisualOnly();
      Color color2 = new Color(0, 255, 0, 0);
      DoubleValue targetESPs2 = this.targetESPs;
      Color color3 = color2;
      String str15 = "ClassicColorTarget";
      ColorSetting colorSetting = new ColorSetting(this, str15, color3, targetESPs2);
      String str16 = "ClassicColor";
      ColorSetting colorSetting2 = (ColorSetting)colorSetting.withDisplayName(str16);
      SettingVisibilityCondition condition30 = () -> this.isTargetClassicColorVisible();
      this.classicColor = (ColorSetting)((ColorSetting)colorSetting2.withVisibility(condition30)).markVisualOnly();
      StringValue stringValue12 = new StringValue(this, "LineWidthTarget", 3.0, 0.0, 10.0, 0, this.targetESPs);
      String str17 = "LineWidth";
      StringValue stringValue13 = stringValue12.named(str17);
      SettingVisibilityCondition condition31 = () -> this.isTargetLineWidthVisible();
      this.lineWidthTarget = (StringValue)stringValue13.visibleWhen(condition31).markVisualOnly();
      BooleanValue booleanValue20 = new BooleanValue(this, "SmoothRendering", false, this.targetESPs);
      SettingVisibilityCondition condition32 = () -> {
         return !this.targetESP.getSelectedValue().equals("None");
      };
      this.smoothRendering = (BooleanValue)((BooleanValue)booleanValue20.withVisibility(condition32)).markVisualOnly();
      BlurShadowSettings blurShadowSettings = new BlurShadowSettings(this, "Target", this.targetESPs);
      SettingVisibilityCondition condition33 = () -> {
         return !this.targetESP.getSelectedValue().equals("None");
      };
      this.target = ((BlurShadowSettings)blurShadowSettings.withVisibility(condition33)).disableBlurByDefault();
      DoubleValue doubleValue = new DoubleValue(this, "PlayerESPs");
      SettingVisibilityCondition condition34 = () -> this.isPlayerEspGroupVisible();
      this.playerESPs = (DoubleValue)doubleValue.visibleWhen(condition34).markVisualOnly();
      this.playerESP = (BooleansSetting)(new BooleansSetting(
            this, "PlayerESP", "Classic", new String[]{"None", "Classic", "PlayerModel", "PlayerModelArmor"}, this.playerESPs
         ))
         .markVisualOnly();
      this.onlyInThirdPerson = (BooleanValue)(new BooleanValue(this, "OnlyInThirdPerson", true, this.playerESPs)).markVisualOnly();
      Color color4 = new Color(0, 255, 0, 0);
      targetESPs2 = this.playerESPs;
      color3 = color4;
      str15 = "ClassicColorMe";
      ColorSetting colorSetting3 = new ColorSetting(this, str15, color3, targetESPs2);
      String str18 = "ClassicColor";
      ColorSetting colorSetting4 = (ColorSetting)colorSetting3.withDisplayName(str18);
      SettingVisibilityCondition condition35 = () -> this.isPlayerClassicColorVisible();
      this.classicColor2 = (ColorSetting)((ColorSetting)colorSetting4.withVisibility(condition35)).markVisualOnly();
      StringValue stringValue14 = new StringValue(this, "LineWidthMe", 3.0, 0.0, 10.0, 0, this.playerESPs);
      String str19 = "LineWidth";
      StringValue stringValue15 = stringValue14.named(str19);
      SettingVisibilityCondition condition36 = () -> {
         return this.playerESP.getSelectedValue().equals("Classic");
      };
      this.lineWidthMe = (StringValue)stringValue15.visibleWhen(condition36).markVisualOnly();
      BlurShadowSettings blurShadowSettings2 = new BlurShadowSettings(this, "Player", this.playerESPs);
      SettingVisibilityCondition condition37 = () -> {
         return !this.playerESP.getSelectedValue().equals("None");
      };
      this.player = ((BlurShadowSettings)((BlurShadowSettings)blurShadowSettings2.withVisibility(condition37)).markAllVisualOnly()).disableBlurByDefault();
      this.currentTarget = null;
      this.wasDelayingIncoming = false;
      this.attackedTarget = false;
      this.backtracking = false;
      this.wasBacktracking = false;
      this.backtrackDurationTimer = new TimeHelper();
      this.betweenBacktracksTimer = new TimeHelper();
      this.predictedTargetPos = new double[3];
      this.betweenLagsTimer = new TimeHelper();
      this.lagPacketCount = 0;
      this.effectiveLagPackets = 0;
      this.attackSentInLag = false;
      this.movePacketSentAfterAttack = false;
      this.lagTrackActive = false;
      this.gommeLagTicks = 0;
      this.gommeDelaying = true;
      this.gommeAttacked = false;
      this.pingModeActive = true;
      this.classicBacktrackActive = false;
   }

   @Override
   public void onEnable() {
      this.attackedTarget = false;
      this.backtracking = false;
      this.delayBetweenBacktrack.nextValue();
      this.incomingReady = true;
      this.outgoingReady = true;
      this.lagTrackActive = false;
      this.gommeLagTicks = 0;
      this.gommeDelaying = false;
      RandomNumberGenerator instance = DefaultRandomGenerator.instance;
      double minValue = this.backTrackTime.getMinValue();
      double maxValue = this.backTrackTime.getMaxValue();
      double d = maxValue;
      double d2 = minValue;
      this.currentDelayMs = instance.nextInt(d2, d);
      this.maxHitRange.nextValue();
      this.maxLagPackets.nextValue();
      this.pingDelay.nextValue();
      this.pingModeActive = true;
   }

   @Override
   public void onDisable() {
      Object obj = null;
      this.targetSelection.setCurrentTarget((Entity)obj);
      this.currentTarget = null;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 25;
      this.toggleEventSubscription(b);
      byte b2 = 32;
      this.toggleEventSubscription(b2);
      byte b3 = 31;
      this.toggleEventSubscription(b3);
      byte b4 = 16;
      this.toggleEventSubscription(b4);
      byte b5 = 40;
      this.toggleEventSubscription(b5);
      byte b6 = 17;
      this.toggleEventSubscription(b6);
      int n = 1000000;
      byte b7 = 7;
      this.toggleEventSubscription(b7, n);
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.currentTarget = null;
      Object obj = null;
      this.targetSelection.setCurrentTarget((Entity)obj);
   }

   @Override
   public void onClick(EventClick event) {
      if (this.currentTarget != null) {
         if (this.cancelHitsIfServerCancelsHit.getValue()) {
            if (!EntityMovementPredictor.hasInvalidPositionHistory(this.currentTarget)) {
               EntityLivingBase currentTarget2 = this.currentTarget;
               if (!this.isWithinServerHitRange(currentTarget2)) {
                  event.clearLeftClicks();
               }
            }
         }
      }
   }

   @Override
   public void onPacketDelay2(EventPacketDelay event) {
      Packet packet = event.getPacket();
      EntityPlayerSP thePlayer = mc.thePlayer;
      WorldClient theWorld = mc.theWorld;
      if (thePlayer != null && packet instanceof S12PacketEntityVelocity) {
         S12PacketEntityVelocity packet2 = (S12PacketEntityVelocity)packet;
         if (packet2.getEntityID() == thePlayer.getEntityId()) {
            boolean flag = true;
            this.setReceivedVelocity(flag);
            boolean flag2 = true;
            this.setWaitingForHurtTime(flag2);
         }
      }

      if (this.wasDelayingIncoming && !ClientManagers.moduleManager.packetHandler.isDelayingIncoming()) {
         this.maxHitRange.nextValue();
      }

      this.wasDelayingIncoming = ClientManagers.moduleManager.packetHandler.isDelayingIncoming();
      String selectedValue = this.mode.getSelectedValue();
      switch (selectedValue) {
         case "ClassicBackTrack":
            if (this.dynamicClassic.getValue() && this.classicBacktrackActive) {
               double d = 3.0;
               EntityLivingBase currentTarget2 = this.currentTarget;
               boolean flag3 = true;
               event.setDelayedWithTarget(flag3, currentTarget2, d);
            } else {
               boolean classicBacktrackActive2 = this.classicBacktrackActive;
               event.setDelayed(classicBacktrackActive2);
            }
            break;
         case "BackTrack":
            EntityLivingBase currentTarget3 = this.currentTarget;
            String str = Math.max(0, this.currentDelayMs) + " ms";
            this.setSuffix(str);
            if (theWorld == null || thePlayer == null || thePlayer.ticksExisted < 10 || theWorld.loadedEntityList == null) {
               return;
            }

            if (currentTarget3 != null) {
               Vec3 serverEntityPos = this.getServerEntityPos(currentTarget3);
               double[] doubles;
               if (this.predict.getValue() && (currentTarget3.realPosX != serverEntityPos.xCoord || currentTarget3.realPosY != serverEntityPos.yCoord || currentTarget3.realPosZ != serverEntityPos.zCoord)) {
                  synchronized (this.predictedTargetPos) {
                     doubles = this.predictedTargetPos;
                  }
               } else {
                  doubles = new double[]{currentTarget3.realPosX, currentTarget3.realPosY, currentTarget3.realPosZ};
               }

               AxisAlignedBB box = RotationUtil.createEntityBoxAt(currentTarget3, serverEntityPos);
               double d2 = doubles[0];
               double d3 = doubles[1];
               double d4 = doubles[2];
               double d5 = d3;
               double d6 = d2;
               AxisAlignedBB box2 = RotationUtil.createEntityBoxAt(currentTarget3, d6, d5, d4);
               Vec3 nearestPointInBox = RotationUtil.getNearestPointInBox(thePlayer, box);
               Vec3 nearestPointInBox2 = RotationUtil.getNearestPointInBox(thePlayer, box2);
               Vec3 positionEyes = thePlayer.getPositionEyes(1.0F);
               if (this.passesClickCheck() && this.checkHurtTimeCondition(thePlayer)) {
                  TimeHelper betweenBacktracksTimer2 = this.betweenBacktracksTimer;
                  long lastIntValue = (long)this.delayBetweenBacktrack.getLastIntValue();
                  if (betweenBacktracksTimer2.reached2(lastIntValue)) {
                     boolean isDelayingIncoming = ClientManagers.moduleManager.packetHandler.isDelayingIncoming();
                     if (this.checkAbortAndStartDistance(thePlayer, isDelayingIncoming, currentTarget3)) {
                        d3 = positionEyes.distanceTo(nearestPointInBox2);
                        double d7 = positionEyes.distanceTo(nearestPointInBox);
                        double d8 = d3;
                        if (this.exceedsDistanceThreshold(d8, d7)) {
                           if (this.isWithinMaxHitRange(thePlayer, currentTarget3)) {
                              long currentDelayMs2 = (long)this.currentDelayMs;
                              if (!this.backtrackDurationTimer.reached2(currentDelayMs2)) {
                                 if (this.dynamic.getValue()) {
                                    if (ClientManagers.moduleManager.packetHandler.isDelayingIncoming() || positionEyes.distanceTo(nearestPointInBox2) > 3.0) {
                                       double d9 = 3.0;
                                       boolean flag4 = true;
                                       event.setDelayedWithTarget(flag4, currentTarget3, d9);
                                    }
                                 } else {
                                    boolean flag5 = true;
                                    event.setDelayed(flag5);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            this.backtracking = event.isDelayed();
            if (event.isDelayed() && !this.wasBacktracking) {
               this.delayBetweenBacktrack.nextValue();
            }

            if (!event.isDelayed()) {
               this.attackedTarget = false;
               if (this.wasBacktracking || this.currentDelayMs <= 0) {
                  if (this.calculateWithOwnPing.getValue()) {
                     RandomNumberGenerator instance = DefaultRandomGenerator.instance;
                     double minValue = this.backTrackTime.getMinValue();
                     double maxValue = this.backTrackTime.getMaxValue();
                     double d10 = maxValue;
                     double d11 = minValue;
                     this.currentDelayMs = Math.max(0, instance.nextInt(d11, d10) - ServerPingUtil.getPing());
                  } else {
                     RandomNumberGenerator instance2 = DefaultRandomGenerator.instance;
                     double minValue2 = this.backTrackTime.getMinValue();
                     double maxValue2 = this.backTrackTime.getMaxValue();
                     double d12 = maxValue2;
                     double d13 = minValue2;
                     this.currentDelayMs = instance2.nextInt(d13, d12);
                  }

                  this.betweenBacktracksTimer.reset();
               }

               this.backtrackDurationTimer.reset();
            }

            this.wasBacktracking = event.isDelayed();
            break;
         case "LagTrack":
            if (theWorld == null || thePlayer == null || thePlayer.ticksExisted < 10 || theWorld.loadedEntityList == null) {
               return;
            }

            this.updateLagTrackState();
            boolean lagTrackActive2 = this.lagTrackActive;
            event.setDelayed(lagTrackActive2);
            this.backtracking = this.lagTrackActive;
            this.incomingReady = !event.isDelayed();
            break;
         case "Ping":
            EntityLivingBase currentTarget4 = this.currentTarget;
            String str2 = this.pingDelay.getLastIntValue() + " ms";
            this.setSuffix(str2);
            if (currentTarget4 != null && theWorld != null && thePlayer != null && thePlayer.ticksExisted >= 10 && theWorld.loadedEntityList != null && currentTarget4.interpolatedServerPos != null) {
               Vec3 positionEyes2 = thePlayer.getPositionEyes(1.0F);
               Vec3 serverEntityPos2 = this.getServerEntityPos(currentTarget4);
               AxisAlignedBB box3 = RotationUtil.createEntityBoxAt(currentTarget4, serverEntityPos2);
               Vec3 vec = RotationUtil.clampPointToBox(positionEyes2, box3);
               double realPosZ = currentTarget4.realPosZ;
               double realPosY = currentTarget4.realPosY;
               double realPosX = currentTarget4.realPosX;
               AxisAlignedBB box4 = RotationUtil.createEntityBoxAt(currentTarget4, realPosX, realPosY, realPosZ);
               Vec3 vec2 = RotationUtil.clampPointToBox(positionEyes2, box4);
               double d14 = positionEyes2.distanceTo(vec2);
               double d15 = positionEyes2.distanceTo(vec);
               if (this.exceedsDistanceThreshold(d14, d15)) {
                  TimeHelper betweenBacktracksTimer3 = this.betweenBacktracksTimer;
                  double value = this.delayBetweenBacktrackPing.getValue();
                  if (betweenBacktracksTimer3.reached(value) && this.checkHurtTimeCondition(thePlayer)) {
                     boolean isDelayingIncoming2 = ClientManagers.moduleManager.packetHandler.isDelayingIncoming();
                     if (this.checkAbortAndStartDistance(thePlayer, isDelayingIncoming2, currentTarget4)) {
                        if (this.isWithinMaxHitRange(thePlayer, currentTarget4)) {
                           if (this.dynamicPing.getValue()) {
                              if (ClientManagers.moduleManager.packetHandler.isDelayingIncoming() || positionEyes2.distanceTo(vec2) > 3.0) {
                                 int lastIntValue2 = this.pingDelay.getLastIntValue();
                                 double d16 = 3.0;
                                 int n = lastIntValue2;
                                 event.setIncomingDelayWithTarget(n, currentTarget4, d16);
                              }
                           } else {
                              int lastIntValue3 = this.pingDelay.getLastIntValue();
                              event.setIncomingDelayMs(lastIntValue3);
                           }

                           this.pingModeActive = true;
                           this.backtracking = true;
                           return;
                        }
                     }
                  }
               }

               this.attackedTarget = false;
               if (this.pingModeActive) {
                  this.betweenBacktracksTimer.reset();
                  this.pingDelay.nextValue();
                  if (this.calculateWithOwnPingPing.getValue()) {
                     RandomizedRangeSettings pingDelay2 = this.pingDelay;
                     double d17 = (double)Math.max(0, this.pingDelay.getLastIntValue() - ServerPingUtil.getPing());
                     pingDelay2.setLastValue(d17);
                  }
               }

               this.pingModeActive = false;
               this.backtracking = false;
            }
      }
   }

   private boolean checkAbortAndStartDistance(EntityPlayerSP player2, boolean flag, EntityLivingBase entity) {
      Vec3 positionEyes = player2.getPositionEyes(1.0F);
      if (flag) {
         if (this.abortCriterion.getSelectedValue().equals("None")) {
            return true;
         } else if (this.abortCriterion.getSelectedValue().equals("OnAttack") && !this.attackedTarget) {
            return true;
         } else if (!this.abortCriterion.getSelectedValue().equals("OnRange")) {
            return false;
         } else {
            Vec3 nearestPointFromEntity = RotationUtil.getNearestPointFromEntity(player2, entity);
            return positionEyes.distanceTo(nearestPointFromEntity) > this.stopOnAttackRange.getValue() || (double)entity.hurtTime > this.stopOnAttackMaxHurtTime.getValue();
         }
      } else {
         boolean flag2 = true;
         if (this.abortCriterion.getSelectedValue().equals("OnRange")) {
            Vec3 nearestPointFromEntity2 = RotationUtil.getNearestPointFromEntity(player2, entity);
            flag2 = positionEyes.distanceTo(nearestPointFromEntity2) > this.stopOnAttackRange.getValue() || (double)entity.hurtTime > this.stopOnAttackMaxHurtTime.getValue();
         }

         double realPosZ = entity.realPosZ;
         double realPosY = entity.realPosY;
         double realPosX = entity.realPosX;
         AxisAlignedBB box = RotationUtil.createEntityBoxAt(entity, realPosX, realPosY, realPosZ);
         Vec3 vec = RotationUtil.clampPointToBox(positionEyes, box);
         return (!this.checkStartDistance.getValue() || positionEyes.distanceTo(vec) > this.startDistance.getValue()) && flag2;
      }
   }

   private boolean isWithinMaxHitRange(EntityPlayerSP player2, EntityLivingBase entity) {
      return player2.getServerPos().distanceTo(entity.realPosX, entity.realPosY, entity.realPosZ) < this.maxHitRange.getLastValue();
   }

   public boolean isWithinServerHitRange(EntityLivingBase entity) {
      double d = mc.thePlayer.canEntityBeSeen(entity) ? 6.0 : Math.min(6.0, this.getEffectiveReach());
      return mc.thePlayer.getServerPos().distanceTo(entity.realPosX, entity.realPosY, entity.realPosZ) < d;
   }

   @Override
   public void onTick() {
      if (this.stopBackTrackAtHurtTime.getValue()) {
         if (mc.thePlayer.hurtTime == 10) {
            boolean flag = false;
            this.setWaitingForHurtTime(flag);
         } else if (mc.thePlayer.hurtTime == 0) {
            boolean flag2 = false;
            this.setReceivedVelocity(flag2);
         }
      }

      if (this.mode.getSelectedValue().equals("ClassicBackTrack") && this.currentTarget != null) {
         String str = Math.max(0, this.classicBackTrackDelay.getLastIntValue()) + " ms";
         this.setSuffix(str);
         double posZ = this.currentTarget.posZ;
         double posY = this.currentTarget.posY;
         double posX = this.currentTarget.posX;
         AxisAlignedBB box = RotationUtil.createEntityBoxAt(this.currentTarget, posX, posY, posZ);
         double realPosZ = this.currentTarget.realPosZ;
         double realPosY = this.currentTarget.realPosY;
         double realPosX = this.currentTarget.realPosX;
         AxisAlignedBB box2 = RotationUtil.createEntityBoxAt(this.currentTarget, realPosX, realPosY, realPosZ);
         Vec3 nearestPointInBox = RotationUtil.getNearestPointInBox(box);
         Vec3 nearestPointInBox2 = RotationUtil.getNearestPointInBox(box2);
         Vec3 positionEyes = mc.thePlayer.getPositionEyes(1.0F);
         double d = mc.thePlayer.getPrevServerPos().xCoord + (mc.thePlayer.getServerPos().xCoord - mc.thePlayer.getPrevServerPos().xCoord) / 3.0;
         double d2 = mc.thePlayer.getPrevServerPos().yCoord + (mc.thePlayer.getServerPos().yCoord - mc.thePlayer.getPrevServerPos().yCoord) / 3.0;
         double d3 = mc.thePlayer.getPrevServerPos().zCoord + (mc.thePlayer.getServerPos().zCoord - mc.thePlayer.getPrevServerPos().zCoord) / 3.0;
         AxisAlignedBB box3 = RotationUtil.createEntityBoxAt(mc.thePlayer, d, d2, d3);
         Vec3 positionEyes2 = this.currentTarget.getPositionEyes(1.0F);
         Vec3 vec = RotationUtil.clampPointToBox(positionEyes2, box3);
         boolean flag3 = !this.onlyWhenNeed.getValue() || positionEyes2.distanceTo(vec) > 3.0 || mc.thePlayer.hurtTime < 8 && mc.thePlayer.hurtTime > 3;
         if (flag3 && this.passesClickCheck()) {
            double d4 = positionEyes.distanceTo(nearestPointInBox2);
            double d5 = positionEyes.distanceTo(nearestPointInBox);
            double d6 = d4;
            if (this.exceedsDistanceThreshold(d6, d5)) {
               EntityLivingBase currentTarget2 = this.currentTarget;
               EntityPlayerSP thePlayer = mc.thePlayer;
               if (this.isWithinMaxHitRange(thePlayer, currentTarget2)) {
                  TimeHelper backtrackDurationTimer2 = this.backtrackDurationTimer;
                  long lastIntValue = (long)this.classicBackTrackDelay.getLastIntValue();
                  if (!backtrackDurationTimer2.reached2(lastIntValue)) {
                     if (!this.dynamicClassic.getValue() || ClientManagers.moduleManager.packetHandler.isDelayingIncoming() || positionEyes.distanceTo(nearestPointInBox2) > 3.0) {
                        this.classicBacktrackActive = true;
                     }

                     return;
                  }
               }
            }
         }

         if (ClientManagers.moduleManager.packetHandler.isDelayingIncoming()) {
            mc.getNetHandler().getNetworkManager().receivePacketAsync(new PacketFlushMarker());
         }

         this.classicBacktrackActive = false;
         this.backtrackDurationTimer.reset();
         this.classicBackTrackDelay.nextValue();
      } else {
         this.classicBacktrackActive = false;
      }
   }

   @Override
   public void onTick3() {
      EntityLivingBase currentTarget2 = this.currentTarget;
      this.targetSelection.setCurrentTarget(currentTarget2);
      this.currentTarget = this.findTarget();
      String selectedValue = this.mode.getSelectedValue();
      switch (selectedValue) {
         case "BackTrack":
            if (this.currentTarget != null) {
               synchronized (this.predictedTargetPos) {
                  byte b = 0;
                  double[] doubles = PlayerUtil.predictTargetPosition(this.currentTarget, b);
                  this.setPredictedTargetPos(doubles);
               }
            }
            break;
         case "Gomme":
            String str = this.lagPackets.getValue() + "";
            this.setSuffix(str);
            if (mc.getTimer().elapsedTicks >= 1 && mc.theWorld != null) {
               this.gommeLagTicks = this.gommeLagTicks + mc.getTimer().elapsedTicks;
               boolean flag = false;
               if (this.currentTarget != null && mc.thePlayer != null && mc.theWorld != null) {
                  double posZ = this.currentTarget.posZ;
                  double posY = this.currentTarget.posY;
                  double posX = this.currentTarget.posX;
                  AxisAlignedBB box = RotationUtil.createEntityBoxAt(this.currentTarget, posX, posY, posZ);
                  Vec3 nearestPointInBox = RotationUtil.getNearestPointInBox(box);
                  double realPosZ = this.currentTarget.realPosZ;
                  double realPosY = this.currentTarget.realPosY;
                  double realPosX = this.currentTarget.realPosX;
                  AxisAlignedBB box2 = RotationUtil.createEntityBoxAt(this.currentTarget, realPosX, realPosY, realPosZ);
                  Vec3 nearestPointInBox2 = RotationUtil.getNearestPointInBox(box2);
                  Vec3 positionEyes = mc.thePlayer.getPositionEyes(1.0F);
                  EntityLivingBase currentTarget3 = this.currentTarget;
                  boolean gommeDelaying2 = this.gommeDelaying;
                  EntityPlayerSP thePlayer = mc.thePlayer;
                  boolean flag2 = this.checkAbortAndStartDistance(thePlayer, gommeDelaying2, currentTarget3);
                  if (this.passesClickCheck()) {
                     EntityPlayerSP thePlayer2 = mc.thePlayer;
                     if (this.checkHurtTimeCondition(thePlayer2)) {
                        double d = positionEyes.distanceTo(nearestPointInBox2);
                        double d2 = positionEyes.distanceTo(nearestPointInBox);
                        double d3 = d;
                        if (this.exceedsDistanceThreshold(d3, d2) && flag2) {
                           EntityLivingBase currentTarget4 = this.currentTarget;
                           EntityPlayerSP thePlayer3 = mc.thePlayer;
                           if (this.isWithinMaxHitRange(thePlayer3, currentTarget4)
                              && (double)mc.thePlayer.getDistanceToEntity(this.currentTarget)
                                 < new Vec3(mc.thePlayer.prevPosX, mc.thePlayer.prevPosY, mc.thePlayer.prevPosZ)
                                    .distanceTo(new Vec3(this.currentTarget.posX, this.currentTarget.posY, this.currentTarget.posZ))) {
                              flag = true;
                           }
                        }
                     }
                  }
               }

               if (flag && !this.gommeAttacked && !((double)this.gommeLagTicks > this.lagPackets.getValue())) {
                  this.backtracking = false;
                  this.gommeDelaying = true;
               } else {
                  this.attackedTarget = false;
                  if (mc.getNetHandler() != null && mc.getNetHandler().getNetworkManager() != null) {
                     mc.getNetHandler().getNetworkManager().sendPacket(new PacketFlushMarker());
                  }

                  this.backtracking = true;
                  this.gommeDelaying = false;
                  this.gommeLagTicks = 0;
                  this.gommeAttacked = false;
               }
            }
      }
   }

   @Override
   public void onPacketDelay(EventPacketDelay event) {
      Packet packet = event.getPacket();
      EntityLivingBase currentTarget2 = this.currentTarget;
      if (this.abortCriterion.getSelectedValue().equals("OnAttack") && currentTarget2 != null) {
         if (this.fixPacketOrder.getValue()
            && (
               packet instanceof C07PacketPlayerDigging
                     && (
                        ((C07PacketPlayerDigging)packet).getStatus() == C07PacketPlayerDigging.Action.DROP_ALL_ITEMS
                           || ((C07PacketPlayerDigging)packet).getStatus() == C07PacketPlayerDigging.Action.DROP_ITEM
                           || ((C07PacketPlayerDigging)packet).getStatus() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM
                     )
                  || packet instanceof C16PacketClientStatus && ((C16PacketClientStatus)packet).getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT
                  || packet instanceof C0BPacketEntityAction
                     && ((C0BPacketEntityAction)packet).getAction() == net.minecraft.network.play.client.C0BPacketEntityAction.Action.OPEN_INVENTORY
                  || packet instanceof C08PacketPlayerBlockPlacement
                     && ((C08PacketPlayerBlockPlacement)packet).getPlacedBlockDirection() == 255
                     && ((C08PacketPlayerBlockPlacement)packet).getPlacedBlockOffsetX() == 0.0F
                     && ((C08PacketPlayerBlockPlacement)packet).getPlacedBlockOffsetY() == 0.0F
                     && ((C08PacketPlayerBlockPlacement)packet).getPlacedBlockOffsetZ() == 0.0F
                  || packet instanceof C0APacketAnimation
                  || packet instanceof C0APacketSwingHand
            )) {
            boolean flag = true;
            event.setDelayed(flag);
         }

         if (packet instanceof C02PacketUseEntity
            && ((C02PacketUseEntity)packet).getAction() == net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK
            && this.stopOnAttackMaxHurtTime.getValue() >= (double)currentTarget2.hurtTime) {
            this.attackedTarget = true;
         }
      }

      String selectedValue = this.mode.getSelectedValue();
      switch (selectedValue) {
         case "LagTrack":
            String str = this.maxLagPackets.getLastIntValue() + "";
            this.setSuffix(str);
            EntityPlayerSP thePlayer = mc.thePlayer;
            WorldClient theWorld = mc.theWorld;
            if (theWorld == null || thePlayer == null || thePlayer.ticksExisted < 10 || theWorld.loadedEntityList == null) {
               return;
            }

            if (packet instanceof C02PacketUseEntity) {
               this.attackSentInLag = true;
            }

            if (packet instanceof C03PacketPlayer && this.attackSentInLag) {
               this.movePacketSentAfterAttack = true;
            }

            if (this.lagTrackActive && this.countMode.getSelectedValue().equals("MovePacket") && packet instanceof C03PacketPlayer) {
               this.lagPacketCount++;
            }

            boolean lagTrackActive2 = this.lagTrackActive;
            event.setDelayed(lagTrackActive2);
            this.outgoingReady = !event.isDelayed();
            break;
         case "Gomme":
            if (packet instanceof C02PacketUseEntity) {
               this.gommeAttacked = true;
            }

            if (this.gommeDelaying) {
               boolean flag2 = true;
               event.setDelayed(flag2);
            }
      }
   }

   @Override
   public void onGameLoop(EventGameLoop event) {
      String selectedValue = this.mode.getSelectedValue();
      switch (selectedValue) {
         case "BackTrack":
            long currentDelayMs2 = (long)this.currentDelayMs;
            if (this.backtrackDurationTimer.reached2(currentDelayMs2) && this.forceDelay.getValue() && ClientManagers.moduleManager.packetHandler.isDelayingIncoming()) {
               mc.getNetHandler().getNetworkManager().receivePacketAsync(new PacketFlushMarker());
            }
            break;
         case "LagTrack":
            if (mc.theWorld == null || mc.thePlayer == null || mc.thePlayer.ticksExisted < 10 || mc.theWorld.loadedEntityList == null) {
               return;
            }

            if (this.countMode.getSelectedValue().equals("Tick")) {
               if (mc.getTimer().elapsedTicks >= 1 && this.effectiveLagPackets > this.maxLagPackets.getLastIntValue()) {
                  mc.getNetHandler().getNetworkManager().receivePacketAsync(new PacketFlushMarker());
               }

               this.lagPacketCount = this.lagPacketCount + mc.getTimer().elapsedTicks;
            }
            break;
         case "Gomme":
            boolean flag = true;
            event.setAllowSkipRender(flag);
            if (this.gommeDelaying) {
               boolean flag2 = true;
               event.setSkipRender(flag2);
            }
      }
   }

   @Override
   public void onRender(float f) {
      if (this.currentTarget != null) {
         RenderUtil.begin3DRender();
         boolean flag = (
               this.mode.getSelectedValue().equals("Ping") || this.mode.getSelectedValue().equals("BackTrack") || this.mode.getSelectedValue().equals("ClassicBackTrack")
            )
            && ClientManagers.moduleManager.packetHandler.isDelayingIncoming();
         boolean flag2 = (this.mode.getSelectedValue().equals("LagTrack") || this.mode.getSelectedValue().equals("Gomme")) && ClientManagers.moduleManager.packetHandler.hasQueuedOutgoingPackets();
         if (flag || flag2) {
            if (this.targetESP.getSelectedValue().equals("Classic")) {
               boolean isBlurActive = this.target.isBlurActive();
               BlurShadowManager.enableOneShotPass(isBlurActive, this.target.isShadowActive());
               BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderClassicTargetBox(shaderRenderPass));
            }

            if (flag2 && this.playerESP.getSelectedValue().equals("Classic") && mc.thePlayer != null && (mc.gameSettings.thirdPersonView != 0 || !this.onlyInThirdPerson.getValue())) {
               boolean isBlurActive2 = this.player.isBlurActive();
               BlurShadowManager.enableOneShotPass(isBlurActive2, this.player.isShadowActive());
               RenderPassTask renderPassTask = shaderRenderPass2 -> {
                  EntityPlayerSP thePlayer = mc.thePlayer;
                  ColorSetting classicColor22 = this.classicColor2;
                  float value = (float)this.lineWidthMe.getValue();
                  boolean flag3 = false;
                  short s = 255;
                  float f2 = value;
                  ColorSetting colorSetting = classicColor22;
                  RenderUtil.drawEntityEspBox(thePlayer, colorSetting, f2, s, flag3);
               };
               boolean flag4 = false;
               BlurShadowManager.submitRenderTask(flag4, renderPassTask);
            }
         }

         RenderUtil.end3DRender();
      }
   }

   @Override
   public void onRenderEntity(EventRenderEntity event) {
      String selectedValue = this.mode.getSelectedValue();
      switch (selectedValue) {
         case "Ping":
         case "ClassicBackTrack":
         case "BackTrack":
            if (this.currentTarget != null && ClientManagers.moduleManager.packetHandler.isDelayingIncoming() && this.currentTarget.equals(event.getEntity())) {
               BooleansSetting targetESP2 = this.targetESP;
               if (this.isPlayerModelEspMode(targetESP2)) {
                  boolean isBlurActive = this.target.isBlurActive();
                  BlurShadowManager.enableOneShotPass(isBlurActive, this.target.isShadowActive());
                  RendererLivingEntity renderer = event.getRenderer();
                  BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderBackTrackTargetPass(event, renderer, shaderRenderPass));
               }
            }
            break;
         case "LagTrack":
         case "Gomme":
            if (ClientManagers.moduleManager.packetHandler.hasQueuedOutgoingPackets()) {
               if (this.currentTarget != null && this.currentTarget.equals(event.getEntity())) {
                  BooleansSetting targetESP3 = this.targetESP;
                  if (this.isPlayerModelEspMode(targetESP3)) {
                     boolean isBlurActive2 = this.target.isBlurActive();
                     BlurShadowManager.enableOneShotPass(isBlurActive2, this.target.isShadowActive());
                     RendererLivingEntity renderer2 = event.getRenderer();
                     RendererLivingEntity rendererLivingEntity = renderer2;
                     BlurShadowManager.submitRenderTask(false, shaderRenderPass2 -> this.renderLagTrackTargetPass(event, rendererLivingEntity, shaderRenderPass2));
                  }
               }

               if (this.currentTarget != null && mc.thePlayer != null && mc.thePlayer.equals(event.getEntity())) {
                  BooleansSetting playerESP2 = this.playerESP;
                  if (this.isPlayerModelEspMode(playerESP2) && (mc.gameSettings.thirdPersonView != 0 || !this.onlyInThirdPerson.getValue())) {
                     boolean isBlurActive3 = this.player.isBlurActive();
                     BlurShadowManager.enableOneShotPass(isBlurActive3, this.player.isShadowActive());
                     RendererLivingEntity renderer3 = event.getRenderer();
                     BlurShadowManager.submitRenderTask(false, shaderRenderPass3 -> {
                        boolean flag = this.playerESP.getSelectedValue().equals("PlayerModelArmor");
                        this.renderPlayerModel(event, renderer3, flag);
                     });
                  }
               }
            }
      }
   }

   private Vec3 getServerEntityPos(EntityLivingBase entity) {
      return new Vec3((double)entity.serverPosX / 32.0, (double)entity.serverPosY / 32.0, (double)entity.serverPosZ / 32.0);
   }

   private boolean passesClickCheck() {
      return MouseInputUtil.isButtonDown(0) || !this.clickCheck.getValue();
   }

   private boolean checkHurtTimeCondition(EntityPlayerSP player2) {
      if (!this.stopBackTrackAtHurtTime.getValue()) {
         boolean flag = false;
         this.setWaitingForHurtTime(flag);
         boolean flag2 = false;
         this.setReceivedVelocity(flag2);
         return true;
      } else if (this.isReceivedVelocity() && this.isWaitingForHurtTime()) {
         return false;
      } else if (!this.isReceivedVelocity()) {
         return true;
      } else {
         boolean flag3 = (double)player2.hurtTime <= this.continueWithBackTrackAtHurtTime.getValue();
         if (flag3) {
            boolean flag4 = false;
            this.setReceivedVelocity(flag4);
         }

         return flag3;
      }
   }

   private boolean isPlayerModelEspMode(BooleansSetting booleansSetting) {
      return booleansSetting.getSelectedValue().equals("PlayerModel") || booleansSetting.getSelectedValue().equals("PlayerModelArmor");
   }

   private double getEffectiveReach() {
      return ClientManagers.moduleManager.killAura.isToggled() ? ClientManagers.moduleManager.killAura.currentRange : (ClientManagers.moduleManager.range.isToggled() ? ClientManagers.moduleManager.range.getCurrentRange() : 3.0);
   }

   private void updateLagTrackState() {
      boolean lagTrackActive2 = this.lagTrackActive;
      this.lagTrackActive = this.shouldContinueLagTrack();
      if (this.movePacketSentAfterAttack && this.attackSentInLag) {
         this.attackSentInLag = false;
         this.movePacketSentAfterAttack = false;
         this.lagTrackActive = false;
      }

      if (!this.lagTrackActive && lagTrackActive2) {
         this.betweenLagsTimer.reset();
      }

      if (!this.lagTrackActive) {
         this.lagPacketCount = 0;
      }
   }

   private boolean shouldContinueLagTrack() {
      EntityLivingBase currentTarget2 = this.currentTarget;
      EntityPlayerSP thePlayer = mc.thePlayer;
      if (currentTarget2 != null && thePlayer != null && mc.theWorld != null) {
         double realPosX = currentTarget2.realPosX;
         double realPosY = currentTarget2.realPosY;
         double realPosZ = currentTarget2.realPosZ;
         Vec3 serverEntityPos = this.getServerEntityPos(currentTarget2);
         AxisAlignedBB box = RotationUtil.createEntityBoxAt(currentTarget2, serverEntityPos);
         AxisAlignedBB box2 = RotationUtil.createEntityBoxAt(currentTarget2, realPosX, realPosY, realPosZ);
         Vec3 nearestPointInBox = RotationUtil.getNearestPointInBox(box);
         Vec3 nearestPointInBox2 = RotationUtil.getNearestPointInBox(box2);
         Vec3 positionEyes = thePlayer.getPositionEyes(1.0F);
         boolean lagTrackActive2 = this.lagTrackActive;
         boolean flag = this.checkAbortAndStartDistance(thePlayer, lagTrackActive2, currentTarget2);
         if (this.calculateWithOwnPingLagTrack.getValue()) {
            int n = Math.max(0, this.lagPacketCount * 50 - ServerPingUtil.getPing());
            this.effectiveLagPackets = (int)((double)n / 50.0);
         } else {
            this.effectiveLagPackets = this.lagPacketCount;
         }

         if (this.passesClickCheck() && this.checkHurtTimeCondition(thePlayer)) {
            TimeHelper betweenLagsTimer2 = this.betweenLagsTimer;
            double value = this.delayBetweenLags.getValue();
            if (betweenLagsTimer2.reached(value) && (this.outgoingReady && this.incomingReady || this.lagTrackActive) && flag) {
               double d = positionEyes.distanceTo(nearestPointInBox2);
               double d2 = positionEyes.distanceTo(nearestPointInBox);
               double d3 = d;
               if (this.exceedsDistanceThreshold(d3, d2)) {
                  if (this.isWithinMaxHitRange(thePlayer, currentTarget2) && this.effectiveLagPackets <= this.maxLagPackets.getLastIntValue()) {
                     this.maxLagPackets.nextValue();
                     if (this.outgoingReady && this.incomingReady) {
                        this.lagPacketCount = 0;
                     }

                     return true;
                  }
               }
            }
         }
      }

      this.attackedTarget = false;
      return false;
   }

   private EntityLivingBase findTarget() {
      if (mc.thePlayer != null && mc.theWorld != null && (!this.notWhileScaffold.getValue() || !BlockUtil.isScaffoldActiveAndValid())) {
         EntityLivingBase activeTarget = CombatTargetUtil.getActiveTarget();
         if (!this.onlyKillAura.getValue() && activeTarget == null) {
            EntityTargetSelector entityTargetSelector = EntityTargetSelector.of(mc.theWorld.loadedEntityList);
            EntityTargetFilter targetFilter = this.targetSelection.getTargetFilter();
            EntityTargetFilter entityTargetFilter = targetFilter;
            entityTargetSelector = entityTargetSelector.withTargetFilter(entityTargetFilter);
            double value = this.preAimRange.getValue();
            entityTargetSelector = entityTargetSelector.maxDistance(value).filterFocus();
            TargetSelectionSettings targetSelection2 = this.targetSelection;
            Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
            double d = ClientManagers.moduleManager.killAura.isToggled() && ClientManagers.moduleManager.killAura.target != null
               ? ClientManagers.moduleManager.killAura.currentRange
               : (ClientManagers.moduleManager.range.isCombatRangeActive() ? ClientManagers.moduleManager.range.getCurrentRange() : 3.0);
            Rotation rotation = previousRotation;
            ToDoubleFunction toDoubleFunction = targetSelection2.createTargetComparatorKey(rotation, d);
            Optional optional = entityTargetSelector.sortBy(toDoubleFunction).findBest();
            return optional.isPresent() && optional.get() instanceof EntityLivingBase ? (EntityLivingBase)optional.get() : null;
         } else {
            return activeTarget;
         }
      } else {
         return null;
      }
   }

   public void renderTargetModel(EventRenderEntity event, EntityLivingBase entity, RendererLivingEntity rendererLivingEntity, boolean flag) {
      double d;
      double d2;
      double d3;
      if (this.smoothRendering.getValue() && entity.interpolatedServerPos != null && entity.prevInterpolatedServerPos != null) {
         d = entity.prevInterpolatedServerPos.xCoord + (entity.interpolatedServerPos.xCoord - entity.prevInterpolatedServerPos.xCoord) * (double)mc.getTimer().renderPartialTicks;
         d2 = entity.prevInterpolatedServerPos.yCoord + (entity.interpolatedServerPos.yCoord - entity.prevInterpolatedServerPos.yCoord) * (double)mc.getTimer().renderPartialTicks;
         d3 = entity.prevInterpolatedServerPos.zCoord + (entity.interpolatedServerPos.zCoord - entity.prevInterpolatedServerPos.zCoord) * (double)mc.getTimer().renderPartialTicks;
      } else {
         d = entity.prevRawServerPos.xCoord + (entity.rawServerPos.xCoord - entity.prevRawServerPos.xCoord) * (double)mc.getTimer().renderPartialTicks;
         d2 = entity.prevRawServerPos.yCoord + (entity.rawServerPos.yCoord - entity.prevRawServerPos.yCoord) * (double)mc.getTimer().renderPartialTicks;
         d3 = entity.prevRawServerPos.zCoord + (entity.rawServerPos.zCoord - entity.prevRawServerPos.zCoord) * (double)mc.getTimer().renderPartialTicks;
      }

      event.renderEntityModel(entity, rendererLivingEntity, d, d2, d3, flag);
   }

   public void renderPlayerModel(EventRenderEntity event, RendererLivingEntity rendererLivingEntity, boolean flag) {
      Vec3 serverPos = mc.thePlayer.getServerPos();
      double zCoord = serverPos.zCoord;
      double yCoord = serverPos.yCoord;
      double xCoord = serverPos.xCoord;
      EntityPlayerSP thePlayer = mc.thePlayer;
      event.renderEntityModel(thePlayer, rendererLivingEntity, xCoord, yCoord, zCoord, flag);
   }

   public boolean isBacktracking() {
      return this.backtracking;
   }

   public void setPredictedTargetPos(double[] doubles) {
      this.predictedTargetPos[0] = doubles[0];
      this.predictedTargetPos[1] = doubles[1];
      this.predictedTargetPos[2] = doubles[2];
   }

   private synchronized boolean isReceivedVelocity() {
      return this.receivedVelocity;
   }

   private synchronized void setReceivedVelocity(boolean flag) {
      this.receivedVelocity = flag;
   }

   private synchronized boolean isWaitingForHurtTime() {
      return this.waitingForHurtTime;
   }

   private synchronized void setWaitingForHurtTime(boolean flag) {
      this.waitingForHurtTime = flag;
   }

   private boolean exceedsDistanceThreshold(double d, double d2) {
      return d > d2 + this.threshold.getValue();
   }

   private void renderLagTrackTargetPass(EventRenderEntity event, RendererLivingEntity rendererLivingEntity, ShaderRenderPass shaderRenderPass) {
      EntityLivingBase currentTarget2 = this.currentTarget;
      boolean flag = this.targetESP.getSelectedValue().equals("PlayerModelArmor");
      EntityLivingBase entity = currentTarget2;
      this.renderTargetModel(event, entity, rendererLivingEntity, flag);
   }

   private void renderBackTrackTargetPass(EventRenderEntity event, RendererLivingEntity rendererLivingEntity, ShaderRenderPass shaderRenderPass) {
      EntityLivingBase currentTarget2 = this.currentTarget;
      boolean flag = this.targetESP.getSelectedValue().equals("PlayerModelArmor");
      EntityLivingBase entity = currentTarget2;
      this.renderTargetModel(event, entity, rendererLivingEntity, flag);
   }

   private void renderClassicTargetBox(ShaderRenderPass shaderRenderPass) {
      EntityLivingBase currentTarget2 = this.currentTarget;
      ColorSetting classicColor3 = this.classicColor;
      float value = (float)this.lineWidthTarget.getValue();
      boolean value2 = this.smoothRendering.getValue();
      short s = 255;
      float f = value;
      ColorSetting colorSetting = classicColor3;
      RenderUtil.drawEntityEspBox(currentTarget2, colorSetting, f, s, value2);
   }

   private boolean isPlayerClassicColorVisible() {
      return this.playerESP.getSelectedValue().equals("Classic");
   }

   private boolean isPlayerEspGroupVisible() {
      return !this.mode.getSelectedValue().equals("BackTrack");
   }

   private boolean isTargetLineWidthVisible() {
      return this.targetESP.getSelectedValue().equals("Classic");
   }

   private boolean isTargetClassicColorVisible() {
      return this.targetESP.getSelectedValue().equals("Classic");
   }

   private boolean isPreAimRangeVisible() {
      return !this.onlyKillAura.getValue();
   }

   private boolean isStopAtHurtTimeVisible() {
      return !this.mode.getSelectedValue().equals("ClassicBackTrack");
   }

   private boolean isStopOnAttackMaxHurtTimeVisible() {
      return !this.abortCriterion.getSelectedValue().equals("None") && !this.mode.getSelectedValue().equals("ClassicBackTrack");
   }

   private boolean isFixPacketOrderVisible() {
      return this.abortCriterion.getSelectedValue().equals("OnAttack") && !this.mode.getSelectedValue().equals("ClassicBackTrack");
   }

   private boolean isStopOnAttackRangeVisible() {
      return this.abortCriterion.getSelectedValue().equals("OnRange") && !this.mode.getSelectedValue().equals("ClassicBackTrack");
   }

   private boolean isCheckStartDistanceVisible() {
      return !this.mode.getSelectedValue().equals("ClassicBackTrack");
   }

   private boolean isDynamicPingVisible() {
      return this.mode.getSelectedValue().equals("Ping");
   }

   private boolean isSubtractPingInPingModeVisible() {
      return this.mode.getSelectedValue().equals("Ping");
   }

   private boolean isDelayBetweenPingBacktracksVisible() {
      return this.mode.getSelectedValue().equals("Ping");
   }

   private boolean isPingDelayVisible() {
      return this.mode.getSelectedValue().equals("Ping");
   }

   private boolean isGommeLagPacketsVisible() {
      return this.mode.getSelectedValue().equals("Gomme");
   }

   private boolean isLagTrackSubtractPingVisible() {
      return this.mode.getSelectedValue().equals("LagTrack");
   }

   private boolean isLagTrackCountModeVisible() {
      return this.mode.getSelectedValue().equals("LagTrack");
   }

   private boolean isDelayBetweenLagsVisible() {
      return this.mode.getSelectedValue().equals("LagTrack");
   }

   private boolean isLagTrackMaxPacketsVisible() {
      return this.mode.getSelectedValue().equals("LagTrack");
   }

   private boolean isBackTrackDynamicVisible() {
      return this.mode.getSelectedValue().equals("BackTrack");
   }

   private boolean isBackTrackPredictVisible() {
      return this.mode.getSelectedValue().equals("BackTrack");
   }

   private boolean isForceDelayVisible() {
      return this.mode.getSelectedValue().equals("BackTrack");
   }

   private boolean isSubtractPingVisible() {
      return this.mode.getSelectedValue().equals("BackTrack");
   }

   private boolean isDelayBetweenBacktracksVisible() {
      return this.mode.getSelectedValue().equals("BackTrack");
   }

   private boolean isBackTrackTimeVisible() {
      return this.mode.getSelectedValue().equals("BackTrack");
   }

   private boolean isClassicDynamicVisible() {
      return this.mode.getSelectedValue().equals("ClassicBackTrack");
   }

   private boolean isClassicDelayVisible() {
      return this.mode.getSelectedValue().equals("ClassicBackTrack");
   }

}
