package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class QuestsClearPacket implements VendorPacket {
   private int ackId;

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeInt(this.ackId);
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.ackId = buf.readInt();
   }

   public int getAckId() {
      return this.ackId;
   }
}
