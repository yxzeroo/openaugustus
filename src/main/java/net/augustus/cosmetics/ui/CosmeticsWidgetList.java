package net.augustus.cosmetics.ui;

import net.augustus.cosmetics.CosmeticsMenuEntry;
import net.augustus.cosmetics.CosmeticsMenuGui;
import net.augustus.cosmetics.CosmeticsMenuRenderer;
import java.util.ArrayList;

public class CosmeticsWidgetList {
   private boolean scrollable;
   private int scrollbarTopY;
   private int scrollbarBottomY;
   private boolean popup;
   private int countedRows;
   private int posX;
   private int topY;
   private ArrayList entries = new ArrayList();
   private int visibleRowCount;
   private int currentDrawY;
   private int scrollOffset;
   private int maxRightX;
   private int bottomY;
   private CosmeticsMenuGui parentGui;
   private int totalRowCount;

   public ArrayList getEntries() {
      return this.entries;
   }

   private static void dispatchKeyTyped(char ch, int n, CosmeticsMenuEntry cosmeticsMenuEntry) {
      cosmeticsMenuEntry.keyTyped(ch, n);
   }

   public int getScrollOffset() {
      return this.scrollOffset;
   }

   public int getBottomY() {
      return this.bottomY;
   }

   public CosmeticsWidgetList(CosmeticsMenuGui cosmeticsMenuGui, int n, boolean flag) {
      this.posX = n;
      this.parentGui = cosmeticsMenuGui;
      this.popup = flag;
   }

   public void layout(int n, int n2) {
      byte b = 0;
      this.layout(n, n2, b);
   }

   public int getTopY() {
      return this.topY;
   }

   public int getScrollbarTopY() {
      return this.scrollbarTopY;
   }

   public void draw(Object obj, int n, int n2, int n3) {
      this.maxRightX = 0;
      float f = (float)this.scrollOffset / (float)this.totalRowCount;
      this.scrollbarTopY = this.topY + (int)((float)(this.bottomY - this.topY) * f);
      float f2 = (float)(this.scrollOffset + this.visibleRowCount) / (float)this.totalRowCount;
      this.scrollbarBottomY = this.topY + (int)((float)(this.bottomY - this.topY) * f2);
      if (this.entries.size() > 0) {
         this.currentDrawY = this.topY + 2;
         if (this.popup) {
            int n4 = this.currentDrawY + this.entries.size() * 20 + 3;
            if ((float)n4 > 345.0F) {
               this.currentDrawY = (int)((float)this.currentDrawY - ((float)n4 - 345.0F));
               n4 = this.currentDrawY + this.entries.size() * 20 + 3;
            }

            CosmeticsMenuRenderer renderer = this.parentGui.getRenderer();
            int n5 = this.currentDrawY - 1;
            int n6 = this.posX + n3 + 3;
            byte b = 5;
            int bordercolor = CosmeticsColors.BORDER_COLOR;
            int n7 = n6;
            int n8 = n5;
            int posX2 = this.posX;
            renderer.drawRoundedRect(posX2, n8, n7, n4, bordercolor, b);
            renderer = this.parentGui.getRenderer();
            int n9 = this.posX + 1;
            n6 = this.posX + n3 + 3 - 1;
            int n10 = n4 - 1;
            byte b2 = 5;
            int panelbackground = CosmeticsColors.PANEL_BACKGROUND;
            int n11 = n10;
            int n12 = n6;
            int currentDrawY2 = this.currentDrawY;
            int n13 = n9;
            renderer.drawRoundedRect(n13, currentDrawY2, n12, n11, panelbackground, b2);
         }

         for (int i = this.scrollOffset; i < this.entries.size(); i++) {
            CosmeticsMenuEntry cosmeticsMenuEntry = (CosmeticsMenuEntry)this.entries.get(i);
            if (this.currentDrawY > this.bottomY) {
               break;
            }

            byte b3 = 20;
            int currentDrawY3 = this.currentDrawY;
            int posX3 = this.posX;
            cosmeticsMenuEntry.draw(obj, posX3, currentDrawY3, n3, b3, n, n2);
            this.maxRightX = this.posX + n3 > this.maxRightX ? this.posX + n3 : this.maxRightX;
            this.currentDrawY = this.currentDrawY + 20 + (this.popup ? 0 : 1);
         }
      }
   }

   public void scroll(int n) {
      this.scrollOffset -= n;
      this.scrollOffset = this.scrollOffset + this.visibleRowCount > this.totalRowCount ? this.totalRowCount - this.visibleRowCount : this.scrollOffset;
      this.scrollOffset = this.scrollOffset < 0 ? 0 : this.scrollOffset;
   }

   public void layout(int n, int n2, int n3) {
      this.posX = n;
      this.updateBounds(n2, n3);
      this.recalculateScroll();
      this.scrollOffset = 0;
   }

   public int getScrollbarBottomY() {
      return this.scrollbarBottomY;
   }

   private void countEntryRows(CosmeticsMenuEntry cosmeticsMenuEntry) {
      this.countedRows++;
      if (cosmeticsMenuEntry.isExpanded()) {
         for (CosmeticsMenuEntry cosmeticsMenuEntry2 : (Iterable<CosmeticsMenuEntry>)(cosmeticsMenuEntry.getChildren())) {
            this.countEntryRows(cosmeticsMenuEntry2);
         }
      }
   }

   public boolean isScrollable() {
      return this.scrollable;
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
      this.entries.forEach(obj -> {
         ((CosmeticsMenuEntry)obj).mouseClicked(mouseX, mouseY, mouseButton);
      });
      this.recalculateScroll();
   }

   public int getMaxRightX() {
      return this.maxRightX;
   }

   public void setEntries(ArrayList list) {
      this.entries = list;
   }

   private void updateBounds(int n, int n2) {
      this.topY = this.parentGui.getHeaderHeight();
      this.bottomY = n;
      if (this.popup) {
         if (this.parentGui.getSelectedTab().isListLayout()) {
            this.topY = this.parentGui.getHeaderHeight() + n2 * 20 + 2;
         } else {
            this.topY = this.parentGui.getHeaderHeight() + n2 * 54 - 2;
            this.bottomY = this.topY + this.entries.size() * 21;
         }
      }
   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
      this.entries.forEach(obj -> dispatchMouseReleased(mouseX, mouseY, state, (CosmeticsMenuEntry)obj));
   }

   private static void dispatchMouseReleased(int n, int n2, int n3, CosmeticsMenuEntry cosmeticsMenuEntry) {
      cosmeticsMenuEntry.mouseReleased(n, n2, n3);
   }

   public void recalculateScroll() {
      this.countedRows = 0;

      for (CosmeticsMenuEntry cosmeticsMenuEntry : (Iterable<CosmeticsMenuEntry>)(this.entries)) {
         this.countEntryRows(cosmeticsMenuEntry);
      }

      float f = (float)(20 + (this.popup ? 0 : 1));
      this.visibleRowCount = (int)Math.floor((double)((float)(this.bottomY - this.topY) / f));
      this.totalRowCount = this.countedRows;
      this.scrollable = this.totalRowCount > this.visibleRowCount;
   }

   public void keyTyped(char typedChar, int keyCode) {
      this.entries.forEach(obj -> dispatchKeyTyped(typedChar, keyCode, (CosmeticsMenuEntry)obj));
   }
}
