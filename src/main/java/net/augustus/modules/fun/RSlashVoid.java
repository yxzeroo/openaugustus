package net.augustus.modules.fun;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.modules.fun.helper.GridCoord2I;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RSlashVoid extends Module implements TickListener, ClickListener, MoveInputListener {
   private final StringValue numberOfBlocksToReplace;
   private final RandomizedRangeSettings clickDelay;
   private final BooleanValue teleport;
   private final RandomizedRangeSettings tpDelay;
   private final RandomizedRangeSettings walkingPointUpdateDelay;
   private final BooleanValue leftClick;
   private final BooleanValue rightClick;
   private final BooleanValue lookDown;
   private Vec3 originPos;
   private Vec3 walkTargetPos;
   private int replaceStep;
   private List spiralCoords;
   private final TimeHelper teleportTimer;
   private final TimeHelper walkTargetTimer;
   private final TimeHelper clickTimer;

   public RSlashVoid() {
       super("RSlashVoid", "RSlashVoid", Color.black, Categorys.FUN);
      this.numberOfBlocksToReplace = new StringValue(this, "Number of blocks to replace", 1000.0, 0.0, 5000.0, 0);
      this.clickDelay = (new RandomizedRangeSettings(this, "ClickDelay", 1000.0, 2000.0, 0.0, 4000.0, 0)).withoutNotInARow();
      this.teleport = new BooleanValue(this, "Teleport", true);
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "TPDelay", 3000.0, 4000.0, 0.0, 10000.0, 0)).withoutNotInARow();
      BooleanValue teleport2 = this.teleport;
      this.teleport.getClass();
      BooleanValue booleanValue = teleport2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.tpDelay = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this, "WalkingPointUpdateDelay", 3000.0, 4000.0, 0.0, 10000.0, 0)).withoutNotInARow();
      SettingVisibilityCondition condition2 = () -> this.isWalkingModeEnabled();
      this.walkingPointUpdateDelay = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition2);
      this.leftClick = new BooleanValue(this, "LeftClick", true);
      this.rightClick = new BooleanValue(this, "RightClick", true);
      this.lookDown = new BooleanValue(this, "LookDown", true);
      this.originPos = null;
      this.walkTargetPos = null;
      this.replaceStep = 0;
      this.spiralCoords = new ArrayList();
      this.teleportTimer = new TimeHelper();
      this.walkTargetTimer = new TimeHelper();
      this.clickTimer = new TimeHelper();
   }

   @Override
   public void onEnable() {
      if (mc.theWorld != null && mc.thePlayer != null) {
         String str = "How to grief r/place on gomme:";
         this.sendChatMessage2(str);
         String str2 = "1. Get a coal block into your hotbar";
         this.sendChatMessage2(str2);
         String str3 = "2. Select the coal block";
         this.sendChatMessage2(str3);
         String str4 = "3. Enable autoplay to tab out and chill";
         this.sendChatMessage2(str4);
         this.originPos = mc.thePlayer.getInterpolatedPos(1.0F);
         this.spiralCoords = generateSpiralCoords((int)this.originPos.xCoord, (int)this.originPos.zCoord, (int)this.numberOfBlocksToReplace.getValue());
         this.replaceStep = 0;
      }

      this.walkTargetPos = null;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
      byte b2 = 7;
      this.toggleEventSubscription(b2);
      byte b3 = 21;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onTick() {
      if (!this.isInactiveOrMissingCoalBlock()) {
         if (this.lookDown.getValue()) {
            byte b = -19;
            float f = 90.0F;
            ClientManagers.rotationController.requestPitch(f, b, this);
         }

         if (this.replaceStep == 0 || !this.teleport.getValue()) {
            TimeHelper teleportTimer2 = this.teleportTimer;
            long lastIntValue = (long)this.tpDelay.getLastIntValue();
            if (teleportTimer2.reached2(lastIntValue) || !this.teleport.getValue()) {
               this.tpDelay.nextValue();
               this.teleportTimer.reset();

               for (GridCoord2I gridCoord2I : (Iterable<GridCoord2I>)(this.spiralCoords)) {
                  Block block = mc.theWorld.getBlockState((double)gridCoord2I.x, mc.thePlayer.lastGroundY - 1.0, (double)gridCoord2I.z).getBlock();
                  if (!block.equals(Blocks.coal_block)) {
                     double d = (double)gridCoord2I.x + 0.5;
                     double d2 = (double)gridCoord2I.z + 0.5;
                     if (this.teleport.getValue()) {
                        double d3 = MathHelper.clamp_double(mc.thePlayer.posX - d, -5.0, 5.0);
                        double d4 = MathHelper.clamp_double(mc.thePlayer.posZ - d2, -5.0, 5.0);
                        mc.thePlayer.setPosition(mc.thePlayer.posX - d3, this.originPos.yCoord + 0.2, mc.thePlayer.posZ - d4);
                     } else {
                        teleportTimer2 = this.walkTargetTimer;
                        long lastIntValue2 = (long)this.walkingPointUpdateDelay.getLastIntValue();
                        if (teleportTimer2.reached2(lastIntValue2)) {
                           this.walkTargetPos = new Vec3(d, mc.thePlayer.posY, d2);
                           this.walkTargetTimer.reset();
                           this.walkingPointUpdateDelay.nextValue();
                        }

                        double xCoord = this.walkTargetPos.xCoord;
                        double d5 = this.walkTargetPos.yCoord + (double)mc.thePlayer.getEyeHeight();
                        double zCoord = this.walkTargetPos.zCoord;
                        Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
                        float f2 = 30.0F;
                        float f3 = 20.0F;
                        float f4 = DefaultRandomGenerator.instance.nextFloat(f3, f2);
                        float f5 = 15.0F;
                        float f6 = 10.0F;
                        float f7 = DefaultRandomGenerator.instance.nextFloat(f6, f5);
                        boolean flag = false;
                        float f8 = f7;
                        float f9 = f4;
                        Rotation rotationsToPos = RotationUtil.getRotationsToPos(xCoord, d5, zCoord, previousRotation, f9, f8, flag);
                        rotationsToPos = new Rotation(rotationsToPos.yaw, this.lookDown.getValue() && mc.thePlayer.getDistanceToVec(this.walkTargetPos) < 3.0 ? 90.0F : rotationsToPos.pitch);
                        if (Math.abs(rotationsToPos.yaw - ClientManagers.rotationController.getPreviousRotation().yaw) > 2.0F || Math.abs(rotationsToPos.pitch - ClientManagers.rotationController.getPreviousRotation().pitch) > 2.0F) {
                           float f10 = 1.0F;
                           float f11 = -1.0F;
                           rotationsToPos.yaw = rotationsToPos.yaw + DefaultRandomGenerator.instance.nextFloat(f11, f10);
                           float f12 = 1.0F;
                           float f13 = -1.0F;
                           rotationsToPos.pitch = rotationsToPos.pitch + DefaultRandomGenerator.instance.nextFloat(f13, f12);
                        }

                        float f14 = 20.0F;
                        float f15 = 10.0F;
                        Rotation rotation = new Rotation(
                           rotationsToPos.yaw, RotationUtil.updatePitchWithGcd(ClientManagers.rotationController.getPreviousRotation().pitch, rotationsToPos.pitch, DefaultRandomGenerator.instance.nextFloat(f15, f14))
                        );
                        Rotation previousRotation2 = ClientManagers.rotationController.getPreviousRotation();
                        rotationsToPos = RotationUtil.applyGcdFix(rotation, previousRotation2);
                        byte b2 = -20;
                        ClientManagers.rotationController.requestRotation(rotationsToPos, b2, this);
                     }

                     this.replaceStep = 1;
                     break;
                  }
               }
            }
         }
      }
   }

   @Override
   public void onClick(EventClick event) {
      event.cancel();
      if (!this.isInactiveOrMissingCoalBlock()) {
         if (this.replaceStep == 1 && this.teleport.getValue()) {
            this.replaceStep = 2;
         } else {
            TimeHelper clickTimer2 = this.clickTimer;
            long lastIntValue = (long)this.clickDelay.getLastIntValue();
            if (clickTimer2.reached2(lastIntValue)) {
               BlockPos pos = new BlockPos(mc.thePlayer.posX, this.originPos.yCoord - 0.7, mc.thePlayer.posZ);
               if (this.replaceStep < 2 && this.teleport.getValue()
                  || mc.theWorld.getBlockState(pos).getBlock().equals(Blocks.coal_block)
                  || this.lookDown.getValue() && !(ClientManagers.rotationController.getPreviousRotation().pitch > 80.0F)) {
                  this.replaceStep++;
                  if (this.replaceStep > 4) {
                     this.replaceStep = 0;
                  }
               } else {
                  this.clickDelay.nextValue();
                  this.clickTimer.reset();
                  if (this.leftClick.getValue()) {
                     mc.thePlayer.swingItem();
                     if (mc.theWorld.getBlockState(pos).getBlock().getMaterial() != Material.air) {
                        mc.playerController.clickBlock(pos, EnumFacing.UP);
                     }
                  }

                  if (this.rightClick.getValue()) {
                     if (mc.theWorld.getBlockState(pos).getBlock().getMaterial() == Material.air) {
                        pos = pos.down();
                     }

                     if (mc.playerController
                        .onPlayerRightClick(
                           mc.thePlayer,
                           mc.theWorld,
                           mc.thePlayer.getCurrentEquippedItem(),
                           pos,
                           EnumFacing.UP,
                           new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5)
                        )) {
                        mc.thePlayer.swingItem();
                     }
                  }

                  this.replaceStep = 0;
               }
            }
         }
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (!this.isInactiveOrMissingCoalBlock() && !this.teleport.getValue() && this.walkTargetPos != null) {
         double distance = mc.thePlayer.getDistance(this.walkTargetPos.xCoord, this.walkTargetPos.yCoord, this.walkTargetPos.zCoord);
         if (distance > 0.2) {
            float f = 1.0F;
            event.setMoveForward(f);
            if (distance > 6.0) {
               boolean flag = true;
               event.setJump(flag);
            }

            if (distance < 1.5) {
               boolean flag2 = true;
               event.setSneak(flag2);
            }
         } else {
            boolean flag3 = true;
            event.setSneak(flag3);
         }
      }
   }

   private boolean isInactiveOrMissingCoalBlock() {
      return this.originPos == null
         || this.spiralCoords == null
         || mc.currentScreen != null
         || mc.thePlayer.getCurrentEquippedItem() == null
         || !mc.thePlayer.getCurrentEquippedItem().getItem().equals(Item.getItemById(173));
   }

   public static List generateSpiralCoords(int n, int n2, int n3) {
      ArrayList list = new ArrayList();
      int n4 = n;
      int n5 = n2;
      int n6 = 1;
      list.add(new GridCoord2I(n, n2));

      for (int i = 0; i < n3; i++) {
         for (int j = 0; j < n6 && i < n3 - 1; j++) {
            list.add(new GridCoord2I(n4, ++n5));
            i++;
         }

         for (int k = 0; k < n6 && i < n3 - 1; k++) {
            list.add(new GridCoord2I(++n4, n5));
            i++;
         }

         n6++;

         for (int m = 0; m < n6 && i < n3 - 1; m++) {
            list.add(new GridCoord2I(n4, --n5));
            i++;
         }

         for (int idx = 0; idx < n6 && i < n3 - 1; idx++) {
            list.add(new GridCoord2I(--n4, n5));
            i++;
         }

         n6++;
      }

      return list;
   }

   private boolean isWalkingModeEnabled() {
      return !this.teleport.getValue();
   }

}
