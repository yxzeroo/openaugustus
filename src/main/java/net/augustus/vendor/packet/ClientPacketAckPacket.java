package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class ClientPacketAckPacket implements VendorPacket {
   private int ackId;

   public int getAckId() {
      return this.ackId;
   }

   public ClientPacketAckPacket(int n) {
      this.ackId = n;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.ackId = buf.readInt();
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeInt(this.ackId);
   }

   public ClientPacketAckPacket() {
   }
}
