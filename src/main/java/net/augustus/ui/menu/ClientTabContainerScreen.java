package net.augustus.ui.menu;

import net.augustus.Augustus;
import net.augustus.ui.TabbedMenuScreen;
import net.minecraft.client.gui.GuiScreen;

public class ClientTabContainerScreen extends TabbedMenuScreen {

   @Override
   public boolean shouldHideTab(ClientMenuTab clientMenuTab) {
      return clientMenuTab instanceof IrcMenuTab && !Augustus.getInstance().getModuleManager().ircChat.isToggled() ? false : super.shouldHideTab(clientMenuTab);
   }

   public ClientTabContainerScreen(GuiScreen screen, int n) {
       super(Augustus.getInstance().getClientTabManager().getTabs(), screen, n);
   }
}
