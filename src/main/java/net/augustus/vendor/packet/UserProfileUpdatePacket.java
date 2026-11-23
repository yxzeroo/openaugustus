package net.augustus.vendor.packet;

import net.augustus.vendor.network.VendorPacketBuffer;

public class UserProfileUpdatePacket implements VendorPacket {
   private int level;
   private String username;
   private String rankColor;
   private String rankName;
   private int experience;

   public UserProfileUpdatePacket(String str, String str2, String str3, int n, int n2) {
      this.username = str;
      this.rankName = str2;
      this.rankColor = str3;
      this.experience = n;
      this.level = n2;
   }

   @Override
   public void writePacketData(VendorPacketBuffer buf) throws Exception {
      buf.writeUtfString(this.username);
      buf.writeUtfString(this.rankName);
      buf.writeUtfString(this.rankColor);
      buf.writeInt(this.experience);
      buf.writeInt(this.level);
   }

   public int getLevel() {
      return this.level;
   }

   public String getRankColor() {
      return this.rankColor;
   }

   public UserProfileUpdatePacket() {
   }

   public String getRankName() {
      return this.rankName;
   }

   public String getUsername() {
      return this.username;
   }

   public int getExperience() {
      return this.experience;
   }

   @Override
   public void readPacketData(VendorPacketBuffer buf) throws Exception {
      this.username = buf.readUtfString();
      this.rankName = buf.readUtfString();
      this.rankColor = buf.readUtfString();
      this.experience = buf.readInt();
      this.level = buf.readInt();
   }
}
