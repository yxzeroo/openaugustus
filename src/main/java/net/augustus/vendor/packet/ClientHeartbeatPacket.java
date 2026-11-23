package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class ClientHeartbeatPacket implements VendorPacket {
   private byte[] sessionToken;
   private long heartbeatTimestamp;

   public byte[] getSessionToken() {
      return this.sessionToken;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeLong(this.heartbeatTimestamp);
   }

   public long getHeartbeatTimestamp() {
      return this.heartbeatTimestamp;
   }

   public ClientHeartbeatPacket(byte[] bytes, long l) {
      this.sessionToken = bytes;
      this.heartbeatTimestamp = l;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.heartbeatTimestamp = buf.readLong();
   }

   public ClientHeartbeatPacket() {
   }
}
