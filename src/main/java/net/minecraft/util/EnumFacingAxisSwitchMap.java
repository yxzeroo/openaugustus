package net.minecraft.util;


public class EnumFacingAxisSwitchMap {
   public static final EnumFacingAxisSwitchMap INSTANCE = new EnumFacingAxisSwitchMap();
   public final int[] FACING_LOOKUP;
   public final int[] AXIS_LOOKUP = new int[EnumFacing.Axis.values().length];

   public EnumFacingAxisSwitchMap() {
      try {
         this.AXIS_LOOKUP[EnumFacing.Axis.X.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         this.AXIS_LOOKUP[EnumFacing.Axis.Y.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         this.AXIS_LOOKUP[EnumFacing.Axis.Z.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      this.FACING_LOOKUP = new int[EnumFacing.getValues().length];

      try {
         this.FACING_LOOKUP[EnumFacing.DOWN.ordinal()] = 1;
      } catch (NoSuchFieldError e4) {
      }

      try {
         this.FACING_LOOKUP[EnumFacing.UP.ordinal()] = 2;
      } catch (NoSuchFieldError e5) {
      }

      try {
         this.FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 3;
      } catch (NoSuchFieldError e6) {
      }

      try {
         this.FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 4;
      } catch (NoSuchFieldError e7) {
      }

      try {
         this.FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 5;
      } catch (NoSuchFieldError e8) {
      }

      try {
         this.FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 6;
      } catch (NoSuchFieldError e9) {
      }
   }
}
