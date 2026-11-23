package net.augustus.modules.latency.helper;

import net.minecraft.network.Packet;

public class DelayedPacketEntry {
   private final Packet packet;
   private final long queuedTimestampMs;
   private static int[] controlFlowState;

   public DelayedPacketEntry(Packet packet2) {
      this.packet = packet2;
      this.queuedTimestampMs = System.currentTimeMillis();
   }

   public Packet getPacket() {
      return this.packet;
   }

   public long getQueuedTimestampMs() {
      return this.queuedTimestampMs;
   }

   public boolean isExpired(long l) {
      int[] controlFlowState2 = getControlFlowState();
      return System.currentTimeMillis() > this.queuedTimestampMs + l;
   }

   public static void setControlFlowState(int[] ints) {
      controlFlowState = ints;
   }

   public static int[] getControlFlowState() {
      return controlFlowState;
   }

   static {
      if (getControlFlowState() == null) {
         setControlFlowState(new int[1]);
      }
   }
}
