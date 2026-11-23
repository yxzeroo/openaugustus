package net.augustus.clickgui;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.clickgui.components.TextFieldComponent;
import net.augustus.config.ClipboardConfigCodec;
import net.augustus.font.CustomFontUtil;
import net.augustus.font.FontManager;
import net.augustus.render.RenderUtil;
import net.augustus.settings.DynamicSettingList;
import net.augustus.settings.Setting;
import net.augustus.ui.widgets.SmoothScrollBar;
import net.augustus.utils.ChatUtil;
import net.augustus.utils.audio.ClientSounds;
import net.augustus.utils.input.KeyboardInputUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.augustus.modules.FeatureBase;
import net.augustus.modules.Module;
import net.augustus.settings.DoubleValue;
import org.lwjgl.opengl.Display;

public class ModuleSettingsPanel {
   private int panelHeight;
   private int cornerRadius;
   public boolean horizontalScrollVisible = false;
   private boolean anySettingFocused;
   private int screenX;
   private int panelWidth;
   private Module currentModule;
   private CustomFontUtil settingFont;
   public boolean verticalScrollVisible = false;
   private final SmoothScrollBar horizontalScrollBar;
   private int screenY;
   private CustomFontUtil headerFont;
   private final SmoothScrollBar verticalScrollBar;

   public ModuleSettingsPanel() {
      this.currentModule = Augustus.getInstance().getModuleManager().velocity;
      this.anySettingFocused = false;
      this.settingFont = FontManager.consolas30;
      this.headerFont = FontManager.esp32;
      this.verticalScrollBar = new SmoothScrollBar();
      this.horizontalScrollBar = new SmoothScrollBar();
   }

   public void handleScrollBarClick(int n, int n2, int n3) throws IOException {
      this.verticalScrollBar.mouseClicked(n, n2, n3);
      this.horizontalScrollBar.mouseClicked(n, n2, n3);
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      if (RenderUtil.isHovered((float)(this.panelWidth - 60), 4.0F, 40.0F, 17.0F, mouseX, mouseY)) {
         for (Setting setting : (Iterable<Setting>)(Augustus.getInstance().getSettingManager().getSettings())) {
            if (setting.getParentModule() == this.currentModule) {
               setting.resetToDefault();
            }
         }
      } else {
         this.currentModule.handleSettingsMouseClick(mouseX, mouseY, mouseButton);
         int n = (int)(this.headerFont.getFontHeight() + 7.0F + this.settingFont.getFontHeight() + 9.0F);

         for (Setting setting2 : (Iterable<Setting>)(this.currentModule.getSettings())) {
            boolean isVisible = setting2.isVisible();
            setting2.setCachedVisible(isVisible);
            if (setting2.isVisible() && setting2.isCachedVisible()) {
               if (!setting2.isVisualOnly() && this.currentModule.isLoadFromConfig() && !Augustus.getInstance().isConfigEditingEnabled()) {
                  Augustus.getInstance().getClass();
               } else {
                  int n2 = n + this.currentModule.getScrollOffsetY();
                  boolean flag = n2 + setting2.getHeight() > -20 && n2 < this.panelHeight + 20;
                  if (flag) {
                     if (setting2.mouseClicked(mouseX, mouseY, mouseButton)) {
                        break;
                     }
                  }

                  n += setting2.getHeight();
               }
            }
         }
      }
   }

