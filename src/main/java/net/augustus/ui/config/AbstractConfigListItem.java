package net.augustus.ui.config;

import net.augustus.ClientManagers;
import net.augustus.clickgui.MainClickGuiWindow;
import net.augustus.font.CustomFontUtil;
import net.augustus.render.RenderUtil;
import net.augustus.vendor.OnlineConfigMetadata;
import java.awt.Color;
import java.io.IOException;
import net.augustus.utils.TimeHelper;

public abstract class AbstractConfigListItem {
   private final String author;
   private final TimeHelper loadCooldownTimer;
   private int x;
   private int tooltipX;
   private int cachedNameWidth;
   private String updatedTime;
   private final TimeHelper hoverTimer;
   private final TimeHelper doubleClickTimer;
   private int lastMouseX;
   private int tooltipY;
   private int width;
   private CustomFontUtil fontRenderer;
   private int lastMouseY;
   private final boolean editable;
   private boolean rendered;
   private boolean hovered;
   private int y;
   private String updatedDate;
   private String configName = "";
   private static Color sharedConfigColor = new Color(64, 251, 178);
   private int height;

   public void renderTooltip() {
      if (this.rendered && this.updatedDate != null && this.updatedTime != null && !this.updatedDate.isEmpty() && !this.updatedTime.isEmpty()) {
         float f = (float)(this.x - 5);
         float f2 = (float)this.y;
         String configName2 = this.configName;
         boolean isHovered = RenderUtil.isHovered(
            f, f2, this.fontRenderer.getStringWidth(configName2) + 10.0F, (float)(this.height + 2), this.lastMouseX, this.lastMouseY
         );
         if (isHovered) {
            if (!this.hovered) {
               this.hoverTimer.reset();
               this.tooltipX = -1337;
               this.tooltipY = -1337;
            }

            long l = 500L;
            if (this.hoverTimer.reached2(l)) {
               if (this.tooltipX == -1337 && this.tooltipY == -1337) {
                  this.tooltipX = this.lastMouseX;
                  this.tooltipY = this.lastMouseY;
               }

               String str = "Updated: " + this.updatedDate + " " + this.updatedTime + " by " + this.author;
               int tooltipY2 = this.tooltipY;
               int tooltipX2 = this.tooltipX;
               String str2 = str;
               MainClickGuiWindow.drawTooltip(this.fontRenderer, str2, tooltipX2, tooltipY2);
            }
         }

         this.hovered = isHovered;
      }
   }

   public CustomFontUtil getFontRenderer() {
      return this.fontRenderer;
   }

   public abstract void loadConfig();

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (mouseButton == 0 && this.getFontRenderer() != null) {
         float f = (float)(this.getX() - 5);
         float y2 = (float)this.getY();
         CustomFontUtil fontRenderer2 = this.getFontRenderer();
         String configName2 = this.getConfigName();
         String str = configName2;
         if (RenderUtil.isHovered(f, y2, (float)((int)(fontRenderer2.getStringWidth(str) + 10.0F)), (float)(this.getHeight() + 2), mouseX, mouseY)) {
            long l = 500L;
            if (!this.doubleClickTimer.reached2(l)) {
               long l2 = 1000L;
               if (this.loadCooldownTimer.reached2(l2)) {
                  this.loadCooldownTimer.reset();
                  this.loadConfig();
               }
            }

            this.doubleClickTimer.reset();
         }
      }
   }

   public int getY() {
      return this.y;
   }

   public int getX() {
      return this.x;
   }

   public boolean isAuthor(String str) {
      return this.author.equals(str);
   }

   public String getConfigName() {
      return this.configName;
   }

   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
   }

   public int getWidth() {
      return this.width;
   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
   }


   public int getHeight() {
      return this.height;
   }

   public AbstractConfigListItem(String str, String str2, String str3, String str4, boolean flag) {
      this.updatedDate = "";
      this.updatedTime = "";
      this.doubleClickTimer = new TimeHelper();
      this.loadCooldownTimer = new TimeHelper();
      this.hoverTimer = new TimeHelper();
      this.rendered = false;
      this.cachedNameWidth = -1;
      this.configName = str;
      this.updatedDate = str3;
      this.updatedTime = str4;
      this.author = str2;
      this.x = 0;
      this.y = 0;
      this.width = 0;
      this.height = 0;
      this.fontRenderer = null;
      this.editable = flag;
   }

   public boolean isEditable() {
      return this.editable;
   }

   public boolean isSharedWithFriends() {
      return false;
   }

   public void keyTyped(char typedChar, int keyCode) throws IOException {
   }

   public void renderItem(int n, int n2, int n3, int n4, int n5, int n6, CustomFontUtil customFontUtil, boolean flag) {
      this.x = n;
      this.y = n2;
      this.lastMouseX = n5;
      this.lastMouseY = n6;
      this.width = n3;
      this.height = n4;
      this.fontRenderer = customFontUtil;
      if (this.cachedNameWidth == -1) {
         String configName2 = this.configName;
         this.cachedNameWidth = (int)customFontUtil.getStringWidth(configName2);
      }

      int n7;
      if (flag) {
         byte b = 0;
         n7 = RenderUtil.getHoveredColor(ClientManagers.moduleManager.clickGui.hoverTextColor.getColorInt(b), 0.2, n5, n6, n - 5, n2, this.cachedNameWidth + 10, n4 + 2);
      } else {
         if (this.isSharedWithFriends()) {
            n7 = sharedConfigColor.getRGB();
         } else {
            byte b2 = 0;
            n7 = ClientManagers.moduleManager.clickGui.textColor.getColorInt(b2);
         }

         n7 = RenderUtil.getHoveredColor(n7, 0.2, n5, n6, n - 5, n2, this.cachedNameWidth + 10, n4 + 2);
      }

      int n8 = n7;
      double d = (double)n;
      double d2 = (double)n2;
      double d3 = d;
      String configName3 = this.configName;
      customFontUtil.drawString(configName3, d3, d2, n8);
      this.rendered = true;
   }

   public OnlineConfigMetadata toMetadata() {
      return new OnlineConfigMetadata(this.getConfigName(), this.updatedDate, this.updatedTime, this.author, this.editable);
   }



}
