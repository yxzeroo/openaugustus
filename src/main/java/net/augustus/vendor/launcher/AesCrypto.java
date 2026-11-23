package net.augustus.vendor.launcher;

import java.security.InvalidKeyException;
import java.security.ProviderException;
import java.util.Arrays;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

public final class AesCrypto {
   private final int[] T1;
   private final byte[] rcon;
   private final int[] U1;
   private int[][] sessionK;
   private byte[] cbcScratchBlock;
   private int roundLimit;
   private final int[] U3;
   private boolean has14Rounds;
   private final int[] T4;
   private int[] roundKeys;
   private final int[] U2;
   private int[] log;
   private final int[] T6;
   private final int[] U4;
   private boolean hasAtLeast12Rounds;
   private final int[] T7;
   private final int[] T8;
   private final byte[] sBox;
   private final int[] T5;
   public byte[] cbcFeedbackIv;
   private int[] alog;
   private final byte[] invSBox;
   private final int[] T2;
   private final int[] T3;

   private int mul(int n, int n2) {
      return n != 0 && n2 != 0 ? this.alog[(this.log[n & 0xFF] + this.log[n2 & 0xFF]) % 255] : 0;
   }

   public byte[] decrypt(byte[] bytes, int n, int n2) {
      if (n2 <= 0) {
         return null;
      } else {
         byte[] bytes2 = new byte[n2];
         int blockSize = this.getBlockSize();
         this.checkBlockAlignment(n2, blockSize);
         byte b = 0;
         this.decryptCbc(bytes, n, n2, bytes2, b);
         int length = bytes.length;
         byte b2 = 0;
         int n3 = this.unpad(bytes2, b2, length);
         return Arrays.copyOf(bytes2, n3);
      }
   }

   private int[] expandToSubKey(int[][] intsArr, boolean flag) {
      int length = intsArr.length;
      int[] ints = new int[length * 4];
      if (flag) {
         System.arraycopy(intsArr[length - 1], 0, ints, 0, 4);

         for (int i = 1; i < length; i++) {
            System.arraycopy(intsArr[i - 1], 0, ints, i * 4, 4);
         }
      } else {
         for (int j = 0; j < length; j++) {
            System.arraycopy(intsArr[j], 0, ints, j * 4, 4);
         }
      }

      return ints;
   }

   public void checkBlockAlignment(int n, int n2) {
      if (n % n2 != 0) {
         throw new ProviderException("Internal error in input buffering");
      }
   }

   private byte[] prepareInput(byte[] bytes, int n, int n2, boolean flag) throws IllegalBlockSizeException, ShortBufferException {
      byte b = 0;
      int blockSize = this.getBlockSize();
      int n3 = n2 % blockSize;
      int n4 = Math.addExact(b, n2);
      int n5 = 0;
      n5 = this.padLength(n4);
      if (flag && n5 > 0 && n5 != blockSize) {
         throw new IllegalBlockSizeException("Input length must be multiple of " + blockSize + " when decrypting with padded cipher");
      } else if (!flag) {
         byte[] bytes2 = new byte[Math.addExact(n4, n5)];
         if (n2 != 0) {
            System.arraycopy(bytes, n, bytes2, b, n2);
         }

         if (n5 != 0) {
            int n6 = Math.addExact(b, n2);
            this.padWithLen(bytes2, n6, n5);
         }

         return bytes2;
      } else {
         return bytes;
      }
   }

   private int encryptCbc(byte[] bytes, int n, int n2, byte[] bytes2, int n3) {
      int n4 = n + n2;

      while (n < n4) {
         for (int i = 0; i < this.getBlockSize(); i++) {
            this.cbcScratchBlock[i] = (byte)(bytes[i + n] ^ this.cbcFeedbackIv[i]);
         }

         byte b = 0;
         byte[] cbcScratchBlock2 = this.cbcScratchBlock;
         this.encryptBlock(cbcScratchBlock2, b, bytes2, n3);
         System.arraycopy(bytes2, n3, this.cbcFeedbackIv, 0, this.getBlockSize());
         n += this.getBlockSize();
         n3 += this.getBlockSize();
      }

      return n2;
   }

