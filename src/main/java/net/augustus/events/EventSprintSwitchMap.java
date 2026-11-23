package net.augustus.events;

public class EventSprintSwitchMap {
   public static final int[] ORDINAL_MAP = new int[SprintEventType.values().length];

   static {
      try {
         ORDINAL_MAP[SprintEventType.SPRINT.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[SprintEventType.JUMP_SPRINT.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[SprintEventType.MOTION_SPRINT.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }
   }
}
