package net.augustus.modules.misc;

import net.minecraft.entity.DataWatcher;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.Mixer.Info;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.modules.combat.KillAura;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.RenderListener;
import net.augustus.events.EventEntityHurt;
import net.augustus.utils.network.PacketFlushMarker;
import net.augustus.utils.simulation.EntityMovementPredictor;
import net.augustus.utils.file.ClientPaths;
import net.augustus.events.listeners.EntityHurtListener;
import net.augustus.events.EventPacketDelay;
import net.augustus.utils.math.NumberFormatUtil;
import net.augustus.utils.math.DoubleSampleWindow;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.utils.math.MathStatisticsUtil;
import net.augustus.utils.rotation.RayTraceUtil;
import net.augustus.utils.math.StatisticalSampleBuffer;
import net.augustus.events.EventMoveInput;
import net.augustus.render.RenderUtil;
import net.augustus.utils.TickCounter;
import net.augustus.events.EventAttackEntity;
import net.augustus.Augustus;
import net.augustus.events.listeners.SoundListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.AttackEntityListener;
import net.augustus.events.EventSound;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;

public class Debug extends Module implements AttackEntityListener, PacketListener, TickListener, RenderListener, SoundListener, MoveInputListener, PacketDelayListener, EntityHurtListener {
   public final BooleanValue rangeDebug;
   public final BooleanValue ignoreBackTrack;
   public final StringValue minRange;
   public final StringValue decimalPlaces;
   public final BooleanValue timerRangeDebug;
   public final BooleanValue lagRangeDebug;
   public final BooleanValue timeManipulation;
   public final BooleanValue hitBug;
   public final BooleanValue serversideChat;
   public final BooleanValue soundName;
   public final BooleanValue blink;
   public final BooleanValue transaction;
   public final BooleanValue onlyDebugChat;
   public final BooleanValue kit1V1Damage;
   public final BooleanValue autoClickerDebug;
   public final BooleanValue placeDebug;
   public final BooleanValue sneakDebug;
   public final BooleanValue lostHitDebug;
   public final BooleanValue motionDebug;
   public final DoubleValue combatDebug;
   public final BooleanValue combatDebug2;
   public final BooleanValue combatDebugOnlyWhenKillaura;
   public BooleanValue devToggleA;
   public BooleanValue devToggleB;
   public BooleanValue devToggleC;
   public BooleanValue healthRoundingDebug;
   public BooleanValue velocityPacketDebug;
   public BooleanValue legitDamageCheck;
   public BooleanValue reducedDamageCheck;
   public BooleanValue reducedDamageAlert;
   public BooleanValue printRawDamageValues;
   public BooleanValue hitCritCounterHud;
   public BooleanValue inputLogEnabled;
   public BooleanValue devToggleD;
   public BooleanValue devToggleE;
   public BooleanValue devToggleF;
   public BooleanValue devToggleG;
   public BooleanValue sprintResetDebug;
   public BooleansSetting packetLoggerMode;
   public BooleanValue entityPredictionDebug;
   public BooleanValue devToggleH;
   public BooleanValue devToggleI;
   public BooleanValue devToggleJ;
   private double lastMotionY;
   private final TimeHelper hitBugTimer;
   private final CopyOnWriteArrayList pendingPacketLogs;
   private final ArrayList actualDamageHistory;
   private final ArrayList expectedDamageHistory;
   private int reducedDamageViolations;
   private int ownHitCount;
   private int ownCritCount;
   private int opponentHitCount;
   private int opponentCritCount;
   private int clicksThisTick;
   private final TimeHelper clickSampleTimer;
   private final ArrayList clicksPerTickHistory;
   private float unusedDamageAccumulatorA;
   private float unusedDamageAccumulatorB;
   private EntityPlayer inputLogTarget;
   private EntityPlayer lastAttackedPlayer;
   private final TimeHelper lastAttackedPlayerTimer;
   private final ArrayList unusedDebugListA;
   private final ArrayList unusedDebugListB;
   private final TimeHelper boatDestroyDelayTimer;
   private final TickCounter tickCounter;
   private int transactionsThisSecond;
   private int transactionsPerSecond;
   private EntityLivingBase sprintResetTarget;
   private final TimeHelper sprintResetTargetTimer;
   private int sprintResetDurationTicks;
   private int ticksSinceHurtReset;
   private final LinkedList sprintResetTimestamps;
   private final LinkedList sprintResetDurations;
   private long lastTransactionTimeMs;
   private final ArrayList transactionIntervals;
   private EntityLivingBase lastAttackedEntity;
   private final TimeHelper lastAttackedEntityTimer;
   private EntityPlayer nearestPlayer;
   private int ticksSinceLastPlace;
   private int placesThisTick;
   private final StatisticalSampleBuffer placeRateBuffer;
   private int sneakTicks;
   private int ticksSinceLastSneak;
   private double pendingDamageAmount;
   private float lastHealthValue;
   private float unusedMetricFloat;
   private long unusedTimestampA;
   private long unusedTimestampB;
   private int unusedCounterA;
   private DoubleSampleWindow sampleWindowA;
   private DoubleSampleWindow sampleWindowB;
   private DoubleSampleWindow sampleWindowC;
   private boolean unusedFlagA;
   private int unusedCounterB;
   private final List unusedConcurrentListA;
   private final List unusedConcurrentListB;
   private int unusedCounterC;
   private int lastAttackTick;
   private final StatisticalSampleBuffer statBufferA;
   private final StatisticalSampleBuffer statBufferB;
   private final StatisticalSampleBuffer statBufferC;
   private final StatisticalSampleBuffer statBufferD;
   private final StatisticalSampleBuffer statBufferE;
   private final StatisticalSampleBuffer statBufferF;
   private final StatisticalSampleBuffer statBufferG;
   private int lastAttackedEntityHurtTime;
   private boolean unusedFlagB;
   private int unusedCounterD;
   private double lastRecordedHealth;
   private double lastRecordedAbsorption;
   private static final String[] c = new String[0];
   private static final String[] d = new String[0];

