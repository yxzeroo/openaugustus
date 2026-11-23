package net.augustus.clickgui;

import net.augustus.ClientManagers;
import net.augustus.font.CustomFontUtil;
import net.augustus.render.RenderUtil;
import java.util.List;
import net.augustus.modules.Categorys;

public class CategoryTabButton {
   public int left;
   private int scrollOffset;
   private boolean hovered;
   public int right;
   private int y;
   public int bottom;
   public int width;
   private int x;
   private final Categorys categorys;
   public int height;
   private int paddingY;
   public CustomFontUtil font;
   public int top;
   private final int textWidth;
   private int paddingX;

   public Categorys getCategorys() {
      return this.categorys;
   }

   public void setScrollOffset(int n) {
      this.scrollOffset = n;
   }

   public int getPaddingY() {
      return this.paddingY;
   }

   public List getModules() {
      return this.categorys.getModules();
   }

   public int getScrollOffset() {
      return this.scrollOffset;
   }

   public CategoryTabButton(int n, int n2, int n3, int n4, Categorys categorys2, CustomFontUtil customFontUtil) {
       super();
      this.scrollOffset = 0;
      this.font = customFontUtil;
      this.x = n;
      this.y = n2;
      this.paddingX = n3;
      this.paddingY = n4;
      this.left = this.x - n3;
      float f = (float)this.x;
      CustomFontUtil font2 = this.font;
      String str = categorys2.name();
      String str2 = str;
      this.right = (int)(f + font2.getStringWidth(str2) + (float)n3);
      this.top = this.y - n4 + 3;
      this.bottom = (int)((float)this.y + this.font.getFontHeight() + (float)n4);
      this.width = this.right - this.left;
      this.height = this.bottom - this.top;
      this.categorys = categorys2;
      String str3 = categorys2.name();
      String str4 = str3;
      this.textWidth = (int)customFontUtil.getStringWidth(str4);
   }

   public void drawTab(int n, int n2, int n3, int n4, boolean flag) {
      this.x = n3;
      this.y = n4;
      this.left = this.x - this.paddingX;
      float f = (float)this.x;
      CustomFontUtil font2 = this.font;
      String str = this.categorys.name();
      String str2 = str;
      this.right = (int)(f + font2.getStringWidth(str2) + (float)this.paddingX);
      this.top = this.y - this.paddingY + 3;
      this.bottom = (int)((float)this.y + this.font.getFontHeight() + (float)this.paddingY);
      byte b = 0;
      int colorInt = ClientManagers.moduleManager.clickGui.textColor.getColorInt(b);
      CustomFontUtil font3 = this.font;
      String str3 = this.categorys.toString();
      double d = (double)this.x;
      double d2 = (double)this.y;
      double d3 = d;
      String str4 = str3;
      font3.drawString(str4, d3, d2, colorInt);
      int height2 = this.height;
      int width2 = this.width;
      int top2 = this.top;
      int left2 = this.left;
      if (this.isMouseOver(left2, top2, width2, height2, n, n2) && !flag) {
         double d4 = (double)this.x;
         double d5 = (double)((float)this.y + this.font.getFontHeight());
         float f2 = (float)this.x;
         CustomFontUtil font4 = this.font;
         String str5 = this.categorys.name();
         String str6 = str5;
         RenderUtil.drawLine4(d4, d5, (double)(f2 + font4.getStringWidth(str6)), (double)((float)n4 + this.font.getFontHeight()), 2.0F, colorInt);
      }
   }

   public int getPaddingX() {
      return this.paddingX;
   }

   public int getTextWidth() {
      return this.textWidth;
   }

   public boolean isMouseOver(int n, int n2, int n3, int n4, int n5, int n6) {
      return n5 >= n && n6 >= n2 && n5 < n + n3 && n6 < n2 + n4;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }
}
