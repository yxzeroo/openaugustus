package net.augustus.commands.impl;



import net.augustus.ClientManagers;
import net.augustus.commands.Command;
public class AutoRegisterCommand extends Command {

   public void commandAction(String[] strings, String str) {
      if (strings.length > 1) {
         if (strings[1].equalsIgnoreCase("set") && strings.length >= 3) {
            String str3 = strings[2];
            ClientManagers.moduleManager.autoRegister.customPassword.setValue(str3);
            this.sendChat("Set CustomPassword to §2" + strings[2]);
            return;
         }

         if (strings[1].equalsIgnoreCase("get")) {
            if (strings.length >= 3) {
               switch (strings[2].toLowerCase()) {
                  case "custom":
                     this.sendChat("The CustomPassword is §2 " + ClientManagers.moduleManager.autoRegister.customPassword.getValue());
                     break;
                  case "generated":
                     StringBuilder sb = new StringBuilder().append("The generated password for ").append(mc.getSession().getUsername()).append(" is ");
                     String str7 = "generated";
                     this.sendChat(sb.append(ClientManagers.moduleManager.autoRegister.resolvePassword(str7, false)).toString());
                     break;
                  case "private":
                     StringBuilder sb2 = new StringBuilder().append("The private password for ").append(mc.getSession().getUsername()).append(" is ");
                     String str9 = "private";
                     this.sendChat(sb2.append(ClientManagers.moduleManager.autoRegister.resolvePassword(str9, false)).toString());
                     break;
                  case "private2":
                     StringBuilder sb3 = new StringBuilder().append("The private2 password for ").append(mc.getSession().getUsername()).append(" is ");
                     String str11 = "private2";
                     this.sendChat(sb3.append(ClientManagers.moduleManager.autoRegister.resolvePassword(str11, false)).toString());
                     break;
                  default:
                     this.sendUsageMessage();
               }

               return;
            }

            this.sendChat("The CustomPassword is §2" + ClientManagers.moduleManager.autoRegister.customPassword.getValue());
            return;
         }
      }

      this.sendUsageMessage();
   }

   public AutoRegisterCommand() {
      super("autoregister");
   }

   public void helpMessage() {
      this.sendChat("§c" + this.getCommand() + "§7 (set or get password)");
   }

   public void sendUsageMessage() {
      this.sendChat("§7" + this.getCommand() + " [§cset§7] [§2Password§7]");
      this.sendChat("§7" + this.getCommand() + " [§cget§7] <custom/generated/private/private2>");
   }

}
