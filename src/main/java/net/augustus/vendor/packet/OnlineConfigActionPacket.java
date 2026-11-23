package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class OnlineConfigActionPacket implements VendorPacket {
   private int actionId;
   private String configName;
   private byte[] sessionToken;

   public OnlineConfigActionPacket() {
   }

   public int getActionId() {
      return this.actionId;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeUtfString(this.configName);
      buf.writeInt(this.actionId);
   }

   public byte[] getSessionToken() {
      return this.sessionToken;
   }

   public String getConfigName() {
      return this.configName;
   }

   public OnlineConfigActionPacket(byte[] bytes, String str, int n) {
      this.sessionToken = bytes;
      this.configName = str;
      this.actionId = n;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.configName = buf.readUtfString();
      this.actionId = buf.readInt();
   }
}
