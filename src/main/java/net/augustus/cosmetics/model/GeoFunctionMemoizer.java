package net.augustus.cosmetics.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class GeoFunctionMemoizer {
   private final Map cache = new ConcurrentHashMap();
   private static int[] flowState;

   public static Function memoize(Function function) {
      return (new GeoFunctionMemoizer()).doMemoize(function);
   }

   private Function doMemoize(Function function) {
      return obj -> this.cache.computeIfAbsent(obj, function);
   }

   public static void setFlowState(int[] ints) {
      flowState = ints;
   }

   public static int[] getFlowState() {
      return flowState;
   }

   static {
      if (getFlowState() == null) {
         setFlowState(new int[2]);
      }
   }
}
