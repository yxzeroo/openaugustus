package net.augustus.commands.impl;



import net.augustus.commands.Command;
import net.augustus.utils.MoveUtil;
public class HClipCommand extends Command {

   public void helpMessage() {
      String str = "§c" + this.getCommand() + "§7 (clip forwards and sidewards through walls)";
      this.sendChat(str);
   }

   public void commandAction(String[] strings, String str) {
      if (strings.length == 3) {
         double d = Double.parseDouble(strings[1]);
         double d2 = Double.parseDouble(strings[2]);
         if (mc.theWorld != null) {
            if (mc.thePlayer != null) {
               mc.thePlayer.setPosition(mc.thePlayer.posX + d, mc.thePlayer.posY, mc.thePlayer.posZ + d2);
            }
         }

         String str2 = "hclipped";
         this.sendChat(str2);
      } else if (strings.length == 2) {
         double d3 = Double.parseDouble(strings[1]);
         double d4 = Math.toRadians((double)MoveUtil.getDirectionDegrees(false));
         double d5 = -(Math.sin(d4) * d3);
         double d6 = Math.cos(d4) * d3;
         if (mc.theWorld != null && mc.thePlayer != null) {
            mc.thePlayer.setPosition(mc.thePlayer.posX + d5, mc.thePlayer.posY, mc.thePlayer.posZ + d6);
         }
      } else {
         String str3 = "§7" + this.getCommand() + " [§2DistanceX§7] [§2DistanceZ§7] or";
         this.sendChat(str3);
         String str4 = "§7" + this.getCommand() + " [§2DistanceXZ§7]";
         this.sendChat(str4);
      }
   }

   public HClipCommand() {
      super("hclip");
   }

}
