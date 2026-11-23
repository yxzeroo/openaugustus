package net.augustus.events;

import net.augustus.events.listeners.PerspectiveListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventPerspective extends AbstractEvent {
   private float aspectRatio;
   public static final int ID = 48;
   private boolean handPass;

   public void setAspectRatio(float f) {
      this.aspectRatio = f;
   }

   @Override
   public void call(Object obj) {
      this.call2((PerspectiveListener)obj);
   }

   public boolean isHandPass() {
      return this.handPass;
   }

   public float getAspectRatio() {
      return this.aspectRatio;
   }

   private void reset(float f, boolean flag) {
      this.aspectRatio = f;
      this.handPass = flag;
   }

   public static void reset(EventPerspective event, float f, boolean flag) {
      event.reset(f, flag);
   }

   public void call2(PerspectiveListener listener) {
      listener.onPerspective(this);
   }
}
