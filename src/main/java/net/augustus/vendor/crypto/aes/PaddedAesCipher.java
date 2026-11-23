package net.augustus.vendor.crypto.aes;

import java.security.InvalidKeyException;
import java.security.ProviderException;
import java.util.Arrays;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

public final class PaddedAesCipher {
   private final int[] t3;
   private boolean ro12;
   private final byte[] rcon;
   private final int[] u3;
   private final int[] u2;
   private int[] log;
   private final byte[] invSBox;
   private final int[] u1;
   private final int[] t2;
   private boolean ro14;
   private int[][] sessionK;
   private int[] alog;
   private final int[] t5;
   private final byte[] sBox;
   private final int[] t1;
   private final int[] t6;
   private final int[] u4;
   private final int[] t4;
   private int limit;
   private byte[] kBuffer;
   private final int[] t8;
   public byte[] r;
   private final int[] t7;
   private int[] k;

   private byte[] prepareBuffer(byte[] bytes, int n, int n2, boolean flag) throws IllegalBlockSizeException, ShortBufferException {
      int n3 = 0;
      boolean flag2 = false;
      Object obj = null;
      int n4 = 0;
      byte b = 0;
      int n5 = 0;
      b = 0;
      PaddedAesCipher paddedAesCipher = this;
      n3 = paddedAesCipher.getBlockSize();
      int n6 = n2 % n3;
      n4 = Math.addExact(b, n2);
      n5 = 0;
      n5 = this.padLength(n4);
      if (flag && n5 > 0 && n5 != n3) {
         throw new IllegalBlockSizeException("Input length must be multiple of " + n3 + " when decrypting with padded cipher");
      } else if (flag) {
         return bytes;
      } else {
         obj = new byte[Math.addExact(n4, n5)];
         if (n2 != 0) {
            System.arraycopy(bytes, n, (byte[])obj, b, n2);
         }

         if (n5 != 0) {
            paddedAesCipher = this;
            paddedAesCipher.padWithLen((byte[])obj, Math.addExact(b, n2), n5);
         }

         return (byte[])obj;
      }
   }

   public byte[] encrypt(byte[] bytes, int n, int n2) {
      if (n2 <= 0) {
         return null;
      } else {
         try {
            bytes = this.prepareBuffer(bytes, n, n2, false);
         } catch (ShortBufferException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
         }

         n2 = bytes.length;
         byte[] bytes2 = new byte[n2];
         this.checkBlockAlignment(n2, this.getBlockSize());
         this.encryptCbc(bytes, n, n2, bytes2, (byte)0);
         if (bytes2.length % this.getBlockSize() != 0) {
            System.out.println("Writing " + bytes2.length);
         }

         return bytes2;
      }
   }

   private int getRounds(int n) {
      return (n >> 2) + 6;
   }

   public void encryptBlock(byte[] bytes, int n, byte[] bytes2, int n2) {
      this.implEncryptBlock(bytes, n, bytes2, n2);
   }

   private int mul(int n, int n2) {
      return n != 0 && n2 != 0 ? this.alog[(this.log[n & 0xFF] + this.log[n2 & 0xFF]) % 255] : 0;
   }

