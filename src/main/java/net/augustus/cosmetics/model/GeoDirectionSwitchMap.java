package net.augustus.cosmetics.model;

public class GeoDirectionSwitchMap {
   public static final int[] DIRECTION_SWITCH_MAP;
   public static final int[] PLANE_SWITCH_MAP = new int[GeoDirectionPlane.values().length];
   public static final int[] AXIS_SWITCH_MAP;

   static {
      try {
         PLANE_SWITCH_MAP[GeoDirectionPlane.HORIZONTAL.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         PLANE_SWITCH_MAP[GeoDirectionPlane.VERTICAL.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      DIRECTION_SWITCH_MAP = new int[GeoDirection.values().length];

      try {
         DIRECTION_SWITCH_MAP[GeoDirection.NORTH.ordinal()] = 1;
      } catch (NoSuchFieldError e3) {
      }

      try {
         DIRECTION_SWITCH_MAP[GeoDirection.EAST.ordinal()] = 2;
      } catch (NoSuchFieldError e4) {
      }

      try {
         DIRECTION_SWITCH_MAP[GeoDirection.SOUTH.ordinal()] = 3;
      } catch (NoSuchFieldError e5) {
      }

      try {
         DIRECTION_SWITCH_MAP[GeoDirection.WEST.ordinal()] = 4;
      } catch (NoSuchFieldError e6) {
      }

      try {
         DIRECTION_SWITCH_MAP[GeoDirection.UP.ordinal()] = 5;
      } catch (NoSuchFieldError e7) {
      }

      try {
         DIRECTION_SWITCH_MAP[GeoDirection.DOWN.ordinal()] = 6;
      } catch (NoSuchFieldError e8) {
      }

      AXIS_SWITCH_MAP = new int[GeoAxis.values().length];

      try {
         AXIS_SWITCH_MAP[GeoAxis.X.ordinal()] = 1;
      } catch (NoSuchFieldError e9) {
      }

      try {
         AXIS_SWITCH_MAP[GeoAxis.Y.ordinal()] = 2;
      } catch (NoSuchFieldError e10) {
      }

      try {
         AXIS_SWITCH_MAP[GeoAxis.Z.ordinal()] = 3;
      } catch (NoSuchFieldError e11) {
      }
   }
}
