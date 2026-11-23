package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class OnlineConfigDownloadRequestPacket implements VendorPacket {
   private byte[] cachedHash;
   private byte[] sessionToken;
   private String author;
   private String configName;
   private int categoryId;

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeByteArray(this.cachedHash);
      buf.writeUtfString(this.configName);
      buf.writeUtfString(this.author);
      buf.writeInt(this.categoryId);
   }

   public int getCategoryId() {
      return this.categoryId;
   }

   public byte[] getSessionToken() {
      return this.sessionToken;
   }

   public OnlineConfigDownloadRequestPacket(byte[] bytes, byte[] bytes2, String str, String str2, int n) {
      this.sessionToken = bytes;
      this.cachedHash = bytes2;
      this.configName = str;
      this.author = str2;
      this.categoryId = n;
   }

   public String getConfigName() {
      return this.configName;
   }

   public OnlineConfigDownloadRequestPacket() {
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.cachedHash = buf.readByteArray();
      this.configName = buf.readUtfString();
      this.author = buf.readUtfString();
      this.categoryId = buf.readInt();
   }
}
