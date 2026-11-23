package net.augustus.vendor.launcher.crypto;


public class InlinedAesGcmCipher {
   private byte[] cachedKey;
   private final byte[] hashSubkeyBuffer;
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
   private final int[] tagCounterState;
   private InlinedGhashLookupTable ghashTable;
   private int[] cachedRoundKeys;
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
   private static final int AES_BLOCK_SIZE = 0;
   private final byte[] encryptedCounterBlock;
   private static final int[][] GF_MUL_TABLE = new int[16][256];
   private static final int GCM_NONCE_LENGTH = 0;
   private static final int GCM_TAG_LENGTH = 0;
   private static final int[] RCON = new int[]{0, 1, 2, 4, 8, 16, 32, 64, 128, 27, 54};
   private static final int AES_128_KEY_SIZE = 0;
   private byte[] cachedHashSubkey;
   private static final int AES_256_KEY_SIZE = 0;
   private final int[] hashSubkeyState = new int[16];

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

      int n = ints.length / 16 - 1;

      for (int k = 0; k < 16; k++) {
         this.hashSubkeyState[k] = this.hashSubkeyState[k] ^ ints[k];
      }

      for (int m = 1; m < n; m++) {
         for (int idx = 0; idx < 16; idx++) {
            this.hashSubkeyState[idx] = SBOX[this.hashSubkeyState[idx]];
         }

         int n2 = this.hashSubkeyState[1];
         this.hashSubkeyState[1] = this.hashSubkeyState[5];
         this.hashSubkeyState[5] = this.hashSubkeyState[9];
         this.hashSubkeyState[9] = this.hashSubkeyState[13];
         this.hashSubkeyState[13] = n2;
         n2 = this.hashSubkeyState[2];
         int n3 = this.hashSubkeyState[6];
         this.hashSubkeyState[2] = this.hashSubkeyState[10];
         this.hashSubkeyState[6] = this.hashSubkeyState[14];
         this.hashSubkeyState[10] = n2;
         this.hashSubkeyState[14] = n3;
         n2 = this.hashSubkeyState[15];
         this.hashSubkeyState[15] = this.hashSubkeyState[11];
         this.hashSubkeyState[11] = this.hashSubkeyState[7];
         this.hashSubkeyState[7] = this.hashSubkeyState[3];
         this.hashSubkeyState[3] = n2;

         for (int index = 0; index < 4; index++) {
            n2 = this.hashSubkeyState[index * 4];
            n3 = this.hashSubkeyState[index * 4 + 1];
            int n4 = this.hashSubkeyState[index * 4 + 2];
            int n5 = this.hashSubkeyState[index * 4 + 3];
            this.hashSubkeyState[index * 4] = GF_MUL_TABLE[2][n2] ^ GF_MUL_TABLE[3][n3] ^ n4 ^ n5;
            this.hashSubkeyState[index * 4 + 1] = n2 ^ GF_MUL_TABLE[2][n3] ^ GF_MUL_TABLE[3][n4] ^ n5;
            this.hashSubkeyState[index * 4 + 2] = n2 ^ n3 ^ GF_MUL_TABLE[2][n4] ^ GF_MUL_TABLE[3][n5];
            this.hashSubkeyState[index * 4 + 3] = GF_MUL_TABLE[3][n2] ^ n3 ^ n4 ^ GF_MUL_TABLE[2][n5];
         }

         int n6 = m * 16;

         for (int i2 = 0; i2 < 16; i2++) {
            this.hashSubkeyState[i2] = this.hashSubkeyState[i2] ^ ints[n6 + i2];
         }
      }

      for (int i3 = 0; i3 < 16; i3++) {
         this.hashSubkeyState[i3] = SBOX[this.hashSubkeyState[i3]];
      }

      int n7 = this.hashSubkeyState[1];
      this.hashSubkeyState[1] = this.hashSubkeyState[5];
      this.hashSubkeyState[5] = this.hashSubkeyState[9];
      this.hashSubkeyState[9] = this.hashSubkeyState[13];
      this.hashSubkeyState[13] = n7;
      n7 = this.hashSubkeyState[2];
      int n8 = this.hashSubkeyState[6];
      this.hashSubkeyState[2] = this.hashSubkeyState[10];
      this.hashSubkeyState[6] = this.hashSubkeyState[14];
      this.hashSubkeyState[10] = n7;
      this.hashSubkeyState[14] = n8;
      n7 = this.hashSubkeyState[15];
      this.hashSubkeyState[15] = this.hashSubkeyState[11];
      this.hashSubkeyState[11] = this.hashSubkeyState[7];
      this.hashSubkeyState[7] = this.hashSubkeyState[3];
      this.hashSubkeyState[3] = n7;
      int n9 = n * 16;

      for (int i4 = 0; i4 < 16; i4++) {
         this.hashSubkeyState[i4] = this.hashSubkeyState[i4] ^ ints[n9 + i4];
      }

      for (int i5 = 0; i5 < 16; i5++) {
         this.hashSubkeyBuffer[i5] = (byte)this.hashSubkeyState[i5];
      }

      if (this.cachedHashSubkey == null) {
         this.cachedHashSubkey = new byte[16];
      }

      System.arraycopy(this.hashSubkeyBuffer, 0, this.cachedHashSubkey, 0, 16);
      return this.hashSubkeyBuffer;
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

         int[] ints = null;
         boolean flag = false;
         if (this.cachedKey != null && bytes2.length == this.cachedKey.length) {
            boolean flag2 = true;

            for (int j = 0; j < bytes2.length; j++) {
               if (bytes2[j] != this.cachedKey[j]) {
                  flag2 = false;
                  break;
               }
            }

            if (flag2 && this.cachedRoundKeys != null) {
               ints = this.cachedRoundKeys;
               flag = true;
            }
         }

