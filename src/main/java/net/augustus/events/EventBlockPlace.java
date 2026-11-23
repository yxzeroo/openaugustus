package net.augustus.events;

import net.augustus.events.listeners.BlockPlaceListener;
import de.florianmichael.dietrichevents2.CancellableEvent;

public class EventBlockPlace extends CancellableEvent {
   public static final int ID = 60;
   private BlockPlaceStage stage;

   public void call2(BlockPlaceListener listener) {
      if (this.stage == BlockPlaceStage.PRE) {
         listener.onBlockPlace(this);
      } else {
         listener.onBlockPlace2(this);
      }
   }

   public void call(Object obj) {
      BlockPlaceListener listener = (BlockPlaceListener)obj;
      this.call2(listener);
   }

   public void reset(BlockPlaceStage blockPlaceStage) {
      this.stage = blockPlaceStage;
      this.setCancelled(false);
   }

   public BlockPlaceStage getStage() {
      return this.stage;
   }
}
