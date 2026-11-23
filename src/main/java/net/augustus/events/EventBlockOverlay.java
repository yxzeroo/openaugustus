package net.augustus.events;

import net.augustus.events.listeners.BlockOverlayListener;
import de.florianmichael.dietrichevents2.CancellableEvent;

public class EventBlockOverlay extends CancellableEvent {
   public static final int ID = 4;

   public void call2(BlockOverlayListener listener) {
      listener.onBlockOverlay(this);
   }

   @Override
   public void call(Object obj) {
      BlockOverlayListener listener = (BlockOverlayListener)obj;
      this.call2(listener);
   }
}
