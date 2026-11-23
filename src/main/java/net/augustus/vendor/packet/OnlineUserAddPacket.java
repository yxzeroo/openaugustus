package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class OnlineUserAddPacket implements VendorPacket {
   private String serverAddress;
   private long sessionId;
   private String clientUsername;
   private int entityId;
   private int level;
   private String rankName;
   private String playerUuid;
   private String rankColor;
   private int ackId;
   private String ingameName;

   public int getEntityId() {
      return this.entityId;
   }

   public int getAckId() {
      return this.ackId;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.ingameName = buf.readUtfString();
      this.clientUsername = buf.readUtfString();
      this.serverAddress = buf.readUtfString();
      this.sessionId = buf.readLong();
      this.rankName = buf.readUtfString();
      this.rankColor = buf.readUtfString();
      this.ackId = buf.readInt();
      this.entityId = buf.readInt();
      this.playerUuid = buf.readUtfString();
      this.level = buf.readInt();
   }

   public String getServerAddress() {
      return this.serverAddress;
   }

   public String getPlayerUuid() {
      return this.playerUuid;
   }

   public String getClientUsername() {
      return this.clientUsername;
   }

   public String getRankName() {
      return this.rankName;
   }

   public int getLevel() {
      return this.level;
   }

   public String getIngameName() {
      return this.ingameName;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeUtfString(this.ingameName);
      buf.writeUtfString(this.clientUsername);
      buf.writeUtfString(this.serverAddress);
      buf.writeLong(this.sessionId);
      buf.writeUtfString(this.rankName);
      buf.writeUtfString(this.rankColor);
      buf.writeInt(this.ackId);
      buf.writeInt(this.entityId);
      buf.writeUtfString(this.playerUuid);
      buf.writeInt(this.level);
   }

   public long getSessionId() {
      return this.sessionId;
   }

   public String getRankColor() {
      return this.rankColor;
   }
}
