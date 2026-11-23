package net.augustus.modules.modes.fly;

import net.augustus.ClientManagers;
import net.augustus.events.EventBlockBoundingBox;
import net.augustus.events.EventClick;
import net.augustus.events.EventMotion;
import net.augustus.events.EventMoveEntity;
import net.augustus.events.EventMoveInput;
import net.augustus.events.EventPacketDelay;
import net.augustus.utils.player.ContainerSlotQuery;
import net.augustus.utils.player.SilentSlotManager;
import java.util.OptionalInt;
import net.augustus.modules.movement.Fly;
import net.augustus.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class Polar2FlyMode extends FlyMode {
   private int waterBucketSlot = -1;
   private volatile boolean waitingWaterPickup = false;
   private volatile boolean shouldJumpAfterPickup = false;
   private boolean initializedOnGround = false;
   private boolean pendingFirstClick = false;
   private boolean pendingSecondClick = false;
   private boolean jumpedAfterPickup;
   private boolean delayIncomingPackets;
   private boolean landedWaitingGlide;
   private boolean flyingActive;
   private double startY;
   private volatile boolean waterPlacedDetected;
   private final TimeHelper initDelayTimer;
   private final TimeHelper secondClickTimer;
   private final TimeHelper pickupConfirmTimer;
   private final TimeHelper postLandTimer;
   private BlockPos waterPlacePos;

   public Polar2FlyMode(String str, Fly fly2) {
      super(str, fly2);
      this.jumpedAfterPickup = false;
      this.delayIncomingPackets = false;
      this.landedWaitingGlide = false;
      this.flyingActive = false;
      this.waterPlacedDetected = false;
      this.initDelayTimer = new TimeHelper();
      this.secondClickTimer = new TimeHelper();
      this.pickupConfirmTimer = new TimeHelper();
      this.postLandTimer = new TimeHelper();
      this.waterPlacePos = new BlockPos(0, 0, 0);
   }

   public void onEnable() {
      if (mc.thePlayer != null) {
         this.pendingSecondClick = false;
         this.shouldJumpAfterPickup = false;
         this.pendingFirstClick = true;
         this.jumpedAfterPickup = false;
         this.initializedOnGround = false;
         this.flyingActive = false;
         this.waterBucketSlot = -1;
         this.delayIncomingPackets = false;
         this.landedWaitingGlide = false;
         this.waterPlacedDetected = true;
         this.waitingWaterPickup = false;
      }
   }

   public void onDisable() {
   }

   public void onPreUpdate() {
   }

   public void onEarlyTick() {
      if (!this.initializedOnGround && mc.thePlayer.onGround) {
         this.startY = mc.thePlayer.posY;
         this.initializedOnGround = true;
         this.initDelayTimer.reset();
         this.waterPlacePos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
      }

      if (this.waterBucketSlot >= 0) {
         Fly fly2 = this.fly;
         byte b = -90;
         float f = 90.0F;
         ClientManagers.rotationController.requestPitch(f, b, fly2);
      }
   }

   public void onMotion(EventMotion event) {
   }

   public void onMoveEntity(EventMoveEntity event) {
   }

   public void onIncomingPacketDelay(EventPacketDelay event) {
      if (this.delayIncomingPackets) {
         boolean flag = true;
         event.setDelayed(flag);
      }

      Packet packet = event.getPacket();
      if (packet instanceof S23PacketBlockChange) {
         S23PacketBlockChange packet2 = (S23PacketBlockChange)packet;
         BlockPos blockPosition = packet2.getBlockPosition();
         IBlockState blockState = packet2.getBlockState();
         BlockPos pos = blockPosition;
         this.onBlockStateChange(pos, blockState);
      }

      if (packet instanceof S22PacketMultiBlockChange) {
         S22PacketMultiBlockChange packet3 = (S22PacketMultiBlockChange)packet;
         S22PacketMultiBlockChange.BlockUpdateData[] changedBlocks = packet3.getChangedBlocks();

         for (S22PacketMultiBlockChange.BlockUpdateData blockUpdateData : changedBlocks) {
            BlockPos pos2 = blockUpdateData.getPos();
            IBlockState blockState2 = blockUpdateData.getBlockState();
            BlockPos pos3 = pos2;
            this.onBlockStateChange(pos3, blockState2);
         }
      }
   }

   public void onMoveInput(EventMoveInput event) {
      exitBlock: {
         if (this.flyingActive) {
            long l = 325L;
            if (this.postLandTimer.reached2(l)) {
               float f = 1.0F;
               event.setMoveForward(f);
               long l2 = 425L;
               if (this.postLandTimer.reached2(l2)) {
                  boolean flag = true;
                  event.setJump(flag);
               } else {
                  boolean flag2 = false;
                  event.setJump(flag2);
               }
               break exitBlock;
            }
         }

         float f2 = 0.0F;
         event.setMoveForward(f2);
         boolean flag3 = false;
         event.setJump(flag3);
      }

      if (this.shouldJumpAfterPickup) {
         this.shouldJumpAfterPickup = false;
         boolean flag4 = true;
         event.setJump(flag4);
         this.jumpedAfterPickup = true;
      }
   }

   public void onClick(EventClick event) {
      long l = 525L;
      if (this.initDelayTimer.reached2(l) && this.initializedOnGround && this.waterBucketSlot >= 0) {
         if (this.pendingFirstClick) {
            this.pendingFirstClick = false;
            byte b = 1;
            event.addRightClicks(b);
            this.secondClickTimer.reset();
            this.pendingSecondClick = true;
         } else if (this.pendingSecondClick) {
            long l2 = 575L;
            if (this.secondClickTimer.reached2(l2)) {
               this.pendingSecondClick = false;
               byte b2 = 1;
               event.addRightClicks(b2);
               this.waitingWaterPickup = true;
            }
         }
      }

      if (this.pendingSecondClick && this.waterPlacedDetected && !this.delayIncomingPackets) {
         this.delayIncomingPackets = true;
      }

      if (this.jumpedAfterPickup && mc.thePlayer.onGround && mc.thePlayer.onGroundTicks > 5) {
         this.jumpedAfterPickup = false;
         this.delayIncomingPackets = false;
         this.landedWaitingGlide = true;
         this.postLandTimer.reset();
      }

      if (this.landedWaitingGlide) {
         long l3 = 325L;
         if (this.postLandTimer.reached2(l3)) {
            this.delayIncomingPackets = true;
            long l4 = 375L;
            if (this.postLandTimer.reached2(l4)) {
               this.flyingActive = true;
            }
         }
      }
   }

   public void onSilentSlot() {
      if (this.waterBucketSlot == -1 && this.initializedOnGround) {
         ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
         byte b = 36;
         containerSlotQuery = containerSlotQuery.fromSlot(b);
         Item waterBucket = Items.water_bucket;
         OptionalInt optionalInt = containerSlotQuery.filterItemExact(waterBucket).findFirstSlotIndex();
         if (optionalInt.isPresent()) {
            this.waterBucketSlot = optionalInt.getAsInt() - 36;
            this.initDelayTimer.reset();
         } else {
            String str = "§cYou need a water bucket in your hotbar. Fly disabled.";
            this.fly.sendChatMessage2(str);
            this.waterBucketSlot = -2;
            boolean flag = false;
            this.fly.setToggled(flag);
         }
      }

      if (this.waterBucketSlot >= 0 && !this.flyingActive) {
         boolean flag2 = true;
         int waterBucketSlot2 = this.waterBucketSlot;
         byte b2 = -10;
         SilentSlotManager.requestSlot(this.fly, b2, waterBucketSlot2, flag2);
      }
   }

   public void onBlockBoundingBox(EventBlockBoundingBox event) {
      if (this.flyingActive) {
         Block block = event.getBlock();
         if ((block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockWeb) && (double)event.getBlockPos().getY() < this.startY) {
            BlockPos blockPos = event.getBlockPos();
            AxisAlignedBB box = new AxisAlignedBB(
               (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), (double)(blockPos.getX() + 1), (double)(blockPos.getY() + 1), (double)(blockPos.getZ() + 1)
            );
            event.setBoundingBox(box);
         }
      }
   }

   private void onBlockStateChange(BlockPos pos, IBlockState state) {
      Block block = state.getBlock();
      if (pos.equals(this.waterPlacePos) && block instanceof BlockDynamicLiquid) {
         this.waterPlacedDetected = true;
      }

      if (this.waitingWaterPickup && pos.equals(this.waterPlacePos) && block instanceof BlockAir) {
         this.pickupConfirmTimer.reset();
         this.shouldJumpAfterPickup = true;
      }
   }

}
