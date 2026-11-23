package net.augustus.ui.config;



import net.augustus.Augustus;
import net.augustus.config.ConfigManager;
public class FriendConfigListItem extends AbstractConfigListItem {

   @Override
   public void loadConfig() {
      ConfigManager configManager = Augustus.getInstance().getConfigManager();
      configManager.snapshotAll();
   }

   public FriendConfigListItem(String str, String str2, String str3, String str4, boolean flag) {
       super(str, str2, str3, str4, flag);
   }
}
