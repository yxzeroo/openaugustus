package net.augustus.modules.modes.autoeagle;

import net.augustus.events.EventBlockPlace;
import net.augustus.events.EventClick;
import net.augustus.events.EventMouseMove;
import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.BlockPlaceListener;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.MouseMoveListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.modules.modes.anticheat.eagle.MoonWalkFacingSwitchMap;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.TickCounter;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.rotation.MouseDeltaInterpolator;
import net.augustus.utils.rotation.Rotation;
import java.util.function.BiFunction;
import net.augustus.modules.legit.AutoEagle;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class MoonWalkAutoEagleMode extends AutoEagleMode implements MoveInputListener, BlockPlaceListener, MouseMoveListener, TickListener, ClickListener {
   private final RandomizedRangeSettings moonWalkWideness;
   private final RandomizedRangeSettings startDistToBlock;
   private final RandomizedRangeSettings postPlacementStrafeTicks;
   private final RandomizedRangeSettings minMoonwalkTicks;
   private final RandomizedRangeSettings directionChangeDelay;
   private final BooleanValue lockMouse;
   private final BooleanValue minimalMouseMovement;
   private final BooleanValue sneakEveryXBlocks;
   private final RandomizedRangeSettings blocksBetweenSneaks;
   private final RandomizedRangeSettings sneakDuration;
   private int strafeDirection;
   private boolean wasOverFullBlock;
   private boolean wasNearEdge;
   private int ticksSinceBlockPlace;
   private int ticksSinceEdgeLeave;
   private int ticksSinceDirectionChange;
   private int lastStrafeInput;
   private TimeHelper timeHelper;
   private TickCounter tickCounter;
   private MouseDeltaInterpolator mouseDeltaInterpolator;
   private int nextMicroMoveDelayMs;
   private int microMoveDurationTicks;
   private int blocksPlacedSinceSneak;
   private int sneakStartDelayTicks;
   private int remainingSneakTicks;

   public MoonWalkAutoEagleMode(AutoEagle autoEagle, DoubleValue doubleValue) {
       super(autoEagle, doubleValue, "MoonWalk");
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this.parentModule, "MoonWalkWideness", 0.08, 0.15, 0.0, 0.3, 2, this.modeSetting)).withoutNotInARow();
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.moonWalkWideness = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this.parentModule, "StartDistToBlock", 0.05, 0.2, -0.2, 0.5, 2, this.modeSetting)).withoutNotInARow();
      String str = "StartDistToBlock";
      RandomizedRangeSettings randomizedRangeSettings3 = (RandomizedRangeSettings)randomizedRangeSettings2.withDisplayNamePrefix(str);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.startDistToBlock = (RandomizedRangeSettings)randomizedRangeSettings3.withVisibility(condition2);
      RandomizedRangeSettings randomizedRangeSettings4 = (new RandomizedRangeSettings(this.parentModule, "PostPlacementStrafeTicks", 0.0, 1.0, 0.0, 8.0, 0, this.modeSetting)).withMaxInARow();
      boolean flag = false;
      double d = 15.0;
      double d2 = 0.0;
      double d3 = 8.0;
      double d4 = 0.0;
      double d5 = 8.0;
      double d6 = 5.0;
      RandomizedRangeSettings randomizedRangeSettings5 = randomizedRangeSettings4.withOutliers(d6, d5, d4, d3, d2, d, flag);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.postPlacementStrafeTicks = (RandomizedRangeSettings)randomizedRangeSettings5.withVisibility(condition3);
      RandomizedRangeSettings randomizedRangeSettings6 = (new RandomizedRangeSettings(this.parentModule, "MinMoonwalkTicks", 3.0, 4.0, 0.0, 10.0, 0, this.modeSetting)).withMaxInARow();
      boolean flag2 = false;
      double d7 = 15.0;
      double d8 = 0.0;
      double d9 = 10.0;
      double d10 = 0.0;
      double d11 = 12.0;
      double d12 = 8.0;
      RandomizedRangeSettings randomizedRangeSettings7 = randomizedRangeSettings6.withOutliers(d12, d11, d10, d9, d8, d7, flag2);
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      this.minMoonwalkTicks = (RandomizedRangeSettings)randomizedRangeSettings7.withVisibility(condition4);
      RandomizedRangeSettings randomizedRangeSettings8 = (new RandomizedRangeSettings(this.parentModule, "DirectionChangeDelay", 0.0, 1.0, 0.0, 3.0, 0, this.modeSetting)).withMaxInARow();
      SettingVisibilityCondition condition5 = () -> this.isSelected();
      this.directionChangeDelay = (RandomizedRangeSettings)randomizedRangeSettings8.withVisibility(condition5);
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "LockMouse", false, this.modeSetting);
      SettingVisibilityCondition condition6 = () -> this.isSelected();
      this.lockMouse = (BooleanValue)booleanValue.withVisibility(condition6);
      BooleanValue booleanValue2 = new BooleanValue(this.parentModule, "MinimalMouseMovement", false, this.modeSetting);
      SettingVisibilityCondition condition7 = () -> this.isSelected();
      this.minimalMouseMovement = (BooleanValue)booleanValue2.withVisibility(condition7);
      BooleanValue booleanValue3 = new BooleanValue(this.parentModule, "SneakEveryXBlocks", false, this.modeSetting);
      SettingVisibilityCondition condition8 = () -> this.isSelected();
      this.sneakEveryXBlocks = (BooleanValue)booleanValue3.withVisibility(condition8);
      RandomizedRangeSettings randomizedRangeSettings9 = (new RandomizedRangeSettings(this.parentModule, "BlocksBetweenSneaks", 30.0, 32.0, 0.0, 128.0, 0, this.modeSetting)).withMaxInARow();
      SettingVisibilityCondition condition9 = () -> {
         return this.sneakEveryXBlocks.getValue() && this.isSelected();
      };
      this.blocksBetweenSneaks = (RandomizedRangeSettings)randomizedRangeSettings9.withVisibility(condition9);
      RandomizedRangeSettings randomizedRangeSettings10 = (new RandomizedRangeSettings(this.parentModule, "SneakDuration", 11.0, 15.0, 0.0, 20.0, 0, this.modeSetting)).withMaxInARow();
      SettingVisibilityCondition condition10 = () -> this.isSneakDurationVisible();
      this.sneakDuration = (RandomizedRangeSettings)randomizedRangeSettings10.withVisibility(condition10);
      this.strafeDirection = 1;
      this.wasOverFullBlock = false;
      this.wasNearEdge = false;
      this.ticksSinceBlockPlace = 0;
      this.ticksSinceEdgeLeave = 0;
      this.ticksSinceDirectionChange = 0;
      this.lastStrafeInput = 0;
      this.timeHelper = new TimeHelper();
      this.tickCounter = new TickCounter();
      this.mouseDeltaInterpolator = new MouseDeltaInterpolator();
      this.nextMicroMoveDelayMs = 0;
      this.microMoveDurationTicks = 0;
      this.blocksPlacedSinceSneak = 0;
      byte b = 3;
      byte b2 = 1;
      this.sneakStartDelayTicks = DefaultRandomGenerator.instance.nextInt2(b2, b);
      this.remainingSneakTicks = 0;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 21;
      this.toggleEventSubscription(b);
      byte b2 = 60;
      this.toggleEventSubscription(b2);
      byte b3 = 19;
      this.toggleEventSubscription(b3);
      byte b4 = 40;
      this.toggleEventSubscription(b4);
      byte b5 = 10;
      byte b6 = 7;
      this.toggleEventSubscription(b6, b5);
   }

   @Override
   public void onEnable() {
      this.strafeDirection = Math.random() < 0.5 ? -1 : 1;
      this.lastStrafeInput = this.strafeDirection;
      this.wasOverFullBlock = false;
      this.ticksSinceBlockPlace = 0;
      this.ticksSinceEdgeLeave = 0;
      this.ticksSinceDirectionChange = 0;
      this.blocksPlacedSinceSneak = 0;
      this.blocksBetweenSneaks.nextValue();
      this.remainingSneakTicks = 0;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onBlockPlace2(EventBlockPlace event) {
      this.blocksPlacedSinceSneak++;
      this.postPlacementStrafeTicks.nextValue();
      this.ticksSinceBlockPlace = 0;
   }

   @Override
   public void onTick() {
      this.mouseDeltaInterpolator.clearTargetDelta();
      if (this.isInMoonWalkPitchRange() && MoveUtil.isMoving() && this.minimalMouseMovement.getValue()) {
         long nextMicroMoveDelayMs2 = (long)this.nextMicroMoveDelayMs;
         if (this.timeHelper.reached2(nextMicroMoveDelayMs2) && mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ).getBlock().getMaterial() != Material.air
            )
          {
            this.tickCounter.increment();
            int microMoveDurationTicks2 = this.microMoveDurationTicks;
            if (this.tickCounter.hasReached2(microMoveDurationTicks2)) {
               short s = 1500;
               short s2 = 750;
               this.nextMicroMoveDelayMs = DefaultRandomGenerator.instance.nextInt2(s2, s);
               byte b = 2;
               byte b2 = 1;
               this.microMoveDurationTicks = DefaultRandomGenerator.instance.nextInt2(b2, b);
               this.tickCounter.reset();
               this.timeHelper.reset();
            }

            double d = 0.2;
            double d2 = -0.2;
            float f = DefaultRandomGenerator.instance.nextFloat2(d2, d);
            double d3 = 0.2;
            double d4 = -0.2;
            float f2 = DefaultRandomGenerator.instance.nextFloat2(d4, d3);
            float f3 = mc.thePlayer.rotationYaw + f;
            float f4 = mc.thePlayer.rotationPitch + f2;
            if ((double)f4 >= 80.4) {
               f4 = mc.thePlayer.rotationPitch - f2;
            } else if ((double)f4 <= 80.0) {
               f4 = mc.thePlayer.rotationPitch - f2;
            }

            MouseDeltaInterpolator mouseDeltaInterpolator2 = this.mouseDeltaInterpolator;
            Rotation rotation = new Rotation(mc.thePlayer.rotationYaw - 10.0F, 80.0F);
            Rotation rotation2 = new Rotation(mc.thePlayer.rotationYaw + 10.0F, 80.4F);
            Rotation rotation3 = rotation;
            mouseDeltaInterpolator2.setTargetRotationBounded(f3, f4, rotation3, rotation2);
         }
      }
   }

   @Override
   public void onMouseMove(EventMouseMove event) {
      if (this.isInMoonWalkPitchRange() && MoveUtil.isMoving() && mc.gameSettings.keyBindUseItem.isKeyDown() && this.lockMouse.getValue()) {
         byte b = 0;
         event.setDeltaY(b);
         byte b2 = 0;
         event.setDeltaX(b2);
      }

      if (this.minimalMouseMovement.getValue()) {
         this.mouseDeltaInterpolator.onMouseMove(event);
      }
   }

   @Override
   public void onTick4() {
      this.mouseDeltaInterpolator.onTick();
   }

   @Override
   public void onClick(EventClick event) {
      if (this.remainingSneakTicks > 0 && this.sneakStartDelayTicks == 0 && ((AutoEagle)this.parentModule).isPitchAboveMinimum()) {
         boolean flag = true;
         event.setBlockAllClicks(flag);
         boolean flag2 = true;
         event.setIgnoreVanillaClicks(flag2);
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (!((AutoEagle)this.parentModule).isPitchAboveMinimum()) {
         ((AutoEagle)this.parentModule).eagling = false;
         this.blocksPlacedSinceSneak = 0;
         this.remainingSneakTicks = 0;
      } else {
         if (!((AutoEagle)this.parentModule).onlyWhileSneak.getValue()) {
            boolean flag = true;
            event.setSneak(flag);
         }

         ((AutoEagle)this.parentModule).eagling = ((AutoEagle)this.parentModule).fastPlace.getValue() && (event.isSneak() || this.isInMoonWalkPitchRange()) && ((AutoEagle)this.parentModule).isPitchAboveMinimum();
         if (!event.isSneak() && ((AutoEagle)this.parentModule).onlyWhileSneak.getValue()) {
            this.blocksPlacedSinceSneak = 0;
            this.remainingSneakTicks = 0;
         } else {
            this.updateMoonWalkMovement(event);
         }
      }
   }

   private void updateMoonWalkMovement(EventMoveInput event) {
      if (this.isInMoonWalkPitchRange() && MoveUtil.isMoving()) {
         if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
            boolean flag = false;
            event.setSneak(flag);
         }

         if (this.sneakEveryXBlocks.getValue()) {
            this.handlePeriodicSneak(event);
         } else {
            this.remainingSneakTicks = 0;
         }

         if (this.remainingSneakTicks > 0) {
            return;
         }

         BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ).down();
         boolean isFullBlock = mc.theWorld.getBlockState(pos).getBlock().isFullBlock();
         if (this.wasOverFullBlock != isFullBlock) {
            this.startDistToBlock.nextValue();
            this.moonWalkWideness.nextValue();
         }

         this.wasOverFullBlock = isFullBlock;
         boolean flag2 = false;

         for (AxisAlignedBB box : (Iterable<AxisAlignedBB>)(mc.theWorld.getCollidingBoundingBoxesCapped(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -1.0, 0.0)))) {
            if (mc.thePlayer
               .getEntityBoundingBox()
               .expand(-0.3, 0.0, -0.3)
               .offset(0.0, -1.0, 0.0)
               .intersectsWith(
                  box.expand(
                     mc.thePlayer.getHorizontalFacing() != EnumFacing.EAST && mc.thePlayer.getHorizontalFacing() != EnumFacing.WEST ? 0.0 : -this.startDistToBlock.getLastValue(),
                     0.0,
                     mc.thePlayer.getHorizontalFacing() != EnumFacing.NORTH && mc.thePlayer.getHorizontalFacing() != EnumFacing.SOUTH ? 0.0 : -this.startDistToBlock.getLastValue()
                  )
               )) {
               flag2 = true;
               break;
            }
         }

         if (!flag2 && this.wasNearEdge) {
            this.ticksSinceEdgeLeave = 0;
            this.minMoonwalkTicks.nextValue();
         }

         this.ticksSinceEdgeLeave++;
         this.ticksSinceBlockPlace++;
         if (flag2) {
            if (this.ticksSinceBlockPlace <= this.postPlacementStrafeTicks.getLastIntValue() || this.ticksSinceEdgeLeave <= this.minMoonwalkTicks.getLastIntValue()) {
               this.updateStrafeForFacing(event, pos);
            }
         } else {
            this.updateStrafeForFacing(event, pos);
         }

         this.wasNearEdge = flag2;
      } else {
         this.remainingSneakTicks = 0;
         this.ticksSinceEdgeLeave = 0;
         this.ticksSinceBlockPlace = 0;
         this.blocksPlacedSinceSneak = 0;
      }

      this.lastStrafeInput = (int)event.getMoveStrafe();
   }

   private void handlePeriodicSneak(EventMoveInput event) {
      if (this.blocksPlacedSinceSneak >= this.blocksBetweenSneaks.getLastIntValue()) {
         this.blocksPlacedSinceSneak = 0;
         this.remainingSneakTicks = (int)this.sneakDuration.nextValue();
         byte b = 4;
         byte b2 = 3;
         this.sneakStartDelayTicks = DefaultRandomGenerator.instance.nextInt2(b2, b);
      }

      if (this.sneakStartDelayTicks > 0) {
         this.sneakStartDelayTicks--;
      }

      if (this.remainingSneakTicks > 0 && this.sneakStartDelayTicks == 0) {
         this.remainingSneakTicks--;
         boolean flag = true;
         event.setSneak(flag);
      }
   }

   private void updateStrafeForFacing(EventMoveInput event, BlockPos pos) {
      switch (MoonWalkFacingSwitchMap.ORDINAL_MAP[mc.thePlayer.getHorizontalFacing().ordinal()]) {
         case 1:
            boolean flag = true;
            double posZ = mc.thePlayer.posZ;
            this.computeStrafeAlongZ(event, pos, posZ, flag);
            break;
         case 2:
            boolean flag2 = false;
            double posZ2 = mc.thePlayer.posZ;
            this.computeStrafeAlongZ(event, pos, posZ2, flag2);
            break;
         case 3:
            boolean flag3 = true;
            double posX = mc.thePlayer.posX;
            this.computeStrafeAlongX(event, pos, posX, flag3);
            break;
         case 4:
            boolean flag4 = false;
            double posX2 = mc.thePlayer.posX;
            this.computeStrafeAlongX(event, pos, posX2, flag4);
      }
   }

   private boolean isInMoonWalkPitchRange() {
      return (double)mc.thePlayer.rotationPitch < 80.5 && (double)mc.thePlayer.rotationPitch > 79.8;
   }

   private void computeStrafeAlongX(EventMoveInput event, BlockPos pos, double d, boolean flag) {
      double lastValue = this.moonWalkWideness.getLastValue();
      byte b = 0;
      if (flag) {
         if ((double)pos.getX() + 0.5 - lastValue > d) {
            b = -1;
         } else if ((double)pos.getX() + 0.5 + lastValue < d) {
            b = 1;
         }
      } else if ((double)pos.getX() + 0.5 + lastValue < d) {
         b = -1;
      } else if ((double)pos.getX() + 0.5 - lastValue > d) {
         b = 1;
      }

      this.applyDelayedStrafeDirection(event, b);
   }

   private void computeStrafeAlongZ(EventMoveInput event, BlockPos pos, double d, boolean flag) {
      double lastValue = this.moonWalkWideness.getLastValue();
      byte b = 0;
      if (flag) {
         if ((double)pos.getZ() + 0.5 - lastValue > d) {
            b = -1;
         } else if ((double)pos.getZ() + 0.5 + lastValue < d) {
            b = 1;
         }
      } else if ((double)pos.getZ() + 0.5 + lastValue < d) {
         b = -1;
      } else if ((double)pos.getZ() + 0.5 - lastValue > d) {
         b = 1;
      }

      this.applyDelayedStrafeDirection(event, b);
   }

   private void applyDelayedStrafeDirection(EventMoveInput event, int n) {
      this.ticksSinceDirectionChange++;
      if (this.lastStrafeInput != 0 && n != 0 && this.lastStrafeInput != n) {
         this.ticksSinceDirectionChange = 0;
         int n2 = this.directionChangeDelay.nextInt();
         this.strafeDirection = 0;
         if (n2 > 0) {
            Block block = mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ).getBlock();
            if (block.getMaterial() == Material.air && mc.thePlayer.onGround) {
               this.ticksSinceDirectionChange = 100;
            }
         }
      }

      if (this.ticksSinceDirectionChange >= this.directionChangeDelay.getLastIntValue()) {
         if (n != 0) {
            this.strafeDirection = n;
         }

         float strafeDirection2 = (float)this.strafeDirection;
         event.setMoveStrafe(strafeDirection2);
      }
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new MoonWalkAutoEagleMode((AutoEagle)obj, (DoubleValue)obj2);
   }

   private boolean isSneakDurationVisible() {
      return this.sneakEveryXBlocks.getValue() && this.isSelected();
   }

}
