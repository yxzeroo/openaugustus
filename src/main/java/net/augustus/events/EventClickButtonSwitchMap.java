package net.augustus.events;

public class EventClickButtonSwitchMap {
   public static final int[] ORDINAL_MAP = new int[ClickButtonType.values().length];

   static {
      try {
         ORDINAL_MAP[ClickButtonType.LEFT.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[ClickButtonType.MID.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }
   }
}
