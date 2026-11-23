package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class OnlineUserRemovePacket implements VendorPacket {
   private int ackId;
   private long sessionId;

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionId = buf.readLong();
      this.ackId = buf.readInt();
   }

   public OnlineUserRemovePacket(long l, int n) {
      this.sessionId = l;
      this.ackId = n;
   }

   public OnlineUserRemovePacket() {
   }

   public int getAckId() {
      return this.ackId;
   }

   public long getSessionId() {
      return this.sessionId;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeLong(this.sessionId);
      buf.writeInt(this.ackId);
   }
}
