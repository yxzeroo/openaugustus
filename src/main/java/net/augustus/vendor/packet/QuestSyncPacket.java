package net.augustus.vendor.packet;

import java.util.List;
import net.augustus.vendor.network.VendorPacketBuffer;

public class QuestSyncPacket implements VendorPacket {
   private long remainingMillis;
   private long startTimestamp;
   private List serverDomains;
   private int objectiveTarget;
   private int rewardXp;
   private int ackId;
   private int currentProgress;
   private boolean completed;
   private long questId;
   private int objectiveType;

   public long getRemainingMillis() {
      return this.remainingMillis;
   }

   public boolean isCompleted() {
      return this.completed;
   }

   public int getAckId() {
      return this.ackId;
   }

   public QuestSyncPacket(int n, long l, List list, int n2, int n3, long l2, long l3, int n4, boolean flag, int n5) {
      this.ackId = n;
      this.questId = l;
      this.serverDomains = list;
      this.objectiveType = n2;
      this.objectiveTarget = n3;
      this.remainingMillis = l2;
      this.startTimestamp = l3;
      this.rewardXp = n4;
      this.completed = flag;
      this.currentProgress = n5;
   }

   public QuestSyncPacket() {
   }

   public int getObjectiveTarget() {
      return this.objectiveTarget;
   }

   public int getRewardXp() {
      return this.rewardXp;
   }

   public List getServerDomains() {
      return this.serverDomains;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.ackId = buf.readInt();
      this.questId = buf.readLong();
      this.serverDomains = buf.readStringList();
      this.objectiveType = buf.readInt();
      this.remainingMillis = buf.readLong();
      this.startTimestamp = buf.readLong();
      this.objectiveTarget = buf.readInt();
      this.rewardXp = buf.readInt();
      this.completed = buf.readBoolean();
      this.currentProgress = buf.readInt();
   }

   public int getCurrentProgress() {
      return this.currentProgress;
   }

   public long getQuestId() {
      return this.questId;
   }

   public long getStartTimestamp() {
      return this.startTimestamp;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeInt(this.ackId);
      buf.writeLong(this.questId);
      buf.writeStringList(this.serverDomains);
      buf.writeInt(this.objectiveType);
      buf.writeLong(this.remainingMillis);
      buf.writeLong(this.startTimestamp);
      buf.writeInt(this.objectiveTarget);
      buf.writeInt(this.rewardXp);
      buf.writeBoolean(this.completed);
      buf.writeInt(this.currentProgress);
   }

   public int getObjectiveType() {
      return this.objectiveType;
   }
}
