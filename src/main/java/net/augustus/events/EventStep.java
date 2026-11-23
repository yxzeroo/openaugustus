package net.augustus.events;

import net.augustus.events.listeners.StepListener;
import de.florianmichael.dietrichevents2.AbstractEvent;
import de.florianmichael.dietrichevents2.StateTypes;

public class EventStep extends AbstractEvent {
   private float stepHeight;
   private StateTypes state;
   public static final int ID = 37;

   public static void reset(EventStep event, float f, StateTypes stateTypes) {
      event.reset(f, stateTypes);
   }

   public void call2(StepListener listener) {
      if (this.state == StateTypes.PRE) {
         listener.onStep(this);
      } else {
         listener.onStep();
      }
   }

   public void call(Object obj) {
      this.call2((StepListener)obj);
   }

   private void reset(float f, StateTypes stateTypes) {
      this.stepHeight = f;
      this.state = stateTypes;
   }

   public float getStepHeight() {
      return this.stepHeight;
   }

   public void setStepHeight(float f) {
      this.stepHeight = f;
   }
}
