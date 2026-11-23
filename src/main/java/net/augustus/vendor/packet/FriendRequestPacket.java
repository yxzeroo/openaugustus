package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class FriendRequestPacket implements VendorPayload, VendorPacket {
   private String friendName;
   private byte[] sessionToken;
   private int ackId;

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeNullTerminatedAscii(this.friendName);
      buf.writeInt(this.ackId);
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.friendName = buf.readNullTerminatedAscii();
      this.ackId = buf.readInt();
   }

   public byte[] getSessionToken() {
      return this.sessionToken;
   }

   public int getAckId() {
      return this.ackId;
   }

   @Override
   public void setSessionToken(byte[] bytes) {
      this.sessionToken = bytes;
   }

   public FriendRequestPacket(byte[] bytes, String str, int n) {
      this.sessionToken = bytes;
      this.friendName = str;
      this.ackId = n;
   }

   public FriendRequestPacket() {
   }

   public String getFriendName() {
      return this.friendName;
   }
}