   private void implDecryptBlock(byte[] bytes, int n, byte[] bytes2, int n2) {
      int n3 = 4;
      int n4 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.k[n3++];
      int n5 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.k[n3++];
      int n6 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.k[n3++];
      int n7 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n] & 255) ^ this.k[n3++];
      if (this.ro12) {
         int n8 = this.t5[n4 >>> 24] ^ this.t6[n7 >>> 16 & 0xFF] ^ this.t7[n6 >>> 8 & 0xFF] ^ this.t8[n5 & 0xFF] ^ this.k[n3++];
         int n9 = this.t5[n5 >>> 24] ^ this.t6[n4 >>> 16 & 0xFF] ^ this.t7[n7 >>> 8 & 0xFF] ^ this.t8[n6 & 0xFF] ^ this.k[n3++];
         int n10 = this.t5[n6 >>> 24] ^ this.t6[n5 >>> 16 & 0xFF] ^ this.t7[n4 >>> 8 & 0xFF] ^ this.t8[n7 & 0xFF] ^ this.k[n3++];
         n7 = this.t5[n7 >>> 24] ^ this.t6[n6 >>> 16 & 0xFF] ^ this.t7[n5 >>> 8 & 0xFF] ^ this.t8[n4 & 0xFF] ^ this.k[n3++];
         n4 = this.t5[n8 >>> 24] ^ this.t6[n7 >>> 16 & 0xFF] ^ this.t7[n10 >>> 8 & 0xFF] ^ this.t8[n9 & 0xFF] ^ this.k[n3++];
         n5 = this.t5[n9 >>> 24] ^ this.t6[n8 >>> 16 & 0xFF] ^ this.t7[n7 >>> 8 & 0xFF] ^ this.t8[n10 & 0xFF] ^ this.k[n3++];
         n6 = this.t5[n10 >>> 24] ^ this.t6[n9 >>> 16 & 0xFF] ^ this.t7[n8 >>> 8 & 0xFF] ^ this.t8[n7 & 0xFF] ^ this.k[n3++];
         n7 = this.t5[n7 >>> 24] ^ this.t6[n10 >>> 16 & 0xFF] ^ this.t7[n9 >>> 8 & 0xFF] ^ this.t8[n8 & 0xFF] ^ this.k[n3++];
         if (this.ro14) {
            n8 = this.t5[n4 >>> 24] ^ this.t6[n7 >>> 16 & 0xFF] ^ this.t7[n6 >>> 8 & 0xFF] ^ this.t8[n5 & 0xFF] ^ this.k[n3++];
            n9 = this.t5[n5 >>> 24] ^ this.t6[n4 >>> 16 & 0xFF] ^ this.t7[n7 >>> 8 & 0xFF] ^ this.t8[n6 & 0xFF] ^ this.k[n3++];
            n10 = this.t5[n6 >>> 24] ^ this.t6[n5 >>> 16 & 0xFF] ^ this.t7[n4 >>> 8 & 0xFF] ^ this.t8[n7 & 0xFF] ^ this.k[n3++];
            n7 = this.t5[n7 >>> 24] ^ this.t6[n6 >>> 16 & 0xFF] ^ this.t7[n5 >>> 8 & 0xFF] ^ this.t8[n4 & 0xFF] ^ this.k[n3++];
            n4 = this.t5[n8 >>> 24] ^ this.t6[n7 >>> 16 & 0xFF] ^ this.t7[n10 >>> 8 & 0xFF] ^ this.t8[n9 & 0xFF] ^ this.k[n3++];
            n5 = this.t5[n9 >>> 24] ^ this.t6[n8 >>> 16 & 0xFF] ^ this.t7[n7 >>> 8 & 0xFF] ^ this.t8[n10 & 0xFF] ^ this.k[n3++];
            n6 = this.t5[n10 >>> 24] ^ this.t6[n9 >>> 16 & 0xFF] ^ this.t7[n8 >>> 8 & 0xFF] ^ this.t8[n7 & 0xFF] ^ this.k[n3++];
            n7 = this.t5[n7 >>> 24] ^ this.t6[n10 >>> 16 & 0xFF] ^ this.t7[n9 >>> 8 & 0xFF] ^ this.t8[n8 & 0xFF] ^ this.k[n3++];
         }
      }

      int n11 = this.t5[n4 >>> 24] ^ this.t6[n7 >>> 16 & 0xFF] ^ this.t7[n6 >>> 8 & 0xFF] ^ this.t8[n5 & 0xFF] ^ this.k[n3++];
      int n12 = this.t5[n5 >>> 24] ^ this.t6[n4 >>> 16 & 0xFF] ^ this.t7[n7 >>> 8 & 0xFF] ^ this.t8[n6 & 0xFF] ^ this.k[n3++];
      int n13 = this.t5[n6 >>> 24] ^ this.t6[n5 >>> 16 & 0xFF] ^ this.t7[n4 >>> 8 & 0xFF] ^ this.t8[n7 & 0xFF] ^ this.k[n3++];
      n7 = this.t5[n7 >>> 24] ^ this.t6[n6 >>> 16 & 0xFF] ^ this.t7[n5 >>> 8 & 0xFF] ^ this.t8[n4 & 0xFF] ^ this.k[n3++];
      n4 = this.t5[n11 >>> 24] ^ this.t6[n7 >>> 16 & 0xFF] ^ this.t7[n13 >>> 8 & 0xFF] ^ this.t8[n12 & 0xFF] ^ this.k[n3++];
      n5 = this.t5[n12 >>> 24] ^ this.t6[n11 >>> 16 & 0xFF] ^ this.t7[n7 >>> 8 & 0xFF] ^ this.t8[n13 & 0xFF] ^ this.k[n3++];
      n6 = this.t5[n13 >>> 24] ^ this.t6[n12 >>> 16 & 0xFF] ^ this.t7[n11 >>> 8 & 0xFF] ^ this.t8[n7 & 0xFF] ^ this.k[n3++];
      n7 = this.t5[n7 >>> 24] ^ this.t6[n13 >>> 16 & 0xFF] ^ this.t7[n12 >>> 8 & 0xFF] ^ this.t8[n11 & 0xFF] ^ this.k[n3++];
      n11 = this.t5[n4 >>> 24] ^ this.t6[n7 >>> 16 & 0xFF] ^ this.t7[n6 >>> 8 & 0xFF] ^ this.t8[n5 & 0xFF] ^ this.k[n3++];
      n12 = this.t5[n5 >>> 24] ^ this.t6[n4 >>> 16 & 0xFF] ^ this.t7[n7 >>> 8 & 0xFF] ^ this.t8[n6 & 0xFF] ^ this.k[n3++];
      n13 = this.t5[n6 >>> 24] ^ this.t6[n5 >>> 16 & 0xFF] ^ this.t7[n4 >>> 8 & 0xFF] ^ this.t8[n7 & 0xFF] ^ this.k[n3++];
      n7 = this.t5[n7 >>> 24] ^ this.t6[n6 >>> 16 & 0xFF] ^ this.t7[n5 >>> 8 & 0xFF] ^ this.t8[n4 & 0xFF] ^ this.k[n3++];
      n4 = this.t5[n11 >>> 24] ^ this.t6[n7 >>> 16 & 0xFF] ^ this.t7[n13 >>> 8 & 0xFF] ^ this.t8[n12 & 0xFF] ^ this.k[n3++];
      n5 = this.t5[n12 >>> 24] ^ this.t6[n11 >>> 16 & 0xFF] ^ this.t7[n7 >>> 8 & 0xFF] ^ this.t8[n13 & 0xFF] ^ this.k[n3++];
      n6 = this.t5[n13 >>> 24] ^ this.t6[n12 >>> 16 & 0xFF] ^ this.t7[n11 >>> 8 & 0xFF] ^ this.t8[n7 & 0xFF] ^ this.k[n3++];
      n7 = this.t5[n7 >>> 24] ^ this.t6[n13 >>> 16 & 0xFF] ^ this.t7[n12 >>> 8 & 0xFF] ^ this.t8[n11 & 0xFF] ^ this.k[n3++];
      n11 = this.t5[n4 >>> 24] ^ this.t6[n7 >>> 16 & 0xFF] ^ this.t7[n6 >>> 8 & 0xFF] ^ this.t8[n5 & 0xFF] ^ this.k[n3++];
      n12 = this.t5[n5 >>> 24] ^ this.t6[n4 >>> 16 & 0xFF] ^ this.t7[n7 >>> 8 & 0xFF] ^ this.t8[n6 & 0xFF] ^ this.k[n3++];
      n13 = this.t5[n6 >>> 24] ^ this.t6[n5 >>> 16 & 0xFF] ^ this.t7[n4 >>> 8 & 0xFF] ^ this.t8[n7 & 0xFF] ^ this.k[n3++];
      n7 = this.t5[n7 >>> 24] ^ this.t6[n6 >>> 16 & 0xFF] ^ this.t7[n5 >>> 8 & 0xFF] ^ this.t8[n4 & 0xFF] ^ this.k[n3++];
      n4 = this.t5[n11 >>> 24] ^ this.t6[n7 >>> 16 & 0xFF] ^ this.t7[n13 >>> 8 & 0xFF] ^ this.t8[n12 & 0xFF] ^ this.k[n3++];
      n5 = this.t5[n12 >>> 24] ^ this.t6[n11 >>> 16 & 0xFF] ^ this.t7[n7 >>> 8 & 0xFF] ^ this.t8[n13 & 0xFF] ^ this.k[n3++];
      n6 = this.t5[n13 >>> 24] ^ this.t6[n12 >>> 16 & 0xFF] ^ this.t7[n11 >>> 8 & 0xFF] ^ this.t8[n7 & 0xFF] ^ this.k[n3++];
      n7 = this.t5[n7 >>> 24] ^ this.t6[n13 >>> 16 & 0xFF] ^ this.t7[n12 >>> 8 & 0xFF] ^ this.t8[n11 & 0xFF] ^ this.k[n3++];
      n11 = this.t5[n4 >>> 24] ^ this.t6[n7 >>> 16 & 0xFF] ^ this.t7[n6 >>> 8 & 0xFF] ^ this.t8[n5 & 0xFF] ^ this.k[n3++];
      n12 = this.t5[n5 >>> 24] ^ this.t6[n4 >>> 16 & 0xFF] ^ this.t7[n7 >>> 8 & 0xFF] ^ this.t8[n6 & 0xFF] ^ this.k[n3++];
      n13 = this.t5[n6 >>> 24] ^ this.t6[n5 >>> 16 & 0xFF] ^ this.t7[n4 >>> 8 & 0xFF] ^ this.t8[n7 & 0xFF] ^ this.k[n3++];
      n7 = this.t5[n7 >>> 24] ^ this.t6[n6 >>> 16 & 0xFF] ^ this.t7[n5 >>> 8 & 0xFF] ^ this.t8[n4 & 0xFF] ^ this.k[n3++];
      n4 = this.t5[n11 >>> 24] ^ this.t6[n7 >>> 16 & 0xFF] ^ this.t7[n13 >>> 8 & 0xFF] ^ this.t8[n12 & 0xFF] ^ this.k[n3++];
      n5 = this.t5[n12 >>> 24] ^ this.t6[n11 >>> 16 & 0xFF] ^ this.t7[n7 >>> 8 & 0xFF] ^ this.t8[n13 & 0xFF] ^ this.k[n3++];
      n6 = this.t5[n13 >>> 24] ^ this.t6[n12 >>> 16 & 0xFF] ^ this.t7[n11 >>> 8 & 0xFF] ^ this.t8[n7 & 0xFF] ^ this.k[n3++];
      n7 = this.t5[n7 >>> 24] ^ this.t6[n13 >>> 16 & 0xFF] ^ this.t7[n12 >>> 8 & 0xFF] ^ this.t8[n11 & 0xFF] ^ this.k[n3++];
      n11 = this.t5[n4 >>> 24] ^ this.t6[n7 >>> 16 & 0xFF] ^ this.t7[n6 >>> 8 & 0xFF] ^ this.t8[n5 & 0xFF] ^ this.k[n3++];
      n12 = this.t5[n5 >>> 24] ^ this.t6[n4 >>> 16 & 0xFF] ^ this.t7[n7 >>> 8 & 0xFF] ^ this.t8[n6 & 0xFF] ^ this.k[n3++];
      n13 = this.t5[n6 >>> 24] ^ this.t6[n5 >>> 16 & 0xFF] ^ this.t7[n4 >>> 8 & 0xFF] ^ this.t8[n7 & 0xFF] ^ this.k[n3++];
      n7 = this.t5[n7 >>> 24] ^ this.t6[n6 >>> 16 & 0xFF] ^ this.t7[n5 >>> 8 & 0xFF] ^ this.t8[n4 & 0xFF] ^ this.k[n3++];
      n5 = this.k[0];
      bytes2[n2++] = (byte)(this.invSBox[n11 >>> 24] ^ n5 >>> 24);
      bytes2[n2++] = (byte)(this.invSBox[n7 >>> 16 & 0xFF] ^ n5 >>> 16);
      bytes2[n2++] = (byte)(this.invSBox[n13 >>> 8 & 0xFF] ^ n5 >>> 8);
      bytes2[n2++] = (byte)(this.invSBox[n12 & 0xFF] ^ n5);
      n5 = this.k[1];
      bytes2[n2++] = (byte)(this.invSBox[n12 >>> 24] ^ n5 >>> 24);
      bytes2[n2++] = (byte)(this.invSBox[n11 >>> 16 & 0xFF] ^ n5 >>> 16);
      bytes2[n2++] = (byte)(this.invSBox[n7 >>> 8 & 0xFF] ^ n5 >>> 8);
      bytes2[n2++] = (byte)(this.invSBox[n13 & 0xFF] ^ n5);
      n5 = this.k[2];
      bytes2[n2++] = (byte)(this.invSBox[n13 >>> 24] ^ n5 >>> 24);
      bytes2[n2++] = (byte)(this.invSBox[n12 >>> 16 & 0xFF] ^ n5 >>> 16);
      bytes2[n2++] = (byte)(this.invSBox[n11 >>> 8 & 0xFF] ^ n5 >>> 8);
      bytes2[n2++] = (byte)(this.invSBox[n7 & 0xFF] ^ n5);
      n5 = this.k[3];
      bytes2[n2++] = (byte)(this.invSBox[n7 >>> 24] ^ n5 >>> 24);
      bytes2[n2++] = (byte)(this.invSBox[n13 >>> 16 & 0xFF] ^ n5 >>> 16);
      bytes2[n2++] = (byte)(this.invSBox[n12 >>> 8 & 0xFF] ^ n5 >>> 8);
      bytes2[n2] = (byte)(this.invSBox[n11 & 0xFF] ^ n5);
   }

   public void decryptBlock(byte[] bytes, int n, byte[] bytes2, int n2) {
      this.implDecryptBlock(bytes, n, bytes2, n2);
   }

   private int encryptCbc(byte[] bytes, int n, int n2, byte[] bytes2, int n3) {
      int n4 = 0;
      int n5 = 0;
      n4 = n + n2;

      while (n < n4) {
         n5 = 0;

         while (true) {
            if (n5 >= this.getBlockSize()) {
               this.encryptBlock(this.kBuffer, (byte)0, bytes2, n3);
               System.arraycopy(bytes2, n3, this.r, 0, this.getBlockSize());
               n += this.getBlockSize();
               n3 += this.getBlockSize();
               break;
            }

            this.kBuffer[n5] = (byte)(bytes[n5 + n] ^ this.r[n5]);
            n5++;
         }
      }

      return n2;
   }

   public void checkBlockAlignment(int n, int n2) {
      if (n % n2 != 0) {
         throw new ProviderException("Internal error in input buffering");
      }
   }

   public PaddedAesCipher() {
      this.ro12 = false;
      this.ro14 = false;
      this.sessionK = (int[][])null;
      this.k = null;
      this.limit = 0;
      this.alog = new int[256];
      this.log = new int[256];
      this.sBox = new byte[256];
      this.invSBox = new byte[256];
      this.t1 = new int[256];
      this.t2 = new int[256];
      this.t3 = new int[256];
      this.t4 = new int[256];
      this.t5 = new int[256];
      this.t6 = new int[256];
      this.t7 = new int[256];
      this.t8 = new int[256];
      this.u1 = new int[256];
      this.u2 = new int[256];
      this.u3 = new int[256];
      this.u4 = new int[256];
      this.rcon = new byte[30];
      this.kBuffer = new byte[this.getBlockSize()];
      this.r = new byte[this.getBlockSize()];
      int n = 0;
      this.alog[0] = 1;

      for (int i = 1; i < 256; i++) {
         n = this.alog[i - 1] << 1 ^ this.alog[i - 1];
         if ((n & 256) != 0) {
            n ^= (short)283;
         }

         this.alog[i] = n;
      }

      int n2 = 1;

      while (n2 < 255) {
         this.log[this.alog[n2]] = n2++;
      }

      byte[][] bytesArr = new byte[][]{
         {1, 1, 1, 1, 1, 0, 0, 0},
         {0, 1, 1, 1, 1, 1, 0, 0},
         {0, 0, 1, 1, 1, 1, 1, 0},
         {0, 0, 0, 1, 1, 1, 1, 1},
         {1, 0, 0, 0, 1, 1, 1, 1},
         {1, 1, 0, 0, 0, 1, 1, 1},
         {1, 1, 1, 0, 0, 0, 1, 1},
         {1, 1, 1, 1, 0, 0, 0, 1}
      };
      byte[] bytes = new byte[]{0, 1, 1, 0, 0, 0, 1, 1};
      byte[][] bytesArr2 = new byte[256][8];
      bytesArr2[1][7] = 1;

      for (int j = 2; j < 256; j++) {
         n = this.alog[255 - this.log[j]];

         for (int m = 0; m < 8; m++) {
            bytesArr2[j][m] = (byte)(n >>> 7 - m & 1);
         }
      }

      byte[][] bytesArr3 = new byte[256][8];

      for (int idx = 0; idx < 256; idx++) {
         for (int index = 0; index < 8; index++) {
            bytesArr3[idx][index] = bytes[index];

            for (int i2 = 0; i2 < 8; i2++) {
               bytesArr3[idx][index] = (byte)(bytesArr3[idx][index] ^ bytesArr[index][i2] * bytesArr2[idx][i2]);
            }
         }
      }

      for (int i3 = 0; i3 < 256; i3++) {
         this.sBox[i3] = (byte)(bytesArr3[i3][0] << 7);

         for (int i4 = 1; i4 < 8; i4++) {
            this.sBox[i3] = (byte)(this.sBox[i3] ^ bytesArr3[i3][i4] << 7 - i4);
         }

         this.invSBox[this.sBox[i3] & 255] = (byte)i3;
      }

      byte[][] bytesArr4 = new byte[][]{{2, 1, 1, 3}, {3, 2, 1, 1}, {1, 3, 2, 1}, {1, 1, 3, 2}};
      byte[][] bytesArr5 = new byte[4][8];

      for (int i5 = 0; i5 < 4; i5++) {
         for (int i6 = 0; i6 < 4; i6++) {
            bytesArr5[i5][i6] = bytesArr4[i5][i6];
         }

         bytesArr5[i5][i5 + 4] = 1;
      }

      byte[][] bytesArr6 = new byte[4][4];

      for (int i7 = 0; i7 < 4; i7++) {
         byte b = bytesArr5[i7][i7];
         if (b == 0) {
            int n3 = i7 + 1;

            while (bytesArr5[n3][i7] == 0) {
               n3++;
            }

            for (int i8 = 0; i8 < 8; i8++) {
               byte b2 = bytesArr5[i7][i8];
               bytesArr5[i7][i8] = bytesArr5[n3][i8];
               bytesArr5[n3][i8] = b2;
            }

            b = bytesArr5[i7][i7];
         }

         for (int i9 = 0; i9 < 8; i9++) {
            if (bytesArr5[i7][i9] != 0) {
               bytesArr5[i7][i9] = (byte)this.alog[(255 + this.log[bytesArr5[i7][i9] & 255] - this.log[b & 255]) % 255];
            }
         }

         for (int i10 = 0; i10 < 4; i10++) {
            if (i7 != i10) {
               for (int i11 = i7 + 1; i11 < 8; i11++) {
                  bytesArr5[i10][i11] = (byte)(bytesArr5[i10][i11] ^ this.mul(bytesArr5[i7][i11], bytesArr5[i10][i7]));
               }

               bytesArr5[i10][i7] = 0;
            }
         }
      }

      for (int i12 = 0; i12 < 4; i12++) {
         for (int i13 = 0; i13 < 4; i13++) {
            bytesArr6[i12][i13] = bytesArr5[i12][i13 + 4];
         }
      }

      for (int i14 = 0; i14 < 256; i14++) {
         byte b7 = this.sBox[i14];
         byte[] bytes3 = bytesArr4[0];
         this.t1[i14] = this.mul4(b7, bytes3);
         byte[] bytes4 = bytesArr4[1];
         this.t2[i14] = this.mul4(b7, bytes4);
         byte[] bytes5 = bytesArr4[2];
         this.t3[i14] = this.mul4(b7, bytes5);
         byte[] bytes6 = bytesArr4[3];
         this.t4[i14] = this.mul4(b7, bytes6);
         b7 = this.invSBox[i14];
         byte[] bytes7 = bytesArr6[0];
         this.t5[i14] = this.mul4(b7, bytes7);
         byte[] bytes8 = bytesArr6[1];
         this.t6[i14] = this.mul4(b7, bytes8);
         byte[] bytes9 = bytesArr6[2];
         this.t7[i14] = this.mul4(b7, bytes9);
         byte[] bytes10 = bytesArr6[3];
         this.t8[i14] = this.mul4(b7, bytes10);
         byte[] bytes11 = bytesArr6[0];
         this.u1[i14] = this.mul4(i14, bytes11);
         byte[] bytes12 = bytesArr6[1];
         this.u2[i14] = this.mul4(i14, bytes12);
         byte[] bytes13 = bytesArr6[2];
         this.u3[i14] = this.mul4(i14, bytes13);
         byte[] bytes14 = bytesArr6[3];
         this.u4[i14] = this.mul4(i14, bytes14);
      }

      this.rcon[0] = 1;
      int n4 = 1;

      for (int i15 = 1; i15 < 30; i15++) {
         n4 = this.mul((byte)2, n4);
         this.rcon[i15] = (byte)n4;
      }

      this.log = null;
      this.alog = null;
   }

   private void implEncryptBlock(byte[] bytes, int n, byte[] bytes2, int n2) {
      int n3 = 0;
      int n4 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.k[n3++];
      int n5 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.k[n3++];
      int n6 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.k[n3++];
      int n7 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.k[n3++];

      while (n3 < this.limit) {
         int n8 = this.t1[n4 >>> 24] ^ this.t2[n5 >>> 16 & 0xFF] ^ this.t3[n6 >>> 8 & 0xFF] ^ this.t4[n7 & 0xFF] ^ this.k[n3++];
         int n9 = this.t1[n5 >>> 24] ^ this.t2[n6 >>> 16 & 0xFF] ^ this.t3[n7 >>> 8 & 0xFF] ^ this.t4[n4 & 0xFF] ^ this.k[n3++];
         int n10 = this.t1[n6 >>> 24] ^ this.t2[n7 >>> 16 & 0xFF] ^ this.t3[n4 >>> 8 & 0xFF] ^ this.t4[n5 & 0xFF] ^ this.k[n3++];
         n7 = this.t1[n7 >>> 24] ^ this.t2[n4 >>> 16 & 0xFF] ^ this.t3[n5 >>> 8 & 0xFF] ^ this.t4[n6 & 0xFF] ^ this.k[n3++];
         n4 = n8;
         n5 = n9;
         n6 = n10;
      }

      int n11 = this.k[n3++];
      bytes2[n2++] = (byte)(this.sBox[n4 >>> 24] ^ n11 >>> 24);
      bytes2[n2++] = (byte)(this.sBox[n5 >>> 16 & 0xFF] ^ n11 >>> 16);
      bytes2[n2++] = (byte)(this.sBox[n6 >>> 8 & 0xFF] ^ n11 >>> 8);
      bytes2[n2++] = (byte)(this.sBox[n7 & 0xFF] ^ n11);
      n11 = this.k[n3++];
      bytes2[n2++] = (byte)(this.sBox[n5 >>> 24] ^ n11 >>> 24);
      bytes2[n2++] = (byte)(this.sBox[n6 >>> 16 & 0xFF] ^ n11 >>> 16);
      bytes2[n2++] = (byte)(this.sBox[n7 >>> 8 & 0xFF] ^ n11 >>> 8);
      bytes2[n2++] = (byte)(this.sBox[n4 & 0xFF] ^ n11);
      n11 = this.k[n3++];
      bytes2[n2++] = (byte)(this.sBox[n6 >>> 24] ^ n11 >>> 24);
      bytes2[n2++] = (byte)(this.sBox[n7 >>> 16 & 0xFF] ^ n11 >>> 16);
      bytes2[n2++] = (byte)(this.sBox[n4 >>> 8 & 0xFF] ^ n11 >>> 8);
      bytes2[n2++] = (byte)(this.sBox[n5 & 0xFF] ^ n11);
      n11 = this.k[n3++];
      bytes2[n2++] = (byte)(this.sBox[n7 >>> 24] ^ n11 >>> 24);
      bytes2[n2++] = (byte)(this.sBox[n4 >>> 16 & 0xFF] ^ n11 >>> 16);
      bytes2[n2++] = (byte)(this.sBox[n5 >>> 8 & 0xFF] ^ n11 >>> 8);
      bytes2[n2] = (byte)(this.sBox[n6 & 0xFF] ^ n11);
   }

   public int getBlockSize() {
      return 16;
   }

   private int decryptCbc(byte[] bytes, int n, int n2, byte[] bytes2, int n3) {
      int n4 = 0;
      int n5 = 0;
      n5 = n + n2;

      while (n < n5) {
         this.decryptBlock(bytes, n, this.kBuffer, (byte)0);
         n4 = 0;

         while (true) {
            if (n4 >= this.getBlockSize()) {
               System.arraycopy(bytes, n, this.r, 0, this.getBlockSize());
               n += this.getBlockSize();
               n3 += this.getBlockSize();
               break;
            }

            bytes2[n4 + n3] = (byte)(this.kBuffer[n4] ^ this.r[n4]);
            n4++;
         }
      }

      return n2;
   }

   public byte[] decrypt(byte[] bytes, int n, int n2) {
      Object obj = null;
      int n3 = 0;
      if (n2 > 0) {
         obj = new byte[n2];
         PaddedAesCipher paddedAesCipher = this;
         paddedAesCipher.checkBlockAlignment(n2, this.getBlockSize());
         paddedAesCipher = this;
         paddedAesCipher.decryptCbc(bytes, n, n2, (byte[])obj, (byte)0);
         paddedAesCipher = this;
         n3 = paddedAesCipher.unpad((byte[])obj, (byte)0, bytes.length);
         return Arrays.copyOf((byte[])obj, n3);
      } else {
         return null;
      }
   }

   private int mul4(int n, byte[] bytes) {
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      if (n != 0) {
         n = this.log[n & 0xFF];
         n4 = ((byte[])bytes)[0] == 0 ? 0 : this.alog[(n + this.log[((byte[])bytes)[0] & 255]) % 255] & 0xFF;
         n3 = ((byte[])bytes)[1] != 0 ? this.alog[(n + this.log[((byte[])bytes)[1] & 255]) % 255] & 0xFF : 0;
         n5 = ((byte[])bytes)[2] != 0 ? this.alog[(n + this.log[((byte[])bytes)[2] & 255]) % 255] & 0xFF : 0;
         n2 = ((byte[])bytes)[3] != 0 ? this.alog[(n + this.log[((byte[])bytes)[3] & 255]) % 255] & 0xFF : 0;
         return n4 << 24 | n3 << 16 | n5 << 8 | n2;
      } else {
         return 0;
      }
   }

   public int padLength(int n) {
      return this.getBlockSize() - n % this.getBlockSize();
   }

   public int unpad(byte[] bytes, int n, int n2) {
      int n3 = 0;
      boolean flag = false;
      byte b = 0;
      int n4 = 0;
      int n5 = 0;
      if (bytes != null && n2 != 0) {
         n5 = Math.addExact(n, n2);
         b = bytes[n5 - 1];
         n4 = b & 255;
         if (n4 >= 1) {
            if (n4 <= this.getBlockSize()) {
               n3 = n5 - n4;
               if (n3 < n) {
                  return -1;
               }

               for (int i = n3; i < n5; i++) {
                  if (bytes[i] != b) {
                     return -1;
                  }
               }

               return n3;
            }
         }

         return -1;
      } else {
         return 0;
      }
   }

   private void makeSessionKey(byte[] bytes) throws InvalidKeyException {
      if (bytes == null) {
         throw new InvalidKeyException("Empty key");
      } else {
         int rounds = this.getRounds(bytes.length);
         int n = (rounds + 1) * 4;
         byte b = 4;
         int[][] intsArr = new int[rounds + 1][4];
         int[][] intsArr2 = new int[rounds + 1][4];
         int n2 = bytes.length / 4;
         int[] ints = new int[n2];
         int n3 = 0;

         for (int i = 0; n3 < n2; i += 4) {
            ints[n3] = bytes[i] << 24 | (bytes[i + 1] & 255) << 16 | (bytes[i + 2] & 255) << 8 | bytes[i + 3] & 255;
            n3++;
         }

         int n4 = 0;

         for (int j = 0; j < n2 && n4 < n; n4++) {
            intsArr[n4 / 4][n4 % 4] = ints[j];
            intsArr2[rounds - n4 / 4][n4 % 4] = ints[j];
            j++;
         }

         int n5 = 0;

         while (n4 < n) {
            int n6 = ints[n2 - 1];
            ints[0] ^= this.sBox[n6 >>> 16 & 0xFF] << 24
               ^ (this.sBox[n6 >>> 8 & 0xFF] & 255) << 16
               ^ (this.sBox[n6 & 0xFF] & 255) << 8
               ^ this.sBox[n6 >>> 24] & 255
               ^ this.rcon[n5++] << 24;
            if (n2 != 8) {
               n3 = 1;

               for (int m = 0; n3 < n2; m++) {
                  ints[n3] ^= ints[m];
                  n3++;
               }
            } else {
               n3 = 1;

               for (int idx = 0; n3 < n2 / 2; idx++) {
                  ints[n3] ^= ints[idx];
                  n3++;
               }

               n6 = ints[n2 / 2 - 1];
               ints[n2 / 2] = ints[n2 / 2]
                  ^ this.sBox[n6 & 0xFF] & 255
                  ^ (this.sBox[n6 >>> 8 & 0xFF] & 255) << 8
                  ^ (this.sBox[n6 >>> 16 & 0xFF] & 255) << 16
                  ^ this.sBox[n6 >>> 24] << 24;
               int n7 = n2 / 2;

               for (int index = n7 + 1; index < n2; n7++) {
                  ints[index] ^= ints[n7];
                  index++;
               }
            }

            for (int i2 = 0; i2 < n2 && n4 < n; n4++) {
               intsArr[n4 / 4][n4 % 4] = ints[i2];
               intsArr2[rounds - n4 / 4][n4 % 4] = ints[i2];
               i2++;
            }
         }

         for (int i3 = 1; i3 < rounds; i3++) {
            for (int i4 = 0; i4 < b; i4++) {
               int n8 = intsArr2[i3][i4];
               intsArr2[i3][i4] = this.u1[n8 >>> 24 & 0xFF]
                  ^ this.u2[n8 >>> 16 & 0xFF]
                  ^ this.u3[n8 >>> 8 & 0xFF]
                  ^ this.u4[n8 & 0xFF];
            }
         }

         int[] ints2 = this.expandToSubKey(intsArr, false);
         int[] ints3 = this.expandToSubKey(intsArr2, true);
         this.ro12 = rounds >= 12;
         this.ro14 = rounds == 14;
         this.limit = rounds * 4;
         this.sessionK = new int[][]{ints2, ints3};
      }
   }

   public void padWithLen(byte[] bytes, int n, int n2) throws ShortBufferException {
      byte b = 0;
      int n3 = 0;
      if (bytes != null) {
         n3 = Math.addExact(n, n2);
         if (n3 <= bytes.length) {
            b = (byte)(n2 & 0xFF);
            Arrays.fill(bytes, n, n3, b);
         } else {
            throw new ShortBufferException("Buffer too small to hold padding");
         }
      }
   }

   public void resetIv() {
      PaddedAesCipher paddedAesCipher = this;
      paddedAesCipher.r = new byte[this.getBlockSize()];
      paddedAesCipher = this;
      paddedAesCipher.kBuffer = new byte[this.getBlockSize()];
   }

   public void init(boolean flag, byte[] bytes) {

      try {
         this.makeSessionKey(bytes);
         this.k = this.sessionK[flag ? 1 : 0];
      } catch (InvalidKeyException e) {
      }
   }

   private int[] expandToSubKey(int[][] intsArr, boolean flag) {
      boolean flag2 = false;
      int n = 0;
      Object obj = null;
      n = intsArr.length;
      obj = new int[n * 4];
      if (!flag) {
         for (int i = 0; i < n; i++) {
            System.arraycopy(intsArr[i], 0, (int[])obj, i * 4, 4);
         }
      } else {
         System.arraycopy(intsArr[n - 1], 0, (int[])obj, 0, 4);

         for (int j = 1; j < n; j++) {
            System.arraycopy(intsArr[j - 1], 0, (int[])obj, j * 4, 4);
         }
      }

      return (int[])obj;
   }




}
