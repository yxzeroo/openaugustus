package net.augustus.settings;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.clickgui.components.ColorAlphaSliderComponent;
import net.augustus.clickgui.components.ColorBrightnessSliderComponent;
import net.augustus.clickgui.components.ColorHueSliderComponent;
import net.augustus.clickgui.components.ColorSaturationSliderComponent;
import net.augustus.font.CustomFontUtil;
import net.augustus.render.RenderUtil;
import net.augustus.settings.property.BooleanProperty;
import net.augustus.settings.property.DoubleProperty;
import net.augustus.settings.property.IntegerProperty;
import net.augustus.utils.render.ColorUtil;
import java.awt.Color;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.augustus.modules.Module;
import net.augustus.modules.render.CustomClientColor;
import net.minecraft.util.MathHelper;

public class ColorSetting extends Setting {
   private final ColorBrightnessSliderComponent primaryBrightnessSlider;
   private final IntegerProperty fa;
   private final BooleanProperty r;
   private final StringValue fSpeed;
   private final StringValue fDist;
   private final ColorSaturationSliderComponent fadeSaturationSlider;
   private final StringValue rSpeed;
   private final ColorSaturationSliderComponent primarySaturationSlider;
   private final BooleanProperty ao;
   private Color fadeColor;
   private final DoubleProperty rd;
   private int renderYOffset;
   private final StringValue rDist;
   private final ColorAlphaSliderComponent fadeAlphaSlider;
   private final StringValue aSpeed;
   private final DoubleProperty rs;
   private final BooleanValue astolfo;
   private final DoubleProperty ad;
   private final ColorHueSliderComponent fadeHueSlider;
   private final BooleanValue clientColor;
   private final DoubleProperty fs;
   private float[] fadeHsb;
   private final ColorBrightnessSliderComponent fadeBrightnessSlider;
   private final IntegerProperty a;
   private final DoubleProperty fd;
   private final BooleanProperty f;
   private final BooleanProperty cc;
   private final DoubleProperty as;
   private final BooleanValue rainbow;
   private final IntegerProperty cca;
   private final ColorAlphaSliderComponent primaryAlphaSlider;
   private final ColorHueSliderComponent primaryHueSlider;
   private int computedArgb;
   private IntegerProperty c;
   private Color primaryColor;
   private final StringValue clientColorAlpha;
   private float[] primaryHsb;
   private static int subPanelOffsetX = 106;
   private final StringValue aDist;
   private final BooleanValue fade;
   private final IntegerProperty fc;

   @Override
   public void renderTooltip() {
      CustomFontUtil renderFont2 = this.renderFont;
      String str = this.getDisplayName() + ": ";
      String str2 = str;
      int stringWidth = (int)renderFont2.getStringWidth(str2);
      this.drawDescriptionTooltip(stringWidth);
   }

   public void setFadeAlpha(int n) {
      IntegerProperty fa2 = this.fa;
      Integer num = n;
      Integer num2 = num;
      fa2.setValue(num2);
   }

   public void setFadeSpeed(double d) {
      DoubleProperty fs2 = this.fs;
      Double d2 = d;
      Double d3 = d2;
      fs2.setValue(d3);
      this.fSpeed.setValue(d);
   }

   public void setAstolfoEnabled(boolean flag) {
      BooleanProperty ao2 = this.ao;
      Boolean bool = flag;
      Boolean bool2 = bool;
      ao2.setValue(bool2);
      this.astolfo.setValue(flag);
   }

   public ColorSetting(Module module, String str, int n) {
       this(str, module, null, new Color(n));
   }

   public void setPrimaryAlpha(int n) {
      IntegerProperty integerProperty = this.a;
      Integer num = n;
      Integer num2 = num;
      integerProperty.setValue(num2);
   }

   @Override
   public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      if (!(this.getParentModule() instanceof CustomClientColor)) {
         this.clientColor.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
         this.clientColorAlpha.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      }

