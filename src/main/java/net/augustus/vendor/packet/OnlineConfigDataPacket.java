package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class OnlineConfigDataPacket implements VendorPacket {
   private byte[] aesKey;
   private byte[] encryptedData;
   private int categoryId;
   private String configName;

   public String getConfigName() {
      return this.configName;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.configName = buf.readUtfString();
      this.categoryId = buf.readInt();
      this.encryptedData = buf.readByteArray();
      this.aesKey = buf.readByteArray();
   }

   public int getCategoryId() {
      return this.categoryId;
   }

   public byte[] getAesKey() {
      return this.aesKey;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeUtfString(this.configName);
      buf.writeInt(this.categoryId);
      buf.writeByteArray(this.encryptedData);
      buf.writeByteArray(this.aesKey);
   }

   public byte[] getEncryptedData() {
      return this.encryptedData;
   }
}
