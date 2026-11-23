package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class LegacyKeyExchangeInitPacket implements VendorPacket {
   private byte[] encryptedPayload;
   private int keyIndex;

   public LegacyKeyExchangeInitPacket(int n, byte[] bytes) {
      this.keyIndex = n;
      this.encryptedPayload = bytes;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.keyIndex = buf.readInt();
      this.encryptedPayload = buf.readByteArray();
   }

   public LegacyKeyExchangeInitPacket() {
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeInt(this.keyIndex);
      buf.writeByteArray(this.encryptedPayload);
   }
}
