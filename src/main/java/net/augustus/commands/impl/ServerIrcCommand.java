package net.augustus.commands.impl;

import net.augustus.Augustus;
import net.augustus.commands.AbstractIrcCommand;
import java.util.Locale;

public class ServerIrcCommand extends AbstractIrcCommand {

   public void helpMessage() {
      this.sendChat("§c" + this.getCommand() + "§7 (Execute server commands)");
   }

   public ServerIrcCommand() {
      super("server");
   }

   public void commandAction(String[] strings, String str) {
      if (strings.length == 2) {
         String str2 = strings[1];
         String str3 = "";
         String str4 = "";
      } else if (strings.length > 2) {
         StringBuilder sb = new StringBuilder();

         for (int i = 3; i < strings.length; i++) {
            sb.append(strings[i]).append(" ");
         }

         sb.setLength(Math.max(0, sb.length() - 1));
         String str5 = strings[1];
         String str6 = strings[2];
         String str8 = sb.toString();
         String str9 = str6;
      } else if (strings.length == 1 && strings[0].toLowerCase(Locale.ROOT).equals(this.getCommand())) {
         String username = Augustus.getInstance().getUsername();
         String str10 = "";
         String str11 = username;
      }
   }

}
