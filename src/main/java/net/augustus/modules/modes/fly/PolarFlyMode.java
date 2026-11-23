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
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class PolarFlyMode extends FlyMode {
   private int waterBucketSlot = -1;
   private boolean blinkRequested = false;
   private boolean blinkActive = false;
   private boolean flyingActive = false;
   private final TimeHelper initDelayTimer = new TimeHelper();
   private final TimeHelper firstClickTimer = new TimeHelper();
   private final TimeHelper secondClickTimer = new TimeHelper();
   private final TimeHelper flyStartTimer = new TimeHelper();
   private boolean pendingFirstClick;
   private boolean pendingSecondClick;
   private boolean waitingStopBlink;
   private boolean waitingStartFly;
   private double startY;
   private boolean waitingStartBlink;
   private BlockPos originBlockPos = BlockPos.ORIGIN;
   private long enableTimestampMs;
   private boolean northWaterFlowed;
   private boolean northWaterCleared;
   private boolean southWaterFlowed;
   private boolean southWaterCleared;
   private boolean eastWaterFlowed;
   private boolean eastWaterCleared;
   private boolean westWaterFlowed;
   private boolean westWaterCleared;
   private boolean initializedOnGround = false;
   private final boolean debugLogging = false;

   public PolarFlyMode(String str, Fly fly2) {
      super(str, fly2);
   }

   public void onEnable() {
      if (mc.thePlayer != null) {
         this.initializedOnGround = false;
         this.waterBucketSlot = -1;
         this.blinkRequested = false;
         this.flyingActive = false;
         this.pendingFirstClick = true;
         this.pendingSecondClick = true;
         this.waitingStopBlink = false;
         this.waitingStartFly = false;
         this.waitingStartBlink = false;
         this.enableTimestampMs = System.currentTimeMillis();
         EnumFacing north = EnumFacing.NORTH;
         BlockPos originBlockPos2 = this.originBlockPos;
         this.northWaterFlowed = this.northWaterCleared = this.isAdjacentBlockSolid(originBlockPos2, north);
         EnumFacing south = EnumFacing.SOUTH;
         BlockPos originBlockPos3 = this.originBlockPos;
         this.southWaterFlowed = this.southWaterCleared = this.isAdjacentBlockSolid(originBlockPos3, south);
         EnumFacing east = EnumFacing.EAST;
         BlockPos originBlockPos4 = this.originBlockPos;
         this.eastWaterFlowed = this.eastWaterCleared = this.isAdjacentBlockSolid(originBlockPos4, east);
         EnumFacing west = EnumFacing.WEST;
         BlockPos originBlockPos5 = this.originBlockPos;
         this.westWaterFlowed = this.westWaterCleared = this.isAdjacentBlockSolid(originBlockPos5, west);
      }
   }

   public void onDisable() {
   }

   public void onEarlyTick() {
      if (!this.initializedOnGround && mc.thePlayer.onGround) {
         if (this.hasClearAirAroundPlayer()) {
            this.initializedOnGround = true;
            this.originBlockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.lastGroundY, mc.thePlayer.posZ);
            this.startY = mc.thePlayer.lastGroundY;
            this.initDelayTimer.reset();
         } else {
            boolean flag = false;
            this.fly.setToggled(flag);
            String str = "§cInvalid starting pos for PolarFly. Please build two blocks up with air to all sites.";
            this.fly.sendChatMessage2(str);
         }
      }

      if (this.waterBucketSlot >= 0) {
         Fly fly2 = this.fly;
         byte b = -90;
         float f = 90.0F;
         ClientManagers.rotationController.requestPitch(f, b, fly2);
      }
   }

   public void onPostUpdate() {
   }

   public void onClick(EventClick event) {
      long l = 500L;
      if (this.initDelayTimer.reached2(l) && this.waterBucketSlot >= 0 && this.initializedOnGround) {
         if (this.pendingFirstClick) {
            byte b = 1;
            event.addRightClicks(b);
            this.pendingFirstClick = false;
            this.firstClickTimer.reset();
            this.waitingStartBlink = true;
            String str = "Click 1 " + this.getElapsedMs();
            this.debugLog(str);
         } else if (this.pendingSecondClick && this.hasWaterFlowedAllSides()) {
            byte b2 = 1;
            event.addRightClicks(b2);
            this.secondClickTimer.reset();
            this.pendingSecondClick = false;
            this.waitingStopBlink = true;
            String str2 = "Click 2 " + this.getElapsedMs();
            this.debugLog(str2);
         }
      }

      if (this.waitingStartBlink) {
         long l2 = 25L;
         if (this.firstClickTimer.reached2(l2)) {
            this.blinkRequested = true;
            this.waitingStartBlink = false;
            String str3 = "Start blink " + this.getElapsedMs();
            this.debugLog(str3);
         }
      }

      if (this.waitingStopBlink) {
         long l3 = 725L;
         if (this.secondClickTimer.reached2(l3)) {
            this.blinkRequested = false;
            this.waitingStopBlink = false;
            String str4 = "Stop blink " + this.getElapsedMs();
            this.debugLog(str4);
            this.flyStartTimer.reset();
            this.waitingStartFly = true;
         }
      }

      if (this.waitingStartFly) {
         long l4 = 475L;
         if (this.flyStartTimer.reached2(l4)) {
            this.blinkRequested = true;
            this.flyingActive = true;
            String str5 = "Start fly " + this.getElapsedMs();
            this.debugLog(str5);
            this.waitingStartFly = false;
         }
      }
   }

   public void onPreUpdate() {
   }

   public void onMotion(EventMotion event) {
   }

   public void onMoveEntity(EventMoveEntity event) {
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
         } else {
            String str = "§cYou need a water bucket in your hotbar. Fly disabled.";
            this.fly.sendChatMessage2(str);
            this.waterBucketSlot = -2;
            boolean flag = false;
            this.fly.setToggled(flag);
         }
      }

      if (this.waterBucketSlot != -1 && this.pendingSecondClick) {
         boolean flag2 = true;
         int waterBucketSlot2 = this.waterBucketSlot;
         byte b2 = -10;
         SilentSlotManager.requestSlot(this.fly, b2, waterBucketSlot2, flag2);
      }
   }

   public void onIncomingPacketDelay(EventPacketDelay event) {
      Packet packet = event.getPacket();
      if (this.blinkRequested) {
         this.blinkActive = true;
      }

      if (packet instanceof S23PacketBlockChange) {
         S23PacketBlockChange packet2 = (S23PacketBlockChange)packet;
         BlockPos blockPosition = packet2.getBlockPosition();
         if (this.isNearOrigin(blockPosition)) {
            IBlockState blockState = packet2.getBlockState();
            Block block = blockState.getBlock();
            if (block instanceof BlockLiquid) {
               float f = (float)((Integer)blockState.getValue(BlockLiquid.LEVEL)).intValue();
               String str = "Value: " + f;
               this.debugLog(str);
            }

            this.updateAdjacentWaterState(blockPosition, block);
            String str2 = "Water flowing block " + blockPosition + " " + blockState.getBlock() + " " + this.getElapsedMs();
            this.debugLog(str2);
         }
      }

      if (packet instanceof S32PacketConfirmTransaction) {
         String str3 = "Confirm transaction " + this.getElapsedMs();
         this.debugLog(str3);
      }

      if (packet instanceof S22PacketMultiBlockChange) {
         S22PacketMultiBlockChange packet3 = (S22PacketMultiBlockChange)packet;
         S22PacketMultiBlockChange.BlockUpdateData[] changedBlocks = packet3.getChangedBlocks();
         boolean flag = false;

         for (S22PacketMultiBlockChange.BlockUpdateData blockUpdateData : changedBlocks) {
            BlockPos pos = blockUpdateData.getPos();
            if (this.isNearOrigin(pos)) {
               IBlockState blockState2 = blockUpdateData.getBlockState();
               Block block2 = blockState2.getBlock();
               if (block2 instanceof BlockLiquid) {
                  float f2 = (float)((Integer)blockState2.getValue(BlockLiquid.LEVEL)).intValue();
                  String str4 = "Value: " + f2;
                  this.debugLog(str4);
               }

               this.updateAdjacentWaterState(pos, block2);
               String str5 = "Water flowing multi block " + pos + " " + blockState2.getBlock() + " " + this.getElapsedMs();
               this.debugLog(str5);
               flag = true;
            }
         }

         if (flag) {
            String str6 = "Multi block END";
            this.debugLog(str6);
         }
      }

      if (event.getPacket() instanceof S08PacketPlayerPosLook) {
         boolean flag2 = false;
         this.fly.setToggled(flag2);
      }

      if (!this.blinkRequested) {
         this.blinkActive = false;
      }

      if (this.blinkActive) {
         boolean flag3 = true;
         event.setDelayed(flag3);
      }

      if (this.blinkRequested && packet instanceof S32PacketConfirmTransaction) {
         this.blinkActive = true;
      }
   }

   public void onMoveInput(EventMoveInput event) {
      if (this.flyingActive) {
         float f = 1.0F;
         event.setMoveForward(f);
      } else if (!this.pendingFirstClick && this.pendingSecondClick) {
         float f2 = 0.0F;
         event.setMoveForward(f2);
         float f3 = 0.0F;
         event.setMoveStrafe(f3);
      }
   }

   public void onBlockBoundingBox(EventBlockBoundingBox event) {
      if (this.flyingActive) {
         Block block = event.getBlock();
         if ((block instanceof BlockAir || block instanceof BlockLiquid) && (double)event.getBlockPos().getY() < this.startY) {
            BlockPos blockPos = event.getBlockPos();
            AxisAlignedBB box = new AxisAlignedBB(
               (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), (double)(blockPos.getX() + 1), (double)(blockPos.getY() + 1), (double)(blockPos.getZ() + 1)
            );
            event.setBoundingBox(box);
         }
      }
   }

   private void updateAdjacentWaterState(BlockPos pos, Block block) {
      if (this.originBlockPos.north().equals(pos) && block instanceof BlockStaticLiquid) {
         this.northWaterFlowed = true;
      }

      if (this.originBlockPos.south().equals(pos) && block instanceof BlockStaticLiquid) {
         this.southWaterFlowed = true;
      }

      if (this.originBlockPos.west().equals(pos) && block instanceof BlockStaticLiquid) {
         this.westWaterFlowed = true;
      }

      if (this.originBlockPos.east().equals(pos) && block instanceof BlockStaticLiquid) {
         this.eastWaterFlowed = true;
      }

      if (this.originBlockPos.north().equals(pos) && block instanceof BlockAir) {
         this.northWaterCleared = true;
      }

      if (this.originBlockPos.south().equals(pos) && block instanceof BlockAir) {
         this.southWaterCleared = true;
      }

      if (this.originBlockPos.west().equals(pos) && block instanceof BlockAir) {
         this.westWaterCleared = true;
      }

      if (this.originBlockPos.east().equals(pos) && block instanceof BlockAir) {
         this.eastWaterCleared = true;
      }
   }

   private boolean isNearOrigin(BlockPos pos) {
      if (this.flyingActive) {
         return false;
      } else {
         for (int i = -1; i <= 1; i++) {
            for (int j = -2; j <= 0; j++) {
               for (int k = -1; k <= 1; k++) {
                  int n = this.originBlockPos.getX() + i;
                  int n2 = this.originBlockPos.getY() + j;
                  int n3 = this.originBlockPos.getZ() + k;
                  if (n == pos.getX() && n2 == pos.getY() && n3 == pos.getZ()) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   private long getElapsedMs() {
      return System.currentTimeMillis() - this.enableTimestampMs;
   }

   private boolean hasWaterFlowedAllSides() {
      return this.northWaterFlowed && this.southWaterFlowed && this.westWaterFlowed && this.eastWaterFlowed;
   }

   private boolean hasWaterClearedAllSides(long l) {
      return this.northWaterCleared && this.southWaterCleared && this.westWaterCleared && this.eastWaterCleared;
   }

   private void debugLog(String str) {
      if (this.debugLogging) {
      }
   }

   private boolean isAdjacentBlockSolid(BlockPos pos, EnumFacing facing) {
      return mc.theWorld
            .getBlockState2(pos.getX() + facing.getFrontOffsetX(), pos.getY() + facing.getFrontOffsetY(), pos.getZ() + facing.getFrontOffsetZ())
            .getBlock()
            .getMaterial()
         != Material.air;
   }

   private boolean hasClearAirAroundPlayer() {

      for (int i = -1; i <= 1; i++) {
         for (EnumFacing facing : EnumFacing.VALUES) {
            if (facing.getAxis().isHorizontal()) {
               double d = mc.thePlayer.posX + (double)facing.getFrontOffsetX();
               double d2 = mc.thePlayer.posY + (double)i;
               double d3 = mc.thePlayer.posZ + (double)facing.getFrontOffsetZ();
               Block block = mc.theWorld.getBlockState(d, d2, d3).getBlock();
               if (block.getMaterial() != Material.air) {
                  return false;
               }
            }
         }
      }

      return true;
   }

}
