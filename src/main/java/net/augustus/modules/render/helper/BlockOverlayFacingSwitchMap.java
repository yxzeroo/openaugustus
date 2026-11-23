package net.augustus.modules.render.helper;

import net.minecraft.util.EnumFacing;

public class BlockOverlayFacingSwitchMap {
   public static final int[] ORDINAL_MAP = new int[EnumFacing.values().length];

   static {
      try {
         ORDINAL_MAP[EnumFacing.SOUTH.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[EnumFacing.NORTH.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[EnumFacing.EAST.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         ORDINAL_MAP[EnumFacing.WEST.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }

      try {
         ORDINAL_MAP[EnumFacing.UP.ordinal()] = 5;
      } catch (NoSuchFieldError e5) {
      }

      try {
         ORDINAL_MAP[EnumFacing.DOWN.ordinal()] = 6;
      } catch (NoSuchFieldError e6) {
      }
   }
}
