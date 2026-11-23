package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class ClientTelemetryReportPacket implements VendorPacket {
   private String reportDetails;
   private String reportCategory;
   private String reportSubject;
   private long timestamp;

   public ClientTelemetryReportPacket(String str, String str2, String str3, long l) {
      this.reportCategory = str;
      this.reportSubject = str2;
      this.reportDetails = str3;
      this.timestamp = l;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.reportCategory = buf.readUtfString();
      this.reportSubject = buf.readUtfString();
      this.reportDetails = buf.readUtfString();
      this.timestamp = buf.readLong();
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeUtfString(this.reportCategory);
      buf.writeUtfString(this.reportSubject);
      buf.writeUtfString(this.reportDetails);
      buf.writeLong(this.timestamp);
   }

   public ClientTelemetryReportPacket() {
   }
}
