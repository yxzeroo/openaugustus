package net.augustus.commands;


import net.augustus.Augustus;
import net.augustus.ui.menu.ClientTabContainerScreen;
public abstract class AbstractIrcCommand extends Command {

   public AbstractIrcCommand(String str) {
      super(str);
   }

   public void sendChat(String str) {
      if (mc.currentScreen instanceof ClientTabContainerScreen) {
         Augustus.getInstance().getClientTabManager().ircMenuTab.appendMessage(str);
      } else {
         super.sendChat(str);
      }
   }



}
