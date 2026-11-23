package net.augustus.vendor.crypto;


public class PeriodicXorByteMixer {

   private byte[] applyPeriodicMaskRound(byte[] bytes) {
      int n = 0;
      boolean flag = false;
      Object obj = null;
      Object obj2 = null;
      obj = new byte[]{26, 43, 60, 77, 94, 111, 112, -127, -110, -93, -76, -59, -42, -25, -8, 9};
      obj2 = new byte[((byte[])bytes).length];

      for (int i = 0; i < ((byte[])bytes).length; i++) {
         ((byte[])obj2)[i] = (byte)(bytes[i] ^ ((byte[])obj)[i % ((byte[])obj).length]);
         if ((i + bytes.length) % 5 == 0 && ((byte[])obj).length > 0) {
            n = (byte)(i * 23 ^ ((byte[])obj)[i * 11 % ((byte[])obj).length]);
            ((byte[])obj2)[i] = (byte)(((byte[])obj2)[i] ^ n);
            ((byte[])obj2)[i] = (byte)(((byte[])obj2)[i] ^ n);
         }

         n = (((byte[])obj2)[i] & 255) + (bytes[i] & 255);
         if (n > 255) {
            ((byte[])obj2)[i] = (byte)(((byte[])obj2)[i] ^ (byte)(bytes.length & 0xFF));
         }
      }

      return (byte[])obj2;
   }

   private byte[] applyDoublePassFeedbackRound(byte[] bytes) {
      Object obj = null;
      boolean flag = false;
      int n = 0;
      byte b = 0;
      boolean flag2 = false;
      byte b2 = 0;
      obj = new byte[((byte[])bytes).length];
      System.arraycopy(bytes, 0, (byte[])obj, 0, bytes.length);
      if (((byte[])obj).length == 0) {
         return (byte[])obj;
      } else {
         for (int i = 0; i < 2; i++) {
            for (int j = 0; j < ((byte[])obj).length; j++) {
               b2 = ((byte[])obj).length > 1 && j > 0 ? ((byte[])obj)[(j * 3 + i) % (((byte[])obj).length - 1)] : (byte)(i ^ 255);
               if (j == 0 && ((byte[])obj).length > 0) {
                  b2 = ((byte[])obj)[0];
               }

               ((byte[])obj)[j] = (byte)(((byte[])obj)[j] ^ b2 ^ (byte)(126 + i - j));
               if ((j + i) % 3 == 0) {
                  ((byte[])obj)[j] = (byte)((((byte[])obj)[j] & 15) << 4 | (((byte[])obj)[j] & 240) >>> 4);
               }

               n = (((byte[])obj)[j] & 255) + (b2 & 255) + i + j + bytes.length;
               b = (byte)(n & 0xFF);
               if (n % 100 < 50) {
                  ((byte[])obj)[j] = (byte)(((byte[])obj)[j] + b);
               } else {
                  ((byte[])obj)[j] = (byte)(((byte[])obj)[j] - b);
               }

               if ((((byte[])obj)[j] & 255) <= 128 || i != 0) {
                  if ((((byte[])obj)[j] & 255) >= 64 || i != 1) {
                     continue;
                  }
               }

               ((byte[])obj)[j] = (byte)(-((byte[])obj)[j]);
            }

            if (((byte[])obj).length > 1) {
               for (int k = ((byte[])obj).length - 1; k > 0; k--) {
                  ((byte[])obj)[k] = (byte)(((byte[])obj)[k] ^ ((byte[])obj)[k - 1] ^ (byte)(i + 209));
               }
            }
         }

         return (byte[])obj;
      }
   }

