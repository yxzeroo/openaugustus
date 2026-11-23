package net.augustus.vendor;

public class ClientUserProfile {
   private int level;
   private int experience;
   private final String username;
   private String rankName;
   private String rankColor;

   public String getUsername() {
      return this.username;
   }

   public String getRankColor() {
      return this.rankColor;
   }

   public void setRankColor(String str) {
      this.rankColor = str;
   }

   public String getRankName() {
      return this.rankName;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int n) {
      this.level = n;
   }

   public void setExperience(int n) {
      this.experience = n;
   }

   public int getExperience() {
      return this.experience;
   }

   public ClientUserProfile(String str, String str2, String str3, int n, int n2) {
      this.username = str;
      this.rankName = str2;
      this.rankColor = str3;
      this.level = n;
      this.experience = n2;
   }

   public ClientUserProfile() {
      this.username = "";
      this.rankName = "";
      this.rankColor = "";
      this.level = -1;
      this.experience = -1;
   }

   public void setRankName(String str) {
      this.rankName = str;
   }

   public ClientUserProfile(String str) {
      this.username = str;
      this.rankName = "";
      this.rankColor = "";
      this.level = -1;
      this.experience = -1;
   }



}
