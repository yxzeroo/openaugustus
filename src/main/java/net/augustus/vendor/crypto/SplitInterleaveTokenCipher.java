package net.augustus.vendor.crypto;


public class SplitInterleaveTokenCipher {
   private long streamState;
   private int keyMix;

   private byte mixByte(byte b, int n, byte b2) {
      int n2 = 0;
      byte b3 = 0;
      b3 = (byte)(b ^ b2);
      b3 = (byte)(b3 ^ this.keyMix >> n % 4 * 8);
      n2 = (n + (b2 & 7)) % 8;
      if (n2 == 0) {
         n2 = 1;
      }

      b3 = (byte)(b3 << n2 | b3 >>> 8 - n2);
      b3 = (byte)(b3 + (byte)((int)(this.streamState & 255L)) + (byte)n);
      return (byte)(b3 ^ 195 + n);
   }

   private byte[] interleaveHalves(byte[] bytes, byte b) {
      Object obj = null;
      boolean flag = false;
      int n = 0;
      if (bytes.length < 4) {
         return (byte[])bytes.clone();
      } else {
         obj = new byte[((byte[])bytes).length];
         n = bytes.length / 2;

         for (int i = 0; i < n; i++) {
            ((byte[])obj)[i * 2] = (byte)(bytes[i] ^ b ^ this.keyMix >> 8);
            ((byte[])obj)[i * 2 + 1] = (byte)((int)((long)(bytes[n + i] ^ b) ^ this.streamState & 255L));
         }

         if (bytes.length % 2 != 0) {
            ((byte[])obj)[((byte[])bytes).length - 1] = (byte)(bytes[((byte[])bytes).length - 1] ^ b ^ 170);
         }

         for (int j = 0; j < ((byte[])obj).length - 1; j++) {
            ((byte[])obj)[j] = (byte)(((byte[])obj)[j] ^ ((byte[])obj)[j + 1]);
         }

         return (byte[])obj;
      }
   }

