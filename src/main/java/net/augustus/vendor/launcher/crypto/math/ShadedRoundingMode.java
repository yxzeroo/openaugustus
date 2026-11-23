package net.augustus.vendor.launcher.crypto.math;

public enum ShadedRoundingMode {
   UP(0),
   DOWN(1),
   CEILING(2),
   FLOOR(3),
   HALF_UP(4),
   HALF_DOWN(5),
   HALF_EVEN(6),
   UNNECESSARY(7);

   public final int oldMode;

   private ShadedRoundingMode(int n) {
      this.oldMode = n;
   }

   public static ShadedRoundingMode valueOf2(int n) {
      switch (n) {
         case 0:
            return UP;
         case 1:
            return DOWN;
         case 2:
            return CEILING;
         case 3:
            return FLOOR;
         case 4:
            return HALF_UP;
         case 5:
            return HALF_DOWN;
         case 6:
            return HALF_EVEN;
         case 7:
            return UNNECESSARY;
         default:
            throw new IllegalArgumentException("argument out of range");
      }
   }

   public static ShadedRoundingMode[] values(long l) {
      return values();
   }
}
