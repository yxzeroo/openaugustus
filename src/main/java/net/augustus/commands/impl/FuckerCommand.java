package net.augustus.commands.impl;



import net.augustus.ClientManagers;
import net.augustus.commands.Command;
public class FuckerCommand extends Command {

   public void commandAction(String[] strings, String str) {
      if (strings.length > 1) {
         if (strings[1].equalsIgnoreCase("set") && strings.length >= 3) {
            int n = -1;

            try {
               n = Integer.parseInt(strings[2]);
            } catch (Exception e) {
               System.err.println("No Integer");
            }

            if (n != -1) {
               double d = (double)n;
               ClientManagers.moduleManager.fucker.customID.setValue(d);
               String str2 = "Set Fucker ID to §2" + (int)ClientManagers.moduleManager.fucker.customID.getValue();
               this.sendChat(str2);
               return;
            }
         } else if (strings[1].equalsIgnoreCase("get")) {
            String str3 = "BlockID: §2" + (int)ClientManagers.moduleManager.fucker.customID.getValue();
            this.sendChat(str3);
            return;
         }
      }

      this.sendUsageMessage();
   }

   public FuckerCommand() {
      super("fucker");
   }

   public void sendUsageMessage() {
      String str = "§7" + this.getCommand() + " [§cset/get§7] [§2BlockID§7]";
      this.sendChat(str);
   }

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (Get or set the blockID for custom mode)";
      this.sendChat(str);
   }

}
