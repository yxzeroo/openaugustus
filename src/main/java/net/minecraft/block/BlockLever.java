package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLever extends Block {
   public static final PropertyEnum FACING = PropertyEnum.create("facing", BlockLever.EnumOrientation.class);
   public static final PropertyBool POWERED = PropertyBool.create("powered");

   @Override
   public boolean isFullCube() {
      return false;
   }

   public static int getMetadataForFacing(EnumFacing facing) {
      switch (BlockLever.AxisSwitchMap.FACING_LOOKUP[facing.ordinal()]) {
         case 1:
            return 0;
         case 2:
            return 5;
         case 3:
            return 4;
         case 4:
            return 3;
         case 5:
            return 2;
         case 6:
            return 1;
         default:
            return -1;
      }
   }

   @Override
   public BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING, POWERED});
   }

   @Override
   public boolean canPlaceBlockAt(World world, BlockPos pos) {
      for (EnumFacing facing : EnumFacing.getValues()) {
         if (canAttachTo(world, pos, facing)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block) {
      if (this.checkCanSurvive(world, pos, state) && !canAttachTo(world, pos, ((BlockLever.EnumOrientation)state.getValue(FACING)).getFacing().getOpposite())) {
         this.dropBlockAsItem(world, pos, state, 0);
         world.setBlockToAir(pos);
      }
   }

   @Override
   public boolean hasCollisionBox() {
      return false;
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBox(World world, IBlockState state, int n, int n2, int n3) {
      return null;
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess world, int n, int n2, int n3) {
      float f = 0.1875F;
      switch (BlockLever.AxisSwitchMap.ORIENTATION_LOOKUP[((BlockLever.EnumOrientation)world.getBlockState2(n, n2, n3).getValue(FACING)).ordinal()]) {
         case 1:
            this.setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
            break;
         case 2:
            this.setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
            break;
         case 3:
            this.setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
            break;
         case 4:
            this.setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
            break;
         case 5:
         case 6:
            f = 0.25F;
            this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
            break;
         case 7:
         case 8:
            f = 0.25F;
            this.setBlockBounds(0.5F - f, 0.4F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
      }
   }

   public static boolean canAttachTo(World world, BlockPos pos, EnumFacing facing) {
      return BlockButton.canPlaceBlock(world, pos, facing);
   }

   private boolean checkCanSurvive(World world, BlockPos pos, IBlockState state) {
      if (this.canPlaceBlockAt(world, pos)) {
         return true;
      } else {
         this.dropBlockAsItem(world, pos, state, 0);
         world.setBlockToAir(pos);
         return false;
      }
   }

   @Override
   public boolean canProvidePower() {
      return true;
   }

   @Override
   public int getWeakPower(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing facing) {
      return (Boolean)state.getValue(POWERED) ? 15 : 0;
   }

   @Override
   public int getStrongPower(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing facing) {
      return !(Boolean)state.getValue(POWERED) ? 0 : (((BlockLever.EnumOrientation)state.getValue(FACING)).getFacing() == facing ? 15 : 0);
   }

   @Override
   public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float f, float f2, float f3) {
      if (!world.isRemote) {
         state = state.cycleProperty(POWERED);
         world.setBlockState(pos, state, 3);
         world.playSoundEffect(
            (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "random.click", 0.3F, (Boolean)state.getValue(POWERED) ? 0.6F : 0.5F
         );
         world.notifyNeighborsOfStateChange(pos, this);
         world.notifyNeighborsOfStateChange(pos.offset(((BlockLever.EnumOrientation)state.getValue(FACING)).getFacing().getOpposite()), this);
      }

      return true;
   }

   @Override
   public IBlockState getStateFromMeta(int n) {
      return this.getDefaultState().withProperty(FACING, BlockLever.EnumOrientation.byMetadata(n & 7)).withProperty(POWERED, (n & 8) > 0);
   }

   @Override
   public void breakBlock(World world, BlockPos pos, IBlockState state) {
      if ((Boolean)state.getValue(POWERED)) {
         world.notifyNeighborsOfStateChange(pos, this);
         world.notifyNeighborsOfStateChange(pos.offset(((BlockLever.EnumOrientation)state.getValue(FACING)).getFacing().getOpposite()), this);
      }

      super.breakBlock(world, pos, state);
   }

   @Override
   public boolean canBlockBeActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float f, float f2, float f3) {
      return true;
   }

   public BlockLever() {
      super(Material.circuits);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, BlockLever.EnumOrientation.NORTH).withProperty(POWERED, Boolean.FALSE));
      this.setCreativeTab(CreativeTabs.tabRedstone);
   }

   @Override
   public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float f, float f2, float f3, int n, EntityLivingBase entity) {
      IBlockState state = this.getDefaultState().withProperty(POWERED, Boolean.FALSE);
      if (canAttachTo(world, pos, facing.getOpposite())) {
         return state.withProperty(FACING, BlockLever.EnumOrientation.forFacings(facing, entity.getHorizontalFacing()));
      } else {
         for (EnumFacing facing2 : (Iterable<EnumFacing>)(EnumFacing.Plane.HORIZONTAL)) {
            if (facing2 != facing && canAttachTo(world, pos, facing2.getOpposite())) {
               return state.withProperty(FACING, BlockLever.EnumOrientation.forFacings(facing2, entity.getHorizontalFacing()));
            }
         }

         return World.doesBlockHaveSolidTopSurface(world, pos.getX(), pos.getY() - 1, pos.getZ())
            ? state.withProperty(FACING, BlockLever.EnumOrientation.forFacings(EnumFacing.UP, entity.getHorizontalFacing()))
            : state;
      }
   }

   @Override
   public int getMetaFromState(IBlockState state) {
      int n = 0;
      n |= ((BlockLever.EnumOrientation)state.getValue(FACING)).getMetadata();
      if ((Boolean)state.getValue(POWERED)) {
         n |= 8;
      }

      return n;
   }

   @Override
   public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing facing) {
      return canAttachTo(world, pos, facing.getOpposite());
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }


   public static class AxisSwitchMap {
      public static final int[] FACING_LOOKUP;
      public static final int[] ORIENTATION_LOOKUP;
      public static final int[] AXIS_LOOKUP = new int[EnumFacing.Axis.values().length];

      static {
         try {
            AXIS_LOOKUP[EnumFacing.Axis.X.ordinal()] = 1;
         } catch (NoSuchFieldError e) {
         }

         try {
            AXIS_LOOKUP[EnumFacing.Axis.Z.ordinal()] = 2;
         } catch (NoSuchFieldError e2) {
         }

         ORIENTATION_LOOKUP = new int[BlockLever.EnumOrientation.values().length];

         try {
            ORIENTATION_LOOKUP[BlockLever.EnumOrientation.EAST.ordinal()] = 1;
         } catch (NoSuchFieldError e3) {
         }

         try {
            ORIENTATION_LOOKUP[BlockLever.EnumOrientation.WEST.ordinal()] = 2;
         } catch (NoSuchFieldError e4) {
         }

         try {
            ORIENTATION_LOOKUP[BlockLever.EnumOrientation.SOUTH.ordinal()] = 3;
         } catch (NoSuchFieldError e5) {
         }

         try {
            ORIENTATION_LOOKUP[BlockLever.EnumOrientation.NORTH.ordinal()] = 4;
         } catch (NoSuchFieldError e6) {
         }

         try {
            ORIENTATION_LOOKUP[BlockLever.EnumOrientation.UP_Z.ordinal()] = 5;
         } catch (NoSuchFieldError e7) {
         }

         try {
            ORIENTATION_LOOKUP[BlockLever.EnumOrientation.UP_X.ordinal()] = 6;
         } catch (NoSuchFieldError e8) {
         }

         try {
            ORIENTATION_LOOKUP[BlockLever.EnumOrientation.DOWN_X.ordinal()] = 7;
         } catch (NoSuchFieldError e9) {
         }

         try {
            ORIENTATION_LOOKUP[BlockLever.EnumOrientation.DOWN_Z.ordinal()] = 8;
         } catch (NoSuchFieldError e10) {
         }

         FACING_LOOKUP = new int[EnumFacing.values().length];

         try {
            FACING_LOOKUP[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError e11) {
         }

         try {
            FACING_LOOKUP[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError e12) {
         }

         try {
            FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError e13) {
         }

         try {
            FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError e14) {
         }

         try {
            FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError e15) {
         }

         try {
            FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError e16) {
         }
      }
   }

   public enum EnumOrientation implements IStringSerializable {
      DOWN_X(0, "down_x", EnumFacing.DOWN),
      EAST(1, "east", EnumFacing.EAST),
      WEST(2, "west", EnumFacing.WEST),
      SOUTH(3, "south", EnumFacing.SOUTH),
      NORTH(4, "north", EnumFacing.NORTH),
      UP_Z(5, "up_z", EnumFacing.UP),
      UP_X(6, "up_x", EnumFacing.UP),
      DOWN_Z(7, "down_z", EnumFacing.DOWN);

   private static final EnumOrientation[] META_LOOKUP = new EnumOrientation[values().length];
      private final int meta;
      private final String name;
      private final EnumFacing facing;
   public static EnumOrientation byMetadata(int n) {
         if (n < 0 || n >= META_LOOKUP.length) {
            n = 0;
         }

         return META_LOOKUP[n];
      }

      public static EnumOrientation forFacings(EnumFacing facing2, EnumFacing facing3) {
         switch (BlockLever.AxisSwitchMap.FACING_LOOKUP[facing2.ordinal()]) {
            case 1:
               switch (BlockLever.AxisSwitchMap.AXIS_LOOKUP[facing3.getAxis().ordinal()]) {
                  case 1:
                     return DOWN_X;
                  case 2:
                     return DOWN_Z;
                  default:
                     throw new IllegalArgumentException("Invalid entityFacing " + facing3 + " for facing " + facing2);
               }
            case 2:
               switch (BlockLever.AxisSwitchMap.AXIS_LOOKUP[facing3.getAxis().ordinal()]) {
                  case 1:
                     return UP_X;
                  case 2:
                     return UP_Z;
                  default:
                     throw new IllegalArgumentException("Invalid entityFacing " + facing3 + " for facing " + facing2);
               }
            case 3:
               return NORTH;
            case 4:
               return SOUTH;
            case 5:
               return WEST;
            case 6:
               return EAST;
            default:
               throw new IllegalArgumentException("Invalid facing: " + facing2);
         }
      }

      @Override
      public String toString() {
         return this.name;
      }

      @Override
      public String getName() {
         return this.name;
      }

      private EnumOrientation(int n, String str, EnumFacing facing2) {
         this.meta = n;
         this.name = str;
         this.facing = facing2;
      }

      static {
         for (EnumOrientation enumOrientation : values()) {
            META_LOOKUP[enumOrientation.getMetadata()] = enumOrientation;
         }
      }

      public EnumFacing getFacing() {
         return this.facing;
      }

      public int getMetadata() {
         return this.meta;
      }
   }
}
