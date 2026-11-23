package net.augustus.vendor.crypto;


public class QuadBlockByteMixer {

   private byte[] applyQuadBlockRound(byte[] bytes) {
      byte b = 0;
      byte b2 = 0;
      boolean flag = false;
      byte b3 = 0;
      boolean flag2 = false;
      Object obj = null;
      byte b4 = 0;
      obj = new byte[((byte[])bytes).length];
      System.arraycopy(bytes, 0, (byte[])obj, 0, bytes.length);
      if (bytes.length < 4) {
         return (byte[])obj;
      } else {
         for (int i = 0; i < ((byte[])bytes).length - 3; i += 4) {
            b2 = ((byte[])obj)[i];
            b = ((byte[])obj)[i + 1];
            b3 = ((byte[])obj)[i + 2];
            b4 = ((byte[])obj)[i + 3];
            ((byte[])obj)[i] = (byte)(~b2);
            ((byte[])obj)[i + 1] = (byte)(b3 ^ -86);
            ((byte[])obj)[i + 2] = (byte)(b ^ 85);
            ((byte[])obj)[i + 3] = (byte)(b4 ^ b2);

            for (int j = 0; j < 4; j++) {
               ((byte[])obj)[i + j] = (byte)(((byte[])obj)[i + j] + (byte)(i / (j + 1) + j * 3 - bytes.length));
               if ((((byte[])obj)[i + j] & 255) % (j + 2) == 0) {
                  ((byte[])obj)[i + j] = (byte)(((byte[])obj)[i + j] ^ (byte)(192 | j));
               }
            }
         }

         for (int k = 0; k < ((byte[])obj).length; k++) {
            b2 = k <= 0 ? (byte)k : ((byte[])obj)[k - 1];
            b = k < ((byte[])obj).length - 1 ? ((byte[])obj)[k + 1] : (byte)(bytes.length - k);
            ((byte[])obj)[k] = (byte)(((byte[])obj)[k] ^ b2 ^ b ^ (byte)(51 + k));
         }

         return (byte[])obj;
      }
   }