      this.primaryHueSlider.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      this.primarySaturationSlider.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      this.primaryBrightnessSlider.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      this.primaryAlphaSlider.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      this.rSpeed.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      this.fSpeed.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      this.fadeHueSlider.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      this.fadeSaturationSlider.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      this.fadeBrightnessSlider.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      this.fadeAlphaSlider.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      this.fDist.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      this.rDist.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      this.aSpeed.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
      this.aDist.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
   }

   public boolean isFadeEnabled() {
      return (Boolean)this.f.getValue();
   }

   public int getAlpha() {
      return ColorUtil.getAlpha(this.computedArgb);
   }

   private int computeRainbowArgb(int n) {
      return ColorUtil.getRainbowColor(n, (int)this.getRainbowSpeed(), this.primaryHsb[1], this.primaryHsb[2], this.primaryColor.getAlpha());
   }

   public BooleanValue getRainbow() {
      return this.rainbow;
   }

   public ColorSetting withFadeDefault(boolean flag) {
      this.setFadeEnabled(flag);
      BooleanProperty booleanProperty = this.f;
      Boolean bool = flag;
      booleanProperty.setDefaultValue(bool);
      return this;
   }

   public float getRedFloat() {
      return ColorUtil.getRedFloat(this.computedArgb);
   }

   public void setRainbowEnabled(boolean flag) {
      BooleanProperty booleanProperty = this.r;
      Boolean bool = flag;
      Boolean bool2 = bool;
      booleanProperty.setValue(bool2);
      this.rainbow.setValue(flag);
   }

   private void onFadeAlphaPropertyChanged(int n) {
      Color color = new Color((Integer)this.fc.getValue());
      Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
      this.setFadeColor(color2);
   }

   private void onPrimaryRgbPropertyChanged(Integer num) {
      Color color = new Color(num);
      Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), this.getPrimaryAlpha());
      this.setPrimaryColor(color2);
   }

   public void setRainbowDistance(double d) {
      DoubleProperty rd2 = this.rd;
      Double d2 = d;
      Double d3 = d2;
      rd2.setValue(d3);
      this.rDist.setValue(d);
   }

   public Color getFadeColor() {
      return this.fadeColor;
   }

   public Color getColor(int n) {
      this.updateComputedColor(n);
      return new Color(this.computedArgb);
   }

   public float getBlueFloat() {
      return ColorUtil.getBlueFloat(this.computedArgb);
   }

   public int getBlue() {
      return ColorUtil.getBlue(this.computedArgb);
   }

   public void setAstolfoDistance(double d) {
      DoubleProperty ad2 = this.ad;
      Double d2 = d;
      Double d3 = d2;
      ad2.setValue(d3);
      this.aDist.setValue(d);
   }

   public void setClientColorAlpha(int n) {
      IntegerProperty cca2 = this.cca;
      Integer num = n;
      Integer num2 = num;
      cca2.setValue(num2);
      double d = (double)n;
      this.clientColorAlpha.setValue(d);
   }

   public StringValue getFSpeed() {
      return this.fSpeed;
   }

   public boolean isRainbowEnabled() {
      return (Boolean)this.r.getValue();
   }

   public int getRed() {
      return ColorUtil.getRed(this.computedArgb);
   }

   @Override
   public void resetToDefault() {
      this.a.resetToDefault();
      this.fa.resetToDefault();
      this.r.resetToDefault();
      this.f.resetToDefault();
      this.ao.resetToDefault();
      this.cc.resetToDefault();
      this.fc.resetToDefault();
      this.cca.resetToDefault();
      this.rs.resetToDefault();
      this.rd.resetToDefault();
      this.fs.resetToDefault();
      this.fd.resetToDefault();
      this.as.resetToDefault();
      this.ad.resetToDefault();
      this.c.resetToDefault();
   }

   public double getAstolfoSpeed() {
      return (Double)this.as.getValue();
   }

   public int getColorInt(int n) {
      this.updateComputedColor(n);
      return this.computedArgb;
   }

   @Override
   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      this.primaryHueSlider.mouseClicked(mouseX, mouseY, mouseButton);
      this.primarySaturationSlider.mouseClicked(mouseX, mouseY, mouseButton);
      this.primaryBrightnessSlider.mouseClicked(mouseX, mouseY, mouseButton);
      this.primaryAlphaSlider.mouseClicked(mouseX, mouseY, mouseButton);
      if (this.isRainbowEnabled()) {
         this.rSpeed.mouseClicked(mouseX, mouseY, mouseButton);
         this.rDist.mouseClicked(mouseX, mouseY, mouseButton);
      }

      if (!this.isFadeEnabled() && !this.isRainbowEnabled()) {
         if (!this.isClientColorEnabled()) {
            this.astolfo.mouseClicked(mouseX, mouseY, mouseButton);
         }
      }

      if (!this.isAstolfoEnabled() && !this.isRainbowEnabled()) {
         if (!this.isClientColorEnabled()) {
            this.fade.mouseClicked(mouseX, mouseY, mouseButton);
         }
      }

      if (!this.isFadeEnabled() && !this.isAstolfoEnabled()) {
         if (!this.isClientColorEnabled()) {
            this.rainbow.mouseClicked(mouseX, mouseY, mouseButton);
         }
      }

      if (!this.isFadeEnabled() && !this.isAstolfoEnabled() && !this.isRainbowEnabled() && !(this.getParentModule() instanceof CustomClientColor)) {
         this.clientColor.mouseClicked(mouseX, mouseY, mouseButton);
      }

      if (this.isFadeEnabled() && !this.isRainbowEnabled()) {
         this.fSpeed.mouseClicked(mouseX, mouseY, mouseButton);
         this.fDist.mouseClicked(mouseX, mouseY, mouseButton);
         this.fadeHueSlider.mouseClicked(mouseX, mouseY, mouseButton);
         this.fadeSaturationSlider.mouseClicked(mouseX, mouseY, mouseButton);
         this.fadeBrightnessSlider.mouseClicked(mouseX, mouseY, mouseButton);
         this.fadeAlphaSlider.mouseClicked(mouseX, mouseY, mouseButton);
      }

      if (this.isAstolfoEnabled() && !this.isRainbowEnabled() && !this.isFadeEnabled()) {
         this.aSpeed.mouseClicked(mouseX, mouseY, mouseButton);
         this.aDist.mouseClicked(mouseX, mouseY, mouseButton);
      }

      if (this.isClientColorEnabled() && !(this.getParentModule() instanceof CustomClientColor)) {
         this.clientColorAlpha.mouseClicked(mouseX, mouseY, mouseButton);
      }

      this.notifyValueChanged();
      this.notifyUserChanged();
      return false;
   }

   public void updateComputedColor(int n) {
      if (this.isRainbowEnabled()) {
         int n2 = (int)this.getRainbowDistance() * n;
         this.computedArgb = this.computeRainbowArgb(n2);
      } else if (this.isFadeEnabled()) {
         int n3 = (int)(this.getFadeDistance() * (double)n);
         this.computedArgb = this.computeFadeArgb(n3);
      } else if (this.isAstolfoEnabled()) {
         int n4 = (int)(this.getAstolfoDistance() * (double)n);
         this.computedArgb = this.computeAstolfoArgb(n4);
      } else {
         if (this.isClientColorEnabled()) {
            this.computedArgb = ColorUtil.withAlpha(Augustus.getInstance().getClientColor(), this.getClientColorAlpha2());
         } else {
            this.computedArgb = this.primaryColor.getRGB();
         }
      }
   }

   public ColorSetting withRainbowDefault(boolean flag) {
      this.setRainbowEnabled(flag);
      BooleanProperty booleanProperty = this.r;
      Boolean bool = flag;
      booleanProperty.setDefaultValue(bool);
      return this;
   }

   public ColorSetting withDefaultPrimaryColor(Color color) {
      Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
      this.setPrimaryColor(color2);
      IntegerProperty integerProperty = this.c;
      Integer rgb = color.getRGB();
      integerProperty.setDefaultValue(rgb);
      return this;
   }

   public ColorSaturationSliderComponent getFadeSaturationSlider() {
      return this.fadeSaturationSlider;
   }


   public BooleanValue getAstolfo() {
      return this.astolfo;
   }

   public ColorAlphaSliderComponent getFadeAlphaSlider() {
      return this.fadeAlphaSlider;
   }

   public float[] getPrimaryHsb() {
      return this.primaryHsb;
   }

   public ColorSaturationSliderComponent getPrimarySaturationSlider() {
      return this.primarySaturationSlider;
   }

   @Override
   public void render(int n, int n2, int n3, int n4, int n5, int n6, CustomFontUtil customFontUtil, boolean flag) {
      super.render(n, n2, n3, n4, n5, n6, customFontUtil, flag);
      if (flag) {
         this.renderYOffset = 0;
         short s = 200;
         byte b = 20;
         String str = this.getDisplayName() + ": ";
         byte b2 = 0;
         int colorInt = ClientManagers.moduleManager.clickGui.textColor.getColorInt(b2);
         double d = (double)n3;
         double d2 = (double)n4;
         double d3 = d;
         customFontUtil.drawString(str, d3, d2, colorInt);
         byte b3 = 0;
         int colorInt2 = ClientManagers.moduleManager.clickGui.headerColor.getColorInt(b3);
         this.renderYOffset += 25;
         float[] primaryHsb2 = this.primaryHsb;
         ColorHueSliderComponent primaryHueSlider2 = this.primaryHueSlider;
         int n7 = n4 - 2 + this.renderYOffset;
         double d4 = (double)this.primaryHsb[0];
         float[] primaryHsb3 = this.primaryHsb;
         int primaryAlpha = this.getPrimaryAlpha();
         float[] floats = primaryHsb3;
         byte b4 = 7;
         double d5 = 1.0;
         double d6 = 0.0;
         double d7 = d4;
         int n8 = n7;
         primaryHsb2[0] = (float)primaryHueSlider2.drawHueSlider(n3, n8, s, b, n, n2, d7, d6, d5, b4, customFontUtil, colorInt2, floats, primaryAlpha);
         this.renderYOffset += 25;
         primaryHsb2 = this.primaryHsb;
         ColorSaturationSliderComponent component = this.primarySaturationSlider;
         n7 = n4 - 2 + this.renderYOffset;
         double d8 = (double)this.primaryHsb[1];
         primaryHsb3 = this.primaryHsb;
         int primaryAlpha2 = this.getPrimaryAlpha();
         float[] floats2 = primaryHsb3;
         byte b5 = 7;
         double d9 = 1.0;
         double d10 = 0.0;
         double d11 = d8;
         int n9 = n7;
         primaryHsb2[1] = (float)component.drawSaturationSlider(n3, n9, s, b, n, n2, d11, d10, d9, b5, customFontUtil, colorInt2, floats2, primaryAlpha2);
         this.renderYOffset += 25;
         primaryHsb2 = this.primaryHsb;
         ColorBrightnessSliderComponent component2 = this.primaryBrightnessSlider;
         n7 = n4 - 2 + this.renderYOffset;
         d4 = (double)this.primaryHsb[2];
         primaryHsb3 = this.primaryHsb;
         int primaryAlpha3 = this.getPrimaryAlpha();
         float[] floats3 = primaryHsb3;
         byte b6 = 7;
         double d12 = 1.0;
         double d13 = 0.0;
         double d14 = d4;
         int n10 = n7;
         primaryHsb2[2] = (float)component2.drawBrightnessSlider(n3, n10, s, b, n, n2, d14, d13, d12, b6, customFontUtil, colorInt2, floats3, primaryAlpha3);
         this.renderYOffset += 25;
         ColorAlphaSliderComponent primaryAlphaSlider2 = this.primaryAlphaSlider;
         int n11 = n4 - 2 + this.renderYOffset;
         double primaryAlpha4 = (double)this.getPrimaryAlpha();
         float[] primaryHsb4 = this.primaryHsb;
         byte b7 = 7;
         double d15 = 255.0;
         double d16 = 0.0;
         double d17 = primaryAlpha4;
         int n12 = n11;
         int n13 = (int)primaryAlphaSlider2.drawAlphaSlider(n3, n12, s, b, n, n2, d17, d16, d15, b7, customFontUtil, colorInt2, primaryHsb4);
         int n14;
         int n15 = n13;
         this.setPrimaryAlpha(n15);
         this.renderYOffset += 27;
         Color color = new Color(Color.HSBtoRGB(this.primaryHsb[0], this.primaryHsb[1], this.primaryHsb[2]));
         this.primaryColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), this.getPrimaryAlpha());
         int n16 = s + 40;
         int n17 = 25;
         n14 = n3 + n16;
         n7 = n4 + n17;
         boolean flag2 = true;
         int n18 = n7;
         int n19 = n14;
         this.rainbow.render(n, n2, n19, n18, n5, n6, customFontUtil, flag2);
         boolean value = this.rainbow.getValue();
         this.setRainbowEnabled(value);
         n17 += 25;
         if (this.isRainbowEnabled()) {
            n14 = n3 + n16;
            n7 = n4 + n17;
            boolean flag3 = true;
            int n20 = n7;
            int n21 = n14;
            this.rSpeed.render(n, n2, n21, n20, n5, n6, customFontUtil, flag3);
            double value2 = this.rSpeed.getValue();
            this.setRainbowSpeed(value2);
            n17 += 25;
            n14 = n3 + n16;
            n7 = n4 + n17;
            boolean flag4 = true;
            int n22 = n7;
            int n23 = n14;
            this.rDist.render(n, n2, n23, n22, n5, n6, customFontUtil, flag4);
            double value3 = this.rDist.getValue();
            this.setRainbowDistance(value3);
            n17 += 25;
         }

         n14 = n3 + n16;
         n7 = n4 + n17;
         int n24 = n6 + 50;
         boolean flag5 = true;
         int n25 = n24;
         int n26 = n7;
         int n27 = n14;
         this.fade.render(n, n2, n27, n26, n5, n25, customFontUtil, flag5);
         boolean value4 = this.fade.getValue();
         this.setFadeEnabled(value4);
         n17 += 25;
         if (this.isFadeEnabled() && !this.isRainbowEnabled()) {
            if (!this.isClientColorEnabled()) {
               n14 = n3 + n16;
               n7 = n4 + n17;
               boolean flag6 = true;
               int n28 = n7;
               int n29 = n14;
               this.fSpeed.render(n, n2, n29, n28, n5, n6, customFontUtil, flag6);
               double value5 = this.fSpeed.getValue();
               this.setFadeSpeed(value5);
               n17 += 25;
               n14 = n3 + n16;
               n7 = n4 + n17;
               boolean flag7 = true;
               int n30 = n7;
               int n31 = n14;
               this.fDist.render(n, n2, n31, n30, n5, n6, customFontUtil, flag7);
               double value6 = this.fDist.getValue();
               this.setFadeDistance(value6);
               int n32 = 0;
               n16 += s + 100;
               d = (double)(n3 + n16);
               double d18 = (double)(n4 + n32);
               double d19 = d;
               String str2 = "FadeColor:";
               customFontUtil.drawString(str2, d19, d18, colorInt);
               n32 += 25;
               primaryHsb2 = this.fadeHsb;
               ColorHueSliderComponent fadeHueSlider2 = this.fadeHueSlider;
               n14 = n3 + n16;
               n7 = n4 - 2 + n32;
               d4 = (double)this.fadeHsb[0];
               primaryHsb3 = this.fadeHsb;
               int fadeAlpha = this.getFadeAlpha();
               float[] floats4 = primaryHsb3;
               byte b8 = 7;
               double d20 = 1.0;
               double d21 = 0.0;
               double d22 = d4;
               int n33 = n7;
               int n34 = n14;
               primaryHsb2[0] = (float)fadeHueSlider2.drawHueSlider(n34, n33, s, b, n, n2, d22, d21, d20, b8, customFontUtil, colorInt2, floats4, fadeAlpha);
               n32 += 25;
               primaryHsb2 = this.fadeHsb;
               ColorSaturationSliderComponent fadeSaturationSlider2 = this.fadeSaturationSlider;
               n14 = n3 + n16;
               n7 = n4 - 2 + n32;
               d8 = (double)this.fadeHsb[1];
               primaryHsb3 = this.fadeHsb;
               int fadeAlpha2 = this.getFadeAlpha();
               float[] floats5 = primaryHsb3;
               byte b9 = 7;
               double d23 = 1.0;
               double d24 = 0.0;
               double d25 = d8;
               int n35 = n7;
               int n36 = n14;
               primaryHsb2[1] = (float)fadeSaturationSlider2.drawSaturationSlider(n36, n35, s, b, n, n2, d25, d24, d23, b9, customFontUtil, colorInt2, floats5, fadeAlpha2);
               n32 += 25;
               primaryHsb2 = this.fadeHsb;
               ColorBrightnessSliderComponent fadeBrightnessSlider2 = this.fadeBrightnessSlider;
               n14 = n3 + n16;
               n7 = n4 - 2 + n32;
               d4 = (double)this.fadeHsb[2];
               primaryHsb3 = this.fadeHsb;
               int fadeAlpha3 = this.getFadeAlpha();
               float[] floats6 = primaryHsb3;
               byte b10 = 7;
               double d26 = 1.0;
               double d27 = 0.0;
               double d28 = d4;
               int n37 = n7;
               int n38 = n14;
               primaryHsb2[2] = (float)fadeBrightnessSlider2.drawBrightnessSlider(n38, n37, s, b, n, n2, d28, d27, d26, b10, customFontUtil, colorInt2, floats6, fadeAlpha3);
               n32 += 25;
               ColorAlphaSliderComponent fadeAlphaSlider2 = this.fadeAlphaSlider;
               int n39 = n3 + n16;
               n14 = n4 - 2 + n32;
               primaryAlpha4 = (double)this.getFadeAlpha();
               float[] fadeHsb2 = this.fadeHsb;
               byte b11 = 7;
               double d29 = 255.0;
               double d30 = 0.0;
               double d31 = primaryAlpha4;
               int n40 = n14;
               int n41 = n39;
               int n42 = (int)fadeAlphaSlider2.drawAlphaSlider(n41, n40, s, b, n, n2, d31, d30, d29, b11, customFontUtil, colorInt2, fadeHsb2);
               this.setFadeAlpha(n42);
               n17 = 0;
               n16 += s + 50;
               Color color2 = new Color(Color.HSBtoRGB(this.fadeHsb[0], this.fadeHsb[1], this.fadeHsb[2]));
               this.fadeColor = new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), this.getFadeAlpha());
            }
         }

         if (this.isRainbowEnabled()) {
            n16 += s + 100;
            n17 = 25;
         }

         n14 = n3 + n16;
         n7 = n4 + n17;
         n24 = n6 + 50;
         boolean flag8 = true;
         int n43 = n24;
         int n44 = n7;
         int n45 = n14;
         this.astolfo.render(n, n2, n45, n44, n5, n43, customFontUtil, flag8);
         boolean value7 = this.astolfo.getValue();
         this.setAstolfoEnabled(value7);
         if (this.isAstolfoEnabled() && !this.isRainbowEnabled() && !this.isFadeEnabled()) {
            if (!this.isClientColorEnabled()) {
               n16 += 50 + subPanelOffsetX;
               int n46 = 25;
               n14 = n3 + n16;
               n7 = n4 + n46;
               boolean flag9 = true;
               int n47 = n7;
               int n48 = n14;
               this.aSpeed.render(n, n2, n48, n47, n5, n6, customFontUtil, flag9);
               double value8 = this.aSpeed.getValue();
               this.setAstolfoSpeed(value8);
               n46 += 25;
               n14 = n3 + n16;
               n7 = n4 + n46;
               boolean flag10 = true;
               int n49 = n7;
               int n50 = n14;
               this.aDist.render(n, n2, n50, n49, n5, n6, customFontUtil, flag10);
               double value9 = this.aDist.getValue();
               this.setAstolfoDistance(value9);
               n17 = n46 + 25;
               n16 -= 50 + subPanelOffsetX;
            }
         }

         if (!(this.getParentModule() instanceof CustomClientColor)) {
            n17 += 25;
            n14 = n3 + n16;
            n7 = n4 + n17;
            n24 = n6 + 50;
            boolean flag11 = true;
            int n51 = n24;
            int n52 = n7;
            int n53 = n14;
            this.clientColor.render(n, n2, n53, n52, n5, n51, customFontUtil, flag11);
            boolean value10 = this.clientColor.getValue();
            this.setClientColorEnabled(value10);
            if (this.isClientColorEnabled()) {
               n17 = 25;
               n16 += 50 + subPanelOffsetX;
            }

            if (this.isClientColorEnabled()) {
               n14 = n3 + n16;
               n7 = n4 + n17;
               n24 = n6 + 50;
               boolean flag12 = true;
               int n54 = n24;
               int n55 = n7;
               int n56 = n14;
               this.clientColorAlpha.render(n, n2, n56, n55, n5, n54, customFontUtil, flag12);
               int n57 = MathHelper.clamp_int((int)this.clientColorAlpha.getValue(), 1, 255);
               this.setClientColorAlpha(n57);
            }
         } else {
            boolean flag13 = false;
            this.setClientColorEnabled(flag13);
         }

         this.contentWidth = n16 + s + 20;
         RenderUtil.resetGlColorState();
      }
   }

   private ColorSetting(String str, Module module, DoubleValue doubleValue, Color color) {
       super(module, doubleValue, str);
      IntegerProperty integerProperty = new IntegerProperty(this, "A", 0);
      Consumer consumer = obj -> {
         Color color2 = new Color((Integer)this.c.getValue());
         Color color3 = new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), (Integer)obj);
         this.setPrimaryColor(color3);
      };
      this.a = (IntegerProperty)integerProperty.withOnValueChanged(consumer);
      IntegerProperty integerProperty2 = new IntegerProperty(this, "FA", 0);
      Consumer consumer2 = obj2 -> this.onFadeAlphaPropertyChanged((int)obj2);
      this.fa = (IntegerProperty)integerProperty2.withOnValueChanged(consumer2);
      BooleanProperty booleanProperty = new BooleanProperty(this, "R", false);
      Consumer consumer3 = obj3 -> this.setRainbowEnabled((boolean)obj3);
      this.r = (BooleanProperty)booleanProperty.withOnValueChanged(consumer3);
      BooleanProperty booleanProperty2 = new BooleanProperty(this, "F", false);
      Consumer consumer4 = obj4 -> this.setFadeEnabled((boolean)obj4);
      this.f = (BooleanProperty)booleanProperty2.withOnValueChanged(consumer4);
      BooleanProperty booleanProperty3 = new BooleanProperty(this, "AO", false);
      Consumer consumer5 = obj5 -> this.setAstolfoEnabled((boolean)obj5);
      this.ao = (BooleanProperty)booleanProperty3.withOnValueChanged(consumer5);
      BooleanProperty booleanProperty4 = new BooleanProperty(this, "CC", false);
      Consumer consumer6 = obj6 -> this.setClientColorEnabled((boolean)obj6);
      this.cc = (BooleanProperty)booleanProperty4.withOnValueChanged(consumer6);
      IntegerProperty integerProperty3 = new IntegerProperty(this, "FC", 0);
      Supplier supplier = () -> {
         Color fadeColor2 = this.fadeColor;
         return this.toOpaqueRgb(fadeColor2);
      };
      IntegerProperty integerProperty4 = (IntegerProperty)integerProperty3.withValueSupplier(supplier);
      Consumer consumer7 = obj7 -> this.onFadeRgbPropertyChanged((Integer)obj7);
      this.fc = (IntegerProperty)integerProperty4.withOnValueChanged(consumer7);
      IntegerProperty integerProperty5 = new IntegerProperty(this, "CCA", 100);
      Consumer consumer8 = obj8 -> this.setClientColorAlpha((int)obj8);
      this.cca = (IntegerProperty)integerProperty5.withOnValueChanged(consumer8);
      DoubleProperty doubleProperty = new DoubleProperty(this, "RS", 0.0);
      Consumer consumer9 = obj9 -> this.setRainbowSpeed((double)obj9);
      this.rs = (DoubleProperty)doubleProperty.withOnValueChanged(consumer9);
      DoubleProperty doubleProperty2 = new DoubleProperty(this, "RD", 0.0);
      Consumer consumer10 = obj10 -> this.setRainbowDistance((double)obj10);
      this.rd = (DoubleProperty)doubleProperty2.withOnValueChanged(consumer10);
      DoubleProperty doubleProperty3 = new DoubleProperty(this, "FS", 0.0);
      Consumer consumer11 = obj11 -> this.setFadeSpeed((double)obj11);
      this.fs = (DoubleProperty)doubleProperty3.withOnValueChanged(consumer11);
      DoubleProperty doubleProperty4 = new DoubleProperty(this, "FD", 0.0);
      Consumer consumer12 = obj12 -> this.setFadeDistance((double)obj12);
      this.fd = (DoubleProperty)doubleProperty4.withOnValueChanged(consumer12);
      DoubleProperty doubleProperty5 = new DoubleProperty(this, "AS", 0.0);
      Consumer consumer13 = obj13 -> this.setAstolfoSpeed((double)obj13);
      this.as = (DoubleProperty)doubleProperty5.withOnValueChanged(consumer13);
      DoubleProperty doubleProperty6 = new DoubleProperty(this, "AD", 0.0);
      Consumer consumer14 = obj14 -> this.setAstolfoDistance((double)obj14);
      this.ad = (DoubleProperty)doubleProperty6.withOnValueChanged(consumer14);
      IntegerProperty integerProperty6 = new IntegerProperty(this, "C", 0);
      Supplier supplier2 = () -> {
         Color primaryColor2 = this.primaryColor;
         return this.toOpaqueRgb(primaryColor2);
      };
      IntegerProperty integerProperty7 = (IntegerProperty)integerProperty6.withValueSupplier(supplier2);
      Consumer consumer15 = obj15 -> this.onPrimaryRgbPropertyChanged((Integer)obj15);
      this.c = (IntegerProperty)integerProperty7.withOnValueChanged(consumer15);
      this.primaryHsb = new float[3];
      this.fadeHsb = new float[3];
      this.primaryHueSlider = new ColorHueSliderComponent();
      this.primarySaturationSlider = new ColorSaturationSliderComponent();
      this.primaryBrightnessSlider = new ColorBrightnessSliderComponent();
      this.primaryAlphaSlider = new ColorAlphaSliderComponent();
      this.rainbow = new BooleanValue("Rainbow", false);
      this.fade = new BooleanValue("Fade", false);
      this.astolfo = new BooleanValue("Astolfo", false);
      this.clientColor = new BooleanValue("ClientColor", false);
      this.rSpeed = new StringValue("RSpeed", 70.0, 1.0, 200.0, 0);
      this.fSpeed = new StringValue("FSpeed", 70.0, 1.0, 200.0, 0);
      this.aSpeed = new StringValue("ASpeed", 70.0, 1.0, 200.0, 0);
      this.rDist = new StringValue("RDist", 60.0, 0.0, 200.0, 0);
      this.fDist = new StringValue("FDist", 60.0, 0.0, 200.0, 0);
      this.aDist = new StringValue("ADist", 60.0, 0.0, 200.0, 0);
      this.clientColorAlpha = new StringValue("ClientColorAlpha", 100.0, 1.0, 255.0, 0);
      this.fadeHueSlider = new ColorHueSliderComponent();
      this.fadeSaturationSlider = new ColorSaturationSliderComponent();
      this.fadeBrightnessSlider = new ColorBrightnessSliderComponent();
      this.fadeAlphaSlider = new ColorAlphaSliderComponent();
      this.renderYOffset = 0;
      this.computedArgb = 0;
      this.setPrimaryColor(color);
      this.setFadeColor(color);
      int alpha = this.fadeColor.getAlpha();
      this.setFadeAlpha(alpha);
      int alpha2 = this.primaryColor.getAlpha();
      int n = alpha2;
      this.setPrimaryAlpha(n);
      IntegerProperty integerProperty8 = this.c;
      Integer rgb = this.primaryColor.getRGB();
      Integer num = rgb;
      integerProperty8.setValue(num);
      integerProperty8 = this.c;
      Integer rgb2 = this.primaryColor.getRGB();
      integerProperty8.setDefaultValue(rgb2);
      integerProperty8 = this.fc;
      Integer rgb3 = this.primaryColor.getRGB();
      Integer num2 = rgb3;
      integerProperty8.setValue(num2);
      integerProperty8 = this.fc;
      Integer rgb4 = this.primaryColor.getRGB();
      integerProperty8.setDefaultValue(rgb4);
      integerProperty8 = this.a;
      Integer alpha3 = this.primaryColor.getAlpha();
      integerProperty8.setDefaultValue(alpha3);
      integerProperty8 = this.fa;
      Integer alpha4 = this.primaryColor.getAlpha();
      integerProperty8.setDefaultValue(alpha4);
      Color.RGBtoHSB(this.primaryColor.getRed(), this.primaryColor.getGreen(), this.primaryColor.getBlue(), this.primaryHsb);
      Color.RGBtoHSB(this.primaryColor.getRed(), this.primaryColor.getGreen(), this.primaryColor.getBlue(), this.fadeHsb);
   }

   public StringValue getASpeed() {
      return this.aSpeed;
   }

   @Override
   public void onGuiClosed() {
      super.onGuiClosed();
   }

   public ColorBrightnessSliderComponent getPrimaryBrightnessSlider() {
      return this.primaryBrightnessSlider;
   }

   private int computeFadeArgb(int n) {
      Color primaryColor2 = this.primaryColor;
      Color fadeColor2 = this.fadeColor;
      int fadeSpeed = (int)this.getFadeSpeed();
      Color color = fadeColor2;
      return ColorUtil.getAnimatedFadeColor(primaryColor2, color, n, fadeSpeed);
   }

   public void setAstolfoSpeed(double d) {
      DoubleProperty as2 = this.as;
      Double d2 = d;
      Double d3 = d2;
      as2.setValue(d3);
      this.aSpeed.setValue(d);
   }

   public ColorSetting withDefaultFadeColor(Color color) {
      Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
      this.setFadeColor(color2);
      IntegerProperty fc2 = this.fc;
      Integer rgb = color.getRGB();
      fc2.setDefaultValue(rgb);
      return this;
   }

   private int computeAstolfoArgb(int n) {
      return ColorUtil.getAstolfoColor(n, (int)this.getAstolfoSpeed(), this.primaryColor.getAlpha());
   }

   public ColorSetting withDefaultFadeSpeed(int n) {
      double d = (double)n;
      this.setFadeSpeed(d);
      DoubleProperty fs2 = this.fs;
      Double d2 = (double)n;
      fs2.setDefaultValue(d2);
      return this;
   }

   public ColorHueSliderComponent getPrimaryHueSlider() {
      return this.primaryHueSlider;
   }

   @Override
   public void mouseReleased(int mouseX, int mouseY, int state) {
      super.mouseReleased(mouseX, mouseY, state);
      if (!(this.getParentModule() instanceof CustomClientColor)) {
         this.clientColor.mouseReleased(mouseX, mouseY, state);
         this.clientColorAlpha.mouseReleased(mouseX, mouseY, state);
      }

      this.primaryHueSlider.mouseReleased(mouseX, mouseY, state);
      this.primarySaturationSlider.mouseReleased(mouseX, mouseY, state);
      this.primaryBrightnessSlider.mouseReleased(mouseX, mouseY, state);
      this.primaryAlphaSlider.mouseReleased(mouseX, mouseY, state);
      this.rSpeed.mouseReleased(mouseX, mouseY, state);
      this.fSpeed.mouseReleased(mouseX, mouseY, state);
      this.fadeHueSlider.mouseReleased(mouseX, mouseY, state);
      this.fadeSaturationSlider.mouseReleased(mouseX, mouseY, state);
      this.fadeBrightnessSlider.mouseReleased(mouseX, mouseY, state);
      this.fadeAlphaSlider.mouseReleased(mouseX, mouseY, state);
      this.fDist.mouseReleased(mouseX, mouseY, state);
      this.rDist.mouseReleased(mouseX, mouseY, state);
      this.aSpeed.mouseReleased(mouseX, mouseY, state);
      this.aDist.mouseReleased(mouseX, mouseY, state);
   }

   public int getGreen() {
      return ColorUtil.getGreen(this.computedArgb);
   }

   public boolean isAstolfoEnabled() {
      return (Boolean)this.ao.getValue();
   }

   public float getAlphaFloat(long l) {
      return ColorUtil.getAlphaFloat(this.computedArgb);
   }

   public void setFadeEnabled(boolean flag) {
      BooleanProperty booleanProperty = this.f;
      Boolean bool = flag;
      Boolean bool2 = bool;
      booleanProperty.setValue(bool2);
      this.fade.setValue(flag);
   }

   public ColorSetting withDefaultClientColorAlpha(int n) {
      this.setClientColorAlpha(n);
      IntegerProperty cca2 = this.cca;
      Integer num = n;
      cca2.setDefaultValue(num);
      return this;
   }

   public StringValue getClientColorAlpha() {
      return this.clientColorAlpha;
   }

   public StringValue getFDist() {
      return this.fDist;
   }

   public ColorHueSliderComponent getFadeHueSlider() {
      return this.fadeHueSlider;
   }

   public ColorSetting withDefaultFadeDistance(int n) {
      double d = (double)n;
      this.setFadeDistance(d);
      DoubleProperty fd2 = this.fd;
      Double d2 = (double)n;
      fd2.setDefaultValue(d2);
      return this;
   }

   public int getPrimaryAlpha() {
      return (Integer)this.a.getValue();
   }

   public int getFadeAlpha() {
      return (Integer)this.fa.getValue();
   }

   public ColorAlphaSliderComponent getPrimaryAlphaSlider() {
      return this.primaryAlphaSlider;
   }

   @Override
   public void keyTyped(char typedChar, int keyCode) throws IOException {
      super.keyTyped(typedChar, keyCode);
      if (!(this.getParentModule() instanceof CustomClientColor)) {
         this.clientColor.keyTyped(typedChar, keyCode);
         this.clientColorAlpha.keyTyped(typedChar, keyCode);
      }

      this.primaryHueSlider.keyTyped(typedChar, keyCode);
      this.primarySaturationSlider.keyTyped(typedChar, keyCode);
      this.primaryBrightnessSlider.keyTyped(typedChar, keyCode);
      this.primaryAlphaSlider.keyTyped(typedChar, keyCode);
      this.rSpeed.keyTyped(typedChar, keyCode);
      this.fSpeed.keyTyped(typedChar, keyCode);
      this.fadeHueSlider.keyTyped(typedChar, keyCode);
      this.fadeSaturationSlider.keyTyped(typedChar, keyCode);
      this.fadeBrightnessSlider.keyTyped(typedChar, keyCode);
      this.fadeAlphaSlider.keyTyped(typedChar, keyCode);
      this.fDist.keyTyped(typedChar, keyCode);
      this.rDist.keyTyped(typedChar, keyCode);
      this.aSpeed.keyTyped(typedChar, keyCode);
      this.aDist.keyTyped(typedChar, keyCode);
   }

   public ColorSetting(Module module, String str, int n, DoubleValue doubleValue) {
       this(str, module, doubleValue, new Color(n));
   }

   public int getClientColorAlpha2() {
      return (Integer)this.cca.getValue();
   }

   public double getFadeDistance() {
      return (Double)this.fd.getValue();
   }

   public StringValue getADist() {
      return this.aDist;
   }

   public void setRainbowSpeed(double d) {
      DoubleProperty rs2 = this.rs;
      Double d2 = d;
      Double d3 = d2;
      rs2.setValue(d3);
      this.rSpeed.setValue(d);
   }

   public ColorSetting(Module module, String str, Color color, DoubleValue doubleValue) {
       this(str, module, doubleValue, color);
   }

   private void onFadeRgbPropertyChanged(Integer num) {
      Color color = new Color(num);
      Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), this.getFadeAlpha());
      this.setFadeColor(color2);
   }

   public ColorSetting withDefaultRainbowSpeed(int n) {
      double d = (double)n;
      this.setRainbowSpeed(d);
      DoubleProperty rs2 = this.rs;
      Double d2 = (double)n;
      rs2.setDefaultValue(d2);
      return this;
   }

   public ColorSetting withClientColorDefault(boolean flag) {
      this.setClientColorEnabled(flag);
      BooleanProperty cc2 = this.cc;
      Boolean bool = flag;
      cc2.setDefaultValue(bool);
      return this;
   }

   public void setPrimaryColor(Color color) {
      this.primaryColor = color;
      int alpha = this.primaryColor.getAlpha();
      int n = alpha;
      this.setPrimaryAlpha(n);
      Color.RGBtoHSB(this.primaryColor.getRed(), this.primaryColor.getGreen(), this.primaryColor.getBlue(), this.primaryHsb);
   }

   public double getFadeSpeed() {
      return (Double)this.fs.getValue();
   }

   public boolean isClientColorEnabled() {
      return (Boolean)this.cc.getValue();
   }

   @Override
   public int getHeight() {
      return 127;
   }

   public Color getPrimaryColor() {
      return this.primaryColor;
   }

   public BooleanValue getFade() {
      return this.fade;
   }

   public double getRainbowSpeed() {
      return (Double)this.rs.getValue();
   }

   public void setFadeDistance(double d) {
      DoubleProperty fd2 = this.fd;
      Double d2 = d;
      Double d3 = d2;
      fd2.setValue(d3);
      this.fDist.setValue(d);
   }

   public StringValue getRDist() {
      return this.rDist;
   }

   public double getAstolfoDistance() {
      return (Double)this.ad.getValue();
   }

   public int getRenderYOffset() {
      return this.renderYOffset;
   }

   public StringValue getRSpeed() {
      return this.rSpeed;
   }

   public double getRainbowDistance() {
      return (Double)this.rd.getValue();
   }

   public ColorBrightnessSliderComponent getFadeBrightnessSlider() {
      return this.fadeBrightnessSlider;
   }

   public ColorSetting(Module module, String str, Color color) {
       this(str, module, null, color);
   }

   public void setFadeHsb(float[] floats) {
      this.fadeHsb = floats;
   }

   public void setPrimaryHsb(float[] floats) {
      this.primaryHsb = floats;
   }

   public float[] getFadeHsb() {
      return this.fadeHsb;
   }

   public float getGreenFloat() {
      return ColorUtil.getGreenFloat(this.computedArgb);
   }

   public void setClientColorEnabled(boolean flag) {
      BooleanProperty cc2 = this.cc;
      Boolean bool = flag;
      Boolean bool2 = bool;
      cc2.setValue(bool2);
      this.clientColor.setValue(flag);
   }

   private int toOpaqueRgb(Color color) {
      Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue());
      return color2.getRGB();
   }

   public BooleanValue getClientColor() {
      return this.clientColor;
   }

   public void setFadeColor(Color color) {
      this.fadeColor = color;
      int alpha = this.fadeColor.getAlpha();
      this.setFadeAlpha(alpha);
      Color.RGBtoHSB(this.fadeColor.getRed(), this.fadeColor.getGreen(), this.fadeColor.getBlue(), this.fadeHsb);
   }

}
