package net.minecraft.util;


public class EnumFacingPlaneSwitchMap {
   public static final EnumFacingPlaneSwitchMap INSTANCE = new EnumFacingPlaneSwitchMap();
   public final int[] AXIS_LOOKUP;
   public final int[] FACING_LOOKUP;
   public final int[] PLANE_LOOKUP = new int[EnumFacing.Plane.values().length];

   public EnumFacingPlaneSwitchMap() {
      try {
         this.PLANE_LOOKUP[EnumFacing.Plane.HORIZONTAL.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         this.PLANE_LOOKUP[EnumFacing.Plane.VERTICAL.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      this.FACING_LOOKUP = new int[EnumFacing.getValues().length];

      try {
         this.FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 1;
      } catch (NoSuchFieldError e3) {
      }

      try {
         this.FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 2;
      } catch (NoSuchFieldError e4) {
      }

      try {
         this.FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 3;
      } catch (NoSuchFieldError e5) {
      }

      try {
         this.FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 4;
      } catch (NoSuchFieldError e6) {
      }

      try {
         this.FACING_LOOKUP[EnumFacing.UP.ordinal()] = 5;
      } catch (NoSuchFieldError e7) {
      }

      try {
         this.FACING_LOOKUP[EnumFacing.DOWN.ordinal()] = 6;
      } catch (NoSuchFieldError e8) {
      }

      this.AXIS_LOOKUP = new int[EnumFacing.Axis.values().length];

      try {
         this.AXIS_LOOKUP[EnumFacing.Axis.X.ordinal()] = 1;
      } catch (NoSuchFieldError e9) {
      }

      try {
         this.AXIS_LOOKUP[EnumFacing.Axis.Y.ordinal()] = 2;
      } catch (NoSuchFieldError e10) {
      }

      try {
         this.AXIS_LOOKUP[EnumFacing.Axis.Z.ordinal()] = 3;
      } catch (NoSuchFieldError e11) {
      }
   }
}
