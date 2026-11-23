package net.augustus.events.listeners;

import net.augustus.events.EventManager;
import net.augustus.events.EventUpdate;
import de.florianmichael.dietrichevents2.StateTypes;

public interface UpdateListener {
   EventUpdate UPDATE_EVENT = new EventUpdate();

   default void onUpdate(EventUpdate event) {
   }

   default void onUpdate() {
   }

   default void onUpdate2(EventUpdate event) {
   }

   static EventUpdate onUpdate3(StateTypes stateTypes) {
      EventUpdate.reset(UPDATE_EVENT, stateTypes);
      EventManager.getEventBus().post(43, UPDATE_EVENT);
      return UPDATE_EVENT;
   }

   default void onUpdate2() {
   }
}
