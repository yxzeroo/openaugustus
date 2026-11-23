package net.augustus.quests;

public class QuestLevelCalculator {
   public static int getExperienceForLevel(int n) {
      return (int)(500.0 * Math.pow(1.15, (double)(Math.max(1, n / 5) - 1)));
   }
}
