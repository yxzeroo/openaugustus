package net.augustus.commands.impl;

import net.augustus.Augustus;
import net.augustus.commands.AbstractIrcCommand;
import net.minecraft.util.EnumChatFormatting;

public class IrcCommand extends AbstractIrcCommand {

   public void commandAction(String[] strings, String str) {
      if (!Augustus.getInstance().getModuleManager().ircChat.isToggled()) {
         String str2 = "Please enable the IRCChat module";
         this.sendChat(str2);
      } else {
         if (strings.length > 1) {
            StringBuilder sb = new StringBuilder();

            for (int i = 1; i < strings.length; i++) {
               sb.append(strings[i]).append(" ");
            }

            sb.setLength(Math.max(0, sb.length() - 1));
            String str3 = sb.toString();

            for (EnumChatFormatting enumChatFormatting : EnumChatFormatting.values()) {
               String str4 = "&" + enumChatFormatting.getFormattingCode();
               if (str3.contains(str4)) {
                  str3 = sb.toString().replace(str4, "§" + enumChatFormatting.getFormattingCode());
               }
            }

         } else {
            String str5 = "§7" + this.getCommand() + " [§2message§7]";
            this.sendChat(str5);
         }
      }
   }

   public IrcCommand() {
      super("irc");
   }

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (send messages to other augustus users)";
      this.sendChat(str);
   }

}
