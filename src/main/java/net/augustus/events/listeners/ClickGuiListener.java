package net.augustus.events.listeners;


import net.augustus.events.EventClickGui;
import net.augustus.events.EventManager;
public interface ClickGuiListener {
   EventClickGui CLICK_GUI_EVENT = new EventClickGui();

   default void onClickGui() {
   }

   void onClickGui2();

   static void onClickGui3() {
      EventClickGui.reset(CLICK_GUI_EVENT, true);
      EventManager.getEventBus().post(6, CLICK_GUI_EVENT);
   }

   static void onClickGui4() {
      EventClickGui.reset(CLICK_GUI_EVENT, false);
      EventManager.getEventBus().post(6, CLICK_GUI_EVENT);
   }
}
