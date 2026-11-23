package net.augustus.utils.rotation;

import net.augustus.ClientManagers;
import net.augustus.events.EventMoveInput;
import net.augustus.events.MoveInputStage;
import net.augustus.utils.BoundedHistoryList;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.utils.simulation.EntityMotionSnapshot;
import net.augustus.utils.simulation.EntityMovementPredictor;
import java.util.ArrayList;
import net.augustus.utils.TimeHelper;
import net.augustus.utils.interfaces.MC;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class PredictiveAimSolver implements MC {
   public int maxPlayerResponseTicks;
   public float maxAimSpeed;
   public float minAcceleration;
   private Rotation currentTargetRotation;
   public static final int HISTORY_BUFFER_SIZE = 23;
   public double minLeadDistanceScale;
   private Rotation previousTargetRotation;
   public int maxTargetResponseTicks;
   public int minTargetResponseTicks = 3;
   public int minPlayerResponseTicks;
   public float maxAcceleration;
   public double betterAccelerationFactor;
   public float minAimSpeed;
   private EntityLivingBase lastTarget;

   public Rotation computeRotation(
      EntityLivingBase entity, EntityPlayerSP player, float f, float f2, double d, double d2, Vec3 vec, Vec3 vec2
   ) {
      if (entity == null) {
         return new Rotation(f, f2);
      } else {
         Vec3 positionEyes = mc.thePlayer.getPositionEyes(1.0F);
         Vec3 interpolatedPos = entity.getInterpolatedPos(1.0F);
         Vec3 vec3 = new Vec3(0.0, 0.0, 0.0);

         for (int i = this.minPlayerResponseTicks; i <= this.maxPlayerResponseTicks; i++) {
            EntityPlayerSP thePlayer = mc.thePlayer;
            vec3 = vec3.add(this.predictEntityPosition((EntityLivingBase)thePlayer, i).scale(1.0 / (double)(this.maxPlayerResponseTicks - this.minPlayerResponseTicks + 1)));
         }

         vec3 = vec3.addVector(0.0, (double)mc.thePlayer.getEyeHeight(), 0.0);
         Vec3 vec4 = new Vec3(0.0, 0.0, 0.0);

         for (int j = this.minTargetResponseTicks; j <= this.maxTargetResponseTicks; j++) {
            vec4 = vec4.add(this.predictEntityPosition(entity, j).scale(1.0 / (double)(this.maxTargetResponseTicks - this.minTargetResponseTicks + 1)));
         }

         double minLeadDistanceScale2 = this.minLeadDistanceScale;
         vec4 = this.enforceMinimumHorizontalLead(interpolatedPos, positionEyes, vec4, vec3, minLeadDistanceScale2);
         Rotation rotation = new Rotation(f, f2);
         Rotation rotation2 = this.computePredictedTargetAngles(entity, vec3, vec4, positionEyes, interpolatedPos, rotation);
         Rotation rotation3 = new Rotation(f, f2);
         Rotation rotation4 = this.applyAccelerationAndHoldCheck(entity, vec3, vec4, positionEyes, interpolatedPos, rotation2, rotation3);
         this.lastTarget = entity;
         Rotation rotation5 = new Rotation(MathHelper.wrapAngleTo180_float(f), f2);
         return this.applyAntiAxisLockJitter(entity, rotation4, rotation5);
      }
   }

   public PredictiveAimSolver() {
      this.maxTargetResponseTicks = 5;
      this.minPlayerResponseTicks = 4;
      this.maxPlayerResponseTicks = 4;
      this.minAcceleration = 3.0F;
      this.maxAcceleration = 6.0F;
      this.minAimSpeed = 20.0F;
      this.maxAimSpeed = 30.0F;
      this.minLeadDistanceScale = 1.5;
      this.betterAccelerationFactor = 0.0;
      this.previousTargetRotation = new Rotation();
      this.currentTargetRotation = new Rotation();
      this.lastTarget = null;
   }

   private float computeOverlapPitch(EntityPlayerSP player, EntityLivingBase entity, double d) {
      if (player.getDistanceToEntityBox(entity) > 4.0F) {
         return (float)d;
      } else {
         double posX = player.posX;
         double posY = player.posY;
         double posZ = player.posZ;
         EventMoveInput event = new EventMoveInput(
            player.movementInput.moveStrafe,
            player.movementInput.moveForward,
            player.movementInput.jump,
            player.movementInput.sneak,
            MoveInputStage.PRE
         );
         double d2 = posZ;
         double d3 = posY;
         double d4 = posX;
         ArrayList list = PlayerUtil.predictPlayerTrajectory(5, player, d4, d3, d2, event);
         int size = list.size();
         ArrayList list2 = EntityMovementPredictor.predictTrajectoryFromServerPos(entity, size);
         list.add(player.getInterpolatedPos(1.0F));
         list2.add(entity.getInterpolatedPos(1.0F));
         int n = Math.min(list.size(), list2.size());
         double[][] doublesArr = new double[n][];
         ArrayList list3 = new ArrayList();

         for (int i = 0; i < n; i++) {
            double d5 = MathHelper.clamp_double((double)(player.getDistanceToEntityBox(entity) - 3.0F) / 3.0, 0.0, 1.0);
            Vec3 vec = (Vec3)list2.get(i);
            boolean flag = false;
            AxisAlignedBB box = EntityBoundsUtil.createPlayerBoundingBox(vec, flag).expand(d5, d5, d5);
            Vec3 vec2 = ((Vec3)list.get(i)).addVector(0.0, (double)player.getEyeHeight(), 0.0);
            double[] pitchRange = HitboxAngleUtil.getPitchRange(vec2, box);
            doublesArr[i] = pitchRange;
            list3.add(pitchRange[0]);
            list3.add(pitchRange[1]);
         }

         double d6 = d;
         double d7 = d;
         double d8 = 0.0;
         int n2 = -1;
         int n3 = 0;

         for (int j = 0; j < list3.size(); j++) {
            double d9 = (Double)list3.get(j);
            int n4 = 0;

            for (double[] doubles : doublesArr) {
               if (d9 >= doubles[0] && d9 <= doubles[1]) {
                  n4++;
               }
            }

            if (n4 > n2 || n4 == n2 && Math.abs(d - d9) < d8) {
               d6 = d9;
               d8 = Math.abs(d - d9);
               n2 = n4;
               n3 = j;
            }
         }

         d8 = 0.0;
         n2 = -1;

         for (int k = 0; k < list3.size(); k++) {
            if (k != n3) {
               double d10 = (Double)list3.get(k);
               if (d10 != d6) {
                  int n5 = 0;

                  for (double[] doubles2 : doublesArr) {
                     if (d10 >= doubles2[0] && d10 <= doubles2[1]) {
                        n5++;
                     }
                  }

                  if (n5 > n2 || n5 == n2 && Math.abs(d6 - d10) < d8) {
                     d7 = d10;
                     d8 = Math.abs(d6 - d10);
                     n2 = n5;
                     n3 = k;
                  }
               }
            }
         }

         double[] doubles3 = new double[]{Math.min(d6, d7), Math.max(d6, d7)};
         double d11 = MathHelper.clamp_double(d, doubles3[0], doubles3[1]);
         return (float)d11;
      }
   }

   private Vec3 enforceMinimumHorizontalLead(Vec3 vec, Vec3 vec2, Vec3 vec3, Vec3 vec4, double d) {
      Vec3 vec5 = vec.subtract(vec2);
      Vec3 vec6 = vec3.subtract(vec4);
      vec5.yCoord = 0.0;
      vec6.yCoord = 0.0;
      Vec3 vec7 = null;
      if (vec6.length() < d) {
         for (double i = vec5.length(); i < 10.0; i += 0.1) {
            double d2 = i / vec5.length();
            Vec3 vec8 = vec5.scale(d2).add(vec2);
            Vec3 vec9 = vec3.subtract(vec);
            vec7 = vec8.add(vec9);
            double length = vec7.subtract(vec4).length();
            if (length > d) {
               break;
            }
         }
      }

      if (vec7 == null) {
         vec7 = vec3;
      }

      vec7.yCoord = vec3.yCoord;
      return vec7;
   }

   private Rotation applyAntiAxisLockJitter(EntityLivingBase entity, Rotation rotation, Rotation rotation2) {
      double d = Math.abs(EntityBoundsUtil.getShortestSignedAngleDelta((double)rotation.yaw, (double)rotation2.yaw));
      double d2 = (double)Math.abs(rotation.pitch - rotation2.pitch);
      if (!(d2 > 0.075) && !(d > 0.075) || !(d < 0.075) && !(d2 < 0.075) && !(d / d2 < 0.1) && !(d2 / d < 0.1)) {
         return rotation;
      } else {
         double d3 = 0.6;
         double d4 = -0.6;
         float f = rotation.yaw = rotation.yaw + DefaultRandomGenerator.instance.nextFloat2(d4, d3);
         double d5 = 0.6;
         double d6 = -0.6;
         return new Rotation(f, rotation.pitch = rotation.pitch + DefaultRandomGenerator.instance.nextFloat2(d6, d5));
      }
   }

   private void updateTargetRotationHistory(EntityLivingBase entity, float f, float f2) {
      if (this.lastTarget != null && entity != null && entity.getEntityId() == this.lastTarget.getEntityId()) {
         this.previousTargetRotation = new Rotation(this.currentTargetRotation);
         this.currentTargetRotation = new Rotation(f, f2);
      } else {
         this.previousTargetRotation = new Rotation(f, f2);
         this.currentTargetRotation = new Rotation(f, f2);
      }
   }

   private Vec3 predictEntityPositionWithOffset(EntityLivingBase entity, int n, int n2) {
      if (entity != mc.thePlayer) {
         BoundedHistoryList motionSnapshotHistory = entity.motionSnapshotHistory;
         if (motionSnapshotHistory.getEntries().isEmpty()) {
            return entity.getInterpolatedPos(1.0F);
         } else {
            EntityMotionSnapshot entityMotionSnapshot;
            if (!motionSnapshotHistory.isFull()) {
               entityMotionSnapshot = (EntityMotionSnapshot)motionSnapshotHistory.getEntries().get(motionSnapshotHistory.getEntries().size() - 1);
            } else {
               int n3 = Math.max(0, 23 - n - 1 + n2);
               entityMotionSnapshot = (EntityMotionSnapshot)motionSnapshotHistory.get(n3);
            }

            return EntityMovementPredictor.predictPositionFromSnapshot(entity, entityMotionSnapshot, n);
         }
      } else {
         int n4 = entity.tickSnapshotHistory.size() - n - 1;
         if (n4 < entity.tickSnapshotHistory.size() && n4 >= 0) {
            ArrayList list = new ArrayList();

            for (int i = n4; i < entity.tickSnapshotHistory.size(); i++) {
               list.add(entity.tickSnapshotHistory.get(i));
            }

            EntityPlayerSP player = (EntityPlayerSP)entity;
            boolean flag = false;
            double[] doubles = PlayerUtil.simulatePlayerSnapshotsFinal(player, list, n, flag);
            return new Vec3(doubles[0], doubles[1], doubles[2]);
         } else {
            return entity.getInterpolatedPos(1.0F);
         }
      }
   }

   private Rotation applyAccelerationAndHoldCheck(EntityLivingBase entity, Vec3 vec, Vec3 vec2, Vec3 vec3, Vec3 vec4, Rotation rotation, Rotation rotation2) {
      float yaw = rotation2.yaw;
      byte b = 100;
      byte b2 = 0;
      Vec3 bestHitVec = RotationUtil.getBestHitVec(entity, b2, b);
      float f = 180.0F;
      float f2 = 180.0F;
      Rotation rotation3 = new Rotation(yaw, RotationUtil.getRotationsBetweenPoints(bestHitVec, vec3, rotation2, f2, f).pitch);
      double d = entity.hurtTime >= 5 && mc.thePlayer.hurtTime <= 5 ? 0.2 : -0.3;
      double d2 = 7.0;
      MovingObjectPosition hit = RayTraceUtil.rayTraceEntity(1.0F, rotation3, d2, d);
      if (hit != null && hit.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
         byte b3 = 1;
         Vec3 vec5 = EntityMovementPredictor.predictConstantHorizontalPosition(entity, b3);
         double zCoord = vec5.zCoord;
         double yCoord = vec5.yCoord;
         double xCoord = vec5.xCoord;
         AxisAlignedBB box = RotationUtil.createEntityBoxAt(entity, xCoord, yCoord, zCoord).contract(0.3, 0.3, 0.3);
         double[] doubles = PlayerUtil.predictPlayerPosition(0, mc.thePlayer);
         Vec3 vec6 = new Vec3(doubles[0], doubles[1], doubles[2]).addVector(0.0, (double)mc.thePlayer.getEyeHeight(), 0.0);
         EntityPlayerSP thePlayer = mc.thePlayer;
         float yaw2 = rotation2.yaw;
         byte b4 = 100;
         byte b5 = 0;
         Vec3 vec7 = RotationUtil.getScaledNearestPointInBox(vec6, box, b5, b4);
         float f3 = 180.0F;
         float f4 = 180.0F;
         Vec3 lookCustom2 = thePlayer.getLookCustom2(1.0F, yaw2, RotationUtil.getRotationsBetweenPoints(vec7, vec6, rotation2, f4, f3).pitch);
         Vec3 vec8 = vec6.addVector(lookCustom2.xCoord * 7.0, lookCustom2.yCoord * 7.0, lookCustom2.zCoord * 7.0);
         hit = box.calculateIntercept(vec6, vec8);
         if (hit != null || entity.hurtTime < 5 || mc.thePlayer.hurtTime > 5) {
            rotation.yaw = MathHelper.wrapAngleTo180_float(rotation2.yaw);
         }
      }

      float pitch = rotation.pitch;
      float yaw3 = rotation.yaw;
      this.updateTargetRotationHistory(entity, yaw3, pitch);
      float yaw4 = rotation2.yaw;
      float yaw5 = rotation.yaw;
      float maxAimSpeed2 = this.maxAimSpeed;
      float minAimSpeed2 = this.minAimSpeed;
      float f5 = DefaultRandomGenerator.instance.nextFloat(minAimSpeed2, maxAimSpeed2);
      yaw = ClientManagers.rotationController.getPreviousRotation().yaw - ClientManagers.rotationController.getLastTickRotation().yaw;
      float maxAcceleration2 = this.maxAcceleration;
      float minAcceleration2 = this.minAcceleration;
      float f6 = DefaultRandomGenerator.instance.nextFloat(minAcceleration2, maxAcceleration2);
      float minAcceleration3 = this.minAcceleration;
      float maxAcceleration3 = this.maxAcceleration;
      boolean flag = this.betterAccelerationFactor != 0.0;
      float f7 = (float)Math.min((double)Math.abs(this.previousTargetRotation.yaw - this.currentTargetRotation.yaw) * this.betterAccelerationFactor, (double)this.minAimSpeed);
      boolean flag2 = flag;
      float f8 = maxAcceleration3;
      float f9 = minAcceleration3;
      float f10 = f6;
      float f11 = RotationUtil.accelerateAngle(yaw4, yaw5, f5, yaw, f10, f9, f8, flag2, f7);
      yaw4 = rotation2.pitch;
      float pitch2 = rotation.pitch;
      float maxAimSpeed3 = this.maxAimSpeed;
      float minAimSpeed3 = this.minAimSpeed;
      f5 = DefaultRandomGenerator.instance.nextFloat(minAimSpeed3, maxAimSpeed3);
      yaw = ClientManagers.rotationController.getPreviousRotation().pitch - ClientManagers.rotationController.getLastTickRotation().pitch;
      float maxAcceleration4 = this.maxAcceleration;
      float minAcceleration4 = this.minAcceleration;
      float f12 = DefaultRandomGenerator.instance.nextFloat(minAcceleration4, maxAcceleration4);
      minAcceleration3 = this.minAcceleration;
      maxAcceleration3 = this.maxAcceleration;
      flag = this.betterAccelerationFactor != 0.0;
      float f13 = (float)Math.min((double)Math.abs(this.previousTargetRotation.pitch - this.currentTargetRotation.pitch) * this.betterAccelerationFactor, (double)this.minAimSpeed);
      flag2 = flag;
      f8 = maxAcceleration3;
      f9 = minAcceleration3;
      f10 = f12;
      float f14 = RotationUtil.accelerateAngle(yaw4, pitch2, f5, yaw, f10, f9, f8, flag2, f13);
      return new Rotation(MathHelper.wrapAngleTo180_float(f11), f14);
   }

   private Rotation computePredictedTargetAngles(EntityLivingBase entity, Vec3 vec, Vec3 vec2, Vec3 vec3, Vec3 vec4, Rotation rotation) {
      double zCoord = vec2.zCoord;
      double yCoord = vec2.yCoord;
      double xCoord = vec2.xCoord;
      AxisAlignedBB box = RotationUtil.createEntityBoxAt(entity, xCoord, yCoord, zCoord);
      byte b = 20;
      byte b2 = 100;
      AxisAlignedBB box2 = box;
      Vec3 vec5 = RotationUtil.getScaledNearestPointInBox(vec, box2, b2, b);
      Vec3 vec6 = vec5.subtract(vec);
      double zCoord2 = vec4.zCoord;
      double yCoord2 = vec4.yCoord;
      double xCoord2 = vec4.xCoord;
      box = RotationUtil.createEntityBoxAt(entity, xCoord2, yCoord2, zCoord2);
      byte b3 = 20;
      byte b4 = 100;
      AxisAlignedBB box3 = box;
      Vec3 vec7 = RotationUtil.getScaledNearestPointInBox(vec, box3, b4, b3);
      Vec3 vec8 = vec7.subtract(vec);
      float f = (float)(MathHelper.atan2(vec6.zCoord, vec6.xCoord) * 180.0 / 3.141592653589793 - 90.0);
      float f2 = (float)(
         -(
            MathHelper.atan2(vec8.yCoord, (double)MathHelper.sqrt_double(vec8.xCoord * vec8.xCoord + vec8.zCoord * vec8.zCoord))
               * 180.0
               / 3.141592653589793
         )
      );
      double pitch = (double)rotation.pitch;
      EntityPlayerSP thePlayer = mc.thePlayer;
      return new Rotation(f, this.computeOverlapPitch(thePlayer, entity, pitch));
   }

   public void reset() {
      this.lastTarget = null;
   }

   private Vec3 predictEntityPosition(EntityLivingBase entity, int n) {
      byte b = 0;
      return this.predictEntityPositionWithOffset(entity, n, b);
   }
}
