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
      vendorPacketBuffer.writeUtfString(this.relativePath);
      vendorPacketBuffer = buf;
      vendorPacketBuffer.writeByteArray(this.fileContent);
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
