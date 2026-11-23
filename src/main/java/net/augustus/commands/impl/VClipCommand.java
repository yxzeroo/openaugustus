package net.augustus.commands.impl;



import net.augustus.commands.Command;
public class VClipCommand extends Command {

   public void helpMessage() {
      this.sendChat("§c" + this.getCommand() + "§7 (clip up and down through walls)");
   }

   public VClipCommand() {
      super("vclip");
   }

   public void commandAction(String[] strings, String str) {
      if (strings.length == 2) {
         double d = Double.parseDouble(strings[1]);
         if (mc.theWorld != null) {
            if (mc.thePlayer != null) {
               mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + d, mc.thePlayer.posZ);
            }
         }

         this.sendChat("vclipped");
      } else {
         this.sendChat("§7" + this.getCommand() + " [§2Distance§7]");
      }
   }

}
