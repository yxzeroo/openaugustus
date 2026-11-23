package net.optifine.shaders.gui;

import net.optifine.shaders.config.EnumShaderOption;

public class GuiButtonEnumShaderOptionSwitchMap {
   public static final int[] ORDINAL_MAP = new int[EnumShaderOption.values().length];

   static {
      try {
         ORDINAL_MAP[EnumShaderOption.ANTIALIASING.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[EnumShaderOption.NORMAL_MAP.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[EnumShaderOption.SPECULAR_MAP.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         ORDINAL_MAP[EnumShaderOption.RENDER_RES_MUL.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }

      try {
         ORDINAL_MAP[EnumShaderOption.SHADOW_RES_MUL.ordinal()] = 5;
      } catch (NoSuchFieldError e5) {
      }

      try {
         ORDINAL_MAP[EnumShaderOption.HAND_DEPTH_MUL.ordinal()] = 6;
      } catch (NoSuchFieldError e6) {
      }

      try {
         ORDINAL_MAP[EnumShaderOption.CLOUD_SHADOW.ordinal()] = 7;
      } catch (NoSuchFieldError e7) {
      }

      try {
         ORDINAL_MAP[EnumShaderOption.OLD_HAND_LIGHT.ordinal()] = 8;
      } catch (NoSuchFieldError e8) {
      }

      try {
         ORDINAL_MAP[EnumShaderOption.OLD_LIGHTING.ordinal()] = 9;
      } catch (NoSuchFieldError e9) {
      }

      try {
         ORDINAL_MAP[EnumShaderOption.SHADOW_CLIP_FRUSTRUM.ordinal()] = 10;
      } catch (NoSuchFieldError e10) {
      }

      try {
         ORDINAL_MAP[EnumShaderOption.TWEAK_BLOCK_DAMAGE.ordinal()] = 11;
      } catch (NoSuchFieldError e11) {
      }
   }
}
