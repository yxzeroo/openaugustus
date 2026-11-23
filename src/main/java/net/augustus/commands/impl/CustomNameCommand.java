package net.augustus.commands.impl;



import net.augustus.ClientManagers;
import net.augustus.commands.Command;
public class CustomNameCommand extends Command {

   public void commandAction(String[] strings, String str) {
      if (strings.length == 2) {
         String str2 = strings[1];
         String str3 = str2;
         ClientManagers.moduleManager.customName.customName.setValue(str3);
         String str4 = "Set your custom name to: §c§l" + ClientManagers.moduleManager.customName.customName.getValue();
         this.sendChat(str4);
         ClientManagers.moduleManager.customName.refreshCustomName();
      } else {
         String str5 = "§7" + this.getCommand() + " [§2Name§7]";
         this.sendChat(str5);
      }
   }

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (Sets your custom name)";
      this.sendChat(str);
   }

   public CustomNameCommand() {
      super("customName");
   }

}
