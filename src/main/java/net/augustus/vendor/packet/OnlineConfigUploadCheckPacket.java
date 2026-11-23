package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class OnlineConfigUploadCheckPacket implements VendorPacket {
   private int statusCode;
   private String configName;
   private String errorMessage;

   public String getErrorMessage() {
      return this.errorMessage;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.statusCode = buf.readInt();
      this.errorMessage = buf.readUtfString();
      this.configName = buf.readUtfString();
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeInt(this.statusCode);
      buf.writeUtfString(this.errorMessage);
      buf.writeUtfString(this.configName);
   }

   public OnlineConfigUploadCheckPacket() {
   }

   public String getConfigName() {
      return this.configName;
   }

   public int getStatusCode() {
      return this.statusCode;
   }

   public OnlineConfigUploadCheckPacket(int n, String str, String str2) {
      this.statusCode = n;
      this.errorMessage = str;
      this.configName = str2;
   }




}
