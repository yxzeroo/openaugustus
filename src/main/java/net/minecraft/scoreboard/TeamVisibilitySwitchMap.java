package net.minecraft.scoreboard;


public class TeamVisibilitySwitchMap {
   public static final TeamVisibilitySwitchMap INSTANCE = new TeamVisibilitySwitchMap();
   public final int[] SWITCH_MAP = new int[Team.EnumVisible.values().length];

   public TeamVisibilitySwitchMap() {
      try {
         this.SWITCH_MAP[Team.EnumVisible.ALWAYS.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         this.SWITCH_MAP[Team.EnumVisible.NEVER.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         this.SWITCH_MAP[Team.EnumVisible.HIDE_FOR_OTHER_TEAMS.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         this.SWITCH_MAP[Team.EnumVisible.HIDE_FOR_OWN_TEAM.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }
   }
}
