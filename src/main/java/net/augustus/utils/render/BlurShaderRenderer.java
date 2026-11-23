package net.augustus.utils.render;

import net.augustus.ClientManagers;
import net.augustus.render.FramebufferStack;
import net.augustus.settings.Setting;
import net.augustus.utils.shader.ShaderProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.optifine.config.Config;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class BlurShaderRenderer {
   private final GlStateSaver glStateSaver;
   private final Minecraft mc;
   private final Framebuffer guiMaskFramebuffer;
   private final Framebuffer hudMaskFramebuffer;
   private final ShaderProgram cutoutShader;

   public BlurShaderRenderer() {
       super();
      this.glStateSaver = GlStateSaver.create();
      this.mc = Minecraft.getMinecraft();
      this.guiMaskFramebuffer = new ResizableFramebuffer(this.mc.displayWidth, this.mc.displayHeight, true);
      this.hudMaskFramebuffer = new ResizableFramebuffer(this.mc.displayWidth, this.mc.displayHeight, true);
      this.cutoutShader = new ShaderProgram(RenderSystemAssetLoader.loadAssetString("legacy.vert"), RenderSystemAssetLoader.loadAssetString("cutout.frag"));
   }

   public void endMaskCapture() {
      if (Config.isShaderPackLoaded()) {
         FramebufferStack.popFramebuffer();
      } else {
         this.mc.getFramebuffer().bindFramebuffer(Minecraft.isRunningOnMac);
      }
   }

   public void beginMaskCapture(boolean flag, boolean flag2) {
      if (Config.isShaderPackLoaded()) {
         FramebufferStack.pushFramebuffer();
      }

      if (flag2) {
         this.guiMaskFramebuffer.bindFramebuffer(flag);
      } else {
         this.hudMaskFramebuffer.bindFramebuffer(flag);
      }

      Setting.getControlFlowMarker();
   }

   public void renderBlurPass(boolean flag, int n) {
      if (Display.isActive()) {
         Framebuffer framebuffer = flag ? this.guiMaskFramebuffer : this.hudMaskFramebuffer;
         String selectedValue = ClientManagers.moduleManager.blur.modesNew.getSelectedValue();
         BlurMode blurMode = BlurMode.fromName(selectedValue);
         GL11.glPushMatrix();
         boolean flag2 = false;
         this.glStateSaver.setCull(flag2);
         boolean flag3 = true;
         this.glStateSaver.setTexture2D(flag3);
         boolean flag4 = false;
         this.glStateSaver.setBlend(flag4);
         if (blurMode == BlurMode.KAWASE) {
            Framebuffer framebuffer2 = this.mc.getFramebuffer();
            Framebuffer framebuffer3 = framebuffer2;
            Framebuffer framebuffer4 = blurMode.applyBlurToFramebuffer(framebuffer3, n);
            this.mc.getFramebuffer().bindFramebuffer(Minecraft.isRunningOnMac);
            this.cutoutShader.bind();
            String str = "shapes";
            this.cutoutShader.bindTextureUniform(str, framebuffer);
            String str2 = "tex";
            this.cutoutShader.bindTextureUniform(str2, framebuffer4);
            float f = (float)framebuffer.framebufferTextureWidth;
            float f2 = (float)framebuffer.framebufferTextureHeight;
            float f3 = f;
            String str3 = "resolution";
            this.cutoutShader.setUniform2f(str3, f3, f2);
            float f4 = (float)framebuffer.framebufferTextureWidth;
            float f5 = (float)framebuffer.framebufferTextureHeight;
            float f6 = f4;
            float f7 = 0.0F;
            float f8 = 0.0F;
            this.cutoutShader.drawQuad(f8, f7, f6, f5);
            this.cutoutShader.unbind();
         } else {
            Framebuffer framebuffer5 = this.mc.getFramebuffer();
            blurMode.applyMaskedBlur(framebuffer5, framebuffer, n);
         }

         this.guiMaskFramebuffer.framebufferClear();
         this.hudMaskFramebuffer.framebufferClear();
         this.mc.getFramebuffer().bindFramebuffer(Minecraft.isRunningOnMac);
         this.glStateSaver.restoreCull();
         this.glStateSaver.restoreTexture2D();
         this.glStateSaver.restoreBlend();
         GL11.glPopMatrix();
      }
   }




}
