package net.augustus.commands.impl;



import net.augustus.ClientManagers;
import net.augustus.commands.Command;
public class AutoRegisterCommand extends Command {

   public void commandAction(String[] strings, String str) {
      if (strings.length > 1) {
         if (strings[1].equalsIgnoreCase("set") && strings.length >= 3) {
            String str2 = strings[2];
            String str3 = str2;
            ClientManagers.moduleManager.autoRegister.customPassword.setValue(str3);
            String str4 = "Set CustomPassword to §2" + strings[2];
            this.sendChat(str4);
            return;
         }

         if (strings[1].equalsIgnoreCase("get")) {
            if (strings.length >= 3) {
               String str5 = strings[2].toLowerCase();
               switch (str5) {
                  case "custom":
                     String str6 = "The CustomPassword is §2 " + ClientManagers.moduleManager.autoRegister.customPassword.getValue();
                     this.sendChat(str6);
                     break;
                  case "generated":
                     StringBuilder sb = new StringBuilder().append("The generated password for ").append(mc.getSession().getUsername()).append(" is ");
                     boolean flag = false;
                     String str7 = "generated";
                     String str8 = sb.append(ClientManagers.moduleManager.autoRegister.resolvePassword(str7, flag)).toString();
                     this.sendChat(str8);
                     break;
                  case "private":
                     StringBuilder sb2 = new StringBuilder().append("The private password for ").append(mc.getSession().getUsername()).append(" is ");
                     boolean flag2 = false;
                     String str9 = "private";
                     String str10 = sb2.append(ClientManagers.moduleManager.autoRegister.resolvePassword(str9, flag2)).toString();
                     this.sendChat(str10);
                     break;
                  case "private2":
                     StringBuilder sb3 = new StringBuilder().append("The private2 password for ").append(mc.getSession().getUsername()).append(" is ");
                     boolean flag3 = false;
                     String str11 = "private2";
                     String str12 = sb3.append(ClientManagers.moduleManager.autoRegister.resolvePassword(str11, flag3)).toString();
                     this.sendChat(str12);
                     break;
                  default:
                     this.sendUsageMessage();
               }

               return;
            }

            String str13 = "The CustomPassword is §2" + ClientManagers.moduleManager.autoRegister.customPassword.getValue();
            this.sendChat(str13);
            return;
         }
      }

      this.sendUsageMessage();
   }

   public AutoRegisterCommand() {
      super("autoregister");
   }

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (set or get password)";
      this.sendChat(str);
   }

   public void sendUsageMessage() {
      String str = "§7" + this.getCommand() + " [§cset§7] [§2Password§7]";
      this.sendChat(str);
      String str2 = "§7" + this.getCommand() + " [§cget§7] <custom/generated/private/private2>";
      this.sendChat(str2);
   }

}
