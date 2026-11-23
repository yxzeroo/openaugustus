package net.augustus.events;

import net.augustus.events.listeners.RayCastListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventRayCast extends AbstractEvent {
   public static final int ID = 29;

   public void call2(RayCastListener listener) {
      listener.onRayCast();
   }

   @Override
   public void call(Object obj) {
      RayCastListener listener = (RayCastListener)obj;
      this.call2(listener);
   }
}
