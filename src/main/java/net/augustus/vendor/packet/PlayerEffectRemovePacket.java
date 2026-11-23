package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class PlayerEffectRemovePacket implements VendorPacket {
   private int ackId;
   private String playerName;
   private int effectSlot;

   public PlayerEffectRemovePacket(String str, int n, int n2) {
      this.playerName = str;
      this.effectSlot = n;
      this.ackId = n2;
   }

   public int getAckId() {
      return this.ackId;
   }

   public String getPlayerName() {
      return this.playerName;
   }

   public int getEffectSlot() {
      return this.effectSlot;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeNullTerminatedAscii(this.playerName);
      buf.writeInt(this.effectSlot);
      buf.writeInt(this.ackId);
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.playerName = buf.readNullTerminatedAscii();
      this.effectSlot = buf.readInt();
      this.ackId = buf.readInt();
   }

   public PlayerEffectRemovePacket() {
   }
}