         if (!flag) {
            byte b = 0;
            if (bytes2.length == 16) {
               b = 10;
            } else if (bytes2.length == 24) {
               b = 12;
            } else {
               b = 14;
            }

            int n = bytes2.length / 4;
            int n2 = 4 * (b + 1);
            int[] ints2 = new int[n2];

            for (int k = 0; k < n; k++) {
               ints2[k] = (bytes2[4 * k] & 255) << 24 | (bytes2[4 * k + 1] & 255) << 16 | (bytes2[4 * k + 2] & 255) << 8 | bytes2[4 * k + 3] & 255;
            }

            for (int m = n; m < n2; m++) {
               int n3 = ints2[m - 1];
               if (m % n == 0) {
                  n3 = n3 << 8 & -256 | n3 >>> 24 & 0xFF;
                  n3 = SBOX[n3 >>> 24 & 0xFF] << 24 | SBOX[n3 >>> 16 & 0xFF] << 16 | SBOX[n3 >>> 8 & 0xFF] << 8 | SBOX[n3 & 0xFF];
                  n3 ^= RCON[m / n] << 24;
               } else if (n > 6 && m % n == 4) {
                  n3 = SBOX[n3 >>> 24 & 0xFF] << 24 | SBOX[n3 >>> 16 & 0xFF] << 16 | SBOX[n3 >>> 8 & 0xFF] << 8 | SBOX[n3 & 0xFF];
               }

               ints2[m] = ints2[m - n] ^ n3;
            }

            int[] ints3 = new int[n2 * 4];

            for (int idx = 0; idx < n2; idx++) {
               ints3[4 * idx] = ints2[idx] >>> 24 & 0xFF;
               ints3[4 * idx + 1] = ints2[idx] >>> 16 & 0xFF;
               ints3[4 * idx + 2] = ints2[idx] >>> 8 & 0xFF;
               ints3[4 * idx + 3] = ints2[idx] & 0xFF;
            }

            this.cachedKey = new byte[bytes2.length];
            System.arraycopy(bytes2, 0, this.cachedKey, 0, bytes2.length);
            this.cachedRoundKeys = ints3;
            ints = ints3;
         }

         byte[] hashSubkeyBuffer2 = this.hashSubkeyBuffer;
         boolean flag3 = false;
         if (this.cachedKey != null && this.cachedHashSubkey != null) {
            boolean flag4 = true;

            for (int index = 0; index < bytes2.length; index++) {
               if (bytes2[index] != this.cachedKey[index]) {
                  flag4 = false;
                  break;
               }
            }

            if (flag4) {
               hashSubkeyBuffer2 = this.cachedHashSubkey;
               flag3 = true;
            }
         }

         if (!flag3) {
            for (int i2 = 0; i2 < 16; i2++) {
               this.hashSubkeyState[i2] = 0;
            }

            int n4 = ints.length / 16 - 1;

            for (int i3 = 0; i3 < 16; i3++) {
               this.hashSubkeyState[i3] = this.hashSubkeyState[i3] ^ ints[i3];
            }

            for (int i4 = 1; i4 < n4; i4++) {
               for (int i5 = 0; i5 < 16; i5++) {
                  this.hashSubkeyState[i5] = SBOX[this.hashSubkeyState[i5]];
               }

               int n5 = this.hashSubkeyState[1];
               this.hashSubkeyState[1] = this.hashSubkeyState[5];
               this.hashSubkeyState[5] = this.hashSubkeyState[9];
               this.hashSubkeyState[9] = this.hashSubkeyState[13];
               this.hashSubkeyState[13] = n5;
               n5 = this.hashSubkeyState[2];
               int n6 = this.hashSubkeyState[6];
               this.hashSubkeyState[2] = this.hashSubkeyState[10];
               this.hashSubkeyState[6] = this.hashSubkeyState[14];
               this.hashSubkeyState[10] = n5;
               this.hashSubkeyState[14] = n6;
               n5 = this.hashSubkeyState[15];
               this.hashSubkeyState[15] = this.hashSubkeyState[11];
               this.hashSubkeyState[11] = this.hashSubkeyState[7];
               this.hashSubkeyState[7] = this.hashSubkeyState[3];
               this.hashSubkeyState[3] = n5;

               for (int i6 = 0; i6 < 4; i6++) {
                  n5 = this.hashSubkeyState[i6 * 4];
                  n6 = this.hashSubkeyState[i6 * 4 + 1];
                  int n7 = this.hashSubkeyState[i6 * 4 + 2];
                  int n8 = this.hashSubkeyState[i6 * 4 + 3];
                  this.hashSubkeyState[i6 * 4] = GF_MUL_TABLE[2][n5] ^ GF_MUL_TABLE[3][n6] ^ n7 ^ n8;
                  this.hashSubkeyState[i6 * 4 + 1] = n5 ^ GF_MUL_TABLE[2][n6] ^ GF_MUL_TABLE[3][n7] ^ n8;
                  this.hashSubkeyState[i6 * 4 + 2] = n5 ^ n6 ^ GF_MUL_TABLE[2][n7] ^ GF_MUL_TABLE[3][n8];
                  this.hashSubkeyState[i6 * 4 + 3] = GF_MUL_TABLE[3][n5] ^ n6 ^ n7 ^ GF_MUL_TABLE[2][n8];
               }

               int n9 = i4 * 16;

               for (int i7 = 0; i7 < 16; i7++) {
                  this.hashSubkeyState[i7] = this.hashSubkeyState[i7] ^ ints[n9 + i7];
               }
            }

            for (int i8 = 0; i8 < 16; i8++) {
               this.hashSubkeyState[i8] = SBOX[this.hashSubkeyState[i8]];
            }

            int n10 = this.hashSubkeyState[1];
            this.hashSubkeyState[1] = this.hashSubkeyState[5];
            this.hashSubkeyState[5] = this.hashSubkeyState[9];
            this.hashSubkeyState[9] = this.hashSubkeyState[13];
            this.hashSubkeyState[13] = n10;
            n10 = this.hashSubkeyState[2];
            int n11 = this.hashSubkeyState[6];
            this.hashSubkeyState[2] = this.hashSubkeyState[10];
            this.hashSubkeyState[6] = this.hashSubkeyState[14];
            this.hashSubkeyState[10] = n10;
            this.hashSubkeyState[14] = n11;
            n10 = this.hashSubkeyState[15];
            this.hashSubkeyState[15] = this.hashSubkeyState[11];
            this.hashSubkeyState[11] = this.hashSubkeyState[7];
            this.hashSubkeyState[7] = this.hashSubkeyState[3];
            this.hashSubkeyState[3] = n10;
            int n12 = n4 * 16;

            for (int i9 = 0; i9 < 16; i9++) {
               this.hashSubkeyState[i9] = this.hashSubkeyState[i9] ^ ints[n12 + i9];
            }

            for (int i10 = 0; i10 < 16; i10++) {
               this.hashSubkeyBuffer[i10] = (byte)this.hashSubkeyState[i10];
            }

            if (this.cachedHashSubkey == null) {
               this.cachedHashSubkey = new byte[16];
            }

            System.arraycopy(this.hashSubkeyBuffer, 0, this.cachedHashSubkey, 0, 16);
         }

         byte[] bytes7 = new byte[16];
         System.arraycopy(bytes4, 0, bytes7, 0, bytes4.length);
         bytes7[15] = 1;
         byte[] bytes8 = new byte[bytes.length];
         int[] ints4 = new int[16];

