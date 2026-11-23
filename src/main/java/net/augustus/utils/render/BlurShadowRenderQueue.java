package net.augustus.utils.render;

import net.augustus.Augustus;
import net.augustus.events.EventManager;
import net.augustus.render.GlStateSnapshot;
import net.augustus.render.RenderPassTask;
import java.util.ArrayList;

public class BlurShadowRenderQueue {
   private int queuedCount;
   private final ArrayList snapshots = new ArrayList();

   public void enqueue(RenderPassTask renderPassTask) {
      if (this.queuedCount >= this.snapshots.size()) {
         GlStateSnapshot glStateSnapshot = new GlStateSnapshot();
         glStateSnapshot.capture(renderPassTask);
         this.snapshots.add(glStateSnapshot);
      } else {
         GlStateSnapshot glStateSnapshot2 = (GlStateSnapshot)this.snapshots.get(this.queuedCount);
         glStateSnapshot2.capture(renderPassTask);
      }

      this.queuedCount++;
   }

   public boolean isEmpty() {
      return this.queuedCount <= 0;
   }

   public void flush(ShaderRenderPass shaderRenderPass) {

      try {
         for (int i = 0; i < this.queuedCount; i++) {
            GlStateSnapshot glStateSnapshot = (GlStateSnapshot)this.snapshots.get(i);
            glStateSnapshot.restore(shaderRenderPass);
         }

         this.queuedCount = 0;
      } catch (Throwable e) {
         Augustus.getInstance().getClass();
         EventManager.reportException(e);
      }
   }

   public BlurShadowRenderQueue() {
      this.queuedCount = 0;
   }
}
