package net.augustus.vendor.crypto;



import net.augustus.settings.Setting;
public class SBoxSubstitutionTokenCipher {
   private byte primaryKeyByte;
   private byte secondaryKeyByte;
   private int[] sBox;

   public byte[] transform(byte[] bytes, int[] ints, long l) {
      Object obj = null;
      boolean flag = false;
      if (bytes != null && bytes.length != 0) {
         if (ints == null || ints.length < 3) {
            ints = new int[]{7, 11, 13, (int)(l & 255L)};
         }

         obj = (byte[])bytes.clone();
         this.primaryKeyByte ^= (byte)((int)(l & 255L));
         this.secondaryKeyByte ^= (byte)((int)(l >> 8 & 255L));
         SBoxSubstitutionTokenCipher cipher = this;
         byte[] bytes2 = (byte[])obj;
         int n = ints[0];
         int n2 = ints[1];
         int n3 = n;
         byte[] bytes3 = bytes2;
         obj = cipher.applyLinearSBoxRound(bytes3, n3, n2);
         cipher = this;
         bytes2 = (byte[])obj;
         byte b = (byte)(ints[2] & 0xFF);
         byte b2 = b;
         byte[] bytes4 = bytes2;
         obj = cipher.applyRotateSBoxRound(bytes4, b2, l);

         for (int i = 0; i < 2; i++) {
            cipher = this;
            byte[] bytes5 = (byte[])obj;
            int n4 = ints[(i + 1) % ((int[])ints).length];
            byte[] bytes6 = bytes5;
            obj = cipher.applyParityInvertRound(bytes6, n4);
            cipher = this;
            bytes2 = (byte[])obj;
            byte b3 = (byte)((int)(l >> i * 16 & 255L));
            byte[] bytes7 = bytes2;
            obj = cipher.applyAlternatingShiftRound(bytes7, b3);
         }

         cipher = this;
         byte[] bytes8 = (byte[])obj;
         int n5 = (int)(l & 65535L);
         int n6 = n5;
         byte[] bytes9 = bytes8;
         obj = cipher.applyChainedSBoxRound(bytes9, n6);
         cipher = this;
         byte[] bytes10 = (byte[])obj;
         byte[] bytes11 = cipher.finalizeAndSwapHalves(bytes10);
         Setting.getControlFlowMarker();
         return bytes11;
      } else {
         return new byte[0];
      }
   }

   public SBoxSubstitutionTokenCipher(int n, byte b) {
      this.sBox = new int[256];
      int n2 = n;

      for (int i = 0; i < 256; i++) {
         n2 = n2 * 1103515245 + 12345 & 2147483647;
         this.sBox[i] = n2 >> 16 & 0xFF;
      }

      this.primaryKeyByte = (byte)(b ^ 153);
      this.secondaryKeyByte = (byte)(b << 4 ^ b >>> 4 ^ 102);
   }

   private byte[] applyAlternatingShiftRound(byte[] bytes, byte b) {
      boolean flag = false;
      Object obj = null;
      int n = 0;
      byte b2 = 0;
      int n2 = 0;
      obj = new byte[((byte[])bytes).length];

      for (int i = 0; i < ((byte[])bytes).length; i++) {
         b2 = bytes[i];
         n2 = b & 7;
         if (i % 2 != 0) {
            if (n2 <= 0) {
               b2 = (byte)(b2 >>> -n2);
            } else {
               b2 = (byte)(b2 << n2);
            }

            ((byte[])obj)[i] = b2;
         } else {
            if (n2 <= 0) {
               n = b2 << -n2;
            } else {
               n = b2 >> n2;
            }

            ((byte[])obj)[i] = (byte)n;
         }

         ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ this.sBox[(i ^ b) & 0xFF]);
      }

