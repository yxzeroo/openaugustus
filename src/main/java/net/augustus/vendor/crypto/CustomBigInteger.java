package net.augustus.vendor.crypto;

import net.augustus.vendor.crypto.math.PrimeBitSieve;
import net.augustus.vendor.crypto.math.ShadedMutableBigInteger;
import java.io.ObjectStreamField;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CustomBigInteger implements Comparable {
   private static final int MAX_CONSTANT = 16;
   public static final int BURNIKEL_ZIEGLER_THRESHOLD = 80;
   private static final int TOOM_COOK_SQUARE_THRESHOLD = 216;
   private static final CustomBigInteger NEGATIVE_ONE;
   private static final double LOG_TWO = Math.log(2.0);
   private static final ObjectStreamField[] serialPersistentFields;
   public static final CustomBigInteger TWO;
   private static final int SCHOENHAGE_BASE_CONVERSION_THRESHOLD = 20;
   private static CustomBigInteger[] posConst = new CustomBigInteger[17];
   private static volatile CustomBigInteger[][] powerCache;
   private static final double[] logCache;
   public static final CustomBigInteger TEN;
   @Deprecated
   private int bitCountPlusOne;
   private static final int MULTIPLY_SQUARE_THRESHOLD = 20;
   private static final int SMALL_PRIME_THRESHOLD = 95;
   private static int[] digitsPerInt;
   public final int[] mag;
   public final int signum;
   private static long[] bitsPerDigit = new long[]{
      0L,
      0L,
      1024L,
      1624L,
      2048L,
      2378L,
      2648L,
      2875L,
      3072L,
      3247L,
      3402L,
      3543L,
      3672L,
      3790L,
      3899L,
      4001L,
      4096L,
      4186L,
      4271L,
      4350L,
      4426L,
      4498L,
      4567L,
      4633L,
      4696L,
      4756L,
      4814L,
      4870L,
      4923L,
      4975L,
      5025L,
      5074L,
      5120L,
      5166L,
      5210L,
      5253L,
      5295L
   };
   private static String[] zeros;
   public static final CustomBigInteger ZERO;
   private static final int MONTGOMERY_INTRINSIC_THRESHOLD = 512;
   private static CustomBigInteger[] longRadix;
   private static final CustomBigInteger SMALL_PRIME_PRODUCT;
   public static final int BURNIKEL_ZIEGLER_OFFSET = 40;
   private static CustomBigInteger[] negConst = new CustomBigInteger[17];
   private static final int KARATSUBA_THRESHOLD = 80;
   private static final int MAX_MAG_LENGTH = 67108864;
   private static final int KARATSUBA_SQUARE_THRESHOLD = 128;
   public static int[] bnExpModThreshTable;
   private static int[] intRadix;
   public static final long LONG_MASK = 4294967295L;
   private static final int PRIME_SEARCH_BIT_LENGTH_LIMIT = 500000000;
   @Deprecated
   private int bitLengthPlusOne;
   private static int[] digitsPerLong;
   @Deprecated
   private int firstNonzeroIntNumPlusTwo;
   public static final CustomBigInteger ONE;
   @Deprecated
   private int lowestSetBitPlusTwo;
   private static final int TOOM_COOK_THRESHOLD = 240;
   private static final int DEFAULT_PRIME_CERTAINTY = 100;

   private static int[] makePositive2(byte[] bytes) {
      int n = 0;
      boolean flag = false;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      Object obj = null;
      int n5 = 0;
      int n6 = 0;
      int n7 = 0;
      int n8 = 0;
      n6 = bytes.length;
      n2 = 0;

      while (n2 < n6 && ((byte[])bytes)[n2] == -1) {
         n2++;
      }

      n7 = n2;

      while (n7 < n6 && ((byte[])bytes)[n7] == 0) {
         n7++;
      }

      n3 = n7 != n6 ? 0 : 1;
      n = n6 - n2 + n3 + 3 >>> 2;
      obj = new int[n];
      n8 = n6 - 1;

      for (int i = n - 1; i >= 0; i--) {
         ((int[])obj)[i] = bytes[n8--] & 255;
         n5 = Math.min(3, n8 - n2 + 1);
         if (n5 < 0) {
            n5 = 0;
         }

         for (int j = 8; j <= 8 * n5; j += 8) {
            ((int[])obj)[i] = ((int[])obj)[i] | (bytes[n8--] & 255) << j;
         }

         n4 = -1 >>> 8 * (3 - n5);
         ((int[])obj)[i] = ~((int[])obj)[i] & n4;
      }

      for (int k = ((int[])obj).length - 1; k >= 0; k--) {
         ((int[])obj)[k] = (int)(((long)((int[])obj)[k] & 4294967295L) + 1L);
         if (((int[])obj)[k] != 0) {
            break;
         }
      }

      return (int[])obj;
   }

   @Override
   public boolean equals(Object obj) {
      boolean flag = false;
      Object obj2 = null;
      Object obj3 = null;
      int n = 0;
      Object obj4 = null;
      if (obj == this) {
         return true;
      } else if (!(obj instanceof CustomBigInteger)) {
         return false;
      } else {
         obj4 = (CustomBigInteger)obj;
         if (((CustomBigInteger)obj4).signum != this.signum) {
            return false;
         } else {
            obj2 = this.mag;
            n = ((int[])obj2).length;
            obj3 = ((CustomBigInteger)obj4).mag;
            if (n != ((int[])obj3).length) {
               return false;
            } else {
               for (int i = 0; i < n; i++) {
                  if (((int[])obj3)[i] != ((int[])obj2)[i]) {
                     return false;
                  }
               }

               return true;
            }
         }
      }
   }

   private CustomBigInteger getLower(int n) {
      int n2 = 0;
      Object obj = null;
      n2 = this.mag.length;
      if (n2 > n) {
         obj = new int[n];
         System.arraycopy(this.mag, n2 - n, (int[])obj, 0, n);
         return new CustomBigInteger(trustedStripLeadingZeroInts((int[])obj), 1);
      } else {
         return this.abs();
      }
   }

   private static CustomBigInteger lucasLehmerSequence(int n, CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      Object obj5 = null;
      boolean flag = false;
      obj = valueOf((long)n);
      obj3 = ONE;
      obj5 = ONE;

      for (int i = customBigInteger.bitLength() - 2; i >= 0; i--) {
         CustomBigInteger customBigInteger3 = (CustomBigInteger)obj3;
         customBigInteger3 = customBigInteger3.multiply2((CustomBigInteger)obj5);
         CustomBigInteger customBigInteger5 = customBigInteger2;
         obj2 = customBigInteger3.mod(customBigInteger5);
         customBigInteger3 = ((CustomBigInteger)obj5).square();
         customBigInteger3 = customBigInteger3.add2(((CustomBigInteger)obj).multiply2(((CustomBigInteger)obj3).square()));
         customBigInteger5 = customBigInteger2;
         obj4 = customBigInteger3.mod(customBigInteger5);
         customBigInteger3 = (CustomBigInteger)obj4;
         if (customBigInteger3.testBit((byte)0)) {
            customBigInteger3 = (CustomBigInteger)obj4;
            obj4 = customBigInteger3.subtract(customBigInteger2);
         }

         customBigInteger3 = (CustomBigInteger)obj4;
         obj4 = customBigInteger3.shiftRight((byte)1);
         obj3 = (CustomBigInteger)obj2;
         obj5 = (CustomBigInteger)obj4;
         if (customBigInteger.testBit(i)) {
            customBigInteger3 = (CustomBigInteger)obj3;
            customBigInteger3 = customBigInteger3.add2((CustomBigInteger)obj5);
            customBigInteger5 = customBigInteger2;
            obj2 = customBigInteger3.mod(customBigInteger5);
            customBigInteger3 = (CustomBigInteger)obj2;
            if (customBigInteger3.testBit((byte)0)) {
               customBigInteger3 = (CustomBigInteger)obj2;
               obj2 = customBigInteger3.subtract(customBigInteger2);
            }

            customBigInteger3 = (CustomBigInteger)obj2;
            obj2 = customBigInteger3.shiftRight((byte)1);
            customBigInteger3 = (CustomBigInteger)obj5;
            customBigInteger3 = customBigInteger3.add2(((CustomBigInteger)obj).multiply2((CustomBigInteger)obj3));
            customBigInteger5 = customBigInteger2;
            obj4 = customBigInteger3.mod(customBigInteger5);
            customBigInteger3 = (CustomBigInteger)obj4;
            if (customBigInteger3.testBit((byte)0)) {
               customBigInteger3 = (CustomBigInteger)obj4;
               obj4 = customBigInteger3.subtract(customBigInteger2);
            }

            customBigInteger3 = (CustomBigInteger)obj4;
            obj4 = customBigInteger3.shiftRight((byte)1);
            obj3 = (CustomBigInteger)obj2;
            obj5 = (CustomBigInteger)obj4;
         }
      }

      return (CustomBigInteger)obj3;
   }

   public int intValueExact() {
      if (this.mag.length <= 1 && this.bitLength() <= 31) {
         return this.intValue();
      } else {
         throw new ArithmeticException("BigInteger out of int range");
      }
   }

   private static int[] makePositive(int[] ints) {
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      Object obj = null;
      n = 0;

      while (n < ((int[])ints).length && ((int[])ints)[n] == -1) {
         n++;
      }

      n3 = n;

      while (n3 < ((int[])ints).length && ((int[])ints)[n3] == 0) {
         n3++;
      }

      n4 = n3 != ((int[])ints).length ? 0 : 1;
      obj = new int[((int[])ints).length - n + n4];

      for (int i = n; i < ((int[])ints).length; i++) {
         ((int[])obj)[i - n + n4] = ~ints[i];
      }

      n2 = ((int[])obj).length - 1;

      while (++((int[])obj)[n2] == 0) {
         n2--;
      }

      return (int[])obj;
   }

   private static int[] multiplyToLen(int[] ints, int n, int[] ints2, int n2, int[] ints3) {
      int n3 = 0;
      long l = 0L;
      boolean flag = false;
      int n4 = 0;
      long l2 = 0L;
      long l3 = 0L;
      int n5 = 0;
      int n6 = 0;
      n3 = n - 1;
      n5 = n2 - 1;
      if (ints3 == null || ints3.length < n + n2) {
         ints3 = new int[n + n2];
      }

      l2 = 0L;
      n6 = n5;

      for (int i = n5 + 1 + n3; n6 >= 0; i--) {
         l = ((long)ints2[n6] & 4294967295L) * ((long)ints[n3] & 4294967295L) + l2;
         ints3[i] = (int)l;
         l2 = l >>> 32;
         n6--;
      }

      ints3[n3] = (int)l2;

      for (int j = n3 - 1; j >= 0; j--) {
         l2 = 0L;
         n4 = n5;

         for (int k = n5 + 1 + j; n4 >= 0; k--) {
            l3 = ((long)ints2[n4] & 4294967295L) * ((long)ints[j] & 4294967295L) + ((long)ints3[k] & 4294967295L) + l2;
            ints3[k] = (int)l3;
            l2 = l3 >>> 32;
            n4--;
         }

         ints3[j] = (int)l2;
      }

      return ints3;
   }

   private static final int[] implSquareToLen(int[] ints, int n, int[] ints2, int n2) {
      int n3 = 0;
      int n4 = 0;
      long l = 0L;
      long l2 = 0L;
      boolean flag = false;
      int n5 = 0;
      n4 = 0;
      n3 = 0;

      for (int i = 0; n3 < n; n3++) {
         l = (long)ints[n3] & 4294967295L;
         l2 = l * l;
         ints2[i++] = n4 << 31 | (int)(l2 >>> 33);
         ints2[i++] = (int)(l2 >>> 1);
         n4 = (int)l2;
      }

      n3 = n;

      for (int j = 1; n3 > 0; j += 2) {
         n5 = ints[n3 - 1];
         int[] ints3 = ints2;
         n5 = mulAdd(ints3, ints, j, n3 - 1, n5);
         ints3 = ints2;
         addOne(ints3, j - 1, n3, n5);
         n3--;
      }

      primitiveLeftShift(ints2, n2, (byte)1);
      ints2[n2 - 1] = ints2[n2 - 1] | ints[n - 1] & 1;
      return ints2;
   }

   private static byte[] randomBits(int n, Random rand) {
      int n2 = 0;
      int n3 = 0;
      Object obj = null;
      if (n >= 0) {
         n3 = (int)(((long)n + 7L) / 8L);
         obj = new byte[n3];
         if (n3 > 0) {
            rand.nextBytes((byte[])obj);
            n2 = 8 * n3 - n;
            ((byte[])obj)[0] = (byte)(((byte[])obj)[0] & (1 << 8 - n2) - 1);
         }

         return (byte[])obj;
      } else {
         throw new IllegalArgumentException("numBits must be non-negative");
      }
   }

   private CustomBigInteger(long l) {
      if (l < 0L) {
         l = -l;
         this.signum = -1;
      } else {
         this.signum = 1;
      }

      int n = (int)(l >>> 32);
      if (n == 0) {
         this.mag = new int[1];
         this.mag[0] = (int)l;
      } else {
         this.mag = new int[2];
         this.mag[0] = n;
         this.mag[1] = (int)l;
      }
   }

   public static void primitiveRightShift(int[] ints, int n, int n2) {
      int n3 = 0;
      int n4 = 0;
      boolean flag = false;
      int n5 = 0;
      n4 = 32 - n2;
      n3 = n - 1;

      for (int i = ints[n3]; n3 > 0; n3--) {
         n5 = i;
         i = ints[n3 - 1];
         ints[n3] = i << n4 | n5 >>> n2;
      }

      ints[0] = ints[0] >>> n2;
   }

   private static CustomBigInteger multiplyToomCook3(CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      int n = 0;
      Object obj4 = null;
      Object obj5 = null;
      Object obj6 = null;
      Object obj7 = null;
      int n2 = 0;
      Object obj8 = null;
      Object obj9 = null;
      int n3 = 0;
      Object obj10 = null;
      Object obj11 = null;
      Object obj12 = null;
      int n4 = 0;
      Object obj13 = null;
      int n5 = 0;
      Object obj14 = null;
      Object obj15 = null;
      int n6 = 0;
      Object obj16 = null;
      Object obj17 = null;
      n = customBigInteger.mag.length;
      n4 = customBigInteger2.mag.length;
      n3 = Math.max(n, n4);
      n5 = (n3 + 2) / 3;
      n2 = n3 - 2 * n5;
      CustomBigInteger customBigInteger3 = customBigInteger;
      obj10 = customBigInteger3.getToomSlice(n5, n2, (byte)0, n3);
      customBigInteger3 = customBigInteger;
      obj9 = customBigInteger3.getToomSlice(n5, n2, (byte)1, n3);
      customBigInteger3 = customBigInteger;
      obj11 = customBigInteger3.getToomSlice(n5, n2, (byte)2, n3);
      customBigInteger3 = customBigInteger2;
      obj2 = customBigInteger3.getToomSlice(n5, n2, (byte)0, n3);
      customBigInteger3 = customBigInteger2;
      obj7 = customBigInteger3.getToomSlice(n5, n2, (byte)1, n3);
      customBigInteger3 = customBigInteger2;
      obj12 = customBigInteger3.getToomSlice(n5, n2, (byte)2, n3);
      customBigInteger3 = (CustomBigInteger)obj11;
      CustomBigInteger customBigInteger4 = (CustomBigInteger)obj12;
      obj = customBigInteger3.multiply(customBigInteger4, true);
      customBigInteger3 = (CustomBigInteger)obj10;
      obj4 = customBigInteger3.add2((CustomBigInteger)obj11);
      customBigInteger3 = (CustomBigInteger)obj2;
      obj3 = customBigInteger3.add2((CustomBigInteger)obj12);
      customBigInteger3 = (CustomBigInteger)obj4;
      customBigInteger3 = customBigInteger3.subtract((CustomBigInteger)obj9);
      customBigInteger4 = (CustomBigInteger)obj3;
      customBigInteger4 = customBigInteger4.subtract((CustomBigInteger)obj7);
      obj14 = customBigInteger3.multiply(customBigInteger4, true);
      customBigInteger3 = (CustomBigInteger)obj4;
      obj4 = customBigInteger3.add2((CustomBigInteger)obj9);
      customBigInteger3 = (CustomBigInteger)obj3;
      obj3 = customBigInteger3.add2((CustomBigInteger)obj7);
      customBigInteger3 = (CustomBigInteger)obj4;
      customBigInteger4 = (CustomBigInteger)obj3;
      obj16 = customBigInteger3.multiply(customBigInteger4, true);
      customBigInteger3 = (CustomBigInteger)obj4;
      customBigInteger3 = customBigInteger3.add2((CustomBigInteger)obj10);
      customBigInteger3 = customBigInteger3.shiftLeft((byte)1);
      customBigInteger3 = customBigInteger3.subtract((CustomBigInteger)obj11);
      customBigInteger4 = (CustomBigInteger)obj3;
      customBigInteger4 = customBigInteger4.add2((CustomBigInteger)obj2);
      customBigInteger4 = customBigInteger4.shiftLeft((byte)1);
      customBigInteger4 = customBigInteger4.subtract((CustomBigInteger)obj12);
      obj5 = customBigInteger3.multiply(customBigInteger4, true);
      customBigInteger3 = (CustomBigInteger)obj10;
      customBigInteger4 = (CustomBigInteger)obj2;
      obj17 = customBigInteger3.multiply(customBigInteger4, true);
      customBigInteger3 = (CustomBigInteger)obj5;
      obj15 = customBigInteger3.subtract((CustomBigInteger)obj14).exactDivideBy3();
      customBigInteger3 = (CustomBigInteger)obj16;
      customBigInteger3 = customBigInteger3.subtract((CustomBigInteger)obj14);
      obj13 = customBigInteger3.shiftRight((byte)1);
      customBigInteger3 = (CustomBigInteger)obj16;
      obj8 = customBigInteger3.subtract((CustomBigInteger)obj);
      customBigInteger3 = (CustomBigInteger)obj15;
      customBigInteger3 = customBigInteger3.subtract((CustomBigInteger)obj8);
      obj15 = customBigInteger3.shiftRight((byte)1);
      customBigInteger3 = (CustomBigInteger)obj8;
      customBigInteger3 = customBigInteger3.subtract((CustomBigInteger)obj13);
      obj8 = customBigInteger3.subtract((CustomBigInteger)obj17);
      customBigInteger3 = (CustomBigInteger)obj15;
      customBigInteger4 = (CustomBigInteger)obj17;
      obj15 = customBigInteger3.subtract(customBigInteger4.shiftLeft((byte)1));
      customBigInteger3 = (CustomBigInteger)obj13;
      obj13 = customBigInteger3.subtract((CustomBigInteger)obj15);
      n6 = n5 * 32;
      customBigInteger3 = ((CustomBigInteger)obj17).shiftLeft(n6);
      customBigInteger3 = customBigInteger3.add2((CustomBigInteger)obj15).shiftLeft(n6);
      customBigInteger3 = customBigInteger3.add2((CustomBigInteger)obj8).shiftLeft(n6);
      customBigInteger3 = customBigInteger3.add2((CustomBigInteger)obj13).shiftLeft(n6);
      obj6 = customBigInteger3.add2((CustomBigInteger)obj);
      return customBigInteger.signum == customBigInteger2.signum ? (CustomBigInteger)obj6 : ((CustomBigInteger)obj6).negate();
   }

   static {
      SMALL_PRIME_PRODUCT = valueOf(152125131763605L);

      for (int i = 1; i <= 16; i++) {
         int[] ints = new int[]{i};
         posConst[i] = new CustomBigInteger(ints, 1);
         negConst[i] = new CustomBigInteger(ints, -1);
      }

      powerCache = new CustomBigInteger[37][];
      logCache = new double[37];

      for (int j = 2; j <= 36; j++) {
         powerCache[j] = new CustomBigInteger[]{valueOf((long)j)};
         logCache[j] = Math.log((double)j);
      }

      ZERO = new CustomBigInteger(new int[0], 0);
      ONE = valueOf(1L);
      TWO = valueOf(2L);
      NEGATIVE_ONE = valueOf(-1L);
      TEN = valueOf(10L);
      bnExpModThreshTable = new int[]{7, 25, 81, 241, 673, 1793, Integer.MAX_VALUE};
      zeros = new String[64];
      zeros[63] = "000000000000000000000000000000000000000000000000000000000000000";

      for (int k = 0; k < 63; k++) {
         zeros[k] = zeros[63].substring(0, k);
      }

      digitsPerLong = new int[]{
         0, 0, 62, 39, 31, 27, 24, 22, 20, 19, 18, 18, 17, 17, 16, 16, 15, 15, 15, 14, 14, 14, 14, 13, 13, 13, 13, 13, 13, 12, 12, 12, 12, 12, 12, 12, 12
      };
      longRadix = new CustomBigInteger[]{
         null,
         null,
         valueOf(4611686018427387904L),
         valueOf(4052555153018976267L),
         valueOf(4611686018427387904L),
         valueOf(7450580596923828125L),
         valueOf(4738381338321616896L),
         valueOf(3909821048582988049L),
         valueOf(1152921504606846976L),
         valueOf(1350851717672992089L),
         valueOf(1000000000000000000L),
         valueOf(5559917313492231481L),
         valueOf(2218611106740436992L),
         valueOf(8650415919381337933L),
         valueOf(2177953337809371136L),
         valueOf(6568408355712890625L),
         valueOf(1152921504606846976L),
         valueOf(2862423051509815793L),
         valueOf(6746640616477458432L),
         valueOf(799006685782884121L),
         valueOf(1638400000000000000L),
         valueOf(3243919932521508681L),
         valueOf(6221821273427820544L),
         valueOf(504036361936467383L),
         valueOf(876488338465357824L),
         valueOf(1490116119384765625L),
         valueOf(2481152873203736576L),
         valueOf(4052555153018976267L),
         valueOf(6502111422497947648L),
         valueOf(353814783205469041L),
         valueOf(531441000000000000L),
         valueOf(787662783788549761L),
         valueOf(1152921504606846976L),
         valueOf(1667889514952984961L),
         valueOf(2386420683693101056L),
         valueOf(3379220508056640625L),
         valueOf(4738381338321616896L)
      };
      digitsPerInt = new int[]{0, 0, 30, 19, 15, 13, 11, 11, 10, 9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5};
      intRadix = new int[]{
         0,
         0,
         1073741824,
         1162261467,
         1073741824,
         1220703125,
         362797056,
         1977326743,
         1073741824,
         387420489,
         1000000000,
         214358881,
         429981696,
         815730721,
         1475789056,
         170859375,
         268435456,
         410338673,
         612220032,
         893871739,
         1280000000,
         1801088541,
         113379904,
         148035889,
         191102976,
         244140625,
         308915776,
         387420489,
         481890304,
         594823321,
         729000000,
         887503681,
         1073741824,
         1291467969,
         1544804416,
         1838265625,
         60466176
      };
      serialPersistentFields = new ObjectStreamField[]{
         new ObjectStreamField("signum", int.class),
         new ObjectStreamField("magnitude", byte[].class),
         new ObjectStreamField("bitCount", int.class),
         new ObjectStreamField("bitLength", int.class),
         new ObjectStreamField("firstNonzeroByteNum", int.class),
         new ObjectStreamField("lowestSetBit", int.class)
      };
   }

   private CustomBigInteger divideBurnikelZiegler(CustomBigInteger customBigInteger) {
      return this.divideAndRemainderBurnikelZiegler(customBigInteger)[0];
   }

   private CustomBigInteger square(boolean flag) {
      Object obj = null;
      int n = 0;
      long l = 0L;
      l = 1283695814953682787L ^ (long)"ᬸ쯗䊢쥉礂\ud831㧒攃\uaa3c⊂諲ộ꤬㢗\ue068\udfb0婃ꁋ\ue701샟퐃隗⫘蟓䃗ᩜ".hashCode();
      if (this.signum != 0) {
         l = l - 8185821811994011723L ^ 4431658052514204250L;
         n = this.mag.length;
         l = l - -7828505316802200186L ^ 81266023522199300L;
         if (n >= 128) {
            l = (l ^ 9112167593997221086L) + -5434576023476111722L;
            if (n >= 216) {
               l = l + 3628260827124130491L ^ 8696062686951583652L;
               if (flag) {
                  l = l + -6020123226072092058L ^ -4470512682026625105L;
               } else {
                  l = (l ^ 6256107339103879050L) + -8154737590504908271L;
                  int length = this.mag.length;
                  if ((long)bitLength(this.mag, length) <= 1073741824L) {
                     l = l - -6803698580997623423L ^ 7850829019055459711L;
                  } else {
                     l = l - 6751865371199096827L ^ -2763914406592639469L;
                     reportOverflow();
                     l = l - 1495749524335566980L ^ 7238999515381452398L;
                  }
               }

               return this.squareToomCook3();
            } else {
               l = l - 7592453178835422844L ^ -4414292315579663916L;
               return this.squareKaratsuba();
            }
         } else {
            l = l - 768586883784523926L ^ 419963754479874608L;
            obj = squareToLen(this.mag, n, (int[])null);
            l = (l ^ -3476840565419333468L) - 8075129253977862740L;
            return new CustomBigInteger(trustedStripLeadingZeroInts((int[])obj), 1);
         }
      } else {
         l = l - 7132332235528438241L ^ -5416367029421626732L;
         return ZERO;
      }
   }

   public CustomBigInteger(byte[] bytes) {
      super();
      if (bytes.length == 0) {
         throw new NumberFormatException("Zero length BigInteger");
      } else {
         if (bytes[0] < 0) {
            this.mag = makePositive2(bytes);
            this.signum = -1;
         } else {
            this.mag = stripLeadingZeroBytes(bytes);
            this.signum = this.mag.length == 0 ? 0 : 1;
         }

         if (this.mag.length >= 67108864) {
            this.checkRange();
         }
      }
   }

   private static int[] trustedStripLeadingZeroInts(int[] ints) {
      int n = 0;
      int n2 = 0;
      n = ints.length;
      n2 = 0;

      while (n2 < n && ((int[])ints)[n2] == 0) {
         n2++;
      }

      return n2 != 0 ? Arrays.copyOfRange(ints, n2, n) : ints;
   }

   private static int[] add2(int[] ints, int[] ints2) {
      long l = 0L;
      boolean flag = false;
      Object obj = null;
      Object obj2 = null;
      int n = 0;
      int n2 = 0;
      Object obj3 = null;
      long l2 = 0L;
      l2 = 4921104336742052236L ^ (long)"䤨퐝㎮뻲솷饂\uddcfථ꼹ザﭛ\ue448꼓Ｐ嬲돑貗癒䲁샸姩㧵\ued3aꐷ麁".hashCode();
      if (ints.length >= ints2.length) {
         l2 = (l2 ^ -1847263828821085226L) - 5249991949621606289L;
      } else {
         l2 = l2 - -6638114246833375985L ^ 92822628105142463L;
         obj2 = ints;
         l2 = l2 - 3440846533025917601L ^ -1199978214733284156L;
         ints = ints2;
         l2 = (l2 ^ 2189971071631140511L) + 7662139585997636104L;
         ints2 = (int[])obj2;
         l2 = (l2 ^ 4599216145270219479L) - 8947692661431259682L;
      }

      n = ints.length;
      l2 = (l2 ^ -568131867350129249L) - -1482816457406939054L;
      n2 = ints2.length;
      l2 = (l2 ^ 1326361179912122123L) - 7082831880226235658L;
      obj = new int[n];
      l2 = l2 + -5714127274531265647L ^ 5690721964660963921L;
      l = 0L;
      l2 = l2 + -7390272495868194043L ^ 2252930484530445865L;
      if (n2 != 1) {
         l2 = (l2 ^ 3039942414837156136L) - -195223362451424198L;

         while (n2 > 0) {
            l2 = (l2 ^ -6857388347854188653L) + 6615634029763002117L;
            l = ((long)ints[--n] & 4294967295L) + ((long)ints2[--n2] & 4294967295L) + (l >>> 32);
            l2 = (l2 ^ -7535840968607354279L) - 2374126493943951619L;
            ((int[])obj)[n] = (int)l;
            l2 = (l2 ^ 1312599908869512807L) - 7988364577768666107L;
            l2 = l2 + 1931850838496450148L ^ 5004732230984241674L;
         }

         l2 = l2 + 5227256867097957270L ^ 6674236655356737422L;
      } else {
         l2 = l2 - 452072610182910405L ^ 1313981761138283337L;
         l = ((long)ints[--n] & 4294967295L) + ((long)ints2[0] & 4294967295L);
         l2 = (l2 ^ 2258637485872361633L) - 1554580430786973604L;
         ((int[])obj)[n] = (int)l;
         l2 = l2 - 6756995356778604927L ^ -7884429498517651740L;
         l2 = (l2 ^ 6404153425482917017L) + -3348123747641423639L;
      }

      boolean flag2;
      if (l >>> 32 == 0L) {
         l2 = (l2 ^ 6251669763416454806L) - 4883771220583436669L;
         flag2 = false;
      } else {
         l2 = l2 - 3491643751461080687L ^ -3985411375421302560L;
         flag2 = true;
      }

      flag = flag2;

      while (n > 0 && flag) {
         int[] ints3 = (int[])obj;
         n--;
         flag = (ints3[n] = ints[n] + 1) == 0;
      }

      while (n > 0) {
         ((int[])obj)[n] = ints[--n];
      }

      if (flag) {
         obj3 = new int[((int[])obj).length + 1];
         System.arraycopy((int[])obj, 0, (int[])obj3, 1, ((int[])obj).length);
         ((int[])obj3)[0] = 1;
         return (int[])obj3;
      } else {
         return (int[])obj;
      }
   }

   private int intLength() {
      return (this.bitLength() >>> 5) + 1;
   }

   private static int bitLength(int[] ints, int n) {
      return n != 0 ? (n - 1 << 5) + bitLengthForInt(ints[0]) : 0;
   }

   public static CustomBigInteger probablePrime(int n, Random rand) {
      long l = 0L;
      l = 2562629296626168759L ^ (long)"₻䊵ꈄ油\ufbc3햙緬資ꏾ禠ါ껈گ暰蚥\u0b12玉嵏帽ს".hashCode();
      if (n >= 2) {
         l = l - 3348014709504609569L ^ -9081365605874371136L;
         short s = 1057;
         CustomBigInteger customBigInteger;
         if (n >= 95) {
            l = (l ^ -7880086440277349823L) - 1140448652992171493L;
            customBigInteger = largePrime(n, 100, rand);
         } else {
            l = (l ^ -8791172916768821374L) - -347724312140345180L;
            customBigInteger = smallPrime(n, (byte)100, rand);
         }

         return customBigInteger;
      } else {
         l = (l ^ 5241682345853385131L) + 7716239402176078457L;
         throw new ArithmeticException("bitLength < 2");
      }
   }

   private int signBit(short s, long l) {
      return this.signum < 0 ? 1 : 0;
   }

   private byte[] magSerializedForm() {
      int n = 0;
      int n2 = 0;
      boolean flag = false;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      Object obj = null;
      int n6 = 0;
      n5 = this.mag.length;
      n4 = n5 != 0 ? (n5 - 1 << 5) + bitLengthForInt(this.mag[0]) : 0;
      n6 = n4 + 7 >>> 3;
      obj = new byte[n6];
      n3 = n6 - 1;
      n = 4;
      n2 = n5 - 1;

      for (int i = 0; n3 >= 0; n3--) {
         if (n == 4) {
            i = this.mag[n2--];
            n = 1;
         } else {
            i >>>= 8;
            n++;
         }

         ((byte[])obj)[n3] = (byte)i;
      }

      return (byte[])obj;
   }

   private static int[] stripLeadingZeroBytes(byte[] bytes) {
      int n = 0;
      boolean flag = false;
      int n2 = 0;
      boolean flag2 = false;
      int n3 = 0;
      Object obj = null;
      int n4 = 0;
      int n5 = 0;
      int n6 = 0;
      n5 = bytes.length;
      n6 = 0;

      while (n6 < n5 && ((byte[])bytes)[n6] == 0) {
         n6++;
      }

      n3 = n5 - n6 + 3 >>> 2;
      obj = new int[n3];
      n2 = n5 - 1;

      for (int i = n3 - 1; i >= 0; i--) {
         ((int[])obj)[i] = bytes[n2--] & 255;
         n = n2 - n6 + 1;
         n4 = Math.min(3, n);

         for (int j = 8; j <= n4 << 3; j += 8) {
            ((int[])obj)[i] = ((int[])obj)[i] | (bytes[n2--] & 255) << j;
         }
      }

      return (int[])obj;
   }

   @Override
   public int compareTo(Object other) {
      return this.compareTo2((CustomBigInteger)other);
   }

   private CustomBigInteger remainderKnuth(CustomBigInteger customBigInteger) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      obj2 = new ShadedMutableBigInteger();
      obj = new ShadedMutableBigInteger(this.mag);
      obj3 = new ShadedMutableBigInteger(customBigInteger.mag);
      ShadedMutableBigInteger shadedMutableBigInteger = (ShadedMutableBigInteger)obj;
      shadedMutableBigInteger = shadedMutableBigInteger.divideKnuth((ShadedMutableBigInteger)obj3, (ShadedMutableBigInteger)obj2);
      return shadedMutableBigInteger.toBigInteger(this.signum);
   }

   private static void reportOverflow() {
      throw new ArithmeticException("BigInteger would overflow supported range");
   }

   private CustomBigInteger divideKnuth(CustomBigInteger customBigInteger) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      obj3 = new ShadedMutableBigInteger();
      obj2 = new ShadedMutableBigInteger(this.mag);
      obj = new ShadedMutableBigInteger(customBigInteger.mag);
      ShadedMutableBigInteger shadedMutableBigInteger = (ShadedMutableBigInteger)obj2;
      shadedMutableBigInteger.divideKnuth((ShadedMutableBigInteger)obj, (ShadedMutableBigInteger)obj3, false);
      shadedMutableBigInteger = (ShadedMutableBigInteger)obj3;
      return shadedMutableBigInteger.toBigInteger(this.signum * customBigInteger.signum);
   }

   public byte[] toByteArray() {
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      Object obj = null;
      boolean flag = false;
      n = this.bitLength() / 8 + 1;
      obj = new byte[n];
      n2 = n - 1;
      n4 = 4;
      n3 = 0;

      for (int i = 0; n2 >= 0; n2--) {
         if (n4 != 4) {
            n3 >>>= 8;
            n4++;
         } else {
            n3 = this.getInt(i++);
            n4 = 1;
         }

         ((byte[])obj)[n2] = (byte)n3;
      }

      return (byte[])obj;
   }

   public boolean primeToCertainty(int n, Random rand) {
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      n3 = (Math.min(n, 2147483646) + 1) / 2;
      n2 = this.bitLength();
      if (n2 >= 100) {
         if (n2 < 256) {
            n4 = (byte)27;
         } else if (n2 < 512) {
            n4 = (byte)15;
         } else if (n2 < 768) {
            n4 = (byte)8;
         } else if (n2 < 1024) {
            n4 = (byte)4;
         } else {
            n4 = (byte)2;
         }

         n4 = n3 < n4 ? n3 : n4;
         return this.passesMillerRabin(n4, rand) && this.passesLucasLehmer();
      } else {
         int n5 = 50;
         n5 = n3 >= n5 ? n5 : n3;
         return this.passesMillerRabin(n5, rand);
      }
   }

   private static int[] materialize(int[] ints, int n) {
      if (ints == null || ints.length < n) {
         ints = new int[n];
      }

      return ints;
   }

   public CustomBigInteger multiply(long l) {
      long l2 = 0L;
      long l3 = 0L;
      Object obj = null;
      long l4 = 0L;
      int n = 0;
      int n2 = 0;
      long l5 = 0L;
      Object obj2 = null;
      boolean flag = false;
      int n3 = 0;
      long l6 = 0L;
      l6 = -6460699474741310234L ^ (long)"濫㑮ꠄ郒皩蚜௴㛫쎈頖紱\udb6e혡땚몂緦她놯༐㿊⼴넲劤棱椋".hashCode();
      if (l == 0L) {
         l6 = l6 + -8445564418673876415L ^ -7012394514345319814L;
      } else {
         l6 = (l6 ^ 4039593461297296302L) + 5368806868500511211L;
         if (this.signum != 0) {
            l6 = (l6 ^ 7851934417143578726L) + -6368622982919271548L;
            if (l == Long.MIN_VALUE) {
               l6 = l6 - -2446038088506576659L ^ -6527181092642334591L;
               return this.multiply2(valueOf(l));
            }

            l6 = (l6 ^ 5742479183066566923L) + -357701382391357658L;
            int n4;
            if (l <= 0L) {
               l6 = l6 - 5303514130638223440L ^ -2517822879829872124L;
               n4 = -this.signum;
            } else {
               l6 = l6 - 2221988803000449064L ^ 14316138548409324L;
               n4 = this.signum;
            }

            n2 = n4;
            if (l < 0L) {
               l = -l;
            }

            l2 = l >>> 32;
            l4 = l & 4294967295L;
            n3 = this.mag.length;
            obj2 = this.mag;
            obj = l2 == 0L ? new int[n3 + 1] : new int[n3 + 2];
            l5 = 0L;
            n = ((int[])obj).length - 1;

            for (int i = n3 - 1; i >= 0; i--) {
               l3 = ((long)((int[])obj2)[i] & 4294967295L) * l4 + l5;
               ((int[])obj)[n--] = (int)l3;
               l5 = l3 >>> 32;
            }

            ((int[])obj)[n] = (int)l5;
            if (l2 != 0L) {
               l5 = 0L;
               n = ((int[])obj).length - 2;

               for (int j = n3 - 1; j >= 0; j--) {
                  l3 = ((long)((int[])obj2)[j] & 4294967295L) * l2 + ((long)((int[])obj)[n] & 4294967295L) + l5;
                  ((int[])obj)[n--] = (int)l3;
                  l5 = l3 >>> 32;
               }

               ((int[])obj)[0] = (int)l5;
            }

            if (l5 == 0L) {
               obj = Arrays.copyOfRange((int[])obj, 1, ((int[])obj).length);
            }

            return new CustomBigInteger((int[])obj, n2);
         }

         l6 = (l6 ^ -2070974218785270601L) - -3130133865053801049L;
      }

      return ZERO;
   }

   public CustomBigInteger(char[] chars, int n, int n2) {
      super();
      int n3 = 0;

      while (n3 < n2 && Character.digit(chars[n3], 10) == 0) {
         n3++;
      }

      if (n3 == n2) {
         this.signum = 0;
         this.mag = ZERO.mag;
      } else {
         int n4 = n2 - n3;
         this.signum = n;
         int n5;
         if (n2 < 10) {
            n5 = 1;
         } else {
            long l = ((long)n4 * bitsPerDigit[10] >>> 10) + 1L;
            if (l + 31L >= 4294967296L) {
               reportOverflow();
            }

            n5 = (int)(l + 31L) >>> 5;
         }

         int[] ints = new int[n5];
         int n6 = n4 % digitsPerInt[10];
         if (n6 == 0) {
            n6 = digitsPerInt[10];
         }

         int n7 = n5 - 1;
         int n8;
         ints[n7] = this.parseInt(chars, n3, n8 = n3 + n6);

         while (n8 < n2) {
            int n13 = this.parseInt(chars, n8, n8 += digitsPerInt[10]);
            destructiveMulAdd(ints, intRadix[10], n13);
         }

         this.mag = trustedStripLeadingZeroInts(ints);
         if (this.mag.length >= 67108864) {
            this.checkRange();
         }
      }
   }

   public CustomBigInteger min(CustomBigInteger customBigInteger) {
      return this.compareTo2(customBigInteger) < 0 ? this : customBigInteger;
   }

   public CustomBigInteger abs() {
      return this.signum >= 0 ? this : this.negate();
   }

   private static int intArrayCmpToLen(int[] ints, int[] ints2, int n) {
      long l = 0L;
      int n2 = 0;
      long l2 = 0L;
      long l3 = 0L;
      l3 = 229629092787153606L ^ (long)"\udddd緷抯拴剙\ueac8럏簐\ue3c5ꡰ℧圊\uebd9侠\uf5f5믃콠禽慵㢴\uecd3깅".hashCode();
      n2 = 0;
      l3 = l3 - 8653718789823718346L ^ -7963521882195308854L;

      while (n2 < n) {
         l3 = (l3 ^ -2854439179632561691L) - -434049994958143934L;
         l2 = (long)ints[n2] & 4294967295L;
         l3 = (l3 ^ -5545810564775760592L) + 8319854202739481076L;
         l = (long)ints2[n2] & 4294967295L;
         l3 = (l3 ^ -4628156536508793306L) + 6272243697105357254L;
         if (l2 < l) {
            l3 = (l3 ^ 8129586960978657600L) + -3571679642337413739L;
            return -1;
         }

         l3 = (l3 ^ -286896727921986174L) + -2260217288437542438L;
         if (l2 > l) {
            l3 = (l3 ^ -4023268541472558992L) + 8535421609521623761L;
            return 1;
         }

         l3 = l3 - 483154083826736273L ^ -536387705785068049L;
         n2++;
         l3 = l3 - -5458017966670422913L ^ -8499013033166979913L;
         l3 = (l3 ^ 2802638442931795556L) - 7726651105127520457L;
      }

      l3 = (l3 ^ 3916923915605829195L) - -3052274039716033388L;
      return 0;
   }

   public CustomBigInteger flipBit(int n) {
      boolean flag = false;
      Object obj = null;
      int n2 = 0;
      if (n < 0) {
         throw new ArithmeticException("Negative bit address");
      } else {
         n2 = n >>> 5;
         obj = new int[Math.max(this.intLength(), n2 + 2)];

         for (int i = 0; i < ((int[])obj).length; i++) {
            ((int[])obj)[((int[])obj).length - i - 1] = this.getInt(i);
         }

         ((int[])obj)[((int[])obj).length - n2 - 1] = ((int[])obj)[((int[])obj).length - n2 - 1] ^ 1 << (n & 31);
         return valueOf2((int[])obj);
      }
   }

   private static void implMontgomeryMultiplyChecks(int[] ints, int[] ints2, int[] ints3, int n, int[] ints4) throws RuntimeException {
      if (n % 2 == 0) {
         if (n < 1) {
            throw new IllegalArgumentException("invalid input length: " + n);
         } else if (n > ints.length || n > ints2.length || n > ints3.length || ints4 != null && n > ints4.length) {
            throw new IllegalArgumentException("input array length out of bound: " + n);
         }
      } else {
         throw new IllegalArgumentException("input array length must be even: " + n);
      }
   }

   private static int[] implMontgomeryMultiply(int[] ints, int[] ints2, int[] ints3, int n, long l, int[] ints4) {
      ints4 = multiplyToLen(ints, n, ints2, n, ints4);
      return montReduce(ints4, ints3, n, (int)l);
   }

   public CustomBigInteger(int[] ints, int n) {
      super();
      this.signum = ints.length == 0 ? 0 : n;
      this.mag = ints;
      if (this.mag.length >= 67108864) {
         this.checkRange();
      }
   }

   public int bitLength() {
      int n = 0;
      int n2 = 0;
      Object obj = null;
      boolean flag = false;
      int n3 = 0;
      boolean flag2 = false;
      n3 = this.bitLengthPlusOne - 1;
      if (n3 == -1) {
         obj = this.mag;
         n2 = ((int[])obj).length;
         if (n2 == 0) {
            n3 = 0;
         } else {
            n = (n2 - 1 << 5) + bitLengthForInt(this.mag[0]);
            if (this.signum >= 0) {
               n3 = n;
            } else {
               flag2 = Integer.bitCount(this.mag[0]) == 1;

               for (int i = 1; i < n2 && flag2; i++) {
                  flag2 = this.mag[i] == 0;
               }

               n3 = flag2 ? n - 1 : n;
            }
         }

         this.bitLengthPlusOne = n3 + 1;
      }

      return n3;
   }

   private CustomBigInteger modPow2(CustomBigInteger customBigInteger, int n) {
      int n2 = 0;
      Object obj = null;
      int n3 = 0;
      Object obj2 = null;
      obj = ONE;
      CustomBigInteger customBigInteger2 = this;
      obj2 = customBigInteger2.mod2(n);
      n3 = 0;
      n2 = customBigInteger.bitLength();
      customBigInteger2 = this;
      if (customBigInteger2.testBit((byte)0)) {
         n2 = n - 1 >= n2 ? n2 : n - 1;
      }

      while (n3 < n2) {
         if (customBigInteger.testBit(n3)) {
            customBigInteger2 = (CustomBigInteger)obj;
            customBigInteger2 = customBigInteger2.multiply2((CustomBigInteger)obj2);
            obj = customBigInteger2.mod2(n);
         }

         if (++n3 < n2) {
            customBigInteger2 = ((CustomBigInteger)obj2).square();
            obj2 = customBigInteger2.mod2(n);
         }
      }

      return (CustomBigInteger)obj;
   }

   private static int[] stripLeadingZeroInts(int[] ints) {
      int n = 0;
      int n2 = 0;
      n = ints.length;
      n2 = 0;

      while (n2 < n && ((int[])ints)[n2] == 0) {
         n2++;
      }

      return Arrays.copyOfRange(ints, n2, n);
   }

   public int getSignum() {
      return this.signum;
   }

   private CustomBigInteger[] divideAndRemainderBurnikelZiegler(CustomBigInteger customBigInteger) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      obj = new ShadedMutableBigInteger();
      ShadedMutableBigInteger shadedMutableBigInteger = new ShadedMutableBigInteger(this);
      obj2 = shadedMutableBigInteger.divideAndRemainderBurnikelZiegler(new ShadedMutableBigInteger(customBigInteger), (ShadedMutableBigInteger)obj);
      CustomBigInteger customBigInteger2;
      if (!((ShadedMutableBigInteger)obj).isZero()) {
         shadedMutableBigInteger = (ShadedMutableBigInteger)obj;
         customBigInteger2 = shadedMutableBigInteger.toBigInteger(this.signum * customBigInteger.signum);
      } else {
         customBigInteger2 = ZERO;
      }

      obj4 = customBigInteger2;
      CustomBigInteger customBigInteger3;
      if (((ShadedMutableBigInteger)obj2).isZero()) {
         customBigInteger3 = ZERO;
      } else {
         shadedMutableBigInteger = (ShadedMutableBigInteger)obj2;
         customBigInteger3 = shadedMutableBigInteger.toBigInteger(this.signum);
      }

      obj3 = customBigInteger3;
      return new CustomBigInteger[]{(CustomBigInteger)obj4, (CustomBigInteger)obj3};
   }

   private CustomBigInteger remainderBurnikelZiegler(CustomBigInteger customBigInteger) {
      return this.divideAndRemainderBurnikelZiegler(customBigInteger)[1];
   }

   public CustomBigInteger negate() {
      return new CustomBigInteger(this.mag, -this.signum);
   }

   public CustomBigInteger(String str, int n) {
      super();
      int n2 = 0;
      int length = str.length();
      if (n < 2 || n > 36) {
         throw new NumberFormatException("Radix out of range");
      } else if (length == 0) {
         throw new NumberFormatException("Zero length BigInteger");
      } else {
         byte b = 1;
         int n3 = str.lastIndexOf(45);
         int n4 = str.lastIndexOf(43);
         if (n3 >= 0) {
            if (n3 != 0 || n4 >= 0) {
               throw new NumberFormatException("Illegal embedded sign character");
            }

            b = -1;
            n2 = 1;
         } else if (n4 >= 0) {
            if (n4 != 0) {
               throw new NumberFormatException("Illegal embedded sign character");
            }

            n2 = 1;
         }

         if (n2 == length) {
            throw new NumberFormatException("Zero length BigInteger");
         } else {
            while (n2 < length && Character.digit(str.charAt(n2), n) == 0) {
               n2++;
            }

            if (n2 == length) {
               this.signum = 0;
               this.mag = ZERO.mag;
            } else {
               int n5 = length - n2;
               this.signum = b;
               long l = ((long)n5 * bitsPerDigit[n] >>> 10) + 1L;
               if (l + 31L >= 4294967296L) {
                  reportOverflow();
               }

               int n6 = (int)(l + 31L) >>> 5;
               int[] ints = new int[n6];
               int n7 = n5 % digitsPerInt[n];
               if (n7 == 0) {
                  n7 = digitsPerInt[n];
               }

               int n8;
               String str2 = str.substring(n2, n8 = n2 + n7);
               ints[n6 - 1] = Integer.parseInt(str2, n);
               if (ints[n6 - 1] < 0) {
                  throw new NumberFormatException("Illegal digit");
               } else {
                  int n9 = intRadix[n];
                  int n10 = 0;

                  while (n8 < length) {
                     str2 = str.substring(n8, n8 += digitsPerInt[n]);
                     n10 = Integer.parseInt(str2, n);
                     if (n10 < 0) {
                        throw new NumberFormatException("Illegal digit");
                     }

                     destructiveMulAdd(ints, n9, n10);
                  }

                  this.mag = trustedStripLeadingZeroInts(ints);
                  if (this.mag.length >= 67108864) {
                     this.checkRange();
                  }
               }
            }
         }
      }
   }

   public CustomBigInteger not() {
      boolean flag = false;
      Object obj = null;
      obj = new int[this.intLength()];

      for (int i = 0; i < ((int[])obj).length; i++) {
         ((int[])obj)[i] = ~this.getInt(((int[])obj).length - i - 1);
      }

      return valueOf2((int[])obj);
   }

   public CustomBigInteger nextProbablePrime() {
      Object obj = null;
      int n = 0;
      long l = 0L;
      Object obj2 = null;
      Object obj3 = null;
      long l2 = 0L;
      l2 = -3254344292540842564L ^ (long)"\ue749β㰮\uf514\ue9f8\ue603䉑\uf1a6膇飣\ud818㍼멑禆魫蓝臶䅼샃\u05ec池".hashCode();
      if (this.signum < 0) {
         l2 = l2 - 4830160581217484253L ^ -879361078440653390L;
         throw new ArithmeticException("start < 0: " + this);
      } else {
         l2 = (l2 ^ -7442106910266815951L) - 1084712806563262337L;
         if (this.signum == 0) {
            l2 = l2 + -1231153275167702332L ^ 7709014834697806686L;
         } else {
            l2 = (l2 ^ -2516122261737304148L) - 4536460740630707385L;
            CustomBigInteger customBigInteger = this;
            if (!customBigInteger.equals(ONE)) {
               l2 = l2 - -5185581359047882191L ^ 2367408011365775094L;
               customBigInteger = this;
               obj2 = customBigInteger.add2(ONE);
               l2 = l2 - 7206627573463470819L ^ 8313780117570102144L;
               if (((CustomBigInteger)obj2).bitLength() >= 95) {
                  l2 = (l2 ^ 1675128445216981172L) + -5382126396105085138L;
                  customBigInteger = (CustomBigInteger)obj2;
                  if (!customBigInteger.testBit((byte)0)) {
                     l2 = l2 - -4769306242118416468L ^ -3667191869455687237L;
                  } else {
                     l2 = (l2 ^ -4792100454391043225L) - 2405066147296412284L;
                     customBigInteger = (CustomBigInteger)obj2;
                     obj2 = customBigInteger.subtract(ONE);
                     l2 = (l2 ^ -3402456919329072575L) - -4926098273235102117L;
                  }

                  n = getPrimeSearchLen(((CustomBigInteger)obj2).bitLength());
                  l2 = (l2 ^ 676611867512405829L) - 8378953627904490277L;

                  while (true) {
                     obj = new PrimeBitSieve((CustomBigInteger)obj2, n);
                     l2 = (l2 ^ 5834496939486698261L) + 6055870793441547214L;
                     obj3 = ((PrimeBitSieve)obj).retrieve((CustomBigInteger)obj2, (byte)100, (Random)null);
                     l2 = (l2 ^ -2198062454194234124L) - 8086229052485558479L;
                     if ((CustomBigInteger)obj3 != null) {
                        l2 = l2 + -8275227020085402456L ^ -2890168419806319278L;
                        return (CustomBigInteger)obj3;
                     }

                     l2 = (l2 ^ 1010580658101022752L) - -1204876248165915141L;
                     customBigInteger = (CustomBigInteger)obj2;
                     obj2 = customBigInteger.add2(valueOf((long)(2 * n)));
                     l2 = l2 - -2269165682368058594L ^ -3433638230985448170L;
                     l2 = (l2 ^ 6713390364285488257L) - -9108635218605819888L;
                  }
               }

               l2 = (l2 ^ 7223124733986542810L) + -7470562948860177782L;
               customBigInteger = (CustomBigInteger)obj2;
               if (customBigInteger.testBit((byte)0)) {
                  l2 = (l2 ^ 5570802602461623122L) - -4498063795859805110L;
               } else {
                  l2 = (l2 ^ 6013098401682785120L) - 8443276199392400511L;
                  customBigInteger = (CustomBigInteger)obj2;
                  obj2 = customBigInteger.add2(ONE);
                  l2 = l2 - 8043014133033061445L ^ -639657273611279224L;
               }

               while (true) {
                  while (true) {
                     if (((CustomBigInteger)obj2).bitLength() <= 6) {
                        l2 = (l2 ^ -3157028020191371195L) + -5449271530949665850L;
                        break;
                     }

                     l2 = (l2 ^ -2707609198627166641L) + -3448717579860044592L;
                     customBigInteger = (CustomBigInteger)obj2;
                     l = customBigInteger.remainder(SMALL_PRIME_PRODUCT).longValue();
                     l2 = (l2 ^ 7241998563294424600L) + 2804513173275908681L;
                     if (l % 3L == 0L) {
                        l2 = l2 + 3446692990268405237L ^ -4717044137293373717L;
                     } else {
                        l2 = l2 - -3255166948560164313L ^ -150355004537945513L;
                        if (l % 5L == 0L) {
                           l2 = (l2 ^ 793250334073367115L) + 4015447948532009421L;
                        } else {
                           l2 = (l2 ^ 9044666179381033797L) - 3308409298252352122L;
                           if (l % 7L == 0L) {
                              l2 = l2 + 459242770513321581L ^ 6113976809854135248L;
                           } else {
                              l2 = l2 - -4979532936773911611L ^ 3959031926357952811L;
                              if (l % 11L == 0L) {
                                 l2 = (l2 ^ 5280639309196120721L) + 6171107794648159580L;
                              } else {
                                 l2 = l2 + 3637263403075059778L ^ -2880386203453637301L;
                                 if (l % 13L == 0L) {
                                    l2 = (l2 ^ -8487986652631971632L) - 7183019013144464620L;
                                 } else {
                                    l2 = (l2 ^ 8381328487074537269L) - 1356875043962665445L;
                                    if (l % 17L == 0L) {
                                       l2 = l2 + 4846616768747098660L ^ 3298355552248779592L;
                                    } else {
                                       l2 = l2 + 8818222018233214088L ^ -8506701812826622449L;
                                       if (l % 19L == 0L) {
                                          l2 = (l2 ^ -217715426850052219L) + -8381056829996446886L;
                                       } else {
                                          l2 = (l2 ^ -6523692086033105392L) + -3280031275345284686L;
                                          if (l % 23L == 0L) {
                                             l2 = (l2 ^ -4645044793674124613L) - -4912095003625988770L;
                                          } else {
                                             l2 = (l2 ^ 6307793292455865005L) + -5264017164733953645L;
                                             if (l % 29L == 0L) {
                                                l2 = (l2 ^ -8775866402025536268L) + 8605340738296325265L;
                                             } else {
                                                l2 = (l2 ^ -2349358511697200757L) - 4157614356372711738L;
                                                if (l % 31L == 0L) {
                                                   l2 = (l2 ^ -1574074451155245695L) - 1015429460223322039L;
                                                } else {
                                                   l2 = l2 - 6197220723892878770L ^ 5831380846902138870L;
                                                   if (l % 37L == 0L) {
                                                      l2 = l2 + -6685219498966387086L ^ -7353195838882906428L;
                                                   } else {
                                                      l2 = (l2 ^ -8774561403388626991L) + 7778775831282893489L;
                                                      if (l % 41L != 0L) {
                                                         l2 = (l2 ^ 1738044680847679092L) + -4630234085157696363L;
                                                         break;
                                                      }

                                                      l2 = (l2 ^ -8916879039449401034L) - 4083410739612015426L;
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }

                     customBigInteger = (CustomBigInteger)obj2;
                     obj2 = customBigInteger.add2(TWO);
                     l2 = (l2 ^ -8170611562192461252L) + 6711690782577839212L;
                     l2 = l2 - 8961757964211414498L ^ 8522175016174241068L;
                  }

                  if (((CustomBigInteger)obj2).bitLength() < 4) {
                     l2 = (l2 ^ -1110026196037335336L) - -801868326891036296L;
                     return (CustomBigInteger)obj2;
                  }

                  l2 = (l2 ^ 7894722810051679415L) + 5107851734129695915L;
                  customBigInteger = (CustomBigInteger)obj2;
                  if (customBigInteger.primeToCertainty((byte)100, (Random)null)) {
                     l2 = l2 - -3303406153938353584L ^ 1348366799722769486L;
                     return (CustomBigInteger)obj2;
                  }

                  l2 = (l2 ^ -6186326948656422030L) - -2800041517245985555L;
                  customBigInteger = (CustomBigInteger)obj2;
                  obj2 = customBigInteger.add2(TWO);
                  l2 = l2 - 8563108411374013162L ^ -2735285905162788181L;
                  l2 = l2 - -956407928713909540L ^ -5112361354753755795L;
               }
            }

            l2 = l2 - -266214418585104422L ^ -1477069312089078239L;
         }

         return TWO;
      }
   }

   private CustomBigInteger(int n, int[] ints) {
      super();
      this.mag = stripLeadingZeroInts(ints);
      if (n >= -1 && n <= 1) {
         if (this.mag.length == 0) {
            this.signum = 0;
         } else {
            if (n == 0) {
               throw new NumberFormatException("signum-magnitude mismatch");
            }

            this.signum = n;
         }

         if (this.mag.length >= 67108864) {
            this.checkRange();
         }
      } else {
         throw new NumberFormatException("Invalid signum value");
      }
   }

   public long longValue() {
      long l = 0L;
      boolean flag = false;
      l = 0L;

      for (int i = 1; i >= 0; i--) {
         l = (l << 32) + ((long)this.getInt(i) & 4294967295L);
      }

      return l;
   }

   private static int[] add(int[] ints, long l) {
      Object obj = null;
      boolean flag = false;
      int n = 0;
      Object obj2 = null;
      int n2 = 0;
      long l2 = 0L;
      long l3 = 0L;
      l3 = -9074104673260312032L ^ (long)"磋囕\ua9dc嶐ꦢ䨑욷㘂\ueecc馂毚뵷㱈\u0a7d烟ࣂ启㌓ṝ䊖ᖿ봒\ue801窡\ud937簚".hashCode();
      l2 = 0L;
      l3 = (l3 ^ 3065692521915290036L) - 7492782686745078303L;
      n2 = ints.length;
      l3 = l3 - -4206826385210656108L ^ -7966560688598228203L;
      n = (int)(l >>> 32);
      l3 = (l3 ^ -6724223457832495279L) + 2883919570290566335L;
      if (n != 0) {
         l3 = l3 - -7873755129940981036L ^ 3767701395145987728L;
         if (n2 == 1) {
            l3 = (l3 ^ 66036403042061857L) - 5233167383493047465L;
            obj = new int[2];
            l3 = (l3 ^ 5964816664252124043L) - -4608219966660283616L;
            l2 = l + ((long)ints[0] & 4294967295L);
            l3 = l3 - 8792046715099112644L ^ 9000181024975426192L;
            ((int[])obj)[1] = (int)l2;
            l3 = (l3 ^ 909515212203810433L) - -8601494129314999678L;
            ((int[])obj)[0] = (int)(l2 >>> 32);
            l3 = l3 + 4861105830453455985L ^ -8725802828410281481L;
            return (int[])obj;
         }

         l3 = l3 + 7007433074695571543L ^ -7880039315613400598L;
         obj = new int[n2];
         l3 = (l3 ^ -7959597870001969944L) - -4952400512983548313L;
         long l4 = ((long)ints[--n2] & 4294967295L) + (l & 4294967295L);
         l3 = l3 + -8180447667254354382L ^ 6219901811438664785L;
         ((int[])obj)[n2] = (int)l4;
         l3 = l3 - -466011017971554145L ^ -5350662777942821010L;
         l2 = ((long)ints[--n2] & 4294967295L) + ((long)n & 4294967295L) + (l4 >>> 32);
         l3 = l3 - -1201609188727328513L ^ -1532728966489589323L;
         ((int[])obj)[n2] = (int)l2;
         l3 = l3 + -4952219148027572704L ^ 4088806508760504260L;
      } else {
         l3 = l3 - -2423583695075575820L ^ -9179900789777185973L;
         obj = new int[n2];
         l3 = (l3 ^ -7753952247161623670L) - -2420414105004998751L;
         l2 = ((long)ints[--n2] & 4294967295L) + l;
         l3 = (l3 ^ 4586632962353502193L) - -3997045257755896499L;
         ((int[])obj)[n2] = (int)l2;
         l3 = (l3 ^ 3943178794938550545L) + 2008264977097916736L;
         l3 = l3 - 4277382570442619596L ^ -7969612730537874595L;
      }

      boolean flag2;
      if (l2 >>> 32 == 0L) {
         l3 = (l3 ^ 8593816482356500073L) - -394215433068852054L;
         flag2 = false;
      } else {
         l3 = l3 + 2709125763736160933L ^ 5414193510114858854L;
         flag2 = true;
      }

      flag = flag2;

      while (n2 > 0 && flag) {
         int[] ints2 = (int[])obj;
         n2--;
         flag = (ints2[n2] = ints[n2] + 1) == 0;
      }

      while (n2 > 0) {
         ((int[])obj)[n2] = ints[--n2];
      }

      if (flag) {
         obj2 = new int[((int[])obj).length + 1];
         System.arraycopy((int[])obj, 0, (int[])obj2, 1, ((int[])obj).length);
         ((int[])obj2)[0] = 1;
         return (int[])obj2;
      } else {
         return (int[])obj;
      }
   }

   public CustomBigInteger multiply2(CustomBigInteger customBigInteger) {
      return this.multiply(customBigInteger, false);
   }

   public CustomBigInteger add(long l) {
      int n = 0;
      Object obj = null;
      long l2 = 0L;
      l2 = 1044750515939055580L ^ (long)"ோ雜褮뮤䴡ំ馄ﲢ\uef1d⌴뗕\uedabӕ쀺҆\u0c5fᙳ\ud852璨鑸ᜌࠩ\uf1a9ꏚ鿎".hashCode();
      if (l != 0L) {
         l2 = l2 + 7341111897154217802L ^ 709192874596414455L;
         if (this.signum != 0) {
            l2 = (l2 ^ 1467399925108847223L) - -3202786401505552932L;
            if (Long.signum(l) != this.signum) {
               l2 = (l2 ^ -1339179987647684557L) - -3405974508742743884L;
               n = this.compareMagnitude2(l);
               l2 = (l2 ^ -1610482500481763967L) - 8448066968578086145L;
               if (n != 0) {
                  l2 = (l2 ^ -856810396997563414L) - -456838614574098207L;
                  int[] ints;
                  if (n <= 0) {
                     l2 = l2 + 1454910047621535263L ^ 4995259700036087300L;
                     ints = subtract2(Math.abs(l), this.mag);
                  } else {
                     l2 = l2 - 1322009190205014072L ^ 4529513473929066619L;
                     ints = subtract3(this.mag, Math.abs(l));
                  }

                  obj = ints;
                  obj = trustedStripLeadingZeroInts((int[])obj);
                  return new CustomBigInteger((int[])obj, n == this.signum ? 1 : -1);
               } else {
                  l2 = l2 + -3123248647850902977L ^ 6616824587560825223L;
                  return ZERO;
               }
            } else {
               l2 = (l2 ^ -544022400308589375L) + 6622053415434521064L;
               int[] mag3 = this.mag;
               long l4 = Math.abs(l);
               return new CustomBigInteger(add(mag3, l4), this.signum);
            }
         } else {
            l2 = l2 + -3993003557678321111L ^ 806111085005614024L;
            return valueOf(l);
         }
      } else {
         l2 = l2 + -60114764273393053L ^ 436130795715115647L;
         return this;
      }
   }

   public CustomBigInteger setBit(int n) {
      boolean flag = false;
      int n2 = 0;
      Object obj = null;
      if (n < 0) {
         throw new ArithmeticException("Negative bit address");
      } else {
         n2 = n >>> 5;
         obj = new int[Math.max(this.intLength(), n2 + 2)];

         for (int i = 0; i < ((int[])obj).length; i++) {
            ((int[])obj)[((int[])obj).length - i - 1] = this.getInt(i);
         }

         ((int[])obj)[((int[])obj).length - n2 - 1] = ((int[])obj)[((int[])obj).length - n2 - 1] | 1 << (n & 31);
         return valueOf2((int[])obj);
      }
   }

   private static CustomBigInteger largePrime(int n, int n2, Random rand) {
      Object obj = null;
      Object obj2 = null;
      int n3 = 0;
      Object obj3 = null;
      CustomBigInteger customBigInteger = new CustomBigInteger(n, rand);
      obj = customBigInteger.setBit(n - 1);
      ((CustomBigInteger)obj).mag[((int[])((CustomBigInteger)obj).mag).length - 1] = ((CustomBigInteger)obj).mag[((int[])((CustomBigInteger)obj).mag).length - 1]
         & -2;
      n3 = getPrimeSearchLen(n);
      obj3 = new PrimeBitSieve((CustomBigInteger)obj, n3);
      PrimeBitSieve primeBitSieve = (PrimeBitSieve)obj3;
      CustomBigInteger customBigInteger2 = (CustomBigInteger)obj;
      obj2 = primeBitSieve.retrieve(customBigInteger2, n2, rand);

      while ((CustomBigInteger)obj2 == null || ((CustomBigInteger)obj2).bitLength() != n) {
         customBigInteger = (CustomBigInteger)obj;
         obj = customBigInteger.add2(valueOf((long)(2 * n3)));
         if (((CustomBigInteger)obj).bitLength() != n) {
            customBigInteger = new CustomBigInteger(n, rand);
            obj = customBigInteger.setBit(n - 1);
         }

         ((CustomBigInteger)obj).mag[((int[])((CustomBigInteger)obj).mag).length - 1] = ((CustomBigInteger)obj).mag[((int[])((CustomBigInteger)obj).mag).length - 1]
            & -2;
         obj3 = new PrimeBitSieve((CustomBigInteger)obj, n3);
         PrimeBitSieve primeBitSieve2 = (PrimeBitSieve)obj3;
         customBigInteger2 = (CustomBigInteger)obj;
         obj2 = primeBitSieve2.retrieve(customBigInteger2, n2, rand);
      }

      return (CustomBigInteger)obj2;
   }

   public int compareTo2(CustomBigInteger customBigInteger) {
      int n = 0;
      if (this.signum != customBigInteger.signum) {
         return this.signum <= customBigInteger.signum ? -1 : 1;
      } else {
         n = this.signum;
         if (n == -1) {
            return customBigInteger.compareMagnitude(this);
         } else if (n == 1) {
            return this.compareMagnitude(customBigInteger);
         } else {
            return 0;
         }
      }
   }

   public CustomBigInteger shiftRight(int n) {
      if (this.signum != 0) {
         if (n <= 0) {
            if (n != 0) {
               return new CustomBigInteger(shiftLeft(this.mag, -n), this.signum);
            } else {
               return this;
            }
         } else {
            return this.shiftRightImpl(n);
         }
      } else {
         return ZERO;
      }
   }

   public CustomBigInteger(String str) {
      this(str, 10);
   }

   private CustomBigInteger(int[] ints) {
      super();
      if (ints.length == 0) {
         throw new NumberFormatException("Zero length BigInteger");
      } else {
         if (ints[0] < 0) {
            this.mag = makePositive(ints);
            this.signum = -1;
         } else {
            this.mag = trustedStripLeadingZeroInts(ints);
            this.signum = this.mag.length == 0 ? 0 : 1;
         }

         if (this.mag.length >= 67108864) {
            this.checkRange();
         }
      }
   }

   public boolean isProbablePrime(int n) {
      Object obj = null;
      if (n > 0) {
         obj = this.abs();
         CustomBigInteger customBigInteger = (CustomBigInteger)obj;
         if (!customBigInteger.equals(TWO)) {
            customBigInteger = (CustomBigInteger)obj;
            if (customBigInteger.testBit((byte)0)) {
               customBigInteger = (CustomBigInteger)obj;
               if (!customBigInteger.equals(ONE)) {
                  customBigInteger = (CustomBigInteger)obj;
                  return customBigInteger.primeToCertainty(n, (Random)null);
               }
            }

            return false;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private CustomBigInteger(byte[] bytes, int n) {
      super();
      this.signum = bytes.length == 0 ? 0 : n;
      this.mag = stripLeadingZeroBytes(bytes);
      if (this.mag.length >= 67108864) {
         this.checkRange();
      }
   }

   private CustomBigInteger multiply(CustomBigInteger customBigInteger, boolean flag) {
      int n = 0;
      Object obj = null;
      int n2 = 0;
      int n3 = 0;
      if (customBigInteger.signum != 0 && this.signum != 0) {
         n = this.mag.length;
         if (customBigInteger == this && n > 20) {
            return this.square();
         } else {
            n3 = customBigInteger.mag.length;
            if (n < 80 || n3 < 80) {
               n2 = this.signum != customBigInteger.signum ? -1 : 1;
               if (customBigInteger.mag.length == 1) {
                  int n4 = customBigInteger.mag[0];
                  return multiplyByInt(this.mag, n4, n2);
               } else if (this.mag.length == 1) {
                  int n5 = this.mag[0];
                  return multiplyByInt(customBigInteger.mag, n5, n2);
               } else {
                  int[] mag2 = customBigInteger.mag;
                  obj = multiplyToLen(this.mag, n, mag2, n3, (int[])null);
                  obj = trustedStripLeadingZeroInts((int[])obj);
                  return new CustomBigInteger((int[])obj, n2);
               }
            } else if (n < 240 && n3 < 240) {
               return multiplyKaratsuba(this, customBigInteger);
            } else {
               if (!flag) {
                  int length = this.mag.length;
                  int n6 = bitLength(this.mag, length);
                  int length2 = customBigInteger.mag.length;
                  if ((long)(n6 + bitLength(customBigInteger.mag, length2)) > 2147483648L) {
                     reportOverflow();
                  }
               }

               return multiplyToomCook3(this, customBigInteger);
            }
         }
      } else {
         return ZERO;
      }
   }

   public static int bitLengthForInt(int n) {
      return 32 - Integer.numberOfLeadingZeros(n);
   }

   public CustomBigInteger or(CustomBigInteger customBigInteger) {
      boolean flag = false;
      Object obj = null;
      obj = new int[Math.max(this.intLength(), customBigInteger.intLength())];

      for (int i = 0; i < ((int[])obj).length; i++) {
         ((int[])obj)[i] = this.getInt(((int[])obj).length - i - 1) | customBigInteger.getInt(((int[])obj).length - i - 1);
      }

      return valueOf2((int[])obj);
   }

   private static int[] montgomerySquare(int[] ints, int[] ints2, int n, long l, int[] ints3) {
      int[] ints4 = ints;
      int[] ints5 = ints;
      implMontgomeryMultiplyChecks(ints4, ints5, ints2, n, ints3);
      if (n <= 512) {
         ints5 = ints2;
         return implMontgomerySquare(ints, ints5, n, l, materialize(ints3, n));
      } else {
         int[] ints13 = ints;
         ints3 = squareToLen(ints13, n, ints3);
         ints4 = ints3;
         ints13 = ints2;
         return montReduce(ints4, ints13, n, (int)l);
      }
   }

   private static CustomBigInteger smallPrime(int n, int n2, Random rand) {
      long l = 0L;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      Object obj = null;
      Object obj2 = null;
      int n6 = 0;
      long l2 = 0L;
      l2 = -7611816622368118010L ^ (long)"ٹ㠫냨\uee51\uea7dᖱ㮽反撤ຕ\uda09貅\u206a㜚肮\ue9c0癿羽뇘\ueb1c졓ီ\uef1d\ue098읪ⱹ탅↤".hashCode();
      n4 = n + 31 >>> 5;
      l2 = l2 + 7094430445422288190L ^ 202080240281952000L;
      obj2 = new int[n4];
      l2 = l2 + 6986132029394230817L ^ -7901054428618795709L;
      n5 = 1 << (n + 31 & 31);
      l2 = (l2 ^ 7058620015207468028L) + -4443867566507944909L;
      n3 = (n5 << 1) - 1;
      l2 = (l2 ^ 4873856975541151617L) - -1284944864607652969L;

      while (true) {
         n6 = 0;
         l2 = l2 - 4057017627186396784L ^ -1068019784203627074L;

         while (n6 < n4) {
            l2 = (l2 ^ -8546186112558475348L) - 8635338856388717797L;
            ((int[])obj2)[n6] = rand.nextInt();
            l2 = l2 - 8603411132372672366L ^ -7991267669341049721L;
            n6++;
            l2 = l2 - -7815340647460756890L ^ 2419842390822409956L;
            l2 = l2 + 6691426602063141688L ^ -4615879263527916334L;
         }

         l2 = (l2 ^ 5680242365988811503L) + -4478936795809897588L;
         ((int[])obj2)[0] = ((int[])obj2)[0] & n3 | n5;
         l2 = l2 - 2075210875389780946L ^ 7203120715639057246L;
         if (n <= 2) {
            l2 = (l2 ^ -6302077534550317884L) + 6501765585236222158L;
         } else {
            l2 = (l2 ^ 8793938890819300734L) + 3506671099902846630L;
            ((int[])obj2)[n4 - 1] = ((int[])obj2)[n4 - 1] | 1;
            l2 = (l2 ^ 4154144655306123156L) - 1318009748355407542L;
         }

         obj = new CustomBigInteger((int[])obj2, 1);
         l2 = (l2 ^ -4467852872753662209L) - 6914594692391685666L;
         if (n <= 6) {
            l2 = (l2 ^ 4826403150926858456L) - 613257860930103442L;
         } else {
            l2 = l2 + 2651124990663043471L ^ -1539515173904160004L;
            l = ((CustomBigInteger)obj).remainder(SMALL_PRIME_PRODUCT).longValue();
            l2 = (l2 ^ -6129952114735484187L) - -3911329928906187147L;
            if (l % 3L == 0L) {
               l2 = l2 - -2876230499824025006L ^ -8135242959778714223L;
               continue;
            }

            l2 = l2 - -8384449840193716757L ^ 8496489126163453015L;
            if (l % 5L == 0L) {
               l2 = l2 + 2605213600373803088L ^ 1251477529406819505L;
               continue;
            }

            l2 = l2 + -4787754510348475566L ^ -2169745598329102143L;
            if (l % 7L == 0L) {
               l2 = (l2 ^ -7460942708975204910L) + 1035307683251073952L;
               continue;
            }

            l2 = (l2 ^ -7430148224159815000L) - 4304605484755730089L;
            if (l % 11L == 0L) {
               l2 = (l2 ^ -6012509764112080839L) + -1866654705799937866L;
               continue;
            }

            l2 = (l2 ^ -2158380141164380363L) + -9136156178682928067L;
            if (l % 13L == 0L) {
               l2 = l2 - 3789825237283225981L ^ 8942110904682078998L;
               continue;
            }

            l2 = l2 + -7221198639483267837L ^ 7494196695740130961L;
            if (l % 17L == 0L) {
               l2 = (l2 ^ -6492943544435262430L) + 4627905332354103069L;
               continue;
            }

            l2 = l2 + -1337637398199053074L ^ 7100879110874095601L;
            if (l % 19L == 0L) {
               l2 = l2 + -3619489808109882146L ^ -8719275896239305502L;
               continue;
            }

            l2 = (l2 ^ 1903077619765577895L) - 7101874749385616550L;
            if (l % 23L == 0L) {
               l2 = l2 + 7259333960158620979L ^ 7835160413112955390L;
               continue;
            }

            l2 = l2 + -4393849422865492115L ^ -5325496304315248575L;
            if (l % 29L == 0L) {
               l2 = (l2 ^ 5768035903747261287L) - 1702183762454104846L;
               continue;
            }

            l2 = (l2 ^ -2715136366306763148L) - -5309352127948291548L;
            if (l % 31L == 0L) {
               l2 = (l2 ^ 5127179317748664744L) + 6720692163322055297L;
               continue;
            }

            l2 = l2 - 1926566959703620942L ^ 1010830027552985258L;
            if (l % 37L == 0L) {
               l2 = (l2 ^ -7895290805843996602L) - 4090636610842144821L;
               continue;
            }

            l2 = l2 + 5621386078021544291L ^ -8752393736302375270L;
            if (l % 41L == 0L) {
               l2 = l2 - 522118660285884629L ^ -4938984752119914946L;
               l2 = (l2 ^ 1667169122434754015L) - -2058347337162826044L;
               continue;
            }

            l2 = l2 - 8217914629067555718L ^ 4680582755020735270L;
         }

         if (n < 4) {
            l2 = (l2 ^ -6481305244363599202L) + -3793290386722778825L;
            return (CustomBigInteger)obj;
         }

         l2 = l2 + 4059861760267510291L ^ 224742660267746654L;
         if (((CustomBigInteger)obj).primeToCertainty(n2, rand)) {
            l2 = l2 - 2508731453031037321L ^ 4445762874520615420L;
            return (CustomBigInteger)obj;
         }

         l2 = l2 - 1418170445783527511L ^ -8744500115514119516L;
         l2 = (l2 ^ -5812635714140222936L) + -7181736307125127378L;
      }
   }

   private static int[] subtract3(int[] ints, long l) {
      boolean flag = false;
      int n = 0;
      int n2 = 0;
      Object obj = null;
      long l2 = 0L;
      long l3 = 0L;
      l3 = 2699055954639409038L ^ (long)"ퟔ▰㛔簡Ꙕ\udf83ᧆ\ue8c0韁戝ᡵ捘列\udcbdᐍ턼\ue378烸\u1879ʌ朡僵굵⺇峊ｇ\u2fd9".hashCode();
      n2 = (int)(l >>> 32);
      l3 = l3 - -7186623333891081179L ^ 1777032554526458128L;
      n = ints.length;
      l3 = (l3 ^ 2318439007425134185L) + -5501855752821425100L;
      obj = new int[n];
      l3 = (l3 ^ 8793961732516751649L) + 7335393587497745769L;
      l2 = 0L;
      l3 = (l3 ^ 8138020821224688766L) + 7107064130906280688L;
      if (n2 != 0) {
         l3 = l3 + -9006510902825675217L ^ 5715810945730368434L;
         int[] ints2 = ints;
         n--;
         long l4 = ((long)ints2[n] & 4294967295L) - (l & 4294967295L);
         l3 = (l3 ^ 4097980122566352016L) - -664270032048639695L;
         ((int[])obj)[n] = (int)l4;
         l3 = l3 + 93227655871303132L ^ 2488576094236222102L;
         ints2 = ints;
         n--;
         l2 = ((long)ints2[n] & 4294967295L) - ((long)n2 & 4294967295L) + (l4 >> 32);
         l3 = l3 - 6950166999690841166L ^ 5656487341773341864L;
         ((int[])obj)[n] = (int)l2;
         l3 = (l3 ^ 4055456744833341134L) + 8906984329344930471L;
      } else {
         l3 = l3 - 5752834001341766480L ^ 5569757605701326435L;
         n--;
         l2 = ((long)ints[n] & 4294967295L) - l;
         l3 = l3 - -5388777569251868728L ^ 3605811911537406628L;
         ((int[])obj)[n] = (int)l2;
         l3 = (l3 ^ 6219115938531895968L) - 3000277676175597021L;
         l3 = (l3 ^ -3663216731001231468L) - 5992856029240629443L;
      }

      boolean flag2;
      if (l2 >> 32 == 0L) {
         l3 = l3 + 3710602754339498566L ^ 3909494132263192122L;
         flag2 = false;
      } else {
         l3 = l3 - -529301075284060217L ^ -2709318428116548567L;
         flag2 = true;
      }

      flag = flag2;

      while (n > 0 && flag) {
         int[] ints4 = (int[])obj;
         n--;
         flag = (ints4[n] = ints[n] - 1) == -1;
      }

      while (n > 0) {
         ((int[])obj)[n] = ints[--n];
      }

      return (int[])obj;
   }

   public CustomBigInteger clearBit(int n) {
      int n2 = 0;
      Object obj = null;
      boolean flag = false;
      if (n < 0) {
         throw new ArithmeticException("Negative bit address");
      } else {
         n2 = n >>> 5;
         obj = new int[Math.max(this.intLength(), (n + 1 >>> 5) + 1)];

         for (int i = 0; i < ((int[])obj).length; i++) {
            ((int[])obj)[((int[])obj).length - i - 1] = this.getInt(i);
         }

         ((int[])obj)[((int[])obj).length - n2 - 1] = ((int[])obj)[((int[])obj).length - n2 - 1] & ~(1 << (n & 31));
         return valueOf2((int[])obj);
      }
   }

   public int bitCount(byte b, int n, int n2) {
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      n3 = this.bitCountPlusOne - 1;
      if (n3 == -1) {
         n3 = 0;

         for (int i = 0; i < ((int[])this.mag).length; i++) {
            n3 += Integer.bitCount(this.mag[i]);
         }

         if (this.signum < 0) {
            n4 = (byte)0;

            for (n5 = this.mag.length - 1; this.mag[n5] == 0; n5--) {
               n4 += 32;
            }

            n4 += Integer.numberOfTrailingZeros(this.mag[n5]);
            n3 += n4 - 1;
         }

         this.bitCountPlusOne = n3 + 1;
      }

      return n3;
   }

   public static CustomBigInteger valueOf(long l) {
      long l2 = 0L;
      l2 = -784947456396248268L ^ (long)"\uda93\ue305ꓝⷛ嘓㴿⨀饞◺≛\u2e62쒵樴㠳닍鮠盀웱抺\uf3bf盍盺ڹ摼肋뗙\udf9e".hashCode();
      if (l != 0L) {
         l2 = l2 - 3226789764917148631L ^ -6467886432035767892L;
         if (l <= 0L) {
            l2 = l2 + -8689267335162185204L ^ -3835353429645273989L;
         } else {
            l2 = l2 + -3195072117551405464L ^ 5921440560352315420L;
            if (l <= 16L) {
               l2 = (l2 ^ -6992726087488421026L) - -4261906923913474602L;
               return posConst[(int)l];
            }

            l2 = l2 - 9061727676999971722L ^ -5950433279078230903L;
         }

         if (l >= 0L) {
            l2 = (l2 ^ 1318543720009542591L) + 4895087993950147477L;
         } else {
            l2 = l2 + -8425673370336147987L ^ -2560836697156917291L;
            if (l >= -16L) {
               l2 = (l2 ^ 5713651376302440744L) + 7298124087652199891L;
               return negConst[(int)(-l)];
            }

            l2 = (l2 ^ 7693199902961989661L) + -813292971303596585L;
         }

         return new CustomBigInteger(l);
      } else {
         l2 = (l2 ^ 5680786620387413879L) - 4082477936884164847L;
         return ZERO;
      }
   }

   public static int addOne(int[] ints, int n, int n2, int n3) {
      long l = 0L;
      long l2 = 0L;
      l2 = 4290260544061868583L ^ (long)"暾\u0b0eຫ鲁둺哕⇉ƫ댾◩⫑快㝹訰鯧棣럍슒\ue9ea\ud7ae㳇≉醏범\u0bc5ﰢ쥺ꃮ䋥".hashCode();
      n = ints.length - 1 - n2 - n;
      l2 = (l2 ^ 7969129784292008368L) + -6714050277788685992L;
      l = ((long)ints[n] & 4294967295L) + ((long)n3 & 4294967295L);
      l2 = l2 - -2889451702656333986L ^ -1923025248186091429L;
      ints[n] = (int)l;
      l2 = l2 - 7557766624159799927L ^ -9124491874627107964L;
      if (l >>> 32 == 0L) {
         l2 = (l2 ^ 4154577998495430069L) - -170555073664010213L;
         return 0;
      } else {
         l2 = l2 + -4005250857827987413L ^ 7359637401818927257L;

         while (true) {
            n2--;
            l2 = l2 + 4313988767459280912L ^ 6033200981550361401L;
            if (n2 < 0) {
               l2 = (l2 ^ 3656013973766793782L) + -1970287678194353393L;
               return 1;
            }

            l2 = l2 + 1656933370475033669L ^ -4510525562505694137L;
            n--;
            l2 = l2 - 1906780744624842016L ^ 8176541200526688431L;
            if (n < 0) {
               l2 = l2 + -2504921796876429541L ^ -584816434952217519L;
               return 1;
            }

            l2 = l2 + 5537718555721823452L ^ -6851617658667682638L;
            ints[n]++;
            l2 = l2 + 8816651920650374679L ^ -2823789262027727391L;
            if (ints[n] != 0) {
               l2 = l2 - 2790157570369471635L ^ -3497524459110090769L;
               return 0;
            }

            l2 = (l2 ^ -7362952508511893771L) - -9070119775491597529L;
         }
      }
   }

   private static int jacobiSymbol(int n, CustomBigInteger customBigInteger) {
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      if (n == 0) {
         return 0;
      } else {
         n3 = 1;
         n2 = customBigInteger.mag[((int[])customBigInteger.mag).length - 1];
         if (n < 0) {
            n = -n;
            n4 = n2 & 7;
            if (n4 == 3 || n4 == 7) {
               n3 = -n3;
            }
         }

         while ((n & 3) == 0) {
            n >>= 2;
         }

         if ((n & 1) == 0) {
            n >>= 1;
            if (((n2 ^ n2 >> 1) & 2) != 0) {
               n3 = -n3;
            }
         }

         if (n == 1) {
            return n3;
         } else {
            if ((n & n2 & 2) != 0) {
               n3 = -n3;
            }

            n2 = customBigInteger.mod(valueOf((long)n)).intValue();

            while (n2 != 0) {
               while ((n2 & 3) == 0) {
                  n2 >>= 2;
               }

               if ((n2 & 1) == 0) {
                  n2 >>= 1;
                  if (((n ^ n >> 1) & 2) != 0) {
                     n3 = -n3;
                  }
               }

               if (n2 == 1) {
                  return n3;
               }

               assert n2 < n;

               int n5 = n;
               n = n2;
               if ((n5 & n2 & 2) != 0) {
                  n3 = -n3;
               }

               n2 = n5 % n2;
            }

            return 0;
         }
      }
   }

   public boolean testBit(int n) {
      if (n >= 0) {
         return (this.getInt(n >>> 5) & 1 << (n & 31)) != 0;
      } else {
         throw new ArithmeticException("Negative bit address");
      }
   }

   private CustomBigInteger squareKaratsuba() {
      Object obj = null;
      int n = 0;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      n = (this.mag.length + 1) / 2;
      obj4 = this.getLower(n);
      obj2 = this.getUpper(n);
      obj3 = ((CustomBigInteger)obj2).square();
      obj = ((CustomBigInteger)obj4).square();
      CustomBigInteger customBigInteger = (CustomBigInteger)obj3;
      customBigInteger = customBigInteger.shiftLeft(n * 32);
      CustomBigInteger customBigInteger2 = (CustomBigInteger)obj4;
      customBigInteger2 = customBigInteger2.add2((CustomBigInteger)obj2).square();
      customBigInteger = customBigInteger.add2(customBigInteger2.subtract(((CustomBigInteger)obj3).add2((CustomBigInteger)obj)));
      customBigInteger = customBigInteger.shiftLeft(n * 32);
      return customBigInteger.add2((CustomBigInteger)obj);
   }

   public CustomBigInteger max(CustomBigInteger customBigInteger) {
      return this.compareTo2(customBigInteger) > 0 ? this : customBigInteger;
   }

   private boolean passesLucasLehmer() {
      Object obj = null;
      int n = 0;
      Object obj2 = null;
      CustomBigInteger customBigInteger = this;
      obj2 = customBigInteger.add2(ONE);
      n = 5;

      while (jacobiSymbol(n, this) != -1) {
         n = n >= 0 ? -(n + 2) : Math.abs(n) + 2;
      }

      obj = lucasLehmerSequence(n, (CustomBigInteger)obj2, this);
      customBigInteger = (CustomBigInteger)obj;
      customBigInteger = customBigInteger.mod(this);
      return customBigInteger.equals(ZERO);
   }

   private static void implSquareToLenChecks(int[] ints, int n, int[] ints2, int n2) throws RuntimeException {
      if (n >= 1) {
         if (n <= ints.length) {
            if (n * 2 <= ints2.length) {
               if (n2 >= 1) {
                  if (n2 > ints2.length) {
                     throw new IllegalArgumentException("input length out of bound: " + n + " > " + ints2.length);
                  }
               } else {
                  throw new IllegalArgumentException("invalid input length: " + n2);
               }
            } else {
               throw new IllegalArgumentException("input length out of bound: " + n * 2 + " > " + ints2.length);
            }
         } else {
            throw new IllegalArgumentException("input length out of bound: " + n + " > " + ints.length);
         }
      } else {
         throw new IllegalArgumentException("invalid input length: " + n);
      }
   }

   public CustomBigInteger divide(CustomBigInteger customBigInteger) {
      if (customBigInteger.mag.length >= 80 && this.mag.length - customBigInteger.mag.length >= 40) {
         return this.divideBurnikelZiegler(customBigInteger);
      } else {
         return this.divideKnuth(customBigInteger);
      }
   }

   private static int getPrimeSearchLen(int n) {
      long l = 0L;
      l = 1668624293547879246L ^ (long)"萢ᒹ尴Ԙⶁ꣨猯纹轤⏃늹窦䟮뽮\u20c2펐ꌛ\ue3b8籿帠挭蓞ഔ".hashCode();
      if (n <= 500000001) {
         l = (l ^ 5132638716948668100L) - 4944514012641618026L;
         return n / 20 * 64;
      } else {
         l = l - -7288493269870673825L ^ -698804538512543459L;
         throw new ArithmeticException("Prime search implementation restriction on bitLength");
      }
   }

   private CustomBigInteger oddModPow(CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2) {
      long l = 0L;
      int n = 0;
      int n2 = 0;
      Object obj = null;
      long l2 = 0L;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      boolean flag = false;
      int n3 = 0;
      Object obj5 = null;
      int n4 = 0;
      int n5 = 0;
      Object obj6 = null;
      Object obj7 = null;
      int n6 = 0;
      int n7 = 0;
      Object obj8 = null;
      boolean flag2 = false;
      Object obj9 = null;
      Object obj10 = null;
      int n8 = 0;
      Object obj11 = null;
      Object obj12 = null;
      int n9 = 0;
      int n10 = 0;
      Object obj13 = null;
      Object obj14 = null;
      if (customBigInteger.equals(ONE)) {
         return this;
      } else if (this.signum != 0) {
         obj10 = (int[])this.mag.clone();
         obj13 = customBigInteger.mag;
         obj7 = customBigInteger2.mag;
         n5 = ((int[])obj7).length;
         if ((n5 & 1) != 0) {
            obj12 = new int[n5 + 1];
            System.arraycopy((int[])obj7, 0, (int[])obj12, 1, n5);
            obj7 = (int[])obj12;
            n5++;
         }

         n = 0;
         int length = ((int[])obj13).length;
         n8 = bitLength((int[])obj13, length);
         if (n8 != 17 || ((int[])obj13)[0] != 65537) {
            while (n8 > bnExpModThreshTable[n]) {
               n++;
            }
         }

         n6 = 1 << n;
         obj6 = new int[n6][];

         for (int i = 0; i < n6; i++) {
            ((int[][])obj6)[i] = new int[n5];
         }

         l2 = ((long)((int[])obj7)[n5 - 1] & 4294967295L) + (((long)((int[])obj7)[n5 - 2] & 4294967295L) << 32);
         l = -ShadedMutableBigInteger.inverseMod64(l2);
         obj14 = leftShift((int[])obj10, ((int[])obj10).length, n5 << 5);
         obj9 = new ShadedMutableBigInteger();
         obj3 = new ShadedMutableBigInteger((int[])obj14);
         obj8 = new ShadedMutableBigInteger((int[])obj7);
         ((ShadedMutableBigInteger)obj8).normalize();
         obj5 = ((ShadedMutableBigInteger)obj3).divide2((ShadedMutableBigInteger)obj8, (ShadedMutableBigInteger)obj9);
         ((int[][])obj6)[0] = ((ShadedMutableBigInteger)obj5).toIntArray();
         if (((int[][])obj6)[0].length < n5) {
            n3 = n5 - ((int[][])obj6)[0].length;
            obj = new int[n5];
            System.arraycopy(((int[][])obj6)[0], 0, (int[])obj, n3, ((int[][])obj6)[0].length);
            ((int[][])obj6)[0] = (int[])obj;
         }

         obj2 = montgomerySquare(((int[][])obj6)[0], (int[])obj7, n5, l, (int[])null);
         obj = Arrays.copyOf((int[])obj2, n5);

         for (int j = 1; j < n6; j++) {
            ((int[][])obj6)[j] = montgomeryMultiply((int[])obj, ((int[][])obj6)[j - 1], (int[])obj7, n5, l, (int[])null);
         }

         n10 = 1 << (n8 - 1 & 31);
         n4 = 0;
         n9 = ((int[])obj13).length;
         n2 = 0;

         for (int k = 0; k <= n; k++) {
            n4 = n4 << 1 | ((((int[])obj13)[n2] & n10) != 0 ? 1 : 0);
            n10 >>>= 1;
            if (n10 == 0) {
               n2++;
               n10 = Integer.MIN_VALUE;
               n9--;
            }
         }

         n8--;
         flag2 = true;

         for (n7 = n8 - n; (n4 & 1) == 0; n7++) {
            n4 >>>= 1;
         }

         obj4 = ((int[][])obj6)[n4 >>> 1];
         n4 = 0;
         if (n7 == n8) {
            flag2 = false;
         }

         while (true) {
            n8--;
            n4 <<= 1;
            if (n9 != 0) {
               n4 |= (((int[])obj13)[n2] & n10) != 0 ? 1 : 0;
               n10 >>>= 1;
               if (n10 == 0) {
                  n2++;
                  n10 = Integer.MIN_VALUE;
                  n9--;
               }
            }

            if ((n4 & n6) != 0) {
               for (n7 = n8 - n; (n4 & 1) == 0; n7++) {
                  n4 >>>= 1;
               }

               obj4 = ((int[][])obj6)[n4 >>> 1];
               n4 = 0;
            }

            if (n8 == n7) {
               if (flag2) {
                  obj2 = (int[])((int[])obj4).clone();
                  flag2 = false;
               } else {
                  obj = (int[])obj2;
                  obj14 = montgomeryMultiply((int[])obj, (int[])obj4, (int[])obj7, n5, l, (int[])obj14);
                  obj = (int[])obj14;
                  obj14 = (int[])obj2;
                  obj2 = (int[])obj;
               }
            }

            if (n8 == 0) {
               obj11 = new int[2 * n5];
               System.arraycopy((int[])obj2, 0, (int[])obj11, n5, n5);
               obj2 = montReduce((int[])obj11, (int[])obj7, n5, (int)l);
               obj11 = Arrays.copyOf((int[])obj2, n5);
               return new CustomBigInteger(1, (int[])obj11);
            }

            if (!flag2) {
               obj = (int[])obj2;
               obj14 = montgomerySquare((int[])obj, (int[])obj7, n5, l, (int[])obj14);
               obj = (int[])obj14;
               obj14 = (int[])obj2;
               obj2 = (int[])obj;
            }
         }
      } else {
         return ZERO;
      }
   }

   public final int compareMagnitude(CustomBigInteger customBigInteger) {
      int n = 0;
      Object obj = null;
      int n2 = 0;
      int n3 = 0;
      Object obj2 = null;
      int n4 = 0;
      int n5 = 0;
      long l = 0L;
      l = -8515158813089241353L ^ (long)"놪╄\udf86틋ᔧ嬟葲揿\udaa9\ue820ऒƛ蒼\ue986䘨៳왵㙨钑\uf442ꁠ洠裼䏗䤤\uf5b4\ue52b".hashCode();
      obj2 = this.mag;
      l = (l ^ 7796032918225886823L) + -6113498324249372314L;
      n = ((int[])obj2).length;
      l = (l ^ -4015725102582543530L) + -4608581280112178088L;
      obj = customBigInteger.mag;
      l = l + 4738913637915533366L ^ -2033108031058969902L;
      n2 = ((int[])obj).length;
      l = l + -4825239708642801361L ^ -1827933493997972082L;
      if (n < n2) {
         l = l - 4569106440957835294L ^ -4767991408130266398L;
         return -1;
      } else {
         l = l + -4260058438710652796L ^ -4676362618559402227L;
         if (n > n2) {
            l = (l ^ 1997931150817019440L) - -264257570429590422L;
            return 1;
         } else {
            l = (l ^ -4882795256145470836L) - -4039119941618119798L;
            n4 = 0;
            l = l - 2293160942688114176L ^ -9048046052922001140L;

            while (n4 < n) {
               l = l - 4814136613993082899L ^ -4256209925956333132L;
               n5 = ((int[])obj2)[n4];
               l = l - 5167756025912259872L ^ -3963125713409727709L;
               n3 = ((int[])obj)[n4];
               l = l - 1801128044125460493L ^ 1618754882579681397L;
               if (n5 != n3) {
                  l = (l ^ 4165123350713503993L) - -1333833436653786507L;
                  byte b;
                  if (((long)n5 & 4294967295L) >= ((long)n3 & 4294967295L)) {
                     l = l - 5851667407399132397L ^ -7501736184794120385L;
                     b = 1;
                  } else {
                     l = (l ^ -5604382666693403622L) + -5450035608485971512L;
                     b = -1;
                  }

                  return b;
               }

               l = l + -8749111434352644969L ^ 8820998915389280509L;
               n4++;
               l = l - 7174061003069011370L ^ -8866297186603435526L;
            }

            l = (l ^ 2006257335165047323L) + -2561828336485707701L;
            return 0;
         }
      }
   }

   public CustomBigInteger mod(CustomBigInteger customBigInteger) {
      Object obj = null;
      if (customBigInteger.signum > 0) {
         CustomBigInteger customBigInteger2 = this;
         obj = customBigInteger2.remainder(customBigInteger);
         if (((CustomBigInteger)obj).signum < 0) {
            customBigInteger2 = (CustomBigInteger)obj;
            customBigInteger2 = customBigInteger2.add2(customBigInteger);
         } else {
            customBigInteger2 = (CustomBigInteger)obj;
         }

         return customBigInteger2;
      } else {
         throw new ArithmeticException("BigInteger: modulus not positive");
      }
   }

   private static int[] montgomeryMultiply(int[] ints, int[] ints2, int[] ints3, int n, long l, int[] ints4) {
      int[] ints5 = ints;
      int[] ints6 = ints2;
      int[] ints7 = ints3;
      implMontgomeryMultiplyChecks(ints5, ints6, ints7, n, ints4);
      if (n <= 512) {
         ints5 = ints;
         ints6 = ints3;
         return implMontgomeryMultiply(ints5, ints2, ints6, n, l, materialize(ints4, n));
      } else {
         ints5 = ints;
         ints7 = ints2;
         ints4 = multiplyToLen(ints5, n, ints7, n, ints4);
         ints5 = ints4;
         return montReduce(ints5, ints3, n, (int)l);
      }
   }

   public String toString(int n) {
      Object obj = null;
      if (this.signum == 0) {
         return "0";
      } else {
         if (n < 2 || n > 36) {
            n = 10;
         }

         if (this.mag.length > 20) {
            obj = new StringBuilder();
            if (this.signum >= 0) {
               toString(this, (StringBuilder)obj, n, (byte)0);
            } else {
               toString(this.negate(), (StringBuilder)obj, n, (byte)0);
               ((StringBuilder)obj).insert(0, '-');
            }

            return ((StringBuilder)obj).toString();
         } else {
            return this.smallToString(n);
         }
      }
   }

   private static void destructiveMulAdd(int[] ints, int n, int n2) {
      boolean flag = false;
      boolean flag2 = false;
      long l = 0L;
      int n3 = 0;
      long l2 = 0L;
      long l3 = 0L;
      long l4 = 0L;
      long l5 = 0L;
      l5 = (long)n & 4294967295L;
      l4 = (long)n2 & 4294967295L;
      n3 = ints.length;
      l2 = 0L;
      l3 = 0L;

      for (int i = n3 - 1; i >= 0; i--) {
         l2 = l5 * ((long)ints[i] & 4294967295L) + l3;
         ints[i] = (int)l2;
         l3 = l2 >>> 32;
      }

      l = ((long)ints[n3 - 1] & 4294967295L) + l4;
      ints[n3 - 1] = (int)l;
      l3 = l >>> 32;

      for (int j = n3 - 2; j >= 0; j--) {
         l = ((long)ints[j] & 4294967295L) + l3;
         ints[j] = (int)l;
         l3 = l >>> 32;
      }
   }

   public CustomBigInteger shiftLeft(int n) {
      if (this.signum != 0) {
         if (n <= 0) {
            if (n != 0) {
               return this.shiftRightImpl(-n);
            } else {
               return this;
            }
         } else {
            return new CustomBigInteger(shiftLeft(this.mag, n), this.signum);
         }
      } else {
         return ZERO;
      }
   }

   public int intValue() {
      boolean flag = false;
      flag = false;
      return this.getInt((byte)0);
   }

   private static CustomBigInteger multiplyByInt(int[] ints, int n, int n2) {
      int n3 = 0;
      long l = 0L;
      int n4 = 0;
      int n5 = 0;
      long l2 = 0L;
      Object obj = null;
      long l3 = 0L;
      long l4 = 0L;
      l4 = 6704258610777304508L ^ (long)"\ud7ac禚歮쟌\ueecb塯Ὥꓫ\uf225ﷇ↽铽\uf0c4\ue489맃範恰晲㣃\uedda낐뽷⒴꣙뺦㢐\ue676".hashCode();
      if (Integer.bitCount(n) == 1) {
         l4 = (l4 ^ 8603093930366330109L) - 7435126483041251787L;
         int n6 = Integer.numberOfTrailingZeros(n);
         return new CustomBigInteger(shiftLeft(ints, n6), n2);
      } else {
         l4 = l4 - -5924726146020576580L ^ 4249996684370731310L;
         n3 = ints.length;
         l4 = l4 - 4977142170630066646L ^ 2173380261576686560L;
         obj = new int[n3 + 1];
         l4 = l4 - -5454094990530071289L ^ 4210369154795971092L;
         l = 0L;
         l4 = (l4 ^ 287772725372233318L) + -7807545697569236246L;
         l3 = (long)n & 4294967295L;
         l4 = l4 - 3991610924855520167L ^ 6857735488773565264L;
         n5 = ((int[])obj).length - 1;
         l4 = (l4 ^ 7516034498099341214L) + 7172792530343285263L;
         n4 = n3 - 1;
         l4 = l4 + 5016852841399160038L ^ 305415482747945762L;

         while (n4 >= 0) {
            l4 = l4 + 2247943704913648082L ^ 6707945315473376512L;
            l2 = ((long)ints[n4] & 4294967295L) * l3 + l;
            l4 = (l4 ^ -1150322379750450047L) - 4120505218015744837L;
            ((int[])obj)[n5--] = (int)l2;
            l4 = l4 + 3681948662823870833L ^ -3217398085432780689L;
            l = l2 >>> 32;
            l4 = l4 - -8311782259842046212L ^ 8468878516657918672L;
            n4--;
            l4 = l4 - 5843779113418796757L ^ -877433634268073715L;
            l4 = l4 - -3000751559066636577L ^ 5793269960705033315L;
         }

         l4 = (l4 ^ 7077231128183597215L) + 6888048854091203137L;
         if (l != 0L) {
            l4 = (l4 ^ 5711501747767492145L) - 1174608952979598156L;
            ((int[])obj)[n5] = (int)l;
            l4 = (l4 ^ -6983184677841510870L) + 1671543425451537427L;
         } else {
            l4 = (l4 ^ 3103527808716454603L) + -8782539962727425274L;
            obj = Arrays.copyOfRange((int[])obj, 1, ((int[])obj).length);
            l4 = l4 - 3061072162378571150L ^ -8670314854290509773L;
            l4 = (l4 ^ 6890463818295913700L) + 2879351553107564686L;
         }

         return new CustomBigInteger((int[])obj, n2);
      }
   }

   private CustomBigInteger mod2(int n) {
      Object obj = null;
      int n2 = 0;
      int n3 = 0;
      if (this.bitLength() > n) {
         n2 = n + 31 >>> 5;
         obj = new int[n2];
         System.arraycopy(this.mag, this.mag.length - n2, (int[])obj, 0, n2);
         n3 = (n2 << 5) - n;
         ((int[])obj)[0] = (int)((long)((int[])obj)[0] & (1L << 32 - n3) - 1L);
         return ((int[])obj)[0] != 0 ? new CustomBigInteger((int[])obj, 1) : new CustomBigInteger(1, (int[])obj);
      } else {
         return this;
      }
   }

   public CustomBigInteger modInverse(CustomBigInteger customBigInteger) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      if (customBigInteger.signum == 1) {
         CustomBigInteger customBigInteger2 = customBigInteger;
         if (customBigInteger2.equals(ONE)) {
            return ZERO;
         } else {
            exitBlock: {
               obj = this;
               if (this.signum >= 0) {
                  customBigInteger2 = this;
                  if (customBigInteger2.compareMagnitude(customBigInteger) < 0) {
                     break exitBlock;
                  }
               }

               customBigInteger2 = this;
               obj = customBigInteger2.mod(customBigInteger);
            }

            customBigInteger2 = (CustomBigInteger)obj;
            if (!customBigInteger2.equals(ONE)) {
               obj4 = new ShadedMutableBigInteger((CustomBigInteger)obj);
               obj2 = new ShadedMutableBigInteger(customBigInteger);
               obj3 = ((ShadedMutableBigInteger)obj4).mutableModInverse((ShadedMutableBigInteger)obj2);
               return ((ShadedMutableBigInteger)obj3).toBigInteger((byte)1);
            } else {
               return ONE;
            }
         }
      } else {
         throw new ArithmeticException("BigInteger: modulus not positive");
      }
   }

   private CustomBigInteger square() {
      return this.square(false);
   }

   private static int subN(int[] ints, int[] ints2, int n) {
      long l = 0L;
      long l2 = 0L;
      l2 = 6719526752654111540L ^ (long)"➳\uf377掼苙眺\uea97Ặ赲䩟螄\uf15d鵜嬻㙴ﲖ슅䰨鼲\uf833ꤓư漲깫Н헶䀘忤".hashCode();
      l = 0L;
      l2 = (l2 ^ 1001622818601535851L) - -8991569689570669379L;

      while (true) {
         n--;
         l2 = (l2 ^ 5364991904706286364L) + 3815392536015287645L;
         if (n < 0) {
            l2 = l2 - 5263494473190797070L ^ -9117206620249866776L;
            return (int)(l >> 32);
         }

         l2 = (l2 ^ 2191096750599444183L) - -6518697618618115544L;
         l = ((long)ints[n] & 4294967295L) - ((long)ints2[n] & 4294967295L) + (l >> 32);
         l2 = (l2 ^ 5280576623124586824L) + 1438122276912373702L;
         ints[n] = (int)l;
         l2 = l2 + -1937696580457749070L ^ 7713182328990057240L;
         l2 = l2 + -7876056022465280183L ^ -7696561712541343361L;
      }
   }

   private boolean passesMillerRabin(int n, Random rand) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      int n2 = 0;
      Object obj4 = null;
      int n3 = 0;
      boolean flag = false;
      CustomBigInteger customBigInteger = this;
      obj2 = customBigInteger.subtract(ONE);
      obj4 = (CustomBigInteger)obj2;
      n3 = ((CustomBigInteger)obj4).getLowestSetBit();
      obj4 = ((CustomBigInteger)obj4).shiftRight(n3);
      if ((Random)rand == null) {
         rand = ThreadLocalRandom.current();
      }

      outerLoop:
      for (int i = 0; i < n; i++) {
         while (true) {
            obj = new CustomBigInteger(this.bitLength(), (Random)rand);
            customBigInteger = (CustomBigInteger)obj;
            if (customBigInteger.compareTo2(ONE) > 0) {
               customBigInteger = (CustomBigInteger)obj;
               if (customBigInteger.compareTo2(this) < 0) {
                  n2 = 0;
                  customBigInteger = (CustomBigInteger)obj;
                  obj3 = customBigInteger.modPow((CustomBigInteger)obj4, this);

                  while (true) {
                     if (n2 == 0) {
                        customBigInteger = (CustomBigInteger)obj3;
                        if (customBigInteger.equals(ONE)) {
                           continue outerLoop;
                        }
                     }

                     customBigInteger = (CustomBigInteger)obj3;
                     if (customBigInteger.equals((CustomBigInteger)obj2)) {
                        continue outerLoop;
                     }

                     if (n2 > 0) {
                        customBigInteger = (CustomBigInteger)obj3;
                        if (customBigInteger.equals(ONE)) {
                           return false;
                        }
                     }

                     if (++n2 == n3) {
                        return false;
                     }

                     customBigInteger = (CustomBigInteger)obj3;
                     obj3 = customBigInteger.modPow(TWO, this);
                  }
               }
            }
         }
      }

      return true;
   }

   private static final int[] squareToLen(int[] ints, int n, int[] ints2) {
      int n2 = 0;
      n2 = n << 1;
      if (ints2 == null || ints2.length < n2) {
         ints2 = new int[n2];
      }

      int[] ints3 = ints;
      implSquareToLenChecks(ints3, n, ints2, n2);
      ints3 = ints;
      return implSquareToLen(ints3, n, ints2, n2);
   }

   private CustomBigInteger getToomSlice(int n, int n2, int n3, int n4) {
      Object obj = null;
      int n5 = 0;
      int n6 = 0;
      int n7 = 0;
      int n8 = 0;
      int n9 = 0;
      n6 = this.mag.length;
      n7 = n4 - n6;
      if (n3 != 0) {
         n8 = n2 + (n3 - 1) * n - n7;
         n5 = n8 + n - 1;
      } else {
         n8 = 0 - n7;
         n5 = n2 - 1 - n7;
      }

      if (n8 < 0) {
         n8 = 0;
      }

      if (n5 >= 0) {
         n9 = n5 - n8 + 1;
         if (n9 > 0) {
            if (n8 == 0 && n9 >= n6) {
               return this.abs();
            } else {
               obj = new int[n9];
               System.arraycopy(this.mag, n8, (int[])obj, 0, n9);
               return new CustomBigInteger(trustedStripLeadingZeroInts((int[])obj), 1);
            }
         } else {
            return ZERO;
         }
      } else {
         return ZERO;
      }
   }

   private static void implMulAddCheck(int[] ints, int[] ints2, int n, int n2, int n3) {
      if (n2 <= ints2.length) {
         if (n >= 0) {
            if (n <= ints.length - 1) {
               if (n2 > ints.length - n) {
                  throw new IllegalArgumentException("input len is out of bound: " + n2 + " > " + (ints.length - n));
               }
            } else {
               throw new IllegalArgumentException("input offset is out of bound: " + n + " > " + (ints.length - 1));
            }
         } else {
            throw new IllegalArgumentException("input offset is invalid: " + n);
         }
      } else {
         throw new IllegalArgumentException("input length is out of bound: " + n2 + " > " + ints2.length);
      }
   }

   private int getInt(int n) {
      int n2 = 0;
      if (n >= 0) {
         if (n < this.mag.length) {
            n2 = this.mag[((int[])this.mag).length - n - 1];
            return this.signum < 0 ? (n <= this.firstNonzeroIntNum() ? -n2 : ~n2) : n2;
         } else {
            return this.signInt();
         }
      } else {
         return 0;
      }
   }

   public static void primitiveLeftShift(int[] ints, int n, int n2) {
      int n3 = 0;
      int n4 = 0;
      boolean flag = false;
      int n5 = 0;
      int n6 = 0;
      if (n != 0 && n2 != 0) {
         n6 = 32 - n2;
         n5 = 0;
         n3 = ints[n5];

         for (int i = n5 + n - 1; n5 < i; n5++) {
            n4 = n3;
            n3 = ints[n5 + 1];
            ints[n5] = n4 << n2 | n3 >>> n6;
         }

         ints[n - 1] = ints[n - 1] << n2;
      }
   }

   public static int mulAdd(int[] ints, int[] ints2, int n, int n2, int n3) {
      implMulAddCheck(ints, ints2, n, n2, n3);
      return implMulAdd(ints, ints2, n, n2, n3);
   }

   private CustomBigInteger exactDivideBy3() {
      long l = 0L;
      Object obj = null;
      long l2 = 0L;
      int n = 0;
      long l3 = 0L;
      int n2 = 0;
      long l4 = 0L;
      long l5 = 0L;
      l5 = -853100034311963879L ^ (long)"鼹禢\ue79c닂䣅ꍇ椽먿\uef8eꑃ⃘챎撣ﾳ왽쳻滨ᢀ鷐嬒\uefe3\ue881Ꮙ籣춢ဘ₱".hashCode();
      n = this.mag.length;
      l5 = (l5 ^ -3140704317328504748L) - 66714660195771521L;
      obj = new int[n];
      l5 = (l5 ^ 6621326316055274137L) - 2944862847460415011L;
      l3 = 0L;
      l5 = (l5 ^ 7858791498764097741L) - -5452839877717916821L;
      n2 = n - 1;
      l5 = (l5 ^ 7630712740424019855L) - -5421732138454696961L;

      while (n2 >= 0) {
         l5 = (l5 ^ 1435595645483452603L) - -3748819599507483403L;
         l = (long)this.mag[n2] & 4294967295L;
         l5 = (l5 ^ 5827393899503488735L) - 351635137890553738L;
         l4 = l - l3;
         l5 = (l5 ^ -8769443774458156005L) + 5093796197070684464L;
         if (l3 <= l) {
            l5 = (l5 ^ -8878710634216446439L) - -1780312269534147167L;
            l3 = 0L;
            l5 = (l5 ^ 8015052396482423635L) + -6357327684543471158L;
         } else {
            l5 = (l5 ^ -2591302050860093724L) - 7320231795760108002L;
            l3 = 1L;
            l5 = l5 + -5689581396091515441L ^ -8682683720797246257L;
            l5 = l5 - -6151904675338522965L ^ 6188387579156236828L;
         }

         l2 = l4 * 2863311531L & 4294967295L;
         l5 = l5 - 1804134960274676487L ^ -8099739335767469461L;
         ((int[])obj)[n2] = (int)l2;
         l5 = (l5 ^ 8199751142945693185L) + 3659530188426089864L;
         if (l2 < 1431655766L) {
            l5 = (l5 ^ 5327446886074090882L) - 1262276744288365204L;
         } else {
            l5 = l5 + -270430684919948525L ^ 5382646076661484152L;
            l3++;
            l5 = (l5 ^ -5967503264967504216L) + 3270367106098435721L;
            if (l2 < 2863311531L) {
               l5 = (l5 ^ -6813638384973831052L) - -151397866667163858L;
            } else {
               l5 = l5 - -7235705890387903934L ^ 5614423800580255245L;
               l3++;
               l5 = l5 + 1745587235875356790L ^ 7409966868128074107L;
            }
         }

         n2--;
         l5 = (l5 ^ -2324642365233395936L) + 7124760284777663746L;
         l5 = (l5 ^ -698082299691502490L) - 7467849655503972212L;
      }

      l5 = l5 - 7530628607849439738L ^ 3170950946751737565L;
      obj = trustedStripLeadingZeroInts((int[])obj);
      l5 = (l5 ^ -1289683068138859579L) + -5389359570122933284L;
      return new CustomBigInteger((int[])obj, this.signum);
   }

   public long longValueExact() {
      if (this.mag.length <= 2 && this.bitLength() <= 63) {
         return this.longValue();
      } else {
         throw new ArithmeticException("BigInteger out of long range");
      }
   }

   private static int[] subtract(int[] ints, int[] ints2) {
      Object obj = null;
      int n = 0;
      long l = 0L;
      boolean flag = false;
      int n2 = 0;
      long l2 = 0L;
      l2 = 5776279974146985733L ^ (long)"㥼⛅馇戲爁\uf828皕壒뷈텷쯠ℶ唋䧓쒟ꉀ㷠\uef64ꓖ䦦ꆁ邓뉁쳢徻".hashCode();
      n2 = ints.length;
      l2 = (l2 ^ 8751677650213073333L) - 9158352686076805802L;
      obj = new int[n2];
      l2 = l2 - 3767039408490025882L ^ -4351185292225453204L;
      n = ints2.length;
      l2 = (l2 ^ 8475004779931184423L) - -4162956472544597749L;
      l = 0L;
      l2 = (l2 ^ -6709112876251766837L) - 9039182320456181937L;

      while (n > 0) {
         l2 = (l2 ^ -1814277291592834526L) + -933112143857955338L;
         long l3 = (long)ints[--n2] & 4294967295L;
         n--;
         l = l3 - ((long)ints2[n] & 4294967295L) + (l >> 32);
         l2 = l2 - -7145624853614343478L ^ 3890302323638243483L;
         ((int[])obj)[n2] = (int)l;
         l2 = l2 - -2032975425831052387L ^ 4318787817064929730L;
         l2 = l2 + 649472878957851314L ^ 3012799554458596692L;
      }

      l2 = l2 - 2169885546812905536L ^ -8373630639755389064L;
      boolean flag2;
      if (l >> 32 == 0L) {
         l2 = (l2 ^ 3659669277716495134L) + -1262432444814351407L;
         flag2 = false;
      } else {
         l2 = (l2 ^ 6505279011880626679L) - -6265999511062632120L;
         flag2 = true;
      }

      flag = flag2;

      while (n2 > 0 && flag) {
         int[] ints4 = (int[])obj;
         n2--;
         flag = (ints4[n2] = ints[n2] - 1) == -1;
      }

      while (n2 > 0) {
         ((int[])obj)[n2] = ints[--n2];
      }

      return (int[])obj;
   }

   private static void toString(CustomBigInteger customBigInteger, StringBuilder sb, int n, int n2) {
      Object obj = null;
      int n3 = 0;
      Object obj2 = null;
      Object obj3 = null;
      int n4 = 0;
      int n5 = 0;
      if (customBigInteger.mag.length > 20) {
         n4 = customBigInteger.bitLength();
         n5 = (int)Math.round(Math.log((double)n4 * LOG_TWO / logCache[n]) / LOG_TWO - 1.0);
         obj3 = getRadixConversionCache(n, n5);
         CustomBigInteger customBigInteger2 = customBigInteger;
         obj = customBigInteger2.divideAndRemainder((CustomBigInteger)obj3);
         n3 = 1 << n5;
         customBigInteger2 = ((CustomBigInteger[])obj)[0];
         toString(customBigInteger2, sb, n, n2 - n3);
         customBigInteger2 = ((CustomBigInteger[])obj)[1];
         toString(customBigInteger2, sb, n, n3);
      } else {
         obj2 = customBigInteger.smallToString(n);
         if (((String)obj2).length() < n2 && sb.length() > 0) {
            for (int i = ((String)obj2).length(); i < n2; i++) {
               sb.append('0');
            }
         }

         sb.append((String)obj2);
      }
   }

   private String smallToString(int n) {
      boolean flag = false;
      Object obj = null;
      Object obj2 = null;
      int n2 = 0;
      Object obj3 = null;
      Object obj4 = null;
      Object obj5 = null;
      Object obj6 = null;
      Object obj7 = null;
      Object obj8 = null;
      Object obj9 = null;
      int n3 = 0;
      int n4 = 0;
      if (this.signum == 0) {
         return "0";
      } else {
         n4 = (4 * this.mag.length + 6) / 7;
         obj5 = new String[n4];
         obj3 = this.abs();
         n3 = 0;

         while (((CustomBigInteger)obj3).signum != 0) {
            obj4 = longRadix[n];
            obj7 = new ShadedMutableBigInteger();
            obj9 = new ShadedMutableBigInteger(((CustomBigInteger)obj3).mag);
            obj2 = new ShadedMutableBigInteger(((CustomBigInteger)obj4).mag);
            ShadedMutableBigInteger shadedMutableBigInteger = (ShadedMutableBigInteger)obj9;
            obj8 = shadedMutableBigInteger.divide2((ShadedMutableBigInteger)obj2, (ShadedMutableBigInteger)obj7);
            shadedMutableBigInteger = (ShadedMutableBigInteger)obj7;
            obj = shadedMutableBigInteger.toBigInteger(((CustomBigInteger)obj3).signum * ((CustomBigInteger)obj4).signum);
            shadedMutableBigInteger = (ShadedMutableBigInteger)obj8;
            obj6 = shadedMutableBigInteger.toBigInteger(((CustomBigInteger)obj3).signum * ((CustomBigInteger)obj4).signum);
            ((String[])obj5)[n3++] = Long.toString(((CustomBigInteger)obj6).longValue(), n);
            obj3 = (CustomBigInteger)obj;
         }

         obj4 = new StringBuilder(n3 * digitsPerLong[n] + 1);
         if (this.signum < 0) {
            ((StringBuilder)obj4).append('-');
         }

         ((StringBuilder)obj4).append(((String[])obj5)[n3 - 1]);

         for (int i = n3 - 2; i >= 0; i--) {
            n2 = digitsPerLong[n] - ((String[])obj5)[i].length();
            if (n2 != 0) {
               ((StringBuilder)obj4).append(zeros[n2]);
            }

            ((StringBuilder)obj4).append(((String[])obj5)[i]);
         }

         return ((StringBuilder)obj4).toString();
      }
   }

   public CustomBigInteger and(CustomBigInteger customBigInteger) {
      Object obj = null;
      boolean flag = false;
      obj = new int[Math.max(this.intLength(), customBigInteger.intLength())];

      for (int i = 0; i < ((int[])obj).length; i++) {
         ((int[])obj)[i] = this.getInt(((int[])obj).length - i - 1) & customBigInteger.getInt(((int[])obj).length - i - 1);
      }

      return valueOf2((int[])obj);
   }

   private CustomBigInteger getUpper(int n) {
      Object obj = null;
      int n2 = 0;
      int n3 = 0;
      n2 = this.mag.length;
      if (n2 > n) {
         n3 = n2 - n;
         obj = new int[n3];
         System.arraycopy(this.mag, 0, (int[])obj, 0, n3);
         return new CustomBigInteger(trustedStripLeadingZeroInts((int[])obj), 1);
      } else {
         return ZERO;
      }
   }

   public CustomBigInteger remainder(CustomBigInteger customBigInteger) {
      if (customBigInteger.mag.length >= 80 && this.mag.length - customBigInteger.mag.length >= 40) {
         return this.remainderBurnikelZiegler(customBigInteger);
      } else {
         return this.remainderKnuth(customBigInteger);
      }
   }

   public CustomBigInteger pow(int n) {
      long l = 0L;
      int n2 = 0;
      Object obj = null;
      long l2 = 0L;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      long l3 = 0L;
      long l4 = 0L;
      Object obj2 = null;
      int n6 = 0;
      int n7 = 0;
      if (n >= 0) {
         if (this.signum == 0) {
            return n != 0 ? this : ONE;
         } else {
            obj2 = this.abs();
            n6 = ((CustomBigInteger)obj2).getLowestSetBit();
            l2 = (long)n6 * (long)n;
            if (l2 > 2147483647L) {
               reportOverflow();
            }

            n5 = (int)l2;
            if (n6 > 0) {
               obj2 = ((CustomBigInteger)obj2).shiftRight(n6);
               n7 = ((CustomBigInteger)obj2).bitLength();
               if (n7 == 1) {
                  if (this.signum < 0 && (n & 1) == 1) {
                     return NEGATIVE_ONE.shiftLeft(n5);
                  }

                  return ONE.shiftLeft(n5);
               }
            } else {
               n7 = ((CustomBigInteger)obj2).bitLength();
               if (n7 == 1) {
                  if (this.signum < 0 && (n & 1) == 1) {
                     return NEGATIVE_ONE;
                  }

                  return ONE;
               }
            }

            l = (long)n7 * (long)n;
            if (((CustomBigInteger)obj2).mag.length == 1 && l <= 62L) {
               n2 = this.signum < 0 && (n & 1) == 1 ? -1 : 1;
               l4 = 1L;
               l3 = (long)((CustomBigInteger)obj2).mag[0] & 4294967295L;
               n4 = n;

               while (n4 != 0) {
                  if ((n4 & 1) == 1) {
                     l4 *= l3;
                  }

                  if ((n4 >>>= 1) != 0) {
                     l3 *= l3;
                  }
               }

               if (n6 <= 0) {
                  return valueOf(l4 * (long)n2);
               } else {
                  return (long)n5 + l <= 62L ? valueOf((l4 << n5) * (long)n2) : valueOf(l4 * (long)n2).shiftLeft(n5);
               }
            } else {
               if ((long)this.bitLength() * (long)n / 32L > 67108864L) {
                  reportOverflow();
               }

               obj = ONE;
               n3 = n;

               while (n3 != 0) {
                  if ((n3 & 1) == 1) {
                     obj = ((CustomBigInteger)obj).multiply2((CustomBigInteger)obj2);
                  }

                  if ((n3 >>>= 1) != 0) {
                     obj2 = ((CustomBigInteger)obj2).square();
                  }
               }

               if (n6 > 0) {
                  obj = ((CustomBigInteger)obj).shiftLeft(n5);
               }

               return this.signum < 0 && (n & 1) == 1 ? ((CustomBigInteger)obj).negate() : (CustomBigInteger)obj;
            }
         }
      } else {
         throw new ArithmeticException("Negative exponent");
      }
   }

   public CustomBigInteger xor(CustomBigInteger customBigInteger) {
      boolean flag = false;
      Object obj = null;
      obj = new int[Math.max(this.intLength(), customBigInteger.intLength())];

      for (int i = 0; i < ((int[])obj).length; i++) {
         ((int[])obj)[i] = this.getInt(((int[])obj).length - i - 1) ^ customBigInteger.getInt(((int[])obj).length - i - 1);
      }

      return valueOf2((int[])obj);
   }

   @Override
   public int hashCode() {
      int n = 0;
      boolean flag = false;
      n = 0;

      for (int i = 0; i < ((int[])this.mag).length; i++) {
         n = (int)((long)(31 * n) + ((long)this.mag[i] & 4294967295L));
      }

      return n * this.signum;
   }

   public CustomBigInteger andNot(CustomBigInteger customBigInteger) {
      Object obj = null;
      boolean flag = false;
      obj = new int[Math.max(this.intLength(), customBigInteger.intLength())];

      for (int i = 0; i < ((int[])obj).length; i++) {
         ((int[])obj)[i] = this.getInt(((int[])obj).length - i - 1) & ~customBigInteger.getInt(((int[])obj).length - i - 1);
      }

      return valueOf2((int[])obj);
   }

   public CustomBigInteger subtract(CustomBigInteger customBigInteger) {
      Object obj = null;
      int n = 0;
      if (customBigInteger.signum != 0) {
         if (this.signum != 0) {
            if (customBigInteger.signum == this.signum) {
               n = this.compareMagnitude(customBigInteger);
               if (n != 0) {
                  int[] ints;
                  if (n <= 0) {
                     int[] mag2 = this.mag;
                     ints = subtract(customBigInteger.mag, mag2);
                  } else {
                     int[] mag3 = customBigInteger.mag;
                     ints = subtract(this.mag, mag3);
                  }

                  obj = ints;
                  obj = trustedStripLeadingZeroInts((int[])obj);
                  return new CustomBigInteger((int[])obj, n == this.signum ? 1 : -1);
               } else {
                  return ZERO;
               }
            } else {
               int[] mag4 = customBigInteger.mag;
               return new CustomBigInteger(add2(this.mag, mag4), this.signum);
            }
         } else {
            return customBigInteger.negate();
         }
      } else {
         return this;
      }
   }

   public CustomBigInteger(int n, int n2, Random rand) {
      super();
      if (n < 2) {
         throw new ArithmeticException("bitLength < 2");
      } else {
         CustomBigInteger customBigInteger = n < 95 ? smallPrime(n, n2, rand) : largePrime(n, n2, rand);
         this.signum = 1;
         this.mag = customBigInteger.mag;
      }
   }

   public int[] javaIncrement(int[] ints) {
      int n = 0;
      boolean flag = false;
      n = 0;

      for (int i = ints.length - 1; i >= 0 && n == 0; i--) {
         n = ++ints[i];
      }

      if (n == 0) {
         ints = new int[((int[])ints).length + 1];
         ints[0] = 1;
      }

      return ints;
   }

   public CustomBigInteger(int n, byte[] bytes) {
      super();
      this.mag = stripLeadingZeroBytes(bytes);
      if (n >= -1 && n <= 1) {
         if (this.mag.length == 0) {
            this.signum = 0;
         } else {
            if (n == 0) {
               throw new NumberFormatException("signum-magnitude mismatch");
            }

            this.signum = n;
         }

         if (this.mag.length >= 67108864) {
            this.checkRange();
         }
      } else {
         throw new NumberFormatException("Invalid signum value");
      }
   }

   public int getLowestSetBit() {
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      n3 = this.lowestSetBitPlusTwo - 2;
      if (n3 == -2) {
         n3 = 0;
         if (this.signum == 0) {
            n3--;
         } else {
            n = 0;

            while ((n2 = this.getInt(n)) == 0) {
               n++;
            }

            n3 += (n << 5) + Integer.numberOfTrailingZeros(n2);
         }

         this.lowestSetBitPlusTwo = n3 + 2;
      }

      return n3;
   }

   private static int[] implMontgomerySquare(int[] ints, int[] ints2, int n, long l, int[] ints3) {
      ints3 = squareToLen(ints, n, ints3);
      return montReduce(ints3, ints2, n, (int)l);
   }

   private static CustomBigInteger getRadixConversionCache(int n, int n2) {
      boolean flag = false;
      Object obj = null;
      Object obj2 = null;
      int n3 = 0;
      obj = powerCache[n];
      if (n2 < ((CustomBigInteger[])obj).length) {
         return ((CustomBigInteger[])obj)[n2];
      } else {
         n3 = ((CustomBigInteger[])obj).length;
         obj = (CustomBigInteger[])Arrays.copyOf((CustomBigInteger[])obj, n2 + 1);

         for (int i = n3; i <= n2; i++) {
            ((CustomBigInteger[])obj)[i] = ((CustomBigInteger[])obj)[i - 1].pow((byte)2);
         }

         obj2 = powerCache;
         if (n2 >= ((CustomBigInteger[][])obj2)[n].length) {
            obj2 = (CustomBigInteger[][])((CustomBigInteger[][])obj2).clone();
            ((CustomBigInteger[][])obj2)[n] = (CustomBigInteger[])obj;
            powerCache = (CustomBigInteger[][])obj2;
         }

         return ((CustomBigInteger[])obj)[n2];
      }
   }

   private CustomBigInteger shiftRightImpl(int n) {
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      int n6 = 0;
      int n7 = 0;
      int n8 = 0;
      Object obj = null;
      Object obj2 = null;
      n6 = n >>> 5;
      n8 = n & 31;
      n2 = this.mag.length;
      obj = null;
      if (n6 >= n2) {
         return this.signum < 0 ? negConst[1] : ZERO;
      } else {
         if (n8 == 0) {
            n7 = n2 - n6;
            obj2 = Arrays.copyOf(this.mag, n7);
         } else {
            n7 = 0;
            n5 = this.mag[0] >>> n8;
            if (n5 != 0) {
               obj2 = new int[n2 - n6];
               ((int[])obj2)[n7++] = n5;
            } else {
               obj2 = new int[n2 - n6 - 1];
            }

            n3 = 32 - n8;
            n4 = 0;

            while (n4 < n2 - n6 - 1) {
               ((int[])obj2)[n7++] = this.mag[n4++] << n3 | this.mag[n4] >>> n8;
            }
         }

         if (this.signum < 0) {
            boolean flag = false;
            n5 = n2 - 1;

            for (int i = n2 - n6; n5 >= i && !flag; n5--) {
               flag = this.mag[n5] != 0;
            }

            if (!flag && n8 != 0) {
               flag = this.mag[n2 - n6 - 1] << 32 - n8 != 0;
            }

            if (flag) {
               obj2 = this.javaIncrement((int[])obj2);
            }
         }

         return new CustomBigInteger((int[])obj2, this.signum);
      }
   }

   private static int[] montReduce(int[] ints, int[] ints2, int n, int n2) {
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      int n6 = 0;
      int n7 = 0;
      n4 = 0;
      n7 = n;
      n6 = 0;

      do {
         n5 = ints[((int[])ints).length - 1 - n6];
         n3 = mulAdd(ints, ints2, n6, n, n2 * n5);
         n4 += addOne(ints, n6, n, n3);
         n6++;
      } while (--n7 > 0);

      while (n4 > 0) {
         n4 += subN(ints, ints2, n);
      }

      while (true) {
         if (intArrayCmpToLen(ints, ints2, n) < 0) {
            return ints;
         }

         subN(ints, ints2, n);
      }
   }

   public final int compareMagnitude2(long l) {
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      Object obj = null;
      long l2 = 0L;
      l2 = 6271424112204302900L ^ (long)"⟨栨ͪ졛쓩\udea6ㇵ䴯䏡\ueb6d뮒\udb5dꅾ馒\uee03㒦듨ﱀ꽈ណꯄ㮁".hashCode();
      if (!CustomBigInteger.class.desiredAssertionStatus()) {
         l2 = (l2 ^ 2608196064343351127L) + 5540617890958260605L;
      } else {
         l2 = l2 + 7586230582518442072L ^ -7643919084785348804L;
         if (l == Long.MIN_VALUE) {
            l2 = l2 + 5642432014688498780L ^ 1168237360159178157L;
            throw new AssertionError();
         }

         l2 = (l2 ^ -5575874524600883062L) - 6602698292057806428L;
      }

      obj = this.mag;
      l2 = l2 - 2080573276124802048L ^ -8305810607798927734L;
      n2 = ((int[])obj).length;
      l2 = (l2 ^ 5912038173780501315L) + -8365764267776822240L;
      if (n2 <= 2) {
         l2 = l2 - -4204127342278132582L ^ 8157825128787473439L;
         if (l >= 0L) {
            l2 = l2 - -2481458430162324043L ^ 5267991302384315446L;
         } else {
            l2 = (l2 ^ -3211563849147215913L) + -7250554712676901663L;
            l = -l;
            l2 = l2 + -8732306589136299561L ^ -6920005448792211244L;
         }

         n4 = (int)(l >>> 32);
         l2 = (l2 ^ -1807615638953567005L) + -952666827590597636L;
         if (n4 != 0) {
            l2 = l2 - 2249317000797932814L ^ -3216511514869135386L;
            if (n2 < 2) {
               return -1;
            } else {
               n3 = ((int[])obj)[0];
               if (n3 != n4) {
                  return ((long)n3 & 4294967295L) < ((long)n4 & 4294967295L) ? -1 : 1;
               } else {
                  n3 = ((int[])obj)[1];
                  n = (int)l;
                  if (n3 != n) {
                     return ((long)n3 & 4294967295L) < ((long)n & 4294967295L) ? -1 : 1;
                  } else {
                     return 0;
                  }
               }
            }
         } else {
            l2 = l2 + 3663452766392685950L ^ -736801200377258912L;
            if (n2 >= 1) {
               l2 = (l2 ^ 203208168660589200L) + -2299065853815370943L;
               if (n2 <= 1) {
                  l2 = (l2 ^ 4876730217459494547L) + -5329310071138398195L;
                  n3 = ((int[])obj)[0];
                  l2 = l2 - -8389081360662477991L ^ -2166874808294566155L;
                  n = (int)l;
                  l2 = l2 - -7274405918593845869L ^ 6197878368369464362L;
                  if (n3 == n) {
                     l2 = l2 + -8013772882029301219L ^ 2962099860750466895L;
                     return 0;
                  } else {
                     l2 = l2 - 5395151673495581917L ^ -3827490933447622579L;
                     byte b;
                     if (((long)n3 & 4294967295L) >= ((long)n & 4294967295L)) {
                        l2 = l2 + -3926113781329018520L ^ 7391067784597104028L;
                        b = 1;
                     } else {
                        l2 = l2 + 7068063526798006636L ^ -2438849837575320168L;
                        b = -1;
                     }

                     return b;
                  }
               } else {
                  l2 = (l2 ^ -8090741196860240366L) + 8895189272014145847L;
                  return 1;
               }
            } else {
               l2 = (l2 ^ 6763366007811074465L) - -4696776011975881799L;
               return -1;
            }
         }
      } else {
         l2 = (l2 ^ -5335689772560731877L) - 2953926464534450027L;
         return 1;
      }
   }

   private static int[] leftShift(int[] ints, int n, int n2) {
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      Object obj = null;
      n5 = n2 >>> 5;
      n4 = n2 & 31;
      n3 = bitLengthForInt(ints[0]);
      if (n2 > 32 - n3) {
         if (n4 > 32 - n3) {
            obj = new int[n5 + n + 1];
            System.arraycopy(ints, 0, (int[])obj, 0, n);
            primitiveRightShift((int[])obj, ((int[])obj).length, 32 - n4);
            return (int[])obj;
         } else {
            obj = new int[n5 + n];
            System.arraycopy(ints, 0, (int[])obj, 0, n);
            primitiveLeftShift((int[])obj, ((int[])obj).length, n4);
            return (int[])obj;
         }
      } else {
         primitiveLeftShift(ints, n, n4);
         return ints;
      }
   }

   private static CustomBigInteger valueOf2(int[] ints) {
      return ints[0] > 0 ? new CustomBigInteger(ints, 1) : new CustomBigInteger(ints);
   }

   @Override
   public String toString() {
      return this.toString((byte)10);
   }

   private int signInt() {
      return this.signum < 0 ? -1 : 0;
   }

   private void checkRange() {
      if (this.mag.length > 67108864 || this.mag.length == 67108864 && this.mag[0] < 0) {
         reportOverflow();
      }
   }

   public CustomBigInteger[] divideAndRemainder(CustomBigInteger customBigInteger) {
      if (customBigInteger.mag.length >= 80 && this.mag.length - customBigInteger.mag.length >= 40) {
         return this.divideAndRemainderBurnikelZiegler(customBigInteger);
      } else {
         return this.divideAndRemainderKnuth(customBigInteger);
      }
   }

   public CustomBigInteger modPow(CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      Object obj5 = null;
      Object obj6 = null;
      Object obj7 = null;
      Object obj8 = null;
      Object obj9 = null;
      boolean flag = false;
      int n = 0;
      Object obj10 = null;
      Object obj11 = null;
      Object obj12 = null;
      if (customBigInteger2.signum > 0) {
         if (customBigInteger.signum == 0) {
            return !customBigInteger2.equals(ONE) ? ONE : ZERO;
         } else {
            CustomBigInteger customBigInteger5 = this;
            if (customBigInteger5.equals(ONE)) {
               customBigInteger5 = customBigInteger2;
               return customBigInteger5.equals(ONE) ? ZERO : ONE;
            } else {
               customBigInteger5 = this;
               if (customBigInteger5.equals(ZERO) && customBigInteger.signum >= 0) {
                  return ZERO;
               } else {
                  customBigInteger5 = this;
                  if (customBigInteger5.equals(negConst[1])) {
                     customBigInteger5 = customBigInteger;
                     if (!customBigInteger5.testBit((byte)0)) {
                        customBigInteger5 = customBigInteger2;
                        return customBigInteger5.equals(ONE) ? ZERO : ONE;
                     }
                  }

                  if (flag = customBigInteger.signum < 0) {
                     customBigInteger = customBigInteger.negate();
                  }

                  exitBlock: {
                     if (this.signum >= 0) {
                        customBigInteger5 = this;
                        if (customBigInteger5.compareTo2(customBigInteger2) < 0) {
                           customBigInteger5 = this;
                           break exitBlock;
                        }
                     }

                     customBigInteger5 = this;
                     customBigInteger5 = customBigInteger5.mod(customBigInteger2);
                  }

                  obj7 = customBigInteger5;
                  customBigInteger5 = customBigInteger2;
                  if (customBigInteger5.testBit((byte)0)) {
                     customBigInteger5 = (CustomBigInteger)obj7;
                     obj11 = customBigInteger5.oddModPow(customBigInteger, customBigInteger2);
                  } else {
                     exitBlock2: {
                        n = customBigInteger2.getLowestSetBit();
                        obj8 = customBigInteger2.shiftRight(n);
                        obj = ONE.shiftLeft(n);
                        if (this.signum >= 0) {
                           customBigInteger5 = this;
                           if (customBigInteger5.compareTo2((CustomBigInteger)obj8) < 0) {
                              customBigInteger5 = this;
                              break exitBlock2;
                           }
                        }

                        customBigInteger5 = this;
                        customBigInteger5 = customBigInteger5.mod((CustomBigInteger)obj8);
                     }

                     obj3 = customBigInteger5;
                     customBigInteger5 = (CustomBigInteger)obj8;
                     if (customBigInteger5.equals(ONE)) {
                        customBigInteger5 = ZERO;
                     } else {
                        customBigInteger5 = (CustomBigInteger)obj3;
                        customBigInteger5 = customBigInteger5.oddModPow(customBigInteger, (CustomBigInteger)obj8);
                     }

                     obj9 = customBigInteger5;
                     customBigInteger5 = (CustomBigInteger)obj7;
                     obj5 = customBigInteger5.modPow2(customBigInteger, n);
                     customBigInteger5 = (CustomBigInteger)obj;
                     obj6 = customBigInteger5.modInverse((CustomBigInteger)obj8);
                     customBigInteger5 = (CustomBigInteger)obj8;
                     obj4 = customBigInteger5.modInverse((CustomBigInteger)obj);
                     if (customBigInteger2.mag.length < 33554432) {
                        customBigInteger5 = (CustomBigInteger)obj9;
                        customBigInteger5 = customBigInteger5.multiply2((CustomBigInteger)obj);
                        customBigInteger5 = customBigInteger5.multiply2((CustomBigInteger)obj6);
                        customBigInteger5 = customBigInteger5.add2(((CustomBigInteger)obj5).multiply2((CustomBigInteger)obj8).multiply2((CustomBigInteger)obj4));
                        obj11 = customBigInteger5.mod(customBigInteger2);
                     } else {
                        obj2 = new ShadedMutableBigInteger();
                        CustomBigInteger customBigInteger36 = (CustomBigInteger)obj9;
                        CustomBigInteger customBigInteger37 = (CustomBigInteger)obj;
                        new ShadedMutableBigInteger(customBigInteger36.multiply2(customBigInteger37)).multiply(new ShadedMutableBigInteger((CustomBigInteger)obj6), (ShadedMutableBigInteger)obj2);
                        obj12 = new ShadedMutableBigInteger();
                        customBigInteger36 = (CustomBigInteger)obj5;
                        CustomBigInteger customBigInteger38 = (CustomBigInteger)obj8;
                        new ShadedMutableBigInteger(customBigInteger36.multiply2(customBigInteger38)).multiply(new ShadedMutableBigInteger((CustomBigInteger)obj4), (ShadedMutableBigInteger)obj12);
                        ((ShadedMutableBigInteger)obj2).add((ShadedMutableBigInteger)obj12);
                        obj10 = new ShadedMutableBigInteger();
                        obj11 = ((ShadedMutableBigInteger)obj2).divide2(new ShadedMutableBigInteger(customBigInteger2), (ShadedMutableBigInteger)obj10).toBigInteger();
                     }
                  }

                  if (flag) {
                     customBigInteger5 = (CustomBigInteger)obj11;
                     customBigInteger5 = customBigInteger5.modInverse(customBigInteger2);
                  } else {
                     customBigInteger5 = (CustomBigInteger)obj11;
                  }

                  return customBigInteger5;
               }
            }
         }
      } else {
         throw new ArithmeticException("BigInteger: modulus not positive");
      }
   }

   private int firstNonzeroIntNum() {
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      n = this.firstNonzeroIntNumPlusTwo - 2;
      if (n == -2) {
         boolean flag = false;
         n2 = this.mag.length;
         n3 = n2 - 1;

         while (n3 >= 0 && this.mag[n3] == 0) {
            n3--;
         }

         n = n2 - n3 - 1;
         this.firstNonzeroIntNumPlusTwo = n + 2;
      }

      return n;
   }

   private int parseInt(char[] chars, int n, int n2) {
      boolean flag = false;
      int n3 = 0;
      int n4 = 0;
      n3 = Character.digit(chars[n++], 10);
      if (n3 == -1) {
         throw new NumberFormatException(new String(chars));
      } else {
         for (int i = n; i < n2; i++) {
            n4 = Character.digit(chars[i], 10);
            if (n4 == -1) {
               throw new NumberFormatException(new String(chars));
            }

            n3 = 10 * n3 + n4;
         }

         return n3;
      }
   }

   public CustomBigInteger add2(CustomBigInteger customBigInteger) {
      int n = 0;
      Object obj = null;
      if (customBigInteger.signum != 0) {
         if (this.signum != 0) {
            if (customBigInteger.signum != this.signum) {
               n = this.compareMagnitude(customBigInteger);
               if (n != 0) {
                  int[] ints;
                  if (n <= 0) {
                     int[] mag2 = this.mag;
                     ints = subtract(customBigInteger.mag, mag2);
                  } else {
                     int[] mag3 = customBigInteger.mag;
                     ints = subtract(this.mag, mag3);
                  }

                  obj = ints;
                  obj = trustedStripLeadingZeroInts((int[])obj);
                  return new CustomBigInteger((int[])obj, n == this.signum ? 1 : -1);
               } else {
                  return ZERO;
               }
            } else {
               int[] mag4 = customBigInteger.mag;
               return new CustomBigInteger(add2(this.mag, mag4), this.signum);
            }
         } else {
            return customBigInteger;
         }
      } else {
         return this;
      }
   }

   public CustomBigInteger(int n, Random rand) {
      this(1, randomBits(n, rand));
   }

   private CustomBigInteger squareToomCook3() {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      Object obj5 = null;
      int n = 0;
      Object obj6 = null;
      Object obj7 = null;
      Object obj8 = null;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      Object obj9 = null;
      Object obj10 = null;
      Object obj11 = null;
      Object obj12 = null;
      n = this.mag.length;
      n3 = (n + 2) / 3;
      n4 = n - 2 * n3;
      CustomBigInteger customBigInteger = this;
      obj5 = customBigInteger.getToomSlice(n3, n4, (byte)0, n);
      customBigInteger = this;
      obj12 = customBigInteger.getToomSlice(n3, n4, (byte)1, n);
      customBigInteger = this;
      obj9 = customBigInteger.getToomSlice(n3, n4, (byte)2, n);
      customBigInteger = (CustomBigInteger)obj9;
      obj = customBigInteger.square(true);
      customBigInteger = (CustomBigInteger)obj5;
      obj4 = customBigInteger.add2((CustomBigInteger)obj9);
      customBigInteger = (CustomBigInteger)obj4;
      customBigInteger = customBigInteger.subtract((CustomBigInteger)obj12);
      obj10 = customBigInteger.square(true);
      customBigInteger = (CustomBigInteger)obj4;
      obj4 = customBigInteger.add2((CustomBigInteger)obj12);
      customBigInteger = (CustomBigInteger)obj4;
      obj11 = customBigInteger.square(true);
      customBigInteger = (CustomBigInteger)obj5;
      obj8 = customBigInteger.square(true);
      customBigInteger = (CustomBigInteger)obj4;
      customBigInteger = customBigInteger.add2((CustomBigInteger)obj5);
      customBigInteger = customBigInteger.shiftLeft((byte)1);
      customBigInteger = customBigInteger.subtract((CustomBigInteger)obj9);
      obj7 = customBigInteger.square(true);
      customBigInteger = (CustomBigInteger)obj7;
      obj2 = customBigInteger.subtract((CustomBigInteger)obj10).exactDivideBy3();
      customBigInteger = (CustomBigInteger)obj11;
      customBigInteger = customBigInteger.subtract((CustomBigInteger)obj10);
      obj3 = customBigInteger.shiftRight((byte)1);
      customBigInteger = (CustomBigInteger)obj11;
      obj6 = customBigInteger.subtract((CustomBigInteger)obj);
      customBigInteger = (CustomBigInteger)obj2;
      customBigInteger = customBigInteger.subtract((CustomBigInteger)obj6);
      obj2 = customBigInteger.shiftRight((byte)1);
      customBigInteger = (CustomBigInteger)obj6;
      customBigInteger = customBigInteger.subtract((CustomBigInteger)obj3);
      obj6 = customBigInteger.subtract((CustomBigInteger)obj8);
      customBigInteger = (CustomBigInteger)obj2;
      obj2 = customBigInteger.subtract(((CustomBigInteger)obj8).shiftLeft((byte)1));
      customBigInteger = (CustomBigInteger)obj3;
      obj3 = customBigInteger.subtract((CustomBigInteger)obj2);
      n2 = n3 * 32;
      customBigInteger = ((CustomBigInteger)obj8).shiftLeft(n2);
      customBigInteger = customBigInteger.add2((CustomBigInteger)obj2).shiftLeft(n2);
      customBigInteger = customBigInteger.add2((CustomBigInteger)obj6).shiftLeft(n2);
      customBigInteger = customBigInteger.add2((CustomBigInteger)obj3).shiftLeft(n2);
      return customBigInteger.add2((CustomBigInteger)obj);
   }

   public CustomBigInteger gcd(CustomBigInteger customBigInteger) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      if (customBigInteger.signum != 0) {
         if (this.signum != 0) {
            obj3 = new ShadedMutableBigInteger(this);
            obj2 = new ShadedMutableBigInteger(customBigInteger);
            ShadedMutableBigInteger shadedMutableBigInteger = (ShadedMutableBigInteger)obj3;
            obj = shadedMutableBigInteger.hybridGCD((ShadedMutableBigInteger)obj2);
            shadedMutableBigInteger = (ShadedMutableBigInteger)obj;
            return shadedMutableBigInteger.toBigInteger((byte)1);
         } else {
            return customBigInteger.abs();
         }
      } else {
         return this.abs();
      }
   }

   private static int[] subtract2(long l, int[] ints) {
      int n = 0;
      long l2 = 0L;
      boolean flag = false;
      Object obj = null;
      long l3 = 0L;
      l3 = 8076005820413155421L ^ (long)"鉼ᑂ響꿁奚\uf1fa穊賰䢦飏䣯\ueb24咱➍떼ථ\uf8ddꅣ\uab1a絲곕鼮ީ\u0dfb歼".hashCode();
      n = (int)(l >>> 32);
      l3 = (l3 ^ -112585349796238231L) - -7376498692993483978L;
      if (n != 0) {
         l3 = (l3 ^ -6923122000753167181L) - 1134180695439898952L;
         obj = new int[2];
         l3 = l3 + 53600984367414255L ^ 1927320971800213356L;
         if (ints.length != 1) {
            l3 = (l3 ^ 484337486573044328L) - 1739549154124638272L;
            l2 = ((long)((int)l) & 4294967295L) - ((long)ints[1] & 4294967295L);
            ((int[])obj)[1] = (int)l2;
            l2 = ((long)n & 4294967295L) - ((long)ints[0] & 4294967295L) + (l2 >> 32);
            ((int[])obj)[0] = (int)l2;
            return (int[])obj;
         } else {
            l3 = (l3 ^ -4950219801856094110L) - -624003150148175489L;
            l2 = ((long)((int)l) & 4294967295L) - ((long)ints[0] & 4294967295L);
            l3 = (l3 ^ 1636358769691313944L) - -6435209981807030488L;
            ((int[])obj)[1] = (int)l2;
            l3 = (l3 ^ -3623003851771214483L) + -3361295497728657037L;
            boolean flag2;
            if (l2 >> 32 == 0L) {
               l3 = l3 - 5124479046301767123L ^ 506155897126580616L;
               flag2 = false;
            } else {
               l3 = l3 - -8453848188900558078L ^ -4362261095705764741L;
               flag2 = true;
            }

            flag = flag2;
            if (flag) {
               ((int[])obj)[0] = n - 1;
            } else {
               ((int[])obj)[0] = n;
            }

            return (int[])obj;
         }
      } else {
         l3 = (l3 ^ 6133795252757828644L) + -1279014396256489716L;
         obj = new int[1];
         l3 = (l3 ^ 516475513241134608L) + 6412583014594342706L;
         ((int[])obj)[0] = (int)(l - ((long)ints[0] & 4294967295L));
         l3 = (l3 ^ -8125230447565463449L) + -2121644304717719788L;
         return (int[])obj;
      }
   }

   private static int implMulAdd(int[] ints, int[] ints2, int n, int n2, int n3) {
      long l = 0L;
      long l2 = 0L;
      int n4 = 0;
      long l3 = 0L;
      long l4 = 0L;
      l4 = -8820436693089587116L ^ (long)"ᜠꓜ⯻粿劬躔\uf01c﨤嗁純쒌้햕۷룞餞ࢮ鹦퀆⚛몚朞傎醇矕곡ⰴ".hashCode();
      l3 = (long)n3 & 4294967295L;
      l4 = l4 + 9159472506446806107L ^ -7916669191933763668L;
      l2 = 0L;
      l4 = (l4 ^ -8192106092932109243L) + 7173965963448157408L;
      n = ints.length - n - 1;
      l4 = (l4 ^ 2411466433857061335L) - 8517019863075681019L;
      n4 = n2 - 1;
      l4 = l4 + 4542406478168237962L ^ 3391769101388413635L;

      while (n4 >= 0) {
         l4 = (l4 ^ 4985308510003867852L) - -3769455212042791805L;
         l = ((long)ints2[n4] & 4294967295L) * l3 + ((long)ints[n] & 4294967295L) + l2;
         l4 = (l4 ^ -3478574054049410883L) - -321396283506756694L;
         ints[n--] = (int)l;
         l4 = l4 + -2288881774759707924L ^ -709648541546690091L;
         l2 = l >>> 32;
         l4 = (l4 ^ 4388076760063055996L) + -5305727671135212518L;
         n4--;
         l4 = (l4 ^ 2105420100911155139L) + 168711631278965823L;
         l4 = (l4 ^ 3680905053513634305L) + -7525916307094594852L;
      }

      l4 = l4 - 5520762313455356145L ^ 1267016226366430216L;
      return (int)l2;
   }

   private static int[] shiftLeft(int[] ints, int n) {
      Object obj = null;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      Object obj2 = null;
      int n6 = 0;
      int n7 = 0;
      int n8 = 0;
      n2 = n >>> 5;
      n8 = n & 31;
      n3 = ints.length;
      obj = null;
      if (n8 != 0) {
         n5 = 0;
         n4 = 32 - n8;
         n7 = ints[0] >>> n4;
         if (n7 == 0) {
            obj2 = new int[n3 + n2];
         } else {
            obj2 = new int[n3 + n2 + 1];
            ((int[])obj2)[n5++] = n7;
         }

         n6 = 0;

         while (n6 < n3 - 1) {
            ((int[])obj2)[n5++] = ints[n6++] << n8 | ints[n6] >>> n4;
         }

         ((int[])obj2)[n5] = ints[n6] << n8;
      } else {
         obj2 = new int[n3 + n2];
         System.arraycopy(ints, 0, (int[])obj2, 0, n3);
      }

      return (int[])obj2;
   }

   private static CustomBigInteger multiplyKaratsuba(CustomBigInteger customBigInteger, CustomBigInteger customBigInteger2) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      int n = 0;
      Object obj4 = null;
      Object obj5 = null;
      int n2 = 0;
      int n3 = 0;
      Object obj6 = null;
      Object obj7 = null;
      Object obj8 = null;
      n3 = customBigInteger.mag.length;
      n2 = customBigInteger2.mag.length;
      n = (Math.max(n3, n2) + 1) / 2;
      obj2 = customBigInteger.getLower(n);
      obj7 = customBigInteger.getUpper(n);
      obj8 = customBigInteger2.getLower(n);
      obj5 = customBigInteger2.getUpper(n);
      CustomBigInteger customBigInteger3 = (CustomBigInteger)obj7;
      obj3 = customBigInteger3.multiply2((CustomBigInteger)obj5);
      customBigInteger3 = (CustomBigInteger)obj2;
      obj6 = customBigInteger3.multiply2((CustomBigInteger)obj8);
      customBigInteger3 = (CustomBigInteger)obj7;
      customBigInteger3 = customBigInteger3.add2((CustomBigInteger)obj2);
      CustomBigInteger customBigInteger7 = (CustomBigInteger)obj5;
      obj = customBigInteger3.multiply2(customBigInteger7.add2((CustomBigInteger)obj8));
      customBigInteger3 = (CustomBigInteger)obj3;
      customBigInteger3 = customBigInteger3.shiftLeft(32 * n);
      customBigInteger7 = (CustomBigInteger)obj;
      customBigInteger7 = customBigInteger7.subtract((CustomBigInteger)obj3);
      customBigInteger3 = customBigInteger3.add2(customBigInteger7.subtract((CustomBigInteger)obj6));
      customBigInteger3 = customBigInteger3.shiftLeft(32 * n);
      obj4 = customBigInteger3.add2((CustomBigInteger)obj6);
      return customBigInteger.signum == customBigInteger2.signum ? (CustomBigInteger)obj4 : ((CustomBigInteger)obj4).negate();
   }

   private CustomBigInteger[] divideAndRemainderKnuth(CustomBigInteger customBigInteger) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      Object obj5 = null;
      obj3 = new CustomBigInteger[2];
      obj4 = new ShadedMutableBigInteger();
      obj5 = new ShadedMutableBigInteger(this.mag);
      obj = new ShadedMutableBigInteger(customBigInteger.mag);
      ShadedMutableBigInteger shadedMutableBigInteger = (ShadedMutableBigInteger)obj5;
      ShadedMutableBigInteger shadedMutableBigInteger2 = (ShadedMutableBigInteger)obj;
      obj2 = shadedMutableBigInteger.divideKnuth(shadedMutableBigInteger2, (ShadedMutableBigInteger)obj4);
      CustomBigInteger[] customBigIntegers = (CustomBigInteger[])obj3;
      shadedMutableBigInteger2 = (ShadedMutableBigInteger)obj4;
      customBigIntegers[0] = shadedMutableBigInteger2.toBigInteger(this.signum == customBigInteger.signum ? 1 : -1);
      CustomBigInteger[] customBigIntegers2 = (CustomBigInteger[])obj3;
      shadedMutableBigInteger2 = (ShadedMutableBigInteger)obj2;
      customBigIntegers2[1] = shadedMutableBigInteger2.toBigInteger(this.signum);
      return (CustomBigInteger[])obj3;
   }



}
