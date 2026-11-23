package net.augustus.modules.modes.killaura.unoise3;

import net.augustus.utils.BoundedHistoryList;
import net.augustus.utils.math.DoubleSampleWindow;
import net.augustus.utils.math.Vec2d;
import net.augustus.utils.rotation.EntityBoundsUtil;
import net.augustus.utils.rotation.HitboxAngleUtil;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.utils.simulation.PlayerTickSnapshot;
import net.augustus.utils.simulation.TrajectoryPredictionUtil;
import java.util.ArrayList;
import net.augustus.settings.StringValue;
import net.augustus.utils.interfaces.MC;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class UNoise3MovementFilter implements MC {
   private StringValue uNoise3FilterTimeOffset;
   public int targetTickOffset;
   private StringValue uNoise3MinInaccuracyForFilterToApplyMoment;
   private StringValue uNoise3TargetMovementConstantOffset;
   public DoubleSampleWindow inaccuracyWindow;
   public EntityPlayerSP player;
   private StringValue uNoise3PlayerMovementConstantOffset;
   public UNoise3SettingContext uNoise3SettingContext;
   public int playerTickOffset;
   private StringValue uNoise3MinInaccuracyForFilterToApply;

   private static double computeMidpointAngleDiff(long l, double[] doubles, double[] doubles2) {
      double d = 0.5;
      double d2 = MathHelper.wrapAngleTo180_double(HitboxAngleUtil.lerpRange(doubles, d));
      double d3 = 0.5;
      return HitboxAngleUtil.getShortestAngularDistance(d2, MathHelper.wrapAngleTo180_double(HitboxAngleUtil.lerpRange(doubles2, d3)));
   }

   public UNoise3MovementFilter(UNoise3SettingContext uNoise3SettingContext2) {
       super();
      this.inaccuracyWindow = new DoubleSampleWindow(10);
      this.uNoise3SettingContext = uNoise3SettingContext2;
      StringValue stringValue = new StringValue(uNoise3SettingContext2.getModule(), "UNoise3MinInaccuracyForFilterToApply", 20.0, 10.0, 30.0, 1, uNoise3SettingContext2.getParentGroup());
      String str = "Min inaccuracy for filter to apply";
      this.uNoise3MinInaccuracyForFilterToApply = stringValue.named(str);
      StringValue stringValue2 = new StringValue(uNoise3SettingContext2.getModule(), "UNoise3MinInaccuracyForFilterToApplyMoment", 20.0, 10.0, 30.0, 1, uNoise3SettingContext2.getParentGroup());
      String str2 = "Min inaccuracy for filter to apply in the moment";
      this.uNoise3MinInaccuracyForFilterToApplyMoment = stringValue2.named(str2);
      StringValue stringValue3 = new StringValue(uNoise3SettingContext2.getModule(), "UNoise3FilterTimeOffset", 1.0, -2.0, 2.0, 0, uNoise3SettingContext2.getParentGroup());
      String str3 = "Filter time offset";
      this.uNoise3FilterTimeOffset = stringValue3.named(str3);
      StringValue stringValue4 = new StringValue(uNoise3SettingContext2.getModule(), "UNoise3TargetMovementConstantOffset", 0.0, -3.0, 3.0, 0, uNoise3SettingContext2.getParentGroup());
      String str4 = "Target movement constant";
      this.uNoise3TargetMovementConstantOffset = stringValue4.named(str4);
      StringValue stringValue5 = new StringValue(uNoise3SettingContext2.getModule(), "UNoise3PlayerMovementConstantOffset", 0.0, -3.0, 3.0, 0, uNoise3SettingContext2.getParentGroup());
      String str5 = "Player movement constant";
      this.uNoise3PlayerMovementConstantOffset = stringValue5.named(str5);
   }

   public UNoise3PredictionEntry[] predictPitchEntries(EntityPlayerSP player2, EntityLivingBase entity, int n, int n2, int n3, int n4) {
      int n5 = n4 + this.targetTickOffset;
      ArrayList list = this.predictEntityPositions(entity, n, n5);
      TrajectoryPredictionUtil.getPredictionStateFlags();
      int n6 = n4 + this.playerTickOffset;
      ArrayList list2 = TrajectoryPredictionUtil.predictPlayerPositions(player2, n, n2, n3, n6);
      boolean flag = false;
      double d = 1.0;
      byte b = 0;
      Vec3 clampedPosition = TrajectoryPredictionUtil.getClampedPosition(list, b);
      byte b2 = 0;
      Vec3 clampedPosition2 = TrajectoryPredictionUtil.getClampedPosition(list2, b2);
      int n7 = 0;
      double d2 = 1.0 / 0.0;

      for (int i = 0; i < Math.max(list.size(), list2.size()); n7 = i++) {
         Vec3 clampedPosition3 = TrajectoryPredictionUtil.getClampedPosition(list, i);
         Vec3 clampedPosition4 = TrajectoryPredictionUtil.getClampedPosition(list2, i);
         if (clampedPosition3.subtract(clampedPosition4).length() < d) {
            flag = true;
            break;
         }

         d2 = Math.min(d2, clampedPosition3.subtract(clampedPosition4).length());
         clampedPosition = clampedPosition3;
         clampedPosition2 = clampedPosition4;
      }

      boolean flag2 = clampedPosition.subtract(clampedPosition2).length() < d;
      int n8 = n7 - 1;
      Vec3 clampedPosition5 = TrajectoryPredictionUtil.getClampedPosition(list, n8);
      int n9 = n7 - 1;
      Vec3 clampedPosition6 = TrajectoryPredictionUtil.getClampedPosition(list2, n9);
      boolean flag3 = false;
      AxisAlignedBB entityBoundingBoxAt = EntityBoundsUtil.getEntityBoundingBoxAt(entity, clampedPosition, flag3);
      double[] pitchRange = HitboxAngleUtil.getPitchRange(clampedPosition2, entityBoundingBoxAt);
      boolean flag4 = false;
      AxisAlignedBB entityBoundingBoxAt2 = EntityBoundsUtil.getEntityBoundingBoxAt(entity, clampedPosition5, flag4);
      double[] pitchRange2 = HitboxAngleUtil.getPitchRange(clampedPosition6, entityBoundingBoxAt2);
      double d3 = pitchRange[0];
      double d4 = pitchRange[1];
      double d5 = EntityBoundsUtil.getShortestSignedAngleDelta(MathHelper.wrapAngleTo180_double(pitchRange2[0]), d3);
      double d6 = EntityBoundsUtil.getShortestSignedAngleDelta(MathHelper.wrapAngleTo180_double(pitchRange2[1]), d4);
      if (flag) {
         d5 = 0.0;
         d6 = 0.0;
      }

      double d7 = pitchRange2[0];
      double d8 = pitchRange2[1];
      UNoise3PredictionEntry uNoise3PredictionEntry = new UNoise3PredictionEntry(clampedPosition2, clampedPosition, d5, 0.0, d3, d7, 1);
      UNoise3PredictionEntry uNoise3PredictionEntry2 = new UNoise3PredictionEntry(clampedPosition2, clampedPosition, d6, 0.0, d4, d8, 1);
      uNoise3PredictionEntry.filtered = uNoise3PredictionEntry2.filtered = flag;
      uNoise3PredictionEntry.tooClose = uNoise3PredictionEntry2.tooClose = flag2;
      return new UNoise3PredictionEntry[]{uNoise3PredictionEntry, uNoise3PredictionEntry2};
   }

   public ArrayList predictPlayerPositions(EntityPlayerSP player2, int n, int n2) {
      byte b = 0;
      int ticksExisted = player2.ticksExisted;
      return TrajectoryPredictionUtil.predictPlayerPositions(player2, n, ticksExisted, b, n2);
   }

   public Vec3 getHistoricalPosition(EntityLivingBase entity, int n) {
      if (entity instanceof EntityPlayerSP) {
         EntityPlayerSP player2 = (EntityPlayerSP)entity;
         return this.getHistoricalPlayerPos(player2, n);
      } else {
         return this.getHistoricalTargetPos(entity, n);
      }
   }

   public double predictMinDistance(EntityPlayerSP player2, EntityLivingBase entity, int n) {
      int n2 = player2.ticksExisted + n;
      int ticksExisted = player2.ticksExisted;
      ArrayList list = this.predictEntityPositions((EntityLivingBase)player2, ticksExisted, n2);
      TrajectoryPredictionUtil.getPredictionStateFlags();
      int n3 = player2.ticksExisted + n;
      int ticksExisted2 = player2.ticksExisted;
      ArrayList list2 = this.predictEntityPositions(entity, ticksExisted2, n3);
      int n4 = Math.max(list.size(), list2.size());
      double d = 1.0 / 0.0;

      for (int i = 0; i < n4; i++) {
         Vec3 clampedPosition = TrajectoryPredictionUtil.getClampedPosition(list, i);
         Vec3 clampedPosition2 = TrajectoryPredictionUtil.getClampedPosition(list2, i);
         boolean flag = true;
         AxisAlignedBB entityBoundingBoxAt = EntityBoundsUtil.getEntityBoundingBoxAt(entity, clampedPosition2, flag);
         Vec3 vec = RotationUtil.clampPointToBox(clampedPosition.addVector(0.0, (double)player2.getEyeHeight(), 0.0), entityBoundingBoxAt);
         d = Math.min(d, vec.subtract(clampedPosition).length());
      }

      return d;
   }

   public Vec3 getHistoricalPlayerPos(EntityPlayerSP player2, int n) {
      BoundedHistoryList extraSnapshotHistory = player2.extraSnapshotHistory;
      int n2 = MathHelper.clamp_int(player2.ticksExisted - n, 0, Math.max(player2.extraSnapshotHistory.size() - 1, 0));
      return ((PlayerTickSnapshot)extraSnapshotHistory.getFromEnd(n2)).position;
   }

   public ArrayList predictEntityPositions(EntityLivingBase entity, int n, int n2) {
      if (entity instanceof EntityPlayerSP) {
         EntityPlayerSP player2 = (EntityPlayerSP)entity;
         return this.predictPlayerPositions(player2, n, n2);
      } else {
         return TrajectoryPredictionUtil.predictTargetPositions(this.player, entity, n, n2);
      }
   }

   public UNoise3PredictionEntry[] predictYawEntries(EntityPlayerSP player2, EntityLivingBase entity, int n, int n2, int n3, int n4) {
      int n5 = n4 + this.targetTickOffset;
      boolean flag = true;
      int n6 = n5 + (int)this.uNoise3TargetMovementConstantOffset.getValue(flag);
      ArrayList list = this.predictEntityPositions(entity, n, n6);
      int n7 = n4 + this.playerTickOffset;
      boolean flag2 = true;
      int n8 = n7 + (int)this.uNoise3PlayerMovementConstantOffset.getValue(flag2);
      ArrayList list2 = TrajectoryPredictionUtil.predictPlayerPositions(player2, n, n2, n3, n8);
      int n9 = list2.size() - 1;
      Vec3 clampedPosition = TrajectoryPredictionUtil.getClampedPosition(list2, n9);
      int n10 = list.size() - 1;
      Vec3 clampedPosition2 = TrajectoryPredictionUtil.getClampedPosition(list, n10);
      TrajectoryPredictionUtil.getPredictionStateFlags();
      boolean flag3 = false;
      AxisAlignedBB entityBoundingBoxAt = EntityBoundsUtil.getEntityBoundingBoxAt(entity, clampedPosition2, flag3);
      double[] yawRange = HitboxAngleUtil.getYawRange(clampedPosition, entityBoundingBoxAt);
      double d = 0.5;
      double d2 = MathHelper.wrapAngleTo180_double(HitboxAngleUtil.lerpRange(yawRange, d));
      Vec3 positionEyes = player2.getPositionEyes(1.0F);
      Vec3 interpolatedPos = entity.getInterpolatedPos(1.0F);
      boolean flag4 = false;
      Vec3 vec = interpolatedPos;
      AxisAlignedBB entityBoundingBoxAt2 = EntityBoundsUtil.getEntityBoundingBoxAt(entity, vec, flag4);
      double[] yawRange2 = HitboxAngleUtil.getYawRange(positionEyes, entityBoundingBoxAt2);
      double d3 = 0.5;
      double d4 = MathHelper.wrapAngleTo180_double(HitboxAngleUtil.lerpRange(yawRange2, d3));
      double d5 = HitboxAngleUtil.getShortestAngularDistance(d2, d4);
      boolean flag5 = false;
      boolean flag6 = true;
      if (d5 > this.uNoise3MinInaccuracyForFilterToApply.getValue(flag6)) {
         for (int i = n; i <= n4; i++) {
            int n11 = i - n;
            Vec3 clampedPosition3 = TrajectoryPredictionUtil.getClampedPosition(list2, n11);
            int n12 = i - n;
            Vec3 clampedPosition4 = TrajectoryPredictionUtil.getClampedPosition(list, n12);
            int n13 = i + 1;
            Vec3 historicalPlayerPos = this.getHistoricalPlayerPos(player2, n13);
            int n14 = i + 1;
            Vec3 historicalTargetPos = this.getHistoricalTargetPos(entity, n14);
            Vec2d vec2d = new Vec2d(clampedPosition3.xCoord, clampedPosition3.zCoord);
            Vec2d vec2d2 = new Vec2d(clampedPosition4.xCoord, clampedPosition4.zCoord);
            Vec2d vec2d3 = new Vec2d(historicalPlayerPos.xCoord, historicalPlayerPos.zCoord);
            Vec2d vec2d4 = new Vec2d(historicalTargetPos.xCoord, historicalTargetPos.zCoord);
            Vec2d vec2d5 = vec2d.subtract(vec2d2);
            Vec2d vec2d6 = vec2d3.subtract(vec2d4);
            Vec2d vec2d7 = vec2d6;
            double d6 = vec2d5.angleDegrees(vec2d7);
            boolean flag7 = true;
            if (d6 > this.uNoise3MinInaccuracyForFilterToApplyMoment.getValue(flag7)) {
               flag5 = true;
               int n15 = n4;
               boolean flag8 = true;
               n4 = Math.min(i + (int)this.uNoise3FilterTimeOffset.getValue(flag8), n4);
               if (n15 != n4) {
               }
            }
         }
      }

      int n16 = n4 + this.targetTickOffset;
      boolean flag9 = true;
      int n17 = n16 + (int)this.uNoise3TargetMovementConstantOffset.getValue(flag9) - 1;
      ArrayList list3 = this.predictEntityPositions(entity, n, n17);
      n7 = n4 + this.playerTickOffset;
      boolean flag10 = true;
      int n18 = n7 + (int)this.uNoise3PlayerMovementConstantOffset.getValue(flag10) - 1;
      ArrayList list4 = TrajectoryPredictionUtil.predictPlayerPositions(player2, n, n2, n3, n18);
      n5 = n4 + this.targetTickOffset;
      boolean flag11 = true;
      int n19 = n5 + (int)this.uNoise3TargetMovementConstantOffset.getValue(flag11);
      list = this.predictEntityPositions(entity, n, n19);
      n7 = n4 + this.playerTickOffset;
      boolean flag12 = true;
      int n20 = n7 + (int)this.uNoise3PlayerMovementConstantOffset.getValue(flag12);
      list2 = TrajectoryPredictionUtil.predictPlayerPositions(player2, n, n2, n3, n20);
      double d7 = 1.0;
      byte b = 0;
      Vec3 clampedPosition5 = TrajectoryPredictionUtil.getClampedPosition(list, b);
      byte b2 = 0;
      Vec3 clampedPosition6 = TrajectoryPredictionUtil.getClampedPosition(list2, b2);
      int n21 = 0;
      double d8 = 1.0 / 0.0;

      for (int j = 0; j < Math.max(list.size(), list2.size()); n21 = j++) {
         Vec3 clampedPosition7 = TrajectoryPredictionUtil.getClampedPosition(list, j);
         Vec3 clampedPosition8 = TrajectoryPredictionUtil.getClampedPosition(list2, j);
         if (clampedPosition7.subtract(clampedPosition8).horizontalLength() < d7) {
            flag5 = true;
            break;
         }

         d8 = Math.min(d8, clampedPosition7.subtract(clampedPosition8).horizontalLength());
         clampedPosition5 = clampedPosition7;
         clampedPosition6 = clampedPosition8;
      }

      boolean flag13 = d8 < d7;
      int n22 = n21 - 1;
      Vec3 clampedPosition9 = TrajectoryPredictionUtil.getClampedPosition(list3, n22);
      int n23 = n21 - 1;
      Vec3 clampedPosition10 = TrajectoryPredictionUtil.getClampedPosition(list4, n23);
      boolean flag14 = false;
      AxisAlignedBB entityBoundingBoxAt3 = EntityBoundsUtil.getEntityBoundingBoxAt(entity, clampedPosition5, flag14);
      double[] yawRange3 = HitboxAngleUtil.getYawRange(clampedPosition6, entityBoundingBoxAt3);
      boolean flag15 = false;
      AxisAlignedBB entityBoundingBoxAt4 = EntityBoundsUtil.getEntityBoundingBoxAt(entity, clampedPosition9, flag15);
      double[] yawRange4 = HitboxAngleUtil.getYawRange(clampedPosition10, entityBoundingBoxAt4);
      double d9 = MathHelper.wrapAngleTo180_double(yawRange3[0]);
      double d10 = MathHelper.wrapAngleTo180_double(yawRange3[1]);
      double d11 = EntityBoundsUtil.getShortestSignedAngleDelta(MathHelper.wrapAngleTo180_double(yawRange4[0]), d9);
      double d12 = EntityBoundsUtil.getShortestSignedAngleDelta(MathHelper.wrapAngleTo180_double(yawRange4[1]), d10);
      if (flag5) {
         d11 = 0.0;
         d12 = 0.0;
      }

      double d13 = MathHelper.wrapAngleTo180_double(yawRange4[0]);
      double d14 = MathHelper.wrapAngleTo180_double(yawRange4[1]);
      UNoise3PredictionEntry uNoise3PredictionEntry = new UNoise3PredictionEntry(clampedPosition6, clampedPosition5, d11, 0.0, d9, d13, 1);
      UNoise3PredictionEntry uNoise3PredictionEntry2 = new UNoise3PredictionEntry(clampedPosition6, clampedPosition5, d12, 0.0, d10, d14, 1);
      uNoise3PredictionEntry.filtered = uNoise3PredictionEntry2.filtered = flag5;
      uNoise3PredictionEntry.tooClose = uNoise3PredictionEntry2.tooClose = flag13;
      return new UNoise3PredictionEntry[]{uNoise3PredictionEntry, uNoise3PredictionEntry2};
   }

   public Vec3 getHistoricalTargetPos(EntityLivingBase entity, int n) {
      BoundedHistoryList positionHistory = entity.positionHistory;
      int n2 = MathHelper.clamp_int(this.player.ticksExisted - n, 0, Math.max(entity.positionHistory.size() - 1, 0));
      return (Vec3)positionHistory.getFromEnd(n2);
   }

}
