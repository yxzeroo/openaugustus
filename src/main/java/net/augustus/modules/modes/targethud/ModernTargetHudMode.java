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

public class ModernTargetHudMode extends AbstractTargetHudMode {
   private final ColorSetting backGroundColor;
   private final ColorSetting healthBackGroundColor;
   private final ColorSetting healthColor;
   private final ColorSetting headDamageColor;
   private final ColorSetting textColor;

   public ModernTargetHudMode(TargetHud targetHud, DoubleValue doubleValue) {
       super(targetHud, doubleValue, "Modern", false);
      AbstractTargetHudMode.getControlFlowState2();
      String str = this.getName() + "BackGroundColor";
      Color color = new Color(0, 0, 0, 100);
      String str2 = str;
      ColorSetting colorSetting = new ColorSetting(targetHud, str2, color, doubleValue);
      String str3 = "BackGroundColor";
      colorSetting = (ColorSetting)colorSetting.withDisplayName(str3);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.backGroundColor = (ColorSetting)colorSetting.withVisibility(condition);
      String str4 = this.getName() + "HealthBackGroundColor";
      color = new Color(0, 0, 0);
      str2 = str4;
      ColorSetting colorSetting2 = new ColorSetting(targetHud, str2, color, doubleValue);
      String str5 = "HealthBackGroundColor";
      ColorSetting colorSetting3 = (ColorSetting)colorSetting2.withDisplayName(str5);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.healthBackGroundColor = (ColorSetting)colorSetting3.withVisibility(condition2);
      str4 = this.getName() + "HealthColor";
      color = new Color(255, 0, 0);
      str2 = str4;
      ColorSetting colorSetting4 = new ColorSetting(targetHud, str2, color, doubleValue);
      String str6 = "HealthColor";
      ColorSetting colorSetting5 = (ColorSetting)colorSetting4.withDisplayName(str6);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.healthColor = (ColorSetting)colorSetting5.withVisibility(condition3);
      str4 = this.getName() + "HeadDamageColor";
      color = new Color(255, 0, 0);
      str2 = str4;
      ColorSetting colorSetting6 = new ColorSetting(targetHud, str2, color, doubleValue);
      String str7 = "HeadDamageColor";
      ColorSetting colorSetting7 = (ColorSetting)colorSetting6.withDisplayName(str7);
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      this.headDamageColor = (ColorSetting)colorSetting7.withVisibility(condition4);
      str4 = this.getName() + "TextColor";
      color = new Color(255, 255, 255);
      str2 = str4;
      ColorSetting colorSetting8 = new ColorSetting(targetHud, str2, color, doubleValue);
      String str8 = "TextColor";
      ColorSetting colorSetting9 = (ColorSetting)colorSetting8.withDisplayName(str8);
      SettingVisibilityCondition condition5 = () -> this.isSelected();
      this.textColor = (ColorSetting)colorSetting9.withVisibility(condition5);
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new ModernTargetHudMode((TargetHud)obj, (DoubleValue)obj2);
   }

   @Override
   public int getHalfWidth() {
      return 120;
   }

   @Override
   public int getHalfHeight() {
      return 40;
   }

   @Override
   public void drawHudContent(AbstractClientPlayer clientPlayer, NetworkPlayerInfo networkPlayerInfo, ScaledResolution sr) {
      boolean isUpsideDownPlayer = this.isUpsideDownPlayer(clientPlayer);
      int halfWidth = this.getHalfWidth();
      int halfHeight = this.getHalfHeight();
      byte b = 0;
      int colorInt = this.backGroundColor.getColorInt(b);
      boolean isBlurActive = ((TargetHud)this.parentModule).blurShadowSettings.isBlurActive();
      BlurShadowManager.enableOneShotPass(isBlurActive, ((TargetHud)this.parentModule).blurShadowSettings.isShadowActive());
      BlurShadowManager.submitRenderTask(true, shaderRenderPass -> {
         int[] controlFlowState22 = AbstractTargetHudMode.getControlFlowState2();
         float f = (float)(-halfWidth);
         float f2 = (float)(-halfHeight);
         float f3 = (float)halfWidth;
         float f4 = (float)halfHeight;
         BlurShadowSettings blurShadowSettings = ((TargetHud)this.parentModule).blurShadowSettings;
         int n = ColorUtil.resolvePassColor(shaderRenderPass, colorInt);
         RenderUtil.drawRoundedRect(f, f2, f3, f4, 20.0F, 1, blurShadowSettings.resolvePassColor(shaderRenderPass, n));
         if (shaderRenderPass == ShaderRenderPass.NONE) {
            this.renderAvatar(clientPlayer, isUpsideDownPlayer);
         }

         this.renderHealthBar(clientPlayer, shaderRenderPass);
      });
      this.renderPlayerName(clientPlayer);
   }

