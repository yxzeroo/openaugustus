package net.augustus.ui.widgets;

import net.augustus.render.RenderUtil;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiTabContainer {
   private int widthRemainder;
   private int x;
   private int selectedTabIndex;
   private final boolean drawBackground;
   private final int y;
   private final List tabButtons;
   private final int width;
   private final List tabPages;
   private final TabLayoutMode layoutMode;
   private static final long a = 68910699260376L;

   public GuiTabContainer(int n, int n2, int n3, long l, int n4, String[] strings) {
       this(n, n2, n3, n4, TabLayoutMode.STATIC, false, createStaticTabPages(strings, (short)((int)(((a ^ l) ^ 89831978495787L) << 32 >>> 48))));
       l = a ^ l;
   }

   public int getSelectedTabIndex() {
      return this.selectedTabIndex;
   }

   public void setSelectedTabIndex(int n) {
      this.selectedTabIndex = n;
   }

   private static GuiTabPage[] createStaticTabPages(String[] strings, short s) {
      GuiTabPage[] guiTabPages = new GuiTabPage[strings.length];
      int n = 0;
      Object obj = null;

      GuiTabPage[] guiTabPages2;
      outerLoop:
      while (true) {
         if (n < strings.length) {
            guiTabPages2 = guiTabPages;
            if (obj == null) {
               break;
            }

            guiTabPages[n] = new StaticLabelTabItem(strings[n]);
            n++;
            if (obj != null) {
               continue;
            }
         }

         while (s >= 0) {
            if (obj != null) {
               continue outerLoop;
            }
         }

         guiTabPages2 = guiTabPages;
         break;
      }

      return guiTabPages2;
   }

   public void selectTabButton(GuiButton button, int n, short s, int n2) {
      this.selectedTabIndex = button.id;
      for (int i = 0; i < this.tabButtons.size(); i++) {
         ((GuiButton)this.tabButtons.get(i)).enabled = i != this.selectedTabIndex;
      }
   }

   public void drawTabs(int n, int n2) {
      byte b = 0;
      this.drawTabsAtX(n, n2, b);
   }

   public GuiTabContainer(int n, int n2, long l, int n3, String[] strings) {
       this(n, n2, n3, 20, TabLayoutMode.STATIC, false, createStaticTabPages(strings, (short)((int)(((a ^ l) ^ 99484240875905L) << 32 >>> 48))));
       l = a ^ l;
   }

   public TabLayoutMode getLayoutMode() {
      return this.layoutMode;
   }

   public void drawTabsAtX(int n, int n2, int n3) {
      String controlFlowGuard = ScrollBar.getControlFlowGuard();
      if (n3 != 0) {
         this.x = n3;
      }

      GuiTabPage guiTabPage = (GuiTabPage)this.tabPages.get(this.selectedTabIndex);
      int n4 = 0;
      Iterator it = this.tabButtons.iterator();

      int n5;
      while (true) {
         if (it.hasNext()) {
            GuiButton button = (GuiButton)it.next();
            n5 = n3;
            if (controlFlowGuard == null) {
               break;
            }

            if (controlFlowGuard != null) {
               if (n3 != 0) {
                  button.xPosition = n3 + this.width / this.tabPages.size() * n4;
               }

               n5 = (this.layoutMode.equals(TabLayoutMode.STATIC)) ? 1 : 0;
            }

            exitBlock: {
               if (n5 != 0) {
                  button.yPosition = this.y;
                  if (controlFlowGuard != null) {
                     break exitBlock;
                  }
               }

               button.yPosition = this.y - guiTabPage.getContentHeight();
            }

            button.drawButton(Minecraft.getMinecraft(), n, n2);
            n4++;
            if (controlFlowGuard != null) {
               continue;
            }
         }

         n5 = (this.layoutMode.equals(TabLayoutMode.STATIC)) ? 1 : 0;
         break;
      }

      int n6;
      exitBlock2: {
         if (controlFlowGuard != null) {
            if (n5 != 0) {
               n6 = this.y + 22;
               break exitBlock2;
            }

            n5 = this.y + 22 - guiTabPage.getContentHeight();
         }

         n6 = n5;
      }

      exitBlock3: {
         n5 = (this.drawBackground) ? 1 : 0;
         if (controlFlowGuard != null) {
            if (!this.drawBackground) {
               break exitBlock3;
            }

            n5 = this.x;
         }

         RenderUtil.drawRect3(n5, n6 - 2, this.x + this.width - this.widthRemainder, n6 + guiTabPage.getContentHeight(), Integer.MIN_VALUE);
      }

      int n7 = this.x;
      guiTabPage.drawContent(n7, n6, n, n2);
   }

   public void handleMouseClick(int n, short s, int n2, int n3, long l) {
      Object obj;
      exitBlock: {
         obj = null;
         int n4 = n3;
         if (obj != null) {
            if (n3 == 0) {
               break exitBlock;
            }

            n4 = n3;
         }

         if (n4 != 1) {
            return;
         }
      }

      for (GuiButton button : (Iterable<GuiButton>)(this.tabButtons)) {
         if (button.mousePressed(Minecraft.getMinecraft(), n, n2)) {
            return;
         }

         if (obj == null) {
            break;
         }
      }
   }

   public GuiTabContainer(int n, int n2, int n3, int n4, TabLayoutMode tabLayoutMode, boolean flag, GuiTabPage[] guiTabPages) {
      ScrollBar.getControlFlowGuard();
      this.widthRemainder = 0;
      this.selectedTabIndex = 0;
      this.x = n;
      this.y = n2;
      this.layoutMode = tabLayoutMode;
      this.width = n3;
      this.drawBackground = flag;
      this.tabPages = Lists.newArrayList();
      this.tabButtons = Lists.newArrayList();
      int n5 = this.width / guiTabPages.length;
      this.widthRemainder = this.width - n5 * guiTabPages.length;

      for (int i = 0; i < guiTabPages.length; i++) {
         GuiTabPage guiTabPage = guiTabPages[i];
         this.tabPages.add(guiTabPage);
         GuiButton button;
         this.tabButtons.add(button = new GuiButton(i, n + n5 * i, n2, n5, n4, guiTabPage.getTitle()));
         button.enabled = i != this.selectedTabIndex;
      }
   }

   public boolean isDrawBackground() {
      return this.drawBackground;
   }

   public int getX() {
      return this.x;
   }

   public int getWidth() {
      return this.width;
   }

   public int getY() {
      return this.y;
   }

}
