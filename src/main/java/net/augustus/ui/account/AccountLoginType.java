package net.augustus.ui.account;

public enum AccountLoginType {
   MICROSOFT_CREDENTIALS("Credentials"),
   MICROSOFT_EXTERNAL_BROWSER("External browser"),
   MICROSOFT_DEVICE_CODE("Device code"),
   MICROSOFT_ACCESS_TOKEN("Access Token"),
   THE_ALTENING("TheAltening"),
   CRACKED("Cracked");

private final String displayName;

   public String getDisplayName() {
      return this.displayName;
   }

   private AccountLoginType(String str) {
      this.displayName = str;
   }
}
