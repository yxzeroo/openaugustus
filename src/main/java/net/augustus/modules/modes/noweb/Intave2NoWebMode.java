package net.augustus.modules.modes.noweb;

import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.utils.MoveUtil;
import net.augustus.modules.movement.NoWeb;
import net.augustus.settings.DoubleValue;

public class Intave2NoWebMode extends AbstractNoWebMode implements UpdateListener, MoveInputListener {
   public Intave2NoWebMode(NoWeb noWeb, DoubleValue doubleValue) {
      super(noWeb, doubleValue, "Intave2");
   }

   @Override
   public void onEnable() {
      super.onEnable();
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
      byte b = 21;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onDisable() {
   }

   public void onUpdate2() {
      if (mc.thePlayer.isInWeb() && MoveUtil.isMoving()) {
         long l = 600L;
         if (((NoWeb)this.parentModule).blockPlaceTimer.reached2(l) && mc.thePlayer.ticksExisted % this.getSlowTickInterval() != 0) {
            MoveUtil.setSpeed2(mc.thePlayer.isSprinting() ? 0.7F : 0.51F);
         }
      }
   }

   public void onMoveInput3(EventMoveInput event) {
      long l = 600L;
      if (((NoWeb)this.parentModule).blockPlaceTimer.reached2(l) && mc.thePlayer.isInWeb() && event.getMoveStrafe() == 0.0F && mc.gameSettings.keyBindForward.isKeyDown()) {
         float f = mc.thePlayer.ticksExisted % this.getSlowTickInterval() == 0 ? 0.0F : 1.0F;
         event.setMoveForward(f);
      }
   }

   private int getSlowTickInterval() {
      return mc.thePlayer.isSprinting() ? 5 : 7;
   }

}
