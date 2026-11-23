package net.augustus.events;

import net.augustus.events.listeners.GuiScaleListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventGuiScale extends AbstractEvent {
   public static final int ID = 11;

   @Override
   public void call(Object obj) {
      this.call2((GuiScaleListener)obj);
   }

   public void call2(GuiScaleListener listener) {
      listener.onGuiScale();
   }
}
