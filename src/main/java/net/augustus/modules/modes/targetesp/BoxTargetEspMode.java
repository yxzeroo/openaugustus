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

public class BoxTargetEspMode extends AbstractColoredTargetEspMode {
   private final StringValue boxLineWidth;
   private final StringValue boxAdjustWidth;
   private final StringValue boxHeight;
   private final StringValue boxAdjustYPos;

   public BoxTargetEspMode(TargetESP targetESP, DoubleValue doubleValue) {
       super(targetESP, doubleValue, "Box");
      StringValue stringValue = new StringValue(this.parentModule, "BoxLineWidth", 0.0, 0.0, 12.0, 0, this.modeSetting);
      String str = "LineWidth";
      StringValue stringValue2 = stringValue.named(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.boxLineWidth = stringValue2.visibleWhen(condition);
      StringValue stringValue3 = new StringValue(this.parentModule, "BoxAdjustWidth", -0.1, -1.0, 1.0, 2, this.modeSetting);
      String str2 = "AdjustWidth";
      StringValue stringValue4 = stringValue3.named(str2);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.boxAdjustWidth = stringValue4.visibleWhen(condition2);
      StringValue stringValue5 = new StringValue(this.parentModule, "BoxHeight", 0.08, 0.0, 0.5, 2, this.modeSetting);
      String str3 = "Height";
      StringValue stringValue6 = stringValue5.named(str3);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.boxHeight = stringValue6.visibleWhen(condition3);
      StringValue stringValue7 = new StringValue(this.parentModule, "BoxAdjustYPos", 0.14, -2.0, 2.0, 2, this.modeSetting);
      String str4 = "AdjustYPos";
      StringValue stringValue8 = stringValue7.named(str4);
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      this.boxAdjustYPos = stringValue8.visibleWhen(condition4);
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
      boolean isBlurActive = ((TargetESP)this.parentModule).blurShadowSettings.isBlurActive();
      BlurShadowManager.enableOneShotPass(isBlurActive, ((TargetESP)this.parentModule).blurShadowSettings.isShadowActive());
      BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderBoxPass(targetColor, entity, interpolatedRenderX, interpolatedRenderY, interpolatedRenderZ, shaderRenderPass));
   }

   private void renderBoxPass(int n, EntityLivingBase entity, float f, float f2, float f3, ShaderRenderPass shaderRenderPass) {
      BlurShadowSettings blurShadowSettings = ((TargetESP)this.parentModule).blurShadowSettings;
      int n2 = ColorUtil.resolvePassColor(shaderRenderPass, n);
      RenderUtil.setGlColor(blurShadowSettings.resolvePassColor(shaderRenderPass, n2));
      float f4 = f2 + entity.height + (float)this.boxAdjustYPos.getValue();
      double d = (double)entity.width + this.boxAdjustWidth.getValue();
      double value = this.boxHeight.getValue();
      double d2 = d;
      float f5 = f4;
      RenderUtil.drawRotatedEntityBox(entity, f, f5, f3, d2, value);
      if (this.boxLineWidth.getValue() > 0.0) {
         GL11.glLineWidth((float)(this.boxLineWidth.getValue() / 2.0));
         RenderUtil.setGlColorWithAlpha(n, 1.0F);
         float f6 = f2 + entity.height + (float)this.boxAdjustYPos.getValue();
         double d3 = (double)entity.width + this.boxAdjustWidth.getValue();
         double value2 = this.boxHeight.getValue();
         double d4 = d3;
         float f7 = f6;
         RenderUtil.drawRotatedEntityBoxOutline(entity, f, f7, f3, d4, value2);
      }
   }

}
