package net.augustus.events.listeners;


import net.augustus.events.EventManager;
import net.augustus.events.EventTimer;
import net.augustus.events.TimerEventType;
public interface TimerListener {
   EventTimer TIMER_EVENT = new EventTimer();
   long Y = 94879371394454L;

   static EventTimer onTimer(int n, float f, TimerEventType timerEventType) {
      EventTimer.reset(TIMER_EVENT, n, f, timerEventType);
      EventManager.getEventBus().post(41, TIMER_EVENT);
      return TIMER_EVENT;
   }

   void onTimer(EventTimer event);

   void onTimer2(EventTimer event);
}
