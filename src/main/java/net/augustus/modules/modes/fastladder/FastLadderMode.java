package net.augustus.modules.modes.fastladder;

import net.augustus.modules.modes.AbstractSubMode;
import net.augustus.modules.movement.FastLadder;
import net.augustus.settings.DoubleValue;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;

public abstract class FastLadderMode extends AbstractSubMode {
   private static int[] controlFlowState2;

   public FastLadderMode(FastLadder fastLadder, DoubleValue doubleValue, String str) {
      super(fastLadder, doubleValue, str);
   }

   public boolean isClimbableAtYOffset(double d) {
      int n = MathHelper.floor_double(mc.thePlayer.posX);
      getControlFlowState2();
      int n2 = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY + d);
      int n3 = MathHelper.floor_double(mc.thePlayer.posZ);
      Block block = mc.thePlayer.entityWorld.getBlockState2(n, n2, n3).getBlock();
      return block == Blocks.ladder || block == Blocks.vine && ((FastLadder)this.parentModule).vines.getValue();
   }

   public static void setControlFlowState2(int[] ints) {
      controlFlowState2 = ints;
   }

   public static int[] getControlFlowState2() {
      return controlFlowState2;
   }

   static {
      if (getControlFlowState2() != null) {
         setControlFlowState2(new int[2]);
      }
   }
}
