package net.augustus.events.listeners;


import net.augustus.events.BlockPlaceStage;
import net.augustus.events.EventBlockPlace;
import net.augustus.events.EventManager;
public interface BlockPlaceListener {
   EventBlockPlace BLOCK_PLACE_EVENT = new EventBlockPlace();

   default void onBlockPlace(EventBlockPlace event) {
   }

   static EventBlockPlace onBlockPlace3(BlockPlaceStage blockPlaceStage) {
      BLOCK_PLACE_EVENT.reset(blockPlaceStage);
      EventManager.getEventBus().post(60, BLOCK_PLACE_EVENT);
      return BLOCK_PLACE_EVENT;
   }

   default void onBlockPlace2(EventBlockPlace event) {
   }
}
