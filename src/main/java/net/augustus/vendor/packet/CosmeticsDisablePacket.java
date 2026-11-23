package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class CosmeticsDisablePacket implements VendorPacket {
   private byte[] sessionToken;

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
   }

   public CosmeticsDisablePacket() {
   }

   public CosmeticsDisablePacket(byte[] bytes) {
      this.sessionToken = bytes;
   }
}
