package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class KeyExchangeServerKeyPacket implements VendorPacket {
   private int clientKeyIndex;
   private byte[] encryptedServerKey;
   private byte[] encryptedAesKey;

   public KeyExchangeServerKeyPacket() {
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.clientKeyIndex = buf.readInt();
      this.encryptedServerKey = buf.readByteArray();
      this.encryptedAesKey = buf.readByteArray();
   }

   public KeyExchangeServerKeyPacket(int n, byte[] bytes, byte[] bytes2) {
      this.clientKeyIndex = n;
      this.encryptedServerKey = bytes;
      this.encryptedAesKey = bytes2;
   }

   public byte[] getEncryptedServerKey() {
      return this.encryptedServerKey;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeInt(this.clientKeyIndex);
      buf.writeByteArray(this.encryptedServerKey);
      buf.writeByteArray(this.encryptedAesKey);
   }

   public int getClientKeyIndex() {
      return this.clientKeyIndex;
   }

   public byte[] getEncryptedAesKey() {
      return this.encryptedAesKey;
   }
}
