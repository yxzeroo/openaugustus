package net.augustus.modules.modes.fly;

import net.augustus.Augustus;
import net.augustus.events.EventClick;
import net.augustus.events.EventMotion;
import net.augustus.events.EventMoveEntity;
import net.augustus.events.EventMoveInput;
import net.augustus.utils.player.ContainerSlotQuery;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.utils.rotation.RotationController;
import net.augustus.utils.world.BlockUtil;
import java.util.OptionalInt;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import net.augustus.modules.movement.Fly;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class AirPlaceFlyMode extends FlyMode {
   private int targetYLevel = 0;
   private int verticalStepDir = 0;

   public AirPlaceFlyMode(String str, Fly fly2) {
      super(str, fly2);
   }

   @Override
   public void onEnable() {
      this.targetYLevel = (int)mc.thePlayer.lastGroundY;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onPreUpdate() {
   }

   @Override
   public void onMoveInput(EventMoveInput event) {
      if (mc.thePlayer.airTicks == 1) {
         this.targetYLevel = this.targetYLevel + this.verticalStepDir;
         System.out.println(this.verticalStepDir);
      }

      this.verticalStepDir = 0;
      if (event.isJump()) {
         this.verticalStepDir = 1;
      } else if (event.isSneak()) {
         boolean flag = false;
         event.setSneak(flag);
         this.verticalStepDir = -1;
      }

      boolean flag2 = true;
      event.setJump(flag2);
   }

   @Override
   public void onClick(EventClick event) {
      boolean flag = true;
      event.setBlockAllClicks(flag);
      boolean flag2 = true;
      event.setIgnoreVanillaClicks(flag2);
      if (mc.thePlayer.posY - 0.5 < (double)this.targetYLevel && !mc.thePlayer.onGround && mc.thePlayer.motionY < 0.0) {
         double d = mc.thePlayer.posX + mc.thePlayer.lastMotionX * 2.0;
         double d2 = mc.thePlayer.posZ + mc.thePlayer.lastMotionZ * 2.0;
         BlockPos pos = new BlockPos(d, (double)(this.targetYLevel - 1), d2);
         if (mc.theWorld.getBlockState(pos).getBlock().getMaterial() == Material.air
            && mc.thePlayer.getCurrentEquippedItem() != null
            && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock
            && mc.playerController
               .onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), pos, EnumFacing.UP, new Vec3(d, (double)pos.getY(), d2))
            )
          {
            mc.thePlayer.swingItem();
         }
      }
   }

   @Override
   public void onEarlyTick() {
      RotationController rotationController = Augustus.getInstance().getRotationController();
      Fly fly2 = Augustus.getInstance().getModuleManager().fly;
      byte b = -100;
      float f = 90.0F;
      rotationController.requestPitch(f, b, fly2);
   }

   @Override
   public void onMotion(EventMotion event) {
   }

   @Override
   public void onMoveEntity(EventMoveEntity event) {
   }

   @Override
   public void onSilentSlot() {
      int n = this.findBlockHotbarSlot();
      if (n != -1) {
         short s = -1000;
         SilentSlotManager.requestSlot(Augustus.getInstance().getModuleManager().fly, s, n);
      }
   }

   private int findBlockHotbarSlot() {
      ContainerSlotQuery containerSlotQuery = ContainerSlotQuery.of(mc.thePlayer.inventoryContainer);
      byte b = 36;
      containerSlotQuery = containerSlotQuery.fromSlot(b);
      Predicate predicate = obj -> obj instanceof ItemBlock;
      containerSlotQuery = containerSlotQuery.filterItem(predicate);
      BiPredicate biPredicate = (obj2, obj3) -> isValidPlaceableBlock((ItemStack)obj2, (Integer)obj3);
      OptionalInt optionalInt = containerSlotQuery.filterStackAndSlot(biPredicate).findFirstSlotIndex();
      return optionalInt.isPresent() ? optionalInt.getAsInt() - 36 : -1;
   }

   private static boolean isValidPlaceableBlock(ItemStack stack, Integer num) {
      return stack != null && stack.stackSize > 0 && BlockUtil.isValidScaffoldBlock(stack);
   }
}
