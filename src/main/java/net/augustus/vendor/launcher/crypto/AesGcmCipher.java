package net.augustus.vendor.launcher.crypto;


public class AesGcmCipher {
   private static final int AES_BLOCK_SIZE = 0;
   private final byte[] encryptedCounterBlock;
   private static final int GCM_NONCE_LENGTH = 0;
   private final int[] tagCounterState;
   private static final int[] RCON = new int[]{0, 1, 2, 4, 8, 16, 32, 64, 128, 27, 54};
   private static final int GCM_TAG_LENGTH = 0;
   private static final int[] INV_SBOX = new int[]{
      82,
      9,
      106,
      213,
      48,
      54,
      165,
      56,
      191,
      64,
      163,
      158,
      129,
      243,
      215,
      251,
      124,
      227,
      57,
      130,
      155,
      47,
      255,
      135,
      52,
      142,
      67,
      68,
      196,
      222,
      233,
      203,
      84,
      123,
      148,
      50,
      166,
      194,
      35,
      61,
      238,
      76,
      149,
      11,
      66,
      250,
      195,
      78,
      8,
      46,
      161,
      102,
      40,
      217,
      36,
      178,
      118,
      91,
      162,
      73,
      109,
      139,
      209,
      37,
      114,
      248,
      246,
      100,
      134,
      104,
      152,
      22,
      212,
      164,
      92,
      204,
      93,
      101,
      182,
      146,
      108,
      112,
      72,
      80,
      253,
      237,
      185,
      218,
      94,
      21,
      70,
      87,
      167,
      141,
      157,
      132,
      144,
      216,
      171,
      0,
      140,
      188,
      211,
      10,
      247,
      228,
      88,
      5,
      184,
      179,
      69,
      6,
      208,
      44,
      30,
      143,
      202,
      63,
      15,
      2,
      193,
      175,
      189,
      3,
      1,
      19,
      138,
      107,
      58,
      145,
      17,
      65,
      79,
      103,
      220,
      234,
      151,
      242,
      207,
      206,
      240,
      180,
      230,
      115,
      150,
      172,
      116,
      34,
      231,
      173,
      53,
      133,
      226,
      249,
      55,
      232,
      28,
      117,
      223,
      110,
      71,
      241,
      26,
      113,
      29,
      41,
      197,
      137,
      111,
      183,
      98,
      14,
      170,
      24,
      190,
      27,
      252,
      86,
      62,
      75,
      198,
      210,
      121,
      32,
      154,
      219,
      192,
      254,
      120,
      205,
      90,
      244,
      31,
      221,
      168,
      51,
      136,
      7,
      199,
      49,
      177,
      18,
      16,
      89,
      39,
      128,
      236,
      95,
      96,
      81,
      127,
      169,
      25,
      181,
      74,
      13,
      45,
      229,
      122,
      159,
      147,
      201,
      156,
      239,
      160,
      224,
      59,
      77,
      174,
      42,
      245,
      176,
      200,
      235,
      187,
      60,
      131,
      83,
      153,
      97,
      23,
      43,
      4,
      126,
      186,
      119,
      214,
      38,
      225,
      105,
      20,
      99,
      85,
      33,
      12,
      125
   };
   private static final int AES_128_KEY_SIZE = 0;
   private static final int[][] GF_MUL_TABLE = new int[16][256];
   private static final int[] SBOX = new int[]{
      99,
      124,
      119,
      123,
      242,
      107,
      111,
      197,
      48,
      1,
      103,
      43,
      254,
      215,
      171,
      118,
      202,
      130,
      201,
      125,
      250,
      89,
      71,
      240,
      173,
      212,
      162,
      175,
      156,
      164,
      114,
      192,
      183,
      253,
      147,
      38,
      54,
      63,
      247,
      204,
      52,
      165,
      229,
      241,
      113,
      216,
      49,
      21,
      4,
      199,
      35,
      195,
      24,
      150,
      5,
      154,
      7,
      18,
      128,
      226,
      235,
      39,
      178,
      117,
      9,
      131,
      44,
      26,
      27,
      110,
      90,
      160,
      82,
      59,
      214,
      179,
      41,
      227,
      47,
      132,
      83,
      209,
      0,
      237,
      32,
      252,
      177,
      91,
      106,
      203,
      190,
      57,
      74,
      76,
      88,
      207,
      208,
      239,
      170,
      251,
      67,
      77,
      51,
      133,
      69,
      249,
      2,
      127,
      80,
      60,
      159,
      168,
      81,
      163,
      64,
      143,
      146,
      157,
      56,
      245,
      188,
      182,
      218,
      33,
      16,
      255,
      243,
      210,
      205,
      12,
      19,
      236,
      95,
      151,
      68,
      23,
      196,
      167,
      126,
      61,
      100,
      93,
      25,
      115,
      96,
      129,
      79,
      220,
      34,
      42,
      144,
      136,
      70,
      238,
      184,
      20,
      222,
      94,
      11,
      219,
      224,
      50,
      58,
      10,
      73,
      6,
      36,
      92,
      194,
      211,
      172,
      98,
      145,
      149,
      228,
      121,
      231,
      200,
      55,
      109,
      141,
      213,
      78,
      169,
      108,
      86,
      244,
      234,
      101,
      122,
      174,
      8,
      186,
      120,
      37,
      46,
      28,
      166,
      180,
      198,
      232,
      221,
      116,
      31,
      75,
      189,
      139,
      138,
      112,
      62,
      181,
      102,
      72,
      3,
      246,
      14,
      97,
      53,
      87,
      185,
      134,
      193,
      29,
      158,
      225,
      248,
      152,
      17,
      105,
      217,
      142,
      148,
      155,
      30,
      135,
      233,
      206,
      85,
      40,
      223,
      140,
      161,
      137,
      13,
      191,
      230,
      66,
      104,
      65,
      153,
      45,
      15,
      176,
      84,
      187,
      22
   };
   private final int[] hashSubkeyState = new int[16];
   private final byte[] hashSubkeyBuffer;
   private int[] cachedRoundKeys;
   private byte[] cachedHashSubkey;
   private static final int AES_256_KEY_SIZE = 0;
   private GhashLookupTable ghashTable;
   private byte[] cachedKey;