   private byte[] applyAlternatingRotateRound(byte[] bytes) {
      boolean flag = false;
      int n = 0;
      boolean flag2 = false;
      int n2 = 0;
      byte b = 0;
      Object obj = null;
      obj = new byte[((byte[])bytes).length];

      for (int i = 0; i < ((byte[])bytes).length; i++) {
         n = bytes[i];
         n2 = i * 5 % 3 + 1;
         if (i % 2 != 0) {
            n = (byte)((n & 255) >>> n2 | (n & 255) << 8 - n2);
            n = (byte)(n ^ (byte)(77 - i));
         } else {
            n = (byte)((n & 255) << n2 | (n & 255) >>> 8 - n2);
            n = (byte)(n ^ (byte)(176 + i));
         }

         ((byte[])obj)[i] = (byte)n;

         for (int j = 0; j < 2; j++) {
            b = (byte)(15 << (i + j) % 4);
            if ((n & 255) >> (i + j) % 3 <= 15) {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] & ~b);
            } else {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] | b);
            }

            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] + (byte)(i * 3 - bytes.length / (j + 1) & 0xFF));
         }
      }

      for (int k = 0; k < ((byte[])obj).length; k++) {
         n = ((byte[])obj)[k] & 15;
         n2 = (((byte[])obj)[k] & 240) >> 4;
         if ((n + n2) % 5 == k % 5) {
            ((byte[])obj)[k] = (byte)(((byte[])obj)[k] ^ (byte)(255 - k));
         }
      }

      return (byte[])obj;
   }

   private byte[] applyNeighborDiffusionRound(byte[] bytes) {
      byte b = 0;
      byte b2 = 0;
      Object obj = null;
      Object obj2 = null;
      int n = 0;
      byte b3 = 0;
      boolean flag = false;
      obj = new byte[((byte[])bytes).length];
      System.arraycopy(bytes, 0, (byte[])obj, 0, bytes.length);
      if (((byte[])obj).length == 0) {
         return (byte[])obj;
      } else {
         b2 = (byte)(bytes.length ^ 119);

         for (int i = 0; i < ((byte[])obj).length; i++) {
            b2 = (byte)(b2 ^ ((byte[])obj)[i] ^ (byte)i);
            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] + b2 - (byte)(i * 2));
            if ((b2 & 3) == 1) {
               ((byte[])obj)[i] = (byte)(~((byte[])obj)[i]);
            }

            n = (((byte[])obj)[i] & 255) * (b2 & 255);
            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ (byte)(n & 0xFF) ^ (byte)(n >> 8 & 0xFF));

            for (int j = 0; j < 2; j++) {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ (byte)(j + 160 + i));
               if (i > 0 && (((byte[])obj)[i] & 1) != (((byte[])obj)[i - 1] & 1)) {
                  ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ -16);
               }
            }
         }

         if (((byte[])obj).length > 2) {
            obj2 = new byte[((byte[])obj).length];
            System.arraycopy((byte[])obj, 0, (byte[])obj2, 0, ((byte[])obj).length);

            for (int k = 0; k < ((byte[])obj).length; k++) {
               b3 = k != 0 ? ((byte[])obj2)[k - 1] : ((byte[])obj2)[((byte[])obj).length - 1];
               b = k == ((byte[])obj).length - 1 ? ((byte[])obj2)[0] : ((byte[])obj2)[k + 1];
               ((byte[])obj)[k] = (byte)(((byte[])obj2)[k] ^ b3 ^ b ^ (byte)(91 + k));
               if (k % 4 < 2) {
                  ((byte[])obj)[k] = (byte)(((byte[])obj)[k] + k % 5 + 1);
               } else {
                  ((byte[])obj)[k] = (byte)(((byte[])obj)[k] - (k % 5 + 1));
               }
            }
         }

         return (byte[])obj;
      }
   }

   public byte[] mix(byte[] bytes) {
      Object obj = null;
      boolean flag = false;
      if (bytes == null) {
         return new byte[0];
      } else if (bytes.length == 0) {
         return new byte[0];
      } else {
         obj = new byte[((byte[])bytes).length];

         for (int i = 0; i < ((byte[])bytes).length; i++) {
            ((byte[])obj)[i] = bytes[i];
         }

         QuadBlockByteMixer quadBlockByteMixer = this;
         obj = quadBlockByteMixer.applyInitialXorTableRound((byte[])obj);
         quadBlockByteMixer = this;
         obj = quadBlockByteMixer.applyQuadBlockRound((byte[])obj);
         quadBlockByteMixer = this;
         obj = quadBlockByteMixer.applyAlternatingRotateRound((byte[])obj);
         quadBlockByteMixer = this;
         obj = quadBlockByteMixer.applyNeighborDiffusionRound((byte[])obj);
         return (byte[])obj;
      }
   }

   private byte[] applyInitialXorTableRound(byte[] bytes) {
      boolean flag = false;
      Object obj = null;
      boolean flag2 = false;
      int n = 0;
      Object obj2 = null;
      obj2 = new byte[]{-66, -17, -54, -2, -34, -83, -70, -66, 17, 34, 51, 68, 85, 102, 119, -120};
      obj = new byte[((byte[])bytes).length];

      for (int i = 0; i < ((byte[])bytes).length; i++) {
         ((byte[])obj)[i] = (byte)(bytes[i] ^ ((byte[])obj2)[i % ((byte[])obj2).length]);
         if ((bytes.length - i) % 6 == 1 && ((byte[])obj2).length > 0) {
            n = (byte)(~(i * 19) & ((byte[])obj2)[i * 7 % ((byte[])obj2).length]);
            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ n);
            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ n);
         }

         n = 0;

         for (int j = 0; j < 3; j++) {
            n += ((byte[])obj)[i] & 15 << j;
         }

         if (n > 30) {
            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ (byte)(n & 0xFF));
         }
      }

      return (byte[])obj;
   }

}
