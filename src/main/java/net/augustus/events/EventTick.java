package net.augustus.events;

import net.augustus.events.listeners.TickListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventTick extends AbstractEvent {
   public static final int ID = 40;
   public TickPhase phase;

   private void setPhase(TickPhase tickPhase) {
      this.phase = tickPhase;
   }

   public static void reset(EventTick event, TickPhase tickPhase) {
      event.setPhase(tickPhase);
   }

   public void call(Object obj) {
      this.call2((TickListener)obj);
   }

   public void call2(TickListener listener) {
      switch (EventTickPhaseSwitchMap.ORDINAL_MAP[this.phase.ordinal()]) {
         case 1:
            listener.onTick3();
            break;
         case 2:
            listener.onTick();
            break;
         case 3:
            listener.onTick5();
            break;
         case 4:
            listener.onTick4();
            break;
         case 5:
            listener.onTick2();
      }
   }
}
