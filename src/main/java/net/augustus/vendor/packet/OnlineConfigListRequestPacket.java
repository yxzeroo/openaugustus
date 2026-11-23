package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class OnlineConfigListRequestPacket implements VendorPacket {
   private byte[] sessionToken;

   public OnlineConfigListRequestPacket() {
   }

   public byte[] getSessionToken() {
      return this.sessionToken;
   }

   public OnlineConfigListRequestPacket(byte[] bytes) {
      this.sessionToken = bytes;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
   }
}
