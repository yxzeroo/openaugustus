package net.augustus.cosmetics.render;


import net.augustus.cosmetics.model.CosmeticBodyPart;
import net.augustus.cosmetics.model.CosmeticEffectType;
public class PlayerCosmeticRendererSwitchMap {
   public static final int[] BODY_PART_SWITCH_MAP = new int[CosmeticBodyPart.values().length];
   public static final int[] EFFECT_TYPE_SWITCH_MAP;

   static {
      try {
         BODY_PART_SWITCH_MAP[CosmeticBodyPart.FREE.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         BODY_PART_SWITCH_MAP[CosmeticBodyPart.BODY.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         BODY_PART_SWITCH_MAP[CosmeticBodyPart.HEAD.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         BODY_PART_SWITCH_MAP[CosmeticBodyPart.ABOVE_HEAD.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }

      try {
         BODY_PART_SWITCH_MAP[CosmeticBodyPart.RIGHT_ARM.ordinal()] = 5;
      } catch (NoSuchFieldError e5) {
      }

      try {
         BODY_PART_SWITCH_MAP[CosmeticBodyPart.LEFT_ARM.ordinal()] = 6;
      } catch (NoSuchFieldError e6) {
      }

      try {
         BODY_PART_SWITCH_MAP[CosmeticBodyPart.RIGHT_LEG.ordinal()] = 7;
      } catch (NoSuchFieldError e7) {
      }

      try {
         BODY_PART_SWITCH_MAP[CosmeticBodyPart.LEFT_LEG.ordinal()] = 8;
      } catch (NoSuchFieldError e8) {
      }

      EFFECT_TYPE_SWITCH_MAP = new int[CosmeticEffectType.values().length];

      try {
         EFFECT_TYPE_SWITCH_MAP[CosmeticEffectType.BOUNCE.ordinal()] = 1;
      } catch (NoSuchFieldError e9) {
      }

      try {
         EFFECT_TYPE_SWITCH_MAP[CosmeticEffectType.ROTATE.ordinal()] = 2;
      } catch (NoSuchFieldError e10) {
      }

      try {
         EFFECT_TYPE_SWITCH_MAP[CosmeticEffectType.FORWARD.ordinal()] = 3;
      } catch (NoSuchFieldError e11) {
      }

      try {
         EFFECT_TYPE_SWITCH_MAP[CosmeticEffectType.BACKWARD.ordinal()] = 4;
      } catch (NoSuchFieldError e12) {
      }

      try {
         EFFECT_TYPE_SWITCH_MAP[CosmeticEffectType.RESIZE.ordinal()] = 5;
      } catch (NoSuchFieldError e13) {
      }

      try {
         EFFECT_TYPE_SWITCH_MAP[CosmeticEffectType.MOTION.ordinal()] = 6;
      } catch (NoSuchFieldError e14) {
      }
   }
}
