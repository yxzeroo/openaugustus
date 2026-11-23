package net.augustus.modules.modes.anticheat.eagle;

import net.minecraft.util.EnumFacing;

public class MoonWalkFacingSwitchMap {
   public static final int[] ORDINAL_MAP = new int[EnumFacing.values().length];

   static {
      try {
         ORDINAL_MAP[EnumFacing.EAST.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[EnumFacing.WEST.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[EnumFacing.NORTH.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         ORDINAL_MAP[EnumFacing.SOUTH.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }
   }
}
