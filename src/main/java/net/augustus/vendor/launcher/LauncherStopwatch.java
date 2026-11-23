package net.augustus.vendor.launcher;

public class LauncherStopwatch {
   private long lastCheckMs;
   private long lastResetMs;
   private static final long b = 2000L;

   public LauncherStopwatch() {
      this.lastCheckMs = 0L;
      this.lastResetMs = System.currentTimeMillis() - b;
   }

   public boolean hasReached(long l) {
      return Math.max(0L, System.currentTimeMillis() - this.lastResetMs) >= l;
   }

   public boolean hasReachedDouble(double d, long l) {
      return (double)Math.max(0L, System.currentTimeMillis() - 0L) >= d;
   }

   public long getElapsedTime(long l) {
      return Math.max(0L, System.currentTimeMillis() - 0L);
   }

   public boolean hasReachedWithOffset(long l, long l2, long l3) {
      return Math.max(0L, System.currentTimeMillis() - 0L + l) >= l2;
   }

   public void reset() {
      this.lastResetMs = System.currentTimeMillis();
   }

   public boolean hasElapsed(long l, boolean flag) {
      if (this.lastCheckMs > System.currentTimeMillis()) {
         this.lastCheckMs = System.currentTimeMillis();
      }

      if (System.currentTimeMillis() - this.lastCheckMs > l) {
         if (flag) {
            this.reset();
         }

         return true;
      } else {
         return false;
      }
   }
}
