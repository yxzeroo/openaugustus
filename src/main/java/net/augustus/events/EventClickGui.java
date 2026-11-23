package net.augustus.events;

import net.augustus.events.listeners.ClickGuiListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventClickGui extends AbstractEvent {
   public static final int ID = 6;
   private boolean renderPass;

   public static void reset(EventClickGui event, boolean flag) {
      event.setRenderPass(flag);
   }

   public void call(Object obj) {
      this.call2((ClickGuiListener)obj);
   }

   public void call2(ClickGuiListener listener) {
      if (this.renderPass) {
         listener.onClickGui();
      } else {
         listener.onClickGui2();
      }
   }

   private void setRenderPass(boolean flag) {
      this.renderPass = flag;
   }
}