   private byte[] computeGcmTag(byte[] bytes, byte[] bytes2, byte[] bytes3, int[] ints, byte[] bytes4) {
      int n = bytes3 != null ? bytes3.length : 0;
      if (this.ghashTable == null || !this.ghashTable.matchesHashSubkey(bytes4)) {
         this.ghashTable = new GhashLookupTable(bytes4);
      }

      byte[] bytes5 = new byte[16];
      if (bytes3 != null && n > 0) {
         int n2 = (n + 16 - 1) / 16;

         for (int i = 0; i < n2; i++) {
            int n3 = Math.min(16, n - i * 16);

            for (int j = 0; j < n3; j++) {
               bytes5[j] ^= bytes3[i * 16 + j];
            }

            this.ghashMultiply(bytes5);
         }
      }

      if (bytes2.length > 0) {
         int n4 = (bytes2.length + 16 - 1) / 16;

         for (int k = 0; k < n4; k++) {
            int n5 = Math.min(16, bytes2.length - k * 16);

            for (int m = 0; m < n5; m++) {
               bytes5[m] ^= bytes2[k * 16 + m];
            }

            this.ghashMultiply(bytes5);
         }
      }

      byte[] bytes6 = new byte[16];
      long l = (long)n * 8L;
      long l2 = (long)bytes2.length * 8L;

      for (int idx = 7; idx >= 0; idx--) {
         bytes6[idx] = (byte)((int)(l & 255L));
         l >>>= 8;
         bytes6[idx + 8] = (byte)((int)(l2 & 255L));
         l2 >>>= 8;
      }

      for (int index = 0; index < 16; index++) {
         bytes5[index] ^= bytes6[index];
      }

      this.ghashMultiply(bytes5);
      byte[] bytes7 = new byte[16];
      System.arraycopy(bytes, 0, bytes7, 0, bytes.length);
      bytes7[15] = 1;

      for (int i2 = 0; i2 < 16; i2++) {
         this.tagCounterState[i2] = bytes7[i2] & 255;
      }

      this.encryptBlock(this.tagCounterState, ints);

      for (int i3 = 0; i3 < 16; i3++) {
         this.encryptedCounterBlock[i3] = (byte)this.tagCounterState[i3];
      }

      byte[] bytes8 = new byte[16];

      for (int i4 = 0; i4 < 16; i4++) {
         bytes8[i4] = (byte)(bytes5[i4] ^ this.encryptedCounterBlock[i4]);
      }

      return bytes8;
   }

