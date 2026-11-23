package net.augustus.events;

import net.augustus.events.listeners.AIMoveSpeedListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventAIMoveSpeed extends AbstractEvent {
   public static final int ID = 0;
   private float moveSpeed;

   public float getMoveSpeed() {
      return this.moveSpeed;
   }

   public void call2(AIMoveSpeedListener listener) {
      listener.onAIMoveSpeed(this);
   }

   public static void reset(EventAIMoveSpeed event, float f) {
      event.setMoveSpeed2(f);
   }

   private void setMoveSpeed2(float f) {
      this.moveSpeed = f;
   }

   @Override
   public void call(Object obj) {
      AIMoveSpeedListener listener = (AIMoveSpeedListener)obj;
      AIMoveSpeedListener listener2 = listener;
      this.call2(listener2);
   }

   public void setMoveSpeed(float f) {
      this.moveSpeed = f;
   }
}
