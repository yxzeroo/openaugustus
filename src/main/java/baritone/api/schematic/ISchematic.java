package baritone.api.schematic;

import net.minecraft.util.EnumFacing;
import java.util.List;
import net.minecraft.block.state.IBlockState;

public interface ISchematic {
   default int size(EnumFacing.Axis axis) {
      switch (ISchematic.AxisSwitchMap.ORDINAL_MAP[axis.ordinal()]) {
         case 1:
            return this.widthX();
         case 2:
            return this.heightY();
         case 3:
            return this.lengthZ();
         default:
            throw new UnsupportedOperationException(axis + "");
      }
   }

   default boolean inSchematic(int n, int n2, int n3, IBlockState state) {
      return n >= 0 && n < this.widthX() && n2 >= 0 && n2 < this.heightY() && n3 >= 0 && n3 < this.lengthZ();
   }

   IBlockState desiredState(int n, int n2, int n3, IBlockState state, List list);

   default void reset() {
   }

   int heightY();

   int widthX();

   int lengthZ();


   public static class AxisSwitchMap {
      public static final int[] ORDINAL_MAP = new int[EnumFacing.Axis.values().length];

      static {
         try {
            ORDINAL_MAP[EnumFacing.Axis.X.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }

         try {
            ORDINAL_MAP[EnumFacing.Axis.Y.ordinal()] = 2;
         } catch (NoSuchFieldError e2) {
         }

         try {
            ORDINAL_MAP[EnumFacing.Axis.Z.ordinal()] = 3;
         } catch (NoSuchFieldError e3) {
         }
      }
   }
}
