package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class KeyExchangeFinishPacket implements VendorPacket {
   private byte[] encryptedKeyPayload;

   public KeyExchangeFinishPacket(byte[] bytes) {
      this.encryptedKeyPayload = bytes;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.encryptedKeyPayload);
   }

   public KeyExchangeFinishPacket() {
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.encryptedKeyPayload = buf.readByteArray();
   }
}
