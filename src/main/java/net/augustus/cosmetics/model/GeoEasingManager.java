package net.augustus.cosmetics.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.DoubleStream;

public class GeoEasingManager {
   public static Function<Double, Double> QUINT = poly(5.0);
   public static Function<Double, Double> QUART = poly(4.0);
   public static Function CACHED_EASING;

   public static Function<Double, Double> back(Double d) {
      double d2 = d == null ? 1.70158 : d * 1.70158;
      return d3 -> d3 * d3 * ((d2 + 1.0) * d3 - d2);
   }

   private static Double stepLookup(double[] doubles, Double d) {
      double d2 = d;
      boolean flag = false;
      return doubles[binarySearch(d2, doubles, flag)];
   }

   public static double min(double d, double d2, double d3, double d4) {
      return Math.min(Math.min(d, d2), Math.min(d3, d4));
   }

   public static Function<Double, Double> easeInOut(Function<Double, Double> function) {
      return d -> {
         return d < 0.5 ? (Double)function.apply(d * 2.0) / 2.0 : 1.0 - (Double)function.apply((1.0 - d) * 2.0) / 2.0;
      };
   }

   public static Function<Double, Double> getEasing(GeoEasingCacheKey geoEasingCacheKey) {
      switch (GeoEasingTypeSwitchMap.ORDINAL_MAP[geoEasingCacheKey.easingType.ordinal()]) {
         case 1:
         default:
            return easeIn((Double x) -> GeoEasingManager.linear((double)x));
         case 2:
            return easeIn(step(geoEasingCacheKey.customArgument));
         case 3:
            return easeIn((Double x) -> GeoEasingManager.sine((double)x));
         case 4:
            return easeOut((Double x) -> GeoEasingManager.sine((double)x));
         case 5:
            return easeInOut((Double x) -> GeoEasingManager.sine((double)x));
         case 6:
            return easeIn((Double x) -> GeoEasingManager.quad((double)x));
         case 7:
            return easeOut((Double x) -> GeoEasingManager.quad((double)x));
         case 8:
            return easeInOut((Double x) -> GeoEasingManager.quad((double)x));
         case 9:
            return easeIn((Double x) -> GeoEasingManager.cubic((double)x));
         case 10:
            return easeOut((Double x) -> GeoEasingManager.cubic((double)x));
         case 11:
            return easeInOut((Double x) -> GeoEasingManager.cubic((double)x));
         case 12:
            return easeIn((Double x) -> GeoEasingManager.expo((double)x));
         case 13:
            return easeOut((Double x) -> GeoEasingManager.expo((double)x));
         case 14:
            return easeInOut((Double x) -> GeoEasingManager.expo((double)x));
         case 15:
            return easeIn((Double x) -> GeoEasingManager.circ((double)x));
         case 16:
            return easeOut((Double x) -> GeoEasingManager.circ((double)x));
         case 17:
            return easeInOut((Double x) -> GeoEasingManager.circ((double)x));
         case 18:
            return easeIn(QUART);
         case 19:
            return easeOut(QUART);
         case 20:
            return easeInOut(QUART);
         case 21:
            return easeIn(QUINT);
         case 22:
            return easeOut(QUINT);
         case 23:
            return easeInOut(QUINT);
         case 24:
            return easeIn(back(geoEasingCacheKey.customArgument));
         case 25:
            return easeOut(back(geoEasingCacheKey.customArgument));
         case 26:
            return easeInOut(back(geoEasingCacheKey.customArgument));
         case 27:
            return easeIn(elastic(geoEasingCacheKey.customArgument));
         case 28:
            return easeOut(elastic(geoEasingCacheKey.customArgument));
         case 29:
            return easeInOut(elastic(geoEasingCacheKey.customArgument));
         case 30:
            return easeIn(bounce(geoEasingCacheKey.customArgument));
         case 31:
            return easeOut(bounce(geoEasingCacheKey.customArgument));
         case 32:
            return easeInOut(bounce(geoEasingCacheKey.customArgument));
      }
   }

