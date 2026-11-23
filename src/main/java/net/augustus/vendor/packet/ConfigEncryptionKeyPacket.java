package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class ConfigEncryptionKeyPacket implements VendorPacket {
   private byte[] encryptionKey;

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
   }

   public byte[] getEncryptionKey() {
      return this.encryptionKey;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.encryptionKey = buf.readByteArray();
   }
}