   public Debug() {
       super("Debug", "Fehlersuche", Color.BLACK, Categorys.MISC);
      this.rangeDebug = (BooleanValue)(new BooleanValue(this, "RangeDebug", false)).markVisualOnly();
      BooleanValue booleanValue = new BooleanValue(this, "IgnoreBackTrack", false);
      BooleanValue rangeDebug2 = this.rangeDebug;
      this.rangeDebug.getClass();
      BooleanValue booleanValue2 = rangeDebug2;
      SettingVisibilityCondition condition = () -> booleanValue2.getValue();
      this.ignoreBackTrack = (BooleanValue)((BooleanValue)booleanValue.withVisibility(condition)).markVisualOnly();
      StringValue stringValue = new StringValue(this, "MinRange", 3.0, 0.0, 6.0, 2);
      rangeDebug2 = this.rangeDebug;
      this.rangeDebug.getClass();
      BooleanValue booleanValue3 = rangeDebug2;
      SettingVisibilityCondition condition2 = () -> booleanValue3.getValue();
      this.minRange = (StringValue)stringValue.visibleWhen(condition2).markVisualOnly();
      StringValue stringValue2 = new StringValue(this, "DecimalPlaces", 4.0, 1.0, 10.0, 0);
      rangeDebug2 = this.rangeDebug;
      this.rangeDebug.getClass();
      BooleanValue booleanValue4 = rangeDebug2;
      SettingVisibilityCondition condition3 = () -> booleanValue4.getValue();
      this.decimalPlaces = (StringValue)stringValue2.visibleWhen(condition3).markVisualOnly();
      this.timerRangeDebug = (BooleanValue)(new BooleanValue(this, "TimerRangeDebug", false)).markVisualOnly();
      this.lagRangeDebug = (BooleanValue)(new BooleanValue(this, "LagRangeDebug", false)).markVisualOnly();
      this.timeManipulation = (BooleanValue)(new BooleanValue(this, "TimeManipulation", false)).markVisualOnly();
      this.hitBug = (BooleanValue)(new BooleanValue(this, "HitBug", false)).markVisualOnly();
      this.serversideChat = (BooleanValue)(new BooleanValue(this, "ServersideChat", false)).markVisualOnly();
      this.soundName = (BooleanValue)(new BooleanValue(this, "SoundName", false)).markVisualOnly();
      this.blink = (BooleanValue)(new BooleanValue(this, "Blink", false)).markVisualOnly();
      this.transaction = (BooleanValue)(new BooleanValue(this, "Transaction", false)).markVisualOnly();
      this.onlyDebugChat = (BooleanValue)(new BooleanValue(this, "OnlyDebugChat", false)).markVisualOnly();
      this.kit1V1Damage = (BooleanValue)(new BooleanValue(this, "Kit1V1Damage", false)).markVisualOnly();
      this.autoClickerDebug = (BooleanValue)(new BooleanValue(this, "AutoClickerDebug", false)).markVisualOnly();
      this.placeDebug = (BooleanValue)(new BooleanValue(this, "PlaceDebug", false)).markVisualOnly();
      this.sneakDebug = (BooleanValue)(new BooleanValue(this, "SneakDebug", false)).markVisualOnly();
      this.lostHitDebug = (BooleanValue)(new BooleanValue(this, "LostHitDebug", false)).markVisualOnly();
      this.motionDebug = (BooleanValue)(new BooleanValue(this, "Motion Debug", false)).markVisualOnly();
      this.combatDebug = new DoubleValue(this, "Combat Debug");
      this.combatDebug2 = (BooleanValue)(new BooleanValue(this, "CombatDebug", false, this.combatDebug)).markVisualOnly();
      BooleanValue booleanValue5 = new BooleanValue(this, "Combat debug only when killaura", false, this.combatDebug);
      rangeDebug2 = this.combatDebug2;
      this.combatDebug2.getClass();
      BooleanValue booleanValue6 = rangeDebug2;
      SettingVisibilityCondition condition4 = () -> booleanValue6.getValue();
      this.combatDebugOnlyWhenKillaura = (BooleanValue)((BooleanValue)booleanValue5.withVisibility(condition4)).markVisualOnly();
      this.devToggleA = null;
      this.devToggleB = null;
      this.devToggleC = null;
      this.healthRoundingDebug = null;
      this.velocityPacketDebug = null;
      this.legitDamageCheck = null;
      this.reducedDamageCheck = null;
      this.reducedDamageAlert = null;
      this.printRawDamageValues = null;
      this.hitCritCounterHud = null;
      this.inputLogEnabled = null;
      this.devToggleD = null;
      this.devToggleE = null;
      this.devToggleF = null;
      this.devToggleG = null;
      this.sprintResetDebug = null;
      this.packetLoggerMode = null;
      this.entityPredictionDebug = null;
      this.devToggleH = null;
      this.devToggleI = null;
      this.devToggleJ = null;
      this.lastMotionY = Double.NaN;
      this.hitBugTimer = new TimeHelper();
      this.pendingPacketLogs = new CopyOnWriteArrayList();
      this.actualDamageHistory = new ArrayList();
      this.expectedDamageHistory = new ArrayList();
      this.reducedDamageViolations = 0;
      this.ownHitCount = 0;
      this.ownCritCount = 0;
      this.opponentHitCount = 0;
      this.opponentCritCount = 0;
      this.clicksThisTick = 0;
      this.clickSampleTimer = new TimeHelper();
      this.clicksPerTickHistory = new ArrayList();
      this.lastAttackedPlayerTimer = new TimeHelper();
      this.unusedDebugListA = new ArrayList();
      this.unusedDebugListB = new ArrayList();
      this.boatDestroyDelayTimer = new TimeHelper();
      this.tickCounter = new TickCounter();
      this.transactionsThisSecond = 0;
      this.transactionsPerSecond = 0;
      this.sprintResetTarget = null;
      this.sprintResetTargetTimer = new TimeHelper();
      this.sprintResetDurationTicks = -1;
      this.ticksSinceHurtReset = 0;
      this.sprintResetTimestamps = new LinkedList();
      this.sprintResetDurations = new LinkedList();
      this.lastTransactionTimeMs = 0L;
      this.transactionIntervals = new ArrayList();
      this.lastAttackedEntity = null;
      this.lastAttackedEntityTimer = new TimeHelper();
      this.nearestPlayer = null;
      this.ticksSinceLastPlace = 0;
      this.placesThisTick = 0;
      this.placeRateBuffer = new StatisticalSampleBuffer(20);
      this.sneakTicks = 0;
      this.ticksSinceLastSneak = 0;
      this.pendingDamageAmount = 0.0;
      this.unusedMetricFloat = 0.0F;
      this.unusedTimestampA = 0L;
      this.unusedTimestampB = 0L;
      this.unusedCounterA = 0;
      this.sampleWindowA = new DoubleSampleWindow(100);
      this.sampleWindowB = new DoubleSampleWindow(100);
      this.sampleWindowC = new DoubleSampleWindow(100);
      this.unusedConcurrentListA = Lists.newCopyOnWriteArrayList();
      this.unusedConcurrentListB = Lists.newCopyOnWriteArrayList();
      this.unusedCounterC = 0;
      this.statBufferA = new StatisticalSampleBuffer(20);
      this.statBufferB = new StatisticalSampleBuffer(20);
      this.statBufferC = new StatisticalSampleBuffer(20);
      this.statBufferD = new StatisticalSampleBuffer(20);
      this.statBufferE = new StatisticalSampleBuffer(20);
      this.statBufferF = new StatisticalSampleBuffer(20);
      this.statBufferG = new StatisticalSampleBuffer(20);
      this.lastAttackedEntityHurtTime = 0;
      this.unusedFlagB = false;
      this.unusedCounterD = 0;
      this.lastRecordedHealth = 0.0;
      this.lastRecordedAbsorption = 0.0;
      Augustus.getInstance().getClass();
   }

   @Override
   public void onEnable() {
      this.opponentHitCount = 0;
      this.ownHitCount = 0;
      this.ownCritCount = 0;
      this.opponentCritCount = 0;
      this.unusedDamageAccumulatorA = 0.0F;
      this.unusedDamageAccumulatorB = 0.0F;
   }

   @Override
   public void onDisable() {
      this.sprintResetTarget = null;
      this.lastAttackedEntity = null;
      this.inputLogTarget = null;
      this.lastAttackedPlayer = null;
   }

   @Override
   public void updateEventSubscriptions() {
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      this.onAttackEntity();
      short s = 2000;
      byte b = 21;
      this.toggleEventSubscription(b, s);
      this.onAttackEntity();
      this.onAttackEntity();
   }

   @Override
   public void onTick() {
      if (this.placeDebug.getValue()) {
         this.ticksSinceLastPlace++;
         double placesThisTick2 = (double)this.placesThisTick;
         this.placeRateBuffer.add(placesThisTick2);
         this.placesThisTick = 0;
      }

      if (this.motionDebug.getValue()
         && (
            mc.thePlayer.motionX != 0.0
               || mc.thePlayer.motionZ != 0.0
               || mc.thePlayer.motionY != this.lastMotionY
               || this.lastMotionY != -0.0784000015258789
               || mc.thePlayer.ticksExisted % 20 == 0
         )) {
         String str = "motion=(x=" + mc.thePlayer.motionX + ", y=" + mc.thePlayer.motionY + ", z=" + mc.thePlayer.motionZ + ")";
         this.sendChatMessage2(str);
         this.lastMotionY = mc.thePlayer.motionY;
      }

      this.nearestPlayer = this.findNearestPlayer();
      long l = 2000L;
      if (this.sprintResetTargetTimer.reached2(l)) {
         this.sprintResetTarget = null;
         this.sprintResetDurationTicks = -1;
      }

      if (this.sprintResetDebug != null && this.sprintResetDebug.getValue() && this.sprintResetTarget != null) {
         this.ticksSinceHurtReset++;
         if (this.sprintResetDurationTicks >= 0) {
            this.sprintResetDurationTicks++;
         }

         if (this.sprintResetTarget.hurtTime == 9) {
            if (this.sprintResetTimestamps.isEmpty()) {
               String str2 = "No Sprint Reset";
               this.sendChatMessage2(str2);
            } else {
               String str3 = "MoreKBTimes: "
                  + Arrays.toString(this.sprintResetTimestamps.toArray())
                  + ";  MoreKBDurations: "
                  + Arrays.toString(this.sprintResetDurations.toArray())
                  + ";  "
                  + this.ticksSinceHurtReset;
               this.sendChatMessage2(str3);
            }

            this.sprintResetTimestamps.clear();
            this.sprintResetDurations.clear();
            this.ticksSinceHurtReset = 0;
         }
      }

      this.tickCounter.increment();
      byte b = 20;
      if (this.tickCounter.hasReached2(b)) {
         this.tickCounter.reset();
         this.transactionsPerSecond = this.transactionsThisSecond;
         this.transactionsThisSecond = 0;
      }
   }

