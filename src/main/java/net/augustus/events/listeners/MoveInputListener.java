package net.augustus.events.listeners;


import net.augustus.events.EventManager;
import net.augustus.events.EventMoveInput;
import net.augustus.events.MoveInputStage;
public interface MoveInputListener {
   long B = 84648645920613L;

   default void onMoveInput(EventMoveInput event) {
   }

   default void onMoveInput2(EventMoveInput event) {
   }

   void onMoveInput3(EventMoveInput event);

   static EventMoveInput onMoveInput(float f, float f2, boolean flag, boolean flag2, MoveInputStage moveInputStage) {
      EventMoveInput event = new EventMoveInput(f, f2, flag, flag2, moveInputStage);
      EventManager.getEventBus().post(21, event);
      return event;
   }
}