   private void ghashMultiply(byte[] bytes) {
      long l = 0L;
      long l2 = 0L;

      for (int i = 0; i < 16; i++) {
         int n = bytes[i] & 255;
         if (n != 0) {
            long l3 = this.ghashTable.lookupTable[i][n];
            l ^= l3 >>> 32;
            l2 ^= l3 & 4294967295L;
         }
      }

      for (int j = 0; j < 8; j++) {
         bytes[j] = (byte)((int)(l >>> 56 - j * 8 & 255L));
         bytes[j + 8] = (byte)((int)(l2 >>> 56 - j * 8 & 255L));
      }
   }

   private static String toHexString(byte[] bytes) {
      return toHexString(bytes, (byte)0, bytes.length);
   }

   public AesGcmCipher(long l) {
      this.tagCounterState = new int[16];
      this.hashSubkeyBuffer = new byte[16];
      this.encryptedCounterBlock = new byte[16];
      this.cachedKey = (byte[])null;
      this.cachedRoundKeys = (int[])null;
      this.cachedHashSubkey = (byte[])null;
   }

   private byte[] computeHashSubkey(byte[] bytes, int[] ints) {
      if (this.cachedKey != null && this.cachedHashSubkey != null) {
         boolean flag = true;

         for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != this.cachedKey[i]) {
               flag = false;
               break;
            }
         }

