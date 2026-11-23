package net.augustus.utils.render;

import net.augustus.utils.shader.ShaderProgram;
import net.augustus.utils.interfaces.MC;
import net.minecraft.client.shader.Framebuffer;

public class SeparableBlurPass implements BlurPass, MC {
   private final Framebuffer outputFramebuffer;
   private final Framebuffer intermediateFramebuffer;
   private final ShaderProgram blurShader;

   public void applyMaskedBlur(Framebuffer framebuffer, Framebuffer framebuffer2, int n) {
      if (this.blurShader != null) {
         this.intermediateFramebuffer.framebufferClear();
         float f = (float)n;
         boolean flag = true;
         BlurPassDirection horizontal = BlurPassDirection.HORIZONTAL;
         Framebuffer framebuffer3 = this.intermediateFramebuffer;
         float f2 = f;
         this.renderDirectionalPass(f2, framebuffer3, framebuffer, horizontal, framebuffer2, flag);
         f = (float)n;
         boolean flag2 = false;
         BlurPassDirection vertical = BlurPassDirection.VERTICAL;
         Framebuffer framebuffer4 = this.intermediateFramebuffer;
         float f3 = f;
         this.renderDirectionalPass(f3, framebuffer, framebuffer4, vertical, framebuffer2, flag2);
      }
   }

   public SeparableBlurPass(ShaderProgram shaderProgram) {
       super();
      this.outputFramebuffer = new ResizableFramebuffer(mc.displayWidth, mc.displayHeight, true);
      this.intermediateFramebuffer = new ResizableFramebuffer(mc.displayWidth, mc.displayHeight, true);
      this.blurShader = shaderProgram;
   }

   public Framebuffer applyBlurToFramebuffer(Framebuffer framebuffer, int n) {
      return this.outputFramebuffer;
   }

   public void renderDirectionalPass(float f, Framebuffer framebuffer, Framebuffer framebuffer2, BlurPassDirection blurPassDirection, Framebuffer framebuffer3, boolean flag) {
      framebuffer.bindFramebuffer(true);
      this.blurShader.bind();
      String str = "power";
      this.blurShader.setUniform1f(str, f);
      String str2 = "tex";
      this.blurShader.bindTextureUniform(str2, framebuffer2);
      float f2 = (float)blurPassDirection.x;
      float f3 = (float)blurPassDirection.y;
      float f4 = f2;
      String str3 = "direction";
      this.blurShader.setUniform2f(str3, f4, f3);
      f2 = (float)framebuffer.framebufferTextureWidth;
      float f5 = (float)framebuffer.framebufferTextureHeight;
      float f6 = f2;
      String str4 = "resolution";
      this.blurShader.setUniform2f(str4, f6, f5);
      String str5 = "mask";
      this.blurShader.bindTextureUniform(str5, framebuffer3);
      int n = flag ? 1 : 0;
      String str6 = "isFirstPass";
      this.blurShader.setUniform1i(str6, n);
      float f7 = (float)framebuffer.framebufferTextureWidth;
      float f8 = (float)framebuffer.framebufferTextureHeight;
      float f9 = f7;
      float f10 = 0.0F;
      float f11 = 0.0F;
      this.blurShader.drawQuad(f11, f10, f9, f8);
      this.blurShader.unbind();
   }




}
