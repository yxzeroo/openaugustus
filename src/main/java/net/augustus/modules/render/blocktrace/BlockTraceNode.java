package net.augustus.modules.render.blocktrace;

import java.util.ArrayList;
import java.util.List;
import net.augustus.utils.TimeHelper;
import net.augustus.utils.interfaces.MC;
import net.minecraft.util.BlockPos;

public class BlockTraceNode implements Comparable, MC {
   private float hCost;
   private float blockHardness;
   private final List neighbors;
   private float fCost;
   private final int minY;
   private float gCost;
   private BlockTraceNode blockTraceNode;
   private final TraceBlockPos traceBlockPos;

   public void setBlockTraceNode(BlockTraceNode blockTraceNode2) {
      this.blockTraceNode = blockTraceNode2;
   }

   public void setGCost(float f) {
      this.gCost = f;
      this.fCost = f + this.hCost;
   }

   public BlockTraceNode getBlockTraceNode() {
      return this.blockTraceNode;
   }

   public TraceBlockPos getTraceBlockPos() {
      return this.traceBlockPos;
   }

   public void setHCost(float f) {
      this.hCost = f;
      this.fCost = this.gCost + f;
   }

   public float getFCost() {
      return this.fCost;
   }

   public float getHCost() {
      return this.hCost;
   }

   public float getGCost() {
      return this.gCost;
   }

   public BlockTraceNode(TraceBlockPos traceBlockPos2, float f, float f2, BlockTraceNode blockTraceNode2, int n) {
       super();
      this.neighbors = new ArrayList();
      this.hCost = f2;
      this.traceBlockPos = traceBlockPos2;
      this.minY = n;
      this.blockHardness = mc.theWorld.getBlockState(traceBlockPos2.getBlockPos()).getBlock().getBlockHardness(null, null);
      this.gCost = f;
      this.blockTraceNode = blockTraceNode2;
      this.neighbors.clear();
      this.fCost = f + f2;
      byte b = 0;
      byte b2 = 0;
      byte b3 = 1;
      this.addNeighborOffset(b3, b2, b);
      byte b4 = 0;
      byte b5 = 0;
      byte b6 = -1;
      this.addNeighborOffset(b6, b5, b4);
      byte b7 = 1;
      byte b8 = 0;
      byte b9 = 0;
      this.addNeighborOffset(b9, b8, b7);
      byte b10 = -1;
      byte b11 = 0;
      byte b12 = 0;
      this.addNeighborOffset(b12, b11, b10);
      byte b13 = 0;
      byte b14 = 1;
      byte b15 = 0;
      this.addNeighborOffset(b15, b14, b13);
      byte b16 = 0;
      byte b17 = -1;
      byte b18 = 0;
      this.addNeighborOffset(b18, b17, b16);
   }

   private void addNeighborOffset(int n, int n2, int n3) {
      BlockPos pos = this.traceBlockPos.getBlockPos().add(n, n2, n3);
      if (pos.getY() >= this.minY) {
         if (!pos.equalsPos(this.traceBlockPos.getBlockPos())) {
            this.neighbors.add(new TraceBlockPos(pos));
         }
      }
   }

   @Override
   public int compareTo(Object other) {
      if (other instanceof BlockTraceNode) {
         if (((BlockTraceNode)other).getGCost() > this.getGCost()) {
            return -2;
         } else {
            return ((BlockTraceNode)other).getGCost() < this.getGCost() ? 2 : Double.compare((double)this.getHCost(), (double)((BlockTraceNode)other).getHCost());
         }
      } else {
         return 0;
      }
   }

   public List getNeighbors() {
      return this.neighbors;
   }
}
