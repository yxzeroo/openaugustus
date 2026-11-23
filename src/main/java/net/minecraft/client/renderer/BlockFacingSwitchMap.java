package net.minecraft.client.renderer;

import net.minecraft.util.EnumFacing;

public class BlockFacingSwitchMap {
   public static final BlockFacingSwitchMap INSTANCE = new BlockFacingSwitchMap();
   public final int[] facingOrdinalMap = new int[EnumFacing.getValues().length];

   public BlockFacingSwitchMap() {
      try {
         this.facingOrdinalMap[EnumFacing.DOWN.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         this.facingOrdinalMap[EnumFacing.UP.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         this.facingOrdinalMap[EnumFacing.NORTH.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         this.facingOrdinalMap[EnumFacing.SOUTH.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }

      try {
         this.facingOrdinalMap[EnumFacing.WEST.ordinal()] = 5;
      } catch (NoSuchFieldError e5) {
      }

      try {
         this.facingOrdinalMap[EnumFacing.EAST.ordinal()] = 6;
      } catch (NoSuchFieldError e6) {
      }
   }
}
