package net.augustus.modules.modes.targetesp;

import net.augustus.render.RenderUtil;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.utils.render.ColorUtil;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.modules.render.TargetESP;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

public class HitBoxTargetEspMode extends AbstractColoredTargetEspMode {
   private final StringValue hitBoxLineWidth;

   public HitBoxTargetEspMode(TargetESP targetESP, DoubleValue doubleValue) {
       super(targetESP, doubleValue, "HitBox");
      StringValue stringValue = new StringValue(this.parentModule, "HitBoxLineWidth", 0.0, 0.0, 12.0, 0, this.modeSetting);
      String str = "LineWidth";
      StringValue stringValue2 = stringValue.named(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.hitBoxLineWidth = stringValue2.visibleWhen(condition);
   }

   @Override
   public void updateEventSubscriptions() {
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void renderTargetEsp(EntityLivingBase entity) {
      float interpolatedRenderX = this.getInterpolatedRenderX(entity);
      float interpolatedRenderY = this.getInterpolatedRenderY(entity);
      float interpolatedRenderZ = this.getInterpolatedRenderZ(entity);
      int targetColor = this.getTargetColor(entity);
      float collisionBorderSize = entity.getCollisionBorderSize();
      float f = entity.width / 2.0F + collisionBorderSize;
      float f2 = entity.height + collisionBorderSize;
      boolean isBlurActive = ((TargetESP)this.parentModule).blurShadowSettings.isBlurActive();
      BlurShadowManager.enableOneShotPass(isBlurActive, ((TargetESP)this.parentModule).blurShadowSettings.isShadowActive());
      BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderHitBoxPass(targetColor, interpolatedRenderX, f, interpolatedRenderY, collisionBorderSize, interpolatedRenderZ, f2, shaderRenderPass));
   }

   private void renderHitBoxPass(int n, float f, float f2, float f3, float f4, float f5, float f6, ShaderRenderPass shaderRenderPass) {
      BlurShadowSettings blurShadowSettings = ((TargetESP)this.parentModule).blurShadowSettings;
      int n2 = ColorUtil.resolvePassColor(shaderRenderPass, n);
      RenderUtil.setGlColor(blurShadowSettings.resolvePassColor(shaderRenderPass, n2));
      RenderUtil.drawFilledBox2(f - f2, f3 - f4, f5 - f2, f + f2, f3 + f6, f5 + f2);
      if (this.hitBoxLineWidth.getValue() > 0.0) {
         GL11.glLineWidth((float)(this.hitBoxLineWidth.getValue() / 2.0));
         RenderUtil.setGlColorWithAlpha(n, 1.0F);
         RenderUtil.drawWireframeBox2(f - f2, f3 - f4, f5 - f2, f + f2, f3 + f6, f5 + f2);
      }
   }

}
