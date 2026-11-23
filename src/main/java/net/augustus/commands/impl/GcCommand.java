package net.augustus.commands.impl;



import net.augustus.commands.Command;
import net.augustus.utils.ChatUtil;
public class GcCommand extends Command {

   public GcCommand() {
      super("gc");
   }

   @Override
   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (clears your memory)";
      this.sendChat(str);
   }

   @Override
   public void commandAction(String[] strings, String str) {
      System.gc();
      ChatUtil.sendChat3("Memory cleared.");
   }
}
