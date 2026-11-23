package net.augustus.modules.modes.jesus;

import net.augustus.events.listeners.UpdateListener;
import net.augustus.utils.MoveUtil;
import net.augustus.modules.movement.Jesus;
import net.augustus.settings.DoubleValue;

public class IntaveJesusMode extends AbstractJesusMode implements UpdateListener {
   public IntaveJesusMode(Jesus jesus, DoubleValue doubleValue) {
      super(jesus, doubleValue, "Intave");
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 43;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public void onUpdate2() {
      int[] controlFlowState22 = AbstractJesusMode.getControlFlowState2();
      if (this.shouldApplyJesus() && mc.thePlayer.isInWater() && MoveUtil.isMoving() && mc.thePlayer.ticksExisted % 3 == 0 && mc.thePlayer.hurtTime < 7) {
         MoveUtil.setSpeed2(0.26F);
      }
   }

}
