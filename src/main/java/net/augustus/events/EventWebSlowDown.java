package net.augustus.events;

import net.augustus.events.listeners.WebSlowDownListener;
import de.florianmichael.dietrichevents2.AbstractEvent;

public class EventWebSlowDown extends AbstractEvent {
   private boolean keepVerticalMotion;
   private boolean keepHorizontalMotion;
   private double verticalFactor;
   private double horizontalFactor;
   public static final int ID = 49;

   public void call2(WebSlowDownListener listener) {
      listener.onWebSlowDown(this);
   }

   public static void reset(EventWebSlowDown event, double d, double d2, boolean flag, boolean flag2) {
      event.reset(d, d2, flag, flag2);
   }

   public void setHorizontalFactor(double d) {
      this.horizontalFactor = d;
   }

   public void setVerticalFactor(double d) {
      this.verticalFactor = d;
   }

   public void setKeepHorizontalMotion(boolean flag) {
      this.keepHorizontalMotion = flag;
   }

   public boolean isKeepVerticalMotion() {
      return this.keepVerticalMotion;
   }

   public double getHorizontalFactor() {
      return this.horizontalFactor;
   }

   @Override
   public void call(Object obj) {
      WebSlowDownListener listener = (WebSlowDownListener)obj;
      this.call2(listener);
   }

   private void reset(double d, double d2, boolean flag, boolean flag2) {
      this.horizontalFactor = d;
      this.verticalFactor = d2;
      this.keepHorizontalMotion = flag;
      this.keepVerticalMotion = flag2;
   }

   public void setKeepVerticalMotion(boolean flag) {
      this.keepVerticalMotion = flag;
   }

   public boolean isKeepHorizontalMotion() {
      return this.keepHorizontalMotion;
   }

   public double getVerticalFactor() {
      return this.verticalFactor;
   }
}
