package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class IntegrityChallengeResponsePacket implements VendorPacket {
   private long challengeResponse;

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeLong(this.challengeResponse);
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.challengeResponse = buf.readLong();
   }

   public long getChallengeResponse() {
      return this.challengeResponse;
   }
}
