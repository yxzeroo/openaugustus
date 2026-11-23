package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class FriendsClearPacket implements VendorPacket {
   private int ackId;

   public int getAckId() {
      return this.ackId;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.ackId = buf.readInt();
   }

   public FriendsClearPacket(int n) {
      this.ackId = n;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeInt(this.ackId);
   }

   public FriendsClearPacket() {
   }
}
