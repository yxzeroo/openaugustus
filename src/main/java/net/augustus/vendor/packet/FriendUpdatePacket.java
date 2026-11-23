package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class FriendUpdatePacket implements VendorPacket {
   private int status;
   private int ackId;
   private String friendName;
   private int userId;

   public int getUserId() {
      return this.userId;
   }

   public String getFriendName() {
      return this.friendName;
   }

   public int getAckId() {
      return this.ackId;
   }

   public int getStatus() {
      return this.status;
   }

   public FriendUpdatePacket() {
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeNullTerminatedAscii(this.friendName);
      buf.writeInt(this.status);
      buf.writeInt(this.ackId);
      buf.writeInt(this.userId);
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.friendName = buf.readNullTerminatedAscii();
      this.status = buf.readInt();
      this.ackId = buf.readInt();
      this.userId = buf.readInt();
   }

   public FriendUpdatePacket(String str, int n, int n2, int n3) {
      this.friendName = str;
      this.status = n;
      this.ackId = n3;
      this.userId = n2;
   }
}