   private void renderAvatar(AbstractClientPlayer clientPlayer, boolean flag) {
      int hurtTime = clientPlayer.hurtTime;
      int n = 66 - hurtTime;
      int n2 = -113 + hurtTime / 2;
      int n3 = -33 + hurtTime / 2;
      StencilUtil.initStencilToWrite();
      StencilUtil.beginStencilWrite();
      AbstractTargetHudMode.getControlFlowState2();
      RenderUtil.drawRoundedRectGl(
         (float)n2,
         (float)n3,
         (float)(n2 + n),
         (float)(n3 + n),
         15.0F,
         1,
         true,
         true,
         true,
         true,
         Color.white.getRGB()
      );
      StencilUtil.readStencilBuffer(1);
      ResourceLocation locationSkin = clientPlayer.getLocationSkin();
      this.drawPlayerHead(locationSkin, clientPlayer, flag, n2, n3, n);
      StencilUtil.uninitStencilBuffer();
      if (hurtTime > 0) {
         byte b = 0;
         int colorInt = this.headDamageColor.getColorInt(b);
         RenderUtil.drawRoundedRect(
            (float)n2,
            (float)n3,
            (float)(n2 + n),
            (float)(n3 + n),
            15.0F,
            1,
            ColorUtil.withAlpha(colorInt, 20 * hurtTime)
         );
      }
   }

   private void renderHealthBar(AbstractClientPlayer clientPlayer, ShaderRenderPass shaderRenderPass) {
      int[] controlFlowState22 = AbstractTargetHudMode.getControlFlowState2();
      short s = 147;
      if (shaderRenderPass == ShaderRenderPass.NONE) {
         float playerHealth = this.getPlayerHealth(clientPlayer);
         this.updateHealthAnimation(clientPlayer, playerHealth);
      }

      float f = this.currentHealth / clientPlayer.getMaxHealth();
      if (shaderRenderPass == ShaderRenderPass.NONE) {
         float f2 = this.previousHealth / clientPlayer.getMaxHealth();
         this.healthAnimation.setBounds((float)s * f2, 0.0F, (float)s * f, 0.0F);
      }

      if (shaderRenderPass != ShaderRenderPass.BLUR
         && (float)s * f > -40.0F
         && mc.getCurrentServerData() != null
         && !mc.getCurrentServerData().serverIP.contains("gommehd.net")) {
         BlurShadowSettings blurShadowSettings = ((TargetHud)this.parentModule).blurShadowSettings;
         byte b = 0;
         int colorInt = this.healthBackGroundColor.getColorInt(b);
         RenderUtil.drawRoundedRect(-37.0F, 0.0F, 110.0F, 20.0F, 8.0F, 1, blurShadowSettings.resolvePassColor(shaderRenderPass, colorInt));
         float f3 = MathHelper.clamp_float(-37.0F + this.healthAnimation.updateX(), -37.0F, 110.0F);
         blurShadowSettings = ((TargetHud)this.parentModule).blurShadowSettings;
         byte b2 = 0;
         int colorInt2 = this.healthColor.getColorInt(b2);
         RenderUtil.drawRoundedRect(-37.0F, 0.0F, f3, 20.0F, 8.0F, 1, blurShadowSettings.resolvePassColor(shaderRenderPass, colorInt2));
      }
   }

   private void renderPlayerName(AbstractClientPlayer clientPlayer) {
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 1);
      CustomFontUtil rubikBold32 = FontManager.rubikBold32;
      String name2 = clientPlayer.getGameProfile().getName();
      String str = name2;
      int stringWidth = (int)rubikBold32.getStringWidth(str);
      rubikBold32 = FontManager.rubikBold32;
      name2 = clientPlayer.getGameProfile().getName();
      double d = (double)((int)(33.0F + (float)(-stringWidth) / 2.0F));
      byte b = 0;
      int colorInt = this.textColor.getColorInt(b);
      double d2 = -25.0;
      double d3 = d;
      String str2 = name2;
      rubikBold32.drawString(str2, d3, d2, colorInt);
   }

}
