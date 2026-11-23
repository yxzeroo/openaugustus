package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class AuthSuccessPacket implements VendorPacket {
   private byte[] reconnectToken;
   private byte[] challengeToken;
   private long serverTimestamp;

   public long getServerTimestamp() {
      return this.serverTimestamp;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeLong(this.serverTimestamp);
      buf.writeByteArray(this.challengeToken);
      buf.writeByteArray(this.reconnectToken);
   }

   public AuthSuccessPacket(long l, byte[] bytes, byte[] bytes2) {
      this.serverTimestamp = l;
      this.challengeToken = bytes;
      this.reconnectToken = bytes2;
   }

   public byte[] getChallengeToken() {
      return this.challengeToken;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.serverTimestamp = buf.readLong();
      this.challengeToken = buf.readByteArray();
      this.reconnectToken = buf.readByteArray();
   }

   public byte[] getReconnectToken() {
      return this.reconnectToken;
   }

   public AuthSuccessPacket() {
   }
}
