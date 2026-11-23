package net.augustus.commands.impl;


import net.augustus.Augustus;
import net.augustus.commands.Command;
public class OnlineConfigCommand extends Command {
   @Override
   public void helpMessage() {
      Augustus.getInstance().getClass();
   }

   public OnlineConfigCommand(long l) {
      super("onlineconfig");
   }

   @Override
   public void commandAction(String[] strings, String str) {
      Augustus.getInstance().getClass();
   }
}
