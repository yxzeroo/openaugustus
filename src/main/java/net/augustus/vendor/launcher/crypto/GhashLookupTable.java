package net.augustus.vendor.launcher.crypto;


public class GhashLookupTable {
   public final byte[] hashSubkey = new byte[16];
   public final long[][] lookupTable;

   public GhashLookupTable(byte[] bytes) {
      System.arraycopy(bytes, 0, this.hashSubkey, 0, 16);
      this.lookupTable = new long[16][256];
      long l = 0L;
      long l2 = 0L;

      for (int i = 0; i < 8; i++) {
         l = l << 8 | (long)(bytes[i] & 255);
         l2 = l2 << 8 | (long)(bytes[i + 8] & 255);
      }

      for (int j = 0; j < 16; j++) {
         long l3 = l;
         long l4 = l2;

         for (int k = 0; k < j * 8; k++) {
            boolean flag = (l4 & 1L) != 0L;
            l4 = l4 >>> 1 | (l3 & 1L) << 63;
            l3 >>>= 1;
            if (flag) {
               l3 ^= -2233785415175766016L;
            }
         }

         for (int m = 1; m < 256; m++) {
            long l5 = 0L;
            long l6 = 0L;

            for (int idx = 0; idx < 8; idx++) {
               if ((m & 1 << idx) != 0) {
                  l5 ^= l3;
                  l6 ^= l4;
               }

               boolean flag2 = (l4 & 1L) != 0L;
               l4 = l4 >>> 1 | (l3 & 1L) << 63;
               l3 >>>= 1;
               if (flag2) {
                  l3 ^= -2233785415175766016L;
               }
            }

            this.lookupTable[j][m] = l5 << 32 | l6 & 4294967295L;
         }
      }
   }

   public boolean matchesHashSubkey(byte[] bytes) {
      if (bytes.length != this.hashSubkey.length) {
         return false;
      } else {
         for (int i = 0; i < this.hashSubkey.length; i++) {
            if (bytes[i] != this.hashSubkey[i]) {
               return false;
            }
         }

         return true;
      }
   }
}
