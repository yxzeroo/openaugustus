package net.minecraft.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;

public abstract class BlockDirectional extends Block {
   public static final PropertyDirection FACING;

   static {
      String str = "facing";
      FACING = PropertyDirection.create(str, EnumFacing.Plane.HORIZONTAL);
   }

   public BlockDirectional(Material material, MapColor mapColor) {
      super(material, mapColor);
   }

   public BlockDirectional(Material material) {
      super(material);
   }
}
