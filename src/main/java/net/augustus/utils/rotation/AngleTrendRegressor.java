package net.augustus.utils.rotation;

import net.augustus.utils.math.DoubleSampleWindow;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class AngleTrendRegressor {

   public static AnglePredictionSample[] predictAngleBounds(
      TargetTrajectoryPredictor predictor, EntityPlayerSP player, EntityLivingBase entity, int n, int n2, int n3, double d, int n4
   ) {
      DoubleSampleWindow doubleSampleWindow = new DoubleSampleWindow(n3);
      int n5 = n - n3 + 1;
      double d2 = 0.0;
      double d3 = 0.0;
      double d4 = 0.0;

      for (int i = n5; i <= n; i++) {
         Vec3 vec = predictor.getPredictedEntityPosition((EntityLivingBase)player, n, i);
         Vec3 vec2 = predictor.getPredictedEntityPosition(entity, n, i);
         boolean flag = true;
         AxisAlignedBB entityBoundingBoxAt = EntityBoundsUtil.getEntityBoundingBoxAt(entity, vec2, flag);
         double d5 = MathHelper.wrapAngleTo180_double(HitboxAngleUtil.getYawRange(vec, entityBoundingBoxAt)[n4]);
         if (i == n5) {
            d3 = d5;
            d4 = d5;
            double d6 = 0.0;
            doubleSampleWindow.add(d6);
         } else {
            double d7 = EntityBoundsUtil.getShortestSignedAngleDelta(d3, d5);
            if (entityBoundingBoxAt.isVecInside(vec.xCoord, (entityBoundingBoxAt.minY + entityBoundingBoxAt.maxY) / 2.0, vec.zCoord) || Math.abs(d7) > 90.0) {
               break;
            }

            d2 += d7;
            d3 = d5;
            doubleSampleWindow.add(d2);
         }
      }

      int n6 = n2 - n5;
      if (doubleSampleWindow.getSamples().size() < 2) {
         return null;
      } else {
         double[] doubles = doubleSampleWindow.linearRegression();
         double d8 = doubles[0];
         double d9 = doubles[1];
         double d10 = Math.abs(d8);
         double d11 = Math.abs(d9);
         return new AnglePredictionSample[]{
            new AnglePredictionSample(d10, d11, MathHelper.wrapAngleTo180_double(d10 * (double)n6 + d11 + d4), d4, n6),
            new AnglePredictionSample(-d10, -d11, MathHelper.wrapAngleTo180_double(-d10 * (double)n6 - d11 + d4), d4, n6)
         };
      }
   }
}
