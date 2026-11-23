package net.augustus.events;

import net.augustus.events.listeners.UpdateListener;
import de.florianmichael.dietrichevents2.CancellableEvent;
import de.florianmichael.dietrichevents2.StateTypes;

public class EventUpdate extends CancellableEvent {
   public StateTypes state;
   public static final int ID = 43;

   private void reset(StateTypes stateTypes) {
      this.state = stateTypes;
      this.setCancelled(false);
   }

   public void call2(UpdateListener listener) {
      if (this.state == StateTypes.PRE) {
         listener.onUpdate2();
         listener.onUpdate(this);
      } else {
         listener.onUpdate();
         listener.onUpdate2(this);
      }
   }

   public static void reset(EventUpdate event, StateTypes stateTypes) {
      event.reset(stateTypes);
   }

   public void call(Object obj) {
      UpdateListener listener = (UpdateListener)obj;
      this.call2(listener);
   }
}