   public void drawPanel(int n, int n2, int n3, int n4, int n5, int n6, int n7, Module module, boolean flag) {
      if (module != null) {
         this.currentModule = module;
      }

      this.screenX = n5;
      this.screenY = n6;
      this.panelWidth = n3;
      this.panelHeight = n4;
      this.cornerRadius = n7;
      CustomFontUtil headerFont2 = this.headerFont;
      String str = module.getName2() + ":";
      byte b = 0;
      int colorInt = ClientManagers.moduleManager.clickGui.hoverTextColor.getColorInt(b);
      double d = 6.0;
      double d2 = 0.0;
      String str2 = str;
      headerFont2.drawString(str2, d2, d, colorInt);
      headerFont2 = this.settingFont;
      double d3 = (double)(n3 - 60);
      byte b2 = 0;
      int hoveredColor = RenderUtil.getHoveredColor(ClientManagers.moduleManager.clickGui.textColor.getColorInt(b2), 0.2, n, n2, n3 - 60, 4, 40, 17);
      double d4 = 4.0;
      double d5 = d3;
      String str3 = "Reset";
      headerFont2.drawString(str3, d5, d4, hoveredColor);
      int n8 = (int)(this.headerFont.getFontHeight() + 10.0F);
      CustomFontUtil settingFont2 = this.settingFont;
      int n9 = n8;
      byte b3 = 0;
      int n10 = module.renderSettingsHeader(b3, n9, n, n2, settingFont2, flag);
      int n11 = (int)(this.headerFont.getFontHeight() + 7.0F + this.settingFont.getFontHeight() + 9.0F);
      RenderUtil.scissorRectScaled(
         (float)(this.screenX - 3), (float)(this.screenY + n11 - 5), (float)(this.screenX + n3), (float)(this.screenY + this.panelHeight), 1.0
      );
      int n12 = (int)(this.headerFont.getFontHeight() + 7.0F + this.settingFont.getFontHeight() + 9.0F);

      for (Setting setting : (Iterable<Setting>)(module.getSettings())) {
         boolean isVisible = setting.isVisible();
         setting.setCachedVisible(isVisible);
         if (setting.isVisible() && setting.isCachedVisible() && (setting.isVisualOnly() || !module.isLoadFromConfig() || Augustus.getInstance().isConfigEditingEnabled())) {
            int n13 = n12 + module.getScrollOffsetY();
            boolean flag2 = n13 + setting.getHeight() > -20 && n13 < n4 + 20;
            int scrollOffsetX = module.getScrollOffsetX();
            int n14 = n12 + module.getScrollOffsetY();
            CustomFontUtil settingFont3 = this.settingFont;
            int n15 = n14;
            int n16 = scrollOffsetX;
            setting.render(n, n2, n16, n15, n3, n4, settingFont3, flag2);
            n12 += setting.getHeight();
            if (n10 < setting.getContentWidth()) {
               n10 = setting.getContentWidth();
            }
         }
      }

      for (Setting setting2 : (Iterable<Setting>)(module.getSettings())) {
         if (setting2.isVisible() && setting2.isCachedVisible() && (setting2.isVisualOnly() || !module.isLoadFromConfig() || Augustus.getInstance().isConfigEditingEnabled())) {
            setting2.renderTooltip();
         }
      }

      this.anySettingFocused = false;
      ArrayList settings = module.getSettings();
      this.checkFocusedSettings(settings);
      RenderUtil.scissorRectScaled((float)this.screenX, (float)(this.screenY + n11 - 5), (float)(this.screenX + n3), (float)(this.screenY + this.panelHeight), 1.0);
      byte b4 = 0;
      int colorInt2 = ClientManagers.moduleManager.clickGui.headerColor.getColorInt(b4);
      SmoothScrollBar verticalScrollBar2 = this.verticalScrollBar;
      int n17 = n3 - 10;
      int n18 = n11 - module.getScrollOffsetY();
      int n19 = n17;
      this.verticalScrollVisible = verticalScrollBar2.drawVerticalBar(n19, n11, n3, n4, n18, n12, colorInt2);
      SmoothScrollBar horizontalScrollBar2 = this.horizontalScrollBar;
      int n20 = n4 - 10;
      int n21 = -module.getScrollOffsetX();
      int n22 = n21;
      int n23 = n20;
      byte b5 = 0;
      this.horizontalScrollVisible = horizontalScrollBar2.drawHorizontalBar(b5, n23, n3, n4, n22, n10, colorInt2);
      if (this.horizontalScrollVisible) {
         SmoothScrollBar verticalScrollBar3 = this.verticalScrollBar;
         n21 = module.getScrollOffsetY();
         int n24 = n4 - 50;
         byte b6 = 0;
         byte b7 = 0;
         int n25 = n21;
         int n26 = verticalScrollBar3.updateScrollOffset(n12, n11, n4, n25, b7, b6, n3, n24, n, n2);
         module.setScrollOffsetY(n26);
      } else {
         SmoothScrollBar verticalScrollBar4 = this.verticalScrollBar;
         n21 = module.getScrollOffsetY();
         byte b8 = 0;
         byte b9 = 0;
         int n27 = n21;
         int n28 = verticalScrollBar4.updateScrollOffset(n12, n11, n4, n27, b9, b8, n3, n4, n, n2);
         module.setScrollOffsetY(n28);
      }

      if (this.verticalScrollVisible) {
         SmoothScrollBar horizontalScrollBar3 = this.horizontalScrollBar;
         n21 = module.getScrollOffsetX();
         int n29 = n4 - 50;
         int n30 = n29;
         byte b10 = 0;
         int n31 = n21;
         byte b11 = 0;
         int n32 = horizontalScrollBar3.updateScrollOffset(n10, b11, n3, n31, b10, n30, n3, n4, n, n2);
         int n33 = n32;
         module.setScrollOffsetX(n33);
      } else {
         SmoothScrollBar horizontalScrollBar4 = this.horizontalScrollBar;
         n21 = module.getScrollOffsetX();
         byte b12 = 0;
         byte b13 = 0;
         int n34 = n21;
         byte b14 = 0;
         int n35 = horizontalScrollBar4.updateScrollOffset(n10, b14, n3, n34, b13, b12, n3, n4, n, n2);
         int n36 = n35;
         module.setScrollOffsetX(n36);
      }
   }

