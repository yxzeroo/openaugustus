package net.augustus.modules.modes.velocity;

import net.augustus.events.EventBlockBoundingBox;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.BlockBoundingBoxListener;
import net.augustus.events.listeners.PacketListener;
import net.augustus.events.listeners.RenderListener;
import net.augustus.render.RenderUtil;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.MoveUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Velocity;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class FenceVelocityMode extends VelocityMode implements BlockBoundingBoxListener, PacketListener, RenderListener {
   private List virtualFencePositions;
   private Map virtualFenceBoxes;
   private final StringValue fenceExtraStrength;
   private final BooleanValue fenceOnlyWhileMoving;
   private final BooleanValue fenceVertical;

   public FenceVelocityMode(Velocity velocity, DoubleValue doubleValue) {
       super(velocity, doubleValue, "Fence");
      this.virtualFencePositions = new ArrayList();
      this.virtualFenceBoxes = new HashMap();
      StringValue stringValue = new StringValue(this.parentModule, "FenceExtraStrength", 0.075, 0.0, 0.075, 3, this.modeSetting);
      String str = "Extra Strength";
      StringValue stringValue2 = stringValue.named(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.fenceExtraStrength = stringValue2.visibleWhen(condition);
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "FenceOnlyWhileMoving", false, this.modeSetting);
      String str2 = "Only While Moving";
      BooleanValue booleanValue2 = (BooleanValue)booleanValue.withDisplayName(str2);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.fenceOnlyWhileMoving = (BooleanValue)booleanValue2.withVisibility(condition2);
      BooleanValue booleanValue3 = new BooleanValue(this.parentModule, "FenceVertical", false, this.modeSetting);
      String str3 = "Vertical";
      BooleanValue booleanValue4 = (BooleanValue)booleanValue3.withDisplayName(str3);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.fenceVertical = (BooleanValue)booleanValue4.withVisibility(condition3);
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new FenceVelocityMode((Velocity)obj, (DoubleValue)obj2);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 3;
      this.toggleEventSubscription(b);
      byte b2 = 26;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onPacket(EventPacket event) {
      if (this.canApplyVelocity() && !event.isCancelled() && mc.thePlayer != null && mc.theWorld != null) {
         Packet packet = event.getPacket();
         if (packet instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet2 = (S12PacketEntityVelocity)packet;
            if (packet2.getEntityID() == mc.thePlayer.getEntityId() && (!this.fenceOnlyWhileMoving.getValue() || MoveUtil.isMoving())) {
               Vec3 vec = new Vec3((double)packet2.getMotionX(), (double)packet2.getMotionY(), (double)packet2.getMotionZ());
               boolean flag = true;
               double value = this.fenceExtraStrength.getValue(flag);
               BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.lastGroundY, mc.thePlayer.posZ);
               double d = mc.thePlayer.posX % 1.0;
               double d2 = mc.thePlayer.posZ % 1.0;
               if (vec.xCoord > 0.0) {
                  if (d < 0.0 && Math.abs(d) > 1.0 - value) {
                     pos = pos.west();
                  } else if (d > 0.0 && Math.abs(d) < value) {
                     pos = pos.west();
                  }
               } else if (d < 0.0 && Math.abs(d) < value) {
                  pos = pos.east();
               } else if (d > 0.0 && Math.abs(d) > 1.0 - value) {
                  pos = pos.east();
               }

               if (vec.zCoord > 0.0) {
                  if (d2 < 0.0 && Math.abs(d2) > 1.0 - value) {
                     pos = pos.north();
                  } else if (d2 > 0.0 && Math.abs(d2) < value) {
                     pos = pos.north();
                  }
               } else if (d2 < 0.0 && Math.abs(d2) < value) {
                  pos = pos.south();
               } else if (d2 > 0.0 && Math.abs(d2) > 1.0 - value) {
                  pos = pos.south();
               }

               this.virtualFencePositions = this.computeFencePositions(pos, vec);
               this.virtualFenceBoxes.clear();

               for (BlockPos pos2 : (Iterable<BlockPos>)(this.virtualFencePositions)) {
                  this.buildFenceBoundingBox(pos2);
               }
            }
         }
      }
   }

   @Override
   public void onBlockBoundingBox(EventBlockBoundingBox event) {
      if (mc.thePlayer != null) {
         if (mc.thePlayer.hurtTime != 0) {
            AxisAlignedBB box;
            if ((box = (AxisAlignedBB)this.virtualFenceBoxes.get(event.getBlockPos())) != null) {
               event.setBoundingBox(box);
            }
         } else {
            this.virtualFenceBoxes.clear();
         }
      }
   }

   @Override
   public void onRender(float f) {
      RenderUtil.begin3DRender();
      GlStateManager.translate(-mc.getRenderManager().getRenderPosX(), -mc.getRenderManager().getRenderPosY(), -mc.getRenderManager().getRenderPosZ());

      for (AxisAlignedBB box : (Iterable<AxisAlignedBB>)(this.virtualFenceBoxes.values())) {
         RenderUtil.setGlColorRgba(255, 0, 0, 200);
         RenderUtil.drawFilledBox(box);
      }

      GlStateManager.translate(mc.getRenderManager().getRenderPosX(), mc.getRenderManager().getRenderPosY(), mc.getRenderManager().getRenderPosZ());
      RenderUtil.end3DRender();
   }

   public void buildFenceBoundingBox(BlockPos pos) {
      int x = pos.getX();
      int n = pos.getZ() - 1;
      int y = pos.getY();
      int n2 = x;
      boolean flag = this.hasFenceAt(n2, y, n);
      x = pos.getX();
      int n3 = pos.getZ() + 1;
      int y2 = pos.getY();
      int n4 = x;
      boolean flag2 = this.hasFenceAt(n4, y2, n3);
      int n5 = pos.getX() - 1;
      int z = pos.getZ();
      int y3 = pos.getY();
      int n6 = n5;
      boolean flag3 = this.hasFenceAt(n6, y3, z);
      n5 = pos.getX() + 1;
      int z2 = pos.getZ();
      int y4 = pos.getY();
      int n7 = n5;
      boolean flag4 = this.hasFenceAt(n7, y4, z2);
      float f = 0.375F;
      float f2 = 0.625F;
      float f3 = 0.375F;
      float f4 = 0.625F;
      if (flag) {
         f3 = 0.0F;
      }

      if (flag2) {
         f4 = 1.0F;
      }

      if (flag || flag2) {
         double d = (double)f;
         double d2 = (double)f3;
         double d3 = (double)f2;
         double d4 = (double)f4;
         double d5 = 1.5;
         double d6 = d3;
         double d7 = d2;
         double d8 = 0.0;
         double d9 = d;
         this.putFenceBoundingBox(pos, d9, d8, d7, d6, d5, d4);
      }

      f3 = 0.375F;
      f4 = 0.625F;
      if (flag3) {
         f = 0.0F;
      }

      if (flag4) {
         f2 = 1.0F;
      }

      if (flag3 || flag4 || !flag && !flag2) {
         double d10 = (double)f;
         double d11 = (double)f3;
         double d12 = (double)f2;
         double d13 = (double)f4;
         double d14 = 1.5;
         double d15 = d12;
         double d16 = d11;
         double d17 = 0.0;
         double d18 = d10;
         this.putFenceBoundingBox(pos, d18, d17, d16, d15, d14, d13);
      }

      if (flag) {
         f3 = 0.0F;
      }

      if (flag2) {
         f4 = 1.0F;
      }
   }

   public boolean hasFenceAt(int n, int n2, int n3) {
      return this.virtualFencePositions.contains(new BlockPos(n, n2, n3));
   }

   private AxisAlignedBB putFenceBoundingBox(BlockPos pos, double d, double d2, double d3, double d4, double d5, double d6) {
      return (AxisAlignedBB)this.virtualFenceBoxes
         .put(
            pos,
            new AxisAlignedBB(
               (double)pos.getX() + d,
               (double)pos.getY() + d2,
               (double)pos.getZ() + d3,
               (double)pos.getX() + d4,
               (double)pos.getY() + d5,
               (double)pos.getZ() + d6
            )
         );
   }

   private boolean isAirBlock(BlockPos pos) {
      IBlockState blockState = mc.theWorld.getBlockState(pos);
      Block block = blockState.getBlock();
      return block.getMaterial() == Material.air;
   }

   public List computeFencePositions(BlockPos pos, Vec3 vec) {
      ArrayList list = new ArrayList();
      Vec3 vec2 = vec.normalize();
      double d = 0.0;
      byte b = 0;
      byte b2 = 0;
      if (vec2.xCoord > d) {
         b = 1;
      } else if (vec2.xCoord < -d) {
         b = -1;
      }

      if (vec2.zCoord > d) {
         b2 = 1;
      } else if (vec2.zCoord < -d) {
         b2 = -1;
      }

      if (b == 0 && b2 == 0) {
         return list;
      } else {
         boolean flag = b != 0;
         boolean flag2 = b2 != 0;
         if (flag && flag2) {
            this.addAirColumn(b, b2, pos, list);
         }

         if (flag) {
            byte b3 = 0;
            this.addAirColumn(b, b3, pos, list);
         }

         if (flag2) {
            byte b4 = 0;
            this.addAirColumn(b4, b2, pos, list);
         }

         if (this.fenceVertical.getValue()) {
            BlockPos pos2 = new BlockPos((double)pos.getX(), mc.thePlayer.posY + 2.0, (double)pos.getZ());
            if (this.isAirBlock(pos2)) {
               list.add(pos2);
            }
         }

         return list;
      }
   }

   private void addAirColumn(int n, int n2, BlockPos pos, List list) {

      for (int i = 0; i < 3; i++) {
         BlockPos pos2 = pos.add(n, i, n2);
         if (this.isAirBlock(pos2)) {
            list.add(pos2);
         }
      }
   }

}
