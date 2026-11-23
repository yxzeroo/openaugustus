package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class FriendAcceptPacket implements VendorPayload, VendorPacket {
   private int ackId;
   private String friendName;
   private byte[] sessionToken;

   public FriendAcceptPacket() {
   }

   public String getFriendName() {
      return this.friendName;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.friendName = buf.readNullTerminatedAscii();
      this.ackId = buf.readInt();
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeNullTerminatedAscii(this.friendName);
      buf.writeInt(this.ackId);
   }

   public FriendAcceptPacket(byte[] bytes, String str, int n) {
      this.sessionToken = bytes;
      this.friendName = str;
      this.ackId = n;
   }

   public int getAckId() {
      return this.ackId;
   }

   public byte[] getSessionToken() {
      return this.sessionToken;
   }

   @Override
   public void setSessionToken(byte[] bytes) {
      this.sessionToken = bytes;
   }
}
