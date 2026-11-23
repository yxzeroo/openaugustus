package net.augustus.commands.impl;



import net.augustus.ClientManagers;
import net.augustus.commands.Command;
import net.augustus.settings.TextInputSetting;
public class DeathMessageCommand extends Command {

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (set the death message)";
      this.sendChat(str);
   }

   public DeathMessageCommand() {
      super("deathmessage");
   }

   public void commandAction(String[] strings, String str) {
      if (strings.length > 1) {
         StringBuilder sb = new StringBuilder();

         for (int i = 1; i < strings.length; i++) {
            sb.append(strings[i]).append(" ");
         }

         sb.setLength(Math.max(0, sb.length() - 1));
         if (ClientManagers.moduleManager.deathCommand.messageAtDeath.getEntryCount() > 0) {
            String str2 = sb.toString();
            byte b = 0;
            TextInputSetting entry = (TextInputSetting)ClientManagers.moduleManager.deathCommand.messageAtDeath.getEntry(b);
            entry.setValue(str2);
            String str3 = "Set your first death message to: §c§l" + str2;
            this.sendChat(str3);
         } else {
            String str4 = "§cNo messages available. Add one in the ClickGUI first.";
            this.sendChat(str4);
         }
      }
   }

}
