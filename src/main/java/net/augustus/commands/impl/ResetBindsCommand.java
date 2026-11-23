package net.augustus.commands.impl;

import net.augustus.Augustus;
import net.augustus.commands.Command;
import net.augustus.modules.Module;

public class ResetBindsCommand extends Command {

   public void commandAction(String[] strings, String str) {

      for (Module module : (Iterable<Module>)(Augustus.getInstance().getModuleManager().getModules())) {
         byte b = 0;
         module.setKeyCode(b);
      }

      String str2 = "§cAll module binds were reset";
      this.sendChat(str2);
   }

   public ResetBindsCommand() {
      super("resetbinds");
   }

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (resets all module binds)";
      this.sendChat(str);
   }

}