      this.secondaryKeyByte = (byte)(this.secondaryKeyByte ^ ((byte[])obj)[0]);
      return (byte[])obj;
   }

   private byte[] applyChainedSBoxRound(byte[] bytes, int n) {
      int n2 = 0;
      byte b = 0;
      byte b2 = 0;
      Object obj = null;
      boolean flag = false;
      if (bytes.length >= 2) {
         obj = (byte[])bytes.clone();
         b2 = 4;

         for (int i = 0; i < ((byte[])obj).length; i++) {
            n2 = i % b2;
            b = (byte)(n >> n2 * 2);
            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] + b);
            if (i <= 0) {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ this.primaryKeyByte);
            } else {
               ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ ((byte[])obj)[i - 1]);
            }

            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ this.sBox[((byte[])obj)[i] & 255 ^ n2]);
         }

         return (byte[])obj;
      } else {
         return (byte[])bytes.clone();
      }
   }

   private byte[] finalizeAndSwapHalves(byte[] bytes) {
      boolean flag = false;
      Object obj = null;
      byte b = 0;
      int n = 0;
      byte b2 = 0;
      obj = new byte[((byte[])bytes).length];
      b2 = (byte)(this.primaryKeyByte ^ this.secondaryKeyByte);

      for (int i = 0; i < ((byte[])bytes).length; i++) {
         ((byte[])obj)[i] = (byte)(bytes[i] ^ b2 ^ this.sBox[i & 0xFF]);
         b2 = (byte)(((byte[])obj)[i] + bytes[i]);
         ((byte[])obj)[i] = (byte)((((byte[])obj)[i] & 240) >> 4 | (((byte[])obj)[i] & 15) << 4);
      }

      if (bytes.length > 1) {
         n = bytes.length / 2;

         for (int j = 0; j < n; j++) {
            b = ((byte[])obj)[j];
            ((byte[])obj)[j] = (byte)(((byte[])obj)[j + n] ^ 18);
            ((byte[])obj)[j + n] = (byte)(b ^ 52);
         }
      }

      return (byte[])obj;
   }

   private byte[] applyLinearSBoxRound(byte[] bytes, int n, int n2) {
      int n3 = 0;
      Object obj = null;
      boolean flag = false;
      byte b = 0;
      int n4 = 0;
      obj = new byte[((byte[])bytes).length];
      b = this.primaryKeyByte;

      for (int i = 0; i < ((byte[])bytes).length; i++) {
         n3 = bytes[i] & 255;
         n4 = n3 * (n & 0xFF) + (b & 255) * (n2 & 0xFF);
         ((byte[])obj)[i] = (byte)(n4 ^ this.sBox[n3] ^ this.secondaryKeyByte);
         b = ((byte[])obj)[i];
         if (i % 4 == 0) {
            ((byte[])obj)[i] = (byte)(((byte[])obj)[i] + (i >> 2));
         }
      }

      return (byte[])obj;
   }

   private byte[] applyParityInvertRound(byte[] bytes, int n) {
      boolean flag = false;
      Object obj = null;
      obj = (byte[])bytes.clone();

      for (int i = 0; i < ((byte[])obj).length; i++) {
         if ((this.sBox[i & 0xFF] & 1) == (n & 1)) {
            ((byte[])obj)[i] = (byte)(~((byte[])obj)[i]);
         }

         ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ this.primaryKeyByte ^ (byte)n);
         ((byte[])obj)[i] = (byte)(((byte[])obj)[i] + (this.sBox[i + n & 0xFF] & 15));
      }

      return (byte[])obj;
   }

   private byte[] applyRotateSBoxRound(byte[] bytes, byte b, long l) {
      Object obj = null;
      boolean flag = false;
      int n = 0;
      byte b2 = 0;
      obj = new byte[((byte[])bytes).length];

      for (int i = 0; i < ((byte[])bytes).length; i++) {
         b2 = bytes[i];
         n = ((b2 & 255) + (b & 255)) % 8;
         if (n == 0) {
            n = 1;
         }

         if ((i & 1) != 0) {
            b2 = (byte)(b2 >>> n | b2 << 8 - n);
         } else {
            b2 = (byte)(b2 << n | b2 >>> 8 - n);
         }

         b2 = (byte)(b2 ^ this.sBox[((byte[])bytes)[(i + ((byte[])bytes).length - 1) % ((byte[])bytes).length] & 255]);
         b2 = (byte)((int)((long)b2 ^ l >> i % 8 * 8));
         ((byte[])obj)[i] = b2;
      }

      this.primaryKeyByte = (byte)(this.primaryKeyByte ^ ((byte[])obj)[((byte[])obj).length / 2]);
      return (byte[])obj;
   }

}