         for (int i11 = 0; i11 < bytes.length; i11 += 16) {
            for (int i12 = 0; i12 < 16; i12++) {
               ints4[i12] = bytes7[i12] & 255;
            }

            int n13 = ints.length / 16 - 1;

            for (int i13 = 0; i13 < 16; i13++) {
               ints4[i13] ^= ints[i13];
            }

            for (int i14 = 1; i14 < n13; i14++) {
               for (int i15 = 0; i15 < 16; i15++) {
                  ints4[i15] = SBOX[ints4[i15]];
               }

               int n14 = ints4[1];
               ints4[1] = ints4[5];
               ints4[5] = ints4[9];
               ints4[9] = ints4[13];
               ints4[13] = n14;
               n14 = ints4[2];
               int n15 = ints4[6];
               ints4[2] = ints4[10];
               ints4[6] = ints4[14];
               ints4[10] = n14;
               ints4[14] = n15;
               n14 = ints4[15];
               ints4[15] = ints4[11];
               ints4[11] = ints4[7];
               ints4[7] = ints4[3];
               ints4[3] = n14;

               for (int i16 = 0; i16 < 4; i16++) {
                  n14 = ints4[i16 * 4];
                  n15 = ints4[i16 * 4 + 1];
                  int n16 = ints4[i16 * 4 + 2];
                  int n17 = ints4[i16 * 4 + 3];
                  ints4[i16 * 4] = GF_MUL_TABLE[2][n14] ^ GF_MUL_TABLE[3][n15] ^ n16 ^ n17;
                  ints4[i16 * 4 + 1] = n14 ^ GF_MUL_TABLE[2][n15] ^ GF_MUL_TABLE[3][n16] ^ n17;
                  ints4[i16 * 4 + 2] = n14 ^ n15 ^ GF_MUL_TABLE[2][n16] ^ GF_MUL_TABLE[3][n17];
                  ints4[i16 * 4 + 3] = GF_MUL_TABLE[3][n14] ^ n15 ^ n16 ^ GF_MUL_TABLE[2][n17];
               }

               int n18 = i14 * 16;

               for (int i17 = 0; i17 < 16; i17++) {
                  ints4[i17] ^= ints[n18 + i17];
               }
            }

            for (int i18 = 0; i18 < 16; i18++) {
               ints4[i18] = SBOX[ints4[i18]];
            }

            int n19 = ints4[1];
            ints4[1] = ints4[5];
            ints4[5] = ints4[9];
            ints4[9] = ints4[13];
            ints4[13] = n19;
            n19 = ints4[2];
            int n20 = ints4[6];
            ints4[2] = ints4[10];
            ints4[6] = ints4[14];
            ints4[10] = n19;
            ints4[14] = n20;
            n19 = ints4[15];
            ints4[15] = ints4[11];
            ints4[11] = ints4[7];
            ints4[7] = ints4[3];
            ints4[3] = n19;
            int n21 = n13 * 16;

            for (int i19 = 0; i19 < 16; i19++) {
               ints4[i19] ^= ints[n21 + i19];
            }

            int n22 = Math.min(16, bytes.length - i11);

            for (int i20 = 0; i20 < n22; i20++) {
               bytes8[i11 + i20] = (byte)(bytes[i11 + i20] ^ (byte)ints4[i20]);
            }

            int n23 = bytes7.length - 1;

            while (n23 >= 12 && ++bytes7[n23] == 0) {
               n23--;
            }
         }

         int n24 = bytes3 != null ? bytes3.length : 0;
         if (this.ghashTable == null || !this.ghashTable.matchesHashSubkey(hashSubkeyBuffer2)) {
            this.ghashTable = new InlinedGhashLookupTable(hashSubkeyBuffer2);
         }

         byte[] bytes9 = new byte[16];
         if (bytes3 != null && n24 > 0) {
            int n25 = (n24 + 16 - 1) / 16;

            for (int i21 = 0; i21 < n25; i21++) {
               int n26 = Math.min(16, n24 - i21 * 16);

               for (int i22 = 0; i22 < n26; i22++) {
                  bytes9[i22] ^= bytes3[i21 * 16 + i22];
               }

               long l5 = 0L;
               long l6 = 0L;

               for (int i23 = 0; i23 < 16; i23++) {
                  int n27 = bytes9[i23] & 255;
                  if (n27 != 0) {
                     long l7 = this.ghashTable.lookupTable[i23][n27];
                     l5 ^= l7 >>> 32;
                     l6 ^= l7 & 4294967295L;
                  }
               }

               for (int i24 = 0; i24 < 8; i24++) {
                  bytes9[i24] = (byte)((int)(l5 >>> 56 - i24 * 8 & 255L));
                  bytes9[i24 + 8] = (byte)((int)(l6 >>> 56 - i24 * 8 & 255L));
               }
            }
         }

         if (bytes8.length > 0) {
            int n28 = (bytes8.length + 16 - 1) / 16;

            for (int i25 = 0; i25 < n28; i25++) {
               int n29 = Math.min(16, bytes8.length - i25 * 16);

               for (int i26 = 0; i26 < n29; i26++) {
                  bytes9[i26] ^= bytes8[i25 * 16 + i26];
               }

               long l8 = 0L;
               long l9 = 0L;

               for (int i27 = 0; i27 < 16; i27++) {
                  int n30 = bytes9[i27] & 255;
                  if (n30 != 0) {
                     long l10 = this.ghashTable.lookupTable[i27][n30];
                     l8 ^= l10 >>> 32;
                     l9 ^= l10 & 4294967295L;
                  }
               }

               for (int i28 = 0; i28 < 8; i28++) {
                  bytes9[i28] = (byte)((int)(l8 >>> 56 - i28 * 8 & 255L));
                  bytes9[i28 + 8] = (byte)((int)(l9 >>> 56 - i28 * 8 & 255L));
               }
            }
         }

         byte[] bytes10 = new byte[16];
         long l11 = (long)n24 * 8L;
         long l12 = (long)bytes8.length * 8L;

         for (int i29 = 7; i29 >= 0; i29--) {
            bytes10[i29] = (byte)((int)(l11 & 255L));
            l11 >>>= 8;
            bytes10[i29 + 8] = (byte)((int)(l12 & 255L));
            l12 >>>= 8;
         }

         for (int i30 = 0; i30 < 16; i30++) {
            bytes9[i30] ^= bytes10[i30];
         }

         long l13 = 0L;
         long l14 = 0L;

         for (int i31 = 0; i31 < 16; i31++) {
            int n31 = bytes9[i31] & 255;
            if (n31 != 0) {
               long l15 = this.ghashTable.lookupTable[i31][n31];
               l13 ^= l15 >>> 32;
               l14 ^= l15 & 4294967295L;
            }
         }

         for (int i32 = 0; i32 < 8; i32++) {
            bytes9[i32] = (byte)((int)(l13 >>> 56 - i32 * 8 & 255L));
            bytes9[i32 + 8] = (byte)((int)(l14 >>> 56 - i32 * 8 & 255L));
         }

