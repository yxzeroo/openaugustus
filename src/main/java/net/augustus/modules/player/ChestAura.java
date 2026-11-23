package net.augustus.modules.player;

import com.google.common.base.Predicates;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.EntityCollidablePredicate;
import net.augustus.events.listeners.ClickListener;
import net.augustus.events.EventMove;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.events.EventClick;
import net.augustus.settings.RangeSetting;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.rotation.MoveFixMode;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.events.listeners.MoveListener;
import net.augustus.utils.rotation.RotationController;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.utils.rotation.RayTraceUtil;
import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.RayCastListener;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class ChestAura extends Module implements MoveListener, ClickListener, TickListener, MoveInputListener, RayCastListener {
   private final StringValue preRange;
   private final StringValue range;
   private final RangeSetting yawSpeed;
   private final RangeSetting pitchSpeed;
   private final RangeSetting delay;
   private final BooleanValue throughWalls;
   private final BooleanValue throughEntities;
   private final BooleanValue targetNearbyCheck;
   private final BooleanValue notWhileInventoryIsFull;
   private final BooleanValue onlyOnGround;
   private final BooleanValue moveFix;
   private final ArrayList openedChestPositions;
   private final TimeHelper timeHelper;
   private TileEntity targetChest;
   private Vec3 clickedChestPos;
   private WorldClient lastWorld;

   public ChestAura() {
       super("ChestAura", "TruhenAura", new Color(134, 227, 145), Categorys.PLAYER);
      this.preRange = new StringValue(this, "PreRange", 6.0, 0.0, 10.0, 1);
      this.range = new StringValue(this, "Range", 4.5, 4.5, 6.0, 2);
      this.yawSpeed = new RangeSetting(this, "YawSpeed", 30.0, 50.0, 0.0, 180.0, 0);
      this.pitchSpeed = new RangeSetting(this, "PitchSpeed", 30.0, 50.0, 0.0, 180.0, 0);
      this.delay = new RangeSetting(this, "Delay", 200.0, 500.0, 0.0, 1000.0, 0);
      this.throughWalls = new BooleanValue(this, "ThroughWalls", false);
      this.throughEntities = new BooleanValue(this, "ThroughEntities", false);
      this.targetNearbyCheck = new BooleanValue(this, "TargetNearbyCheck", true);
      this.notWhileInventoryIsFull = new BooleanValue(this, "NotWhileInventoryIsFull", false);
      this.onlyOnGround = new BooleanValue(this, "OnlyOnGround", false);
      this.moveFix = new BooleanValue(this, "MoveFix", false);
      this.openedChestPositions = new ArrayList();
      this.timeHelper = new TimeHelper();
      this.targetChest = null;
      this.clickedChestPos = null;
      this.lastWorld = null;
   }

   @Override
   public void onEnable() {
      this.clickedChestPos = null;
      this.targetChest = null;
   }

   @Override
   public void onDisable() {
      this.lastWorld = null;
      this.targetChest = null;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 28;
      this.toggleEventSubscription(b);
      byte b2 = 7;
      this.toggleEventSubscription(b2);
      byte b3 = 40;
      this.toggleEventSubscription(b3);
      byte b4 = 21;
      this.toggleEventSubscription(b4);
      byte b5 = 29;
      this.toggleEventSubscription(b5);
   }

   @Override
   public void onTick() {
      if (this.lastWorld != mc.theWorld) {
         this.openedChestPositions.clear();
      }

      this.lastWorld = mc.theWorld;
      if (mc.currentScreen instanceof GuiChest) {
         this.openedChestPositions.add(this.clickedChestPos);
         this.clickedChestPos = null;
      }

      if (this.targetNearbyCheck.getValue()) {
         Stream stream = mc.theWorld.loadedEntityList.stream();
         Object[] array = stream.filter(
               obj -> {
                  if (obj instanceof EntityPlayer
                     && obj != mc.thePlayer
                     && (double)mc.thePlayer.getDistanceToEntity((Entity)obj) < this.preRange.getValue() + 3.0
                     && !((Entity)obj).isInvisible()
                     && !ClientManagers.moduleManager.teams.isTeammate((Entity)obj)) {
                     if (!ClientManagers.moduleManager.antiBot.isBot((Entity)obj)
                        && (ClientManagers.moduleManager.midClick.autoUnfriendTriggered || !ClientManagers.moduleManager.midClick.isFriend((Entity)obj))) {
                        return true;
                     }
                  }

                  return false;
               }
            )
            .toArray();
         if (array.length > 0) {
            this.targetChest = null;
            return;
         }
      }

      if (!this.onlyOnGround.getValue() || mc.thePlayer.onGround) {
         if (this.notWhileInventoryIsFull.getValue()) {
            boolean flag = true;

            for (int i = 9; i < mc.thePlayer.inventoryContainer.inventorySlots.size(); i++) {
               ItemStack stack = ((Slot)mc.thePlayer.inventoryContainer.inventorySlots.get(i)).getStack();
               if (stack == null) {
                  flag = false;
                  break;
               }
            }

            if (flag) {
               return;
            }
         }

         if (mc.isNotInGui()) {
            Object[] array2 = mc.theWorld
               .loadedTileEntityList
               .stream()
               .filter(obj2 -> this.isValidChest((TileEntity)obj2))
               .sorted(
                  Comparator.comparingDouble(
                     obj3 -> mc.thePlayer.getDistance((double)((TileEntity)obj3).getPos().getX() + 0.5, (double)((TileEntity)obj3).getPos().getY() + 0.5, (double)((TileEntity)obj3).getPos().getZ() + 0.5)
                  )
               )
               .toArray();
            this.targetChest = null;
            if (array2.length > 0 && mc.currentScreen == null) {
               this.targetChest = (TileEntity)array2[0];
               Vec3 vec = new Vec3(
                  (double)this.targetChest.getPos().getX() + 0.5, (double)this.targetChest.getPos().getY() + 0.5, (double)this.targetChest.getPos().getZ() + 0.5
               );
               Vec3 vec2 = null;
               boolean flag2 = false;
               if (this.throughWalls.getValue()) {
                  this.targetChest = (TileEntity)array2[0];
                  vec2 = vec;
               } else {
                  TileEntity targetChest2 = this.targetChest;
                  if (this.canSeeChestPoint(vec, targetChest2)) {
                     this.targetChest = (TileEntity)array2[0];
                     vec2 = vec;
                     flag2 = true;
                  } else {
                     for (int j = 0; j < array2.length; j++) {
                        TileEntity tileEntity = (TileEntity)array2[j];
                        AxisAlignedBB selectedBoundingBox = tileEntity.getBlockType().getSelectedBoundingBox(mc.theWorld, tileEntity.getPos());
                        double d = 15.0;

                        for (double k = selectedBoundingBox.minX; k <= selectedBoundingBox.maxX; k += 0.07) {
                           for (double m = selectedBoundingBox.minZ; m <= selectedBoundingBox.maxZ; m += 0.07) {
                              for (double idx = selectedBoundingBox.minY; idx <= selectedBoundingBox.maxY; idx += 0.07) {
                                 Vec3 vec3 = new Vec3(k, idx, m);
                                 TileEntity targetChest3 = this.targetChest;
                                 if (this.canSeeChestPoint(vec3, targetChest3)) {
                                    Vec3 positionEyes = mc.thePlayer.getPositionEyes(1.0F);
                                    double d2 = Math.sqrt(
                                       Math.pow(k - positionEyes.xCoord, 2.0) + Math.pow(idx - positionEyes.yCoord, 2.0) + Math.pow(m - positionEyes.zCoord, 2.0)
                                    );
                                    if (d2 <= d) {
                                       d = d2;
                                       vec2 = vec3;
                                    }
                                 }
                              }
                           }
                        }

                        if (vec2 != null) {
                           this.targetChest = (TileEntity)array2[j];
                           flag2 = true;
                           break;
                        }
                     }
                  }

                  if (!flag2) {
                     this.targetChest = null;
                  }
               }

               if (vec2 != null) {
                  RotationController rotationController = ClientManagers.rotationController;
                  double xCoord = vec2.xCoord;
                  double yCoord = vec2.yCoord;
                  double zCoord = vec2.zCoord;
                  Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
                  RandomNumberGenerator instance = DefaultRandomGenerator.instance;
                  double minValue = this.yawSpeed.getMinValue();
                  double maxValue = this.yawSpeed.getMaxValue();
                  double d3 = minValue;
                  float f = instance.nextSecureFloat(d3, maxValue);
                  RandomNumberGenerator instance2 = DefaultRandomGenerator.instance;
                  double minValue2 = this.pitchSpeed.getMinValue();
                  double maxValue2 = this.pitchSpeed.getMaxValue();
                  double d4 = minValue2;
                  float f2 = instance2.nextSecureFloat(d4, maxValue2);
                  boolean flag3 = true;
                  float f3 = f2;
                  float f4 = f;
                  Rotation rotationsToPos = RotationUtil.getRotationsToPos(xCoord, yCoord, zCoord, previousRotation, f4, f3, flag3);
                  Rotation previousRotation2 = ClientManagers.rotationController.getPreviousRotation();
                  Rotation rotation = RotationUtil.applyGcdFix(rotationsToPos, previousRotation2);
                  byte b = 4;
                  Rotation rotation2 = rotation;
                  rotationController.requestRotation(rotation2, b, this);
               }
            }
         }
      }
   }

   @Override
   public void onClick(EventClick event) {
      if (ClientManagers.rotationController.isOwnedBy(this)
         && mc.currentScreen == null
         && this.targetChest != null
         && mc.objectMouseOver != null
         && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK
         && mc.objectMouseOver.getBlockPos() != null
         && mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock().equals(mc.theWorld.getBlockState(this.targetChest.getPos()).getBlock())) {
         TimeHelper timeHelper2 = this.timeHelper;
         RandomNumberGenerator instance = DefaultRandomGenerator.instance;
         double minValue = this.delay.getMinValue();
         double maxValue = this.delay.getMaxValue();
         double d = minValue;
         double d2 = instance.nextSecureIntAsDouble(d, maxValue);
         if (timeHelper2.reached(d2)) {
            byte b = 1;
            event.addRightClicks(b);
            this.clickedChestPos = new Vec3((double)this.targetChest.getPos().getX() + 0.5, (double)this.targetChest.getPos().getY() + 0.5, (double)this.targetChest.getPos().getZ() + 0.5);
            this.timeHelper.reset();
         }
      } else {
         this.timeHelper.reset();
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (this.moveFix.getValue() && ClientManagers.rotationController.isOwnedBy(this)) {
         MoveFixMode moveFixMode = MoveFixMode.DEFAULT;
         event.setMoveFixMode(moveFixMode);
      }
   }

   @Override
   public void onMove(EventMove event) {
      if (!this.moveFix.getValue() && ClientManagers.rotationController.isOwnedBy(this)) {
         float movementYaw = MoveUtil.getMovementYaw();
         event.setYaw(movementYaw);
      }
   }

   @Override
   public void onMove2(EventMove event) {
      if (!this.moveFix.getValue() && ClientManagers.rotationController.isOwnedBy(this)) {
         float movementYaw = MoveUtil.getMovementYaw();
         event.setYaw(movementYaw);
      }
   }

   @Override
   public void onRayCast() {
      if (this.targetChest != null && ClientManagers.rotationController.isOwnedBy(this)) {
         float f = 1.0F;
         this.updateMouseOverChest(f);
      }
   }

   private void updateMouseOverChest(float f) {
      Entity renderViewEntity = mc.getRenderViewEntity();
      if (renderViewEntity != null && mc.theWorld != null) {
         Object obj = null;
         mc.pointedEntity = (Entity)obj;
         double blockReachDistance = (double)mc.playerController.getBlockReachDistance();
         blockReachDistance = Math.max(blockReachDistance, this.range.getValue());
         if (this.throughWalls.getValue()) {
            Minecraft mc2 = mc;
            BlockPos pos = this.targetChest.getPos();
            float rotationYaw = mc.thePlayer.rotationYaw;
            float rotationPitch = mc.thePlayer.rotationPitch;
            double value = this.range.getValue();
            float f2 = rotationPitch;
            float f3 = rotationYaw;
            MovingObjectPosition hit = RayTraceUtil.rayTraceBlock(pos, f3, f2, value);
            mc2.objectMouseOver = hit;
            if (mc.objectMouseOver != null) {
               mc2 = mc;
               MovingObjectPosition hit2 = new MovingObjectPosition(mc.objectMouseOver.hitVec, mc.objectMouseOver.sideHit, this.targetChest.getPos());
               mc2.objectMouseOver = hit2;
            } else {
               mc2 = mc;
               MovingObjectPosition hit3 = renderViewEntity.rayTrace(blockReachDistance, f);
               mc2.objectMouseOver = hit3;
            }
         } else {
            Minecraft mc3 = mc;
            MovingObjectPosition hit4 = renderViewEntity.rayTrace(blockReachDistance, f);
            mc3.objectMouseOver = hit4;
         }

         double d = blockReachDistance;
         Vec3 positionEyes = renderViewEntity.getPositionEyes(f);
         boolean flag = false;
         if (blockReachDistance > 3.0) {
            flag = true;
         }

         if (mc.objectMouseOver != null) {
            d = mc.objectMouseOver.hitVec.distanceTo(positionEyes);
         }

         if (this.throughEntities.getValue()) {
            return;
         }

         Vec3 look = renderViewEntity.getLook(f);
         Vec3 vec = positionEyes.addVector(look.xCoord * blockReachDistance, look.yCoord * blockReachDistance, look.zCoord * blockReachDistance);
         mc.entityRenderer.pointedEntity = null;
         Vec3 vec2 = null;
         float f4 = 1.0F;
         List list = mc.theWorld
            .getEntitiesInAABBexcluding(
               renderViewEntity,
               renderViewEntity.getEntityBoundingBox()
                  .addCoord(look.xCoord * blockReachDistance, look.yCoord * blockReachDistance, look.zCoord * blockReachDistance)
                  .expand((double)f4, (double)f4, (double)f4),
               Predicates.and(EntitySelectors.NOT_SPECTATING, new EntityCollidablePredicate(mc.entityRenderer))
            );
         double d2 = d;

         for (int i = 0; i < list.size(); i++) {
            Entity entity = (Entity)list.get(i);
            float collisionBorderSize = entity.getCollisionBorderSize();
            AxisAlignedBB box = entity.getEntityBoundingBox().expand((double)collisionBorderSize, (double)collisionBorderSize, (double)collisionBorderSize);
            MovingObjectPosition hit5 = box.calculateIntercept(positionEyes, vec);
            if (box.isVecInside(positionEyes)) {
               if (d2 >= 0.0) {
                  mc.entityRenderer.pointedEntity = entity;
                  vec2 = hit5 == null ? positionEyes : hit5.hitVec;
                  d2 = 0.0;
               }
            } else if (hit5 != null) {
               double d3 = positionEyes.distanceTo(hit5.hitVec);
               if (d3 < d2 || d2 == 0.0) {
                  boolean flag2 = false;
                  if (entity != renderViewEntity.ridingEntity || flag2) {
                     mc.entityRenderer.pointedEntity = entity;
                     vec2 = hit5.hitVec;
                     d2 = d3;
                  } else if (d2 == 0.0) {
                     mc.entityRenderer.pointedEntity = entity;
                     vec2 = hit5.hitVec;
                  }
               }
            }
         }

         if (mc.entityRenderer.pointedEntity != null && flag && positionEyes.distanceTo(vec2) > 3.0) {
            mc.entityRenderer.pointedEntity = null;
            Minecraft mc4 = mc;
            MovingObjectPosition hit6 = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec2, (EnumFacing)null, new BlockPos(vec2));
            mc4.objectMouseOver = hit6;
         }

         if (mc.entityRenderer.pointedEntity != null && (d2 < d || mc.objectMouseOver == null)) {
            Minecraft mc5 = mc;
            MovingObjectPosition hit7 = new MovingObjectPosition(mc.entityRenderer.pointedEntity, vec2);
            mc5.objectMouseOver = hit7;
            if (mc.entityRenderer.pointedEntity instanceof EntityLivingBase || mc.entityRenderer.pointedEntity instanceof EntityItemFrame) {
               Entity pointedEntity = mc.entityRenderer.pointedEntity;
               mc.pointedEntity = pointedEntity;
            }
         }
      }
   }

   private boolean canSeeChestPoint(Vec3 vec, TileEntity tileEntity) {
      MovingObjectPosition hit = mc.theWorld
         .rayTraceBlocks(mc.thePlayer.posX, mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ, vec.xCoord, vec.yCoord, vec.zCoord, false, false, true);
      return hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && hit.getBlockPos().equalsPos(tileEntity.getPos());
   }

   private boolean isValidChest(TileEntity tileEntity) {
      if (tileEntity instanceof TileEntityChest || tileEntity instanceof TileEntityEnderChest) {
         Vec3 vec = new Vec3((double)tileEntity.getPos().getX() + 0.5, (double)tileEntity.getPos().getY() + 0.5, (double)tileEntity.getPos().getZ() + 0.5);
         if (mc.thePlayer.getDistance(vec.xCoord, vec.yCoord, vec.zCoord) < this.preRange.getValue()) {
            for (Vec3 vec2 : (Iterable<Vec3>)(this.openedChestPositions)) {
               if (vec2 != null && vec.xCoord == vec2.xCoord && vec.yCoord == vec2.yCoord && vec.zCoord == vec2.zCoord) {
                  return false;
               }
            }

            Block block = mc.theWorld.getBlockState(tileEntity.getPos().up()).getBlock();
            if (block.isFullBlock()) {
               return false;
            }

            return true;
         }
      }

      return false;
   }

}
