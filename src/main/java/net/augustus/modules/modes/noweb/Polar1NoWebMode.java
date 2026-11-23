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
import net.augustus.settings.SettingGroup;

public class Polar1NoWebMode extends AbstractNoWebMode implements UpdateListener, WebSlowDownListener, MoveInputListener {
   private final TickCounter tickCounter = new TickCounter();

   public Polar1NoWebMode(NoWeb noWeb, SettingGroup doubleValue) {
      super(noWeb, doubleValue, "Polar1");
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
      this.toggleEventSubscription((byte)49);
      this.toggleEventSubscription((byte)21);
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
            if (this.tickCounter.hasReached2((byte)2) && mc.gameSettings.keyBindForward.isKeyDown()) {
               if (!AntiSamplerGuard.isGuardActive(1)) {
                  mc.thePlayer.motionY = 1.0E-5;
               }

               MoveUtil.addMotion(0.34, false);
            }
         }
      }
   }

   public void onWebSlowDown(EventWebSlowDown event) {
      if (this.tickCounter.hasReached2((byte)2) && !mc.thePlayer.onGround) {
         if (mc.thePlayer.airTicks > 1) {
            if (!AntiSamplerGuard.isGuardActive(1)) {
               event.setHorizontalFactor(1.0);
            }
         } else {
            event.setHorizontalFactor(0.45);
         }
      }
   }

   public void onMoveInput3(EventMoveInput event) {
      if (mc.thePlayer.isInWeb()) {
         if (this.tickCounter.hasReached2((byte)2)) {
            return;
         }
      }
   }

}
