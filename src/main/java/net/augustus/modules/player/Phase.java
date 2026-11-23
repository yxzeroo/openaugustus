package net.augustus.modules.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.world.WorldSettings;
import java.awt.Color;
import java.util.List;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.BlockBreakListener;
import net.augustus.events.listeners.BlockPlaceListener;
import net.augustus.utils.MoveUtil;
import net.augustus.events.EventBlockPlace;
import net.augustus.events.listeners.MoveEntityListener;
import net.augustus.events.listeners.NoClipListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.EventMoveInput;
import net.augustus.events.BlockPlaceStage;
import net.augustus.events.EventBlockBreak;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.EventMoveEntity;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

public class Phase extends Module implements NoClipListener, MoveInputListener, MoveEntityListener, TickListener, BlockBreakListener, BlockPlaceListener {
   private final DoubleValue horizontal;
   private final BooleanValue customizeHorizontalMovespeedInBlocks;
   private final BooleanValue useSneakMotionInBlocks;
   private final StringValue movespeedInBlocks;
   private final DoubleValue vertical;
   private final BooleanValue fallIfNoBlockIsUnderPlayer;
   private final BooleanValue upAndDown;
   private final StringValue upwardsSteps;
   private final StringValue upwardsTickDelay;
   private final StringValue downwardsSteps;
   private final StringValue downwardsTickDelay;
   private final BooleanValue clipDownByBreaking;
   private final StringValue packetCount;
   private int verticalStepTicks;
   private float cachedYaw;
   private boolean insideBlock;
   private boolean clippedByBreaking;

   public Phase() {
       super("Phase", "DurchWändeGehen", new Color(45, 196, 148), Categorys.PLAYER);
      this.horizontal = new DoubleValue(this, "Horizontal");
      this.customizeHorizontalMovespeedInBlocks = new BooleanValue(this, "Customize horizontal Movespeed in blocks", false, this.horizontal);
      BooleanValue booleanValue = new BooleanValue(this, "Use sneak motion in blocks", false, this.horizontal);
      BooleanValue booleanValue2 = this.customizeHorizontalMovespeedInBlocks;
      this.customizeHorizontalMovespeedInBlocks.getClass();
      BooleanValue booleanValue3 = booleanValue2;
      SettingVisibilityCondition condition = () -> booleanValue3.getValue();
      this.useSneakMotionInBlocks = (BooleanValue)booleanValue.withVisibility(condition);
      StringValue stringValue = new StringValue(this, "Movespeed in blocks", 0.1, 1.0E-4, 1.0, 4, this.horizontal);
      SettingVisibilityCondition condition2 = () -> this.isMovespeedInBlocksVisible();
      this.movespeedInBlocks = stringValue.visibleWhen(condition2);
      this.vertical = new DoubleValue(this, "Vertical");
      this.fallIfNoBlockIsUnderPlayer = new BooleanValue(this, "Fall if no block is under player", false, this.vertical);
      this.upAndDown = new BooleanValue(this, "UpAndDown", true, this.vertical);
      StringValue stringValue2 = new StringValue(this, "UpwardsSteps", 1.0, 0.0, 2.0, 2, this.vertical);
      booleanValue2 = this.upAndDown;
      this.upAndDown.getClass();
      BooleanValue booleanValue4 = booleanValue2;
      SettingVisibilityCondition condition3 = () -> booleanValue4.getValue();
      this.upwardsSteps = stringValue2.visibleWhen(condition3);
      StringValue stringValue3 = new StringValue(this, "UpwardsTickDelay", 5.0, 0.0, 20.0, 0, this.vertical);
      booleanValue2 = this.upAndDown;
      this.upAndDown.getClass();
      BooleanValue booleanValue5 = booleanValue2;
      SettingVisibilityCondition condition4 = () -> booleanValue5.getValue();
      this.upwardsTickDelay = stringValue3.visibleWhen(condition4);
      StringValue stringValue4 = new StringValue(this, "DownwardsSteps", 1.0, 0.0, 2.0, 2, this.vertical);
      booleanValue2 = this.upAndDown;
      this.upAndDown.getClass();
      BooleanValue booleanValue6 = booleanValue2;
      SettingVisibilityCondition condition5 = () -> booleanValue6.getValue();
      this.downwardsSteps = stringValue4.visibleWhen(condition5);
      StringValue stringValue5 = new StringValue(this, "DownwardsTickDelay", 5.0, 0.0, 20.0, 0, this.vertical);
      booleanValue2 = this.upAndDown;
      this.upAndDown.getClass();
      BooleanValue booleanValue7 = booleanValue2;
      SettingVisibilityCondition condition6 = () -> booleanValue7.getValue();
      this.downwardsTickDelay = stringValue5.visibleWhen(condition6);
      this.clipDownByBreaking = new BooleanValue(this, "Clip down by breaking", false, this.vertical);
      StringValue stringValue6 = new StringValue(this, "Packet count", 1.0, 1.0, 10.0, 0, this.vertical);
      booleanValue2 = this.clipDownByBreaking;
      this.clipDownByBreaking.getClass();
      BooleanValue booleanValue8 = booleanValue2;
      SettingVisibilityCondition condition7 = () -> booleanValue8.getValue();
      this.packetCount = stringValue6.visibleWhen(condition7);
      this.verticalStepTicks = 0;
      this.cachedYaw = 0.0F;
      this.clippedByBreaking = false;
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
      if (mc.playerController.getCurrentGameType() == WorldSettings.GameType.ADVENTURE) {
         mc.thePlayer.capabilities.allowEdit = false;
      }
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 27;
      this.toggleEventSubscription(b);
      byte b2 = 21;
      this.toggleEventSubscription(b2);
      byte b3 = 20;
      this.toggleEventSubscription(b3);
      byte b4 = 40;
      this.toggleEventSubscription(b4);
      byte b5 = 59;
      this.toggleEventSubscription(b5);
      byte b6 = 60;
      this.toggleEventSubscription(b6);
   }

