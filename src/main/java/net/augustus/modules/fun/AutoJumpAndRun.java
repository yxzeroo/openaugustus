package net.augustus.modules.fun;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.utils.rotation.RotationController;
import net.augustus.utils.world.BlockUtil;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleansSetting;
import net.augustus.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class AutoJumpAndRun extends Module implements TickListener, MoveInputListener {
   public final BooleansSetting mode;
   private final TimeHelper timeHelper;
   private BlockPos nextWoolBlock;
   private BlockPos currentStandingBlock;
   private boolean wasAirborne;

   public AutoJumpAndRun() {
       super("AutoJumpAndRun", "AutoSpringenUndLaufen", new Color(119, 148, 61), Categorys.FUN);
      this.mode = new BooleansSetting(this, "Mode", "Gomme", new String[]{"Gomme"});
      this.timeHelper = new TimeHelper();
      this.nextWoolBlock = null;
      this.currentStandingBlock = null;
   }

   public void onEnable() {
      this.timeHelper.reset();
   }

   public void onDisable() {
   }

   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
      byte b2 = 21;
      this.toggleEventSubscription(b2);
   }

   public void onTick() {
      if (mc.thePlayer.onGround) {
         BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ);
         Block block = mc.theWorld.getBlockState(pos).getBlock();
         this.currentStandingBlock = block.isFullBlock() ? pos : this.currentStandingBlock;
         if (this.currentStandingBlock == null) {
            return;
         }

         if (this.wasAirborne) {
            this.timeHelper.reset();
         }

         BlockPos pos2 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ);
         this.nextWoolBlock = this.findNearbyWoolBlock(pos2);
         if (this.nextWoolBlock != null) {
            long l = 1000L;
            float f;
            if (this.timeHelper.reached2(l)) {
               double d = (double)this.nextWoolBlock.getX() + 0.5;
               double d2 = (double)this.nextWoolBlock.getY() + 0.5;
               double d3 = (double)this.nextWoolBlock.getZ() + 0.5;
               Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
               boolean flag = false;
               float f2 = 180.0F;
               float f3 = 180.0F;
               f = RotationUtil.getRotationsToPos(d, d2, d3, previousRotation, f3, f2, flag).yaw;
            } else {
               double d4 = (double)this.currentStandingBlock.getX() + 0.5;
               double d5 = (double)this.currentStandingBlock.getY() + 0.5;
               double d6 = (double)this.currentStandingBlock.getZ() + 0.5;
               Rotation previousRotation2 = ClientManagers.rotationController.getPreviousRotation();
               boolean flag2 = false;
               float f4 = 180.0F;
               float f5 = 180.0F;
               f = RotationUtil.getRotationsToPos(d4, d5, d6, previousRotation2, f5, f4, flag2).yaw;
            }

            byte b = 10;
            ClientManagers.rotationController.requestYaw(f, b, this);
         } else {
            RotationController rotationController = ClientManagers.rotationController;
            byte b2 = 10;
            float yaw = ClientManagers.rotationController.getPreviousRotation().yaw;
            rotationController.requestYaw(yaw, b2, this);
         }

         this.wasAirborne = false;
      } else {
         this.wasAirborne = true;
         RotationController rotationController2 = ClientManagers.rotationController;
         byte b3 = 10;
         float yaw2 = ClientManagers.rotationController.getPreviousRotation().yaw;
         rotationController2.requestYaw(yaw2, b3, this);
      }
   }

   public void onMoveInput3(EventMoveInput event) {
      if (mc.isNotInGui() && mc.thePlayer != null && this.nextWoolBlock != null && this.currentStandingBlock != null) {
         long l = 2000L;
         if (this.timeHelper.reached2(l)) {
            Block block = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock();
            double d = 1.29;
            double d2 = 0.29;
            double posX = mc.thePlayer.posX;
            double posZ = mc.thePlayer.posZ;
            posX += (mc.thePlayer.posX - mc.thePlayer.prevPosX) * 0.0;
            posZ += (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * 0.0;
            double d3 = (double)this.currentStandingBlock.getX() + d;
            double d4 = (double)this.currentStandingBlock.getX() - d2;
            double d5 = (double)this.currentStandingBlock.getZ() + d;
            double d6 = (double)this.currentStandingBlock.getZ() - d2;
            if (mc.thePlayer.getDistance((double)this.nextWoolBlock.getX(), (double)this.nextWoolBlock.getY(), (double)this.nextWoolBlock.getZ()) > 2.6 && this.isLicenseTimestampValid()) {
               if (posX > d3 || posX < d4 || posZ > d5 || posZ < d6) {
                  boolean flag = true;
                  event.setJump(flag);
               }
            } else if (!block.isFullBlock()) {
               boolean flag2 = true;
               event.setJump(flag2);
            }

            float f = 1.0F;
            event.setMoveForward(f);

            for (int i = 1; i < 4; i++) {
               BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - (double)i, mc.thePlayer.posZ);
               if (BlockUtil.isSolidBlock(pos) && pos.equalsPos(this.nextWoolBlock)) {
                  float f2 = -1.0F;
                  event.setMoveForward(f2);
                  if (mc.thePlayer.onGround) {
                     boolean flag3 = true;
                     event.setSneak(flag3);
                  }
               }
            }
         } else {
            long l2 = 1000L;
            if (!this.timeHelper.reached2(l2)) {
               float f3 = 1.0F;
               event.setMoveForward(f3);
               boolean flag4 = true;
               event.setSneak(flag4);
            }
         }
      }
   }

   private BlockPos findNearbyWoolBlock(BlockPos pos) {

      for (int i = -6; i <= 6; i++) {
         for (int j = 0; j <= 1; j++) {
            for (int k = -6; k <= 6; k++) {
               int n = pos.getX() + i;
               int n2 = pos.getY() + j;
               int n3 = pos.getZ() + k;
               if (BlockUtil.isSolidBlock((double)n, (double)n2, (double)n3)) {
                  Block block = mc.theWorld.getBlockState2(n, n2, n3).getBlock();
                  Block block2 = mc.theWorld.getBlockState2(n, n2 + 1, n3).getBlock();
                  if (block.equals(Blocks.wool) && !block2.isFullBlock()) {
                     return pos.add(i, j, k);
                  }
               }
            }
         }
      }

      return null;
   }

   private boolean isLicenseTimestampValid() {
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
