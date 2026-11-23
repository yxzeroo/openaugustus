package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class PlayerEffectsClearPacket implements VendorPacket {
   private int ackId;

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.ackId = buf.readInt();
   }

   public PlayerEffectsClearPacket(int n) {
      this.ackId = n;
   }

   public PlayerEffectsClearPacket() {
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeInt(this.ackId);
   }

   public int getAckId() {
      return this.ackId;
   }
}
