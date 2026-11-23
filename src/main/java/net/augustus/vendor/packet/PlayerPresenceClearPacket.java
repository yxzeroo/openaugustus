package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class PlayerPresenceClearPacket implements VendorPayload, VendorPacket {
   private byte[] sessionToken;
   private int ackId;

   public PlayerPresenceClearPacket(byte[] bytes, int n) {
      this.sessionToken = bytes;
      this.ackId = n;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeInt(this.ackId);
   }

   public PlayerPresenceClearPacket() {
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
   }

   @Override
   public void setSessionToken(byte[] bytes) {
      this.sessionToken = bytes;
   }
}