   private byte[] applyRotateQuarterReverseRound(byte[] bytes) {
      Object obj = null;
      int n = 0;
      boolean flag = false;
      byte b = 0;
      int n2 = 0;
      obj = new byte[((byte[])bytes).length];

      for (int i = 0; i < ((byte[])bytes).length; i++) {
         ((byte[])obj)[i] = bytes[i];
      }

      if (((byte[])obj).length < 2) {
         return (byte[])obj;
      } else {
         for (int j = 0; j < ((byte[])obj).length; j++) {
            n2 = j % 3 + 1;
            ((byte[])obj)[j] = (byte)((bytes[j] & 255) << n2 | (bytes[j] & 255) >>> 8 - n2);
            ((byte[])obj)[j] = (byte)(((byte[])obj)[j] ^ (byte)(170 + j));
            b = (byte)(j * 17 & 0xFF ^ bytes.length / (j + 1) & 0xFF);
            if (j % 2 != 0) {
               ((byte[])obj)[j] = (byte)(((byte[])obj)[j] - b);
            } else {
               ((byte[])obj)[j] = (byte)(((byte[])obj)[j] + b);
            }

            for (int k = 0; k < 2; k++) {
               ((byte[])obj)[j] = (byte)(((byte[])obj)[j] ^ (byte)(k * 5 + j + bytes.length));
               if ((((byte[])obj)[j] & 15) > (k + 1) * 2) {
                  ((byte[])obj)[j] = (byte)(~((byte[])obj)[j]);
               }
            }
         }

         if (bytes.length > 4) {
            n = bytes.length / 4;

            for (int m = 0; m < n / 2; m++) {
               b = ((byte[])obj)[m];
               ((byte[])obj)[m] = ((byte[])obj)[n - 1 - m];
               ((byte[])obj)[n - 1 - m] = b;
            }
         }

         return (byte[])obj;
      }
   }

   private byte[] applyBranchingShiftRound(byte[] bytes) {
      byte b = 0;
      Object obj = null;
      boolean flag = false;
      int n = 0;
      byte b2 = 0;
      obj = new byte[((byte[])bytes).length];

      for (int i = 0; i < ((byte[])bytes).length; i++) {
         b = bytes[i];
         if (i % 4 == 0 || (b & -16) == -16) {
            ((byte[])obj)[i] = (byte)(b ^ (byte)(204 + i));
            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] + (byte)(i & 15));
         } else if ((b & 15) <= 8) {
            ((byte[])obj)[i] = (byte)(b ^ 85);
         } else {
            ((byte[])obj)[i] = (byte)(b - (byte)(i + 1));
            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ (byte)(bytes.length & 0xFF));
         }

         for (int j = 0; j < 2; j++) {
            n = (b & 255) * (i + j + 1);
            if (n % (bytes.length + 1) <= bytes.length / 2) {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] + (byte)(17 * j));
            } else {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ (byte)(n & 0xFF));
            }

            ((byte[])obj)[i] = (byte)((((byte[])obj)[i] & 255) >>> j + 1 | (((byte[])obj)[i] & 255) << 8 - (j + 1));
         }
      }

      if (bytes.length > 1) {
         b2 = ((byte[])obj)[((byte[])bytes).length - 1];

         for (int k = 0; k < ((byte[])bytes).length / 2; k++) {
            if ((b2 & 1 << k % 8) != 0) {
               ((byte[])obj)[k] = (byte)(((byte[])obj)[k] ^ ((byte[])obj)[((byte[])bytes).length - 1 - k]);
            }
         }
      }

      return (byte[])obj;
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

         PeriodicXorByteMixer periodicXorByteMixer = this;
         obj = periodicXorByteMixer.applyPeriodicMaskRound((byte[])obj);
         periodicXorByteMixer = this;
         obj = periodicXorByteMixer.applyRotateQuarterReverseRound((byte[])obj);
         periodicXorByteMixer = this;
         obj = periodicXorByteMixer.applyBranchingShiftRound((byte[])obj);
         periodicXorByteMixer = this;
         obj = periodicXorByteMixer.applyDoublePassFeedbackRound((byte[])obj);
         return (byte[])obj;
      }
   }

}
