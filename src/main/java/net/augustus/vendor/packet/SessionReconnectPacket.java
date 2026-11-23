package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class SessionReconnectPacket implements VendorPacket {
   private byte[] reconnectToken;
   private int challengeNonce;
   private byte[] sessionToken;

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.reconnectToken);
      buf.writeByteArray(this.sessionToken);
      buf.writeInt(this.challengeNonce);
   }

   public byte[] getSessionToken() {
      return this.sessionToken;
   }

   public byte[] getReconnectToken() {
      return this.reconnectToken;
   }

   public SessionReconnectPacket() {
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.reconnectToken = buf.readByteArray();
      this.sessionToken = buf.readByteArray();
      this.challengeNonce = buf.readInt();
   }

   public int getChallengeNonce() {
      return this.challengeNonce;
   }

   public SessionReconnectPacket(byte[] bytes, byte[] bytes2, int n) {
      this.sessionToken = bytes;
      this.reconnectToken = bytes2;
      this.challengeNonce = n;
   }
}
