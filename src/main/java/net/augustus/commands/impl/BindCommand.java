package net.augustus.commands.impl;

import net.augustus.Augustus;
import net.augustus.commands.Command;
import net.augustus.modules.FeatureBase;
import net.augustus.modules.Module;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

   public void sendUsageMessage() {
      String str = "§7" + this.getCommand() + " [§cModule§7] [§2Key§7]";
      this.sendChat(str);
   }

   public BindCommand() {
      super("bind");
   }

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (Binds a module to a key)";
      this.sendChat(str);
   }

   public void commandAction(String[] strings, String str) {
      if (strings.length >= 3) {
         for (Module module : (Iterable<Module>)(Augustus.getInstance().getModuleManager().getModules())) {
            if (strings[1].equalsIgnoreCase(((FeatureBase)module).getName())) {
               for (int i = 0; i < 84; i++) {
                  if (Keyboard.getKeyName(i).equalsIgnoreCase(strings[2])) {
                     String str2 = strings[2].toUpperCase();
                     this.bindModuleKey(i, module, str2);
                     return;
                  }
               }
            }
         }
      }

      this.sendUsageMessage();
   }

   private void bindModuleKey(int n, Module module, String str) {
      module.setKeyCode(n);
      String str2 = "§c" + ((FeatureBase)module).getName() + " §7bound to §2" + str.toUpperCase();
      this.sendChat(str2);
   }

}
