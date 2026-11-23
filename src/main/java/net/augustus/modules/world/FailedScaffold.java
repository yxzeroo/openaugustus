package net.augustus.modules.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.utils.ActionRateAccumulator;
import net.augustus.modules.world.scaffold.ScaffoldSimStep;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.modules.world.scaffold.BlockPlacementData;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.events.EventClick;
import net.augustus.settings.RangeSetting;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.rotation.RayCastUtil;
import net.augustus.events.EventSafeWalk;
import net.augustus.events.listeners.SafeWalkListener;
import net.augustus.utils.rotation.RotationController;
import net.augustus.utils.world.BlockUtil;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.modules.world.scaffold.FailedScaffoldFacingSwitchMap;
import net.augustus.events.EventMoveInput;
import net.augustus.events.MoveInputStage;
import net.augustus.Augustus;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.events.listeners.TickListener;
import net.augustus.modules.world.scaffold.ScaffoldRaycastResult;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class FailedScaffold extends Module implements TickListener, MoveInputListener, ClickListener, SafeWalkListener {
   public final DoubleValue rotations;
   public final RangeSetting yawSpeed;
   public final RangeSetting pitchSpeed;
   public final BooleanValue strafe;
   public final StringValue predictTicks;
   public final DoubleValue movement;
   public final DoubleValue sneaking;
   public final BooleanValue sneak;
   public final BooleanValue removeSneakStop;
   public final BooleanValue oneStackForward;
   public final RangeSetting minOffGroundSneakTickForward;
   public final RangeSetting maxOffGroundSneakTickForward;
   public final BooleanValue oneStackDiagonal;
   public final RangeSetting minOffGroundSneakTickDiagonal;
   public final RangeSetting maxOffGroundSneakTickDiagonal;
   public final RangeSetting sneakTicks;
   public final RangeSetting startSneakDistToBlock;
   public final RangeSetting sneakBlocksForward;
   public final RangeSetting sneakBlocksDiagonal;
   public final StringValue sneakSlowDown;
   public final BooleanValue sameY;
   public final BooleanValue autoJump;
   public final BooleanValue tellyBridge;
   private final RotationUtil rotationUtil;
   private final TimeHelper timeHelper;
   private int predictTicksCount;
   private BlockPos targetBlockPos;
   private EventMoveInput lastMoveInput;
   private ArrayList plannedSimSteps;
   private float strafeYawOffset;
   private int offGroundTicks;
   private int sneakTickCounter;
   private int remainingPlannedTicks;
   private boolean executingPlan;
   private float lastForwardInput;
   private final ActionRateAccumulator cpsAccumulator;
   private final ActionRateAccumulator dcpsAccumulator;

   public FailedScaffold() {
       super("FailedScaffold", "NeuGerüst", new Color(182, 194, 207), Categorys.WORLD);
      this.rotations = new DoubleValue(this, "Rotations");
      this.yawSpeed = new RangeSetting(this, "YawSpeed", 30.0, 50.0, 0.0, 180.0, 0, this.rotations);
      this.pitchSpeed = new RangeSetting(this, "PitchSpeed", 30.0, 50.0, 0.0, 180.0, 0, this.rotations);
      this.strafe = new BooleanValue(this, "Strafe", false, this.rotations);
      this.predictTicks = new StringValue(this, "PredictTicks", 0.0, 0.0, 2.0, 0, this.rotations);
      this.movement = new DoubleValue(this, "Movement");
      this.sneaking = new DoubleValue(this, "Sneaking", this.movement);
      this.sneak = new BooleanValue(this, "Sneak", false, this.sneaking);
      BooleanValue booleanValue = new BooleanValue(this, "RemoveSneakStop", false, this.sneaking);
      BooleanValue sneak2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue2 = sneak2;
      SettingVisibilityCondition condition = () -> booleanValue2.getValue();
      this.removeSneakStop = (BooleanValue)booleanValue.withVisibility(condition);
      BooleanValue booleanValue3 = new BooleanValue(this, "OneStackForward", false, this.sneaking);
      sneak2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue4 = sneak2;
      SettingVisibilityCondition condition2 = () -> booleanValue4.getValue();
      this.oneStackForward = (BooleanValue)booleanValue3.withVisibility(condition2);
      RangeSetting rangeSetting = new RangeSetting(this, "MinOffGroundSneakTickForward", 1.0, 2.0, 0.0, 12.0, 0, this.sneaking);
      SettingVisibilityCondition condition3 = () -> this.isMinOffGroundSneakForwardVisible();
      this.minOffGroundSneakTickForward = rangeSetting.visibleWhen(condition3);
      RangeSetting rangeSetting2 = new RangeSetting(this, "MaxOffGroundSneakTickForward", 10.0, 11.0, 0.0, 12.0, 0, this.sneaking);
      SettingVisibilityCondition condition4 = () -> this.isMaxOffGroundSneakForwardVisible();
      this.maxOffGroundSneakTickForward = rangeSetting2.visibleWhen(condition4);
      BooleanValue booleanValue5 = new BooleanValue(this, "OneStackDiagonal", false, this.sneaking);
      sneak2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue6 = sneak2;
      SettingVisibilityCondition condition5 = () -> booleanValue6.getValue();
      this.oneStackDiagonal = (BooleanValue)booleanValue5.withVisibility(condition5);
      RangeSetting rangeSetting3 = new RangeSetting(this, "MinOffGroundSneakTickDiagonal", 1.0, 2.0, 0.0, 12.0, 0, this.sneaking);
      SettingVisibilityCondition condition6 = () -> {
         return this.sneak.getValue() && this.oneStackDiagonal.getValue();
      };
      this.minOffGroundSneakTickDiagonal = rangeSetting3.visibleWhen(condition6);
      RangeSetting rangeSetting4 = new RangeSetting(this, "MaxOffGroundSneakTickDiagonal", 10.0, 11.0, 0.0, 12.0, 0, this.sneaking);
      SettingVisibilityCondition condition7 = () -> {
         return this.sneak.getValue() && this.oneStackDiagonal.getValue();
      };
      this.maxOffGroundSneakTickDiagonal = rangeSetting4.visibleWhen(condition7);
      RangeSetting rangeSetting5 = new RangeSetting(this, "SneakTicks", 1.0, 2.0, 0.0, 10.0, 0, this.sneaking);
      sneak2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue7 = sneak2;
      SettingVisibilityCondition condition8 = () -> booleanValue7.getValue();
      this.sneakTicks = rangeSetting5.visibleWhen(condition8);
      RangeSetting rangeSetting6 = new RangeSetting(this, "StartSneakDistToBlock", 0.05, 0.2, -0.2, 0.5, 2, this.sneaking);
      sneak2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue8 = sneak2;
      SettingVisibilityCondition condition9 = () -> booleanValue8.getValue();
      this.startSneakDistToBlock = rangeSetting6.visibleWhen(condition9);
      RangeSetting rangeSetting7 = new RangeSetting(this, "SneakBlocksForward", 1.0, 2.0, 0.0, 15.0, 0, this.sneaking);
      sneak2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue9 = sneak2;
      SettingVisibilityCondition condition10 = () -> booleanValue9.getValue();
      this.sneakBlocksForward = rangeSetting7.visibleWhen(condition10);
      RangeSetting rangeSetting8 = new RangeSetting(this, "SneakBlocksDiagonal", 1.0, 2.0, 0.0, 15.0, 0, this.sneaking);
      sneak2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue10 = sneak2;
      SettingVisibilityCondition condition11 = () -> booleanValue10.getValue();
      this.sneakBlocksDiagonal = rangeSetting8.visibleWhen(condition11);
      StringValue stringValue = new StringValue(this, "SneakSlowDown", 0.3, 0.3, 1.0, 2, this.sneaking);
      sneak2 = this.sneak;
      this.sneak.getClass();
      BooleanValue booleanValue11 = sneak2;
      SettingVisibilityCondition condition12 = () -> booleanValue11.getValue();
      this.sneakSlowDown = stringValue.visibleWhen(condition12);
      this.sameY = new BooleanValue(this, "SameY", false);
      BooleanValue booleanValue12 = new BooleanValue(this, "AutoJump", false);
      sneak2 = this.sameY;
      this.sameY.getClass();
      BooleanValue booleanValue13 = sneak2;
      SettingVisibilityCondition condition13 = () -> booleanValue13.getValue();
      this.autoJump = (BooleanValue)booleanValue12.withVisibility(condition13);
      BooleanValue booleanValue14 = new BooleanValue(this, "TellyBridge", false);
      sneak2 = this.sameY;
      this.sameY.getClass();
      BooleanValue booleanValue15 = sneak2;
      SettingVisibilityCondition condition14 = () -> booleanValue15.getValue();
      this.tellyBridge = (BooleanValue)booleanValue14.withVisibility(condition14);
      this.rotationUtil = new RotationUtil();
      this.timeHelper = new TimeHelper();
      this.predictTicksCount = 1;
      this.targetBlockPos = null;
      this.lastMoveInput = null;
      this.plannedSimSteps = new ArrayList();
      this.strafeYawOffset = 45.0F;
      this.offGroundTicks = 0;
      this.sneakTickCounter = 0;
      this.remainingPlannedTicks = 0;
      this.executingPlan = false;
      this.lastForwardInput = 0.0F;
      this.cpsAccumulator = new ActionRateAccumulator(20, 0.0);
      this.dcpsAccumulator = new ActionRateAccumulator(20, 0.0);
   }

   @Override
   public void onEnable() {
      this.timeHelper.reset();
      this.plannedSimSteps.clear();
      this.predictTicksCount = 0;
   }

   @Override
   public void onDisable() {
      this.lastMoveInput = null;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
      byte b2 = 21;
      this.toggleEventSubscription(b2);
      byte b3 = 7;
      this.toggleEventSubscription(b3);
      byte b4 = 33;
      this.toggleEventSubscription(b4);
   }

   @Override
   public void onTick() {
      if (!this.plannedSimSteps.isEmpty()) {
         this.plannedSimSteps.remove(0);
      }

      this.offGroundTicks = mc.thePlayer.onGround ? 0 : this.offGroundTicks + 1;
      if (this.lastMoveInput != null) {
         BlockPlacementData blockPlacementData = this.findTargetBlockData();
         this.targetBlockPos = blockPlacementData == null ? null : blockPlacementData.getBlockPos();
         float f = this.computeDefaultPitch();
         boolean flag = !this.executingPlan;
         System.out.println(mc.thePlayer.posX + " " + mc.thePlayer.posZ);
         if (flag) {
            this.predictTicksCount = (int)this.predictTicks.getValue();
            double[] doubles = PlayerUtil.capturePlayerState();
            ArrayList list = new ArrayList();
            ScaffoldSimStep scaffoldSimStep = this.createInitialSimStep();
            ScaffoldSimStep scaffoldSimStep2 = this.advanceSimStep(scaffoldSimStep);
            list.add(scaffoldSimStep2);
            double[] doubles2 = new double[]{mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ};

            for (int i = 0; i < this.predictTicksCount; i++) {
               EntityPlayerSP thePlayer = mc.thePlayer;
               double posX = mc.thePlayer.posX;
               double posY = mc.thePlayer.posY;
               double posZ = mc.thePlayer.posZ;
               EventMoveInput moveInput = scaffoldSimStep2.getMoveInput();
               float yaw = scaffoldSimStep2.getYaw();
               double d = posZ;
               double d2 = posY;
               double d3 = posX;
               doubles2 = PlayerUtil.predictPlayerPosition(0, thePlayer, d3, d2, d, yaw, moveInput);
               System.out.println("Last: " + doubles2[0] + " " + doubles2[2] + "  " + scaffoldSimStep2.getYaw() + " " + scaffoldSimStep2.getMoveInput().isSneak());
               PlayerUtil.restorePlayerState(doubles2);
               if (i < this.predictTicksCount - 1) {
                  ScaffoldSimStep scaffoldSimStep3 = (ScaffoldSimStep)list.get(list.size() - 1);
                  scaffoldSimStep2 = this.advanceSimStep(scaffoldSimStep3);
                  list.add(scaffoldSimStep2);
               }
            }

            PlayerUtil.restorePlayerState(doubles);
            float yaw2 = scaffoldSimStep2.getYaw();
            Vec3 vec = new Vec3(doubles2[0], doubles2[1], doubles2[2]);
            System.out.println(vec.xCoord + "           " + vec.zCoord);
            ArrayList list2 = new ArrayList();
            float f2 = 0.0F;

            for (float j = 15.0F; j < 85.0F; j += 0.05F) {
               float f3 = RotationUtil.applyPitchGcd(j, ClientManagers.rotationController.getPreviousRotation().pitch);
               if (f3 != f2) {
                  MovingObjectPosition hit = RayCastUtil.rayCastFromPosition(1.0F, vec, yaw2, f3);
                  if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
                     && BlockUtil.isSolidBlock(hit.getBlockPos())
                     && hit.sideHit != EnumFacing.DOWN
                     && hit.sideHit != EnumFacing.UP
                     && (double)hit.getBlockPos().getY() < mc.thePlayer.posY) {
                     list2.add(new ScaffoldRaycastResult(hit, f3));
                  }
               }

               f2 = f3;
            }

            if (!list2.isEmpty()) {
               ScaffoldSimStep scaffoldSimStep4 = (ScaffoldSimStep)list.get(0);
               float yaw3 = scaffoldSimStep4.getYaw();
               ScaffoldRaycastResult scaffoldRaycastResult = this.selectBestRaycastResult(list2, yaw3, vec);
               RotationController rotationController = ClientManagers.rotationController;
               float[] floats = new float[]{scaffoldSimStep4.getYaw(), scaffoldRaycastResult.pitch};
               byte b = -1;
               float[] floats2 = floats;
               rotationController.requestRotation2(floats2, b, this);
               this.plannedSimSteps = list;
               this.remainingPlannedTicks = this.predictTicksCount;
               this.executingPlan = true;
               this.applySimStepState(scaffoldSimStep2);
               return;
            }
         }

         this.executingPlan = this.remainingPlannedTicks > 0;
         this.remainingPlannedTicks--;
         if (this.plannedSimSteps.isEmpty()) {
            ScaffoldSimStep scaffoldSimStep5 = this.createInitialSimStep();
            ScaffoldSimStep scaffoldSimStep6 = this.advanceSimStep(scaffoldSimStep5);
            this.plannedSimSteps.add(scaffoldSimStep6);
            this.applySimStepState(scaffoldSimStep6);
            RotationController rotationController2 = ClientManagers.rotationController;
            Rotation rotation = new Rotation(scaffoldSimStep6.getYaw(), f);
            byte b2 = -1;
            Rotation rotation2 = rotation;
            rotationController2.requestRotation(rotation2, b2, this);
         } else {
            RotationController rotationController3 = ClientManagers.rotationController;
            Rotation rotation3 = new Rotation(((ScaffoldSimStep)this.plannedSimSteps.get(0)).getYaw(), f);
            byte b3 = -1;
            Rotation rotation4 = rotation3;
            rotationController3.requestRotation(rotation4, b3, this);
         }
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (!this.plannedSimSteps.isEmpty()) {
         EventMoveInput moveInput = ((ScaffoldSimStep)this.plannedSimSteps.get(0)).getMoveInput();
         event.copyFrom(moveInput);
      }

      this.lastMoveInput = new EventMoveInput(event);
   }

   @Override
   public void onClick(EventClick event) {
      ActionRateAccumulator cpsAccumulator2 = this.cpsAccumulator;
      byte b = 15;
      byte b2 = 11;
      int n = DefaultRandomGenerator.instance.nextInt2(b2, b);
      int n2 = cpsAccumulator2.accumulate(n);
      if (n2 > 0) {
         ActionRateAccumulator dcpsAccumulator2 = this.dcpsAccumulator;
         byte b3 = 8;
         byte b4 = 4;
         int n3 = DefaultRandomGenerator.instance.nextInt2(b4, b3);
         n2 += dcpsAccumulator2.accumulate(n3);
      }

      if (n2 < 1 && mc.objectMouseOver != null) {
         if (this.isLicenseValid()) {
            ItemStack currentEquippedItem = mc.thePlayer.getCurrentEquippedItem();
            BlockPos blockPos = mc.objectMouseOver.getBlockPos();
            Vec3 hitVec = mc.objectMouseOver.hitVec;
            EnumFacing sideHit = mc.objectMouseOver.sideHit;
            BlockPos pos = blockPos;
            if (PlayerUtil.canPlaceBlockAt(currentEquippedItem, pos, sideHit, hitVec)) {
               n2 = 1;
            }
         }
      }

      event.addRightClicks(n2);
      boolean flag = true;
      event.setIgnoreVanillaClicks(flag);
      boolean flag2 = false;
      event.setLeftMouseDown(flag2);
   }

   @Override
   public void onSafeWalk(EventSafeWalk event) {
      if (this.sneak.getValue() && this.removeSneakStop.getValue()) {
         boolean flag = true;
         event.setDisableSneakSafeWalk(flag);
      }
   }

   private ScaffoldSimStep createInitialSimStep() {
      float f = 0.0F;
      float f2 = 0.0F;
      if (mc.gameSettings.keyBindForward.isKeyDown()) {
         f++;
      }

      if (mc.gameSettings.keyBindBack.isKeyDown()) {
         f--;
      }

      if (mc.gameSettings.keyBindLeft.isKeyDown()) {
         f2++;
      }

      if (mc.gameSettings.keyBindRight.isKeyDown()) {
         f2--;
      }

      boolean isKeyDown = mc.gameSettings.keyBindSneak.isKeyDown();
      boolean isKeyDown2 = mc.gameSettings.keyBindJump.isKeyDown();
      float f3 = MathHelper.wrapAngleTo180_float(Augustus.getInstance().getYawPitchHelper().realYaw - ClientManagers.rotationController.getPreviousRotation().yaw);
      float f4 = 0.0F;
      float f5 = 0.0F;
      if ((double)f3 >= 22.5 && (double)f3 < 67.5) {
         f5 += f2;
         f4 += f;
         f5 -= f;
         f4 += f2;
      } else if ((double)f3 >= 67.5 && (double)f3 < 112.5) {
         f5 -= f;
         f4 += f2;
      } else if ((double)f3 >= 112.5 && (double)f3 < 157.5) {
         f5 -= f2;
         f4 -= f;
         f5 -= f;
         f4 += f2;
      } else if ((double)f3 >= 157.5 || (double)f3 <= -157.5) {
         f5 -= f2;
         f4 -= f;
      } else if ((double)f3 > -157.5 && (double)f3 <= -112.5) {
         f5 -= f2;
         f4 -= f;
         f5 += f;
         f4 -= f2;
      } else if ((double)f3 > -112.5 && (double)f3 <= -67.5) {
         f5 += f;
         f4 -= f2;
      } else if ((double)f3 > -67.5 && (double)f3 <= -22.5) {
         f5 += f2;
         f4 += f;
         f5 += f;
         f4 -= f2;
      } else {
         f5 += f2;
         f4 += f;
      }

      if (isKeyDown) {
         f = (float)MathHelper.clamp_double((double)f4, -0.3, 0.3);
         f2 = (float)MathHelper.clamp_double((double)f5, -0.3, 0.3);
      } else {
         f = (float)MathHelper.clamp_double((double)f4, -1.0, 1.0);
         f2 = (float)MathHelper.clamp_double((double)f5, -1.0, 1.0);
      }

      this.lastForwardInput = f;
      return new ScaffoldSimStep(
         !this.executingPlan ? new EventMoveInput(f2, f, isKeyDown2, isKeyDown, MoveInputStage.PRE) : new EventMoveInput(this.lastMoveInput),
         ClientManagers.rotationController.getPreviousRotation().yaw,
         this.strafeYawOffset,
         this.targetBlockPos,
         this.offGroundTicks
      );
   }

   private void applySimStepState(ScaffoldSimStep scaffoldSimStep) {
      this.strafeYawOffset = scaffoldSimStep.getStrafeYawOffset();
   }

   private ScaffoldSimStep advanceSimStep(ScaffoldSimStep scaffoldSimStep) {
      BlockPlacementData blockPlacementData = this.findTargetBlockData();
      BlockPos pos = blockPlacementData == null ? null : blockPlacementData.getBlockPos();
      float backwardYaw = this.getBackwardYaw();
      boolean flag = this.shouldStrafeYaw(backwardYaw, pos);
      float strafeYawOffset2 = scaffoldSimStep.getStrafeYawOffset();
      if (flag) {
         strafeYawOffset2 = this.computeStrafeYawOffset(backwardYaw, strafeYawOffset2, pos);
         backwardYaw += strafeYawOffset2;
      }

      RotationUtil rotationUtil2 = this.rotationUtil;
      RandomNumberGenerator instance = DefaultRandomGenerator.instance;
      double minValue = this.yawSpeed.getMinValue();
      double maxValue = this.yawSpeed.getMaxValue();
      double d = minValue;
      float f = instance.nextFloat2(d, maxValue);
      float yaw = scaffoldSimStep.getYaw();
      float f2 = f;
      float f3 = rotationUtil2.stepYawWithGcd(f2, yaw, backwardYaw);
      scaffoldSimStep = this.updateSimStepMovement(scaffoldSimStep);
      return new ScaffoldSimStep(scaffoldSimStep.getMoveInput(), f3, strafeYawOffset2, pos, scaffoldSimStep.getOffGroundTicks());
   }

   private ScaffoldSimStep updateSimStepMovement(ScaffoldSimStep scaffoldSimStep) {
      EventMoveInput moveInput = scaffoldSimStep.getMoveInput();
      if (this.sneak.getValue()) {
         double value = this.sneakSlowDown.getValue();
         moveInput.setSneakForwardMultiplier(value);
         double value2 = this.sneakSlowDown.getValue();
         moveInput.setSneakStrafeMultiplier(value2);
      }

      int n = mc.thePlayer.onGround ? 0 : scaffoldSimStep.getOffGroundTicks() + 1;
      scaffoldSimStep.setOffGroundTicks(n);
      scaffoldSimStep.setMoveInput(moveInput);
      return scaffoldSimStep;
   }

   private float computeDefaultPitch() {
      float pitch = ClientManagers.rotationController.getPreviousRotation().pitch;
      long l = 200L;
      if (!this.timeHelper.reached2(l)) {
         RotationUtil rotationUtil2 = this.rotationUtil;
         float f = 24.0F;
         float f2 = 18.0F;
         float f3 = DefaultRandomGenerator.instance.nextFloat(f2, f);
         float f4 = 80.34F;
         float pitch2 = ClientManagers.rotationController.getPreviousRotation().pitch;
         float f5 = f3;
         pitch = rotationUtil2.updatePitchWithIntaveSin(f5, pitch2, f4);
      }

      return RotationUtil.applyPitchGcd(pitch, ClientManagers.rotationController.getPreviousRotation().pitch);
   }

   private ScaffoldRaycastResult selectBestRaycastResult(List list, float f, Vec3 vec) {
      list.sort(Comparator.comparingDouble(obj -> this.scoreRaycastCandidate(f, vec, (ScaffoldRaycastResult)obj)));
      return (ScaffoldRaycastResult)list.get(0);
   }

   private double getPitchDeltaScore(ScaffoldRaycastResult scaffoldRaycastResult, long l) {
      return (double)Math.abs(scaffoldRaycastResult.pitch - ((Rotation)null).pitch);
   }

   private double getHitCenterOffset(ScaffoldRaycastResult scaffoldRaycastResult, float f, Vec3 vec) {
      MovingObjectPosition hit = RayCastUtil.rayCastFromPosition(1.0F, vec, f, scaffoldRaycastResult.pitch);
      if (hit != null && hit.getBlockPos() != null && hit.hitVec != null) {
         double d = hit.hitVec.yCoord % 1.0;
         return Math.abs(0.5 - d);
      } else {
         return 100.0;
      }
   }

   private float getBackwardYaw() {
      float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
      return realYaw - 180.0F;
   }

   private float getTellyYawOffset() {
      return !this.autoJump.getValue()
            || !this.sameY.getValue()
            || !this.tellyBridge.getValue()
            || mc.thePlayer.motionY < 0.0 && !mc.thePlayer.onGround && MoveUtil.hasHorizontalMotion()
         ? 0.0F
         : -180.0F;
   }

   private BlockPlacementData findTargetBlockData() {
      if (Math.abs(mc.thePlayer.motionZ) < 1.0E-6 && Math.abs(mc.thePlayer.motionX) < 1.0E-6) {
         for (int i = 1; i < 4; i++) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - (double)i, mc.thePlayer.posZ);
            if (BlockUtil.isSolidBlock(pos)) {
               return new BlockPlacementData(pos, EnumFacing.UP, new Vec3((double)pos.getX() + 0.5, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5));
            }
         }
      }

      float tellyYawOffset = this.getTellyYawOffset();
      Vec3 vectorForRotation = mc.thePlayer.getVectorForRotation(Augustus.getInstance().getYawPitchHelper().realPitch, Augustus.getInstance().getYawPitchHelper().realYaw - 180.0F + tellyYawOffset);
      double d = (mc.thePlayer.prevPosX - mc.thePlayer.posX == 0.0 ? vectorForRotation.xCoord : mc.thePlayer.prevPosX - mc.thePlayer.posX) * 2.0;
      double d2 = (mc.thePlayer.prevPosZ - mc.thePlayer.posZ == 0.0 ? vectorForRotation.zCoord : mc.thePlayer.prevPosZ - mc.thePlayer.posZ) * 2.0;
      ArrayList list = new ArrayList();
      ArrayList list2 = new ArrayList();
      float f = this.sameY.getValue() ? (float)(mc.thePlayer.posY - mc.thePlayer.lastGroundY) : 0.0F;
      float f2 = this.sameY.getValue() ? 4.5F : 1.0F;

      for (float j = 0.5F + f; j < 3.0F + f; j++) {
         double d3 = mc.thePlayer.posX + d;
         double d4 = mc.thePlayer.posY - (double)j;
         double d5 = mc.thePlayer.posZ + d2;
         double d6 = d3 - mc.thePlayer.posX;
         double d7 = d5 - mc.thePlayer.posZ;
         float f3 = (float)(MathHelper.atan2(d7, d6) * 180.0 / Math.PI - 90.0);
         float f4 = (float)(-(MathHelper.atan2(0.0, (double)MathHelper.sqrt_double(d6 * d6 + d7 * d7)) * 180.0 / Math.PI));
         float f5 = RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().yaw, f3, 180.0F);
         float f6 = RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().pitch, f4, 180.0F);
         Vec3 vectorForRotation2 = mc.thePlayer.getVectorForRotation(f6, f5);
         MovingObjectPosition hit = mc.theWorld
            .rayTraceBlocks(
               d3,
               d4,
               d5,
               d3 + vectorForRotation2.xCoord * (double)f2,
               d4 + vectorForRotation2.yCoord * (double)f2,
               d5 + vectorForRotation2.zCoord * (double)f2,
               false,
               false,
               true
            );
         if (hit != null
            && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
            && hit.getBlockPos() != null
            && BlockUtil.isSolidBlock(hit.getBlockPos())
            && !list2.contains(hit.getBlockPos())
            && !BlockUtil.isWalkableBlock(hit.getBlockPos().up())) {
            list.add(new BlockPlacementData(hit.getBlockPos(), hit.sideHit, hit.hitVec));
            list2.add(hit.getBlockPos());
         }
      }

      list.sort(
         Comparator.comparingDouble(
            obj -> mc.thePlayer.getDistance((double)((BlockPlacementData)obj).getBlockPos().getX() + 0.5, (double)((BlockPlacementData)obj).getBlockPos().getY() + 0.5, (double)((BlockPlacementData)obj).getBlockPos().getZ() + 0.5)
         )
      );
      if (!list.isEmpty()) {
         return (BlockPlacementData)list.get(0);
      } else {
         for (float k = 0.5F + f; k < 4.0F + f; k++) {
            double d8 = mc.thePlayer.posX + d;
            double d9 = mc.thePlayer.posY - (double)k;
            double d10 = mc.thePlayer.posZ + d2;
            double d11 = d8 - mc.thePlayer.posX;
            double d12 = d10 - mc.thePlayer.posZ;
            float f7 = (float)(MathHelper.atan2(d12, d11) * 180.0 / Math.PI - 90.0);
            float f8 = (float)(-(MathHelper.atan2(0.0, (double)MathHelper.sqrt_double(d11 * d11 + d12 * d12)) * 180.0 / Math.PI));
            float f9 = RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().yaw, f7, 180.0F);
            float f10 = RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().pitch, f8, 180.0F);

            for (float m = 0.0F; m <= 360.0F; m++) {
               Vec3 vectorForRotation3 = mc.thePlayer.getVectorForRotation(f10, f9 + m);
               MovingObjectPosition hit2 = mc.theWorld
                  .rayTraceBlocks(d8, d9, d10, d8 + vectorForRotation3.xCoord * 4.5, d9 + vectorForRotation3.yCoord * 4.5, d10 + vectorForRotation3.zCoord * 4.5, false, false, true);
               if (hit2 != null
                  && hit2.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
                  && hit2.getBlockPos() != null
                  && BlockUtil.isSolidBlock(hit2.getBlockPos())
                  && !list2.contains(hit2.getBlockPos())
                  && !BlockUtil.isWalkableBlock(hit2.getBlockPos().up())) {
                  list.add(new BlockPlacementData(hit2.getBlockPos(), hit2.sideHit, hit2.hitVec));
                  list2.add(hit2.getBlockPos());
               }
            }
         }

         list.sort(
            Comparator.comparingDouble(
               obj2 -> mc.thePlayer.getDistance((double)((BlockPlacementData)obj2).getBlockPos().getX() + 0.5, (double)((BlockPlacementData)obj2).getBlockPos().getY() + 0.5, (double)((BlockPlacementData)obj2).getBlockPos().getZ() + 0.5)
            )
         );
         if (!list.isEmpty()) {
            return (BlockPlacementData)list.get(0);
         } else {
            for (int idx = 1; idx < 4; idx++) {
               BlockPos pos2 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - (double)idx, mc.thePlayer.posZ);
               if (BlockUtil.isSolidBlock(pos2)) {
                  return new BlockPlacementData(pos2, EnumFacing.UP, new Vec3((double)pos2.getX() + 0.5, (double)(pos2.getY() + 1), (double)pos2.getZ() + 0.5));
               }
            }

            System.out.println("Null");
            return null;
         }
      }
   }

   private boolean shouldStrafeYaw(float f, BlockPos pos) {
      return this.strafe.getValue() && RotationUtil.isNearCardinalYaw(f) && pos != null;
   }

   private float computeStrafeYawOffset(float f, float f2, BlockPos pos) {
      switch (FailedScaffoldFacingSwitchMap.ORDINAL_MAP[mc.thePlayer.getHorizontalFacing(f).ordinal()]) {
         case 1:
            if (mc.thePlayer.posX > (double)pos.getX() + 0.63) {
               f2 = -45.0F;
            } else if (mc.thePlayer.posX < (double)pos.getX() + 0.38) {
               f2 = 45.0F;
            }
            break;
         case 2:
            if (mc.thePlayer.posX > (double)pos.getX() + 0.63) {
               f2 = 45.0F;
            } else if (mc.thePlayer.posX < (double)pos.getX() + 0.38) {
               f2 = -45.0F;
            }
            break;
         case 3:
            if (mc.thePlayer.posZ > (double)pos.getZ() + 0.63) {
               f2 = -45.0F;
            } else if (mc.thePlayer.posZ < (double)pos.getZ() + 0.38) {
               f2 = 45.0F;
            }
            break;
         case 4:
            if (mc.thePlayer.posZ > (double)pos.getZ() + 0.63) {
               f2 = 45.0F;
            } else if (mc.thePlayer.posZ < (double)pos.getZ() + 0.38) {
               f2 = -45.0F;
            }
      }

      return f2;
   }

   private double scoreRaycastCandidate(float f, Vec3 vec, ScaffoldRaycastResult scaffoldRaycastResult) {
      return this.getHitCenterOffset(scaffoldRaycastResult, f, vec);
   }

   private boolean isMaxOffGroundSneakForwardVisible() {
      return this.sneak.getValue() && this.oneStackForward.getValue();
   }

   private boolean isMinOffGroundSneakForwardVisible() {
      return this.sneak.getValue() && this.oneStackForward.getValue();
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
