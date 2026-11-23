package net.augustus.events;

public class EventTickPhaseSwitchMap {
   public static final int[] ORDINAL_MAP = new int[TickPhase.values().length];

   static {
      try {
         ORDINAL_MAP[TickPhase.PRE.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[TickPhase.EARLY.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[TickPhase.LATE.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         ORDINAL_MAP[TickPhase.POST.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }

      try {
         ORDINAL_MAP[TickPhase.TICK.ordinal()] = 5;
      } catch (NoSuchFieldError e5) {
      }
   }
}
