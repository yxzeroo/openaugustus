package net.augustus.events;

import net.augustus.events.listeners.AsyncTickListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventAsyncTick extends AbstractEvent {
   public static final int ID = 61;

   public void call2(AsyncTickListener listener) {
      listener.onAsyncTick();
   }

   @Override
   public void call(Object obj) {
      this.call2((AsyncTickListener)obj);
   }
}
