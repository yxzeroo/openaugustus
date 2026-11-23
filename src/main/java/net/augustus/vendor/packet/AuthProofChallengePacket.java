package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class AuthProofChallengePacket implements VendorPacket {
   private String publicBase;
   private String commitmentValue;
   private String responseExponent;

   public AuthProofChallengePacket(String str, String str2, String str3) {
      this.commitmentValue = str;
      this.responseExponent = str2;
      this.publicBase = str3;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeNullTerminatedAscii(this.commitmentValue);
      buf.writeNullTerminatedAscii(this.responseExponent);
      buf.writeNullTerminatedAscii(this.publicBase);
   }

   public String getPublicBase() {
      return this.publicBase;
   }

   public AuthProofChallengePacket() {
   }

   public String getCommitmentValue() {
      return this.commitmentValue;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.commitmentValue = buf.readNullTerminatedAscii();
      this.responseExponent = buf.readNullTerminatedAscii();
      this.publicBase = buf.readNullTerminatedAscii();
   }

   public String getResponseExponent() {
      return this.responseExponent;
   }
}
