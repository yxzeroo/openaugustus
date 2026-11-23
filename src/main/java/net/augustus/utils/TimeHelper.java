package net.augustus.utils;

public class TimeHelper {
   private volatile long lastTime = System.currentTimeMillis();

   public void setLastTime(long l) {
      this.lastTime = l;
   }

   public boolean reached(double d) {
      return (double)Math.max(0L, System.currentTimeMillis() - this.lastTime) >= d;
   }

   public boolean reached2(long l) {
      return Math.max(0L, System.currentTimeMillis() - this.lastTime) >= l;
   }

   public void reset() {
      this.lastTime = System.currentTimeMillis();
   }

   public long getTime() {
      return Math.max(0L, System.currentTimeMillis() - this.lastTime);
   }




}
