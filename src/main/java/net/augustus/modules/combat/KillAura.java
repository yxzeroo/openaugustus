package net.augustus.modules.combat;

import com.google.common.base.Predicates;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.modules.render.Line;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.utils.EntityCollidablePredicate;
import net.augustus.events.listeners.LeftClickListener;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.WeaponFilterSettings;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.LookDownDisableSettings;
import net.augustus.utils.ActionRateAccumulator;
import net.augustus.render.RenderPassTask;
import net.augustus.settings.options.KillAuraActiveSlotOption;
import net.augustus.events.listeners.MouseMoveListener;
import net.augustus.utils.rotation.HitboxAngleUtil;
import net.augustus.events.EventMove;
import net.augustus.utils.simulation.TrajectoryPredictionUtil;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.MotionListener;
import net.augustus.events.listeners.ClickGuiListener;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.listeners.AttackSlowdownListener;
import net.augustus.ai.OnnxModelRegistry;
import net.augustus.utils.file.ClientPaths;
import net.augustus.events.EventClick;
import net.augustus.settings.RangeSetting;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.ModeSetting;
import net.augustus.modules.combat.helper.ClickModuleContext;
import net.augustus.settings.ActionSetting;
import net.augustus.modules.combat.killaura.HitVecCandidate;
import net.augustus.utils.combat.ProtectedPlayerCheck;
import net.augustus.events.EventAttackSlowdown;
import net.augustus.settings.options.MoveHelperModeOption;
import net.augustus.utils.rotation.MoveFixMode;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.rotation.RayCastUtil;
import net.augustus.events.listeners.MoveListener;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.utils.combat.EntityTargetFilter;
import net.augustus.events.EventLoadWorld;
import net.augustus.utils.TuplePair;
import net.augustus.utils.rotation.RotationController;
import net.augustus.events.listeners.MoveEntityListener;
import net.augustus.modules.combat.killaura.HysteresisRangeTracker;
import net.augustus.utils.world.BlockUtil;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.viaversion.ViaAugustus;
import net.augustus.utils.math.MathStatisticsUtil;
import net.augustus.modules.combat.killaura.MovingObjectTypeSwitchMap;
import net.augustus.utils.rotation.FullBlockRaytraceFilter;
import net.augustus.modules.modes.clicking.SimpleClickPatternMode;
import net.augustus.modules.modes.clicking.DistributionClickPatternMode;
import net.augustus.modules.modes.clicking.SimpleV2ClickPatternMode;
import net.augustus.modules.modes.clicking.SmartClickPatternMode;
import net.augustus.modules.modes.clicking.DeviationClickPatternMode;
import net.augustus.modules.modes.clicking.RecordedClickPatternMode;
import net.augustus.modules.modes.clicking.AbstractClickPatternMode;
import net.augustus.modules.modes.clicking.AdvancedClickPatternMode;
import net.augustus.modules.modes.clicking.DragClickPatternMode;
import net.augustus.utils.combat.TargetFilterPresets;
import net.augustus.utils.combat.EntityTargetSelector;
import net.augustus.utils.rotation.RayTraceUtil;
import net.augustus.modules.modes.killaura.unoise3.UNoise3RotationEngine;
import net.augustus.events.EventMoveInput;
import net.augustus.events.EventMouseMove;
import net.augustus.render.RenderUtil;
import net.augustus.modules.modes.killaura.unoise3.UNoise3SettingContext;
import net.augustus.utils.TickCounter;
import net.augustus.utils.BoundedHistoryList;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.modules.combat.helper.ClickSpeedMonitor;
import net.augustus.Augustus;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.clickgui.ClickGuiScreen;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.EventMoveEntity;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.ai.AiRotationPredictor;
import net.augustus.events.listeners.RayCastListener;
import net.augustus.settings.TargetSelectionSettings;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.events.EventMotion;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class KillAura
   extends Module
   implements ClickModuleContext,
   LoadWorldListener,
   MoveListener,
   AttackSlowdownListener,
   ClickListener,
   ClickGuiListener,
   TickListener,
   MoveInputListener,
   MotionListener,
   RenderListener,
   RayCastListener,
   PacketListener,
   MouseMoveListener,
   SilentSlotListener,
   MoveEntityListener {
   public final RotationUtil rotationUtil;
   public final DoubleValue range;
   public final RandomizedRangeSettings preAimRange;
   public final StringValue range2;
   public final BooleansSetting rangeMode;
   public final StringValue hazeAdd;
   public final StringValue hazeMax;
   public final BooleansSetting actionToTrackInterval;
   private final StringValue interval;
   private final StringValue rangeAtInterval;
   private final StringValue minReachToIntervalHit;
   private final StringValue hurtTimeToHitWithReach;
   private final RangeSetting baseRange;
   private final StringValue chanceOfExtendedRange;
   public final DoubleValue rotations;
   public final StringValue yawFov;
   public final StringValue pitchFov;
   public final RandomizedRangeSettings yawSpeed;
   public final RandomizedRangeSettings pitchSpeed;
   public final RandomizedRangeSettings yawAcceleration;
   public final RandomizedRangeSettings pitchAcceleration;
   public final BooleanValue betterAcceleration;
   public final DoubleValue fixRotationGroup;
   public final BooleansSetting fixRotationMode;
   public final StringValue fixRotationChance;
   public final RandomizedRangeSettings maxFixRotationReach;
   public final StringValue minFixRotationAngle;
   public final BooleansSetting minDistanceToBestVecCalculation;
   public final StringValue minDistanceToBestVecYaw;
   public final StringValue minDistanceToBestVecPitch;
   public final StringValue minDistanceToBestVec;
   public final BooleansSetting heuristics;
   public final BooleansSetting rotationMode;
   public final StringValue hitBoxPercentageVertical;
   public final StringValue hitBoxPercentageHorizontal;
   public final StringValue hitBoxPercentageVertical1;
   public final StringValue hitBoxPercentageHorizontal1;
   private final DoubleValue randomize;
   public final BooleansSetting randomType;
   public BooleansSetting randomizeMode;
   public final StringValue randomStrength;
   public final BooleanValue reducedRotate;
   public final StringValue reduceRotationPercentage;
   public final BooleanValue smartRotate;
   public final BooleanValue pitchOutliers;
   public final RandomizedRangeSettings pitchOutliersAdd;
   public final RandomizedRangeSettings pitchOutliersDelay;
   public final RangeSetting innerRadius;
   public final StringValue bestHitXMinDist;
   public final StringValue bestHitYMInDist;
   public final StringValue minAbsDist;
   public final StringValue maxXAbsDist;
   public final StringValue maxYAbsDist;
   public final RangeSetting xRelDist;
   public final RangeSetting yRelDist;
   public final StringValue maxRotationAngle;
   public final StringValue additionalMinAimspeed;
   public final StringValue additionalDynamicSpeed;
   public final StringValue additionalAimspeedIfNeeded;
   public final StringValue probabilityOfAimingEvenWhenNotNeeded;
   public final BooleanValue test;
   public final StringValue aimSpeed;
   public final StringValue decelerationfactorPre;
   public final StringValue decelerationfactorPost;
   public final RangeSetting reactionTime;
   public final RangeSetting lookaheadTime;
   public final StringValue attention;
   public final RangeSetting predictionInaccuracy;
   public final StringValue predictionInaccuracySmoothness;
   public final DoubleValue superCorrection;
   public final StringValue supercorrectionProbability;
   public final RandomizedRangeSettings supercorrectionDelay;
   public final BooleanValue additionalAccuracy;
   public final DoubleValue rotationOffset;
   public final RangeSetting rotationYawOffset;
   public final RangeSetting rotationPitchOffset;
   public final StringValue rotationYawOffsetSpeed;
   public final StringValue rotationPitchOffsetSpeed;
   public final StringValue additionalInaccuracy;
   public final RangeSetting orthogonalScale;
   public final StringValue maxOrthogonalMovement;
   public final StringValue maxAimpointAnglediff;
   public final RandomizedRangeSettings bestvecPitchinterpolation;
   public final RangeSetting rotationOnTarget;
   public final BooleanValue noYawAdjustmentIfTargetOutOfReach;
   public final BooleanValue noPitchAdjustmentIfTargetOutOfReach;
   public final BooleanValue applyRecordedMouseJitterMovementIfAvailable;
   public final BooleanValue ultimateTrashcodeBypass;
   public final BooleanValue adaptCurveIfNeeded;
   public final RandomizedRangeSettings noise3AimSpeed;
   public final RandomizedRangeSettings noise3Acceleration;
   public final StringValue noise33BetterAcceleration;
   public final StringValue noise3Scale;
   public final RangeSetting noise3ResponseTarget;
   public final RangeSetting noise3PlayerResponse;
   private final DoubleValue aiSettingsGroup;
   public final BooleansSetting aiModel;
   private final ActionSetting aiRefreshModels;
   public final BooleanValue smartAim;
   public final BooleanValue smartAimEntityCheck;
   public final BooleanValue advancedRots;
   public final BooleanValue advancedRotsNotAtTarget;
   public final BooleanValue timerRangeV2Fix;
   public final BooleanValue interpolation;
   public final BooleanValue lockView;
   public final BooleanValue stopRotationInsideTarget;
   public final DoubleValue throughWallsGroup;
   public final BooleansSetting throughWallsMode;
   public final StringValue throughWallsRange;
   public final BooleanValue aimThroughWalls;
   public final RandomizedRangeSettings aimThroughWallsRange;
   public final BooleanValue noRotation;
   public final DoubleValue clicking;
   public final DoubleValue clickSettings;
   public final ModeSetting clickMode;
   public final StringValue cpsLimiter;
   public final BooleanValue cpsLimiterIntave;
   private final DoubleValue interactions;
   public final BooleansSetting attackMode;
   public final StringValue maxTargets;
   public final BooleanValue optifineHitbox;
   public final BooleansSetting blockMode;
   public final BooleanValue smartBlock;
   public final BooleanValue interactAutoBlock;
   public final BooleanValue onlyRightClickBlock;
   public final BooleanValue onlyWhileSwingInProgress;
   public final BooleanValue onlyWhileHurt;
   public final StringValue unblockAAC;
   public final BooleanValue preHit;
   public final BooleanValue smartPreHit;
   private final RandomizedRangeSettings prediction;
   private final RandomizedRangeSettings loseDelay;
   private final RandomizedRangeSettings endurance;
   private final RandomizedRangeSettings enduranceReduction;
   public final RandomizedRangeSettings preHitRange;
   public final RandomizedRangeSettings preHitFovYaw;
   public final RandomizedRangeSettings preHitFovPitch;
   public final BooleanValue missClick;
   public final StringValue missClickPercentage;
   public final BooleanValue perfectHit;
   public final BooleanValue longClick;
   public final BooleanValue critFix;
   public final BooleanValue critFixPacketCheck;
   public final StringValue critFixChance;
   public final BooleanValue noLoopHit;
   public final BooleanValue fixBeta;
   private final BooleanValue breakBlocks;
   public final BooleanValue doubleClickFirstHit;
   public final BooleanValue reduceCPSIfYouCanNotHitATargett;
   public final BooleanValue noSwing;
   public final BooleanValue slowDown;
   public final BooleanValue coolDown;
   public final DoubleValue movement;
   private final BooleanValue jumpFix;
   public final BooleanValue moveFix;
   public final BooleansSetting silentMoveFix;
   public final BooleanValue silentMoveFixIntave;
   public final DoubleValue targets;
   public final TargetSelectionSettings targetSelection;
   public final StringValue targetDelay;
   public final RandomizedRangeSettings targetLoseDelay;
   public final RandomizedRangeSettings maxDeathTime;
   public final DoubleValue miscellaneous;
   public final BooleanValue closeGuisIfTargetArrives;
   public final BooleanValue inInv;
   public final MultiOptionSetting activeSlots;
   public final WeaponFilterSettings weaponFilter;
   public final LookDownDisableSettings lookDownDisable;
   public final BooleanValue intave13;
   private final RandomizedRangeSettings delayRotationsAfterClosingGuiScreen;
   private final RandomizedRangeSettings delayClicksAfterClosingGuiScreen;
   private final RandomizedRangeSettings delayRotationsAfterRespawn;
   private final RandomizedRangeSettings delayClicksAfterRespawn;
   private final DoubleValue visuals;
   public final BooleanValue dot;
   public final StringValue dotSize;
   public final ColorSetting dotColor;
   public final BlurShadowSettings dotShadowSettings;
   private final TimeHelper targetSwitchTimer;
   private final TimeHelper lastAttackTimer;
   private final TimeHelper clickCycleTimer;
   public double currentRange;
   public EntityLivingBase target;
   private Vec3 targetHitVec;
   private long lastHazeHitTime;
   private long lastMouseMoveTime;
   private EntityLivingBase previousTarget;
   private EntityLivingBase lastValidTarget;
   private int intaveRotationFlags;
   private OnnxModelRegistry aiModelRegistry;
   private String loadedAiModelName;
   public final CopyOnWriteArrayList rotationTrailPoints;
   private int ticksSinceBlockAttack;
   private final TimeHelper lastSwingPacketTimer;
   private final TimeHelper doubleClickFirstHitTimer;
   public final ClickSpeedMonitor clickSpeedMonitor;
   private Rotation currentRotation;
   private int[] lockViewMouseDelta;
   private long lastTickTimestamp;
   private int lastTickDurationMs;
   private boolean firstHitReady;
   private MovingObjectPosition lastObjectMouseOver;
   private boolean holdingLeftClick;
   public boolean itemUseCleared;
   public int ticksSinceBugBlockSwitch;
   public boolean grimRangeActive;
   private List lastTickTargets;
   private String activeAttackMode;
   private boolean grimAttackPending;
   private boolean pendingLongClickRelease;
   private int intervalActionCount;
   private int betaFixHitCount;
   private boolean attackedThisTick;
   private Vec3 previousHitVec;
   private final TimeHelper targetLoseTimer;
   private final TickCounter tickCounter;
   private int longClickBlockDelay;
   private boolean useExtendedDualRange;
   private final ActionRateAccumulator actionRateAccumulator;
   private BoundedHistoryList debugPosHistory;
   private int ticksSinceTeleport;
   private final HysteresisRangeTracker preHitRangeTracker;
   private final HysteresisRangeTracker preAimRangeTracker;
   private final HysteresisRangeTracker aimThroughWallsRangeTracker;
   private final HysteresisRangeTracker preHitFovYawTracker;
   private final HysteresisRangeTracker preHitFovPitchTracker;
   private boolean needsClickPatternReset;
   private int consecutiveHits;
   private int[] appliedMouseDelta;
   private double[] fractionalMouseDelta;
   private boolean critLanded;
   private int preHitEnduranceUsed;
   private boolean preHitEnduranceExhausted;
   private static final long a = 78982037544592L;

   public KillAura() {
      super("KillAura", "AutomatischerTöter", new Color(102, 38, 28, 255), Categorys.COMBAT);
      this.rotationUtil = new RotationUtil();
      this.range = new DoubleValue(this, "Range", true);
      this.preAimRange = (new RandomizedRangeSettings(this, "PreAimRange", 6.0, 6.0, 0.0, 15.0, 1, this.range)).withoutNotInARow();
      this.range2 = new StringValue(this, "Range", 3.0, 3.0, 6.0, 3, this.range);
      this.rangeMode = new BooleansSetting(this, "RangeMode", "Simple", new String[]{"Simple", "HazeRange", "Grim", "IntervalRange", "Dual"}, this.range);
      StringValue stringValue = new StringValue(this, "HazeAdd", 0.5, 0.0, 1.0, 2, this.range);
      SettingVisibilityCondition condition = () -> {
         return this.rangeMode.getSelectedValue().equals("HazeRange");
      };
      this.hazeAdd = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(this, "HazeMax", 4.5, 3.0, 6.0, 2, this.range);
      SettingVisibilityCondition condition2 = () -> this.isHazeMaxVisible();
      this.hazeMax = stringValue2.visibleWhen(condition2);
      BooleansSetting booleansSetting = new BooleansSetting(this, "ActionToTrackInterval", "Attacks", new String[]{"Attacks", "Swings"}, this.range);
      SettingVisibilityCondition condition3 = () -> this.isActionToTrackIntervalVisible();
      this.actionToTrackInterval = (BooleansSetting)booleansSetting.withVisibility(condition3);
      stringValue2 = new StringValue(this, "Interval", 2.0, 1.0, 10.0, 0, this.range);
      SettingVisibilityCondition condition4 = () -> this.isIntervalVisible();
      this.interval = stringValue2.visibleWhen(condition4);
      stringValue2 = new StringValue(this, "RangeAtInterval", 3.0, 3.0, 6.0, 3, this.range);
      SettingVisibilityCondition condition5 = () -> {
         return this.rangeMode.getSelectedValue().equals("IntervalRange");
      };
      this.rangeAtInterval = stringValue2.visibleWhen(condition5);
      stringValue2 = new StringValue(this, "MinReachToIntervalHit", 4.0, 3.0, 6.0, 3, this.range);
      SettingVisibilityCondition condition6 = () -> this.isMinReachToIntervalHitVisible();
      this.minReachToIntervalHit = stringValue2.visibleWhen(condition6);
      stringValue2 = new StringValue(this, "HurtTimeToHitWithReach", 2.0, 0.0, 9.0, 0, this.range);
      SettingVisibilityCondition condition7 = () -> this.isHurtTimeToHitWithReachVisible();
      this.hurtTimeToHitWithReach = stringValue2.visibleWhen(condition7);
      RangeSetting rangeSetting = new RangeSetting(this, "BaseRange", "ExtendedRange", 3.0, 3.7, 3.0, 6.0, 2, this.range);
      SettingVisibilityCondition condition8 = () -> {
         return this.rangeMode.getSelectedValue().equals("Dual");
      };
      this.baseRange = rangeSetting.visibleWhen(condition8);
      stringValue2 = new StringValue(this, "Chance of Extended Range", 50.0, 0.0, 100.0, 0, this.range);
      SettingVisibilityCondition condition9 = () -> {
         return this.rangeMode.getSelectedValue().equals("Dual");
      };
      this.chanceOfExtendedRange = stringValue2.visibleWhen(condition9);
      this.rotations = new DoubleValue(this, "Rotations", true);
      this.yawFov = (StringValue)(new StringValue(this, "YawFov", 180.0, 0.0, 180.0, 0, this.rotations)).markVisualOnly();
      this.pitchFov = (StringValue)(new StringValue(this, "PitchFov", 180.0, 0.0, 180.0, 0, this.rotations)).markVisualOnly();
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "YawSpeed", 30.0, 50.0, 1.0, 180.0, 0, this.rotations)).withoutNotInARow();
      SettingVisibilityCondition condition10 = () -> this.isYawSpeedVisible();
      this.yawSpeed = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition10);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this, "PitchSpeed", 30.0, 50.0, 1.0, 180.0, 0, this.rotations)).withoutNotInARow();
      SettingVisibilityCondition condition11 = () -> {
         return !this.randomizeMode.getSelectedValue().contains("Noise") && !this.randomizeMode.getSelectedValue().equals("Turbo") && !this.randomizeMode.getSelectedValue().equals("AI");
      };
      this.pitchSpeed = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition11);
      RandomizedRangeSettings randomizedRangeSettings3 = (new RandomizedRangeSettings(this, "YawAcceleration", 15.0, 20.0, 1.0, 180.0, 1, this.rotations)).withoutNotInARow();
      SettingVisibilityCondition condition12 = () -> this.isYawAccelerationVisible();
      this.yawAcceleration = (RandomizedRangeSettings)randomizedRangeSettings3.withVisibility(condition12);
      RandomizedRangeSettings randomizedRangeSettings4 = (new RandomizedRangeSettings(this, "PitchAcceleration", 15.0, 20.0, 1.0, 180.0, 1, this.rotations)).withoutNotInARow();
      SettingVisibilityCondition condition13 = () -> this.isPitchAccelerationVisible();
      this.pitchAcceleration = (RandomizedRangeSettings)randomizedRangeSettings4.withVisibility(condition13);
      BooleanValue booleanValue = new BooleanValue(this, "BetterAcceleration", false, this.rotations);
      SettingVisibilityCondition condition14 = () -> {
         return !this.isInterpolationEnabled()
            && !this.randomizeMode.getSelectedValue().contains("Noise")
            && !this.randomizeMode.getSelectedValue().equals("Turbo")
            && !this.randomizeMode.getSelectedValue().equals("AI");
      };
      this.betterAcceleration = (BooleanValue)booleanValue.withVisibility(condition14);
      DoubleValue doubleValue = new DoubleValue(this, "FixRotationGroup", true, this.rotations);
      String str = "FixRotations";
      this.fixRotationGroup = (DoubleValue)doubleValue.withDisplayName(str);
      this.fixRotationMode = new BooleansSetting(this, "FixRotationMode", "None", new String[]{"None", "OnlyWhenNeed", "Always"}, this.fixRotationGroup);
      StringValue stringValue3 = new StringValue(this, "FixRotationChance", 100.0, 1.0, 100.0, 0, this.fixRotationGroup);
      SettingVisibilityCondition condition15 = () -> this.isFixRotationChanceVisible();
      this.fixRotationChance = stringValue3.visibleWhen(condition15);
      RandomizedRangeSettings randomizedRangeSettings5 = (new RandomizedRangeSettings(this, "MaxFixRotationReach", 2.96, 2.98, 2.5, 3.0, 3, this.fixRotationGroup)).withoutNotInARow();
      SettingVisibilityCondition condition16 = () -> this.isMaxFixRotationReachVisible();
      this.maxFixRotationReach = (RandomizedRangeSettings)randomizedRangeSettings5.withVisibility(condition16);
      StringValue stringValue4 = new StringValue(this, "MinFixRotationAngle", 90.0, 0.0, 180.0, 0, this.fixRotationGroup);
      SettingVisibilityCondition condition17 = () -> this.isMinFixRotationAngleVisible();
      this.minFixRotationAngle = stringValue4.visibleWhen(condition17);
      BooleansSetting booleansSetting2 = new BooleansSetting(
         this, "MinDistanceToBestVecCalculation", "None", new String[]{"None", "Separate", "Combined"}, this.fixRotationGroup
      );
      SettingVisibilityCondition condition18 = () -> this.isMinDistanceCalculationVisible();
      this.minDistanceToBestVecCalculation = (BooleansSetting)booleansSetting2.withVisibility(condition18);
      StringValue stringValue5 = new StringValue(this, "MinDistanceToBestVecYaw", 2.5, 0.0, 10.0, 2, this.fixRotationGroup);
      SettingVisibilityCondition condition19 = () -> this.isMinDistanceYawVisible();
      this.minDistanceToBestVecYaw = stringValue5.visibleWhen(condition19);
      StringValue stringValue6 = new StringValue(this, "MinDistanceToBestVecPitch", 2.5, 0.0, 10.0, 2, this.fixRotationGroup);
      SettingVisibilityCondition condition20 = () -> this.isMinDistancePitchVisible();
      this.minDistanceToBestVecPitch = stringValue6.visibleWhen(condition20);
      StringValue stringValue7 = new StringValue(this, "MinDistanceToBestVec", 2.5, 0.0, 10.0, 2, this.fixRotationGroup);
      SettingVisibilityCondition condition21 = () -> this.isMinDistanceCombinedVisible();
      this.minDistanceToBestVec = stringValue7.visibleWhen(condition21);
      this.heuristics = new BooleansSetting(this, "Heuristics", "None", new String[]{"None", "Pattern1", "IntaveTest"}, this.rotations);
      this.rotationMode = new BooleansSetting(this, "RotationMode", "BestHitVec", new String[]{"Head", "BestHitVec", "NearestHitVec"}, this.rotations);
      StringValue stringValue8 = new StringValue(this, "HitBoxPercentageVertical", 100.0, 1.0, 100.0, 0, this.rotations);
      SettingVisibilityCondition condition22 = () -> this.isBestHitVecVerticalVisible();
      this.hitBoxPercentageVertical = stringValue8.visibleWhen(condition22);
      StringValue stringValue9 = new StringValue(this, "HitBoxPercentageHorizontal", 100.0, 1.0, 100.0, 0, this.rotations);
      SettingVisibilityCondition condition23 = () -> {
         return this.rotationMode.getSelectedValue().equalsIgnoreCase("BestHitVec");
      };
      this.hitBoxPercentageHorizontal = stringValue9.visibleWhen(condition23);
      StringValue stringValue10 = new StringValue(this, "HitBoxPercentageVertical1", 100.0, 1.0, 100.0, 0, this.rotations);
      String str2 = "HitBoxPercentageVertical";
      StringValue stringValue11 = stringValue10.named(str2);
      SettingVisibilityCondition condition24 = () -> this.isNearestHitVecVerticalVisible();
      this.hitBoxPercentageVertical1 = stringValue11.visibleWhen(condition24);
      StringValue stringValue12 = new StringValue(this, "HitBoxPercentageHorizontal1", 100.0, 1.0, 100.0, 0, this.rotations);
      String str3 = "HitBoxPercentageHorizontal";
      StringValue stringValue13 = stringValue12.named(str3);
      SettingVisibilityCondition condition25 = () -> this.isNearestHitVecHorizontalVisible();
      this.hitBoxPercentageHorizontal1 = stringValue13.visibleWhen(condition25);
      this.randomize = new DoubleValue(this, "Randomize", true, this.rotations);
      this.randomType = new BooleansSetting(this, "RandomType", "SecureRandom", new String[]{"Random", "SecureRandom", "Gaussian", "Intave"}, this.randomize);
      Augustus.getInstance().getClass();
      String[] strings = !Augustus.getInstance().getUsername().equals("7yns")
            && !Augustus.getInstance().getUsername().equals("xaelo")
            && !Augustus.getInstance().getUsername().equals("goatqjudoo")
         ? new String[]{
            "None", "Basic", "Doubled", "OnlyRotation", "Hybrid", "PolarTest", "Circle", "Advanced", "Noise", "Turbo", "Noise2", "Noise3", "LegitTest"
         }
         : new String[]{
            "None", "Basic", "Doubled", "OnlyRotation", "Hybrid", "PolarTest", "Circle", "Advanced", "Noise", "Turbo", "Noise2", "Noise3", "LegitTest", "AI"
         };
      DoubleValue randomize2 = this.randomize;
      String[] strings2 = strings;
      String str4 = "OnlyRotation";
      String str5 = "Randomize";
      this.randomizeMode = new BooleansSetting(this, str5, str4, strings2, randomize2);
      StringValue stringValue14 = new StringValue(this, "RandomStrength", 1.25, 0.01, 5.0, 2, this.randomize);
      SettingVisibilityCondition condition26 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Basic")
            || this.randomizeMode.getSelectedValue().equals("Doubled")
            || this.randomizeMode.getSelectedValue().equals("OnlyRotation")
            || this.randomizeMode.getSelectedValue().equals("Hybrid");
      };
      this.randomStrength = stringValue14.visibleWhen(condition26);
      BooleanValue booleanValue2 = new BooleanValue(this, "ReducedRotate", false, this.randomize);
      SettingVisibilityCondition condition27 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Advanced");
      };
      this.reducedRotate = (BooleanValue)booleanValue2.withVisibility(condition27);
      StringValue stringValue15 = new StringValue(this, "ReduceRotationPercentage", 100.0, 0.0, 100.0, 0, this.randomize);
      SettingVisibilityCondition condition28 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Advanced") && this.reducedRotate.getValue();
      };
      this.reduceRotationPercentage = stringValue15.visibleWhen(condition28);
      BooleanValue booleanValue3 = new BooleanValue(this, "SmartRotate", false, this.randomize);
      SettingVisibilityCondition condition29 = () -> this.isSmartRotateVisible();
      this.smartRotate = (BooleanValue)booleanValue3.withVisibility(condition29);
      BooleanValue booleanValue4 = new BooleanValue(this, "PitchOutliers", false, this.randomize);
      SettingVisibilityCondition condition30 = () -> this.isPitchOutliersVisible();
      this.pitchOutliers = (BooleanValue)booleanValue4.withVisibility(condition30);
      RandomizedRangeSettings randomizedRangeSettings6 = new RandomizedRangeSettings(this, "pitchOutliersAdd", 4.0, 10.0, 0.0, 20.0, 2, this.randomize);
      SettingVisibilityCondition condition31 = () -> this.isPitchOutliersAddVisible();
      this.pitchOutliersAdd = (RandomizedRangeSettings)randomizedRangeSettings6.withVisibility(condition31);
      RandomizedRangeSettings randomizedRangeSettings7 = (new RandomizedRangeSettings(this, "PitchOutliersDelay", 15.0, 20.0, 1.0, 40.0, 0, this.randomize)).withoutNotInARow();
      SettingVisibilityCondition condition32 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Advanced") && this.pitchOutliers.getValue();
      };
      this.pitchOutliersDelay = (RandomizedRangeSettings)randomizedRangeSettings7.withVisibility(condition32);
      RangeSetting rangeSetting2 = new RangeSetting(this, "InnerRadius", "Radius", 4.0, 9.0, 0.0, 20.0, 2, this.randomize);
      SettingVisibilityCondition condition33 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Advanced");
      };
      this.innerRadius = rangeSetting2.visibleWhen(condition33);
      StringValue stringValue16 = new StringValue(this, "BestHitXMinDist", 2.0, 0.0, 10.0, 2, this.randomize);
      SettingVisibilityCondition condition34 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Advanced");
      };
      this.bestHitXMinDist = stringValue16.visibleWhen(condition34);
      StringValue stringValue17 = new StringValue(this, "BestHitYMInDist", 2.0, 0.0, 10.0, 2, this.randomize);
      SettingVisibilityCondition condition35 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Advanced");
      };
      this.bestHitYMInDist = stringValue17.visibleWhen(condition35);
      StringValue stringValue18 = new StringValue(this, "MinAbsDist", 0.5, 0.0, 10.0, 2, this.randomize);
      SettingVisibilityCondition condition36 = () -> this.isMinAbsDistVisible();
      this.minAbsDist = stringValue18.visibleWhen(condition36);
      StringValue stringValue19 = new StringValue(this, "MaxXAbsDist", 5.0, 0.0, 20.0, 2, this.randomize);
      SettingVisibilityCondition condition37 = () -> this.isMaxXAbsDistVisible();
      this.maxXAbsDist = stringValue19.visibleWhen(condition37);
      StringValue stringValue20 = new StringValue(this, "MaxYAbsDist", 20.0, 0.0, 20.0, 2, this.randomize);
      SettingVisibilityCondition condition38 = () -> this.isMaxYAbsDistVisible();
      this.maxYAbsDist = stringValue20.visibleWhen(condition38);
      RangeSetting rangeSetting3 = new RangeSetting(this, "XRelDist", 0.5, 10.0, 0.0, 20.0, 2, this.randomize);
      SettingVisibilityCondition condition39 = () -> this.isXRelDistVisible();
      this.xRelDist = rangeSetting3.visibleWhen(condition39);
      RangeSetting rangeSetting4 = new RangeSetting(this, "YRelDist", 0.5, 10.0, 0.0, 20.0, 2, this.randomize);
      SettingVisibilityCondition condition40 = () -> this.isYRelDistVisible();
      this.yRelDist = rangeSetting4.visibleWhen(condition40);
      StringValue stringValue21 = new StringValue(this, "MaxRotationAngle", 90.0, 0.0, 180.0, 0, this.randomize);
      SettingVisibilityCondition condition41 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Advanced");
      };
      this.maxRotationAngle = stringValue21.visibleWhen(condition41);
      StringValue stringValue22 = new StringValue(this, "Additional min Aimspeed", 100.0, 0.0, 100.0, 0, this.randomize);
      SettingVisibilityCondition condition42 = () -> this.isAdditionalMinAimSpeedVisible();
      this.additionalMinAimspeed = stringValue22.visibleWhen(condition42);
      StringValue stringValue23 = new StringValue(this, "Additional dynamic Speed", 100.0, 0.0, 100.0, 0, this.randomize);
      SettingVisibilityCondition condition43 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Noise");
      };
      this.additionalDynamicSpeed = stringValue23.visibleWhen(condition43);
      StringValue stringValue24 = new StringValue(this, "Additional Aimspeed if needed", 100.0, 0.0, 100.0, 0, this.randomize);
      SettingVisibilityCondition condition44 = () -> this.isAdditionalAimSpeedIfNeededVisible();
      this.additionalAimspeedIfNeeded = stringValue24.visibleWhen(condition44);
      StringValue stringValue25 = new StringValue(this, "Probability of aiming even when not needed", 100.0, 0.0, 100.0, 0, this.randomize);
      SettingVisibilityCondition condition45 = () -> this.isAimProbabilityWhenNotNeededVisible();
      this.probabilityOfAimingEvenWhenNotNeeded = stringValue25.visibleWhen(condition45);
      BooleanValue booleanValue5 = new BooleanValue(this, "Test", false, this.randomize);
      SettingVisibilityCondition condition46 = () -> this.isNoiseTestVisible();
      this.test = (BooleanValue)booleanValue5.withVisibility(condition46);
      StringValue stringValue26 = new StringValue(this, "AimSpeed", 75.0, 0.0, 100.0, 0, this.randomize);
      SettingVisibilityCondition condition47 = () -> this.isNoise2AimSpeedVisible();
      this.aimSpeed = stringValue26.visibleWhen(condition47);
      StringValue stringValue27 = new StringValue(this, "Decelerationfactor Pre", 12.5, 0.0, 100.0, 1, this.randomize);
      SettingVisibilityCondition condition48 = () -> this.isDecelerationPreVisible();
      this.decelerationfactorPre = stringValue27.visibleWhen(condition48);
      StringValue stringValue28 = new StringValue(this, "Decelerationfactor Post", 0.0, 0.0, 100.0, 1, this.randomize);
      SettingVisibilityCondition condition49 = () -> this.isDecelerationPostVisible();
      this.decelerationfactorPost = stringValue28.visibleWhen(condition49);
      RangeSetting rangeSetting5 = new RangeSetting(this, "Reaction Time", 2.0, 4.0, 0.0, 6.0, 0, this.randomize);
      String str6 = " Reaction Time";
      RangeSetting rangeSetting6 = rangeSetting5.named(str6);
      SettingVisibilityCondition condition50 = () -> this.isReactionTimeVisible();
      this.reactionTime = rangeSetting6.visibleWhen(condition50);
      RangeSetting rangeSetting7 = new RangeSetting(this, "Lookahead Time", 2.0, 3.0, 0.0, 10.0, 0, this.randomize);
      String str7 = " Lookahead Time";
      RangeSetting rangeSetting8 = rangeSetting7.named(str7);
      SettingVisibilityCondition condition51 = () -> this.isLookaheadTimeVisible();
      this.lookaheadTime = rangeSetting8.visibleWhen(condition51);
      StringValue stringValue29 = new StringValue(this, "Attention", 3.0, 2.0, 4.0, 0, this.randomize);
      SettingVisibilityCondition condition52 = () -> this.isAttentionVisible();
      this.attention = stringValue29.visibleWhen(condition52);
      RangeSetting rangeSetting9 = new RangeSetting(this, "Prediction Inaccuracy", 0.0, 0.0, -100.0, 100.0, 0, this.randomize);
      String str8 = " Prediction Inaccuracy";
      RangeSetting rangeSetting10 = rangeSetting9.named(str8);
      SettingVisibilityCondition condition53 = () -> this.isPredictionInaccuracyVisible();
      this.predictionInaccuracy = rangeSetting10.visibleWhen(condition53);
      StringValue stringValue30 = new StringValue(this, "Prediction Inaccuracy Smoothness", 0.0, 0.0, 100.0, 0, this.randomize);
      SettingVisibilityCondition condition54 = () -> this.isPredictionInaccuracySmoothnessVisible();
      this.predictionInaccuracySmoothness = stringValue30.visibleWhen(condition54);
      DoubleValue doubleValue2 = new DoubleValue(this, "SuperCorrection", this.randomize);
      SettingVisibilityCondition condition55 = () -> this.isSuperCorrectionGroupVisible();
      this.superCorrection = doubleValue2.visibleWhen(condition55);
      StringValue stringValue31 = new StringValue(this, "SuperCorrection Probability", 100.0, 0.0, 100.0, 0, this.superCorrection);
      SettingVisibilityCondition condition56 = () -> this.isSuperCorrectionProbabilityVisible();
      this.supercorrectionProbability = stringValue31.visibleWhen(condition56);
      RandomizedRangeSettings randomizedRangeSettings8 = new RandomizedRangeSettings(this, "SuperCorrection Delay", 0.0, 0.0, 0.0, 5.0, 0, this.superCorrection);
      String str9 = " SuperCorrection Delay";
      RandomizedRangeSettings randomizedRangeSettings9 = (RandomizedRangeSettings)randomizedRangeSettings8.withDisplayNamePrefix(str9);
      SettingVisibilityCondition condition57 = () -> this.isSuperCorrectionDelayVisible();
      this.supercorrectionDelay = ((RandomizedRangeSettings)randomizedRangeSettings9.withVisibility(condition57)).withoutNotInARow();
      BooleanValue booleanValue6 = new BooleanValue(this, "Additional Accuracy", false, this.superCorrection);
      SettingVisibilityCondition condition58 = () -> {
         if (this.randomizeMode.getSelectedValue().equals("Noise2")) {
            boolean flag = true;
            if (this.supercorrectionProbability.getValue(flag) > 0.0) {
               return true;
            }
         }

         return false;
      };
      this.additionalAccuracy = (BooleanValue)booleanValue6.withVisibility(condition58);
      DoubleValue doubleValue3 = new DoubleValue(this, "Rotation Offset", this.randomize);
      SettingVisibilityCondition condition59 = () -> this.isRotationOffsetGroupVisible();
      this.rotationOffset = doubleValue3.visibleWhen(condition59);
      RangeSetting rangeSetting11 = new RangeSetting(this, "Rotation Yaw Offset", 0.0, 0.0, -30.0, 30.0, 1, this.rotationOffset);
      String str10 = " Rotation Yaw Offset";
      RangeSetting rangeSetting12 = rangeSetting11.named(str10);
      SettingVisibilityCondition condition60 = () -> this.isRotationYawOffsetVisible();
      this.rotationYawOffset = rangeSetting12.visibleWhen(condition60);
      RangeSetting rangeSetting13 = new RangeSetting(this, "Rotation Pitch Offset", 0.0, 0.0, -30.0, 30.0, 1, this.rotationOffset);
      String str11 = " Rotation Pitch Offset";
      RangeSetting rangeSetting14 = rangeSetting13.named(str11);
      SettingVisibilityCondition condition61 = () -> this.isRotationPitchOffsetVisible();
      this.rotationPitchOffset = rangeSetting14.visibleWhen(condition61);
      StringValue stringValue32 = new StringValue(this, "Rotation Yaw Offset Speed", 0.0, 0.0, 100.0, 1, this.rotationOffset);
      SettingVisibilityCondition condition62 = () -> this.isRotationYawOffsetSpeedVisible();
      this.rotationYawOffsetSpeed = stringValue32.visibleWhen(condition62);
      StringValue stringValue33 = new StringValue(this, "Rotation Pitch Offset Speed", 0.0, 0.0, 100.0, 1, this.rotationOffset);
      SettingVisibilityCondition condition63 = () -> this.isRotationPitchOffsetSpeedVisible();
      this.rotationPitchOffsetSpeed = stringValue33.visibleWhen(condition63);
      StringValue stringValue34 = new StringValue(this, "Additional inaccuracy", 0.0, 0.0, 100.0, 0, this.randomize);
      SettingVisibilityCondition condition64 = () -> this.isAdditionalInaccuracyVisible();
      this.additionalInaccuracy = stringValue34.visibleWhen(condition64);
      RangeSetting rangeSetting15 = new RangeSetting(this, "Orthogonal Scale", 0.0, 30.0, 0.0, 100.0, 0, this.randomize);
      String str12 = " Orthogonal Scale";
      RangeSetting rangeSetting16 = rangeSetting15.named(str12);
      SettingVisibilityCondition condition65 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Noise2");
      };
      this.orthogonalScale = rangeSetting16.visibleWhen(condition65);
      StringValue stringValue35 = new StringValue(this, "Max Orthogonal Movement", 10.0, 0.0, 90.0, 1, this.randomize);
      SettingVisibilityCondition condition66 = () -> this.isMaxOrthogonalMovementVisible();
      this.maxOrthogonalMovement = stringValue35.visibleWhen(condition66);
      StringValue stringValue36 = new StringValue(this, "Max Aimpoint Anglediff", 60.0, 1.0, 180.0, 0, this.randomize);
      SettingVisibilityCondition condition67 = () -> this.isMaxAimpointAngleDiffVisible();
      this.maxAimpointAnglediff = stringValue36.visibleWhen(condition67);
      RandomizedRangeSettings randomizedRangeSettings10 = (new RandomizedRangeSettings(this, "Bestvec Pitchinterpolation", 0.0, 0.0, 0.0, 100.0, 0, this.randomize)).withoutNotInARow();
      SettingVisibilityCondition condition68 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Noise2");
      };
      this.bestvecPitchinterpolation = (RandomizedRangeSettings)randomizedRangeSettings10.withVisibility(condition68);
      RangeSetting rangeSetting17 = new RangeSetting(this, "Rotation on Target", 100.0, 100.0, 0.0, 100.0, 0, this.randomize);
      String str13 = " Rotation on Target";
      RangeSetting rangeSetting18 = rangeSetting17.named(str13);
      SettingVisibilityCondition condition69 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Noise2");
      };
      this.rotationOnTarget = rangeSetting18.visibleWhen(condition69);
      BooleanValue booleanValue7 = new BooleanValue(this, "No yaw adjustment if target out of reach", true, this.randomize);
      SettingVisibilityCondition condition70 = () -> this.isNoYawOutOfReachVisible();
      this.noYawAdjustmentIfTargetOutOfReach = (BooleanValue)booleanValue7.withVisibility(condition70);
      BooleanValue booleanValue8 = new BooleanValue(this, "No pitch adjustment if target out of reach", true, this.randomize);
      SettingVisibilityCondition condition71 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Noise2");
      };
      this.noPitchAdjustmentIfTargetOutOfReach = (BooleanValue)booleanValue8.withVisibility(condition71);
      BooleanValue booleanValue9 = new BooleanValue(this, "Apply recorded mouse jitter movement if available", true, this.randomize);
      SettingVisibilityCondition condition72 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Noise2");
      };
      this.applyRecordedMouseJitterMovementIfAvailable = (BooleanValue)booleanValue9.withVisibility(condition72);
      BooleanValue booleanValue10 = new BooleanValue(this, "Ultimate Trashcode Bypass", true, this.randomize);
      SettingVisibilityCondition condition73 = () -> this.isTrashcodeBypassVisible();
      this.ultimateTrashcodeBypass = (BooleanValue)booleanValue10.withVisibility(condition73);
      BooleanValue booleanValue11 = new BooleanValue(this, "Adapt Curve if needed", false, this.randomize);
      SettingVisibilityCondition condition74 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Noise2");
      };
      this.adaptCurveIfNeeded = (BooleanValue)booleanValue11.withVisibility(condition74);
      RandomizedRangeSettings randomizedRangeSettings11 = new RandomizedRangeSettings(this, "Noise3AimSpeed", 20.0, 30.0, 0.0, 180.0, 0, this.randomize);
      String str14 = "AimSpeed";
      RandomizedRangeSettings randomizedRangeSettings12 = ((RandomizedRangeSettings)randomizedRangeSettings11.withDisplayNamePrefix(str14)).withoutNotInARow();
      SettingVisibilityCondition condition75 = () -> this.isTurboAimSpeedVisible();
      this.noise3AimSpeed = (RandomizedRangeSettings)randomizedRangeSettings12.withVisibility(condition75);
      RandomizedRangeSettings randomizedRangeSettings13 = new RandomizedRangeSettings(this, "Noise3Acceleration", 2.0, 4.0, 2.0, 20.0, 2, this.randomize);
      String str15 = "Acceleration";
      RandomizedRangeSettings randomizedRangeSettings14 = ((RandomizedRangeSettings)randomizedRangeSettings13.withDisplayNamePrefix(str15)).withoutNotInARow();
      SettingVisibilityCondition condition76 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Turbo");
      };
      this.noise3Acceleration = (RandomizedRangeSettings)randomizedRangeSettings14.withVisibility(condition76);
      StringValue stringValue37 = new StringValue(this, "Noise33BetterAcceleration", 0.0, 0.0, 1.0, 2, this.randomize);
      String str16 = "BetterAcceleration";
      StringValue stringValue38 = stringValue37.named(str16);
      SettingVisibilityCondition condition77 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Turbo");
      };
      this.noise33BetterAcceleration = stringValue38.visibleWhen(condition77);
      StringValue stringValue39 = new StringValue(this, "Noise3Scale", 1.5, 1.0, 7.0, 2, this.randomize);
      String str17 = "Scale";
      StringValue stringValue40 = stringValue39.named(str17);
      SettingVisibilityCondition condition78 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Turbo");
      };
      this.noise3Scale = stringValue40.visibleWhen(condition78);
      RangeSetting rangeSetting19 = new RangeSetting(this, "Noise3ResponseTarget", 3.0, 5.0, 1.0, 10.0, 0, this.randomize);
      String str18 = "TargetResponse";
      RangeSetting rangeSetting20 = rangeSetting19.named(str18);
      SettingVisibilityCondition condition79 = () -> {
         return this.randomizeMode.getSelectedValue().equals("Turbo");
      };
      this.noise3ResponseTarget = rangeSetting20.visibleWhen(condition79);
      RangeSetting rangeSetting21 = new RangeSetting(this, "Noise3PlayerResponse", 3.0, 5.0, 1.0, 10.0, 0, this.randomize);
      String str19 = "PlayerResponse";
      RangeSetting rangeSetting22 = rangeSetting21.named(str19);
      SettingVisibilityCondition condition80 = () -> this.isTurboPlayerResponseVisible();
      this.noise3PlayerResponse = rangeSetting22.visibleWhen(condition80);
      DoubleValue doubleValue4 = new DoubleValue(this, "AISettingsGroup", this.randomize);
      String str20 = "AI Settings";
      DoubleValue doubleValue5 = (DoubleValue)doubleValue4.withDisplayName(str20);
      SettingVisibilityCondition condition81 = () -> this.isAiSettingsGroupVisible();
      this.aiSettingsGroup = doubleValue5.visibleWhen(condition81);
      BooleansSetting booleansSetting3 = new BooleansSetting(this, "AIModel", "None", new String[]{"None"}, this.aiSettingsGroup);
      String str21 = "Model";
      this.aiModel = (BooleansSetting)booleansSetting3.withDisplayName(str21);
      ActionSetting actionSetting = new ActionSetting(this, "AIRefreshModels", () -> this.refreshAiModels(), this.aiSettingsGroup);
      String str22 = "Refresh Models";
      this.aiRefreshModels = (ActionSetting)actionSetting.withDisplayName(str22);
      this.smartAim = new BooleanValue(this, "SmartAim", true, this.rotations);
      BooleanValue booleanValue12 = new BooleanValue(this, "SmartAimEntityCheck", true, this.rotations);
      BooleanValue smartAim2 = this.smartAim;
      this.smartAim.getClass();
      BooleanValue booleanValue13 = smartAim2;
      SettingVisibilityCondition condition82 = () -> booleanValue13.getValue();
      this.smartAimEntityCheck = (BooleanValue)booleanValue12.withVisibility(condition82);
      this.advancedRots = new BooleanValue(this, "AdvancedRots", true, this.rotations);
      BooleanValue booleanValue14 = new BooleanValue(this, "AdvancedRotsNotAtTarget", false, this.rotations);
      BooleanValue advancedRots2 = this.advancedRots;
      this.advancedRots.getClass();
      BooleanValue booleanValue15 = advancedRots2;
      SettingVisibilityCondition condition83 = () -> booleanValue15.getValue();
      this.advancedRotsNotAtTarget = (BooleanValue)booleanValue14.withVisibility(condition83);
      this.timerRangeV2Fix = new BooleanValue(this, "TimerRangeV2Fix", false, this.rotations);
      this.interpolation = new BooleanValue(this, "Interpolation", false, this.rotations);
      this.lockView = new BooleanValue(this, "LockView", false, this.rotations);
      this.stopRotationInsideTarget = new BooleanValue(this, "StopRotationInsideTarget", false, this.rotations);
      DoubleValue doubleValue6 = new DoubleValue(this, "ThroughWallsGroup", true, this.rotations);
      String str23 = "ThroughWalls";
      this.throughWallsGroup = (DoubleValue)((DoubleValue)doubleValue6.withDisplayName(str23)).markVisualOnly();
      this.throughWallsMode = (BooleansSetting)(new BooleansSetting(
            this, "ThroughWallsMode", "None", new String[]{"None", "NoneFullBlocks", "AllBlocks"}, this.throughWallsGroup
         ))
         .markVisualOnly();
      StringValue stringValue41 = new StringValue(this, "ThroughWallsRange", 3.0, 0.0, 3.0, 3, this.throughWallsGroup);
      SettingVisibilityCondition condition84 = () -> this.isThroughWallsRangeVisible();
      this.throughWallsRange = (StringValue)stringValue41.visibleWhen(condition84).markVisualOnly();
      BooleanValue booleanValue16 = new BooleanValue(this, "AimThroughWalls", false, this.throughWallsGroup);
      SettingVisibilityCondition condition85 = () -> {
         return !this.throughWallsMode.getSelectedValue().equals("AllBlocks");
      };
      this.aimThroughWalls = (BooleanValue)((BooleanValue)booleanValue16.withVisibility(condition85)).markVisualOnly();
      RandomizedRangeSettings randomizedRangeSettings15 = (new RandomizedRangeSettings(this, "AimThroughWallsRange", 6.0, 6.0, 0.0, 6.0, 3, this.throughWallsGroup)).withoutNotInARow();
      SettingVisibilityCondition condition86 = () -> this.isAimThroughWallsRangeVisible();
      this.aimThroughWallsRange = (RandomizedRangeSettings)((RandomizedRangeSettings)randomizedRangeSettings15.withVisibility(condition86)).markAllVisualOnly();
      this.noRotation = new BooleanValue(this, "NoRotation", false, this.rotations);
      this.clicking = new DoubleValue(this, "Clicking", true);
      this.clickSettings = new DoubleValue(this, "ClickSettings", true, this.clicking);
      this.clickMode = new ModeSetting(
         this,
         "ClickMode",
         "Advanced",
         this.clickSettings,
         new BiFunction[]{
            (obj, obj2) -> new DeviationClickPatternMode((Module)obj, (DoubleValue)obj2),
            (obj3, obj4) -> new SimpleClickPatternMode((Module)obj3, (DoubleValue)obj4),
            (obj5, obj6) -> new SimpleV2ClickPatternMode((Module)obj5, (DoubleValue)obj6),
            (obj7, obj8) -> new AdvancedClickPatternMode((Module)obj7, (DoubleValue)obj8),
            (obj9, obj10) -> new RecordedClickPatternMode((Module)obj9, (DoubleValue)obj10),
            (obj11, obj12) -> new SmartClickPatternMode((Module)obj11, (DoubleValue)obj12),
            (obj13, obj14) -> new DragClickPatternMode((Module)obj13, (DoubleValue)obj14),
            (obj15, obj16) -> new DistributionClickPatternMode((Module)obj15, (DoubleValue)obj16)
         }
      );
      this.cpsLimiter = new StringValue(this, "CPSLimiter", 15.0, 1.0, 40.0, 0, this.clickSettings);
      this.cpsLimiterIntave = new BooleanValue(this, "CPSLimiterIntave", true, this.clickSettings);
      this.interactions = new DoubleValue(this, "Interactions", true, this.clicking);
      this.attackMode = new BooleansSetting(this, "AttackMode", "Legit", new String[]{"Legit", "Packet", "Multi"}, this.interactions);
      StringValue stringValue42 = new StringValue(this, "MaxTargets", 10.0, 1.0, 20.0, 0, this.interactions);
      SettingVisibilityCondition condition87 = () -> this.isMaxTargetsVisible();
      this.maxTargets = stringValue42.visibleWhen(condition87);
      this.optifineHitbox = new BooleanValue(this, "OptifineHitbox", false, this.interactions);
      this.blockMode = new BooleansSetting(
         this,
         "BlockMode",
         "None",
         new String[]{
            "None",
            "Constant",
            "Constant2",
            "Pre",
            "Post",
            "UnblockOnHit",
            "UnblockOnHit2",
            "Spam",
            "SlowSpam",
            "OldHypixel",
            "AAC",
            "AACOld",
            "Legit",
            "Bug",
            "Test"
         },
         this.interactions
      );
      BooleanValue booleanValue17 = new BooleanValue(this, "SmartBlock", true, this.interactions);
      SettingVisibilityCondition condition88 = () -> this.isSmartBlockVisible();
      this.smartBlock = (BooleanValue)booleanValue17.withVisibility(condition88);
      BooleanValue booleanValue18 = new BooleanValue(this, "InteractAutoBlock", true, this.interactions);
      SettingVisibilityCondition condition89 = () -> this.isInteractAutoBlockVisible();
      this.interactAutoBlock = (BooleanValue)booleanValue18.withVisibility(condition89);
      BooleanValue booleanValue19 = new BooleanValue(this, "OnlyRightClickBlock", false, this.interactions);
      SettingVisibilityCondition condition90 = () -> {
         return !this.blockMode.getSelectedValue().equals("None");
      };
      this.onlyRightClickBlock = (BooleanValue)booleanValue19.withVisibility(condition90);
      BooleanValue booleanValue20 = new BooleanValue(this, "OnlyWhileSwingInProgress", false, this.interactions);
      SettingVisibilityCondition condition91 = () -> {
         return !this.blockMode.getSelectedValue().equals("None");
      };
      this.onlyWhileSwingInProgress = (BooleanValue)booleanValue20.withVisibility(condition91);
      BooleanValue booleanValue21 = new BooleanValue(this, "OnlyWhileHurt", false, this.interactions);
      SettingVisibilityCondition condition92 = () -> this.isOnlyWhileHurtVisible();
      this.onlyWhileHurt = (BooleanValue)booleanValue21.withVisibility(condition92);
      StringValue stringValue43 = new StringValue(this, "UnblockAAC", 4.0, 0.0, 10.0, 0, this.interactions);
      SettingVisibilityCondition condition93 = () -> this.isUnblockAacVisible();
      this.unblockAAC = stringValue43.visibleWhen(condition93);
      this.preHit = new BooleanValue(this, "PreHit", true, this.clicking);
      BooleanValue booleanValue22 = new BooleanValue(this, "SmartPreHit", false, this.clicking);
      BooleanValue preHit2 = this.preHit;
      this.preHit.getClass();
      BooleanValue booleanValue23 = preHit2;
      SettingVisibilityCondition condition94 = () -> booleanValue23.getValue();
      this.smartPreHit = (BooleanValue)booleanValue22.withVisibility(condition94);
      RandomizedRangeSettings randomizedRangeSettings16 = (new RandomizedRangeSettings(this, "Prediction", 3.0, 5.0, 1.0, 10.0, 0, this.clicking)).withoutNotInARow();
      SettingVisibilityCondition condition95 = () -> this.isPreHitPredictionVisible();
      this.prediction = (RandomizedRangeSettings)randomizedRangeSettings16.withVisibility(condition95);
      RandomizedRangeSettings randomizedRangeSettings17 = (new RandomizedRangeSettings(this, "LoseDelay", 3.0, 5.0, 1.0, 10.0, 0, this.clicking)).withoutNotInARow();
      SettingVisibilityCondition condition96 = () -> this.isPreHitLoseDelayVisible();
      this.loseDelay = (RandomizedRangeSettings)randomizedRangeSettings17.withVisibility(condition96);
      RandomizedRangeSettings randomizedRangeSettings18 = (new RandomizedRangeSettings(this, "Endurance", 90.0, 110.0, 1.0, 400.0, 0, this.clicking)).withoutNotInARow();
      SettingVisibilityCondition condition97 = () -> this.isPreHitEnduranceVisible();
      this.endurance = (RandomizedRangeSettings)randomizedRangeSettings18.withVisibility(condition97);
      RandomizedRangeSettings randomizedRangeSettings19 = (new RandomizedRangeSettings(this, "EnduranceReduction", 5.0, 10.0, 1.0, 20.0, 0, this.clicking)).withoutNotInARow();
      SettingVisibilityCondition condition98 = () -> {
         return this.smartPreHit.getValue() && this.smartPreHit.isVisible();
      };
      this.enduranceReduction = (RandomizedRangeSettings)randomizedRangeSettings19.withVisibility(condition98);
      RandomizedRangeSettings randomizedRangeSettings20 = (new RandomizedRangeSettings(this, "PreHitRange", 6.0, 6.0, 3.0, 8.0, 2, this.clicking)).withoutNotInARow();
      BooleanValue preHit3 = this.preHit;
      this.preHit.getClass();
      BooleanValue booleanValue24 = preHit3;
      SettingVisibilityCondition condition99 = () -> booleanValue24.getValue();
      this.preHitRange = (RandomizedRangeSettings)randomizedRangeSettings20.withVisibility(condition99);
      RandomizedRangeSettings randomizedRangeSettings21 = (new RandomizedRangeSettings(this, "PreHitFovYaw", 50.0, 60.0, 0.0, 180.0, 0, this.clicking)).withoutNotInARow();
      BooleanValue preHit4 = this.preHit;
      this.preHit.getClass();
      BooleanValue booleanValue25 = preHit4;
      SettingVisibilityCondition condition100 = () -> booleanValue25.getValue();
      this.preHitFovYaw = (RandomizedRangeSettings)randomizedRangeSettings21.withVisibility(condition100);
      RandomizedRangeSettings randomizedRangeSettings22 = (new RandomizedRangeSettings(this, "PreHitFovPitch", 50.0, 60.0, 0.0, 180.0, 0, this.clicking)).withoutNotInARow();
      BooleanValue preHit5 = this.preHit;
      this.preHit.getClass();
      BooleanValue booleanValue26 = preHit5;
      SettingVisibilityCondition condition101 = () -> booleanValue26.getValue();
      this.preHitFovPitch = (RandomizedRangeSettings)randomizedRangeSettings22.withVisibility(condition101);
      this.missClick = new BooleanValue(this, "MissClick", false, this.clicking);
      StringValue stringValue44 = new StringValue(this, "MissClickPercentage", 5.0, 0.0, 100.0, 0, this.clicking);
      BooleanValue missClick2 = this.missClick;
      this.missClick.getClass();
      BooleanValue booleanValue27 = missClick2;
      SettingVisibilityCondition condition102 = () -> booleanValue27.getValue();
      this.missClickPercentage = stringValue44.visibleWhen(condition102);
      this.perfectHit = new BooleanValue(this, "PerfectHit", true, this.clicking);
      BooleanValue booleanValue28 = new BooleanValue(this, "LongClick", false, this.clicking);
      SettingVisibilityCondition condition103 = () -> {
         return !this.supportsHoldClicking();
      };
      this.longClick = (BooleanValue)booleanValue28.withVisibility(condition103);
      this.critFix = new BooleanValue(this, "CritFix", true, this.clicking);
      BooleanValue booleanValue29 = new BooleanValue(this, "CritFixPacketCheck", true, this.clicking);
      BooleanValue critFix2 = this.critFix;
      this.critFix.getClass();
      BooleanValue booleanValue30 = critFix2;
      SettingVisibilityCondition condition104 = () -> booleanValue30.getValue();
      this.critFixPacketCheck = (BooleanValue)booleanValue29.withVisibility(condition104);
      StringValue stringValue45 = new StringValue(this, "CritFixChance", 100.0, 0.0, 100.0, 1, this.clicking);
      BooleanValue critFix3 = this.critFix;
      this.critFix.getClass();
      BooleanValue booleanValue31 = critFix3;
      SettingVisibilityCondition condition105 = () -> booleanValue31.getValue();
      this.critFixChance = stringValue45.visibleWhen(condition105);
      this.noLoopHit = new BooleanValue(this, "NoLoopHit", true, this.clicking);
      this.fixBeta = new BooleanValue(this, "Fix (Beta)", false, this.clicking);
      this.breakBlocks = new BooleanValue(this, "BreakBlocks", false, this.clicking);
      BooleanValue booleanValue32 = new BooleanValue(this, "DoubleClickFirstHit", true, this.clicking);
      SettingVisibilityCondition condition106 = () -> this.isDoubleClickFirstHitVisible();
      this.doubleClickFirstHit = (BooleanValue)booleanValue32.withVisibility(condition106);
      BooleanValue booleanValue33 = new BooleanValue(this, "ReduceCPSIfYouCanNotHitATargett", false, this.clicking);
      String str24 = "ReduceCPSIfYouCanNotHitATarget";
      BooleanValue booleanValue34 = (BooleanValue)booleanValue33.withDisplayName(str24);
      SettingVisibilityCondition condition107 = () -> {
         return this.clickMode.getSelectedValue().equals("Advanced")
            || this.clickMode.getSelectedValue().equals("Smart")
            || this.clickMode.getSelectedValue().equals("Recorded")
            || this.clickMode.getSelectedValue().equals("Deviation")
            || this.clickMode.getSelectedValue().equals("Distribution");
      };
      this.reduceCPSIfYouCanNotHitATargett = (BooleanValue)booleanValue34.withVisibility(condition107);
      this.noSwing = new BooleanValue(this, "NoSwing", false, this.clicking);
      this.slowDown = new BooleanValue(this, "SlowDown", true, this.clicking);
      BooleanValue booleanValue35 = new BooleanValue(this, "CoolDown", false, this.clicking);
      SettingVisibilityCondition condition108 = () -> {
         return !this.attackMode.getSelectedValue().equals("Multi");
      };
      this.coolDown = (BooleanValue)booleanValue35.withVisibility(condition108);
      this.movement = (DoubleValue)(new DoubleValue(this, "Movement", true)).markVisualOnly();
      this.jumpFix = (BooleanValue)(new BooleanValue(this, "JumpFix", true, this.movement)).markVisualOnly();
      BooleanValue booleanValue36 = new BooleanValue(this, "MoveFix", true, this.movement);
      SettingVisibilityCondition condition109 = () -> this.isMoveFixVisible();
      this.moveFix = (BooleanValue)((BooleanValue)booleanValue36.withVisibility(condition109)).markVisualOnly();
      BooleansSetting booleansSetting4 = new BooleansSetting(this, "SilentMoveFix", "Default", new String[]{"None", "Default", "Advanced"}, this.movement);
      SettingVisibilityCondition condition110 = () -> this.isSilentMoveFixVisible();
      this.silentMoveFix = (BooleansSetting)((BooleansSetting)booleansSetting4.withVisibility(condition110)).markVisualOnly();
      BooleanValue booleanValue37 = new BooleanValue(this, "SilentMoveFixIntave", true, this.movement);
      SettingVisibilityCondition condition111 = () -> {
         return this.silentMoveFix.isVisible() && !this.silentMoveFix.getSelectedValue().equals("None");
      };
      this.silentMoveFixIntave = (BooleanValue)((BooleanValue)booleanValue37.withVisibility(condition111)).markVisualOnly();
      this.targets = (DoubleValue)(new DoubleValue(this, "Targets", true)).markVisualOnly();
      this.targetSelection = (new TargetSelectionSettings(this, TargetFilterPresets.createCombatFilter(), this.targets)).withSorting();
      this.targetDelay = (StringValue)(new StringValue(this, "TargetDelay", 0.0, 0.0, 1000.0, 0, this.targets)).markVisualOnly();
      this.targetLoseDelay = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, "TargetLoseDelay", 0.0, 0.0, 0.0, 1000.0, 0, this.targets))
         .withoutNotInARow()
         .markAllVisualOnly();
      this.maxDeathTime = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, "MaxDeathTime", 0.0, 0.0, 0.0, 30.0, 0, this.targets)).withoutNotInARow().markAllVisualOnly();
      this.miscellaneous = (DoubleValue)(new DoubleValue(this, "Miscellaneous")).markVisualOnly();
      this.closeGuisIfTargetArrives = (BooleanValue)(new BooleanValue(this, "CloseGuisIfTargetArrives", false, this.miscellaneous)).markVisualOnly();
      this.inInv = new BooleanValue(this, "InInv", false, this.miscellaneous);
      this.activeSlots = (MultiOptionSetting)(new MultiOptionSetting(this, "ActiveSlots", KillAuraActiveSlotOption.values(), this.miscellaneous)).markVisualOnly();
      this.weaponFilter = (WeaponFilterSettings)(new WeaponFilterSettings(this, this.miscellaneous)).markAllVisualOnly();
      this.lookDownDisable = new LookDownDisableSettings(this, this.miscellaneous);
      BooleanValue booleanValue38 = new BooleanValue(this, "Intave13", false, this.miscellaneous);
      SettingVisibilityCondition condition112 = () -> this.isIntave13Visible();
      this.intave13 = (BooleanValue)booleanValue38.withVisibility(condition112);
      RandomizedRangeSettings randomizedRangeSettings23 = (new RandomizedRangeSettings(this, "DelayRotationsAfterClosingGuiScreen", 3.0, 5.0, 1.0, 20.0, 0, this.miscellaneous)).withoutNotInARow();
      SettingVisibilityCondition condition113 = () -> this.isDelayRotationsAfterGuiVisible();
      this.delayRotationsAfterClosingGuiScreen = (RandomizedRangeSettings)randomizedRangeSettings23.withVisibility(condition113);
      RandomizedRangeSettings randomizedRangeSettings24 = (new RandomizedRangeSettings(this, "DelayClicksAfterClosingGuiScreen", 3.0, 5.0, 1.0, 20.0, 0, this.miscellaneous)).withoutNotInARow();
      SettingVisibilityCondition condition114 = () -> this.isDelayClicksAfterGuiVisible();
      this.delayClicksAfterClosingGuiScreen = (RandomizedRangeSettings)randomizedRangeSettings24.withVisibility(condition114);
      RandomizedRangeSettings randomizedRangeSettings25 = (new RandomizedRangeSettings(this, "DelayRotationsAfterRespawn", 4.0, 6.0, 1.0, 20.0, 0, this.miscellaneous)).withoutNotInARow();
      SettingVisibilityCondition condition115 = () -> this.isDelayRotationsAfterRespawnVisible();
      this.delayRotationsAfterRespawn = (RandomizedRangeSettings)randomizedRangeSettings25.withVisibility(condition115);
      RandomizedRangeSettings randomizedRangeSettings26 = (new RandomizedRangeSettings(this, "DelayClicksAfterRespawn", 4.0, 6.0, 1.0, 20.0, 0, this.miscellaneous)).withoutNotInARow();
      SettingVisibilityCondition condition116 = () -> this.isDelayClicksAfterRespawnVisible();
      this.delayClicksAfterRespawn = (RandomizedRangeSettings)randomizedRangeSettings26.withVisibility(condition116);
      this.visuals = (DoubleValue)(new DoubleValue(this, "Visuals")).markVisualOnly();
      this.dot = (BooleanValue)(new BooleanValue(this, "Dot", false, this.visuals)).markVisualOnly();
      StringValue stringValue46 = new StringValue(this, "DotSize", 0.3, 0.01, 1.0, 2, this.visuals);
      BooleanValue dot2 = this.dot;
      this.dot.getClass();
      BooleanValue booleanValue39 = dot2;
      SettingVisibilityCondition condition117 = () -> booleanValue39.getValue();
      this.dotSize = (StringValue)stringValue46.visibleWhen(condition117).markVisualOnly();
      Color color2 = new Color(41, 146, 222);
      randomize2 = this.visuals;
      Color color3 = color2;
      str4 = "DotColor";
      ColorSetting colorSetting = new ColorSetting(this, str4, color3, randomize2);
      BooleanValue dot3 = this.dot;
      this.dot.getClass();
      BooleanValue booleanValue40 = dot3;
      SettingVisibilityCondition condition118 = () -> booleanValue40.getValue();
      this.dotColor = (ColorSetting)((ColorSetting)colorSetting.withVisibility(condition118)).markVisualOnly();
      BlurShadowSettings blurShadowSettings = new BlurShadowSettings(this, this.visuals);
      BooleanValue dot4 = this.dot;
      this.dot.getClass();
      BooleanValue booleanValue41 = dot4;
      SettingVisibilityCondition condition119 = () -> booleanValue41.getValue();
      BlurShadowSettings blurShadowSettings2 = ((BlurShadowSettings)blurShadowSettings.withVisibility(condition119)).disableBlurByDefault();
      Color black = Color.black;
      this.dotShadowSettings = (BlurShadowSettings)blurShadowSettings2.withCustomShadowColor(black).markAllVisualOnly();
      this.targetSwitchTimer = new TimeHelper();
      this.lastAttackTimer = new TimeHelper();
      this.clickCycleTimer = new TimeHelper();
      this.currentRange = 3.0;
      this.target = null;
      this.previousTarget = null;
      this.lastValidTarget = null;
      this.intaveRotationFlags = 0;
      this.loadedAiModelName = null;
      this.rotationTrailPoints = new CopyOnWriteArrayList();
      this.ticksSinceBlockAttack = 0;
      this.lastSwingPacketTimer = new TimeHelper();
      this.doubleClickFirstHitTimer = new TimeHelper();
      this.clickSpeedMonitor = new ClickSpeedMonitor(60);
      this.currentRotation = new Rotation();
      this.lockViewMouseDelta = new int[2];
      this.lastTickTimestamp = 0L;
      this.lastTickDurationMs = 0;
      this.firstHitReady = false;
      this.lastObjectMouseOver = null;
      this.holdingLeftClick = false;
      this.itemUseCleared = false;
      this.ticksSinceBugBlockSwitch = 0;
      this.grimRangeActive = false;
      this.lastTickTargets = new ArrayList();
      this.grimAttackPending = false;
      this.pendingLongClickRelease = false;
      this.intervalActionCount = 0;
      this.betaFixHitCount = 0;
      this.attackedThisTick = false;
      this.targetLoseTimer = new TimeHelper();
      this.tickCounter = new TickCounter();
      this.longClickBlockDelay = 0;
      this.useExtendedDualRange = false;
      this.actionRateAccumulator = new ActionRateAccumulator(100, 0.0);
      this.debugPosHistory = new BoundedHistoryList(23);
      this.ticksSinceTeleport = 0;
      this.preHitRangeTracker = new HysteresisRangeTracker();
      this.preAimRangeTracker = new HysteresisRangeTracker();
      this.aimThroughWallsRangeTracker = new HysteresisRangeTracker();
      this.preHitFovYawTracker = new HysteresisRangeTracker();
      this.preHitFovPitchTracker = new HysteresisRangeTracker();
      this.needsClickPatternReset = false;
      this.consecutiveHits = 0;
      this.appliedMouseDelta = new int[2];
      this.fractionalMouseDelta = new double[2];
      this.critLanded = false;
      this.preHitEnduranceUsed = 0;
      this.preHitEnduranceExhausted = false;
      RotationUtil rotationUtil2 = this.rotationUtil;
      UNoise3SettingContext uNoise3SettingContext = new UNoise3SettingContext(this, this.randomize);
      rotationUtil2.uNoise3RotationEngine = new UNoise3RotationEngine(uNoise3SettingContext, () -> {
         return this.randomizeMode.getSelectedValue().equals("Noise3");
      });
      this.aiModelRegistry = new OnnxModelRegistry(this.aiModel);
   }

   @Override
   public void onEnable() {
      this.firstHitReady = true;
      this.currentRange = this.range2.getValue();
      this.lockViewMouseDelta[0] = this.lockViewMouseDelta[1] = 0;
      String str = "0";
      this.setSuffix(str);
      this.target = null;
      this.currentRotation = ClientManagers.rotationController.getPreviousRotation();
      this.previousTarget = null;
      this.needsClickPatternReset = true;
      this.resetClickPattern();
      RotationUtil rotationUtil2 = this.rotationUtil;
      float f;
      if (this.randomizeMode.getSelectedValue().equals("Advanced")) {
         f = (float)this.innerRadius.getMaxValue();
      } else {
         f = 20.0F;
      }

      short s = 1000;
      float f2 = f;
      rotationUtil2.generateDeltaCandidates(f2, s);
      this.activeAttackMode = this.attackMode.getSelectedValue();
      this.rotationUtil.reset();
      RandomizedRangeSettings preAimRange2 = this.preAimRange;
      this.preAimRangeTracker.reset(preAimRange2);
      RandomizedRangeSettings preHitRange2 = this.preHitRange;
      this.preHitRangeTracker.reset(preHitRange2);
      RandomizedRangeSettings aimThroughWallsRange2 = this.aimThroughWallsRange;
      this.aimThroughWallsRangeTracker.reset(aimThroughWallsRange2);
      RandomizedRangeSettings preHitFovYaw2 = this.preHitFovYaw;
      this.preHitFovYawTracker.reset(preHitFovYaw2);
      RandomizedRangeSettings preHitFovPitch2 = this.preHitFovPitch;
      this.preHitFovPitchTracker.reset(preHitFovPitch2);
      this.maxDeathTime.nextValue();
      this.refreshAiModels();
      if (this.rotationUtil.aiRotationPredictor != null) {
         this.rotationUtil.aiRotationPredictor.reset();
      }
   }

   @Override
   public void onDisable() {
      this.rotationTrailPoints.clear();
      this.lockViewMouseDelta[0] = this.lockViewMouseDelta[1] = 0;
      this.appliedMouseDelta[0] = this.appliedMouseDelta[1] = 0;
      this.fractionalMouseDelta[0] = this.fractionalMouseDelta[1] = 0.0;
      this.clearTargets();
      this.disableGrimRange();
      this.rotationUtil.reset();
      this.unloadAiModel();
      this.preHitEnduranceUsed = 0;
      this.preHitEnduranceExhausted = false;
      Object obj = null;
      this.targetSelection.setCurrentTarget((Entity)obj);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 18;
      byte b2 = 28;
      this.toggleEventSubscription(b2, b);
      byte b3 = 2;
      this.toggleEventSubscription(b3);
      byte b4 = 7;
      this.toggleEventSubscription(b4);
      byte b5 = 6;
      this.toggleEventSubscription(b5);
      byte b6 = 40;
      this.toggleEventSubscription(b6);
      byte b7 = 21;
      this.toggleEventSubscription(b7);
      byte b8 = 18;
      this.toggleEventSubscription(b8);
      byte b9 = 32;
      this.toggleEventSubscription(b9);
      byte b10 = 18;
      byte b11 = 29;
      this.toggleEventSubscription(b11, b10);
      byte b12 = 26;
      this.toggleEventSubscription(b12);
      byte b13 = 19;
      this.toggleEventSubscription(b13);
      byte b14 = 34;
      this.toggleEventSubscription(b14);
      byte b15 = 20;
      this.toggleEventSubscription(b15);
      byte b16 = 17;
      this.toggleEventSubscription(b16);
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.clearTargets();
      this.rerollRespawnDelays();
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
            if (this.target != null && entityByID != null && entityByID.getEntityId() == this.target.getEntityId()) {
               this.critLanded = true;
            }
         }

         if (packet instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet3 = (S08PacketPlayerPosLook)packet;
            Vec3 vec = new Vec3(packet3.getX(), packet3.getY(), packet3.getZ());
            if (vec.distanceTo(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ) > 4.0) {
               this.ticksSinceTeleport = 0;
               this.rerollRespawnDelays();
            }
         }
      }
   }

   @Override
   public void onSilentSlot() {
      this.ticksSinceBugBlockSwitch++;
      if (this.canOperate()
         && this.target != null
         && this.blockMode.getSelectedValue().equals("Bug")
         && mc.thePlayer.getHeldItem() != null
         && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword
         && this.target.hurtTime <= 2
         && this.itemUseCleared) {
         int n = SilentSlotManager.realHotbarSlot == 0 ? 1 : -1;
         int n2 = SilentSlotManager.realHotbarSlot + n;
         byte b = 1;
         SilentSlotManager.requestSlot(this, b, n2);
         this.itemUseCleared = false;
         this.ticksSinceBugBlockSwitch = 0;
      }
   }

   @Override
   public void onPacket2(EventPacket event) {
      if (event.getPacket() instanceof C03PacketPlayer && this.grimRangeActive && !BlockUtil.isScaffoldActiveAndValid()) {
         event.cancel();
      }
   }

   @Override
   public void onMoveEntity(EventMoveEntity event) {
      if (this.grimRangeActive && !BlockUtil.isScaffoldActiveAndValid()) {
         double d = 0.0;
         event.setX(d);
         double d2 = 0.0;
         event.setY(d2);
         double d3 = 0.0;
         event.setZ(d3);
      }
   }

   public List getSortedTargets() {
      double maxValue = this.preHitRange.getMaxValue();
      double d = Math.max(maxValue, this.preAimRange.getMaxValue());
      return this.findTargetsInRange(d);
   }

   private List findTargetsInRange(double d) {
      EntityTargetSelector entityTargetSelector = EntityTargetSelector.of(mc.theWorld.loadedEntityList);
      EntityTargetFilter targetFilter = this.targetSelection.getTargetFilter();
      EntityTargetFilter entityTargetFilter = targetFilter;
      entityTargetSelector = entityTargetSelector.withTargetFilter(entityTargetFilter).maxDistance(d).filterFocus();
      double value = this.yawFov.getValue();
      double value2 = this.pitchFov.getValue();
      double d2 = value;
      entityTargetSelector = entityTargetSelector.withinFov(d2, value2);
      int lastIntValue = this.maxDeathTime.getLastIntValue();
      entityTargetSelector = entityTargetSelector.maxDeathTime(lastIntValue);
      Predicate predicate = obj -> this.isEntityReachableThroughWalls((Entity)obj);
      entityTargetSelector = entityTargetSelector.filter(predicate);
      TargetSelectionSettings targetSelection2 = this.targetSelection;
      Rotation effectiveRotation = this.getEffectiveRotation();
      double currentRange2 = this.currentRange;
      Rotation rotation = effectiveRotation;
      ToDoubleFunction toDoubleFunction = targetSelection2.createTargetComparatorKey(rotation, currentRange2);
      return entityTargetSelector.sortBy(toDoubleFunction).toList();
   }

   @Override
   public void onTick() {
      long l = a ^ 17667390453267L;
      long l2 = l ^ 48852419907699L;
      l2 = l ^ 55445232593535L;
      l2 = l ^ 82978559677762L;
      l2 = l ^ 45310865166084L;
      if (mc.currentScreen != null && mc.previousScreen == null) {
         this.delayClicksAfterClosingGuiScreen.nextValue();
         this.delayRotationsAfterClosingGuiScreen.nextValue();
      }

      RandomizedRangeSettings preHitRange2 = this.preHitRange;
      EntityLivingBase previousTarget2 = this.previousTarget;
      EntityLivingBase target2 = this.target;
      this.preHitRangeTracker.updateRange(target2, previousTarget2, preHitRange2);
      RandomizedRangeSettings preAimRange2 = this.preAimRange;
      EntityLivingBase previousTarget3 = this.previousTarget;
      EntityLivingBase target3 = this.target;
      this.preAimRangeTracker.updateRange(target3, previousTarget3, preAimRange2);
      RandomizedRangeSettings aimThroughWallsRange2 = this.aimThroughWallsRange;
      EntityLivingBase previousTarget4 = this.previousTarget;
      EntityLivingBase target4 = this.target;
      this.aimThroughWallsRangeTracker.updateRange(target4, previousTarget4, aimThroughWallsRange2);
      float[] floats;
      if (this.target == null) {
         floats = new float[]{0.0F, 0.0F};
      } else {
         float rotationPitch = mc.thePlayer.rotationPitch;
         float rotationYaw = mc.thePlayer.rotationYaw;
         floats = RotationUtil.getAngleDiffToEntityNearestPoint(this.target, rotationYaw, rotationPitch);
      }

      float[] floats2 = floats;
      HysteresisRangeTracker preHitFovYawTracker2 = this.preHitFovYawTracker;
      EntityLivingBase target5 = this.target;
      EntityLivingBase previousTarget5 = this.previousTarget;
      RandomizedRangeSettings preHitFovYaw2 = this.preHitFovYaw;
      double d = (double)Math.abs(floats2[0]);
      RandomizedRangeSettings randomizedRangeSettings = preHitFovYaw2;
      EntityLivingBase entity = previousTarget5;
      EntityLivingBase entity2 = target5;
      preHitFovYawTracker2.updateRangeWithDistance(entity2, entity, randomizedRangeSettings, d);
      HysteresisRangeTracker preHitFovPitchTracker2 = this.preHitFovPitchTracker;
      EntityLivingBase target6 = this.target;
      previousTarget5 = this.previousTarget;
      preHitFovYaw2 = this.preHitFovPitch;
      double d2 = (double)Math.abs(floats2[1]);
      RandomizedRangeSettings randomizedRangeSettings2 = preHitFovYaw2;
      EntityLivingBase entity3 = previousTarget5;
      EntityLivingBase entity4 = target6;
      preHitFovPitchTracker2.updateRangeWithDistance(entity4, entity3, randomizedRangeSettings2, d2);
      this.lockViewMouseDelta[0] = this.lockViewMouseDelta[1] = 0;
      this.previousTarget = this.target;
      EntityLivingBase previousTarget6 = this.previousTarget;
      this.targetSelection.setCurrentTarget(previousTarget6);
      this.target = null;
      if (!this.canOperate() && !this.closeGuisIfTargetArrives.getValue()) {
         this.currentRotation = ClientManagers.rotationController.getCurrentRotation();
      } else {
         List sortedTargets = this.getSortedTargets();
         Vec3 targetHitVec2 = this.targetHitVec;
         boolean flag = this.throughWallsMode.getSelectedValue().equals("NoneFullBlocks");
         if (flag) {
            FullBlockRaytraceFilter.INSTANCE.enable();
         }

         TuplePair tuplePair = this.selectTargetAndHitVec(sortedTargets);
         if (flag) {
            FullBlockRaytraceFilter.INSTANCE.disable();
         }

         this.targetHitVec = (Vec3)tuplePair.getFirst();
         EntityLivingBase second = (EntityLivingBase)tuplePair.getSecond();
         String str = "" + (sortedTargets.isEmpty() ? 0 : sortedTargets.size());
         this.setSuffix(str);
         if (this.target != null) {
            if (this.closeGuisIfTargetArrives.getValue() && !this.canOperate() && !(mc.currentScreen instanceof ClickGuiScreen) && this.target != null) {
               mc.thePlayer.closeScreen();
               this.currentRotation = ClientManagers.rotationController.getCurrentRotation();
               this.target = null;
               return;
            }

            EntityLivingBase target7 = this.target;
            if (this.rangeMode.getSelectedValue().equals("Grim") && this.target != null) {
               ArrayList list = new ArrayList();

               for (Object obj : sortedTargets) {
                  if (obj instanceof EntityLivingBase && !this.lastTickTargets.contains(obj)) {
                     EntityLivingBase entity5 = (EntityLivingBase)obj;
                     if (entity5.getDistanceToEntityBox(mc.thePlayer) <= 3.0F) {
                        list.add(entity5);
                     }
                  }
               }

               if (target7 != null && target7.hurtTime != 0 && this.grimAttackPending && mc.thePlayer.onGround) {
                  this.grimAttackPending = false;
                  this.enableGrimRange();
               }

               if (!list.isEmpty()) {
                  this.disableGrimRange();
               }
            }
         }

         this.lastTickTargets.clear();
         this.lastTickTargets.addAll(sortedTargets);
         if (second != null) {
            this.targetLoseTimer.reset();
            this.targetLoseDelay.nextValue();
            this.maxDeathTime.nextValue();
         }

         if (second == null && this.lastValidTarget != null) {
            TimeHelper targetLoseTimer2 = this.targetLoseTimer;
            long lastIntValue = (long)this.targetLoseDelay.getLastIntValue();
            if (!targetLoseTimer2.reached2(lastIntValue) && (double)mc.thePlayer.getDistanceToEntityBox(this.lastValidTarget) < this.preAimRangeTracker.getCurrentThreshold()) {
               second = this.lastValidTarget;
               this.target = second;
            }
         }

         EntityLivingBase target8 = this.target;
         this.target = this.filterByActiveSlotAndWeapon(target8);
         second = this.filterByActiveSlotAndWeapon(second);
         if (second != null && this.previousTarget != null && this.target != null && !this.target.getName().equals(this.previousTarget.getName()) && this.targetDelay.getValue() != 0.0) {
            TimeHelper targetSwitchTimer2 = this.targetSwitchTimer;
            l2 = (long)this.targetDelay.getValue();
            long l3 = 60L;
            long l4 = 0L;
            long l5 = l2 + DefaultRandomGenerator.instance.nextLong2(l4, l3);
            if (!targetSwitchTimer2.reached2(l5)) {
               this.target = this.previousTarget;
               second = this.target;
               this.targetHitVec = targetHitVec2;
            } else {
               this.targetSwitchTimer.reset();
            }
         }

         this.lastValidTarget = second;
         if (second != null && this.target != null) {
            exitBlock: {
               if (mc.getTicksWithoutScreen() > this.delayRotationsAfterClosingGuiScreen.getLastIntValue()) {
                  RandomizedRangeSettings randomizedRangeSettings3 = this.delayRotationsAfterRespawn;
                  if (!this.isRespawnDelayActive(randomizedRangeSettings3)) {
                     break exitBlock;
                  }
               }

               if (!this.inInv.getValue()) {
                  this.currentRotation = ClientManagers.rotationController.getPreviousRotation();
                  if (!this.noRotation.getValue()) {
                     RotationController rotationController = ClientManagers.rotationController;
                     Rotation currentRotation2 = this.currentRotation;
                     int rotationPriority = this.getRotationPriority();
                     Rotation rotation = currentRotation2;
                     rotationController.requestRotation(rotation, rotationPriority, this);
                  }

                  return;
               }
            }

            this.rotationUtil.noiseMinAimSpeed = this.additionalMinAimspeed.getValue();
            this.rotationUtil.noiseDynamicSpeed = this.additionalDynamicSpeed.getValue();
            this.rotationUtil.noiseExtraAimSpeed = this.additionalAimspeedIfNeeded.getValue();
            boolean flag2 = true;
            this.rotationUtil.noiseIdleAimProbability = this.probabilityOfAimingEvenWhenNotNeeded.getValue(flag2) / 100.0;
            this.rotationUtil.noiseTestJitterEnabled = this.test.getValue();
            boolean flag3 = true;
            this.rotationUtil.bezierAimSpeed = this.aimSpeed.getValue(flag3) / 100.0 * 8.0 + 2.0;
            boolean flag4 = true;
            this.rotationUtil.bezierPreDecelerationFactor = this.decelerationfactorPre.getValue(flag4) / 100.0 * 4.0 + 0.5;
            boolean flag5 = true;
            this.rotationUtil.bezierPostDecelerationFactor = this.decelerationfactorPost.getValue(flag5) / 100.0 * 4.0 + 1.0;
            this.rotationUtil.bezierMinReactionTime = MathHelper.clamp_int((int)this.reactionTime.getMinValue(), 0, 6);
            this.rotationUtil.bezierMaxReactionTime = MathHelper.clamp_int((int)this.reactionTime.getMaxValue(), 0, 6);
            boolean flag6 = true;
            this.rotationUtil.bezierAttentionWindow = (int)this.attention.getValue(flag6);
            boolean flag7 = true;
            this.rotationUtil.bezierAdditionalInaccuracy = this.additionalInaccuracy.getValue(flag7) / 100.0 * 20.0;
            boolean flag8 = true;
            this.rotationUtil.bezierSuperCorrectionDelay = this.supercorrectionDelay.nextInt(flag8);
            boolean flag9 = true;
            this.rotationUtil.bezierSuperCorrectionProbability = this.supercorrectionProbability.getValue(flag9) / 100.0;
            this.rotationUtil.bezierAdditionalAccuracy = this.additionalAccuracy.getValue();
            boolean flag10 = true;
            this.rotationUtil.bezierMinLookaheadTicks = (int)this.lookaheadTime.getMinValue(flag10);
            boolean flag11 = true;
            this.rotationUtil.bezierMaxLookaheadTicks = (int)this.lookaheadTime.getMaxValue(flag11);
            boolean flag12 = true;
            this.rotationUtil.bezierMinPredictionInaccuracy = this.predictionInaccuracy.getMinValue(flag12) / 100.0;
            boolean flag13 = true;
            this.rotationUtil.bezierMaxPredictionInaccuracy = this.predictionInaccuracy.getMaxValue(flag13) / 100.0;
            this.rotationUtil.bezierPredictionInaccuracySmoothness = (float)MathStatisticsUtil.lerp(0.016666666666666666, 0.05, 1.0 - this.predictionInaccuracySmoothness.getValue() / 100.0);
            boolean flag14 = true;
            this.rotationUtil.bezierBestVecPitchInterpolation = this.bestvecPitchinterpolation.nextDouble(flag14) / 100.0;
            boolean flag15 = true;
            this.rotationUtil.bezierMinRotationOnTarget = this.rotationOnTarget.getMinValue(flag15) / 100.0;
            boolean flag16 = true;
            this.rotationUtil.bezierMaxRotationOnTarget = this.rotationOnTarget.getMaxValue(flag16) / 100.0;
            boolean flag17 = true;
            this.rotationUtil.bezierMaxOrthogonalMovement = this.maxOrthogonalMovement.getValue(flag17);
            boolean flag18 = true;
            this.rotationUtil.bezierMinOrthogonalScale = this.orthogonalScale.getMinValue(flag18) / 100.0;
            boolean flag19 = true;
            this.rotationUtil.bezierMaxOrthogonalScale = this.orthogonalScale.getMaxValue(flag19) / 100.0;
            boolean flag20 = true;
            this.rotationUtil.bezierMaxAimpointAngleDiff = this.maxAimpointAnglediff.getValue(flag20);
            boolean flag21 = true;
            this.rotationUtil.bezierMinYawOffset = this.rotationYawOffset.getMinValue(flag21);
            boolean flag22 = true;
            this.rotationUtil.bezierMaxYawOffset = this.rotationYawOffset.getMaxValue(flag22);
            boolean flag23 = true;
            this.rotationUtil.bezierMinPitchOffset = this.rotationPitchOffset.getMinValue(flag23);
            boolean flag24 = true;
            this.rotationUtil.bezierMaxPitchOffset = this.rotationPitchOffset.getMaxValue(flag24);
            boolean flag25 = true;
            this.rotationUtil.bezierYawOffsetSpeed = this.rotationYawOffsetSpeed.getValue(flag25) / 100.0 * 0.2;
            boolean flag26 = true;
            this.rotationUtil.bezierPitchOffsetSpeed = this.rotationPitchOffsetSpeed.getValue(flag26) / 100.0 * 0.2;
            this.rotationUtil.bezierSkipYawOutOfReach = this.noYawAdjustmentIfTargetOutOfReach.getValue();
            this.rotationUtil.bezierSkipPitchOutOfReach = this.noPitchAdjustmentIfTargetOutOfReach.getValue();
            this.rotationUtil.bezierApplyRecordedMouseJitter = this.applyRecordedMouseJitterMovementIfAvailable.getValue();
            this.rotationUtil.bezierDuplicateEndControlPoint = this.ultimateTrashcodeBypass.getValue();
            this.rotationUtil.bezierAdaptCurveIfNeeded = this.adaptCurveIfNeeded.getValue();
            boolean flag27 = true;
            this.rotationUtil.turboMinTargetResponseTicks = (int)this.noise3ResponseTarget.getMinValue(flag27);
            boolean flag28 = true;
            this.rotationUtil.turboMaxTargetResponseTicks = (int)this.noise3ResponseTarget.getMaxValue(flag28);
            boolean flag29 = true;
            this.rotationUtil.turboMinPlayerResponseTicks = (int)this.noise3PlayerResponse.getMinValue(flag29);
            boolean flag30 = true;
            this.rotationUtil.turboMaxPlayerResponseTicks = (int)this.noise3PlayerResponse.getMaxValue(flag30);
            this.rotationUtil.turboMinAcceleration = (float)((int)this.noise3Acceleration.getMinValue());
            this.rotationUtil.turboMaxAcceleration = (float)((int)this.noise3Acceleration.getMaxValue());
            this.rotationUtil.turboMinAimSpeed = (float)((int)this.noise3AimSpeed.getMinValue());
            this.rotationUtil.turboMaxAimSpeed = (float)((int)this.noise3AimSpeed.getMaxValue());
            boolean flag31 = true;
            this.rotationUtil.turboMinLeadDistanceScale = this.noise3Scale.getValue(flag31);
            boolean flag32 = true;
            this.rotationUtil.turboBetterAccelerationFactor = this.noise33BetterAcceleration.getValue(flag32);
            if (this.randomizeMode.getSelectedValue().equals("AI")) {
               this.ensureAiModelLoaded();
               if (this.rotationUtil.aiRotationPredictor != null && this.target != null && this.target != this.previousTarget) {
                  EntityLivingBase target9 = this.target;
                  this.rotationUtil.aiRotationPredictor.syncTarget(target9);
               }
            } else {
               this.unloadAiModel();
            }

            if (second != this.target) {
               String str2 = "ERROR OCCURED. Please report this to the developer.";
               this.sendChatMessage2(str2);
            }

            RotationUtil rotationUtil2 = this.rotationUtil;
            EntityLivingBase target10 = this.target;
            RandomizedRangeSettings yawAcceleration2 = this.yawAcceleration;
            RandomizedRangeSettings pitchAcceleration2 = this.pitchAcceleration;
            preHitFovYaw2 = this.yawSpeed;
            RandomizedRangeSettings pitchSpeed2 = this.pitchSpeed;
            float yaw = this.getEffectiveRotation().yaw;
            float pitch = this.getEffectiveRotation().pitch;
            String selectedValue = this.randomizeMode.getSelectedValue();
            boolean value = this.interpolation.getValue();
            boolean value2 = this.smartAim.getValue();
            boolean value3 = this.intave13.getValue();
            float value4 = (float)this.randomStrength.getValue();
            Vec3 targetHitVec3 = this.targetHitVec;
            boolean value5 = this.advancedRots.getValue();
            boolean value6 = this.advancedRotsNotAtTarget.getValue();
            String selectedValue2 = this.heuristics.getSelectedValue();
            String selectedValue3 = this.rotationMode.getSelectedValue();
            double currentRange2 = this.currentRange;
            int value7 = (int)this.hitBoxPercentageVertical.getValue();
            int value8 = (int)this.hitBoxPercentageHorizontal.getValue();
            int value9 = (int)this.hitBoxPercentageVertical1.getValue();
            int value10 = (int)this.hitBoxPercentageHorizontal1.getValue();
            int intaveRotationFlags2 = this.intaveRotationFlags;
            String selectedValue4 = this.randomType.getSelectedValue();
            String selectedValue5 = this.fixRotationMode.getSelectedValue();
            boolean value11 = this.smartRotate.getValue();
            boolean value12 = this.reducedRotate.getValue();
            boolean value13 = this.pitchOutliers.getValue();
            RandomizedRangeSettings pitchOutliersAdd2 = this.pitchOutliersAdd;
            RandomizedRangeSettings pitchOutliersDelay2 = this.pitchOutliersDelay;
            double value14 = this.reduceRotationPercentage.getValue();
            float value15 = (float)this.maxXAbsDist.getValue();
            float value16 = (float)this.maxYAbsDist.getValue();
            float value17 = (float)this.minAbsDist.getValue();
            float minValue = (float)this.xRelDist.getMinValue();
            float minValue2 = (float)this.yRelDist.getMinValue();
            float maxValue = (float)this.xRelDist.getMaxValue();
            float maxValue2 = (float)this.yRelDist.getMaxValue();
            float value18 = (float)this.maxRotationAngle.getValue();
            float minValue3 = (float)this.innerRadius.getMinValue();
            float value19 = (float)this.bestHitXMinDist.getValue();
            float value20 = (float)this.bestHitYMInDist.getValue();
            boolean value21 = this.betterAcceleration.getValue();
            boolean flag33 = true;
            int value22 = (int)this.fixRotationChance.getValue(flag33);
            double d3 = this.maxFixRotationReach.nextValue();
            double value23 = this.minFixRotationAngle.getValue();
            double value24 = this.minDistanceToBestVecYaw.getValue();
            double value25 = this.minDistanceToBestVecPitch.getValue();
            double value26 = this.minDistanceToBestVec.getValue();
            String selectedValue6 = this.minDistanceToBestVecCalculation.getSelectedValue();
            boolean value27 = this.stopRotationInsideTarget.getValue();
            String str3 = selectedValue6;
            double d4 = value26;
            double d5 = value25;
            double d6 = value24;
            double d7 = value23;
            double d8 = d3;
            int n = value22;
            boolean flag34 = value21;
            float f = value20;
            float f2 = value19;
            float f3 = minValue3;
            float f4 = value18;
            float f5 = maxValue2;
            float f6 = maxValue;
            float f7 = minValue2;
            float f8 = minValue;
            float f9 = value17;
            float f10 = value16;
            float f11 = value15;
            double d9 = value14;
            RandomizedRangeSettings randomizedRangeSettings4 = pitchOutliersDelay2;
            RandomizedRangeSettings randomizedRangeSettings5 = pitchOutliersAdd2;
            boolean flag35 = value13;
            boolean flag36 = value12;
            boolean flag37 = value11;
            String str4 = selectedValue5;
            String str5 = selectedValue4;
            int n2 = intaveRotationFlags2;
            int n3 = value10;
            int n4 = value9;
            int n5 = value8;
            int n6 = value7;
            double d10 = currentRange2;
            String str6 = selectedValue3;
            String str7 = selectedValue2;
            boolean flag38 = value6;
            boolean flag39 = value5;
            Vec3 vec = targetHitVec3;
            float f12 = value4;
            boolean flag40 = value3;
            boolean flag41 = value2;
            boolean flag42 = value;
            String str8 = selectedValue;
            float f13 = pitch;
            float f14 = yaw;
            RandomizedRangeSettings randomizedRangeSettings6 = pitchSpeed2;
            RandomizedRangeSettings randomizedRangeSettings7 = preHitFovYaw2;
            RandomizedRangeSettings randomizedRangeSettings8 = pitchAcceleration2;
            RandomizedRangeSettings randomizedRangeSettings9 = yawAcceleration2;
            EntityLivingBase entity6 = target10;
            Rotation rotation2 = rotationUtil2.computeKillAuraRotation(
               entity6,
               randomizedRangeSettings9,
               randomizedRangeSettings8,
               randomizedRangeSettings7,
               randomizedRangeSettings6,
               f14,
               f13,
               str8,
               flag42,
               flag41,
               flag40,
               f12,
               vec,
               flag39,
               flag38,
               str7,
               str6,
               d10,
               n6,
               n5,
               n4,
               n3,
               n2,
               str5,
               str4,
               flag37,
               flag36,
               flag35,
               randomizedRangeSettings5,
               randomizedRangeSettings4,
               d9,
               f11,
               f10,
               f9,
               f8,
               f7,
               f6,
               f5,
               f4,
               f3,
               f2,
               f,
               flag34,
               n,
               d8,
               d7,
               d6,
               d5,
               d4,
               str3,
               value27
            );
            if (!this.noRotation.getValue()) {
               RotationController rotationController2 = ClientManagers.rotationController;
               int rotationPriority2 = this.getRotationPriority();
               rotationController2.requestRotation(rotation2, rotationPriority2, this);
            }

            this.lockViewMouseDelta = RotationUtil.computeMouseDeltas(rotation2.yaw, rotation2.pitch, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
            this.currentRotation = rotation2;
         } else {
            this.rotationUtil.reset();
            this.currentRotation = ClientManagers.rotationController.getCurrentRotation();
         }
      }
   }

   private void refreshAiModels() {
      String absolutePath = new File(ClientPaths.CLIENT_DIR, "ai-7yns").getAbsolutePath();
      OnnxModelRegistry aiModelRegistry2 = this.aiModelRegistry;
      File file = new File(absolutePath.trim());
      aiModelRegistry2.setModelsDirectory(file);
   }

   private void ensureAiModelLoaded() {
      String selectedValue = this.aiModel.getSelectedValue();
      if (selectedValue != null && !selectedValue.equals("None") && this.randomizeMode.getSelectedValue().equals("AI")) {
         if (!selectedValue.equals(this.loadedAiModelName)) {
            this.unloadAiModel();
            File modelFile = this.aiModelRegistry.getModelFile(selectedValue);
            File normalizationFile = this.aiModelRegistry.getNormalizationFile(selectedValue);
            if (modelFile.exists() && normalizationFile.exists()) {
               this.rotationUtil.aiRotationPredictor = new AiRotationPredictor(modelFile, normalizationFile);
               this.loadedAiModelName = selectedValue;
            }
         }
      } else {
         this.unloadAiModel();
      }
   }

   private void unloadAiModel() {
      if (this.rotationUtil.aiRotationPredictor != null) {
         this.rotationUtil.aiRotationPredictor.close();
         this.rotationUtil.aiRotationPredictor = null;
      }

      this.loadedAiModelName = null;
   }

   private TuplePair selectTargetAndHitVec(List list) {
      EntityTargetSelector entityTargetSelector = EntityTargetSelector.of(list);
      double currentThreshold = this.preAimRangeTracker.getCurrentThreshold();
      List list2 = entityTargetSelector.maxDistance(currentThreshold).toList();
      this.targetHitVec = null;
      if (this.smartAim.getValue() && !this.throughWallsMode.getSelectedValue().equals("AllBlocks")) {
         Vec3 vec = this.findVisibleHitVec(list2);
         if (vec != null) {
            return new TuplePair(vec, this.target);
         }

         ArrayList list3 = new ArrayList(list);
         list3.removeAll(list2);
         vec = this.findVisibleHitVec(list3);
         if (vec != null) {
            return new TuplePair(vec, null);
         }
      }

      if (this.canAimThroughWalls() || !this.smartAim.getValue()) {
         if (!list2.isEmpty()) {
            this.target = (EntityLivingBase)list2.get(0);
            return new TuplePair(null, this.target);
         }

         if (!list.isEmpty()) {
            this.target = (EntityLivingBase)list.get(0);
            return new TuplePair(null, null);
         }
      }

      this.target = null;
      return new TuplePair(null, null);
   }

   private Vec3 findVisibleHitVec(List list) {
      boolean flag = false;

      for (int i = 0; i < list.size(); i++) {
         EntityLivingBase entity = (EntityLivingBase)list.get(i);
         if (mc.thePlayer.getDistanceToEntity(entity) <= 6.0F) {
            flag = true;
            break;
         }
      }

      for (int j = 0; j < list.size(); j++) {
         EntityLivingBase entity2 = (EntityLivingBase)list.get(j);
         if (!(mc.thePlayer.getDistanceToEntity(entity2) > 6.0F) || !flag) {
            RotationUtil rotationUtil2 = this.rotationUtil;
            String selectedValue = this.rotationMode.getSelectedValue();
            int value = (int)this.hitBoxPercentageVertical.getValue();
            int value2 = (int)this.hitBoxPercentageHorizontal.getValue();
            int value3 = (int)this.hitBoxPercentageVertical1.getValue();
            int value4 = (int)this.hitBoxPercentageHorizontal1.getValue();
            Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
            Rotation rotation = previousRotation;
            int n = value4;
            int n2 = value3;
            int n3 = value2;
            int n4 = value;
            String str = selectedValue;
            Vec3 vec = rotationUtil2.selectEntityAimPoint(entity2, str, n4, n3, n2, n, rotation);
            double d = vec.xCoord - mc.thePlayer.posX;
            double d2 = vec.yCoord - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
            double d3 = vec.zCoord - mc.thePlayer.posZ;
            Rotation rotation2 = new Rotation(
               (float)(MathHelper.atan2(d3, d) * 180.0 / Math.PI - 90.0),
               (float)(-(MathHelper.atan2(d2, (double)MathHelper.sqrt_double(d * d + d3 * d3)) * 180.0 / Math.PI))
            );
            double maxValue = this.preAimRange.getMaxValue();
            double d4 = Math.max(maxValue, this.preHitRange.getMaxValue()) + 3.0;
            double d5 = 0.0;
            double d6 = d4;
            MovingObjectPosition hit = RayTraceUtil.rayTraceEntity(1.0F, rotation2, d6, d5);
            if (hit != null
               && hit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY
               && hit.entityHit != null
               && (hit.entityHit.getEntityId() == entity2.getEntityId() || !this.smartAimEntityCheck.getValue())) {
               this.target = (EntityLivingBase)list.get(j);
               return new Vec3(vec.xCoord, vec.yCoord, vec.zCoord);
            }

            float collisionBorderSize = entity2.getCollisionBorderSize();
            AxisAlignedBB box = entity2.getEntityBoundingBox().expand((double)collisionBorderSize, (double)collisionBorderSize, (double)collisionBorderSize);
            HitVecCandidate hitVecCandidate = new HitVecCandidate(
               entity2.posX - mc.thePlayer.posX, mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight(), entity2.posZ - mc.thePlayer.posZ
            );
            AxisAlignedBB box2 = box;
            List list2 = MathStatisticsUtil.sampleBoxSurfacePoints(box2, hitVecCandidate);
            boolean flag2 = false;
            byte b = 1;

            for (HitVecCandidate hitVecCandidate2 : (Iterable<HitVecCandidate>)(list2)) {
               double d7 = hitVecCandidate2.z;
               double d8 = hitVecCandidate2.y;
               double d9 = hitVecCandidate2.x;
               hitVecCandidate2.visibilityFlags = this.evaluateHitVecVisibility(d9, d8, d7, entity2);
               if (hitVecCandidate2.visibilityFlags[0] || hitVecCandidate2.visibilityFlags[1]) {
                  flag2 = true;
               }

               if (hitVecCandidate2.visibilityFlags[0]) {
                  b = 0;
               }
            }

            if (flag2 && (b == 0 || this.canAimThroughWalls())) {
               HitVecCandidate hitVecCandidate3 = MathStatisticsUtil.findDensestHitVecCandidate(list2, b);
               this.target = (EntityLivingBase)list.get(j);
               return new Vec3(hitVecCandidate3.x, hitVecCandidate3.y, hitVecCandidate3.z);
            }
         }
      }

      return null;
   }

   private EntityLivingBase filterByActiveSlotAndWeapon(EntityLivingBase entity) {
      int n = mc.thePlayer.inventory.currentItem + 1;
      if ((n != 1 || KillAuraActiveSlotOption.SLOT1.isSelected())
         && (n != 2 || KillAuraActiveSlotOption.SLOT2.isSelected())
         && (n != 3 || KillAuraActiveSlotOption.SLOT3.isSelected())
         && (n != 4 || KillAuraActiveSlotOption.SLOT4.isSelected())
         && (n != 5 || KillAuraActiveSlotOption.SLOT5.isSelected())
         && (n != 6 || KillAuraActiveSlotOption.SLOT6.isSelected())
         && (n != 7 || KillAuraActiveSlotOption.SLOT7.isSelected())
         && (n != 8 || KillAuraActiveSlotOption.SLOT8.isSelected())
         && (n != 9 || KillAuraActiveSlotOption.SLOT9.isSelected())) {
         return this.weaponFilter.isHeldItemAllowed() && !this.lookDownDisable.shouldDisable() ? entity : null;
      } else {
         return null;
      }
   }

   @Override
   public void onClickGui2() {
      this.currentRange = this.range2.getValue();
      this.activeAttackMode = this.attackMode.getSelectedValue();
   }

   @Override
   public void onMotion() {
      if (this.lastObjectMouseOver != null) {
         this.previousHitVec = this.lastObjectMouseOver.hitVec;
      }

      this.lastObjectMouseOver = mc.objectMouseOver;
      if (this.target != null && this.canOperate() && !BlockUtil.isScaffoldActiveAndValid()) {
         String selectedValue = this.blockMode.getSelectedValue();
         switch (selectedValue) {
            case "Spam":
            case "Post":
               this.blockSword();
         }
      }
   }

   @Override
   public void onMotion(EventMotion event) {
      AbstractClickPatternMode abstractSubMode = (AbstractClickPatternMode)this.clickMode.getAbstractSubMode();
      if (abstractSubMode != null) {
         abstractSubMode.onTick2();
      }

      this.tickCounter.increment();
      if (this.target != null && this.canOperate() && !BlockUtil.isScaffoldActiveAndValid()) {
         String selectedValue = this.blockMode.getSelectedValue();
         byte b = -1;
         switch (selectedValue.hashCode()) {
            case 80515:
               if (selectedValue.equals("Pre")) {
                  b = 0;
               }
            default:
               switch (b) {
                  case 0:
                     this.blockSword();
               }
         }
      }
   }

   @Override
   public void onMove(EventMove event) {
      if (this.hasRotationControl()
         && !BlockUtil.isScaffoldActiveAndValid()
         && (!ClientManagers.moduleManager.targetStrafe.isToggled() || ClientManagers.moduleManager.targetStrafe.target == null)
         && !this.moveFix.getValue()) {
         float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
         event.setYaw(realYaw);
      }
   }

   @Override
   public void onMove2(EventMove event) {
      if (this.hasRotationControl()
         && !BlockUtil.isScaffoldActiveAndValid()
         && (!ClientManagers.moduleManager.targetStrafe.isToggled() || ClientManagers.moduleManager.targetStrafe.target == null)
         && !this.jumpFix.getValue()) {
         float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
         event.setYaw(realYaw);
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (this.hasRotationControl()
         && !BlockUtil.isScaffoldActiveAndValid()
         && (!ClientManagers.moduleManager.targetStrafe.isToggled() || ClientManagers.moduleManager.targetStrafe.target == null)
         && this.moveFix.getValue()
         && !this.silentMoveFix.getSelectedValue().equals("None")) {
         if (this.silentMoveFix.getSelectedValue().equals("Advanced")) {
            MoveFixMode advanced = MoveFixMode.ADVANCED;
            event.setMoveFixMode(advanced);
         } else {
            MoveFixMode moveFixMode = MoveFixMode.DEFAULT;
            event.setMoveFixMode(moveFixMode);
         }

         if (this.silentMoveFixIntave.getValue()) {
            boolean flag = true;
            event.setPreventInstantReverse(flag);
         }
      }
   }

   @Override
   public void onAttackSlowdown(EventAttackSlowdown event) {
      if (!this.slowDown.getValue() && this.target != null) {
         double d = 1.0;
         event.setSlowDownFactor(d);
         boolean flag = true;
         event.setSprint(flag);
      }
   }

   @Override
   public void onClick(EventClick event) {
      this.attackedThisTick = false;
      if ((this.hasRotationControl() || this.noRotation.getValue()) && (!ClientManagers.moduleManager.autoGapple.isToggled() || ClientManagers.moduleManager.autoGapple.getGappleSlot() == -1) && this.canOperate()) {
         exitBlock: {
            if (this.target != null && (mc.getTicksWithoutScreen() > this.delayClicksAfterClosingGuiScreen.getLastIntValue() && mc.currentScreen == null || this.inInv.getValue())) {
               RandomizedRangeSettings randomizedRangeSettings = this.delayClicksAfterRespawn;
               if (!this.isRespawnDelayActive(randomizedRangeSettings) || this.inInv.getValue()) {
                  if (this.needsClickPatternReset) {
                     this.resetClickPattern();
                     this.needsClickPatternReset = false;
                  }

                  this.handleCombatClick(event);
                  break exitBlock;
               }
            }

            this.decayPreHitEndurance();
            this.needsClickPatternReset = true;
            this.resetClickPattern();
            this.firstHitReady = true;
         }

         String selectedValue = this.rangeMode.getSelectedValue();
         switch (selectedValue) {
            case "HazeRange":
               if (this.target != null && this.target.hurtTime == 10) {
                  this.currentRange = Math.min(this.currentRange + this.hazeAdd.getValue(), this.hazeMax.getValue());
                  this.lastHazeHitTime = System.currentTimeMillis();
               }

               if (this.target != this.previousTarget || this.previousTarget == null) {
                  this.currentRange = this.range2.getValue();
               }

               if (Math.abs(System.currentTimeMillis() - this.lastHazeHitTime) > 1000L) {
                  this.currentRange = this.range2.getValue();
               }
               break;
            case "IntervalRange":
               this.currentRange = this.range2.getValue();
               if ((double)this.intervalActionCount >= this.interval.getValue()
                  && this.target != null
                  && (double)this.target.hurtTime <= this.hurtTimeToHitWithReach.getValue()
                  && (double)mc.thePlayer.getDistanceToEntityBox(this.target) > this.minReachToIntervalHit.getValue()
                  && mc.thePlayer.getDistanceToEntity(this.target) < 6.0F) {
                  this.currentRange = this.rangeAtInterval.getValue();
                  this.intervalActionCount = 0;
               }
            case "Grim":
               break;
            case "Dual":
               if (this.target != null) {
                  double d;
                  if (this.useExtendedDualRange) {
                     d = this.baseRange.getMaxValue();
                  } else {
                     d = this.baseRange.getMinValue();
                  }

                  this.currentRange = d;
                  if (this.target.hurtTime == 9) {
                     int n = MathHelper.clamp_int((int)this.chanceOfExtendedRange.getValue(), 0, 100);
                     this.useExtendedDualRange = this.actionRateAccumulator.shouldTrigger(n);
                  }
               }
               break;
            default:
               this.currentRange = this.range2.getValue();
         }
      } else {
         this.needsClickPatternReset = true;
         this.resetClickPattern();
         this.decayPreHitEndurance();
      }
   }

   @Override
   public void onRayCast() {
      if (this.target == null) {
         this.disableGrimRange();
      }

      if (this.target != null && (ClientManagers.rotationController.getCurrentPriority() >= this.getRotationPriority() || this.noRotation.getValue())) {
         float rotationYaw = mc.thePlayer.rotationYaw;
         float rotationPitch = mc.thePlayer.rotationPitch;
         if (this.noRotation.getValue()) {
            mc.thePlayer.rotationYaw = this.currentRotation.yaw;
            mc.thePlayer.rotationPitch = this.currentRotation.pitch;
         }

         float f = 1.0F;
         this.updateRayTraceTarget(f);
         if (this.optifineHitbox.getValue() && (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY)) {
            MovingObjectPosition hit = RayCastUtil.rayCast(1.0F, ClientManagers.rotationController.getPreviousRotation().yaw, ClientManagers.rotationController.getCurrentRotation().pitch);
            if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
               mc.objectMouseOver = hit;
            }
         }

         if (this.noRotation.getValue()) {
            mc.thePlayer.rotationYaw = rotationYaw;
            mc.thePlayer.rotationPitch = rotationPitch;
         }
      }
   }

   @Override
   public void onRender(float f) {
      if (ClientManagers.moduleManager.line.isToggled() && ClientManagers.moduleManager.line.activateAuraLine.getValue() && !ClientManagers.moduleManager.line.displayLegitimateRotations.getValue()) {
         boolean isBlurActive = ClientManagers.moduleManager.line.blurShadowSettings.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, ClientManagers.moduleManager.line.blurShadowSettings.isShadowActive());
         RenderPassTask renderPassTask = shaderRenderPass -> this.renderRotationLineTrail(shaderRenderPass);
         boolean flag = false;
         BlurShadowManager.submitRenderTask(flag, renderPassTask);
      }

      if (this.target != null) {
         if (this.randomizeMode.getSelectedValue().equals("Noise3")) {
            this.rotationUtil.uNoise3RotationEngine.onRender(f);
         }

         boolean isBlurActive2 = this.dotShadowSettings.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive2, this.dotShadowSettings.isShadowActive());
         RenderPassTask renderPassTask2 = shaderRenderPass2 -> this.renderTargetDotPass(f, shaderRenderPass2);
         boolean flag2 = false;
         BlurShadowManager.submitRenderTask(flag2, renderPassTask2);
      }

      if (RotationUtil.debugAimPoint1 != null) {
         RenderUtil.begin3DRender();
         GL11.glPushMatrix();
         GL11.glLineWidth(3.0F);
         GL11.glTranslated(-mc.getRenderManager().getRenderPosX(), -mc.getRenderManager().getRenderPosY(), -mc.getRenderManager().getRenderPosZ());
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         RenderUtil.drawFilledBox(AxisAlignedBB.fromCenter(RotationUtil.debugAimPoint1, 0.05));
         GL11.glPopMatrix();
         RenderUtil.end3DRender();
      }

      if (RotationUtil.targetPositionTracker != null && RotationUtil.targetPositionTracker.targetEntity == this.target && RotationUtil.debugAimPoint1Prev != null) {
         RenderUtil.begin3DRender();
         GL11.glPushMatrix();
         GL11.glLineWidth(3.0F);
         GL11.glTranslated(-mc.getRenderManager().getRenderPosX(), -mc.getRenderManager().getRenderPosY(), -mc.getRenderManager().getRenderPosZ());
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         RenderUtil.drawFilledBox(AxisAlignedBB.fromCenter(RotationUtil.debugAimPoint1Prev.lerp(RotationUtil.debugAimPoint1, (double)f), 0.05));
         GlStateManager.color(1.0F, 0.0F, 0.0F, 1.0F);
         RenderUtil.drawFilledBox(AxisAlignedBB.fromCenter(RotationUtil.debugAimPoint2Prev.lerp(RotationUtil.debugAimPoint2, (double)f), 0.05));
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         RenderUtil.drawFilledBox(AxisAlignedBB.fromCenter(RotationUtil.debugAimPoint3Prev.lerp(RotationUtil.debugAimPoint3, (double)f), 0.05));
         GlStateManager.color(1.0F, 0.0F, 0.0F, 1.0F);
         RenderUtil.drawFilledBox(AxisAlignedBB.fromCenter(RotationUtil.debugAimPoint4Prev.lerp(RotationUtil.debugAimPoint4, (double)f), 0.05));
         GL11.glPopMatrix();
         RenderUtil.end3DRender();
      }

      if (this.target != null) {
         RenderUtil.begin3DRender();
         GL11.glPushMatrix();
         GL11.glLineWidth(3.0F);
         GL11.glTranslated(-mc.getRenderManager().getRenderPosX(), -mc.getRenderManager().getRenderPosY(), -mc.getRenderManager().getRenderPosZ());

         for (int i = 0; i < this.target.targetPosHistory.size(); i++) {
            Vec3 vec = (Vec3)this.target.targetPosHistory.get(i);
            if (this.debugPosHistory.getEntries().size() > i) {
               vec = vec.lerp((Vec3)this.debugPosHistory.get(i), (double)f);
            }

            RenderUtil.drawFilledBox(AxisAlignedBB.fromCenter(vec, 0.05));
         }

         for (Vec3 vec2 : (Iterable<Vec3>)(this.target.targetPosHistory.getEntries())) {
            this.debugPosHistory.add(vec2);
         }

         if (RotationUtil.debugAimPoint4Prev != null) {
            GlStateManager.color(1.0F, 0.0F, 0.0F, 1.0F);
            RenderUtil.drawFilledBox(AxisAlignedBB.fromCenter(RotationUtil.debugAimPoint4Prev.lerp(RotationUtil.debugAimPoint4, (double)f), 0.05));
         }

         if (RotationUtil.debugAimPoint4Prev != null) {
            GlStateManager.color(0.0F, 1.0F, 0.0F, 1.0F);
            RenderUtil.drawFilledBox(AxisAlignedBB.fromCenter(RotationUtil.debugAimPoint2Prev.lerp(RotationUtil.debugAimPoint2, (double)f), 0.05));
         }

         GL11.glPopMatrix();
         RenderUtil.end3DRender();
      }
   }

   @Override
   public void onMouseMove(EventMouseMove event) {
      exitBlock: {
         if (this.lastTickDurationMs > 10
            && this.lockView.getValue()
            && this.canOperate()
            && this.target != null
            && ClientManagers.rotationController.isOwnedBy(this)
            && !this.randomizeMode.getSelectedValue().equals("LegitTest")) {
            if (!this.randomizeMode.getSelectedValue().equals("Noise2")) {
               int n = (int)(System.currentTimeMillis() - this.lastMouseMoveTime);
               double d = (double)this.lockViewMouseDelta[0] / (double)this.lastTickDurationMs * (double)n;
               double d2 = (double)this.lockViewMouseDelta[1] / (double)this.lastTickDurationMs * (double)n;
               this.fractionalMouseDelta[0] = this.fractionalMouseDelta[0] + d;
               this.fractionalMouseDelta[1] = this.fractionalMouseDelta[1] + d2;
               int n2 = (int)Math.round(this.fractionalMouseDelta[0] - (double)this.appliedMouseDelta[0]);
               int n3 = (int)Math.round(this.fractionalMouseDelta[1] - (double)this.appliedMouseDelta[1]);
               this.appliedMouseDelta[0] = this.appliedMouseDelta[0] + n2;
               this.appliedMouseDelta[1] = this.appliedMouseDelta[1] + n3;
               event.setDeltaX(n2);
               event.setDeltaY(n3);
               boolean flag = true;
               event.setIgnoreInvertMouse(flag);
               boolean flag2 = true;
               event.setBypassSmoothCamera(flag2);
               break exitBlock;
            }

            Augustus.getInstance().getClass();
         }

         this.appliedMouseDelta[0] = this.appliedMouseDelta[1] = 0;
         this.fractionalMouseDelta[0] = this.fractionalMouseDelta[1] = 0.0;
         this.lockViewMouseDelta[0] = this.lockViewMouseDelta[1] = 0;
      }

      this.lastMouseMoveTime = System.currentTimeMillis();
   }

   @Override
   public void onTick4() {
      this.lastTickDurationMs = (int)Math.abs(this.lastTickTimestamp - System.currentTimeMillis());
      this.lastTickTimestamp = System.currentTimeMillis();
      this.appliedMouseDelta[0] = this.appliedMouseDelta[1] = 0;
      this.fractionalMouseDelta[0] = this.fractionalMouseDelta[1] = 0.0;
   }

   private void enableGrimRange() {
      this.grimRangeActive = true;
      this.currentRange = 6.0;
      this.activeAttackMode = "Multi";
   }

   private void disableGrimRange() {
      this.grimRangeActive = false;
      this.currentRange = 3.0;
      this.activeAttackMode = this.attackMode.getSelectedValue();
   }

   private boolean[] evaluateHitVecVisibility(double d, double d2, double d3, EntityLivingBase entity) {
      if (mc.thePlayer.canEntityBeSeen(d, d2, d3)) {
         double d4 = d - mc.thePlayer.posX;
         double d5 = d2 - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
         double d6 = d3 - mc.thePlayer.posZ;
         Rotation rotation = new Rotation(
            (float)(MathHelper.atan2(d6, d4) * 180.0 / Math.PI - 90.0),
            (float)(-(MathHelper.atan2(d5, (double)MathHelper.sqrt_double(d4 * d4 + d6 * d6)) * 180.0 / Math.PI))
         );
         double d7 = 0.0;
         double currentRange2 = this.currentRange;
         MovingObjectPosition hit = RayTraceUtil.rayTraceEntity(1.0F, rotation, currentRange2, d7);
         if (hit == null
            || hit.typeOfHit == MovingObjectPosition.MovingObjectType.MISS
            || hit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && hit.entityHit.getEntityId() == entity.getEntityId()) {
            return new boolean[]{hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY, true};
         }
      }

      return new boolean[]{false, false};
   }

   private boolean shouldMissClick() {
      byte b = 99;
      byte b2 = 0;
      return (double)DefaultRandomGenerator.instance.nextInt2(b2, b) > 100.0 - this.missClickPercentage.getValue();
   }

   private boolean hasPendingClicks(int n) {
      return n > 0;
   }

   private boolean isEventBasedBlockMode(String str) {
      return str.equals("AAC") || str.equals("Legit") || str.equals("None") || str.equals("Test");
   }

   private void handleCombatClick(EventClick event) {
      if (this.target != null && !BlockUtil.isScaffoldActiveAndValid()) {
         boolean flag = this.canAttackTarget();
         int n = this.computeClickCount(flag);
         String selectedValue = this.blockMode.getSelectedValue();
         if (!this.isEventBasedBlockMode(selectedValue)
            && this.canAutoBlock()
            && mc.thePlayer.getCurrentEquippedItem() != null
            && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
            if (mc.thePlayer.getCurrentEquippedItem() != null
                  && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword
                  && !ClientManagers.moduleManager.fixes.shouldApplyIntaveBlockingFix()
               || this.blockMode.getSelectedValue().equals("Bug") && Math.abs(mc.thePlayer.inventory.currentItem - SilentSlotManager.realHotbarSlot) == 1 && this.ticksSinceBugBlockSwitch < 4) {
               String selectedValue2 = this.blockMode.getSelectedValue();
               switch (selectedValue2) {
                  case "Constant":
                     if (this.hasPendingClicks(n)) {
                        this.performDirectAttacks(n);
                     }

                     this.blockSword();
                     break;
                  case "Constant2":
                     if (this.hasPendingClicks(n)) {
                        this.performDirectAttacks(n);
                     }

                     if (!mc.thePlayer.isUsingItem()) {
                        this.blockSword();
                     }
                     break;
                  case "UnblockOnHit":
                     if (this.hasPendingClicks(n)) {
                        this.unblockSword();
                        this.performDirectAttacks(n);
                     }

                     this.blockSword();
                     break;
                  case "UnblockOnHit2":
                     if (this.hasPendingClicks(n)) {
                        this.unblockSword();
                        this.performDirectAttacks(n);
                     }

                     if (!mc.thePlayer.isUsingItem()) {
                        this.blockSword();
                     }
                     break;
                  case "Spam":
                     this.unblockSword();
                     if (this.hasPendingClicks(n)) {
                        this.performDirectAttacks(n);
                     }
                     break;
                  case "SlowSpam":
                     if (mc.thePlayer.ticksExisted % 2 == 0) {
                        this.unblockSword();
                     }

                     this.blockSword();
                     if (this.hasPendingClicks(n)) {
                        this.performDirectAttacks(n);
                     }
                     break;
                  case "OldHypixel":
                     if (mc.thePlayer.hurtTime > 0) {
                        if (!mc.thePlayer.isUsingItem()) {
                           this.blockSword();
                        }

                        if (this.hasPendingClicks(n)) {
                           this.performDirectAttacks(n);
                        }
                     } else if (mc.thePlayer.isUsingItem()) {
                        this.unblockSword();
                     } else if (this.hasPendingClicks(n)) {
                        this.performDirectAttacks(n);
                     }
                     break;
                  case "Hypixel":
                     if (mc.thePlayer.hurtTime > 0) {
                        if (!mc.thePlayer.isUsingItem()) {
                           this.blockSword();
                        }

                        if (this.hasPendingClicks(n)) {
                           this.performDirectAttacks(n);
                        }
                     } else {
                        this.unblockSword();
                        if (this.hasPendingClicks(n)) {
                           this.performDirectAttacks(n);
                        }
                     }
                     break;
                  case "Post":
                  case "Pre":
                     if (this.hasPendingClicks(n)) {
                        this.performDirectAttacks(n);
                     }
                     break;
                  case "AACOld":
                     if (!this.canAutoBlock()) {
                        this.unblockSword();
                        if (this.hasPendingClicks(n)) {
                           this.performDirectAttacks(n);
                        }
                     } else if ((this.target.hurtTime == 0 && this.ticksSinceBlockAttack > 4 || (double)this.target.clientHurtTime == this.unblockAAC.getValue())
                        && mc.objectMouseOver != null
                        && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                        this.unblockSword();
                        this.performDirectAttacks(n);
                        this.blockSword();
                        this.ticksSinceBlockAttack = 0;
                     } else if (!mc.thePlayer.isUsingItem()) {
                        this.blockSword();
                     }
                     break;
                  case "Bug":
                     if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                        if (this.target.hurtTime <= 1 && !this.itemUseCleared && this.ticksSinceBugBlockSwitch > 1) {
                           mc.clickMouse();
                           if (this.interactAutoBlock.getValue()) {
                              mc.rightClickMouse();
                           } else {
                              mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
                           }
                        }
                     } else if (n > 0) {
                        this.performDirectAttacks(n);
                     }
               }

               event.cancel();
               boolean flag2 = true;
               event.setBlockAllClicks(flag2);
               boolean flag3 = this.shouldReleaseLongClick();
               event.setLeftMouseDown(flag3);
            }

            this.ticksSinceBlockAttack++;
         } else {
            if (!this.isBreakingBlock()) {
               if (!this.blockMode.getSelectedValue().equals("None")
                  && mc.thePlayer.getCurrentEquippedItem() != null
                  && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword
                  && !ClientManagers.moduleManager.fixes.shouldApplyIntaveBlockingFix()
                  && this.canAutoBlock()) {
                  String selectedValue3 = this.blockMode.getSelectedValue();
                  switch (selectedValue3) {
                     case "Legit":
                        if (mc.thePlayer.isUsingItem()) {
                           boolean flag4 = false;
                           event.setRightMouseDown(flag4);
                        } else {
                           byte b = 1;
                           this.performAttacks(b, event);
                           this.blockSwordWithEvent(event);
                        }
                        break;
                     case "AAC":
                        MovingObjectPosition hit = RayCastUtil.rayCastPredicted(1, ClientManagers.rotationController.getCurrentRotation(), this.currentRange);
                        if ((
                              this.target.hurtTime == 0 && this.ticksSinceBlockAttack > 4
                                 || (double)this.target.clientHurtTime == this.unblockAAC.getValue() + 1.0
                                 || (double)this.target.clientHurtTime == this.unblockAAC.getValue()
                           )
                           && (
                              mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY
                                 || hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY
                           )) {
                           if (mc.thePlayer.isUsingItem()) {
                              boolean flag5 = false;
                              event.setRightMouseDown(flag5);
                           } else {
                              byte b2 = 1;
                              this.performAttacks(b2, event);
                              this.ticksSinceBlockAttack = 0;
                              this.blockSwordWithEvent(event);
                           }
                        } else {
                           this.blockSwordWithEvent(event);
                        }
                        break;
                     case "Test":
                        if (mc.thePlayer.isUsingItem()) {
                           boolean flag6 = false;
                           event.setRightMouseDown(flag6);
                        } else if (mc.thePlayer.hurtTime == 2) {
                           byte b3 = 1;
                           this.performAttacks(b3, event);
                           this.blockSwordWithEvent(event);
                        } else if (this.hasPendingClicks(n)) {
                           this.performAttacks(n, event);
                        }
                  }
               } else if (!mc.thePlayer.isUsingItem()) {
                  this.performAttacks(n, event);
               }

               boolean value = this.longClick.getValue();
               event.setDelayBlockBreak(value);
               if (this.supportsHoldClicking() && flag) {
                  boolean flag7 = true;
                  event.setOverrideBlockBreak(flag7);
                  boolean holdingLeftClick2 = this.holdingLeftClick;
                  event.setBreakingBlock(holdingLeftClick2);
               }
            } else {
               boolean flag8 = true;
               event.setLeftMouseDown(flag8);
            }

            this.ticksSinceBlockAttack++;
         }
      } else {
         this.decayPreHitEndurance();
      }
   }

   private boolean shouldReleaseLongClick() {
      if (this.pendingLongClickRelease && this.longClick.getValue()) {
         if (mc.currentScreen == null && mc.inGameHasFocus && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int longClickBlockDelay2 = this.longClickBlockDelay;
            if (this.tickCounter.hasReached2(longClickBlockDelay2)) {
               byte b = 3;
               byte b2 = 1;
               this.longClickBlockDelay = DefaultRandomGenerator.instance.nextInt2(b2, b);
               this.tickCounter.reset();
               this.pendingLongClickRelease = false;
               return true;
            }
         } else {
            this.tickCounter.reset();
            this.pendingLongClickRelease = false;
         }

         return mc.currentScreen == null && mc.inGameHasFocus;
      } else {
         this.tickCounter.reset();
         return false;
      }
   }

   private int computeClickCount(boolean flag) {
      int n = 0;
      if (flag) {
         n = this.sampleClickPattern();
      } else {
         this.needsClickPatternReset = true;
         this.resetClickPattern();
      }

      if (ClientManagers.moduleManager.moveHelper.isToggled() && MoveHelperModeOption.HIT_SELECTION.isSelected() && ClientManagers.moduleManager.moveHelper.shouldHitSelect()) {
         n = 0;
      }

      if (n == 0
         && ClientManagers.moduleManager.moveHelper.isToggled()
         && MoveHelperModeOption.HIT_SELECTION.isSelected()
         && !ClientManagers.moduleManager.moveHelper.shouldHitSelect()
         && ClientManagers.moduleManager.moveHelper.isPreviousShouldHitSelect()) {
         n = 1;
      }

      int n2 = (int)((double)n + EventClick.recentClicksBuffer.sum());
      if (this.cpsLimiterIntave.getValue() ? !this.clickSpeedMonitor.isExceedsCpsLimit() : !((double)n2 > this.cpsLimiter.getValue())) {
         if (n == 0 && this.critFix.getValue() && mc.objectMouseOver != null) {
            boolean flag2 = true;
            if (this.critFixChance.getValue(flag2) != 100.0) {
               double d = Math.random();
               boolean flag3 = true;
               if (!(d < this.critFixChance.getValue(flag3) / 100.0)) {
                  return n;
               }
            }

            boolean flag4 = ClientManagers.moduleManager.criticals.isToggled()
               || mc.thePlayer.fallDistance > 0.0F
                  && !mc.thePlayer.onGround
                  && !mc.thePlayer.isOnLadder()
                  && !mc.thePlayer.isInWater()
                  && !mc.thePlayer.isPotionActive(Potion.blindness)
                  && mc.thePlayer.ridingEntity == null
                  && mc.objectMouseOver.entityHit instanceof EntityLivingBase;
            if (!this.critLanded && flag4) {
               return 1;
            }
         }

         return n;
      } else {
         return -1337;
      }
   }

   private int sampleClickPattern() {
      if (this.target.hurtTime == 10) {
         this.critLanded = false;
      }

      if (this.perfectHit.getValue()
         && mc.objectMouseOver != null
         && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY
         && mc.objectMouseOver.entityHit instanceof EntityLivingBase
         && !this.shouldReduceHits()) {
         EntityLivingBase entityHit = (EntityLivingBase)mc.objectMouseOver.entityHit;
         if (entityHit.hurtTime == 0 || entityHit.hurtTime == 1) {
            return 1;
         }
      }

      if (this.intave13.getValue() && ClientManagers.moduleManager.timerRange.isToggled() && ClientManagers.moduleManager.timerRange.fastAttackScore >= 4) {
         return 0;
      } else {
         AbstractClickPatternMode abstractSubMode = (AbstractClickPatternMode)this.clickMode.getAbstractSubMode();
         if (abstractSubMode != null) {
            Object obj = null;
            return abstractSubMode.computeClicks((EventClick)obj);
         } else {
            return 0;
         }
      }
   }

   @Override
   public int applyHitReduction(int n) {
      if (!this.shouldReduceHits()) {
         return n;
      } else {
         if (ClientManagers.moduleManager.debug.devToggleF != null && ClientManagers.moduleManager.debug.devToggleF.getValue() && ClientManagers.moduleManager.debug.isToggled()) {
            String str = "Reduce hits";
            ClientManagers.moduleManager.debug.sendChatMessage2(str);
         }

         return 0;
      }
   }

   private boolean shouldReduceHits() {
      return this.reduceCPSIfYouCanNotHitATargett.getValue()
         && this.target != null
         && this.target.hurtTime == 0
         && System.currentTimeMillis() - this.target.lastDamageAttackTime > 75L
         && System.currentTimeMillis() - this.target.lastDamageAttackTime < 225L;
   }

   private boolean canAutoBlock() {
      if (this.onlyRightClickBlock.getValue() && !mc.gameSettings.keyBindUseItem.isKeyDown()) {
         return false;
      } else if (this.onlyWhileSwingInProgress.getValue() && !mc.thePlayer.isSwingInProgress) {
         return false;
      } else if (this.onlyWhileHurt.getValue() && mc.thePlayer.hurtTime == 0) {
         return false;
      } else {
         return !this.smartBlock.getValue()
            ? true
            : this.target != null
               && RotationUtil.getEntityLookAngleToPlayer(this.target) < 100.0F
               && this.target.getHeldItem() != null
               && (this.target.getHeldItem().getItem() instanceof ItemSword || this.target.getHeldItem().getItem() instanceof ItemTool);
      }
   }

   private void blockSword() {
      Object obj = null;
      this.blockSwordWithEvent((EventClick)obj);
   }

   private void blockSwordWithEvent(EventClick event) {
      if (this.canAutoBlock()
         && mc.thePlayer != null
         && mc.thePlayer.inventory != null
         && mc.thePlayer.inventory.getCurrentItem() != null
         && mc.thePlayer.inventory.getCurrentItem().getItem() != null
         && mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
         if (this.interactAutoBlock.getValue()) {
            if (event == null) {
               mc.rightClickMouse();
            } else {
               boolean flag = true;
               event.setRightMouseDown(flag);
            }
         } else if (event != null) {
            boolean flag2 = true;
            event.setRightMouseDown(flag2);
            Runnable runnable = () -> mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
            event.setRightClickAction(runnable);
         } else {
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
         }
      }
   }

   private void unblockSword() {
      if (mc.thePlayer.isUsingItem()) {
         mc.playerController.onStoppedUsingItem(mc.thePlayer);
      }
   }

   private void performDirectAttacks(int n) {
      Object obj = null;
      this.performAttacks(n, (EventClick)obj);
   }

   private boolean canAttackTarget() {
      if (ClientManagers.moduleManager.autoRod.isToggled() && ClientManagers.moduleManager.autoRod.combat.getValue() && !ClientManagers.moduleManager.gommeMode.isToggled()) {
         mc.playerController.syncCurrentPlayItem();
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(mc.playerController.currentPlayerItem + 36).getStack();
         if (stack == null || !(stack.getItem() instanceof ItemFishingRod)) {
            long l = 90L;
            if (!mc.playerController.timeHelper.reached2(l)) {
               this.decayPreHitEndurance();
               return false;
            }
         }
      }

      if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit instanceof EntityPlayer) {
         Entity entityHit = mc.objectMouseOver.entityHit;
         if (ClientManagers.moduleManager.teams.isTeammate(entityHit)) {
            return false;
         }

         Entity entityHit2 = mc.objectMouseOver.entityHit;
         if (ClientManagers.moduleManager.midClick.isFriend(entityHit2) && !ClientManagers.moduleManager.midClick.autoUnfriendTriggered) {
            return false;
         }
      }

      if (this.noLoopHit.getValue() && mc.remainingTicksInFrame != 0) {
         return false;
      } else if (!(mc.thePlayer.getDistanceToEntity(this.target) < 6.0F) && this.currentRange > 5.0) {
         return false;
      } else if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
         return true;
      } else {
         if (!this.preHit.getValue()) {
            if (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
               return false;
            }
         } else {
            if ((double)mc.thePlayer.getDistanceToEntityBox(this.target) > this.preHitRangeTracker.getCurrentThreshold()) {
               this.decayPreHitEndurance();
               return false;
            }

            AxisAlignedBB expandedEntityBox = RotationUtil.getExpandedEntityBox(this.target);
            Vec3 positionEyes = mc.thePlayer.getPositionEyes(1.0F);
            double rotationPitch = (double)mc.thePlayer.rotationPitch;
            double normalizedPitchInBox = HitboxAngleUtil.getNormalizedPitchInBox(expandedEntityBox, positionEyes, rotationPitch);
            double rotationYaw = (double)mc.thePlayer.rotationYaw;
            double normalizedYawInBox = HitboxAngleUtil.getNormalizedYawInBox(expandedEntityBox, positionEyes, rotationYaw);
            if (normalizedPitchInBox < 0.0 || normalizedPitchInBox > 1.0 || normalizedYawInBox < 0.0 || normalizedYawInBox > 1.0) {
               float rotationPitch2 = mc.thePlayer.rotationPitch;
               float rotationYaw2 = mc.thePlayer.rotationYaw;
               float[] floats = RotationUtil.getAngleDiffToEntityNearestPoint(this.target, rotationYaw2, rotationPitch2);
               if ((double)Math.abs(floats[0]) > this.preHitFovYawTracker.getCurrentThreshold() || (double)Math.abs(floats[1]) > this.preHitFovPitchTracker.getCurrentThreshold()) {
                  return false;
               }
            }

            int lastIntValue = this.prediction.getLastIntValue();
            double d = 3.0;
            if (!this.checkSmartPreHit(d, lastIntValue)) {
               return false;
            }
         }

         return true;
      }
   }

   private void decayPreHitEndurance() {
      boolean flag = true;
      this.preHitEnduranceUsed = Math.max(this.preHitEnduranceUsed - this.enduranceReduction.nextInt(flag), 0);
      if (this.preHitEnduranceUsed == 0) {
         this.preHitEnduranceExhausted = false;
      }
   }

   private boolean checkSmartPreHit(double d, int n) {
      if (!this.smartPreHit.getValue()) {
         return true;
      } else if (this.preHitEnduranceUsed <= this.endurance.getLastIntValue() && !this.preHitEnduranceExhausted) {
         if (mc.ticksLookingAtEntity == 1) {
            this.loseDelay.nextValue();
            this.prediction.nextValue();
         }

         if ((mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) && mc.ticksNotLookingAtEntity > this.loseDelay.getLastIntValue()) {
            if (this.preHitEnduranceUsed > this.endurance.getLastIntValue() / 2) {
               return false;
            } else {
               MovingObjectPosition hit = RayTraceUtil.rayTrace(
                  1.0F, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, 10.0, 6.0, 1.0
               );
               if (hit != null && hit.entityHit instanceof EntityLivingBase) {
                  EntityLivingBase entityHit = (EntityLivingBase)hit.entityHit;
                  int ticksExisted = mc.thePlayer.ticksExisted;

                  for (int i = 1; i < n; i++) {
                     EntityLivingBase entityHit2 = (EntityLivingBase)hit.entityHit;
                     int n2 = ticksExisted + i;
                     EntityLivingBase entity = entityHit2;
                     ArrayList list = TrajectoryPredictionUtil.predictTargetPositions(mc.thePlayer, entity, ticksExisted, n2);
                     int n3 = 99999;
                     Vec3 clampedPosition = TrajectoryPredictionUtil.getClampedPosition(list, n3);
                     AxisAlignedBB box = RotationUtil.createEntityBoxAt(entityHit, clampedPosition);
                     double[] doubles = PlayerUtil.predictPlayerPosition(i - 1, mc.thePlayer);
                     Vec3 vec = new Vec3(doubles[0], doubles[1] + (double)mc.thePlayer.getEyeHeight(), doubles[2]);
                     Vec3 vec2 = RotationUtil.clampPointToBox(vec, box);
                     if (vec.distanceTo(vec2) < d) {
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
         if (!this.preHitEnduranceExhausted) {
            this.endurance.nextValue();
         }

         this.preHitEnduranceExhausted = true;
         return false;
      }
   }

   private void performAttacks(int n, EventClick event) {
      if (this.activeAttackMode.equals("Multi")) {
         if (n > 0) {
            double currentThreshold = this.preAimRangeTracker.getCurrentThreshold();
            List list = this.findTargetsInRange(currentThreshold);
            if (!list.isEmpty()) {
               mc.thePlayer.swingItem();
            }

            for (int i = 0; i < list.size(); i++) {
               Entity entity = (Entity)list.get(i);
               if ((double)i < this.maxTargets.getValue() && !ProtectedPlayerCheck.isProtectedPlayer(entity)) {
                  if (!ClientManagers.moduleManager.antiBot.isBot(entity) && (double)mc.thePlayer.getDistanceToEntityBox(entity) <= this.currentRange) {
                     mc.playerController.attackEntity(mc.thePlayer, entity);
                  }
               }
            }
         }
      } else {
         boolean flag;
         exitBlock: {
            if (this.missClick.getValue()) {
               if (this.shouldMissClick()) {
                  flag = true;
                  break exitBlock;
               }
            }

            flag = false;
         }

         boolean flag2 = flag;
         if (this.coolDown.getValue()) {
            while (n > 0) {
               if (mc.thePlayer.getCurrentEquippedItem() != null
                  && (mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword || mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemTool)) {
                  int attackCooldownTicks = mc.thePlayer.getCurrentEquippedItem().getItem().attackCooldownTicks;
                  if (mc.thePlayer.ticksSinceLastSwing >= attackCooldownTicks) {
                     this.dispatchAttackClick(event, flag2);
                  }
               } else {
                  this.dispatchAttackClick(event, flag2);
               }

               n--;
            }
         } else {
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
               exitBlock2:
               if (this.doubleClickFirstHit.getValue()) {
                  if (!this.firstHitReady) {
                     long l = 1500L;
                     if (!this.doubleClickFirstHitTimer.reached2(l)) {
                        break exitBlock2;
                     }
                  }

                  if (!this.clickMode.getSelectedValue().equals("Smart")) {
                     this.dispatchAttackClick(event, flag2);
                     this.firstHitReady = false;
                  }
               }

               this.doubleClickFirstHitTimer.reset();
            }

            while (n > 0) {
               this.dispatchAttackClick(event, flag2);
               n--;
            }
         }
      }

      this.clickCycleTimer.reset();
   }

   public void dispatchAttackClick(EventClick event, boolean flag) {
      if (!flag) {
         this.lastAttackTimer.reset();
      }

      if (!flag && mc.objectMouseOver != null && event != null) {
         boolean flag2 = ClientManagers.moduleManager.criticals.isToggled()
            || mc.objectMouseOver.entityHit instanceof EntityLivingBase
               && mc.thePlayer.fallDistance > 0.0F
               && !mc.thePlayer.onGround
               && !mc.thePlayer.isOnLadder()
               && !mc.thePlayer.isInWater()
               && !mc.thePlayer.isPotionActive(Potion.blindness)
               && mc.thePlayer.ridingEntity == null;
         if (!this.critLanded && flag2) {
            boolean flag3 = true;
            event.setCriticalHit(flag3);
         }
      }

      if (event == null) {
         this.executeLeftClick(flag);
         if (!flag) {
            EventClick.clicksThisTick++;
         }
      } else {
         byte b = 1;
         event.addLeftClicks(b);
         Runnable runnable = () -> this.runScheduledLeftClick(flag);
         event.setLeftClickAction(runnable);
      }
   }

   private boolean isBreakingBlock() {
      return this.breakBlocks.getValue() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK;
   }

   private void executeLeftClick(boolean flag) {
      if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
         if (ProtectedPlayerCheck.isProtectedPlayer(mc.objectMouseOver.entityHit)) {
            return;
         }

         Entity entityHit = mc.objectMouseOver.entityHit;
         if (ClientManagers.moduleManager.antiBot.isBot(entityHit)) {
            return;
         }
      }

      if (!flag && mc.objectMouseOver != null) {
         boolean flag2 = ClientManagers.moduleManager.criticals.isToggled()
            || mc.objectMouseOver.entityHit instanceof EntityLivingBase
               && mc.thePlayer.fallDistance > 0.0F
               && !mc.thePlayer.onGround
               && !mc.thePlayer.isOnLadder()
               && !mc.thePlayer.isInWater()
               && !mc.thePlayer.isPotionActive(Potion.blindness)
               && mc.thePlayer.ridingEntity == null;
         if (!this.critLanded && flag2) {
            this.critLanded = true;
         }

         this.pendingLongClickRelease = true;
         LeftClickListener.onLeftClick();
      }

      exitBlock:
      if (ClientManagers.moduleManager.fixes.isToggled()) {
         if (!ClientManagers.moduleManager.fixes.hitDelayFixMode.getSelectedValue().equals("OnlyAtTargetLite") || mc.leftClickCounter >= 9) {
            if (!ClientManagers.moduleManager.fixes.hitDelayFixMode.getSelectedValue().equals("OnlyAtTarget")) {
               break exitBlock;
            }

            boolean flag3 = mc.leftClickCounter < 9;
            if (!ClientManagers.moduleManager.fixes.shouldApplyHitDelayFix(flag3)) {
               break exitBlock;
            }
         }

         if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            byte b = 0;
            mc.leftClickCounter = b;
         }
      }

      if (mc.leftClickCounter > 0
         && ClientManagers.moduleManager.debug.lostHitDebug.getValue()
         && ClientManagers.moduleManager.debug.isToggled()
         && mc.objectMouseOver != null
         && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
         String str = "Lost Hit";
         ClientManagers.moduleManager.debug.sendChatMessage2(str);
      }

      if (mc.leftClickCounter <= 0) {
         if (ViaAugustus.targetProtocolVersion.olderThanOrEqualTo(ProtocolVersion.v1_9) && !this.noSwing.getValue()) {
            mc.thePlayer.swingItem();
         }

         if (mc.objectMouseOver == null) {
            if (mc.playerController.isNotCreative()) {
               byte b2 = 10;
               mc.leftClickCounter = b2;
            }
         } else {
            if (flag) {
               if (ViaAugustus.targetProtocolVersion.newerThan(ProtocolVersion.v1_9) && !this.noSwing.getValue()) {
                  mc.thePlayer.swingItem();
               }

               return;
            }

            switch (MovingObjectTypeSwitchMap.ORDINAL_MAP[mc.objectMouseOver.typeOfHit.ordinal()]) {
               case 1:
                  mc.playerController.attackEntity(mc.thePlayer, mc.objectMouseOver.entityHit);
                  if (this.rangeMode.getSelectedValue().equals("Grim") && !this.grimRangeActive) {
                     this.grimAttackPending = true;
                  }

                  if (this.fixBeta.getValue() && this.betaFixHitCount > 19 && !this.noSwing.getValue()) {
                     mc.thePlayer.swingItem();
                     this.betaFixHitCount = 0;
                  }

                  this.betaFixHitCount++;
                  break;
               case 2:
                  BlockPos blockPos = mc.objectMouseOver.getBlockPos();
                  if (mc.theWorld.getBlockState(blockPos).getBlock().getMaterial() != Material.air) {
                     mc.playerController.clickBlock(blockPos, mc.objectMouseOver.sideHit);
                     break;
                  }
               case 3:
               default:
                  if (mc.playerController.isNotCreative()) {
                     byte b3 = 10;
                     mc.leftClickCounter = b3;
                  }

                  mc.thePlayer.resetTicksSinceLastSwing();
                  if (ClientManagers.moduleManager.fixes.isToggled() && ClientManagers.moduleManager.fixes.hitDelayFixMode.getSelectedValue().equals("Default")) {
                     byte b4 = 0;
                     mc.leftClickCounter = b4;
                  }
            }

            if (ViaAugustus.targetProtocolVersion.newerThan(ProtocolVersion.v1_9) && !this.noSwing.getValue()) {
               mc.thePlayer.swingItem();
            }
         }
      }
   }

   private void updateRayTraceTarget(float f) {
      Entity renderViewEntity = mc.getRenderViewEntity();
      if (renderViewEntity != null && mc.theWorld != null) {
         Object obj = null;
         mc.pointedEntity = (Entity)obj;
         double blockReachDistance = (double)mc.playerController.getBlockReachDistance();
         blockReachDistance = Math.max(blockReachDistance, this.currentRange);
         if (this.throughWallsMode.getSelectedValue().equals("NoneFullBlocks")) {
            FullBlockRaytraceFilter.INSTANCE.enable();
         }

         Minecraft mc2 = mc;
         MovingObjectPosition hit = renderViewEntity.rayTrace(blockReachDistance, f);
         mc2.objectMouseOver = hit;
         FullBlockRaytraceFilter.INSTANCE.disable();
         double d = blockReachDistance;
         Vec3 positionEyes = renderViewEntity.getPositionEyes(f);
         boolean flag = false;
         if (blockReachDistance > 3.0) {
            flag = true;
         }

         if (mc.objectMouseOver != null) {
            d = mc.objectMouseOver.hitVec.distanceTo(positionEyes);
            if (this.throughWallsMode.getSelectedValue().equals("AllBlocks")) {
               d = Math.max(d, this.throughWallsRange.getValue());
            }
         }

         if (ClientManagers.moduleManager.fixes.isIntaveFixEnabled()) {
            blockReachDistance = 6.0;
         }

         blockReachDistance = Math.max(blockReachDistance, this.currentRange);
         Vec3 look = renderViewEntity.getLook(f);
         Vec3 vec = positionEyes.addVector(look.xCoord * blockReachDistance, look.yCoord * blockReachDistance, look.zCoord * blockReachDistance);
         mc.entityRenderer.pointedEntity = null;
         Vec3 vec2 = null;
         float f2 = 1.0F;
         List list = mc.theWorld
            .getEntitiesInAABBexcluding(
               renderViewEntity,
               renderViewEntity.getEntityBoundingBox()
                  .addCoord(look.xCoord * blockReachDistance, look.yCoord * blockReachDistance, look.zCoord * blockReachDistance)
                  .expand((double)f2, (double)f2, (double)f2),
               Predicates.and(EntitySelectors.NOT_SPECTATING, new EntityCollidablePredicate(mc.entityRenderer))
            );
         double d2 = d;
         if (this.activeAttackMode.equals("Packet")) {
            float collisionBorderSize = this.target.getCollisionBorderSize();
            AxisAlignedBB entityBoundingBox = this.target.getEntityBoundingBox();
            if (ClientManagers.moduleManager.hitBox.isToggled()) {
               EntityLivingBase target2 = this.target;
               entityBoundingBox = ClientManagers.moduleManager.hitBox.getExpandedBoundingBox(target2, collisionBorderSize);
            } else {
               entityBoundingBox = entityBoundingBox.expand((double)collisionBorderSize, (double)collisionBorderSize, (double)collisionBorderSize);
            }

            MovingObjectPosition hit2 = entityBoundingBox.calculateIntercept(positionEyes, vec);
            if (entityBoundingBox.isVecInside(positionEyes)) {
               if (d >= 0.0) {
                  mc.entityRenderer.pointedEntity = this.target;
                  vec2 = hit2 == null ? positionEyes : hit2.hitVec;
                  d2 = 0.0;
               }
            } else if (hit2 != null) {
               double d3 = positionEyes.distanceTo(hit2.hitVec);
               if (d3 < d || d == 0.0) {
                  boolean flag2 = false;
                  if (this.target != renderViewEntity.ridingEntity || flag2) {
                     mc.entityRenderer.pointedEntity = this.target;
                     vec2 = hit2.hitVec;
                     d2 = d3;
                  } else if (d == 0.0) {
                     mc.entityRenderer.pointedEntity = this.target;
                     vec2 = hit2.hitVec;
                  }
               }
            }
         } else {
            for (int i = 0; i < list.size(); i++) {
               Entity entity = (Entity)list.get(i);
               float collisionBorderSize2 = entity.getCollisionBorderSize();
               AxisAlignedBB entityBoundingBox2 = entity.getEntityBoundingBox();
               if (ClientManagers.moduleManager.hitBox.isToggled()) {
                  entityBoundingBox2 = ClientManagers.moduleManager.hitBox.getExpandedBoundingBox(entity, collisionBorderSize2);
               } else {
                  entityBoundingBox2 = entityBoundingBox2.expand((double)collisionBorderSize2, (double)collisionBorderSize2, (double)collisionBorderSize2);
               }

               MovingObjectPosition hit3 = entityBoundingBox2.calculateIntercept(positionEyes, vec);
               if (entityBoundingBox2.isVecInside(positionEyes)) {
                  if (d2 >= 0.0) {
                     mc.entityRenderer.pointedEntity = entity;
                     vec2 = hit3 == null ? positionEyes : hit3.hitVec;
                     d2 = 0.0;
                  }
               } else if (hit3 != null) {
                  double d4 = positionEyes.distanceTo(hit3.hitVec);
                  if (d4 < d2 || d2 == 0.0) {
                     boolean flag3 = false;
                     if (entity != renderViewEntity.ridingEntity || flag3) {
                        mc.entityRenderer.pointedEntity = entity;
                        vec2 = hit3.hitVec;
                        d2 = d4;
                     } else if (d2 == 0.0) {
                        mc.entityRenderer.pointedEntity = entity;
                        vec2 = hit3.hitVec;
                     }
                  }
               }
            }
         }

         if (mc.entityRenderer.pointedEntity != null && flag && positionEyes.distanceTo(vec2) > this.currentRange) {
            mc.entityRenderer.pointedEntity = null;
            mc2 = mc;
            MovingObjectPosition hit4 = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec2, (EnumFacing)null, new BlockPos(vec2));
            mc2.objectMouseOver = hit4;
         }

         if (mc.entityRenderer.pointedEntity != null && (d2 < d || mc.objectMouseOver == null)) {
            mc2 = mc;
            MovingObjectPosition hit5 = new MovingObjectPosition(mc.entityRenderer.pointedEntity, vec2);
            mc2.objectMouseOver = hit5;
            if (mc.entityRenderer.pointedEntity instanceof EntityLivingBase || mc.entityRenderer.pointedEntity instanceof EntityItemFrame) {
               Entity pointedEntity = mc.entityRenderer.pointedEntity;
               mc.pointedEntity = pointedEntity;
            }
         }
      }
   }

   private boolean canOperate() {
      return (this.inInv.getValue() || mc.isNotInGui())
         && (!ClientManagers.moduleManager.blink.isToggled() || !ClientManagers.moduleManager.blink.disableKillAura.getValue())
         && (!ClientManagers.moduleManager.fly.isToggled() || !ClientManagers.moduleManager.fly.disableAura.getValue());
   }

   public boolean isEntityReachableThroughWalls(Entity entity) {
      return !mc.thePlayer.canEntityBeSeen(entity) && !this.canAimThroughWalls() && !this.smartAim.getValue()
         ? false
         : mc.thePlayer.canEntityBeSeen(entity) || !this.canAimThroughWalls() || !((double)mc.thePlayer.getDistanceToEntityBox(entity) > this.aimThroughWallsRangeTracker.getCurrentThreshold());
   }

   private boolean canAimThroughWalls() {
      return this.aimThroughWalls.getValue() || this.throughWallsMode.getSelectedValue().equals("AllBlocks");
   }

   private boolean hasRotationControl() {
      return this.target != null && ClientManagers.rotationController.getCurrentPriority() >= this.getRotationPriority();
   }

   private boolean isTrailPointExpired(double d) {
      return Math.abs(d - (double)System.currentTimeMillis()) > ClientManagers.moduleManager.line.auraLineTime.getValue();
   }

   public void onOutgoingCombatPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof C0APacketAnimation && this.actionToTrackInterval.getSelectedValue().equals("Swings")
         || packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)packet).getAction() == C02PacketUseEntity.Action.ATTACK && this.actionToTrackInterval.getSelectedValue().equals("Attacks")) {
         this.intervalActionCount++;
      }

      if (this.intave13.getValue()) {
         if (packet instanceof C03PacketPlayer.C06PacketPlayerPosLook || packet instanceof C03PacketPlayer.C05PacketPlayerLook) {
            float rotationYaw = mc.thePlayer.rotationYaw;
            float rotationPitch = mc.thePlayer.rotationPitch;
            float yaw = this.getEffectiveRotation().yaw;
            float pitch = this.getEffectiveRotation().pitch;
            double d = (double)Math.abs(yaw - rotationYaw);
            double d2 = (double)Math.abs(pitch - rotationPitch);
            double d3 = Math.abs(d) + Math.abs(d2);
            long time = this.lastSwingPacketTimer.getTime();
            if (d3 > 8.0 && time < 4000L) {
               this.intaveRotationFlags++;
               if (d3 > 30.0 && time < 250L) {
                  this.intaveRotationFlags++;
                  if (d3 > 60.0) {
                     this.intaveRotationFlags++;
                  }
               }
            } else if (this.intaveRotationFlags > 0) {
               if (!(d3 < 3.0) && time <= 5000L) {
                  this.intaveRotationFlags--;
               } else {
                  this.intaveRotationFlags = 0;
               }
            }
         }

         if (packet instanceof C0APacketAnimation) {
            this.lastSwingPacketTimer.reset();
         }
      }
   }

   public Rotation getEffectiveRotation() {
      return this.noRotation.getValue() ? this.currentRotation : ClientManagers.rotationController.getPreviousRotation();
   }

   public void renderRotationLineTrail(ShaderRenderPass shaderRenderPass) {
      RenderUtil.begin3DRender();
      Vec3 lookCustom2 = mc.thePlayer.getLookCustom2(1.0F, this.currentRotation.yaw, this.currentRotation.pitch);
      Vec3 vec = mc.thePlayer.getPositionEyes(1.0F).addVector(lookCustom2.xCoord * 3.0, lookCustom2.yCoord * 3.0, lookCustom2.zCoord * 3.0);
      float xCoord = (float)vec.xCoord;
      float yCoord = (float)vec.yCoord;
      float zCoord = (float)vec.zCoord;
      this.rotationTrailPoints.add(new double[]{(double)xCoord, (double)yCoord, (double)zCoord, (double)System.currentTimeMillis()});

      for (int i = 0; i < this.rotationTrailPoints.size(); i++) {
         double d = ((double[])this.rotationTrailPoints.get(i))[3];
         if (this.isTrailPointExpired(d)) {
            this.rotationTrailPoints.remove(i);
            i--;
         }
      }

      Line line = ClientManagers.moduleManager.line;
      BlurShadowSettings blurShadowSettings = ClientManagers.moduleManager.line.blurShadowSettings;
      byte b = 0;
      int colorInt = line.color2.getColorInt(b);
      RenderUtil.setGlColor(blurShadowSettings.resolvePassColor(shaderRenderPass, colorInt));
      GL11.glLineWidth((float)line.auraLineWidth.getValue());
      Tessellator instance = Tessellator.getInstance();
      WorldRenderer worldRenderer = instance.getWorldRenderer();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      worldRenderer.begin(3, DefaultVertexFormats.POSITION);
      double[] doubles = new double[]{-2.0, -1.0, -1.0};
      double[] doubles2 = new double[]{-2.0, -1.0, -1.0};

      for (double[] doubles3 : (Iterable<double[]>)(this.rotationTrailPoints)) {
         if (!Arrays.equals(doubles, doubles2) && !Arrays.equals(doubles, doubles3)) {
            worldRenderer.pos(
                  (double)((float)doubles3[0]) - mc.getRenderManager().getRenderPosX(),
                  (double)((float)doubles3[1]) - mc.getRenderManager().getRenderPosY(),
                  (double)((float)doubles3[2]) - mc.getRenderManager().getRenderPosZ()
               )
               .endVertex();
         }

         doubles = doubles3;
      }

      instance.draw();
      RenderUtil.end3DRender();
   }

   public void renderTargetDot(ShaderRenderPass shaderRenderPass, double d) {
      RenderUtil.begin3DRender();
      if (this.dot.getValue() && this.target != null && this.lastObjectMouseOver != null && this.lastObjectMouseOver.hitVec != null && this.previousHitVec != null) {
         Vec3 vec = this.previousHitVec
            .lerp(this.lastObjectMouseOver.hitVec, d)
            .addVector(-mc.getRenderManager().getRenderPosX(), -mc.getRenderManager().getRenderPosY(), -mc.getRenderManager().getRenderPosZ());
         float f = (float)this.dotSize.getValue() / 10.0F;
         BlurShadowSettings dotShadowSettings2 = this.dotShadowSettings;
         byte b = 0;
         int colorInt = this.dotColor.getColorInt(b);
         RenderUtil.setGlColor(dotShadowSettings2.resolvePassColor(shaderRenderPass, colorInt));
         RenderUtil.drawFilledBox(
            vec.xCoord - (double)f,
            vec.yCoord - (double)f,
            vec.zCoord - (double)f,
            vec.xCoord + (double)f,
            vec.yCoord + (double)f,
            vec.zCoord + (double)f
         );
      }

      RenderUtil.end3DRender();
   }

   private void resetClickPattern() {
      AbstractClickPatternMode abstractSubMode = (AbstractClickPatternMode)this.clickMode.getAbstractSubMode();
      if (abstractSubMode instanceof DeviationClickPatternMode) {
         DeviationClickPatternMode mode = (DeviationClickPatternMode)abstractSubMode;
         mode.resetNextClickTime();
      } else if (abstractSubMode instanceof RecordedClickPatternMode) {
         ((RecordedClickPatternMode)abstractSubMode).resetTiming();
      }
   }

   private void clearTargets() {
      this.target = null;
      this.previousTarget = null;
      Object obj = null;
      this.targetSelection.setCurrentTarget((Entity)obj);
      this.lastTickTargets.clear();
   }

   private boolean supportsHoldClicking() {
      return ((AbstractClickPatternMode)this.clickMode.getAbstractSubMode()).simulatesMousePress();
   }

   private boolean isRespawnDelayActive(RandomizedRangeSettings randomizedRangeSettings) {
      if (mc.thePlayer.ticksExisted - 5 >= randomizedRangeSettings.getLastIntValue() && this.ticksSinceTeleport >= randomizedRangeSettings.getLastIntValue()) {
         return false;
      } else {
         this.ticksSinceTeleport++;
         return true;
      }
   }

   private void rerollRespawnDelays() {
      this.delayClicksAfterRespawn.nextValue();
      this.delayRotationsAfterRespawn.nextValue();
   }

   private int getRotationPriority() {
      return 2;
   }

   private boolean isInterpolationEnabled() {
      return this.interpolation.getValue();
   }

   @Override
   public EntityLivingBase getTarget() {
      return this.target;
   }

   @Override
   public void setHoldingAttack(boolean flag) {
      this.holdingLeftClick = flag;
   }

   @Override
   public boolean hasLandedCrit() {
      return this.critLanded;
   }

   @Override
   public boolean isCritFixEnabled() {
      return this.critFix.getValue();
   }

   private void runScheduledLeftClick(boolean flag) {
      this.executeLeftClick(flag);
   }

   private void renderTargetDotPass(float f, ShaderRenderPass shaderRenderPass) {
      double d = (double)f;
      double d2 = d;
      this.renderTargetDot(shaderRenderPass, d2);
   }

   private boolean isDelayClicksAfterRespawnVisible() {
      return !this.inInv.getValue();
   }

   private boolean isDelayRotationsAfterRespawnVisible() {
      return !this.inInv.getValue();
   }

   private boolean isDelayClicksAfterGuiVisible() {
      return !this.inInv.getValue();
   }

   private boolean isDelayRotationsAfterGuiVisible() {
      return !this.inInv.getValue();
   }

   private boolean isIntave13Visible() {
      return !this.noRotation.getValue();
   }

   private boolean isSilentMoveFixVisible() {
      return this.moveFix.getValue() && this.moveFix.isVisible();
   }

   private boolean isMoveFixVisible() {
      return !this.noRotation.getValue();
   }

   private boolean isDoubleClickFirstHitVisible() {
      return !this.clickMode.getSelectedValue().equals("Smart");
   }

   private boolean isPreHitEnduranceVisible() {
      return this.smartPreHit.getValue() && this.smartPreHit.isVisible();
   }

   private boolean isPreHitLoseDelayVisible() {
      return this.smartPreHit.getValue() && this.smartPreHit.isVisible();
   }

   private boolean isPreHitPredictionVisible() {
      return this.smartPreHit.getValue() && this.smartPreHit.isVisible();
   }

   private boolean isUnblockAacVisible() {
      return this.blockMode.getSelectedValue().equals("AAC") || this.blockMode.getSelectedValue().equals("AACOld");
   }

   private boolean isOnlyWhileHurtVisible() {
      return !this.blockMode.getSelectedValue().equals("None");
   }

   private boolean isInteractAutoBlockVisible() {
      return !this.blockMode.getSelectedValue().equals("None");
   }

   private boolean isSmartBlockVisible() {
      return !this.blockMode.getSelectedValue().equals("None");
   }

   private boolean isMaxTargetsVisible() {
      return this.attackMode.getSelectedValue().equals("Multi");
   }

   private boolean isAimThroughWallsRangeVisible() {
      return this.aimThroughWalls.isVisible() && this.aimThroughWalls.getValue();
   }

   private boolean isThroughWallsRangeVisible() {
      return this.throughWallsMode.getSelectedValue().equals("AllBlocks");
   }

   private boolean isAiSettingsGroupVisible() {
      return this.randomizeMode.getSelectedValue().equals("AI");
   }

   private boolean isTurboPlayerResponseVisible() {
      return this.randomizeMode.getSelectedValue().equals("Turbo");
   }

   private boolean isTurboAimSpeedVisible() {
      return this.randomizeMode.getSelectedValue().equals("Turbo");
   }

   private boolean isTrashcodeBypassVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isNoYawOutOfReachVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isMaxAimpointAngleDiffVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isMaxOrthogonalMovementVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isAdditionalInaccuracyVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isRotationPitchOffsetSpeedVisible() {
      if (this.randomizeMode.getSelectedValue().equals("Noise2")) {
         boolean flag = true;
         double maxValue = this.rotationPitchOffset.getMaxValue(flag);
         boolean flag2 = true;
         if (maxValue != this.rotationPitchOffset.getMinValue(flag2)) {
            return true;
         }
      }

      return false;
   }

   private boolean isRotationYawOffsetSpeedVisible() {
      if (this.randomizeMode.getSelectedValue().equals("Noise2")) {
         boolean flag = true;
         double maxValue = this.rotationYawOffset.getMaxValue(flag);
         boolean flag2 = true;
         if (maxValue != this.rotationYawOffset.getMinValue(flag2)) {
            return true;
         }
      }

      return false;
   }

   private boolean isRotationPitchOffsetVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isRotationYawOffsetVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isRotationOffsetGroupVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isSuperCorrectionDelayVisible() {
      if (this.randomizeMode.getSelectedValue().equals("Noise2")) {
         boolean flag = true;
         if (this.supercorrectionProbability.getValue(flag) > 0.0) {
            return true;
         }
      }

      return false;
   }

   private boolean isSuperCorrectionProbabilityVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isSuperCorrectionGroupVisible() {
      if (this.randomizeMode.getSelectedValue().equals("Noise2")) {
         if (this.reactionTime.getMaxValue() > 0.0) {
            return true;
         }
      }

      return false;
   }

   private boolean isPredictionInaccuracySmoothnessVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isPredictionInaccuracyVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isAttentionVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isLookaheadTimeVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isReactionTimeVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isDecelerationPostVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isDecelerationPreVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isNoise2AimSpeedVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise2");
   }

   private boolean isNoiseTestVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise");
   }

   private boolean isAimProbabilityWhenNotNeededVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise");
   }

   private boolean isAdditionalAimSpeedIfNeededVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise");
   }

   private boolean isAdditionalMinAimSpeedVisible() {
      return this.randomizeMode.getSelectedValue().equals("Noise");
   }

   private boolean isYRelDistVisible() {
      return this.randomizeMode.getSelectedValue().equals("Advanced");
   }

   private boolean isXRelDistVisible() {
      return this.randomizeMode.getSelectedValue().equals("Advanced");
   }

   private boolean isMaxYAbsDistVisible() {
      return this.randomizeMode.getSelectedValue().equals("Advanced");
   }

   private boolean isMaxXAbsDistVisible() {
      return this.randomizeMode.getSelectedValue().equals("Advanced");
   }

   private boolean isMinAbsDistVisible() {
      return this.randomizeMode.getSelectedValue().equals("Advanced");
   }

   private boolean isPitchOutliersAddVisible() {
      return this.randomizeMode.getSelectedValue().equals("Advanced") && this.pitchOutliers.getValue();
   }

   private boolean isPitchOutliersVisible() {
      return this.randomizeMode.getSelectedValue().equals("Advanced");
   }

   private boolean isSmartRotateVisible() {
      return this.randomizeMode.getSelectedValue().equals("Advanced");
   }

   private boolean isNearestHitVecHorizontalVisible() {
      return this.rotationMode.getSelectedValue().equalsIgnoreCase("NearestHitVec");
   }

   private boolean isNearestHitVecVerticalVisible() {
      return this.rotationMode.getSelectedValue().equalsIgnoreCase("NearestHitVec");
   }

   private boolean isBestHitVecVerticalVisible() {
      return this.rotationMode.getSelectedValue().equalsIgnoreCase("BestHitVec");
   }

   private boolean isMinDistanceCombinedVisible() {
      return !this.fixRotationMode.getSelectedValue().equals("None") && this.minDistanceToBestVecCalculation.getSelectedValue().equalsIgnoreCase("Combined");
   }

   private boolean isMinDistancePitchVisible() {
      return !this.fixRotationMode.getSelectedValue().equals("None") && this.minDistanceToBestVecCalculation.getSelectedValue().equalsIgnoreCase("Separate");
   }

   private boolean isMinDistanceYawVisible() {
      return !this.fixRotationMode.getSelectedValue().equals("None") && this.minDistanceToBestVecCalculation.getSelectedValue().equalsIgnoreCase("Separate");
   }

   private boolean isMinDistanceCalculationVisible() {
      return !this.fixRotationMode.getSelectedValue().equals("None");
   }

   private boolean isMinFixRotationAngleVisible() {
      return !this.fixRotationMode.getSelectedValue().equals("None");
   }

   private boolean isMaxFixRotationReachVisible() {
      return !this.fixRotationMode.getSelectedValue().equals("None");
   }

   private boolean isFixRotationChanceVisible() {
      return !this.fixRotationMode.getSelectedValue().equals("None");
   }

   private boolean isPitchAccelerationVisible() {
      return !this.isInterpolationEnabled()
         && !this.randomizeMode.getSelectedValue().contains("Noise")
         && !this.randomizeMode.getSelectedValue().equals("Turbo")
         && !this.randomizeMode.getSelectedValue().equals("AI");
   }

   private boolean isYawAccelerationVisible() {
      return !this.isInterpolationEnabled()
         && !this.randomizeMode.getSelectedValue().contains("Noise")
         && !this.randomizeMode.getSelectedValue().equals("Turbo")
         && !this.randomizeMode.getSelectedValue().equals("AI");
   }

   private boolean isYawSpeedVisible() {
      return !this.randomizeMode.getSelectedValue().contains("Noise") && !this.randomizeMode.getSelectedValue().equals("Turbo") && !this.randomizeMode.getSelectedValue().equals("AI");
   }

   private boolean isHurtTimeToHitWithReachVisible() {
      return this.rangeMode.getSelectedValue().equals("IntervalRange");
   }

   private boolean isMinReachToIntervalHitVisible() {
      return this.rangeMode.getSelectedValue().equals("IntervalRange");
   }

   private boolean isIntervalVisible() {
      return this.rangeMode.getSelectedValue().equals("IntervalRange");
   }

   private boolean isActionToTrackIntervalVisible() {
      return this.rangeMode.getSelectedValue().equals("IntervalRange");
   }

   private boolean isHazeMaxVisible() {
      return this.rangeMode.getSelectedValue().equals("HazeRange");
   }




}
