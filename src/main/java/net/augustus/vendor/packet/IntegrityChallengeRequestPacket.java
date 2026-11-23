package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class IntegrityChallengeRequestPacket implements VendorPacket {
   private byte[] sessionToken;
   private long challengeSeed;

   public IntegrityChallengeRequestPacket(byte[] bytes, long l) {
      this.sessionToken = bytes;
      this.challengeSeed = l;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeLong(this.challengeSeed);
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.challengeSeed = buf.readLong();
   }

   public IntegrityChallengeRequestPacket() {
   }
}
