package net.augustus.cosmetics.model;

import java.util.HashMap;

public class GeoAnimationData {
   public double tick;
   private HashMap boneSnapshotCollection;
   public Object tickListener;
   public boolean isFirstTick;
   public boolean shouldPlayWhilePaused;
   public Double startTick;
   private double resetTickLength;
   private HashMap animationControllers;
   private static int[] flowState;

   public HashMap getBoneSnapshotCollection() {
      return this.boneSnapshotCollection;
   }

   public void clearSnapshotCollection() {
      this.boneSnapshotCollection = new HashMap();
   }

   public GeoAnimationController addAnimationController(GeoAnimationController geoAnimationController) {
      return (GeoAnimationController)(this.animationControllers.put(geoAnimationController.getName(), geoAnimationController));
   }

   public HashMap getAnimationControllers() {
      return this.animationControllers;
   }

   public void setResetSpeedInTicks(long l, double d) {
      int[] flowState2 = getFlowState();
      this.resetTickLength = d < 0.0 ? 0.0 : d;
   }

   public GeoAnimationData() {
      getFlowState();
      this.boneSnapshotCollection = new HashMap();
      this.animationControllers = new HashMap();
      this.isFirstTick = true;
      this.resetTickLength = 1.0;
      this.shouldPlayWhilePaused = false;
   }

   public double getResetTickLength() {
      return this.resetTickLength;
   }

   public void setBoneSnapshotCollection(HashMap map) {
      this.boneSnapshotCollection = map;
   }

   public static void setFlowState(int[] ints) {
      flowState = ints;
   }

   public static int[] getFlowState() {
      return flowState;
   }

   static {
      if (getFlowState() == null) {
         setFlowState(new int[5]);
      }
   }
}
