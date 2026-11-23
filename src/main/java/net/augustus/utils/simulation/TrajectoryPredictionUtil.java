package net.augustus.utils.simulation;

import net.augustus.utils.player.PlayerUtil;
import java.util.ArrayList;
import net.augustus.utils.interfaces.MC;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class TrajectoryPredictionUtil implements MC {
   public static MovementPredictionProvider playerPredictionProvider = null;
   public static MovementPredictionProvider targetPredictionProvider = null;
   private static int[] predictionStateFlags;

   public static double estimateVerticalVelocity(EntityLivingBase entity, int n) {
      HybridKalmanPredictor hybridKalmanPredictor = new HybridKalmanPredictor();
      getPredictionStateFlags();
      byte b = 0;
      Vec3 vec = (Vec3)entity.positionHistory.get(b);
      Vec3 vec2 = (Vec3)entity.positionHistory.get(n);
      double d = (double)n / 20.0;
      Vec3 vec3 = vec2;
      Vec3 vec4 = vec;
      hybridKalmanPredictor.initVertical(vec4, vec3, d);
      byte b2 = 0;
      double yCoord = ((Vec3)entity.positionHistory.get(b2)).yCoord;
      double d2 = 0.0;
      byte b3 = 0;
      double yCoord2 = ((Vec3)entity.positionHistory.get(b3)).yCoord;

      for (int i = 0; i <= n; i++) {
         Vec3 vec5 = (Vec3)entity.positionHistory.get(i);
         if (vec5.yCoord - yCoord2 > 0.8) {
            return 0.0;
         }

         yCoord2 = vec5.yCoord;
      }

      for (int j = 0; j <= n; j++) {
         Vec3 vec6 = (Vec3)entity.positionHistory.get(j);
         double d3 = 0.0;
         double yCoord3 = vec6.yCoord;
         hybridKalmanPredictor.updateMeasurement(yCoord3, d3);
         yCoord = d2;
         d2 = hybridKalmanPredictor.predictNextPosition()[0];
      }

      double d4 = d2 - yCoord;
      return MathHelper.clamp_double(d4, -1.0, 1.0);
   }

   public static ArrayList predictPlayerPositions(EntityPlayerSP player, int n, int n2, int n3, int n4) {
      boolean flag = false;
      return predictPlayerPositions(player, n, n2, n3, n4, flag);
   }

   public static ArrayList predictPlayerPositions(EntityPlayerSP player, int n, int n2, int n3, int n4, boolean flag) {
      int[] predictionStateFlags2 = getPredictionStateFlags();
      if (!flag && playerPredictionProvider != null) {
         return playerPredictionProvider.predictTrajectory(n, n2, n3, n4);
      } else if (player.extraSnapshotHistory.size() == 0) {
         ArrayList list = new ArrayList();
         list.add(player.getInterpolatedPos(1.0F));
         return list;
      } else {
         int ticksExisted = player.ticksExisted;
         if (n > ticksExisted) {
            n = ticksExisted;
         }

         if (player.ticksExisted - n4 > player.extraSnapshotHistory.getEntries().size() - 1) {
            ArrayList list2 = new ArrayList();
            byte b = 0;
            list2.add(((PlayerTickSnapshot)player.extraSnapshotHistory.get(b)).position);
            return list2;
         } else if (n >= n4) {
            ArrayList list3 = new ArrayList();
            int n5 = player.ticksExisted - n4;
            list3.add(((PlayerTickSnapshot)player.extraSnapshotHistory.getFromEnd(n5)).position);
            return list3;
         } else {
            int n6 = ticksExisted - n;
            int n7 = n4 - n;
            if (player.extraSnapshotHistory.size() <= n6) {
               ArrayList list4 = new ArrayList();
               list4.add(player.getInterpolatedPos(1.0F));
               return list4;
            } else {
               ArrayList list5 = new ArrayList();

               for (int i = n; i <= n2; i++) {
                  int n8 = ticksExisted - i + n3 - 1;
                  if (n8 >= 0 && n8 < player.extraSnapshotHistory.size()) {
                     list5.add(player.extraSnapshotHistory.getFromEnd(n8));
                  }
               }

               PlayerTickSnapshot fromEnd = (PlayerTickSnapshot)player.extraSnapshotHistory.getFromEnd(n6);
               int n9 = n7 - 1;
               boolean flag2 = false;
               int n10 = n9;
               ArrayList list6 = PlayerUtil.simulatePlayerSnapshotsHistory(player, list5, fromEnd, n10, flag2);
               ArrayList list7 = new ArrayList();

               for (double[] doubles : (Iterable<double[]>)(list6)) {
                  list7.add(new Vec3(doubles[0], doubles[1], doubles[2]));
               }

               return list7;
            }
         }
      }
   }

   public static ArrayList predictTargetPositions(EntityPlayerSP player, EntityLivingBase entity, int n, int n2, boolean flag) {
      int[] predictionStateFlags2 = getPredictionStateFlags();
      if (entity.positionHistory.size() != 0 && entity.motionSnapshotHistory.size() != 0) {
         int ticksExisted = player.ticksExisted;
         if (n > ticksExisted) {
            n = ticksExisted;
         }

         if (ticksExisted - n2 > entity.positionHistory.size() - 1) {
            ArrayList list = new ArrayList();
            byte b = 0;
            list.add(entity.positionHistory.get(b));
            return list;
         } else if (n < n2 && n >= 0) {
            int n3 = ticksExisted - n;
            int n4 = n2 - n;
            int n5 = entity.positionHistory.size() - (ticksExisted - n) - 1;
            HybridKalmanPredictor hybridKalmanPredictor = new HybridKalmanPredictor();
            byte b2 = 0;
            Vec3 vec = (Vec3)entity.positionHistory.get(b2);
            Vec3 vec2 = (Vec3)entity.positionHistory.get(n5);
            double d = (double)n5 / 20.0;
            Vec3 vec3 = vec2;
            Vec3 vec4 = vec;
            hybridKalmanPredictor.initHorizontal(vec4, vec3, d);
            double[] doubles = new double[2];
            byte b3 = 0;
            doubles[0] = ((Vec3)entity.positionHistory.get(b3)).xCoord;
            byte b4 = 0;
            doubles[1] = ((Vec3)entity.positionHistory.get(b4)).zCoord;
            double[] doubles2 = doubles;
            double[] doubles3 = new double[2];

            for (int i = 0; i <= n5; i++) {
               Vec3 vec5 = (Vec3)entity.positionHistory.get(i);
               double zCoord = vec5.zCoord;
               double xCoord = vec5.xCoord;
               hybridKalmanPredictor.updateMeasurement(xCoord, zCoord);
               doubles2 = doubles3;
               doubles3 = hybridKalmanPredictor.predictNextPosition();
            }

            doubles3 = new double[]{doubles3[0] - doubles2[0], doubles3[1] - doubles2[1]};
            double d2 = estimateVerticalVelocity(entity, n5);
            if (n3 > entity.motionSnapshotHistory.getEntries().size() - 1) {
               byte b5 = 0;
               EntityMotionSnapshot entityMotionSnapshot = (EntityMotionSnapshot)entity.motionSnapshotHistory.get(b5);
               double d3 = doubles3[0];
               double d4 = doubles3[1];
               double d5 = d3;
               EntityMotionSnapshot entityMotionSnapshot2 = entityMotionSnapshot;
               return EntityMovementPredictor.predictTrajectoryFromSnapshot(entity, entityMotionSnapshot2, n4, d5, d2, d4, flag);
            } else {
               EntityMotionSnapshot fromEnd = (EntityMotionSnapshot)entity.motionSnapshotHistory.getFromEnd(n3);
               double d6 = doubles3[0];
               double d7 = doubles3[1];
               double d8 = d6;
               EntityMotionSnapshot entityMotionSnapshot3 = fromEnd;
               return EntityMovementPredictor.predictTrajectoryFromSnapshot(entity, entityMotionSnapshot3, n4, d8, d2, d7, flag);
            }
         } else {
            ArrayList list2 = new ArrayList();
            int n6 = ticksExisted - n2;
            list2.add(entity.positionHistory.getFromEnd(n6));
            return list2;
         }
      } else {
         ArrayList list3 = new ArrayList();
         list3.add(entity.getInterpolatedPos(1.0F));
         return list3;
      }
   }

   public static ArrayList predictTargetPositions(EntityPlayerSP player, EntityLivingBase entity, int n, int n2) {
      int[] predictionStateFlags2 = getPredictionStateFlags();
      if (targetPredictionProvider != null) {
         byte b = 0;
         byte b2 = 0;
         return targetPredictionProvider.predictTrajectory(n, b2, b, n2);
      } else {
         boolean flag = false;
         return predictTargetPositions(player, entity, n, n2, flag);
      }
   }

   public static Vec3 getClampedPosition(ArrayList list, int n) {
      int[] predictionStateFlags2 = getPredictionStateFlags();
      if (n < 0) {
         return (Vec3)list.get(0);
      } else {
         return n >= list.size() ? (Vec3)list.get(list.size() - 1) : (Vec3)list.get(n);
      }
   }

   static {
      int[] ints = new int[2];
      setPredictionStateFlags(ints);
   }

   public static void setPredictionStateFlags(int[] ints) {
      predictionStateFlags = ints;
   }

   public static int[] getPredictionStateFlags() {
      return predictionStateFlags;
   }
}
