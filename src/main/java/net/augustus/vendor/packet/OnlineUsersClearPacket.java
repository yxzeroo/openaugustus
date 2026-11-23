package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class OnlineUsersClearPacket implements VendorPacket {
   private int ackId;

   public OnlineUsersClearPacket(int n) {
      this.ackId = n;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeInt(this.ackId);
   }

   public int getAckId() {
      return this.ackId;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.ackId = buf.readInt();
   }

   public OnlineUsersClearPacket() {
   }
}
