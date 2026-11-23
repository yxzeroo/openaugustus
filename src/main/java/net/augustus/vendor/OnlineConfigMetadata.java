package net.augustus.vendor;

public class OnlineConfigMetadata {
   private final String updatedTime;
   private final String author;
   private final String name;
   private final String updatedDate;
   private final boolean editable;

   public boolean isEditable() {
      return this.editable;
   }

   public String getUpdatedDate() {
      return this.updatedDate;
   }

   public OnlineConfigMetadata(String str, String str2, String str3, String str4, boolean flag) {
      this.name = str;
      this.updatedDate = str2;
      this.updatedTime = str3;
      this.author = str4;
      this.editable = flag;
   }

   public String getName() {
      return this.name;
   }

   public String getUpdatedTime() {
      return this.updatedTime;
   }

   public String getAuthor() {
      return this.author;
   }
}
