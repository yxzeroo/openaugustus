package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class OnlineConfigHashCheckPacket implements VendorPacket {
   private byte[] configHash;
   private String configName;
   private byte[] sessionToken;

   public byte[] getConfigHash() {
      return this.configHash;
   }

   public byte[] getSessionToken() {
      return this.sessionToken;
   }

   public String getConfigName() {
      return this.configName;
   }

   public OnlineConfigHashCheckPacket() {
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.configHash = buf.readByteArray();
      this.configName = buf.readUtfString();
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeByteArray(this.configHash);
      buf.writeUtfString(this.configName);
   }

   public OnlineConfigHashCheckPacket(byte[] bytes, byte[] bytes2, String str) {
      this.sessionToken = bytes;
      this.configHash = bytes2;
      this.configName = str;
   }
}
