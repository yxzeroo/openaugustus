package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class FriendRemoveC2SPacket implements VendorPacket, VendorPayload {
   private String friendName;
   private byte[] sessionToken;
   private int ackId;

   public int getAckId() {
      return this.ackId;
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

   public String getFriendName() {
      return this.friendName;
   }

   public FriendRemoveC2SPacket(byte[] bytes, String str, int n) {
      this.sessionToken = bytes;
      this.friendName = str;
      this.ackId = n;
   }

   @Override
   public void setSessionToken(byte[] bytes) {
      this.sessionToken = bytes;
   }

   public FriendRemoveC2SPacket() {
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeNullTerminatedAscii(this.friendName);
      buf.writeInt(this.ackId);
   }
}
