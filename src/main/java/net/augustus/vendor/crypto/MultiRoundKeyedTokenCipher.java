package net.augustus.vendor.crypto;


public class MultiRoundKeyedTokenCipher {
   private byte[] subkeyTable;
   private int roundSeed;
   private long accumulator;

   public byte[] transform(byte[] bytes, byte[] bytes2, int n) {
      boolean flag = false;
      Object obj = null;
      Object obj2 = null;
      if (bytes != null && bytes.length != 0) {
         obj = (byte[])bytes2 != null && ((byte[])bytes2).length != 0 ? bytes2 : new byte[]{-1, -18, -35};
         obj2 = (byte[])bytes.clone();
         this.accumulator += (long)(((byte[])bytes).length * n);
         this.roundSeed = this.roundSeed ^ ((byte[])obj)[0];

         for (int i = 0; i < n % 5 + 1; i++) {
            MultiRoundKeyedTokenCipher cipher = this;
            byte[] bytes3 = (byte[])obj2;
            int n2 = (int)(this.accumulator & 255L) + i;
            byte[] bytes4 = bytes3;
            obj2 = cipher.rotateAndMask(bytes4, n2);
            cipher = this;
            byte[] bytes5 = (byte[])obj2;
            byte[] bytes6 = (byte[])obj;
            byte[] bytes7 = bytes5;
            obj2 = cipher.mixWithKeyAndSwapNibbles(bytes7, bytes6, i);
            if (i % 2 == 0) {
               cipher = this;
               byte[] bytes8 = (byte[])obj2;
               byte b = (byte)(this.roundSeed & 0xFF);
               byte b2 = b;
               byte[] bytes9 = bytes8;
               obj2 = cipher.applyForwardFeedback(bytes9, b2);
            } else {
               cipher = this;
               byte[] bytes10 = (byte[])obj2;
               byte b3 = (byte)((int)(this.accumulator >> 8 & 255L));
               byte[] bytes11 = bytes10;
               obj2 = cipher.applyConditionalInvert(bytes11, b3);
            }
         }

         MultiRoundKeyedTokenCipher cipher2 = this;
         byte[] bytes12 = (byte[])obj2;
         int n3 = (int)(this.accumulator & 65535L);
         byte[] bytes13 = bytes12;
         obj2 = cipher2.shuffleWithSubkey(bytes13, n3);
         cipher2 = this;
         byte[] bytes14 = (byte[])obj2;
         int length = bytes.length;
         int n4 = length;
         byte[] bytes15 = bytes14;
         return cipher2.finalizeBlock(bytes15, n4);
      } else {
         return new byte[0];
      }
   }

   private byte[] shuffleWithSubkey(byte[] bytes, int n) {
      int n2 = 0;
      byte b = 0;
      Object obj = null;
      boolean flag = false;
      int n3 = 0;
      obj = (byte[])bytes.clone();
      n3 = ((byte[])obj).length;
      if (n3 == 0) {
         return (byte[])obj;
      } else {
         for (int i = 0; i < n3; i++) {
            n2 = (i + n % n3 + (this.subkeyTable[i % 16] & 255)) % n3;
            if (n2 < 0) {
               n2 += n3;
            }

            b = ((byte[])obj)[i];
            ((byte[])obj)[i] = (byte)(((byte[])obj)[n2] ^ n >> 8 ^ b);
         }

         for (int j = 0; j < n3; j++) {
            ((byte[])obj)[j] = (byte)(((byte[])obj)[j] ^ this.subkeyTable[j % 16]);
         }

         return (byte[])obj;
      }
   }

   private byte[] mixWithKeyAndSwapNibbles(byte[] bytes, byte[] bytes2, int n) {
      boolean flag = false;
      Object obj = null;
      int n2 = 0;
      byte b = 0;
      byte b2 = 0;
      byte b3 = 0;
      obj = (byte[])bytes.clone();
      n2 = bytes2.length;
      if (n2 == 0) {
         n2 = 1;
      }

      for (int i = 0; i < ((byte[])obj).length; i++) {
         b = ((byte[])obj)[i];
         b = (byte)(b ^ bytes2[i % n2]);
         b = (byte)(b ^ this.subkeyTable[(i + n) % 16]);
         b = (byte)(b + (byte)((int)(this.accumulator >> i % 8 * 8)));
         b = (byte)(b - (byte)(this.roundSeed >> n % 4 * 8));
         b2 = (byte)((b & 240) >>> 4);
         b3 = (byte)(b & 15);
         ((byte[])obj)[i] = (byte)(b3 << 4 | b2);
         ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ 170 + n);
      }

