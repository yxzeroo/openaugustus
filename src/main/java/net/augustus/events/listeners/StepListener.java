package net.augustus.events.listeners;

import net.augustus.events.EventManager;
import net.augustus.events.EventStep;
import de.florianmichael.dietrichevents2.StateTypes;

public interface StepListener {
   EventStep STEP_EVENT = new EventStep();
   long U = 71329453388836L;

   static EventStep onStep(float f, StateTypes stateTypes) {
      EventStep.reset(STEP_EVENT, f, stateTypes);
      EventManager.getEventBus().post(37, STEP_EVENT);
      return STEP_EVENT;
   }

   void onStep();

   static void onStep2(StateTypes stateTypes) {
      float f = 0.0F;
      EventStep.reset(STEP_EVENT, f, stateTypes);
      EventManager.getEventBus().post(37, STEP_EVENT);
   }

   void onStep(EventStep event);
}
