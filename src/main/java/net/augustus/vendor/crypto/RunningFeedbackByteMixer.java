package net.augustus.vendor.crypto;


public class RunningFeedbackByteMixer {

   private byte[] applyRunningFeedbackRound(byte[] bytes) {
      Object obj = null;
      int n = 0;
      byte b = 0;
      byte b2 = 0;
      int n2 = 0;
      obj = new byte[((byte[])bytes).length];
      System.arraycopy(bytes, 0, (byte[])obj, 0, bytes.length);
      if (((byte[])obj).length == 0) {
         return (byte[])obj;
      } else {
         b = (byte)(227 ^ bytes.length);

         for (int i = 0; i < ((byte[])obj).length; i++) {
            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ b);
            b = (byte)(b + ((byte[])obj)[i] + (byte)i);
            if ((b & 7) != (i & 7)) {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] / 3 + 22);
               if (((byte[])obj)[i] == 0 && bytes.length % (i + 1) != 0) {
                  ((byte[])obj)[i] = (byte)(i ^ 255);
               }
            } else {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] * 3 - 17);
            }

            for (int j = 0; j < 2; j++) {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ (byte)(112 + j * 8 + i));
               b2 = (byte)(207 << j);
               if (i % (j + 2) != 0) {
                  ((byte[])obj)[i] = (byte)(((byte[])obj)[i] & ~b2);
               } else {
                  ((byte[])obj)[i] = (byte)(((byte[])obj)[i] | b2);
               }
            }
         }

         if (((byte[])obj).length > 1) {
            n = (b & 255) % ((byte[])obj).length;
            n2 = (b & 255) * 17 % ((byte[])obj).length;
            if (n != n2) {
               b2 = ((byte[])obj)[n];
               ((byte[])obj)[n] = ((byte[])obj)[n2];
               ((byte[])obj)[n2] = b2;
            }
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

         RunningFeedbackByteMixer runningFeedbackByteMixer = this;
         obj = runningFeedbackByteMixer.applyCombinedMaskRound((byte[])obj);
         runningFeedbackByteMixer = this;
         obj = runningFeedbackByteMixer.applyNeighborRotateRound((byte[])obj);
         runningFeedbackByteMixer = this;
         obj = runningFeedbackByteMixer.applyChunkReverseRound((byte[])obj);
         runningFeedbackByteMixer = this;
         obj = runningFeedbackByteMixer.applyRunningFeedbackRound((byte[])obj);
         return (byte[])obj;
      }
   }

   private byte[] applyChunkReverseRound(byte[] bytes) {
      byte b = 0;
      boolean flag = false;
      int n = 0;
      boolean flag2 = false;
      int n2 = 0;
      Object obj = null;
      obj = new byte[((byte[])bytes).length];
      System.arraycopy(bytes, 0, (byte[])obj, 0, bytes.length);
      n2 = bytes.length / 4;
      if (n2 < 2) {
         n2 = 2;
      }

      if (bytes.length < n2) {
         return (byte[])obj;
      } else {
         for (int i = 0; i <= ((byte[])bytes).length - n2; i += n2) {
            n = 0;

            for (int j = 0; j < n2; j++) {
               n += ((byte[])obj)[i + j] & 255;
            }

            if (n % (n2 * 2) > n2 / 2) {
               for (int k = 0; k < n2 / 2; k++) {
                  b = ((byte[])obj)[i + k];
                  ((byte[])obj)[i + k] = ((byte[])obj)[i + n2 - 1 - k];
                  ((byte[])obj)[i + n2 - 1 - k] = b;
               }
            }

            for (int m = 0; m < n2; m++) {
               ((byte[])obj)[i + m] = (byte)(((byte[])obj)[i + m] ^ (byte)(n + i + m + 29));
               if (m % 2 != 0) {
                  ((byte[])obj)[i + m] = (byte)(((byte[])obj)[i + m] - (byte)(m * 2));
               } else {
                  ((byte[])obj)[i + m] = (byte)(((byte[])obj)[i + m] + (byte)(m * 3));
               }
            }
         }

         return (byte[])obj;
      }
   }

   private byte[] applyNeighborRotateRound(byte[] bytes) {
      byte b = 0;
      boolean flag = false;
      byte b2 = 0;
      boolean flag2 = false;
      Object obj = null;
      int n = 0;
      obj = new byte[((byte[])bytes).length];
      System.arraycopy(bytes, 0, (byte[])obj, 0, bytes.length);
      if (bytes.length < 3) {
         return (byte[])obj;
      } else {
         for (int i = 0; i < 3; i++) {
            for (int j = 0; j < ((byte[])bytes).length; j++) {
               b2 = ((byte[])obj)[(j + ((byte[])bytes).length - 1) % ((byte[])bytes).length];
               b = ((byte[])obj)[(j + 1) % ((byte[])bytes).length];
               ((byte[])obj)[j] = (byte)(((byte[])obj)[j] ^ b2 ^ b ^ (byte)(143 + i - j + bytes.length));
               if ((j + i) % 2 == 0) {
                  n = (i + 1) % 8;
                  if (n == 0) {
                     n = 1;
                  }

                  ((byte[])obj)[j] = (byte)((((byte[])obj)[j] & 255) << n | (((byte[])obj)[j] & 255) >>> 8 - n);
               }

               for (int k = 0; k < 2; k++) {
                  ((byte[])obj)[j] = (byte)(((byte[])obj)[j] + (byte)(i * k - j * 2 + bytes.length / (k + j + 1)));
                  if ((b2 & 255) + (b & 255) > (((byte[])obj)[j] & 255)) {
                     ((byte[])obj)[j] = (byte)(((byte[])obj)[j] ^ 90);
                  }
               }
            }
         }

         return (byte[])obj;
      }
   }

   private byte[] deriveCombinedMask() {
      Object obj = null;
      Object obj2 = null;
      boolean flag = false;
      Object obj3 = null;
      Object obj4 = null;
      byte b = 0;
      Object obj5 = null;
      obj5 = new byte[]{26, 43, 60, 77, 94, 111, 112, -127, -110, -93, -76, -59, -42, -25, -8, 9};
      obj = new byte[]{-66, -17, -54, -2, -34, -83, -70, -66, 17, 34, 51, 68, 85, 102, 119, -120};
      obj2 = new byte[]{96, 13, -81, -70, -83, -64, -34, 0, -1, -18, -35, -52, -69, -86, -103, -120};
      obj4 = new byte[]{15, 30, 45, 60, 75, 90, 105, 120, -121, -106, -91, -76, -61, -46, -31, -16};
      b = 16;
      obj3 = new byte[b];

      for (int i = 0; i < b; i++) {
         ((byte[])obj3)[i] = (byte)(
            ((byte[])obj5)[i % ((byte[])obj5).length]
               ^ ((byte[])obj)[i % ((byte[])obj).length]
               ^ ((byte[])obj2)[i % ((byte[])obj2).length]
               ^ ((byte[])obj4)[i % ((byte[])obj4).length]
         );
      }

      for (int j = 0; j < b; j++) {
         ((byte[])obj3)[j] = (byte)(((byte[])obj3)[j] ^ (byte)(j * 5 + b));
         ((byte[])obj3)[j] = (byte)(((byte[])obj3)[j] ^ (byte)(j * 5 + b));
      }

      return (byte[])obj3;
   }

   private byte[] applyCombinedMaskRound(byte[] bytes) {
      Object obj = null;
      int n = 0;
      boolean flag = false;
      Object obj2 = null;
      obj = this.deriveCombinedMask();
      obj2 = new byte[((byte[])bytes).length];

      for (int i = 0; i < ((byte[])bytes).length; i++) {
         ((byte[])obj2)[i] = (byte)(bytes[i] ^ ((byte[])obj)[i % ((byte[])obj).length]);
         if ((i + bytes.length + ((byte[])obj).length) % 9 == 4 && ((byte[])obj).length > 0) {
            n = (byte)(i - 51 ^ ((byte[])obj)[i * 13 % ((byte[])obj).length]);
            ((byte[])obj2)[i] = (byte)(((byte[])obj2)[i] ^ n);
            ((byte[])obj2)[i] = (byte)(((byte[])obj2)[i] ^ n);
         }

         n = ((byte[])obj2)[i] & 255 | bytes[i] & 255;
         if ((n & 128) == 0) {
            ((byte[])obj2)[i] = (byte)(((byte[])obj2)[i] - (byte)(n >> 4 & 15));
         } else {
            ((byte[])obj2)[i] = (byte)(((byte[])obj2)[i] + (byte)(n & 15));
         }
      }

      return (byte[])obj2;
   }





}
