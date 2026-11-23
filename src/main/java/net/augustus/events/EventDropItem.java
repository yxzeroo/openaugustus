package net.augustus.events;

import net.augustus.events.listeners.DropItemListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventDropItem extends AbstractEvent {
   public static final int ID = 9;

   @Override
   public void call(Object obj) {
      this.call2((DropItemListener)obj);
   }

   public void call2(DropItemListener listener) {
      listener.onDropItem2();
   }
}
