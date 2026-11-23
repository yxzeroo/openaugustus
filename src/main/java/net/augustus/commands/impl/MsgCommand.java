package net.augustus.commands.impl;



import net.augustus.Augustus;
import net.augustus.commands.AbstractIrcCommand;
public class MsgCommand extends AbstractIrcCommand {

   public void commandAction(String[] strings, String str) {
      if (!Augustus.getInstance().getModuleManager().ircChat.isToggled()) {
         this.sendChat("Please enable the IRCChat module");
      } else {
         if (strings.length > 2) {
            StringBuilder sb = new StringBuilder();

            for (int i = 2; i < strings.length; i++) {
               sb.append(strings[i]).append(" ");
            }

            sb.setLength(Math.max(0, sb.length() - 1));
            String str3 = strings[1];
            String str4 = sb.toString();
         } else {
            this.sendChat("§7" + this.getCommand() + " [§cusername§7] [§2message§7]");
         }
      }
   }

   public void helpMessage() {
      this.sendChat("§c" + this.getCommand() + "§7 (send private messages to other augustus user)");
   }

   public MsgCommand() {
      super("msg");
   }

}
