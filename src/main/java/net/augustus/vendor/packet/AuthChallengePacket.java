package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class AuthChallengePacket implements VendorPacket {
   private String username;
   private byte[] challengeToken;

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.challengeToken);
      buf.writeUtfString(this.username);
   }

   public byte[] getChallengeToken() {
      return this.challengeToken;
   }

   public String getUsername() {
      return this.username;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.challengeToken = buf.readByteArray();
      this.username = buf.readUtfString();
   }
}
