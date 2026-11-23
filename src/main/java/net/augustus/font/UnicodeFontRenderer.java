package net.augustus.font;

import net.augustus.utils.render.GlStateSaver;
import java.awt.Color;
import java.awt.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class UnicodeFontRenderer {
   private final Font awtFont;
   private final UnicodeFont unicodeFont;
   private final GlStateSaver glStateSaver = GlStateSaver.create();
   private final float scaleFactor;

   public void loadGlyphs() {
      try {
         this.unicodeFont.loadGlyphs();
      } catch (SlickException e) {
         throw new RuntimeException(e);
      }
   }

   public float drawStringWithShadow(String str, float f, float f2, int n) {
      float f3 = f + 1.0F;
      float f4 = f2 + 1.0F;
      int n2 = -16777216;
      float f5 = f4;
      float f6 = f3;
      this.drawString(str, f6, f5, n2);
      return this.drawString(str, f, f2, n);
   }

   public int getCharWidth(long l, char ch) {
      String str = Character.toString(ch);
      return (int)((float)this.getStringWidth(str) / this.scaleFactor);
   }

   public Font getAwtFont() {
      return this.awtFont;
   }

   public void drawCenteredString(String str, float f, float f2, int n) {
      float f3 = f - (float)(this.getStringWidth(str) / 2);
      this.drawString(str, f3, f2, n);
   }

   public int getStringHeight(String str) {
      return (int)((float)this.unicodeFont.getHeight(str) / this.scaleFactor);
   }

   public int getStringWidth(String str) {
      return (int)((float)this.unicodeFont.getWidth(str) / this.scaleFactor);
   }

   public UnicodeFontRenderer(Font font) {
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
      this.scaleFactor = (float)sr.getScaleFactor();
      this.unicodeFont = new UnicodeFont(font.deriveFont(0, (float)font.getSize() / 2.0F * this.scaleFactor));
      this.awtFont = font;
      this.unicodeFont.addGlyphs(0, 8303);
      this.unicodeFont.addGlyphs("❤");
      this.unicodeFont.getEffects().add(new ColorEffect(Color.WHITE));
      this.loadGlyphs();
   }

   public UnicodeFontRenderer(Font font, float f) {
      this.scaleFactor = f;
      this.unicodeFont = new UnicodeFont(font.deriveFont(0, (float)font.getSize() / 2.0F * this.scaleFactor));
      this.awtFont = font;
      this.unicodeFont.addGlyphs(0, 8303);
      this.unicodeFont.addGlyphs("❤");
      this.unicodeFont.getEffects().add(new ColorEffect(Color.WHITE));
      this.loadGlyphs();
   }

   public float drawString(String str, float f, float f2, int n) {
      if (str == null) {
         return 0.0F;
      } else {
         GL11.glPushMatrix();
         GL11.glScaled((double)(1.0F / this.scaleFactor), (double)(1.0F / this.scaleFactor), 1.0);
         boolean flag = true;
         this.glStateSaver.setBlend(flag);
         boolean flag2 = false;
         this.glStateSaver.setLighting(flag2);
         boolean flag3 = false;
         this.glStateSaver.setTexture2D(flag3);
         boolean flag4 = false;
         this.glStateSaver.setAlpha(flag4);
         f *= this.scaleFactor;
         f2 *= this.scaleFactor;
         this.unicodeFont.drawString(f, f2, str, new org.newdawn.slick.Color(n));
         GL11.glDisable(3553);
         GL11.glBindTexture(3553, 0);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         OpenGlHelper.glBlendFunc(GlStateManager.getBlendSrcFactor(), GlStateManager.getBlendDstFactor(), GlStateManager.getBlendSrcFactorAlpha(), GlStateManager.getBlendDstFactorAlpha());
         this.glStateSaver.restoreAlpha();
         this.glStateSaver.restoreTexture2D();
         this.glStateSaver.restoreLighting();
         this.glStateSaver.restoreBlend();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
         GlStateManager.bindTexture(0);
         return f;
      }
   }
}
