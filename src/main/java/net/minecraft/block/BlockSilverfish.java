package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockSilverfish extends Block {
   public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockSilverfish.EnumType.class);

   @Override
   public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float f, int n) {
      if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops")) {
         EntitySilverfish entitySilverfish = new EntitySilverfish(world);
         entitySilverfish.setLocationAndAngles((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5, 0.0F, 0.0F);
         world.spawnEntityInWorld(entitySilverfish);
         entitySilverfish.spawnExplosionParticle();
      }
   }

   @Override
   public int getDamageValue(World world, BlockPos pos) {
      IBlockState blockState2 = world.getBlockState(pos);
      return blockState2.getBlock().getMetaFromState(blockState2);
   }

   @Override
   public int quantityDropped(Random rand) {
      return 0;
   }

   @Override
   public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
      for (BlockSilverfish.EnumType enumType : BlockSilverfish.EnumType.values()) {
         list.add(new ItemStack(item, 1, enumType.getMetadata()));
      }
   }

   public BlockSilverfish() {
      super(Material.clay);
      this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockSilverfish.EnumType.STONE));
      this.setHardness(0.0F);
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   @Override
   public int getMetaFromState(IBlockState state) {
      return ((BlockSilverfish.EnumType)state.getValue(VARIANT)).getMetadata();
   }

   @Override
   public IBlockState getStateFromMeta(int n) {
      return this.getDefaultState().withProperty(VARIANT, BlockSilverfish.EnumType.byMetadata(n));
   }

   @Override
   public ItemStack createStackedBlock(IBlockState state) {
      switch (BlockSilverfish.EnumTypeSwitchMap.ORDINAL_MAP[((BlockSilverfish.EnumType)state.getValue(VARIANT)).ordinal()]) {
         case 1:
            return new ItemStack(Blocks.cobblestone);
         case 2:
            return new ItemStack(Blocks.stonebrick);
         case 3:
            return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.MOSSY.getMetadata());
         case 4:
            return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CRACKED.getMetadata());
         case 5:
            return new ItemStack(Blocks.stonebrick, 1, BlockStoneBrick.EnumType.CHISELED.getMetadata());
         default:
            return new ItemStack(Blocks.stone);
      }
   }

   @Override
   public BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{VARIANT});
   }

   public static boolean canContainSilverfish(IBlockState state) {
      Block block = state.getBlock();
      return state == Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE)
         || block == Blocks.cobblestone
         || block == Blocks.stonebrick;
   }


   public static class EnumTypeSwitchMap {
      public static final int[] ORDINAL_MAP = new int[BlockSilverfish.EnumType.values().length];

      static {
         try {
            ORDINAL_MAP[BlockSilverfish.EnumType.COBBLESTONE.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }

         try {
            ORDINAL_MAP[BlockSilverfish.EnumType.STONEBRICK.ordinal()] = 2;
         } catch (NoSuchFieldError e2) {
         }

         try {
            ORDINAL_MAP[BlockSilverfish.EnumType.MOSSY_STONEBRICK.ordinal()] = 3;
         } catch (NoSuchFieldError e3) {
         }

         try {
            ORDINAL_MAP[BlockSilverfish.EnumType.CRACKED_STONEBRICK.ordinal()] = 4;
         } catch (NoSuchFieldError e4) {
         }

         try {
            ORDINAL_MAP[BlockSilverfish.EnumType.CHISELED_STONEBRICK.ordinal()] = 5;
         } catch (NoSuchFieldError e5) {
         }
      }
   }

   public enum EnumType implements IStringSerializable {
      STONE(0, "stone") {
         @Override
            public IBlockState getModelBlock() {
               return Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE);
            }
      },
      COBBLESTONE(1, "cobblestone", "cobble") {
         @Override
            public IBlockState getModelBlock() {
               return Blocks.cobblestone.getDefaultState();
            }
      },
      STONEBRICK(2, "stone_brick", "brick") {
         @Override
            public IBlockState getModelBlock() {
               return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT);
            }
      },
      MOSSY_STONEBRICK(3, "mossy_brick", "mossybrick") {
         @Override
            public IBlockState getModelBlock() {
               return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY);
            }
      },
      CRACKED_STONEBRICK(4, "cracked_brick", "crackedbrick") {
         @Override
            public IBlockState getModelBlock() {
               return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED);
            }
      },
      CHISELED_STONEBRICK(5, "chiseled_brick", "chiseledbrick") {
         @Override
            public IBlockState getModelBlock() {
               return Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
            }
      };

      private static final EnumType[] META_LOOKUP = new EnumType[values().length];
      private final int meta;
      private final String name;
      private final String unlocalizedName;
   @Override
      public String toString() {
         return this.name;
      }

      @Override
      public String getName() {
         return this.name;
      }

      EnumType(int n, String str, String str2, BlockSilverfish.EnumTypeSwitchMap obj) {
         this(n, str, str2);
      }

      private EnumType(int n, String str, String str2) {
         this.meta = n;
         this.name = str;
         this.unlocalizedName = str2;
      }

      public static EnumType forModelBlock(IBlockState state) {
         for (EnumType enumType : values()) {
            if (state == enumType.getModelBlock()) {
               return enumType;
            }
         }

         return STONE;
      }

      private EnumType(int n, String str) {
         this(n, str, str);
      }

      public abstract IBlockState getModelBlock();

      public static EnumType byMetadata(int n) {
         if (n < 0 || n >= META_LOOKUP.length) {
            n = 0;
         }

         return META_LOOKUP[n];
      }

      public int getMetadata() {
         return this.meta;
      }

      public String getUnlocalizedName() {
         return this.unlocalizedName;
      }

      static {
         for (EnumType enumType : values()) {
            META_LOOKUP[enumType.getMetadata()] = enumType;
         }
      }

      EnumType(int n, String str, BlockSilverfish.EnumTypeSwitchMap obj) {
         this(n, str);
      }
   }
}