   private void checkMissedCombatHits() {
      EntityPlayerSP thePlayer = mc.thePlayer;
      KillAura killAura = Augustus.getInstance().getModuleManager().killAura;

      for (EntityPlayer player : (Iterable<EntityPlayer>)(mc.theWorld.playerEntities)) {
         double d2;
         if (player != thePlayer && player.hurtTime == 0 && thePlayer.getDistanceSqToEntity(player) <= 36.0 && (d2 = (double)thePlayer.getDistanceToEntityBox(player)) <= 3.0) {
            if (mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
               MovingObjectPosition hit = RayTraceUtil.rayTrace(
                  1.0F, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, 15.0, 15.0, 0.0
               );
               if (hit.entityHit == player) {
                  String str = player.getName()
                     + " in reach ("
                     + MathStatisticsUtil.roundToDecimalPlaces(d2, 3)
                     + ") but not in raytrace (tE="
                     + thePlayer.ticksExisted
                     + ", raydist="
                     + MathStatisticsUtil.roundToDecimalPlaces(hit.hitVec.distanceTo(mc.thePlayer.getPositionEyes(1.0F)), 3)
                     + ") "
                     + mc.objectMouseOver.entityHit;
                  this.sendChatMessage2(str);
               } else {
                  String str2 = player.getName()
                     + " in reach ("
                     + MathStatisticsUtil.roundToDecimalPlaces(d2, 3)
                     + ") but not in raytrace (tE="
                     + thePlayer.ticksExisted
                     + ") "
                     + mc.objectMouseOver.entityHit;
                  this.sendChatMessage2(str2);
               }

               return;
            }

            if (mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit != player) {
               String str3 = "Other entity (" + mc.objectMouseOver.entityHit.getName() + ") in raytrace (tE=" + thePlayer.ticksExisted + ")";
               this.sendChatMessage2(str3);
            }

            if (killAura.isToggled() && killAura.target == null) {
               String str4 = player.getName() + " in reach (" + MathStatisticsUtil.roundToDecimalPlaces(d2, 3) + ") but isn't a killaura target (tE=" + thePlayer.ticksExisted + ")";
               this.sendChatMessage2(str4);
               return;
            }

            if (thePlayer.ticksExisted != this.lastAttackTick) {
               String str5 = "No attack this tick but "
                  + player.getName()
                  + " is hittable (tE="
                  + thePlayer.ticksExisted
                  + ", lastAttack="
                  + (thePlayer.ticksExisted - this.lastAttackTick)
                  + ")";
               this.sendChatMessage2(str5);
               return;
            }
         }
      }
   }

   @Override
   public void onTick2() {
      if (this.combatDebug2.getValue() && (!this.combatDebugOnlyWhenKillaura.getValue() || Augustus.getInstance().getModuleManager().killAura.isToggled())) {
         this.checkMissedCombatHits();
      }

      Augustus.getInstance().getClass();
      if (this.entityPredictionDebug != null && this.entityPredictionDebug.getValue()) {
         EntityPlayer player = null;
         double d2 = 10000.0;

         for (EntityPlayer player2 : (Iterable<EntityPlayer>)(mc.theWorld.playerEntities)) {
            if (player2 != mc.thePlayer) {
               double d3 = (double)player2.getDistanceSqToEntityFloat(mc.thePlayer);
               if (d3 < d2) {
                  player = player2;
                  d2 = d3;
               }
            }
         }

         if (player != null) {
            if (!EntityMovementPredictor.hasInvalidPositionHistory(player)) {
               String str = player.getName() + " " + Arrays.toString(EntityMovementPredictor.estimateStrafeForwardFromServerPos(player));
               this.sendChatMessage2(str);
            }
         }
      }

      long l = 3000L;
      if (this.lastAttackedPlayerTimer.reached2(l)) {
         this.lastAttackedPlayer = null;
      }

      long l2 = 4000L;
      if (this.lastAttackedEntityTimer.reached2(l2)) {
         this.lastAttackedEntity = null;
      }
   }

   public List getInputAudioDevices() {
      Class<TargetDataLine> clazz = TargetDataLine.class;
      return this.getSupportedMixerNames(clazz);
   }

   public List getOutputAudioDevices() {
      Class<SourceDataLine> clazz = SourceDataLine.class;
      return this.getSupportedMixerNames(clazz);
   }

   public TargetDataLine getTargetDataLine(String str) {
      Class<TargetDataLine> clazz = TargetDataLine.class;
      return (TargetDataLine)this.getAudioLine(clazz, str);
   }

   public SourceDataLine getSourceDataLine(String str) {
      Class<SourceDataLine> clazz = SourceDataLine.class;
      return (SourceDataLine)this.getAudioLine(clazz, str);
   }

   public List getSupportedMixerNames(Class clazz) {
      ArrayList list = new ArrayList();
      Info[] mixerInfo = AudioSystem.getMixerInfo();

      for (Info info : mixerInfo) {
         Mixer mixer = AudioSystem.getMixer(info);
         javax.sound.sampled.Line.Info info2 = new javax.sound.sampled.Line.Info(clazz);
         if (mixer.isLineSupported(info2)) {
            list.add(info.getName());
         }
      }

      return list;
   }

