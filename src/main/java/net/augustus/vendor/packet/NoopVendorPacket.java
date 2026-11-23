package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class NoopVendorPacket implements VendorPacket {
   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
   }
}
