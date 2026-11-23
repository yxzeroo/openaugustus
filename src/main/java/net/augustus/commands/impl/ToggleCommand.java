package net.augustus.commands.impl;

import net.augustus.Augustus;
import net.augustus.commands.Command;
import net.augustus.modules.FeatureBase;
import net.augustus.modules.Module;

public class ToggleCommand extends Command {

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (Toggles a module)";
      this.sendChat(str);
   }

   public ToggleCommand() {
      super("t");
   }

   public void commandAction(String[] strings, String str) {
      if (strings.length > 1) {
         for (Module module : (Iterable<Module>)(Augustus.getInstance().getModuleManager().getModules())) {
            if (strings[1].equalsIgnoreCase(((FeatureBase)module).getName()) && !module.isHiddenInGui()) {
               module.toggle();
               StringBuilder sb = new StringBuilder().append("§c").append(((FeatureBase)module).getName());
               StringBuilder sb2 = sb.append(" §7toggled ");
               String str2;
               if (module.isToggled()) {
                  str2 = "§2on";
               } else {
                  str2 = "§4off";
               }

               String str3 = sb2.append(str2).toString();
               this.sendChat(str3);
               return;
            }
         }
      } else {
         String str4 = "§7" + this.getCommand() + " [§2Module§7]";
         this.sendChat(str4);
      }
   }


}
