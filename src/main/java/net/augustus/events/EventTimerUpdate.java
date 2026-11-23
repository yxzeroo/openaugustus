package net.augustus.events;

import net.augustus.events.listeners.TimerUpdateListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventTimerUpdate extends AbstractEvent {
   private float elapsedPartialTicks;
   private int elapsedTicks;
   public static final int ID = 42;

   public int getElapsedTicks() {
      return this.elapsedTicks;
   }

   public float getElapsedPartialTicks() {
      return this.elapsedPartialTicks;
   }

   public void call2(TimerUpdateListener listener) {
      listener.onTimerUpdate(this);
   }

   private void reset(float f, int n) {
      this.elapsedPartialTicks = f;
      this.elapsedTicks = n;
   }

   public void setElapsedPartialTicks(float f) {
      this.elapsedPartialTicks = f;
   }

   public static void reset(EventTimerUpdate event, float f, int n) {
      event.reset(f, n);
   }

   public void setElapsedTicks(int n) {
      this.elapsedTicks = n;
   }

   @Override
   public void call(Object obj) {
      TimerUpdateListener listener = (TimerUpdateListener)obj;
      this.call2(listener);
   }
}
