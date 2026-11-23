package net.augustus.modules.modes.jesus;

import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.modules.movement.Jesus;
import net.augustus.settings.DoubleValue;

public class DolphinJesusMode extends AbstractJesusMode implements MoveInputListener {
   public DolphinJesusMode(Jesus jesus, DoubleValue doubleValue) {
      super(jesus, doubleValue, "Dolphin");
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 21;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public void onMoveInput3(EventMoveInput event) {
      int[] controlFlowState22 = AbstractJesusMode.getControlFlowState2();
      if ((mc.thePlayer.isInWater() || mc.thePlayer.isInLava()) && !mc.gameSettings.keyBindSneak.isKeyDown()) {
         boolean flag = true;
         event.setJump(flag);
      }
   }

}
