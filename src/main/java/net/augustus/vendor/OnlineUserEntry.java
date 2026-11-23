package net.augustus.vendor;

public class OnlineUserEntry {
   private final String clientUsername;
   private final String serverAddress;
   private final String ingameName;
   private final int level;
   private final int entityId;
   private final long sessionId;
   private final String rankName;
   private final String rankColor;
   private final String playerUuid;

   public String getIngameName() {
      return this.ingameName;
   }

   public String getPlayerUuid() {
      return this.playerUuid;
   }

   public long getSessionId() {
      return this.sessionId;
   }

   public int getLevel() {
      return this.level;
   }

   public String getRankColor() {
      return this.rankColor;
   }

   public String getServerAddress() {
      return this.serverAddress;
   }

   public OnlineUserEntry(String str, String str2, String str3, int n, String str4, String str5, String str6, long l, int n2) {
      this.ingameName = str;
      this.clientUsername = str2;
      this.entityId = n;
      this.serverAddress = str4;
      this.rankName = str5;
      this.rankColor = str6;
      this.sessionId = l;
      this.playerUuid = str3;
      this.level = n2;
   }

   public String getRankName() {
      return this.rankName;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public String getClientUsername() {
      return this.clientUsername;
   }

   public String getFormattedRank() {
      return this.rankColor + this.rankName;
   }
}
