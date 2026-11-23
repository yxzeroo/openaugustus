package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockNewLog extends BlockLog {
   public static final PropertyEnum VARIANT;

   static {
      VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, obj -> ((BlockPlanks.EnumType)obj).getMetadata() >= 4);
   }

   @Override
   public int damageDropped(IBlockState state) {
      return ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata() - 4;
   }

   public BlockNewLog() {
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
   }

   @Override
   public int getMetaFromState(IBlockState state) {
      int n = 0;
      n |= ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata() - 4;
      switch (BlockNewLog.EnumAxisSwitchMap.AXIS_LOOKUP[((BlockLog.EnumAxis)state.getValue(LOG_AXIS)).ordinal()]) {
         case 1:
            n |= 4;
            break;
         case 2:
            n |= 8;
            break;
         case 3:
            n |= 12;
      }

      return n;
   }

   @Override
   public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(item, 1, BlockPlanks.EnumType.ACACIA.getMetadata() - 4));
      list.add(new ItemStack(item, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4));
   }

   @Override
   public MapColor getMapColor(IBlockState state) {
      BlockPlanks.EnumType value = (BlockPlanks.EnumType)state.getValue(VARIANT);
      switch (BlockNewLog.EnumAxisSwitchMap.AXIS_LOOKUP[((BlockLog.EnumAxis)state.getValue(LOG_AXIS)).ordinal()]) {
         case 1:
         case 2:
         case 3:
         default:
            switch (BlockNewLog.EnumAxisSwitchMap.WOOD_TYPE_LOOKUP[value.ordinal()]) {
               case 1:
               default:
                  return MapColor.stoneColor;
               case 2:
                  return BlockPlanks.EnumType.DARK_OAK.getMapColor();
            }
         case 4:
            return value.getMapColor();
      }
   }

   @Override
   public IBlockState getStateFromMeta(int n) {
      IBlockState state = this.getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata((n & 3) + 4));
      switch (n & 12) {
         case 0:
            state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
            break;
         case 4:
            state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
            break;
         case 8:
            state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
            break;
         default:
            state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
      }

      return state;
   }

   @Override
   public BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{VARIANT, LOG_AXIS});
   }

   @Override
   public ItemStack createStackedBlock(IBlockState state) {
      return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata() - 4);
   }



   public static class EnumAxisSwitchMap {
      public static final int[] WOOD_TYPE_LOOKUP;
      public static final int[] AXIS_LOOKUP = new int[BlockLog.EnumAxis.values().length];

      static {
         try {
            AXIS_LOOKUP[BlockLog.EnumAxis.X.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }

         try {
            AXIS_LOOKUP[BlockLog.EnumAxis.Z.ordinal()] = 2;
         } catch (NoSuchFieldError e2) {
         }

         try {
            AXIS_LOOKUP[BlockLog.EnumAxis.NONE.ordinal()] = 3;
         } catch (NoSuchFieldError e3) {
         }

         try {
            AXIS_LOOKUP[BlockLog.EnumAxis.Y.ordinal()] = 4;
         } catch (NoSuchFieldError e4) {
         }

         WOOD_TYPE_LOOKUP = new int[BlockPlanks.EnumType.values().length];

         try {
            WOOD_TYPE_LOOKUP[BlockPlanks.EnumType.ACACIA.ordinal()] = 1;
         } catch (NoSuchFieldError e5) {
         }

         try {
            WOOD_TYPE_LOOKUP[BlockPlanks.EnumType.DARK_OAK.ordinal()] = 2;
         } catch (NoSuchFieldError e6) {
         }
      }
   }
}