         byte[] bytes11 = new byte[16];
         System.arraycopy(bytes4, 0, bytes11, 0, bytes4.length);
         bytes11[15] = 1;

         for (int i33 = 0; i33 < 16; i33++) {
            this.tagCounterState[i33] = bytes11[i33] & 255;
         }

         int n32 = ints.length / 16 - 1;

         for (int i34 = 0; i34 < 16; i34++) {
            this.tagCounterState[i34] = this.tagCounterState[i34] ^ ints[i34];
         }

         for (int i35 = 1; i35 < n32; i35++) {
            for (int i36 = 0; i36 < 16; i36++) {
               this.tagCounterState[i36] = SBOX[this.tagCounterState[i36]];
            }

            int n33 = this.tagCounterState[1];
            this.tagCounterState[1] = this.tagCounterState[5];
            this.tagCounterState[5] = this.tagCounterState[9];
            this.tagCounterState[9] = this.tagCounterState[13];
            this.tagCounterState[13] = n33;
            n33 = this.tagCounterState[2];
            int n34 = this.tagCounterState[6];
            this.tagCounterState[2] = this.tagCounterState[10];
            this.tagCounterState[6] = this.tagCounterState[14];
            this.tagCounterState[10] = n33;
            this.tagCounterState[14] = n34;
            n33 = this.tagCounterState[15];
            this.tagCounterState[15] = this.tagCounterState[11];
            this.tagCounterState[11] = this.tagCounterState[7];
            this.tagCounterState[7] = this.tagCounterState[3];
            this.tagCounterState[3] = n33;

            for (int i37 = 0; i37 < 4; i37++) {
               n33 = this.tagCounterState[i37 * 4];
               n34 = this.tagCounterState[i37 * 4 + 1];
               int n35 = this.tagCounterState[i37 * 4 + 2];
               int n36 = this.tagCounterState[i37 * 4 + 3];
               this.tagCounterState[i37 * 4] = GF_MUL_TABLE[2][n33] ^ GF_MUL_TABLE[3][n34] ^ n35 ^ n36;
               this.tagCounterState[i37 * 4 + 1] = n33 ^ GF_MUL_TABLE[2][n34] ^ GF_MUL_TABLE[3][n35] ^ n36;
               this.tagCounterState[i37 * 4 + 2] = n33 ^ n34 ^ GF_MUL_TABLE[2][n35] ^ GF_MUL_TABLE[3][n36];
               this.tagCounterState[i37 * 4 + 3] = GF_MUL_TABLE[3][n33] ^ n34 ^ n35 ^ GF_MUL_TABLE[2][n36];
            }

            int n37 = i35 * 16;

            for (int i38 = 0; i38 < 16; i38++) {
               this.tagCounterState[i38] = this.tagCounterState[i38] ^ ints[n37 + i38];
            }
         }

         for (int i39 = 0; i39 < 16; i39++) {
            this.tagCounterState[i39] = SBOX[this.tagCounterState[i39]];
         }

         int n38 = this.tagCounterState[1];
         this.tagCounterState[1] = this.tagCounterState[5];
         this.tagCounterState[5] = this.tagCounterState[9];
         this.tagCounterState[9] = this.tagCounterState[13];
         this.tagCounterState[13] = n38;
         n38 = this.tagCounterState[2];
         int n39 = this.tagCounterState[6];
         this.tagCounterState[2] = this.tagCounterState[10];
         this.tagCounterState[6] = this.tagCounterState[14];
         this.tagCounterState[10] = n38;
         this.tagCounterState[14] = n39;
         n38 = this.tagCounterState[15];
         this.tagCounterState[15] = this.tagCounterState[11];
         this.tagCounterState[11] = this.tagCounterState[7];
         this.tagCounterState[7] = this.tagCounterState[3];
         this.tagCounterState[3] = n38;
         int n40 = n32 * 16;

         for (int i40 = 0; i40 < 16; i40++) {
            this.tagCounterState[i40] = this.tagCounterState[i40] ^ ints[n40 + i40];
         }

         for (int i41 = 0; i41 < 16; i41++) {
            this.encryptedCounterBlock[i41] = (byte)this.tagCounterState[i41];
         }

         byte[] bytes12 = new byte[16];

         for (int i42 = 0; i42 < 16; i42++) {
            bytes12[i42] = (byte)(bytes9[i42] ^ this.encryptedCounterBlock[i42]);
         }