      for (int j = 0; j < ((byte[])obj).length - 1; j++) {
         ((byte[])obj)[j] = (byte)(((byte[])obj)[j] ^ ((byte[])obj)[j + 1]);
      }

      return (byte[])obj;
   }

   private byte[] finalizeBlock(byte[] bytes, int n) {
      Object obj = null;
      byte b = 0;
      boolean flag = false;
      byte b2 = 0;
      obj = new byte[n];
      b2 = (byte)((int)(this.accumulator ^ (long)this.roundSeed));

      for (int i = 0; i < n; i++) {
         if (i >= bytes.length) {
            ((byte[])obj)[i] = (byte)(i ^ b2);
         } else {
            ((byte[])obj)[i] = (byte)(bytes[i] ^ b2 ^ this.subkeyTable[i % 16]);
         }
      }

      b = ((byte[])obj)[n - 1];

      for (int j = 0; j < n - 1; j++) {
         ((byte[])obj)[j] = (byte)(((byte[])obj)[j] ^ b);
      }

      return (byte[])obj;
   }

   private byte[] applyForwardFeedback(byte[] bytes, byte b) {
      Object obj = null;
      boolean flag = false;
      byte b2 = 0;
      obj = (byte[])bytes.clone();
      b2 = b;

      for (int i = 0; i < ((byte[])obj).length; i++) {
         ((byte[])obj)[i] = (byte)(((byte[])obj)[i] + b2);
         b2 = (byte)(((byte[])obj)[i] ^ b ^ b2 << 1 ^ this.subkeyTable[i % 16]);
      }

      for (int j = 0; j < ((byte[])obj).length; j++) {
         ((byte[])obj)[j] = (byte)(((byte[])obj)[j] ^ b2);
      }

      return (byte[])obj;
   }

   private byte[] rotateAndMask(byte[] bytes, int n) {
      boolean flag = false;
      byte b = 0;
      Object obj = null;
      int n2 = 0;
      byte b2 = 0;
      obj = new byte[((byte[])bytes).length];
      b = (byte)(n ^ this.roundSeed >> 8);

      for (int i = 0; i < ((byte[])bytes).length; i++) {
         b2 = bytes[i];
         b2 = (byte)(b2 + b + this.subkeyTable[i % 16]);
         n2 = (n + i) % 7 + 1;
         b2 = (byte)(b2 << n2 | b2 >>> 8 - n2);
         b2 = (byte)(b2 ^ this.subkeyTable[(i + n) % 16]);
         ((byte[])obj)[i] = b2;
      }

      for (int j = 0; j < ((byte[])bytes).length; j++) {
         ((byte[])obj)[j] = (byte)(((byte[])obj)[j] ^ (byte)(n + j) ^ b);
      }

      return (byte[])obj;
   }

   public MultiRoundKeyedTokenCipher(long l, int n) {
      this.accumulator = l ^ -2401053089206453570L;
      this.roundSeed = n ^ 305419896;
      this.subkeyTable = new byte[16];
      long l2 = l;

      for (int i = 0; i < 16; i++) {
         l2 = l2 * 6364136223846793005L + 1442695040888963407L;
         this.subkeyTable[i] = (byte)((int)(l2 >> i % 8 * 8));
      }
   }

   private byte[] applyConditionalInvert(byte[] bytes, byte b) {
      boolean flag = false;
      Object obj = null;
      obj = (byte[])bytes.clone();

      for (int i = 0; i < ((byte[])obj).length; i++) {
         if ((bytes[i] & 255 ^ i * 5 ^ b & 255) > 128) {
            ((byte[])obj)[i] = (byte)(~bytes[i]);
         }

         ((byte[])obj)[i] = (byte)(((byte[])obj)[i] + (byte)(this.roundSeed + i));
         ((byte[])obj)[i] = (byte)(((byte[])obj)[i] ^ this.subkeyTable[i % 16]);
      }

      return (byte[])obj;
   }




}
