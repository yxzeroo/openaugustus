package net.augustus.font;

import net.augustus.events.EventManager;
import net.augustus.events.listeners.GuiScaleListener;
import java.awt.Font;
import net.augustus.utils.interfaces.MC;

public class CustomFontUtil implements GuiScaleListener, MC {
   public Font awtFont;
   public UnicodeFontRenderer unicodeFontRenderer;
   public float fontHeight = 0.0F;
   private final String fontName;
   public int scaleOverride = 2147483647;
   private static int[] fontStateFlags;

   public void drawCenteredStringInBox(String str, double d, double d2, double d3, double d4, int n) {
      UnicodeFontRenderer unicodeFontRenderer2 = this.unicodeFontRenderer;
      float f = (float)(d + d3 / 2.0 - (double)((float)this.unicodeFontRenderer.getStringWidth(str) / 2.0F));
      float f2 = (float)(d2 + d4 / 2.0 - (double)((float)this.unicodeFontRenderer.getStringHeight(str) / 2.0F));
      float f3 = f;
      unicodeFontRenderer2.drawString(str, f3, f2, n);
   }

   public float getStringWidth(String str) {
      return (float)this.unicodeFontRenderer.getStringWidth(str);
   }

   public float getStringHeight(String str) {
      return (float)this.unicodeFontRenderer.getStringHeight(str);
   }

   public CustomFontUtil(String str, Font font, int n) {
       super();
      this.fontHeight = 0.0F;
      this.scaleOverride = 2147483647;
      this.fontName = str;
      this.scaleOverride = n;
      this.awtFont = font;
      this.unicodeFontRenderer = new UnicodeFontRenderer(font, (float)this.scaleOverride);
      this.unicodeFontRenderer.loadGlyphs();
      String str2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqurstuvwxyz1234567890";
      this.fontHeight = (float)this.unicodeFontRenderer.getStringHeight(str2);
      EventManager.getEventBus().subscribe(11, this);
   }

   public CustomFontUtil(String str, int n, int n2) {
       super();
      this.fontHeight = 0.0F;
      this.scaleOverride = 2147483647;
      this.fontName = str;
      this.scaleOverride = n2;
      this.awtFont = new Font(str, 0, n);
      this.unicodeFontRenderer = new UnicodeFontRenderer(this.awtFont, (float)this.scaleOverride);
      String str2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqurstuvwxyz1234567890";
      this.fontHeight = (float)this.unicodeFontRenderer.getStringHeight(str2);
      EventManager.getEventBus().subscribe(11, this);
   }

   public void drawVerticallyCenteredStringFixed(String str, double d, double d2, double d3, int n) {
      UnicodeFontRenderer unicodeFontRenderer2 = this.unicodeFontRenderer;
      float f = (float)d;
      double d4 = d2 + d3 / 2.0;
      String str2 = "Dj";
      float f2 = (float)(d4 - (double)((float)this.unicodeFontRenderer.getStringHeight(str2) / 2.0F));
      float f3 = f;
      unicodeFontRenderer2.drawString(str, f3, f2, n);
   }

   public CustomFontUtil(String str, Font font) {
       super();
      this.fontHeight = 0.0F;
      this.scaleOverride = 2147483647;
      this.fontName = str;
      this.awtFont = font;
      this.unicodeFontRenderer = new UnicodeFontRenderer(font);
      String str2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqurstuvwxyz1234567890";
      this.fontHeight = (float)this.unicodeFontRenderer.getStringHeight(str2);
      EventManager.getEventBus().subscribe(11, this);
   }

   public float getFontHeight() {
      return this.fontHeight;
   }

   public CustomFontUtil(String str, int n) {
       super();
      this.fontHeight = 0.0F;
      this.scaleOverride = 2147483647;
      this.fontName = str;
      this.awtFont = new Font(str, 0, n);
      this.unicodeFontRenderer = new UnicodeFontRenderer(this.awtFont);
      String str2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqurstuvwxyz1234567890";
      this.fontHeight = (float)this.unicodeFontRenderer.getStringHeight(str2);
      EventManager.getEventBus().subscribe(11, this);
   }

   public void drawCenteredStringInBoxWithShadow(String str, double d, double d2, double d3, double d4, int n) {
      UnicodeFontRenderer unicodeFontRenderer2 = this.unicodeFontRenderer;
      float f = (float)(d + d3 / 2.0 - (double)((float)this.unicodeFontRenderer.getStringWidth(str) / 2.0F));
      float f2 = (float)(d2 + d4 / 2.0 - (double)((float)this.unicodeFontRenderer.getStringHeight(str) / 2.0F));
      float f3 = f;
      unicodeFontRenderer2.drawStringWithShadow(str, f3, f2, n);
   }

   public void onGuiScale() {
      this.reloadFont();
   }

   public void drawHorizontallyCenteredString(String str, double d, double d2, double d3, int n) {
      UnicodeFontRenderer unicodeFontRenderer2 = this.unicodeFontRenderer;
      float f = (float)(d + d3 / 2.0 - (double)((float)this.unicodeFontRenderer.getStringWidth(str) / 2.0F));
      float f2 = (float)d2;
      float f3 = f;
      unicodeFontRenderer2.drawString(str, f3, f2, n);
   }

   public void drawVerticallyCenteredString(String str, double d, double d2, double d3, int n) {
      UnicodeFontRenderer unicodeFontRenderer2 = this.unicodeFontRenderer;
      float f = (float)d;
      float f2 = (float)(d2 + d3 / 2.0 - (double)((float)this.unicodeFontRenderer.getStringHeight(str) / 2.0F));
      float f3 = f;
      unicodeFontRenderer2.drawString(str, f3, f2, n);
   }

   public String getFontName() {
      return this.fontName;
   }

   public void drawString(String str, double d, double d2, int n, boolean flag) {
      int[] fontStateFlags2 = getFontStateFlags();
      if (flag) {
         float f = (float)d;
         float f2 = (float)d2;
         float f3 = f;
         this.unicodeFontRenderer.drawStringWithShadow(str, f3, f2, n);
      } else {
         float f4 = (float)d;
         float f5 = (float)d2;
         float f6 = f4;
         this.unicodeFontRenderer.drawString(str, f6, f5, n);
      }
   }

   private void reloadFont() {
      int[] fontStateFlags2 = getFontStateFlags();
      if (this.scaleOverride == 2147483647) {
         this.unicodeFontRenderer = new UnicodeFontRenderer(this.awtFont);
         this.unicodeFontRenderer.loadGlyphs();
         String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqurstuvwxyz1234567890";
         this.fontHeight = (float)this.unicodeFontRenderer.getStringHeight(str);
      }
   }

   public void drawString(String str, double d, double d2, int n) {
      float f = (float)d;
      float f2 = (float)d2;
      float f3 = f;
      this.unicodeFontRenderer.drawString(str, f3, f2, n);
   }

   public void drawStringWithShadow(String str, double d, double d2, int n) {
      float f = (float)d;
      float f2 = (float)d2;
      float f3 = f;
      this.unicodeFontRenderer.drawStringWithShadow(str, f3, f2, n);
   }

   public CustomFontUtil() {
      this.fontName = "";
   }

   public static void setFontStateFlags(int[] ints) {
      fontStateFlags = ints;
   }

   public static int[] getFontStateFlags() {
      return fontStateFlags;
   }

   static {
      setFontStateFlags((int[])null);
   }

}
