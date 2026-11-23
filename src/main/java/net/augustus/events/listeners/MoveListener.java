package net.augustus.events.listeners;


import net.augustus.events.EventManager;
import net.augustus.events.EventMove;
import net.augustus.events.MoveEventType;
public interface MoveListener {
   EventMove MOVE_EVENT = new EventMove();
   long K = 35760961229654L;

   void onMove(EventMove event);

   static EventMove onMove(float f, MoveEventType moveEventType) {
      EventMove.reset(MOVE_EVENT, f, moveEventType);
      EventManager.getEventBus().post(28, MOVE_EVENT);
      return MOVE_EVENT;
   }

   void onMove2(EventMove event);

   static EventMove onMove(float f, float f2, float f3, float f4, MoveEventType moveEventType) {
      EventMove.reset(MOVE_EVENT, f, f2, f3, f4, moveEventType);
      EventManager.getEventBus().post(28, MOVE_EVENT);
      return MOVE_EVENT;
   }
}
