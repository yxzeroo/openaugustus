package net.augustus.vendor;

public class ElectricFriend {
   private int status;
   private final String username;
   private int userId;

   public int getStatus() {
      return this.status;
   }

   public int getUserId() {
      return this.userId;
   }

   public String getUsername() {
      return this.username;
   }

   public void setStatus(int n) {
      this.status = n;
   }

   public ElectricFriend(String str, int n, int n2) {
      this.username = str;
      this.status = n;
      this.userId = n2;
   }
}
