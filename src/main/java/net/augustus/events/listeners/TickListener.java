package net.augustus.events.listeners;


import net.augustus.events.EventManager;
import net.augustus.events.EventTick;
import net.augustus.events.TickPhase;
public interface TickListener {
   EventTick TICK_EVENT = new EventTick();
   long X = 108199053152015L;

   static void onTick2(TickPhase tickPhase) {
      EventTick.reset(TICK_EVENT, tickPhase);
      EventManager.getEventBus().post(40, TICK_EVENT);
   }

   default void onTick2() {
   }

   default void onTick3() {
   }

   default void onTick() {
   }

   default void onTick4() {
   }

   default void onTick5() {
   }
}
