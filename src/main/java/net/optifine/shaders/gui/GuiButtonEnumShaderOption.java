package net.optifine.shaders.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.optifine.shaders.Shaders;
import net.optifine.shaders.config.EnumShaderOption;

public class GuiButtonEnumShaderOption extends GuiButton {
   private EnumShaderOption enumShaderOption = null;

   private static String getButtonText(EnumShaderOption enumShaderOption2) {
      String str = I18n.format(enumShaderOption2.getResourceKey(), new Object[0]) + ": ";
      switch (GuiButtonEnumShaderOptionSwitchMap.ORDINAL_MAP[enumShaderOption2.ordinal()]) {
         case 1:
            return str + GuiShaders.toStringAa(Shaders.configAntialiasingLevel);
         case 2:
            return str + GuiShaders.toStringOnOff(Shaders.configNormalMap);
         case 3:
            return str + GuiShaders.toStringOnOff(Shaders.configSpecularMap);
         case 4:
            return str + GuiShaders.toStringQuality(Shaders.configRenderResMul);
         case 5:
            return str + GuiShaders.toStringQuality(Shaders.configShadowResMul);
         case 6:
            return str + GuiShaders.toStringHandDepth(Shaders.configHandDepthMul);
         case 7:
            return str + GuiShaders.toStringOnOff(Shaders.configCloudShadow);
         case 8:
            return str + Shaders.configOldHandLight.getUserValue();
         case 9:
            return str + Shaders.configOldLighting.getUserValue();
         case 10:
            return str + GuiShaders.toStringOnOff(Shaders.configShadowClipFrustrum);
         case 11:
            return str + GuiShaders.toStringOnOff(Shaders.configTweakBlockDamage);
         default:
            return str + Shaders.getEnumShaderOption(enumShaderOption2);
      }
   }

   public void updateButtonText() {
      this.displayString = getButtonText(this.enumShaderOption);
   }

   public GuiButtonEnumShaderOption(EnumShaderOption enumShaderOption2, int n, int n2, int n3, int n4) {
      super(enumShaderOption2.ordinal(), n, n2, n3, n4, getButtonText(enumShaderOption2));
      this.enumShaderOption = enumShaderOption2;
   }

   public EnumShaderOption getEnumShaderOption() {
      return this.enumShaderOption;
   }
}
