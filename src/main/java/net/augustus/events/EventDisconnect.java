package net.augustus.events;

import net.augustus.events.listeners.DisconnectListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventDisconnect extends AbstractEvent {
   public static final int ID = 62;

   public void call2(DisconnectListener listener) {
      listener.onDisconnect2();
   }

   @Override
   public void call(Object obj) {
      DisconnectListener listener = (DisconnectListener)obj;
      this.call2(listener);
   }
}
