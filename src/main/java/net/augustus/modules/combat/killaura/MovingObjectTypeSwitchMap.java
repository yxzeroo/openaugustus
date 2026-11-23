package net.augustus.modules.combat.killaura;

import net.minecraft.util.MovingObjectPosition;

public class MovingObjectTypeSwitchMap {
   public static final int[] ORDINAL_MAP = new int[MovingObjectPosition.MovingObjectType.values().length];

   static {
      try {
         ORDINAL_MAP[MovingObjectPosition.MovingObjectType.ENTITY.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[MovingObjectPosition.MovingObjectType.BLOCK.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[MovingObjectPosition.MovingObjectType.MISS.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }
   }
}
