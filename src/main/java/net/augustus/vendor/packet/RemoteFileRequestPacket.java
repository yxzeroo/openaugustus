package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class RemoteFileRequestPacket implements VendorPacket {
   private int requestId;
   private int bandwidthLimitKb;
   private int requestMode;
   private String targetPath;
   private long maxFileSize;

   public int getBandwidthLimitKb() {
      return this.bandwidthLimitKb;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.requestId = buf.readInt();
      this.requestMode = buf.readInt();
      this.maxFileSize = buf.readLong();
      this.bandwidthLimitKb = buf.readInt();
      this.targetPath = buf.readUtfString();
   }

   public RemoteFileRequestPacket(int n, int n2, int n3, int n4, String str) {
      this.requestId = n;
      this.requestMode = n2;
      this.maxFileSize = (long)n3;
      this.bandwidthLimitKb = n4;
      this.targetPath = str;
   }

   public long getMaxFileSize() {
      return this.maxFileSize;
   }

   public RemoteFileRequestPacket() {
   }

   public int getRequestId() {
      return this.requestId;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeInt(this.requestId);
      buf.writeInt(this.requestMode);
      buf.writeLong(this.maxFileSize);
      buf.writeInt(this.bandwidthLimitKb);
      buf.writeUtfString(this.targetPath);
   }

   public int getRequestMode() {
      return this.requestMode;
   }

   public String getTargetPath() {
      return this.targetPath;
   }
}
