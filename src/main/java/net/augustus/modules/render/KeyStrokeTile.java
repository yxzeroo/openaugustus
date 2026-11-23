package net.augustus.modules.render;

import net.augustus.ClientManagers;
import net.augustus.render.RenderUtil;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.utils.animation.AnimationUtil;
import net.augustus.utils.animation.EasingType;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.utils.render.ColorUtil;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.utils.interfaces.MC;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;

public class KeyStrokeTile {
   private final String label;
   private final KeyBinding keyBinding;
   private final int offsetX;
   private final int offsetY;
   private final int width;
   private final int height;
   private final FontRenderer fontRenderer = MC.mc.fontRendererObj;
   private boolean wasKeyDown = false;
   private final AnimationUtil pressAnimation = new AnimationUtil(0.0F, 0.0F, 0.0F, 0.0F);

   public KeyStrokeTile(String str, KeyBinding keyBinding2, int n, int n2, int n3, int n4) {
      this.label = str;
      this.keyBinding = keyBinding2;
      this.offsetX = n;
      this.offsetY = n2;
      this.width = n3;
      this.height = n4;
   }

   public void render(int n, int n2) {
      if (this.wasKeyDown != this.keyBinding.isKeyDown()) {
         this.pressAnimation.start((int)ClientManagers.moduleManager.keyStrokes.fadeSpeed.getValue(), EasingType.easeInOutSine, this.keyBinding.isKeyDown() ? 1 : 0, false);
      }

      this.wasKeyDown = this.keyBinding.isKeyDown();
      float easedProgress = (float)this.pressAnimation.getEasedProgress();
      byte b = 0;
      int colorInt = ClientManagers.moduleManager.keyStrokes.backgroundColorPressed.getColorInt(b);
      byte b2 = 0;
      int n3 = ColorUtil.lerpRgba(colorInt, ClientManagers.moduleManager.keyStrokes.backgroundColor.getColorInt(b2), easedProgress);
      byte b3 = 0;
      colorInt = ClientManagers.moduleManager.keyStrokes.keyColorPressed.getColorInt(b3);
      byte b4 = 0;
      int n4 = ColorUtil.lerpRgba(colorInt, ClientManagers.moduleManager.keyStrokes.keyColor.getColorInt(b4), easedProgress);
      boolean isBlurActive = ClientManagers.moduleManager.keyStrokes.blurShadowSettings.isBlurActive();
      BlurShadowManager.enableOneShotPass(isBlurActive, ClientManagers.moduleManager.keyStrokes.blurShadowSettings.isShadowActive());
      BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderBackgroundPass(n, n2, n3, shaderRenderPass));
      this.fontRenderer
         .drawString2(
            this.label,
            (float)(n + this.offsetX) + (float)this.width / 2.0F - (float)this.fontRenderer.getStringWidth(this.label) / 2.0F + 0.5F,
            (float)(n2 + this.offsetY) + (float)this.height / 2.0F - 3.5F,
            n4
         );
   }

   public String getLabel() {
      return this.label;
   }

   public KeyBinding getKeyBinding() {
      return this.keyBinding;
   }

   public int getOffsetX() {
      return this.offsetX;
   }

   public int getOffsetY() {
      return this.offsetY;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   private void renderBackgroundPass(int n, int n2, int n3, ShaderRenderPass shaderRenderPass) {
      int n4 = n + this.offsetX;
      int n5 = n2 + this.offsetY;
      int n6 = n + this.offsetX + this.width;
      int n7 = n2 + this.offsetY + this.height;
      BlurShadowSettings blurShadowSettings = ClientManagers.moduleManager.keyStrokes.blurShadowSettings;
      int n8 = ColorUtil.resolvePassColor(shaderRenderPass, n3);
      RenderUtil.drawRect3(n4, n5, n6, n7, blurShadowSettings.resolvePassColor(shaderRenderPass, n8));
   }
}
