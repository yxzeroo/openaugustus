package net.augustus.ui.menu;

import net.augustus.settings.Setting;
import net.augustus.ui.account.AccountManagerScreen;
import net.augustus.ui.proxy.ProxyManagerScreen;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientTabManager {
   private final List tabs = new CopyOnWriteArrayList();
   public IrcMenuTab ircMenuTab;
   public ClientMenuTab activeTab;
   private static Setting[] cachedSettingsGuard;

   public List getTabs() {
      return this.tabs;
   }

   public void initTabs() {
      this.tabs.add(new AccountManagerScreen());
      this.tabs.add(new ProxyManagerScreen());
      this.tabs.add(new QuestsMenuTab());
      this.tabs.add(new BackgroundMenuTab());
      Setting[] cachedSettingsGuard2 = getCachedSettingsGuard();
      this.tabs.add(new ChangelogMenuTab());
      this.ircMenuTab = new IrcMenuTab();
      this.tabs.add(this.ircMenuTab);
      this.tabs.add(new CreditsMenuTab());
      if (cachedSettingsGuard2 != null) {
         Setting.setControlFlowMarker("Q5lUuc");
      }
   }

   public static void setCachedSettingsGuard(Setting[] settings) {
      cachedSettingsGuard = settings;
   }

   public static Setting[] getCachedSettingsGuard() {
      return cachedSettingsGuard;
   }
}