   @Override
   public void onNoClip2() {
      AxisAlignedBB entityBoundingBox = mc.thePlayer.getEntityBoundingBox();
      this.insideBlock = this.isInsideBlock(entityBoundingBox);
      if (this.insideBlock) {
         if (this.fallIfNoBlockIsUnderPlayer.getValue()) {
            if (!this.hasBlockUnderPlayer()) {
               return;
            }
         }

         mc.thePlayer.noClip = true;
         mc.thePlayer.motionY = 0.0;
         mc.thePlayer.onGround = true;
      }
   }

   private boolean hasBlockUnderPlayer() {
      AxisAlignedBB entityBoundingBox = mc.thePlayer.getEntityBoundingBox();
      entityBoundingBox = new AxisAlignedBB(entityBoundingBox.minX, entityBoundingBox.minY - 0.078, entityBoundingBox.minZ, entityBoundingBox.maxX, entityBoundingBox.maxY, entityBoundingBox.maxZ);

      for (AxisAlignedBB box : (Iterable<AxisAlignedBB>)(mc.theWorld.getCollidingBoundingBoxesCapped(mc.thePlayer, entityBoundingBox))) {
         if (box.maxY <= mc.thePlayer.posY && box.intersectsWith(entityBoundingBox)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void onTick() {
      this.clippedByBreaking = false;
      if (this.clipDownByBreaking.getValue()) {
         mc.thePlayer.capabilities.allowEdit = true;
         if (mc.playerController.isHittingBlock) {
            MovingObjectPosition objectMouseOver = mc.objectMouseOver;
            BlockPos blockPos = objectMouseOver.getBlockPos();
            if (objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
               && Math.abs((double)blockPos.getY() - mc.thePlayer.posY) < 2.0
               && !mc.theWorld.getBlockState2(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ()).getBlock().hasCollisionBox()) {
               EntityPlayerSP thePlayer = mc.thePlayer;
               thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, objectMouseOver.getBlockPos(), objectMouseOver.sideHit));

               for (int i = 0; (double)i < this.packetCount.getValue(); i++) {
                  thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(thePlayer.posX, thePlayer.posY, thePlayer.posZ, true));
               }

               this.clippedByBreaking = true;
               mc.playerController.setCurBlockDamageMP(1.0E-4F);
            }
         }
      }
   }

   private boolean isInsideBlock(AxisAlignedBB box) {
      List list = mc.theWorld.getCollidingBoundingBoxesCapped(mc.thePlayer, mc.thePlayer.getEntityBoundingBox());

      for (AxisAlignedBB box2 : (Iterable<AxisAlignedBB>)(list)) {
         if (box2.intersectsWith(box)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      this.verticalStepTicks++;
      if (!this.insideBlock || !this.upAndDown.getValue()) {
         this.verticalStepTicks = 0;
      } else if (event.isJump()) {
         if ((double)this.verticalStepTicks > this.upwardsTickDelay.getValue()) {
            mc.thePlayer.motionY = this.upwardsSteps.getValue();
            this.verticalStepTicks = 0;
         }
      } else if (event.isSneak()) {
         if ((double)this.verticalStepTicks > this.downwardsTickDelay.getValue()) {
            mc.thePlayer.motionY = -this.downwardsSteps.getValue();
            this.verticalStepTicks = 0;
         }
      } else {
         this.verticalStepTicks = 0;
      }

      if (this.insideBlock) {
         if (event.isSneak() && this.upAndDown.getValue()) {
            boolean flag = false;
            event.setSneak(flag);
         }

         if (this.useSneakMotionInBlocks.getValue()) {
            boolean flag2 = true;
            event.setSneak(flag2);
         }

         if (event.isJump()) {
            boolean flag3 = false;
            event.setJump(flag3);
         }
      }
   }

   @Override
   public void onMoveEntity(EventMoveEntity event) {
      if (mc.thePlayer.noClip && this.customizeHorizontalMovespeedInBlocks.getValue() && !this.useSneakMotionInBlocks.getValue()) {
         MoveUtil.setMotionWithOffsetAndSprint(this.movespeedInBlocks.getValue(), 0.0, mc.thePlayer.rotationYaw, false);
         double motionX = mc.thePlayer.motionX;
         event.setX(motionX);
         double motionZ = mc.thePlayer.motionZ;
         event.setZ(motionZ);
      }
   }

   @Override
   public void onBlockBreak(EventBlockBreak event) {
      if (this.clippedByBreaking || mc.playerController.getCurrentGameType() == WorldSettings.GameType.ADVENTURE) {
         event.setCancelled(true);
      }
   }

   @Override
   public void onBlockPlace(EventBlockPlace event) {
      if (event.getStage() == BlockPlaceStage.PRE && mc.thePlayer.capabilities.allowEdit && mc.playerController.getCurrentGameType() == WorldSettings.GameType.ADVENTURE) {
         IBlockState blockState = mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos());
         if (blockState.getBlock() != Blocks.trapdoor && (!(blockState.getBlock() instanceof BlockDoor) || blockState.getBlock() == Blocks.iron_door)) {
            event.setCancelled(true);
         }
      }
   }

   private boolean isMovespeedInBlocksVisible() {
      return this.customizeHorizontalMovespeedInBlocks.getValue() && !this.useSneakMotionInBlocks.getValue();
   }

}
