package net.augustus.modules.modes.noweb;

import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.modules.movement.NoWeb;
import net.augustus.settings.DoubleValue;

public class IntaveNoWebMode extends AbstractNoWebMode implements UpdateListener, MoveInputListener {
   public IntaveNoWebMode(NoWeb noWeb, DoubleValue doubleValue) {
      super(noWeb, doubleValue, "Intave");
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
      byte b = 21;
      this.toggleEventSubscription(b);
   }

   public void onUpdate2() {
      if (mc.thePlayer.isInWeb()) {
         long l = 600L;
         if (((NoWeb)this.parentModule).blockPlaceTimer.reached2(l)) {
            mc.thePlayer.onGround = false;
            if (mc.thePlayer.movementInput.moveStrafe == 0.0F) {
               if (mc.gameSettings.keyBindForward.isKeyDown()) {
                  mc.thePlayer.jumpMovementFactor = mc.thePlayer.isCollidedVertically ? 0.74F : 0.68F;
                  return;
               }
            }

            mc.thePlayer.jumpMovementFactor = 0.02F;
         }
      }
   }

   public void onMoveInput3(EventMoveInput event) {
      long l = 600L;
      if (((NoWeb)this.parentModule).blockPlaceTimer.reached2(l) && mc.thePlayer.isInWeb() && event.getMoveStrafe() == 0.0F && mc.gameSettings.keyBindForward.isKeyDown()) {
         float f = mc.thePlayer.ticksExisted % 5 == 0 ? 0.0F : 1.0F;
         event.setMoveForward(f);
      }
   }

}
