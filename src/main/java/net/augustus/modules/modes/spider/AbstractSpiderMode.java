package net.augustus.modules.modes.spider;

import net.augustus.modules.modes.AbstractSubMode;
import net.augustus.modules.movement.Spider;
import net.augustus.settings.DoubleValue;

public abstract class AbstractSpiderMode extends AbstractSubMode {
   private static int[] controlFlowState2;

   public AbstractSpiderMode(Spider spider, DoubleValue doubleValue, String str) {
      super(spider, doubleValue, str);
   }

   public boolean canClimbWall() {
      int[] controlFlowState22 = getControlFlowState2();
      return mc.thePlayer != null && mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava();
   }

   public static void setControlFlowState2(int[] ints) {
      controlFlowState2 = ints;
   }

   public static int[] getControlFlowState2() {
      return controlFlowState2;
   }

   static {
      if (getControlFlowState2() != null) {
         setControlFlowState2(new int[1]);
      }
   }
}
