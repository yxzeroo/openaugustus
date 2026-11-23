package net.augustus.render;

import net.augustus.modules.render.EmojiRegistry;
import java.util.UUID;
import net.augustus.utils.interfaces.MC;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class EmojifyRenderer implements MC {
   private int comboHitCount;
   private long targetTimeoutMs;
   private long displayUntilMs;
   public final EmojiRegistry emojiRegistry = new EmojiRegistry();
   private UUID targetPlayerUuid;

   public void onAttackEntity(Entity entity) {
      if (entity instanceof EntityPlayer) {
         UUID uniqueID = entity.getUniqueID();
         if (!uniqueID.equals(this.targetPlayerUuid)) {
            this.comboHitCount = 0;
         }

         this.targetPlayerUuid = uniqueID;
         this.targetTimeoutMs = System.currentTimeMillis() + 2000L;
      }
   }

   public void renderEmoji(EntityPlayer player, double d, double d2, double d3) {
      EntityPlayerSP thePlayer = mc.thePlayer;
      if (this.emojiRegistry.getSelectedSprite() != null && mc.gameSettings.thirdPersonView == 0 && !player.equals(thePlayer) && !player.isInvisibleToPlayer(thePlayer)) {
         if (this.emojiRegistry.getComboHits() == 0 || player.getUniqueID() == this.targetPlayerUuid && System.currentTimeMillis() <= this.displayUntilMs) {
            EmojiSprite selectedSprite = this.emojiRegistry.getSelectedSprite();
            double d4 = Math.sqrt(d * d + d3 * d3);
            if (d4 <= 1.0) {
               return;
            }

            GlStateManager.pushMatrix();
            double d5 = Math.atan2(d3, d);
            double d6 = Math.atan2(d2, d4);
            double d7 = 0.8;
            double d8 = -Math.cos(d5) * Math.cos(d6);
            double d9 = -Math.sin(d5) * Math.cos(d6);
            double d10 = -Math.sin(d6);
            GL11.glTranslated(d, d2 + (double)player.getEyeHeight() - 0.2, d3);
            GL11.glTranslated(d8 * d7, d10 * d7, d9 * d7);
            GL11.glRotated((double)(-mc.getRenderManager().playerViewY), 0.0, 1.0, 0.0);
            GL11.glRotated((double)mc.getRenderManager().playerViewX, 1.0, 0.0, 0.0);
            GL11.glRotated(180.0, 0.0, 0.0, 0.0);
            GL11.glScaled(-0.02666667, -0.02666667, 0.02666667);
            GL11.glTranslated(0.0, 9.374999, 0.0);
            GL11.glTranslated((double)(-selectedSprite.offsetX), (double)(-selectedSprite.offsetY), 0.0);
            if (player.isSneaking()) {
               GL11.glTranslated(0.0, -8.0, 0.0);
            }

            GlStateManager.disableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.enableBlend();
            GlStateManager.enableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            ResourceLocation resourceLocation = selectedSprite.getResourceLocation();
            mc.getTextureManager().bindTexture(resourceLocation);
            Tessellator instance = Tessellator.getInstance();
            WorldRenderer worldRenderer = instance.getWorldRenderer();
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            int halfSize = selectedSprite.halfSize;
            worldRenderer.pos((double)(-halfSize), (double)(-halfSize), 0.0).tex(1.0, 1.0).color(255, 255, 255, 255).endVertex();
            worldRenderer.pos((double)(-halfSize), (double)halfSize, 0.0).tex(1.0, 0.0).color(255, 255, 255, 255).endVertex();
            worldRenderer.pos((double)halfSize, (double)halfSize, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
            worldRenderer.pos((double)halfSize, (double)(-halfSize), 0.0).tex(0.0, 1.0).color(255, 255, 255, 255).endVertex();
            instance.draw();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
         }
      }
   }

   public void onEntityHurt(Entity entity) {
      if (entity != null) {
         if (!entity.equals(mc.thePlayer)) {
            UUID uniqueID = entity.getUniqueID();
            long l = System.currentTimeMillis();
            if (uniqueID == this.targetPlayerUuid && l < this.targetTimeoutMs) {
               this.comboHitCount++;
               if (this.comboHitCount >= this.emojiRegistry.getComboHits()) {
                  this.displayUntilMs = l + (long)this.emojiRegistry.getComboTime() * 1000L;
               }
            }
         } else {
            this.comboHitCount = 0;
         }
      }
   }
}
