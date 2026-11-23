package net.augustus.modules.player;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.EventClick;
import net.augustus.utils.rotation.MoveFixMode;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class AutoExtinguish extends Module implements TickListener, ClickListener, SilentSlotListener, LoadWorldListener, MoveInputListener {
   private final RandomizedRangeSettings verticalSpeed;
   private final BooleanValue randomizeYaw;
   private final BooleanValue notWhileKillAura;
   private final BooleanValue silentSlotSwitching;
   private int bucketHotbarSlot;
   private int previousBucketHotbarSlot;
   private Vec3 waterTargetPos;
   private BlockPos placedWaterBlockPos;
   private int ticksSinceWaterPlaced;

   public AutoExtinguish() {
       super("AutoExtinguish", "AutomatischesLöschen", new Color(252, 152, 3), Categorys.PLAYER);
      this.verticalSpeed = new RandomizedRangeSettings(this, "Vertical Speed", 15.0, 30.0, 0.0, 180.0, 0);
      this.randomizeYaw = new BooleanValue(this, "RandomizeYaw", true);
      this.notWhileKillAura = new BooleanValue(this, "NotWhileKillAura", false);
      this.silentSlotSwitching = new BooleanValue(this, "SilentSlotSwitching", true);
      this.bucketHotbarSlot = -1;
      this.previousBucketHotbarSlot = -1;
      this.waterTargetPos = null;
      this.placedWaterBlockPos = null;
      this.ticksSinceWaterPlaced = 0;
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
      this.bucketHotbarSlot = -1;
      this.previousBucketHotbarSlot = -1;
      this.waterTargetPos = null;
      this.placedWaterBlockPos = null;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
      byte b2 = 7;
      this.toggleEventSubscription(b2);
      byte b3 = 34;
      this.toggleEventSubscription(b3);
      byte b4 = 17;
      this.toggleEventSubscription(b4);
      byte b5 = 21;
      this.toggleEventSubscription(b5);
   }

   @Override
   public void onClick(EventClick event) {
      if (this.canOperate()) {
         this.ticksSinceWaterPlaced++;
         if (this.bucketHotbarSlot >= 0 && this.previousBucketHotbarSlot >= 0) {
            if (this.bucketHotbarSlot == mc.thePlayer.inventory.currentItem) {
               if (ClientManagers.rotationController.getPreviousRotation().pitch > 88.0F && this.waterTargetPos == null) {
                  if (this.isBurningWithoutProtection()) {
                     byte b = 1;
                     event.addRightClicks(b);
                     Runnable runnable = () -> this.placeWaterAndRecordTarget();
                     event.setRightClickAction(runnable);
                     return;
                  }
               }

               if (this.waterTargetPos != null) {
                  int lastGroundY = (int)mc.thePlayer.lastGroundY;
                  Vec3 vec = this.findNearbyWaterSourcePos(lastGroundY);
                  if (vec == null && this.ticksSinceWaterPlaced > 2) {
                     this.waterTargetPos = null;
                     return;
                  }

                  if (vec != null) {
                     this.waterTargetPos = vec;
                  }

                  if (this.waterTargetPos != null && this.ticksSinceWaterPlaced > 0) {
                     if (!this.isBurningWithoutProtection()
                        && mc.objectMouseOver != null
                        && mc.previousObjectMouseOver != null
                        && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
                        && mc.previousObjectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
                        && this.placedWaterBlockPos != null) {
                        BlockPos pos = new BlockPos(this.waterTargetPos.xCoord, this.waterTargetPos.yCoord, this.waterTargetPos.zCoord);
                        if (pos.equalsPos(mc.objectMouseOver.getBlockPos()) && pos.equalsPos(mc.previousObjectMouseOver.getBlockPos())) {
                           byte b2 = 1;
                           event.addRightClicks(b2);
                           this.waterTargetPos = null;
                           this.ticksSinceWaterPlaced = 0;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void onSilentSlot() {
      if (this.canOperate()) {
         this.previousBucketHotbarSlot = this.bucketHotbarSlot;
         if (this.isBurningWithoutProtection() || this.waterTargetPos != null) {
            this.bucketHotbarSlot = this.findBucketHotbarSlot();
            if (this.bucketHotbarSlot >= 0) {
               int bucketHotbarSlot2 = this.bucketHotbarSlot;
               boolean value = this.silentSlotSwitching.getValue();
               int n = bucketHotbarSlot2;
               byte b = -2;
               SilentSlotManager.requestSlot(this, b, n, value);
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (this.canOperate()) {
         if ((this.isBurningWithoutProtection() || this.waterTargetPos != null) && this.bucketHotbarSlot >= 0) {
            if (this.waterTargetPos == null) {
               float f = RotationUtil.updatePitchWithGcd(ClientManagers.rotationController.getPreviousRotation().pitch, 90.0F, (float)this.verticalSpeed.nextValue());
               if (f != ClientManagers.rotationController.getPreviousRotation().pitch) {
                  Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
                  previousRotation.pitch = f;
                  if (this.randomizeYaw.getValue() && f < 88.0F) {
                     float f2 = 2.0F;
                     float f3 = -2.0F;
                     previousRotation.yaw = previousRotation.yaw + DefaultRandomGenerator.instance.nextFloat(f3, f2);
                  }

                  Rotation previousRotation2 = ClientManagers.rotationController.getPreviousRotation();
                  previousRotation = RotationUtil.applyGcdFix(previousRotation, previousRotation2);
                  byte b = -2;
                  ClientManagers.rotationController.requestRotation(previousRotation, b, this);
               }
            } else {
               double xCoord = this.waterTargetPos.xCoord;
               double yCoord = this.waterTargetPos.yCoord;
               double zCoord = this.waterTargetPos.zCoord;
               Rotation previousRotation3 = ClientManagers.rotationController.getPreviousRotation();
               float f4 = (float)this.verticalSpeed.nextValue();
               float f5 = (float)this.verticalSpeed.nextValue();
               boolean flag = true;
               float f6 = f5;
               float f7 = f4;
               Rotation rotationsToPos = RotationUtil.getRotationsToPos(xCoord, yCoord, zCoord, previousRotation3, f7, f6, flag);
               Rotation previousRotation4 = ClientManagers.rotationController.getPreviousRotation();
               rotationsToPos = RotationUtil.applyGcdFix(rotationsToPos, previousRotation4);
               byte b2 = -2;
               ClientManagers.rotationController.requestRotation(rotationsToPos, b2, this);
            }
         }
      }
   }

   private boolean canOperate() {
      return mc.isNotInGui() && (!this.notWhileKillAura.getValue() || !ClientManagers.moduleManager.killAura.isToggled() || ClientManagers.moduleManager.killAura.target == null);
   }

   private boolean isBurningWithoutProtection() {
      return mc.thePlayer != null && mc.thePlayer.isBurning() && !ClientManagers.moduleManager.scaffold.isToggled() && !mc.thePlayer.isPotionActive(Potion.fireResistance);
   }

   private int findBucketHotbarSlot() {

      for (int i = 36; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (stack != null && (this.waterTargetPos == null ? stack.getItem().equals(Items.water_bucket) : stack.getItem().equals(Items.bucket))) {
            return i - 36;
         }
      }

      return -1;
   }

   private Vec3 getBlockFaceCenter(int n, int n2, int n3, EnumFacing facing) {
      return new Vec3(
         (double)n + (facing.getFrontOffsetX() == 0 ? 0.5 : (double)facing.getFrontOffsetX()),
         (double)n2 + (facing.getFrontOffsetY() == 0 ? 0.5 : (double)facing.getFrontOffsetY()),
         (double)n3 + (facing.getFrontOffsetZ() == 0 ? 0.5 : (double)facing.getFrontOffsetZ())
      );
   }

   private Vec3 getBlockFaceCenterFromPos(BlockPos pos, EnumFacing facing) {
      int x = pos.getX();
      int z = pos.getZ();
      int y = pos.getY();
      int n = x;
      return this.getBlockFaceCenter(n, y, z, facing);
   }

   private Vec3 getHitFaceCenter(MovingObjectPosition hit) {
      EnumFacing sideHit = hit.sideHit;
      BlockPos blockPos = hit.getBlockPos();
      return this.getBlockFaceCenterFromPos(blockPos, sideHit);
   }

   private Vec3 findNearbyWaterSourcePos(int n) {

      for (int i = -10; i <= 10; i++) {
         for (int j = -10; j <= 10; j++) {
            IBlockState blockState2 = mc.theWorld
               .getBlockState2(MathHelper.floor_double(mc.thePlayer.posX + (double)i), n, MathHelper.floor_double(mc.thePlayer.posZ + (double)j));
            Block block = blockState2.getBlock();
            if (block instanceof BlockLiquid && (Integer)blockState2.getValue(BlockLiquid.LEVEL) == 0 && block.getMaterial() == Material.water) {
               int n2 = MathHelper.floor_double(mc.thePlayer.posX + (double)i);
               int n3 = MathHelper.floor_double(mc.thePlayer.posZ + (double)j);
               int n4 = n2;
               return this.findAdjacentSolidFace(n4, n, n3);
            }
         }
      }

      return null;
   }

   private Vec3 findAdjacentSolidFace(int n, int n2, int n3) {
      EnumFacing[] values = EnumFacing.getValues();
      int length = values.length;

      for (int i = 0; i < length; i++) {
         EnumFacing facing = values[i];
         IBlockState blockState2 = mc.theWorld.getBlockState2(n + facing.getFrontOffsetX(), n2 + facing.getFrontOffsetY(), n3 + facing.getFrontOffsetZ());
         if (blockState2.getBlock().getMaterial() != Material.air) {
            return this.getBlockFaceCenter(n, n2, n3, facing);
         }
      }

      return null;
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.bucketHotbarSlot = -1;
      this.previousBucketHotbarSlot = -1;
      this.waterTargetPos = null;
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (ClientManagers.rotationController.isOwnedBy(this)) {
         MoveFixMode moveFixMode = MoveFixMode.DEFAULT;
         event.setMoveFixMode(moveFixMode);
      }
   }

   private void placeWaterAndRecordTarget() {
      if (mc.objectMouseOver != null
         && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
         && mc.previousObjectMouseOver != null
         && mc.previousObjectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
         && mc.objectMouseOver.getBlockPos().equalsPos(mc.previousObjectMouseOver.getBlockPos())) {
         mc.rightClickMouse();
         MovingObjectPosition objectMouseOver = mc.objectMouseOver;
         this.waterTargetPos = this.getHitFaceCenter(objectMouseOver);
         this.placedWaterBlockPos = mc.objectMouseOver.getBlockPos();
         this.ticksSinceWaterPlaced = 0;
      }
   }

}