   public static double ease(double d, GeoEasingType geoEasingType, List list) {
      Double d2 = list != null && list.size() >= 1 ? (Double)list.get(0) : null;
      return (Double)((Function)CACHED_EASING.apply(new GeoEasingCacheKey(geoEasingType, d2))).apply(d);
   }

   public static double linear(double d) {
      return d;
   }

   public static double quad(double d) {
      return d * d;
   }

   public static double cubic(double d) {
      return d * d * d;
   }

   public static Function<Double, Double> stepEnd() {
      return d -> {
         return d >= 1.0 ? 1.0 : 0.0;
      };
   }

   public static double sine(double d) {
      return 1.0 - Math.cos((double)((float)(d * Math.PI / 2.0)));
   }

   public static Function<Double, Double> easeIn(Function<Double, Double> function) {
      return function;
   }

   static {
      CACHED_EASING = GeoFunctionMemoizer.memoize(obj -> getEasing((GeoEasingCacheKey)obj));
   }

   public static Function<Double, Double> stepStart() {
      return d -> {
         return d > 0.0 ? 1.0 : 0.0;
      };
   }

   public static Function<Double, Double> easeOut(Function<Double, Double> function) {
      return d -> 1.0 - (Double)function.apply(1.0 - d);
   }

   public static int binarySearch(double d, double[] doubles, boolean flag) {
      if (d < doubles[0]) {
         return 0;
      } else if (d > doubles[doubles.length - 1]) {
         return doubles.length - 1;
      } else {
         int n = 0;
         int n2 = 0;
         int n3 = doubles.length - 1;

         while (n3 - n2 != 1) {
            n = n2 + (n3 - n2) / 2;
            if (d >= doubles[n]) {
               n2 = n;
            } else {
               n3 = n;
            }
         }

         return flag ? n3 : n2;
      }
   }

   public static Function<Double, Double> step(Double d) {
      int n = d != null ? d.intValue() : 2;
      double[] doubles = linspace(n);
      return d2 -> stepLookup(doubles, (Double)d2);
   }

   public static double[] linspace(int n) {
      double d = 1.0;
      if (n < 2) {
         throw new IllegalArgumentException("steps must be > 2, got:" + n);
      } else {
         double d2 = 1.0 / (double)n;
         AtomicInteger atomicInteger = new AtomicInteger();
         return DoubleStream.generate(() -> (double)atomicInteger.getAndIncrement() * d2).limit((long)n).toArray();
      }
   }

   public static double circ(double d) {
      return 1.0 - Math.sqrt(1.0 - d * d);
   }

   public static double expo(double d) {
      return Math.pow(2.0, 10.0 * (d - 1.0));
   }

   public static Function<Double, Double> elastic(Double d) {
      double d2 = (d == null ? 1.0 : d) * Math.PI;
      return d3 -> 1.0 - Math.pow(Math.cos((double)((float)(d3 * Math.PI / 2.0))), 3.0) * Math.cos((double)((float)(d3 * d2)));
   }

   public static Function<Double, Double> bounce(Double d) {
      double d2 = d == null ? 0.5 : d;
      Function<Double, Double> function = d3 -> 7.5625 * d3 * d3;
      Function<Double, Double> function2 = d4 -> 30.25 * d2 * Math.pow(d4 - 0.5454545454545454, 2.0) + 1.0 - d2;
      Function<Double, Double> function3 = d5 -> 121.0 * d2 * d2 * Math.pow(d5 - 0.8181818181818182, 2.0) + 1.0 - d2 * d2;
      Function<Double, Double> function4 = d6 -> 484.0 * d2 * d2 * d2 * Math.pow(d6 - 0.9545454545454546, 2.0) + 1.0 - d2 * d2 * d2;
      return d7 -> min((Double)function.apply(d7), (Double)function2.apply(d7), (Double)function3.apply(d7), (Double)function4.apply(d7));
   }

   public static Function<Double, Double> poly(double d) {
      return d2 -> Math.pow(d2, d);
   }

}
