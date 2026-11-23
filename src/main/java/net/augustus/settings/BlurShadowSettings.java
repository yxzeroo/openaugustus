package net.augustus.settings;

import net.augustus.Augustus;
import net.augustus.ClientManagers;
import net.augustus.utils.render.ShaderRenderPass;
import java.awt.Color;
import net.augustus.modules.Module;

public class BlurShadowSettings extends SettingComposite {
   private final DoubleValue parentGroup;
   private String namePrefix;
   private final Module parentModule;
   private BooleanValue shadow;
   private ColorSetting customShadowColor;
   private BooleanValue blur;
   private BooleanValue useCustomShadowColor;

   public BlurShadowSettings withCustomShadowColor(Color color) {
      BooleanValue booleanValue = new BooleanValue(this.parentModule, this.namePrefix + "CustomShadowColor", false, this.parentGroup);
      SettingVisibilityCondition condition = () -> this.isCustomShadowToggleVisible();
      Setting setting = booleanValue.withVisibility(condition);
      String str = "CustomShadowColor";
      this.useCustomShadowColor = (BooleanValue)this.registerNamedSetting(str, setting);
      ColorSetting colorSetting = new ColorSetting(this.parentModule, this.namePrefix + "CustomShadowColorValue", color, this.parentGroup);
      String str2 = "CustomShadowColor";
      ColorSetting colorSetting2 = (ColorSetting)colorSetting.withDisplayName(str2);
      SettingVisibilityCondition condition2 = () -> this.isCustomShadowColorVisible();
      Setting setting2 = colorSetting2.withVisibility(condition2);
      String str3 = "CustomShadowColorValue";
      this.customShadowColor = (ColorSetting)this.registerNamedSetting(str3, setting2);
      return this;
   }

   public BlurShadowSettings(Module module, DoubleValue doubleValue) {
       this(module, "", doubleValue);
   }

   public BlurShadowSettings(Module module, String str, DoubleValue doubleValue) {
       super();
      this.useCustomShadowColor = null;
      this.customShadowColor = null;
      this.namePrefix = "";
      this.namePrefix = str;
      this.parentGroup = doubleValue;
      this.parentModule = module;
      BooleanValue booleanValue = new BooleanValue(module, str + "Blur", true, doubleValue);
      String str2 = "Blur";
      this.blur = (BooleanValue)this.registerNamedSetting(str2, booleanValue);
      BooleanValue booleanValue2 = new BooleanValue(module, str + "Shadow", true, doubleValue);
      String str3 = "Shadow";
      this.shadow = (BooleanValue)this.registerNamedSetting(str3, booleanValue2);
   }

   public BlurShadowSettings(Module module) {
       this(module, "", null);
   }

   public int resolveShadowColor(int n) {
      int n2;
      if (this.useCustomShadowColor != null && this.useCustomShadowColor.getValue()) {
         byte b = 0;
         n2 = this.customShadowColor.getColorInt(b);
      } else {
         n2 = n;
      }

      return n2;
   }

   public boolean isBlurActive() {
      return this.blur.getValue() && ClientManagers.moduleManager.blur.isToggled();
   }

   public boolean isShadowActive() {
      return this.shadow.getValue() && ClientManagers.moduleManager.shadow.isToggled();
   }

   public BlurShadowSettings withCustomShadowColor2(int n) {
      Color color = new Color(n);
      Color color2 = color;
      return this.withCustomShadowColor(color2);
   }

   public BlurShadowSettings disableBlurByDefault() {
      if (this.blur != null) {
         boolean flag = false;
         this.blur.setValue(flag);
         boolean flag2 = false;
         this.blur.setDefaultValue(flag2);
      }

      return this;
   }

   public int resolvePassColor(ShaderRenderPass shaderRenderPass, int n) {
      int n2;
      if (shaderRenderPass == ShaderRenderPass.SHADOW && this.useCustomShadowColor != null && this.useCustomShadowColor.getValue()) {
         byte b = 0;
         n2 = this.customShadowColor.getColorInt(b);
      } else {
         n2 = n;
      }

      return n2;
   }

   public int resolvePassColor(ShaderRenderPass shaderRenderPass, int n, int n2) {
      return shaderRenderPass == ShaderRenderPass.SHADOW && this.useCustomShadowColor != null && this.useCustomShadowColor.getValue() ? this.customShadowColor.getColorInt(n2) : n;
   }

   public BlurShadowSettings removeShadowSetting(long l) {
      ClientManagers.settingManager.getSettings().remove(this.shadow);
      if (this.parentGroup != null) {
         this.parentGroup.getChildren().remove(this.shadow);
      } else {
         this.parentModule.getSettings().remove(this.shadow);
      }

      this.shadow = null;
      return this;
   }

   public BlurShadowSettings disableShadowByDefault() {
      if (this.shadow != null) {
         boolean flag = false;
         this.shadow.setValue(flag);
         boolean flag2 = false;
         this.shadow.setDefaultValue(flag2);
      }

      return this;
   }

   public BlurShadowSettings disableBothByDefault() {
      if (this.shadow != null) {
         boolean flag = false;
         this.shadow.setValue(flag);
         boolean flag2 = false;
         this.shadow.setDefaultValue(flag2);
      }

      if (this.blur != null) {
         boolean flag3 = false;
         this.blur.setValue(flag3);
         boolean flag4 = false;
         this.blur.setDefaultValue(flag4);
      }

      return this;
   }

   public BlurShadowSettings(Module module, String str) {
       this(module, str, null);
   }

   private boolean isCustomShadowToggleVisible() {
      return this.shadow.getValue() && !Augustus.getInstance().getModuleManager().shadow.customColor.getValue();
   }

   public int resolveShadowColor(int n, int n2) {
      return this.useCustomShadowColor != null && this.useCustomShadowColor.getValue() ? this.customShadowColor.getColorInt(n2) : n;
   }

   private boolean isCustomShadowColorVisible() {
      return this.shadow.getValue() && !Augustus.getInstance().getModuleManager().shadow.customColor.getValue() && this.useCustomShadowColor.getValue();
   }

   public BlurShadowSettings removeBlurSetting() {
      ClientManagers.settingManager.getSettings().remove(this.blur);
      if (this.parentGroup != null) {
         this.parentGroup.getChildren().remove(this.blur);
      } else {
         this.parentModule.getSettings().remove(this.blur);
      }

      this.blur = null;
      return this;
   }


}
