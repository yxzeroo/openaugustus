package net.augustus.events;

import net.augustus.events.listeners.GameLoopListener;
import net.augustus.utils.AntiSamplerGuard;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventGameLoop extends AbstractEvent {
   private int elapsedTicks;
   private float renderPartialTicks;
   private boolean allowSkipRender = true;
   private boolean skipRender = false;
   public static final int ID = 16;

   private void reset(int n, float f) {
      this.elapsedTicks = n;
      this.renderPartialTicks = f;
      this.allowSkipRender = true;
      this.skipRender = false;
   }

   @Override
   public void call(Object obj) {
      GameLoopListener listener = (GameLoopListener)obj;
      this.call2(listener);
   }

   public void call2(GameLoopListener listener) {
      listener.onGameLoop(this);
   }

   public void setSkipRender(boolean flag) {
      this.skipRender = flag;
   }

   public void setElapsedTicks(int n) {
      this.elapsedTicks = n;
   }

   public void setAllowSkipRender(boolean flag) {
      if (AntiSamplerGuard.isGuardActive(1)) {
         this.allowSkipRender = false;
      } else {
         this.allowSkipRender = flag;
      }
   }

   public float getRenderPartialTicks() {
      return this.renderPartialTicks;
   }

   public boolean isAllowSkipRender() {
      return this.allowSkipRender;
   }

   public int getElapsedTicks() {
      return this.elapsedTicks;
   }

   public boolean isSkipRender() {
      return this.skipRender;
   }

   public static void reset(EventGameLoop event, int n, float f) {
      event.reset(n, f);
   }

   public void setRenderPartialTicks(float f) {
      this.renderPartialTicks = f;
   }
}
