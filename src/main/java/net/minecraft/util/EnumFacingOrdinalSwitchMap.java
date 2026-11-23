package net.minecraft.util;


public class EnumFacingOrdinalSwitchMap {
   public static final EnumFacingOrdinalSwitchMap INSTANCE = new EnumFacingOrdinalSwitchMap();
   public final int[] FACING_LOOKUP = new int[EnumFacing.getValues().length];

   public EnumFacingOrdinalSwitchMap() {
      try {
         this.FACING_LOOKUP[EnumFacing.DOWN.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         this.FACING_LOOKUP[EnumFacing.UP.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         this.FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         this.FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }

      try {
         this.FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 5;
      } catch (NoSuchFieldError e5) {
      }

      try {
         this.FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 6;
      } catch (NoSuchFieldError e6) {
      }
   }
}