         if (flag) {
            return this.cachedHashSubkey;
         }
      }

      for (int j = 0; j < 16; j++) {
         this.hashSubkeyState[j] = 0;
      }

      this.encryptBlock(this.hashSubkeyState, ints);

      for (int k = 0; k < 16; k++) {
         this.hashSubkeyBuffer[k] = (byte)this.hashSubkeyState[k];
      }

      if (this.cachedHashSubkey == null) {
         this.cachedHashSubkey = new byte[16];
      }

      System.arraycopy(this.hashSubkeyBuffer, 0, this.cachedHashSubkey, 0, 16);
      return this.hashSubkeyBuffer;
   }

   private int[] expandKey(byte[] bytes) {
      if (this.cachedKey != null && bytes.length == this.cachedKey.length) {
         boolean flag = true;

         for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != this.cachedKey[i]) {
               flag = false;
               break;
            }
         }

         if (flag && this.cachedRoundKeys != null) {
            return this.cachedRoundKeys;
         }
      }

      byte b;
      if (bytes.length == 16) {
         b = 10;
      } else if (bytes.length == 24) {
         b = 12;
      } else {
         b = 14;
      }

      int n = bytes.length / 4;
      int n2 = 4 * (b + 1);
      int[] ints = new int[n2];

      for (int j = 0; j < n; j++) {
         ints[j] = (bytes[4 * j] & 255) << 24 | (bytes[4 * j + 1] & 255) << 16 | (bytes[4 * j + 2] & 255) << 8 | bytes[4 * j + 3] & 255;
      }

      for (int k = n; k < n2; k++) {
         int n3 = ints[k - 1];
         if (k % n == 0) {
            n3 = n3 << 8 & -256 | n3 >>> 24 & 0xFF;
            n3 = SBOX[n3 >>> 24 & 0xFF] << 24 | SBOX[n3 >>> 16 & 0xFF] << 16 | SBOX[n3 >>> 8 & 0xFF] << 8 | SBOX[n3 & 0xFF];
            n3 ^= RCON[k / n] << 24;
         } else if (n > 6 && k % n == 4) {
            n3 = SBOX[n3 >>> 24 & 0xFF] << 24 | SBOX[n3 >>> 16 & 0xFF] << 16 | SBOX[n3 >>> 8 & 0xFF] << 8 | SBOX[n3 & 0xFF];
         }

         ints[k] = ints[k - n] ^ n3;
      }

      int[] ints2 = new int[n2 * 4];

      for (int m = 0; m < n2; m++) {
         ints2[4 * m] = ints[m] >>> 24 & 0xFF;
         ints2[4 * m + 1] = ints[m] >>> 16 & 0xFF;
         ints2[4 * m + 2] = ints[m] >>> 8 & 0xFF;
         ints2[4 * m + 3] = ints[m] & 0xFF;
      }

      this.cachedKey = new byte[bytes.length];
      System.arraycopy(bytes, 0, this.cachedKey, 0, bytes.length);
      this.cachedRoundKeys = ints2;
      return ints2;
   }

   public byte[] decrypt(byte[] bytes, byte[] bytes2, byte[] bytes3) {
      if (bytes2.length != 16 && bytes2.length != 24 && bytes2.length != 32) {
         throw new IllegalArgumentException("Invalid key size. Must be 16, 24, or 32 bytes");
      } else if (bytes.length < 28) {
         throw new IllegalArgumentException("Data too short for GCM mode");
      } else {
         byte[] bytes4 = new byte[12];
         System.arraycopy(bytes, 0, bytes4, 0, bytes4.length);
         int n = bytes.length - bytes4.length - 16;
         byte[] bytes5 = new byte[n];
         System.arraycopy(bytes, bytes4.length, bytes5, 0, n);
         byte[] bytes6 = new byte[16];
         System.arraycopy(bytes, bytes4.length + n, bytes6, 0, 16);
         int[] ints = this.expandKey(bytes2);
         byte[] bytes8 = this.computeGcmTag(bytes4, bytes5, bytes3, ints, this.computeHashSubkey(bytes2, ints));
         int n2 = 0;

         for (int i = 0; i < 16; i++) {
            n2 |= bytes6[i] ^ bytes8[i];
         }

         if (n2 != 0) {
            throw new SecurityException("GCM authentication failed");
         } else {
            byte[] bytes9 = new byte[16];
            System.arraycopy(bytes4, 0, bytes9, 0, bytes4.length);
            bytes9[15] = 1;
            byte[] bytes10 = new byte[n];
            int[] ints2 = new int[16];

            for (int j = 0; j < bytes5.length; j += 16) {
               for (int k = 0; k < 16; k++) {
                  ints2[k] = bytes9[k] & 255;
               }

               this.encryptBlock(ints2, ints);
               int n3 = Math.min(16, bytes5.length - j);

               for (int m = 0; m < n3; m++) {
                  bytes10[j + m] = (byte)(bytes5[j + m] ^ (byte)ints2[m]);
               }

               int n4 = bytes9.length - 1;

               while (n4 >= 12 && ++bytes9[n4] == 0) {
                  n4--;
               }
            }

            return bytes10;
         }
      }
   }

   public byte[] decrypt(byte[] bytes, byte[] bytes2) {
      return this.decrypt(bytes, bytes2, (byte[])null);
   }

   public byte[] encrypt(byte[] bytes, byte[] bytes2) {
      return this.encrypt(bytes, bytes2, (byte[])null);
   }

   public byte[] encrypt(byte[] bytes, byte[] bytes2, byte[] bytes3) {
      if (bytes2.length != 16 && bytes2.length != 24 && bytes2.length != 32) {
         throw new IllegalArgumentException("Invalid key size. Must be 16, 24, or 32 bytes");
      } else {
         byte[] bytes4 = new byte[12];
         long l = System.nanoTime();

         for (int i = 0; i < bytes4.length; i++) {
            l = l * 25214903917L + 11L & 281474976710655L;
            bytes4[i] = (byte)((int)(l >> 16 & 255L));
         }

         int[] ints = this.expandKey(bytes2);
         byte[] bytes5 = this.computeHashSubkey(bytes2, ints);
         byte[] bytes6 = new byte[16];
         System.arraycopy(bytes4, 0, bytes6, 0, bytes4.length);
         bytes6[15] = 1;
         byte[] bytes7 = new byte[bytes.length];
         int[] ints2 = new int[16];

         for (int j = 0; j < bytes.length; j += 16) {
            for (int k = 0; k < 16; k++) {
               ints2[k] = bytes6[k] & 255;
            }

            this.encryptBlock(ints2, ints);
            int n = Math.min(16, bytes.length - j);

            for (int m = 0; m < n; m++) {
               bytes7[j + m] = (byte)(bytes[j + m] ^ (byte)ints2[m]);
            }

            int n2 = bytes6.length - 1;

            while (n2 >= 12 && ++bytes6[n2] == 0) {
               n2--;
            }
         }

         byte[] bytes8 = this.computeGcmTag(bytes4, bytes7, bytes3, ints, bytes5);
         byte[] bytes9 = new byte[bytes4.length + bytes7.length + bytes8.length];
         System.arraycopy(bytes4, 0, bytes9, 0, bytes4.length);
         System.arraycopy(bytes7, 0, bytes9, bytes4.length, bytes7.length);
         System.arraycopy(bytes8, 0, bytes9, bytes4.length + bytes7.length, bytes8.length);
         return bytes9;
      }
   }

   private void encryptBlock(int[] ints, int[] ints2) {
      int n = ints2.length / 16 - 1;

      for (int i = 0; i < 16; i++) {
         ints[i] ^= ints2[i];
      }

      for (int j = 1; j < n; j++) {
         for (int k = 0; k < 16; k++) {
            ints[k] = SBOX[ints[k]];
         }

         int n2 = ints[1];
         ints[1] = ints[5];
         ints[5] = ints[9];
         ints[9] = ints[13];
         ints[13] = n2;
         n2 = ints[2];
         int n3 = ints[6];
         ints[2] = ints[10];
         ints[6] = ints[14];
         ints[10] = n2;
         ints[14] = n3;
         n2 = ints[15];
         ints[15] = ints[11];
         ints[11] = ints[7];
         ints[7] = ints[3];
         ints[3] = n2;

         for (int m = 0; m < 4; m++) {
            n2 = ints[m * 4];
            n3 = ints[m * 4 + 1];
            int n4 = ints[m * 4 + 2];
            int n5 = ints[m * 4 + 3];
            ints[m * 4] = GF_MUL_TABLE[2][n2] ^ GF_MUL_TABLE[3][n3] ^ n4 ^ n5;
            ints[m * 4 + 1] = n2 ^ GF_MUL_TABLE[2][n3] ^ GF_MUL_TABLE[3][n4] ^ n5;
            ints[m * 4 + 2] = n2 ^ n3 ^ GF_MUL_TABLE[2][n4] ^ GF_MUL_TABLE[3][n5];
            ints[m * 4 + 3] = GF_MUL_TABLE[3][n2] ^ n3 ^ n4 ^ GF_MUL_TABLE[2][n5];
         }

         int n6 = j * 16;

         for (int idx = 0; idx < 16; idx++) {
            ints[idx] ^= ints2[n6 + idx];
         }
      }

      for (int index = 0; index < 16; index++) {
         ints[index] = SBOX[ints[index]];
      }

      int n7 = ints[1];
      ints[1] = ints[5];
      ints[5] = ints[9];
      ints[9] = ints[13];
      ints[13] = n7;
      n7 = ints[2];
      int n8 = ints[6];
      ints[2] = ints[10];
      ints[6] = ints[14];
      ints[10] = n7;
      ints[14] = n8;
      n7 = ints[15];
      ints[15] = ints[11];
      ints[11] = ints[7];
      ints[7] = ints[3];
      ints[3] = n7;
      int n9 = n * 16;

      for (int i2 = 0; i2 < 16; i2++) {
         ints[i2] ^= ints2[n9 + i2];
      }
   }

   private static String toHexString(byte[] bytes, int n, int n2) {
      StringBuilder sb = new StringBuilder();

      for (int i = n; i < n + n2; i++) {
         sb.append(String.format("%02X", bytes[i]));
      }

      return sb.toString();
   }

   static {

      for (int i = 0; i < 256; i++) {
         GF_MUL_TABLE[2][i] = (i & 128) != 0 ? (i << 1 ^ 27) & 0xFF : i << 1 & 0xFF;
         GF_MUL_TABLE[3][i] = GF_MUL_TABLE[2][i] ^ i;
         GF_MUL_TABLE[9][i] = GF_MUL_TABLE[2][GF_MUL_TABLE[2][GF_MUL_TABLE[2][i]]] ^ i;
         GF_MUL_TABLE[11][i] = GF_MUL_TABLE[2][GF_MUL_TABLE[2][i] ^ i] ^ i;
         GF_MUL_TABLE[13][i] = GF_MUL_TABLE[2][GF_MUL_TABLE[2][i ^ i]] ^ i;
         GF_MUL_TABLE[14][i] = GF_MUL_TABLE[2][GF_MUL_TABLE[2][i ^ i] ^ i];
      }
   }

}
