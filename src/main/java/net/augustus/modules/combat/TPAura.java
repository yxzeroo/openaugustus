package net.augustus.modules.combat;

import net.minecraft.network.play.client.C03PacketPlayer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.RenderListener;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.utils.simulation.EntityMovementPredictor;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.settings.MultiOptionSetting;
import net.augustus.utils.pathfinding.PathNode;
import net.augustus.settings.options.TPAuraTargetOption;
import net.augustus.utils.pathfinding.AStarPathFinder;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.utils.rotation.EntityBoundsUtil;
import net.augustus.utils.combat.CombatTargetUtil;
import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.TickListener;
import net.augustus.utils.pathfinding.PathBlockPos;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class TPAura extends Module implements TickListener, RenderListener, MoveInputListener {
   public final StringValue maxDistance;
   public final StringValue maxTeleportDistance;
   public final StringValue attacksPerSecond;
   public final BooleanValue noswing;
   public final BooleanValue fly;
   public final DoubleValue target;
   public final MultiOptionSetting targets;
   public final StringValue maxTargets;
   public final StringValue minhurttime;
   public final BooleanValue debuglines;
   public final BooleanValue legit;
   public final BooleanValue groundSpoof;
   public final TimeHelper timeHelper;
   public ArrayList pathNodes;
   public ArrayList teleportPositions;
   public ArrayList blockedRaySegments;
   private Rotation currentRotation;
   public boolean teleporting;
   public boolean aimingAtTarget;

   public TPAura() {
       super("TPAura", "Ferntötung mittels Teleportation", Color.RED, Categorys.COMBAT);
      this.maxDistance = new StringValue(this, "MaxDistance", 50.0, 3.0, 150.0, 0);
      this.maxTeleportDistance = new StringValue(this, "MaxTeleportDistance", 5.0, 2.0, 9.0, 0);
      this.attacksPerSecond = new StringValue(this, "AttacksPerSecond", 2.0, 1.0, 20.0, 0);
      this.noswing = new BooleanValue(this, "Noswing", false);
      this.fly = new BooleanValue(this, "Fly", true);
      this.target = new DoubleValue(this, "Target");
      this.targets = new MultiOptionSetting(this, "Targets", TPAuraTargetOption.values(), this.target);
      this.maxTargets = new StringValue(this, "MaxTargets", 1.0, 1.0, 10.0, 0, this.target);
      this.minhurttime = new StringValue(this, "Minhurttime", 3.0, 0.0, 10.0, 0, this.target);
      this.debuglines = new BooleanValue(this, "Debuglines", false);
      this.legit = new BooleanValue(this, "Legit", false);
      this.groundSpoof = new BooleanValue(this, "GroundSpoof", false);
      this.timeHelper = new TimeHelper();
      this.blockedRaySegments = new ArrayList();
      this.currentRotation = new Rotation();
      this.teleporting = false;
      this.aimingAtTarget = false;
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
      byte b2 = 32;
      this.toggleEventSubscription(b2);
      byte b3 = 21;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onTick() {
      this.currentRotation = ClientManagers.rotationController.getPreviousRotation();
      TimeHelper timeHelper2 = this.timeHelper;
      double d = 1000.0 / this.attacksPerSecond.getValue();
      if (timeHelper2.reached(d)) {
         this.timeHelper.reset();
         this.teleporting = true;
         double d2 = this.maxDistance.getValue() * this.maxDistance.getValue();
         ArrayList list = new ArrayList();

         for (Entity entity : (Iterable<Entity>)(mc.theWorld.loadedEntityList)) {
            if (entity != mc.thePlayer && entity instanceof EntityLivingBase) {
               EntityLivingBase entity2 = (EntityLivingBase)entity;
               if (!(entity.getDistanceSqToEntity(mc.thePlayer) > d2) && !((double)entity2.hurtTime > this.minhurttime.getValue()) && !(entity instanceof EntityPlayerSP)
                  )
                {
                  boolean isSelected = TPAuraTargetOption.PLAYER.isSelected();
                  boolean isSelected2 = TPAuraTargetOption.MOB.isSelected();
                  boolean isSelected3 = TPAuraTargetOption.ANIMAL.isSelected();
                  boolean isSelected4 = TPAuraTargetOption.VILLAGER.isSelected();
                  boolean isSelected5 = TPAuraTargetOption.INVISIBLE.isSelected();
                  boolean isSelected6 = TPAuraTargetOption.DEAD.isSelected();
                  boolean flag = true;
                  boolean flag2 = isSelected6;
                  boolean flag3 = true;
                  boolean flag4 = true;
                  boolean flag5 = true;
                  boolean flag6 = isSelected5;
                  boolean flag7 = false;
                  boolean flag8 = isSelected4;
                  boolean flag9 = isSelected3;
                  boolean flag10 = isSelected2;
                  boolean flag11 = isSelected;
                  if (CombatTargetUtil.isValidTarget(entity, flag11, flag10, flag9, flag8, flag7, flag6, flag5, flag4, flag3, flag2, flag)) {
                     list.add(entity);
                  }
               }
            }
         }

         list.sort(Comparator.comparingDouble(obj -> ((Entity)obj).getDistanceSqToEntity(mc.thePlayer)));
         int n = 0;
         if (!list.isEmpty()) {
            Vec3 interpolatedPos = mc.thePlayer.getInterpolatedPos(1.0F);
            Vec3 interpolatedPos2 = mc.thePlayer.getInterpolatedPos(1.0F);
            PathBlockPos pathBlockPos = new PathBlockPos((int)Math.floor(interpolatedPos2.xCoord), (int)Math.floor(interpolatedPos2.yCoord), (int)Math.floor(interpolatedPos2.zCoord));
            if (mc.thePlayer.collidedYBoundingBox != null) {
               pathBlockPos = new PathBlockPos((int)mc.thePlayer.collidedYBoundingBox.minX, (int)mc.thePlayer.collidedYBoundingBox.maxY, (int)mc.thePlayer.collidedYBoundingBox.minZ);
            }

            ArrayList list2 = new ArrayList();
            this.pathNodes = new ArrayList();
            this.teleportPositions = new ArrayList();
            this.blockedRaySegments = new ArrayList();
            this.teleportPositions.add(mc.thePlayer.getInterpolatedPos(1.0F));
            int n2 = 100;
            boolean value = this.fly.getValue();
            long l = (long)(100.0 / this.maxTargets.getValue());
            float[] floats = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};

            while (n2 > 0) {
               ArrayList list3 = new ArrayList();

               for (Entity entity3 : (Iterable<Entity>)(list)) {
                  Vec3 interpolatedPos3 = entity3.getInterpolatedPos(1.0F);
                  PathBlockPos pathBlockPos2 = new PathBlockPos((int)Math.floor(interpolatedPos3.xCoord), (int)Math.floor(interpolatedPos3.yCoord), (int)Math.floor(interpolatedPos3.zCoord));
                  AStarPathFinder aStarPathFinder = new AStarPathFinder(pathBlockPos, pathBlockPos2, false, false, value);
                  aStarPathFinder.allowDiagonals = false;
                  boolean flag12 = true;
                  ArrayList list4 = aStarPathFinder.computePath(flag12, l);
                  list3.add(entity3);
                  if (list4 != null && !list4.isEmpty()) {
                     Collections.reverse(list4);
                     int n3 = 0;
                     this.pathNodes.addAll(list4);

                     Vec3 vec;
                     do {
                        int n4 = n3 + 1;
                        n3 = this.findFarthestReachableNode(list4, interpolatedPos2, n4);
                        PathBlockPos pos = ((PathNode)list4.get(n3)).getPos();
                        vec = pos.toVec3().addVector(0.5, 0.0, 0.5);
                        if (n3 == list4.size() - 1) {
                           vec = interpolatedPos3;
                        }

                        this.moveToWaypoint(vec, entity3);
                        interpolatedPos2 = vec;
                        list2.add(vec);
                     } while (n3 < list4.size() - 1);

                     this.attackTarget(entity3);
                     if (this.legit.getValue()) {
                        this.aimAndTickTarget(entity3);
                     }

                     pathBlockPos = new PathBlockPos((int)Math.round(vec.xCoord), (int)Math.round(vec.yCoord), (int)Math.round(vec.zCoord));
                     if ((double)(++n) >= this.maxTargets.getValue()) {
                        n2 = 0;
                     }
                     break;
                  }

                  if ((double)(++n) >= this.maxTargets.getValue()) {
                     n2 = 0;
                     break;
                  }
               }

               if (list.isEmpty()) {
                  break;
               }

               n2--;
               list.removeAll(list3);
               Vec3 vec2 = interpolatedPos2;
               list.sort(Comparator.comparingDouble(obj2 -> ((Entity)obj2).getDistanceSq(vec2.xCoord, vec2.yCoord, vec2.zCoord)));
            }

            if (!list2.isEmpty()) {
               this.returnAlongPath(list2, interpolatedPos);
            }

            if (this.legit.getValue()) {
               mc.thePlayer.rotationYaw = floats[0];
               mc.thePlayer.rotationPitch = floats[1];
            }
         }

         this.teleporting = false;
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (this.teleporting || this.aimingAtTarget) {
         float f = this.aimingAtTarget ? 0.0F : 1.0F;
         event.setMoveForward(f);
         boolean flag = false;
         event.setSneak(flag);
         boolean isCollidedHorizontally = mc.thePlayer.isCollidedHorizontally;
         event.setJump(isCollidedHorizontally);
         float f2 = 0.0F;
         event.setMoveStrafe(f2);
      }
   }

   private void moveToWaypoint(Vec3 vec, Entity entity) {
      if (this.legit.getValue()) {
         int n = this.walkToWaypoint(vec);

         while (n > 0) {
            n--;
            entity.onUpdate();
         }
      } else {
         boolean flag;
         exitBlock: {
            if (!this.groundSpoof.getValue()) {
               EntityPlayerSP thePlayer = mc.thePlayer;
               boolean flag2 = false;
               AxisAlignedBB box = EntityBoundsUtil.createPlayerBoundingBox(vec, flag2);
               if (!EntityMovementPredictor.isOnGroundAtBox(thePlayer, box)) {
                  flag = false;
                  break exitBlock;
               }
            }

            flag = true;
         }

         boolean flag3 = flag;
         mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec.xCoord, vec.yCoord, vec.zCoord, flag3));
         this.teleportPositions.add(vec);
      }
   }

   private void returnAlongPath(ArrayList list, Vec3 vec) {
      Collections.reverse(list);
      list.add(vec);
      if (this.legit.getValue()) {
         for (Vec3 vec2 : (Iterable<Vec3>)(list)) {
            this.walkToWaypoint(vec2);
         }
      } else {
         for (Vec3 vec3 : (Iterable<Vec3>)(list)) {
            boolean flag;
            exitBlock: {
               if (!this.groundSpoof.getValue()) {
                  EntityPlayerSP thePlayer = mc.thePlayer;
                  boolean flag2 = false;
                  AxisAlignedBB box = EntityBoundsUtil.createPlayerBoundingBox(vec3, flag2);
                  if (!EntityMovementPredictor.isOnGroundAtBox(thePlayer, box)) {
                     flag = false;
                     break exitBlock;
                  }
               }

               flag = true;
            }

            boolean flag3 = flag;
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord, flag3));
         }

         mc.thePlayer
            .sendQueue
            .addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, this.groundSpoof.getValue() || mc.thePlayer.onGround));
      }
   }

   private void attackTarget(Entity entity) {
      if (!this.noswing.getValue()) {
         mc.thePlayer.swingItem();
      }

      mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
   }

   private void aimAndTickTarget(Entity entity) {
      this.teleporting = false;
      this.aimingAtTarget = true;
      Vec3 nearestPointOnEntity = RotationUtil.getNearestPointOnEntity(entity);
      double d = nearestPointOnEntity.xCoord - mc.thePlayer.posX;
      double d2 = nearestPointOnEntity.yCoord - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
      double d3 = nearestPointOnEntity.zCoord - mc.thePlayer.posZ;
      float f = (float)(MathHelper.atan2(d3, d) * 180.0 / Math.PI - 90.0);
      float f2 = (float)(-(MathHelper.atan2(d2, (double)MathHelper.sqrt_double(d * d + d3 * d3)) * 180.0 / Math.PI));
      this.applySmoothedRotation(f, f2);
      mc.thePlayer.onUpdate();
      entity.onUpdate();
      this.teleporting = true;
      this.aimingAtTarget = false;
   }

   private int walkToWaypoint(Vec3 vec) {
      int n = 0;

      while ((double)n < Math.max(10.0, 15.0 * this.maxTeleportDistance.getValue()) && mc.thePlayer.getDistanceToVec(vec) > 0.8) {
         n++;
         double d = vec.xCoord + 0.5 - mc.thePlayer.posX;
         double d2 = vec.zCoord + 0.5 - mc.thePlayer.posZ;
         float f = (float)(MathHelper.atan2(d2, d) * 180.0 / Math.PI - 90.0);
         float f2 = (float)ThreadLocalRandom.current().nextDouble(-1.0, 1.0);
         this.applySmoothedRotation(f, f2);
         mc.thePlayer.onUpdate();
         this.teleportPositions.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
      }

      return n;
   }

   private void applySmoothedRotation(float f, float f2) {
      Rotation rotation = RotationUtil.applyGcdFix(
         RotationUtil.updateAngle(this.currentRotation.yaw, f, 180.0F), RotationUtil.updateAngle(this.currentRotation.pitch, f2, 180.0F), this.currentRotation.yaw, this.currentRotation.pitch
      );
      this.currentRotation.yaw = rotation.yaw;
      this.currentRotation.pitch = rotation.pitch;
      mc.thePlayer.rotationYaw = rotation.yaw;
      mc.thePlayer.rotationPitch = rotation.pitch;
   }

   private int findFarthestReachableNode(ArrayList list, Vec3 vec, int n) {

      for (int i = n; i < list.size(); i++) {
         PathNode pathNode = (PathNode)list.get(i);
         PathBlockPos pos = pathNode.getPos();
         Vec3 vec2 = pos.toVec3().addVector(0.5, 0.0, 0.5);
         if ((vec2.squareDistanceTo(vec) > this.maxTeleportDistance.getValue() * this.maxTeleportDistance.getValue() || this.isPathBlocked(vec, vec2)) && this.isLicenseTimestampValid()) {
            if (n == i) {
               return n;
            }

            return i - 1;
         }
      }

      return list.size() - 1;
   }

   public boolean isPathBlocked(Vec3 vec, Vec3 vec2) {
      Vec3[][] vecsArr = new Vec3[6][];
      Vec3 vec3 = vec2.subtract(vec).crossProduct(0.0, 1.0, 0.0).normalize().scale(0.6);
      vecsArr[0] = new Vec3[]{vec, vec2};
      vecsArr[1] = new Vec3[]{vec.addVector(0.0, 1.5, 0.0), vec2.addVector(0.0, 1.5, 0.0)};
      vecsArr[2] = new Vec3[]{vecsArr[0][0].add(vec3), vecsArr[0][1].add(vec3)};
      vecsArr[3] = new Vec3[]{vecsArr[1][0].add(vec3), vecsArr[1][1].add(vec3)};
      vecsArr[4] = new Vec3[]{vecsArr[0][0].subtract(vec3), vecsArr[0][1].subtract(vec3)};
      vecsArr[5] = new Vec3[]{vecsArr[1][0].subtract(vec3), vecsArr[1][1].subtract(vec3)};

      for (Vec3[] vecs : vecsArr) {
         Vec3 vec4 = vecs[0];
         Vec3 vec5 = vecs[1];
         MovingObjectPosition hit = mc.theWorld.rayTraceBlocksCustom(vec4.xCoord, vec4.yCoord, vec4.zCoord, vec5.xCoord, vec5.yCoord, vec5.zCoord, false, false, false);
         boolean flag = hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK;
         if (flag) {
            this.blockedRaySegments.add(vecsArr);
            return true;
         }
      }

      return false;
   }

   @Override
   public void onRender(float f) {
      GL11.glPushMatrix();
      GL11.glTranslated(-mc.getRenderManager().getRenderPosX(), -mc.getRenderManager().getRenderPosY(), -mc.getRenderManager().getRenderPosZ());
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      GlStateManager.disableAlpha();
      GL11.glEnable(2848);
      GlStateManager.disableDepth();
      GlStateManager.disableTexture2D();
      GlStateManager.disableCull();
      GlStateManager.depthMask(false);
      if (this.pathNodes != null) {
         if (this.debuglines.getValue()) {
            GlStateManager.color(1.0F, 0.0F, 0.0F, 0.3F);
         } else {
            GlStateManager.color(1.0F, 0.0F, 0.0F, 0.15F);
         }

         GL11.glBegin(3);

         for (PathNode pathNode : (Iterable<PathNode>)(this.pathNodes)) {
            PathBlockPos pos = pathNode.getPos();
            Vec3 vec = pos.toVec3();
            GL11.glVertex3d(vec.xCoord + 0.5, vec.yCoord + 0.01, vec.zCoord + 0.5);
         }

         GL11.glEnd();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 0.9F);
         GL11.glBegin(3);

         for (Vec3 vec2 : (Iterable<Vec3>)(this.teleportPositions)) {
            GL11.glVertex3d(vec2.xCoord, vec2.yCoord + 0.01, vec2.zCoord);
         }

         GL11.glEnd();
         if (this.debuglines.getValue()) {
            GlStateManager.color(0.0F, 0.0F, 1.0F, 0.4F);

            for (Vec3[][] vecsArr : (Iterable<Vec3[][]>)(this.blockedRaySegments)) {
               for (Vec3[] vecs : vecsArr) {
                  GL11.glBegin(3);
                  GL11.glVertex3d(vecs[0].xCoord, vecs[0].yCoord, vecs[0].zCoord);
                  GL11.glVertex3d(vecs[1].xCoord, vecs[1].yCoord, vecs[1].zCoord);
                  GL11.glEnd();
               }
            }
         }
      }

      GL11.glPopMatrix();
      GlStateManager.depthMask(true);
      GlStateManager.enableCull();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
      GlStateManager.enableDepth();
      GlStateManager.disableBlend();
      GL11.glDisable(2848);
   }

   private boolean isLicenseTimestampValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
      boolean flag = false;
      if (timestampBytes != null) {
         long l = 0L;

         for (int i = 0; i < 8; i++) {
            l <<= 8;
            l |= (long)(timestampBytes[i] & 255);
         }

         long l2 = System.currentTimeMillis() - l;
         String str = "518400000";
         long l3 = 0L;
         int n = 0;
         int length = str.length();

         while (n < length) {
            int n2 = Character.digit(str.charAt(n++), 10);
            l3 *= 10L;
            l3 -= (long)n2;
         }

         l3 = -l3;
         flag = l2 <= l3;
      }

      return flag;
   }

}
