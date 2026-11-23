package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class ServerHeartbeatPacket implements VendorPacket {
   private long heartbeatTimestamp;

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.heartbeatTimestamp = buf.readLong();
   }

   public ServerHeartbeatPacket(long l) {
      this.heartbeatTimestamp = l;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeLong(this.heartbeatTimestamp);
   }

   public long getHeartbeatTimestamp() {
      return this.heartbeatTimestamp;
   }

   public ServerHeartbeatPacket() {
   }
}
