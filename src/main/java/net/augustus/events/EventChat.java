package net.augustus.events;

import net.augustus.events.listeners.ChatListener;
import de.florianmichael.dietrichevents2.CancellableEvent;

public class EventChat extends CancellableEvent {
   public static final int ID = 5;
   private String message;

   public void call2(ChatListener listener) {
      listener.onChat(this);
   }

   private void reset(String str) {
      this.setCancelled(false);
      this.message = str;
   }

   public String getMessage() {
      return this.message;
   }

   public static void reset(EventChat event, String str) {
      event.reset(str);
   }

   @Override
   public void call(Object obj) {
      ChatListener listener = (ChatListener)obj;
      ChatListener listener2 = listener;
      this.call2(listener2);
   }
}
