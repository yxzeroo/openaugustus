package net.augustus.vendor.crypto;


public class CyclicShiftByteMixer {

   private byte[] applyFeedbackRotateRound(byte[] bytes) {
      Object obj = null;
      byte b = 0;
      int n = 0;
      boolean flag = false;
      obj = new byte[((byte[])bytes).length];
      if (bytes.length == 0) {
         return (byte[])obj;
      } else {
         b = (byte)(bytes.length ^ 221);

         for (int i = 0; i < ((byte[])bytes).length; i++) {
            b = (byte)(b + bytes[i] + (byte)i);
            ((byte[])obj)[i] = (byte)(bytes[i] ^ b);
            if ((b & 15) <= 7) {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] + (byte)(b << 4));
            } else {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] - (byte)(b >> 4));
            }

            for (int j = 0; j < 2; j++) {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ (byte)(32 * j + i - (b & 255)));
               if ((b & 1 << j) != 0) {
                  ((byte[])obj)[i] = (byte)(~((byte[])obj)[i]);
               }
            }
         }

         for (int k = 0; k < ((byte[])obj).length; k++) {
            ((byte[])obj)[k] = (byte)(((byte[])obj)[k] ^ (byte)(b - k));
            n = k % 3 + 1;
            ((byte[])obj)[k] = (byte)((((byte[])obj)[k] & 255) << n | (((byte[])obj)[k] & 255) >>> 8 - n);
         }

         return (byte[])obj;
      }
   }

   private byte[] applyCyclicShiftRound(byte[] bytes) {
      byte b = 0;
      Object obj = null;
      boolean flag = false;
      byte b2 = 0;
      int n = 0;
      obj = new byte[((byte[])bytes).length];
      if (bytes.length == 0) {
         return (byte[])obj;
      } else {
         n = 0;

         for (int i = 0; i < ((byte[])bytes).length; i++) {
            n = (n + (bytes[i] & 255)) % bytes.length;
         }

         if (n == 0 && bytes.length > 0) {
            n = 1;
         }

         for (int j = 0; j < ((byte[])bytes).length; j++) {
            ((byte[])obj)[j] = bytes[(j + n) % ((byte[])bytes).length];
            if ((((byte[])obj)[j] & 255) % (j + 1) < bytes.length / 2 % (j + 1)) {
               ((byte[])obj)[j] = (byte)(((byte[])obj)[j] ^ (byte)(255 - j * 2));
            }

            for (int k = 0; k < 2; k++) {
               ((byte[])obj)[j] = (byte)(((byte[])obj)[j] - (byte)(n + j + k));
               b = (byte)(170 >> k);
               if ((j & 1 << k) == 0) {
                  ((byte[])obj)[j] = (byte)(((byte[])obj)[j] | ~b);
               } else {
                  ((byte[])obj)[j] = (byte)(((byte[])obj)[j] & b);
               }
            }
         }

         for (int m = 0; m < ((byte[])obj).length; m++) {
            b2 = (byte)((((byte[])obj)[m] & 240) >> 4);
            b = (byte)(((byte[])obj)[m] & 15);
            ((byte[])obj)[m] = (byte)(b << 4 | b2);
            ((byte[])obj)[m] = (byte)(((byte[])obj)[m] ^ (byte)(n + m + 66));
         }

         return (byte[])obj;
      }
   }

   public byte[] mix(byte[] bytes) {
      boolean flag = false;
      Object obj = null;
      if (bytes == null) {
         return new byte[0];
      } else if (bytes.length == 0) {
         return new byte[0];
      } else {
         obj = new byte[((byte[])bytes).length];

         for (int i = 0; i < ((byte[])bytes).length; i++) {
            ((byte[])obj)[i] = bytes[i];
         }

         CyclicShiftByteMixer cyclicShiftByteMixer = this;
         byte[] bytes2 = (byte[])obj;
         obj = cyclicShiftByteMixer.applyTableXorRound(bytes2);
         cyclicShiftByteMixer = this;
         byte[] bytes3 = (byte[])obj;
         obj = cyclicShiftByteMixer.applyCyclicShiftRound(bytes3);
         cyclicShiftByteMixer = this;
         byte[] bytes4 = (byte[])obj;
         obj = cyclicShiftByteMixer.applyFeedbackRotateRound(bytes4);
         cyclicShiftByteMixer = this;
         byte[] bytes5 = (byte[])obj;
         obj = cyclicShiftByteMixer.applyNibbleAffineRound(bytes5);
         return (byte[])obj;
      }
   }

   private byte[] applyTableXorRound(byte[] bytes) {
      boolean flag = false;
      byte b = 0;
      Object obj = null;
      Object obj2 = null;
      obj = new byte[]{15, 30, 45, 60, 75, 90, 105, 120, -121, -106, -91, -76, -61, -46, -31, -16};
      obj2 = new byte[((byte[])bytes).length];

      for (int i = 0; i < ((byte[])bytes).length; i++) {
         ((byte[])obj2)[i] = (byte)(bytes[i] ^ ((byte[])obj)[i % ((byte[])obj).length]);
         if ((i ^ bytes.length ^ ((byte[])obj).length) % 8 == 3 && ((byte[])obj).length > 0) {
            b = (byte)(~(i + 112) | ((byte[])obj)[i * 3 % ((byte[])obj).length]);
            ((byte[])obj2)[i] = (byte)(((byte[])obj2)[i] ^ b);
            ((byte[])obj2)[i] = (byte)(((byte[])obj2)[i] ^ b);
         }

         for (int j = 0; j < 2; j++) {
            ((byte[])obj2)[i] = (byte)(((byte[])obj2)[i] + (byte)(i * j - bytes.length / (i + j + 1) & 0xFF));
            if ((((byte[])obj2)[i] & 1 << j) != 0) {
               ((byte[])obj2)[i] = (byte)(((byte[])obj2)[i] ^ (byte)(128 >> j));
            }
         }
      }

      return (byte[])obj2;
   }

   private byte[] applyNibbleAffineRound(byte[] bytes) {
      boolean flag = false;
      byte b = 0;
      Object obj = null;
      byte b2 = 0;
      byte b3 = 0;
      byte b4 = 0;
      Object obj2 = null;
      byte b5 = 0;
      obj2 = new byte[((byte[])bytes).length];
      System.arraycopy(bytes, 0, (byte[])obj2, 0, bytes.length);
      if (((byte[])obj2).length == 0) {
         return (byte[])obj2;
      } else {
         obj = new int[16];

         for (int i = 0; i < 16; i++) {
            ((int[])obj)[i] = (i * 7 + 3) % 16;
         }

         for (int j = 0; j < ((byte[])obj2).length; j++) {
            b5 = ((byte[])obj2)[j];
            b3 = 0;
            b = (byte)((b5 & 240) >> 4);
            b4 = (byte)(b5 & 15);
            b3 = (byte)(((int[])obj)[b] << 4 | ((int[])obj)[b4]);
            ((byte[])obj2)[j] = (byte)(b3 ^ (byte)(154 + j - bytes.length));

            for (int k = 0; k < 2; k++) {
               ((byte[])obj2)[j] = (byte)(((byte[])obj2)[j] + (byte)(j * k - ((int[])obj)[k] + 63));
               if ((((byte[])obj2)[j] & 16 << k) != 0) {
                  ((byte[])obj2)[j] = (byte)(((byte[])obj2)[j] ^ (byte)(255 >> k * 2));
               }
            }
         }

         if (((byte[])obj2).length > 1) {
            b2 = ((byte[])obj2)[((byte[])obj2).length - 1];

            for (int m = 0; m < ((byte[])obj2).length; m++) {
               b3 = ((byte[])obj2)[m];
               ((byte[])obj2)[m] = (byte)(((byte[])obj2)[m] ^ b2 ^ (byte)(197 + m));
               b2 = b3;
               if ((m + b3 & 0xFF) % 4 != 0) {
                  if ((m + b3 & 0xFF) % 4 == 1) {
                     ((byte[])obj2)[m] = (byte)(((byte[])obj2)[m] - (byte)(m + 5));
                  }
               } else {
                  ((byte[])obj2)[m] = (byte)(((byte[])obj2)[m] + (byte)(m + 5));
               }
            }
         }

         return (byte[])obj2;
      }
   }

}
