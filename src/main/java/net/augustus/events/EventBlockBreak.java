package net.augustus.events;

import net.augustus.events.listeners.BlockBreakListener;
import de.florianmichael.dietrichevents2.CancellableEvent;

public class EventBlockBreak extends CancellableEvent {
   public static final int ID = 59;

   @Override
   public void call(Object obj) {
      this.call2((BlockBreakListener)obj);
   }

   public void call2(BlockBreakListener listener) {
      listener.onBlockBreak(this);
   }
}
