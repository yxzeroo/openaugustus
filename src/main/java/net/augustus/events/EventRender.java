package net.augustus.events;

import net.augustus.events.listeners.RenderListener;
import de.florianmichael.dietrichevents2.AbstractEvent;
import net.minecraft.client.gui.ScaledResolution;

public class EventRender extends AbstractEvent {
   private float partialTicks;
   public static final int ID = 32;
   private RenderPassType passType;
   private ScaledResolution scaledResolution;

   private void reset(float f, ScaledResolution sr, RenderPassType renderPassType) {
      this.partialTicks = f;
      this.passType = renderPassType;
      this.scaledResolution = sr;
   }

   public static void reset(EventRender event, float f, ScaledResolution sr, RenderPassType renderPassType) {
      event.reset(f, sr, renderPassType);
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }

   public void call(Object obj) {
      RenderListener listener = (RenderListener)obj;
      this.call2(listener);
   }

   public void call2(RenderListener listener) {
      switch (EventRenderSwitchMap.ORDINAL_MAP[this.passType.ordinal()]) {
         case 1:
            float partialTicks2 = this.partialTicks;
            listener.onRender(partialTicks2);
            break;
         case 2:
            ScaledResolution scaledResolution2 = this.scaledResolution;
            float partialTicks3 = this.partialTicks;
            listener.onRender2(partialTicks3, scaledResolution2);
            break;
         case 3:
            ScaledResolution scaledResolution3 = this.scaledResolution;
            float partialTicks4 = this.partialTicks;
            listener.onRender(partialTicks4, scaledResolution3);
      }
   }
}
