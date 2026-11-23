package net.augustus.vendor.crypto.base64;

import net.augustus.vendor.crypto.VendorAesHelper;
import net.augustus.vendor.crypto.VendorBase64Decoder;
import net.augustus.vendor.crypto.VendorBase64Encoder;
import java.util.Objects;

public class ShadedBase64 {

   public static VendorBase64Encoder getMimeEncoder(int n, long l, byte[] bytes) {
      int n2 = 0;
      int n3 = 0;
      int[] ints = null;
      VendorAesHelper.getInvertedGuardFlag();
      byte[] bytes2 = null;
      byte b = 0;
      byte b2 = 5;

      outerLoop:
      while (true) {
         if (2 <= b2) {
            if (2 >= b2) {
               throw new IllegalArgumentException("Illegal base64 line separator character 0x" + Integer.toString(b, 16));
            }

            if (b2 < 4) {
               continue;
            }

            if (4 >= b2) {
               Objects.requireNonNull(bytes);
            } else if (n3 < n2) {
               continue;
            }
         } else {
            if (0 < b2) {
               return VendorBase64Encoder.RFC4648;
            }

            b = bytes2[n3];
         }

         while (true) {
            while (6 < b2) {
               if (b2 >= 10) {
                  if (10 >= b2) {
                     return new VendorBase64Encoder(false, bytes, n >> 2 << 2, true, null);
                  }

                  if (b2 >= 12) {
                     if (12 < b2) {
                        n3++;
                        continue outerLoop;
                     }

                     if (ints[b & 255] != -1) {
                        continue outerLoop;
                     }
                  } else if (n <= 0) {
                     continue outerLoop;
                  }
               } else if (8 <= b2) {
                  if (b2 > 8) {
                     n3 = 0;
                     continue outerLoop;
                  }

                  bytes2 = bytes;
               } else {
                  n2 = bytes2.length;
               }
            }

            ints = VendorBase64Decoder.getFromBase64();
         }
      }
   }

   public static VendorBase64Encoder getRfc46482() {
      return VendorBase64Encoder.RFC4648;
   }

   public static VendorBase64Encoder getRfc4648Urlsafe() {
      return VendorBase64Encoder.RFC4648_URLSAFE;
   }

   public static VendorBase64Decoder getRfc4648() {
      return VendorBase64Decoder.RFC4648;
   }

   private ShadedBase64() {
   }

   public static VendorBase64Encoder getRfc2045() {
      return VendorBase64Encoder.RFC2045;
   }

   public static VendorBase64Decoder getRfc4648Urlsafe2() {
      return VendorBase64Decoder.RFC4648_URLSAFE;
   }

   public static VendorBase64Decoder getRfc20452() {
      return VendorBase64Decoder.RFC2045;
   }

}