   public boolean isAnySettingFocused() {
      return this.anySettingFocused;
   }

   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

      for (Setting setting : (Iterable<Setting>)(this.currentModule.getSettings())) {
         if (setting.isVisible() && setting.isCachedVisible()) {
            setting.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
         }
      }
   }

   public void keyTyped(char typedChar, int keyCode) throws IOException {
      this.currentModule.handleSettingsKeyTyped(typedChar, keyCode);
      if (this.currentModule != null && KeyboardInputUtil.isCtrlKeyCombo(keyCode) && Display.isActive()) {
         if (!Augustus.getInstance().isConfigEditingEnabled()) {
            Augustus.getInstance().getClass();
         } else if (!TextFieldComponent.anyFieldFocused) {
            switch (keyCode) {
               case 45:
               case 46:
                  boolean flag = ClipboardConfigCodec.exportModule(this.currentModule);
                  if (!flag) {
                     ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE6);
                     ChatUtil.sendChat3("Failed to export " + ((FeatureBase)this.currentModule).getName() + " to clipboard");
                  }
                  break;
               case 47:
                  boolean flag2 = ClipboardConfigCodec.importModule(this.currentModule);
                  if (!flag2) {
                     ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE6);
                     ChatUtil.sendChat3("Failed to import " + ((FeatureBase)this.currentModule).getName() + " to clipboard");
                  }
            }
         }
      }

      for (Setting setting : (Iterable<Setting>)(this.currentModule.getSettings())) {
         setting.keyTyped(typedChar, keyCode);
      }
   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
      this.verticalScrollBar.mouseReleased(mouseX, mouseY, state);
      this.horizontalScrollBar.mouseReleased(mouseX, mouseY, state);

      for (Setting setting : (Iterable<Setting>)(this.currentModule.getSettings())) {
         if (setting.isVisible() && setting.isCachedVisible()) {
            setting.mouseReleased(mouseX, mouseY, state);
         }
      }
   }

   public void onClose() {

      for (Setting setting : (Iterable<Setting>)(this.currentModule.getSettings())) {
         if (setting.isVisible() && setting.isCachedVisible()) {
            setting.onGuiClosed();
         }
      }
   }

   private void checkFocusedSettings(List list) {

      for (Setting setting : (Iterable<Setting>)(list)) {
         if (this.anySettingFocused) {
            return;
         }

         if (setting.isVisible()) {
            if (setting.isFocused()) {
               this.anySettingFocused = true;
               return;
            }

            if (setting instanceof DoubleValue || setting instanceof DynamicSettingList) {
               if (setting instanceof DoubleValue) {
                  ArrayList children = ((DoubleValue)setting).getChildren();
                  this.checkFocusedSettings(children);
               } else {
                  List entries = ((DynamicSettingList)setting).getEntries();
                  this.checkFocusedSettings(entries);
               }
            }
         }
      }
   }

}
