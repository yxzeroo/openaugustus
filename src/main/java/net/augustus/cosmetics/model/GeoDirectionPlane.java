package net.augustus.cosmetics.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.Random;

public enum GeoDirectionPlane implements Iterable, Predicate {
   HORIZONTAL,
   VERTICAL;


   public boolean apply2(GeoDirection geoDirection) {
      int[] flowState = GeoVector3f.getFlowState();
      return geoDirection != null && geoDirection.getAxis().getPlane() == this;
   }

   public GeoDirection[] facings() {
      int[] flowState = GeoVector3f.getFlowState();
      switch (GeoDirectionSwitchMap.PLANE_SWITCH_MAP[this.ordinal()]) {
         case 1:
            return new GeoDirection[]{GeoDirection.NORTH, GeoDirection.EAST, GeoDirection.SOUTH, GeoDirection.WEST};
         case 2:
            return new GeoDirection[]{GeoDirection.UP, GeoDirection.DOWN};
         default:
            throw new Error("Someone's been tampering with the universe!");
      }
   }

   public GeoDirection random(Random rand) {
      GeoDirection[] geoDirections = this.facings();
      return geoDirections[rand.nextInt(geoDirections.length)];
   }

   public boolean apply(Object obj) {
      GeoDirection geoDirection = (GeoDirection)obj;
      return this.apply2(geoDirection);
   }

   @Override
   public Iterator iterator() {
      return Iterators.forArray(this.facings());
   }
}
