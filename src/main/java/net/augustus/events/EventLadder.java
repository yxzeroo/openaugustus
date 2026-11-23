package net.augustus.events;

import net.augustus.events.listeners.LadderListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventLadder extends AbstractEvent {
   public static final int ID = 15;
   private double motionY;

   public static void reset(EventLadder event, double d) {
      event.setMotionY2(d);
   }

   public void call2(LadderListener listener) {
      listener.onLadder(this);
   }

   public double getMotionY() {
      return this.motionY;
   }

   public void setMotionY(double d) {
      this.motionY = d;
   }

   private void setMotionY2(double d) {
      this.motionY = d;
   }

   @Override
   public void call(Object obj) {
      this.call2((LadderListener)obj);
   }
}