   private byte[] applyKeyedSwap(byte[] bytes, byte[] bytes2, int n) {
      Object obj = null;
      int n2 = 0;
      byte b = 0;
      boolean flag = false;
      int n3 = 0;
      obj = (byte[])bytes.clone();
      n2 = bytes2.length;
      if (n2 == 0) {
         return (byte[])obj;
      } else {
         for (int i = 0; i < ((byte[])obj).length; i++) {
            ((byte[])obj)[i] = (byte)((int)((long)(((byte[])obj)[i] ^ bytes2[i % n2]) ^ this.streamState >> i % 7 * 8));
            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ n + i);
         }

         for (int j = 0; j < ((byte[])obj).length / 2; j++) {
            n3 = (j * 17 + n) % ((byte[])obj).length;
            if (n3 < 0) {
               n3 += ((byte[])obj).length;
            }

            if (j != n3) {
               b = ((byte[])obj)[j];
               ((byte[])obj)[j] = (byte)(((byte[])obj)[n3] ^ 119);
               ((byte[])obj)[n3] = (byte)(b ^ 136);
            }
         }

         return (byte[])obj;
      }
   }

   private byte[] applyThresholdMask(byte[] bytes, int n) {
      boolean flag = false;
      byte b = 0;
      Object obj = null;
      obj = new byte[((byte[])bytes).length];
      b = (byte)((int)(this.streamState ^ (long)this.keyMix));

      for (int i = 0; i < ((byte[])bytes).length; i++) {
         if (((bytes[i] & 255) + i * 3) % 256 <= (n & 0xFF)) {
            ((byte[])obj)[i] = (byte)(bytes[i] + b);
         } else {
            ((byte[])obj)[i] = (byte)(bytes[i] ^ b);
         }

         ((byte[])obj)[i] = (byte)(((byte[])obj)[i] - (i & 15));
      }

      return (byte[])obj;
   }

   public byte[] transform(byte[] bytes, byte[] bytes2) {
      Object obj = null;
      Object obj2 = null;
      boolean flag = false;
      byte b = 0;
      boolean flag2 = false;
      int n = 0;
      if (bytes != null && bytes.length != 0) {
         if (bytes2 == null || bytes2.length == 0) {
            bytes2 = new byte[]{-34, -83, -66, -17};
         }

         obj = (byte[])bytes.clone();
         this.streamState += (long)((byte[])bytes).length;
         obj2 = bytes2;
         n = ((byte[])obj2).length;

         for (int i = 0; i < n; i++) {
            b = ((byte[])obj2)[i];
            this.keyMix = this.keyMix << 3 ^ b;
         }

         for (int j = 0; j < ((byte[])obj).length; j++) {
            byte[] bytes3 = (byte[])obj;
            SplitInterleaveTokenCipher cipher = this;
            byte b2 = ((byte[])obj)[j];
            byte b3 = bytes2[j % ((byte[])bytes2).length];
            byte b4 = b3;
            byte b5 = b2;
            bytes3[j] = cipher.mixByte(b5, j, b4);
         }

         SplitInterleaveTokenCipher cipher2 = this;
         byte[] bytes4 = (byte[])obj;
         int n2 = (int)(this.streamState & 255L);
         byte[] bytes5 = bytes4;
         obj = cipher2.applyThresholdMask(bytes5, n2);
         cipher2 = this;
         byte[] bytes6 = (byte[])obj;
         byte[] bytes7 = bytes2;
         int n3 = this.keyMix & 0xFF;
         byte[] bytes8 = bytes7;
         byte[] bytes9 = bytes6;
         obj = cipher2.applyKeyedSwap(bytes9, bytes8, n3);

         for (int k = 0; k < 2; k++) {
            cipher2 = this;
            byte[] bytes10 = (byte[])obj;
            byte b6 = (byte)((int)(this.streamState >> k * 8 & 255L));
            byte[] bytes11 = bytes10;
            obj = cipher2.interleaveHalves(bytes11, b6);
            cipher2 = this;
            byte[] bytes12 = (byte[])obj;
            byte b7 = (byte)(this.keyMix & 15);
            byte[] bytes13 = bytes12;
            obj = cipher2.applyPeriodicRotate(bytes13, k, b7);
         }

         cipher2 = this;
         byte[] bytes14 = (byte[])obj;
         return cipher2.finalizeAndMirrorQuarter(bytes14);
      } else {
         return new byte[0];
      }
   }

   public SplitInterleaveTokenCipher(long l, int n) {
      this.streamState = l ^ 0L;
      this.keyMix = n ^ 0;
   }

   private byte[] applyPeriodicRotate(byte[] bytes, int n, byte b) {
      Object obj = null;
      boolean flag = false;
      int n2 = 0;
      obj = (byte[])bytes.clone();

      for (int i = 0; i < ((byte[])obj).length; i++) {
         if ((i + n) % 3 == 0) {
            n2 = b & 7;
            if (n2 == 0) {
               n2 = 1;
            }

            if ((i & 1) != 0) {
               ((byte[])obj)[i] = (byte)(bytes[i] >>> n2 | bytes[i] << 8 - n2);
            } else {
               ((byte[])obj)[i] = (byte)(bytes[i] << n2 | bytes[i] >>> 8 - n2);
            }

            ((byte[])obj)[i] = (byte)((int)((long)((byte[])obj)[i] ^ this.streamState >> i % 8 * 8));
         }

         ((byte[])obj)[i] = (byte)(((byte[])obj)[i] + (byte)n + b);
      }

      return (byte[])obj;
   }

   private byte[] finalizeAndMirrorQuarter(byte[] bytes) {
      int n = 0;
      boolean flag = false;
      Object obj = null;
      byte b = 0;
      byte b2 = 0;
      obj = new byte[((byte[])bytes).length];
      b2 = (byte)((int)(this.streamState ^ (long)this.keyMix));

      for (int i = 0; i < ((byte[])bytes).length; i++) {
         ((byte[])obj)[i] = (byte)(bytes[i] + b2);
         b2 = (byte)(((byte[])obj)[i] ^ bytes[i] ^ b2 >>> 2);
         ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ 255 - (i & 0xFF));
      }

      n = bytes.length / 4;

      for (int j = 0; j < n; j++) {
         b = ((byte[])obj)[j];
         ((byte[])obj)[j] = ((byte[])obj)[n * 2 - 1 - j];
         ((byte[])obj)[n * 2 - 1 - j] = b;
      }

      return (byte[])obj;
   }

}
