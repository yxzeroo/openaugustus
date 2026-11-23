package net.augustus.quests;

import java.util.HashSet;
import java.util.List;

public class QuestServerFilter {
   private final HashSet allowedHosts = new HashSet();
   private final String serverName;

   public QuestServerFilter(List list, String str) {

      for (String str2 : (Iterable<String>)(list)) {
         this.allowedHosts.add(str2.toLowerCase());
      }

      this.serverName = str;
   }

   public String getServerName() {
      return this.serverName;
   }

   public boolean matchesHost(String str) {
      return this.allowedHosts.contains(str.toLowerCase());
   }




}
