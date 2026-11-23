package net.augustus.quests;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import net.augustus.utils.interfaces.MC;

public class QuestManager implements MC {
   private final HashMap quests = new HashMap();

   public Collection getQuests() {
      return this.quests.values();
   }

   public void clear() {
      this.quests.clear();
   }

   public void addOrUpdateQuest(long l, ServerQuest serverQuest) {
      ServerQuest serverQuest2 = (ServerQuest)this.quests.get(l);
      if (serverQuest2 == null || !(serverQuest2.getProgressPercent() > serverQuest.getProgressPercent())) {
         this.quests.put(l, serverQuest);
      }
   }

   private String normalizeServerIp(String str) {
      str = str.toLowerCase();
      return str.trim();
   }

   public QuestManager() {
      ServerQuest.debugMode = false;
   }

   public void updateQuestsForServer(String str) {
      str = this.normalizeServerIp(str);
      Iterator it = this.quests.values().iterator();

      while (it.hasNext()) {
         ServerQuest serverQuest = (ServerQuest)it.next();
         if (serverQuest.isExpired()) {
            it.remove();
         } else if (serverQuest.isActiveForServer(str)) {
            serverQuest.getObjective().setActive(true, serverQuest.getQuestId());
         } else {
            serverQuest.getObjective().setActive(false, serverQuest.getQuestId());
         }

         serverQuest.runPendingTasks();
      }
   }

   public void deactivateAllQuests() {

      for (ServerQuest serverQuest : (Iterable<ServerQuest>)(this.quests.values())) {
         serverQuest.getObjective().setActive(false, serverQuest.getQuestId());
      }
   }

   public void activateQuestsForServer(String str) {
      str = this.normalizeServerIp(str);

      for (ServerQuest serverQuest : (Iterable<ServerQuest>)(this.quests.values())) {
         if (serverQuest.isActiveForServer(str)) {
            serverQuest.getObjective().setActive(true, serverQuest.getQuestId());
         }
      }

   }



}
