package net.augustus.modules.modes.jesus;

import net.augustus.ClientManagers;
import net.augustus.modules.modes.AbstractSubMode;
import net.augustus.modules.movement.Jesus;
import net.augustus.settings.DoubleValue;
import net.minecraft.block.BlockLiquid;

public abstract class AbstractJesusMode extends AbstractSubMode {
   private static int[] controlFlowState2;

   public AbstractJesusMode(Jesus jesus, DoubleValue doubleValue, String str) {
      super(jesus, doubleValue, str);
   }

   public boolean shouldApplyJesus() {
      int[] controlFlowState22 = getControlFlowState2();
      return !ClientManagers.moduleManager.jesus.disableatsneakJump.getValue()
         || !mc.gameSettings.keyBindSneak.isKeyDown()
            && (
               !mc.gameSettings.keyBindJump.isKeyDown()
                  || !(mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ).getBlock() instanceof BlockLiquid)
            );
   }

   public static void setControlFlowState2(int[] ints) {
      controlFlowState2 = ints;
   }

   public static int[] getControlFlowState2() {
      return controlFlowState2;
   }

   static {
      if (getControlFlowState2() == null) {
         setControlFlowState2(new int[2]);
      }
   }
}
