package net.augustus.vendor.crypto;


public class NibbleLookupByteMixer {

   private byte[] applyNibbleTableRound(byte[] bytes) {
      byte b = 0;
      int n = 0;
      Object obj = null;
      byte b2 = 0;
      boolean flag = false;
      Object obj2 = null;
      byte b3 = 0;
      boolean flag2 = false;
      obj = new byte[((byte[])bytes).length];
      System.arraycopy(bytes, 0, (byte[])obj, 0, bytes.length);
      if (((byte[])obj).length == 0) {
         return (byte[])obj;
      } else {
         obj2 = new byte[16];

         for (int i = 0; i < 16; i++) {
            ((byte[])obj2)[i] = (byte)(i * 17 + (bytes.length & 15) ^ 60);
         }

         for (int j = 0; j < ((byte[])obj).length; j++) {
            b = ((byte[])obj)[j];
            b3 = (byte)((b & 240) >> 4);
            b2 = (byte)(b & 15);
            ((byte[])obj)[j] = (byte)(((byte[])obj2)[b3] ^ ((byte[])obj2)[b2] ^ (byte)(j + 111));
            if (((b & 255) + j) % 5 != 0) {
               ((byte[])obj)[j] = (byte)(((byte[])obj)[j] - (byte)(bytes.length + j));
            } else {
               ((byte[])obj)[j] = (byte)(((byte[])obj)[j] + (byte)(bytes.length - j));
            }

            for (int k = 0; k < 2; k++) {
               ((byte[])obj)[j] = (byte)(((byte[])obj)[j] ^ (byte)(208 + k - j));
               n = (j + k) % 4 + 1;
               if (k % 2 != 0) {
                  ((byte[])obj)[j] = (byte)((((byte[])obj)[j] & 255) >>> n | (((byte[])obj)[j] & 255) << 8 - n);
               } else {
                  ((byte[])obj)[j] = (byte)((((byte[])obj)[j] & 255) << n | (((byte[])obj)[j] & 255) >>> 8 - n);
               }
            }
         }

         if (((byte[])obj).length > 5) {
            for (int m = 0; m < ((byte[])obj).length; m++) {
               ((byte[])obj)[m] = (byte)(((byte[])obj)[m] ^ ((byte[])obj)[(m + 5) % ((byte[])obj).length] ^ -70);
            }
         }

         return (byte[])obj;
      }
   }

