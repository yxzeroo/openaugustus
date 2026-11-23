package net.augustus.commands.impl;



import net.augustus.ClientManagers;
import net.augustus.commands.Command;
import net.augustus.settings.TextInputSetting;
public class ClientSpooferCommand extends Command {

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (set a custom string for clientspoofing)";
      this.sendChat(str);
   }

   public ClientSpooferCommand() {
      super("clientSpoofer");
   }

   public void commandAction(String[] strings, String str) {
      if (strings.length > 1) {
         TextInputSetting customSpoofString = ClientManagers.moduleManager.clientSpoofer.customSpoofString;
         String str2 = str.substring(this.getCommand().length() + 1);
         String str3 = str2;
         customSpoofString.setValue(str3);
         String str4 = "Set spoofer string to: §c§l\"" + ClientManagers.moduleManager.clientSpoofer.customSpoofString.getValue() + "\"";
         this.sendChat(str4);
      } else {
         String str5 = "§7" + this.getCommand() + " [§2Custom string§7]";
         this.sendChat(str5);
      }
   }

}