   public void encryptBlock(byte[] bytes, int n, byte[] bytes2, int n2) {
      this.implEncryptBlock(bytes, n, bytes2, n2);
   }

   public byte[] encrypt(byte[] bytes, int n, int n2) {
      if (n2 <= 0) {
         return null;
      } else {
         try {
            boolean flag = false;
            bytes = this.prepareInput(bytes, n, n2, flag);
         } catch (ShortBufferException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
         }

         n2 = bytes.length;
         byte[] bytes2 = new byte[n2];
         int blockSize = this.getBlockSize();
         this.checkBlockAlignment(n2, blockSize);
         byte b = 0;
         this.encryptCbc(bytes, n, n2, bytes2, b);
         if (bytes2.length % this.getBlockSize() != 0) {
            System.out.println("Writing " + bytes2.length);
         }

         return bytes2;
      }
   }

   public int getBlockSize() {
      return 16;
   }

   public void init(boolean flag, byte[] bytes) {

      try {
         this.makeKey(bytes);
         this.roundKeys = this.sessionK[flag ? 1 : 0];
      } catch (InvalidKeyException e) {
      }
   }

   private int getRounds(int n) {
      return (n >> 2) + 6;
   }

   public void resetIv() {
      this.cbcFeedbackIv = new byte[this.getBlockSize()];
      this.cbcScratchBlock = new byte[this.getBlockSize()];
   }

   public int unpad(byte[] bytes, int n, int n2) {
      if (bytes != null && n2 != 0) {
         int n3 = Math.addExact(n, n2);
         byte b = bytes[n3 - 1];
         int n4 = b & 255;
         if (n4 >= 1 && n4 <= this.getBlockSize()) {
            int n5 = n3 - n4;
            if (n5 < n) {
               return -1;
            } else {
               for (int i = n5; i < n3; i++) {
                  if (bytes[i] != b) {
                     return -1;
                  }
               }

               return n5;
            }
         } else {
            return -1;
         }
      } else {
         return 0;
      }
   }

   private void makeKey(byte[] bytes) throws InvalidKeyException {
      int length = bytes.length;
      int rounds = this.getRounds(length);
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

            for (int k = 0; n3 < n2; k++) {
               ints[n3] ^= ints[k];
               n3++;
            }
         } else {
            n3 = 1;

            for (int m = 0; n3 < n2 / 2; m++) {
               ints[n3] ^= ints[m];
               n3++;
            }

            n6 = ints[n2 / 2 - 1];
            ints[n2 / 2] = ints[n2 / 2]
               ^ this.sBox[n6 & 0xFF] & 255
               ^ (this.sBox[n6 >>> 8 & 0xFF] & 255) << 8
               ^ (this.sBox[n6 >>> 16 & 0xFF] & 255) << 16
               ^ this.sBox[n6 >>> 24] << 24;
            int n7 = n2 / 2;

            for (int idx = n7 + 1; idx < n2; n7++) {
               ints[idx] ^= ints[n7];
               idx++;
            }
         }

