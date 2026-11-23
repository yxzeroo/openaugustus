package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class FriendActionStatusPacket implements VendorPacket {
   private String statusMessage;
   private int statusCode;

   public FriendActionStatusPacket(String str, int n) {
      this.statusMessage = str;
      this.statusCode = n;
   }

   public int getStatusCode() {
      return this.statusCode;
   }

   public String getStatusMessage() {
      return this.statusMessage;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.statusMessage = buf.readNullTerminatedAscii();
      this.statusCode = buf.readInt();
   }

   public FriendActionStatusPacket() {
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeNullTerminatedAscii(this.statusMessage);
      buf.writeInt(this.statusCode);
   }
}
