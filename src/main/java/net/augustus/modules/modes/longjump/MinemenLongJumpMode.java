package net.augustus.modules.modes.longjump;

import net.augustus.ClientManagers;
import net.augustus.events.EventClick;
import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.SilentSlotListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.player.SilentSlotManager;
import net.augustus.utils.rotation.RotationController;
import net.augustus.utils.rotation.RotationUtil;
import java.util.function.BiFunction;
import net.augustus.modules.Module;
import net.augustus.modules.movement.LongJump;
import net.augustus.settings.DoubleValue;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;

public class MinemenLongJumpMode extends AbstractLongJumpMode implements ClickListener, UpdateListener, SilentSlotListener, TickListener, MoveInputListener {
   private boolean chargingBow = true;
   private int chargeTicks = 0;
   private int bowSlot = -1;

   public MinemenLongJumpMode(LongJump longJump, DoubleValue doubleValue) {
      super(longJump, doubleValue, "Minemen");
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
      byte b = 7;
      this.toggleEventSubscription(b);
      byte b2 = 40;
      this.toggleEventSubscription(b2);
      byte b3 = 43;
      this.toggleEventSubscription(b3);
      byte b4 = 34;
      this.toggleEventSubscription(b4);
   }

   @Override
   public void onEnable() {
      this.chargingBow = true;
      this.chargeTicks = 0;
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new MinemenLongJumpMode((LongJump)obj, (DoubleValue)obj2);
   }

   @Override
   public void onClick(EventClick event) {
      if (this.bowSlot != -1 && this.chargingBow) {
         this.chargeTicks++;
         if (this.chargeTicks < 4) {
            boolean flag = true;
            event.setRightMouseDown(flag);
         } else {
            this.chargingBow = false;
         }
      }

      if (!this.chargingBow) {
         this.chargeTicks++;
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      float f = 0.0F;
      event.setMoveForward(f);
      float f2 = 0.0F;
      event.setMoveStrafe(f2);
      if (!this.chargingBow && this.chargeTicks > 14) {
         float f3 = 1.0F;
         event.setMoveForward(f3);
      }
   }

   @Override
   public void onSilentSlot() {
      this.bowSlot = -1;
      if (this.chargingBow) {
         this.bowSlot = this.findBowHotbarSlot();
      }

      if (this.bowSlot >= 0) {
         int bowSlot2 = this.bowSlot;
         byte b = 7;
         SilentSlotManager.requestSlot(this.parentModule, b, bowSlot2);
      }
   }

   @Override
   public void onTick() {
      if (this.chargingBow) {
         RotationController rotationController = ClientManagers.rotationController;
         float f = RotationUtil.applyPitchGcd(-90.0F, ClientManagers.rotationController.getPreviousRotation().pitch);
         Module parentModule2 = this.parentModule;
         byte b = -10;
         float f2 = f;
         rotationController.requestPitch(f2, b, parentModule2);
      }
   }

   @Override
   public void onUpdate2() {
      if (mc.thePlayer.hurtTime != 0) {
         if (mc.thePlayer.onGround) {
            mc.thePlayer.motionY = 0.42F;
         }

         if (mc.thePlayer.hurtTime == 8) {
            mc.thePlayer.movementInput.rawMoveForward = mc.thePlayer.movementInput.moveForward;
            MoveUtil.setMoveSpeed(6.0);
         }

         if (mc.thePlayer.hurtTime == 1) {
            boolean flag = false;
            this.setEnabled(flag);
         }
      }
   }

   private int findBowHotbarSlot() {

      for (int i = 36; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
         ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
         if (stack != null && stack.getItem() instanceof ItemBow) {
            return i - 36;
         }
      }

      return -1;
   }

}
