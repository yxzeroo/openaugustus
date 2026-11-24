package net.augustus.cosmetics.model;

public enum GeoEasingType {
   NONE,
   CUSTOM,
   Linear,
   Step,
   EaseInSine,
   EaseOutSine,
   EaseInOutSine,
   EaseInQuad,
   EaseOutQuad,
   EaseInOutQuad,
   EaseInCubic,
   EaseOutCubic,
   EaseInOutCubic,
   EaseInQuart,
   EaseOutQuart,
   EaseInOutQuart,
   EaseInQuint,
   EaseOutQuint,
   EaseInOutQuint,
   EaseInExpo,
   EaseOutExpo,
   EaseInOutExpo,
   EaseInCirc,
   EaseOutCirc,
   EaseInOutCirc,
   EaseInBack,
   EaseOutBack,
   EaseInOutBack,
   EaseInElastic,
   EaseOutElastic,
   EaseInOutElastic,
   EaseInBounce,
   EaseOutBounce,
   EaseInOutBounce;


   public static GeoEasingType getEasingTypeFromString(String str, long l) {

      for (GeoEasingType geoEasingType : values()) {
         if (geoEasingType.name().compareToIgnoreCase(str) == 0) {
            return geoEasingType;
         }
      }

      return null;
   }






}
