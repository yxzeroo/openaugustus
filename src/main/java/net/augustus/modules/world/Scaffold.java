package net.augustus.modules.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.modules.modes.anticheat.intave14.scaffold.Intave14TimeBlockPlaceCheck;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.events.EventOrientCamera;
import net.augustus.events.listeners.SprintListener;
import net.augustus.events.EventMove;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.MotionListener;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.modules.world.scaffold.BlockPlacementData;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.events.EventClick;
import net.augustus.utils.input.MouseInputUtil;
import net.augustus.modules.world.scaffold.ScaffoldFacingSwitchMap;
import net.augustus.settings.RangeSetting;
import net.augustus.settings.ColorSetting;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.rotation.MoveFixMode;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.rotation.RayCastUtil;
import net.augustus.events.EventSafeWalk;
import net.augustus.events.listeners.MoveListener;
import net.augustus.events.listeners.SafeWalkListener;
import net.augustus.utils.TuplePair;
import net.augustus.utils.rotation.RotationKinematicState;
import net.augustus.utils.rotation.RotationController;
import net.augustus.events.listeners.MoveEntityListener;
import net.augustus.utils.world.BlockUtil;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.utils.rotation.BezierCurve2D;
import net.augustus.events.listeners.AIMoveSpeedListener;
import net.augustus.utils.math.MathStatisticsUtil;
import net.augustus.events.EventSwing;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.utils.rotation.EntityBoundsUtil;
import net.augustus.utils.combat.CombatTargetUtil;
import net.augustus.events.EventMoveInput;
import net.augustus.render.RenderUtil;
import net.augustus.utils.TickCounter;
import net.augustus.events.EventAIMoveSpeed;
import net.augustus.utils.render.ColorUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.events.listeners.OrientCameraListener;
import net.augustus.Augustus;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.clickgui.ClickGuiScreen;
import net.augustus.events.EventUpdate;
import net.augustus.events.listeners.TickListener;
import net.augustus.modules.world.scaffold.ScaffoldRaycastResult;
import net.augustus.events.EventMoveEntity;
import net.augustus.utils.AntiSamplerGuard;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.events.listeners.RayCastListener;
import net.augustus.events.EventSprint;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.events.EventMotion;
import net.augustus.events.listeners.SwingListener;
import net.augustus.utils.rotation.BezierRotationStepper;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class Scaffold
   extends Module
   implements MoveListener,
   SprintListener,
   ClickListener,
   TickListener,
   OrientCameraListener,
   MoveEntityListener,
   MoveInputListener,
   MotionListener,
   RenderListener,
   RayCastListener,
   PacketListener,
   SafeWalkListener,
   SwingListener,
   SilentSlotListener,
   AIMoveSpeedListener,
   UpdateListener {
   private final DoubleValue rotations;
   private final DoubleValue rotationSpeed;
   private final BooleanValue modeNew;
   private final StringValue aimSpeed;
   private final RandomizedRangeSettings yawSpeed;
   private final RandomizedRangeSettings pitchSpeed;
   private final RandomizedRangeSettings yawAcceleration;
   private final RandomizedRangeSettings pitchAcceleration;
   private final BooleanValue customBlockClutchRotationSpeeds;
   private final DoubleValue blockClutchRotationSpeed;
   private final BooleanValue blockClutchModeNew;
   private final StringValue blockClutchAimSpeed;
   private final RandomizedRangeSettings blockClutchYawSpeed;
   private final RandomizedRangeSettings blockClutchPitchSpeed;
   private final RandomizedRangeSettings blockClutchYawAcceleration;
   private final RandomizedRangeSettings blockClutchPitchAcceleration;
   private final RandomizedRangeSettings keepRotationsAfterModuleDisable;
   private final DoubleValue rotationRandomization;
   private final BooleanValue randomizePitch;
   private final StringValue randomizePitchChanceWhileYawRotation;
   private final StringValue randomizePitchChanceOnBlock;
   private final StringValue randomizePitchChanceInAir;
   private final BooleanValue otherPitchRandomization;
   private final RandomizedRangeSettings randomizePitchVal;
   private final RandomizedRangeSettings randomizePitchInterval;
   private final BooleanValue randomizeYaw;
   private final StringValue randomizeYawChanceWhilePitchRotation;
   private final StringValue randomizeYawChanceOnBlock;
   private final StringValue randomizeYawChanceOnAir;
   private final BooleanValue otherYawRandomization;
   private final RandomizedRangeSettings randomizeYawVal;
   private final RandomizedRangeSettings randomizeYawInterval;
   private final DoubleValue staticPitchSettings;
   private final BooleanValue staticPitchBool;
   private final BooleanValue staticPitchAtJump;
   private final BooleanValue staticPitchRandomizeOnlyAtStart;
   private final BooleanValue ignoreStaticPitchForBlockClutches;
   private final RandomizedRangeSettings staticPitch;
   private final BooleanValue staticPitchBoolDiagonal;
   private final BooleanValue staticPitchAtJumpDiagonal;
   private final BooleanValue staticPitchRandomizeOnlyAtStartDiagonal;
   private final BooleanValue ignoreStaticPitchForBlockClutchesDiagonal;
   private final RandomizedRangeSettings staticPitchDiagonal;
   private final BooleanValue minimalMouseMovement;
   private final BooleanValue reduceYawRotation;
   private final BooleanValue strictYaw;
   private final RandomizedRangeSettings strictYawIncorrectFactor;
   private final RandomizedRangeSettings startDelayAfterDirectionChange;
   private final BooleanValue ignoreStartDelayAfterDirectionChangeAtBlockClutch;
   private final BooleanValue changeDirectionOnlyIfBlockUnderYou;
   private final BooleanValue rotateWithMovement;
   public final BooleanValue rotateWithMovementOnlyOrthogonal;
   public final BooleanValue improve;
   public final BooleanValue improveOnlyAtClutch;
   public final BooleanValue extraBlockSearch;
   public final BooleanValue extraBlockSearchOnlyAtClutch;
   public final DoubleValue strafeSettings;
   public final BooleanValue strafe;
   public final BooleanValue strafeOnlyWhileJump;
   public final RandomizedRangeSettings strafeDegree;
   public final RandomizedRangeSettings strafeWidth;
   public final BooleanValue correctRotationWhileBuilding;
   public final BooleanValue correctStrafeWithKeyBinds;
   private final BooleanValue correctStrafeWithKeyBindsCage;
   private final RandomizedRangeSettings correctStrafeWithKeyBindsWidth;
   private final RandomizedRangeSettings correctStrafeWithKeyBindsDuration;
   public final RandomizedRangeSettings correctStrafeWithKeyBindsMiddle;
   public final RangeSetting correctStrafeWithKeyBindsMiddleExclusion;
   public final RandomizedRangeSettings correctStrafeWithKeyBindsDelayToRecalculateMiddle;
   public final BooleanValue startAtEdge;
   public final RandomizedRangeSettings startAtEdgeDelay;
   public final BooleanValue ignoreStartAtEdgeAtBlockClutch;
   public final BooleanValue edgeRotateCheck;
   public final BooleanValue edgePlaceCheck;
   private final BooleansSetting flickMode;
   private final BooleanValue flickUserStrafeSettingForForwardLooking;
   public final BooleanValue otherRotation;
   public final BooleanValue recycleRotation;
   public final BooleanValue intave;
   public final BooleanValue intaveSafe;
   public final BooleanValue intaveFast;
   public final BooleansSetting pitchSelection;
   public final StringValue maxPitchCorrection;
   public final StringValue maxPitchCorrectionAtBlockClutch;
   public final BooleanValue expand;
   public final StringValue expandLength;
   public final DoubleValue clicking;
   public final BooleanValue noSwing;
   public final BooleanValue serverSideNoSwing;
   public final RandomizedRangeSettings cps;
   private final BooleansSetting dcpsMode;
   public final RandomizedRangeSettings dcps;
   public final BooleanValue forcePlaceIfYouWouldFallDown;
   public final BooleanValue alwaysPlaceWhenPossible;
   private final BooleanValue clickPause;
   private final BooleanValue delayClick;
   public final BooleanValue oneClick;
   private final BooleanValue stopClickingAtSneaking;
   public final StringValue maxYawRotationDeltaToPlaceBlocks;
   public final StringValue maxPitchRotationDeltaToPlaceBlocks;
   public final StringValue maxYawRotationDeltaToPlaceBlocksAtBlockClutch;
   public final StringValue maxPitchRotationDeltaToPlaceBlocksAtBlockClutch;
   public final DoubleValue movement;
   public final BooleanValue removeSneakStop;
   public final DoubleValue sneaking;
   public final BooleanValue sneak;
   public final BooleanValue timeJumpForOneStackForward;
   public final BooleanValue oneStackForward;
   public final RangeSetting minOffGroundSneakTickForward;
   public final RangeSetting maxOffGroundSneakTickForward;
   public final BooleanValue oneStackDiagonal;
   public final RangeSetting minOffGroundSneakTickDiagonal;
   public final RangeSetting maxOffGroundSneakTickDiagonal;
   public final RandomizedRangeSettings sneakTicks;
   public final RangeSetting startSneakDistToBlock;
   public final RangeSetting sneakBlocksForward;
   public final RangeSetting sneakBlocksDiagonal;
   public final StringValue sneakSlowDown;
   public final BooleanValue earlySneakCalculation;
   public final BooleanValue earlySneakCalculationOnlyAtSpeedEffect;
   public final BooleansSetting adhsMode;
   public BooleansSetting moveFix;
   public final BooleanValue moveFixIntave;
   public final BooleansSetting mode;
   public final BooleanValue jumpSprint;
   public final BooleanValue hover;
   public final BooleanValue stopAtStart;
   public final BooleanValue safeWalkNoBlocks;
   public final BooleanValue sneakNoBlocks;
   public final BooleanValue stopNoBlocks;
   public final BooleanValue reduceVelocity;
   public final BooleanValue ignoreSpeedMotion;
   private final BooleanValue sneakAtRotation;
   private final BooleanValue stopAtRotation;
   public final DoubleValue godBridging;
   public final BooleanValue godBridge;
   public final BooleanValue disableGodbridgeSafetyFeature;
   public final RandomizedRangeSettings resetGodBridgeAfterXJumps;
   public final RandomizedRangeSettings jumpAfterXBlocks;
   public final RandomizedRangeSettings jumpDelayAfterPlace;
   public final RandomizedRangeSettings startDelay;
   public final DoubleValue moonWalking;
   public final BooleanValue moonWalk;
   public final BooleanValue onlyOnGround;
   public final RandomizedRangeSettings moonWalkWideness;
   public final RangeSetting moonWalkStartDistToBlock;
   public final RandomizedRangeSettings moonWalkMiddle;
   public final RandomizedRangeSettings delayToRecalculateMiddle;
   public final RandomizedRangeSettings postPlacementStrafeTicks;
   public final RandomizedRangeSettings minMoonwalkTicks;
   public final RandomizedRangeSettings directionChangeDelay;
   public final DoubleValue sameYBlockGroup;
   public final BooleanValue sameYBlock;
   public final BooleanValue stopMovementAfterJump;
   public final DoubleValue sameYGroup;
   public final BooleanValue sameY;
   public final BooleanValue autoJump;
   public final BooleanValue intaveFast2;
   public final BooleanValue tellyBridge;
   public final DoubleValue safeWalkGroup;
   public final BooleanValue safeWalk;
   public final BooleanValue safeWalkAir;
   public final BooleanValue safeWalkOnlyDiagonal;
   public final DoubleValue jumpGroup;
   public final BooleanValue jump;
   public final BooleanValue jumpOnlyWhileSpacePressed;
   public final RandomizedRangeSettings jumpAfterXBlocksForward;
   public final RandomizedRangeSettings jumpAfterXBlocksDiagonal;
   public final DoubleValue timeGroup;
   public final StringValue timer;
   public final BooleanValue timerDrop;
   public final RangeSetting timerSpeed;
   public final DoubleValue motionMultiplier;
   public final RandomizedRangeSettings motionMultiplierAirForward;
   public final RandomizedRangeSettings motionMultiplierAirStrafe;
   public final RandomizedRangeSettings motionMultiplierGroundForward;
   public final RandomizedRangeSettings motionMultiplierGroundStrafe;
   public final DoubleValue miscellaneous;
   public final BooleansSetting towerMode;
   public final BooleanValue towerWhileMoving;
   public final StringValue constantMotion;
   public final StringValue jumpMotion;
   public final BooleansSetting silentMode;
   public final BooleanValue useBiggestStackSilent;
   public final RandomizedRangeSettings silentStartDelay;
   public final RandomizedRangeSettings silentSwitchDelay;
   public final RandomizedRangeSettings stackCountToStartSwitchProcess;
   public final RandomizedRangeSettings backSwitchTickDelay;
   public final BooleanValue dynamicBackSwitchTickDelay;
   public final DoubleValue visuals;
   private final BooleanValue thirdPersonMode;
   public final BooleanValue hideSneakAnimation;
   public final BooleanValue blockCount;
   private final StringValue x;
   private final StringValue y;
   private final StringValue scale;
   private final BooleanValue backGround;
   private final ColorSetting backgroundColor;
   private final StringValue roundFactor;
   public final BlurShadowSettings blockCount2;
   public final BooleanValue esp;
   public final ColorSetting espColor;
   public final BlurShadowSettings esp2;
   private BlockPos lastPlacedBlockPos;
   private BlockPos targetBlockPos;
   private int tellyPredictedBlockDistance;
   private BlockPlacementData blockPlacementData;
   private MovingObjectPosition overrideRayTrace;
   private final RotationUtil rotationUtil;
   private final BezierRotationStepper bezierRotationStepper;
   private BezierCurve2D bezierCurve;
   private RotationKinematicState rotationKinematicState;
   private Rotation lastBezierRotation;
   private int bezierTickCounter;
   private final TimeHelper lastRightClickTimer;
   private final TimeHelper lastPlaceTimer;
   private final TimeHelper enableTimer;
   private int sneakHoldTicks;
   private int blocksSinceLastSneak;
   private int placedBlocksSinceSneak;
   private double sneakEdgeExpandDist;
   private double ncpTowerStartY;
   private int offGroundTicks;
   private boolean wasSneakingLastTick;
   private boolean isOverSupportBlock;
   private int sneakIntervalBlocksForward;
   private int sneakIntervalBlocksDiagonal;
   private final Intave14TimeBlockPlaceCheck intave14TimeBlockPlaceCheck;
   private float strafeYawOffset;
   private int hotbarBlockCount;
   private final BridgeClickController bridgeClickController;
   private int moonWalkStrafeDir;
   private int keyBindStrafeCorrectionDir;
   private double moonWalkEdgeExpandDist;
   private float lastTargetYaw;
   private int ticksSinceLastPlace;
   private int moonWalkTicks;
   private int moonWalkDirChangeTicks;
   private int lastAppliedStrafeInput;
   private boolean wasOverMoonWalkBlock;
   private int minOffGroundSneakForward;
   private int maxOffGroundSneakForward;
   private int minOffGroundSneakDiagonal;
   private int maxOffGroundSneakDiagonal;
   private double expandAirYOffset;
   private boolean godBridgeResetting;
   private int godBridgePlacedBlocks;
   private int godBridgeSetupTicks;
   private int godBridgeJumpCount;
   private int ticksSinceGodBridgePlace;
   private int godBridgeSneakSetupTicks;
   private double lastGodBridgePosX;
   private double lastGodBridgePosZ;
   private boolean wasCardinalDirection;
   private boolean safeWalkEdgeDetected;
   private boolean godBridgeJumpHeld;
   private int blocksPlacedSinceJump;
   private boolean yawRandomizationTriggered;
   private boolean pitchRandomizationTriggered;
   private final TickCounter yawRandomizeCounter;
   private final TickCounter pitchRandomizeCounter;
   private boolean rightClickHeldOnGround;
   private Vec3 velocityAnchorPos;
   private int watchdogTowerTick;
   private boolean hoverJumpStarted;
   private boolean hoverOffsetActive;
   private final TickCounter strafeCorrectionCounter;
   private final TickCounter strafeMiddleRecalcCounter;
   private BlockPos previousTargetBlockPos;
   private int strictYawErrorSign;
   private int lastStrictYawSector;
   private boolean strafeCorrectionActive;
   private final TickCounter silentSwitchCounter;
   private boolean firstSilentSwitchPending;
   private int selectedBlockSlot;
   private final HashMap slotMinStackThresholds;
   private int stopAtStartDelayMs;
   private int lastMoonWalkSide;
   private double strafeCorrectionMiddle;
   private final TickCounter moonWalkMiddleRecalcCounter;
   private final TickCounter directionChangeDelayCounter;
   private boolean directionChangePending;
   private boolean reducingVelocity;
   private boolean waitingForStartEdge;
   private boolean hasPlacedFirstBlock;
   private int ticksSinceFirstPlace;
   private int startEdgeSneakTicks;
   private final TickCounter startAtEdgeDelayCounter;
   private boolean initialStrafeDirectionPending;
   private int savedThirdPersonView;
   private int ticksSinceDirectionChange;
   private int postDirectionChangeSneakTicks;
   private boolean keepInitialPitch;
   private float cachedStrictRotationYaw;
   private float cachedStrictMoveYaw;
   private boolean sneakingOnGroundForEdge;
   private boolean placedBlockSinceEnable;
   private float minimalMouseYawOffset;
   private float minimalMousePitchOffset;
   private TimeHelper minimalMouseMoveTimer;
   private TickCounter minimalMouseMoveCounter;
   private int minimalMouseMoveIntervalMs;
   private int minimalMouseMoveDurationTicks;
   private boolean sameYBlockJumpActive;
   private double sameYBlockBaseY;
   private boolean sameYBlockResetPending;
   private boolean sameYBlockTopPlaced;
   private boolean stopMovementUntilLand;
   private double intaveBlockPlacePenalty;

   public Scaffold() {
      super("Scaffold", "Gerüst", new Color(182, 194, 207), Categorys.WORLD);
      this.rotations = new DoubleValue(this, "Rotations", true);
      this.rotationSpeed = new DoubleValue(this, "RotationSpeed", true, this.rotations);
      BooleanValue booleanValue = new BooleanValue(this, "ModeNew", false, this.rotationSpeed);
      SettingVisibilityCondition condition = () -> this.isModeNewVisible();
      this.modeNew = (BooleanValue)booleanValue.withVisibility(condition);
      StringValue stringValue = new StringValue(this, "AimSpeed", 50.0, 0.0, 100.0, 0, this.rotationSpeed);
      SettingVisibilityCondition condition2 = () -> this.isAimSpeedVisible();
      this.aimSpeed = stringValue.visibleWhen(condition2);
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "YawSpeed", 30.0, 50.0, 0.0, 180.0, 1, this.rotationSpeed)).withoutNotInARow();
      SettingVisibilityCondition condition3 = () -> {
         return !this.modeNew.isVisible() || !this.modeNew.getValue();
      };
      this.yawSpeed = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition3);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this, "PitchSpeed", 30.0, 50.0, 0.0, 180.0, 1, this.rotationSpeed)).withoutNotInARow();
      SettingVisibilityCondition condition4 = () -> this.isPitchSpeedVisible();
      this.pitchSpeed = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition4);
      RandomizedRangeSettings randomizedRangeSettings3 = (new RandomizedRangeSettings(this, "YawAcceleration", 7.0, 15.0, 1.0, 180.0, 1, this.rotationSpeed)).withoutNotInARow();
      SettingVisibilityCondition condition5 = () -> {
         return !this.modeNew.isVisible() || !this.modeNew.getValue();
      };
      this.yawAcceleration = (RandomizedRangeSettings)randomizedRangeSettings3.withVisibility(condition5);
      RandomizedRangeSettings randomizedRangeSettings4 = (new RandomizedRangeSettings(this, "PitchAcceleration", 7.0, 15.0, 1.0, 180.0, 1, this.rotationSpeed)).withoutNotInARow();
      SettingVisibilityCondition condition6 = () -> this.isPitchAccelerationVisible();
      this.pitchAcceleration = (RandomizedRangeSettings)randomizedRangeSettings4.withVisibility(condition6);
      this.customBlockClutchRotationSpeeds = new BooleanValue(this, "CustomBlockClutchRotationSpeeds", false, this.rotationSpeed);
      DoubleValue doubleValue = new DoubleValue(this, "BlockClutchRotationSpeed", true, this.rotationSpeed);
      BooleanValue booleanValue2 = this.customBlockClutchRotationSpeeds;
      this.customBlockClutchRotationSpeeds.getClass();
      BooleanValue booleanValue3 = booleanValue2;
      SettingVisibilityCondition condition7 = () -> booleanValue3.getValue();
      this.blockClutchRotationSpeed = doubleValue.visibleWhen(condition7);
      BooleanValue booleanValue4 = new BooleanValue(this, "BlockClutchModeNew", false, this.blockClutchRotationSpeed);
      String str = "ModeNew";
      BooleanValue booleanValue5 = (BooleanValue)booleanValue4.withDisplayName(str);
      SettingVisibilityCondition condition8 = () -> this.isBlockClutchModeNewVisible();
      this.blockClutchModeNew = (BooleanValue)booleanValue5.withVisibility(condition8);
      StringValue stringValue2 = new StringValue(this, "BlockClutchAimSpeed", 50.0, 0.0, 100.0, 0, this.blockClutchRotationSpeed);
      String str2 = "AimSpeed";
      StringValue stringValue3 = stringValue2.named(str2);
      SettingVisibilityCondition condition9 = () -> this.isBlockClutchAimSpeedVisible();
      this.blockClutchAimSpeed = stringValue3.visibleWhen(condition9);
      RandomizedRangeSettings randomizedRangeSettings5 = (new RandomizedRangeSettings(this, "BlockClutchYawSpeed", 30.0, 50.0, 0.0, 180.0, 1, this.blockClutchRotationSpeed)).withoutNotInARow();
      String str3 = "YawSpeed";
      RandomizedRangeSettings randomizedRangeSettings6 = (RandomizedRangeSettings)randomizedRangeSettings5.withDisplayNamePrefix(str3);
      SettingVisibilityCondition condition10 = () -> {
         return !this.blockClutchModeNew.isVisible() || !this.blockClutchModeNew.getValue();
      };
      this.blockClutchYawSpeed = (RandomizedRangeSettings)randomizedRangeSettings6.withVisibility(condition10);
      RandomizedRangeSettings randomizedRangeSettings7 = (new RandomizedRangeSettings(this, "BlockClutchPitchSpeed", 30.0, 50.0, 0.0, 180.0, 1, this.blockClutchRotationSpeed)).withoutNotInARow();
      String str4 = "PitchSpeed";
      RandomizedRangeSettings randomizedRangeSettings8 = (RandomizedRangeSettings)randomizedRangeSettings7.withDisplayNamePrefix(str4);
      SettingVisibilityCondition condition11 = () -> this.isBlockClutchPitchSpeedVisible();
      this.blockClutchPitchSpeed = (RandomizedRangeSettings)randomizedRangeSettings8.withVisibility(condition11);
      RandomizedRangeSettings randomizedRangeSettings9 = new RandomizedRangeSettings(this, "BlockClutchYawAcceleration", 7.0, 15.0, 1.0, 180.0, 1, this.blockClutchRotationSpeed);
      String str5 = "YawAcceleration";
      RandomizedRangeSettings randomizedRangeSettings10 = ((RandomizedRangeSettings)randomizedRangeSettings9.withDisplayNamePrefix(str5)).withoutNotInARow();
      SettingVisibilityCondition condition12 = () -> {
         return !this.blockClutchModeNew.isVisible() || !this.blockClutchModeNew.getValue();
      };
      this.blockClutchYawAcceleration = (RandomizedRangeSettings)randomizedRangeSettings10.withVisibility(condition12);
      RandomizedRangeSettings randomizedRangeSettings11 = new RandomizedRangeSettings(this, "BlockClutchPitchAcceleration", 7.0, 15.0, 1.0, 180.0, 1, this.blockClutchRotationSpeed);
      String str6 = "PitchAcceleration";
      RandomizedRangeSettings randomizedRangeSettings12 = ((RandomizedRangeSettings)randomizedRangeSettings11.withDisplayNamePrefix(str6)).withoutNotInARow();
      SettingVisibilityCondition condition13 = () -> {
         return !this.blockClutchModeNew.isVisible() || !this.blockClutchModeNew.getValue();
      };
      this.blockClutchPitchAcceleration = (RandomizedRangeSettings)randomizedRangeSettings12.withVisibility(condition13);
      RandomizedRangeSettings randomizedRangeSettings13 = (new RandomizedRangeSettings(this, "KeepRotationsAfterModuleDisable", 3.0, 5.0, 0.0, 20.0, 0, this.rotationSpeed)).withoutNotInARow();
      SettingVisibilityCondition condition14 = () -> {
         return this.moveFix.getSelectedValue().equals("Default");
      };
      this.keepRotationsAfterModuleDisable = (RandomizedRangeSettings)randomizedRangeSettings13.withVisibility(condition14);
      this.rotationRandomization = new DoubleValue(this, "RotationRandomization", true, this.rotations);
      this.randomizePitch = new BooleanValue(this, "RandomizePitch", false, this.rotationRandomization);
      StringValue stringValue4 = new StringValue(this, "RandomizePitchChanceWhileYawRotation", 100.0, 0.0, 100.0, 0, this.rotationRandomization);
      SettingVisibilityCondition condition15 = () -> this.isRandomizePitchChanceWhileYawVisible();
      this.randomizePitchChanceWhileYawRotation = stringValue4.visibleWhen(condition15);
      StringValue stringValue5 = new StringValue(this, "RandomizePitchChanceOnBlock", 100.0, 0.0, 100.0, 0, this.rotationRandomization);
      SettingVisibilityCondition condition16 = () -> this.isRandomizePitchChanceOnBlockVisible();
      this.randomizePitchChanceOnBlock = stringValue5.visibleWhen(condition16);
      StringValue stringValue6 = new StringValue(this, "RandomizePitchChanceInAir", 100.0, 0.0, 100.0, 0, this.rotationRandomization);
      SettingVisibilityCondition condition17 = () -> this.isRandomizePitchChanceInAirVisible();
      this.randomizePitchChanceInAir = stringValue6.visibleWhen(condition17);
      BooleanValue booleanValue6 = new BooleanValue(this, "OtherPitchRandomization", false, this.rotationRandomization);
      SettingVisibilityCondition condition18 = () -> {
         return this.randomizePitch.getValue() && this.randomizePitch.isVisible();
      };
      this.otherPitchRandomization = (BooleanValue)booleanValue6.withVisibility(condition18);
      RandomizedRangeSettings randomizedRangeSettings14 = (new RandomizedRangeSettings(this, "RandomizePitchVal", -1.0, 1.0, -10.0, 10.0, 2, this.rotationRandomization)).withoutNotInARow();
      SettingVisibilityCondition condition19 = () -> {
         return !this.otherPitchRandomization.getValue() && this.randomizePitch.getValue() && this.randomizePitch.isVisible();
      };
      this.randomizePitchVal = (RandomizedRangeSettings)randomizedRangeSettings14.withVisibility(condition19);
      RandomizedRangeSettings randomizedRangeSettings15 = new RandomizedRangeSettings(this, "RandomizePitchInterval", 2.0, 4.0, 0.0, 20.0, 0, this.rotationRandomization);
      SettingVisibilityCondition condition20 = () -> this.isRandomizePitchIntervalVisible();
      this.randomizePitchInterval = (RandomizedRangeSettings)randomizedRangeSettings15.withVisibility(condition20);
      this.randomizeYaw = new BooleanValue(this, "RandomizeYaw", false, this.rotationRandomization);
      StringValue stringValue7 = new StringValue(this, "RandomizeYawChanceWhilePitchRotation", 100.0, 0.0, 100.0, 0, this.rotationRandomization);
      SettingVisibilityCondition condition21 = () -> this.isRandomizeYawChanceWhilePitchVisible();
      this.randomizeYawChanceWhilePitchRotation = stringValue7.visibleWhen(condition21);
      StringValue stringValue8 = new StringValue(this, "RandomizeYawChanceOnBlock", 100.0, 0.0, 100.0, 0, this.rotationRandomization);
      SettingVisibilityCondition condition22 = () -> this.isRandomizeYawChanceOnBlockVisible();
      this.randomizeYawChanceOnBlock = stringValue8.visibleWhen(condition22);
      StringValue stringValue9 = new StringValue(this, "RandomizeYawChanceOnAir", 100.0, 0.0, 100.0, 0, this.rotationRandomization);
      SettingVisibilityCondition condition23 = () -> {
         return this.randomizeYaw.getValue() && this.randomizeYaw.isVisible();
      };
      this.randomizeYawChanceOnAir = stringValue9.visibleWhen(condition23);
      BooleanValue booleanValue7 = new BooleanValue(this, "OtherYawRandomization", false, this.rotationRandomization);
      SettingVisibilityCondition condition24 = () -> this.isOtherYawRandomizationVisible();
      this.otherYawRandomization = (BooleanValue)booleanValue7.withVisibility(condition24);
      RandomizedRangeSettings randomizedRangeSettings16 = (new RandomizedRangeSettings(this, "RandomizeYawVal", -1.0, 1.0, -10.0, 10.0, 2, this.rotationRandomization)).withoutNotInARow();
      SettingVisibilityCondition condition25 = () -> this.isRandomizeYawValVisible();
      this.randomizeYawVal = (RandomizedRangeSettings)randomizedRangeSettings16.withVisibility(condition25);
      RandomizedRangeSettings randomizedRangeSettings17 = new RandomizedRangeSettings(this, "RandomizeYawInterval", 2.0, 4.0, 0.0, 20.0, 0, this.rotationRandomization);
      SettingVisibilityCondition condition26 = () -> this.isRandomizeYawIntervalVisible();
      this.randomizeYawInterval = (RandomizedRangeSettings)randomizedRangeSettings17.withVisibility(condition26);
      this.staticPitchSettings = new DoubleValue(this, "StaticPitchSettings", true, this.rotations);
      BooleanValue booleanValue8 = new BooleanValue(this, "StaticPitchBool", false, this.staticPitchSettings);
      String str7 = "StaticPitch";
      this.staticPitchBool = (BooleanValue)booleanValue8.withDisplayName(str7);
      BooleanValue booleanValue9 = new BooleanValue(this, "StaticPitchAtJump", false, this.staticPitchSettings);
      booleanValue2 = this.staticPitchBool;
      this.staticPitchBool.getClass();
      BooleanValue booleanValue10 = booleanValue2;
      SettingVisibilityCondition condition27 = () -> booleanValue10.getValue();
      this.staticPitchAtJump = (BooleanValue)booleanValue9.withVisibility(condition27);
      BooleanValue booleanValue11 = new BooleanValue(this, "StaticPitchRandomizeOnlyAtStart", false, this.staticPitchSettings);
      booleanValue2 = this.staticPitchBool;
      this.staticPitchBool.getClass();
      BooleanValue booleanValue12 = booleanValue2;
      SettingVisibilityCondition condition28 = () -> booleanValue12.getValue();
      this.staticPitchRandomizeOnlyAtStart = (BooleanValue)booleanValue11.withVisibility(condition28);
      BooleanValue booleanValue13 = new BooleanValue(this, "IgnoreStaticPitchForBlockClutches", false, this.staticPitchSettings);
      booleanValue2 = this.staticPitchBool;
      this.staticPitchBool.getClass();
      BooleanValue booleanValue14 = booleanValue2;
      SettingVisibilityCondition condition29 = () -> booleanValue14.getValue();
      this.ignoreStaticPitchForBlockClutches = (BooleanValue)booleanValue13.withVisibility(condition29);
      RandomizedRangeSettings randomizedRangeSettings18 = (new RandomizedRangeSettings(this, "StaticPitch", 80.0, 80.0, 0.0, 90.0, 2, this.staticPitchSettings)).withoutNotInARow();
      String str8 = "MaxStaticPitchForward";
      String str9 = "MinStaticPitchForward";
      RandomizedRangeSettings randomizedRangeSettings19 = randomizedRangeSettings18.withRangeNames(str9, str8);
      booleanValue2 = this.staticPitchBool;
      this.staticPitchBool.getClass();
      BooleanValue booleanValue15 = booleanValue2;
      SettingVisibilityCondition condition30 = () -> booleanValue15.getValue();
      this.staticPitch = (RandomizedRangeSettings)randomizedRangeSettings19.withVisibility(condition30);
      BooleanValue booleanValue16 = new BooleanValue(this, "StaticPitchBoolDiagonal", false, this.staticPitchSettings);
      String str10 = "StaticPitchDiagonal";
      this.staticPitchBoolDiagonal = (BooleanValue)booleanValue16.withDisplayName(str10);
      BooleanValue booleanValue17 = new BooleanValue(this, "StaticPitchAtJumpDiagonal", false, this.staticPitchSettings);
      booleanValue2 = this.staticPitchBoolDiagonal;
      this.staticPitchBoolDiagonal.getClass();
      BooleanValue booleanValue18 = booleanValue2;
      SettingVisibilityCondition condition31 = () -> booleanValue18.getValue();
      this.staticPitchAtJumpDiagonal = (BooleanValue)booleanValue17.withVisibility(condition31);
      BooleanValue booleanValue19 = new BooleanValue(this, "StaticPitchRandomizeOnlyAtStartDiagonal", false, this.staticPitchSettings);
      booleanValue2 = this.staticPitchBoolDiagonal;
      this.staticPitchBoolDiagonal.getClass();
      BooleanValue booleanValue20 = booleanValue2;
      SettingVisibilityCondition condition32 = () -> booleanValue20.getValue();
      this.staticPitchRandomizeOnlyAtStartDiagonal = (BooleanValue)booleanValue19.withVisibility(condition32);
      BooleanValue booleanValue21 = new BooleanValue(this, "IgnoreStaticPitchForBlockClutchesDiagonal", false, this.staticPitchSettings);
      booleanValue2 = this.staticPitchBoolDiagonal;
      this.staticPitchBoolDiagonal.getClass();
      BooleanValue booleanValue22 = booleanValue2;
      SettingVisibilityCondition condition33 = () -> booleanValue22.getValue();
      this.ignoreStaticPitchForBlockClutchesDiagonal = (BooleanValue)booleanValue21.withVisibility(condition33);
      RandomizedRangeSettings randomizedRangeSettings20 = (new RandomizedRangeSettings(this, "StaticPitchDiagonal", 80.0, 80.0, 0.0, 90.0, 2, this.staticPitchSettings)).withoutNotInARow();
      booleanValue2 = this.staticPitchBoolDiagonal;
      this.staticPitchBoolDiagonal.getClass();
      BooleanValue booleanValue23 = booleanValue2;
      SettingVisibilityCondition condition34 = () -> booleanValue23.getValue();
      this.staticPitchDiagonal = (RandomizedRangeSettings)randomizedRangeSettings20.withVisibility(condition34);
      BooleanValue booleanValue24 = new BooleanValue(this, "MinimalMouseMovement", false, this.staticPitchSettings);
      SettingVisibilityCondition condition35 = () -> {
         return this.staticPitchBool.getValue() || this.staticPitchBoolDiagonal.getValue();
      };
      this.minimalMouseMovement = (BooleanValue)booleanValue24.withVisibility(condition35);
      this.reduceYawRotation = new BooleanValue(this, "ReduceYawRotation", false, this.rotations);
      this.strictYaw = new BooleanValue(this, "StrictYaw", false, this.rotations);
      RandomizedRangeSettings randomizedRangeSettings21 = (new RandomizedRangeSettings(this, "StrictYawIncorrectFactor", 1.5, 3.0, 0.0, 6.0, 2, this.rotations)).withoutNotInARow();
      booleanValue2 = this.strictYaw;
      this.strictYaw.getClass();
      BooleanValue booleanValue25 = booleanValue2;
      SettingVisibilityCondition condition36 = () -> booleanValue25.getValue();
      this.strictYawIncorrectFactor = (RandomizedRangeSettings)randomizedRangeSettings21.withVisibility(condition36);
      RandomizedRangeSettings randomizedRangeSettings22 = (new RandomizedRangeSettings(this, "StartDelayAfterDirectionChange", 0.0, 0.0, 0.0, 20.0, 0, this.rotations)).withoutNotInARow();
      booleanValue2 = this.strictYaw;
      this.strictYaw.getClass();
      BooleanValue booleanValue26 = booleanValue2;
      SettingVisibilityCondition condition37 = () -> booleanValue26.getValue();
      this.startDelayAfterDirectionChange = (RandomizedRangeSettings)randomizedRangeSettings22.withVisibility(condition37);
      BooleanValue booleanValue27 = new BooleanValue(this, "IgnoreStartDelayAfterDirectionChangeAtBlockClutch", false, this.rotations);
      SettingVisibilityCondition condition38 = () -> this.isIgnoreDirChangeDelayClutchVisible();
      this.ignoreStartDelayAfterDirectionChangeAtBlockClutch = (BooleanValue)booleanValue27.withVisibility(condition38);
      BooleanValue booleanValue28 = new BooleanValue(this, "ChangeDirectionOnlyIfBlockUnderYou", false, this.rotations);
      booleanValue2 = this.strictYaw;
      this.strictYaw.getClass();
      BooleanValue booleanValue29 = booleanValue2;
      SettingVisibilityCondition condition39 = () -> booleanValue29.getValue();
      this.changeDirectionOnlyIfBlockUnderYou = (BooleanValue)booleanValue28.withVisibility(condition39);
      this.rotateWithMovement = new BooleanValue(this, "RotateWithMovement", true, this.rotations);
      booleanValue2 = new BooleanValue(this, "RotateWithMovementOnlyOrthogonal", false, this.rotations);
      BooleanValue rotateWithMovement2 = this.rotateWithMovement;
      this.rotateWithMovement.getClass();
      BooleanValue booleanValue30 = rotateWithMovement2;
      SettingVisibilityCondition condition40 = () -> booleanValue30.getValue();
      this.rotateWithMovementOnlyOrthogonal = (BooleanValue)booleanValue2.withVisibility(condition40);
      this.improve = new BooleanValue(this, "Improve", true, this.rotations);
      booleanValue2 = new BooleanValue(this, "ImproveOnlyAtClutch", false, this.rotations);
      SettingVisibilityCondition condition41 = () -> this.isImproveOnlyAtClutchVisible();
      this.improveOnlyAtClutch = (BooleanValue)booleanValue2.withVisibility(condition41);
      this.extraBlockSearch = new BooleanValue(this, "ExtraBlockSearch", true, this.rotations);
      booleanValue2 = new BooleanValue(this, "ExtraBlockSearchOnlyAtClutch", false, this.rotations);
      BooleanValue extraBlockSearch2 = this.extraBlockSearch;
      this.extraBlockSearch.getClass();
      BooleanValue booleanValue31 = extraBlockSearch2;
      SettingVisibilityCondition condition42 = () -> booleanValue31.getValue();
      this.extraBlockSearchOnlyAtClutch = (BooleanValue)booleanValue2.withVisibility(condition42);
      this.strafeSettings = new DoubleValue(this, "StrafeSettings", true, this.rotations);
      this.strafe = new BooleanValue(this, "Strafe", false, this.strafeSettings);
      booleanValue2 = new BooleanValue(this, "StrafeOnlyWhileJump", false, this.strafeSettings);
      SettingVisibilityCondition condition43 = () -> this.isStrafeOnlyWhileJumpVisible();
      this.strafeOnlyWhileJump = (BooleanValue)booleanValue2.withVisibility(condition43);
      RandomizedRangeSettings randomizedRangeSettings23 = (new RandomizedRangeSettings(this, "StrafeDegree", 40.0, 45.0, 1.0, 50.0, 2, this.strafeSettings)).withoutNotInARow();
      booleanValue2 = this.strafe;
      this.strafe.getClass();
      BooleanValue booleanValue32 = booleanValue2;
      SettingVisibilityCondition condition44 = () -> booleanValue32.getValue();
      this.strafeDegree = (RandomizedRangeSettings)randomizedRangeSettings23.withVisibility(condition44);
      RandomizedRangeSettings randomizedRangeSettings24 = (new RandomizedRangeSettings(this, "StrafeWidth", 0.35, 0.39, 0.0, 0.4, 2, this.strafeSettings)).withoutNotInARow();
      SettingVisibilityCondition condition45 = () -> this.isStrafeWidthVisible();
      this.strafeWidth = (RandomizedRangeSettings)randomizedRangeSettings24.withVisibility(condition45);
      BooleanValue booleanValue33 = new BooleanValue(this, "CorrectRotationWhileBuilding", true, this.strafeSettings);
      booleanValue2 = this.strafe;
      this.strafe.getClass();
      BooleanValue booleanValue34 = booleanValue2;
      SettingVisibilityCondition condition46 = () -> booleanValue34.getValue();
      this.correctRotationWhileBuilding = (BooleanValue)booleanValue33.withVisibility(condition46);
      BooleanValue booleanValue35 = new BooleanValue(this, "CorrectStrafeWithKeyBinds", true, this.strafeSettings);
      booleanValue2 = this.strafe;
      this.strafe.getClass();
      BooleanValue booleanValue36 = booleanValue2;
      SettingVisibilityCondition condition47 = () -> booleanValue36.getValue();
      this.correctStrafeWithKeyBinds = (BooleanValue)booleanValue35.withVisibility(condition47);
      BooleanValue booleanValue37 = new BooleanValue(this, "CorrectStrafeWithKeyBindsCage", false, this.strafeSettings);
      SettingVisibilityCondition condition48 = () -> this.isStrafeKeyBindCageVisible();
      this.correctStrafeWithKeyBindsCage = (BooleanValue)booleanValue37.withVisibility(condition48);
      RandomizedRangeSettings randomizedRangeSettings25 = (new RandomizedRangeSettings(this, "CorrectStrafeWithKeyBindsWidth", -0.05, 0.05, -0.3, 0.3, 2, this.strafeSettings))
         .withoutNotInARow()
         .withoutNotInARow();
      SettingVisibilityCondition condition49 = () -> this.isStrafeKeyBindWidthVisible();
      this.correctStrafeWithKeyBindsWidth = (RandomizedRangeSettings)randomizedRangeSettings25.withVisibility(condition49);
      RandomizedRangeSettings randomizedRangeSettings26 = new RandomizedRangeSettings(this, "CorrectStrafeWithKeyBindsDuration", 1.0, 3.0, 1.0, 5.0, 0, this.strafeSettings);
      SettingVisibilityCondition condition50 = () -> {
         return this.correctStrafeWithKeyBinds.getValue() && this.correctStrafeWithKeyBinds.isVisible();
      };
      this.correctStrafeWithKeyBindsDuration = (RandomizedRangeSettings)randomizedRangeSettings26.withVisibility(condition50);
      RandomizedRangeSettings randomizedRangeSettings27 = (new RandomizedRangeSettings(this, "CorrectStrafeWithKeyBindsMiddle", 0.5, 0.5, 0.2, 0.8, 2, this.strafeSettings)).withoutNotInARow();
      SettingVisibilityCondition condition51 = () -> this.isStrafeKeyBindMiddleVisible();
      this.correctStrafeWithKeyBindsMiddle = (RandomizedRangeSettings)randomizedRangeSettings27.withVisibility(condition51);
      RangeSetting rangeSetting = new RangeSetting(this, "CorrectStrafeWithKeyBindsMiddleExclusion", 0.0, 0.0, 0.2, 0.8, 2, this.strafeSettings);
      SettingVisibilityCondition condition52 = () -> this.isStrafeKeyBindMiddleExclusionVisible();
      this.correctStrafeWithKeyBindsMiddleExclusion = rangeSetting.visibleWhen(condition52);
      RandomizedRangeSettings randomizedRangeSettings28 = (new RandomizedRangeSettings(this, "CorrectStrafeWithKeyBindsDelayToRecalculateMiddle", 15.0, 30.0, 0.0, 40.0, 0, this.strafeSettings))
         .withoutNotInARow();
      String str11 = "DelayToRecalculateMiddle";
      RandomizedRangeSettings randomizedRangeSettings29 = (RandomizedRangeSettings)randomizedRangeSettings28.withDisplayNamePrefix(str11);
      SettingVisibilityCondition condition53 = () -> this.isStrafeKeyBindRecalcDelayVisible();
      this.correctStrafeWithKeyBindsDelayToRecalculateMiddle = (RandomizedRangeSettings)randomizedRangeSettings29.withVisibility(condition53);
      this.startAtEdge = new BooleanValue(this, "StartAtEdge", false, this.rotations);
      RandomizedRangeSettings randomizedRangeSettings30 = (new RandomizedRangeSettings(this, "StartAtEdgeDelay", 2.0, 4.0, 0.0, 10.0, 0, this.rotations)).withoutNotInARow();
      booleanValue2 = this.startAtEdge;
      this.startAtEdge.getClass();
      BooleanValue booleanValue38 = booleanValue2;
      SettingVisibilityCondition condition54 = () -> booleanValue38.getValue();
      this.startAtEdgeDelay = (RandomizedRangeSettings)randomizedRangeSettings30.withVisibility(condition54);
      BooleanValue booleanValue39 = new BooleanValue(this, "IgnoreStartAtEdgeAtBlockClutch", false, this.rotations);
      SettingVisibilityCondition condition55 = () -> this.isIgnoreStartAtEdgeClutchVisible();
      this.ignoreStartAtEdgeAtBlockClutch = (BooleanValue)booleanValue39.withVisibility(condition55);
      this.edgeRotateCheck = new BooleanValue(this, "EdgeRotateCheck", false, this.rotations);
      BooleanValue booleanValue40 = new BooleanValue(this, "EdgePlaceCheck", false, this.rotations);
      booleanValue2 = this.edgeRotateCheck;
      this.edgeRotateCheck.getClass();
      BooleanValue booleanValue41 = booleanValue2;
      SettingVisibilityCondition condition56 = () -> booleanValue41.getValue();
      this.edgePlaceCheck = (BooleanValue)booleanValue40.withVisibility(condition56);
      this.flickMode = new BooleansSetting(this, "FlickMode", "None", new String[]{"None", "Simple", "Jump"}, this.rotations);
      BooleanValue booleanValue42 = new BooleanValue(this, "FlickUserStrafeSettingForForwardLooking", false, this.rotations);
      SettingVisibilityCondition condition57 = () -> this.isFlickForwardStrafeVisible();
      this.flickUserStrafeSettingForForwardLooking = (BooleanValue)booleanValue42.withVisibility(condition57);
      this.otherRotation = new BooleanValue(this, "OtherRotation", true, this.rotations);
      this.recycleRotation = new BooleanValue(this, "RecycleRotation", false, this.rotations);
      this.intave = new BooleanValue(this, "Intave", true, this.rotations);
      BooleanValue booleanValue43 = new BooleanValue(this, "IntaveSafe", true, this.rotations);
      booleanValue2 = this.intave;
      this.intave.getClass();
      BooleanValue booleanValue44 = booleanValue2;
      SettingVisibilityCondition condition58 = () -> booleanValue44.getValue();
      this.intaveSafe = (BooleanValue)booleanValue43.withVisibility(condition58);
      BooleanValue booleanValue45 = new BooleanValue(this, "IntaveFast", false, this.rotations);
      booleanValue2 = this.intave;
      this.intave.getClass();
      BooleanValue booleanValue46 = booleanValue2;
      SettingVisibilityCondition condition59 = () -> booleanValue46.getValue();
      this.intaveFast = (BooleanValue)booleanValue45.withVisibility(condition59);
      this.pitchSelection = new BooleansSetting(
         this, "PitchSelection", "Nearest", new String[]{"Nearest", "Furthest", "Highest", "Lowest", "Mid", "Random"}, this.rotations
      );
      StringValue stringValue10 = new StringValue(this, "MaxPitchCorrection", 180.0, 0.0, 180.0, 2, this.rotations);
      SettingVisibilityCondition condition60 = () -> this.isMaxPitchCorrectionVisible();
      this.maxPitchCorrection = stringValue10.visibleWhen(condition60);
      StringValue stringValue11 = new StringValue(this, "MaxPitchCorrectionAtBlockClutch", 180.0, 0.0, 180.0, 2, this.rotations);
      SettingVisibilityCondition condition61 = () -> this.isMaxPitchCorrectionClutchVisible();
      this.maxPitchCorrectionAtBlockClutch = stringValue11.visibleWhen(condition61);
      this.expand = new BooleanValue(this, "Expand", false, this.rotations);
      StringValue stringValue12 = new StringValue(this, "ExpandLength", 1.0, 1.0, 10.0, 1, this.rotations);
      booleanValue2 = this.expand;
      this.expand.getClass();
      BooleanValue booleanValue47 = booleanValue2;
      SettingVisibilityCondition condition62 = () -> booleanValue47.getValue();
      this.expandLength = stringValue12.visibleWhen(condition62);
      this.clicking = new DoubleValue(this, "Clicking", true);
      this.noSwing = new BooleanValue(this, "NoSwing", false, this.clicking);
      BooleanValue booleanValue48 = new BooleanValue(this, "ServerSideNoSwing", false, this.clicking);
      booleanValue2 = this.noSwing;
      this.noSwing.getClass();
      BooleanValue booleanValue49 = booleanValue2;
      SettingVisibilityCondition condition63 = () -> booleanValue49.getValue();
      this.serverSideNoSwing = (BooleanValue)booleanValue48.withVisibility(condition63);
      this.cps = (new RandomizedRangeSettings(this, "CPS", 10.0, 20.0, 0.0, 40.0, 0, this.clicking)).withoutNotInARow();
      this.dcpsMode = new BooleansSetting(this, "DCPSMode", "External", new String[]{"Internal", "External"}, this.clicking);
      RandomizedRangeSettings randomizedRangeSettings31 = (new RandomizedRangeSettings(this, "DCPS", 2.0, 4.0, 0.0, 20.0, 0, this.clicking)).withoutNotInARow();
      SettingVisibilityCondition condition64 = () -> {
         return this.dcpsMode.getSelectedValue().equals("External");
      };
      this.dcps = (RandomizedRangeSettings)randomizedRangeSettings31.withVisibility(condition64);
      this.forcePlaceIfYouWouldFallDown = new BooleanValue(this, "ForcePlaceIfYouWouldFallDown", true, this.clicking);
      this.alwaysPlaceWhenPossible = new BooleanValue(this, "AlwaysPlaceWhenPossible", true, this.clicking);
      this.clickPause = new BooleanValue(this, "ClickPause", false, this.clicking);
      this.delayClick = new BooleanValue(this, "DelayClick", false, this.clicking);
      this.oneClick = new BooleanValue(this, "OneClick", false, this.clicking);
      BooleanValue booleanValue50 = new BooleanValue(this, "StopClickingAtSneaking", false, this.clicking);
      String str12 = "StopClickingAtSneakingOnGround";
      this.stopClickingAtSneaking = (BooleanValue)booleanValue50.withDisplayName(str12);
      this.maxYawRotationDeltaToPlaceBlocks = new StringValue(this, "MaxYawRotationDeltaToPlaceBlocks", 180.0, 0.0, 180.0, 2, this.clicking);
      this.maxPitchRotationDeltaToPlaceBlocks = new StringValue(this, "MaxPitchRotationDeltaToPlaceBlocks", 180.0, 0.0, 180.0, 2, this.clicking);
      this.maxYawRotationDeltaToPlaceBlocksAtBlockClutch = new StringValue(this, "MaxYawRotationDeltaToPlaceBlocksAtBlockClutch", 180.0, 0.0, 180.0, 2, this.clicking);
      this.maxPitchRotationDeltaToPlaceBlocksAtBlockClutch = new StringValue(this, "MaxPitchRotationDeltaToPlaceBlocksAtBlockClutch", 180.0, 0.0, 180.0, 2, this.clicking);
      this.movement = new DoubleValue(this, "Movement", true);
      this.removeSneakStop = new BooleanValue(this, "RemoveSneakStop", false, this.movement);
      this.sneaking = new DoubleValue(this, "Sneaking", true, this.movement);
      this.sneak = new BooleanValue(this, "Sneak", false, this.sneaking);
      BooleanValue booleanValue51 = new BooleanValue(this, "TimeJumpForOneStackForward", false, this.sneaking);
      booleanValue2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue52 = booleanValue2;
      SettingVisibilityCondition condition65 = () -> booleanValue52.getValue();
      this.timeJumpForOneStackForward = (BooleanValue)booleanValue51.withVisibility(condition65);
      BooleanValue booleanValue53 = new BooleanValue(this, "OneStackForward", false, this.sneaking);
      booleanValue2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue54 = booleanValue2;
      SettingVisibilityCondition condition66 = () -> booleanValue54.getValue();
      this.oneStackForward = (BooleanValue)booleanValue53.withVisibility(condition66);
      RangeSetting rangeSetting2 = new RangeSetting(this, "MinOffGroundSneakTickForward", 1.0, 2.0, 0.0, 12.0, 0, this.sneaking);
      SettingVisibilityCondition condition67 = () -> this.isMinOffGroundSneakForwardVisible();
      this.minOffGroundSneakTickForward = rangeSetting2.visibleWhen(condition67);
      RangeSetting rangeSetting3 = new RangeSetting(this, "MaxOffGroundSneakTickForward", 10.0, 11.0, 0.0, 12.0, 0, this.sneaking);
      SettingVisibilityCondition condition68 = () -> this.isMaxOffGroundSneakForwardVisible();
      this.maxOffGroundSneakTickForward = rangeSetting3.visibleWhen(condition68);
      BooleanValue booleanValue55 = new BooleanValue(this, "OneStackDiagonal", false, this.sneaking);
      booleanValue2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue56 = booleanValue2;
      SettingVisibilityCondition condition69 = () -> booleanValue56.getValue();
      this.oneStackDiagonal = (BooleanValue)booleanValue55.withVisibility(condition69);
      RangeSetting rangeSetting4 = new RangeSetting(this, "MinOffGroundSneakTickDiagonal", 1.0, 2.0, 0.0, 12.0, 0, this.sneaking);
      SettingVisibilityCondition condition70 = () -> this.isMinOffGroundSneakDiagonalVisible();
      this.minOffGroundSneakTickDiagonal = rangeSetting4.visibleWhen(condition70);
      RangeSetting rangeSetting5 = new RangeSetting(this, "MaxOffGroundSneakTickDiagonal", 10.0, 11.0, 0.0, 12.0, 0, this.sneaking);
      SettingVisibilityCondition condition71 = () -> this.isMaxOffGroundSneakDiagonalVisible();
      this.maxOffGroundSneakTickDiagonal = rangeSetting5.visibleWhen(condition71);
      RandomizedRangeSettings randomizedRangeSettings32 = new RandomizedRangeSettings(this, "SneakTicks", 1.0, 2.0, 0.0, 10.0, 0, this.sneaking);
      double d = 15.0;
      double d2 = 8.0;
      double d3 = 30.0;
      double d4 = 1.0;
      double d5 = 3.0;
      double d6 = 3.0;
      RandomizedRangeSettings randomizedRangeSettings33 = randomizedRangeSettings32.withOutliers(d6, d5, d4, d3, d2, d);
      booleanValue2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue57 = booleanValue2;
      SettingVisibilityCondition condition72 = () -> booleanValue57.getValue();
      this.sneakTicks = (RandomizedRangeSettings)randomizedRangeSettings33.withVisibility(condition72);
      RangeSetting rangeSetting6 = new RangeSetting(this, "StartSneakDistToBlock", 0.05, 0.2, -0.2, 0.5, 2, this.sneaking);
      booleanValue2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue58 = booleanValue2;
      SettingVisibilityCondition condition73 = () -> booleanValue58.getValue();
      this.startSneakDistToBlock = rangeSetting6.visibleWhen(condition73);
      RangeSetting rangeSetting7 = new RangeSetting(this, "SneakBlocksForward", 1.0, 2.0, 0.0, 15.0, 0, this.sneaking);
      booleanValue2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue59 = booleanValue2;
      SettingVisibilityCondition condition74 = () -> booleanValue59.getValue();
      this.sneakBlocksForward = rangeSetting7.visibleWhen(condition74);
      RangeSetting rangeSetting8 = new RangeSetting(this, "SneakBlocksDiagonal", 1.0, 2.0, 0.0, 15.0, 0, this.sneaking);
      booleanValue2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue60 = booleanValue2;
      SettingVisibilityCondition condition75 = () -> booleanValue60.getValue();
      this.sneakBlocksDiagonal = rangeSetting8.visibleWhen(condition75);
      StringValue stringValue13 = new StringValue(this, "SneakSlowDown", 0.3, 0.3, 1.0, 2, this.sneaking);
      booleanValue2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue61 = booleanValue2;
      SettingVisibilityCondition condition76 = () -> booleanValue61.getValue();
      this.sneakSlowDown = stringValue13.visibleWhen(condition76);
      BooleanValue booleanValue62 = new BooleanValue(this, "EarlySneakCalculation", true, this.sneaking);
      booleanValue2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue63 = booleanValue2;
      SettingVisibilityCondition condition77 = () -> booleanValue63.getValue();
      this.earlySneakCalculation = (BooleanValue)booleanValue62.withVisibility(condition77);
      BooleanValue booleanValue64 = new BooleanValue(this, "EarlySneakCalculationOnlyAtSpeedEffect", true, this.sneaking);
      SettingVisibilityCondition condition78 = () -> this.isEarlySneakSpeedOnlyVisible();
      this.earlySneakCalculationOnlyAtSpeedEffect = (BooleanValue)booleanValue64.withVisibility(condition78);
      this.adhsMode = new BooleansSetting(this, "ADHSMode", "None", new String[]{"None", "ADS", "ADHS"}, this.movement);
      this.moveFix = new BooleansSetting(this, "MoveFix", "Default", new String[]{"None", "Default", "Advanced"}, this.movement);
      BooleanValue booleanValue65 = new BooleanValue(this, "MoveFixIntave", true, this.movement);
      SettingVisibilityCondition condition79 = () -> this.isMoveFixIntaveVisible();
      this.moveFixIntave = (BooleanValue)booleanValue65.withVisibility(condition79);
      BooleansSetting booleansSetting = new BooleansSetting(
         this, "Mode", "None", new String[]{"None", "Legit", "AllDirection", "AllDirectionMotion"}, this.movement
      );
      String str13 = "SprintMode";
      this.mode = (BooleansSetting)booleansSetting.withDisplayName(str13);
      this.jumpSprint = new BooleanValue(this, "JumpSprint", false, this.movement);
      this.hover = new BooleanValue(this, "Hover", false, this.movement);
      this.stopAtStart = new BooleanValue(this, "StopAtStart", false, this.movement);
      this.safeWalkNoBlocks = new BooleanValue(this, "SafeWalkNoBlocks", true, this.movement);
      this.sneakNoBlocks = new BooleanValue(this, "SneakNoBlocks", true, this.movement);
      this.stopNoBlocks = new BooleanValue(this, "StopNoBlocks", false, this.movement);
      this.reduceVelocity = new BooleanValue(this, "ReduceVelocity", false, this.movement);
      this.ignoreSpeedMotion = new BooleanValue(this, "IgnoreSpeedMotion", false, this.movement);
      this.sneakAtRotation = new BooleanValue(this, "SneakAtRotation", false, this.movement);
      this.stopAtRotation = new BooleanValue(this, "StopAtRotation", false, this.movement);
      this.godBridging = new DoubleValue(this, "GodBridging", true, this.movement);
      this.godBridge = new BooleanValue(this, "GodBridge", false, this.godBridging);
      this.disableGodbridgeSafetyFeature = new BooleanValue(this, "DisableGodbridgeSafetyFeature", false, this.godBridging);
      RandomizedRangeSettings randomizedRangeSettings34 = new RandomizedRangeSettings(this, "ResetGodBridgeAfterXJumps", 7.0, 8.0, 0.0, 12.0, 0, this.godBridging);
      booleanValue2 = this.godBridge;
      this.godBridge.getClass();
      BooleanValue booleanValue66 = booleanValue2;
      SettingVisibilityCondition condition80 = () -> booleanValue66.getValue();
      this.resetGodBridgeAfterXJumps = (RandomizedRangeSettings)randomizedRangeSettings34.withVisibility(condition80);
      RandomizedRangeSettings randomizedRangeSettings35 = (new RandomizedRangeSettings(this, "JumpAfterXBlocks", 8.0, 8.0, 0.0, 9.0, 0, this.godBridging)).withoutNotInARow();
      booleanValue2 = this.godBridge;
      this.godBridge.getClass();
      BooleanValue booleanValue67 = booleanValue2;
      SettingVisibilityCondition condition81 = () -> booleanValue67.getValue();
      this.jumpAfterXBlocks = (RandomizedRangeSettings)randomizedRangeSettings35.withVisibility(condition81);
      RandomizedRangeSettings randomizedRangeSettings36 = new RandomizedRangeSettings(this, "JumpDelayAfterPlace", 0.0, 0.0, 0.0, 1.0, 0, this.godBridging);
      booleanValue2 = this.godBridge;
      this.godBridge.getClass();
      BooleanValue booleanValue68 = booleanValue2;
      SettingVisibilityCondition condition82 = () -> booleanValue68.getValue();
      this.jumpDelayAfterPlace = (RandomizedRangeSettings)randomizedRangeSettings36.withVisibility(condition82);
      RandomizedRangeSettings randomizedRangeSettings37 = (new RandomizedRangeSettings(this, "StartDelay", 10.0, 13.0, 8.0, 20.0, 0, this.godBridging)).withoutNotInARow();
      booleanValue2 = this.godBridge;
      this.godBridge.getClass();
      BooleanValue booleanValue69 = booleanValue2;
      SettingVisibilityCondition condition83 = () -> booleanValue69.getValue();
      this.startDelay = (RandomizedRangeSettings)randomizedRangeSettings37.withVisibility(condition83);
      this.moonWalking = new DoubleValue(this, "MoonWalking", true, this.movement);
      this.moonWalk = new BooleanValue(this, "MoonWalk", true, this.moonWalking);
      this.onlyOnGround = new BooleanValue(this, "OnlyOnGround", false, this.moonWalking);
      RandomizedRangeSettings randomizedRangeSettings38 = (new RandomizedRangeSettings(this, "MoonWalkWideness", 0.05, 0.1, 0.0, 0.3, 2, this.moonWalking)).withoutNotInARow();
      SettingVisibilityCondition condition84 = () -> {
         return this.moonWalk.getValue() && this.moonWalk.isVisible();
      };
      this.moonWalkWideness = (RandomizedRangeSettings)randomizedRangeSettings38.withVisibility(condition84);
      RangeSetting rangeSetting9 = new RangeSetting(this, "MoonWalkStartDistToBlock", 0.05, 0.2, -0.2, 0.5, 2, this.moonWalking);
      SettingVisibilityCondition condition85 = () -> {
         return this.moonWalk.getValue() && this.moonWalk.isVisible();
      };
      this.moonWalkStartDistToBlock = rangeSetting9.visibleWhen(condition85);
      RandomizedRangeSettings randomizedRangeSettings39 = new RandomizedRangeSettings(this, "MoonWalkMiddle", 0.5, 0.5, 0.2, 0.8, 2, this.moonWalking);
      SettingVisibilityCondition condition86 = () -> this.isMoonWalkMiddleVisible();
      this.moonWalkMiddle = (RandomizedRangeSettings)randomizedRangeSettings39.withVisibility(condition86);
      RandomizedRangeSettings randomizedRangeSettings40 = new RandomizedRangeSettings(this, "DelayToRecalculateMiddle", 15.0, 30.0, 0.0, 40.0, 0, this.moonWalking);
      SettingVisibilityCondition condition87 = () -> this.isDelayToRecalculateMiddleVisible();
      this.delayToRecalculateMiddle = (RandomizedRangeSettings)randomizedRangeSettings40.withVisibility(condition87);
      RandomizedRangeSettings randomizedRangeSettings41 = (new RandomizedRangeSettings(this, "PostPlacementStrafeTicks", 0.0, 0.0, 0.0, 8.0, 0, this.moonWalking)).withMaxInARow();
      boolean flag = false;
      double d7 = 15.0;
      double d8 = 0.0;
      double d9 = 8.0;
      double d10 = 0.0;
      double d11 = 8.0;
      double d12 = 5.0;
      RandomizedRangeSettings randomizedRangeSettings42 = randomizedRangeSettings41.withOutliers(d12, d11, d10, d9, d8, d7, flag);
      SettingVisibilityCondition condition88 = () -> this.isPostPlacementStrafeTicksVisible();
      this.postPlacementStrafeTicks = (RandomizedRangeSettings)randomizedRangeSettings42.withVisibility(condition88);
      RandomizedRangeSettings randomizedRangeSettings43 = (new RandomizedRangeSettings(this, "MinMoonwalkTicks", 0.0, 0.0, 0.0, 10.0, 0, this.moonWalking)).withMaxInARow();
      boolean flag2 = false;
      double d13 = 15.0;
      double d14 = 0.0;
      double d15 = 10.0;
      double d16 = 0.0;
      double d17 = 12.0;
      double d18 = 8.0;
      RandomizedRangeSettings randomizedRangeSettings44 = randomizedRangeSettings43.withOutliers(d18, d17, d16, d15, d14, d13, flag2);
      SettingVisibilityCondition condition89 = () -> {
         return this.moonWalk.getValue() && this.moonWalk.isVisible();
      };
      this.minMoonwalkTicks = (RandomizedRangeSettings)randomizedRangeSettings44.withVisibility(condition89);
      RandomizedRangeSettings randomizedRangeSettings45 = (new RandomizedRangeSettings(this, "DirectionChangeDelay", 0.0, 0.0, 0.0, 3.0, 0, this.moonWalking)).withMaxInARow();
      SettingVisibilityCondition condition90 = () -> this.isDirectionChangeDelayVisible();
      this.directionChangeDelay = (RandomizedRangeSettings)randomizedRangeSettings45.withVisibility(condition90);
      this.sameYBlockGroup = new DoubleValue(this, "SameYBlockGroup", true, this.movement);
      this.sameYBlock = new BooleanValue(this, "SameYBlock", false, this.sameYBlockGroup);
      BooleanValue booleanValue70 = new BooleanValue(this, "StopMovementAfterJump", true, this.sameYBlockGroup);
      booleanValue2 = this.sameYBlock;
      this.sameYBlock.getClass();
      BooleanValue booleanValue71 = booleanValue2;
      SettingVisibilityCondition condition91 = () -> booleanValue71.getValue();
      this.stopMovementAfterJump = (BooleanValue)booleanValue70.withVisibility(condition91);
      this.sameYGroup = new DoubleValue(this, "SameYGroup", true, this.movement);
      this.sameY = new BooleanValue(this, "SameY", false, this.sameYGroup);
      BooleanValue booleanValue72 = new BooleanValue(this, "AutoJump", false, this.sameYGroup);
      booleanValue2 = this.sameY;
      this.sameY.getClass();
      BooleanValue booleanValue73 = booleanValue2;
      SettingVisibilityCondition condition92 = () -> booleanValue73.getValue();
      this.autoJump = (BooleanValue)booleanValue72.withVisibility(condition92);
      BooleanValue booleanValue74 = new BooleanValue(this, "IntaveFast2", true, this.sameYGroup);
      SettingVisibilityCondition condition93 = () -> this.isIntaveFast2Visible();
      this.intaveFast2 = (BooleanValue)booleanValue74.withVisibility(condition93);
      BooleanValue booleanValue75 = new BooleanValue(this, "TellyBridge", false, this.sameYGroup);
      SettingVisibilityCondition condition94 = () -> this.isTellyBridgeVisible();
      this.tellyBridge = (BooleanValue)booleanValue75.withVisibility(condition94);
      this.safeWalkGroup = new DoubleValue(this, "SafeWalkGroup", true, this.movement);
      this.safeWalk = new BooleanValue(this, "SafeWalk", false, this.safeWalkGroup);
      BooleanValue booleanValue76 = new BooleanValue(this, "SafeWalkAir", false, this.safeWalkGroup);
      booleanValue2 = this.safeWalk;
      this.safeWalk.getClass();
      BooleanValue booleanValue77 = booleanValue2;
      SettingVisibilityCondition condition95 = () -> booleanValue77.getValue();
      this.safeWalkAir = (BooleanValue)booleanValue76.withVisibility(condition95);
      BooleanValue booleanValue78 = new BooleanValue(this, "SafeWalkOnlyDiagonal", false, this.safeWalkGroup);
      booleanValue2 = this.safeWalk;
      this.safeWalk.getClass();
      BooleanValue booleanValue79 = booleanValue2;
      SettingVisibilityCondition condition96 = () -> booleanValue79.getValue();
      this.safeWalkOnlyDiagonal = (BooleanValue)booleanValue78.withVisibility(condition96);
      this.jumpGroup = new DoubleValue(this, "JumpGroup", true, this.movement);
      this.jump = new BooleanValue(this, "Jump", false, this.jumpGroup);
      BooleanValue booleanValue80 = new BooleanValue(this, "JumpOnlyWhileSpacePressed", false, this.jumpGroup);
      booleanValue2 = this.jump;
      this.jump.getClass();
      BooleanValue booleanValue81 = booleanValue2;
      SettingVisibilityCondition condition97 = () -> booleanValue81.getValue();
      this.jumpOnlyWhileSpacePressed = (BooleanValue)booleanValue80.withVisibility(condition97);
      RandomizedRangeSettings randomizedRangeSettings46 = (new RandomizedRangeSettings(this, "JumpAfterXBlocksForward", 6.0, 6.0, 0.0, 10.0, 0, this.jumpGroup)).withoutNotInARow();
      booleanValue2 = this.jump;
      this.jump.getClass();
      BooleanValue booleanValue82 = booleanValue2;
      SettingVisibilityCondition condition98 = () -> booleanValue82.getValue();
      this.jumpAfterXBlocksForward = (RandomizedRangeSettings)randomizedRangeSettings46.withVisibility(condition98);
      RandomizedRangeSettings randomizedRangeSettings47 = (new RandomizedRangeSettings(this, "JumpAfterXBlocksDiagonal", 6.0, 6.0, 0.0, 10.0, 0, this.jumpGroup)).withoutNotInARow();
      booleanValue2 = this.jump;
      this.jump.getClass();
      BooleanValue booleanValue83 = booleanValue2;
      SettingVisibilityCondition condition99 = () -> booleanValue83.getValue();
      this.jumpAfterXBlocksDiagonal = (RandomizedRangeSettings)randomizedRangeSettings47.withVisibility(condition99);
      this.timeGroup = new DoubleValue(this, "TimeGroup", true, this.movement);
      this.timer = new StringValue(this, "Timer", 1.0, 0.1, 4.0, 2, this.timeGroup);
      this.timerDrop = new BooleanValue(this, "TimerDrop", false, this.timeGroup);
      RangeSetting rangeSetting10 = new RangeSetting(this, "TimerSpeed", 0.1, 50.0, 0.0, 50.0, 2, this.timeGroup);
      SettingVisibilityCondition condition100 = () -> this.isTimerSpeedVisible();
      this.timerSpeed = rangeSetting10.visibleWhen(condition100);
      this.motionMultiplier = new DoubleValue(this, "MotionMultiplier", true, this.movement);
      this.motionMultiplierAirForward = new RandomizedRangeSettings(this, "MotionMultiplierAirForward", 1.0, 1.0, 0.1, 2.0, 2, this.motionMultiplier);
      this.motionMultiplierAirStrafe = new RandomizedRangeSettings(this, "MotionMultiplierAirStrafe", 1.0, 1.0, 0.1, 2.0, 2, this.motionMultiplier);
      this.motionMultiplierGroundForward = new RandomizedRangeSettings(this, "MotionMultiplierGroundForward", 1.0, 1.0, 0.1, 2.0, 2, this.motionMultiplier);
      this.motionMultiplierGroundStrafe = new RandomizedRangeSettings(this, "MotionMultiplierGroundStrafe", 1.0, 1.0, 0.1, 2.0, 2, this.motionMultiplier);
      this.miscellaneous = new DoubleValue(this, "Miscellaneous", true);
      this.towerMode = new BooleansSetting(
         this, "TowerMode", "Legit", new String[]{"Legit", "Intave1", "Intave2", "NCP", "NCPFast", "Verus", "ConstantMotion", "Jump"}, this.miscellaneous
      );
      BooleanValue booleanValue84 = new BooleanValue(this, "TowerWhileMoving", false, this.miscellaneous);
      SettingVisibilityCondition condition101 = () -> this.isTowerWhileMovingVisible();
      this.towerWhileMoving = (BooleanValue)booleanValue84.withVisibility(condition101);
      StringValue stringValue14 = new StringValue(this, "ConstantMotion", 0.3, 0.01, 0.4, 2, this.miscellaneous);
      SettingVisibilityCondition condition102 = () -> this.isConstantMotionVisible();
      this.constantMotion = stringValue14.visibleWhen(condition102);
      StringValue stringValue15 = new StringValue(this, "JumpMotion", 0.5, 0.37, 1.0, 2, this.miscellaneous);
      SettingVisibilityCondition condition103 = () -> {
         return this.towerMode.getSelectedValue().equals("Jump");
      };
      this.jumpMotion = stringValue15.visibleWhen(condition103);
      this.silentMode = new BooleansSetting(this, "SilentMode", "Default", new String[]{"None", "Legit", "Default", "Exploit"}, this.miscellaneous);
      BooleanValue booleanValue85 = new BooleanValue(this, "UseBiggestStackSilent", false, this.miscellaneous);
      SettingVisibilityCondition condition104 = () -> this.isUseBiggestStackSilentVisible();
      this.useBiggestStackSilent = (BooleanValue)booleanValue85.withVisibility(condition104);
      RandomizedRangeSettings randomizedRangeSettings48 = new RandomizedRangeSettings(this, "SilentStartDelay", 0.0, 0.0, 0.0, 10.0, 0, this.miscellaneous);
      SettingVisibilityCondition condition105 = () -> this.isSilentStartDelayVisible();
      this.silentStartDelay = (RandomizedRangeSettings)randomizedRangeSettings48.withVisibility(condition105);
      RandomizedRangeSettings randomizedRangeSettings49 = new RandomizedRangeSettings(this, "SilentSwitchDelay", 0.0, 0.0, 0.0, 10.0, 0, this.miscellaneous);
      SettingVisibilityCondition condition106 = () -> this.isSilentSwitchDelayVisible();
      this.silentSwitchDelay = (RandomizedRangeSettings)randomizedRangeSettings49.withVisibility(condition106);
      RandomizedRangeSettings randomizedRangeSettings50 = new RandomizedRangeSettings(this, "StackCountToStartSwitchProcess", 0.0, 0.0, 0.0, 5.0, 0, this.miscellaneous);
      SettingVisibilityCondition condition107 = () -> this.isStackCountToStartSwitchVisible();
      this.stackCountToStartSwitchProcess = (RandomizedRangeSettings)randomizedRangeSettings50.withVisibility(condition107);
      RandomizedRangeSettings randomizedRangeSettings51 = (new RandomizedRangeSettings(this, "BackSwitchTickDelay", 5.0, 10.0, 0.0, 40.0, 0, this.miscellaneous)).withoutNotInARow();
      SettingVisibilityCondition condition108 = () -> this.isBackSwitchTickDelayVisible();
      this.backSwitchTickDelay = (RandomizedRangeSettings)randomizedRangeSettings51.withVisibility(condition108);
      BooleanValue booleanValue86 = new BooleanValue(this, "DynamicBackSwitchTickDelay", true, this.miscellaneous);
      SettingVisibilityCondition condition109 = () -> this.isDynamicBackSwitchTickDelayVisible();
      this.dynamicBackSwitchTickDelay = (BooleanValue)booleanValue86.withVisibility(condition109);
      this.visuals = (DoubleValue)(new DoubleValue(this, "Visuals", true)).markVisualOnly();
      this.thirdPersonMode = (BooleanValue)(new BooleanValue(this, "ThirdPersonMode", false, this.visuals)).markVisualOnly();
      BooleanValue booleanValue87 = new BooleanValue(this, "HideSneakAnimation", false, this.visuals);
      booleanValue2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue88 = booleanValue2;
      SettingVisibilityCondition condition110 = () -> booleanValue88.getValue();
      this.hideSneakAnimation = (BooleanValue)((BooleanValue)booleanValue87.withVisibility(condition110)).markVisualOnly();
      this.blockCount = (BooleanValue)(new BooleanValue(this, "BlockCount", true, this.visuals)).markVisualOnly();
      StringValue stringValue16 = new StringValue(this, "X", 50.0, 0.0, 100.0, 2, this.visuals);
      booleanValue2 = this.blockCount;
      this.blockCount.getClass();
      BooleanValue booleanValue89 = booleanValue2;
      SettingVisibilityCondition condition111 = () -> booleanValue89.getValue();
      this.x = (StringValue)stringValue16.visibleWhen(condition111).markVisualOnly();
      StringValue stringValue17 = new StringValue(this, "Y", 53.0, 0.0, 100.0, 2, this.visuals);
      booleanValue2 = this.blockCount;
      this.blockCount.getClass();
      BooleanValue booleanValue90 = booleanValue2;
      SettingVisibilityCondition condition112 = () -> booleanValue90.getValue();
      this.y = (StringValue)stringValue17.visibleWhen(condition112).markVisualOnly();
      StringValue stringValue18 = new StringValue(this, "Scale", 2.0, 1.0, 4.0, 1, this.visuals);
      booleanValue2 = this.blockCount;
      this.blockCount.getClass();
      BooleanValue booleanValue91 = booleanValue2;
      SettingVisibilityCondition condition113 = () -> booleanValue91.getValue();
      this.scale = (StringValue)stringValue18.visibleWhen(condition113).markVisualOnly();
      BooleanValue booleanValue92 = new BooleanValue(this, "BackGround", true, this.visuals);
      booleanValue2 = this.blockCount;
      this.blockCount.getClass();
      BooleanValue booleanValue93 = booleanValue2;
      SettingVisibilityCondition condition114 = () -> booleanValue93.getValue();
      this.backGround = (BooleanValue)((BooleanValue)booleanValue92.withVisibility(condition114)).markVisualOnly();
      Color color2 = new Color(0, 0, 0, 100);
      DoubleValue visuals2 = this.visuals;
      Color color3 = color2;
      String str14 = "BackGroundColor";
      ColorSetting colorSetting = new ColorSetting(this, str14, color3, visuals2);
      booleanValue2 = this.backGround;
      this.backGround.getClass();
      BooleanValue booleanValue94 = booleanValue2;
      SettingVisibilityCondition condition115 = () -> booleanValue94.getValue();
      this.backgroundColor = (ColorSetting)((ColorSetting)colorSetting.withVisibility(condition115)).markVisualOnly();
      StringValue stringValue19 = new StringValue(this, "RoundFactor", 5.0, 0.0, 11.0, 0, this.visuals);
      SettingVisibilityCondition condition116 = () -> this.isRoundFactorVisible();
      this.roundFactor = (StringValue)stringValue19.visibleWhen(condition116).markVisualOnly();
      BlurShadowSettings blurShadowSettings = new BlurShadowSettings(this, "BlockCount", this.visuals);
      Color black = Color.black;
      BlurShadowSettings blurShadowSettings2 = blurShadowSettings.withCustomShadowColor(black);
      SettingVisibilityCondition condition117 = () -> this.isBlockCountShadowVisible();
      this.blockCount2 = (BlurShadowSettings)((BlurShadowSettings)blurShadowSettings2.withVisibility(condition117)).markAllVisualOnly();
      this.esp = (BooleanValue)(new BooleanValue(this, "ESP", true, this.visuals)).markVisualOnly();
      color2 = new Color(41, 146, 222, 100);
      visuals2 = this.visuals;
      color3 = color2;
      str14 = "Color";
      ColorSetting colorSetting2 = new ColorSetting(this, str14, color3, visuals2);
      booleanValue2 = this.esp;
      this.esp.getClass();
      BooleanValue booleanValue95 = booleanValue2;
      SettingVisibilityCondition condition118 = () -> booleanValue95.getValue();
      this.espColor = (ColorSetting)((ColorSetting)colorSetting2.withVisibility(condition118)).markVisualOnly();
      BlurShadowSettings blurShadowSettings3 = new BlurShadowSettings(this, "ESP", this.visuals);
      Color black2 = Color.black;
      BlurShadowSettings blurShadowSettings4 = blurShadowSettings3.withCustomShadowColor(black2);
      booleanValue2 = this.esp;
      this.esp.getClass();
      BooleanValue booleanValue96 = booleanValue2;
      SettingVisibilityCondition condition119 = () -> booleanValue96.getValue();
      this.esp2 = (BlurShadowSettings)((BlurShadowSettings)blurShadowSettings4.withVisibility(condition119)).markAllVisualOnly();
      this.lastPlacedBlockPos = null;
      this.targetBlockPos = null;
      this.tellyPredictedBlockDistance = 0;
      this.blockPlacementData = null;
      this.overrideRayTrace = null;
      this.rotationUtil = new RotationUtil();
      this.bezierRotationStepper = new BezierRotationStepper();
      this.bezierCurve = null;
      this.rotationKinematicState = null;
      this.lastBezierRotation = null;
      this.bezierTickCounter = 0;
      this.lastRightClickTimer = new TimeHelper();
      this.lastPlaceTimer = new TimeHelper();
      this.enableTimer = new TimeHelper();
      this.sneakHoldTicks = 0;
      this.blocksSinceLastSneak = 0;
      this.placedBlocksSinceSneak = 0;
      this.sneakEdgeExpandDist = 0.0;
      this.offGroundTicks = 0;
      this.wasSneakingLastTick = false;
      this.isOverSupportBlock = false;
      this.sneakIntervalBlocksForward = 0;
      this.sneakIntervalBlocksDiagonal = 0;
      this.intave14TimeBlockPlaceCheck = new Intave14TimeBlockPlaceCheck("Intave14SusBlockPlacementTime", null, false);
      this.strafeYawOffset = 45.0F;
      this.hotbarBlockCount = 0;
      this.bridgeClickController = new BridgeClickController(this.cps, this.dcpsMode, this.dcps, this.alwaysPlaceWhenPossible, this.oneClick, this.delayClick);
      this.moonWalkStrafeDir = 1;
      this.keyBindStrafeCorrectionDir = 1;
      this.moonWalkEdgeExpandDist = 0.0;
      this.ticksSinceLastPlace = 0;
      this.moonWalkTicks = 0;
      this.moonWalkDirChangeTicks = 0;
      this.lastAppliedStrafeInput = 0;
      this.wasOverMoonWalkBlock = false;
      this.minOffGroundSneakForward = 0;
      this.maxOffGroundSneakForward = 10;
      this.minOffGroundSneakDiagonal = 0;
      this.maxOffGroundSneakDiagonal = 10;
      this.expandAirYOffset = 0.0;
      this.godBridgeResetting = false;
      this.godBridgePlacedBlocks = 0;
      this.godBridgeSetupTicks = 0;
      this.godBridgeJumpCount = 0;
      this.ticksSinceGodBridgePlace = 0;
      this.godBridgeSneakSetupTicks = 0;
      this.lastGodBridgePosX = 0.0;
      this.lastGodBridgePosZ = 0.0;
      this.wasCardinalDirection = false;
      this.safeWalkEdgeDetected = false;
      this.godBridgeJumpHeld = false;
      this.blocksPlacedSinceJump = 0;
      this.yawRandomizationTriggered = false;
      this.pitchRandomizationTriggered = false;
      this.yawRandomizeCounter = new TickCounter();
      this.pitchRandomizeCounter = new TickCounter();
      this.rightClickHeldOnGround = false;
      this.velocityAnchorPos = null;
      this.watchdogTowerTick = 0;
      this.hoverJumpStarted = false;
      this.hoverOffsetActive = false;
      this.strafeCorrectionCounter = new TickCounter();
      this.strafeMiddleRecalcCounter = new TickCounter();
      this.strictYawErrorSign = 1;
      this.lastStrictYawSector = 0;
      this.strafeCorrectionActive = false;
      this.silentSwitchCounter = new TickCounter();
      this.firstSilentSwitchPending = false;
      this.selectedBlockSlot = -1;
      this.slotMinStackThresholds = new HashMap();
      this.stopAtStartDelayMs = 0;
      this.lastMoonWalkSide = 0;
      this.strafeCorrectionMiddle = 0.0;
      this.moonWalkMiddleRecalcCounter = new TickCounter();
      this.directionChangeDelayCounter = new TickCounter();
      this.directionChangePending = false;
      this.reducingVelocity = false;
      this.startEdgeSneakTicks = 0;
      this.startAtEdgeDelayCounter = new TickCounter();
      this.initialStrafeDirectionPending = false;
      this.savedThirdPersonView = 0;
      this.ticksSinceDirectionChange = 0;
      this.keepInitialPitch = false;
      this.sneakingOnGroundForEdge = false;
      this.placedBlockSinceEnable = false;
      this.minimalMouseMoveTimer = new TimeHelper();
      this.minimalMouseMoveCounter = new TickCounter();
      this.minimalMouseMoveIntervalMs = 0;
      this.minimalMouseMoveDurationTicks = 0;
      this.sameYBlockJumpActive = false;
      this.sameYBlockBaseY = 0.0;
      this.sameYBlockResetPending = false;
      this.sameYBlockTopPlaced = false;
      this.stopMovementUntilLand = false;
      this.intaveBlockPlacePenalty = 0.0;
   }

   public static void initScaffold() {
   }

   @Override
   public void onEnable() {
      this.targetBlockPos = null;
      this.tellyPredictedBlockDistance = 0;
      this.lastPlacedBlockPos = null;
      this.minimalMouseYawOffset = 0.0F;
      this.minimalMousePitchOffset = 0.0F;
      this.sneakIntervalBlocksDiagonal = 0;
      this.godBridgePlacedBlocks = 0;
      this.placedBlockSinceEnable = false;
      this.blocksSinceLastSneak = 0;
      this.godBridgeResetting = true;
      this.godBridgeSetupTicks = 0;
      this.safeWalkEdgeDetected = false;
      this.godBridgeJumpCount = 0;
      this.godBridgeJumpHeld = false;
      this.sneakIntervalBlocksForward = 0;
      mc.getTimer().timerSpeed = 1.0F;
      this.enableTimer.reset();
      this.blocksSinceLastSneak = 1000;
      this.placedBlocksSinceSneak = 1000;
      this.offGroundTicks = 0;
      this.strafeYawOffset = this.strafeYawOffset > 0.0F ? (float)this.strafeDegree.nextValue() : (float)(-this.strafeDegree.nextValue());
      this.startDelay.nextValue();
      this.jumpDelayAfterPlace.nextValue();
      this.resetGodBridgeAfterXJumps.nextValue();
      this.jumpAfterXBlocks.nextValue();
      byte b = 7;
      byte b2 = 5;
      this.godBridgeSneakSetupTicks = DefaultRandomGenerator.instance.nextInt2(b2, b);
      this.jumpAfterXBlocks.nextValue();
      this.sneakTicks.nextValue();
      this.blocksPlacedSinceJump = this.jumpOnlyWhileSpacePressed.getValue() ? 10000 : 0;
      this.watchdogTowerTick = 0;
      this.hoverJumpStarted = false;
      this.hoverOffsetActive = false;
      this.strafeDegree.nextValue();
      this.previousTargetBlockPos = null;
      this.firstSilentSwitchPending = true;
      this.silentStartDelay.nextValue();
      short s = 255;
      short s2 = 145;
      this.stopAtStartDelayMs = DefaultRandomGenerator.instance.nextInt2(s2, s);
      byte b3 = 3;
      byte b4 = 1;
      this.startEdgeSneakTicks = DefaultRandomGenerator.instance.nextInt2(b4, b3);
      byte b5 = 3;
      byte b6 = 1;
      this.postDirectionChangeSneakTicks = DefaultRandomGenerator.instance.nextInt2(b6, b5);
      this.strafeCorrectionMiddle = this.rollStrafeCorrectionMiddle();
      this.staticPitch.nextValue();
      this.staticPitchDiagonal.nextValue();
      this.backSwitchTickDelay.nextValue();
      this.reducingVelocity = false;
      this.waitingForStartEdge = true;
      this.hasPlacedFirstBlock = false;
      this.ticksSinceFirstPlace = 0;
      this.overrideRayTrace = null;
      this.startAtEdgeDelay.nextValue();
      this.initialStrafeDirectionPending = true;
      this.directionChangePending = false;
      this.savedThirdPersonView = mc.gameSettings.thirdPersonView;
      this.keepInitialPitch = false;
      this.lastStrictYawSector = -1;
      this.sameYBlockTopPlaced = false;
      this.sameYBlockResetPending = false;
      this.sameYBlockJumpActive = false;
      if (mc.thePlayer != null) {
         this.sameYBlockBaseY = mc.thePlayer.lastGroundY;
      }

      this.stopMovementUntilLand = false;
      this.keepRotationsAfterModuleDisable.nextValue();
   }

   @Override
   public void onDisable() {
      mc.getTimer().timerSpeed = 1.0F;
      this.selectedBlockSlot = -1;
      this.slotMinStackThresholds.clear();
      this.rotationKinematicState = null;
      this.bezierCurve = null;
      this.bezierTickCounter = 0;
      if (this.thirdPersonMode.getValue() && mc.gameSettings != null) {
         mc.gameSettings.thirdPersonView = this.savedThirdPersonView;
      }
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 28;
      this.toggleEventSubscription(b);
      byte b2 = 20;
      byte b3 = 36;
      this.toggleEventSubscription(b3, b2);
      byte b4 = 7;
      this.toggleEventSubscription(b4);
      byte b5 = 40;
      this.toggleEventSubscription(b5);
      byte b6 = 24;
      this.toggleEventSubscription(b6);
      byte b7 = 20;
      this.toggleEventSubscription(b7);
      byte b8 = 5;
      byte b9 = 21;
      this.toggleEventSubscription(b9, b8);
      byte b10 = 18;
      this.toggleEventSubscription(b10);
      byte b11 = 32;
      this.toggleEventSubscription(b11);
      byte b12 = 29;
      this.toggleEventSubscription(b12);
      byte b13 = 26;
      this.toggleEventSubscription(b13);
      byte b14 = 33;
      this.toggleEventSubscription(b14);
      byte b15 = 39;
      this.toggleEventSubscription(b15);
      byte b16 = 34;
      this.toggleEventSubscription(b16);
      byte b17 = 0;
      this.toggleEventSubscription(b17);
      byte b18 = 43;
      this.toggleEventSubscription(b18);
   }

   @Override
   public void onUpdate(EventUpdate event) {
      if (this.thirdPersonMode.getValue()) {
         mc.gameSettings.thirdPersonView = 1;
      }

      if (!this.waitingForStartEdge) {
         if (this.canRunScaffoldInCurrentScreen()
            && this.targetBlockPos != null
            && (this.towerWhileMoving.getValue() && mc.gameSettings.keyBindJump.isKeyDown() || this.isStationaryTowering())
            && BlockUtil.isValidScaffoldBlock(mc.thePlayer.getCurrentEquippedItem())) {
            String selectedValue = this.towerMode.getSelectedValue();
            switch (selectedValue) {
               case "Intave1":
                  if (mc.thePlayer.onGround) {
                     mc.thePlayer.motionY = 0.405;
                  }

                  if (this.offGroundTicks == 5) {
                     mc.thePlayer.motionY = (mc.thePlayer.motionY - 0.08) * 0.98F;
                  }
                  break;
               case "Intave2":
                  if (mc.thePlayer.onGround) {
                     mc.thePlayer.motionY = 0.405;
                  }

                  if (this.offGroundTicks == 5) {
                     double d = 0.9909900590734863;
                     double d2 = 0.9908900590734863;
                     float f = DefaultRandomGenerator.instance.nextFloat2(d2, d);
                     mc.thePlayer.motionY = (mc.thePlayer.motionY - 0.08) * (double)f;
                  }
                  break;
               case "NCP":
                  if (mc.thePlayer.onGround) {
                     mc.thePlayer.jump();
                     this.ncpTowerStartY = mc.thePlayer.posY;
                  } else if (mc.thePlayer.posY > this.ncpTowerStartY + 1.0) {
                     mc.thePlayer.motionY = -0.1;
                  }
                  break;
               case "Verus":
                  if (mc.thePlayer.ticksExisted % 2 == 0) {
                     mc.thePlayer.jump();
                  }
                  break;
               case "ConstantMotion":
                  mc.thePlayer.motionY = this.constantMotion.getValue();
                  break;
               case "Jump":
                  if (mc.thePlayer.onGround) {
                     mc.thePlayer.motionY = this.jumpMotion.getValue();
                  }
                  break;
               case "NCPFast":
                  if (this.offGroundTicks <= 1) {
                     this.offGroundTicks = 0;
                     if (mc.thePlayer.ticksExisted % 3 == 0) {
                        mc.thePlayer.motionY = 0.4196;
                     }
                  }
                  break;
               case "Watchdog":
                  if (!mc.gameSettings.keyBindForward.isKeyDown()) {
                     MoveUtil.setSpeed2(0.0F);
                     this.watchdogTowerTick = 0;
                  } else {
                     this.watchdogTowerTick++;
                     if (mc.thePlayer.onGround) {
                        this.watchdogTowerTick = 0;
                     }

                     mc.thePlayer.motionY = 0.41965;
                     MoveUtil.setSpeed(0.241F, false);
                     double posY = mc.thePlayer.posY;
                     if (this.watchdogTowerTick == 1) {
                        mc.thePlayer.motionY = 0.33;
                     } else if (this.watchdogTowerTick == 2) {
                        mc.thePlayer.motionY = 1.0 - posY % 1.0;
                     } else if (this.watchdogTowerTick >= 3) {
                        this.watchdogTowerTick = 0;
                     }
                  }
            }
         }

         if (this.sameYBlock.getValue()) {
            if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
               this.sameYBlockResetPending = true;
               this.sameYBlockTopPlaced = false;
               this.sameYBlockJumpActive = false;
            }

            if (this.sameYBlockResetPending && mc.thePlayer.onGround) {
               this.sameYBlockBaseY = mc.thePlayer.posY;
               this.sameYBlockResetPending = false;
            }
         } else {
            this.sameYBlockTopPlaced = false;
            this.sameYBlockJumpActive = false;
         }
      }
   }

   @Override
   public void onTick() {
      if (mc.thePlayer.onGround) {
         this.rightClickHeldOnGround = mc.gameSettings.keyBindUseItem.isKeyDown();
      }

      if (this.isEarlySneakCalculationActive()) {
         this.isOverSupportBlock = false;
         this.hotbarBlockCount = this.countHotbarBlocks();
         List list = mc.theWorld.getCollidingBoundingBoxesCapped(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -1.0, 0.0));
         boolean flag = Math.abs(mc.thePlayer.motionX) > 0.02;
         boolean flag2 = Math.abs(mc.thePlayer.motionZ) > 0.02;

         for (AxisAlignedBB box : (Iterable<AxisAlignedBB>)(list)) {
            if (mc.thePlayer
               .getEntityBoundingBox()
               .expand(
                  -Math.abs(mc.thePlayer.getEntityBoundingBox().minX - mc.thePlayer.getEntityBoundingBox().maxX) / 2.0,
                  0.0,
                  -Math.abs(mc.thePlayer.getEntityBoundingBox().minZ - mc.thePlayer.getEntityBoundingBox().maxZ) / 2.0
               )
               .offset(0.0, -1.0, 0.0)
               .intersectsWith(box.expand(flag ? this.sneakEdgeExpandDist : 0.0, 0.0, flag2 ? this.sneakEdgeExpandDist : 0.0))) {
               this.isOverSupportBlock = true;
               break;
            }
         }
      }

      if (mc.thePlayer.onGround) {
         this.offGroundTicks = 0;
      } else {
         this.offGroundTicks++;
      }

      if (this.canRunScaffoldInCurrentScreen()) {
         this.blockPlacementData = this.findBlockPlacementData();
         BlockPos targetBlockPos2 = this.targetBlockPos;
         if (this.blockPlacementData != null) {
            this.targetBlockPos = this.blockPlacementData.getBlockPos();
         } else {
            this.targetBlockPos = null;
         }

         exitBlock: {
            if (this.startAtEdge.getValue() && !mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ).getBlock().getMaterial().isReplaceable()) {
               boolean flag3 = true;
               if (!this.checkBlockClutchState(flag3)) {
                  break exitBlock;
               }
            }

            this.waitingForStartEdge = false;
         }

         if (this.previousTargetBlockPos == null) {
            this.previousTargetBlockPos = this.targetBlockPos;
         }

         if (this.targetBlockPos != null && targetBlockPos2 != null && !this.targetBlockPos.equalsPos(targetBlockPos2)) {
            this.previousTargetBlockPos = targetBlockPos2;
         }

         this.overrideRayTrace = null;
         if (!this.waitingForStartEdge) {
            if (this.expand.getValue()) {
               this.updateExpandRotation();
            } else {
               this.updateStandardRotation();
            }
         } else {
            this.sameYBlockBaseY = mc.thePlayer.lastGroundY;
         }
      }

      this.yawRandomizeCounter.increment();
      this.pitchRandomizeCounter.increment();
   }

   private void swingHandIfAllowed() {
      if (!this.noSwing.getValue() || !this.serverSideNoSwing.getValue()) {
         mc.thePlayer.swingItem();
      }
   }

   private int switchToExploitSlot() {
      if (!this.silentMode.getSelectedValue().equals("Exploit")) {
         return -1;
      } else {
         int selectedBlockSlot2 = this.getSelectedBlockSlot();
         int currentItem = mc.thePlayer.inventory.currentItem;
         if (selectedBlockSlot2 >= 0) {
            mc.thePlayer.inventory.currentItem = selectedBlockSlot2;
         }

         return currentItem;
      }
   }

   private void restoreExploitSlot(int n) {
      if (this.silentMode.getSelectedValue().equals("Exploit")) {
         if (n >= 0) {
            mc.thePlayer.inventory.currentItem = n;
         }
      }
   }

   @Override
   public void onClick(EventClick event) {
      this.strafeCorrectionCounter.increment();
      this.strafeMiddleRecalcCounter.increment();
      this.directionChangeDelayCounter.increment();
      if (!this.waitingForStartEdge) {
         event.cancel();
         boolean flag = true;
         event.setBlockAllClicks(flag);
         this.intaveBlockPlacePenalty = 0.0;
         double d = (double)Math.abs(ClientManagers.rotationController.getCurrentRotation().yaw - ClientManagers.rotationController.getPreviousRotation().yaw);
         double d2 = (double)Math.abs(ClientManagers.rotationController.getCurrentRotation().pitch - ClientManagers.rotationController.getPreviousRotation().pitch);
         this.startAtEdgeDelayCounter.increment();
         boolean flag2 = d2 > 2.0 || d > 2.0;
         if (flag2) {
            this.startAtEdgeDelayCounter.reset();
         }

         boolean flag3;
         exitBlock: {
            exitBlock2: {
               if (!flag2) {
                  TickCounter tickCounter = this.startAtEdgeDelayCounter;
                  int lastIntValue = this.startAtEdgeDelay.getLastIntValue();
                  if (tickCounter.hasReached2(lastIntValue)) {
                     break exitBlock2;
                  }
               }

               if (this.startAtEdge.getValue() && !this.hasPlacedFirstBlock) {
                  boolean flag4 = true;
                  if (!this.checkBlockClutchState(flag4) || !this.ignoreStartAtEdgeAtBlockClutch.getValue()) {
                     flag3 = false;
                     break exitBlock;
                  }
               }
            }

            flag3 = true;
         }

         boolean flag5;
         flag5 = flag3;
         double d3 = Math.abs(d);
         double d4 = Math.abs(d2);
         exitBlock3:
         if (this.isBlockClutching()) {
            boolean flag6 = true;
            if (!(d3 > this.maxYawRotationDeltaToPlaceBlocksAtBlockClutch.getValue(flag6))) {
               boolean flag7 = true;
               if (!(d4 > this.maxPitchRotationDeltaToPlaceBlocksAtBlockClutch.getValue(flag7))) {
                  break exitBlock3;
               }
            }

            flag5 = false;
         } else {
            exitBlock4: {
               boolean flag8 = true;
               if (!(d3 > this.maxYawRotationDeltaToPlaceBlocks.getValue(flag8))) {
                  boolean flag9 = true;
                  if (!(d4 > this.maxPitchRotationDeltaToPlaceBlocks.getValue(flag9))) {
                     break exitBlock4;
                  }
               }

               flag5 = false;
            }
         }

         if (this.stopClickingAtSneaking.getValue() && this.sneakingOnGroundForEdge && this.placedBlockSinceEnable) {
            flag5 = false;
         }

         this.ticksSinceDirectionChange++;
         if (this.strictYaw.getValue() && this.directionChangePending && (!this.ignoreStartDelayAfterDirectionChangeAtBlockClutch.getValue() || !this.isBlockClutching()) && this.hasPlacedFirstBlock) {
            if (flag2) {
               this.directionChangeDelayCounter.reset();
            }

            TickCounter tickCounter2 = this.directionChangeDelayCounter;
            int lastIntValue2 = this.startDelayAfterDirectionChange.getLastIntValue();
            if (!tickCounter2.hasReached2(lastIntValue2)) {
               flag5 = false;
               this.ticksSinceDirectionChange = 0;
            } else {
               byte b = 3;
               byte b2 = 1;
               this.postDirectionChangeSneakTicks = DefaultRandomGenerator.instance.nextInt2(b2, b);
               this.startDelayAfterDirectionChange.nextValue();
               this.directionChangePending = false;
            }
         }

         if (flag5 && this.targetBlockPos != null) {
            if (this.canRunScaffoldInCurrentScreen()
               && (!mc.playerController.isHittingBlock() || ClientManagers.moduleManager.fixes.isPlaceWhileBreakEnabled())
               && (BlockUtil.isValidScaffoldBlock(mc.thePlayer.getCurrentEquippedItem()) || this.silentMode.getSelectedValue().equals("Exploit"))) {
               if (this.expand.getValue() && !this.isStationaryTowering()) {
                  MovingObjectPosition hit = new MovingObjectPosition(this.blockPlacementData.getHitVec(), this.blockPlacementData.getFacing(), this.blockPlacementData.getBlockPos());
                  if (this.clickPause.getValue() && !this.bridgeClickController.shouldClickAtEdge()) {
                     return;
                  }

                  BridgeClickController bridgeClickController2 = this.bridgeClickController;
                  ScaffoldPlaceCondition scaffoldPlaceCondition = () -> {
                     boolean flag10 = true;
                     int n = -1;
                     if (this.silentMode.getSelectedValue().equals("Exploit")) {
                        int n2 = this.getSelectedBlockSlot() + 36;
                        ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(n2).getStack();
                        BlockPos blockPos = this.blockPlacementData.getBlockPos();
                        Vec3 hitVec = this.blockPlacementData.getHitVec();
                        EnumFacing facing = this.blockPlacementData.getFacing();
                        BlockPos pos = blockPos;
                        if (!PlayerUtil.canPlaceBlockAt(stack, pos, facing, hitVec)) {
                           return false;
                        }

                        n = this.switchToExploitSlot();
                     }

                     if (mc.playerController
                        .onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), hit.getBlockPos(), hit.sideHit, hit.hitVec)) {
                        this.hasPlacedFirstBlock = true;
                        this.placedBlocksSinceSneak++;
                        this.godBridgePlacedBlocks++;
                        this.blocksPlacedSinceJump++;
                        this.ticksSinceGodBridgePlace = 0;
                        this.lastRightClickTimer.reset();
                        this.lastPlaceTimer.reset();
                        flag10 = false;
                        RandomNumberGenerator instance = DefaultRandomGenerator.instance;
                        double minValue = this.moonWalkStartDistToBlock.getMinValue();
                        double maxValue = this.moonWalkStartDistToBlock.getMaxValue();
                        double d5 = maxValue;
                        double d6 = minValue;
                        this.moonWalkEdgeExpandDist = -instance.nextDouble(d6, d5);
                        this.ticksSinceLastPlace = 0;
                        this.postPlacementStrafeTicks.nextValue();
                        this.placedBlockSinceEnable = true;
                        this.lastPlacedBlockPos = hit.getBlockPos().offset(hit.sideHit);
                        this.swingHandIfAllowed();
                     }

                     if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().stackSize == 0) {
                        mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem] = null;
                     }

                     if (flag10) {
                        this.lastRightClickTimer.reset();
                        if (mc.thePlayer.getCurrentEquippedItem() != null) {
                           if (mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem())) {
                              mc.entityRenderer.itemRenderer.resetEquippedProgress2();
                           }
                        }
                     }

                     this.restoreExploitSlot(n);
                     return false;
                  };
                  bridgeClickController2.handleBlockClick(hit, event, scaffoldPlaceCondition);
                  return;
               }

               if (mc.objectMouseOver == null) {
                  return;
               }

               if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
                  && mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock().getMaterial() != Material.air
                  && (!this.godBridge.getValue() || !this.godBridgeResetting || this.godBridgeJumpHeld)) {
                  BlockPos blockPos2 = mc.objectMouseOver.getBlockPos();
                  EnumFacing sideHit = mc.objectMouseOver.sideHit;
                  if (sideHit == EnumFacing.UP) {
                     ItemStack currentEquippedItem = mc.thePlayer.getCurrentEquippedItem();
                     boolean flag11 = PlayerUtil.canPlaceHeldItemAtMouseOver(currentEquippedItem);
                     if ((!this.sameY.getValue() || !flag11 || this.rightClickHeldOnGround || this.isStationaryTowering())
                        && blockPos2.equalsPos(this.targetBlockPos)
                        && this.canClickWithPause()
                        && (!this.hover.getValue() || this.hoverOffsetActive)
                        && !this.sameYBlockTopPlaced) {
                        exitBlock5: {
                           if (!(ClientManagers.rotationController.getPreviousRotation().pitch < 85.0F) && this.intave.getValue() && !this.otherRotation.getValue()) {
                              ItemStack currentEquippedItem2 = mc.thePlayer.getCurrentEquippedItem();
                              if (PlayerUtil.canPlaceHeldItemAtMouseOver(currentEquippedItem2)) {
                                 flag3 = false;
                                 break exitBlock5;
                              }
                           }

                           flag3 = true;
                        }

                        boolean flag12 = flag3;
                        double d7;
                        if (this.intave.getValue() && this.intaveSafe.getValue()) {
                           Intave14TimeBlockPlaceCheck check = this.intave14TimeBlockPlaceCheck;
                           BlockPos pos2 = blockPos2.offset(sideHit);
                           BlockPos blockPos3 = mc.objectMouseOver.getBlockPos();
                           BlockPos pos3 = pos2;
                           d7 = check.computePlaceTimeDeficit(pos3, blockPos3);
                        } else {
                           d7 = 0.0;
                        }

                        this.intaveBlockPlacePenalty = d7;
                        BridgeClickController bridgeClickController3 = this.bridgeClickController;
                        MovingObjectPosition objectMouseOver = mc.objectMouseOver;
                        ScaffoldPlaceCondition scaffoldPlaceCondition2 = () -> this.placeTopBlock(flag12);
                        MovingObjectPosition hit2 = objectMouseOver;
                        bridgeClickController3.handleBlockClick(hit2, event, scaffoldPlaceCondition2);
                     }
                  } else if ((
                        blockPos2.equalsPos(this.targetBlockPos)
                           || (
                                 mc.theWorld
                                          .getCollidingBoundingBoxes(
                                             mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001)
                                          )
                                          .isEmpty()
                                       && mc.thePlayer.onGround
                                    || ClientManagers.rotationController.getCurrentRotation().pitch == ClientManagers.rotationController.getPreviousRotation().pitch
                              )
                              && !this.sameYBlockJumpActive
                     )
                     && this.canClickWithPause()
                     && this.canPlaceAtEdge()) {
                     double d8;
                     if (this.intave.getValue() && this.intaveSafe.getValue()) {
                        Intave14TimeBlockPlaceCheck check2 = this.intave14TimeBlockPlaceCheck;
                        BlockPos pos4 = mc.objectMouseOver.getBlockPos().offset(mc.objectMouseOver.sideHit);
                        BlockPos blockPos4 = mc.objectMouseOver.getBlockPos();
                        BlockPos pos5 = pos4;
                        d8 = check2.computePlaceTimeDeficit(pos5, blockPos4);
                     } else {
                        d8 = 0.0;
                     }

                     exitBlock6: {
                        this.intaveBlockPlacePenalty = d8;
                        if (!(ClientManagers.rotationController.getPreviousRotation().pitch < 85.0F) && this.intave.getValue() && !this.otherRotation.getValue()) {
                           ItemStack currentEquippedItem3 = mc.thePlayer.getCurrentEquippedItem();
                           if (PlayerUtil.canPlaceHeldItemAtMouseOver(currentEquippedItem3)) {
                              flag3 = false;
                              break exitBlock6;
                           }
                        }

                        flag3 = true;
                     }

                     boolean flag13 = flag3;
                     BridgeClickController bridgeClickController4 = this.bridgeClickController;
                     MovingObjectPosition objectMouseOver2 = mc.objectMouseOver;
                     ScaffoldPlaceCondition scaffoldPlaceCondition3 = () -> this.placeSideBlock(flag13);
                     boolean flag14 = this.wouldFallNextTick();
                     ScaffoldPlaceCondition scaffoldPlaceCondition4 = scaffoldPlaceCondition3;
                     MovingObjectPosition hit3 = objectMouseOver2;
                     bridgeClickController4.handleBlockClick(hit3, event, scaffoldPlaceCondition4, flag14);
                  }
               }

               return;
            }
         }
      }
   }

   private boolean canClickWithPause() {
      return !this.clickPause.getValue() || this.bridgeClickController.shouldClickAtEdge();
   }

   public boolean canPlaceAtEdge() {
      return this.isAtBlockEdge() && this.edgeRotateCheck.getValue() && this.edgePlaceCheck.getValue() || !this.edgeRotateCheck.getValue() || !this.edgePlaceCheck.getValue();
   }

   @Override
   public void onPacket2(EventPacket event) {
      if (this.intave.getValue() && this.intaveSafe.getValue() && this.waitingForStartEdge) {
         Intave14TimeBlockPlaceCheck check = this.intave14TimeBlockPlaceCheck;
         Packet packet = event.getPacket();
         check.onClientPacket(packet);
      }
   }

   @Override
   public void onMoveInput2(EventMoveInput event) {
      if (this.canRunScaffoldInCurrentScreen()) {
         if (!this.waitingForStartEdge) {
            if (MoveUtil.isMoving()) {
               if (this.isTellyBridgeEnabled() && this.blocksSinceLastSneak > 0 && !mc.thePlayer.isSprinting()) {
                  boolean flag = false;
                  event.setJump(flag);
               }
            }

            if (this.sameY.getValue()
               && this.autoJump.getValue()
               && (MoveUtil.hasHorizontalMotion() || MoveUtil.isMoving())
               && (this.hotbarBlockCount > 5 || mc.playerController.isInCreativeMode())
               && mc.thePlayer.onGround) {
               if (!mc.thePlayer.isSprinting()) {
                  if (this.isTellyBridgeEnabled()) {
                     return;
                  }
               }

               boolean flag2 = true;
               event.setJump(flag2);
               if (this.isTellyBridgeEnabled() && mc.thePlayer.onGround) {
                  double[] doubles = PlayerUtil.predictPlayerPosition(9, mc.thePlayer);
                  double d = doubles[0];
                  double d2 = doubles[2];
                  double d3 = d2;
                  double d4 = d;
                  BlockPlacementData blockPlacementData2 = this.blockPlacementData;
                  this.tellyPredictedBlockDistance = this.countVoxelStepsToTarget(blockPlacementData2, d4, d3);
               }
            }
         }
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      this.sneakingOnGroundForEdge = false;
      this.moonWalkMiddleRecalcCounter.increment();
      boolean isKeyDown = mc.gameSettings.keyBindJump.isKeyDown();
      if (this.canRunScaffoldInCurrentScreen()) {
         if (!this.waitingForStartEdge) {
            if (this.startAtEdge.getValue() && (!this.hasPlacedFirstBlock || this.ticksSinceFirstPlace < this.startEdgeSneakTicks)) {
               boolean flag = true;
               event.setSneak(flag);
            }

            if (this.strictYaw.getValue() && (!this.ignoreStartDelayAfterDirectionChangeAtBlockClutch.getValue() || !this.isBlockClutching())) {
               if (this.startDelayAfterDirectionChange.getMaxValue() > 0.0 && (this.directionChangePending || this.ticksSinceDirectionChange <= this.postDirectionChangeSneakTicks)) {
                  boolean flag2 = true;
                  event.setSneak(flag2);
               }
            }

            if (this.hasPlacedFirstBlock) {
               this.ticksSinceFirstPlace++;
            }

            if (!this.isEarlySneakCalculationActive()) {
               this.isOverSupportBlock = false;
               this.hotbarBlockCount = this.countHotbarBlocks();
               List collidingBoundingBoxes = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -1.0, 0.0));
               boolean flag3 = Math.abs(mc.thePlayer.motionX) > 0.01;
               boolean flag4 = Math.abs(mc.thePlayer.motionZ) > 0.01;

               for (AxisAlignedBB box : (Iterable<AxisAlignedBB>)(collidingBoundingBoxes)) {
                  if (mc.thePlayer
                     .getEntityBoundingBox()
                     .expand(
                        Math.abs(mc.thePlayer.motionX) < 0.002
                           ? 0.0
                           : -Math.abs(mc.thePlayer.getEntityBoundingBox().minX - mc.thePlayer.getEntityBoundingBox().maxX) / 2.0,
                        0.0,
                        Math.abs(mc.thePlayer.motionZ) < 0.002
                           ? 0.0
                           : -Math.abs(mc.thePlayer.getEntityBoundingBox().minZ - mc.thePlayer.getEntityBoundingBox().maxZ) / 2.0
                     )
                     .offset(0.0, -1.0, 0.0)
                     .intersectsWith(box.expand(flag3 ? this.sneakEdgeExpandDist : 0.0, 0.0, flag4 ? this.sneakEdgeExpandDist : 0.0))) {
                     this.isOverSupportBlock = true;
                     break;
                  }
               }
            }

            if ((double)Math.abs(ClientManagers.rotationController.getPreviousRotation().yaw - ClientManagers.rotationController.getLastTickRotation().yaw) > 1.5
               || (double)Math.abs(ClientManagers.rotationController.getCurrentRotation().yaw - ClientManagers.rotationController.getPreviousRotation().yaw) > 1.5) {
               if (this.sneakAtRotation.getValue()) {
                  boolean flag5 = true;
                  event.setSneak(flag5);
               }

               if (this.stopAtRotation.getValue()) {
                  float f = 0.0F;
                  event.setMoveForward(f);
                  float f2 = 0.0F;
                  event.setMoveStrafe(f2);
               }
            }

            if (this.hover.getValue() && !this.hoverJumpStarted && mc.thePlayer.onGround) {
               boolean flag6 = true;
               event.setJump(flag6);
               this.hoverJumpStarted = true;
            }

            if (this.reduceVelocity.getValue()) {
               if (mc.thePlayer.hurtTime == 9) {
                  this.reducingVelocity = true;
               } else if (mc.thePlayer.hurtTime == 0) {
                  this.reducingVelocity = false;
               }

               if (this.reducingVelocity) {
                  double posX = mc.thePlayer.posX;
                  double posZ = mc.thePlayer.posZ;
                  if (mc.thePlayer.hurtTime == 9) {
                     this.velocityAnchorPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                     posX += mc.thePlayer.motionX;
                     posZ += mc.thePlayer.motionZ;
                  }

                  if (this.velocityAnchorPos != null) {
                     double d = this.velocityAnchorPos.xCoord - posX;
                     double d2 = this.velocityAnchorPos.zCoord - posZ;
                     if (Math.sqrt(d * d + d2 * d2) < 0.15) {
                        this.reducingVelocity = false;
                     }

                     float f3 = (float)(MathHelper.atan2(d2, d) * 180.0 / Math.PI - 90.0);
                     event.setYaw(f3);
                  }
               }
            }

            if (event.getMoveForward() == 0.0F && event.getMoveStrafe() == 0.0F && this.godBridge.getValue() && !this.godBridgeResetting && mc.thePlayer.onGround) {
               this.resetGodBridgeState();
            }

            if (this.targetBlockPos != null
               && (this.towerWhileMoving.getValue() && isKeyDown || this.isStationaryTowering())
               && !this.towerMode.getSelectedValue().equals("Intave1")
               && !this.towerMode.getSelectedValue().equals("Intave2")
               && !this.towerMode.getSelectedValue().equals("Legit")) {
               boolean flag7 = false;
               event.setJump(flag7);
            }

            if (this.sneakNoBlocks.getValue()) {
               ItemStack currentEquippedItem = mc.thePlayer.getCurrentEquippedItem();
               if (!BlockUtil.isValidScaffoldBlock(currentEquippedItem)
                  && (this.getSelectedBlockSlot() < 0 && !this.firstSilentSwitchPending || this.silentMode.getSelectedValue().equals("None"))
                  && mc.thePlayer.onGround
                  && !mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ)).getBlock().isFullBlock()) {
                  boolean flag8 = true;
                  event.setSneak(flag8);
               }
            }

            if (this.stopNoBlocks.getValue()) {
               ItemStack currentEquippedItem2 = mc.thePlayer.getCurrentEquippedItem();
               if (!BlockUtil.isValidScaffoldBlock(currentEquippedItem2) && (this.getSelectedBlockSlot() < 0 && !this.firstSilentSwitchPending || this.silentMode.getSelectedValue().equals("None"))) {
                  float f4 = 0.0F;
                  event.setMoveForward(f4);
                  float f5 = 0.0F;
                  event.setMoveStrafe(f5);
               }
            }

            if (this.sneak.getValue()) {
               double value = this.sneakSlowDown.getValue();
               event.setSneakForwardMultiplier(value);
               double value2 = this.sneakSlowDown.getValue();
               event.setSneakStrafeMultiplier(value2);
            }

            if (this.isIntaveFastSneakActive()) {
               boolean flag9 = true;
               event.setSneak(flag9);
               double d3 = 1.0;
               event.setSneakForwardMultiplier(d3);
               double d4 = 0.0;
               event.setSneakStrafeMultiplier(d4);
            }

            if (this.moveFix.getSelectedValue().equals("Default")) {
               MoveFixMode moveFixMode = MoveFixMode.DEFAULT;
               event.setMoveFixMode(moveFixMode);
            } else if (this.moveFix.getSelectedValue().equals("Advanced")) {
               MoveFixMode advanced = MoveFixMode.ADVANCED;
               event.setMoveFixMode(advanced);
            }

            if (!this.moveFix.getSelectedValue().equals("None") && RotationUtil.isNearCardinalYaw(this.getMovementYaw())) {
               boolean flag10 = false;
               float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
               float f6 = this.applyStrictYaw(realYaw, flag10);
               this.cachedStrictMoveYaw = f6;
               event.setYaw(f6);
            }

            if (this.moveFixIntave.getValue() && !this.moveFix.getSelectedValue().equals("None")) {
               boolean flag11 = true;
               event.setPreventInstantReverse(flag11);
            }

            if (this.sameY.getValue() && this.intave.getValue() && this.intaveFast2.getValue() && this.rightClickHeldOnGround) {
               boolean flag12 = true;
               event.setJump(flag12);
            }

            if (this.stopAtStart.getValue()) {
               long stopAtStartDelayMs2 = (long)this.stopAtStartDelayMs;
               if (!this.enableTimer.reached2(stopAtStartDelayMs2)) {
                  float f7 = 0.0F;
                  event.setMoveForward(f7);
               }
            }

            if (this.jump.getValue()) {
               if (this.jumpOnlyWhileSpacePressed.getValue() && MoveUtil.isMoving()) {
                  boolean flag13 = false;
                  event.setJump(flag13);
                  isKeyDown = false;
               }

               int n = RotationUtil.isNearCardinalYaw(this.computeBaseBridgeYaw()) ? this.jumpAfterXBlocksForward.getLastIntValue() : this.jumpAfterXBlocksDiagonal.getLastIntValue();
               if (this.blocksPlacedSinceJump >= n && (!this.jumpOnlyWhileSpacePressed.getValue() || mc.gameSettings.keyBindJump.isKeyDown())) {
                  this.blocksPlacedSinceJump = 0;
                  this.jumpAfterXBlocksForward.nextValue();
                  this.jumpAfterXBlocksDiagonal.nextValue();
                  boolean flag14 = true;
                  event.setJump(flag14);
               }
            }

            if (this.sneak.getValue()) {
               boolean isNearCardinalYaw = RotationUtil.isNearCardinalYaw(this.computeBaseBridgeYaw());
               if (this.timeJumpForOneStackForward.getValue() && isKeyDown && isNearCardinalYaw && MoveUtil.isMoving() && !this.isStationaryTowering()) {
                  isKeyDown = !mc.thePlayer.onGround;
                  if ((mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ).getBlock().getMaterial().isReplaceable() || !this.hasPlacedFirstBlock)
                     && mc.thePlayer.onGround) {
                     boolean flag15 = true;
                     event.setJump(flag15);
                     boolean flag16 = true;
                     event.setSneak(flag16);
                     isKeyDown = true;
                  } else {
                     boolean flag17 = false;
                     event.setJump(flag17);
                  }
               }

               boolean flag18 = isNearCardinalYaw && this.isStrafeActive()
                  ? RotationUtil.isNearCardinalYaw(ClientManagers.rotationController.getPreviousRotation().yaw - this.strafeYawOffset)
                  : RotationUtil.isNearCardinalYaw(ClientManagers.rotationController.getPreviousRotation().yaw);
               if ((isKeyDown && !this.sameY.getValue() || this.sameY.getValue() && this.rightClickHeldOnGround) && !this.isStationaryTowering() && this.hotbarBlockCount > 0) {
                  if (this.offGroundTicks == 0) {
                     if (this.oneStackDiagonal.getValue()) {
                        RandomNumberGenerator instance = DefaultRandomGenerator.instance;
                        double minValue = this.minOffGroundSneakTickDiagonal.getMinValue();
                        double maxValue = this.minOffGroundSneakTickDiagonal.getMaxValue();
                        double d5 = maxValue;
                        double d6 = minValue;
                        this.minOffGroundSneakDiagonal = instance.nextInt(d6, d5);
                        RandomNumberGenerator instance2 = DefaultRandomGenerator.instance;
                        minValue = this.maxOffGroundSneakTickDiagonal.getMinValue();
                        maxValue = this.maxOffGroundSneakTickDiagonal.getMaxValue();
                        double d7 = maxValue;
                        double d8 = minValue;
                        this.maxOffGroundSneakDiagonal = instance2.nextInt(d8, d7);
                     }

                     if (this.oneStackForward.getValue()) {
                        RandomNumberGenerator instance3 = DefaultRandomGenerator.instance;
                        double minValue2 = this.minOffGroundSneakTickForward.getMinValue();
                        double maxValue2 = this.minOffGroundSneakTickForward.getMaxValue();
                        double d9 = maxValue2;
                        double d10 = minValue2;
                        this.minOffGroundSneakForward = instance3.nextInt(d10, d9);
                        RandomNumberGenerator instance4 = DefaultRandomGenerator.instance;
                        minValue2 = this.maxOffGroundSneakTickForward.getMinValue();
                        maxValue2 = this.maxOffGroundSneakTickForward.getMaxValue();
                        double d11 = maxValue2;
                        double d12 = minValue2;
                        this.maxOffGroundSneakForward = instance4.nextInt(d12, d11);
                     }
                  }

                  if (this.oneStackDiagonal.getValue() && !flag18 && this.offGroundTicks >= this.minOffGroundSneakDiagonal && this.offGroundTicks < this.maxOffGroundSneakDiagonal) {
                     boolean flag19 = true;
                     event.setSneak(flag19);
                  }

                  if (this.oneStackForward.getValue() && flag18 && this.offGroundTicks >= this.minOffGroundSneakForward && this.offGroundTicks < this.maxOffGroundSneakForward) {
                     boolean flag20 = true;
                     event.setSneak(flag20);
                  }
               }

               if (!isKeyDown
                  && (!this.sameYBlockJumpActive || !this.sameYBlock.getValue())
                  && (this.sneakHoldTicks < this.sneakTicks.getLastIntValue() - 1 || !this.isOverSupportBlock && (flag18 ? this.blocksSinceLastSneak >= this.sneakIntervalBlocksForward : this.blocksSinceLastSneak >= this.sneakIntervalBlocksDiagonal))) {
                  boolean flag21 = true;
                  event.setSneak(flag21);
                  this.sneakingOnGroundForEdge = mc.thePlayer.onGround;
               }

               this.blocksSinceLastSneak = this.placedBlocksSinceSneak;
               this.sneakHoldTicks++;
               if (event.isSneak() && !this.wasSneakingLastTick) {
                  this.sneakHoldTicks = 0;
                  this.blocksSinceLastSneak = 0;
                  this.placedBlocksSinceSneak = 0;
                  this.sneakTicks.nextValue();
                  RandomNumberGenerator instance5 = DefaultRandomGenerator.instance;
                  double minValue3 = this.sneakBlocksForward.getMinValue();
                  double maxValue3 = this.sneakBlocksForward.getMaxValue();
                  double d13 = maxValue3;
                  double d14 = minValue3;
                  this.sneakIntervalBlocksForward = instance5.nextInt(d14, d13);
                  RandomNumberGenerator instance6 = DefaultRandomGenerator.instance;
                  minValue3 = this.sneakBlocksDiagonal.getMinValue();
                  maxValue3 = this.sneakBlocksDiagonal.getMaxValue();
                  double d15 = maxValue3;
                  double d16 = minValue3;
                  this.sneakIntervalBlocksDiagonal = instance6.nextInt(d16, d15);
               }

               if (!event.isSneak() && this.wasSneakingLastTick) {
                  RandomNumberGenerator instance7 = DefaultRandomGenerator.instance;
                  double minValue4 = this.startSneakDistToBlock.getMinValue();
                  double maxValue4 = this.startSneakDistToBlock.getMaxValue();
                  double d17 = maxValue4;
                  double d18 = minValue4;
                  this.sneakEdgeExpandDist = -instance7.nextDouble(d18, d17);
               }

               this.wasSneakingLastTick = event.isSneak();
            }

            if (!this.adhsMode.getSelectedValue().equals("None")) {
               if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
                  for (PotionEffect potionEffect : (Iterable<PotionEffect>)(mc.thePlayer.getActivePotionEffects())) {
                     if (potionEffect.getPotionID() == Potion.moveSpeed.id && mc.thePlayer.ticksExisted % (3 + potionEffect.getAmplifier()) != 0) {
                        boolean flag22 = true;
                        event.setSneak(flag22);
                        break;
                     }
                  }
               } else if (this.adhsMode.getSelectedValue().equals("ADS")) {
                  if (mc.thePlayer.ticksExisted % 3 == 0 || mc.thePlayer.ticksExisted % 4 == 0) {
                     boolean flag23 = true;
                     event.setSneak(flag23);
                  }
               } else if (this.adhsMode.getSelectedValue().equals("ADHS") && mc.thePlayer.ticksExisted % 2 == 0) {
                  boolean flag24 = true;
                  event.setSneak(flag24);
               }
            }

            if (this.moonWalk.getValue()
               && (!this.onlyOnGround.getValue() || mc.thePlayer.onGround)
               && this.targetBlockPos != null
               && !isKeyDown
               && MoveUtil.isMoving()
               && RotationUtil.isNearCardinalYaw(ClientManagers.rotationController.getPreviousRotation().yaw)
               && (!this.sneakingOnGroundForEdge || !this.stopClickingAtSneaking.getValue())) {
               EnumFacing horizontalFacing = mc.thePlayer.getHorizontalFacing(ClientManagers.rotationController.getPreviousRotation().yaw);
               boolean flag25 = false;

               for (AxisAlignedBB box2 : (Iterable<AxisAlignedBB>)(mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -1.0, 0.0)))) {
                  if (mc.thePlayer
                     .getEntityBoundingBox()
                     .expand(-0.3, 0.0, -0.3)
                     .offset(0.0, -1.0, 0.0)
                     .intersectsWith(
                        box2.expand(
                           horizontalFacing != EnumFacing.EAST && horizontalFacing != EnumFacing.WEST ? 0.0 : this.moonWalkEdgeExpandDist,
                           0.0,
                           horizontalFacing != EnumFacing.NORTH && horizontalFacing != EnumFacing.SOUTH ? 0.0 : this.moonWalkEdgeExpandDist
                        )
                     )) {
                     flag25 = true;
                     break;
                  }
               }

               if (!flag25 && this.wasOverMoonWalkBlock) {
                  this.moonWalkTicks = 0;
                  this.minMoonwalkTicks.nextValue();
               }

               this.wasOverMoonWalkBlock = flag25;
               this.ticksSinceLastPlace++;
               this.moonWalkTicks++;
               double lastValue = this.moonWalkMiddle.getLastValue();
               double lastValue2 = this.moonWalkWideness.getLastValue();
               if (!flag25 || this.ticksSinceLastPlace <= this.postPlacementStrafeTicks.getLastIntValue() || this.moonWalkTicks <= this.minMoonwalkTicks.getLastIntValue()) {
                  switch (ScaffoldFacingSwitchMap.ORDINAL_MAP[horizontalFacing.ordinal()]) {
                     case 1:
                        if ((double)this.targetBlockPos.getZ() + lastValue - lastValue2 > mc.thePlayer.posZ) {
                           this.setMoonWalkLeftStrafe(event);
                        } else if ((double)this.targetBlockPos.getZ() + lastValue + lastValue2 < mc.thePlayer.posZ) {
                           this.setMoonWalkRightStrafe(event);
                        }
                        break;
                     case 2:
                        if ((double)this.targetBlockPos.getZ() + lastValue + lastValue2 < mc.thePlayer.posZ) {
                           this.setMoonWalkLeftStrafe(event);
                        } else if ((double)this.targetBlockPos.getZ() + lastValue - lastValue2 > mc.thePlayer.posZ) {
                           this.setMoonWalkRightStrafe(event);
                        }
                        break;
                     case 3:
                        if ((double)this.targetBlockPos.getX() + lastValue + lastValue2 < mc.thePlayer.posX) {
                           this.setMoonWalkLeftStrafe(event);
                        } else if ((double)this.targetBlockPos.getX() + lastValue - lastValue2 > mc.thePlayer.posX) {
                           this.setMoonWalkRightStrafe(event);
                        }
                        break;
                     case 4:
                        if ((double)this.targetBlockPos.getX() + lastValue - lastValue2 > mc.thePlayer.posX) {
                           this.setMoonWalkLeftStrafe(event);
                        } else if ((double)this.targetBlockPos.getX() + lastValue + lastValue2 < mc.thePlayer.posX) {
                           this.setMoonWalkRightStrafe(event);
                        }
                  }

                  this.applyMoonWalkStrafe(event);
               }
            }

            if (this.isStrafeActive() && RotationUtil.isNearCardinalYaw(this.getMovementYaw()) && this.targetBlockPos != null && this.correctStrafeWithKeyBinds.getValue()) {
               double lastValue3 = this.correctStrafeWithKeyBindsWidth.getLastValue();
               float strafeYawOffset2 = this.strafeYawOffset;
               boolean value3 = this.correctStrafeWithKeyBindsCage.getValue();
               double d19;
               double d20;
               if (Math.abs(this.strafeYawOffset) > 45.0F) {
                  d19 = this.strafeCorrectionMiddle + lastValue3;
                  d20 = this.strafeCorrectionMiddle - lastValue3;
                  strafeYawOffset2 = -strafeYawOffset2;
               } else if ((double)Math.abs(this.strafeYawOffset) >= 22.5) {
                  if (value3) {
                     d19 = this.strafeCorrectionMiddle + lastValue3;
                     d20 = this.strafeCorrectionMiddle - lastValue3;
                  } else {
                     d19 = this.strafeCorrectionMiddle - lastValue3;
                     d20 = this.strafeCorrectionMiddle + lastValue3;
                  }
               } else {
                  d19 = this.strafeCorrectionMiddle + lastValue3;
                  d20 = this.strafeCorrectionMiddle - lastValue3;
               }

               switch (ScaffoldFacingSwitchMap.ORDINAL_MAP[mc.thePlayer.getHorizontalFacing(this.computeBaseBridgeYaw() - 180.0F).ordinal()]) {
                  case 1:
                     if (!(mc.thePlayer.posZ > (double)this.targetBlockPos.getZ() + d19) || !(strafeYawOffset2 > 0.0F) && !value3) {
                        if (mc.thePlayer.posZ < (double)this.targetBlockPos.getZ() + d20 && (strafeYawOffset2 < 0.0F || value3)) {
                           this.strafeCorrectionCounter.reset();
                           this.setKeyBindCorrectionRight(event);
                        }
                     } else {
                        this.strafeCorrectionCounter.reset();
                        this.setKeyBindCorrectionLeft(event);
                     }
                     break;
                  case 2:
                     if (!(mc.thePlayer.posZ > (double)this.targetBlockPos.getZ() + d19) || !(strafeYawOffset2 < 0.0F) && !value3) {
                        if (mc.thePlayer.posZ < (double)this.targetBlockPos.getZ() + d20 && (strafeYawOffset2 > 0.0F || value3)) {
                           this.strafeCorrectionCounter.reset();
                           this.setKeyBindCorrectionLeft(event);
                        }
                        break;
                     }

                     this.strafeCorrectionCounter.reset();
                     this.setKeyBindCorrectionRight(event);
                     break;
                  case 3:
                     if (!(mc.thePlayer.posX > (double)this.targetBlockPos.getX() + d19) || !(strafeYawOffset2 < 0.0F) && !value3) {
                        if (mc.thePlayer.posX < (double)this.targetBlockPos.getX() + d20 && (strafeYawOffset2 > 0.0F || value3)) {
                           this.strafeCorrectionCounter.reset();
                           this.setKeyBindCorrectionLeft(event);
                        }
                        break;
                     }

                     this.strafeCorrectionCounter.reset();
                     this.setKeyBindCorrectionRight(event);
                     break;
                  case 4:
                     if (!(mc.thePlayer.posX > (double)this.targetBlockPos.getX() + d19) || !(strafeYawOffset2 > 0.0F) && !value3) {
                        if (mc.thePlayer.posX < (double)this.targetBlockPos.getX() + d20 && (strafeYawOffset2 < 0.0F || value3)) {
                           this.strafeCorrectionCounter.reset();
                           this.setKeyBindCorrectionRight(event);
                        }
                     } else {
                        this.strafeCorrectionCounter.reset();
                        this.setKeyBindCorrectionLeft(event);
                     }
               }

               TickCounter tickCounter = this.strafeCorrectionCounter;
               int lastIntValue = this.correctStrafeWithKeyBindsDuration.getLastIntValue();
               if (!tickCounter.hasReached2(lastIntValue)) {
                  if (event.getMoveForward() != 0.0F) {
                     float f8 = (float)this.keyBindStrafeCorrectionDir;
                     event.setMoveStrafe(f8);
                  } else if (event.getMoveStrafe() != 0.0F) {
                     float f9 = (float)this.keyBindStrafeCorrectionDir;
                     event.setMoveForward(f9);
                  }

                  this.strafeCorrectionActive = true;
               } else if (this.strafeCorrectionActive) {
                  this.strafeCorrectionActive = false;
                  this.correctStrafeWithKeyBindsWidth.nextValue();
                  this.correctStrafeWithKeyBindsDuration.nextValue();
                  tickCounter = this.strafeMiddleRecalcCounter;
                  int lastIntValue2 = this.correctStrafeWithKeyBindsDelayToRecalculateMiddle.getLastIntValue();
                  if (tickCounter.hasReached2(lastIntValue2)) {
                     this.correctStrafeWithKeyBindsDelayToRecalculateMiddle.nextValue();
                     this.strafeMiddleRecalcCounter.reset();
                     this.strafeCorrectionMiddle = this.rollStrafeCorrectionMiddle();
                  }
               }
            }

            if (this.sameYBlock.getValue()) {
               if (this.sameYBlockJumpActive && mc.thePlayer.onGround && mc.thePlayer.posY == this.sameYBlockBaseY) {
                  this.sameYBlockJumpActive = false;
               }

               if (this.stopMovementUntilLand && this.stopMovementAfterJump.getValue()) {
                  if (!mc.thePlayer.onGround) {
                     float f10 = 0.0F;
                     event.setMoveForward(f10);
                     float f11 = 0.0F;
                     event.setMoveStrafe(f11);
                  } else {
                     this.stopMovementUntilLand = false;
                  }
               }

               if (event.isJump()) {
                  this.sameYBlockJumpActive = true;
                  this.stopMovementUntilLand = true;
                  this.sameYBlockTopPlaced = false;
               }
            }

            if (this.intaveBlockPlacePenalty > 0.0) {
               if (mc.thePlayer.onGround) {
                  boolean flag26 = true;
                  event.setSneak(flag26);
               }

               float f12 = this.intaveBlockPlacePenalty > 22.5 ? -1.0F : 0.0F;
               event.setMoveForward(f12);
            }

            if (this.intave.getValue() && this.intaveSafe.getValue()) {
               this.intave14TimeBlockPlaceCheck.onMoveInput(event);
            }

            if (this.godBridge.getValue()) {
               this.handleGodBridgeInput(event);
            }

            this.lastAppliedStrafeInput = event.getMoveForward() != 0.0F ? (int)event.getMoveStrafe() : (int)event.getMoveForward();
         }
      }
   }

   private void handleGodBridgeInput(EventMoveInput event) {
      if (mc.gameSettings.keyBindJump.isKeyDown()) {
         this.godBridgeJumpHeld = true;
         this.resetGodBridgeState();
      } else if (this.godBridgeJumpHeld) {
         if (mc.thePlayer.onGroundTicks > 2) {
            this.godBridgeJumpHeld = false;
         }
      } else {
         boolean wasCardinalDirection2 = this.wasCardinalDirection;
         this.wasCardinalDirection = RotationUtil.isNearCardinalYaw(Augustus.getInstance().getYawPitchHelper().realYaw);
         if (wasCardinalDirection2 != this.wasCardinalDirection) {
            this.resetGodBridgeState();
         }

         if (!RotationUtil.isNearCardinalYaw(Augustus.getInstance().getYawPitchHelper().realYaw)) {
            if (this.godBridgeResetting && !this.disableGodbridgeSafetyFeature.getValue()) {
               boolean flag = true;
               event.setSneak(flag);
               if (event.getMoveForward() > 0.0F && this.safeWalkEdgeDetected && this.lastGodBridgePosX == mc.thePlayer.posX && this.lastGodBridgePosZ == mc.thePlayer.posZ || this.godBridgeSetupTicks > 3) {
                  float f = 0.0F;
                  event.setMoveForward(f);
                  float f2 = 0.0F;
                  event.setMoveStrafe(f2);
                  this.godBridgeSetupTicks++;
                  if ((double)this.godBridgeSetupTicks > this.startDelay.nextValue() - 4.0) {
                     if ((double)this.godBridgeSetupTicks < this.startDelay.nextValue() - 4.0 + 5.0) {
                        float f3 = 1.0F;
                        event.setMoveStrafe(f3);
                     } else if ((double)this.godBridgeSetupTicks < this.startDelay.nextValue() - 4.0 + 7.0) {
                        float f4 = -1.0F;
                        event.setMoveForward(f4);
                     } else {
                        this.godBridgeResetting = false;
                     }
                  }
               }
            }

            this.lastGodBridgePosX = mc.thePlayer.posX;
            this.lastGodBridgePosZ = mc.thePlayer.posZ;
         } else {
            if (this.godBridgePlacedBlocks >= this.jumpAfterXBlocks.getLastIntValue() && this.ticksSinceGodBridgePlace >= this.jumpDelayAfterPlace.getLastIntValue()) {
               this.ticksSinceGodBridgePlace = 0;
               this.godBridgeJumpCount++;
               this.jumpAfterXBlocks.nextValue();
               this.jumpDelayAfterPlace.nextValue();
               if (this.godBridgeJumpCount >= this.resetGodBridgeAfterXJumps.getLastIntValue()) {
                  this.resetGodBridgeState();
               } else {
                  boolean flag2 = true;
                  event.setJump(flag2);
                  this.godBridgePlacedBlocks = 0;
               }
            }

            this.ticksSinceGodBridgePlace++;
            if (!this.godBridgeResetting) {
               float f5 = 0.0F;
               event.setMoveStrafe(f5);
            } else {
               this.godBridgePlacedBlocks = 0;
               if (!(event.getMoveForward() <= 0.0F) && !this.alignGodBridgePosition(event) && (this.safeWalkEdgeDetected || this.godBridgeSetupTicks > 3)) {
                  if (this.godBridgeSetupTicks >= this.startDelay.getLastIntValue()) {
                     this.godBridgeResetting = false;
                  } else {
                     this.godBridgeSetupTicks++;
                     float f6 = 0.0F;
                     event.setMoveForward(f6);
                     float f7 = 0.0F;
                     event.setMoveStrafe(f7);
                     if (this.godBridgeSetupTicks < this.godBridgeSneakSetupTicks) {
                        boolean flag3 = true;
                        event.setSneak(flag3);
                     }
                  }
               } else {
                  this.resetGodBridgeState();
                  boolean flag4 = true;
                  event.setSneak(flag4);
               }
            }
         }
      }
   }

   public double rollStrafeCorrectionMiddle() {

      double d;
      for (d = this.correctStrafeWithKeyBindsMiddle.nextValue(); d > this.correctStrafeWithKeyBindsMiddleExclusion.getMinValue(); d = this.correctStrafeWithKeyBindsMiddle.nextValue()) {
         if (!(d < this.correctStrafeWithKeyBindsMiddleExclusion.getMaxValue())) {
            break;
         }

         if (!(this.correctStrafeWithKeyBindsMiddle.getMinValue() < this.correctStrafeWithKeyBindsMiddleExclusion.getMinValue())) {
            double maxValue = this.correctStrafeWithKeyBindsMiddle.getMaxValue();
            if (!(maxValue > this.correctStrafeWithKeyBindsMiddleExclusion.getMaxValue())) {
               break;
            }
         }
      }

      return d;
   }

   @Override
   public void onOrientCamera(EventOrientCamera event) {
      if (!this.waitingForStartEdge) {
         if (this.sneak.getValue() && this.hideSneakAnimation.getValue()) {
            float f = 1.62F;
            event.setEyeHeight(f);
         }
      }
   }

   @Override
   public void onMove(EventMove event) {
      if (!this.waitingForStartEdge) {
         if (this.moveFix.getSelectedValue().equals("None")) {
            float movementYaw = MoveUtil.getMovementYaw();
            event.setYaw(movementYaw);
         }
      }
   }

   @Override
   public void onMove2(EventMove event) {
      if (!this.waitingForStartEdge) {
         if (this.moveFix.getSelectedValue().equals("None")) {
            float movementYaw = MoveUtil.getMovementYaw();
            event.setYaw(movementYaw);
         }

         if (this.intave.getValue() && this.intaveSafe.getValue()) {
            this.intave14TimeBlockPlaceCheck.onMoveUpdate();
         }
      }
   }

   @Override
   public void onRayCast() {
      if (!this.waitingForStartEdge) {
         if (this.overrideRayTrace != null) {
            MovingObjectPosition overrideRayTrace2 = this.overrideRayTrace;
            mc.objectMouseOver = overrideRayTrace2;
            this.overrideRayTrace = null;
         }
      }
   }

   @Override
   public void onSafeWalk(EventSafeWalk event) {
      if (!this.waitingForStartEdge) {
         exitBlock: {
            if (!this.removeSneakStop.getValue()) {
               if (!this.isIntaveFastSneakActive()) {
                  break exitBlock;
               }
            }

            if (this.sneakNoBlocks.getValue()) {
               ItemStack currentEquippedItem = mc.thePlayer.getCurrentEquippedItem();
               if (currentEquippedItem != null && BlockUtil.isValidScaffoldBlock(currentEquippedItem)
                  || mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ)).getBlock().isFullBlock()) {
                  boolean flag = true;
                  event.setDisableSneakSafeWalk(flag);
               }
            }
         }

         if (this.safeWalk.getValue()
            && (this.safeWalkAir.getValue() || mc.thePlayer.onGround)
            && (!this.safeWalkOnlyDiagonal.getValue() || !RotationUtil.isNearCardinalYaw(ClientManagers.rotationController.getPreviousRotation().yaw))) {
            boolean flag2 = true;
            event.setSafeWalk(flag2);
            boolean flag3 = false;
            event.setDisableSneakSafeWalk(flag3);
         }

         if (this.safeWalkNoBlocks.getValue()) {
            ItemStack currentEquippedItem2 = mc.thePlayer.getCurrentEquippedItem();
            if (!BlockUtil.isValidScaffoldBlock(currentEquippedItem2) && this.getSelectedBlockSlot() < 0 && !this.firstSilentSwitchPending) {
               boolean flag4 = true;
               event.setSafeWalk(flag4);
               boolean flag5 = false;
               event.setDisableSneakSafeWalk(flag5);
            }
         }

         if (this.sneakNoBlocks.getValue()) {
            ItemStack currentEquippedItem3 = mc.thePlayer.getCurrentEquippedItem();
            if (!BlockUtil.isValidScaffoldBlock(currentEquippedItem3) && this.getSelectedBlockSlot() < 0 && !this.firstSilentSwitchPending) {
               boolean flag6 = false;
               event.setDisableSneakSafeWalk(flag6);
            }
         }
      }
   }

   @Override
   public void onSwing(EventSwing event) {
      if (this.noSwing.getValue()) {
         boolean flag = true;
         event.setCancelClientSwing(flag);
      }
   }

   @Override
   public void onSprint3(EventSprint event) {
      boolean flag = !this.mode.getSelectedValue().equals("None");
      event.setSprintKeyDown(flag);
      boolean flag2 = this.mode.getSelectedValue().equals("AllDirection") && MoveUtil.isMoving();
      event.setForceSprint(flag2);
   }

   @Override
   public void onSprint(EventSprint event) {
      boolean flag = this.jumpSprint.getValue() && MoveUtil.isMoving();
      event.setOmniJumpSprint(flag);
   }

   @Override
   public void onSprint2(EventSprint event) {
      if (!this.waitingForStartEdge) {
         boolean flag = this.mode.getSelectedValue().equals("AllDirectionMotion") && MoveUtil.isMoving();
         event.setMotionSprint(flag);
      }
   }

   @Override
   public void onMotion(EventMotion event) {
      if (!this.waitingForStartEdge) {
         if (!ClientManagers.moduleManager.timer.isToggled() || ClientManagers.moduleManager.timer.time.getValue() == 1.0) {
            if (this.timerDrop.getValue()) {
               if (!mc.gameSettings.keyBindJump.isKeyDown()) {
                  if (mc.thePlayer.ticksExisted % 3 == 0) {
                     mc.getTimer().timerSpeed = (float)MathHelper.clamp_double(this.timerSpeed.getMinValue(), 0.1F, 100.0);
                  } else {
                     Timer timer2 = mc.getTimer();
                     timer2.timerSpeed = (float)MathHelper.clamp_double(this.timerSpeed.getMaxValue(), 0.1F, 100.0);
                  }
               }
            } else {
               if (!mc.gameSettings.keyBindJump.isKeyDown()) {
                  mc.getTimer().timerSpeed = (float)this.timer.getValue();
               }
            }
         }

         if (this.hover.getValue() && this.hoverJumpStarted && mc.thePlayer.ticksExisted % 2 == 0 && mc.thePlayer.onGround && event.getY() % 1.0 == 0.0) {
            this.hoverOffsetActive = true;
            double d = Math.random() * 1.0E-4;
            double d2 = event.getY() + d;
            event.setY(d2);
            boolean flag = false;
            event.setOnGround(flag);
         }
      }
   }

   @Override
   public void onMoveEntity(EventMoveEntity event) {
      if (!this.waitingForStartEdge) {
         double d;
         if (mc.thePlayer.onGround) {
            d = RotationUtil.isNearCardinalYaw(ClientManagers.rotationController.getPreviousRotation().yaw) ? this.motionMultiplierGroundForward.nextValue() : this.motionMultiplierGroundStrafe.nextValue();
         } else {
            d = RotationUtil.isNearCardinalYaw(ClientManagers.rotationController.getPreviousRotation().yaw) ? this.motionMultiplierAirForward.nextValue() : this.motionMultiplierAirStrafe.nextValue();
         }

         double d2 = event.getX() * d;
         event.setX(d2);
         double d3 = event.getZ() * d;
         event.setZ(d3);
      }
   }

   @Override
   public void onUpdate2() {
      if (!this.waitingForStartEdge) {
         if (this.sameY.getValue()
            && this.intaveFast2.getValue()
            && this.intave.getValue()
            && !this.rightClickHeldOnGround
            && MoveUtil.isMoving()
            && this.hotbarBlockCount > 0
            && mc.thePlayer.hurtTime <= 0
            && !mc.thePlayer.isSprinting()) {
            long l = 400L;
            if (this.enableTimer.reached2(l)) {
               if (mc.thePlayer.onGround) {
                  mc.thePlayer.jump();
               } else if (mc.thePlayer.motionY > 0.0) {
                  MoveUtil.setSpeed(0.28F, false);
               }
            }
         }
      }
   }

   private boolean alignGodBridgePosition(EventMoveInput event) {
      boolean flag = false;
      EnumFacing horizontalFacing = mc.thePlayer.getHorizontalFacing(Augustus.getInstance().getYawPitchHelper().realYaw);
      double d = horizontalFacing != EnumFacing.NORTH && horizontalFacing != EnumFacing.SOUTH ? mc.thePlayer.posZ : mc.thePlayer.posX;
      double d2 = d < 0.0 ? 1.0 + d % 1.0 : d % 1.0;
      boolean flag2 = horizontalFacing == EnumFacing.NORTH || horizontalFacing == EnumFacing.EAST;
      if (d2 < 0.5) {
         if (d2 < 0.2) {
            float f = flag2 ? -1.0F : 1.0F;
            event.setMoveStrafe(f);
            flag = true;
         } else if (d2 > 0.3) {
            float f2 = flag2 ? 1.0F : -1.0F;
            event.setMoveStrafe(f2);
            flag = true;
         }
      } else if (d2 < 0.7) {
         float f3 = flag2 ? -1.0F : 1.0F;
         event.setMoveStrafe(f3);
         flag = true;
      } else if (d2 > 0.8) {
         float f4 = flag2 ? 1.0F : -1.0F;
         event.setMoveStrafe(f4);
         flag = true;
      }

      return flag;
   }

   private void resetGodBridgeState() {
      if (!this.godBridgeResetting) {
         this.resetGodBridgeAfterXJumps.nextValue();
         this.startDelay.nextValue();
         byte b = 7;
         byte b2 = 5;
         this.godBridgeSneakSetupTicks = DefaultRandomGenerator.instance.nextInt2(b2, b);
      }

      this.godBridgeResetting = true;
      this.godBridgeSetupTicks = 0;
      this.godBridgeJumpCount = 0;
      this.ticksSinceGodBridgePlace = 0;
      this.godBridgePlacedBlocks = 0;
   }

   private float applyStrictYaw(float f, boolean flag) {
      if (!this.strictYaw.getValue()) {
         return f;
      } else if (this.lastStrictYawSector < 0
         || !this.hasPlacedFirstBlock
         || !this.changeDirectionOnlyIfBlockUnderYou.getValue()
         || mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ).getBlock().getMaterial() != Material.air) {
         float f2 = MathHelper.wrapAngleTo180_float(f);
         if (f2 > -25.0F && f2 < 25.0F) {
            byte b = 0;
            float f3 = 0.0F;
            return this.applyStrictYawError(f3, b, flag);
         } else if (f2 > -25.0F && f2 < 70.0F) {
            byte b2 = 1;
            float f4 = 45.0F;
            return this.applyStrictYawError(f4, b2, flag);
         } else if (f2 > 70.0F && f2 < 115.0F) {
            byte b3 = 2;
            float f5 = 90.0F;
            return this.applyStrictYawError(f5, b3, flag);
         } else if (f2 > 115.0F && f2 < 160.0F) {
            byte b4 = 3;
            float f6 = 135.0F;
            return this.applyStrictYawError(f6, b4, flag);
         } else if (f2 > 160.0F || f2 < -160.0F) {
            byte b5 = 4;
            float f7 = 180.0F;
            return this.applyStrictYawError(f7, b5, flag);
         } else if (f2 > -160.0F && f2 < -115.0F) {
            byte b6 = 5;
            float f8 = -135.0F;
            return this.applyStrictYawError(f8, b6, flag);
         } else if (f2 > -115.0F && f2 < -70.0F) {
            byte b7 = 6;
            float f9 = -90.0F;
            return this.applyStrictYawError(f9, b7, flag);
         } else if (f2 > -70.0F && f2 < -25.0F) {
            byte b8 = 7;
            float f10 = -45.0F;
            return this.applyStrictYawError(f10, b8, flag);
         } else {
            return f;
         }
      } else {
         return flag ? this.cachedStrictRotationYaw : this.cachedStrictMoveYaw;
      }
   }

   private float applyStrictYawError(float f, int n, boolean flag) {
      if (flag) {
         if (!(this.strictYawIncorrectFactor.getMaxValue() <= 0.0)) {
            if (n != this.lastStrictYawSector) {
               this.lastStrictYawSector = n;
               this.strictYawIncorrectFactor.nextValue();
               this.strictYawErrorSign = ThreadLocalRandom.current().nextBoolean() ? 1 : -1;
               this.directionChangePending = true;
               this.directionChangeDelayCounter.reset();
            }

            return f + (float)((double)this.strictYawErrorSign * this.strictYawIncorrectFactor.getLastValue());
         }
      }

      return f;
   }

   private float applyMovementYawOffset(float f) {
      if (this.rotateWithMovement.getValue()) {
         if (mc.gameSettings.keyBindBack.isKeyDown()) {
            f += 180.0F;
            if (!this.rotateWithMovementOnlyOrthogonal.getValue()) {
               if (mc.gameSettings.keyBindLeft.isKeyDown()) {
                  f += 45.0F;
               } else if (mc.gameSettings.keyBindRight.isKeyDown()) {
                  f -= 45.0F;
               }
            }
         } else if (mc.gameSettings.keyBindForward.isKeyDown()) {
            if (!this.rotateWithMovementOnlyOrthogonal.getValue()) {
               if (mc.gameSettings.keyBindLeft.isKeyDown()) {
                  f -= 45.0F;
               } else if (mc.gameSettings.keyBindRight.isKeyDown()) {
                  f += 45.0F;
               }
            }
         } else if (mc.gameSettings.keyBindRight.isKeyDown()) {
            f += 90.0F;
         } else if (mc.gameSettings.keyBindLeft.isKeyDown()) {
            f -= 90.0F;
         }
      }

      return f;
   }

   private float getMovementYaw() {
      float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
      return this.applyMovementYawOffset(realYaw);
   }

   private float computeBaseBridgeYaw() {
      float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
      float f = this.applyMovementYawOffset(realYaw);
      boolean flag = true;
      f = this.applyStrictYaw(f, flag);
      if (this.isTellyBridgeEnabled() && !mc.thePlayer.onGround) {
         f = this.cachedStrictRotationYaw;
      } else {
         this.cachedStrictRotationYaw = f;
      }

      if (this.shouldImproveRotation() && MoveUtil.hasHorizontalMotion()) {
         double d = mc.thePlayer.posX + mc.thePlayer.motionX;
         double posY = mc.thePlayer.posY;
         double d2 = mc.thePlayer.posZ + mc.thePlayer.motionZ;
         Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
         boolean flag2 = false;
         float f2 = 180.0F;
         float f3 = 180.0F;
         float yaw = RotationUtil.getRotationsToPos(d, posY, d2, previousRotation, f3, f2, flag2).yaw;
         if (Math.abs(EntityBoundsUtil.getShortestSignedAngleDelta((double)MathHelper.wrapAngleTo180_float(yaw), (double)MathHelper.wrapAngleTo180_float(f))) > 12.0
               && !this.moonWalk.getValue()
               && mc.thePlayer.hurtResistantTime != 0
            || mc.thePlayer.hurtTime != 0) {
            f = yaw;
         }
      }

      if (this.isTellyBridgeEnabled()) {
         if (this.shouldRotateBackwardInTelly()) {
            f -= 180.0F;
         }
      } else if (this.flickMode.getSelectedValue().equals("Jump")) {
         if (!mc.thePlayer.onGround) {
            if (mc.thePlayer.airTicks != 1 && PlayerUtil.predictPlayerPosition(0, mc.thePlayer)[9] != 1.0) {
               f -= 180.0F;
            } else {
               f -= 160.0F;
            }
         }
      } else if (this.flickMode.getSelectedValue().equals("Simple")) {
         if (this.isAtBlockEdge()) {
            f -= 180.0F;
         }
      } else {
         f -= 180.0F;
      }

      if (this.reduceYawRotation.getValue() && MoveUtil.isMoving()) {
         long l = 500L;
         if (this.enableTimer.reached2(l)) {
            if (Math.abs(this.lastTargetYaw - f) < 25.0F) {
               f = this.lastTargetYaw;
            } else {
               this.lastTargetYaw = f;
            }

            return f;
         }
      }

      this.lastTargetYaw = f;
      return f;
   }

   private boolean shouldImproveRotation() {
      return this.improve.getValue() && (!this.improveOnlyAtClutch.getValue() || this.isImproveClutchConditionMet());
   }

   private void updateExpandRotation() {
      float f = this.rollActiveYawSpeed();
      float f2 = this.rollActivePitchSpeed();
      RotationUtil rotationUtil2 = this.rotationUtil;
      float yaw = ClientManagers.rotationController.getPreviousRotation().yaw;
      float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
      float f3 = yaw;
      float f4 = rotationUtil2.stepYawWithGcd(f, f3, realYaw);
      if (this.isStationaryTowering() && this.targetBlockPos != null) {
         Block block = mc.theWorld.getBlockState(this.targetBlockPos).getBlock();
         Vec3 positionEyes = mc.thePlayer.getPositionEyes(1.0F);
         double d = MathHelper.clamp_double(
            positionEyes.xCoord, (double)this.targetBlockPos.getX() + block.getBlockBoundsMinX(), (double)this.targetBlockPos.getX() + block.getBlockBoundsMaxY()
         );
         double d2 = MathHelper.clamp_double(
            positionEyes.yCoord, (double)this.targetBlockPos.getY() + block.getBlockBoundsMinY(), (double)this.targetBlockPos.getY() + block.getBlockBoundsMaxY() - 0.02
         );
         double d3 = MathHelper.clamp_double(
            positionEyes.zCoord, (double)this.targetBlockPos.getZ() + block.getBlockBoundsMinZ(), (double)this.targetBlockPos.getZ() + block.getBlockBoundsMaxZ()
         );
         Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
         boolean flag = true;
         Rotation rotationsToPos = RotationUtil.getRotationsToPos(d, d2, d3, previousRotation, f, f2, flag);
         if (new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ).equalsXZ(this.targetBlockPos)) {
            rotationsToPos.yaw = f4;
         }

         Rotation previousRotation2 = ClientManagers.rotationController.getPreviousRotation();
         rotationsToPos = RotationUtil.applyGcdFix(rotationsToPos, previousRotation2);
         RotationController rotationController = ClientManagers.rotationController;
         int rotationPriority = this.getRotationPriority();
         byte b = -1;
         rotationController.requestRotation(rotationsToPos, b, this, rotationPriority);
      } else {
         double value = this.expandLength.getValue();
         double d4 = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
         value /= d4;
         if (mc.thePlayer.onGround) {
            this.expandAirYOffset = 0.0;
         }

         if (!mc.thePlayer.onGround && (!this.sameY.getValue() || MouseInputUtil.isButtonDown(1))) {
            this.expandAirYOffset = 1.0;
            if (PlayerUtil.predictPlayerPosition(0, mc.thePlayer)[9] == 1.0) {
               return;
            }

            double d5 = 0.0;
            double d6 = 0.0;

            for (int i = 0; i < 10; i++) {
               double[] doubles = PlayerUtil.predictPlayerPosition(i, mc.thePlayer);
               double d7 = doubles[1] - mc.thePlayer.lastGroundY;
               if (d7 > 1.0 && d7 < 2.0) {
                  d5 = doubles[0];
                  d6 = doubles[2];
               }
            }

            BlockPos pos = new BlockPos(d5, mc.thePlayer.lastGroundY - 0.1, d6);
            if (d5 != 0.0
               && d6 != 0.0
               && mc.theWorld.getBlockState(pos).getBlock().isFullBlock()
               && !mc.theWorld.getBlockState(pos.up()).getBlock().isFullBlock()) {
               this.blockPlacementData = new BlockPlacementData(pos, EnumFacing.UP, new Vec3(d5, mc.thePlayer.lastGroundY, d6));
               double xCoord = this.blockPlacementData.getHitVec().xCoord;
               double yCoord = this.blockPlacementData.getHitVec().yCoord;
               double zCoord = this.blockPlacementData.getHitVec().zCoord;
               Rotation previousRotation3 = ClientManagers.rotationController.getPreviousRotation();
               boolean flag2 = false;
               Rotation rotationsToPos2 = RotationUtil.getRotationsToPos(xCoord, yCoord, zCoord, previousRotation3, f, f2, flag2);
               RotationUtil rotationUtil3 = this.rotationUtil;
               float yaw2 = rotationsToPos2.yaw;
               float yaw3 = ClientManagers.rotationController.getPreviousRotation().yaw;
               f4 = rotationUtil3.stepYawWithGcd(f, yaw3, yaw2);
               RotationUtil rotationUtil4 = this.rotationUtil;
               float pitch = rotationsToPos2.pitch;
               float pitch2 = ClientManagers.rotationController.getPreviousRotation().pitch;
               float f5 = rotationUtil4.updatePitchWithIntaveSin(f2, pitch2, pitch);
               RotationController rotationController2 = ClientManagers.rotationController;
               Rotation rotation = new Rotation(f4, f5);
               int rotationPriority2 = this.getRotationPriority();
               byte b2 = -1;
               Rotation rotation2 = rotation;
               rotationController2.requestRotation(rotation2, b2, this, rotationPriority2);
               return;
            }
         }

         for (float j = 0.5F; (double)j < Math.min(20.0, value); j = (float)((double)j + 0.25)) {
            double d8 = mc.thePlayer.posX + MathHelper.clamp_double(mc.thePlayer.motionX * (double)j, (double)(-j), (double)j);
            double d9 = mc.thePlayer.lastGroundY - 0.5 + this.expandAirYOffset;
            double d10 = mc.thePlayer.posZ + MathHelper.clamp_double(mc.thePlayer.motionZ * (double)j, (double)(-j), (double)j);
            MovingObjectPosition hit = mc.theWorld.rayTraceBlocks(d8, d9, d10, mc.thePlayer.posX, d9, mc.thePlayer.posZ, false, false, true);
            if (MoveUtil.isMoving()
               && hit != null
               && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
               && !hit.getBlockPos().equalsPos(new BlockPos(d8, d9, d10))) {
               this.blockPlacementData = new BlockPlacementData(hit.getBlockPos(), hit.sideHit, hit.hitVec);
               this.targetBlockPos = this.blockPlacementData.getBlockPos();
               double xCoord2 = this.blockPlacementData.getHitVec().xCoord;
               double yCoord2 = this.blockPlacementData.getHitVec().yCoord;
               double zCoord2 = this.blockPlacementData.getHitVec().zCoord;
               Rotation previousRotation4 = ClientManagers.rotationController.getPreviousRotation();
               boolean flag3 = false;
               Rotation rotationsToPos3 = RotationUtil.getRotationsToPos(xCoord2, yCoord2, zCoord2, previousRotation4, f, f2, flag3);
               RotationUtil rotationUtil5 = this.rotationUtil;
               float yaw4 = rotationsToPos3.yaw;
               float yaw5 = ClientManagers.rotationController.getPreviousRotation().yaw;
               f4 = rotationUtil5.stepYawWithGcd(f, yaw5, yaw4);
               RotationUtil rotationUtil6 = this.rotationUtil;
               float pitch3 = rotationsToPos3.pitch;
               float pitch4 = ClientManagers.rotationController.getPreviousRotation().pitch;
               float f6 = rotationUtil6.updatePitchWithIntaveSin(f2, pitch4, pitch3);
               RotationController rotationController3 = ClientManagers.rotationController;
               Rotation rotation3 = new Rotation(f4, f6);
               int rotationPriority3 = this.getRotationPriority();
               byte b3 = -1;
               Rotation rotation4 = rotation3;
               rotationController3.requestRotation(rotation4, b3, this, rotationPriority3);
               return;
            }
         }

         double d11 = mc.thePlayer.posX + MathHelper.clamp_double(mc.thePlayer.motionX * value, -value, value);
         double d12 = mc.thePlayer.lastGroundY - 0.5;
         double d13 = mc.thePlayer.posZ + MathHelper.clamp_double(mc.thePlayer.motionZ * value, -value, value);
         MovingObjectPosition hit2 = mc.theWorld.rayTraceBlocks(d11, d12, d13, mc.thePlayer.posX, d12, mc.thePlayer.posZ, false, false, true);
         if (MoveUtil.isMoving() && hit2 != null && hit2.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            this.blockPlacementData = new BlockPlacementData(hit2.getBlockPos(), hit2.sideHit, hit2.hitVec);
            this.targetBlockPos = this.blockPlacementData.getBlockPos();
            double xCoord3 = this.blockPlacementData.getHitVec().xCoord;
            double yCoord3 = this.blockPlacementData.getHitVec().yCoord;
            double zCoord3 = this.blockPlacementData.getHitVec().zCoord;
            Rotation previousRotation5 = ClientManagers.rotationController.getPreviousRotation();
            boolean flag4 = false;
            Rotation rotationsToPos4 = RotationUtil.getRotationsToPos(xCoord3, yCoord3, zCoord3, previousRotation5, f, f2, flag4);
            RotationUtil rotationUtil7 = this.rotationUtil;
            float yaw6 = rotationsToPos4.yaw;
            float yaw7 = ClientManagers.rotationController.getPreviousRotation().yaw;
            f4 = rotationUtil7.stepYawWithGcd(f, yaw7, yaw6);
            RotationUtil rotationUtil8 = this.rotationUtil;
            float pitch5 = rotationsToPos4.pitch;
            float pitch6 = ClientManagers.rotationController.getPreviousRotation().pitch;
            float f7 = rotationUtil8.updatePitchWithIntaveSin(f2, pitch6, pitch5);
            RotationController rotationController4 = ClientManagers.rotationController;
            Rotation rotation5 = new Rotation(f4, f7);
            int rotationPriority4 = this.getRotationPriority();
            byte b4 = -1;
            Rotation rotation6 = rotation5;
            rotationController4.requestRotation(rotation6, b4, this, rotationPriority4);
         } else {
            RotationController rotationController5 = ClientManagers.rotationController;
            Rotation previousRotation6 = ClientManagers.rotationController.getPreviousRotation();
            int rotationPriority5 = this.getRotationPriority();
            byte b5 = -1;
            Rotation rotation7 = previousRotation6;
            rotationController5.requestRotation(rotation7, b5, this, rotationPriority5);
         }
      }
   }

   private TuplePair computeTargetRotation() {
      float f = this.computeBaseBridgeYaw();
      boolean flag = this.shouldApplyStrafeYaw(f);
      String selectedValue = this.flickMode.getSelectedValue();
      boolean flag2;
      if (this.isTellyBridgeEnabled()) {
         flag2 = this.shouldRotateBackwardInTelly();
      } else {
         exitBlock: {
            exitBlock2:
            if (!selectedValue.equals("None")) {
               if (selectedValue.equals("Jump")) {
                  if (!mc.thePlayer.onGround) {
                     break exitBlock2;
                  }
               } else {
                  if (this.flickUserStrafeSettingForForwardLooking.getValue()) {
                     break exitBlock2;
                  }

                  if (this.isAtBlockEdge()) {
                     break exitBlock2;
                  }
               }

               flag2 = false;
               break exitBlock;
            }

            flag2 = true;
         }
      }

      boolean flag3 = flag2;
      if (flag && flag3 && this.targetBlockPos != null) {
         float strafeYawOffset2 = this.strafeYawOffset;
         double d;
         double d2;
         if (this.initialStrafeDirectionPending) {
            d = 0.5;
            d2 = 0.5;
         } else if ((double)Math.abs(this.strafeYawOffset) <= 22.5) {
            double d3 = MathHelper.clamp_double(this.strafeWidth.nextValue(), 0.0, 0.5);
            d = 0.5 + d3;
            d2 = 0.5 - d3;
         } else {
            Vec3 vectorForRotation = mc.thePlayer.getVectorForRotation(80.0F, 0.0F);
            Vec3 vectorForRotation2 = mc.thePlayer.getVectorForRotation(80.0F, this.strafeYawOffset);
            double d4 = MathHelper.clamp_double(MathStatisticsUtil.lawOfSinesSide(MathStatisticsUtil.angleBetweenDegrees(vectorForRotation, vectorForRotation2), 90.0, 1.6) - 0.05, 0.0, 0.5);
            d = 0.5 + d4;
            d2 = 0.5 - d4;
         }

         exitBlock3:
         if (this.initialStrafeDirectionPending || this.correctRotationWhileBuilding.getValue()) {
            if (selectedValue.equals("Simple")) {
               if (!this.isAtBlockEdge()) {
                  break exitBlock3;
               }
            }

            switch (ScaffoldFacingSwitchMap.ORDINAL_MAP[mc.thePlayer.getHorizontalFacing(f - 180.0F).ordinal()]) {
               case 1:
                  if (mc.thePlayer.posZ > (double)this.targetBlockPos.getZ() + d && strafeYawOffset2 > 0.0F) {
                     this.strafeYawOffset = -((float)Math.max(1.0, this.strafeDegree.nextValue()));
                  } else if (mc.thePlayer.posZ < (double)this.targetBlockPos.getZ() + d2 && strafeYawOffset2 < 0.0F) {
                     this.strafeYawOffset = (float)Math.max(1.0, this.strafeDegree.nextValue());
                  }
                  break;
               case 2:
                  if (mc.thePlayer.posZ > (double)this.targetBlockPos.getZ() + d && strafeYawOffset2 < 0.0F) {
                     this.strafeYawOffset = (float)Math.max(1.0, this.strafeDegree.nextValue());
                  } else if (mc.thePlayer.posZ < (double)this.targetBlockPos.getZ() + d2 && strafeYawOffset2 > 0.0F) {
                     this.strafeYawOffset = -((float)Math.max(1.0, this.strafeDegree.nextValue()));
                  }
                  break;
               case 3:
                  if (mc.thePlayer.posX > (double)this.targetBlockPos.getX() + d && strafeYawOffset2 < 0.0F) {
                     this.strafeYawOffset = (float)Math.max(1.0, this.strafeDegree.nextValue());
                  } else if (mc.thePlayer.posX < (double)this.targetBlockPos.getX() + d2 && strafeYawOffset2 > 0.0F) {
                     this.strafeYawOffset = -((float)Math.max(1.0, this.strafeDegree.nextValue()));
                  }
                  break;
               case 4:
                  if (mc.thePlayer.posX > (double)this.targetBlockPos.getX() + d && strafeYawOffset2 > 0.0F) {
                     this.strafeYawOffset = -((float)Math.max(1.0, this.strafeDegree.nextValue()));
                  } else if (mc.thePlayer.posX < (double)this.targetBlockPos.getX() + d2 && strafeYawOffset2 < 0.0F) {
                     this.strafeYawOffset = (float)Math.max(1.0, this.strafeDegree.nextValue());
                  }
            }
         }

         this.initialStrafeDirectionPending = false;
         boolean flag4 = (double)Math.abs(this.strafeYawOffset) > 22.5;
         if (selectedValue.equals("Simple") && this.flickUserStrafeSettingForForwardLooking.getValue()) {
            if (!this.isAtBlockEdge()) {
               flag4 = !flag4;
            }
         }

         if (flag4) {
            f -= this.strafeYawOffset;
         } else {
            f += this.strafeYawOffset;
         }
      }

      if (selectedValue.equals("Jump")) {
         f += (float)(mc.thePlayer.ticksExisted % 2 == 0 ? 1 : 0);
      }

      if (this.randomizeYaw.getValue() && (this.getYawRandomizeChance() > 0.0 || this.yawRandomizationTriggered)) {
         boolean flag5 = this.yawRandomizationTriggered;
         f += this.computeYawRandomOffset(flag5);
      }

      float pitch = ClientManagers.rotationController.getPreviousRotation().pitch;
      if (pitch > 75.0F) {
         this.keepInitialPitch = false;
      }

      exitBlock4: {
         if (MoveUtil.isMoving()) {
            long l = 200L;
            if (this.enableTimer.reached2(l) && this.keepInitialPitch) {
               break exitBlock4;
            }
         }

         pitch = 80.34F;
      }

      if (this.godBridge.getValue() && !this.godBridgeResetting) {
         f = ClientManagers.rotationController.getPreviousRotation().yaw;
      }

      if (this.targetBlockPos == null) {
         return new TuplePair(new Rotation(f, pitch), true);
      } else {
         Block block;
         block = mc.theWorld.getBlockState(this.targetBlockPos).getBlock();
         exitBlock5:
         if (this.shouldImproveRotation()) {
            if (!selectedValue.equals("None")) {
               if (!this.isAtBlockEdge()) {
                  break exitBlock5;
               }
            }

            if (!this.isTellyBridgeEnabled() || this.shouldRotateBackwardInTelly()) {
               float f2 = RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().yaw, f, 180.0F);
               double d5 = (double)this.targetBlockPos.getX() + block.getBlockBoundsMinX();
               double d6 = (double)this.targetBlockPos.getX() + block.getBlockBoundsMaxX();
               double d7 = (double)this.targetBlockPos.getY() + block.getBlockBoundsMaxY();
               double d8 = (double)this.targetBlockPos.getZ() + block.getBlockBoundsMinZ();
               double d9 = (double)this.targetBlockPos.getZ() + block.getBlockBoundsMaxZ();
               ArrayList list = new ArrayList();
               Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
               boolean flag6 = false;
               float f3 = 180.0F;
               float f4 = 180.0F;
               list.add(RotationUtil.getRotationsToPos(d5, d7, d8, previousRotation, f4, f3, flag6).yaw);
               previousRotation = ClientManagers.rotationController.getPreviousRotation();
               boolean flag7 = false;
               float f5 = 180.0F;
               float f6 = 180.0F;
               list.add(RotationUtil.getRotationsToPos(d5, d7, d9, previousRotation, f6, f5, flag7).yaw);
               previousRotation = ClientManagers.rotationController.getPreviousRotation();
               boolean flag8 = false;
               float f7 = 180.0F;
               float f8 = 180.0F;
               list.add(RotationUtil.getRotationsToPos(d6, d7, d8, previousRotation, f8, f7, flag8).yaw);
               previousRotation = ClientManagers.rotationController.getPreviousRotation();
               boolean flag9 = false;
               float f9 = 180.0F;
               float f10 = 180.0F;
               list.add(RotationUtil.getRotationsToPos(d6, d7, d9, previousRotation, f10, f9, flag9).yaw);
               Collections.sort(list);
               f = MathHelper.clamp_float(f2, (Float)list.get(0) + 0.5F, (Float)list.get(list.size() - 1) - 0.5F);
               this.lastTargetYaw = flag ? f - this.strafeYawOffset : f;
            }
         }

         if (this.isStationaryTowering()) {
            Vec3 positionEyes = mc.thePlayer.getPositionEyes(1.0F);
            double d10 = MathHelper.clamp_double(
               positionEyes.xCoord, (double)this.targetBlockPos.getX() + block.getBlockBoundsMinX(), (double)this.targetBlockPos.getX() + block.getBlockBoundsMaxY()
            );
            double d11 = MathHelper.clamp_double(
               positionEyes.yCoord, (double)this.targetBlockPos.getY() + block.getBlockBoundsMinY(), (double)this.targetBlockPos.getY() + block.getBlockBoundsMaxY() - 0.02
            );
            double d12 = MathHelper.clamp_double(
               positionEyes.zCoord, (double)this.targetBlockPos.getZ() + block.getBlockBoundsMinZ(), (double)this.targetBlockPos.getZ() + block.getBlockBoundsMaxZ()
            );
            Rotation previousRotation2 = ClientManagers.rotationController.getPreviousRotation();
            boolean flag10 = false;
            float f11 = 180.0F;
            float f12 = 180.0F;
            Rotation rotationsToPos = RotationUtil.getRotationsToPos(d10, d11, d12, previousRotation2, f12, f11, flag10);
            if (new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ).equalsXZ(this.targetBlockPos)) {
               rotationsToPos.yaw = f;
            }

            Rotation previousRotation3 = ClientManagers.rotationController.getPreviousRotation();
            rotationsToPos = RotationUtil.applyGcdFix(rotationsToPos, previousRotation3);
            return new TuplePair(rotationsToPos, true);
         } else {
            boolean isNearCardinalYaw = RotationUtil.isNearCardinalYaw(f);
            if (isNearCardinalYaw && this.isLicenseValid()) {
               if (this.staticPitchBool.getValue() && (!mc.gameSettings.keyBindJump.isKeyDown() || this.staticPitchAtJump.getValue()) && !this.shouldIgnoreStaticPitchForClutch()) {
                  if (!this.staticPitchRandomizeOnlyAtStart.getValue()) {
                     this.staticPitch.nextValue();
                  }

                  float lastValue = (float)this.staticPitch.getLastValue();
                  Rotation rotation = new Rotation(f, lastValue);
                  return new TuplePair(this.applyMinimalMouseMovement(rotation), true);
               }
            } else if (this.staticPitchBoolDiagonal.getValue() && (!mc.gameSettings.keyBindJump.isKeyDown() || this.staticPitchAtJumpDiagonal.getValue()) && !this.shouldIgnoreDiagonalStaticPitchForClutch()) {
               if (!this.staticPitchRandomizeOnlyAtStartDiagonal.getValue()) {
                  this.staticPitchDiagonal.nextValue();
               }

               float lastValue2 = (float)this.staticPitchDiagonal.getLastValue();
               Rotation rotation2 = new Rotation(f, lastValue2);
               return new TuplePair(this.applyMinimalMouseMovement(rotation2), true);
            }

            return this.godBridge.getValue()
               ? new TuplePair(new Rotation(f, RotationUtil.isNearCardinalYaw(Augustus.getInstance().getYawPitchHelper().realYaw) ? 75.83F : 76.5F), true)
               : new TuplePair(new Rotation(f, pitch), false);
         }
      }
   }

   private Rotation applyMinimalMouseMovement(Rotation rotation) {
      if (this.minimalMouseMovement.getValue() && !AntiSamplerGuard.isGuardActive(1)) {
         long l = (long)this.minimalMouseMoveIntervalMs;
         if (this.minimalMouseMoveTimer.reached2(l) && mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ).getBlock().getMaterial() != Material.air
            )
          {
            this.minimalMouseMoveCounter.increment();
            int n = this.minimalMouseMoveDurationTicks;
            if (this.minimalMouseMoveCounter.hasReached2(n)) {
               short s = 1500;
               short s2 = 750;
               this.minimalMouseMoveIntervalMs = DefaultRandomGenerator.instance.nextInt2(s2, s);
               byte b = 2;
               byte b2 = 1;
               this.minimalMouseMoveDurationTicks = DefaultRandomGenerator.instance.nextInt2(b2, b);
               this.minimalMouseMoveCounter.reset();
               this.minimalMouseMoveTimer.reset();
            }

            double d = 0.2;
            double d2 = -0.2;
            float f = DefaultRandomGenerator.instance.nextFloat2(d2, d);
            double d3 = 0.2;
            double d4 = -0.2;
            float f2 = DefaultRandomGenerator.instance.nextFloat2(d4, d3);
            if (this.minimalMouseYawOffset + f > 5.0F || this.minimalMouseYawOffset + f < 5.0F) {
               f *= -1.0F;
            }

            Rotation rotation2 = RotationUtil.applyGcdFix(rotation.yaw + this.minimalMouseYawOffset + f, rotation.pitch + this.minimalMousePitchOffset + f2, ClientManagers.rotationController.getPreviousRotation());
            boolean isNearCardinalYaw = RotationUtil.isNearCardinalYaw(this.getMovementYaw());
            double d5;
            if (isNearCardinalYaw) {
               d5 = this.staticPitchDiagonal.getMinValue();
            } else {
               d5 = this.staticPitch.getMaxValue();
            }

            float f3 = (float)d5;
            if (isNearCardinalYaw) {
               d5 = this.staticPitchDiagonal.getMaxValue();
            } else {
               d5 = this.staticPitch.getMaxValue();
            }

            float f4 = (float)d5;
            this.minimalMouseYawOffset += f;
            if (rotation2.pitch >= f4) {
               this.minimalMousePitchOffset -= f2;
               rotation2 = RotationUtil.applyGcdFix(rotation.yaw + this.minimalMouseYawOffset, MathHelper.clamp_float(rotation.pitch + this.minimalMousePitchOffset, f3, f4), ClientManagers.rotationController.getPreviousRotation());
            } else if (rotation2.pitch <= f3) {
               this.minimalMousePitchOffset -= f2;
               rotation2 = RotationUtil.applyGcdFix(rotation.yaw + this.minimalMouseYawOffset, MathHelper.clamp_float(rotation.pitch + this.minimalMousePitchOffset, f3, f4), ClientManagers.rotationController.getPreviousRotation());
            } else {
               this.minimalMousePitchOffset += f2;
            }

            rotation = rotation2;
         } else {
            rotation.yaw = rotation.yaw + this.minimalMouseYawOffset;
            rotation.pitch = rotation.pitch + this.minimalMousePitchOffset;
         }
      } else {
         this.minimalMousePitchOffset = 0.0F;
         this.minimalMouseYawOffset = 0.0F;
      }

      return rotation;
   }

   private void updateStandardRotation() {
      TuplePair tuplePair = this.computeTargetRotation();
      Rotation first = (Rotation)tuplePair.getFirst();
      Rotation rotation;
      if (this.isBezierRotationModeActive()) {
         this.bezierRotationStepper.aimSpeed = this.getActiveAimSpeed() / 100.0 * 16.0 + 2.0;
         this.bezierRotationStepper.adaptCurveIfNeeded = true;
         byte b = 3;
         Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
         Rotation lastBezierRotation2 = this.lastBezierRotation;
         if (!previousRotation.equalsRotation(lastBezierRotation2) || this.rotationKinematicState == null || this.bezierCurve == null) {
            RotationUtil rotationUtil2 = this.rotationUtil;
            Rotation previousRotation2 = ClientManagers.rotationController.getPreviousRotation();
            this.rotationKinematicState = rotationUtil2.createKinematicState(previousRotation2);
            RotationUtil rotationUtil3 = this.rotationUtil;
            Rotation previousRotation3 = ClientManagers.rotationController.getPreviousRotation();
            boolean flag = false;
            double d = 10.0;
            double d2 = 0.3;
            Rotation rotation2 = previousRotation3;
            this.bezierCurve = rotationUtil3.createBezierCurve(rotation2, first, b, d2, d, flag);
            this.bezierTickCounter = 0;
         }

         if (this.bezierTickCounter % b == 0) {
            RotationUtil rotationUtil4 = this.rotationUtil;
            Rotation previousRotation4 = ClientManagers.rotationController.getPreviousRotation();
            boolean flag2 = false;
            double d3 = 10.0;
            double d4 = 0.3;
            Rotation rotation3 = previousRotation4;
            this.bezierCurve = rotationUtil4.createBezierCurve(rotation3, first, b, d4, d3, flag2);
         }

         double d5 = 0.0;
         BezierCurve2D bezierCurve2 = this.bezierCurve;
         RotationKinematicState rotationKinematicState2 = this.rotationKinematicState;
         BezierRotationStepper bezierRotationStepper2 = this.bezierRotationStepper;
         this.rotationKinematicState = this.rotationUtil.stepAndAdvanceBezierCurve(bezierRotationStepper2, rotationKinematicState2, bezierCurve2, b, d5);
         this.bezierTickCounter++;
         rotation = RotationUtil.applyGcdFix(
            RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().yaw, (float)this.rotationKinematicState.getYaw(), 180.0F),
            RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().pitch, (float)this.rotationKinematicState.getPitch(), 180.0F),
            ClientManagers.rotationController.getPreviousRotation().yaw,
            ClientManagers.rotationController.getPreviousRotation().pitch
         );
         this.lastBezierRotation = new Rotation(rotation);
      } else {
         float f = this.rollActiveYawSpeed();
         float f2 = this.rollActivePitchSpeed();
         RotationUtil rotationUtil5 = this.rotationUtil;
         float yaw = ClientManagers.rotationController.getPreviousRotation().yaw;
         float yaw2 = first.yaw;
         RandomizedRangeSettings activeYawAcceleration = this.getActiveYawAcceleration();
         float f3 = ClientManagers.rotationController.getPreviousRotation().yaw - ClientManagers.rotationController.getLastTickRotation().yaw;
         RandomizedRangeSettings randomizedRangeSettings = activeYawAcceleration;
         float f4 = yaw2;
         float f5 = yaw;
         float f6 = rotationUtil5.stepYawAccelerated(f, f5, f4, randomizedRangeSettings, f3);
         RotationUtil rotationUtil6 = this.rotationUtil;
         yaw2 = ClientManagers.rotationController.getPreviousRotation().pitch;
         float f7 = RotationUtil.applyPitchGcd(first.pitch, ClientManagers.rotationController.getPreviousRotation().pitch);
         RandomizedRangeSettings randomizedRangeSettings2 = this.getActivePitchAcceleration();
         float f8 = ClientManagers.rotationController.getPreviousRotation().pitch - ClientManagers.rotationController.getLastTickRotation().pitch;
         RandomizedRangeSettings randomizedRangeSettings3 = randomizedRangeSettings2;
         float f9 = f7;
         float f10 = yaw2;
         rotation = new Rotation(f6, rotationUtil6.stepPitchAccelerated(f2, f10, f9, randomizedRangeSettings3, f8));
      }

      if ((Boolean)tuplePair.getSecond()) {
         boolean flag3 = false;
         Object obj = null;
         float pitch = rotation.pitch;
         float yaw3 = rotation.yaw;
         this.applyScaffoldRotation(yaw3, pitch, (MovingObjectPosition)obj, flag3);
      } else {
         float yaw4 = rotation.yaw;
         float pitch2 = rotation.pitch;
         if (this.targetBlockPos == null) {
            Object obj2 = null;
            this.applyScaffoldRotation(yaw4, pitch2, (MovingObjectPosition)obj2);
         } else {
            if (this.isStationaryTowering()) {
               Object obj3 = null;
               this.applyScaffoldRotation(yaw4, pitch2, (MovingObjectPosition)obj3);
            } else {
               if (this.godBridge.getValue()) {
                  Object obj4 = null;
                  this.applyScaffoldRotation(yaw4, pitch2, (MovingObjectPosition)obj4);
                  return;
               }

               if (this.recycleRotation.getValue()) {
                  MovingObjectPosition hit = RayCastUtil.rayCast(1.0F, yaw4, pitch2);
                  boolean flag4 = !this.intave.getValue()
                     && this.towerWhileMoving.getValue()
                     && hit.getBlockPos() != null
                     && !mc.thePlayer.onGround
                     && mc.thePlayer.posY - (double)hit.getBlockPos().getY() > 2.0
                     && mc.thePlayer.posY - (double)hit.getBlockPos().getY() < 2.5;
                  if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
                     && BlockUtil.isSolidBlock(hit.getBlockPos())
                     && hit.sideHit != EnumFacing.DOWN
                     && (flag4 ? hit.sideHit == EnumFacing.UP : hit.sideHit != EnumFacing.UP)
                     && this.targetBlockPos.equalsPos(hit.getBlockPos())) {
                     this.applyScaffoldRotation(yaw4, pitch2, hit);
                     return;
                  }
               }

               if (this.isAtBlockEdgeForYaw(yaw4) && this.targetBlockPos != null) {
                  float f11 = yaw4;
                  if (this.randomizeYaw.getValue() && this.randomizeYawChanceWhilePitchRotation.getValue() > 0.0 && yaw4 == ClientManagers.rotationController.getPreviousRotation().yaw) {
                     this.yawRandomizationTriggered = true;
                     boolean flag5 = true;
                     float f12 = this.computeYawRandomOffset(flag5);
                     yaw4 = RotationUtil.applyYawGcd(f12 + yaw4, ClientManagers.rotationController.getPreviousRotation().yaw);
                  }

                  ArrayList list = new ArrayList();
                  float f13 = 0.0F;

                  for (float i = 15.0F; i < (float)(!this.intave.getValue() && !this.otherRotation.getValue() ? 90 : 85); i += 0.04F) {
                     float f14 = RotationUtil.applyPitchGcd(i, ClientManagers.rotationController.getPreviousRotation().pitch);
                     if (f14 != f13) {
                        MovingObjectPosition hit2 = RayCastUtil.rayCast(1.0F, yaw4, f14);
                        if (hit2 != null) {
                           boolean flag6 = !this.intave.getValue()
                              && this.towerWhileMoving.getValue()
                              && hit2.getBlockPos() != null
                              && !mc.thePlayer.onGround
                              && mc.thePlayer.posY - (double)hit2.getBlockPos().getY() > 2.0
                              && mc.thePlayer.posY - (double)hit2.getBlockPos().getY() < 2.5;
                           if (hit2.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
                              && BlockUtil.isSolidBlock(hit2.getBlockPos())
                              && hit2.sideHit != EnumFacing.DOWN
                              && (flag6 ? hit2.sideHit == EnumFacing.UP : hit2.sideHit != EnumFacing.UP)
                              && this.targetBlockPos.equalsPos(hit2.getBlockPos())
                              && (hit2.hitVec.yCoord % 1.0 < 0.88 && hit2.hitVec.yCoord % 1.0 > 0.18 || !this.intave.getValue())) {
                              list.add(new ScaffoldRaycastResult(hit2, f14));
                           }
                        }
                     }

                     f13 = f14;
                  }

                  ScaffoldRaycastResult scaffoldRaycastResult = this.selectBestPitchCandidate(list, yaw4);
                  if (scaffoldRaycastResult != null) {
                     TuplePair tuplePair2 = this.clampPitchCorrection(scaffoldRaycastResult, yaw4);
                     float first2 = (Float)tuplePair2.getFirst();
                     MovingObjectPosition second = (MovingObjectPosition)tuplePair2.getSecond();
                     boolean flag7 = true;
                     MovingObjectPosition hit3 = second;
                     float f15 = first2;
                     this.applyScaffoldRotation(yaw4, f15, hit3, flag7);
                     return;
                  }

                  if (this.extraBlockSearch.getValue()
                     && (!this.extraBlockSearchOnlyAtClutch.getValue() || this.isBlockClutching())
                     && mc.theWorld
                        .getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001))
                        .isEmpty()
                     && mc.thePlayer.onGround) {
                     int n = Math.min(30, (int)this.yawSpeed.getMaxValue());
                     BlockPos pos = new BlockPos(mc.thePlayer.posX + mc.thePlayer.motionX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ + mc.thePlayer.motionZ);
                     int n2 = n * 2;
                     ArrayList list2 = new ArrayList();
                     byte b2 = 0;
                     float f16 = ClientManagers.rotationController.getPreviousRotation().yaw + (float)n;
                     float f17 = ClientManagers.rotationController.getPreviousRotation().yaw - (float)n;

                     for (int j = 0; n2 > 0; n2--) {
                        if (b2 == 0) {
                           float f18 = yaw4 + (float)(++j);
                           float f19 = yaw4 - (float)j;
                           if (f18 > f16) {
                              b2 = 1;
                           }

                           if (f19 < f17) {
                              b2 = -1;
                           }

                           list2.add(f18);
                           list2.add(f19);
                           n2 -= 2;
                        } else if (b2 > 0) {
                           j++;
                           n2--;
                           list2.add(yaw4 - (float)j);
                        } else {
                           j++;
                           n2--;
                           list2.add(yaw4 + (float)j);
                        }
                     }

                     for (float f20 : (Iterable<Float>)(list2)) {
                        list.clear();
                        f13 = 0.0F;
                        float f21 = RotationUtil.applyYawGcd(f20, ClientManagers.rotationController.getPreviousRotation().yaw);

                        for (float k = 15.0F; k < (float)(!this.intave.getValue() && !this.otherRotation.getValue() ? 90 : 85); k += 0.04F) {
                           float f22 = RotationUtil.applyPitchGcd(k, ClientManagers.rotationController.getPreviousRotation().pitch);
                           if (f22 != f13) {
                              MovingObjectPosition hit4 = RayCastUtil.rayCast(1.0F, f21, f22);
                              if (hit4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
                                 && BlockUtil.isSolidBlock(hit4.getBlockPos())
                                 && hit4.sideHit != EnumFacing.DOWN
                                 && hit4.sideHit != EnumFacing.UP
                                 && (hit4.getBlockPos().offset(hit4.sideHit).equalsXZ(pos) && !this.sameYBlockJumpActive || hit4.getBlockPos().equalsPos(this.targetBlockPos))
                                 && (hit4.hitVec.yCoord % 1.0 < 0.88 && hit4.hitVec.yCoord % 1.0 > 0.18 || !this.intave.getValue())) {
                                 list.add(new ScaffoldRaycastResult(hit4, f22));
                              }
                           }

                           f13 = f22;
                        }

                        scaffoldRaycastResult = this.selectBestPitchCandidate(list, f21);
                        if (scaffoldRaycastResult != null) {
                           TuplePair tuplePair3 = this.clampPitchCorrection(scaffoldRaycastResult, f21);
                           float first3 = (Float)tuplePair3.getFirst();
                           MovingObjectPosition second2 = (MovingObjectPosition)tuplePair3.getSecond();
                           boolean flag8 = true;
                           MovingObjectPosition hit5 = second2;
                           float f23 = first3;
                           this.applyScaffoldRotation(f21, f23, hit5, flag8);
                           return;
                        }
                     }
                  }

                  yaw4 = f11;
               }
            }

            Object obj5 = null;
            this.applyScaffoldRotation(yaw4, pitch2, (MovingObjectPosition)obj5);
         }
      }
   }

   private TuplePair clampPitchCorrection(ScaffoldRaycastResult scaffoldRaycastResult, float f) {
      if (this.maxPitchCorrection.getValue() == 180.0) {
         return new TuplePair(scaffoldRaycastResult.pitch, scaffoldRaycastResult.hitResult);
      } else {
         MovingObjectPosition hitResult = scaffoldRaycastResult.hitResult;
         float f2 = this.isBlockClutching() ? (float)this.maxPitchCorrectionAtBlockClutch.getValue() : (float)this.maxPitchCorrection.getValue();
         float f3 = MathHelper.clamp_float(scaffoldRaycastResult.pitch, ClientManagers.rotationController.getPreviousRotation().pitch - f2, ClientManagers.rotationController.getPreviousRotation().pitch + f2);
         if (f3 != scaffoldRaycastResult.pitch) {
            f3 = RotationUtil.applyPitchGcd(f3, ClientManagers.rotationController.getPreviousRotation().pitch);
            hitResult = RayCastUtil.rayCast(1.0F, f, f3);
         }

         return new TuplePair(f3, hitResult);
      }
   }

   private boolean isBlockClutching() {
      boolean flag = false;
      return this.checkBlockClutchState(flag);
   }

   private boolean checkBlockClutchState(boolean flag) {
      if (mc.thePlayer.onGround && this.isLicenseValid()) {
         return false;
      } else if (mc.thePlayer.fallDistance > 3.0F) {
         return true;
      } else {
         return mc.thePlayer.hurtTime != 0 ? true : mc.thePlayer.posY < mc.thePlayer.lastGroundY + (flag ? 0.5 : 0.9) && mc.thePlayer.motionY < 0.0;
      }
   }

   private boolean isImproveClutchConditionMet() {
      return this.improveOnlyAtClutch.getValue() && this.improve.getValue() ? this.isBlockClutching() : false;
   }

   private boolean shouldIgnoreStaticPitchForClutch() {
      return this.ignoreStaticPitchForBlockClutches.getValue() && this.staticPitchBool.getValue() ? this.isBlockClutching() : false;
   }

   private boolean shouldIgnoreDiagonalStaticPitchForClutch() {
      return this.ignoreStaticPitchForBlockClutchesDiagonal.getValue() && this.staticPitchBoolDiagonal.getValue() ? this.isBlockClutching() : false;
   }

   private ScaffoldRaycastResult selectBestPitchCandidate(ArrayList list, float f) {
      if (list.isEmpty()) {
         return null;
      } else {
         String selectedValue = this.pitchSelection.getSelectedValue();
         switch (selectedValue) {
            case "Nearest":
               list.sort(Comparator.comparingDouble(obj -> this.getPitchDeltaScore((ScaffoldRaycastResult)obj)));
               return (ScaffoldRaycastResult)list.get(0);
            case "Furthest":
               list.sort(Comparator.comparingDouble(obj2 -> this.scoreFurthestPitchCandidate((ScaffoldRaycastResult)obj2)));
               return (ScaffoldRaycastResult)list.get(0);
            case "Highest":
               ScaffoldRaycastResult scaffoldRaycastResult = null;

               for (ScaffoldRaycastResult scaffoldRaycastResult2 : (Iterable<ScaffoldRaycastResult>)(list)) {
                  if (scaffoldRaycastResult2 != null && (scaffoldRaycastResult == null || scaffoldRaycastResult2.pitch > scaffoldRaycastResult.pitch)) {
                     scaffoldRaycastResult = scaffoldRaycastResult2;
                  }
               }

               return scaffoldRaycastResult;
            case "Lowest":
               ScaffoldRaycastResult scaffoldRaycastResult3 = null;

               for (ScaffoldRaycastResult scaffoldRaycastResult4 : (Iterable<ScaffoldRaycastResult>)(list)) {
                  if (scaffoldRaycastResult4 != null && (scaffoldRaycastResult3 == null || scaffoldRaycastResult4.pitch < scaffoldRaycastResult3.pitch)) {
                     scaffoldRaycastResult3 = scaffoldRaycastResult4;
                  }
               }

               return scaffoldRaycastResult3;
            case "Mid":
               list.sort(Comparator.comparingDouble(obj3 -> this.scoreMidPitchCandidate(f, (ScaffoldRaycastResult)obj3)));
               return (ScaffoldRaycastResult)list.get(0);
            case "Random":
               RandomNumberGenerator instance = DefaultRandomGenerator.instance;
               int n = list.size() - 1;
               byte b = 0;
               return (ScaffoldRaycastResult)list.get(instance.nextInt2(b, n));
            default:
               return null;
         }
      }
   }

   private float computeYawRandomOffset(boolean flag) {
      if (AntiSamplerGuard.isGuardActive(1)) {
         return 0.0F;
      } else if (Math.random() * 100.0 > (flag ? this.randomizeYawChanceWhilePitchRotation.getValue() : this.getYawRandomizeChance())) {
         return 0.0F;
      } else {
         float f;
         if (this.otherYawRandomization.getValue()) {
            double d = (double)mc.thePlayer.ticksExisted * 0.75;
            double d2 = 0.2;
            double d3 = -0.2;
            f = (float)Math.sin(d + (double)DefaultRandomGenerator.instance.nextFloat2(d3, d2)) * 0.75F;
         } else {
            RandomNumberGenerator instance = DefaultRandomGenerator.instance;
            double d4 = -this.randomizeYawVal.getLastValue();
            double d5 = this.randomizeYawVal.nextValue();
            double d6 = d4;
            f = instance.nextFloat2(d6, d5);
         }

         TickCounter yawRandomizeCounter2 = this.yawRandomizeCounter;
         int lastIntValue = this.randomizeYawInterval.getLastIntValue();
         if (yawRandomizeCounter2.hasReached2(lastIntValue)) {
            this.yawRandomizeCounter.reset();
            this.randomizeYawInterval.nextValue();
            this.randomizeYawVal.nextValue();
            this.yawRandomizationTriggered = false;
         }

         return f;
      }
   }

   public float computePitchRandomOffset(boolean flag) {
      if (AntiSamplerGuard.isGuardActive(1)) {
         return 0.0F;
      } else if (Math.random() * 100.0 > (flag ? this.randomizePitchChanceWhileYawRotation.getValue() : this.getPitchRandomizeChance())) {
         return 0.0F;
      } else {
         float f;
         if (this.otherYawRandomization.getValue()) {
            double d = (double)mc.thePlayer.ticksExisted * 0.75;
            double d2 = 0.2;
            double d3 = -0.2;
            f = (float)Math.sin(d + (double)DefaultRandomGenerator.instance.nextFloat2(d3, d2) + 1000.0) * 0.75F;
         } else {
            RandomNumberGenerator instance = DefaultRandomGenerator.instance;
            double d4 = -this.randomizePitchVal.getLastValue();
            double d5 = this.randomizePitchVal.nextValue();
            double d6 = d4;
            f = instance.nextFloat2(d6, d5);
         }

         TickCounter pitchRandomizeCounter2 = this.pitchRandomizeCounter;
         int lastIntValue = this.randomizePitchInterval.getLastIntValue();
         if (pitchRandomizeCounter2.hasReached2(lastIntValue)) {
            this.pitchRandomizeCounter.reset();
            this.randomizePitchInterval.nextValue();
            this.randomizePitchVal.nextValue();
            this.pitchRandomizationTriggered = false;
         }

         return f;
      }
   }

   private double getYawRandomizeChance() {
      BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ);
      return mc.theWorld.isAirBlock(pos) ? this.randomizeYawChanceOnAir.getValue() : this.randomizeYawChanceOnBlock.getValue();
   }

   private double getPitchRandomizeChance() {
      BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ);
      return mc.theWorld.isAirBlock(pos) ? this.randomizePitchChanceInAir.getValue() : this.randomizePitchChanceOnBlock.getValue();
   }

   private void applyScaffoldRotation(float f, float f2, MovingObjectPosition hit) {
      boolean flag = false;
      this.applyScaffoldRotation(f, f2, hit, flag);
   }

   private void applyScaffoldRotation(float f, float f2, MovingObjectPosition hit, boolean flag) {
      boolean flag2 = false;
      this.applyScaffoldRotation(f, f2, hit, flag, flag2);
   }

   private void applyScaffoldRotation(float f, float f2, MovingObjectPosition hit, boolean flag, boolean flag2) {
      RotationUtil rotationUtil2 = this.rotationUtil;
      Rotation rotation = new Rotation(f, f2);
      Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
      Rotation rotation2 = rotation;
      rotationUtil2.updateLastRotationSpeeds(rotation2, previousRotation);
      this.overrideRayTrace = hit;
      if (this.randomizeYaw.getValue() && this.randomizeYawChanceWhilePitchRotation.getValue() > 0.0 && f2 != ClientManagers.rotationController.getPreviousRotation().pitch && !flag && f == ClientManagers.rotationController.getPreviousRotation().yaw) {
         this.yawRandomizationTriggered = true;
         boolean flag3 = true;
         f = RotationUtil.applyYawGcd(this.computeYawRandomOffset(flag3) + f, ClientManagers.rotationController.getPreviousRotation().yaw);
         if (this.overrideRayTrace != null) {
            this.overrideRayTrace = RayCastUtil.rayCast(1.0F, f, f2);
         }
      }

      if ((f != ClientManagers.rotationController.getPreviousRotation().yaw || this.getPitchRandomizeChance() > 0.0) && this.randomizePitch.getValue() && f2 == ClientManagers.rotationController.getPreviousRotation().pitch) {
         this.pitchRandomizationTriggered = true;
         boolean flag4 = f != ClientManagers.rotationController.getPreviousRotation().yaw;
         f2 = RotationUtil.applyPitchGcd(this.computePitchRandomOffset(flag4) + f2, ClientManagers.rotationController.getPreviousRotation().pitch);
         if (f == ClientManagers.rotationController.getPreviousRotation().yaw == this.randomizeYaw.getValue() && this.randomizeYawChanceWhilePitchRotation.getValue() > 0.0 && f2 != ClientManagers.rotationController.getPreviousRotation().pitch) {
            this.yawRandomizationTriggered = true;
            boolean flag5 = true;
            f = RotationUtil.applyYawGcd(this.computeYawRandomOffset(flag5) + f, ClientManagers.rotationController.getPreviousRotation().yaw);
         }

         if (this.overrideRayTrace != null) {
            this.overrideRayTrace = RayCastUtil.rayCast(1.0F, f, f2);
         }
      }

      if (this.godBridge.getValue() && (f != ClientManagers.rotationController.getPreviousRotation().yaw || f2 != ClientManagers.rotationController.getPreviousRotation().pitch)) {
         this.resetGodBridgeState();
      }

      RotationController rotationController = ClientManagers.rotationController;
      float[] floats = new float[]{f, f2};
      int rotationPriority = this.getRotationPriority();
      byte b = -1;
      float[] floats2 = floats;
      rotationController.requestRotation2(floats2, b, this, rotationPriority);
   }

   private int getRotationPriority() {
      return 0;
   }

   private boolean shouldApplyStrafeYaw(float f) {
      return this.isStrafeActive() && RotationUtil.isNearCardinalYaw(f) && this.targetBlockPos != null;
   }

   private double getPitchDeltaFromNull(long l, float[] floats) {
      return (double)Math.abs(floats[0] - ((Rotation)null).pitch);
   }

   private double getPitchDeltaScore(ScaffoldRaycastResult scaffoldRaycastResult) {
      return (double)Math.abs(scaffoldRaycastResult.pitch - ClientManagers.rotationController.getPreviousRotation().pitch);
   }

   private double getHitCenterOffset(ScaffoldRaycastResult scaffoldRaycastResult, float f) {
      MovingObjectPosition hit = RayCastUtil.rayCast(1.0F, f, scaffoldRaycastResult.pitch);
      if (hit != null && hit.getBlockPos() != null && hit.hitVec != null) {
         double d = hit.hitVec.yCoord % 1.0;
         return Math.abs(0.5 - d);
      } else {
         return 100.0;
      }
   }

   private boolean isAtBlockEdge() {
      float yaw = ClientManagers.rotationController.getPreviousRotation().yaw;
      return this.isAtBlockEdgeForYaw(yaw);
   }

   private boolean isAtBlockEdgeForYaw(float f) {
      if (this.isToweringWhileMoving()) {
         return true;
      } else if (this.edgeRotateCheck.getValue() && this.targetBlockPos != null) {
         if (!this.isOutsideTargetBlockBounds()) {
            if (!this.isPredictedOutsideNextBlock(f)) {
               return false;
            }
         }

         return true;
      } else {
         return mc.theWorld
            .isAirBlock(MathHelper.floor_double(mc.thePlayer.posX), MathHelper.floor_double(mc.thePlayer.posY - 0.5), MathHelper.floor_double(mc.thePlayer.posZ));
      }
   }

   private boolean isToweringWhileMoving() {
      return !mc.thePlayer.onGround && this.towerWhileMoving.getValue() && MoveUtil.isMoving() && mc.thePlayer.posY - mc.thePlayer.lastGroundY > 1.0;
   }

   private void setMoonWalkLeftStrafe(EventMoveInput event) {
      if (event.getMoveForward() != 0.0F) {
         this.moonWalkStrafeDir = event.getMoveForward() > 0.0F ? 1 : -1;
      } else if (event.getMoveStrafe() != 0.0F) {
         this.moonWalkStrafeDir = event.getMoveStrafe() > 0.0F ? -1 : 1;
      }

      if (this.lastMoonWalkSide == 2) {
         this.moonWalkWideness.nextValue();
         TickCounter tickCounter = this.moonWalkMiddleRecalcCounter;
         int lastIntValue = this.delayToRecalculateMiddle.getLastIntValue();
         if (tickCounter.hasReached2(lastIntValue)) {
            this.moonWalkMiddle.nextValue();
            this.delayToRecalculateMiddle.nextValue();
            this.moonWalkMiddleRecalcCounter.reset();
         }
      }

      this.lastMoonWalkSide = 1;
   }

   private void setMoonWalkRightStrafe(EventMoveInput event) {
      if (event.getMoveForward() != 0.0F) {
         this.moonWalkStrafeDir = event.getMoveForward() > 0.0F ? -1 : 1;
      } else if (event.getMoveStrafe() != 0.0F) {
         this.moonWalkStrafeDir = event.getMoveStrafe() > 0.0F ? 1 : -1;
      }

      if (this.lastMoonWalkSide == 1) {
         this.moonWalkWideness.nextValue();
         TickCounter tickCounter = this.moonWalkMiddleRecalcCounter;
         int lastIntValue = this.delayToRecalculateMiddle.getLastIntValue();
         if (tickCounter.hasReached2(lastIntValue)) {
            this.moonWalkMiddle.nextValue();
            this.delayToRecalculateMiddle.nextValue();
            this.moonWalkMiddleRecalcCounter.reset();
         }
      }

      this.lastMoonWalkSide = 2;
   }

   private void setKeyBindCorrectionLeft(EventMoveInput event) {
      if (event.getMoveForward() != 0.0F) {
         this.keyBindStrafeCorrectionDir = event.getMoveForward() > 0.0F ? 1 : -1;
      } else if (event.getMoveStrafe() != 0.0F) {
         this.keyBindStrafeCorrectionDir = event.getMoveStrafe() > 0.0F ? -1 : 1;
      }
   }

   private void setKeyBindCorrectionRight(EventMoveInput event) {
      if (event.getMoveForward() != 0.0F) {
         this.keyBindStrafeCorrectionDir = event.getMoveForward() > 0.0F ? -1 : 1;
      } else if (event.getMoveStrafe() != 0.0F) {
         this.keyBindStrafeCorrectionDir = event.getMoveStrafe() > 0.0F ? 1 : -1;
      }
   }

   private void applyMoonWalkStrafe(EventMoveInput event) {
      int moonWalkStrafeDir2 = this.moonWalkStrafeDir;
      this.moonWalkDirChangeTicks++;
      if (this.lastAppliedStrafeInput != 0 && moonWalkStrafeDir2 != 0 && this.lastAppliedStrafeInput != moonWalkStrafeDir2) {
         this.moonWalkDirChangeTicks = 0;
         this.directionChangeDelay.nextValue();
         int lastIntValue = this.directionChangeDelay.getLastIntValue();
         if (lastIntValue > 0) {
            Block block = mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ).getBlock();
            if (block.getMaterial() == Material.air && mc.thePlayer.onGround) {
               this.moonWalkDirChangeTicks = 100;
            }
         }
      }

      if (this.moonWalkDirChangeTicks >= this.directionChangeDelay.getLastIntValue()) {
         if (event.getMoveForward() != 0.0F) {
            float f = (float)moonWalkStrafeDir2;
            event.setMoveStrafe(f);
         } else if (event.getMoveStrafe() != 0.0F) {
            float f2 = (float)moonWalkStrafeDir2;
            event.setMoveForward(f2);
         }
      }
   }

   public boolean isPredictedOutsideNextBlock(float f) {
      double[] doubles = PlayerUtil.predictPlayerPosition(0, mc.thePlayer, f);
      BlockPos pos = this.findNextRaytracedBlockPos();
      if (pos == null) {
         return false;
      } else {
         double d = (double)pos.getX() + 1.285;
         double d2 = (double)pos.getX() - 0.285;
         double d3 = (double)pos.getZ() + 1.285;
         double d4 = (double)pos.getZ() - 0.285;
         return doubles[0] > d || doubles[0] < d2 || doubles[2] > d3 || doubles[2] < d4;
      }
   }

   private BlockPos findNextRaytracedBlockPos() {
      ArrayList list = new ArrayList();

      for (float i = 15.0F; i < (float)(!this.intave.getValue() && !this.otherRotation.getValue() ? 90 : 85); i += 0.05F) {
         float[] floats = new float[]{ClientManagers.rotationController.getPreviousRotation().yaw, i};
         MovingObjectPosition hit = mc.thePlayer.rayTraceCustom(4.5, 2.0F, ClientManagers.rotationController.getPreviousRotation().yaw, floats[1]);
         if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
            && BlockUtil.isSolidBlock(hit.getBlockPos())
            && hit.sideHit != EnumFacing.DOWN
            && hit.sideHit != EnumFacing.UP
            && (hit.hitVec.yCoord % 1.0 < 0.65 && hit.hitVec.yCoord % 1.0 > 0.35 || !this.intave.getValue())) {
            list.add(floats[1]);
         }
      }

      float[] floats2 = new float[2];
      if (!list.isEmpty()) {
         list.sort(Comparator.comparingDouble(obj -> (double)Math.abs(((Number)obj).floatValue() - ClientManagers.rotationController.getPreviousRotation().pitch)));
         if (!list.isEmpty()) {
            floats2[1] = (Float)list.get(0);
            floats2[0] = ClientManagers.rotationController.getPreviousRotation().yaw;
         }

         MovingObjectPosition hit2 = mc.thePlayer.rayTraceCustom(4.5, 2.0F, floats2[0], floats2[1]);
         EnumFacing sideHit = hit2.sideHit;
         BlockPos blockPos = hit2.getBlockPos();
         if (sideHit == EnumFacing.EAST) {
            return blockPos.add(1, 0, 0);
         }

         if (sideHit == EnumFacing.WEST) {
            return blockPos.add(-1, 0, 0);
         }

         if (sideHit == EnumFacing.NORTH) {
            return blockPos.add(0, 0, -1);
         }

         if (sideHit == EnumFacing.SOUTH) {
            return blockPos.add(0, 0, 1);
         }
      }

      return null;
   }

   private boolean isTellyBridgeEnabled() {
      return this.autoJump.getValue() && this.sameY.getValue() && this.tellyBridge.getValue();
   }

   private boolean shouldRotateBackwardInTelly() {
      boolean flag = !mc.thePlayer.onGround && mc.thePlayer.airTicks < 1
         || mc.thePlayer.onGround && PlayerUtil.predictPlayerPosition(0, mc.thePlayer)[9] == 1.0 && MoveUtil.hasHorizontalMotion();
      return !flag;
   }

   private int countVoxelStepsToTarget(BlockPlacementData blockPlacementData2, double d, double d2) {
      if (blockPlacementData2 != null && blockPlacementData2.getBlockPos() != null) {
         BlockPos blockPos = blockPlacementData2.getBlockPos();
         Vec3 hitVec = blockPlacementData2.getHitVec();
         double d3 = (double)blockPos.getY() + 0.5;
         double d4 = d - ((double)blockPos.getX() + hitVec.xCoord);
         double d5 = 0.0;
         double d6 = d2 - ((double)blockPos.getZ() + hitVec.zCoord);
         float f = (float)(MathHelper.atan2(d6, d4) * 180.0 / Math.PI - 90.0);
         float f2 = (float)(-(MathHelper.atan2(0.0, (double)MathHelper.sqrt_double(d4 * d4 + d6 * d6)) * 180.0 / Math.PI));
         float f3 = RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().yaw, f, 180.0F);
         float f4 = RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().pitch, f2, 180.0F);
         Vec3 vectorForRotation = mc.thePlayer.getVectorForRotation(f4, f3);
         double xCoord = vectorForRotation.xCoord;
         double yCoord = vectorForRotation.yCoord;
         double zCoord = vectorForRotation.zCoord;
         int n = MathHelper.floor_double(d);
         int n2 = MathHelper.floor_double(d3);
         int n3 = MathHelper.floor_double(d2);
         int x2 = blockPos.getX();
         int y2 = blockPos.getY();
         int z = blockPos.getZ();
         int n4 = Integer.signum((int)Math.round(xCoord * 1000.0));
         int n5 = Integer.signum((int)Math.round(yCoord * 1000.0));
         int n6 = Integer.signum((int)Math.round(zCoord * 1000.0));
         double d7 = n4 == 0 ? Double.POSITIVE_INFINITY : Math.abs(1.0 / xCoord);
         double d8 = n5 == 0 ? Double.POSITIVE_INFINITY : Math.abs(1.0 / yCoord);
         double d9 = n6 == 0 ? Double.POSITIVE_INFINITY : Math.abs(1.0 / zCoord);
         double d10 = d - (double)n;
         double d11 = d3 - (double)n2;
         double d12 = d2 - (double)n3;
         double d13 = n4 > 0 ? (1.0 - d10) * d7 : d10 * d7;
         double d14 = n5 > 0 ? (1.0 - d11) * d8 : d11 * d8;
         double d15 = n6 > 0 ? (1.0 - d12) * d9 : d12 * d9;
         if (n4 == 0) {
            d13 = Double.POSITIVE_INFINITY;
         }

         if (n5 == 0) {
            d14 = Double.POSITIVE_INFINITY;
         }

         if (n6 == 0) {
            d15 = Double.POSITIVE_INFINITY;
         }

         int n7 = 0;
         double d16 = 4.5;

         for (double i = 0.0; i <= d16 && (n != x2 || n2 != y2 || n3 != z); n7++) {
            if (d13 < d14 && d13 < d15) {
               i = d13;
               if (d13 > d16) {
                  break;
               }

               n += n4;
               d13 += d7;
            } else if (d14 < d15) {
               i = d14;
               if (d14 > d16) {
                  break;
               }

               n2 += n5;
               d14 += d8;
            } else {
               i = d15;
               if (d15 > d16) {
                  break;
               }

               n3 += n6;
               d15 += d9;
            }
         }

         return n7;
      } else {
         return 0;
      }
   }

   public BlockPlacementData findBlockPlacementData() {
      if (Math.abs(mc.thePlayer.motionZ) < 1.0E-6 && Math.abs(mc.thePlayer.motionX) < 1.0E-6) {
         for (int i = 1; i < 4; i++) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - (double)i, mc.thePlayer.posZ);
            if (BlockUtil.isSolidBlock(pos)) {
               return new BlockPlacementData(pos, EnumFacing.UP, new Vec3((double)pos.getX() + 0.5, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5));
            }
         }
      }

      float f;
      exitBlock: {
         exitBlock2: {
            if (this.isTellyBridgeEnabled()) {
               if (!this.shouldRotateBackwardInTelly()) {
                  break exitBlock2;
               }
            } else if (!this.flickMode.getSelectedValue().equals("None")) {
               if (this.targetBlockPos == null) {
                  break exitBlock2;
               }

               if (!this.isAtBlockEdge()) {
                  break exitBlock2;
               }
            }

            f = 0.0F;
            break exitBlock;
         }

         f = -180.0F;
      }

      float f2 = f;
      Vec3 vectorForRotation = mc.thePlayer.getVectorForRotation(Augustus.getInstance().getYawPitchHelper().realPitch, Augustus.getInstance().getYawPitchHelper().realYaw - 180.0F + f2);
      double d = (mc.thePlayer.lastTickPosX - mc.thePlayer.posX == 0.0 ? vectorForRotation.xCoord : mc.thePlayer.lastTickPosX - mc.thePlayer.posX) * 2.0;
      double d2 = (mc.thePlayer.lastTickPosZ - mc.thePlayer.posZ == 0.0 ? vectorForRotation.zCoord : mc.thePlayer.lastTickPosZ - mc.thePlayer.posZ) * 2.0;
      ArrayList list = new ArrayList();
      ArrayList list2 = new ArrayList();
      float f3 = this.sameY.getValue() && !this.rightClickHeldOnGround ? (float)(mc.thePlayer.posY - mc.thePlayer.lastGroundY) : 0.0F;
      float f4 = this.sameY.getValue() && !this.rightClickHeldOnGround ? 4.5F : 1.0F;
      double[] doubles = new double[]{1.5, 0.5, 2.5, 3.5};

      for (double d3 : doubles) {
         double d4 = mc.thePlayer.posX + d;
         double d5 = mc.thePlayer.posY - (d3 + (double)f3);
         double d6 = mc.thePlayer.posZ + d2;
         double d7 = d4 - mc.thePlayer.posX;
         double d8 = 0.0;
         double d9 = d6 - mc.thePlayer.posZ;
         float f5 = (float)(MathHelper.atan2(d9, d7) * 180.0 / Math.PI - 90.0);
         float f6 = (float)(-(MathHelper.atan2(d8, (double)MathHelper.sqrt_double(d7 * d7 + d9 * d9)) * 180.0 / Math.PI));
         float f7 = RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().yaw, f5, 180.0F);
         float f8 = RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().pitch, f6, 180.0F);

         for (float j = -45.0F; j <= 45.0F; j++) {
            Vec3 vectorForRotation2 = mc.thePlayer.getVectorForRotation(f8, f7 + j);
            MovingObjectPosition hit = mc.theWorld
               .rayTraceBlocks(
                  d4,
                  d5,
                  d6,
                  d4 + vectorForRotation2.xCoord * (double)f4,
                  d5 + vectorForRotation2.yCoord * (double)f4,
                  d6 + vectorForRotation2.zCoord * (double)f4,
                  false,
                  false,
                  true
               );
            if (hit != null
               && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
               && hit.getBlockPos() != null
               && BlockUtil.isSolidBlock(hit.getBlockPos())
               && !list2.contains(hit.getBlockPos())
               && (!BlockUtil.isWalkableBlock(hit.getBlockPos().up()) || this.sameYBlockJumpActive)) {
               BlockPos blockPos = hit.getBlockPos();
               BlockPos blockPos2 = hit.getBlockPos();
               EnumFacing sideHit = hit.sideHit;
               Vec3 hitVec = hit.hitVec;
               double d10 = mc.thePlayer.getDistance((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5)
                  + (double)(d3 == 1.5 && this.sameYBlock.getValue() ? -10 : 0)
                  + (double)j / 20.0
                  - (hit.getBlockPos().equals(this.lastPlacedBlockPos) ? 1.5 : 0.0);
               Vec3 vec = hitVec;
               EnumFacing facing = sideHit;
               BlockPos pos2 = blockPos2;
               list.add(new BlockPlacementData(pos2, facing, vec, d10));
               list2.add(hit.getBlockPos());
            }
         }
      }

      list.sort(Comparator.comparingDouble(BlockPlacementData::getScore));
      if (!list.isEmpty()) {
         return (BlockPlacementData)list.get(0);
      } else {
         for (double d11 : doubles) {
            double d12 = mc.thePlayer.posX + d;
            double d13 = mc.thePlayer.posY - (d11 + (double)f3);
            double d14 = mc.thePlayer.posZ + d2;
            double d15 = d12 - mc.thePlayer.posX;
            double d16 = 0.0;
            double d17 = d14 - mc.thePlayer.posZ;
            float f9 = (float)(MathHelper.atan2(d17, d15) * 180.0 / Math.PI - 90.0);
            float f10 = (float)(-(MathHelper.atan2(d16, (double)MathHelper.sqrt_double(d15 * d15 + d17 * d17)) * 180.0 / Math.PI));
            float f11 = RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().yaw, f9, 180.0F);
            float f12 = RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().pitch, f10, 180.0F);

            for (float k = 0.0F; k <= 360.0F; k++) {
               Vec3 vectorForRotation3 = mc.thePlayer.getVectorForRotation(f12, f11 + k);
               MovingObjectPosition hit2 = mc.theWorld
                  .rayTraceBlocks(d12, d13, d14, d12 + vectorForRotation3.xCoord * 4.5, d13 + vectorForRotation3.yCoord * 4.5, d14 + vectorForRotation3.zCoord * 4.5, false, false, true);
               if (hit2 != null
                  && hit2.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
                  && hit2.getBlockPos() != null
                  && BlockUtil.isSolidBlock(hit2.getBlockPos())
                  && !list2.contains(hit2.getBlockPos())
                  && (!BlockUtil.isWalkableBlock(hit2.getBlockPos().up()) || this.sameYBlockJumpActive)) {
                  BlockPos blockPos3 = hit2.getBlockPos();
                  BlockPos blockPos4 = hit2.getBlockPos();
                  EnumFacing sideHit2 = hit2.sideHit;
                  Vec3 hitVec2 = hit2.hitVec;
                  double d18 = mc.thePlayer.getDistance((double)blockPos3.getX() + 0.5, (double)blockPos3.getY() + 0.5, (double)blockPos3.getZ() + 0.5)
                     + (double)(d11 == 1.5 && this.sameYBlock.getValue() ? -10 : 0);
                  Vec3 vec2 = hitVec2;
                  EnumFacing facing2 = sideHit2;
                  BlockPos pos3 = blockPos4;
                  list.add(new BlockPlacementData(pos3, facing2, vec2, d18));
                  list2.add(hit2.getBlockPos());
               }
            }
         }

         list.sort(Comparator.comparingDouble(BlockPlacementData::getScore));
         if (!list.isEmpty()) {
            return (BlockPlacementData)list.get(0);
         } else {
            for (int m = 1; m < 4; m++) {
               BlockPos pos4 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - (double)m, mc.thePlayer.posZ);
               if (BlockUtil.isSolidBlock(pos4)) {
                  return new BlockPlacementData(pos4, EnumFacing.UP, new Vec3((double)pos4.getX() + 0.5, (double)(pos4.getY() + 1), (double)pos4.getZ() + 0.5));
               }
            }

            System.out.println("Null");
            return null;
         }
      }
   }

   private boolean canRunScaffoldInCurrentScreen() {
      boolean flag = mc.isNotInGui() || mc.currentScreen instanceof ClickGuiScreen;
      return !ClientManagers.moduleManager.guiMove.isToggled() ? flag : ClientManagers.moduleManager.guiMove.canMoveInGui() || flag;
   }

   private boolean isStationaryTowering() {
      return mc.gameSettings.keyBindJump.isKeyDown() && Math.abs(mc.thePlayer.motionZ) < 0.01 && Math.abs(mc.thePlayer.motionX) < 0.01;
   }

   @Override
   public void onRender(float f) {
      if (this.esp.getValue()) {
         RenderUtil.begin3DRender();
         byte b = 0;
         int colorInt = this.espColor.getColorInt(b);
         float f2 = 0.0F;
         if (this.targetBlockPos != null) {
            GlStateManager.alphaFunc(516, 0.0F);
            boolean isBlurActive = this.esp2.isBlurActive();
            BlurShadowManager.enableOneShotPass(isBlurActive, this.esp2.isShadowActive());
            BlurShadowManager.submitRenderTask(false, shaderRenderPass -> {
               BlockPos targetBlockPos2 = this.targetBlockPos;
               BlurShadowSettings esp22 = this.esp2;
               int n = ColorUtil.resolvePassColor(shaderRenderPass, colorInt);
               int n2 = esp22.resolvePassColor(shaderRenderPass, n);
               float f3 = 1.0F;
               int n3 = n2;
               RenderUtil.drawBlockEspBox(targetBlockPos2, n3, f3, f2);
            });
         }

         RenderUtil.end3DRender();
      }
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      if (this.blockCount.getValue()) {
         this.renderBlockCountHud();
      }

      RenderUtil.resetGlColorState();
   }

   private void renderBlockCountHud() {
      if (this.hotbarBlockCount != 0 && this.getSelectedBlockSlot() >= 0) {
         ScaledResolution sr = new ScaledResolution(mc);
         GL11.glPushMatrix();
         GL11.glScaled(1.0 / (double)sr.getScaleFactor(), 1.0 / (double)sr.getScaleFactor(), 1.0);
         GL11.glScaled(this.scale.getValue(), this.scale.getValue(), 1.0);
         double d = (double)mc.displayWidth / this.scale.getValue();
         double d2 = (double)mc.displayHeight / this.scale.getValue();
         int n = MathHelper.ceiling_double_int(d);
         int n2 = MathHelper.ceiling_double_int(d2);
         String str = this.hotbarBlockCount + "";
         int stringWidth = mc.fontRendererObj.getStringWidth(str);
         int n3 = (20 + stringWidth) / 2;
         int n4 = n3 + 2 + (int)((double)(n - (n3 + 2) * 2) * (this.x.getValue() / 100.0));
         int n5 = 2 + (int)((double)(n2 - 19) * (this.y.getValue() / 100.0));
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         if (this.backGround.getValue()) {
            byte b = 0;
            int colorInt = this.backgroundColor.getColorInt(b);
            boolean isBlurActive = this.blockCount2.isBlurActive();
            BlurShadowManager.enableOneShotPass(isBlurActive, this.blockCount2.isShadowActive());
            BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderBlockCountBackground(n4, n3, n5, colorInt, shaderRenderPass));
         }

         RenderHelper.enableGUIStandardItemLighting();
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(this.getSelectedBlockSlot() + 36).getStack();
         int n6 = n4 - n3;
         boolean flag = false;
         int n7 = n6;
         RenderUtil.renderItemIntoGuiElevated(stack, n7, n5, flag);
         RenderHelper.disableStandardItemLighting();
         mc.fontRendererObj.drawString(str, n4 - n3 + 20, n5 + 4, -1);
         GL11.glPopMatrix();
      }
   }

   private int countHotbarBlocks() {
      int n = 0;

      for (int i = 0; i < 9; i++) {
         ItemStack stackInSlot = mc.thePlayer.inventory.getStackInSlot(i);
         if (stackInSlot != null && stackInSlot.getItem() instanceof ItemBlock && BlockUtil.isValidScaffoldBlock(stackInSlot)) {
            n += stackInSlot.stackSize;
         }
      }

      return n;
   }

   private int getSelectedBlockSlot() {
      return this.selectedBlockSlot;
   }

   private void updateSelectedBlockSlot() {
      int n = -1;
      int n2 = -1;

      for (int i = 36; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (stack != null
            && stack.getItem() instanceof ItemBlock
            && BlockUtil.isValidScaffoldBlock(stack)
            && (this.slotMinStackThresholds.containsKey(i - 36) ? stack.stackSize > (Integer)this.slotMinStackThresholds.get(i - 36) : stack.stackSize > this.stackCountToStartSwitchProcess.getLastIntValue())) {
            if (!this.useBiggestStackSilent.getValue()) {
               n2 = i - 36;
               break;
            }

            if (stack.stackSize > n) {
               n = stack.stackSize;
               n2 = i - 36;
            }
         }
      }

      if (this.silentMode.getSelectedValue().equals("Exploit")) {
         this.selectedBlockSlot = n2;
         this.firstSilentSwitchPending = false;
      } else {
         if (this.selectedBlockSlot == n2) {
            this.silentSwitchCounter.reset();
         } else if (n2 < 0) {
            this.selectedBlockSlot = n2;
         } else {
            TickCounter silentSwitchCounter2 = this.silentSwitchCounter;
            int n3 = (this.firstSilentSwitchPending ? this.silentStartDelay.getLastIntValue() : this.silentSwitchDelay.getLastIntValue()) + 1;
            if (silentSwitchCounter2.hasReached2(n3)) {
               this.slotMinStackThresholds.put(n2, this.stackCountToStartSwitchProcess.getLastIntValue());
               this.selectedBlockSlot = n2;
               this.firstSilentSwitchPending = false;
               this.silentSwitchDelay.nextValue();
               this.stackCountToStartSwitchProcess.nextValue();
            }
         }
      }
   }

   private int findNonBlockHotbarSlot() {

      for (int i = 36; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (stack == null || !(stack.getItem() instanceof ItemBlock) || !BlockUtil.isValidScaffoldBlock(stack)) {
            return i - 36;
         }
      }

      return -1;
   }

   @Override
   public void onSilentSlot() {
      this.silentSwitchCounter.increment();
      if (!this.silentMode.getSelectedValue().equals("None")) {
         this.updateSelectedBlockSlot();
         int n;
         if (this.silentMode.getSelectedValue().equals("Exploit")) {
            n = this.findNonBlockHotbarSlot();
         } else {
            n = this.getSelectedBlockSlot();
         }

         int n2 = n;
         if (n2 >= 0 && n2 < 9) {
            int n3 = 1;
            if (this.silentMode.getSelectedValue().equals("Default")) {
               n3 = this.backSwitchTickDelay.getLastIntValue() + 1;
               if (this.dynamicBackSwitchTickDelay.getValue() && CombatTargetUtil.getActiveTargetOrDefault((EntityLivingBase)null) != null) {
                  n3 = Math.max(1, n3 / 2);
               }
            }

            boolean flag = !this.silentMode.getSelectedValue().equals("Legit");
            byte b = -1;
            SilentSlotManager.scheduleSlotSwitch(this, b, n2, n3, flag);
         }
      } else {
         this.selectedBlockSlot = 0;
      }
   }

   @Override
   public void onAIMoveSpeed(EventAIMoveSpeed event) {
      if (!this.waitingForStartEdge) {
         if (this.ignoreSpeedMotion.getValue()) {
            float f = MathHelper.clamp_float(event.getMoveSpeed(), -0.13000001F, 0.13000001F);
            event.setMoveSpeed(f);
         }
      }
   }

   @Override
   public void onSafeWalk2(EventSafeWalk event) {
      this.safeWalkEdgeDetected = event.isClippedAtEdge();
   }

   private boolean isIntaveFastSneakActive() {
      float f = MathHelper.wrapAngleTo180_float(ClientManagers.rotationController.getPreviousRotation().yaw);
      return this.intave.getValue()
         && this.intaveFast.getValue()
         && MoveUtil.isMoving()
         && !this.isStationaryTowering()
         && (f > -2.0F && f < 2.0F || f > 88.0F && f < 92.0F || f < -178.0F || f > 178.0F || f < -88.0F && f > -92.0F)
         && mc.thePlayer.onGround
         && !mc.gameSettings.keyBindJump.isKeyDown();
   }

   private boolean wouldFallNextTick() {
      if (mc.thePlayer.onGround && this.forcePlaceIfYouWouldFallDown.getValue()) {
         double[] doubles = PlayerUtil.predictPlayerPosition(0, mc.thePlayer);
         return doubles[9] != 1.0;
      } else {
         return false;
      }
   }

   public boolean isOutsideTargetBlockBounds() {
      if (this.targetBlockPos == null) {
         return false;
      } else {
         double d = 1.282;
         double d2 = 0.282;
         double posX = mc.thePlayer.posX;
         double posZ = mc.thePlayer.posZ;
         double d3 = (double)this.targetBlockPos.getX() + d;
         double d4 = (double)this.targetBlockPos.getX() - d2;
         double d5 = (double)this.targetBlockPos.getZ() + d;
         double d6 = (double)this.targetBlockPos.getZ() - d2;
         return posX > d3 || posX < d4 || posZ > d5 || posZ < d6;
      }
   }

   private boolean isStrafeActive() {
      return this.strafe.getValue() && (mc.gameSettings.keyBindJump.isKeyDown() || !this.strafeOnlyWhileJump.getValue());
   }

   private boolean isBezierRotationModeActive() {
      return this.isBlockClutching() && this.customBlockClutchRotationSpeeds.getValue() ? this.blockClutchModeNew.getValue() : this.modeNew.getValue();
   }

   private double getActiveAimSpeed() {
      return this.isBlockClutching() && this.customBlockClutchRotationSpeeds.getValue() ? this.blockClutchAimSpeed.getValue() : this.aimSpeed.getValue();
   }

   private float rollActiveYawSpeed() {
      return (float)(this.isBlockClutching() && this.customBlockClutchRotationSpeeds.getValue() ? this.blockClutchYawSpeed.nextValue() : this.yawSpeed.nextValue());
   }

   private float rollActivePitchSpeed() {
      return (float)(this.isBlockClutching() && this.customBlockClutchRotationSpeeds.getValue() ? this.blockClutchPitchSpeed.nextValue() : this.pitchSpeed.nextValue());
   }

   private RandomizedRangeSettings getActiveYawAcceleration() {
      return this.isBlockClutching() && this.customBlockClutchRotationSpeeds.getValue() ? this.blockClutchYawAcceleration : this.yawAcceleration;
   }

   private RandomizedRangeSettings getActivePitchAcceleration() {
      return this.isBlockClutching() && this.customBlockClutchRotationSpeeds.getValue() ? this.blockClutchPitchAcceleration : this.pitchAcceleration;
   }

   private boolean isExpandEnabled() {
      return this.expand.getValue();
   }

   private boolean isEarlySneakCalculationActive() {
      return mc.thePlayer != null && this.earlySneakCalculation.getValue() && (mc.thePlayer.isPotionActive(Potion.moveSpeed) || !this.earlySneakCalculationOnlyAtSpeedEffect.getValue());
   }

   private void renderBlockCountBackground(int n, int n2, int n3, int n4, ShaderRenderPass shaderRenderPass) {
      float f = (float)(n - n2 - 2);
      float f2 = (float)(n3 - 2);
      float f3 = (float)(n + n2 + 2);
      float f4 = (float)(n3 + 15 + 2);
      float value = (float)this.roundFactor.getValue();
      BlurShadowSettings blockCount22 = this.blockCount2;
      int n5 = ColorUtil.resolvePassColor(shaderRenderPass, n4);
      RenderUtil.drawRoundedRect(f, f2, f3, f4, value, 1, blockCount22.resolvePassColor(shaderRenderPass, n5));
   }

   private double scoreMidPitchCandidate(float f, ScaffoldRaycastResult scaffoldRaycastResult) {
      return this.getHitCenterOffset(scaffoldRaycastResult, f);
   }

   private double scoreFurthestPitchCandidate(ScaffoldRaycastResult scaffoldRaycastResult) {
      return 180.0 - this.getPitchDeltaScore(scaffoldRaycastResult);
   }

   private boolean placeSideBlock(boolean flag) {
      boolean flag2 = true;
      if (this.intaveBlockPlacePenalty == 0.0 && flag) {
         int n = -1;
         if (this.silentMode.getSelectedValue().equals("Exploit")) {
            int n2 = this.getSelectedBlockSlot() + 36;
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(n2).getStack();
            if (!PlayerUtil.canPlaceHeldItemAtMouseOver(stack)) {
               return false;
            }

            n = this.switchToExploitSlot();
         }

         ItemStack currentItem = mc.thePlayer.inventory.getCurrentItem();
         int n3 = currentItem != null ? currentItem.stackSize : 0;
         if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, currentItem, mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit, mc.objectMouseOver.hitVec)) {
            this.hasPlacedFirstBlock = true;
            this.placedBlocksSinceSneak++;
            this.godBridgePlacedBlocks++;
            this.blocksPlacedSinceJump++;
            this.ticksSinceGodBridgePlace = 0;
            this.lastRightClickTimer.reset();
            this.lastPlaceTimer.reset();
            flag2 = false;
            RandomNumberGenerator instance = DefaultRandomGenerator.instance;
            double minValue = this.moonWalkStartDistToBlock.getMinValue();
            double maxValue = this.moonWalkStartDistToBlock.getMaxValue();
            double d = maxValue;
            double d2 = minValue;
            this.moonWalkEdgeExpandDist = -instance.nextDouble(d2, d);
            this.ticksSinceLastPlace = 0;
            this.postPlacementStrafeTicks.nextValue();
            this.placedBlockSinceEnable = true;
            this.lastPlacedBlockPos = mc.objectMouseOver.getBlockPos().offset(mc.objectMouseOver.sideHit);
            this.swingHandIfAllowed();
         }

         if (currentItem == null) {
            this.restoreExploitSlot(n);
            return false;
         } else {
            if (currentItem.stackSize == 0) {
               mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem] = null;
            } else if ((currentItem.stackSize != n3 || mc.playerController.isInCreativeMode()) && !this.noSwing.getValue()) {
               mc.entityRenderer.itemRenderer.resetEquippedProgress();
            }

            if (flag2) {
               this.lastRightClickTimer.reset();
               if (currentItem != null && mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, currentItem)) {
                  mc.entityRenderer.itemRenderer.resetEquippedProgress2();
               }
            }

            this.restoreExploitSlot(n);
            return !flag2;
         }
      } else {
         return false;
      }
   }

   private boolean placeTopBlock(boolean flag) {
      boolean flag2 = true;
      if (this.intaveBlockPlacePenalty == 0.0 && flag) {
         int n = -1;
         if (this.silentMode.getSelectedValue().equals("Exploit")) {
            int n2 = this.getSelectedBlockSlot() + 36;
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(n2).getStack();
            if (!PlayerUtil.canPlaceHeldItemAtMouseOver(stack)) {
               return false;
            }

            n = this.switchToExploitSlot();
         }

         ItemStack currentItem = mc.thePlayer.inventory.getCurrentItem();
         int n3 = currentItem != null ? currentItem.stackSize : 0;
         if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, currentItem, mc.objectMouseOver.getBlockPos(), mc.objectMouseOver.sideHit, mc.objectMouseOver.hitVec)) {
            if (this.sameYBlockJumpActive) {
               this.sameYBlockTopPlaced = true;
            }

            this.hasPlacedFirstBlock = true;
            this.placedBlocksSinceSneak++;
            this.blocksPlacedSinceJump++;
            this.lastRightClickTimer.reset();
            this.lastPlaceTimer.reset();
            flag2 = false;
            RandomNumberGenerator instance = DefaultRandomGenerator.instance;
            double minValue = this.moonWalkStartDistToBlock.getMinValue();
            double maxValue = this.moonWalkStartDistToBlock.getMaxValue();
            double d = maxValue;
            double d2 = minValue;
            this.moonWalkEdgeExpandDist = -instance.nextDouble(d2, d);
            this.ticksSinceLastPlace = 0;
            this.postPlacementStrafeTicks.nextValue();
            this.placedBlockSinceEnable = true;
            this.lastPlacedBlockPos = mc.objectMouseOver.getBlockPos().offset(mc.objectMouseOver.sideHit);
            this.swingHandIfAllowed();
         }

         if (currentItem == null) {
            this.restoreExploitSlot(n);
            return false;
         } else {
            if (currentItem.stackSize == 0) {
               mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem] = null;
            } else if ((currentItem.stackSize != n3 || mc.playerController.isInCreativeMode()) && !this.noSwing.getValue()) {
               mc.entityRenderer.itemRenderer.resetEquippedProgress();
            }

            if (flag2) {
               this.lastRightClickTimer.reset();
               if (currentItem != null && mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, currentItem)) {
                  mc.entityRenderer.itemRenderer.resetEquippedProgress2();
               }
            }

            this.restoreExploitSlot(n);
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean isBlockCountShadowVisible() {
      return this.blockCount.getValue() && this.backGround.getValue();
   }

   private boolean isRoundFactorVisible() {
      return this.blockCount.getValue() && this.backGround.getValue();
   }

   private boolean isDynamicBackSwitchTickDelayVisible() {
      return this.silentMode.getSelectedValue().equals("Default") && this.backSwitchTickDelay.getMinValue() > 0.0;
   }

   private boolean isBackSwitchTickDelayVisible() {
      return this.silentMode.getSelectedValue().equals("Default");
   }

   private boolean isStackCountToStartSwitchVisible() {
      return this.silentMode.getSelectedValue().equals("Default") || this.silentMode.getSelectedValue().equals("Legit");
   }

   private boolean isSilentSwitchDelayVisible() {
      return this.silentMode.getSelectedValue().equals("Default") || this.silentMode.getSelectedValue().equals("Legit");
   }

   private boolean isSilentStartDelayVisible() {
      return this.silentMode.getSelectedValue().equals("Default") || this.silentMode.getSelectedValue().equals("Legit");
   }

   private boolean isUseBiggestStackSilentVisible() {
      return !this.silentMode.getSelectedValue().equals("None");
   }

   private boolean isConstantMotionVisible() {
      return this.towerMode.getSelectedValue().equals("ConstantMotion");
   }

   private boolean isTowerWhileMovingVisible() {
      return !this.towerMode.getSelectedValue().equals("Legit");
   }

   private boolean isTimerSpeedVisible() {
      return this.timerDrop.isVisible() && this.timerDrop.getValue();
   }

   private boolean isTellyBridgeVisible() {
      return this.sameY.getValue() && this.autoJump.getValue();
   }

   private boolean isIntaveFast2Visible() {
      return this.sameY.getValue() && this.intave.getValue();
   }

   private boolean isDirectionChangeDelayVisible() {
      return this.moonWalk.getValue() && this.moonWalk.isVisible();
   }

   private boolean isPostPlacementStrafeTicksVisible() {
      return this.moonWalk.getValue() && this.moonWalk.isVisible();
   }

   private boolean isDelayToRecalculateMiddleVisible() {
      return this.moonWalk.getValue() && this.moonWalk.isVisible();
   }

   private boolean isMoonWalkMiddleVisible() {
      return this.moonWalk.getValue() && this.moonWalk.isVisible();
   }

   private boolean isMoveFixIntaveVisible() {
      return !this.moveFix.getSelectedValue().equalsIgnoreCase("None");
   }

   private boolean isEarlySneakSpeedOnlyVisible() {
      return this.sneak.getValue() && this.earlySneakCalculation.getValue();
   }

   private boolean isMaxOffGroundSneakDiagonalVisible() {
      return this.sneak.getValue() && this.oneStackDiagonal.getValue();
   }

   private boolean isMinOffGroundSneakDiagonalVisible() {
      return this.sneak.getValue() && this.oneStackDiagonal.getValue();
   }

   private boolean isMaxOffGroundSneakForwardVisible() {
      return this.sneak.getValue() && this.oneStackForward.getValue();
   }

   private boolean isMinOffGroundSneakForwardVisible() {
      return this.sneak.getValue() && this.oneStackForward.getValue();
   }

   private boolean isMaxPitchCorrectionClutchVisible() {
      return !this.isExpandEnabled();
   }

   private boolean isMaxPitchCorrectionVisible() {
      return !this.isExpandEnabled();
   }

   private boolean isFlickForwardStrafeVisible() {
      return this.flickMode.getSelectedValue().equals("Simple");
   }

   private boolean isIgnoreStartAtEdgeClutchVisible() {
      if (this.startAtEdge.getValue()) {
         if (this.startAtEdgeDelay.getMaxValue() > 0.0) {
            return true;
         }
      }

      return false;
   }

   private boolean isStrafeKeyBindRecalcDelayVisible() {
      return this.correctStrafeWithKeyBinds.getValue() && this.correctStrafeWithKeyBinds.isVisible();
   }

   private boolean isStrafeKeyBindMiddleExclusionVisible() {
      return this.correctStrafeWithKeyBinds.getValue() && this.correctStrafeWithKeyBinds.isVisible();
   }

   private boolean isStrafeKeyBindMiddleVisible() {
      return this.correctStrafeWithKeyBinds.getValue() && this.correctStrafeWithKeyBinds.isVisible();
   }

   private boolean isStrafeKeyBindWidthVisible() {
      return this.correctStrafeWithKeyBinds.getValue() && this.correctStrafeWithKeyBinds.isVisible();
   }

   private boolean isStrafeKeyBindCageVisible() {
      return this.correctStrafeWithKeyBinds.getValue() && this.correctStrafeWithKeyBinds.isVisible();
   }

   private boolean isStrafeWidthVisible() {
      return this.strafe.getValue() && this.strafeDegree.getMinValue() <= 25.0;
   }

   private boolean isStrafeOnlyWhileJumpVisible() {
      return this.strafe.getValue();
   }

   private boolean isImproveOnlyAtClutchVisible() {
      return this.improve.getValue();
   }

   private boolean isIgnoreDirChangeDelayClutchVisible() {
      if (this.strictYaw.getValue()) {
         if (this.startDelayAfterDirectionChange.getMaxValue() > 0.0) {
            return true;
         }
      }

      return false;
   }

   private boolean isRandomizeYawIntervalVisible() {
      return !this.otherYawRandomization.getValue() && this.randomizeYaw.getValue() && this.randomizeYaw.isVisible();
   }

   private boolean isRandomizeYawValVisible() {
      return !this.otherYawRandomization.getValue() && this.randomizeYaw.getValue() && this.randomizeYaw.isVisible();
   }

   private boolean isOtherYawRandomizationVisible() {
      return this.randomizeYaw.getValue() && this.randomizeYaw.isVisible();
   }

   private boolean isRandomizeYawChanceOnBlockVisible() {
      return this.randomizeYaw.getValue() && this.randomizeYaw.isVisible();
   }

   private boolean isRandomizeYawChanceWhilePitchVisible() {
      return this.randomizeYaw.getValue() && this.randomizeYaw.isVisible();
   }

   private boolean isRandomizePitchIntervalVisible() {
      return !this.otherPitchRandomization.getValue() && this.randomizePitch.getValue() && this.randomizePitch.isVisible();
   }

   private boolean isRandomizePitchChanceInAirVisible() {
      return this.randomizePitch.getValue() && this.randomizePitch.isVisible();
   }

   private boolean isRandomizePitchChanceOnBlockVisible() {
      return this.randomizePitch.getValue() && this.randomizePitch.isVisible();
   }

   private boolean isRandomizePitchChanceWhileYawVisible() {
      return this.randomizePitch.getValue() && this.randomizePitch.isVisible();
   }

   private boolean isBlockClutchPitchSpeedVisible() {
      return !this.blockClutchModeNew.isVisible() || !this.blockClutchModeNew.getValue();
   }

   private boolean isBlockClutchAimSpeedVisible() {
      return this.blockClutchModeNew.getValue() && this.blockClutchModeNew.isVisible();
   }

   private boolean isBlockClutchModeNewVisible() {
      return !this.expand.getValue();
   }

   private boolean isPitchAccelerationVisible() {
      return !this.modeNew.isVisible() || !this.modeNew.getValue();
   }

   private boolean isPitchSpeedVisible() {
      return !this.modeNew.isVisible() || !this.modeNew.getValue();
   }

   private boolean isAimSpeedVisible() {
      return this.modeNew.getValue() && this.modeNew.isVisible();
   }

   private boolean isModeNewVisible() {
      return !this.expand.getValue();
   }

   private boolean isLicenseValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
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
