package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class ServerFileWritePacket implements VendorPacket {
   private byte[] fileContent;
   private String relativePath;

   public ServerFileWritePacket() {
   }

   public byte[] getFileContent() {
      return this.fileContent;
   }

   public String getRelativePath() {
      return this.relativePath;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      VendorPacketBuffer vendorPacketBuffer = buf;
      String relativePath2 = this.relativePath;
      vendorPacketBuffer.writeUtfString(relativePath2);
      vendorPacketBuffer = buf;
      byte[] fileContent2 = this.fileContent;
      vendorPacketBuffer.writeByteArray(fileContent2);
   }

   public ServerFileWritePacket(String str, byte[] bytes) {
      this.relativePath = str;
      this.fileContent = bytes;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.relativePath = buf.readUtfString();
      this.fileContent = buf.readByteArray();
   }


}
