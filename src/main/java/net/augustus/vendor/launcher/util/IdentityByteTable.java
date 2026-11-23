package net.augustus.vendor.launcher.util;

public class IdentityByteTable {
   public static byte[] TABLE;

   public void populate() {
      TABLE = new byte[256];
      for (int i = 0; i < 256; i++) {
         TABLE[i] = (byte)i;
      }
   }



}
