package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class SecurityAlertPacket implements VendorPacket {
   private byte[] sessionToken;
   private byte[] alertSignature;
   private String clientName;
   private long timestamp;

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.clientName = buf.readNullTerminatedAscii();
      this.alertSignature = buf.readByteArray();
      this.sessionToken = buf.readByteArray();
      this.timestamp = buf.readLong();
   }

   public String getClientName() {
      return this.clientName;
   }

   public SecurityAlertPacket() {
   }

   public long getTimestamp() {
      return this.timestamp;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeNullTerminatedAscii(this.clientName);
      buf.writeByteArray(this.alertSignature);
      buf.writeByteArray(this.sessionToken);
      buf.writeLong(this.timestamp);
   }

   public SecurityAlertPacket(String str, byte[] bytes, byte[] bytes2, long l) {
      this.clientName = str;
      this.alertSignature = bytes;
      this.sessionToken = bytes2;
      this.timestamp = l;
   }

   public byte[] getAlertSignature() {
      return this.alertSignature;
   }

   public byte[] getSessionToken() {
      return this.sessionToken;
   }
}
