package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class ReconnectProofChallengePacket implements VendorPacket {
   private String responseExponent;
   private byte[] reconnectToken;
   private String commitmentValue;
   private String publicBase;

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.reconnectToken = buf.readByteArray();
      this.publicBase = buf.readUtfString();
      this.responseExponent = buf.readUtfString();
      this.commitmentValue = buf.readUtfString();
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.reconnectToken);
      buf.writeUtfString(this.publicBase);
      buf.writeUtfString(this.responseExponent);
      buf.writeUtfString(this.commitmentValue);
   }

   public ReconnectProofChallengePacket() {
   }

   public String getPublicBase() {
      return this.publicBase;
   }

   public String getCommitmentValue() {
      return this.commitmentValue;
   }

   public ReconnectProofChallengePacket(byte[] bytes, String str, String str2, String str3) {
      this.reconnectToken = bytes;
      this.commitmentValue = str;
      this.responseExponent = str2;
      this.publicBase = str3;
   }

   public byte[] getReconnectToken() {
      return this.reconnectToken;
   }

   public String getResponseExponent() {
      return this.responseExponent;
   }
}
