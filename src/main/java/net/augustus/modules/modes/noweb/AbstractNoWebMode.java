package net.augustus.modules.modes.noweb;

import net.augustus.events.EventUpdate;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.modules.modes.AbstractSubMode;
import net.augustus.modules.movement.NoWeb;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.TimeHelper;

public abstract class AbstractNoWebMode extends AbstractSubMode implements UpdateListener {
   public boolean wasInWeb = false;
   public final TimeHelper timeHelper = new TimeHelper();

   public AbstractNoWebMode(NoWeb noWeb, DoubleValue doubleValue, String str) {
      super(noWeb, doubleValue, str);
   }

   @Override
   public void onEnable() {
      this.wasInWeb = false;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 43;
      this.toggleEventSubscription(b);
   }

   public void onUpdate(EventUpdate event) {
      if (!mc.thePlayer.isInWeb()) {
         this.timeHelper.reset();
      }

      if (mc.thePlayer.isInWeb() && ((NoWeb)this.parentModule).upDown.getValue()) {
         long l = 200L;
         if (this.timeHelper.reached2(l)) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
               mc.thePlayer.motionY = ((NoWeb)this.parentModule).upSpeed.getValue();
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
               mc.thePlayer.motionY = -((NoWeb)this.parentModule).downSpeed.getValue();
            }
         }
      }

      if (!mc.thePlayer.isInWeb() && this.wasInWeb) {
         if ((double)mc.thePlayer.getSpeedInAir() != 0.2) {
            mc.thePlayer.setSpeedInAir(0.02F);
         }

         if ((double)mc.thePlayer.jumpMovementFactor > 0.03) {
            mc.thePlayer.jumpMovementFactor = 0.02F;
         }
      }

      this.wasInWeb = mc.thePlayer.isInWeb();
   }



}
