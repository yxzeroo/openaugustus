package net.augustus.modules.render;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.settings.ColorSetting;
import net.augustus.utils.math.MathStatisticsUtil;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.ColorUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class HealthDisplay extends Module implements RenderListener {
   private static final int HEART_SYMBOL_WIDTH = mc.fontRendererObj.getStringWidth("❤");
   private final StringValue scale;
   private final StringValue posX;
   private final StringValue posY;
   private final StringValue decimalPlaces;
   private final StringValue spacing;
   private final BooleanValue fontShadow;
   private final BooleanValue background;
   private final ColorSetting backgroundColor;
   private final BlurShadowSettings blurShadowSettings;
   private final ColorSetting textColor;
   private final ColorSetting heartColor;
   private final BooleansSetting absorptionMode;
   private final ColorSetting absorptionTextColor;
   private final ColorSetting absorptionHeartColor;

   public HealthDisplay() {
       super("HealthDisplay", "Lebensanzeige", new Color(85, 235, 52), Categorys.RENDER);
      this.scale = new StringValue(this, "Scale", 2.0, 0.5, 4.0, 2);
      this.posX = new StringValue(this, "PosX", 0.0, 0.0, 100.0, 2);
      this.posY = new StringValue(this, "PosY", 0.0, 0.0, 100.0, 2);
      this.decimalPlaces = new StringValue(this, "DecimalPlaces", 1.0, 0.0, 3.0, 1);
      this.spacing = new StringValue(this, "Spacing", 4.0, 0.0, 10.0, 0);
      this.fontShadow = new BooleanValue(this, "FontShadow", true);
      this.background = new BooleanValue(this, "Background", true);
      ColorSetting colorSetting = new ColorSetting(this, "BackgroundColor", new Color(0, 0, 0, 150));
      BooleanValue background2 = this.background;
      this.background.getClass();
      BooleanValue booleanValue = background2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.backgroundColor = (ColorSetting)colorSetting.withVisibility(condition);
      this.blurShadowSettings = new BlurShadowSettings(this);
      this.textColor = new ColorSetting(this, "TextColor", Color.green);
      this.heartColor = new ColorSetting(this, "HeartColor", Color.red);
      this.absorptionMode = new BooleansSetting(this, "AbsorptionMode", "Combined", new String[]{"Combined", "Separate", "Hidden"});
      ColorSetting colorSetting2 = new ColorSetting(this, "AbsorptionTextColor", Color.orange);
      SettingVisibilityCondition condition2 = () -> this.isAbsorptionTextColorVisible();
      this.absorptionTextColor = (ColorSetting)colorSetting2.withVisibility(condition2);
      ColorSetting colorSetting3 = new ColorSetting(this, "AbsorptionHeartColor", Color.yellow);
      SettingVisibilityCondition condition3 = () -> {
         return this.absorptionMode.getSelectedValue().equals("Separate");
      };
      this.absorptionHeartColor = (ColorSetting)colorSetting3.withVisibility(condition3);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 32;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      GlStateManager.pushMatrix();
      GlStateManager.scale(1.0F / (float)sr.getScaleFactor(), 1.0F / (float)sr.getScaleFactor(), 1.0F);
      String str;
      String str2;
      int n;
      if (this.absorptionMode.getSelectedValue().equals("Separate")) {
         str = MathStatisticsUtil.formatRounded((double)mc.thePlayer.getCurrentHealth(), (int)this.decimalPlaces.getValue());
         str2 = MathStatisticsUtil.formatRounded((double)mc.thePlayer.getAbsorptionAmount(), (int)this.decimalPlaces.getValue());
         int stringWidth = mc.fontRendererObj.getStringWidth(str2);
         boolean flag = true;
         n = stringWidth + (int)this.spacing.getValue(flag);
      } else {
         str = MathStatisticsUtil.formatRounded(
            (double)(mc.thePlayer.getCurrentHealth() + (this.absorptionMode.getSelectedValue().equals("Combined") ? mc.thePlayer.getAbsorptionAmount() : 0.0F)), (int)this.decimalPlaces.getValue()
         );
         str2 = null;
         n = 0;
      }

      boolean flag2 = this.absorptionMode.getSelectedValue().equals("Separate") && mc.thePlayer.getAbsorptionAmount() > 0.0F;
      int stringWidth2 = mc.fontRendererObj.getStringWidth(str);
      boolean flag3 = true;
      int n2 = stringWidth2 + (int)this.spacing.getValue(flag3);
      double d = (double)(n2 + HEART_SYMBOL_WIDTH);
      double d2;
      if (flag2) {
         boolean flag4 = true;
         d2 = this.spacing.getValue(flag4) * 2.0 + (double)n + (double)HEART_SYMBOL_WIDTH;
      } else {
         d2 = 0.0;
      }

      int n3 = (int)(d + d2);
      GlStateManager.scale(this.scale.getValue(), this.scale.getValue(), 1.0);
      int n4 = (int)((double)mc.displayWidth / this.scale.getValue());
      int n5 = (int)((double)mc.displayHeight / this.scale.getValue());
      GlStateManager.translate(
         this.posX.getValue() / 100.0 * (double)(n4 - (n3 + (this.background.getValue() ? 7 : 0))),
         this.posY.getValue() / 100.0 * (double)(n5 - (mc.fontRendererObj.FONT_HEIGHT + (this.background.getValue() ? 6 : 0))),
         0.0
      );
      boolean isBlurActive = this.blurShadowSettings.isBlurActive();
      BlurShadowManager.enableOneShotPass(isBlurActive, this.blurShadowSettings.isShadowActive());
      int n6 = n;
      String str3 = str2;
      String str4 = str;
      BlurShadowManager.submitRenderTask(false, shaderRenderPass -> this.renderHealthPass(n3, str4, n2, flag2, str3, n6, shaderRenderPass));
      GlStateManager.popMatrix();
      RenderUtil.resetGlColorState();
   }

   private void renderHealthPass(int n, String str, int n2, boolean flag, String str2, int n3, ShaderRenderPass shaderRenderPass) {
      byte b = 0;
      byte b2 = 0;
      if (this.background.getValue()) {
         b = 4;
         b2 = 4;
         int n4 = b + n + 3;
         int n5 = b2 + mc.fontRendererObj.FONT_HEIGHT + 2;
         byte b3 = 0;
         int colorInt = this.backgroundColor.getColorInt(b3);
         RenderUtil.drawRect3(0, 0, n4, n5, ColorUtil.resolvePassColor(shaderRenderPass, colorInt));
      }

      if (shaderRenderPass == ShaderRenderPass.NONE || !this.background.getValue() && shaderRenderPass != ShaderRenderPass.BLUR) {
         byte b4 = 0;
         mc.fontRendererObj.drawStringInt(str, b, b2, this.textColor.getColorInt(b4), this.fontShadow.getValue());
         int n6 = b + n2;
         int n7 = b2 - 1;
         byte b5 = 0;
         mc.fontRendererObj.drawStringInt("❤", n6, n7, this.heartColor.getColorInt(b5), this.fontShadow.getValue());
         if (flag) {
            int n8 = n2 + HEART_SYMBOL_WIDTH;
            boolean flag2 = true;
            int n9 = n8 + (int)this.spacing.getValue(flag2) * 2 + b;
            byte b6 = 0;
            mc.fontRendererObj.drawStringInt(str2, n9, b2, this.absorptionTextColor.getColorInt(b6), this.fontShadow.getValue());
            n6 = n9 + n3;
            n7 = b2 - 1;
            byte b7 = 0;
            mc.fontRendererObj.drawStringInt("❤", n6, n7, this.absorptionHeartColor.getColorInt(b7), this.fontShadow.getValue());
         }
      }
   }

   private boolean isAbsorptionTextColorVisible() {
      return this.absorptionMode.getSelectedValue().equals("Separate");
   }

}
