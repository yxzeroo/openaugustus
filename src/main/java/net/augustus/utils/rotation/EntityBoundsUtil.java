package net.augustus.utils.rotation;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class EntityBoundsUtil {

   public static AxisAlignedBB getEntityBoundingBox(Entity entity, boolean flag) {
      Vec3 interpolatedPos = entity.getInterpolatedPos(1.0F);
      return getEntityBoundingBoxAt(entity, interpolatedPos, flag);
   }

   public static double getShortestAngleDistance(double d, double d2) {
      double d3 = Math.abs(d - d2);
      return Math.min(d3, 360.0 - d3);
   }

   public static double getSignedAngleDifference(double d, double d2) {
      double shortestAngleDistance = getShortestAngleDistance(d, d2);
      double d3 = wrapAngle180(d + shortestAngleDistance);
      double d4 = wrapAngle180(d - shortestAngleDistance);
      if (getShortestAngleDistance(d3, d2) > getShortestAngleDistance(d4, d2)) {
         shortestAngleDistance = -shortestAngleDistance;
      }

      return shortestAngleDistance;
   }

   public static AxisAlignedBB createBoundingBox(Vec3 vec, float f, float f2, float f3) {
      if (vec == null) {
         return null;
      } else {
         f2 /= 2.0F;
         return new AxisAlignedBB(
            vec.xCoord - (double)f2 - (double)f,
            vec.yCoord - (double)f,
            vec.zCoord - (double)f2 - (double)f,
            vec.xCoord + (double)f2 + (double)f,
            vec.yCoord + (double)f3 + (double)f,
            vec.zCoord + (double)f2 + (double)f
         );
      }
   }

   public static double wrapAngle180(double d) {
      d %= 360.0;
      if (d >= 180.0) {
         d -= 360.0;
      }

      if (d < -180.0) {
         d += 360.0;
      }

      return d;
   }

   public static AxisAlignedBB getEntityBoundingBoxAt(Entity entity, Vec3 vec, boolean flag) {
      float f = flag ? entity.getCollisionBorderSize() : 0.0F;
      float height = entity.height;
      float width = entity.width;
      float f2 = f;
      return createBoundingBox(vec, f2, width, height);
   }

   public static double getShortestSignedAngleDelta(double d, double d2) {
      double d3 = HitboxAngleUtil.getShortestAngularDistance(d, d2);
      double d4 = MathHelper.wrapAngleTo180_double(d + d3);
      double d5 = MathHelper.wrapAngleTo180_double(d - d3);
      if (HitboxAngleUtil.getShortestAngularDistance(d4, d2) > HitboxAngleUtil.getShortestAngularDistance(d5, d2)) {
         d3 = -d3;
      }

      return d3;
   }

   public static AxisAlignedBB createPlayerBoundingBox(Vec3 vec, boolean flag) {
      float f = flag ? 0.1F : 0.0F;
      float f2 = 1.8F;
      float f3 = 0.6F;
      float f4 = f;
      return createBoundingBox(vec, f4, f3, f2);
   }




}
