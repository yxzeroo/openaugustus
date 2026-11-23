package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class PlayerPresenceUpdatePacket implements VendorPayload, VendorPacket {
   private boolean crackedSession;
   private byte[] sessionToken;
   private int ackId;
   private String username;
   private int entityId;
   private String serverAddress;
   private String playerUuid;

   @Override
   public void setSessionToken(byte[] bytes) {
      this.sessionToken = bytes;
   }

   public PlayerPresenceUpdatePacket() {
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeUtfString(this.serverAddress);
      buf.writeUtfString(this.username);
      buf.writeBoolean(this.crackedSession);
      buf.writeInt(this.ackId);
      buf.writeInt(this.entityId);
      buf.writeUtfString(this.playerUuid);
   }

   public PlayerPresenceUpdatePacket(byte[] bytes, String str, String str2, String str3, int n, boolean flag, int n2) {
      this.sessionToken = bytes;
      this.serverAddress = str;
      this.username = str2;
      this.crackedSession = flag;
      this.ackId = n2;
      this.entityId = n;
      this.playerUuid = str3;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
   }
}
