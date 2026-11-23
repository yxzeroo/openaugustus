package net.augustus.commands.impl;

import net.augustus.commands.Command;
import net.minecraft.client.gui.GuiScreen;

public class IgnCommand extends Command {

   public void helpMessage() {
      String str = "§c" + this.getCommand() + " §7(Copies in-game name to clipboard)";
      this.sendChat(str);
   }

   public void commandAction(String[] strings, String str) {
      if (mc.thePlayer != null) {
         String name = mc.thePlayer.getName();
         GuiScreen.setClipboardString(name);
         String str2 = "Your name: §c" + name;
         this.sendChat(str2);
      }
   }

   public IgnCommand() {
      super("ign");
   }

}
