package net.augustus.events.listeners;

import net.augustus.events.EventManager;
import net.augustus.events.EventSafeWalk;
import de.florianmichael.dietrichevents2.StateTypes;

public interface SafeWalkListener {
   EventSafeWalk SAFE_WALK_EVENT = new EventSafeWalk();
   long Q = 65656069758599L;

   void onSafeWalk(EventSafeWalk event);

   static EventSafeWalk onSafeWalk(StateTypes stateTypes, boolean flag) {
      EventSafeWalk.reset(SAFE_WALK_EVENT, stateTypes, flag);
      EventManager.getEventBus().post(33, SAFE_WALK_EVENT);
      return SAFE_WALK_EVENT;
   }

   default void onSafeWalk2(EventSafeWalk event) {
   }
}
