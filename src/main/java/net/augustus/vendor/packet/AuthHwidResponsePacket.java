package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class AuthHwidResponsePacket implements VendorPacket {
   private byte[] challengeToken;
   private byte[] discordUserIdBytes;
   private byte[] sessionToken;
   private byte[] hwidHash;

   public byte[] getSessionToken() {
      return this.sessionToken;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.discordUserIdBytes);
      buf.writeByteArray(this.challengeToken);
      buf.writeByteArray(this.sessionToken);
      buf.writeByteArray(this.hwidHash);
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.challengeToken = buf.readByteArray();
      this.hwidHash = buf.readByteArray();
   }

   public byte[] getHwidHash() {
      return this.hwidHash;
   }

   public AuthHwidResponsePacket(byte[] bytes, byte[] bytes2, byte[] bytes3, byte[] bytes4) {
      this.sessionToken = bytes;
      this.challengeToken = bytes2;
      this.hwidHash = bytes3;
      this.discordUserIdBytes = bytes4;
   }

   public AuthHwidResponsePacket() {
   }

   public byte[] getChallengeToken() {
      return this.challengeToken;
   }
}
