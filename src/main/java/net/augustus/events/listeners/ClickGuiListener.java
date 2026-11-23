package net.augustus.events.listeners;


import net.augustus.events.EventClickGui;
import net.augustus.events.EventManager;
public interface ClickGuiListener {
   EventClickGui CLICK_GUI_EVENT = new EventClickGui();

   default void onClickGui() {
   }

   void onClickGui2();

   static void onClickGui3() {
      boolean flag = true;
      EventClickGui.reset(CLICK_GUI_EVENT, flag);
      EventManager.getEventBus().post(6, CLICK_GUI_EVENT);
   }

   static void onClickGui4() {
      boolean flag = false;
      EventClickGui.reset(CLICK_GUI_EVENT, flag);
      EventManager.getEventBus().post(6, CLICK_GUI_EVENT);
   }
}
