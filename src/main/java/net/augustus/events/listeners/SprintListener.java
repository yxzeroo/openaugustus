package net.augustus.events.listeners;


import net.augustus.events.EventManager;
import net.augustus.events.EventSprint;
import net.augustus.events.SprintEventType;
public interface SprintListener {
   EventSprint SPRINT_EVENT = new EventSprint();
   long T = 105136440240298L;

   default void onSprint(EventSprint event) {
   }

   static EventSprint onSprint4(SprintEventType sprintEventType) {
      EventSprint.reset(SPRINT_EVENT, sprintEventType);
      EventManager.getEventBus().post(36, SPRINT_EVENT);
      return SPRINT_EVENT;
   }

   default void onSprint2(EventSprint event) {
   }

   default void onSprint3(EventSprint event) {
   }
}
