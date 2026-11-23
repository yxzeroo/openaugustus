package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPressurePlate extends BlockBasePressurePlate {
   public static final PropertyBool POWERED;
   private final BlockPressurePlate.Sensitivity sensitivity;

   @Override
   public BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{POWERED});
   }

   @Override
   public int computeRedstoneStrength(World world, BlockPos pos) {
      AxisAlignedBB sensitiveAABB = this.getSensitiveAABB(pos);
      List list;
      switch (BlockPressurePlate.SensitivitySwitchMap.ORDINAL_MAP[this.sensitivity.ordinal()]) {
         case 1:
            list = world.getEntitiesWithinAABBExcludingEntity((Entity)null, sensitiveAABB);
            break;
         case 2:
            list = world.getEntitiesWithinAABB(EntityLivingBase.class, sensitiveAABB);
            break;
         default:
            return 0;
      }

      if (!list.isEmpty()) {
         for (Entity entity : (Iterable<Entity>)(list)) {
            if (!entity.doesEntityNotTriggerPressurePlate()) {
               return 15;
            }
         }
      }

      return 0;
   }

   static {
      POWERED = PropertyBool.create("powered");
   }

   @Override
   public int getRedstoneStrength(IBlockState state) {
      return (Boolean)state.getValue(POWERED) ? 15 : 0;
   }

   @Override
   public IBlockState setRedstoneStrength(IBlockState state, int n) {
      return state.withProperty(POWERED, n > 0);
   }

   @Override
   public int getMetaFromState(IBlockState state) {
      return (Boolean)state.getValue(POWERED) ? 1 : 0;
   }

   @Override
   public IBlockState getStateFromMeta(int n) {
      return this.getDefaultState().withProperty(POWERED, n == 1);
   }

   public BlockPressurePlate(Material material, BlockPressurePlate.Sensitivity sensitivity2) {
      super(material);
      this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.FALSE));
      this.sensitivity = sensitivity2;
   }


   public static class SensitivitySwitchMap {
      public static final int[] ORDINAL_MAP = new int[BlockPressurePlate.Sensitivity.values().length];

      static {
         try {
            ORDINAL_MAP[BlockPressurePlate.Sensitivity.EVERYTHING.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }

         try {
            ORDINAL_MAP[BlockPressurePlate.Sensitivity.MOBS.ordinal()] = 2;
         } catch (NoSuchFieldError e2) {
         }
      }
   }

   public enum Sensitivity {
      EVERYTHING,
      MOBS;
   }
}
