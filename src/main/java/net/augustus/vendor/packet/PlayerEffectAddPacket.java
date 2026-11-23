package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class PlayerEffectAddPacket implements VendorPacket {
   private String playerName;
   private String effectData;
   private int ackId;
   private String playerUuid;
   private int effectSlot;
   private String effectName;
   private int entityId;

   public PlayerEffectAddPacket(String str, String str2, int n, String str3, int n2, int n3, String str4) {
      this.effectName = str;
      this.playerName = str2;
      this.effectData = str3;
      this.effectSlot = n2;
      this.ackId = n3;
      this.entityId = n;
      this.playerUuid = str4;
   }

   public String getEffectData() {
      return this.effectData;
   }

   public PlayerEffectAddPacket() {
   }

   public int getEntityId() {
      return this.entityId;
   }

   public String getPlayerUuid() {
      return this.playerUuid;
   }

   public int getAckId() {
      return this.ackId;
   }

   public String getPlayerName() {
      return this.playerName;
   }

   public String getEffectName() {
      return this.effectName;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.effectName = buf.readUtfString();
      this.playerName = buf.readUtfString();
      this.effectData = buf.readUtfString();
      this.effectSlot = buf.readInt();
      this.ackId = buf.readInt();
      this.entityId = buf.readInt();
      this.playerUuid = buf.readUtfString();
   }

   public int getEffectSlot() {
      return this.effectSlot;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeUtfString(this.effectName);
      buf.writeUtfString(this.playerName);
      buf.writeUtfString(this.effectData);
      buf.writeInt(this.effectSlot);
      buf.writeInt(this.ackId);
      buf.writeInt(this.entityId);
      buf.writeUtfString(this.playerUuid);
   }
}