   public Object getAudioLine(Class clazz, String str) {
      Info[] mixerInfo = AudioSystem.getMixerInfo();

      for (Info info : mixerInfo) {
         Mixer mixer = AudioSystem.getMixer(info);
         javax.sound.sampled.Line.Info info2 = new javax.sound.sampled.Line.Info(clazz);
         if (mixer.isLineSupported(info2) && info.getName().equals(str)) {
            try {
               return clazz.cast(mixer.getLine(info2));
            } catch (Exception e) {
            }
         }
      }

      return null;
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (this.sneakDebug.getValue()) {
         if (event.isSneak()) {
            this.sneakTicks++;
         } else {
            if (this.sneakTicks > 0) {
               String str = "Sneak ticks: " + this.sneakTicks + "; Sneak delay: " + this.ticksSinceLastSneak;
               this.sendChatMessage2(str);
               this.ticksSinceLastSneak = 0;
            }

            this.ticksSinceLastSneak++;
            this.sneakTicks = 0;
         }
      }

      if (this.inputLogEnabled != null && this.inputLogEnabled.getValue()) {
         if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof EntityPlayer) {
            this.inputLogTarget = (EntityPlayer)mc.objectMouseOver.entityHit;
         }

         if (this.inputLogTarget == null) {
            return;
         }

         File file = new File(ClientPaths.CLIENT_DIR + File.separator + "debug" + File.separator + "InputLog.txt");
         if (!Files.exists(file.getParentFile().toPath())) {
            try {
               Files.createDirectories(file.getParentFile().toPath());
            } catch (IOException e) {
            }
         }

         if (!file.exists()) {
            try {
               file.createNewFile();
            } catch (IOException e2) {
               throw new RuntimeException(e2);
            }
         }

         try {
            Files.write(
               file.toPath(),
               ("Forward: "
                     + event.getMoveForward()
                     + "; Strafe: "
                     + event.getMoveStrafe()
                     + "; TargetHurttime: "
                     + this.inputLogTarget.hurtTime
                     + "; TargetDistance: "
                     + this.inputLogTarget.getDistanceToEntityBox(mc.thePlayer)
                     + "\r")
                  .getBytes(StandardCharsets.UTF_8),
               StandardOpenOption.APPEND
            );
         } catch (IOException e3) {
            throw new RuntimeException(e3);
         }
      }
   }

   @Override
   public void onSound(EventSound event) {
      if (this.hitCritCounterHud != null && this.hitCritCounterHud.getValue() && event.getSoundName().equals("game.player.hurt")) {
         EntityPlayer player = this.findNearestPlayer();
         double x = event.getX();
         double d2 = 0.01;
         double posX = mc.thePlayer.posX;
         double d3 = x;
         if (this.isWithinEpsilon(d3, posX, d2)) {
            double y = event.getY();
            double d4 = 0.01;
            double posY = mc.thePlayer.posY;
            double d5 = y;
            if (this.isWithinEpsilon(d5, posY, d4)) {
               double z = event.getZ();
               double d6 = 0.01;
               double posZ = mc.thePlayer.posZ;
               double d7 = z;
               if (this.isWithinEpsilon(d7, posZ, d6)) {
                  this.opponentHitCount++;
                  return;
               }
            }
         }

         if (player != null) {
            this.ownHitCount++;
         }
      }
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      if (this.hitCritCounterHud != null && this.hitCritCounterHud.getValue()) {
         int n = this.ownHitCount - this.opponentHitCount;
         int n2 = this.opponentHitCount - this.ownHitCount;
         int n3 = this.ownCritCount - this.opponentCritCount;
         int n4 = this.opponentCritCount - this.ownCritCount;
         mc.fontRendererObj
            .drawString(
               "MeHits: §7"
                  + this.ownHitCount
                  + "§f "
                  + (n != 0 ? (n < 0 ? "§c" + n : " §a+" + n) : "")
                  + "§f  Crits: §7"
                  + this.ownCritCount
                  + " §f"
                  + (n3 != 0 ? (n3 < 0 ? "§c" + n3 : " §a+" + n3) : "")
                  + "§f",
               0,
               100,
               -1
            );
         mc.fontRendererObj
            .drawString(
               "OpponentHits: §7"
                  + this.opponentHitCount
                  + "§f "
                  + (n2 != 0 ? (n2 < 0 ? "§c" + n2 : " §a+" + n2) : "")
                  + "§f  Crits: §7"
                  + this.opponentCritCount
                  + " §f"
                  + (n4 != 0 ? (n4 < 0 ? "§c" + n4 : " §a+" + n4) : "")
                  + "§f",
               0,
               120,
               -1
            );
      }

      RenderUtil.resetGlColorState();
   }

   @Override
   public void onEntityHurt(EventEntityHurt event) {
      Augustus.getInstance().getClass();
   }

   @Override
   public void onAttackEntity(EventAttackEntity event) {
      if (event.getTarget() instanceof EntityLivingBase) {
         if (this.sprintResetTarget != event.getTarget()) {
            this.lastAttackedEntityHurtTime = -1;
         }

         this.sprintResetTarget = (EntityLivingBase)event.getTarget();
         this.sprintResetTargetTimer.reset();
      }

      if (event.getTarget() instanceof EntityLivingBase) {
         this.lastAttackedEntity = (EntityLivingBase)event.getTarget();
         this.lastAttackedEntityTimer.reset();
      }

      Augustus.getInstance().getClass();
      if (this.rangeDebug.getValue()) {
         double d2;
         if (this.ignoreBackTrack.getValue()) {
            d2 = mc.thePlayer.getPositionEyes(1.0F).distanceTo(mc.objectMouseOver.hitVec);
         } else {
            MovingObjectPosition hit = RayTraceUtil.rayTraceServerPositions();
            d2 = mc.thePlayer.getPositionEyes(1.0F).distanceTo(hit.hitVec);
         }

         if (d2 >= this.minRange.getValue()) {
            String str = "Attack distance: " + MathStatisticsUtil.formatRounded(d2, (int)this.decimalPlaces.getValue());
            this.outputDebugMessage(str);
         }
      }

      Augustus.getInstance().getClass();
      if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof EntityPlayer) {
         this.lastAttackedPlayer = (EntityPlayer)mc.objectMouseOver.entityHit;
         this.lastAttackedPlayerTimer.reset();
      }
   }

   @Override
   public void onPacketDelay2(EventPacketDelay event) {
      long l = 500L;
      if (!this.boatDestroyDelayTimer.reached2(l)) {
         boolean flag = true;
         event.setDelayed(flag);
      }
   }

   @Override
   public void onPacketDelay(EventPacketDelay event) {
      Packet packet = event.getPacket();
   }

   @Override
   public void onPacket3(EventPacket event) {
   }

   @Override
   public void onPacket5(EventPacket event) {
      Packet packet = event.getPacket();
      if (this.packetLoggerMode != null && (this.packetLoggerMode.getSelectedValue().equals("Outgoing") || this.packetLoggerMode.getSelectedValue().equals("All"))) {
         String str = "Outgoing: ";
         this.logPacketToFile(packet, str);
      }

      if (packet instanceof C02PacketUseEntity) {
         this.lastAttackTick = mc.thePlayer.ticksExisted;
      }

      Augustus.getInstance().getClass();
   }

   @Override
   public void onPacket2(EventPacket event) {
      Packet packet;
      packet = event.getPacket();
      exitBlock:
      if (this.autoClickerDebug.getValue()) {
         long l = 500L;
         if (this.clickSampleTimer.reached2(l)) {
            this.clicksPerTickHistory.clear();
         }

         if (packet instanceof C03PacketPlayer) {
            long l2 = 250L;
            if (!this.clickSampleTimer.reached2(l2)) {
               this.clicksPerTickHistory.add(this.clicksThisTick);

               while (this.clicksPerTickHistory.size() > 20) {
                  this.clicksPerTickHistory.remove(0);
               }

               if (this.clicksPerTickHistory.size() > 19) {
                  StringBuilder sb = new StringBuilder().append("Clicks: ");
                  int clicksThisTick2 = this.clicksThisTick;
                  String str = sb.append(this.getClickCountColorCode(clicksThisTick2))
                     .append(this.clicksThisTick)
                     .append("§r CPS: ")
                     .append(NumberFormatUtil.formatDecimal(MathStatisticsUtil.averageInt(this.clicksPerTickHistory) * 20.0, 0))
                     .toString();
                  this.sendChatMessage2(str);
               } else {
                  StringBuilder sb2 = new StringBuilder().append("Clicks: ");
                  int clicksThisTick3 = this.clicksThisTick;
                  String str2 = sb2.append(this.getClickCountColorCode(clicksThisTick3)).append(this.clicksThisTick).toString();
                  this.sendChatMessage2(str2);
               }

               this.clicksThisTick = 0;
               break exitBlock;
            }
         }

         if (packet instanceof C0APacketAnimation && !mc.playerController.isHittingBlock) {
            this.clickSampleTimer.reset();
            this.clicksThisTick++;
         }
      }

      if (packet instanceof C0BPacketEntityAction && this.sprintResetDebug != null && this.sprintResetDebug.getValue() && this.sprintResetTarget != null) {
         C0BPacketEntityAction.Action action = ((C0BPacketEntityAction)packet).getAction();
         if (action == C0BPacketEntityAction.Action.START_SPRINTING && this.sprintResetDurationTicks >= 0) {
            this.sprintResetTimestamps.add(this.ticksSinceHurtReset);
            this.sprintResetDurations.add(this.sprintResetDurationTicks);
            this.sprintResetDurationTicks = -1;
         }

         if (action == C0BPacketEntityAction.Action.STOP_SPRINTING && !mc.thePlayer.isCollidedHorizontally) {
            this.sprintResetDurationTicks = 0;
         }
      }

      if (packet instanceof C08PacketPlayerBlockPlacement && this.placeDebug.getValue()) {
         C08PacketPlayerBlockPlacement packet2 = (C08PacketPlayerBlockPlacement)packet;
         if (this.placeRateBuffer.isFull()) {
            String str3 = "Delay: " + this.ticksSinceLastPlace + "  AVG: " + this.placeRateBuffer.mean() * 20.0;
            this.sendChatMessage2(str3);
         } else {
            String str4 = "Delay: " + this.ticksSinceLastPlace;
            this.sendChatMessage2(str4);
         }

         this.ticksSinceLastPlace = 0;
         this.placesThisTick++;
      }
   }

   @Override
   public void onPacket4(EventPacket event) {
      Packet packet = event.getPacket();
      Augustus.getInstance().getClass();
      if (this.healthRoundingDebug != null && this.healthRoundingDebug.getValue() && mc.thePlayer != null && packet instanceof S06PacketUpdateHealth) {
         S06PacketUpdateHealth packet2 = (S06PacketUpdateHealth)packet;
         float health = packet2.getHealth();
         float f = this.lastHealthValue - health;
         this.lastHealthValue = health;
         if (f > 0.0F) {
            double d2 = MathStatisticsUtil.roundToDecimalPlaces((double)f, 2);
            double d3 = MathStatisticsUtil.roundToDecimalPlaces((double)f, 3);
            if (d2 != d3) {
               String str = "Damage: " + f + "; R2: " + d2 + "; R3: " + d3;
               this.sendChatMessage2(str);
            }
         }
      }

      if (this.legitDamageCheck != null && this.legitDamageCheck.getValue() && mc.thePlayer != null) {
         if (packet instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)packet).getEntityID() == mc.thePlayer.getEntityId() && this.nearestPlayer != null) {
            EntityPlayer nearestPlayer2 = this.nearestPlayer;
            ItemStack heldItem = nearestPlayer2.getHeldItem();
            float f2 = 1.0F;
            float f3 = 0.0F;
            if (heldItem != null) {
               Item item = heldItem.getItem();
               if (item instanceof ItemSword) {
                  ItemSword itemSword = (ItemSword)item;
                  double enchantmentLevel = (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, heldItem);
                  f2 += itemSword.getAttackDamage();
                  f3 = (float)(enchantmentLevel * 1.25);
                  if (item.hasEffect(heldItem)
                     && enchantmentLevel == 0.0
                     && mc.getCurrentServerData() != null
                     && mc.getCurrentServerData().serverIP.equals("gommehd.net")) {
                     f3 = this.getGommeCustomSharpnessBonus(heldItem);
                  }
               } else if (item instanceof ItemTool) {
                  ItemTool itemTool = (ItemTool)item;
                  double enchantmentLevel2 = (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, heldItem);
                  f2 += itemTool.getDamageVsEntity();
                  f3 = (float)(enchantmentLevel2 * 1.25);
                  if (item.hasEffect(heldItem)
                     && enchantmentLevel2 == 0.0
                     && mc.getCurrentServerData() != null
                     && mc.getCurrentServerData().serverIP.equals("gommehd.net")) {
                     f3 = this.getGommeCustomSharpnessBonus(heldItem);
                  }
               }
            }

            float f4 = f2 + f3;
            boolean flag = false;
            boolean flag2 = true;
            float f5 = f4;
            DamageSource anvil = DamageSource.anvil;
            float f6 = this.calculatePostDefenseDamage(anvil, f5, nearestPlayer2, flag2, flag);
            f4 = f2 * 1.5F + f3;
            boolean flag3 = false;
            boolean flag4 = true;
            float f7 = f4;
            DamageSource anvil2 = DamageSource.anvil;
            float f8 = this.calculatePostDefenseDamage(anvil2, f7, nearestPlayer2, flag4, flag3);
            f4 = f2 * 0.5F;
            boolean flag5 = false;
            boolean flag6 = true;
            float f9 = f4;
            DamageSource anvil3 = DamageSource.anvil;
            float f10 = this.calculatePostDefenseDamage(anvil3, f9, nearestPlayer2, flag6, flag5);
            if (this.pendingDamageAmount > 0.0) {
               double d4 = (double)f8;
               double d5 = 0.01F;
               double pendingDamageAmount2 = this.pendingDamageAmount;
               double d6 = d4;
               boolean isWithinEpsilon = this.isWithinEpsilon(d6, pendingDamageAmount2, d5);
               double d7 = (double)f6;
               double d8 = 0.01F;
               double pendingDamageAmount3 = this.pendingDamageAmount;
               double d9 = d7;
               boolean isWithinEpsilon2 = this.isWithinEpsilon(d9, pendingDamageAmount3, d8);
               if (!isWithinEpsilon && !isWithinEpsilon2) {
                  String str2 = "Maybe no legit Damage " + NumberFormatUtil.formatDecimal(this.pendingDamageAmount, 2) + " " + NumberFormatUtil.formatDecimal((double)f6, 2);
                  this.sendChatMessage2(str2);
               } else if (isWithinEpsilon) {
                  String str3 = "Crit";
                  this.sendChatMessage2(str3);
               } else {
                  String str4 = "Nocrit";
                  this.sendChatMessage2(str4);
               }
            }
         }

         if (packet instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity)packet).getEntityID() == mc.thePlayer.getEntityId()) {
            this.pendingDamageAmount = -1.0;
         }

         if (packet instanceof S06PacketUpdateHealth) {
            S06PacketUpdateHealth packet3 = (S06PacketUpdateHealth)packet;
            float f11 = this.lastHealthValue - packet3.getHealth();
            if (f11 > 0.0F) {
               if (this.pendingDamageAmount < 0.0) {
                  this.pendingDamageAmount = 0.0;
               }

               System.out.println(f11);
               this.pendingDamageAmount += (double)f11;
            }

            this.lastHealthValue = packet3.getHealth();
         }
      }

      if (this.velocityPacketDebug != null && this.velocityPacketDebug.getValue()) {
         if (packet instanceof S12PacketEntityVelocity && mc.thePlayer != null) {
            S12PacketEntityVelocity packet4 = (S12PacketEntityVelocity)packet;
            if (packet4.getEntityID() == mc.thePlayer.getEntityId()) {
               String str5 = "Velocity "
                  + 1.25E-4 * Math.sqrt((double)(packet4.getMotionX() * packet4.getMotionX() + packet4.getMotionZ() * packet4.getMotionZ()));
               this.sendChatMessage2(str5);
            }
         }

         if (packet instanceof S27PacketExplosion && mc.thePlayer != null) {
            S27PacketExplosion packet5 = (S27PacketExplosion)packet;
            if (Math.abs(packet5.getMotionX()) > 0.0F || Math.abs(packet5.getMotionY()) > 0.0F || Math.abs(packet5.getMotionZ()) > 0.0F) {
               String str6 = "Velocity (S27) " + Math.sqrt((double)(packet5.getMotionX() * packet5.getMotionX() + packet5.getMotionZ() * packet5.getMotionZ()));
               this.sendChatMessage2(str6);
            }
         }
      }

      if (packet instanceof S13PacketDestroyEntities && mc.theWorld != null) {
         S13PacketDestroyEntities packet6 = (S13PacketDestroyEntities)packet;

         for (int n : packet6.getEntityIDs()) {
            if (mc.theWorld.getEntityByID(n) instanceof EntityBoat) {
               this.boatDestroyDelayTimer.reset();
            }
         }
      }

      if (packet instanceof S0BPacketAnimation && mc.theWorld != null) {
         S0BPacketAnimation packet7 = (S0BPacketAnimation)packet;
         if (packet7.getAnimationType() == 1) {
            Entity entityByID = mc.theWorld.getEntityByID(packet7.getEntityID());
            if (entityByID instanceof EntityLivingBase) {
               EntityLivingBase entity = (EntityLivingBase)entityByID;
               entity.hurtAnimationCount++;
            }
         }
      }

      if (packet instanceof S19PacketEntityStatus && mc.theWorld != null) {
         S19PacketEntityStatus packet8 = (S19PacketEntityStatus)packet;
         if (packet8.getOpCode() == 2) {
            Entity entityByID2 = mc.theWorld.getEntityByID(packet8.getEntityId());
            if (entityByID2 instanceof EntityLivingBase) {
               EntityLivingBase entity2 = (EntityLivingBase)entityByID2;
               entity2.hurtStatusCount++;
               entity2.totalHurtEvents++;
               entity2.lastHurtStatusTick = entity2.ticksExisted;
            }
         }
      }

      if (packet instanceof S32PacketConfirmTransaction && mc.theWorld != null) {
         long l = System.currentTimeMillis() - this.lastTransactionTimeMs;
         this.lastTransactionTimeMs = System.currentTimeMillis();
         this.transactionIntervals.add(l);

         while (this.transactionIntervals.size() > 15) {
            this.transactionIntervals.remove(0);
         }
      }

      if (packet instanceof S19PacketEntityStatus && mc.theWorld != null) {
         S19PacketEntityStatus packet9 = (S19PacketEntityStatus)packet;
         if (packet9.getOpCode() == 2) {
            Entity entityByID3 = mc.theWorld.getEntityByID(packet9.getEntityId());
            if (entityByID3 != null && this.lastAttackedEntity != null && entityByID3.getEntityId() == this.lastAttackedEntity.getEntityId() && this.lastAttackedEntity.hurtTime == 0) {
               long l2 = System.currentTimeMillis() - this.lastAttackedEntity.lastDamageAttackTime;
               if (l2 < 400L && !this.transactionIntervals.isEmpty()) {
                  long l3 = MathStatisticsUtil.maxLong(this.transactionIntervals);
               }
            }
         }
      }

      if (packet instanceof S0BPacketAnimation && mc.theWorld != null) {
         S0BPacketAnimation packet10 = (S0BPacketAnimation)packet;
         if (packet10.getAnimationType() == 1) {
            Entity entityByID4 = mc.theWorld.getEntityByID(packet10.getEntityID());
            if (entityByID4 != null && this.lastAttackedEntity != null && entityByID4.getEntityId() == this.lastAttackedEntity.getEntityId() && this.lastAttackedEntity.hurtTime == 0) {
               long l4 = System.currentTimeMillis() - this.lastAttackedEntity.lastDamageAttackTime;
               if (l4 < 400L && !this.transactionIntervals.isEmpty()) {
                  long l5 = MathStatisticsUtil.maxLong(this.transactionIntervals);
               }
            }
         }
      }

      if (this.transaction.getValue() && packet instanceof S32PacketConfirmTransaction) {
         S32PacketConfirmTransaction packet11 = (S32PacketConfirmTransaction)packet;
         this.transactionsThisSecond++;
         String str7 = "Transaction debug; WindowID: "
            + packet11.getWindowId()
            + "; ActionNumber: "
            + packet11.getActionNumber()
            + "; Accepted: "
            + packet11.isAccepted()
            + "; TransPerSec: "
            + this.transactionsPerSecond;
         this.sendChatMessage2(str7);
      }

      Augustus.getInstance().getClass();
      if (packet instanceof S07PacketRespawn) {
         this.expectedDamageHistory.clear();
         this.actualDamageHistory.clear();
         this.reducedDamageViolations = 0;
         if (this.hitCritCounterHud != null && this.hitCritCounterHud.getValue()) {
            if (this.opponentHitCount != 0 || this.ownHitCount != 0 || this.ownCritCount != 0 || this.opponentCritCount != 0) {
               int n2 = this.ownHitCount - this.opponentHitCount;
               int n3 = this.opponentHitCount - this.ownHitCount;
               int n4 = this.ownCritCount - this.opponentCritCount;
               int n5 = this.opponentCritCount - this.ownCritCount;
               String str8 = "MeHits: §7"
                  + this.ownHitCount
                  + "§f "
                  + (n2 != 0 ? (n2 < 0 ? "§c" + n2 : " §a+" + n2) : "")
                  + "§f  Crits: §7"
                  + this.ownCritCount
                  + " §f"
                  + (n4 != 0 ? (n4 < 0 ? "§c" + n4 : " §a+" + n4) : "")
                  + "§f";
               this.outputDebugMessage(str8);
               String str9 = "OpponentHits: §7"
                  + this.opponentHitCount
                  + "§f "
                  + (n3 != 0 ? (n3 < 0 ? "§c" + n3 : " §a+" + n3) : "")
                  + "§f  Crits: §7"
                  + this.opponentCritCount
                  + " §f"
                  + (n5 != 0 ? (n5 < 0 ? "§c" + n5 : " §a+" + n5) : "")
                  + "§f";
               this.outputDebugMessage(str9);
            }

            this.opponentHitCount = 0;
            this.ownHitCount = 0;
            this.ownCritCount = 0;
            this.opponentCritCount = 0;
            this.unusedDamageAccumulatorA = 0.0F;
            this.unusedDamageAccumulatorB = 0.0F;
         }
      }

      if (packet instanceof S0BPacketAnimation) {
         S0BPacketAnimation packet12 = (S0BPacketAnimation)packet;
         if (packet12.getAnimationType() == 4 && mc.thePlayer != null && packet12.getEntityID() == mc.thePlayer.getEntityId()) {
            this.opponentCritCount++;
         }

         if (packet12.getAnimationType() == 4 && this.nearestPlayer != null && this.nearestPlayer.getEntityId() == packet12.getEntityID()) {
            this.ownCritCount++;
         }
      }

      if (packet instanceof S06PacketUpdateHealth && this.reducedDamageCheck != null && this.reducedDamageCheck.getValue()) {
         double health2 = (double)((S06PacketUpdateHealth)packet).getHealth();
         double lastRecordedAbsorption2 = this.lastRecordedAbsorption;
         double d10 = health2;
         this.analyzeIncomingDamage(d10, lastRecordedAbsorption2);
      }

      if (packet instanceof S1CPacketEntityMetadata && this.reducedDamageCheck != null && this.reducedDamageCheck.getValue() && mc.thePlayer != null) {
         S1CPacketEntityMetadata packet13 = (S1CPacketEntityMetadata)packet;
         if (packet13.getEntityId() == mc.thePlayer.getEntityId()) {
            for (DataWatcher.WatchableObject watchableObject : (Iterable<DataWatcher.WatchableObject>)(packet13.getDataManagerEntries())) {
               if (watchableObject.getDataValueId() == 17) {
                  double lastRecordedHealth2 = this.lastRecordedHealth;
                  double d11 = (double)((Float)watchableObject.getObject()).floatValue();
                  double d12 = lastRecordedHealth2;
                  this.analyzeIncomingDamage(d12, d11);
               }
            }
         }
      }

      if (this.packetLoggerMode != null && (this.packetLoggerMode.getSelectedValue().equals("Incoming") || this.packetLoggerMode.getSelectedValue().equals("All"))) {
         String str10 = "Incoming: ";
         this.logPacketToFile(packet, str10);
      }

      if (!(packet instanceof S32PacketConfirmTransaction)
         && !(packet instanceof S14PacketEntity)
         && !(packet instanceof S02PacketChat)
         && !(packet instanceof S19PacketEntityHeadLook)
         && !(packet instanceof S18PacketEntityTeleport)
         && !(packet instanceof S38PacketPlayerListItem)
         && !(packet instanceof S3APacketTabComplete)
         && !(packet instanceof S06PacketUpdateHealth)
         && !(packet instanceof S3EPacketTeams)
         && !(packet instanceof S03PacketTimeUpdate)
         && !(packet instanceof S2FPacketSetSlot)
         && !(packet instanceof S04PacketEntityEquipment)
         && !(packet instanceof S0BPacketAnimation)
         && !(packet instanceof S20PacketEntityProperties)
         && !(packet instanceof S1BPacketEntityAttach)
         && !(packet instanceof S1CPacketEntityMetadata)
         && !(packet instanceof S23PacketBlockChange)
         && !(packet instanceof S08PacketPlayerPosLook)
         && !(packet instanceof S07PacketRespawn)
         && !(packet instanceof S21PacketChunkData)
         && !(packet instanceof S33PacketUpdateSign)
         && !(packet instanceof S00PacketKeepAlive)
         && !(packet instanceof S13PacketDestroyEntities)
         && !(packet instanceof S2BPacketChangeGameState)
         && !(packet instanceof S35PacketUpdateTileEntity)
         && !(packet instanceof S30PacketWindowItems)
         && !(packet instanceof S45PacketTitle)
         && !(packet instanceof S48PacketResourcePackSend)) {
      }

      if (!(packet instanceof S32PacketConfirmTransaction)
         && !(packet instanceof S48PacketResourcePackSend)
         && !(packet instanceof S38PacketPlayerListItem)
         && !(packet instanceof S06PacketUpdateHealth)) {
      }

      if (this.kit1V1Damage.getValue() && packet instanceof S06PacketUpdateHealth && mc.thePlayer != null) {
         S06PacketUpdateHealth packet14 = (S06PacketUpdateHealth)packet;
         float f12 = packet14.getHealth() - mc.thePlayer.getCurrentHealth();
         if ((double)f12 > -2.58 && f12 < 0.0F) {
            String str11 = "HealthDiff: " + NumberFormatUtil.formatDecimal((double)f12, 2);
            this.outputDebugMessage(str11);
         }
      }

      if (packet instanceof S29PacketSoundEffect && this.soundName.getValue()) {
         S29PacketSoundEffect packet15 = (S29PacketSoundEffect)packet;
         String str12 = "SoundName: " + packet15.getSoundName();
         this.outputDebugMessage(str12);
      }

      if (packet instanceof S02PacketChat && this.onlyDebugChat.getValue()) {
         S02PacketChat packet16 = (S02PacketChat)packet;
         if (!packet16.getChatComponent().getUnformattedText().contains("Augustus")) {
            event.cancel();
         }
      }

      if (packet instanceof S19PacketEntityStatus && this.hitBug.getValue() && mc.thePlayer != null) {
         S19PacketEntityStatus packet17 = (S19PacketEntityStatus)packet;
         if (packet17.getEntityId() == mc.thePlayer.getEntityId() && packet17.getOpCode() == 2) {
            long l6 = 450L;
            if (!this.hitBugTimer.reached2(l6)) {
               String str13 = "Hitbug! " + this.hitBugTimer.getTime();
               this.outputDebugMessage(str13);
            }

            this.hitBugTimer.reset();
         }
      }
   }

   private void analyzeIncomingDamage(double d2, double d3) {
      double d4 = this.lastRecordedHealth + this.lastRecordedAbsorption - d2 - d3;
      float f = 1.0F;
      float f2 = 0.0F;
      EntityPlayer nearestPlayer2 = this.nearestPlayer;
      if (nearestPlayer2 != null && d4 > 0.0) {
         ItemStack heldItem = nearestPlayer2.getHeldItem();
         if (heldItem != null) {
            Item item = heldItem.getItem();
            if (item instanceof ItemSword) {
               ItemSword itemSword = (ItemSword)item;
               double enchantmentLevel = (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, heldItem);
               f += itemSword.getAttackDamage();
               f2 = (float)(enchantmentLevel * 1.25);
               if (item.hasEffect(heldItem) && enchantmentLevel == 0.0 && mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.equals("gommehd.net")) {
                  f2 = this.getGommeCustomSharpnessBonus(heldItem);
               }
            } else if (item instanceof ItemTool) {
               ItemTool itemTool = (ItemTool)item;
               double enchantmentLevel2 = (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, heldItem);
               f += itemTool.getDamageVsEntity();
               f2 = (float)(enchantmentLevel2 * 1.25);
               if (item.hasEffect(heldItem) && enchantmentLevel2 == 0.0 && mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.equals("gommehd.net")
                  )
                {
                  f2 = this.getGommeCustomSharpnessBonus(heldItem);
               }
            }
         }

         float f3 = f + f2;
         boolean flag = false;
         boolean flag2 = true;
         float f4 = f3;
         DamageSource anvil = DamageSource.anvil;
         float f5 = this.calculatePostDefenseDamage(anvil, f4, nearestPlayer2, flag2, flag);
         f3 = f * 1.5F + f2;
         boolean flag3 = false;
         boolean flag4 = true;
         float f6 = f3;
         DamageSource anvil2 = DamageSource.anvil;
         float f7 = this.calculatePostDefenseDamage(anvil2, f6, nearestPlayer2, flag4, flag3);
         f3 = f * 0.5F;
         boolean flag5 = false;
         boolean flag6 = true;
         float f8 = f3;
         DamageSource anvil3 = DamageSource.anvil;
         float f9 = this.calculatePostDefenseDamage(anvil3, f8, nearestPlayer2, flag6, flag5);
         f3 = f + f2;
         boolean flag7 = false;
         boolean flag8 = false;
         float f10 = f3;
         DamageSource anvil4 = DamageSource.anvil;
         float f11 = this.calculatePostDefenseDamage(anvil4, f10, nearestPlayer2, flag8, flag7);
         f3 = f * 1.5F + f2;
         boolean flag9 = false;
         boolean flag10 = false;
         float f12 = f3;
         DamageSource anvil5 = DamageSource.anvil;
         float f13 = this.calculatePostDefenseDamage(anvil5, f12, nearestPlayer2, flag10, flag9);
         f3 = f * 0.5F;
         boolean flag11 = false;
         boolean flag12 = false;
         float f14 = f3;
         DamageSource anvil6 = DamageSource.anvil;
         float f15 = this.calculatePostDefenseDamage(anvil6, f14, nearestPlayer2, flag12, flag11);
         if (this.printRawDamageValues != null && this.printRawDamageValues.getValue()) {
            String str = d4 + " " + f11 + " " + f13;
            this.outputDebugMessage(str);
         }

         if (this.hasProtectionArmor()) {
            if (this.reducedDamageAlert != null && this.reducedDamageAlert.getValue()) {
               float f16 = (float)d4;
               float[] floats = new float[]{f11, f13};
               float[] floats2 = floats;
               float f17 = f16;
               float f18 = this.findClosestExpectedDamage(f17, floats2);
               double d5 = (double)f18;
               double d6 = (double)Math.abs(f11 - f5);
               double d7 = d5;
               if (!this.isWithinEpsilon(d4, d7, d6) && d4 < (double)f18) {
                  double d8 = (double)f9;
                  double d9 = 0.01F;
                  double d10 = d8;
                  if (!this.isWithinEpsilon(d4, d10, d9)) {
                     if (this.reducedDamageViolations > 8) {
                        String str2 = "Your enemy makes reduced Damage (unsafe) (single)!";
                        this.outputDebugMessage(str2);
                     }

                     this.reducedDamageViolations += 2;
                  }
               }
            }
         } else {
            double d11 = (double)f5;
            double d12 = 0.01F;
            double d13 = d11;
            if (this.isWithinEpsilon(d4, d13, d12)) {
            }

            double d14 = (double)f5;
            double d15 = 0.01F;
            double d16 = d14;
            if (!this.isWithinEpsilon(d4, d16, d15)) {
               double d17 = (double)f7;
               double d18 = 0.01F;
               double d19 = d17;
               if (!this.isWithinEpsilon(d4, d19, d18)) {
                  double d20 = (double)f9;
                  double d21 = 0.01F;
                  double d22 = d20;
                  if (!this.isWithinEpsilon(d4, d22, d21)) {
                     String str3 = "Your enemy makes reduced Damage (single)! " + d4 + " " + f11 + " " + f7;
                     this.outputDebugMessage(str3);
                  }
               }
            }
         }

         this.reducedDamageViolations--;
         f3 = (float)d4;
         float[] floats3 = new float[]{f11, f13, f15};
         float[] floats4 = floats3;
         float f19 = f3;
         this.expectedDamageHistory.add(this.findClosestExpectedDamage(f19, floats4));
         this.actualDamageHistory.add((float)d4);

         while (this.expectedDamageHistory.size() > 10) {
            this.expectedDamageHistory.remove(0);
         }

         while (this.actualDamageHistory.size() > 10) {
            this.actualDamageHistory.remove(0);
         }

         if (this.expectedDamageHistory.size() >= 10 && this.reducedDamageAlert.getValue()) {
            double d23 = this.expectedDamageHistory.stream().mapToDouble(obj -> ((Float)obj).doubleValue()).average().orElse(0.0);
            double d24 = this.actualDamageHistory.stream().mapToDouble(obj2 -> ((Float)obj2).doubleValue()).average().orElse(0.0);
            double d25 = 0.09;
            if (!this.isWithinEpsilon(d24, d23, d25)) {
               String str4 = "Your enemy makes reduced Damage (unsafe) (avg)!";
               this.outputDebugMessage(str4);
               String str5 = d4 + " " + f11;
               this.outputDebugMessage(str5);
            }
         }
      }

      this.lastRecordedHealth = d2;
      this.lastRecordedAbsorption = d3;
   }

   private double getEffectiveHealth(long l) {
      return (double)(mc.thePlayer.getCurrentHealth() + mc.thePlayer.getAbsorptionAmount());
   }

   private String getClickCountColorCode(int n) {
      if (n == 0) {
         return "§3";
      } else if (n == 1) {
         return "§b";
      } else {
         return n >= 3 ? "§c" : "§9";
      }
   }

   private boolean hasProtectionArmor() {
      ItemStack[] armorInventory = mc.thePlayer.inventory.armorInventory;
      int length = armorInventory.length;

      for (int i = 0; i < length; i++) {
         ItemStack stack = armorInventory[i];
         if (stack != null && stack.getItem() instanceof ItemArmor && EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) > 0) {
            return true;
         }
      }

      return false;
   }

   private float findClosestExpectedDamage(float f, float[] floats) {
      float maxvalue = Float.MAX_VALUE;
      int n = 0;

      for (int i = 0; i < floats.length; i++) {
         float f2 = floats[i];
         float f3 = Math.abs(f2 - f);
         if (f3 < maxvalue) {
            maxvalue = f3;
            n = i;
         }
      }

      return floats[n];
   }

   private float getGommeCustomSharpnessBonus(ItemStack stack) {
      NBTTagList enchantmentTagList = stack.getEnchantmentTagList();
      if (enchantmentTagList != null) {
         for (int i = 0; i < enchantmentTagList.tagCount(); i++) {
            short s = enchantmentTagList.getCompoundTagAt(i).getShort("id");
            short s2 = enchantmentTagList.getCompoundTagAt(i).getShort("lvl");
            if (s == 62) {
               return (float)s2 * 1.25F;
            }
         }
      }

      return 0.0F;
   }

   public float calculatePostDefenseDamage(DamageSource damageSource, float f, EntityPlayer player, boolean flag, boolean flag2) {
      if (!damageSource.isUnblockable() && mc.thePlayer.isBlocking() && f > 0.0F) {
         f = (1.0F + f) * 0.5F;
      }

      f = this.applyArmorReduction(damageSource, f, player);
      f = this.applyPotionAndEnchantReduction(damageSource, f, flag, flag2);
      return Math.max(f - mc.thePlayer.getAbsorptionAmount(), 0.0F);
   }

   public float applyPotionAndEnchantReduction(DamageSource damageSource, float f, boolean flag, boolean flag2) {
      if (damageSource.isDamageAbsolute()) {
         return f;
      } else {
         if (mc.thePlayer.isPotionActive(Potion.resistance) && damageSource != DamageSource.outOfWorld) {
            int n = (mc.thePlayer.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
            int n2 = 25 - n;
            float f2 = f * (float)n2;
            f = f2 / 25.0F;
         }

         if (f <= 0.0F) {
            return 0.0F;
         } else {
            int n3 = 0;
            EnchantmentHelper.enchantmentModifierDamage.source = damageSource;
            if (!flag2) {
               for (int i = 0; i < mc.thePlayer.inventory.armorInventory.length; i++) {
                  ItemStack stack = mc.thePlayer.inventory.armorInventory[i];
                  if (stack != null && stack.getItem() instanceof ItemArmor) {
                     float f3 = 0.0F;
                     if (EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) > 0) {
                        int damageModifier = EnchantmentHelper.enchantmentModifierDamage
                           .getDamageModifier(Enchantment.protection, EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack));
                        f3 = (float)(damageModifier + 1 >> 1) + (float)((damageModifier >> 1) + 1) / (flag ? 1.0F : 2.0F);
                     }

                     n3 = (int)((float)n3 + f3);
                  }
               }
            }

            if (n3 > 20) {
               n3 = 20;
            }

            if (n3 > 0) {
               int n4 = 25 - n3;
               float f4 = f * (float)n4;
               f = f4 / 25.0F;
            }

            return f;
         }
      }
   }

   private float applyArmorReduction(DamageSource damageSource, float f, EntityPlayer player) {
      if (!damageSource.isUnblockable()) {
         float f2 = 25.0F - this.getTotalArmorPoints();
         float f3 = f * f2;
         f = f3 / 25.0F;
      }

      return f;
   }

   public float getTotalArmorPoints() {
      float f = 0.0F;

      for (int i = 0; i < mc.thePlayer.inventory.armorInventory.length; i++) {
         ItemStack stack = mc.thePlayer.inventory.armorInventory[i];
         if (stack != null && stack.getItem() instanceof ItemArmor) {
            ItemArmor item = (ItemArmor)stack.getItem();
            float damageReduceAmount = (float)item.damageReduceAmount;
            f += damageReduceAmount;
         }
      }

      return f;
   }

   private EntityPlayer findNearestPlayer() {
      double d2 = 1000000.0;
      EntityPlayer player = null;
      if (mc.theWorld == null) {
         return null;
      } else {
         for (Entity entity : (Iterable<Entity>)(mc.theWorld.loadedEntityList)) {
            if (entity instanceof EntityPlayer && entity.getEntityId() != mc.thePlayer.getEntityId()) {
               double distanceToEntityBox = (double)mc.thePlayer.getDistanceToEntityBox(entity);
               if (distanceToEntityBox < d2 && distanceToEntityBox < 6.0) {
                  d2 = distanceToEntityBox;
                  player = (EntityPlayer)entity;
               }
            }
         }

         return player;
      }
   }

   private boolean isWithinEpsilon(double d2, double d3, double d4) {
      return Math.abs(d2 - d3) <= d4;
   }

   private void logPacketToFile(Packet packet, String str) {
      if (this.pendingPacketLogs.size() > 0) {
         StringBuilder sb = new StringBuilder();

         for (String str2 : (Iterable<String>)(this.pendingPacketLogs)) {
            if (str2 != null) {
               sb.append(str2);
            }
         }

         this.pendingPacketLogs.clear();

         try {
            File file = new File(ClientPaths.CLIENT_DIR + File.separator + "debug" + File.separator + "PacketLog.txt");
            if (!Files.exists(file.getParentFile().toPath())) {
               try {
                  Files.createDirectories(file.getParentFile().toPath());
               } catch (IOException e) {
               }
            }

            if (!file.exists()) {
               file.createNewFile();
            }

            Files.write(file.toPath(), sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
         } catch (IOException e2) {
         }
      }

      if (!(packet instanceof PacketFlushMarker) && !(packet instanceof S00PacketServerInfo) && !(packet instanceof S2APacketParticles)) {
         StringBuilder sb2 = new StringBuilder(" " + packet.getClass().getSimpleName() + " ");
         Field[] declaredFields = packet.getClass().getDeclaredFields();

         for (Field field : declaredFields) {
            sb2.append(" ");
            sb2.append(field.getName());
            sb2.append(": ");
            field.setAccessible(true);
            Object obj = null;

            try {
               obj = field.get(packet);
            } catch (IllegalAccessException e3) {
            }

            if (obj == null) {
               sb2.append("ERROR");
            } else {
               sb2.append(obj.toString());
            }

            sb2.append(" ");
         }

         try {
            declaredFields = packet.getClass().getSuperclass().getDeclaredFields();

            for (Field field2 : declaredFields) {
               sb2.append(" ");
               sb2.append(field2.getName());
               sb2.append(": ");
               field2.setAccessible(true);
               Object obj2 = null;

               try {
                  obj2 = field2.get(packet);
               } catch (IllegalAccessException e4) {
               }

               if (obj2 == null) {
                  sb2.append("ERROR");
               } else {
                  sb2.append(obj2.toString());
               }

               sb2.append(" ");
            }
         } catch (Exception e5) {
         }

         sb2.append("\r");
         this.pendingPacketLogs.add(str + new SimpleDateFormat().format(new Date()) + sb2.toString());
      }
   }

   public void outputDebugMessage(String str) {
      if (this.serversideChat.getValue()) {
         mc.thePlayer.sendChatMessage(Augustus.getInstance().getName() + ": " + str);
      } else {
         this.sendChatMessage2(str);
      }
   }

   private boolean isDamageAlertVisible() {
      return this.reducedDamageCheck.getValue();
   }

   private boolean isDamagePrintVisible() {
      return this.reducedDamageCheck.getValue();
   }




}
