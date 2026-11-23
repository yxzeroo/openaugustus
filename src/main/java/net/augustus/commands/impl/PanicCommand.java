package net.augustus.commands.impl;

import net.augustus.ClientManagers;
import net.augustus.commands.Command;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;

public class PanicCommand extends Command {

   public PanicCommand() {
      super("panic");
   }

   public void commandAction(String[] strings, String str) {
      boolean flag = strings.length == 2 && strings[1].equalsIgnoreCase("norender");

      for (Module module : (Iterable<Module>)(ClientManagers.moduleManager.getModules())) {
         if (module.isToggled() && (module.getCategorys() != Categorys.RENDER || !flag)) {
            boolean flag2 = false;
            boolean flag3 = false;
            module.setToggled(flag3, flag2);
         }
      }

      ClientManagers.moduleManager.sortModules();
   }

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (disables all modules, except for visuals when norender is specified (.panic norender))";
      this.sendChat(str);
   }

}
