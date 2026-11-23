package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class QuestCompletePacket implements VendorPacket {
   private byte[] sessionToken;
   private double completionValue;
   private long questId;

   public double getCompletionValue() {
      return this.completionValue;
   }

   public QuestCompletePacket(byte[] bytes, long l, double d) {
      this.sessionToken = bytes;
      this.questId = l;
      this.completionValue = d;
   }

   public QuestCompletePacket() {
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.sessionToken = buf.readByteArray();
      this.questId = buf.readLong();
      this.completionValue = buf.readDouble();
   }

   public long getQuestId() {
      return this.questId;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeByteArray(this.sessionToken);
      buf.writeLong(this.questId);
      buf.writeDouble(this.completionValue);
   }
}
