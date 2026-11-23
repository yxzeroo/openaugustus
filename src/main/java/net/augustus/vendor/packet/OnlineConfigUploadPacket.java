package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class OnlineConfigUploadPacket implements VendorPacket {
   private boolean editable;
   private byte[] configData;
   private byte[] sessionToken;
   private String configName;

   public OnlineConfigUploadPacket() {
   }

   public OnlineConfigUploadPacket(byte[] bytes, String str, byte[] bytes2, boolean flag) {
      this.sessionToken = bytes;
      this.configName = str;
      this.configData = bytes2;
      this.editable = flag;
   }

   public String getConfigName() {
      return this.configName;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.configName = buf.readUtfString();
      this.configData = buf.readByteArray();
      this.editable = buf.readBoolean();
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeUtfString(this.configName);
      buf.writeByteArray(this.configData);
      buf.writeBoolean(this.editable);
   }

   public byte[] getSessionToken() {
      return this.sessionToken;
   }

   public byte[] getConfigData() {
      return this.configData;
   }
}
