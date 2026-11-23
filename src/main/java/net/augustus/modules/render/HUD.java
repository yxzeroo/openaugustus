package net.augustus.modules.render;

import java.awt.Color;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.render.ShaderRenderPass;
import net.augustus.font.DynamicFontUtil;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.BlurShadowSettings;
import net.augustus.font.CustomFontUtil;
import net.augustus.render.RenderPassTask;
import net.augustus.ClientManagers;
import net.augustus.settings.ColorSetting;
import net.augustus.font.FontManager;
import net.augustus.render.RenderUtil;
import net.augustus.utils.render.ColorUtil;
import net.augustus.utils.render.BlurShadowManager;
import net.augustus.Augustus;
import net.augustus.ui.GuiIngameHook;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class HUD extends Module implements RenderListener {
   public final BooleansSetting mode;
   public final BooleansSetting side;
   public final BooleansSetting casing;
   private final StringValue xPos;
   private final StringValue yPos;
   private final StringValue scale;
   public final StringValue size;
   private final StringValue newFontSize;
   private final ColorSetting color2;
   private final BooleanValue backGround;
   private final BooleanValue roundedBackGround;
   private final StringValue roundFactor;
   private final ColorSetting backgroundColor;
   private final BlurShadowSettings hud;
   private final String upperCaseClientName;
   private final String lowerCaseClientName;
   private final ResourceLocation augustusTexture;
   private final ResourceLocation augustusTexture2;
   private final ResourceLocation augustusTexture3;
   private final ResourceLocation augustusTexture4;
   private int lastSimpleFontSize;

   public HUD() {
       super("HUD", "ÜberlagerungsElemente", new Color(75, 166, 91), Categorys.RENDER);
      this.mode = new BooleansSetting(
         this, "Mode", "Basic", new String[]{"None", "Basic", "Other", "Simple", "Ausustus", "Xave", "Augustus", "Amogus", "Logo", "Fanart"}
      );
      BooleansSetting booleansSetting = new BooleansSetting(this, "Side", "Left", new String[]{"Left", "Right"});
      SettingVisibilityCondition condition = () -> this.isSideVisible();
      this.side = (BooleansSetting)booleansSetting.withVisibility(condition);
      BooleansSetting booleansSetting2 = new BooleansSetting(this, "Casing", "Default", new String[]{"Default", "UpperCase", "LowerCase"});
      SettingVisibilityCondition condition2 = () -> {
         return this.mode.getSelectedValue().equals("Simple");
      };
      this.casing = (BooleansSetting)booleansSetting2.withVisibility(condition2);
      StringValue stringValue = new StringValue(this, "X-Pos", 0.0, 0.0, 100.0, 2);
      SettingVisibilityCondition condition3 = () -> this.isPosXVisible();
      this.xPos = stringValue.visibleWhen(condition3);
      StringValue stringValue2 = new StringValue(this, "Y-Pos", 0.0, 0.0, 100.0, 2);
      SettingVisibilityCondition condition4 = () -> this.isPosYVisible();
      this.yPos = stringValue2.visibleWhen(condition4);
      StringValue stringValue3 = new StringValue(this, "Scale", 2.0, 1.0, 4.0, 2);
      SettingVisibilityCondition condition5 = () -> this.isScaleVisible();
      this.scale = stringValue3.visibleWhen(condition5);
      StringValue stringValue4 = new StringValue(this, "Size", 0.6, 0.01, 2.0, 2);
      SettingVisibilityCondition condition6 = () -> this.isImageLogoMode();
      this.size = stringValue4.visibleWhen(condition6);
      StringValue stringValue5 = new StringValue(this, "NewFontSize", 33.0, 16.0, 80.0, 0);
      String str = "Size";
      StringValue stringValue6 = stringValue5.named(str);
      SettingVisibilityCondition condition7 = () -> {
         return this.mode.getSelectedValue().equals("Simple");
      };
      this.newFontSize = stringValue6.visibleWhen(condition7);
      ColorSetting colorSetting = new ColorSetting(this, "Color", Color.white);
      SettingVisibilityCondition condition8 = () -> this.isTextColorVisible();
      this.color2 = (ColorSetting)colorSetting.withVisibility(condition8);
      BooleanValue booleanValue = new BooleanValue(this, "BackGround", true);
      SettingVisibilityCondition condition9 = () -> {
         return this.mode.getSelectedValue().equals("Ausustus")
            || this.mode.getSelectedValue().equals("Other")
            || this.mode.getSelectedValue().equals("Basic")
            || this.mode.getSelectedValue().equals("Simple");
      };
      this.backGround = (BooleanValue)booleanValue.withVisibility(condition9);
      BooleanValue booleanValue2 = new BooleanValue(this, "RoundedBackGround", false);
      SettingVisibilityCondition condition10 = () -> this.isRoundedBackgroundVisible();
      this.roundedBackGround = (BooleanValue)booleanValue2.withVisibility(condition10);
      StringValue stringValue7 = new StringValue(this, "RoundFactor", 2.0, 1.0, 10.0, 0);
      SettingVisibilityCondition condition11 = () -> this.isRoundFactorVisible();
      this.roundFactor = stringValue7.visibleWhen(condition11);
      ColorSetting colorSetting2 = new ColorSetting(this, "BackGroundColor", new Color(0, 0, 0, 100));
      SettingVisibilityCondition condition12 = () -> this.isBackgroundColorVisible();
      this.backgroundColor = (ColorSetting)colorSetting2.withVisibility(condition12);
      BlurShadowSettings blurShadowSettings = new BlurShadowSettings(this, "Hud");
      Color black = Color.black;
      BlurShadowSettings blurShadowSettings2 = blurShadowSettings.withCustomShadowColor(black);
      SettingVisibilityCondition condition13 = () -> {
         return !this.mode.getSelectedValue().equals("None");
      };
      this.hud = (BlurShadowSettings)blurShadowSettings2.withVisibility(condition13);
      this.upperCaseClientName = Augustus.getInstance().getName().toUpperCase(Locale.ROOT);
      this.lowerCaseClientName = Augustus.getInstance().getName().toLowerCase(Locale.ROOT);
      this.augustusTexture = new ResourceLocation("augustus", "pictures/augustus.png");
      this.augustusTexture2 = new ResourceLocation("augustus", "pictures/amogus.png");
      this.augustusTexture3 = new ResourceLocation("augustus", "pictures/logo.png");
      this.augustusTexture4 = new ResourceLocation("augustus", "pictures/fanart.png");
      this.lastSimpleFontSize = 0;
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 32;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onRender2(float f, ScaledResolution sr) {
      boolean isImageLogoMode = this.isImageLogoMode();
      if (isImageLogoMode && this.hud.isBlurActive()) {
         GL11.glPushMatrix();
         GL11.glScaled((double)(1.0F / (float)sr.getScaleFactor()), (double)(1.0F / (float)sr.getScaleFactor()), 1.0);
         GL11.glTranslated(this.xPos.getValue() / 100.0 * (double)mc.displayWidth, this.yPos.getValue() / 100.0 * (double)mc.displayHeight, 0.0);
         boolean flag = this.side.getSelectedValue().equals("Left");
         double value = this.size.getValue();
         GL11.glScaled(value, value, 1.0);
         ScaledResolution sr2 = new ScaledResolution((double)mc.displayWidth * (1.0 / value), (double)mc.displayHeight * (1.0 / value), (int)value);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         int n = !this.mode.getSelectedValue().equals("Amogus") && !this.mode.getSelectedValue().equals("Logo") ? 210 : 256;
         int n2 = flag ? -20 : sr2.getScaledWidth() - n;
         mc.getTextureManager()
            .bindTexture(this.mode.getSelectedValue().equals("Augustus") ? this.augustusTexture : (this.mode.getSelectedValue().equals("Logo") ? this.augustusTexture3 : this.augustusTexture2), false);
         mc.ingameGUI.drawTexturedModalRect(n2, -3, 0, 0, n, 256);
         BlurShadowManager.enableBlurOnce();
         BlurShadowManager.disableNormalPass();
         BlurShadowManager.submitRenderTask(false, shaderRenderPass -> mc.ingameGUI.drawTexturedModalRect(n2, -3, 0, 0, n, 256));
         GL11.glScaled(value, value, 1.0);
         GL11.glPopMatrix();
      }
   }

   @Override
   public void onRender(float f, ScaledResolution sr) {
      if (!mc.gameSettings.showDebugInfo) {
         GL11.glPushMatrix();
         GL11.glScaled((double)(1.0F / (float)sr.getScaleFactor()), (double)(1.0F / (float)sr.getScaleFactor()), 1.0);
         GL11.glTranslated(this.xPos.getValue() / 100.0 * (double)mc.displayWidth, this.yPos.getValue() / 100.0 * (double)mc.displayHeight, 0.0);
         boolean isImageLogoMode = this.isImageLogoMode();
         boolean flag = this.side.getSelectedValue().equals("Left");
         double d = isImageLogoMode ? this.size.getValue() : this.scale.getValue();
         if (!this.mode.getSelectedValue().equals("Simple")) {
            GL11.glScaled(d, d, 1.0);
         }

         GlStateManager.disableBlend();
         ScaledResolution sr2 = new ScaledResolution((double)mc.displayWidth * (1.0 / d), (double)mc.displayHeight * (1.0 / d), (int)d);
         FontRenderer fontRendererObj = mc.fontRendererObj;
         String selectedValue = this.mode.getSelectedValue();
         switch (selectedValue) {
            case "Augustus":
            case "Logo":
            case "Fanart":
            case "Amogus":
               GlStateManager.enableAlpha();
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               int n = this.mode.getSelectedValue().equals("Augustus") ? 210 : 256;
               int n2 = flag ? -20 : sr2.getScaledWidth() - n;
               mc.getTextureManager()
                  .bindTexture(
                     this.mode.getSelectedValue().equals("Augustus")
                        ? this.augustusTexture
                        : (this.mode.getSelectedValue().equals("Logo") ? this.augustusTexture3 : (this.mode.getSelectedValue().equals("Amogus") ? this.augustusTexture2 : this.augustusTexture4))
                  );
               if (this.hud.isShadowActive()) {
                  BlurShadowManager.enableShadowOnce();
               }

               if (!this.hud.isBlurActive()) {
                  BlurShadowManager.enableNormalPassOnce();
               }

               RenderPassTask renderPassTask = shaderRenderPass -> mc.ingameGUI.drawTexturedModalRect(n2, -3, 0, 0, n, 256);
               boolean flag2 = true;
               BlurShadowManager.submitRenderTask(flag2, renderPassTask);
               double d2 = (double)((float)(256.0 * d) + 1.0F);
               double d3 = 1.0;
               double d4 = d2;
               this.updateArrayListOffset(d4, d3);
               break;
            case "Other":
               GL11.glPushMatrix();
               GL11.glScaled(1.5, 1.5, 1.0);
               double d5 = (double)((int)((double)((float)fontRendererObj.FONT_HEIGHT * 1.75F + 5.0F) * 1.5));
               this.updateArrayListOffset(d5, d);
               int stringWidth = fontRendererObj.getStringWidth("A");
               int stringWidth2 = fontRendererObj.getStringWidth("ugustus");
               byte b = 0;
               int colorInt = this.color2.getColorInt(b);
               byte b2 = 0;
               int colorInt2 = this.backgroundColor.getColorInt(b2);
               if (this.backGround.getValue()) {
                  boolean isBlurActive = this.hud.isBlurActive();
                  BlurShadowManager.enableOneShotPass(isBlurActive, this.hud.isShadowActive());
               }

               if (flag) {
                  if (this.backGround.getValue()) {
                     BlurShadowManager.submitRenderTask(false, shaderRenderPass2 -> this.renderOtherLeftBackground(colorInt2, stringWidth, stringWidth2, fontRendererObj, shaderRenderPass2));
                  }

                  GL11.glPushMatrix();
                  GL11.glScaled(1.75, 1.75, 1.0);
                  fontRendererObj.drawStringWithShadowOffset("A", 1.0F, 1.0F, colorInt, true, 0.5F);
                  GL11.glPopMatrix();
                  fontRendererObj.drawStringWithShadowOffset("ugustus", (float)stringWidth * 1.75F + 2.0F, (float)(2 + fontRendererObj.FONT_HEIGHT) - 4.0F, colorInt, true, 0.5F);
                  Augustus.getInstance().getClass();
               } else {
                  if (this.backGround.getValue()) {
                     BlurShadowManager.submitRenderTask(false, shaderRenderPass3 -> this.renderOtherRightBackground(colorInt2, sr2, stringWidth, stringWidth2, fontRendererObj, shaderRenderPass3));
                  }

                  GL11.glPushMatrix();
                  GL11.glScaled(1.75, 1.75, 1.0);
                  fontRendererObj.drawStringWithShadowOffset("A", (float)sr2.getScaledWidth() / 1.5F / 1.75F - (float)stringWidth - (float)stringWidth2 / 1.75F - 0.25F, 1.0F, colorInt, true, 0.5F);
                  GL11.glPopMatrix();
                  fontRendererObj.drawStringWithShadowOffset(
                     "ugustus",
                     (float)sr2.getScaledWidth() / 1.5F - 1.75F - (float)stringWidth * 1.5F * 1.75F - (float)stringWidth2 / 1.5F + 4.0F,
                     (float)(2 + fontRendererObj.FONT_HEIGHT) - 4.0F,
                     colorInt,
                     true,
                     0.5F
                  );
               }

               GL11.glPopMatrix();
               break;
            case "Basic":
            case "Ausustus":
               GregorianCalendar instance = (GregorianCalendar)GregorianCalendar.getInstance();
               Date date = new Date();
               instance.setTime(date);
               int n3 = ClientManagers.moduleManager.protector.isToggled() && ClientManagers.moduleManager.protector.time.getValue() ? ClientManagers.moduleManager.protector.getFakeHour() : instance.get(10);
               String str = instance.get(12) <= 9 ? "0" + instance.get(12) : String.valueOf(instance.get(12));
               str = ClientManagers.moduleManager.protector.isToggled() && ClientManagers.moduleManager.protector.time.getValue() ? String.valueOf(ClientManagers.moduleManager.protector.getFakeMinute()) : str;
               String str2;
               if (instance.get(9) == 0) {
                  str2 = n3 < 9 ? " (0" + n3 + ":" + str + " AM)" : " (" + n3 + ":" + str + " AM)";
               } else {
                  str2 = n3 < 9 ? " (0" + n3 + ":" + str + " PM)" : " (" + n3 + ":" + str + " PM)";
               }

               StringBuilder sb = new StringBuilder();
               String str3;
               if (this.mode.getSelectedValue().equals("Basic")) {
                  str3 = Augustus.getInstance().getName();
               } else {
                  short s = 32427;
                  str3 = "Ausustus";
               }

               String str4 = sb.append(str3).append(" ").append(Augustus.getInstance().getVersion()).toString();
               float stringWidth3 = (float)fontRendererObj.getStringWidth(str4);
               float stringWidth4 = (float)fontRendererObj.getStringWidth(str2);
               double d6 = (double)(fontRendererObj.FONT_HEIGHT + 1);
               this.updateArrayListOffset(d6, d);
               int rgb = new Color(182, 186, 189).getRGB();
               byte b3 = 0;
               int colorInt3 = this.color2.getColorInt(b3);
               byte b4 = 0;
               int colorInt4 = this.backgroundColor.getColorInt(b4);
               if (this.backGround.getValue()) {
                  boolean isBlurActive2 = this.hud.isBlurActive();
                  BlurShadowManager.enableOneShotPass(isBlurActive2, this.hud.isShadowActive());
               }

               boolean value = this.backGround.getValue();
               boolean value2 = this.roundedBackGround.getValue();
               float f2 = value2 && value ? (float)this.roundFactor.getValue() - 1.0F : 0.0F;
               if (flag) {
                  if (value) {
                     BlurShadowManager.submitRenderTask(
                        false,
                        shaderRenderPass4 -> {
                           BlurShadowSettings hud2 = this.hud;
                           int n4 = ColorUtil.resolvePassColor(shaderRenderPass4, colorInt4);
                           int n5 = hud2.resolvePassColor(shaderRenderPass4, n4);
                           if (value2) {
                              RenderUtil.drawRoundedRect(
                                 0.0F,
                                 0.0F,
                                 stringWidth4 + 1.0F + stringWidth3 + f2 * 2.0F,
                                 (float)(fontRendererObj.FONT_HEIGHT + 1),
                                 (float)this.roundFactor.getValue(),
                                 1,
                                 n5
                              );
                           } else {
                              RenderUtil.drawRect2(0.0F, 0.0F, stringWidth4 + 1.0F + stringWidth3, (float)(fontRendererObj.FONT_HEIGHT + 1), n5);
                           }
                        }
                     );
                  }

                  fontRendererObj.drawStringWithShadowOffset(str4, 0.0F + f2, 1.0F, colorInt3, true, 0.5F);
                  fontRendererObj.drawStringWithShadowOffset(str2, 0.0F + f2 + stringWidth3, 1.0F, rgb, true, 0.5F);
               } else {
                  if (value) {
                     BlurShadowManager.submitRenderTask(
                        false,
                        shaderRenderPass5 -> {
                           BlurShadowSettings hud3 = this.hud;
                           int n6 = ColorUtil.resolvePassColor(shaderRenderPass5, colorInt4);
                           int n7 = hud3.resolvePassColor(shaderRenderPass5, n6);
                           if (value2) {
                              RenderUtil.drawRoundedRect(
                                 (float)sr2.getScaledWidth() - stringWidth4 - 3.0F - stringWidth3,
                                 0.0F,
                                 (float)sr2.getScaledWidth(),
                                 (float)(fontRendererObj.FONT_HEIGHT + 1),
                                 (float)this.roundFactor.getValue(),
                                 1,
                                 n7
                              );
                           } else {
                              RenderUtil.drawRect2(
                                 (float)sr2.getScaledWidth() - stringWidth4 - 3.0F - stringWidth3,
                                 0.0F,
                                 (float)sr2.getScaledWidth(),
                                 (float)(fontRendererObj.FONT_HEIGHT + 1),
                                 n7
                              );
                           }
                        }
                     );
                  }

                  fontRendererObj.drawStringWithShadowOffset(str4, (float)sr2.getScaledWidth() - stringWidth3 - stringWidth4 - 1.0F - f2, 1.0F, colorInt3, true, 0.5F);
                  fontRendererObj.drawStringWithShadowOffset(str2, (float)sr2.getScaledWidth() - stringWidth4 - 1.0F - f2, 1.0F, rgb, true, 0.5F);
               }
               break;
            case "Xave":
               byte b5 = 0;
               int colorInt5 = this.color2.getColorInt(b5);
               BlurShadowManager.enableOneShotPass(false, this.hud.isShadowActive());
               if (flag) {
                  int n8 = colorInt5;
                  BlurShadowManager.submitRenderTask(false, shaderRenderPass6 -> {
                     CustomFontUtil esp60 = FontManager.esp60;
                     String upperCaseClientName2 = this.upperCaseClientName;
                     int n9 = this.hud.resolvePassColor(shaderRenderPass6, n8);
                     double d7 = 2.0;
                     double d8 = 2.0;
                     String str5 = upperCaseClientName2;
                     esp60.drawStringWithShadow(str5, d8, d7, n9);
                  });
               } else {
                  int n10 = colorInt5;
                  RenderPassTask renderPassTask2 = shaderRenderPass7 -> this.renderXaveRightText(sr2, n10, shaderRenderPass7);
                  boolean flag3 = false;
                  BlurShadowManager.submitRenderTask(flag3, renderPassTask2);
               }

               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               double d9 = (double)FontManager.esp60.getFontHeight() * d + (double)FontManager.esp16.getFontHeight() / 1.25;
               double d10 = 1.0;
               double d11 = d9;
               this.updateArrayListOffset(d11, d10);
               break;
            case "Simple":
               int n11 = MathHelper.clamp_int((int)this.newFontSize.getValue(), 1, 300);
               if (this.lastSimpleFontSize != n11) {
                  FontManager.rubikBoldHud.setFontSize(n11);
               }

               String str6;
               if (this.casing.getSelectedValue().equals("UpperCase")) {
                  str6 = this.upperCaseClientName;
               } else if (this.casing.getSelectedValue().equals("LowerCase")) {
                  str6 = this.lowerCaseClientName;
               } else {
                  str6 = Augustus.getInstance().getName();
               }

               this.lastSimpleFontSize = n11;
               byte b6 = 0;
               int colorInt6 = this.color2.getColorInt(b6);
               boolean isBlurActive3 = this.hud.isBlurActive();
               BlurShadowManager.enableOneShotPass(isBlurActive3, this.hud.isShadowActive());
               float stringWidth5 = FontManager.rubikBoldHud.getStringWidth(str6);
               float f3 = FontManager.rubikBoldHud.getStringHeight(str6) * 1.12F;
               if (flag) {
                  if (this.backGround.getValue()) {
                     byte b7 = 0;
                     int colorInt7 = this.backgroundColor.getColorInt(b7);
                     float f4 = f3;
                     float f5 = stringWidth5;
                     int n12 = colorInt7;
                     BlurShadowManager.submitRenderTask(false, shaderRenderPass8 -> {
                        BlurShadowSettings hud4 = this.hud;
                        int n13 = ColorUtil.resolvePassColor(shaderRenderPass8, n12);
                        int n14 = hud4.resolvePassColor(shaderRenderPass8, n13);
                        if (this.roundedBackGround.getValue()) {
                           RenderUtil.drawRoundedRect(0.0F, 0.0F, f5 + 6.0F, f4, (float)this.roundFactor.getValue(), 1, n14);
                        } else {
                           RenderUtil.drawRect2(0.0F, 0.0F, f5 + 6.0F, f4, n14);
                        }
                     });
                  }

                  BlurShadowManager.enableOneShotPass(false, this.hud.isShadowActive() && !this.backGround.getValue());
                  int n15 = colorInt6;
                  String str7 = str6;
                  BlurShadowManager.submitRenderTask(false, shaderRenderPass9 -> this.renderSimpleLeftText(str7, n15, shaderRenderPass9));
               } else {
                  if (this.backGround.getValue()) {
                     byte b8 = 0;
                     int colorInt8 = this.backgroundColor.getColorInt(b8);
                     float f6 = f3;
                     float f7 = stringWidth5;
                     BlurShadowManager.submitRenderTask(false, shaderRenderPass10 -> this.renderSimpleRightBackground(colorInt8, f7, f6, shaderRenderPass10));
                  }

                  BlurShadowManager.enableOneShotPass(false, this.hud.isShadowActive() && !this.backGround.getValue());
                  int n16 = colorInt6;
                  float f8 = stringWidth5;
                  String str8 = str6;
                  BlurShadowManager.submitRenderTask(false, shaderRenderPass11 -> {
                     DynamicFontUtil rubikBoldHud = FontManager.rubikBoldHud;
                     double d12 = (double)((float)mc.displayWidth - f8 - 3.0F);
                     int n17 = this.hud.resolvePassColor(shaderRenderPass11, n16);
                     double d13 = 0.0;
                     double d14 = d12;
                     rubikBoldHud.drawString(str8, d14, d13, n17);
                  });
               }

               double d15 = (double)f3 + (double)f3 / 1.25;
               double d16 = 1.0;
               double d17 = d15;
               this.updateArrayListOffset(d17, d16);
               break;
            default:
               double d18 = 1.0;
               double d19 = 0.0;
               this.updateArrayListOffset(d19, d18);
         }

         GL11.glPopMatrix();
      } else {
         double d20 = 1.0;
         double d21 = 0.0;
         this.updateArrayListOffset(d21, d20);
      }

      RenderUtil.resetGlColorState();
   }

   private boolean isImageLogoMode() {
      return this.mode.getSelectedValue().equals("Augustus")
         || this.mode.getSelectedValue().equals("Amogus")
         || this.mode.getSelectedValue().equals("Logo")
         || this.mode.getSelectedValue().equals("Fanart");
   }

   private void updateArrayListOffset(double d, double d2) {
      if (!this.side.getSelectedValue().equals(ClientManagers.moduleManager.arrayList.side.getSelectedValue())) {
         d = 0.0;
      }

      GuiIngameHook.hudTopOffsetY = (float)((int)((float)(d * d2)) + (d != 0.0 ? 1 : 0));
   }

   private void renderSimpleRightBackground(int n, float f, float f2, ShaderRenderPass shaderRenderPass) {
      BlurShadowSettings hud2 = this.hud;
      int n2 = ColorUtil.resolvePassColor(shaderRenderPass, n);
      int n3 = hud2.resolvePassColor(shaderRenderPass, n2);
      if (this.roundedBackGround.getValue()) {
         RenderUtil.drawRoundedRect(
            (float)mc.displayWidth - f - 6.0F, 0.0F, (float)mc.displayWidth, f2, (float)this.roundFactor.getValue(), 1, n3
         );
      } else {
         RenderUtil.drawRect2((float)mc.displayWidth - f - 6.0F, 0.0F, (float)mc.displayWidth, f2, n3);
      }
   }

   private void renderSimpleLeftText(String str, int n, ShaderRenderPass shaderRenderPass) {
      DynamicFontUtil rubikBoldHud = FontManager.rubikBoldHud;
      double d = (double)(-1.0F * ((float)this.lastSimpleFontSize / 80.0F));
      int n2 = this.hud.resolvePassColor(shaderRenderPass, n);
      double d2 = d;
      double d3 = 3.0;
      rubikBoldHud.drawString(str, d3, d2, n2);
   }

   private void renderXaveRightText(ScaledResolution sr, int n, ShaderRenderPass shaderRenderPass) {
      CustomFontUtil esp60 = FontManager.esp60;
      String upperCaseClientName2 = this.upperCaseClientName;
      float scaledWidth = (float)sr.getScaledWidth();
      String upperCaseClientName3 = this.upperCaseClientName;
      double d = (double)(scaledWidth - FontManager.esp60.getStringWidth(upperCaseClientName3));
      int n2 = this.hud.resolvePassColor(shaderRenderPass, n);
      double d2 = 4.0;
      double d3 = d;
      String str = upperCaseClientName2;
      esp60.drawStringWithShadow(str, d3, d2, n2);
   }

   private void renderOtherRightBackground(int n, ScaledResolution sr, int n2, int n3, FontRenderer fontRenderer, ShaderRenderPass shaderRenderPass) {
      BlurShadowSettings hud2 = this.hud;
      int n4 = ColorUtil.resolvePassColor(shaderRenderPass, n);
      int n5 = hud2.resolvePassColor(shaderRenderPass, n4);
      if (this.roundedBackGround.getValue()) {
         RenderUtil.drawRoundedRect(
            (float)((int)((float)sr.getScaledWidth() / 1.5F - (float)((int)((float)n2 * 1.75F + (float)n3 + 4.0F)))),
            0.0F,
            (float)((int)((float)sr.getScaledWidth() / 1.5F) + 1),
            (float)((int)((float)fontRenderer.FONT_HEIGHT * 1.75F + 3.0F)),
            (float)this.roundFactor.getValue(),
            1,
            n5
         );
      } else {
         RenderUtil.drawRect3(
            (int)((float)sr.getScaledWidth() / 1.5F - (float)((int)((float)n2 * 1.75F + (float)n3 + 4.0F))),
            0,
            (int)((float)sr.getScaledWidth() / 1.5F) + 1,
            (int)((float)fontRenderer.FONT_HEIGHT * 1.75F + 3.0F),
            n5
         );
      }
   }

   private void renderOtherLeftBackground(int n, int n2, int n3, FontRenderer fontRenderer, ShaderRenderPass shaderRenderPass) {
      BlurShadowSettings hud2 = this.hud;
      int n4 = ColorUtil.resolvePassColor(shaderRenderPass, n);
      int n5 = hud2.resolvePassColor(shaderRenderPass, n4);
      if (this.roundedBackGround.getValue()) {
         float value = (float)this.roundFactor.getValue();
         RenderUtil.drawRoundedRect(
            0.0F,
            0.0F,
            (float)((int)((float)n2 * 1.75F + (float)n3 + 4.0F)),
            (float)((int)((float)fontRenderer.FONT_HEIGHT * 1.75F + 3.0F)),
            value,
            1,
            n5
         );
      } else {
         RenderUtil.drawRect3(0, 0, (int)((float)n2 * 1.75F + (float)n3 + 4.0F), (int)((float)fontRenderer.FONT_HEIGHT * 1.75F + 3.0F), n5);
      }
   }

   private boolean isBackgroundColorVisible() {
      return this.backGround.getValue() && this.backGround.isVisible();
   }

   private boolean isRoundFactorVisible() {
      return this.backGround.getValue() && this.backGround.isVisible() && this.roundedBackGround.getValue();
   }

   private boolean isRoundedBackgroundVisible() {
      return this.backGround.getValue() && this.backGround.isVisible();
   }

   private boolean isTextColorVisible() {
      return this.mode.getSelectedValue().equals("Ausustus")
         || this.mode.getSelectedValue().equals("Other")
         || this.mode.getSelectedValue().equals("Basic")
         || this.mode.getSelectedValue().equals("Simple")
         || this.mode.getSelectedValue().equals("Xave");
   }

   private boolean isScaleVisible() {
      return !this.mode.getSelectedValue().equals("Augustus")
         && !this.isImageLogoMode()
         && !this.mode.getSelectedValue().equals("None")
         && !this.mode.getSelectedValue().equals("Simple");
   }

   private boolean isPosYVisible() {
      return !this.mode.getSelectedValue().equals("None");
   }

   private boolean isPosXVisible() {
      return !this.mode.getSelectedValue().equals("None");
   }

   private boolean isSideVisible() {
      return !this.mode.getSelectedValue().equals("None");
   }

}
