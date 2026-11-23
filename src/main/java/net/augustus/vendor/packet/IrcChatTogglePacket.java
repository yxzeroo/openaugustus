package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class IrcChatTogglePacket implements VendorPacket {
   private boolean enabled;
   private byte[] sessionToken;

   public IrcChatTogglePacket(byte[] bytes, boolean flag) {
      this.sessionToken = bytes;
      this.enabled = flag;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeBoolean(this.enabled);
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.enabled = buf.readBoolean();
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public IrcChatTogglePacket() {
   }
}
