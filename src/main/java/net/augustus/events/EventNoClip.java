package net.augustus.events;

import net.augustus.events.listeners.NoClipListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventNoClip extends AbstractEvent {
   public static final int ID = 27;

   public void call2(NoClipListener listener) {
      listener.onNoClip2();
   }

   @Override
   public void call(Object obj) {
      NoClipListener listener = (NoClipListener)obj;
      this.call2(listener);
   }
}
