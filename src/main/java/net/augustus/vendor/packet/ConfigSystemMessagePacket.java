package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class ConfigSystemMessagePacket implements VendorPacket {
   private int messageType;
   private String message;

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeUtfString(this.message);
      buf.writeInt(this.messageType);
   }

   public ConfigSystemMessagePacket(String str) {
      this.message = str;
   }

   public int getMessageType() {
      return this.messageType;
   }

   public ConfigSystemMessagePacket() {
   }

   public String getMessage() {
      return this.message;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.message = buf.readUtfString();
      this.messageType = buf.readInt();
   }
}
