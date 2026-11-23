package net.augustus.events.listeners;


import net.augustus.events.EventAsyncTick;
import net.augustus.events.EventManager;
public interface AsyncTickListener {
   EventAsyncTick ASYNC_TICK_EVENT = new EventAsyncTick();

   void onAsyncTick();

   static void onAsyncTick2() {
      EventManager.getEventBus().post(61, ASYNC_TICK_EVENT);
   }
}
