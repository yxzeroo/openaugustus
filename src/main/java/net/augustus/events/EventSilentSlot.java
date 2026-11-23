package net.augustus.events;

import net.augustus.events.listeners.SilentSlotListener;
import de.florianmichael.dietrichevents2.AbstractEvent;
import de.florianmichael.dietrichevents2.StateTypes;

public class EventSilentSlot extends AbstractEvent {
   private StateTypes state;
   public static final int ID = 34;

   public static StateTypes setState(EventSilentSlot event, StateTypes stateTypes) {
      return event.state = stateTypes;
   }

   public void call2(SilentSlotListener listener) {
      if (this.state == StateTypes.PRE) {
         listener.onSilentSlot2();
      } else {
         listener.onSilentSlot();
      }
   }

   public void call(Object obj) {
      SilentSlotListener listener = (SilentSlotListener)obj;
      this.call2(listener);
   }
}
