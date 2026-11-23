package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class ServerPacketAckPacket implements VendorPacket, VendorPayload {
   private byte[] sessionToken;
   private int ackId;

   public ServerPacketAckPacket(byte[] bytes, int n) {
      this.sessionToken = bytes;
      this.ackId = n;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.ackId = buf.readInt();
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeInt(this.ackId);
   }

   public ServerPacketAckPacket() {
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
}
