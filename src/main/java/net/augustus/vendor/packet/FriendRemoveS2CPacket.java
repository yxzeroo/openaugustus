package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class FriendRemoveS2CPacket implements VendorPacket {
   private String friendName;
   private int ackId;

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.friendName = buf.readNullTerminatedAscii();
      this.ackId = buf.readInt();
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeNullTerminatedAscii(this.friendName);
      buf.writeInt(this.ackId);
   }

   public FriendRemoveS2CPacket() {
   }

   public int getAckId() {
      return this.ackId;
   }

   public String getFriendName() {
      return this.friendName;
   }

   public FriendRemoveS2CPacket(String str, int n) {
      this.friendName = str;
      this.ackId = n;
   }
}
