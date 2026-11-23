package net.augustus.commands.impl;

import net.augustus.Augustus;
import net.augustus.commands.Command;
import net.augustus.utils.ChatUtil;
import net.augustus.modules.misc.RotationRenderer;

public class RotationRendererSetTickCommand extends Command {

   public void commandAction(String[] strings, String str) {
      if (strings.length < 2) {
         ChatUtil.sendChat3(".rrsettick [tick]");
      } else {
         RotationRenderer rotationRenderer = Augustus.getInstance().getModuleManager().rotationRenderer;
         if (!rotationRenderer.isToggled()) {
            ChatUtil.sendChat3("RotationRenderer isn't enabled");
         } else {
            try {
               int n = Integer.parseInt(strings[1]);
               if (rotationRenderer.recordedRotations.size() <= n) {
                  ChatUtil.sendChat3("There are only " + rotationRenderer.recordedRotations.size() + " ticks available");
                  return;
               }

               rotationRenderer.playbackIndex = (double)n;
               ChatUtil.sendChat3("Set tick to " + n);
            } catch (NumberFormatException e) {
               ChatUtil.sendChat3("Tick must be an integer!");
            }
         }
      }
   }

   public void helpMessage() {
      ChatUtil.sendChat3("§c" + this.getCommand() + "§7 (sets the tick inside the rotationrenderer)");
   }

   public RotationRendererSetTickCommand(int n, short s, short s2) {
      super(".rrsettick");
   }

}
