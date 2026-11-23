package net.augustus.modules.modes.noweb;

import net.augustus.events.EventMoveInput;
import net.augustus.events.EventWebSlowDown;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.events.listeners.WebSlowDownListener;
import net.augustus.utils.AntiSamplerGuard;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.TickCounter;
import net.augustus.modules.movement.NoWeb;
import net.augustus.settings.DoubleValue;

public class Polar1NoWebMode extends AbstractNoWebMode implements UpdateListener, WebSlowDownListener, MoveInputListener {
   private final TickCounter tickCounter = new TickCounter();

   public Polar1NoWebMode(NoWeb noWeb, DoubleValue doubleValue) {
      super(noWeb, doubleValue, "Polar1");
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
      byte b = 49;
      this.toggleEventSubscription(b);
      byte b2 = 21;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onDisable() {
   }

   public void onUpdate2() {
      this.tickCounter.increment();
      if (!mc.thePlayer.isInWeb()) {
         this.tickCounter.reset();
      } else {
         if (mc.thePlayer.onGround) {
            byte b = 2;
            if (this.tickCounter.hasReached2(b) && mc.gameSettings.keyBindForward.isKeyDown()) {
               if (!AntiSamplerGuard.isGuardActive(1)) {
                  mc.thePlayer.motionY = 1.0E-5;
               }

               MoveUtil.addMotion(0.34, false);
            }
         }
      }
   }

   public void onWebSlowDown(EventWebSlowDown event) {
      byte b = 2;
      if (this.tickCounter.hasReached2(b) && !mc.thePlayer.onGround) {
         if (mc.thePlayer.airTicks > 1) {
            if (!AntiSamplerGuard.isGuardActive(1)) {
               double d = 1.0;
               event.setHorizontalFactor(d);
            }
         } else {
            double d2 = 0.45;
            event.setHorizontalFactor(d2);
         }
      }
   }

   public void onMoveInput3(EventMoveInput event) {
      if (mc.thePlayer.isInWeb()) {
         byte b = 2;
         if (this.tickCounter.hasReached2(b)) {
            return;
         }
      }
   }

}
