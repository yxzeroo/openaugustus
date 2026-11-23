package net.augustus.clickgui;

import net.augustus.ClientManagers;
import net.augustus.clickgui.components.TextFieldComponent;
import net.augustus.events.listeners.GuiScaleListener;
import net.augustus.events.listeners.RenderGuiBackgroundListener;
import net.augustus.render.RenderUtil;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.SettingVisibilityRegistry;
import net.augustus.ui.UiIconResources;
import net.augustus.ui.config.ConfigSystemPanel;
import net.augustus.utils.audio.ClientSounds;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.utils.render.GlStateSaver;
import net.augustus.utils.render.ShaderRenderPass;
import java.awt.Color;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public class ClickGuiScreen extends GuiScreen {
   private int centerX;
   private final GlStateSaver glStateSaver2;
   private final MainClickGuiWindow mainWindow;
   private int centerY;
   private int framebufferHeight;
   private ClickGuiWindow activeWindow;
   private int framebufferWidth;
   public static boolean configEditable = true;
   private final ConfigSystemPanel configPanel;
   public static boolean protectedConfigCreator = false;

   private boolean isMouseOver(int n, int n2, int n3, int n4, int n5, int n6) {
      int[] controlFlowState = SettingVisibilityRegistry.getControlFlowState();
      return n5 >= n && n6 >= n2 && n5 < n + n3 && n6 < n2 + n4;
   }

   @Override
   public void handleKeyboardInput() throws IOException {
      this.mainWindow.handleKeyboardInput();
      this.configPanel.handleKeyboardInput();
      super.handleKeyboardInput();
   }

   @Override
   public void keyTyped(char typedChar, int keyCode) throws IOException {
      int[] controlFlowState = SettingVisibilityRegistry.getControlFlowState();
      if ((!this.mainWindow.selectedModule.isBindingKey() || keyCode != 1) && (keyCode != 28 && keyCode != 156 && keyCode != 1 || !this.mainWindow.getSettingsPanel().isAnySettingFocused())) {
         super.keyTyped(typedChar, keyCode);
      }

      this.mainWindow.keyTyped(typedChar, keyCode);
      this.configPanel.keyTyped(typedChar, keyCode);
   }

   public ConfigSystemPanel getConfigPanel() {
      return this.configPanel;
   }

   public MainClickGuiWindow getMainWindow() {
      return this.mainWindow;
   }

   @Override
   public void initGui() {
      super.initGui();
      SettingVisibilityRegistry.getControlFlowState();
      this.allowUserInput = true;
      if (this.mainWindow.getX() < 0 || this.mainWindow.getY() < 0 || this.configPanel.getX() < 0 || this.configPanel.getY() < 0) {
         MainClickGuiWindow mainWindow2 = this.mainWindow;
         String name = this.mainWindow.getName();
         mainWindow2.resetWindow(name);
         ConfigSystemPanel configPanel2 = this.configPanel;
         String name2 = this.configPanel.getName();
         configPanel2.resetWindow(name2);
         boolean flag = false;
         this.configPanel.setVisible(flag);
      }

      this.configPanel.onPanelOpened();
      this.activeWindow = null;
      GuiScreen.NO_TOGGLE_SCREENS.add(Minecraft.getMinecraft().currentScreen);
   }

   @Override
   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      boolean flag = false;
      this.glStateSaver2.setAlpha(flag);
      SettingVisibilityRegistry.getControlFlowState();
      TextFieldComponent.anyFieldFocused = false;
      BlurShadowManager.disableGuiLayer();
      ScaledResolution sr = new ScaledResolution(this.mc);
      if (this.mainWindow.isVisible() || this.configPanel.isVisible()) {
         RenderGuiBackgroundListener.onRenderGuiBackground2(this);
      }

      GlStateManager.pushMatrix();
      GlStateManager.scale(1.0 / (double)sr.getScaleFactor(), 1.0 / (double)sr.getScaleFactor(), 1.0);
      int n = mouseX;
      int n2 = mouseY;
      mouseX *= sr.getScaleFactor();
      mouseY *= sr.getScaleFactor();
      this.framebufferWidth = this.mc.getFramebuffer().framebufferWidth;
      this.framebufferHeight = this.mc.getFramebuffer().framebufferHeight;
      this.centerX = (int)((float)this.framebufferWidth / 2.0F);
      this.centerY = (int)((float)this.framebufferHeight / 2.0F);
      byte b = 6;
      if (!this.mainWindow.isFullScreen() && !this.configPanel.isFullScreen()) {
         boolean isBlurActive = ClientManagers.moduleManager.clickGui.clickGui.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, ClientManagers.moduleManager.clickGui.clickGui.isShadowActive());
         Color color = new Color(40, 39, 39, MathHelper.clamp_int((int)ClientManagers.moduleManager.clickGui.alpha.getValue(), 0, 255));
         BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderDockBackgroundPass(b, shaderRenderPass));
         int n3 = this.centerX - 43 - b;
         int n4 = 37 + b;
         byte b2 = 42;
         byte b3 = 0;
         int n5 = n3;
         if (this.isMouseOver(n5, b3, b2, n4, mouseX, mouseY)) {
            int x = this.configPanel.getX();
            int y = this.configPanel.getY();
            int width2 = this.configPanel.getWidth();
            int height2 = this.configPanel.getHeight();
            int n6 = width2;
            int n7 = y;
            int n8 = x;
            if (!this.isMouseOver(n8, n7, n6, height2, mouseX, mouseY) || !this.configPanel.isVisible()) {
               int x2 = this.mainWindow.getX();
               y = this.mainWindow.getY();
               width2 = this.mainWindow.getWidth();
               int height3 = this.mainWindow.getHeight();
               int n9 = width2;
               int n10 = y;
               int n11 = x2;
               if (!this.isMouseOver(n11, n10, n9, height3, mouseX, mouseY) || !this.mainWindow.isVisible()) {
                  RenderUtil.drawRoundedRect2(
                     (float)(this.centerX - 43 - b),
                     0.0F,
                     (float)(this.centerX - 1),
                     (float)(37 + b),
                     (float)b,
                     1,
                     false,
                     false,
                     true,
                     false,
                     new Color(237, 66, 69, MathHelper.clamp_int((int)ClientManagers.moduleManager.clickGui.alpha.getValue(), 0, 255))
                  );
               }
            }
         }

         int n12 = this.centerX + 1;
         int n13 = 37 + b;
         byte b4 = 42;
         byte b5 = 0;
         int n14 = n12;
         if (this.isMouseOver(n14, b5, b4, n13, mouseX, mouseY)) {
            int x3 = this.configPanel.getX();
            int y2 = this.configPanel.getY();
            int width3 = this.configPanel.getWidth();
            int height4 = this.configPanel.getHeight();
            int n15 = width3;
            int n16 = y2;
            int n17 = x3;
            if (!this.isMouseOver(n17, n16, n15, height4, mouseX, mouseY) || !this.configPanel.isVisible()) {
               int x4 = this.mainWindow.getX();
               y2 = this.mainWindow.getY();
               width3 = this.mainWindow.getWidth();
               int height5 = this.mainWindow.getHeight();
               int n18 = width3;
               int n19 = y2;
               int n20 = x4;
               if (!this.isMouseOver(n20, n19, n18, height5, mouseX, mouseY) || !this.mainWindow.isVisible()) {
                  RenderUtil.drawRoundedRect2(
                     (float)(this.centerX + 1),
                     0.0F,
                     (float)(this.centerX + 43 + b),
                     (float)(37 + b),
                     (float)b,
                     1,
                     false,
                     false,
                     false,
                     true,
                     new Color(237, 66, 69, MathHelper.clamp_int((int)ClientManagers.moduleManager.clickGui.alpha.getValue(), 0, 255))
                  );
               }
            }
         }

         if (ClientManagers.moduleManager.clickGui.outline.getValue()) {
            RenderUtil.drawRoundedRectOutline2(
               (float)(this.centerX - 43 - b),
               (float)(-b),
               (float)(this.centerX + 43 + b),
               (float)(37 + b),
               (float)b,
               1,
               1,
               color
            );
         }

         int n21 = this.centerX - 36 - 7;
         double d = 1.0;
         byte b6 = 36;
         byte b7 = 36;
         byte b8 = 2;
         int n22 = n21;
         UiIconResources.drawIcon(UiIconResources.AUGUSTUS_TEXTURE3, n22, b8, b7, b6, d);
         int n23 = this.centerX + 7;
         double d2 = 1.0;
         byte b9 = 36;
         byte b10 = 36;
         byte b11 = 2;
         int n24 = n23;
         UiIconResources.drawIcon(UiIconResources.AUGUSTUS_TEXTURE4, n24, b11, b10, b9, d2);
      }

      GlStateManager.popMatrix();
      if (this.activeWindow != null) {
         if (this.activeWindow.equals(this.mainWindow)) {
            if (!this.mainWindow.isFullScreen()) {
               this.configPanel.drawWindow(n, n2, partialTicks);
            }

            this.mainWindow.drawWindow(n, n2, partialTicks);
         } else {
            if (!this.configPanel.isFullScreen()) {
               this.mainWindow.drawWindow(n, n2, partialTicks);
            }

            this.configPanel.drawWindow(n, n2, partialTicks);
         }
      } else {
         this.configPanel.drawWindow(n, n2, partialTicks);
         this.mainWindow.drawWindow(n, n2, partialTicks);
      }

      BlurShadowManager.enableGuiLayer();
      this.glStateSaver2.restoreAlpha();
      super.drawScreen(n, n2, partialTicks);
   }

   @Override
   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      this.mainWindow.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      this.configPanel.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
   }

   private void renderDockBackgroundPass(int n, ShaderRenderPass shaderRenderPass) {
      int[] controlFlowState = SettingVisibilityRegistry.getControlFlowState();
      if (shaderRenderPass == ShaderRenderPass.SHADOW) {
         float f = (float)(this.centerX - 43 - n);
         float f2 = (float)(-n);
         float f3 = (float)(this.centerX + 43 + n);
         float f4 = (float)(37 + n);
         float f5 = (float)n;
         BlurShadowSettings clickGui = ClientManagers.moduleManager.clickGui.clickGui;
         int rgb = Color.black.getRGB();
         RenderUtil.drawRoundedRect(f, f2, f3, f4, f5, 1, clickGui.resolveShadowColor(rgb));
      } else {
         RenderUtil.drawRoundedRect2(
            (float)(this.centerX - 43 - n), (float)(-n), (float)(this.centerX + 43 + n), (float)(37 + n), (float)n, 1, new Color(40, 39, 39)
         );
      }
   }

   @Override
   public void actionPerformed(GuiButton button) throws IOException {
      super.actionPerformed(button);
      if (button.id == 1) {
         GuiScaleListener.onGuiScale2();
      }
   }

   @Override
   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      int n = mouseX;
      int n2 = mouseY;
      SettingVisibilityRegistry.getControlFlowState();
      ScaledResolution sr = new ScaledResolution(this.mc);
      mouseX *= sr.getScaleFactor();
      mouseY *= sr.getScaleFactor();
      if (this.activeWindow == null) {
         if (RenderUtil.isHovered(
            (float)this.mainWindow.getX(), (float)this.mainWindow.getY(), (float)this.mainWindow.getWidth(), (float)this.mainWindow.getHeight(), mouseX, mouseY
         )) {
            this.activeWindow = this.mainWindow;
         } else if (RenderUtil.isHovered(
            (float)this.configPanel.getX(), (float)this.configPanel.getY(), (float)this.configPanel.getWidth(), (float)this.configPanel.getHeight(), mouseX, mouseY
         )) {
            this.activeWindow = this.configPanel;
         }
      } else if (this.activeWindow.equals(this.mainWindow)) {
         if (!RenderUtil.isHovered(
               (float)this.mainWindow.getX(), (float)this.mainWindow.getY(), (float)this.mainWindow.getWidth(), (float)this.mainWindow.getHeight(), mouseX, mouseY
            )
            && RenderUtil.isHovered(
               (float)this.configPanel.getX(), (float)this.configPanel.getY(), (float)this.configPanel.getWidth(), (float)this.configPanel.getHeight(), mouseX, mouseY
            )) {
            this.activeWindow = this.configPanel;
         }
      } else if (this.activeWindow.equals(this.configPanel)
         && !RenderUtil.isHovered(
            (float)this.configPanel.getX(), (float)this.configPanel.getY(), (float)this.configPanel.getWidth(), (float)this.configPanel.getHeight(), mouseX, mouseY
         )
         && RenderUtil.isHovered(
            (float)this.mainWindow.getX(), (float)this.mainWindow.getY(), (float)this.mainWindow.getWidth(), (float)this.mainWindow.getHeight(), mouseX, mouseY
         )) {
         this.activeWindow = this.mainWindow;
      }

      if (!this.mainWindow.isVisible() && this.configPanel.isVisible()) {
         this.activeWindow = this.configPanel;
      }

      if (!this.configPanel.isVisible() && this.mainWindow.isVisible()) {
         this.activeWindow = this.mainWindow;
      }

      if (this.activeWindow != null) {
         if (this.activeWindow.equals(this.mainWindow)) {
            this.mainWindow.mouseClicked(n, n2, mouseButton);
         } else {
            this.configPanel.mouseClicked(n, n2, mouseButton);
         }
      }

      if (mouseButton == 0 && (!this.configPanel.isFullScreen() || !this.configPanel.isVisible()) && (!this.mainWindow.isFullScreen() || !this.mainWindow.isVisible())) {
         byte b = 6;
         int n3 = this.centerX - 43 - b;
         int n4 = 37 + b;
         byte b2 = 42;
         byte b3 = 0;
         int n5 = n3;
         if (this.isMouseOver(n5, b3, b2, n4, mouseX, mouseY)) {
            int x = this.configPanel.getX();
            int y = this.configPanel.getY();
            int width2 = this.configPanel.getWidth();
            int height2 = this.configPanel.getHeight();
            int n6 = width2;
            int n7 = y;
            int n8 = x;
            if (!this.isMouseOver(n8, n7, n6, height2, mouseX, mouseY) || !this.configPanel.isVisible()) {
               int x2 = this.mainWindow.getX();
               y = this.mainWindow.getY();
               width2 = this.mainWindow.getWidth();
               int height3 = this.mainWindow.getHeight();
               int n9 = width2;
               int n10 = y;
               int n11 = x2;
               if (!this.isMouseOver(n11, n10, n9, height3, mouseX, mouseY) || !this.mainWindow.isVisible()) {
                  if (!this.mainWindow.isVisible() && this.mainWindow.closed) {
                     this.activeWindow = this.mainWindow;
                  }

                  String str = "ClickGui";
                  this.mainWindow.toggleWindow(str);
                  this.activeWindow = this.mainWindow;
                  ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE);
               }
            }
         }

         int n12 = this.centerX + 1;
         int n13 = 37 + b;
         byte b4 = 42;
         byte b5 = 0;
         int n14 = n12;
         if (this.isMouseOver(n14, b5, b4, n13, mouseX, mouseY)) {
            int x3 = this.configPanel.getX();
            int y2 = this.configPanel.getY();
            int width3 = this.configPanel.getWidth();
            int height4 = this.configPanel.getHeight();
            int n15 = width3;
            int n16 = y2;
            int n17 = x3;
            if (!this.isMouseOver(n17, n16, n15, height4, mouseX, mouseY) || !this.configPanel.isVisible()) {
               int x4 = this.mainWindow.getX();
               y2 = this.mainWindow.getY();
               width3 = this.mainWindow.getWidth();
               int height5 = this.mainWindow.getHeight();
               int n18 = width3;
               int n19 = y2;
               int n20 = x4;
               if (!this.isMouseOver(n20, n19, n18, height5, mouseX, mouseY) || !this.mainWindow.isVisible()) {
                  if (!this.configPanel.isVisible() && this.configPanel.closed) {
                     this.activeWindow = this.configPanel;
                  }

                  String str2 = "Configs";
                  this.configPanel.toggleWindow(str2);
                  this.activeWindow = this.configPanel;
                  ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE);
               }
            }
         }
      }
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      this.mainWindow.updatePanel();
   }

   public ClickGuiScreen() {
       super();
      this.mainWindow = new MainClickGuiWindow("ClickGui", -1, -1, 1154, 680);
      this.configPanel = (new ConfigSystemPanel("Configs", -1, -1, 872, 380)).initPanel();
      this.glStateSaver2 = GlStateSaver.create();
      this.activeWindow = null;
      this.framebufferWidth = 0;
      this.framebufferHeight = 0;
      this.centerX = 0;
      this.centerY = 0;
   }

   public void setMc(Minecraft mc2) {
      this.mc = mc2;
   }

   @Override
   public void mouseReleased(int mouseX, int mouseY, int state) {
      this.mainWindow.mouseReleased(mouseX, mouseY, state);
      this.configPanel.mouseReleased(mouseX, mouseY, state);
      super.mouseReleased(mouseX, mouseY, state);
   }

   @Override
   public void onGuiClosed() {
      this.mainWindow.onClose();
      super.onGuiClosed();
   }

}
