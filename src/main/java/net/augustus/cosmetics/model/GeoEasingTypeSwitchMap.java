package net.augustus.cosmetics.model;

public class GeoEasingTypeSwitchMap {
   public static final int[] ORDINAL_MAP = new int[GeoEasingType.values().length];

   static {
      try {
         ORDINAL_MAP[GeoEasingType.Linear.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.Step.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInSine.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseOutSine.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInOutSine.ordinal()] = 5;
      } catch (NoSuchFieldError e5) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInQuad.ordinal()] = 6;
      } catch (NoSuchFieldError e6) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseOutQuad.ordinal()] = 7;
      } catch (NoSuchFieldError e7) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInOutQuad.ordinal()] = 8;
      } catch (NoSuchFieldError e8) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInCubic.ordinal()] = 9;
      } catch (NoSuchFieldError e9) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseOutCubic.ordinal()] = 10;
      } catch (NoSuchFieldError e10) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInOutCubic.ordinal()] = 11;
      } catch (NoSuchFieldError e11) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInExpo.ordinal()] = 12;
      } catch (NoSuchFieldError e12) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseOutExpo.ordinal()] = 13;
      } catch (NoSuchFieldError e13) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInOutExpo.ordinal()] = 14;
      } catch (NoSuchFieldError e14) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInCirc.ordinal()] = 15;
      } catch (NoSuchFieldError e15) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseOutCirc.ordinal()] = 16;
      } catch (NoSuchFieldError e16) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInOutCirc.ordinal()] = 17;
      } catch (NoSuchFieldError e17) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInQuart.ordinal()] = 18;
      } catch (NoSuchFieldError e18) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseOutQuart.ordinal()] = 19;
      } catch (NoSuchFieldError e19) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInOutQuart.ordinal()] = 20;
      } catch (NoSuchFieldError e20) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInQuint.ordinal()] = 21;
      } catch (NoSuchFieldError e21) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseOutQuint.ordinal()] = 22;
      } catch (NoSuchFieldError e22) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInOutQuint.ordinal()] = 23;
      } catch (NoSuchFieldError e23) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInBack.ordinal()] = 24;
      } catch (NoSuchFieldError e24) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseOutBack.ordinal()] = 25;
      } catch (NoSuchFieldError e25) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInOutBack.ordinal()] = 26;
      } catch (NoSuchFieldError e26) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInElastic.ordinal()] = 27;
      } catch (NoSuchFieldError e27) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseOutElastic.ordinal()] = 28;
      } catch (NoSuchFieldError e28) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInOutElastic.ordinal()] = 29;
      } catch (NoSuchFieldError e29) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInBounce.ordinal()] = 30;
      } catch (NoSuchFieldError e30) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseOutBounce.ordinal()] = 31;
      } catch (NoSuchFieldError e31) {
      }

      try {
         ORDINAL_MAP[GeoEasingType.EaseInOutBounce.ordinal()] = 32;
      } catch (NoSuchFieldError e32) {
      }
   }
}
