package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class SessionTokenExchangePacket implements VendorPacket {
   private byte[] primaryToken;
   private byte[] signatureBytes;
   private byte[] secondaryToken;

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.primaryToken = buf.readByteArray();
      this.secondaryToken = buf.readByteArray();
      this.signatureBytes = buf.readByteArray();
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.primaryToken);
      buf.writeByteArray(this.secondaryToken);
      buf.writeByteArray(this.signatureBytes);
   }

   public SessionTokenExchangePacket(byte[] bytes, byte[] bytes2, byte[] bytes3) {
      this.primaryToken = bytes;
      this.secondaryToken = bytes2;
      this.signatureBytes = bytes3;
   }

   public SessionTokenExchangePacket() {
   }
}
