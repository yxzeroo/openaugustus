package net.augustus.events.listeners;

import net.augustus.events.EventManager;
import net.augustus.events.EventRender;
import net.augustus.events.RenderPassType;
import net.minecraft.client.gui.ScaledResolution;

public interface RenderListener {
   EventRender RENDER_EVENT = new EventRender();
   long P = 52685641112639L;

   default void onRender(float f, ScaledResolution sr) {
   }

   default void onRender(float f) {
   }

   static void onRender(float f, ScaledResolution sr, RenderPassType renderPassType) {
      EventRender.reset(RENDER_EVENT, f, sr, renderPassType);
      EventManager.getEventBus().post(32, RENDER_EVENT);
   }

   default void onRender2(float f, ScaledResolution sr) {
   }
}
