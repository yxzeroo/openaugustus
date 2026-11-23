package net.augustus.modules.combat;

import java.awt.Color;
import java.util.Comparator;
import java.util.OptionalInt;
import java.util.stream.Stream;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.ClickListener;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.EventClick;
import net.augustus.settings.options.TestModuleTargetOption;
import net.augustus.utils.combat.CombatTargetUtil;
import net.augustus.utils.rotation.RayTraceUtil;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.player.ContainerSlotQuery;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.utils.rotation.Rotation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Test extends Module implements TickListener, ClickListener, SilentSlotListener {
   private EntityLivingBase target;
   private BlockPos targetBlockPos;
   private BlockPos previousBlockPos;
   private boolean lavaPlaced;
   private int ticksSinceClick;

   public Test() {
       super("Test", "test", Color.black, Categorys.COMBAT);
      this.target = null;
      this.targetBlockPos = null;
      this.previousBlockPos = null;
      this.lavaPlaced = false;
      this.ticksSinceClick = 0;
   }

   @Override
   public void onEnable() {
      this.lavaPlaced = false;
      this.ticksSinceClick = 0;
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
      byte b3 = 34;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onTick() {
      EntityLivingBase target2 = this.target;
      this.target = null;
      this.target = this.findTarget();
      if (this.target != target2) {
         this.lavaPlaced = false;
         this.ticksSinceClick = 0;
      }

      if (this.target != null) {
         if (this.hasRequiredBucket()) {
            Vec3 rawServerPos = this.target.rawServerPos;
            this.previousBlockPos = this.targetBlockPos;
            this.targetBlockPos = this.lavaPlaced ? this.targetBlockPos : new BlockPos(rawServerPos.xCoord, rawServerPos.yCoord - 0.75, rawServerPos.zCoord);
            Block block = mc.theWorld.getBlockState(this.targetBlockPos).getBlock();
            if (block.getMaterial() != Material.air) {
               AxisAlignedBB box = new AxisAlignedBB(
                  (double)this.targetBlockPos.getX() + 0.05,
                  (double)this.targetBlockPos.getY() + block.getBlockBoundsMaxY(),
                  (double)this.targetBlockPos.getZ() + 0.05,
                  (double)this.targetBlockPos.getX() + 0.95,
                  (double)this.targetBlockPos.getY() + block.getBlockBoundsMaxY(),
                  (double)this.targetBlockPos.getZ() + 0.95
               );
               Vec3 nearestPointInBox = RotationUtil.getNearestPointInBox(box);
               double xCoord = nearestPointInBox.xCoord;
               double yCoord = nearestPointInBox.yCoord;
               double zCoord = nearestPointInBox.zCoord;
               Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
               boolean flag = false;
               float f = 180.0F;
               float f2 = 180.0F;
               Rotation rotationsToPos = RotationUtil.getRotationsToPos(xCoord, yCoord, zCoord, previousRotation, f2, f, flag);
               Rotation previousRotation2 = ClientManagers.rotationController.getPreviousRotation();
               rotationsToPos = RotationUtil.applyGcdFix(rotationsToPos, previousRotation2);
               rotationsToPos = this.findValidBlockRotation(rotationsToPos);
               if (rotationsToPos == null) {
                  return;
               }

               byte b = -10;
               ClientManagers.rotationController.requestRotation(rotationsToPos, b, this);
            }
         }
      }
   }

   @Override
   public void onSilentSlot() {
      if (this.target != null && ClientManagers.rotationController.isOwnedBy(this)) {
         ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
         byte b = 36;
         containerSlotQuery = containerSlotQuery.fromSlot(b);
         Item itemById = Item.getItemById(this.lavaPlaced ? 325 : 327);
         OptionalInt optionalInt = containerSlotQuery.filterItemExact(itemById).findFirstSlotIndex();
         if (optionalInt.isPresent()) {
            int n = optionalInt.getAsInt() - 36;
            byte b2 = -10;
            SilentSlotManager.requestSlot(this, b2, n);
         }
      }
   }

   @Override
   public void onClick(EventClick event) {
      if (this.target != null
         && ClientManagers.rotationController.isOwnedBy(this)
         && this.previousBlockPos != null
         && this.targetBlockPos != null
         && this.previousBlockPos.equalsPos(this.targetBlockPos)
         && mc.objectMouseOver != null
         && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
         && mc.objectMouseOver.getBlockPos().equals(this.targetBlockPos)
         && mc.previousObjectMouseOver != null
         && mc.previousObjectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
         && mc.previousObjectMouseOver.getBlockPos().equals(this.targetBlockPos)) {
         byte b = 1;
         event.addRightClicks(b);
         boolean flag = true;
         event.setRightMouseDown(flag);
         if (this.lavaPlaced) {
            this.toggle();
         }

         this.lavaPlaced = true;
         this.ticksSinceClick = 0;
      }

      this.ticksSinceClick++;
   }

   private Rotation findValidBlockRotation(Rotation rotation) {
      MovingObjectPosition hit = RayTraceUtil.rayTraceDefaultReach(1.0F, rotation.yaw, rotation.pitch);
      if (hit != null && hit.getBlockPos() != null && hit.getBlockPos().equalsPos(this.targetBlockPos)) {
         return rotation;
      } else {
         for (float i = (float)this.targetBlockPos.getX() + 0.03F; (double)i < (double)this.targetBlockPos.getX() + 0.97; i += 0.05F) {
            for (float j = (float)this.targetBlockPos.getZ() + 0.03F; (double)j < (double)this.targetBlockPos.getZ() + 0.97; j += 0.05F) {
               double d = (double)i;
               double d2 = (double)(this.targetBlockPos.getY() + 1);
               double d3 = (double)j;
               Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
               boolean flag = false;
               float f = 180.0F;
               float f2 = 180.0F;
               Rotation rotationsToPos = RotationUtil.getRotationsToPos(d, d2, d3, previousRotation, f2, f, flag);
               Rotation previousRotation2 = ClientManagers.rotationController.getPreviousRotation();
               rotationsToPos = RotationUtil.applyGcdFix(rotationsToPos, previousRotation2);
               hit = RayTraceUtil.rayTraceDefaultReach(1.0F, rotationsToPos.yaw, rotationsToPos.pitch);
               if (hit != null && hit.getBlockPos() != null && hit.getBlockPos().equals(this.targetBlockPos)) {
                  return rotationsToPos;
               }
            }
         }

         return null;
      }
   }

   private EntityLivingBase findTarget() {
      Stream stream = mc.theWorld.loadedEntityList.stream();
      return (EntityLivingBase)stream.filter(obj -> {
         if (obj instanceof EntityLivingBase && !(obj instanceof EntityPlayerSP) && mc.thePlayer != null && mc.thePlayer.getDistanceToEntity((Entity)obj) < 8.0F) {
            boolean isOptionSelected = TestModuleTargetOption.isOptionSelected(TestModuleTargetOption.PLAYER);
            boolean isOptionSelected2 = TestModuleTargetOption.isOptionSelected(TestModuleTargetOption.MOB);
            boolean isSelected = TestModuleTargetOption.ANIMAL.isSelected();
            boolean isOptionSelected3 = TestModuleTargetOption.isOptionSelected(TestModuleTargetOption.VILLAGER);
            boolean isOptionSelected4 = TestModuleTargetOption.isOptionSelected(TestModuleTargetOption.INVISIBLE);
            boolean isOptionSelected5 = TestModuleTargetOption.isOptionSelected(TestModuleTargetOption.DEAD);
            boolean flag = true;
            boolean flag2 = isOptionSelected5;
            boolean flag3 = true;
            boolean flag4 = true;
            boolean flag5 = true;
            boolean flag6 = isOptionSelected4;
            boolean flag7 = false;
            boolean flag8 = isOptionSelected3;
            boolean flag9 = isSelected;
            boolean flag10 = isOptionSelected2;
            boolean flag11 = isOptionSelected;
            if (CombatTargetUtil.isValidTarget((Entity)obj, flag11, flag10, flag9, flag8, flag7, flag6, flag5, flag4, flag3, flag2, flag)) {
               return true;
            }
         }

         return false;
      }).min(Comparator.comparingDouble(obj2 -> (double)mc.thePlayer.getDistanceToEntity((Entity)obj2))).orElse(null);
   }

   private boolean hasRequiredBucket() {
      ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
      byte b = 36;
      containerSlotQuery = containerSlotQuery.fromSlot(b);
      Item itemById = Item.getItemById(this.lavaPlaced ? 325 : 327);
      return containerSlotQuery.hasAnyOfItem(itemById);
   }

}
