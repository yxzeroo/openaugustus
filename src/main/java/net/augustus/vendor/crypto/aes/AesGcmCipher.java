package net.augustus.vendor.crypto.aes;



import net.augustus.vendor.crypto.AesGcmGHashTable;
public class AesGcmCipher {
   private final byte[] hashSubkeyByteBuffer;
   private static int BLOCK_SIZE;
   private byte[] cachedHashSubkey;
   private final byte[] counterBlockByteBuffer;
   private final int[] hashSubkeyStateBuffer = new int[0];
   private static int IV_SIZE;
   private static final int[] RCON = new int[]{0, 1, 2, 4, 8, 16, 32, 64, 128, 27, 54};
   private static int TAG_SIZE;
   private static int AES_128_KEY_SIZE;
   private static int AES_256_KEY_SIZE;
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
   private byte[] cachedKey;
   private final int[] counterBlockStateBuffer = new int[0];
   private int[] cachedRoundKeys;
   private static final int[][] GF_MUL_TABLE = new int[16][256];
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
   private AesGcmGHashTable ghashTable;

   public byte[] decrypt(byte[] bytes, byte[] bytes2, byte[] bytes3) {
      Object obj = null;
      boolean flag = false;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      boolean flag2 = false;
      Object obj5 = null;
      int n = 0;
      Object obj6 = null;
      int n2 = 0;
      int n3 = 0;
      Object obj7 = null;
      Object obj8 = null;
      boolean flag3 = false;
      Object obj9 = null;
      if (bytes2.length != 16 && bytes2.length != 24 && bytes2.length != 32) {
         throw new IllegalArgumentException("Invalid key size. Must be 16, 24, or 32 bytes");
      } else if (bytes.length < 28) {
         throw new IllegalArgumentException("Data too short for GCM mode");
      } else {
         obj6 = new byte[12];
         System.arraycopy(bytes, 0, (byte[])obj6, 0, ((byte[])obj6).length);
         n3 = bytes.length - ((byte[])obj6).length - 16;
         obj3 = new byte[n3];
         System.arraycopy(bytes, ((byte[])obj6).length, (byte[])obj3, 0, n3);
         obj5 = new byte[16];
         System.arraycopy(bytes, ((byte[])obj6).length + n3, (byte[])obj5, 0, 16);
         AesGcmCipher aesGcmCipher = this;
         byte[] bytes4 = bytes2;
         obj8 = aesGcmCipher.expandKey(bytes4);
         aesGcmCipher = this;
         byte[] bytes5 = bytes2;
         int[] ints = (int[])obj8;
         int[] ints2 = ints;
         byte[] bytes6 = bytes5;
         obj4 = aesGcmCipher.computeHashSubkey(bytes6, ints2);
         aesGcmCipher = this;
         byte[] bytes7 = (byte[])obj6;
         byte[] bytes8 = (byte[])obj3;
         byte[] bytes9 = bytes3;
         int[] ints3 = (int[])obj8;
         byte[] bytes10 = (byte[])obj4;
         int[] ints4 = ints3;
         byte[] bytes11 = bytes9;
         byte[] bytes12 = bytes8;
         byte[] bytes13 = bytes7;
         obj9 = aesGcmCipher.computeAuthTag(bytes13, bytes12, bytes11, ints4, bytes10);
         n = 0;

         for (int i = 0; i < 16; i++) {
            n |= ((byte[])obj5)[i] ^ ((byte[])obj9)[i];
         }

         if (n != 0) {
            throw new SecurityException("GCM authentication failed");
         } else {
            obj = new byte[16];
            System.arraycopy((byte[])obj6, 0, (byte[])obj, 0, ((byte[])obj6).length);
            ((byte[])obj)[15] = 1;
            obj7 = new byte[n3];
            obj2 = new int[16];

            for (int j = 0; j < ((byte[])obj3).length; j += 16) {
               for (int k = 0; k < 16; k++) {
                  ((int[])obj2)[k] = ((byte[])obj)[k] & 255;
               }

               aesGcmCipher = this;
               int[] ints5 = (int[])obj2;
               int[] ints6 = (int[])obj8;
               int[] ints7 = ints5;
               aesGcmCipher.encryptBlock(ints7, ints6);
               n2 = Math.min(16, ((byte[])obj3).length - j);

               for (int m = 0; m < n2; m++) {
                  ((byte[])obj7)[j + m] = (byte)(((byte[])obj3)[j + m] ^ (byte)((int[])obj2)[m]);
               }

               for (int idx = ((byte[])obj).length - 1; idx >= 12; idx--) {
                  if (++((byte[])obj)[idx] != 0) {
                     break;
                  }
               }
            }

            return (byte[])obj7;
         }
      }
   }

