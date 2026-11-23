package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class QuestProgressPacket implements VendorPacket {
   private int progress;
   private long questId;
   private byte[] sessionToken;

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.questId = buf.readLong();
      this.progress = buf.readInt();
   }

   public byte[] getSessionToken() {
      return this.sessionToken;
   }

   public QuestProgressPacket() {
   }

   public QuestProgressPacket(byte[] bytes, long l, int n) {
      this.sessionToken = bytes;
      this.questId = l;
      this.progress = n;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeLong(this.questId);
      buf.writeInt(this.progress);
   }

   public int getProgress() {
      return this.progress;
   }

   public long getQuestId() {
      return this.questId;
   }
}
