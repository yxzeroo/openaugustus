package net.augustus.vendor;

public class VendorIntegrityState {
   public static long serverAuthTimestamp;
   public static boolean integrityFlag;




   static {
      integrityFlag = false;
   }
}
