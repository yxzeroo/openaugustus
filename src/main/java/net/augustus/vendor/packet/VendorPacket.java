package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public interface VendorPacket {
   void writePacketData(VendorPacketBuffer buf) throws Exception;

   void readPacketData(VendorPacketBuffer buf) throws Exception;
}
