package net.minecraft.world.chunk;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class ChunkPrimer {
   private final short[] data = new short[65536];
   private final IBlockState defaultState = Blocks.air.getDefaultState();

   public IBlockState getBlockState(int n) {
      if (n >= 0 && n < this.data.length) {
         IBlockState byValue = (IBlockState)Block.BLOCK_STATE_IDS.getByValue(this.data[n]);
         return byValue != null ? byValue : this.defaultState;
      } else {
         throw new IndexOutOfBoundsException("The coordinate is out of range");
      }
   }

   public IBlockState getBlockState(int n, int n2, int n3) {
      int n4 = n << 12 | n3 << 8 | n2;
      return this.getBlockState(n4);
   }

   public void setBlockState(int n, int n2, int n3, IBlockState state) {
      int n4 = n << 12 | n3 << 8 | n2;
      this.setBlockState(n4, state);
   }

   public void setBlockState(int n, IBlockState state) {
      if (n >= 0 && n < this.data.length) {
         this.data[n] = (short)Block.BLOCK_STATE_IDS.get(state);
      } else {
         throw new IndexOutOfBoundsException("The coordinate is out of range");
      }
   }
}
