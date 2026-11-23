package net.augustus.events;

import net.augustus.events.listeners.ZoomListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventZoom extends AbstractEvent {
   public static final int ID = 46;
   private float fovMultiplier;

   public float getFovMultiplier() {
      return this.fovMultiplier;
   }

   public void setFovMultiplier(float f) {
      this.fovMultiplier = f;
   }

   @Override
   public void call(Object obj) {
      ZoomListener listener = (ZoomListener)obj;
      ZoomListener listener2 = listener;
      this.call2(listener2);
   }

   private void setFovMultiplier2(float f) {
      this.fovMultiplier = f;
   }

   public void call2(ZoomListener listener) {
      listener.onZoom(this);
   }

   public static void reset(EventZoom event, float f) {
      event.setFovMultiplier2(f);
   }
}