         for (int index = 0; index < n2 && n4 < n; n4++) {
            intsArr[n4 / 4][n4 % 4] = ints[index];
            intsArr2[rounds - n4 / 4][n4 % 4] = ints[index];
            index++;
         }
      }

      for (int i2 = 1; i2 < rounds; i2++) {
         for (int i3 = 0; i3 < b; i3++) {
            int n8 = intsArr2[i2][i3];
            intsArr2[i2][i3] = this.U1[n8 >>> 24 & 0xFF] ^ this.U2[n8 >>> 16 & 0xFF] ^ this.U3[n8 >>> 8 & 0xFF] ^ this.U4[n8 & 0xFF];
         }
      }

      boolean flag = false;
      int[] ints2 = this.expandToSubKey(intsArr, flag);
      boolean flag2 = true;
      int[] ints3 = this.expandToSubKey(intsArr2, flag2);
      this.hasAtLeast12Rounds = rounds >= 12;
      this.has14Rounds = rounds == 14;
      this.roundLimit = rounds * 4;
      this.sessionK = new int[][]{ints2, ints3};
   }

   public int padLength(int n) {
      return this.getBlockSize() - n % this.getBlockSize();
   }

   private int decryptCbc(byte[] bytes, int n, int n2, byte[] bytes2, int n3) {
      int n4 = n + n2;

      while (n < n4) {
         byte b = 0;
         byte[] cbcScratchBlock2 = this.cbcScratchBlock;
         this.decryptBlock(bytes, n, cbcScratchBlock2, b);

         for (int i = 0; i < this.getBlockSize(); i++) {
            bytes2[i + n3] = (byte)(this.cbcScratchBlock[i] ^ this.cbcFeedbackIv[i]);
         }

         System.arraycopy(bytes, n, this.cbcFeedbackIv, 0, this.getBlockSize());
         n += this.getBlockSize();
         n3 += this.getBlockSize();
      }

      return n2;
   }

   public void padWithLen(byte[] bytes, int n, int n2) throws ShortBufferException {
      if (bytes != null) {
         int n3 = Math.addExact(n, n2);
         if (n3 > bytes.length) {
            throw new ShortBufferException("Buffer too small to hold padding");
         } else {
            byte b = (byte)(n2 & 0xFF);
            Arrays.fill(bytes, n, n3, b);
         }
      }
   }

   private void implDecryptBlock(byte[] bytes, int n, byte[] bytes2, int n2) {
      int n3 = 4;
      int n4 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n5 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n6 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n7 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n] & 255) ^ this.roundKeys[n3++];
      if (this.hasAtLeast12Rounds) {
         int n8 = this.T5[n4 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n6 >>> 8 & 0xFF] ^ this.T8[n5 & 0xFF] ^ this.roundKeys[n3++];
         int n9 = this.T5[n5 >>> 24] ^ this.T6[n4 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n6 & 0xFF] ^ this.roundKeys[n3++];
         int n10 = this.T5[n6 >>> 24] ^ this.T6[n5 >>> 16 & 0xFF] ^ this.T7[n4 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
         n7 = this.T5[n7 >>> 24] ^ this.T6[n6 >>> 16 & 0xFF] ^ this.T7[n5 >>> 8 & 0xFF] ^ this.T8[n4 & 0xFF] ^ this.roundKeys[n3++];
         n4 = this.T5[n8 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n10 >>> 8 & 0xFF] ^ this.T8[n9 & 0xFF] ^ this.roundKeys[n3++];
         n5 = this.T5[n9 >>> 24] ^ this.T6[n8 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n10 & 0xFF] ^ this.roundKeys[n3++];
         n6 = this.T5[n10 >>> 24] ^ this.T6[n9 >>> 16 & 0xFF] ^ this.T7[n8 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
         n7 = this.T5[n7 >>> 24] ^ this.T6[n10 >>> 16 & 0xFF] ^ this.T7[n9 >>> 8 & 0xFF] ^ this.T8[n8 & 0xFF] ^ this.roundKeys[n3++];
         if (this.has14Rounds) {
            n8 = this.T5[n4 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n6 >>> 8 & 0xFF] ^ this.T8[n5 & 0xFF] ^ this.roundKeys[n3++];
            n9 = this.T5[n5 >>> 24] ^ this.T6[n4 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n6 & 0xFF] ^ this.roundKeys[n3++];
            n10 = this.T5[n6 >>> 24] ^ this.T6[n5 >>> 16 & 0xFF] ^ this.T7[n4 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
            n7 = this.T5[n7 >>> 24] ^ this.T6[n6 >>> 16 & 0xFF] ^ this.T7[n5 >>> 8 & 0xFF] ^ this.T8[n4 & 0xFF] ^ this.roundKeys[n3++];
            n4 = this.T5[n8 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n10 >>> 8 & 0xFF] ^ this.T8[n9 & 0xFF] ^ this.roundKeys[n3++];
            n5 = this.T5[n9 >>> 24] ^ this.T6[n8 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n10 & 0xFF] ^ this.roundKeys[n3++];
            n6 = this.T5[n10 >>> 24] ^ this.T6[n9 >>> 16 & 0xFF] ^ this.T7[n8 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
            n7 = this.T5[n7 >>> 24] ^ this.T6[n10 >>> 16 & 0xFF] ^ this.T7[n9 >>> 8 & 0xFF] ^ this.T8[n8 & 0xFF] ^ this.roundKeys[n3++];
         }
      }

      int n11 = this.T5[n4 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n6 >>> 8 & 0xFF] ^ this.T8[n5 & 0xFF] ^ this.roundKeys[n3++];
      int n12 = this.T5[n5 >>> 24] ^ this.T6[n4 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n6 & 0xFF] ^ this.roundKeys[n3++];
      int n13 = this.T5[n6 >>> 24] ^ this.T6[n5 >>> 16 & 0xFF] ^ this.T7[n4 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n6 >>> 16 & 0xFF] ^ this.T7[n5 >>> 8 & 0xFF] ^ this.T8[n4 & 0xFF] ^ this.roundKeys[n3++];
      n4 = this.T5[n11 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n13 >>> 8 & 0xFF] ^ this.T8[n12 & 0xFF] ^ this.roundKeys[n3++];
      n5 = this.T5[n12 >>> 24] ^ this.T6[n11 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n13 & 0xFF] ^ this.roundKeys[n3++];
      n6 = this.T5[n13 >>> 24] ^ this.T6[n12 >>> 16 & 0xFF] ^ this.T7[n11 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n13 >>> 16 & 0xFF] ^ this.T7[n12 >>> 8 & 0xFF] ^ this.T8[n11 & 0xFF] ^ this.roundKeys[n3++];
      n11 = this.T5[n4 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n6 >>> 8 & 0xFF] ^ this.T8[n5 & 0xFF] ^ this.roundKeys[n3++];
      n12 = this.T5[n5 >>> 24] ^ this.T6[n4 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n6 & 0xFF] ^ this.roundKeys[n3++];
      n13 = this.T5[n6 >>> 24] ^ this.T6[n5 >>> 16 & 0xFF] ^ this.T7[n4 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n6 >>> 16 & 0xFF] ^ this.T7[n5 >>> 8 & 0xFF] ^ this.T8[n4 & 0xFF] ^ this.roundKeys[n3++];
      n4 = this.T5[n11 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n13 >>> 8 & 0xFF] ^ this.T8[n12 & 0xFF] ^ this.roundKeys[n3++];
      n5 = this.T5[n12 >>> 24] ^ this.T6[n11 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n13 & 0xFF] ^ this.roundKeys[n3++];
      n6 = this.T5[n13 >>> 24] ^ this.T6[n12 >>> 16 & 0xFF] ^ this.T7[n11 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n13 >>> 16 & 0xFF] ^ this.T7[n12 >>> 8 & 0xFF] ^ this.T8[n11 & 0xFF] ^ this.roundKeys[n3++];
      n11 = this.T5[n4 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n6 >>> 8 & 0xFF] ^ this.T8[n5 & 0xFF] ^ this.roundKeys[n3++];
      n12 = this.T5[n5 >>> 24] ^ this.T6[n4 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n6 & 0xFF] ^ this.roundKeys[n3++];
      n13 = this.T5[n6 >>> 24] ^ this.T6[n5 >>> 16 & 0xFF] ^ this.T7[n4 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n6 >>> 16 & 0xFF] ^ this.T7[n5 >>> 8 & 0xFF] ^ this.T8[n4 & 0xFF] ^ this.roundKeys[n3++];
      n4 = this.T5[n11 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n13 >>> 8 & 0xFF] ^ this.T8[n12 & 0xFF] ^ this.roundKeys[n3++];
      n5 = this.T5[n12 >>> 24] ^ this.T6[n11 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n13 & 0xFF] ^ this.roundKeys[n3++];
      n6 = this.T5[n13 >>> 24] ^ this.T6[n12 >>> 16 & 0xFF] ^ this.T7[n11 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n13 >>> 16 & 0xFF] ^ this.T7[n12 >>> 8 & 0xFF] ^ this.T8[n11 & 0xFF] ^ this.roundKeys[n3++];
      n11 = this.T5[n4 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n6 >>> 8 & 0xFF] ^ this.T8[n5 & 0xFF] ^ this.roundKeys[n3++];
      n12 = this.T5[n5 >>> 24] ^ this.T6[n4 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n6 & 0xFF] ^ this.roundKeys[n3++];
      n13 = this.T5[n6 >>> 24] ^ this.T6[n5 >>> 16 & 0xFF] ^ this.T7[n4 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n6 >>> 16 & 0xFF] ^ this.T7[n5 >>> 8 & 0xFF] ^ this.T8[n4 & 0xFF] ^ this.roundKeys[n3++];
      n4 = this.T5[n11 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n13 >>> 8 & 0xFF] ^ this.T8[n12 & 0xFF] ^ this.roundKeys[n3++];
      n5 = this.T5[n12 >>> 24] ^ this.T6[n11 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n13 & 0xFF] ^ this.roundKeys[n3++];
      n6 = this.T5[n13 >>> 24] ^ this.T6[n12 >>> 16 & 0xFF] ^ this.T7[n11 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n13 >>> 16 & 0xFF] ^ this.T7[n12 >>> 8 & 0xFF] ^ this.T8[n11 & 0xFF] ^ this.roundKeys[n3++];
      n11 = this.T5[n4 >>> 24] ^ this.T6[n7 >>> 16 & 0xFF] ^ this.T7[n6 >>> 8 & 0xFF] ^ this.T8[n5 & 0xFF] ^ this.roundKeys[n3++];
      n12 = this.T5[n5 >>> 24] ^ this.T6[n4 >>> 16 & 0xFF] ^ this.T7[n7 >>> 8 & 0xFF] ^ this.T8[n6 & 0xFF] ^ this.roundKeys[n3++];
      n13 = this.T5[n6 >>> 24] ^ this.T6[n5 >>> 16 & 0xFF] ^ this.T7[n4 >>> 8 & 0xFF] ^ this.T8[n7 & 0xFF] ^ this.roundKeys[n3++];
      n7 = this.T5[n7 >>> 24] ^ this.T6[n6 >>> 16 & 0xFF] ^ this.T7[n5 >>> 8 & 0xFF] ^ this.T8[n4 & 0xFF] ^ this.roundKeys[n3++];
      n5 = this.roundKeys[0];
      bytes2[n2++] = (byte)(this.invSBox[n11 >>> 24] ^ n5 >>> 24);
      bytes2[n2++] = (byte)(this.invSBox[n7 >>> 16 & 0xFF] ^ n5 >>> 16);
      bytes2[n2++] = (byte)(this.invSBox[n13 >>> 8 & 0xFF] ^ n5 >>> 8);
      bytes2[n2++] = (byte)(this.invSBox[n12 & 0xFF] ^ n5);
      n5 = this.roundKeys[1];
      bytes2[n2++] = (byte)(this.invSBox[n12 >>> 24] ^ n5 >>> 24);
      bytes2[n2++] = (byte)(this.invSBox[n11 >>> 16 & 0xFF] ^ n5 >>> 16);
      bytes2[n2++] = (byte)(this.invSBox[n7 >>> 8 & 0xFF] ^ n5 >>> 8);
      bytes2[n2++] = (byte)(this.invSBox[n13 & 0xFF] ^ n5);
      n5 = this.roundKeys[2];
      bytes2[n2++] = (byte)(this.invSBox[n13 >>> 24] ^ n5 >>> 24);
      bytes2[n2++] = (byte)(this.invSBox[n12 >>> 16 & 0xFF] ^ n5 >>> 16);
      bytes2[n2++] = (byte)(this.invSBox[n11 >>> 8 & 0xFF] ^ n5 >>> 8);
      bytes2[n2++] = (byte)(this.invSBox[n7 & 0xFF] ^ n5);
      n5 = this.roundKeys[3];
      bytes2[n2++] = (byte)(this.invSBox[n7 >>> 24] ^ n5 >>> 24);
      bytes2[n2++] = (byte)(this.invSBox[n13 >>> 16 & 0xFF] ^ n5 >>> 16);
      bytes2[n2++] = (byte)(this.invSBox[n12 >>> 8 & 0xFF] ^ n5 >>> 8);
      bytes2[n2] = (byte)(this.invSBox[n11 & 0xFF] ^ n5);
   }

   private int mul4(int n, byte[] bytes) {
      if (n == 0) {
         return 0;
      } else {
         n = this.log[n & 0xFF];
         int n2 = bytes[0] != 0 ? this.alog[(n + this.log[bytes[0] & 255]) % 255] & 0xFF : 0;
         int n3 = bytes[1] != 0 ? this.alog[(n + this.log[bytes[1] & 255]) % 255] & 0xFF : 0;
         int n4 = bytes[2] != 0 ? this.alog[(n + this.log[bytes[2] & 255]) % 255] & 0xFF : 0;
         int n5 = bytes[3] != 0 ? this.alog[(n + this.log[bytes[3] & 255]) % 255] & 0xFF : 0;
         return n2 << 24 | n3 << 16 | n4 << 8 | n5;
      }
   }

   public AesCrypto() {
       super();
      this.hasAtLeast12Rounds = false;
      this.has14Rounds = false;
      this.sessionK = (int[][])null;
      this.roundKeys = null;
      this.roundLimit = 0;
      this.alog = new int[256];
      this.log = new int[256];
      this.sBox = new byte[256];
      this.invSBox = new byte[256];
      this.T1 = new int[256];
      this.T2 = new int[256];
      this.T3 = new int[256];
      this.T4 = new int[256];
      this.T5 = new int[256];
      this.T6 = new int[256];
      this.T7 = new int[256];
      this.T8 = new int[256];
      this.U1 = new int[256];
      this.U2 = new int[256];
      this.U3 = new int[256];
      this.U4 = new int[256];
      this.rcon = new byte[30];
      this.cbcScratchBlock = new byte[this.getBlockSize()];
      this.cbcFeedbackIv = new byte[this.getBlockSize()];
      short s = 283;
      int n = 0;
      this.alog[0] = 1;

      for (int i = 1; i < 256; i++) {
         n = this.alog[i - 1] << 1 ^ this.alog[i - 1];
         if ((n & 256) != 0) {
            n ^= s;
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

         for (int k = 0; k < 8; k++) {
            bytesArr2[j][k] = (byte)(n >>> 7 - k & 1);
         }
      }

      byte[][] bytesArr3 = new byte[256][8];

      for (int m = 0; m < 256; m++) {
         for (int idx = 0; idx < 8; idx++) {
            bytesArr3[m][idx] = bytes[idx];

            for (int index = 0; index < 8; index++) {
               bytesArr3[m][idx] = (byte)(bytesArr3[m][idx] ^ bytesArr[idx][index] * bytesArr2[m][index]);
            }
         }
      }

      for (int i2 = 0; i2 < 256; i2++) {
         this.sBox[i2] = (byte)(bytesArr3[i2][0] << 7);

         for (int i3 = 1; i3 < 8; i3++) {
            this.sBox[i2] = (byte)(this.sBox[i2] ^ bytesArr3[i2][i3] << 7 - i3);
         }

         this.invSBox[this.sBox[i2] & 255] = (byte)i2;
      }

      byte[][] bytesArr4 = new byte[][]{{2, 1, 1, 3}, {3, 2, 1, 1}, {1, 3, 2, 1}, {1, 1, 3, 2}};
      byte[][] bytesArr5 = new byte[4][8];

      for (int i4 = 0; i4 < 4; i4++) {
         for (int i5 = 0; i5 < 4; i5++) {
            bytesArr5[i4][i5] = bytesArr4[i4][i5];
         }

         bytesArr5[i4][i4 + 4] = 1;
      }

      byte[][] bytesArr6 = new byte[4][4];

      for (int i6 = 0; i6 < 4; i6++) {
         byte b = bytesArr5[i6][i6];
         if (b == 0) {
            int n3 = i6 + 1;

            while (bytesArr5[n3][i6] == 0) {
               n3++;
            }

            for (int i7 = 0; i7 < 8; i7++) {
               byte b2 = bytesArr5[i6][i7];
               bytesArr5[i6][i7] = bytesArr5[n3][i7];
               bytesArr5[n3][i7] = b2;
            }

            b = bytesArr5[i6][i6];
         }

         for (int i8 = 0; i8 < 8; i8++) {
            if (bytesArr5[i6][i8] != 0) {
               bytesArr5[i6][i8] = (byte)this.alog[(255 + this.log[bytesArr5[i6][i8] & 255] - this.log[b & 255]) % 255];
            }
         }

         for (int i9 = 0; i9 < 4; i9++) {
            if (i6 != i9) {
               for (int i10 = i6 + 1; i10 < 8; i10++) {
                  byte[] bytes2 = bytesArr5[i9];
                  byte b3 = bytesArr5[i9][i10];
                  byte b4 = bytesArr5[i6][i10];
                  byte b5 = bytesArr5[i9][i6];
                  byte b6 = b4;
                  bytes2[i10] = (byte)(b3 ^ this.mul(b6, b5));
               }

               bytesArr5[i9][i6] = 0;
            }
         }
      }

      for (int i11 = 0; i11 < 4; i11++) {
         for (int i12 = 0; i12 < 4; i12++) {
            bytesArr6[i11][i12] = bytesArr5[i11][i12 + 4];
         }
      }

      for (int i13 = 0; i13 < 256; i13++) {
         byte b7 = this.sBox[i13];
         byte[] bytes3 = bytesArr4[0];
         this.T1[i13] = this.mul4(b7, bytes3);
         byte[] bytes4 = bytesArr4[1];
         this.T2[i13] = this.mul4(b7, bytes4);
         byte[] bytes5 = bytesArr4[2];
         this.T3[i13] = this.mul4(b7, bytes5);
         byte[] bytes6 = bytesArr4[3];
         this.T4[i13] = this.mul4(b7, bytes6);
         b7 = this.invSBox[i13];
         byte[] bytes7 = bytesArr6[0];
         this.T5[i13] = this.mul4(b7, bytes7);
         byte[] bytes8 = bytesArr6[1];
         this.T6[i13] = this.mul4(b7, bytes8);
         byte[] bytes9 = bytesArr6[2];
         this.T7[i13] = this.mul4(b7, bytes9);
         byte[] bytes10 = bytesArr6[3];
         this.T8[i13] = this.mul4(b7, bytes10);
         byte[] bytes11 = bytesArr6[0];
         this.U1[i13] = this.mul4(i13, bytes11);
         byte[] bytes12 = bytesArr6[1];
         this.U2[i13] = this.mul4(i13, bytes12);
         byte[] bytes13 = bytesArr6[2];
         this.U3[i13] = this.mul4(i13, bytes13);
         byte[] bytes14 = bytesArr6[3];
         this.U4[i13] = this.mul4(i13, bytes14);
      }

      this.rcon[0] = 1;
      int n4 = 1;

      for (int i14 = 1; i14 < 30; i14++) {
         byte b8 = 2;
         n4 = this.mul(b8, n4);
         this.rcon[i14] = (byte)n4;
      }

      this.log = null;
      this.alog = null;
   }

   private void implEncryptBlock(byte[] bytes, int n, byte[] bytes2, int n2) {
      int n3 = 0;
      int n4 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n5 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n6 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];
      int n7 = (bytes[n++] << 24 | (bytes[n++] & 255) << 16 | (bytes[n++] & 255) << 8 | bytes[n++] & 255) ^ this.roundKeys[n3++];

      while (n3 < this.roundLimit) {
         int n8 = this.T1[n4 >>> 24] ^ this.T2[n5 >>> 16 & 0xFF] ^ this.T3[n6 >>> 8 & 0xFF] ^ this.T4[n7 & 0xFF] ^ this.roundKeys[n3++];
         int n9 = this.T1[n5 >>> 24] ^ this.T2[n6 >>> 16 & 0xFF] ^ this.T3[n7 >>> 8 & 0xFF] ^ this.T4[n4 & 0xFF] ^ this.roundKeys[n3++];
         int n10 = this.T1[n6 >>> 24] ^ this.T2[n7 >>> 16 & 0xFF] ^ this.T3[n4 >>> 8 & 0xFF] ^ this.T4[n5 & 0xFF] ^ this.roundKeys[n3++];
         n7 = this.T1[n7 >>> 24] ^ this.T2[n4 >>> 16 & 0xFF] ^ this.T3[n5 >>> 8 & 0xFF] ^ this.T4[n6 & 0xFF] ^ this.roundKeys[n3++];
         n4 = n8;
         n5 = n9;
         n6 = n10;
      }

      int n11 = this.roundKeys[n3++];
      bytes2[n2++] = (byte)(this.sBox[n4 >>> 24] ^ n11 >>> 24);
      bytes2[n2++] = (byte)(this.sBox[n5 >>> 16 & 0xFF] ^ n11 >>> 16);
      bytes2[n2++] = (byte)(this.sBox[n6 >>> 8 & 0xFF] ^ n11 >>> 8);
      bytes2[n2++] = (byte)(this.sBox[n7 & 0xFF] ^ n11);
      n11 = this.roundKeys[n3++];
      bytes2[n2++] = (byte)(this.sBox[n5 >>> 24] ^ n11 >>> 24);
      bytes2[n2++] = (byte)(this.sBox[n6 >>> 16 & 0xFF] ^ n11 >>> 16);
      bytes2[n2++] = (byte)(this.sBox[n7 >>> 8 & 0xFF] ^ n11 >>> 8);
      bytes2[n2++] = (byte)(this.sBox[n4 & 0xFF] ^ n11);
      n11 = this.roundKeys[n3++];
      bytes2[n2++] = (byte)(this.sBox[n6 >>> 24] ^ n11 >>> 24);
      bytes2[n2++] = (byte)(this.sBox[n7 >>> 16 & 0xFF] ^ n11 >>> 16);
      bytes2[n2++] = (byte)(this.sBox[n4 >>> 8 & 0xFF] ^ n11 >>> 8);
      bytes2[n2++] = (byte)(this.sBox[n5 & 0xFF] ^ n11);
      n11 = this.roundKeys[n3++];
      bytes2[n2++] = (byte)(this.sBox[n7 >>> 24] ^ n11 >>> 24);
      bytes2[n2++] = (byte)(this.sBox[n4 >>> 16 & 0xFF] ^ n11 >>> 16);
      bytes2[n2++] = (byte)(this.sBox[n5 >>> 8 & 0xFF] ^ n11 >>> 8);
      bytes2[n2] = (byte)(this.sBox[n6 & 0xFF] ^ n11);
   }

   public void decryptBlock(byte[] bytes, int n, byte[] bytes2, int n2) {
      this.implDecryptBlock(bytes, n, bytes2, n2);
   }





}
