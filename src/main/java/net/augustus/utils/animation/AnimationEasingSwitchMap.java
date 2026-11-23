package net.augustus.utils.animation;

public class AnimationEasingSwitchMap {
   public static final int[] ORDINAL_MAP = new int[EasingType.values().length];

   static {
      try {
         ORDINAL_MAP[EasingType.easeInSine.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[EasingType.easeOutSine.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInOutSine.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInQuad.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }

      try {
         ORDINAL_MAP[EasingType.easeOutQuad.ordinal()] = 5;
      } catch (NoSuchFieldError e5) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInOutQuad.ordinal()] = 6;
      } catch (NoSuchFieldError e6) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInCubic.ordinal()] = 7;
      } catch (NoSuchFieldError e7) {
      }

      try {
         ORDINAL_MAP[EasingType.easeOutCubic.ordinal()] = 8;
      } catch (NoSuchFieldError e8) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInOutCubic.ordinal()] = 9;
      } catch (NoSuchFieldError e9) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInQuart.ordinal()] = 10;
      } catch (NoSuchFieldError e10) {
      }

      try {
         ORDINAL_MAP[EasingType.easeOutQuart.ordinal()] = 11;
      } catch (NoSuchFieldError e11) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInOutQuart.ordinal()] = 12;
      } catch (NoSuchFieldError e12) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInQuint.ordinal()] = 13;
      } catch (NoSuchFieldError e13) {
      }

      try {
         ORDINAL_MAP[EasingType.easeOutQuint.ordinal()] = 14;
      } catch (NoSuchFieldError e14) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInOutQuint.ordinal()] = 15;
      } catch (NoSuchFieldError e15) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInExpo.ordinal()] = 16;
      } catch (NoSuchFieldError e16) {
      }

      try {
         ORDINAL_MAP[EasingType.easeOutExpo.ordinal()] = 17;
      } catch (NoSuchFieldError e17) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInOutExpo.ordinal()] = 18;
      } catch (NoSuchFieldError e18) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInCirc.ordinal()] = 19;
      } catch (NoSuchFieldError e19) {
      }

      try {
         ORDINAL_MAP[EasingType.easeOutCirc.ordinal()] = 20;
      } catch (NoSuchFieldError e20) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInOutCirc.ordinal()] = 21;
      } catch (NoSuchFieldError e21) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInBack.ordinal()] = 22;
      } catch (NoSuchFieldError e22) {
      }

      try {
         ORDINAL_MAP[EasingType.easeOutBack.ordinal()] = 23;
      } catch (NoSuchFieldError e23) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInOutBack.ordinal()] = 24;
      } catch (NoSuchFieldError e24) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInElastic.ordinal()] = 25;
      } catch (NoSuchFieldError e25) {
      }

      try {
         ORDINAL_MAP[EasingType.easeOutElastic.ordinal()] = 26;
      } catch (NoSuchFieldError e26) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInOutElastic.ordinal()] = 27;
      } catch (NoSuchFieldError e27) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInBounce.ordinal()] = 28;
      } catch (NoSuchFieldError e28) {
      }

      try {
         ORDINAL_MAP[EasingType.easeOutBounce.ordinal()] = 29;
      } catch (NoSuchFieldError e29) {
      }

      try {
         ORDINAL_MAP[EasingType.easeInOutBounce.ordinal()] = 30;
      } catch (NoSuchFieldError e30) {
      }

      try {
         ORDINAL_MAP[EasingType.Linear.ordinal()] = 31;
      } catch (NoSuchFieldError e31) {
      }

      try {
         ORDINAL_MAP[EasingType.None.ordinal()] = 32;
      } catch (NoSuchFieldError e32) {
      }
   }
}
