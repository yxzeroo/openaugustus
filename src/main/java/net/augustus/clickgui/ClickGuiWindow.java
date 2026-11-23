package net.augustus.clickgui;

import net.augustus.ClientManagers;
import net.augustus.config.ClientSettingsConfigFile;
import net.augustus.font.CustomFontUtil;
import net.augustus.font.FontManager;
import net.augustus.render.RenderUtil;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.property.BooleanProperty;
import net.augustus.settings.property.IntegerProperty;
import net.augustus.utils.audio.ClientSounds;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.utils.render.ColorUtil;
import net.augustus.utils.render.ShaderRenderPass;
import java.awt.Color;
import java.io.IOException;
import net.augustus.modules.AbstractFeature;
import net.augustus.modules.render.ClickGui;
import net.augustus.utils.interfaces.MC;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class ClickGuiWindow extends AbstractFeature implements MC {
   private int dragStartMouseY;
   private boolean mouseDragging;
   private final int defaultWidth;
   private CustomFontUtil titleFont;
   private String title;
   private final IntegerProperty windowHeight;
   private int lastMouseY;
   private final BooleanProperty visible;
   private final IntegerProperty windowX;
   private final IntegerProperty width;
   private final IntegerProperty height;
   private final BooleanProperty fullScreen;
   private int lastMouseX;
   private final IntegerProperty x;
   private final IntegerProperty windowWidth;
   private int dragStartMouseX;
   public boolean closed;
   private final int defaultHeight;
   private boolean resizing;
   private int dragStartWindowX;
   private boolean draggingTitleBar;
   private final IntegerProperty windowY;
   private int dragStartWindowY;
   private final IntegerProperty y;
   public int cornerRadius;
   private static int[] windowStateFlags;

   public void setHeight(int n) {
      IntegerProperty height2 = this.height;
      Integer num = n;
      Integer num2 = num;
      height2.setValue(num2);
   }

   public void setY(int n) {
      IntegerProperty integerProperty = this.y;
      Integer num = n;
      Integer num2 = num;
      integerProperty.setValue(num2);
   }

   public boolean isMouseWithinBounds(int n, int n2, int n3, int n4, int n5, int n6) {
      int[] windowStateFlags2 = getWindowStateFlags();
      return n5 >= n && n6 >= n2 && n5 < n3 && n6 < n4 && this.isVisible();
   }

   public void setX(int n) {
      IntegerProperty integerProperty = this.x;
      Integer num = n;
      Integer num2 = num;
      integerProperty.setValue(num2);
   }

   public boolean isVisible() {
      return (Boolean)this.visible.getValue();
   }

   public int getWindowHeight() {
      return (Integer)this.windowHeight.getValue();
   }

   public boolean isFullScreen() {
      return (Boolean)this.fullScreen.getValue();
   }

   public int getWindowX() {
      return (Integer)this.windowX.getValue();
   }

   public void keyTyped(char typedChar, int keyCode) throws IOException {
   }

   public void drawWindow(int n, int n2, float f) {
      int[] windowStateFlags2 = getWindowStateFlags();
      if (this.isVisible()) {
         ScaledResolution sr = new ScaledResolution(mc);
         GL11.glPushMatrix();
         GL11.glScaled(1.0 / (double)sr.getScaleFactor(), 1.0 / (double)sr.getScaleFactor(), 1.0);
         n *= sr.getScaleFactor();
         n2 *= sr.getScaleFactor();
         GL11.glPushMatrix();
         if (this.mouseDragging && !this.isFullScreen()) {
            if (this.draggingTitleBar) {
               int n3 = n - (this.dragStartMouseX - this.dragStartWindowX);
               this.setX(n3);
               int n4 = n2 - (this.dragStartMouseY - this.dragStartWindowY);
               this.setY(n4);
               int x2 = this.getX();
               this.setWindowX(x2);
               int y2 = this.getY();
               this.setWindowY(y2);
            }

            if (this.resizing) {
               int n5 = Math.max(550, n - this.getX());
               this.setWidth(n5);
               int n6 = Math.max(250, n2 - this.getY());
               this.setHeight(n6);
            }
         }

         ClickGui clickGui = ClientManagers.moduleManager.clickGui;
         n -= this.getX();
         n2 -= this.getY();
         GL11.glTranslatef((float)this.getX(), (float)this.getY(), 0.0F);
         boolean isBlurActive = ClientManagers.moduleManager.clickGui.clickGui.isBlurActive();
         BlurShadowManager.enableOneShotPass(isBlurActive, clickGui.clickGui.isShadowActive());
         byte b = 0;
         int colorInt = clickGui.backgroundColor.getColorInt(b);
         BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderBackgroundPass(colorInt, clickGui, shaderRenderPass));
         byte b2 = 0;
         int colorInt2 = clickGui.headerColor.getColorInt(b2);
         if (!this.isFullScreen() && clickGui.backgroundColor.getAlpha() > 0) {
            float f2 = (float)(this.getWidth() - 20);
            float f3 = (float)(this.getHeight() - 20);
            float width2 = (float)this.getWidth();
            float height2 = (float)this.getHeight();
            float f4 = this.isFullScreen() ? 0.0F : (float)this.cornerRadius;
            int n7 = ColorUtil.applyAlphaMask(colorInt2, 80);
            byte b3 = 1;
            float f5 = f4;
            float f6 = height2;
            float f7 = width2;
            RenderUtil.drawResizeCornerTriangle(f2, f3, f7, f6, f5, b3, n7);
         }

         byte b4 = 0;
         int colorInt3 = clickGui.textColor.getColorInt(b4);
         int n8 = RenderUtil.adjustColorBrightness(colorInt3, 0.1);
         float width3 = (float)this.getWidth();
         float f8 = this.isFullScreen() ? 0.0F : (float)this.cornerRadius;
         byte b5 = 1;
         float f9 = f8;
         RenderUtil.drawTopRoundedRect(0.0F, 0.0F, width3, 30.0F, f9, b5, colorInt2);
         int n9 = this.getWidth() - 46;
         byte b6 = 30;
         byte b7 = 46;
         byte b8 = 0;
         int n10 = n9;
         int n11 = this.isMouseOver(n10, b8, b7, b6, n, n2) ? n8 : colorInt3;
         n9 = this.getWidth() - 92;
         byte b9 = 30;
         byte b10 = 45;
         byte b11 = 0;
         int n12 = n9;
         int n13 = this.isMouseOver(n12, b11, b10, b9, n, n2) ? n8 : colorInt3;
         n9 = this.getWidth() - 138;
         byte b12 = 30;
         byte b13 = 45;
         byte b14 = 0;
         int n14 = n9;
         int n15 = this.isMouseOver(n14, b14, b13, b12, n, n2) ? n8 : colorInt3;
         n9 = this.getWidth() - 46;
         byte b15 = 30;
         byte b16 = 46;
         byte b17 = 0;
         int n16 = n9;
         if (this.isMouseOver(n16, b17, b16, b15, n, n2)) {
            RenderUtil.drawTopRightRoundedRect((float)(this.getWidth() - 46), 0.0F, (float)this.getWidth(), 30.0F, (float)this.cornerRadius, 1, new Color(237, 66, 69));
         }

         int n17 = RenderUtil.adjustColorBrightness(colorInt2, (double)0.05F);
         n9 = this.getWidth() - 92;
         byte b18 = 30;
         byte b19 = 45;
         byte b20 = 0;
         int n18 = n9;
         if (this.isMouseOver(n18, b20, b19, b18, n, n2)) {
            RenderUtil.drawRect3(this.getWidth() - 92, 0, this.getWidth() - 92 + 45, 30, n17);
         }

         n9 = this.getWidth() - 138;
         byte b21 = 30;
         byte b22 = 45;
         byte b23 = 0;
         int n19 = n9;
         if (this.isMouseOver(n19, b23, b22, b21, n, n2)) {
            RenderUtil.drawRect3(this.getWidth() - 138, 0, this.getWidth() - 138 + 45, 30, n17);
         }

         RenderUtil.drawLine2(this.getWidth() - 29, 10, this.getWidth() - 19, 20, 1.0F, n11);
         RenderUtil.drawLine2(this.getWidth() - 19, 10, this.getWidth() - 29, 20, 1.0F, n11);
         RenderUtil.drawLine2(this.getWidth() - 65, 10, this.getWidth() - 75, 10, 1.0F, n13);
         RenderUtil.drawLine2(this.getWidth() - 65, 19, this.getWidth() - 75, 19, 1.0F, n13);
         RenderUtil.drawLine2(this.getWidth() - 65, 10, this.getWidth() - 65, 19, 1.0F, n13);
         RenderUtil.drawLine2(this.getWidth() - 74, 10, this.getWidth() - 74, 19, 1.0F, n13);
         RenderUtil.drawLine2(this.getWidth() - 111, 15, this.getWidth() - 121, 15, 1.0F, n15);
         double d = 8.0;
         double d2 = 10.0;
         String title2 = this.title;
         this.titleFont.drawString(title2, d2, d, colorInt3);
         this.lastMouseX = n;
         this.lastMouseY = n2;
         GL11.glPopMatrix();
         GL11.glPopMatrix();
      }
   }

   public ClickGuiWindow(String str, int n, int n2, int n3, int n4) {
       super(ClientSettingsConfigFile.abstractFeature, str);
      this.lastMouseX = 0;
      this.lastMouseY = 0;
      this.dragStartMouseX = 0;
      this.dragStartMouseY = 0;
      this.dragStartWindowX = 0;
      this.dragStartWindowY = 0;
      this.x = new IntegerProperty(this, "X", 0);
      this.y = new IntegerProperty(this, "Y", 0);
      this.width = new IntegerProperty(this, "Width", 0);
      this.height = new IntegerProperty(this, "Height", 0);
      this.windowWidth = new IntegerProperty(this, "WindowWidth", 0);
      this.windowHeight = new IntegerProperty(this, "WindowHeight", 0);
      getWindowStateFlags();
      this.windowX = new IntegerProperty(this, "WindowX", 0);
      this.windowY = new IntegerProperty(this, "WindowY", 0);
      this.visible = new BooleanProperty(this, "Visible", false);
      this.fullScreen = new BooleanProperty(this, "FullScreen", false);
      this.mouseDragging = false;
      this.draggingTitleBar = false;
      this.resizing = false;
      this.closed = false;
      this.title = str;
      this.setWindowWidth(n3);
      this.setWindowHeight(n4);
      this.defaultWidth = n3;
      this.defaultHeight = n4;
      int windowWidth2 = this.getWindowWidth();
      this.setWidth(windowWidth2);
      int windowHeight2 = this.getWindowHeight();
      this.setHeight(windowHeight2);
      this.cornerRadius = 10;
      int n5 = (int)((float)n - (float)n3 / 2.0F);
      this.setWindowX(n5);
      int n6 = (int)((float)n2 - (float)n4 / 2.0F);
      this.setWindowY(n6);
      int windowX2 = this.getWindowX();
      this.setX(windowX2);
      int windowY2 = this.getWindowY();
      this.setY(windowY2);
      boolean flag = false;
      this.setFullScreen(flag);
      this.titleFont = FontManager.esp32;
      this.openWindow();
   }

   public void setWidth(int n) {
      IntegerProperty width2 = this.width;
      Integer num = n;
      Integer num2 = num;
      width2.setValue(num2);
   }

   public int getY() {
      return (Integer)this.y.getValue();
   }

   public void setWindowWidth(int n) {
      IntegerProperty windowWidth2 = this.windowWidth;
      Integer num = n;
      Integer num2 = num;
      windowWidth2.setValue(num2);
   }

   public int getWindowWidth() {
      return (Integer)this.windowWidth.getValue();
   }

   public int getHeight() {
      return (Integer)this.height.getValue();
   }

   public int getX() {
      return (Integer)this.x.getValue();
   }

   public void closeWindow() {
      boolean flag = false;
      this.setVisible(flag);
      boolean flag2 = false;
      this.setFullScreen(flag2);
      int windowWidth2 = this.getWindowWidth();
      this.setWidth(windowWidth2);
      int windowHeight2 = this.getWindowHeight();
      this.setHeight(windowHeight2);
      int windowX2 = this.getWindowX();
      this.setX(windowX2);
      int windowY2 = this.getWindowY();
      this.setY(windowY2);
   }

   public boolean isMouseOver(int n, int n2, int n3, int n4, int n5, int n6) {
      int[] windowStateFlags2 = getWindowStateFlags();
      return n5 >= n && n6 >= n2 && n5 < n + n3 && n6 < n2 + n4 && this.isVisible();
   }

   private void renderBackgroundPass(int n, ClickGui clickGui, ShaderRenderPass shaderRenderPass) {
      int[] windowStateFlags2 = getWindowStateFlags();
      if (shaderRenderPass != ShaderRenderPass.SHADOW) {
         float width2 = (float)this.getWidth();
         float height2 = (float)this.getHeight();
         float f = this.isFullScreen() ? 0.0F : (float)this.cornerRadius;
         byte b = 1;
         float f2 = f;
         float f3 = height2;
         float f4 = width2;
         float f5 = 0.0F;
         float f6 = 0.0F;
         RenderUtil.drawRoundedRect(f6, f5, f4, f3, f2, b, n);
      } else {
         float width3 = (float)this.getWidth();
         float height3 = (float)this.getHeight();
         float f7 = this.isFullScreen() ? 0.0F : (float)this.cornerRadius;
         BlurShadowSettings clickGui2 = clickGui.clickGui;
         int n2 = ColorUtil.resolvePassColor(shaderRenderPass, n);
         int n3 = clickGui2.resolveShadowColor(n2);
         byte b2 = 1;
         float f8 = f7;
         float f9 = height3;
         float f10 = width3;
         float f11 = 0.0F;
         float f12 = 0.0F;
         RenderUtil.drawRoundedRect(f12, f11, f10, f9, f8, b2, n3);
      }
   }

   public void setWindowY(int n) {
      IntegerProperty windowY2 = this.windowY;
      Integer num = n;
      Integer num2 = num;
      windowY2.setValue(num2);
   }

   public void setWindowX(int n) {
      IntegerProperty windowX2 = this.windowX;
      Integer num = n;
      Integer num2 = num;
      windowX2.setValue(num2);
   }

   public void setWindowHeight(int n) {
      IntegerProperty windowHeight2 = this.windowHeight;
      Integer num = n;
      Integer num2 = num;
      windowHeight2.setValue(num2);
   }

   public int getWindowY() {
      return (Integer)this.windowY.getValue();
   }

   public void setVisible(boolean flag) {
      BooleanProperty visible2 = this.visible;
      Boolean bool = flag;
      Boolean bool2 = bool;
      visible2.setValue(bool2);
   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      ScaledResolution sr = new ScaledResolution(mc);
      getWindowStateFlags();
      this.dragStartMouseX = mouseX * sr.getScaleFactor();
      this.dragStartMouseY = mouseY * sr.getScaleFactor();
      int x2 = this.getX();
      int y2 = this.getY();
      int n = this.getWidth() - 138;
      int dragStartMouseY2 = this.dragStartMouseY;
      int dragStartMouseX2 = this.dragStartMouseX;
      byte b = 30;
      int n2 = n;
      int n3 = y2;
      int n4 = x2;
      this.draggingTitleBar = this.isMouseOver(n4, n3, n2, b, dragStartMouseX2, dragStartMouseY2);
      x2 = this.getX() + this.getWidth() - 15;
      y2 = this.getY() + this.getHeight() - 15;
      int dragStartMouseY3 = this.dragStartMouseY;
      int dragStartMouseX3 = this.dragStartMouseX;
      byte b2 = 15;
      byte b3 = 15;
      int n5 = y2;
      int n6 = x2;
      this.resizing = this.isMouseOver(n6, n5, b3, b2, dragStartMouseX3, dragStartMouseY3);
      this.dragStartWindowX = this.getX();
      this.dragStartWindowY = this.getY();
      GL11.glPushMatrix();
      GL11.glScaled(1.0 / (double)sr.getScaleFactor(), 1.0 / (double)sr.getScaleFactor(), 1.0);
      mouseX *= sr.getScaleFactor();
      mouseY *= sr.getScaleFactor();
      GL11.glPushMatrix();
      mouseX -= this.getX();
      mouseY -= this.getY();
      GL11.glTranslatef((float)this.getX(), (float)this.getY(), 0.0F);
      int n7 = this.getWidth() - 46;
      byte b4 = 30;
      byte b5 = 46;
      byte b6 = 0;
      int n8 = n7;
      if (this.isMouseOver(n8, b6, b5, b4, mouseX, mouseY)) {
         RenderUtil.drawTopRightRoundedRect((float)(this.getWidth() - 46), 0.0F, (float)this.getWidth(), 30.0F, (float)this.cornerRadius, 1, new Color(232, 17, 35));
         this.closeWindow();
         this.closed = true;
         boolean flag = false;
         this.setFullScreen(flag);
         ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE);
      }

      n7 = this.getWidth() - 92;
      byte b7 = 30;
      byte b8 = 45;
      byte b9 = 0;
      int n9 = n7;
      if (this.isMouseOver(n9, b9, b8, b7, mouseX, mouseY)) {
         RenderUtil.drawRect6(this.getWidth() - 92, 0, this.getWidth() - 92 + 45, 30, 229, 229, 229);
         if (this.isFullScreen()) {
            int windowWidth2 = this.getWindowWidth();
            this.setWidth(windowWidth2);
            int windowHeight2 = this.getWindowHeight();
            this.setHeight(windowHeight2);
            int windowX2 = this.getWindowX();
            this.setX(windowX2);
            int windowY2 = this.getWindowY();
            this.setY(windowY2);
         } else {
            int framebufferWidth = mc.getFramebuffer().framebufferWidth;
            this.setWidth(framebufferWidth);
            int framebufferHeight = mc.getFramebuffer().framebufferHeight;
            this.setHeight(framebufferHeight);
            byte b10 = 0;
            this.setX(b10);
            byte b11 = 0;
            this.setY(b11);
         }

         boolean flag2 = !this.isFullScreen();
         this.setFullScreen(flag2);
         ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE);
      } else {
         n7 = this.getWidth() - 138;
         byte b12 = 30;
         byte b13 = 45;
         byte b14 = 0;
         int n10 = n7;
         if (this.isMouseOver(n10, b14, b13, b12, mouseX, mouseY)) {
            RenderUtil.drawRect6(this.getWidth() - 138, 0, this.getWidth() - 138 + 45, 30, 229, 229, 229);
            this.closeWindow();
            ClientSounds.playSound(ClientSounds.AUGUSTUS_TEXTURE);
         }

         GL11.glPopMatrix();
         GL11.glPopMatrix();
      }
   }

   public void handleKeyboardInput() throws IOException {
   }

   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      this.mouseDragging = true;
   }

   public void setFullScreen(boolean flag) {
      BooleanProperty fullScreen2 = this.fullScreen;
      Boolean bool = flag;
      Boolean bool2 = bool;
      fullScreen2.setValue(bool2);
   }

   public int getWidth() {
      return (Integer)this.width.getValue();
   }

   public void resetWindow(String str) {
      byte b = 0;
      this.setX(b);
      byte b2 = 0;
      this.setY(b2);
      byte b3 = 0;
      this.setWidth(b3);
      byte b4 = 0;
      this.setHeight(b4);
      this.mouseDragging = false;
      this.draggingTitleBar = false;
      this.resizing = false;
      boolean flag = false;
      this.setFullScreen(flag);
      boolean flag2 = false;
      this.setVisible(flag2);
      this.closed = false;
      this.title = str;
      float framebufferWidth = (float)mc.getFramebuffer().framebufferWidth;
      float framebufferHeight = (float)mc.getFramebuffer().framebufferHeight;
      int n = (int)(framebufferWidth / 2.0F);
      int n2 = (int)(framebufferHeight / 2.0F);
      this.setWindowWidth(n);
      this.setWindowHeight(n2);
      int defaultWidth2 = this.defaultWidth;
      this.setWidth(defaultWidth2);
      int defaultHeight2 = this.defaultHeight;
      this.setHeight(defaultHeight2);
      this.cornerRadius = 10;
      int n3 = (int)((float)n - (float)this.getWidth() / 2.0F);
      this.setWindowX(n3);
      int n4 = (int)((float)n2 - (float)this.getHeight() / 2.0F);
      this.setWindowY(n4);
      int windowX2 = this.getWindowX();
      this.setX(windowX2);
      int windowY2 = this.getWindowY();
      this.setY(windowY2);
      boolean flag3 = false;
      this.setFullScreen(flag3);
      this.titleFont = FontManager.esp32;
      this.openWindow();
   }

   public void mouseReleased(int mouseX, int mouseY, int state) {
      this.mouseDragging = false;
      getWindowStateFlags();
      this.draggingTitleBar = false;
      if (this.resizing) {
         int width2 = this.getWidth();
         this.setWindowWidth(width2);
         int height2 = this.getHeight();
         this.setWindowHeight(height2);
      }

      this.resizing = false;
   }

   public void openWindow() {
      boolean flag = true;
      this.setVisible(flag);
   }

   public void toggleWindow(String str) {
      int[] windowStateFlags2 = getWindowStateFlags();
      if (this.closed) {
         this.resetWindow(str);
      } else {
         boolean flag = !this.isVisible();
         this.setVisible(flag);
      }
   }

   public static void setWindowStateFlags(int[] ints) {
      windowStateFlags = ints;
   }

   public static int[] getWindowStateFlags() {
      return windowStateFlags;
   }

   static {
      setWindowStateFlags((int[])null);
   }

}
