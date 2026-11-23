package net.augustus.quests;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class ServerQuest {
   public static boolean debugMode = false;
   private double progressPercent;
   private final int rewardExperience;
   private final CopyOnWriteArrayList<Runnable> pendingTasks;
   private boolean completed;
   private final QuestObjective objective;
   private final QuestServerFilter serverFilter;
   private final long questId;
   private final long expiresAtMillis;
   private final long createdAtMillis;

   public void runPendingTasks() {
      this.pendingTasks.forEach(Runnable::run);
   }

   public long getQuestId() {
      return this.questId;
   }

   public QuestObjective getObjective() {
      return this.objective;
   }

   public String getRemainingTimeText() {
      return formatDuration(this.expiresAtMillis - System.currentTimeMillis()) + " remaining";
   }

   public boolean isCompleted() {
      return this.completed;
   }

   public boolean isExpired() {
      long l = System.currentTimeMillis();
      return l > this.expiresAtMillis;
   }

   public int getRewardExperience() {
      return this.rewardExperience;
   }

   private long getQuestId2() {
      return this.questId;
   }

   public ServerQuest(QuestServerFilter questServerFilter, QuestObjective questObjective, long l, long l2, int n, long l3, boolean flag, int n2) {
       super();
      this.pendingTasks = new CopyOnWriteArrayList<>();
      this.serverFilter = questServerFilter;
      this.objective = questObjective;
      QuestObjective objective2 = this.objective;
      Consumer consumer = obj -> this.onProgressUpdated((Double)obj);
      objective2.setProgressListener(consumer);

      try {
         double d = (double)n2;
         this.objective.setProgress(d);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }

      this.createdAtMillis = l;
      this.expiresAtMillis = l2 + System.currentTimeMillis();
      this.rewardExperience = n;
      this.questId = l3;
      this.completed = flag;
   }

   public QuestServerFilter getServerFilter() {
      return this.serverFilter;
   }

   public String getDescription() {
      return this.getObjective().getActionVerb() + " " + (int)this.getObjective().getTargetAmount() + " " + this.getObjective().getUnitLabel() + " on " + this.getServerFilter().getServerName();
   }

   private void markCompleted() {
      if (!this.completed) {
         double d;
         try {
            d = this.getObjective().getProgress();
         } catch (Exception e) {
            d = 0.0;
         }

         this.completed = true;
      }
   }

   public double getProgressPercent() {
      return this.progressPercent;
   }

   public boolean isActiveForServer(String str) {
      return this.getServerFilter().matchesHost(str) && !this.isExpired();
   }

   private static String formatDuration(long l) {
      long l2 = l / 1000L;
      long l3 = l2 / 60L;
      long l4 = l3 / 60L;
      long l5 = l4 / 24L;
      l2 %= 60L;
      l3 %= 60L;
      l4 %= 24L;
      return l5 + "d, " + l4 + "h, " + l3 + "m, " + l2 + "s";
   }

   private void onProgressUpdated(Double d) {
      this.progressPercent = d;
      if (d >= 100.0) {
         this.pendingTasks.add(() -> this.markCompleted());
      }
   }

}
