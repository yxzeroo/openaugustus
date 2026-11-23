package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class TelemetryLogPacket implements VendorPayload, VendorPacket {
   private String message;
   private int ackId;
   private int channelId;

   public TelemetryLogPacket() {
   }

   public TelemetryLogPacket(String str, int n, int n2) {
      this.message = str;
      this.ackId = n;
      this.channelId = n2;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.ackId = buf.readInt();
      this.channelId = buf.readInt();
      this.message = buf.readUtfString();
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeInt(this.ackId);
      buf.writeInt(this.channelId);
      buf.writeUtfString(this.message);
   }

   @Override
   public void setSessionToken(byte[] bytes) {
   }
}
