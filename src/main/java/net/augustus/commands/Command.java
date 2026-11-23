package net.augustus.commands;

import net.augustus.Augustus;
import net.augustus.utils.ChatUtil;
import net.augustus.utils.interfaces.MC;

public abstract class Command implements MC {
   public String command;

   public Command(String str) {
      this.command = str;
   }

   public void sendChat(String str) {
      ChatUtil.sendChat3(str);
   }

   public String getCommand() {
      return Augustus.getInstance().getModuleManager().augustusServer.commandPrefixes.getSelectedValue() + this.command;
   }

   public abstract void helpMessage();

   public void setCommand(String str) {
      this.command = str;
   }

   public abstract void commandAction(String[] strings, String str);



}
