package net.augustus.commands.impl;



import net.augustus.Augustus;
import net.augustus.commands.AbstractIrcCommand;
public class ReplyCommand extends AbstractIrcCommand {

   public ReplyCommand() {
      super("r");
   }

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (reply to private messages from other augustus user)";
      this.sendChat(str);
   }

   public void commandAction(String[] strings, String str) {
      if (!Augustus.getInstance().getModuleManager().ircChat.isToggled()) {
         String str2 = "Please enable the IRCChat module";
         this.sendChat(str2);
      } else if (Augustus.getInstance().getModuleManager().ircChat.getLastWhisperPartner().isEmpty()) {
         String str3 = "No direct message to reply";
         this.sendChat(str3);
      } else {
         if (strings.length > 1) {
            StringBuilder sb = new StringBuilder();

            for (int i = 1; i < strings.length; i++) {
               sb.append(strings[i]).append(" ");
            }

            sb.setLength(Math.max(0, sb.length() - 1));
            String lastWhisperPartner = Augustus.getInstance().getModuleManager().ircChat.getLastWhisperPartner();
            String str4 = sb.toString();
         } else {
            String str5 = "§7" + this.getCommand() + " [§cusername§7] [§2message§7]";
            this.sendChat(str5);
         }
      }
   }


}
