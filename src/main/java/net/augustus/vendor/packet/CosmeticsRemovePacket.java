package net.augustus.vendor.packet;

import net.augustus.settings.Setting;
import net.augustus.vendor.network.VendorPacketBuffer;

public class CosmeticsRemovePacket implements VendorPacket {
   private int ackId;
   private String playerName;
   private static Setting[] opaqueStateTag;

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.ackId = buf.readInt();
      this.playerName = buf.readUtfString();
   }

   public CosmeticsRemovePacket(String str) {
      this.playerName = str;
   }

   public String getPlayerName() {
      return this.playerName;
   }

   public int getAckId() {
      return this.ackId;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeInt(this.ackId);
      buf.writeUtfString(this.playerName);
   }

   public CosmeticsRemovePacket() {
   }

   public static void setOpaqueStateTag(Setting[] settings) {
      opaqueStateTag = settings;
   }

   public static Setting[] getOpaqueStateTag() {
      return opaqueStateTag;
   }

   static {
      setOpaqueStateTag(new Setting[1]);
   }
}
