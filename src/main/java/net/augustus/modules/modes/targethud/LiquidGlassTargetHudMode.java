package net.augustus.modules.modes.targethud;

import net.augustus.font.CustomFontUtil;
import net.augustus.font.FontManager;
import net.augustus.render.RenderUtil;
import net.augustus.render.StencilUtil;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.ColorSetting;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.utils.render.ColorUtil;
import net.augustus.utils.render.ShaderRenderPass;
import java.awt.Color;
import java.util.function.BiFunction;
import net.augustus.modules.render.TargetHud;
import net.augustus.settings.DoubleValue;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class LiquidGlassTargetHudMode extends AbstractTargetHudMode {
   private final ColorSetting glassColor;
   private final ColorSetting borderColor;
   private final ColorSetting healthGradientStart;
   private final ColorSetting healthGradientEnd;
   private final ColorSetting healthBackGroundColor;
   private final ColorSetting textColor;
   private final ColorSetting glowColor;

   public LiquidGlassTargetHudMode(TargetHud targetHud, DoubleValue doubleValue) {
       super(targetHud, doubleValue, "LiquidGlass", false);
      String str = this.getName() + "GlassColor";
      Color color = new Color(255, 255, 255, 40);
      String str2 = str;
      ColorSetting colorSetting = new ColorSetting(targetHud, str2, color, doubleValue);
      String str3 = "GlassColor";
      ColorSetting colorSetting2 = (ColorSetting)colorSetting.withDisplayName(str3);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.glassColor = (ColorSetting)colorSetting2.withVisibility(condition);
      str = this.getName() + "BorderColor";
      color = new Color(255, 255, 255, 80);
      str2 = str;
      ColorSetting colorSetting3 = new ColorSetting(targetHud, str2, color, doubleValue);
      String str4 = "BorderColor";
      ColorSetting colorSetting4 = (ColorSetting)colorSetting3.withDisplayName(str4);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.borderColor = (ColorSetting)colorSetting4.withVisibility(condition2);
      AbstractTargetHudMode.getControlFlowState2();
      String str5 = this.getName() + "HealthGradientStart";
      color = new Color(0, 122, 255);
      str2 = str5;
      ColorSetting colorSetting5 = new ColorSetting(targetHud, str2, color, doubleValue);
      String str6 = "HealthGradientStart";
      colorSetting5 = (ColorSetting)colorSetting5.withDisplayName(str6);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.healthGradientStart = (ColorSetting)colorSetting5.withVisibility(condition3);
      str5 = this.getName() + "HealthGradientEnd";
      color = new Color(52, 199, 89);
      str2 = str5;
      colorSetting5 = new ColorSetting(targetHud, str2, color, doubleValue);
      String str7 = "HealthGradientEnd";
      colorSetting5 = (ColorSetting)colorSetting5.withDisplayName(str7);
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      this.healthGradientEnd = (ColorSetting)colorSetting5.withVisibility(condition4);
      str5 = this.getName() + "HealthBackGroundColor";
      color = new Color(255, 255, 255, 30);
      str2 = str5;
      colorSetting5 = new ColorSetting(targetHud, str2, color, doubleValue);
      String str8 = "HealthBackGroundColor";
      colorSetting5 = (ColorSetting)colorSetting5.withDisplayName(str8);
      SettingVisibilityCondition condition5 = () -> this.isSelected();
      this.healthBackGroundColor = (ColorSetting)colorSetting5.withVisibility(condition5);
      str = this.getName() + "TextColor";
      color = new Color(255, 255, 255, 230);
      str2 = str;
      ColorSetting colorSetting6 = new ColorSetting(targetHud, str2, color, doubleValue);
      String str9 = "TextColor";
      ColorSetting colorSetting7 = (ColorSetting)colorSetting6.withDisplayName(str9);
      SettingVisibilityCondition condition6 = () -> this.isSelected();
      this.textColor = (ColorSetting)colorSetting7.withVisibility(condition6);
      str = this.getName() + "GlowColor";
      color = new Color(0, 122, 255, 100);
      str2 = str;
      ColorSetting colorSetting8 = new ColorSetting(targetHud, str2, color, doubleValue);
      String str10 = "GlowColor";
      ColorSetting colorSetting9 = (ColorSetting)colorSetting8.withDisplayName(str10);
      SettingVisibilityCondition condition7 = () -> this.isSelected();
      this.glowColor = (ColorSetting)colorSetting9.withVisibility(condition7);
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new LiquidGlassTargetHudMode((TargetHud)obj, (DoubleValue)obj2);
   }

   @Override
   public int getHalfWidth() {
      return 130;
   }

   @Override
   public int getHalfHeight() {
      return 50;
   }

   @Override
   public void drawHudContent(AbstractClientPlayer clientPlayer, NetworkPlayerInfo networkPlayerInfo, ScaledResolution sr) {
      boolean isUpsideDownPlayer = this.isUpsideDownPlayer(clientPlayer);
      int halfWidth = this.getHalfWidth();
      int halfHeight = this.getHalfHeight();
      byte b = 0;
      int colorInt = this.glassColor.getColorInt(b);
      BlurShadowManager.enableOneShotPass(true, ((TargetHud)this.parentModule).blurShadowSettings.isShadowActive());
      BlurShadowManager.submitRenderTask(true, shaderRenderPass -> this.renderGlassCard(colorInt, halfWidth, halfHeight, clientPlayer, networkPlayerInfo, isUpsideDownPlayer, shaderRenderPass));
      this.renderPlayerName(clientPlayer);
   }

   private void renderBorder(int n, int n2) {
      byte b = 0;
      int colorInt = this.borderColor.getColorInt(b);
      RenderUtil.drawRoundedRectOutline((float)(-n), (float)(-n2), (float)n, (float)n2, 25.0F, 1, 2, colorInt);
   }

   private void renderAvatar(AbstractClientPlayer clientPlayer, NetworkPlayerInfo networkPlayerInfo, boolean flag) {
      int hurtTime = clientPlayer.hurtTime;
      byte b = 70;
      byte b2 = -115;
      AbstractTargetHudMode.getControlFlowState2();
      byte b3 = -35;
      if (hurtTime > 0) {
         Color color = new Color(255, 59, 48, Math.min(30 * hurtTime, 255));
         int n = b2 - 5;
         int n2 = b3 - 5;
         RenderUtil.drawRoundedRect2((float)n, (float)n2, (float)(n + b + 10), (float)(n2 + b + 10), 20.0F, 1, color);
      } else {
         byte b4 = 0;
         int colorInt = this.glowColor.getColorInt(b4);
         int n3 = b2 - 3;
         int n4 = b3 - 3;
         RenderUtil.drawRoundedRect((float)n3, (float)n4, (float)(n3 + b + 6), (float)(n4 + b + 6), 18.0F, 1, colorInt);
      }

      StencilUtil.initStencilToWrite();
      StencilUtil.beginStencilWrite();
      RenderUtil.drawRoundedRectGl(
         (float)b2,
         (float)b3,
         (float)(b2 + b),
         (float)(b3 + b),
         15.0F,
         1,
         true,
         true,
         true,
         true,
         Color.white.getRGB()
      );
      StencilUtil.readStencilBuffer(1);
      GlStateManager.resetColor();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      ResourceLocation locationSkin = networkPlayerInfo.getLocationSkin();
      this.drawPlayerHead(locationSkin, clientPlayer, flag, b2, b3, b);
      StencilUtil.uninitStencilBuffer();
      if (hurtTime > 0) {
         float f = 1.0F + (float)hurtTime / 20.0F * 0.1F;
         GL11.glPushMatrix();
         GL11.glTranslatef((float)b2 + (float)b / 2.0F, (float)b3 + (float)b / 2.0F, 0.0F);
         GL11.glScalef(f, f, 1.0F);
         GL11.glTranslatef(-((float)b2 + (float)b / 2.0F), -((float)b3 + (float)b / 2.0F), 0.0F);
         Color color2 = new Color(255, 59, 48, 15 * hurtTime);
         RenderUtil.drawRoundedRect2((float)b2, (float)b3, (float)(b2 + b), (float)(b3 + b), 15.0F, 1, color2);
         GL11.glPopMatrix();
      }
   }

   private void renderHealthBar(AbstractClientPlayer clientPlayer, ShaderRenderPass shaderRenderPass) {
      int halfWidth = this.getHalfWidth();
      this.getHalfHeight();
      AbstractTargetHudMode.getControlFlowState2();
      int n = (int)((double)(halfWidth * 2) * 0.55);
      byte b = 8;
      byte b2 = -25;
      byte b3 = 15;
      if (shaderRenderPass == ShaderRenderPass.NONE) {
         float playerHealth = this.getPlayerHealth(clientPlayer);
         this.updateHealthAnimation(clientPlayer, playerHealth);
      }

      float f = this.currentHealth / clientPlayer.getMaxHealth();
      f = MathHelper.clamp_float(f, 0.0F, 1.0F);
      if (shaderRenderPass == ShaderRenderPass.NONE) {
         float f2 = this.previousHealth / clientPlayer.getMaxHealth();
         f2 = MathHelper.clamp_float(f2, 0.0F, 1.0F);
         this.healthAnimation.setBounds((float)n * f2, 0.0F, (float)n * f, 0.0F);
      }

      if (shaderRenderPass != ShaderRenderPass.BLUR) {
         BlurShadowSettings blurShadowSettings = ((TargetHud)this.parentModule).blurShadowSettings;
         byte b4 = 0;
         int colorInt = this.healthBackGroundColor.getColorInt(b4);
         int n2 = blurShadowSettings.resolvePassColor(shaderRenderPass, colorInt);
         RenderUtil.drawRoundedRect((float)b2, (float)b3, (float)(b2 + n), (float)(b3 + b), 4.0F, 1, n2);
         float f3 = MathHelper.clamp_float(this.healthAnimation.updateX(), 0.0F, (float)n);
         if (f3 > 1.0F) {
            this.drawGradientHealthFill(b2, b3, f3, b, shaderRenderPass);
         }
      }
   }

   private void drawGradientHealthFill(int n, int n2, float f, int n3, ShaderRenderPass shaderRenderPass) {
      BlurShadowSettings blurShadowSettings = ((TargetHud)this.parentModule).blurShadowSettings;
      byte b = 0;
      int colorInt = this.healthGradientStart.getColorInt(b);
      int n4 = blurShadowSettings.resolvePassColor(shaderRenderPass, colorInt);
      blurShadowSettings = ((TargetHud)this.parentModule).blurShadowSettings;
      byte b2 = 0;
      int colorInt2 = this.healthGradientEnd.getColorInt(b2);
      int n5 = blurShadowSettings.resolvePassColor(shaderRenderPass, colorInt2);
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.shadeModel(7425);
      float f2 = (float)(n4 >> 16 & 0xFF) / 255.0F;
      float f3 = (float)(n4 >> 8 & 0xFF) / 255.0F;
      float f4 = (float)(n4 & 0xFF) / 255.0F;
      float f5 = (float)(n4 >> 24 & 0xFF) / 255.0F;
      float f6 = (float)(n5 >> 16 & 0xFF) / 255.0F;
      float f7 = (float)(n5 >> 8 & 0xFF) / 255.0F;
      float f8 = (float)(n5 & 0xFF) / 255.0F;
      float f9 = (float)(n5 >> 24 & 0xFF) / 255.0F;
      GL11.glBegin(7);
      GlStateManager.color(f2, f3, f4, f5);
      GL11.glVertex2d((double)n, (double)n2);
      GL11.glVertex2d((double)n, (double)(n2 + n3));
      GlStateManager.color(f6, f7, f8, f9);
      GL11.glVertex2d((double)((float)n + f), (double)(n2 + n3));
      GL11.glVertex2d((double)((float)n + f), (double)n2);
      GL11.glEnd();
      GlStateManager.shadeModel(7424);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   private void renderPlayerName(AbstractClientPlayer clientPlayer) {
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 1);
      String name2 = clientPlayer.getGameProfile().getName();
      int stringWidth = (int)FontManager.rubikBold32.getStringWidth(name2);
      byte b = -25;
      byte b2 = -35;
      CustomFontUtil rubikBold32 = FontManager.rubikBold32;
      double d = (double)b;
      double d2 = (double)b2;
      byte b3 = 0;
      int colorInt = this.textColor.getColorInt(b3);
      double d3 = d2;
      double d4 = d;
      rubikBold32.drawString(name2, d4, d3, colorInt);
   }

   private void renderGlassCard(int n, int n2, int n3, AbstractClientPlayer clientPlayer, NetworkPlayerInfo networkPlayerInfo, boolean flag, ShaderRenderPass shaderRenderPass) {
      BlurShadowSettings blurShadowSettings = ((TargetHud)this.parentModule).blurShadowSettings;
      int n4 = ColorUtil.resolvePassColor(shaderRenderPass, n);
      int n5 = blurShadowSettings.resolvePassColor(shaderRenderPass, n4);
      AbstractTargetHudMode.getControlFlowState2();
      RenderUtil.drawRoundedRect((float)(-n2), (float)(-n3), (float)n2, (float)n3, 25.0F, 1, n5);
      if (shaderRenderPass == ShaderRenderPass.NONE) {
         this.renderBorder(n2, n3);
      }

      if (shaderRenderPass == ShaderRenderPass.NONE) {
         this.renderAvatar(clientPlayer, networkPlayerInfo, flag);
      }

      this.renderHealthBar(clientPlayer, shaderRenderPass);
   }

}
