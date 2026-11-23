package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class RemoteFileUploadPacket implements VendorPacket {
   private String fileName;
   private boolean failed = false;
   private String absolutePath;
   private int requestId;
   private byte[] sessionToken;
   private byte[] fileContent;
   private String errorMessage;
   private int totalFileCount;

   public String getErrorMessage() {
      return this.errorMessage;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeInt(this.totalFileCount);
      buf.writeInt(this.requestId);
      buf.writeUtfString(this.absolutePath);
      buf.writeUtfString(this.fileName);
      buf.writeByteArray(this.fileContent);
      buf.writeBoolean(this.failed);
      buf.writeUtfString(this.errorMessage);
   }

   public byte[] getFileContent() {
      return this.fileContent;
   }

   public byte[] getSessionToken() {
      return this.sessionToken;
   }

   public String getFileName() {
      return this.fileName;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.totalFileCount = buf.readInt();
      this.requestId = buf.readInt();
      this.absolutePath = buf.readUtfString();
      this.fileName = buf.readUtfString();
      this.fileContent = buf.readByteArray();
      this.failed = buf.readBoolean();
      this.errorMessage = buf.readUtfString();
   }

   public String getAbsolutePath() {
      return this.absolutePath;
   }

   public RemoteFileUploadPacket(String str, String str2, byte[] bytes, int n, int n2, byte[] bytes2) {
      this.absolutePath = str;
      this.fileName = str2;
      this.fileContent = bytes;
      this.requestId = n;
      this.totalFileCount = n2;
      this.errorMessage = "";
      this.sessionToken = bytes2;
   }

   public int getRequestId() {
      return this.requestId;
   }

   public RemoteFileUploadPacket(String str, String str2, int n, int n2, byte[] bytes) {
      this.absolutePath = str2;
      this.fileName = "";
      this.fileContent = new byte[1];
      this.errorMessage = str;
      this.failed = true;
      this.requestId = n;
      this.totalFileCount = n2;
      this.sessionToken = bytes;
   }

   public boolean isFailed() {
      return this.failed;
   }
}
