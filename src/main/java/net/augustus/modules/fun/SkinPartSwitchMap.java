package net.augustus.modules.fun;

import net.minecraft.entity.player.EnumPlayerModelParts;

public class SkinPartSwitchMap {
   public static final int[] ORDINAL_MAP = new int[EnumPlayerModelParts.values().length];

   static {
      try {
         ORDINAL_MAP[EnumPlayerModelParts.CAPE.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[EnumPlayerModelParts.JACKET.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[EnumPlayerModelParts.LEFT_SLEEVE.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         ORDINAL_MAP[EnumPlayerModelParts.RIGHT_SLEEVE.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }

      try {
         ORDINAL_MAP[EnumPlayerModelParts.LEFT_PANTS_LEG.ordinal()] = 5;
      } catch (NoSuchFieldError e5) {
      }

      try {
         ORDINAL_MAP[EnumPlayerModelParts.RIGHT_PANTS_LEG.ordinal()] = 6;
      } catch (NoSuchFieldError e6) {
      }

      try {
         ORDINAL_MAP[EnumPlayerModelParts.HAT.ordinal()] = 7;
      } catch (NoSuchFieldError e7) {
      }
   }
}
