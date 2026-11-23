package net.augustus.modules.modes.longjump;

import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.modules.modes.AbstractSubMode;
import net.augustus.modules.movement.LongJump;
import net.augustus.settings.DoubleValue;

public abstract class AbstractLongJumpMode extends AbstractSubMode implements MoveInputListener {

   public AbstractLongJumpMode(LongJump longJump, DoubleValue doubleValue, String str) {
      super(longJump, doubleValue, str);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 21;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      float f = 1.0F;
      event.setMoveForward(f);
   }



}
