package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class IrcChatSendPacket implements VendorPacket {
   private String recipientName;
   private String message;
   private byte[] sessionToken;

   public IrcChatSendPacket() {
   }

   public String getRecipientName() {
      return this.recipientName;
   }

   public String getMessage() {
      return this.message;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeUtfString(this.message);
      buf.writeNullTerminatedAscii(this.recipientName);
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.message = buf.readUtfString();
      this.recipientName = buf.readNullTerminatedAscii();
   }

   public IrcChatSendPacket(byte[] bytes, String str, String str2) {
      this.sessionToken = bytes;
      this.message = str != null && !str.isEmpty() ? str.substring(0, Math.min(1100, str.length())) : str;
      this.recipientName = str2;
   }
}
