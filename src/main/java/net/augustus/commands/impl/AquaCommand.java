package net.augustus.commands.impl;



import net.augustus.commands.Command;
public class AquaCommand extends Command {

   public void commandAction(String[] strings, String str) {
      if (strings.length > 1) {
         StringBuilder sb = new StringBuilder();

         for (int i = 1; i < strings.length; i++) {
            sb.append(strings[i]).append(" ");
         }

         sb.setLength(Math.max(0, sb.length() - 1));
      } else {
         String str2 = "§7" + this.getCommand() + " [§2+message§7]";
         this.sendChat(str2);
      }
   }

   public AquaCommand() {
      super("aqua");
   }

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (send messages to other Augustus/Aqua users (unsafe))";
      this.sendChat(str);
   }

}
