package net.augustus.ui.config;



import net.augustus.Augustus;
import net.augustus.config.ConfigManager;
import net.augustus.config.ModuleConfigStorage;
public class UserConfigListItem extends AbstractConfigListItem {
   private boolean localFileExists;
   private boolean sharedWithFriends;

   public UserConfigListItem(String str, String str2, String str3, String str4, boolean flag, boolean flag2, boolean flag3) {
       super(str, str2, str3, str4, flag3);
      this.sharedWithFriends = flag2;
      this.localFileExists = flag;
   }

   @Override
   public boolean isSharedWithFriends() {
      return this.sharedWithFriends;
   }

   public void setSharedWithFriends(boolean flag) {
      this.sharedWithFriends = flag;
   }

   @Override
   public void loadConfig() {
      ConfigManager configManager = Augustus.getInstance().getConfigManager();
      configManager.snapshotAll();
      if (this.localFileExists) {
         ModuleConfigStorage moduleConfigStorage = Augustus.getInstance().getConfigManager().moduleConfigStorage;
         String configName2 = this.getConfigName();
         String str = "configs";
         String str2 = configName2;
         moduleConfigStorage.loadConfig(str2, str);
      } else {
      }
   }

   public boolean isLocalFileExists() {
      return this.localFileExists;
   }

}
