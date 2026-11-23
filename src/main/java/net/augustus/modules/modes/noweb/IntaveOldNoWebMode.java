package net.augustus.modules.modes.noweb;

import net.augustus.events.EventMoveInput;
import net.augustus.events.EventOrientCamera;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.OrientCameraListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.modules.movement.NoWeb;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;

public class IntaveOldNoWebMode extends AbstractNoWebMode implements UpdateListener, MoveInputListener, OrientCameraListener {
   public final BooleanValue intaveOldHideSneakAnimation;

   public IntaveOldNoWebMode(NoWeb noWeb, DoubleValue doubleValue) {
      super(noWeb, doubleValue, "IntaveOld");
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "IntaveOldHideSneakAnimation", true, this.modeSetting);
      String str = "HideSneakAnimation";
      booleanValue = (BooleanValue)booleanValue.withDisplayName(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.intaveOldHideSneakAnimation = (BooleanValue)booleanValue.withVisibility(condition);
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
      byte b = 21;
      this.toggleEventSubscription(b);
      byte b2 = 24;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onUpdate2() {
      if (mc.thePlayer.isInWeb()) {
         mc.thePlayer.onGround = false;
         if (mc.thePlayer.isCollidedVertically) {
            if (mc.thePlayer.movementInput.moveStrafe == 0.0F) {
               if (mc.gameSettings.keyBindForward.isKeyDown()) {
                  mc.thePlayer.jumpMovementFactor = 0.74F;
                  return;
               }
            }

            mc.thePlayer.jumpMovementFactor = 0.2F;
            mc.thePlayer.onGround = true;
         } else {
            mc.thePlayer.jumpMovementFactor = 1.0F;
         }
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (mc.thePlayer.isCollidedVertically) {
         if (mc.thePlayer.isInWeb() && event.getMoveStrafe() == 0.0F && mc.gameSettings.keyBindForward.isKeyDown()) {
            float f = mc.thePlayer.ticksExisted % 2 == 0 ? 0.0F : 1.0F;
            event.setMoveForward(f);
         }
      } else if (mc.thePlayer.isInWeb()) {
         if (mc.thePlayer.isSprinting()) {
            float f2 = 0.0F;
            event.setMoveForward(f2);
         }

         boolean flag = true;
         event.setSneak(flag);
      }
   }

   @Override
   public void onOrientCamera(EventOrientCamera event) {
      if (mc.thePlayer.isInWeb() && this.intaveOldHideSneakAnimation.getValue()) {
         float f = 1.62F;
         event.setEyeHeight(f);
      }
   }

}
