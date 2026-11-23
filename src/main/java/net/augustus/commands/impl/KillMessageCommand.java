package net.augustus.commands.impl;



import net.augustus.ClientManagers;
import net.augustus.commands.Command;
import net.augustus.settings.TextInputSetting;
public class KillMessageCommand extends Command {

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (Set killmessage for module KillMessage)";
      this.sendChat(str);
   }

   public KillMessageCommand() {
      super("killmessage");
   }

   public void commandAction(String[] strings, String str) {
      if (strings.length > 1) {
         StringBuilder sb = new StringBuilder();

         for (int i = 1; i < strings.length; i++) {
            sb.append(strings[i]);
            if (i < strings.length - 1) {
               sb.append(" ");
            }
         }

         if (ClientManagers.moduleManager.killMessage.customKillmessages.getEntryCount() > 0) {
            String str2 = sb.toString();
            byte b = 0;
            TextInputSetting entry = (TextInputSetting)ClientManagers.moduleManager.killMessage.customKillmessages.getEntry(b);
            entry.setValue(str2);
            String str3 = "Set your first kill message to: §c§l" + str2;
            this.sendChat(str3);
         } else {
            String str4 = "§cNo kill messages available. Add one in the ClickGUI first.";
            this.sendChat(str4);
         }
      } else {
         String str5 = "§7" + this.getCommand() + " [§2Message§7]";
         this.sendChat(str5);
      }
   }

}
