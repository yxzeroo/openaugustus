package net.augustus.events;

import net.augustus.events.listeners.WorldTimeListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventWorldTime extends AbstractEvent {
   public static final int ID = 45;
   private long worldTime;

   @Override
   public void call(Object obj) {
      this.call2((WorldTimeListener)obj);
   }

   public long getWorldTime() {
      return this.worldTime;
   }

   public void setWorldTime(long l) {
      this.worldTime = l;
   }

   public void call2(WorldTimeListener listener) {
      listener.onWorldTime(this);
   }

   public static void reset(EventWorldTime event, long l) {
      event.setWorldTime2(l);
   }

   private void setWorldTime2(long l) {
      this.worldTime = l;
   }
}