         byte[] bytes13 = new byte[bytes4.length + bytes8.length + bytes12.length];
         System.arraycopy(bytes4, 0, bytes13, 0, bytes4.length);
         System.arraycopy(bytes8, 0, bytes13, bytes4.length, bytes8.length);
         System.arraycopy(bytes12, 0, bytes13, bytes4.length + bytes8.length, bytes12.length);
         return bytes13;
      }
   }

   private byte[] computeGcmTag(byte[] bytes, byte[] bytes2, byte[] bytes3, int[] ints, byte[] bytes4) {
      int n = bytes3 != null ? bytes3.length : 0;
      if (this.ghashTable == null || !this.ghashTable.matchesHashSubkey(bytes4)) {
         this.ghashTable = new InlinedGhashLookupTable(bytes4);
      }

      byte[] bytes5 = new byte[16];
      if (bytes3 != null && n > 0) {
         int n2 = (n + 16 - 1) / 16;

         for (int i = 0; i < n2; i++) {
            int n3 = Math.min(16, n - i * 16);

            for (int j = 0; j < n3; j++) {
               bytes5[j] ^= bytes3[i * 16 + j];
            }

            long l = 0L;
            long l2 = 0L;

            for (int k = 0; k < 16; k++) {
               int n4 = bytes5[k] & 255;
               if (n4 != 0) {
                  long l3 = this.ghashTable.lookupTable[k][n4];
                  l ^= l3 >>> 32;
                  l2 ^= l3 & 4294967295L;
               }
            }

            for (int m = 0; m < 8; m++) {
               bytes5[m] = (byte)((int)(l >>> 56 - m * 8 & 255L));
               bytes5[m + 8] = (byte)((int)(l2 >>> 56 - m * 8 & 255L));
            }
         }
      }

      if (bytes2.length > 0) {
         int n5 = (bytes2.length + 16 - 1) / 16;

         for (int idx = 0; idx < n5; idx++) {
            int n6 = Math.min(16, bytes2.length - idx * 16);

            for (int index = 0; index < n6; index++) {
               bytes5[index] ^= bytes2[idx * 16 + index];
            }

            long l4 = 0L;
            long l5 = 0L;

            for (int i2 = 0; i2 < 16; i2++) {
               int n7 = bytes5[i2] & 255;
               if (n7 != 0) {
                  long l6 = this.ghashTable.lookupTable[i2][n7];
                  l4 ^= l6 >>> 32;
                  l5 ^= l6 & 4294967295L;
               }
            }

            for (int i3 = 0; i3 < 8; i3++) {
               bytes5[i3] = (byte)((int)(l4 >>> 56 - i3 * 8 & 255L));
               bytes5[i3 + 8] = (byte)((int)(l5 >>> 56 - i3 * 8 & 255L));
            }
         }
      }

      byte[] bytes6 = new byte[16];
      long l7 = (long)n * 8L;
      long l8 = (long)bytes2.length * 8L;

      for (int i4 = 7; i4 >= 0; i4--) {
         bytes6[i4] = (byte)((int)(l7 & 255L));
         l7 >>>= 8;
         bytes6[i4 + 8] = (byte)((int)(l8 & 255L));
         l8 >>>= 8;
      }

      for (int i5 = 0; i5 < 16; i5++) {
         bytes5[i5] ^= bytes6[i5];
      }

      long l9 = 0L;
      long l10 = 0L;

      for (int i6 = 0; i6 < 16; i6++) {
         int n8 = bytes5[i6] & 255;
         if (n8 != 0) {
            long l11 = this.ghashTable.lookupTable[i6][n8];
            l9 ^= l11 >>> 32;
            l10 ^= l11 & 4294967295L;
         }
      }

      for (int i7 = 0; i7 < 8; i7++) {
         bytes5[i7] = (byte)((int)(l9 >>> 56 - i7 * 8 & 255L));
         bytes5[i7 + 8] = (byte)((int)(l10 >>> 56 - i7 * 8 & 255L));
      }

      byte[] bytes7 = new byte[16];
      System.arraycopy(bytes, 0, bytes7, 0, bytes.length);
      bytes7[15] = 1;

      for (int i8 = 0; i8 < 16; i8++) {
         this.tagCounterState[i8] = bytes7[i8] & 255;
      }

      int n9 = ints.length / 16 - 1;

      for (int i9 = 0; i9 < 16; i9++) {
         this.tagCounterState[i9] = this.tagCounterState[i9] ^ ints[i9];
      }

      for (int i10 = 1; i10 < n9; i10++) {
         for (int i11 = 0; i11 < 16; i11++) {
            this.tagCounterState[i11] = SBOX[this.tagCounterState[i11]];
         }

         int n10 = this.tagCounterState[1];
         this.tagCounterState[1] = this.tagCounterState[5];
         this.tagCounterState[5] = this.tagCounterState[9];
         this.tagCounterState[9] = this.tagCounterState[13];
         this.tagCounterState[13] = n10;
         n10 = this.tagCounterState[2];
         int n11 = this.tagCounterState[6];
         this.tagCounterState[2] = this.tagCounterState[10];
         this.tagCounterState[6] = this.tagCounterState[14];
         this.tagCounterState[10] = n10;
         this.tagCounterState[14] = n11;
         n10 = this.tagCounterState[15];
         this.tagCounterState[15] = this.tagCounterState[11];
         this.tagCounterState[11] = this.tagCounterState[7];
         this.tagCounterState[7] = this.tagCounterState[3];
         this.tagCounterState[3] = n10;

         for (int i12 = 0; i12 < 4; i12++) {
            n10 = this.tagCounterState[i12 * 4];
            n11 = this.tagCounterState[i12 * 4 + 1];
            int n12 = this.tagCounterState[i12 * 4 + 2];
            int n13 = this.tagCounterState[i12 * 4 + 3];
            this.tagCounterState[i12 * 4] = GF_MUL_TABLE[2][n10] ^ GF_MUL_TABLE[3][n11] ^ n12 ^ n13;
            this.tagCounterState[i12 * 4 + 1] = n10 ^ GF_MUL_TABLE[2][n11] ^ GF_MUL_TABLE[3][n12] ^ n13;
            this.tagCounterState[i12 * 4 + 2] = n10 ^ n11 ^ GF_MUL_TABLE[2][n12] ^ GF_MUL_TABLE[3][n13];
            this.tagCounterState[i12 * 4 + 3] = GF_MUL_TABLE[3][n10] ^ n11 ^ n12 ^ GF_MUL_TABLE[2][n13];
         }

         int n14 = i10 * 16;

         for (int i13 = 0; i13 < 16; i13++) {
            this.tagCounterState[i13] = this.tagCounterState[i13] ^ ints[n14 + i13];
         }
      }

      for (int i14 = 0; i14 < 16; i14++) {
         this.tagCounterState[i14] = SBOX[this.tagCounterState[i14]];
      }

      int n15 = this.tagCounterState[1];
      this.tagCounterState[1] = this.tagCounterState[5];
      this.tagCounterState[5] = this.tagCounterState[9];
      this.tagCounterState[9] = this.tagCounterState[13];
      this.tagCounterState[13] = n15;
      n15 = this.tagCounterState[2];
      int n16 = this.tagCounterState[6];
      this.tagCounterState[2] = this.tagCounterState[10];
      this.tagCounterState[6] = this.tagCounterState[14];
      this.tagCounterState[10] = n15;
      this.tagCounterState[14] = n16;
      n15 = this.tagCounterState[15];
      this.tagCounterState[15] = this.tagCounterState[11];
      this.tagCounterState[11] = this.tagCounterState[7];
      this.tagCounterState[7] = this.tagCounterState[3];
      this.tagCounterState[3] = n15;
      int n17 = n9 * 16;

      for (int i15 = 0; i15 < 16; i15++) {
         this.tagCounterState[i15] = this.tagCounterState[i15] ^ ints[n17 + i15];
      }

      for (int i16 = 0; i16 < 16; i16++) {
         this.encryptedCounterBlock[i16] = (byte)this.tagCounterState[i16];
      }

      byte[] bytes8 = new byte[16];

      for (int i17 = 0; i17 < 16; i17++) {
         bytes8[i17] = (byte)(bytes5[i17] ^ this.encryptedCounterBlock[i17]);
      }

      return bytes8;
   }

   private static String toHexString(byte[] bytes) {
      return toHexString(bytes, (byte)0, bytes.length);
   }

   public InlinedAesGcmCipher(long l) {
      this.tagCounterState = new int[16];
      this.hashSubkeyBuffer = new byte[16];
      this.encryptedCounterBlock = new byte[16];
      this.cachedKey = (byte[])null;
      this.cachedRoundKeys = (int[])null;
      this.cachedHashSubkey = (byte[])null;
   }

   private void ghashMultiply(byte[] bytes, long l) {
      long l2 = 0L;
      long l3 = 0L;

      for (int i = 0; i < 16; i++) {
         int n = bytes[i] & 255;
         if (n != 0) {
            long l4 = this.ghashTable.lookupTable[i][n];
            l2 ^= l4 >>> 32;
            l3 ^= l4 & 4294967295L;
         }
      }

      for (int j = 0; j < 8; j++) {
         bytes[j] = (byte)((int)(l2 >>> 56 - j * 8 & 255L));
         bytes[j + 8] = (byte)((int)(l3 >>> 56 - j * 8 & 255L));
      }
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
         int[] ints = null;
         boolean flag = false;
         if (this.cachedKey != null && bytes2.length == this.cachedKey.length) {
            boolean flag2 = true;

            for (int i = 0; i < bytes2.length; i++) {
               if (bytes2[i] != this.cachedKey[i]) {
                  flag2 = false;
                  break;
               }
            }

            if (flag2 && this.cachedRoundKeys != null) {
               ints = this.cachedRoundKeys;
               flag = true;
            }
         }

         if (!flag) {
            byte b = 0;
            if (bytes2.length == 16) {
               b = 10;
            } else if (bytes2.length == 24) {
               b = 12;
            } else {
               b = 14;
            }

            int n2 = bytes2.length / 4;
            int n3 = 4 * (b + 1);
            int[] ints2 = new int[n3];

            for (int j = 0; j < n2; j++) {
               ints2[j] = (bytes2[4 * j] & 255) << 24 | (bytes2[4 * j + 1] & 255) << 16 | (bytes2[4 * j + 2] & 255) << 8 | bytes2[4 * j + 3] & 255;
            }

            for (int k = n2; k < n3; k++) {
               int n4 = ints2[k - 1];
               if (k % n2 == 0) {
                  n4 = n4 << 8 & -256 | n4 >>> 24 & 0xFF;
                  n4 = SBOX[n4 >>> 24 & 0xFF] << 24 | SBOX[n4 >>> 16 & 0xFF] << 16 | SBOX[n4 >>> 8 & 0xFF] << 8 | SBOX[n4 & 0xFF];
                  n4 ^= RCON[k / n2] << 24;
               } else if (n2 > 6 && k % n2 == 4) {
                  n4 = SBOX[n4 >>> 24 & 0xFF] << 24 | SBOX[n4 >>> 16 & 0xFF] << 16 | SBOX[n4 >>> 8 & 0xFF] << 8 | SBOX[n4 & 0xFF];
               }

               ints2[k] = ints2[k - n2] ^ n4;
            }

            int[] ints3 = new int[n3 * 4];

            for (int m = 0; m < n3; m++) {
               ints3[4 * m] = ints2[m] >>> 24 & 0xFF;
               ints3[4 * m + 1] = ints2[m] >>> 16 & 0xFF;
               ints3[4 * m + 2] = ints2[m] >>> 8 & 0xFF;
               ints3[4 * m + 3] = ints2[m] & 0xFF;
            }

            this.cachedKey = new byte[bytes2.length];
            System.arraycopy(bytes2, 0, this.cachedKey, 0, bytes2.length);
            this.cachedRoundKeys = ints3;
            ints = ints3;
         }

         byte[] hashSubkeyBuffer2 = this.hashSubkeyBuffer;
         boolean flag3 = false;
         if (this.cachedKey != null && this.cachedHashSubkey != null) {
            boolean flag4 = true;

            for (int idx = 0; idx < bytes2.length; idx++) {
               if (bytes2[idx] != this.cachedKey[idx]) {
                  flag4 = false;
                  break;
               }
            }

            if (flag4) {
               hashSubkeyBuffer2 = this.cachedHashSubkey;
               flag3 = true;
            }
         }

         if (!flag3) {
            for (int index = 0; index < 16; index++) {
               this.hashSubkeyState[index] = 0;
            }

            int n5 = ints.length / 16 - 1;

            for (int i2 = 0; i2 < 16; i2++) {
               this.hashSubkeyState[i2] = this.hashSubkeyState[i2] ^ ints[i2];
            }

            for (int i3 = 1; i3 < n5; i3++) {
               for (int i4 = 0; i4 < 16; i4++) {
                  this.hashSubkeyState[i4] = SBOX[this.hashSubkeyState[i4]];
               }

               int n6 = this.hashSubkeyState[1];
               this.hashSubkeyState[1] = this.hashSubkeyState[5];
               this.hashSubkeyState[5] = this.hashSubkeyState[9];
               this.hashSubkeyState[9] = this.hashSubkeyState[13];
               this.hashSubkeyState[13] = n6;
               n6 = this.hashSubkeyState[2];
               int n7 = this.hashSubkeyState[6];
               this.hashSubkeyState[2] = this.hashSubkeyState[10];
               this.hashSubkeyState[6] = this.hashSubkeyState[14];
               this.hashSubkeyState[10] = n6;
               this.hashSubkeyState[14] = n7;
               n6 = this.hashSubkeyState[15];
               this.hashSubkeyState[15] = this.hashSubkeyState[11];
               this.hashSubkeyState[11] = this.hashSubkeyState[7];
               this.hashSubkeyState[7] = this.hashSubkeyState[3];
               this.hashSubkeyState[3] = n6;

               for (int i5 = 0; i5 < 4; i5++) {
                  n6 = this.hashSubkeyState[i5 * 4];
                  n7 = this.hashSubkeyState[i5 * 4 + 1];
                  int n8 = this.hashSubkeyState[i5 * 4 + 2];
                  int n9 = this.hashSubkeyState[i5 * 4 + 3];
                  this.hashSubkeyState[i5 * 4] = GF_MUL_TABLE[2][n6] ^ GF_MUL_TABLE[3][n7] ^ n8 ^ n9;
                  this.hashSubkeyState[i5 * 4 + 1] = n6 ^ GF_MUL_TABLE[2][n7] ^ GF_MUL_TABLE[3][n8] ^ n9;
                  this.hashSubkeyState[i5 * 4 + 2] = n6 ^ n7 ^ GF_MUL_TABLE[2][n8] ^ GF_MUL_TABLE[3][n9];
                  this.hashSubkeyState[i5 * 4 + 3] = GF_MUL_TABLE[3][n6] ^ n7 ^ n8 ^ GF_MUL_TABLE[2][n9];
               }

               int n10 = i3 * 16;

               for (int i6 = 0; i6 < 16; i6++) {
                  this.hashSubkeyState[i6] = this.hashSubkeyState[i6] ^ ints[n10 + i6];
               }
            }

            for (int i7 = 0; i7 < 16; i7++) {
               this.hashSubkeyState[i7] = SBOX[this.hashSubkeyState[i7]];
            }

            int n11 = this.hashSubkeyState[1];
            this.hashSubkeyState[1] = this.hashSubkeyState[5];
            this.hashSubkeyState[5] = this.hashSubkeyState[9];
            this.hashSubkeyState[9] = this.hashSubkeyState[13];
            this.hashSubkeyState[13] = n11;
            n11 = this.hashSubkeyState[2];
            int n12 = this.hashSubkeyState[6];
            this.hashSubkeyState[2] = this.hashSubkeyState[10];
            this.hashSubkeyState[6] = this.hashSubkeyState[14];
            this.hashSubkeyState[10] = n11;
            this.hashSubkeyState[14] = n12;
            n11 = this.hashSubkeyState[15];
            this.hashSubkeyState[15] = this.hashSubkeyState[11];
            this.hashSubkeyState[11] = this.hashSubkeyState[7];
            this.hashSubkeyState[7] = this.hashSubkeyState[3];
            this.hashSubkeyState[3] = n11;
            int n13 = n5 * 16;

            for (int i8 = 0; i8 < 16; i8++) {
               this.hashSubkeyState[i8] = this.hashSubkeyState[i8] ^ ints[n13 + i8];
            }

            for (int i9 = 0; i9 < 16; i9++) {
               this.hashSubkeyBuffer[i9] = (byte)this.hashSubkeyState[i9];
            }

            if (this.cachedHashSubkey == null) {
               this.cachedHashSubkey = new byte[16];
            }

            System.arraycopy(this.hashSubkeyBuffer, 0, this.cachedHashSubkey, 0, 16);
         }

         int n14 = bytes3 != null ? bytes3.length : 0;
         if (this.ghashTable == null || !this.ghashTable.matchesHashSubkey(hashSubkeyBuffer2)) {
            this.ghashTable = new InlinedGhashLookupTable(hashSubkeyBuffer2);
         }

         byte[] bytes9 = new byte[16];
         if (bytes3 != null && n14 > 0) {
            int n15 = (n14 + 16 - 1) / 16;

            for (int i10 = 0; i10 < n15; i10++) {
               int n16 = Math.min(16, n14 - i10 * 16);

               for (int i11 = 0; i11 < n16; i11++) {
                  bytes9[i11] ^= bytes3[i10 * 16 + i11];
               }

               long l = 0L;
               long l2 = 0L;

               for (int i12 = 0; i12 < 16; i12++) {
                  int n17 = bytes9[i12] & 255;
                  if (n17 != 0) {
                     long l3 = this.ghashTable.lookupTable[i12][n17];
                     l ^= l3 >>> 32;
                     l2 ^= l3 & 4294967295L;
                  }
               }

               for (int i13 = 0; i13 < 8; i13++) {
                  bytes9[i13] = (byte)((int)(l >>> 56 - i13 * 8 & 255L));
                  bytes9[i13 + 8] = (byte)((int)(l2 >>> 56 - i13 * 8 & 255L));
               }
            }
         }

         if (bytes5.length > 0) {
            int n18 = (bytes5.length + 16 - 1) / 16;

            for (int i14 = 0; i14 < n18; i14++) {
               int n19 = Math.min(16, bytes5.length - i14 * 16);

               for (int i15 = 0; i15 < n19; i15++) {
                  bytes9[i15] ^= bytes5[i14 * 16 + i15];
               }

               long l4 = 0L;
               long l5 = 0L;

               for (int i16 = 0; i16 < 16; i16++) {
                  int n20 = bytes9[i16] & 255;
                  if (n20 != 0) {
                     long l6 = this.ghashTable.lookupTable[i16][n20];
                     l4 ^= l6 >>> 32;
                     l5 ^= l6 & 4294967295L;
                  }
               }

               for (int i17 = 0; i17 < 8; i17++) {
                  bytes9[i17] = (byte)((int)(l4 >>> 56 - i17 * 8 & 255L));
                  bytes9[i17 + 8] = (byte)((int)(l5 >>> 56 - i17 * 8 & 255L));
               }
            }
         }

         byte[] bytes10 = new byte[16];
         long l7 = (long)n14 * 8L;
         long l8 = (long)bytes5.length * 8L;

         for (int i18 = 7; i18 >= 0; i18--) {
            bytes10[i18] = (byte)((int)(l7 & 255L));
            l7 >>>= 8;
            bytes10[i18 + 8] = (byte)((int)(l8 & 255L));
            l8 >>>= 8;
         }

         for (int i19 = 0; i19 < 16; i19++) {
            bytes9[i19] ^= bytes10[i19];
         }

         long l9 = 0L;
         long l10 = 0L;

         for (int i20 = 0; i20 < 16; i20++) {
            int n21 = bytes9[i20] & 255;
            if (n21 != 0) {
               long l11 = this.ghashTable.lookupTable[i20][n21];
               l9 ^= l11 >>> 32;
               l10 ^= l11 & 4294967295L;
            }
         }

         for (int i21 = 0; i21 < 8; i21++) {
            bytes9[i21] = (byte)((int)(l9 >>> 56 - i21 * 8 & 255L));
            bytes9[i21 + 8] = (byte)((int)(l10 >>> 56 - i21 * 8 & 255L));
         }

         byte[] bytes11 = new byte[16];
         System.arraycopy(bytes4, 0, bytes11, 0, bytes4.length);
         bytes11[15] = 1;

         for (int i22 = 0; i22 < 16; i22++) {
            this.tagCounterState[i22] = bytes11[i22] & 255;
         }

         int n22 = ints.length / 16 - 1;

         for (int i23 = 0; i23 < 16; i23++) {
            this.tagCounterState[i23] = this.tagCounterState[i23] ^ ints[i23];
         }

         for (int i24 = 1; i24 < n22; i24++) {
            for (int i25 = 0; i25 < 16; i25++) {
               this.tagCounterState[i25] = SBOX[this.tagCounterState[i25]];
            }

            int n23 = this.tagCounterState[1];
            this.tagCounterState[1] = this.tagCounterState[5];
            this.tagCounterState[5] = this.tagCounterState[9];
            this.tagCounterState[9] = this.tagCounterState[13];
            this.tagCounterState[13] = n23;
            n23 = this.tagCounterState[2];
            int n24 = this.tagCounterState[6];
            this.tagCounterState[2] = this.tagCounterState[10];
            this.tagCounterState[6] = this.tagCounterState[14];
            this.tagCounterState[10] = n23;
            this.tagCounterState[14] = n24;
            n23 = this.tagCounterState[15];
            this.tagCounterState[15] = this.tagCounterState[11];
            this.tagCounterState[11] = this.tagCounterState[7];
            this.tagCounterState[7] = this.tagCounterState[3];
            this.tagCounterState[3] = n23;

            for (int i26 = 0; i26 < 4; i26++) {
               n23 = this.tagCounterState[i26 * 4];
               n24 = this.tagCounterState[i26 * 4 + 1];
               int n25 = this.tagCounterState[i26 * 4 + 2];
               int n26 = this.tagCounterState[i26 * 4 + 3];
               this.tagCounterState[i26 * 4] = GF_MUL_TABLE[2][n23] ^ GF_MUL_TABLE[3][n24] ^ n25 ^ n26;
               this.tagCounterState[i26 * 4 + 1] = n23 ^ GF_MUL_TABLE[2][n24] ^ GF_MUL_TABLE[3][n25] ^ n26;
               this.tagCounterState[i26 * 4 + 2] = n23 ^ n24 ^ GF_MUL_TABLE[2][n25] ^ GF_MUL_TABLE[3][n26];
               this.tagCounterState[i26 * 4 + 3] = GF_MUL_TABLE[3][n23] ^ n24 ^ n25 ^ GF_MUL_TABLE[2][n26];
            }

            int n27 = i24 * 16;

            for (int i27 = 0; i27 < 16; i27++) {
               this.tagCounterState[i27] = this.tagCounterState[i27] ^ ints[n27 + i27];
            }
         }

         for (int i28 = 0; i28 < 16; i28++) {
            this.tagCounterState[i28] = SBOX[this.tagCounterState[i28]];
         }

         int n28 = this.tagCounterState[1];
         this.tagCounterState[1] = this.tagCounterState[5];
         this.tagCounterState[5] = this.tagCounterState[9];
         this.tagCounterState[9] = this.tagCounterState[13];
         this.tagCounterState[13] = n28;
         n28 = this.tagCounterState[2];
         int n29 = this.tagCounterState[6];
         this.tagCounterState[2] = this.tagCounterState[10];
         this.tagCounterState[6] = this.tagCounterState[14];
         this.tagCounterState[10] = n28;
         this.tagCounterState[14] = n29;
         n28 = this.tagCounterState[15];
         this.tagCounterState[15] = this.tagCounterState[11];
         this.tagCounterState[11] = this.tagCounterState[7];
         this.tagCounterState[7] = this.tagCounterState[3];
         this.tagCounterState[3] = n28;
         int n30 = n22 * 16;

         for (int i29 = 0; i29 < 16; i29++) {
            this.tagCounterState[i29] = this.tagCounterState[i29] ^ ints[n30 + i29];
         }

         for (int i30 = 0; i30 < 16; i30++) {
            this.encryptedCounterBlock[i30] = (byte)this.tagCounterState[i30];
         }

         byte[] bytes12 = new byte[16];

         for (int i31 = 0; i31 < 16; i31++) {
            bytes12[i31] = (byte)(bytes9[i31] ^ this.encryptedCounterBlock[i31]);
         }

         int n31 = 0;

         for (int i32 = 0; i32 < 16; i32++) {
            n31 |= bytes6[i32] ^ bytes12[i32];
         }

         if (n31 != 0) {
            throw new SecurityException("GCM authentication failed");
         } else {
            byte[] bytes14 = new byte[16];
            System.arraycopy(bytes4, 0, bytes14, 0, bytes4.length);
            bytes14[15] = 1;
            byte[] bytes15 = new byte[n];
            int[] ints4 = new int[16];

            for (int i33 = 0; i33 < bytes5.length; i33 += 16) {
               for (int i34 = 0; i34 < 16; i34++) {
                  ints4[i34] = bytes14[i34] & 255;
               }

               int n32 = ints.length / 16 - 1;

               for (int i35 = 0; i35 < 16; i35++) {
                  ints4[i35] ^= ints[i35];
               }

               for (int i36 = 1; i36 < n32; i36++) {
                  for (int i37 = 0; i37 < 16; i37++) {
                     ints4[i37] = SBOX[ints4[i37]];
                  }

                  int n33 = ints4[1];
                  ints4[1] = ints4[5];
                  ints4[5] = ints4[9];
                  ints4[9] = ints4[13];
                  ints4[13] = n33;
                  n33 = ints4[2];
                  int n34 = ints4[6];
                  ints4[2] = ints4[10];
                  ints4[6] = ints4[14];
                  ints4[10] = n33;
                  ints4[14] = n34;
                  n33 = ints4[15];
                  ints4[15] = ints4[11];
                  ints4[11] = ints4[7];
                  ints4[7] = ints4[3];
                  ints4[3] = n33;

                  for (int i38 = 0; i38 < 4; i38++) {
                     n33 = ints4[i38 * 4];
                     n34 = ints4[i38 * 4 + 1];
                     int n35 = ints4[i38 * 4 + 2];
                     int n36 = ints4[i38 * 4 + 3];
                     ints4[i38 * 4] = GF_MUL_TABLE[2][n33] ^ GF_MUL_TABLE[3][n34] ^ n35 ^ n36;
                     ints4[i38 * 4 + 1] = n33 ^ GF_MUL_TABLE[2][n34] ^ GF_MUL_TABLE[3][n35] ^ n36;
                     ints4[i38 * 4 + 2] = n33 ^ n34 ^ GF_MUL_TABLE[2][n35] ^ GF_MUL_TABLE[3][n36];
                     ints4[i38 * 4 + 3] = GF_MUL_TABLE[3][n33] ^ n34 ^ n35 ^ GF_MUL_TABLE[2][n36];
                  }

                  int n37 = i36 * 16;

                  for (int i39 = 0; i39 < 16; i39++) {
                     ints4[i39] ^= ints[n37 + i39];
                  }
               }

               for (int i40 = 0; i40 < 16; i40++) {
                  ints4[i40] = SBOX[ints4[i40]];
               }

               int n38 = ints4[1];
               ints4[1] = ints4[5];
               ints4[5] = ints4[9];
               ints4[9] = ints4[13];
               ints4[13] = n38;
               n38 = ints4[2];
               int n39 = ints4[6];
               ints4[2] = ints4[10];
               ints4[6] = ints4[14];
               ints4[10] = n38;
               ints4[14] = n39;
               n38 = ints4[15];
               ints4[15] = ints4[11];
               ints4[11] = ints4[7];
               ints4[7] = ints4[3];
               ints4[3] = n38;
               int n40 = n32 * 16;

               for (int i41 = 0; i41 < 16; i41++) {
                  ints4[i41] ^= ints[n40 + i41];
               }

               int n41 = Math.min(16, bytes5.length - i33);

               for (int i42 = 0; i42 < n41; i42++) {
                  bytes15[i33 + i42] = (byte)(bytes5[i33 + i42] ^ (byte)ints4[i42]);
               }

               int n42 = bytes14.length - 1;

               while (n42 >= 12 && ++bytes14[n42] == 0) {
                  n42--;
               }
            }

            return bytes15;
         }
      }
   }

   private void encryptBlock(int[] ints, int[] ints2, long l) {
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

   private int[] expandKey(long l, byte[] bytes) {
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

}
