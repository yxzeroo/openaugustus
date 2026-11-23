package net.augustus.commands.impl;



import net.augustus.ClientManagers;
import net.augustus.commands.Command;
import net.augustus.settings.TextInputSetting;
public class SpammerCommand extends Command {

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (Sets your spam message)";
      this.sendChat(str);
   }

   public SpammerCommand() {
      super("spammer");
   }

   public void commandAction(String[] strings, String str) {
      if (strings.length > 1) {
         String str2 = "";

         for (int i = 0; i < strings.length; i++) {
            if (i > 0) {
               str2 = str2 + strings[i] + " ";
            }
         }

         if (ClientManagers.moduleManager.spammer.messages.getEntryCount() > 0) {
            byte b = 0;
            TextInputSetting entry = (TextInputSetting)ClientManagers.moduleManager.spammer.messages.getEntry(b);
            entry.setValue(str2);
            String str3 = "Set your first spam message to: §c§l" + str2;
            String str4 = str3;
            this.sendChat(str4);
         } else {
            String str5 = "§cNo messages available. Add one in the ClickGUI first.";
            this.sendChat(str5);
         }
      } else {
         String str6 = "§7" + this.getCommand() + " [§2Custom message§7]";
         this.sendChat(str6);
      }
   }

}
