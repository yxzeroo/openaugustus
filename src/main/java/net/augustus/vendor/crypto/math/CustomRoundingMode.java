package net.augustus.vendor.crypto.math;



import net.augustus.vendor.crypto.CustomBigInteger;
public enum CustomRoundingMode {
   DOWN(1),
   FLOOR(3),
   HALF_UP(4),
   HALF_EVEN(6),
   CEILING(2),
   HALF_DOWN(5),
   UP(0),
   UNNECESSARY(7);

   public final int oldMode;

   private CustomRoundingMode(int n) {
      this.oldMode = n;
   }

   public static CustomRoundingMode valueOf2(int n) {
      boolean flag = false;
      int[] opaqueStateTag = CustomBigInteger.getOpaqueStateTag();
      if (n == 4) {
         return HALF_UP;
      } else if (n == 6) {
         return HALF_EVEN;
      } else if (n == 5) {
         return HALF_DOWN;
      } else if (n == 7) {
         return UNNECESSARY;
      } else if (n == 3) {
         return FLOOR;
      } else if (n == 0) {
         return UP;
      } else if (n == 1) {
         return DOWN;
      } else if (n == 2) {
         return CEILING;
      } else {
         throw new IllegalArgumentException("argument out of range");
      }
   }

}
