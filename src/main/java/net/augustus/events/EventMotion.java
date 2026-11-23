package net.augustus.events;

import net.augustus.events.listeners.MotionListener;
import de.florianmichael.dietrichevents2.AbstractEvent;
import de.florianmichael.dietrichevents2.StateTypes;

public class EventMotion extends AbstractEvent {
   private float pitch;
   private double y;
   private float yaw;
   private boolean onGround;
   private double z;
   private double x;
   private StateTypes state;
   public static final int ID = 18;
   private static int[] controlFlowState;

   public void setZ(double d) {
      this.z = d;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void call2(MotionListener listener) {
      if (this.state == StateTypes.PRE) {
         listener.onMotion(this);
      } else {
         listener.onMotion();
      }
   }

   public double getY() {
      return this.y;
   }

   public float getYaw() {
      return this.yaw;
   }

   public void call(Object obj) {
      MotionListener listener = (MotionListener)obj;
      this.call2(listener);
   }

   public void setPitch(float f) {
      this.pitch = f;
   }

   public EventMotion() {
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public void setX(double d) {
      this.x = d;
   }

   public EventMotion(boolean flag, float f, float f2, double d, double d2, double d3, StateTypes stateTypes) {
      this.onGround = flag;
      this.yaw = f;
      this.pitch = f2;
      this.x = d;
      this.y = d2;
      this.z = d3;
      this.state = stateTypes;
   }

   public void setOnGround(boolean flag) {
      this.onGround = flag;
   }

   public void setYaw(float f) {
      this.yaw = f;
   }

   public static void reset(EventMotion event, boolean flag, float f, float f2, double d, double d2, double d3, StateTypes stateTypes) {
      event.reset(flag, f, f2, d, d2, d3, stateTypes);
   }

   public double getX() {
      return this.x;
   }

   public double getZ() {
      return this.z;
   }

   private void reset(boolean flag, float f, float f2, double d, double d2, double d3, StateTypes stateTypes) {
      this.onGround = flag;
      this.yaw = f;
      this.pitch = f2;
      this.x = d;
      this.y = d2;
      this.z = d3;
      this.state = stateTypes;
   }

   public void setY(double d) {
      this.y = d;
   }

   public static void setControlFlowState(int[] ints) {
      controlFlowState = ints;
   }

   public static int[] getControlFlowState() {
      return controlFlowState;
   }

   static {
      if (getControlFlowState() != null) {
         setControlFlowState(new int[1]);
      }
   }
}
