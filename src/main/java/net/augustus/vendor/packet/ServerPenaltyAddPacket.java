package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class ServerPenaltyAddPacket implements VendorPacket {
   private int penaltyLevel;
   private int ackId;

   public int getAckId() {
      return this.ackId;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeInt(this.ackId);
      buf.writeInt(this.penaltyLevel);
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.ackId = buf.readInt();
      this.penaltyLevel = buf.readInt();
   }

   public ServerPenaltyAddPacket() {
   }

   public int getPenaltyLevel() {
      return this.penaltyLevel;
   }

   public ServerPenaltyAddPacket(int n) {
      this.penaltyLevel = n;
   }
}