   private byte[] applyAffineChecksumRound(byte[] bytes) {
      int n = 0;
      byte b = 0;
      boolean flag = false;
      Object obj = null;
      byte b2 = 0;
      int n2 = 0;
      obj = new byte[((byte[])bytes).length];

      for (int i = 0; i < ((byte[])bytes).length; i++) {
         b2 = bytes[i];
         b2 = (byte)(b2 * 19 + (byte)(i * 7));
         if (i % 3 != 0) {
            if (i % 3 != 1) {
               b = -16;
            } else {
               b = 85;
            }
         } else {
            b = -86;
         }

         b2 = (byte)(b2 ^ b);
         ((byte[])obj)[i] = b2;

         for (int j = 0; j < 2; j++) {
            n = (b2 & 255) + bytes.length / (i + 1 + j) - i * (j + 1);
            if ((n & 1) != 0) {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] - (byte)(n & 0xFF));
            } else {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] + (byte)(n & 0xFF));
            }

            if ((i + j) % 4 == 0) {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] | 1 << j * 2);
            }

            if ((i + j) % 4 == 1) {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] & ~(1 << j * 2 + 1));
            }
         }
      }

      n2 = 0;

      for (int k = 0; k < ((byte[])obj).length; k++) {
         n2 = n2 + (((byte[])obj)[k] & 255) & 0xFF;
      }

      for (int m = 0; m < ((byte[])obj).length; m++) {
         ((byte[])obj)[m] = (byte)(((byte[])obj)[m] ^ (byte)n2 ^ (byte)(m * 3));
      }

      return (byte[])obj;
   }

   private byte[] applyPairSwapRotateRound(byte[] bytes) {
      byte b = 0;
      byte b2 = 0;
      boolean flag = false;
      byte b3 = 0;
      Object obj = null;
      boolean flag2 = false;
      obj = new byte[((byte[])bytes).length];
      System.arraycopy(bytes, 0, (byte[])obj, 0, bytes.length);
      if (bytes.length < 2) {
         return (byte[])obj;
      } else {
         for (int i = 0; i < ((byte[])bytes).length - 1; i += 2) {
            b3 = ((byte[])obj)[i];
            b2 = ((byte[])obj)[i + 1];
            ((byte[])obj)[i] = (byte)(b2 ^ (byte)(225 + i));
            ((byte[])obj)[i + 1] = (byte)(b3 ^ (byte)(30 - i));
            ((byte[])obj)[i] = (byte)((((byte[])obj)[i] & 255) << 1 | (((byte[])obj)[i] & 255) >>> 7);
            ((byte[])obj)[i + 1] = (byte)((((byte[])obj)[i + 1] & 255) >>> 2 | (((byte[])obj)[i + 1] & 255) << 6);

            for (int j = 0; j < 2; j++) {
               b = (byte)(240 >> j * 2);
               ((byte[])obj)[i + j] = (byte)(((byte[])obj)[i + j] & b);
               ((byte[])obj)[i + j] = (byte)(((byte[])obj)[i + j] | (byte)(bytes.length * (i + j) & ~b & 0xFF));
            }
         }

         if (bytes.length % 2 != 0) {
            ((byte[])obj)[((byte[])bytes).length - 1] = (byte)(((byte[])obj)[((byte[])bytes).length - 1] ^ (byte)(127 + bytes.length));

            for (int k = 0; k < 3; k++) {
               ((byte[])obj)[((byte[])bytes).length - 1] = (byte)(((byte[])obj)[((byte[])bytes).length - 1] - (byte)(k * 5 + bytes.length / (k + 1)));
            }
         }

         return (byte[])obj;
      }
   }

   private byte[] applyMaskTableRound(byte[] bytes) {
      int n = 0;
      Object obj = null;
      Object obj2 = null;
      boolean flag = false;
      boolean flag2 = false;
      obj2 = new byte[]{96, 13, -81, -70, -83, -64, -34, 0, -1, -18, -35, -52, -69, -86, -103, -120};
      obj = new byte[((byte[])bytes).length];

      for (int i = 0; i < ((byte[])bytes).length; i++) {
         ((byte[])obj)[i] = (byte)(bytes[i] ^ ((byte[])obj2)[i % ((byte[])obj2).length]);
         if (i * bytes.length % 7 == 2 && ((byte[])obj2).length > 0) {
            n = (byte)((i ^ 85) + ((byte[])obj2)[i * 5 % ((byte[])obj2).length]);
            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ n);
            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ n);
         }

         n = 0;

         for (int j = 0; j < i % 3 + 1; j++) {
            n += ((byte[])obj)[i] & 1 << j;
         }

         if (n > 0 && i % 2 == 0) {
            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] - (byte)(n & 0xFF));
         } else if (n == 0 && i % 2 != 0) {
            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] + 19);
         }
      }

      return (byte[])obj;
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

         NibbleLookupByteMixer nibbleLookupByteMixer = this;
         byte[] bytes2 = (byte[])obj;
         byte[] bytes3 = bytes2;
         obj = nibbleLookupByteMixer.applyMaskTableRound(bytes3);
         nibbleLookupByteMixer = this;
         byte[] bytes4 = (byte[])obj;
         obj = nibbleLookupByteMixer.applyPairSwapRotateRound(bytes4);
         nibbleLookupByteMixer = this;
         byte[] bytes5 = (byte[])obj;
         obj = nibbleLookupByteMixer.applyAffineChecksumRound(bytes5);
         nibbleLookupByteMixer = this;
         byte[] bytes6 = (byte[])obj;
         obj = nibbleLookupByteMixer.applyNibbleTableRound(bytes6);
         return (byte[])obj;
      }
   }

}
