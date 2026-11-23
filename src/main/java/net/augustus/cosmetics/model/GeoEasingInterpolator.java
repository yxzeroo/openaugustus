package net.augustus.cosmetics.model;

import net.augustus.utils.UnaryFunction;
import java.util.List;

public class GeoEasingInterpolator {

   public static double lerpValues2(double d, double d2, double d3) {
      return d2 + d * (d3 - d2);
   }

   public static float lerpFloatValues(double d, double d2, double d3) {
      return (float)lerpValues2(d, d2, d3);
   }

   public static float lerpValues(GeoAnimationPoint geoAnimationPoint, GeoEasingType geoEasingType, UnaryFunction unaryFunction) {
      if (geoAnimationPoint.currentTick >= geoAnimationPoint.animationEndTick) {
         return geoAnimationPoint.animationEndValue.floatValue();
      } else if (geoAnimationPoint.currentTick == 0.0 && geoAnimationPoint.animationEndTick == 0.0) {
         return geoAnimationPoint.animationEndValue.floatValue();
      } else if (geoEasingType == GeoEasingType.CUSTOM && unaryFunction != null) {
         Double d = geoAnimationPoint.currentTick / geoAnimationPoint.animationEndTick;
         return lerpFloatValues((Double)unaryFunction.apply(d), geoAnimationPoint.animationStartValue, geoAnimationPoint.animationEndValue);
      } else {
         if (geoEasingType == GeoEasingType.NONE && geoAnimationPoint.keyframe != null) {
            geoEasingType = geoAnimationPoint.keyframe.easingType;
         }

         return lerpFloatValues(GeoEasingManager.ease(geoAnimationPoint.currentTick / geoAnimationPoint.animationEndTick, geoEasingType, geoAnimationPoint.keyframe == null ? null : geoAnimationPoint.keyframe.easingArgs), geoAnimationPoint.animationStartValue, geoAnimationPoint.animationEndValue);
      }
   }
}
