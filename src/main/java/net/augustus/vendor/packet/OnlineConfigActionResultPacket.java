package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class OnlineConfigActionResultPacket implements VendorPacket {
   private String configName;
   private int resultCode;

   public String getConfigName() {
      return this.configName;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.configName = buf.readUtfString();
      this.resultCode = buf.readInt();
   }

   public int getResultCode() {
      return this.resultCode;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeUtfString(this.configName);
      buf.writeInt(this.resultCode);
   }
}
