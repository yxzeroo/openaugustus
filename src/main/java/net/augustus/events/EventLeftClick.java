package net.augustus.events;

import net.augustus.events.listeners.LeftClickListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventLeftClick extends AbstractEvent {
   public static final int ID = 53;

   public void call2(LeftClickListener listener) {
      listener.onLeftClick(this);
   }

   @Override
   public void call(Object obj) {
      this.call2((LeftClickListener)obj);
   }
}
