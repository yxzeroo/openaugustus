package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class KeyExchangeInitPacket implements VendorPacket {
   private byte[] encryptedClientSeed;
   private int encodedKeyIndex;

   public KeyExchangeInitPacket(int n, byte[] bytes) {
      this.encodedKeyIndex = n;
      this.encryptedClientSeed = bytes;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeInt(this.encodedKeyIndex);
      buf.writeByteArray(this.encryptedClientSeed);
   }

   public KeyExchangeInitPacket() {
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.encodedKeyIndex = buf.readInt();
      this.encryptedClientSeed = buf.readByteArray();
   }

   public byte[] getEncryptedClientSeed() {
      return this.encryptedClientSeed;
   }

   public int getEncodedKeyIndex() {
      return this.encodedKeyIndex;
   }
}
