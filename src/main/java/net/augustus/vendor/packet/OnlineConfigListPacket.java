package net.augustus.vendor.packet;

import java.util.List;
import net.augustus.vendor.network.VendorPacketBuffer;

public class OnlineConfigListPacket implements VendorPacket {
   private List configs;
   private int categoryId;

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
   }

   public int getCategoryId() {
      return this.categoryId;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.categoryId = buf.readInt();
      this.configs = buf.readOnlineConfigList();
   }

   public List getConfigs() {
      return this.configs;
   }
}
