package net.augustus.commands.impl;



import net.augustus.ClientManagers;
import net.augustus.commands.Command;
public class ArrayListSuffixCommand extends Command {

   public void commandAction(String[] strings, String str) {
      if (strings.length == 2) {
         String str2 = strings[1];
         String str3 = str2;
         ClientManagers.moduleManager.arrayList.customSuffix.setValue(str3);
         String str4 = "§7Set suffix to " + strings[1];
         this.sendChat(str4);
      } else {
         String str5 = "§7" + this.getCommand() + " [§2Suffix§7]";
         this.sendChat(str5);
      }
   }

   public ArrayListSuffixCommand() {
      super("arraylistsuffix");
   }

   public void helpMessage() {
      String str = "§c" + this.getCommand() + " §7(Set custom arraylist suffix)";
      this.sendChat(str);
   }

}