   public byte[] decrypt(byte[] bytes, byte[] bytes2) {
      Object obj = null;
      return this.decrypt(bytes, bytes2, (byte[])obj);
   }

   private static String bytesToHex(byte[] bytes, int n, int n2) {
      boolean flag = false;
      Object obj = null;
      obj = new StringBuilder();

      for (int i = n; i < n + n2; i++) {
         ((StringBuilder)obj).append(String.format("%02X", bytes[i]));
      }

      return ((StringBuilder)obj).toString();
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

   private void encryptBlock(int[] ints, int[] ints2) {
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      int n6 = 0;
      boolean flag = false;
      int n7 = 0;
      n3 = ints2.length / 16 - 1;

      for (int i = 0; i < 16; i++) {
         ints[i] = ints[i] ^ ints2[i];
      }

      for (int j = 1; j < n3; j++) {
         for (int k = 0; k < 16; k++) {
            ints[k] = SBOX[((int[])ints)[k]];
         }

         n5 = ints[1];
         ints[1] = ints[5];
         ints[5] = ints[9];
         ints[9] = ints[13];
         ints[13] = n5;
         n5 = ints[2];
         n4 = ints[6];
         ints[2] = ints[10];
         ints[6] = ints[14];
         ints[10] = n5;
         ints[14] = n4;
         n5 = ints[15];
         ints[15] = ints[11];
         ints[11] = ints[7];
         ints[7] = ints[3];
         ints[3] = n5;

         for (int m = 0; m < 4; m++) {
            n5 = ints[m * 4];
            n4 = ints[m * 4 + 1];
            n6 = ints[m * 4 + 2];
            n = ints[m * 4 + 3];
            ints[m * 4] = GF_MUL_TABLE[2][n5] ^ GF_MUL_TABLE[3][n4] ^ n6 ^ n;
            ints[m * 4 + 1] = n5 ^ GF_MUL_TABLE[2][n4] ^ GF_MUL_TABLE[3][n6] ^ n;
            ints[m * 4 + 2] = n5 ^ n4 ^ GF_MUL_TABLE[2][n6] ^ GF_MUL_TABLE[3][n];
            ints[m * 4 + 3] = GF_MUL_TABLE[3][n5] ^ n4 ^ n6 ^ GF_MUL_TABLE[2][n];
         }

         n2 = j * 16;

         for (int idx = 0; idx < 16; idx++) {
            ints[idx] = ints[idx] ^ ints2[n2 + idx];
         }
      }

      for (int index = 0; index < 16; index++) {
         ints[index] = SBOX[((int[])ints)[index]];
      }

      n7 = ints[1];
      ints[1] = ints[5];
      ints[5] = ints[9];
      ints[9] = ints[13];
      ints[13] = n7;
      n7 = ints[2];
      n5 = ints[6];
      ints[2] = ints[10];
      ints[6] = ints[14];
      ints[10] = n7;
      ints[14] = n5;
      n7 = ints[15];
      ints[15] = ints[11];
      ints[11] = ints[7];
      ints[7] = ints[3];
      ints[3] = n7;
      n4 = n3 * 16;

      for (int i2 = 0; i2 < 16; i2++) {
         ints[i2] = ints[i2] ^ ints2[n4 + i2];
      }
   }

   private static String bytesToHex(byte[] bytes) {
      int length = bytes.length;
      byte b = 0;
      return bytesToHex(bytes, b, length);
   }

   private byte[] computeAuthTag(byte[] bytes, byte[] bytes2, byte[] bytes3, int[] ints, byte[] bytes4) {
      int n = bytes3 != null ? bytes3.length : 0;
      if (this.ghashTable == null || !this.ghashTable.matchesSubkey(bytes4)) {
         this.ghashTable = new AesGcmGHashTable(bytes4);
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
         this.counterBlockStateBuffer[i2] = bytes7[i2] & 255;
      }

      int[] ints2 = this.counterBlockStateBuffer;
      this.encryptBlock(ints2, ints);

      for (int i3 = 0; i3 < 16; i3++) {
         this.counterBlockByteBuffer[i3] = (byte)this.counterBlockStateBuffer[i3];
      }

      byte[] bytes8 = new byte[16];

      for (int i4 = 0; i4 < 16; i4++) {
         bytes8[i4] = (byte)(bytes5[i4] ^ this.counterBlockByteBuffer[i4]);
      }

      return bytes8;
   }

   private int[] expandKey(byte[] bytes) {
      int n = 0;
      Object obj = null;
      Object obj2 = null;
      int n2 = 0;
      boolean flag = false;
      byte b = 0;
      int n3 = 0;
      if (this.cachedKey != null && bytes.length == this.cachedKey.length) {
         b = 1;

         for (int i = 0; i < ((byte[])bytes).length; i++) {
            if (bytes[i] != this.cachedKey[i]) {
               b = 0;
               break;
            }
         }

         if (b != 0 && this.cachedRoundKeys != null) {
            return this.cachedRoundKeys;
         }
      }

      if (bytes.length != 16) {
         if (bytes.length != 24) {
            b = 14;
         } else {
            b = 12;
         }
      } else {
         b = 10;
      }

      n = bytes.length / 4;
      n2 = 4 * (b + 1);
      obj2 = new int[n2];

      for (int j = 0; j < n; j++) {
         ((int[])obj2)[j] = (bytes[4 * j] & 255) << 24
            | (bytes[4 * j + 1] & 255) << 16
            | (bytes[4 * j + 2] & 255) << 8
            | bytes[4 * j + 3] & 255;
      }

      for (int k = n; k < n2; k++) {
         n3 = ((int[])obj2)[k - 1];
         if (k % n != 0) {
            if (n > 6 && k % n == 4) {
               n3 = SBOX[n3 >>> 24 & 0xFF] << 24 | SBOX[n3 >>> 16 & 0xFF] << 16 | SBOX[n3 >>> 8 & 0xFF] << 8 | SBOX[n3 & 0xFF];
            }
         } else {
            n3 = n3 << 8 & -256 | n3 >>> 24 & 0xFF;
            n3 = SBOX[n3 >>> 24 & 0xFF] << 24 | SBOX[n3 >>> 16 & 0xFF] << 16 | SBOX[n3 >>> 8 & 0xFF] << 8 | SBOX[n3 & 0xFF];
            n3 ^= RCON[k / n] << 24;
         }

         ((int[])obj2)[k] = ((int[])obj2)[k - n] ^ n3;
      }

      obj = new int[n2 * 4];

      for (int m = 0; m < n2; m++) {
         ((int[])obj)[4 * m] = ((int[])obj2)[m] >>> 24 & 0xFF;
         ((int[])obj)[4 * m + 1] = ((int[])obj2)[m] >>> 16 & 0xFF;
         ((int[])obj)[4 * m + 2] = ((int[])obj2)[m] >>> 8 & 0xFF;
         ((int[])obj)[4 * m + 3] = ((int[])obj2)[m] & 0xFF;
      }

      this.cachedKey = new byte[((byte[])bytes).length];
      System.arraycopy(bytes, 0, this.cachedKey, 0, bytes.length);
      this.cachedRoundKeys = (int[])obj;
      return (int[])obj;
   }

   public byte[] encrypt(byte[] bytes, byte[] bytes2, byte[] bytes3) {
      Object obj = null;
      boolean flag = false;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      int n = 0;
      boolean flag2 = false;
      Object obj5 = null;
      long l = 0L;
      Object obj6 = null;
      Object obj7 = null;
      long l2 = 0L;
      long l3 = 0L;
      boolean flag3 = false;
      long l4 = 0L;
      Object obj8 = null;
      if (bytes2.length != 16 && bytes2.length != 24 && bytes2.length != 32) {
         throw new IllegalArgumentException("Invalid key size. Must be 16, 24, or 32 bytes");
      } else {
         obj8 = new byte[12];
         l2 = System.nanoTime();
         l = 25214903917L;
         l4 = 11L;
         l3 = 281474976710655L;

         for (int i = 0; i < ((byte[])obj8).length; i++) {
            l2 = l2 * l + l4 & l3;
            ((byte[])obj8)[i] = (byte)((int)(l2 >> 16 & 255L));
         }

         AesGcmCipher aesGcmCipher = this;
         byte[] bytes4 = bytes2;
         obj3 = aesGcmCipher.expandKey(bytes4);
         aesGcmCipher = this;
         byte[] bytes5 = bytes2;
         int[] ints = (int[])obj3;
         int[] ints2 = ints;
         byte[] bytes6 = bytes5;
         obj2 = aesGcmCipher.computeHashSubkey(bytes6, ints2);
         obj4 = new byte[16];
         System.arraycopy((byte[])obj8, 0, (byte[])obj4, 0, ((byte[])obj8).length);
         ((byte[])obj4)[15] = 1;
         obj5 = new byte[((byte[])bytes).length];
         obj7 = new int[16];

         for (int j = 0; j < ((byte[])bytes).length; j += 16) {
            for (int k = 0; k < 16; k++) {
               ((int[])obj7)[k] = ((byte[])obj4)[k] & 255;
            }

            aesGcmCipher = this;
            int[] ints3 = (int[])obj7;
            int[] ints4 = (int[])obj3;
            int[] ints5 = ints3;
            aesGcmCipher.encryptBlock(ints5, ints4);
            n = Math.min(16, bytes.length - j);

            for (int m = 0; m < n; m++) {
               ((byte[])obj5)[j + m] = (byte)(bytes[j + m] ^ (byte)((int[])obj7)[m]);
            }

            for (int idx = ((byte[])obj4).length - 1; idx >= 12; idx--) {
               if (++((byte[])obj4)[idx] != 0) {
                  break;
               }
            }
         }

         aesGcmCipher = this;
         byte[] bytes7 = (byte[])obj8;
         byte[] bytes8 = (byte[])obj5;
         byte[] bytes9 = bytes3;
         int[] ints6 = (int[])obj3;
         byte[] bytes10 = (byte[])obj2;
         int[] ints7 = ints6;
         byte[] bytes11 = bytes9;
         byte[] bytes12 = bytes8;
         byte[] bytes13 = bytes7;
         obj = aesGcmCipher.computeAuthTag(bytes13, bytes12, bytes11, ints7, bytes10);
         obj6 = new byte[((byte[])obj8).length + ((byte[])obj5).length + ((byte[])obj).length];
         System.arraycopy((byte[])obj8, 0, (byte[])obj6, 0, ((byte[])obj8).length);
         System.arraycopy((byte[])obj5, 0, (byte[])obj6, ((byte[])obj8).length, ((byte[])obj5).length);
         System.arraycopy((byte[])obj, 0, (byte[])obj6, ((byte[])obj8).length + ((byte[])obj5).length, ((byte[])obj).length);
         return (byte[])obj6;
      }
   }

   public AesGcmCipher(short s, int n, short s2) {
      this.hashSubkeyByteBuffer = new byte[0];
      this.counterBlockByteBuffer = new byte[0];
      this.cachedKey = null;
      this.cachedRoundKeys = null;
      this.cachedHashSubkey = null;
   }

   private byte[] computeHashSubkey(byte[] bytes, int[] ints) {
      boolean flag = false;
      boolean flag2 = false;
      if (this.cachedKey != null && this.cachedHashSubkey != null) {
         flag = true;

         for (int i = 0; i < ((byte[])bytes).length; i++) {
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
         this.hashSubkeyStateBuffer[j] = 0;
      }

      AesGcmCipher aesGcmCipher = this;
      int[] ints2 = ints;
      int[] hashSubkeyStateBuffer2 = this.hashSubkeyStateBuffer;
      aesGcmCipher.encryptBlock(hashSubkeyStateBuffer2, ints2);

      for (int k = 0; k < 16; k++) {
         this.hashSubkeyByteBuffer[k] = (byte)this.hashSubkeyStateBuffer[k];
      }

      if (this.cachedHashSubkey == null) {
         this.cachedHashSubkey = new byte[16];
      }

      System.arraycopy(this.hashSubkeyByteBuffer, 0, this.cachedHashSubkey, 0, 16);
      return this.hashSubkeyByteBuffer;
   }

   private void ghashMultiply(byte[] bytes) {
      boolean flag = false;
      long l = 0L;
      long l2 = 0L;
      long l3 = 0L;
      int n = 0;
      l = 0L;
      l3 = 0L;

      for (int i = 0; i < 16; i++) {
         n = bytes[i] & 255;
         if (n != 0) {
            l2 = this.ghashTable.mulTable[i][n];
            l ^= l2 >>> 32;
            l3 ^= l2 & 4294967295L;
         }
      }

      for (int j = 0; j < 8; j++) {
         bytes[j] = (byte)((int)(l >>> 56 - j * 8 & 255L));
         bytes[j + 8] = (byte)((int)(l3 >>> 56 - j * 8 & 255L));
      }
   }

   public byte[] encrypt(byte[] bytes, byte[] bytes2) {
      Object obj = null;
      return this.encrypt(bytes, bytes2, (byte[])obj);
   }

}
