package net.augustus.vendor.launcher.crypto.math;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.StreamCorruptedException;
import java.io.ObjectInputStream.GetField;
import java.io.ObjectOutputStream.PutField;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ShadedBigInteger extends ShadedNumber implements Comparable {
   private static ShadedBigInteger[] longRadix;
   private static volatile ShadedBigInteger[][] powerCache;
   @Deprecated
   private int bitCount;
   private static ShadedBigInteger[] negConst = new ShadedBigInteger[17];
   private static final int KARATSUBA_SQUARE_THRESHOLD = 128;
   public static final ShadedBigInteger TEN;
   private static final int MONTGOMERY_INTRINSIC_THRESHOLD = 512;
   private static ShadedBigInteger[] posConst = new ShadedBigInteger[17];
   private static int[] digitsPerLong;
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
   private static final int TOOM_COOK_THRESHOLD = 240;
   public static int[] bnExpModThreshTable;
   private static final int KARATSUBA_THRESHOLD = 80;
   public static final long LONG_MASK = 4294967295L;
   @Deprecated
   private int firstNonzeroIntNum;
   public static final int BURNIKEL_ZIEGLER_OFFSET = 40;
   public static final int BURNIKEL_ZIEGLER_THRESHOLD = 80;
   private static final int SCHOENHAGE_BASE_CONVERSION_THRESHOLD = 20;
   private static final int MAX_CONSTANT = 16;
   private static int[] intRadix;
   private static final int TOOM_COOK_SQUARE_THRESHOLD = 216;
   public final int signum;
   private static final int MAX_MAG_LENGTH = 67108864;
   private static final ShadedBigInteger SMALL_PRIME_PRODUCT;
   @Deprecated
   private int lowestSetBit;
   private static final int MULTIPLY_SQUARE_THRESHOLD = 20;
   private static final long serialVersionUID = -8287574255936472291L;
   private static String[] zeros;
   private static final int DEFAULT_PRIME_CERTAINTY = 100;
   public static final ShadedBigInteger ONE;
   private static final ShadedBigInteger NEGATIVE_ONE;
   private static int[] digitsPerInt;
   public static final ShadedBigInteger TWO;
   public static final boolean assertionsDisabled = !ShadedBigInteger.class.desiredAssertionStatus();
   private static final double[] logCache;
   private static final int SMALL_PRIME_THRESHOLD = 95;
   public final int[] mag;
   private static final int PRIME_SEARCH_BIT_LENGTH_LIMIT = 500000000;
   private static final ObjectStreamField[] serialPersistentFields;
   private static final double LOG_TWO = Math.log(2.0);
   @Deprecated
   private int bitLength;
   public static final ShadedBigInteger ZERO;

   private ShadedBigInteger(byte[] bytes, int n) {
      super();
      int[] flowState2 = ShadedNumber.getFlowState();
      this.signum = bytes.length == 0 ? 0 : n;
      this.mag = stripLeadingZeroBytes(bytes);
      if (this.mag.length >= 67108864) {
         this.checkRange();
      }
   }

   public ShadedBigInteger divide(ShadedBigInteger shadedBigInteger) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (shadedBigInteger.mag.length >= 80 && this.mag.length - shadedBigInteger.mag.length >= 40) {
         return this.divideBurnikelZiegler(shadedBigInteger);
      } else {
         return this.divideKnuth(shadedBigInteger);
      }
   }

   private static ShadedBigInteger multiplyKaratsuba(ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2) {
      int length = shadedBigInteger.mag.length;
      int length2 = shadedBigInteger2.mag.length;
      int n = (Math.max(length, length2) + 1) / 2;
      ShadedBigInteger lower = shadedBigInteger.getLower(n);
      ShadedBigInteger upper = shadedBigInteger.getUpper(n);
      ShadedBigInteger lower2 = shadedBigInteger2.getLower(n);
      ShadedBigInteger upper2 = shadedBigInteger2.getUpper(n);
      ShadedBigInteger shadedBigInteger3 = upper.multiply(upper2);
      ShadedBigInteger shadedBigInteger4 = lower.multiply(lower2);
      ShadedBigInteger shadedBigInteger5 = upper.add(lower);
      ShadedBigInteger shadedBigInteger6 = upper2.add(lower2);
      ShadedBigInteger shadedBigInteger7 = shadedBigInteger5.multiply(shadedBigInteger6);
      int n2 = 32 * n;
      shadedBigInteger5 = shadedBigInteger3.shiftLeft(n2);
      ShadedBigInteger shadedBigInteger8 = shadedBigInteger7.subtract(shadedBigInteger3).subtract(shadedBigInteger4);
      ShadedBigInteger shadedBigInteger9 = shadedBigInteger8;
      shadedBigInteger5 = shadedBigInteger5.add(shadedBigInteger9);
      int n3 = 32 * n;
      shadedBigInteger5 = shadedBigInteger5.shiftLeft(n3);
      ShadedBigInteger shadedBigInteger10 = shadedBigInteger5.add(shadedBigInteger4);
      return shadedBigInteger.signum != shadedBigInteger2.signum ? shadedBigInteger10.negate() : shadedBigInteger10;
   }

   private static int[] add(int[] ints, long l) {
      long l2 = 0L;
      ShadedNumber.getFlowState();
      int length = ints.length;
      int n = (int)(l >>> 32);
      int[] ints2;
      if (n == 0) {
         ints2 = new int[length];
         l2 = ((long)ints[--length] & 4294967295L) + l;
         ints2[length] = (int)l2;
      } else {
         if (length == 1) {
            ints2 = new int[2];
            l2 = l + ((long)ints[0] & 4294967295L);
            ints2[1] = (int)l2;
            ints2[0] = (int)(l2 >>> 32);
            return ints2;
         }

         ints2 = new int[length];
         long l3 = ((long)ints[--length] & 4294967295L) + (l & 4294967295L);
         ints2[length] = (int)l3;
         l2 = ((long)ints[--length] & 4294967295L) + ((long)n & 4294967295L) + (l3 >>> 32);
         ints2[length] = (int)l2;
      }

      boolean flag;
      for (flag = l2 >>> 32 != 0L; length > 0 && flag; flag = (ints2[length] = ints[length] + 1) == 0) {
         length--;
      }

      while (length > 0) {
         ints2[length] = ints[--length];
      }

      if (flag) {
         int[] ints3 = new int[ints2.length + 1];
         System.arraycopy(ints2, 0, ints3, 1, ints2.length);
         ints3[0] = 1;
         return ints3;
      } else {
         return ints2;
      }
   }

   private static int[] implMontgomerySquare(int[] ints, int[] ints2, int n, long l, int[] ints3) {
      ints3 = squareToLen(ints, n, ints3);
      int n2 = (int)l;
      return montReduce(ints3, ints2, n, n2);
   }

   public ShadedBigInteger remainder(ShadedBigInteger shadedBigInteger) {
      int[] flowState2 = ShadedNumber.getFlowState();
      return shadedBigInteger.mag.length >= 80 && this.mag.length - shadedBigInteger.mag.length >= 40 ? this.remainderBurnikelZiegler(shadedBigInteger) : this.remainderKnuth(shadedBigInteger);
   }

   public ShadedBigInteger multiply(ShadedBigInteger shadedBigInteger) {
      boolean flag = false;
      return this.multiply(shadedBigInteger, flag);
   }

   private ShadedBigInteger remainderKnuth(ShadedBigInteger shadedBigInteger) {
      MutableBigInt mutableBigInt = new MutableBigInt();
      MutableBigInt mutableBigInt2 = new MutableBigInt(this.mag);
      MutableBigInt mutableBigInt3 = new MutableBigInt(shadedBigInteger.mag);
      MutableBigInt mutableBigInt4 = mutableBigInt2.divideKnuth(mutableBigInt3, mutableBigInt);
      int signum2 = this.signum;
      return mutableBigInt4.toBigInteger(signum2);
   }

   private static int bitLength(int[] ints, int n) {
      return n == 0 ? 0 : (n - 1 << 5) + bitLengthForInt(ints[0]);
   }

   public int bitCount(long l) {
      int[] flowState2 = ShadedNumber.getFlowState();
      int n = this.bitCount - 1;
      if (n == -1) {
         n = 0;

         for (int i = 0; i < this.mag.length; i++) {
            n += Integer.bitCount(this.mag[i]);
         }

         if (this.signum < 0) {
            int n2 = 0;

            int n3;
            for (n3 = this.mag.length - 1; this.mag[n3] == 0; n3--) {
               n2 += 32;
            }

            n2 += Integer.numberOfTrailingZeros(this.mag[n3]);
            n += n2 - 1;
         }

         int n4 = n + 1;
         this.bitCount = n4;
      }

      return n;
   }

   private static void montgomeryMultiplyChecks(int[] ints, int[] ints2, int[] ints3, int n, int[] ints4) throws RuntimeException {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (n % 2 != 0) {
         throw new IllegalArgumentException("input array length must be even: " + n);
      } else if (n < 1) {
         throw new IllegalArgumentException("invalid input length: " + n);
      } else if (n > ints.length || n > ints2.length || n > ints3.length || ints4 != null && n > ints4.length) {
         throw new IllegalArgumentException("input array length out of bound: " + n);
      }
   }

   private ShadedBigInteger square() {
      boolean flag = false;
      return this.square(flag);
   }

   public ShadedBigInteger max(ShadedBigInteger shadedBigInteger) {
      int[] flowState2 = ShadedNumber.getFlowState();
      return this.compareTo2(shadedBigInteger) > 0 ? this : shadedBigInteger;
   }

   private int getInt(int n) {
      if (n < 0) {
         return 0;
      } else if (n >= this.mag.length) {
         return this.signInt();
      } else {
         int n2 = this.mag[this.mag.length - n - 1];
         int n3;
         if (this.signum >= 0) {
            n3 = n2;
         } else {
            n3 = n <= this.firstNonzeroIntNum() ? -n2 : ~n2;
         }

         return n3;
      }
   }

   private static int intArrayCmpToLen(int[] ints, int[] ints2, int n) {
      int[] flowState2 = ShadedNumber.getFlowState();

      for (int i = 0; i < n; i++) {
         long l = (long)ints[i] & 4294967295L;
         long l2 = (long)ints2[i] & 4294967295L;
         if (l < l2) {
            return -1;
         }

         if (l > l2) {
            return 1;
         }
      }

      return 0;
   }

   public ShadedBigInteger[] divideAndRemainder(ShadedBigInteger shadedBigInteger) {
      int[] flowState2 = ShadedNumber.getFlowState();
      return shadedBigInteger.mag.length >= 80 && this.mag.length - shadedBigInteger.mag.length >= 40 ? this.divideAndRemainderBurnikelZiegler(shadedBigInteger) : this.divideAndRemainderKnuth(shadedBigInteger);
   }

   public boolean isProbablePrime(int n) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (n <= 0) {
         return true;
      } else {
         ShadedBigInteger shadedBigInteger = this.abs();
         ShadedBigInteger shadedBigInteger2 = TWO;
         if (shadedBigInteger.equals(shadedBigInteger2)) {
            return true;
         } else {
            byte b = 0;
            if (shadedBigInteger.testBit(b)) {
               ShadedBigInteger shadedBigInteger3 = ONE;
               if (!shadedBigInteger.equals(shadedBigInteger3)) {
                  Object obj = null;
                  return shadedBigInteger.primeToCertainty(n, (Random)obj);
               }
            }

            return false;
         }
      }
   }

   public static ShadedBigInteger valueOf(long l) {
      if (l == 0L) {
         return ZERO;
      } else if (l > 0L && l <= 16L) {
         return posConst[(int)l];
      } else if (l < 0L && l >= -16L) {
         return negConst[(int)(-l)];
      } else {
         return new ShadedBigInteger(l);
      }
   }

   public ShadedBigInteger min(ShadedBigInteger shadedBigInteger) {
      int[] flowState2 = ShadedNumber.getFlowState();
      return this.compareTo2(shadedBigInteger) < 0 ? this : shadedBigInteger;
   }

   private static int[] montgomerySquare(int[] ints, int[] ints2, int n, long l, int[] ints3) {
      int[] flowState2 = ShadedNumber.getFlowState();
      montgomeryMultiplyChecks(ints, ints, ints2, n, ints3);
      if (n > 512) {
         ints3 = squareToLen(ints, n, ints3);
         int n2 = (int)l;
         return montReduce(ints3, ints2, n, n2);
      } else {
         int[] ints4 = materialize(ints3, n);
         return implMontgomerySquare(ints, ints2, n, l, ints4);
      }
   }

   public ShadedBigInteger(int n, int n2, Random rand) {
      super();
      int[] flowState2 = ShadedNumber.getFlowState();
      if (n < 2) {
         throw new ArithmeticException("bitLength < 2");
      } else {
         ShadedBigInteger shadedBigInteger;
         if (n < 95) {
            shadedBigInteger = smallPrime(n, n2, rand);
         } else {
            shadedBigInteger = largePrime(n, n2, rand);
         }

         ShadedBigInteger shadedBigInteger2 = shadedBigInteger;
         this.signum = 1;
         this.mag = shadedBigInteger2.mag;
      }
   }

   public static ShadedBigInteger probablePrime(int n, Random rand) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (n < 2) {
         throw new ArithmeticException("bitLength < 2");
      } else {
         ShadedBigInteger shadedBigInteger;
         if (n < 95) {
            shadedBigInteger = smallPrime(n, 100, rand);
         } else {
            shadedBigInteger = largePrime(n, 100, rand);
         }

         return shadedBigInteger;
      }
   }

   public static int mulAdd(int[] ints, int[] ints2, int n, int n2, int n3) {
      implMulAddCheck(ints, ints2, n, n2, n3);
      return implMulAdd(ints, ints2, n, n2, n3);
   }

   private static ShadedBigInteger getRadixConversionCache(int n, int n2) {
      int[] flowState2 = ShadedNumber.getFlowState();
      ShadedBigInteger[] shadedBigIntegers = powerCache[n];
      if (n2 < shadedBigIntegers.length) {
         return shadedBigIntegers[n2];
      } else {
         int length = shadedBigIntegers.length;
         shadedBigIntegers = Arrays.copyOf(shadedBigIntegers, n2 + 1);

         for (int i = length; i <= n2; i++) {
            ShadedBigInteger shadedBigInteger = shadedBigIntegers[i - 1];
            byte b = 2;
            shadedBigIntegers[i] = shadedBigInteger.pow(b);
         }

         ShadedBigInteger[][] shadedBigIntegersArr = powerCache;
         if (n2 >= shadedBigIntegersArr[n].length) {
            shadedBigIntegersArr = (ShadedBigInteger[][])shadedBigIntegersArr.clone();
            shadedBigIntegersArr[n] = shadedBigIntegers;
            powerCache = shadedBigIntegersArr;
         }

         return shadedBigIntegers[n2];
      }
   }

   public long longValueExact() {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (this.mag.length <= 2 && this.bitLength() <= 63) {
         return this.longValue();
      } else {
         throw new ArithmeticException("BigInteger out of long range");
      }
   }

   private byte[] magSerializedForm() {
      int[] flowState2 = ShadedNumber.getFlowState();
      int length = this.mag.length;
      int n = length == 0 ? 0 : (length - 1 << 5) + bitLengthForInt(this.mag[0]);
      int n2 = n + 7 >>> 3;
      byte[] bytes = new byte[n2];
      int n3 = n2 - 1;
      int n4 = 4;
      int n5 = length - 1;

      for (int i = 0; n3 >= 0; n3--) {
         if (n4 == 4) {
            i = this.mag[n5--];
            n4 = 1;
         } else {
            i >>>= 8;
            n4++;
         }

         bytes[n3] = (byte)i;
      }

      return bytes;
   }

   private ShadedBigInteger(int n, int[] ints) {
      super();
      ShadedNumber.getFlowState();
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

   public boolean primeToCertainty(int n, Random rand) {
      int n2 = 0;
      ShadedNumber.getFlowState();
      int n3 = (Math.min(n, 2147483646) + 1) / 2;
      int n4 = this.bitLength();
      if (n4 < 100) {
         n2 = (byte)50;
         n2 = n3 < n2 ? n3 : n2;
         return this.passesMillerRabin(n2, rand);
      } else {
         byte b;
         if (n4 < 256) {
            b = 27;
         } else if (n4 < 512) {
            b = 15;
         } else if (n4 < 768) {
            b = 8;
         } else if (n4 < 1024) {
            b = 4;
         } else {
            b = 2;
         }

         b = (byte)(n3 < b ? n3 : b);
         if (this.passesMillerRabin(b, rand)) {
            if (this.passesLucasLehmer()) {
               return true;
            }
         }

         return false;
      }
   }

   @Override
   public String toString() {
      byte b = 10;
      return this.toString(b);
   }

   @Override
   public int hashCode() {
      int n = 0;
      ShadedNumber.getFlowState();

      for (int i = 0; i < this.mag.length; i++) {
         n = (int)((long)(31 * n) + ((long)this.mag[i] & 4294967295L));
      }

      return n * this.signum;
   }

   private ShadedBigInteger getLower(int n) {
      int length = this.mag.length;
      if (length <= n) {
         return this.abs();
      } else {
         int[] ints = new int[n];
         System.arraycopy(this.mag, length - n, ints, 0, n);
         return new ShadedBigInteger(trustedStripLeadingZeroInts(ints), 1);
      }
   }

   @Override
   public double doubleValue() {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (this.signum == 0) {
         return 0.0;
      } else {
         int n = (this.mag.length - 1 << 5) + bitLengthForInt(this.mag[0]) - 1;
         if (n < 63) {
            return (double)this.longValue();
         } else if (n > 1023) {
            return this.signum > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
         } else {
            int n2 = n - 53;
            int n3 = n2 & 31;
            int n4 = 32 - n3;
            int n5;
            int n6;
            if (n3 == 0) {
               n5 = this.mag[0];
               n6 = this.mag[1];
            } else {
               n5 = this.mag[0] >>> n3;
               n6 = this.mag[0] << n4 | this.mag[1] >>> n3;
               if (n5 == 0) {
                  n5 = n6;
                  n6 = this.mag[1] << n4 | this.mag[2] >>> n3;
               }
            }

            long l = ((long)n5 & 4294967295L) << 32 | (long)n6 & 4294967295L;
            long l2 = l >> 1;
            l2 &= 4503599627370495L;
            boolean flag = (l & 1L) != 0L && ((l2 & 1L) != 0L || this.abs().getLowestSetBit() < n2);
            long l3 = flag ? l2 + 1L : l2;
            long l4 = (long)(n + 1023) << 52;
            l4 += l3;
            l4 |= (long)this.signum & Long.MIN_VALUE;
            return Double.longBitsToDouble(l4);
         }
      }
   }

   private static int[] makePositive(byte[] bytes) {
      int length = bytes.length;
      ShadedNumber.getFlowState();
      int n = 0;

      while (n < length && bytes[n] == -1) {
         n++;
      }

      int n2 = n;

      while (n2 < length && bytes[n2] == 0) {
         n2++;
      }

      int n3 = n2 == length ? 1 : 0;
      int n4 = length - n + n3 + 3 >>> 2;
      int[] ints = new int[n4];
      int n5 = length - 1;

      for (int i = n4 - 1; i >= 0; i--) {
         ints[i] = bytes[n5--] & 255;
         int n6 = Math.min(3, n5 - n + 1);
         if (n6 < 0) {
            n6 = 0;
         }

         for (int j = 8; j <= 8 * n6; j += 8) {
            ints[i] |= (bytes[n5--] & 255) << j;
         }

         int n7 = -1 >>> 8 * (3 - n6);
         ints[i] = ~ints[i] & n7;
      }

      for (int k = ints.length - 1; k >= 0; k--) {
         ints[k] = (int)(((long)ints[k] & 4294967295L) + 1L);
         if (ints[k] != 0) {
            break;
         }
      }

      return ints;
   }

   private int intLength() {
      return (this.bitLength() >>> 5) + 1;
   }

   private String smallToString(int n) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (this.signum == 0) {
         return "0";
      } else {
         int n2 = (4 * this.mag.length + 6) / 7;
         String[] strings = new String[n2];
         ShadedBigInteger shadedBigInteger = this.abs();
         int n3 = 0;

         while (shadedBigInteger.signum != 0) {
            ShadedBigInteger shadedBigInteger2 = longRadix[n];
            MutableBigInt mutableBigInt = new MutableBigInt();
            MutableBigInt mutableBigInt2 = new MutableBigInt(shadedBigInteger.mag);
            MutableBigInt mutableBigInt3 = new MutableBigInt(shadedBigInteger2.mag);
            MutableBigInt mutableBigInt4 = mutableBigInt2.divide2(mutableBigInt3, mutableBigInt);
            int n4 = shadedBigInteger.signum * shadedBigInteger2.signum;
            ShadedBigInteger shadedBigInteger3 = mutableBigInt.toBigInteger(n4);
            int n5 = shadedBigInteger.signum * shadedBigInteger2.signum;
            ShadedBigInteger shadedBigInteger4 = mutableBigInt4.toBigInteger(n5);
            strings[n3++] = Long.toString(shadedBigInteger4.longValue(), n);
            shadedBigInteger = shadedBigInteger3;
         }

         StringBuilder sb = new StringBuilder(n3 * digitsPerLong[n] + 1);
         if (this.signum < 0) {
            sb.append('-');
         }

         sb.append(strings[n3 - 1]);

         for (int i = n3 - 2; i >= 0; i--) {
            int n6 = digitsPerLong[n] - strings[i].length();
            if (n6 != 0) {
               sb.append(zeros[n6]);
            }

            sb.append(strings[i]);
         }

         return sb.toString();
      }
   }

   public ShadedBigInteger pow(int n) {
      if (n < 0) {
         throw new ArithmeticException("Negative exponent");
      } else if (this.signum == 0) {
         return n == 0 ? ONE : this;
      } else {
         ShadedBigInteger shadedBigInteger = this.abs();
         int lowestSetBit2 = shadedBigInteger.getLowestSetBit();
         long l = (long)lowestSetBit2 * (long)n;
         if (l > 2147483647L) {
            reportOverflow();
         }

         int n2 = (int)l;
         int n3;
         if (lowestSetBit2 > 0) {
            shadedBigInteger = shadedBigInteger.shiftRight(lowestSetBit2);
            n3 = shadedBigInteger.bitLength();
            if (n3 == 1) {
               if (this.signum < 0 && (n & 1) == 1) {
                  return NEGATIVE_ONE.shiftLeft(n2);
               }

               return ONE.shiftLeft(n2);
            }
         } else {
            n3 = shadedBigInteger.bitLength();
            if (n3 == 1) {
               if (this.signum < 0 && (n & 1) == 1) {
                  return NEGATIVE_ONE;
               }

               return ONE;
            }
         }

         long l2 = (long)n3 * (long)n;
         if (shadedBigInteger.mag.length == 1 && l2 <= 62L) {
            int n4 = this.signum < 0 && (n & 1) == 1 ? -1 : 1;
            long l3 = 1L;
            long l4 = (long)shadedBigInteger.mag[0] & 4294967295L;
            int n5 = n;

            while (n5 != 0) {
               if ((n5 & 1) == 1) {
                  l3 *= l4;
               }

               if ((n5 >>>= 1) != 0) {
                  l4 *= l4;
               }
            }

            if (lowestSetBit2 <= 0) {
               return valueOf(l3 * (long)n4);
            } else {
               return (long)n2 + l2 <= 62L ? valueOf((l3 << n2) * (long)n4) : valueOf(l3 * (long)n4).shiftLeft(n2);
            }
         } else {
            if ((long)this.bitLength() * (long)n / 32L > 67108864L) {
               reportOverflow();
            }

            ShadedBigInteger shadedBigInteger2 = ONE;
            int n6 = n;

            while (n6 != 0) {
               if ((n6 & 1) == 1) {
                  shadedBigInteger2 = shadedBigInteger2.multiply(shadedBigInteger);
               }

               if ((n6 >>>= 1) != 0) {
                  shadedBigInteger = shadedBigInteger.square();
               }
            }

            if (lowestSetBit2 > 0) {
               shadedBigInteger2 = shadedBigInteger2.shiftLeft(n2);
            }

            return this.signum < 0 && (n & 1) == 1 ? shadedBigInteger2.negate() : shadedBigInteger2;
         }
      }
   }

   public static void primitiveRightShift(int[] ints, int n, int n2) {
      int n3 = 32 - n2;
      int n4 = n - 1;
      ShadedNumber.getFlowState();

      for (int i = ints[n4]; n4 > 0; n4--) {
         int n5 = i;
         i = ints[n4 - 1];
         ints[n4] = i << n3 | n5 >>> n2;
      }

      ints[0] >>>= n2;
   }

   public ShadedBigInteger(int n, byte[] bytes) {
      super();
      ShadedNumber.getFlowState();
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

   private ShadedBigInteger remainderBurnikelZiegler(ShadedBigInteger shadedBigInteger) {
      return this.divideAndRemainderBurnikelZiegler(shadedBigInteger)[1];
   }

   private static int[] subtract3(long l, int[] ints) {
      int[] flowState2 = ShadedNumber.getFlowState();
      int n = (int)(l >>> 32);
      if (n == 0) {
         return new int[]{(int)(l - ((long)ints[0] & 4294967295L))};
      } else {
         int[] ints2 = new int[2];
         if (ints.length == 1) {
            long l2 = ((long)((int)l) & 4294967295L) - ((long)ints[0] & 4294967295L);
            ints2[1] = (int)l2;
            boolean flag = l2 >> 32 != 0L;
            if (flag) {
               ints2[0] = n - 1;
            } else {
               ints2[0] = n;
            }

            return ints2;
         } else {
            long l3 = ((long)((int)l) & 4294967295L) - ((long)ints[1] & 4294967295L);
            ints2[1] = (int)l3;
            l3 = ((long)n & 4294967295L) - ((long)ints[0] & 4294967295L) + (l3 >> 32);
            ints2[0] = (int)l3;
            return ints2;
         }
      }
   }

   public ShadedBigInteger gcd(ShadedBigInteger shadedBigInteger) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (shadedBigInteger.signum == 0) {
         return this.abs();
      } else if (this.signum == 0) {
         return shadedBigInteger.abs();
      } else {
         MutableBigInt mutableBigInt = new MutableBigInt(this);
         MutableBigInt mutableBigInt2 = new MutableBigInt(shadedBigInteger);
         MutableBigInt mutableBigInt3 = mutableBigInt.hybridGCD(mutableBigInt2);
         byte b = 1;
         return mutableBigInt3.toBigInteger(b);
      }
   }

   public ShadedBigInteger add2(long l) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (l == 0L) {
         return this;
      } else if (this.signum == 0) {
         return valueOf(l);
      } else if (Long.signum(l) == this.signum) {
         int[] mag2 = this.mag;
         long l2 = Math.abs(l);
         return new ShadedBigInteger(add(mag2, l2), this.signum);
      } else {
         int n = this.compareMagnitude2(l);
         if (n == 0) {
            return ZERO;
         } else {
            int[] ints;
            if (n > 0) {
               int[] mag3 = this.mag;
               long l3 = Math.abs(l);
               ints = subtract2(mag3, l3);
            } else {
               ints = subtract3(Math.abs(l), this.mag);
            }

            int[] ints2 = ints;
            ints2 = trustedStripLeadingZeroInts(ints2);
            return new ShadedBigInteger(ints2, n == this.signum ? 1 : -1);
         }
      }
   }

   public ShadedBigInteger abs() {
      return this.signum >= 0 ? this : this.negate();
   }

   private static int[] materialize(int[] ints, int n) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (ints == null || ints.length < n) {
         ints = new int[n];
      }

      return ints;
   }

   public ShadedBigInteger shiftLeft(int n) {
      if (this.signum == 0) {
         return ZERO;
      } else if (n > 0) {
         return new ShadedBigInteger(shiftLeft(this.mag, n), this.signum);
      } else if (n == 0) {
         return this;
      } else {
         int n2 = -n;
         return this.shiftRightImpl(n2);
      }
   }

   private ShadedBigInteger square(boolean flag) {
      if (this.signum == 0) {
         return ZERO;
      } else {
         int length = this.mag.length;
         if (length < 128) {
            Object obj = null;
            int[] ints = squareToLen(this.mag, length, (int[])obj);
            return new ShadedBigInteger(trustedStripLeadingZeroInts(ints), 1);
         } else if (length < 216) {
            return this.squareKaratsuba();
         } else {
            if (!flag) {
               int length2 = this.mag.length;
               if ((long)bitLength(this.mag, length2) > 1073741824L) {
                  reportOverflow();
               }
            }

            return this.squareToomCook3();
         }
      }
   }

   private static int[] multiplyToLen(int[] ints, int n, int[] ints2, int n2, int[] ints3) {
      int n3 = n - 1;
      int n4 = n2 - 1;
      if (ints3 == null || ints3.length < n + n2) {
         ints3 = new int[n + n2];
      }

      long l = 0L;
      int n5 = n4;

      for (int i = n4 + 1 + n3; n5 >= 0; i--) {
         long l2 = ((long)ints2[n5] & 4294967295L) * ((long)ints[n3] & 4294967295L) + l;
         ints3[i] = (int)l2;
         l = l2 >>> 32;
         n5--;
      }

      ints3[n3] = (int)l;

      for (int j = n3 - 1; j >= 0; j--) {
         l = 0L;
         int n6 = n4;

         for (int k = n4 + 1 + j; n6 >= 0; k--) {
            long l3 = ((long)ints2[n6] & 4294967295L) * ((long)ints[j] & 4294967295L) + ((long)ints3[k] & 4294967295L) + l;
            ints3[k] = (int)l3;
            l = l3 >>> 32;
            n6--;
         }

         ints3[j] = (int)l;
      }

      return ints3;
   }

   public ShadedBigInteger shiftRight(int n) {
      if (this.signum == 0) {
         return ZERO;
      } else if (n > 0) {
         return this.shiftRightImpl(n);
      } else if (n == 0) {
         return this;
      } else {
         int n2 = -n;
         return new ShadedBigInteger(shiftLeft(this.mag, n2), this.signum);
      }
   }

   public int getLowestSetBit() {
      int n = this.lowestSetBit - 2;
      if (n == -2) {
         n = 0;
         if (this.signum == 0) {
            n--;
         } else {
            int n2 = 0;

            int n3;
            while ((n3 = this.getInt(n2)) == 0) {
               n2++;
            }

            n += (n2 << 5) + Integer.numberOfTrailingZeros(n3);
         }

         int n4 = n + 2;
         this.lowestSetBit = n4;
      }

      return n;
   }

   public static int bitLengthForInt(int n) {
      return 32 - Integer.numberOfLeadingZeros(n);
   }

   @Override
   public long longValue() {
      long l = 0L;
      ShadedNumber.getFlowState();

      for (int i = 1; i >= 0; i--) {
         l = (l << 32) + ((long)this.getInt(i) & 4294967295L);
      }

      return l;
   }

   private static int[] shiftLeft(int[] ints, int n) {
      int n2 = n >>> 5;
      int n3 = n & 31;
      int length = ints.length;
      int[] ints2 = null;
      if (n3 == 0) {
         ints2 = new int[length + n2];
         System.arraycopy(ints, 0, ints2, 0, length);
      } else {
         int n4 = 0;
         int n5 = 32 - n3;
         int n6 = ints[0] >>> n5;
         if (n6 != 0) {
            ints2 = new int[length + n2 + 1];
            ints2[n4++] = n6;
         } else {
            ints2 = new int[length + n2];
         }

         int n7 = 0;

         while (n7 < length - 1) {
            ints2[n4++] = ints[n7++] << n3 | ints[n7] >>> n5;
         }

         ints2[n4] = ints[n7] << n3;
      }

      return (int[])ints2;
   }

   private static int[] subtract2(int[] ints, long l) {
      int n = (int)(l >>> 32);
      ShadedNumber.getFlowState();
      int length = ints.length;
      int[] ints2 = new int[length];
      long l2 = 0L;
      if (n == 0) {
         length--;
         l2 = ((long)ints[length] & 4294967295L) - l;
         ints2[length] = (int)l2;
      } else {
         length--;
         long l3 = ((long)ints[length] & 4294967295L) - (l & 4294967295L);
         ints2[length] = (int)l3;
         length--;
         l2 = ((long)ints[length] & 4294967295L) - ((long)n & 4294967295L) + (l3 >> 32);
         ints2[length] = (int)l2;
      }

      for (boolean i = l2 >> 32 != 0L; length > 0 && i; i = (ints2[length] = ints[length] - 1) == -1) {
         length--;
      }

      while (length > 0) {
         ints2[length] = ints[--length];
      }

      return ints2;
   }

   public int getSignum() {
      return this.signum;
   }

   private static ShadedBigInteger multiplyToomCook3(ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2) {
      int length = shadedBigInteger.mag.length;
      int length2 = shadedBigInteger2.mag.length;
      int n = Math.max(length, length2);
      int n2 = (n + 2) / 3;
      int n3 = n - 2 * n2;
      byte b = 0;
      ShadedBigInteger toomSlice = shadedBigInteger.getToomSlice(n2, n3, b, n);
      byte b2 = 1;
      ShadedBigInteger toomSlice2 = shadedBigInteger.getToomSlice(n2, n3, b2, n);
      byte b3 = 2;
      ShadedBigInteger toomSlice3 = shadedBigInteger.getToomSlice(n2, n3, b3, n);
      byte b4 = 0;
      ShadedBigInteger toomSlice4 = shadedBigInteger2.getToomSlice(n2, n3, b4, n);
      byte b5 = 1;
      ShadedBigInteger toomSlice5 = shadedBigInteger2.getToomSlice(n2, n3, b5, n);
      byte b6 = 2;
      ShadedBigInteger toomSlice6 = shadedBigInteger2.getToomSlice(n2, n3, b6, n);
      boolean flag = true;
      ShadedBigInteger shadedBigInteger3 = toomSlice3.multiply(toomSlice6, flag);
      ShadedBigInteger shadedBigInteger4 = toomSlice.add(toomSlice3);
      ShadedBigInteger shadedBigInteger5 = toomSlice4.add(toomSlice6);
      ShadedBigInteger shadedBigInteger6 = shadedBigInteger4.subtract(toomSlice2);
      ShadedBigInteger shadedBigInteger7 = shadedBigInteger5.subtract(toomSlice5);
      boolean flag2 = true;
      ShadedBigInteger shadedBigInteger8 = shadedBigInteger7;
      ShadedBigInteger shadedBigInteger9 = shadedBigInteger6.multiply(shadedBigInteger8, flag2);
      shadedBigInteger4 = shadedBigInteger4.add(toomSlice2);
      shadedBigInteger5 = shadedBigInteger5.add(toomSlice5);
      boolean flag3 = true;
      ShadedBigInteger shadedBigInteger10 = shadedBigInteger4.multiply(shadedBigInteger5, flag3);
      shadedBigInteger6 = shadedBigInteger4.add(toomSlice);
      byte b7 = 1;
      shadedBigInteger6 = shadedBigInteger6.shiftLeft(b7).subtract(toomSlice3);
      ShadedBigInteger shadedBigInteger11 = shadedBigInteger5.add(toomSlice4);
      byte b8 = 1;
      ShadedBigInteger shadedBigInteger12 = shadedBigInteger11.shiftLeft(b8).subtract(toomSlice6);
      boolean flag4 = true;
      ShadedBigInteger shadedBigInteger13 = shadedBigInteger12;
      ShadedBigInteger shadedBigInteger14 = shadedBigInteger6.multiply(shadedBigInteger13, flag4);
      boolean flag5 = true;
      ShadedBigInteger shadedBigInteger15 = toomSlice.multiply(toomSlice4, flag5);
      ShadedBigInteger shadedBigInteger16 = shadedBigInteger14.subtract(shadedBigInteger9).exactDivideBy3();
      shadedBigInteger6 = shadedBigInteger10.subtract(shadedBigInteger9);
      byte b9 = 1;
      ShadedBigInteger shadedBigInteger17 = shadedBigInteger6.shiftRight((int)b9);
      ShadedBigInteger shadedBigInteger18 = shadedBigInteger10.subtract(shadedBigInteger3);
      shadedBigInteger6 = shadedBigInteger16.subtract(shadedBigInteger18);
      byte b10 = 1;
      shadedBigInteger16 = shadedBigInteger6.shiftRight((int)b10);
      shadedBigInteger18 = shadedBigInteger18.subtract(shadedBigInteger17).subtract(shadedBigInteger15);
      byte b11 = 1;
      ShadedBigInteger shadedBigInteger19 = shadedBigInteger15.shiftLeft(b11);
      shadedBigInteger16 = shadedBigInteger16.subtract(shadedBigInteger19);
      shadedBigInteger17 = shadedBigInteger17.subtract(shadedBigInteger16);
      int n4 = n2 * 32;
      shadedBigInteger6 = shadedBigInteger15.shiftLeft(n4);
      shadedBigInteger6 = shadedBigInteger6.add(shadedBigInteger16).shiftLeft(n4);
      shadedBigInteger6 = shadedBigInteger6.add(shadedBigInteger18).shiftLeft(n4);
      shadedBigInteger6 = shadedBigInteger6.add(shadedBigInteger17).shiftLeft(n4);
      ShadedBigInteger shadedBigInteger20 = shadedBigInteger6.add(shadedBigInteger3);
      return shadedBigInteger.signum != shadedBigInteger2.signum ? shadedBigInteger20.negate() : shadedBigInteger20;
   }

   @Override
   public int compareTo(Object other) {
      ShadedBigInteger shadedBigInteger = (ShadedBigInteger)other;
      return this.compareTo2(shadedBigInteger);
   }

   public ShadedBigInteger mod(ShadedBigInteger shadedBigInteger) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (shadedBigInteger.signum <= 0) {
         throw new ArithmeticException("BigInteger: modulus not positive");
      } else {
         ShadedBigInteger shadedBigInteger2 = this.remainder(shadedBigInteger);
         ShadedBigInteger shadedBigInteger3;
         if (shadedBigInteger2.signum >= 0) {
            shadedBigInteger3 = shadedBigInteger2;
         } else {
            shadedBigInteger3 = shadedBigInteger2.add(shadedBigInteger);
         }

         return shadedBigInteger3;
      }
   }

   public String toString(int n) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (this.signum == 0) {
         return "0";
      } else {
         if (n < 2 || n > 36) {
            n = 10;
         }

         if (this.mag.length <= 20) {
            return this.smallToString(n);
         } else {
            StringBuilder sb = new StringBuilder();
            if (this.signum < 0) {
               ShadedBigInteger shadedBigInteger = this.negate();
               byte b = 0;
               toString(shadedBigInteger, sb, n, b);
               sb.insert(0, '-');
            } else {
               byte b2 = 0;
               toString(this, sb, n, b2);
            }

            return sb.toString();
         }
      }
   }

   public ShadedBigInteger modPow(ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (shadedBigInteger2.signum <= 0) {
         throw new ArithmeticException("BigInteger: modulus not positive");
      } else if (shadedBigInteger.signum == 0) {
         ShadedBigInteger shadedBigInteger3 = ONE;
         return shadedBigInteger2.equals(shadedBigInteger3) ? ZERO : ONE;
      } else {
         ShadedBigInteger shadedBigInteger4 = ONE;
         if (this.equals(shadedBigInteger4)) {
            ShadedBigInteger shadedBigInteger5 = ONE;
            return shadedBigInteger2.equals(shadedBigInteger5) ? ZERO : ONE;
         } else {
            ShadedBigInteger shadedBigInteger6 = ZERO;
            if (this.equals(shadedBigInteger6) && shadedBigInteger.signum >= 0) {
               return ZERO;
            } else {
               ShadedBigInteger shadedBigInteger7 = negConst[1];
               if (this.equals(shadedBigInteger7)) {
                  byte b = 0;
                  if (!shadedBigInteger.testBit(b)) {
                     ShadedBigInteger shadedBigInteger8 = ONE;
                     return shadedBigInteger2.equals(shadedBigInteger8) ? ZERO : ONE;
                  }
               }

               boolean flag;
               if (flag = shadedBigInteger.signum < 0) {
                  shadedBigInteger = shadedBigInteger.negate();
               }

               ShadedBigInteger shadedBigInteger9;
               exitBlock: {
                  if (this.signum >= 0) {
                     if (this.compareTo2(shadedBigInteger2) < 0) {
                        shadedBigInteger9 = this;
                        break exitBlock;
                     }
                  }

                  shadedBigInteger9 = this.mod(shadedBigInteger2);
               }

               ShadedBigInteger shadedBigInteger10 = shadedBigInteger9;
               byte b2 = 0;
               ShadedBigInteger shadedBigInteger11;
               if (shadedBigInteger2.testBit(b2)) {
                  shadedBigInteger11 = shadedBigInteger10.oddModPow(shadedBigInteger, shadedBigInteger2);
               } else {
                  int n;
                  ShadedBigInteger shadedBigInteger12;
                  ShadedBigInteger shadedBigInteger13;
                  exitBlock2: {
                     n = shadedBigInteger2.getLowestSetBit();
                     shadedBigInteger12 = shadedBigInteger2.shiftRight(n);
                     shadedBigInteger13 = ONE.shiftLeft(n);
                     if (this.signum >= 0) {
                        if (this.compareTo2(shadedBigInteger12) < 0) {
                           shadedBigInteger9 = this;
                           break exitBlock2;
                        }
                     }

                     shadedBigInteger9 = this.mod(shadedBigInteger12);
                  }

                  ShadedBigInteger shadedBigInteger14 = shadedBigInteger9;
                  ShadedBigInteger shadedBigInteger15 = ONE;
                  ShadedBigInteger shadedBigInteger16 = shadedBigInteger12.equals(shadedBigInteger15) ? ZERO : shadedBigInteger14.oddModPow(shadedBigInteger, shadedBigInteger12);
                  ShadedBigInteger shadedBigInteger17 = shadedBigInteger10.modPow2(shadedBigInteger, n);
                  ShadedBigInteger shadedBigInteger18 = shadedBigInteger13.modInverse(shadedBigInteger12);
                  ShadedBigInteger shadedBigInteger19 = shadedBigInteger12.modInverse(shadedBigInteger13);
                  if (shadedBigInteger2.mag.length < 33554432) {
                     shadedBigInteger9 = shadedBigInteger16.multiply(shadedBigInteger13).multiply(shadedBigInteger18);
                     ShadedBigInteger shadedBigInteger20 = shadedBigInteger17.multiply(shadedBigInteger12).multiply(shadedBigInteger19);
                     ShadedBigInteger shadedBigInteger21 = shadedBigInteger20;
                     shadedBigInteger9 = shadedBigInteger9.add(shadedBigInteger21);
                     shadedBigInteger11 = shadedBigInteger9.mod(shadedBigInteger2);
                  } else {
                     MutableBigInt mutableBigInt = new MutableBigInt();
                     MutableBigInt mutableBigInt2 = new MutableBigInt(shadedBigInteger16.multiply(shadedBigInteger13));
                     MutableBigInt mutableBigInt3 = new MutableBigInt(shadedBigInteger18);
                     mutableBigInt2.multiply(mutableBigInt3, mutableBigInt);
                     MutableBigInt mutableBigInt4 = new MutableBigInt();
                     MutableBigInt mutableBigInt5 = new MutableBigInt(shadedBigInteger17.multiply(shadedBigInteger12));
                     MutableBigInt mutableBigInt6 = new MutableBigInt(shadedBigInteger19);
                     mutableBigInt5.multiply(mutableBigInt6, mutableBigInt4);
                     mutableBigInt.add(mutableBigInt4);
                     MutableBigInt mutableBigInt7 = new MutableBigInt();
                     MutableBigInt mutableBigInt8 = new MutableBigInt(shadedBigInteger2);
                     shadedBigInteger11 = mutableBigInt.divide2(mutableBigInt8, mutableBigInt7).toBigInteger();
                  }
               }

               if (flag) {
                  shadedBigInteger9 = shadedBigInteger11.modInverse(shadedBigInteger2);
               } else {
                  shadedBigInteger9 = shadedBigInteger11;
               }

               return shadedBigInteger9;
            }
         }
      }
   }

   public final int compareMagnitude(ShadedBigInteger shadedBigInteger) {
      int[] mag2 = this.mag;
      int length = mag2.length;
      int[] mag3 = shadedBigInteger.mag;
      int length2 = mag3.length;
      if (length < length2) {
         return -1;
      } else if (length > length2) {
         return 1;
      } else {
         for (int i = 0; i < length; i++) {
            int n = mag2[i];
            int n2 = mag3[i];
            if (n != n2) {
               return ((long)n & 4294967295L) < ((long)n2 & 4294967295L) ? -1 : 1;
            }
         }

         return 0;
      }
   }

   private int parseInt(char[] chars, int n, int n2) {
      int n3 = Character.digit(chars[n++], 10);
      if (n3 == -1) {
         throw new NumberFormatException(new String(chars));
      } else {
         for (int i = n; i < n2; i++) {
            int n4 = Character.digit(chars[i], 10);
            if (n4 == -1) {
               throw new NumberFormatException(new String(chars));
            }

            n3 = 10 * n3 + n4;
         }

         return n3;
      }
   }

   public ShadedBigInteger modInverse(ShadedBigInteger shadedBigInteger) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (shadedBigInteger.signum != 1) {
         throw new ArithmeticException("BigInteger: modulus not positive");
      } else {
         ShadedBigInteger shadedBigInteger2 = ONE;
         if (shadedBigInteger.equals(shadedBigInteger2)) {
            return ZERO;
         } else {
            ShadedBigInteger shadedBigInteger3 = this;
            if (this.signum < 0 || this.compareMagnitude(shadedBigInteger) >= 0) {
               shadedBigInteger3 = this.mod(shadedBigInteger);
            }

            ShadedBigInteger shadedBigInteger4 = ONE;
            if (shadedBigInteger3.equals(shadedBigInteger4)) {
               return ONE;
            } else {
               MutableBigInt mutableBigInt = new MutableBigInt(shadedBigInteger3);
               MutableBigInt mutableBigInt2 = new MutableBigInt(shadedBigInteger);
               MutableBigInt mutableBigInt3 = mutableBigInt.mutableModInverse(mutableBigInt2);
               byte b = 1;
               return mutableBigInt3.toBigInteger(b);
            }
         }
      }
   }

   @Override
   public int intValue() {
      boolean flag = false;
      byte b = 0;
      return this.getInt(b);
   }

   private ShadedBigInteger getUpper(int n) {
      int length = this.mag.length;
      if (length <= n) {
         return ZERO;
      } else {
         int n2 = length - n;
         int[] ints = new int[n2];
         System.arraycopy(this.mag, 0, ints, 0, n2);
         return new ShadedBigInteger(trustedStripLeadingZeroInts(ints), 1);
      }
   }

   private ShadedBigInteger mod2(int n) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (this.bitLength() <= n) {
         return this;
      } else {
         int n2 = n + 31 >>> 5;
         int[] ints = new int[n2];
         System.arraycopy(this.mag, this.mag.length - n2, ints, 0, n2);
         int n3 = (n2 << 5) - n;
         ints[0] = (int)((long)ints[0] & (1L << 32 - n3) - 1L);
         return ints[0] == 0 ? new ShadedBigInteger(1, ints) : new ShadedBigInteger(ints, 1);
      }
   }

   private static ShadedBigInteger multiplyByInt(int[] ints, int n, int n2) {
      if (Integer.bitCount(n) == 1) {
         int n3 = Integer.numberOfTrailingZeros(n);
         return new ShadedBigInteger(shiftLeft(ints, n3), n2);
      } else {
         int length = ints.length;
         int[] ints2 = new int[length + 1];
         long l = 0L;
         long l2 = (long)n & 4294967295L;
         int n4 = ints2.length - 1;

         for (int i = length - 1; i >= 0; i--) {
            long l3 = ((long)ints[i] & 4294967295L) * l2 + l;
            ints2[n4--] = (int)l3;
            l = l3 >>> 32;
         }

         if (l == 0L) {
            ints2 = Arrays.copyOfRange(ints2, 1, ints2.length);
         } else {
            ints2[n4] = (int)l;
         }

         return new ShadedBigInteger(ints2, n2);
      }
   }

   public ShadedBigInteger(char[] chars, int n, int n2) {
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
         int n9 = n8 = n3 + n6;
         ints[n7] = this.parseInt(chars, n3, n9);

         while (n8 < n2) {
            int n10 = n8;
            int n11 = n8 += digitsPerInt[10];
            int n12 = n10;
            int n13 = this.parseInt(chars, n12, n11);
            int n14 = intRadix[10];
            destructiveMulAdd(ints, n14, n13);
         }

         this.mag = trustedStripLeadingZeroInts(ints);
         if (this.mag.length >= 67108864) {
            this.checkRange();
         }
      }
   }

   public ShadedBigInteger nextProbablePrime() {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (this.signum < 0) {
         throw new ArithmeticException("start < 0: " + this);
      } else {
         if (this.signum != 0) {
            ShadedBigInteger shadedBigInteger = ONE;
            if (!this.equals(shadedBigInteger)) {
               ShadedBigInteger shadedBigInteger2 = ONE;
               ShadedBigInteger shadedBigInteger3 = this.add(shadedBigInteger2);
               if (shadedBigInteger3.bitLength() < 95) {
                  byte b = 0;
                  if (!shadedBigInteger3.testBit(b)) {
                     ShadedBigInteger shadedBigInteger4 = ONE;
                     shadedBigInteger3 = shadedBigInteger3.add(shadedBigInteger4);
                  }

                  while (true) {
                     if (shadedBigInteger3.bitLength() > 6) {
                        ShadedBigInteger shadedBigInteger5 = SMALL_PRIME_PRODUCT;
                        long l = shadedBigInteger3.remainder(shadedBigInteger5).longValue();
                        if (l % 3L == 0L
                           || l % 5L == 0L
                           || l % 7L == 0L
                           || l % 11L == 0L
                           || l % 13L == 0L
                           || l % 17L == 0L
                           || l % 19L == 0L
                           || l % 23L == 0L
                           || l % 29L == 0L
                           || l % 31L == 0L
                           || l % 37L == 0L
                           || l % 41L == 0L) {
                           ShadedBigInteger shadedBigInteger6 = TWO;
                           shadedBigInteger3 = shadedBigInteger3.add(shadedBigInteger6);
                           continue;
                        }
                     }

                     if (shadedBigInteger3.bitLength() < 4) {
                        return shadedBigInteger3;
                     }

                     Object obj = null;
                     byte b2 = 100;
                     if (shadedBigInteger3.primeToCertainty(b2, (Random)obj)) {
                        return shadedBigInteger3;
                     }

                     ShadedBigInteger shadedBigInteger7 = TWO;
                     shadedBigInteger3 = shadedBigInteger3.add(shadedBigInteger7);
                  }
               }

               byte b3 = 0;
               if (shadedBigInteger3.testBit(b3)) {
                  ShadedBigInteger shadedBigInteger8 = ONE;
                  shadedBigInteger3 = shadedBigInteger3.subtract(shadedBigInteger8);
               }

               int primeSearchLen = getPrimeSearchLen(shadedBigInteger3.bitLength());

               while (true) {
                  PrimeBitSieve primeBitSieve = new PrimeBitSieve(shadedBigInteger3, primeSearchLen);
                  Object obj2 = null;
                  byte b4 = 100;
                  ShadedBigInteger shadedBigInteger9 = primeBitSieve.retrieve(shadedBigInteger3, b4, (Random)obj2);
                  if (shadedBigInteger9 != null) {
                     return shadedBigInteger9;
                  }

                  ShadedBigInteger shadedBigInteger10 = valueOf((long)(2 * primeSearchLen));
                  ShadedBigInteger shadedBigInteger11 = shadedBigInteger10;
                  shadedBigInteger3 = shadedBigInteger3.add(shadedBigInteger11);
               }
            }
         }

         return TWO;
      }
   }

   public ShadedBigInteger setBit(int n) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (n < 0) {
         throw new ArithmeticException("Negative bit address");
      } else {
         int n2 = n >>> 5;
         int[] ints = new int[Math.max(this.intLength(), n2 + 2)];

         for (int i = 0; i < ints.length; i++) {
            ints[ints.length - i - 1] = this.getInt(i);
         }

         ints[ints.length - n2 - 1] = ints[ints.length - n2 - 1] | 1 << (n & 31);
         return valueOf2(ints);
      }
   }

   private static final int[] implSquareToLen(int[] ints, int n, int[] ints2, int n2) {
      int n3 = 0;
      int n4 = 0;

      for (int i = 0; n4 < n; n4++) {
         long l = (long)ints[n4] & 4294967295L;
         long l2 = l * l;
         ints2[i++] = n3 << 31 | (int)(l2 >>> 33);
         ints2[i++] = (int)(l2 >>> 1);
         n3 = (int)l2;
      }

      n4 = n;

      for (int j = 1; n4 > 0; j += 2) {
         int n5 = ints[n4 - 1];
         int n6 = n4 - 1;
         n5 = mulAdd(ints2, ints, j, n6, n5);
         int n7 = j - 1;
         addOne(ints2, n7, n4, n5);
         n4--;
      }

      byte b = 1;
      primitiveLeftShift(ints2, n2, b);
      ints2[n2 - 1] = ints2[n2 - 1] | ints[n - 1] & 1;
      return ints2;
   }

   private static int[] montReduce(int[] ints, int[] ints2, int n, int n2) {
      int n3 = 0;
      ShadedNumber.getFlowState();
      int n4 = n;
      int n5 = 0;

      do {
         int n6 = ints[ints.length - 1 - n5];
         int n7 = n2 * n6;
         int n8 = mulAdd(ints, ints2, n5, n, n7);
         n3 += addOne(ints, n5, n, n8);
         n5++;
      } while (--n4 > 0);

      while (n3 > 0) {
         n3 += subN(ints, ints2, n);
      }

      while (intArrayCmpToLen(ints, ints2, n) >= 0) {
         subN(ints, ints2, n);
      }

      return ints;
   }

   private static int[] implMontgomeryMultiply(int[] ints, int[] ints2, int[] ints3, int n, long l, int[] ints4) {
      ints4 = multiplyToLen(ints, n, ints2, n, ints4);
      int n2 = (int)l;
      return montReduce(ints4, ints3, n, n2);
   }

   public int[] javaIncrement(int[] ints) {
      int n = 0;

      for (int i = ints.length - 1; i >= 0 && n == 0; i--) {
         n = ++ints[i];
      }

      if (n == 0) {
         ints = new int[ints.length + 1];
         ints[0] = 1;
      }

      return ints;
   }

   private int signBit(long l) {
      int[] flowState2 = ShadedNumber.getFlowState();
      return this.signum < 0 ? 1 : 0;
   }

   public int compareTo2(ShadedBigInteger shadedBigInteger) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (this.signum == shadedBigInteger.signum) {
         switch (this.signum) {
            case -1:
               return shadedBigInteger.compareMagnitude(this);
            case 1:
               return this.compareMagnitude(shadedBigInteger);
            default:
               return 0;
         }
      } else {
         return this.signum > shadedBigInteger.signum ? 1 : -1;
      }
   }

   private int firstNonzeroIntNum() {
      int n = this.firstNonzeroIntNum - 2;
      if (n == -2) {
         boolean flag = false;
         int length = this.mag.length;
         int n2 = length - 1;

         while (n2 >= 0 && this.mag[n2] == 0) {
            n2--;
         }

         n = length - n2 - 1;
         int n3 = n + 2;
         this.firstNonzeroIntNum = n3;
      }

      return n;
   }

   public ShadedBigInteger negate() {
      return new ShadedBigInteger(this.mag, -this.signum);
   }

   private static int jacobiSymbol(int n, ShadedBigInteger shadedBigInteger) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (n == 0) {
         return 0;
      } else {
         int n2 = 1;
         int n3 = shadedBigInteger.mag[shadedBigInteger.mag.length - 1];
         if (n < 0) {
            n = -n;
            int n4 = n3 & 7;
            if (n4 == 3 || n4 == 7) {
               n2 = -n2;
            }
         }

         while ((n & 3) == 0) {
            n >>= 2;
         }

         if ((n & 1) == 0) {
            n >>= 1;
            if (((n3 ^ n3 >> 1) & 2) != 0) {
               n2 = -n2;
            }
         }

         if (n == 1) {
            return n2;
         } else {
            if ((n & n3 & 2) != 0) {
               n2 = -n2;
            }

            ShadedBigInteger shadedBigInteger2 = valueOf((long)n);
            ShadedBigInteger shadedBigInteger3 = shadedBigInteger2;
            n3 = shadedBigInteger.mod(shadedBigInteger3).intValue();

            while (n3 != 0) {
               while ((n3 & 3) == 0) {
                  n3 >>= 2;
               }

               if ((n3 & 1) == 0) {
                  n3 >>= 1;
                  if (((n ^ n >> 1) & 2) != 0) {
                     n2 = -n2;
                  }
               }

               if (n3 == 1) {
                  return n2;
               }

               if (!assertionsDisabled && n3 >= n) {
                  throw new AssertionError();
               }

               int n5 = n;
               n = n3;
               if ((n5 & n3 & 2) != 0) {
                  n2 = -n2;
               }

               n3 = n5 % n3;
            }

            return 0;
         }
      }
   }

   private static int[] stripLeadingZeroInts(int[] ints) {
      int length = ints.length;
      ShadedNumber.getFlowState();
      int n = 0;

      while (n < length && ints[n] == 0) {
         n++;
      }

      return Arrays.copyOfRange(ints, n, length);
   }

   private ShadedBigInteger squareToomCook3() {
      int length = this.mag.length;
      int n = (length + 2) / 3;
      int n2 = length - 2 * n;
      byte b = 0;
      ShadedBigInteger toomSlice = this.getToomSlice(n, n2, b, length);
      byte b2 = 1;
      ShadedBigInteger toomSlice2 = this.getToomSlice(n, n2, b2, length);
      byte b3 = 2;
      ShadedBigInteger toomSlice3 = this.getToomSlice(n, n2, b3, length);
      boolean flag = true;
      ShadedBigInteger shadedBigInteger = toomSlice3.square(flag);
      ShadedBigInteger shadedBigInteger2 = toomSlice.add(toomSlice3);
      ShadedBigInteger shadedBigInteger3 = shadedBigInteger2.subtract(toomSlice2);
      boolean flag2 = true;
      ShadedBigInteger shadedBigInteger4 = shadedBigInteger3.square(flag2);
      shadedBigInteger2 = shadedBigInteger2.add(toomSlice2);
      boolean flag3 = true;
      ShadedBigInteger shadedBigInteger5 = shadedBigInteger2.square(flag3);
      boolean flag4 = true;
      ShadedBigInteger shadedBigInteger6 = toomSlice.square(flag4);
      shadedBigInteger3 = shadedBigInteger2.add(toomSlice);
      byte b4 = 1;
      shadedBigInteger3 = shadedBigInteger3.shiftLeft(b4).subtract(toomSlice3);
      boolean flag5 = true;
      ShadedBigInteger shadedBigInteger7 = shadedBigInteger3.square(flag5);
      ShadedBigInteger shadedBigInteger8 = shadedBigInteger7.subtract(shadedBigInteger4).exactDivideBy3();
      shadedBigInteger3 = shadedBigInteger5.subtract(shadedBigInteger4);
      byte b5 = 1;
      ShadedBigInteger shadedBigInteger9 = shadedBigInteger3.shiftRight((int)b5);
      ShadedBigInteger shadedBigInteger10 = shadedBigInteger5.subtract(shadedBigInteger);
      shadedBigInteger3 = shadedBigInteger8.subtract(shadedBigInteger10);
      byte b6 = 1;
      shadedBigInteger8 = shadedBigInteger3.shiftRight((int)b6);
      shadedBigInteger10 = shadedBigInteger10.subtract(shadedBigInteger9).subtract(shadedBigInteger6);
      byte b7 = 1;
      ShadedBigInteger shadedBigInteger11 = shadedBigInteger6.shiftLeft(b7);
      shadedBigInteger8 = shadedBigInteger8.subtract(shadedBigInteger11);
      shadedBigInteger9 = shadedBigInteger9.subtract(shadedBigInteger8);
      int n3 = n * 32;
      shadedBigInteger3 = shadedBigInteger6.shiftLeft(n3);
      shadedBigInteger3 = shadedBigInteger3.add(shadedBigInteger8).shiftLeft(n3);
      shadedBigInteger3 = shadedBigInteger3.add(shadedBigInteger10).shiftLeft(n3);
      shadedBigInteger3 = shadedBigInteger3.add(shadedBigInteger9).shiftLeft(n3);
      return shadedBigInteger3.add(shadedBigInteger);
   }

   private ShadedBigInteger multiply(ShadedBigInteger shadedBigInteger, boolean flag) {
      if (shadedBigInteger.signum != 0 && this.signum != 0) {
         int length = this.mag.length;
         if (shadedBigInteger == this && length > 20) {
            return this.square();
         } else {
            int length2 = shadedBigInteger.mag.length;
            if (length < 80 || length2 < 80) {
               int n = this.signum == shadedBigInteger.signum ? 1 : -1;
               if (shadedBigInteger.mag.length == 1) {
                  int n2 = shadedBigInteger.mag[0];
                  return multiplyByInt(this.mag, n2, n);
               } else if (this.mag.length == 1) {
                  int n3 = this.mag[0];
                  return multiplyByInt(shadedBigInteger.mag, n3, n);
               } else {
                  Object obj = null;
                  int[] mag2 = shadedBigInteger.mag;
                  int[] ints = multiplyToLen(this.mag, length, mag2, length2, (int[])obj);
                  ints = trustedStripLeadingZeroInts(ints);
                  return new ShadedBigInteger(ints, n);
               }
            } else if (length < 240 && length2 < 240) {
               return multiplyKaratsuba(this, shadedBigInteger);
            } else {
               if (!flag) {
                  int length3 = this.mag.length;
                  int n4 = bitLength(this.mag, length3);
                  int length4 = shadedBigInteger.mag.length;
                  if ((long)(n4 + bitLength(shadedBigInteger.mag, length4)) > 2147483648L) {
                     reportOverflow();
                  }
               }

               return multiplyToomCook3(this, shadedBigInteger);
            }
         }
      } else {
         return ZERO;
      }
   }

   private static int[] trustedStripLeadingZeroInts(int[] ints) {
      int length = ints.length;
      int n = 0;

      while (n < length && ints[n] == 0) {
         n++;
      }

      return n == 0 ? ints : Arrays.copyOfRange(ints, n, length);
   }

   public ShadedBigInteger clearBit(int n) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (n < 0) {
         throw new ArithmeticException("Negative bit address");
      } else {
         int n2 = n >>> 5;
         int[] ints = new int[Math.max(this.intLength(), (n + 1 >>> 5) + 1)];

         for (int i = 0; i < ints.length; i++) {
            ints[ints.length - i - 1] = this.getInt(i);
         }

         ints[ints.length - n2 - 1] = ints[ints.length - n2 - 1] & ~(1 << (n & 31));
         return valueOf2(ints);
      }
   }

   private static void implSquareToLenChecks(int[] ints, int n, int[] ints2, int n2) throws RuntimeException {
      if (n < 1) {
         throw new IllegalArgumentException("invalid input length: " + n);
      } else if (n > ints.length) {
         throw new IllegalArgumentException("input length out of bound: " + n + " > " + ints.length);
      } else if (n * 2 > ints2.length) {
         throw new IllegalArgumentException("input length out of bound: " + n * 2 + " > " + ints2.length);
      } else if (n2 < 1) {
         throw new IllegalArgumentException("invalid input length: " + n2);
      } else if (n2 > ints2.length) {
         throw new IllegalArgumentException("input length out of bound: " + n + " > " + ints2.length);
      }
   }

   public ShadedBigInteger(int[] ints, int n) {
      super();
      this.signum = ints.length == 0 ? 0 : n;
      this.mag = ints;
      if (this.mag.length >= 67108864) {
         this.checkRange();
      }
   }

   private boolean passesMillerRabin(int n, Random rand) {
      ShadedBigInteger shadedBigInteger = ONE;
      ShadedBigInteger shadedBigInteger2 = this.subtract(shadedBigInteger);
      ShadedNumber.getFlowState();
      int lowestSetBit2 = shadedBigInteger2.getLowestSetBit();
      ShadedBigInteger shadedBigInteger3 = shadedBigInteger2.shiftRight(lowestSetBit2);
      if (rand == null) {
         rand = ThreadLocalRandom.current();
      }

      outerLoop:
      for (int i = 0; i < n; i++) {
         while (true) {
            ShadedBigInteger shadedBigInteger4 = new ShadedBigInteger(this.bitLength(), (Random)rand);
            ShadedBigInteger shadedBigInteger5 = ONE;
            if (shadedBigInteger4.compareTo2(shadedBigInteger5) > 0) {
               if (shadedBigInteger4.compareTo2(this) < 0) {
                  int n2 = 0;
                  ShadedBigInteger shadedBigInteger6 = shadedBigInteger4.modPow(shadedBigInteger3, this);

                  while (true) {
                     if (n2 == 0) {
                        ShadedBigInteger shadedBigInteger7 = ONE;
                        if (shadedBigInteger6.equals(shadedBigInteger7)) {
                           continue outerLoop;
                        }
                     }

                     if (shadedBigInteger6.equals(shadedBigInteger2)) {
                        continue outerLoop;
                     }

                     if (n2 > 0) {
                        ShadedBigInteger shadedBigInteger8 = ONE;
                        if (shadedBigInteger6.equals(shadedBigInteger8)) {
                           return false;
                        }
                     }

                     if (++n2 == lowestSetBit2) {
                        return false;
                     }

                     ShadedBigInteger shadedBigInteger9 = TWO;
                     shadedBigInteger6 = shadedBigInteger6.modPow(shadedBigInteger9, this);
                  }
               }
            }
         }
      }

      return true;
   }

   private static void toString(ShadedBigInteger shadedBigInteger, StringBuilder sb, int n, int n2) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (shadedBigInteger.mag.length > 20) {
         int n3 = shadedBigInteger.bitLength();
         int n4 = (int)Math.round(Math.log((double)n3 * LOG_TWO / logCache[n]) / LOG_TWO - 1.0);
         ShadedBigInteger radixConversionCache = getRadixConversionCache(n, n4);
         ShadedBigInteger[] shadedBigIntegers = shadedBigInteger.divideAndRemainder(radixConversionCache);
         int n5 = 1 << n4;
         ShadedBigInteger shadedBigInteger2 = shadedBigIntegers[0];
         int n6 = n2 - n5;
         toString(shadedBigInteger2, sb, n, n6);
         toString(shadedBigIntegers[1], sb, n, n5);
      } else {
         String str = shadedBigInteger.smallToString(n);
         if (str.length() < n2 && sb.length() > 0) {
            for (int i = str.length(); i < n2; i++) {
               sb.append('0');
            }
         }

         sb.append(str);
      }
   }

   public int bitLength() {
      int n = this.bitLength - 1;
      if (n == -1) {
         int[] mag2 = this.mag;
         int length = mag2.length;
         if (length == 0) {
            n = 0;
         } else {
            int n2 = (length - 1 << 5) + bitLengthForInt(this.mag[0]);
            if (this.signum >= 0) {
               n = n2;
            } else {
               boolean flag = Integer.bitCount(this.mag[0]) == 1;

               for (int i = 1; i < length && flag; i++) {
                  flag = this.mag[i] == 0;
               }

               n = flag ? n2 - 1 : n2;
            }
         }

         int n3 = n + 1;
         this.bitLength = n3;
      }

      return n;
   }

   private static int[] makePositive2(int[] ints) {
      int[] flowState2 = ShadedNumber.getFlowState();
      int n = 0;

      while (n < ints.length && ints[n] == -1) {
         n++;
      }

      int n2 = n;

      while (n2 < ints.length && ints[n2] == 0) {
         n2++;
      }

      int n3 = n2 == ints.length ? 1 : 0;
      int[] ints2 = new int[ints.length - n + n3];

      for (int i = n; i < ints.length; i++) {
         ints2[i - n + n3] = ~ints[i];
      }

      int n4 = ints2.length - 1;

      while (++ints2[n4] == 0) {
         n4--;
      }

      return ints2;
   }

   private static void reportOverflow() {
      throw new ArithmeticException("BigInteger would overflow supported range");
   }

   public final int compareMagnitude2(long l) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (!assertionsDisabled && l == Long.MIN_VALUE) {
         throw new AssertionError();
      } else {
         int[] mag2 = this.mag;
         int length = mag2.length;
         if (length > 2) {
            return 1;
         } else {
            if (l < 0L) {
               l = -l;
            }

            int n = (int)(l >>> 32);
            if (n == 0) {
               if (length < 1) {
                  return -1;
               } else if (length > 1) {
                  return 1;
               } else {
                  int n2 = mag2[0];
                  int n3 = (int)l;
                  if (n2 != n3) {
                     return ((long)n2 & 4294967295L) < ((long)n3 & 4294967295L) ? -1 : 1;
                  } else {
                     return 0;
                  }
               }
            } else if (length < 2) {
               return -1;
            } else {
               int n4 = mag2[0];
               if (n4 != n) {
                  return ((long)n4 & 4294967295L) < ((long)n & 4294967295L) ? -1 : 1;
               } else {
                  n4 = mag2[1];
                  int n5 = (int)l;
                  if (n4 != n5) {
                     return ((long)n4 & 4294967295L) < ((long)n5 & 4294967295L) ? -1 : 1;
                  } else {
                     return 0;
                  }
               }
            }
         }
      }
   }

   public ShadedBigInteger subtract(ShadedBigInteger shadedBigInteger) {
      if (shadedBigInteger.signum == 0) {
         return this;
      } else if (this.signum == 0) {
         return shadedBigInteger.negate();
      } else if (shadedBigInteger.signum != this.signum) {
         int[] mag2 = shadedBigInteger.mag;
         return new ShadedBigInteger(add2(this.mag, mag2), this.signum);
      } else {
         int n = this.compareMagnitude(shadedBigInteger);
         if (n == 0) {
            return ZERO;
         } else {
            int[] ints;
            if (n > 0) {
               int[] mag3 = shadedBigInteger.mag;
               ints = subtract(this.mag, mag3);
            } else {
               int[] mag4 = this.mag;
               ints = subtract(shadedBigInteger.mag, mag4);
            }

            int[] ints2 = ints;
            ints2 = trustedStripLeadingZeroInts(ints2);
            return new ShadedBigInteger(ints2, n == this.signum ? 1 : -1);
         }
      }
   }

   private static ShadedBigInteger smallPrime(int n, int n2, Random rand) {
      int n3 = n + 31 >>> 5;
      int[] ints = new int[n3];
      int n4 = 1 << (n + 31 & 31);
      ShadedNumber.getFlowState();
      int n5 = (n4 << 1) - 1;

      while (true) {
         for (int i = 0; i < n3; i++) {
            ints[i] = rand.nextInt();
         }

         ints[0] = ints[0] & n5 | n4;
         if (n > 2) {
            ints[n3 - 1] = ints[n3 - 1] | 1;
         }

         ShadedBigInteger shadedBigInteger = new ShadedBigInteger(ints, 1);
         if (n > 6) {
            ShadedBigInteger shadedBigInteger2 = SMALL_PRIME_PRODUCT;
            long l = shadedBigInteger.remainder(shadedBigInteger2).longValue();
            if (l % 3L == 0L
               || l % 5L == 0L
               || l % 7L == 0L
               || l % 11L == 0L
               || l % 13L == 0L
               || l % 17L == 0L
               || l % 19L == 0L
               || l % 23L == 0L
               || l % 29L == 0L
               || l % 31L == 0L
               || l % 37L == 0L
               || l % 41L == 0L) {
               continue;
            }
         }

         if (n < 4) {
            return shadedBigInteger;
         }

         if (shadedBigInteger.primeToCertainty(n2, rand)) {
            return shadedBigInteger;
         }
      }
   }

   private int signInt() {
      return this.signum < 0 ? -1 : 0;
   }

   public ShadedBigInteger and(ShadedBigInteger shadedBigInteger) {
      int[] ints = new int[Math.max(this.intLength(), shadedBigInteger.intLength())];
      ShadedNumber.getFlowState();

      for (int i = 0; i < ints.length; i++) {
         int n = ints.length - i - 1;
         int n2 = this.getInt(n);
         int n3 = ints.length - i - 1;
         ints[i] = n2 & shadedBigInteger.getInt(n3);
      }

      return valueOf2(ints);
   }

   private static int[] leftShift(int[] ints, int n, int n2) {
      int n3 = n2 >>> 5;
      int n4 = n2 & 31;
      ShadedNumber.getFlowState();
      int n5 = bitLengthForInt(ints[0]);
      if (n2 <= 32 - n5) {
         primitiveLeftShift(ints, n, n4);
         return ints;
      } else if (n4 <= 32 - n5) {
         int[] ints2 = new int[n3 + n];
         System.arraycopy(ints, 0, ints2, 0, n);
         int length = ints2.length;
         primitiveLeftShift(ints2, length, n4);
         return ints2;
      } else {
         int[] ints3 = new int[n3 + n + 1];
         System.arraycopy(ints, 0, ints3, 0, n);
         int length2 = ints3.length;
         int n6 = 32 - n4;
         int n7 = length2;
         primitiveRightShift(ints3, n7, n6);
         return ints3;
      }
   }

   private static void destructiveMulAdd(int[] ints, int n, int n2) {
      long l = (long)n & 4294967295L;
      long l2 = (long)n2 & 4294967295L;
      int length = ints.length;
      long l3 = 0L;
      long l4 = 0L;

      for (int i = length - 1; i >= 0; i--) {
         l3 = l * ((long)ints[i] & 4294967295L) + l4;
         ints[i] = (int)l3;
         l4 = l3 >>> 32;
      }

      long l5 = ((long)ints[length - 1] & 4294967295L) + l2;
      ints[length - 1] = (int)l5;
      l4 = l5 >>> 32;

      for (int j = length - 2; j >= 0; j--) {
         l5 = ((long)ints[j] & 4294967295L) + l4;
         ints[j] = (int)l5;
         l4 = l5 >>> 32;
      }
   }

   public ShadedBigInteger flipBit(int n) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (n < 0) {
         throw new ArithmeticException("Negative bit address");
      } else {
         int n2 = n >>> 5;
         int[] ints = new int[Math.max(this.intLength(), n2 + 2)];

         for (int i = 0; i < ints.length; i++) {
            ints[ints.length - i - 1] = this.getInt(i);
         }

         ints[ints.length - n2 - 1] = ints[ints.length - n2 - 1] ^ 1 << (n & 31);
         return valueOf2(ints);
      }
   }

   private void checkRange() {
      if (this.mag.length > 67108864 || this.mag.length == 67108864 && this.mag[0] < 0) {
         reportOverflow();
      }
   }

   public ShadedBigInteger multiply2(long l) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (l != 0L && this.signum != 0) {
         if (l == Long.MIN_VALUE) {
            ShadedBigInteger shadedBigInteger = valueOf(l);
            return this.multiply(shadedBigInteger);
         } else {
            int n = l > 0L ? this.signum : -this.signum;
            if (l < 0L) {
               l = -l;
            }

            long l2 = l >>> 32;
            long l3 = l & 4294967295L;
            int length = this.mag.length;
            int[] mag2 = this.mag;
            int[] ints = l2 == 0L ? new int[length + 1] : new int[length + 2];
            long l4 = 0L;
            int n2 = ints.length - 1;

            for (int i = length - 1; i >= 0; i--) {
               long l5 = ((long)mag2[i] & 4294967295L) * l3 + l4;
               ints[n2--] = (int)l5;
               l4 = l5 >>> 32;
            }

            ints[n2] = (int)l4;
            if (l2 != 0L) {
               l4 = 0L;
               n2 = ints.length - 2;

               for (int j = length - 1; j >= 0; j--) {
                  long l6 = ((long)mag2[j] & 4294967295L) * l2 + ((long)ints[n2] & 4294967295L) + l4;
                  ints[n2--] = (int)l6;
                  l4 = l6 >>> 32;
               }

               ints[0] = (int)l4;
            }

            if (l4 == 0L) {
               ints = Arrays.copyOfRange(ints, 1, ints.length);
            }

            return new ShadedBigInteger(ints, n);
         }
      } else {
         return ZERO;
      }
   }

   private ShadedBigInteger[] divideAndRemainderBurnikelZiegler(ShadedBigInteger shadedBigInteger) {
      MutableBigInt mutableBigInt = new MutableBigInt();
      ShadedNumber.getFlowState();
      MutableBigInt mutableBigInt2 = new MutableBigInt(this);
      MutableBigInt mutableBigInt3 = new MutableBigInt(shadedBigInteger);
      MutableBigInt mutableBigInt4 = mutableBigInt2.divideAndRemainderBurnikelZiegler(mutableBigInt3, mutableBigInt);
      ShadedBigInteger shadedBigInteger2;
      if (mutableBigInt.isZero()) {
         shadedBigInteger2 = ZERO;
      } else {
         int n = this.signum * shadedBigInteger.signum;
         shadedBigInteger2 = mutableBigInt.toBigInteger(n);
      }

      ShadedBigInteger shadedBigInteger3 = shadedBigInteger2;
      if (mutableBigInt4.isZero()) {
         shadedBigInteger2 = ZERO;
      } else {
         int signum2 = this.signum;
         shadedBigInteger2 = mutableBigInt4.toBigInteger(signum2);
      }

      ShadedBigInteger shadedBigInteger4 = shadedBigInteger2;
      return new ShadedBigInteger[]{shadedBigInteger3, shadedBigInteger4};
   }

   public ShadedBigInteger andNot(ShadedBigInteger shadedBigInteger) {
      int[] ints = new int[Math.max(this.intLength(), shadedBigInteger.intLength())];
      ShadedNumber.getFlowState();

      for (int i = 0; i < ints.length; i++) {
         int n = ints.length - i - 1;
         int n2 = this.getInt(n);
         int n3 = ints.length - i - 1;
         ints[i] = n2 & ~shadedBigInteger.getInt(n3);
      }

      return valueOf2(ints);
   }

   public ShadedBigInteger not() {
      int[] ints = new int[this.intLength()];
      ShadedNumber.getFlowState();

      for (int i = 0; i < ints.length; i++) {
         int n = ints.length - i - 1;
         ints[i] = ~this.getInt(n);
      }

      return valueOf2(ints);
   }

   private static int implMulAdd(int[] ints, int[] ints2, int n, int n2, int n3) {
      long l = (long)n3 & 4294967295L;
      long l2 = 0L;
      n = ints.length - n - 1;

      for (int i = n2 - 1; i >= 0; i--) {
         long l3 = ((long)ints2[i] & 4294967295L) * l + ((long)ints[n] & 4294967295L) + l2;
         ints[n--] = (int)l3;
         l2 = l3 >>> 32;
      }

      return (int)l2;
   }

   private static int getPrimeSearchLen(int n) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (n > 500000001) {
         throw new ArithmeticException("Prime search implementation restriction on bitLength");
      } else {
         return n / 20 * 64;
      }
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof ShadedBigInteger)) {
         return false;
      } else {
         ShadedBigInteger shadedBigInteger = (ShadedBigInteger)obj;
         if (shadedBigInteger.signum != this.signum) {
            return false;
         } else {
            int[] mag2 = this.mag;
            int length = mag2.length;
            int[] mag3 = shadedBigInteger.mag;
            if (length != mag3.length) {
               return false;
            } else {
               for (int i = 0; i < length; i++) {
                  if (mag3[i] != mag2[i]) {
                     return false;
                  }
               }

               return true;
            }
         }
      }
   }

   private static ShadedBigInteger lucasLehmerSequence(int n, ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2) {
      ShadedBigInteger shadedBigInteger3 = valueOf((long)n);
      ShadedNumber.getFlowState();
      ShadedBigInteger shadedBigInteger4 = ONE;
      ShadedBigInteger shadedBigInteger5 = ONE;

      for (int i = shadedBigInteger.bitLength() - 2; i >= 0; i--) {
         ShadedBigInteger shadedBigInteger6 = shadedBigInteger4.multiply(shadedBigInteger5);
         ShadedBigInteger shadedBigInteger7 = shadedBigInteger6.mod(shadedBigInteger2);
         shadedBigInteger6 = shadedBigInteger5.square();
         ShadedBigInteger shadedBigInteger8 = shadedBigInteger4.square();
         ShadedBigInteger shadedBigInteger9 = shadedBigInteger3.multiply(shadedBigInteger8);
         ShadedBigInteger shadedBigInteger10 = shadedBigInteger9;
         shadedBigInteger6 = shadedBigInteger6.add(shadedBigInteger10);
         ShadedBigInteger shadedBigInteger11 = shadedBigInteger6.mod(shadedBigInteger2);
         byte b = 0;
         if (shadedBigInteger11.testBit(b)) {
            shadedBigInteger11 = shadedBigInteger11.subtract(shadedBigInteger2);
         }

         byte b2 = 1;
         shadedBigInteger11 = shadedBigInteger11.shiftRight((int)b2);
         shadedBigInteger4 = shadedBigInteger7;
         shadedBigInteger5 = shadedBigInteger11;
         if (shadedBigInteger.testBit(i)) {
            shadedBigInteger6 = shadedBigInteger7.add(shadedBigInteger11);
            shadedBigInteger7 = shadedBigInteger6.mod(shadedBigInteger2);
            byte b3 = 0;
            if (shadedBigInteger7.testBit(b3)) {
               shadedBigInteger7 = shadedBigInteger7.subtract(shadedBigInteger2);
            }

            byte b4 = 1;
            ShadedBigInteger shadedBigInteger12 = shadedBigInteger7.shiftRight((int)b4);
            shadedBigInteger9 = shadedBigInteger3.multiply(shadedBigInteger7);
            ShadedBigInteger shadedBigInteger13 = shadedBigInteger9;
            shadedBigInteger6 = shadedBigInteger11.add(shadedBigInteger13);
            shadedBigInteger11 = shadedBigInteger6.mod(shadedBigInteger2);
            byte b5 = 0;
            if (shadedBigInteger11.testBit(b5)) {
               shadedBigInteger11 = shadedBigInteger11.subtract(shadedBigInteger2);
            }

            byte b6 = 1;
            shadedBigInteger11 = shadedBigInteger11.shiftRight((int)b6);
            shadedBigInteger4 = shadedBigInteger12;
            shadedBigInteger5 = shadedBigInteger11;
         }
      }

      return shadedBigInteger4;
   }

   static {
      short s = 21100;
      SMALL_PRIME_PRODUCT = valueOf(152125131763605L);

      for (int i = 1; i <= 16; i++) {
         int[] ints = new int[]{i};
         posConst[i] = new ShadedBigInteger(ints, 1);
         negConst[i] = new ShadedBigInteger(ints, -1);
      }

      powerCache = new ShadedBigInteger[37][];
      logCache = new double[37];

      for (int j = 2; j <= 36; j++) {
         powerCache[j] = new ShadedBigInteger[]{valueOf((long)j)};
         logCache[j] = Math.log((double)j);
      }

      ZERO = new ShadedBigInteger(new int[0], 0);
      short s2 = 16809;
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
      longRadix = new ShadedBigInteger[]{
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

   private static ShadedBigInteger largePrime(int n, int n2, Random rand) {
      ShadedBigInteger shadedBigInteger = new ShadedBigInteger(n, rand);
      int n3 = n - 1;
      int n4 = n3;
      ShadedBigInteger shadedBigInteger2 = shadedBigInteger.setBit(n4);
      ShadedNumber.getFlowState();
      shadedBigInteger2.mag[shadedBigInteger2.mag.length - 1] = shadedBigInteger2.mag[shadedBigInteger2.mag.length - 1] & -2;
      int primeSearchLen = getPrimeSearchLen(n);
      PrimeBitSieve primeBitSieve = new PrimeBitSieve(shadedBigInteger2, primeSearchLen);
      ShadedBigInteger shadedBigInteger3 = primeBitSieve.retrieve(shadedBigInteger2, n2, rand);

      while (shadedBigInteger3 == null || shadedBigInteger3.bitLength() != n) {
         ShadedBigInteger shadedBigInteger4 = valueOf((long)(2 * primeSearchLen));
         ShadedBigInteger shadedBigInteger5 = shadedBigInteger4;
         shadedBigInteger2 = shadedBigInteger2.add(shadedBigInteger5);
         if (shadedBigInteger2.bitLength() != n) {
            shadedBigInteger = new ShadedBigInteger(n, rand);
            int n5 = n - 1;
            int n6 = n5;
            shadedBigInteger2 = shadedBigInteger.setBit(n6);
         }

         shadedBigInteger2.mag[shadedBigInteger2.mag.length - 1] = shadedBigInteger2.mag[shadedBigInteger2.mag.length - 1] & -2;
         primeBitSieve = new PrimeBitSieve(shadedBigInteger2, primeSearchLen);
         shadedBigInteger3 = primeBitSieve.retrieve(shadedBigInteger2, n2, rand);
      }

      return shadedBigInteger3;
   }

   private ShadedBigInteger squareKaratsuba() {
      int n = (this.mag.length + 1) / 2;
      ShadedBigInteger lower = this.getLower(n);
      ShadedBigInteger upper = this.getUpper(n);
      ShadedBigInteger shadedBigInteger = upper.square();
      ShadedBigInteger shadedBigInteger2 = lower.square();
      int n2 = n * 32;
      ShadedBigInteger shadedBigInteger3 = shadedBigInteger.shiftLeft(n2);
      ShadedBigInteger shadedBigInteger4 = lower.add(upper);
      ShadedBigInteger shadedBigInteger5 = shadedBigInteger4.square();
      ShadedBigInteger shadedBigInteger6 = shadedBigInteger.add(shadedBigInteger2);
      ShadedBigInteger shadedBigInteger7 = shadedBigInteger5.subtract(shadedBigInteger6);
      ShadedBigInteger shadedBigInteger8 = shadedBigInteger7;
      shadedBigInteger3 = shadedBigInteger3.add(shadedBigInteger8);
      int n3 = n * 32;
      shadedBigInteger3 = shadedBigInteger3.shiftLeft(n3);
      return shadedBigInteger3.add(shadedBigInteger2);
   }

   private static int[] stripLeadingZeroBytes(byte[] bytes) {
      int length = bytes.length;
      ShadedNumber.getFlowState();
      int n = 0;

      while (n < length && bytes[n] == 0) {
         n++;
      }

      int n2 = length - n + 3 >>> 2;
      int[] ints = new int[n2];
      int n3 = length - 1;

      for (int i = n2 - 1; i >= 0; i--) {
         ints[i] = bytes[n3--] & 255;
         int n4 = n3 - n + 1;
         int n5 = Math.min(3, n4);

         for (int j = 8; j <= n5 << 3; j += 8) {
            ints[i] |= (bytes[n3--] & 255) << j;
         }
      }

      return ints;
   }

   private ShadedBigInteger divideBurnikelZiegler(ShadedBigInteger shadedBigInteger) {
      return this.divideAndRemainderBurnikelZiegler(shadedBigInteger)[0];
   }

   public byte[] toByteArray() {
      int n = this.bitLength() / 8 + 1;
      byte[] bytes = new byte[n];
      int n2 = n - 1;
      int n3 = 4;
      ShadedNumber.getFlowState();
      int n4 = 0;

      for (int i = 0; n2 >= 0; n2--) {
         if (n3 == 4) {
            int n5 = i++;
            n4 = this.getInt(n5);
            n3 = 1;
         } else {
            n4 >>>= 8;
            n3++;
         }

         bytes[n2] = (byte)n4;
      }

      return bytes;
   }

   private ShadedBigInteger divideKnuth(ShadedBigInteger shadedBigInteger) {
      MutableBigInt mutableBigInt = new MutableBigInt();
      MutableBigInt mutableBigInt2 = new MutableBigInt(this.mag);
      MutableBigInt mutableBigInt3 = new MutableBigInt(shadedBigInteger.mag);
      boolean flag = false;
      mutableBigInt2.divideKnuth(mutableBigInt3, mutableBigInt, flag);
      int n = this.signum * shadedBigInteger.signum;
      return mutableBigInt.toBigInteger(n);
   }

   private ShadedBigInteger(int[] ints) {
      super();
      int[] flowState2 = ShadedNumber.getFlowState();
      if (ints.length == 0) {
         throw new NumberFormatException("Zero length BigInteger");
      } else {
         if (ints[0] < 0) {
            this.mag = makePositive2(ints);
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

   private ShadedBigInteger shiftRightImpl(int n) {
      int n2 = n >>> 5;
      int n3 = n & 31;
      int length = this.mag.length;
      Object obj = null;
      if (n2 >= length) {
         return this.signum >= 0 ? ZERO : negConst[1];
      } else {
         if (n3 == 0) {
            int n4 = length - n2;
            obj = Arrays.copyOf(this.mag, n4);
         } else {
            int n5 = 0;
            int n6 = this.mag[0] >>> n3;
            if (n6 != 0) {
               obj = new int[length - n2];
               ((Object[])obj)[n5++] = n6;
            } else {
               obj = new int[length - n2 - 1];
            }

            int n7 = 32 - n3;
            int n8 = 0;

            while (n8 < length - n2 - 1) {
               ((Object[])obj)[n5++] = this.mag[n8++] << n7 | this.mag[n8] >>> n3;
            }
         }

         if (this.signum < 0) {
            boolean flag = false;
            int n9 = length - 1;

            for (int i = length - n2; n9 >= i && !flag; n9--) {
               flag = this.mag[n9] != 0;
            }

            if (!flag && n3 != 0) {
               flag = this.mag[length - n2 - 1] << 32 - n3 != 0;
            }

            if (flag) {
               obj = this.javaIncrement((int[])obj);
            }
         }

         return new ShadedBigInteger((int[])obj, this.signum);
      }
   }

   public ShadedBigInteger or(ShadedBigInteger shadedBigInteger) {
      int[] ints = new int[Math.max(this.intLength(), shadedBigInteger.intLength())];
      ShadedNumber.getFlowState();

      for (int i = 0; i < ints.length; i++) {
         int n = ints.length - i - 1;
         int n2 = this.getInt(n);
         int n3 = ints.length - i - 1;
         ints[i] = n2 | shadedBigInteger.getInt(n3);
      }

      return valueOf2(ints);
   }

   @Override
   public float floatValue() {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (this.signum == 0) {
         return 0.0F;
      } else {
         int n = (this.mag.length - 1 << 5) + bitLengthForInt(this.mag[0]) - 1;
         if (n < 63) {
            return (float)this.longValue();
         } else if (n > 127) {
            return this.signum > 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
         } else {
            int n2 = n - 24;
            int n3 = n2 & 31;
            int n4 = 32 - n3;
            int n5;
            if (n3 == 0) {
               n5 = this.mag[0];
            } else {
               n5 = this.mag[0] >>> n3;
               if (n5 == 0) {
                  n5 = this.mag[0] << n4 | this.mag[1] >>> n3;
               }
            }

            int n6 = n5 >> 1;
            n6 &= 8388607;
            boolean flag = (n5 & 1) != 0 && ((n6 & 1) != 0 || this.abs().getLowestSetBit() < n2);
            int n7 = flag ? n6 + 1 : n6;
            int n8 = n + 127 << 23;
            n8 += n7;
            n8 |= this.signum & -2147483648;
            return Float.intBitsToFloat(n8);
         }
      }
   }

   private ShadedBigInteger modPow2(ShadedBigInteger shadedBigInteger, int n) {
      ShadedBigInteger shadedBigInteger2 = ONE;
      ShadedBigInteger shadedBigInteger3 = this.mod2(n);
      int n2 = 0;
      ShadedNumber.getFlowState();
      int n3 = shadedBigInteger.bitLength();
      byte b = 0;
      if (this.testBit(b)) {
         n3 = n - 1 < n3 ? n - 1 : n3;
      }

      while (n2 < n3) {
         if (shadedBigInteger.testBit(n2)) {
            shadedBigInteger2 = shadedBigInteger2.multiply(shadedBigInteger3).mod2(n);
         }

         if (++n2 < n3) {
            shadedBigInteger3 = shadedBigInteger3.square().mod2(n);
         }
      }

      return shadedBigInteger2;
   }

   private static int[] subtract(int[] ints, int[] ints2) {
      int length = ints.length;
      int[] ints3 = new int[length];
      int length2 = ints2.length;

      long l;
      for (l = 0L; length2 > 0; ints3[length] = (int)l) {
         long l2 = (long)ints[--length] & 4294967295L;
         length2--;
         l = l2 - ((long)ints2[length2] & 4294967295L) + (l >> 32);
      }

      for (boolean i = l >> 32 != 0L; length > 0 && i; i = (ints3[length] = ints[length] - 1) == -1) {
         length--;
      }

      while (length > 0) {
         ints3[length] = ints[--length];
      }

      return ints3;
   }

   private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
      GetField getField = objectInputStream.readFields();
      ShadedNumber.getFlowState();
      int n = getField.get("signum", -2);
      byte[] bytes = (byte[])getField.get("magnitude", null);
      if (n >= -1 && n <= 1) {
         int[] ints = stripLeadingZeroBytes(bytes);
         if (ints.length == 0 != (n == 0)) {
            String str = "BigInteger: signum-magnitude mismatch";
            if (getField.defaulted("magnitude")) {
               short s = 20757;
               str = "BigInteger: Magnitude not present in stream";
            }

            throw new StreamCorruptedException(str);
         } else {
            BigIntegerUnsafeHolder.putSign(this, n);
            BigIntegerUnsafeHolder.putMag(this, ints);
            if (ints.length >= 67108864) {
               try {
                  this.checkRange();
               } catch (ArithmeticException e) {
                  throw new StreamCorruptedException("BigInteger: Out of the supported range");
               }
            }
         }
      } else {
         String str2 = "BigInteger: Invalid signum value";
         if (getField.defaulted("signum")) {
            str2 = "BigInteger: Signum not present in stream";
         }

         throw new StreamCorruptedException(str2);
      }
   }

   private boolean passesLucasLehmer() {
      ShadedBigInteger shadedBigInteger = ONE;
      ShadedBigInteger shadedBigInteger2 = this.add(shadedBigInteger);
      ShadedNumber.getFlowState();
      int n = 5;

      while (jacobiSymbol(n, this) != -1) {
         n = n < 0 ? Math.abs(n) + 2 : -(n + 2);
      }

      ShadedBigInteger shadedBigInteger3 = lucasLehmerSequence(n, shadedBigInteger2, this);
      ShadedBigInteger shadedBigInteger4 = shadedBigInteger3.mod(this);
      ShadedBigInteger shadedBigInteger5 = ZERO;
      return shadedBigInteger4.equals(shadedBigInteger5);
   }

   public static int addOne(int[] ints, int n, int n2, int n3) {
      n = ints.length - 1 - n2 - n;
      long l = ((long)ints[n] & 4294967295L) + ((long)n3 & 4294967295L);
      ints[n] = (int)l;
      if (l >>> 32 == 0L) {
         return 0;
      } else {
         while (--n2 >= 0) {
            if (--n < 0) {
               return 1;
            }

            ints[n]++;
            if (ints[n] != 0) {
               return 0;
            }
         }

         return 1;
      }
   }

   public ShadedBigInteger(int n, Random rand) {
      this(1, randomBits(n, rand));
   }

   public static void primitiveLeftShift(int[] ints, int n, int n2) {
      if (n != 0 && n2 != 0) {
         int n3 = 32 - n2;
         int n4 = 0;
         int n5 = ints[n4];

         for (int i = n4 + n - 1; n4 < i; n4++) {
            int n6 = n5;
            n5 = ints[n4 + 1];
            ints[n4] = n6 << n2 | n5 >>> n3;
         }

         ints[n - 1] = ints[n - 1] << n2;
      }
   }

   private ShadedBigInteger(long l) {
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

   public boolean testBit(int n) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (n < 0) {
         throw new ArithmeticException("Negative bit address");
      } else {
         int n2 = n >>> 5;
         return (this.getInt(n2) & 1 << (n & 31)) != 0;
      }
   }

   private static void implMulAddCheck(int[] ints, int[] ints2, int n, int n2, int n3) {
      if (n2 > ints2.length) {
         throw new IllegalArgumentException("input length is out of bound: " + n2 + " > " + ints2.length);
      } else if (n < 0) {
         throw new IllegalArgumentException("input offset is invalid: " + n);
      } else if (n > ints.length - 1) {
         throw new IllegalArgumentException("input offset is out of bound: " + n + " > " + (ints.length - 1));
      } else if (n2 > ints.length - n) {
         throw new IllegalArgumentException("input len is out of bound: " + n2 + " > " + (ints.length - n));
      }
   }

   private static int subN(int[] ints, int[] ints2, int n) {
      int[] flowState2 = ShadedNumber.getFlowState();
      long l = 0L;

      while (--n >= 0) {
         l = ((long)ints[n] & 4294967295L) - ((long)ints2[n] & 4294967295L) + (l >> 32);
         ints[n] = (int)l;
      }

      return (int)(l >> 32);
   }

   private static final int[] squareToLen(int[] ints, int n, int[] ints2) {
      int n2 = n << 1;
      if (ints2 == null || ints2.length < n2) {
         ints2 = new int[n2];
      }

      implSquareToLenChecks(ints, n, ints2, n2);
      return implSquareToLen(ints, n, ints2, n2);
   }

   private ShadedBigInteger[] divideAndRemainderKnuth(ShadedBigInteger shadedBigInteger) {
      ShadedBigInteger[] shadedBigIntegers = new ShadedBigInteger[2];
      ShadedNumber.getFlowState();
      MutableBigInt mutableBigInt = new MutableBigInt();
      MutableBigInt mutableBigInt2 = new MutableBigInt(this.mag);
      MutableBigInt mutableBigInt3 = new MutableBigInt(shadedBigInteger.mag);
      MutableBigInt mutableBigInt4 = mutableBigInt2.divideKnuth(mutableBigInt3, mutableBigInt);
      int n = this.signum == shadedBigInteger.signum ? 1 : -1;
      shadedBigIntegers[0] = mutableBigInt.toBigInteger(n);
      int signum2 = this.signum;
      shadedBigIntegers[1] = mutableBigInt4.toBigInteger(signum2);
      return shadedBigIntegers;
   }

   public ShadedBigInteger(byte[] bytes) {
      super();
      int[] flowState2 = ShadedNumber.getFlowState();
      if (bytes[0] < 0) {
         this.mag = makePositive(bytes);
         this.signum = -1;
      } else {
         this.mag = stripLeadingZeroBytes(bytes);
         this.signum = this.mag.length == 0 ? 0 : 1;
      }

      if (this.mag.length >= 67108864) {
         this.checkRange();
      }
   }

   private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
      PutField putField = objectOutputStream.putFields();
      putField.put("signum", this.signum);
      putField.put("magnitude", this.magSerializedForm());
      putField.put("bitCount", -1);
      putField.put("bitLength", -1);
      putField.put("lowestSetBit", -2);
      putField.put("firstNonzeroByteNum", -2);
      objectOutputStream.writeFields();
   }

   private static byte[] randomBits(int n, Random rand) {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (n < 0) {
         throw new IllegalArgumentException("numBits must be non-negative");
      } else {
         int n2 = (int)(((long)n + 7L) / 8L);
         byte[] bytes = new byte[n2];
         if (n2 > 0) {
            rand.nextBytes(bytes);
            int n3 = 8 * n2 - n;
            bytes[0] = (byte)(bytes[0] & (1 << 8 - n3) - 1);
         }

         return bytes;
      }
   }

   public ShadedBigInteger(String str) {
      this(str, 10);
   }

   public ShadedBigInteger xor(ShadedBigInteger shadedBigInteger) {
      int[] ints = new int[Math.max(this.intLength(), shadedBigInteger.intLength())];
      ShadedNumber.getFlowState();

      for (int i = 0; i < ints.length; i++) {
         int n = ints.length - i - 1;
         int n2 = this.getInt(n);
         int n3 = ints.length - i - 1;
         ints[i] = n2 ^ shadedBigInteger.getInt(n3);
      }

      return valueOf2(ints);
   }

   private ShadedBigInteger exactDivideBy3() {
      int length = this.mag.length;
      int[] ints = new int[length];
      long l = 0L;

      for (int i = length - 1; i >= 0; i--) {
         long l2 = (long)this.mag[i] & 4294967295L;
         long l3 = l2 - l;
         if (l > l2) {
            l = 1L;
         } else {
            l = 0L;
         }

         long l4 = l3 * 2863311531L & 4294967295L;
         ints[i] = (int)l4;
         if (l4 >= 1431655766L) {
            l++;
            if (l4 >= 2863311531L) {
               l++;
            }
         }
      }

      ints = trustedStripLeadingZeroInts(ints);
      return new ShadedBigInteger(ints, this.signum);
   }

   private ShadedBigInteger getToomSlice(int n, int n2, int n3, int n4) {
      int length = this.mag.length;
      int n5 = n4 - length;
      int n6;
      int n7;
      if (n3 == 0) {
         n6 = 0 - n5;
         n7 = n2 - 1 - n5;
      } else {
         n6 = n2 + (n3 - 1) * n - n5;
         n7 = n6 + n - 1;
      }

      if (n6 < 0) {
         n6 = 0;
      }

      if (n7 < 0) {
         return ZERO;
      } else {
         int n8 = n7 - n6 + 1;
         if (n8 <= 0) {
            return ZERO;
         } else if (n6 == 0 && n8 >= length) {
            return this.abs();
         } else {
            int[] ints = new int[n8];
            System.arraycopy(this.mag, n6, ints, 0, n8);
            return new ShadedBigInteger(trustedStripLeadingZeroInts(ints), 1);
         }
      }
   }

   private static int[] montgomeryMultiply(int[] ints, int[] ints2, int[] ints3, int n, long l, int[] ints4) {
      int[] flowState2 = ShadedNumber.getFlowState();
      montgomeryMultiplyChecks(ints, ints2, ints3, n, ints4);
      if (n > 512) {
         ints4 = multiplyToLen(ints, n, ints2, n, ints4);
         int n2 = (int)l;
         return montReduce(ints4, ints3, n, n2);
      } else {
         int[] ints5 = materialize(ints4, n);
         return implMontgomeryMultiply(ints, ints2, ints3, n, l, ints5);
      }
   }

   private static ShadedBigInteger valueOf2(int[] ints) {
      return ints[0] > 0 ? new ShadedBigInteger(ints, 1) : new ShadedBigInteger(ints);
   }

   public ShadedBigInteger add(ShadedBigInteger shadedBigInteger) {
      if (shadedBigInteger.signum == 0) {
         return this;
      } else if (this.signum == 0) {
         return shadedBigInteger;
      } else if (shadedBigInteger.signum == this.signum) {
         int[] mag2 = shadedBigInteger.mag;
         return new ShadedBigInteger(add2(this.mag, mag2), this.signum);
      } else {
         int n = this.compareMagnitude(shadedBigInteger);
         if (n == 0) {
            return ZERO;
         } else {
            int[] ints;
            if (n > 0) {
               int[] mag3 = shadedBigInteger.mag;
               ints = subtract(this.mag, mag3);
            } else {
               int[] mag4 = this.mag;
               ints = subtract(shadedBigInteger.mag, mag4);
            }

            int[] ints2 = ints;
            ints2 = trustedStripLeadingZeroInts(ints2);
            return new ShadedBigInteger(ints2, n == this.signum ? 1 : -1);
         }
      }
   }

   public short shortValueExact() {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (this.mag.length <= 1 && this.bitLength() <= 31) {
         int n = this.intValue();
         if (n >= -32768 && n <= 32767) {
            return this.shortValue();
         }
      }

      throw new ArithmeticException("BigInteger out of short range");
   }

   public int intValueExact() {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (this.mag.length <= 1 && this.bitLength() <= 31) {
         return this.intValue();
      } else {
         throw new ArithmeticException("BigInteger out of int range");
      }
   }

   private ShadedBigInteger oddModPow(ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2) {
      int[] flowState2 = ShadedNumber.getFlowState();
      ShadedBigInteger shadedBigInteger3 = ONE;
      if (shadedBigInteger.equals(shadedBigInteger3)) {
         return this;
      } else if (this.signum == 0) {
         return ZERO;
      } else {
         int[] ints = (int[])this.mag.clone();
         int[] mag2 = shadedBigInteger.mag;
         int[] mag3 = shadedBigInteger2.mag;
         int length = mag3.length;
         if ((length & 1) != 0) {
            int[] ints2 = new int[length + 1];
            System.arraycopy(mag3, 0, ints2, 1, length);
            mag3 = ints2;
            length++;
         }

         int n = 0;
         int length2 = mag2.length;
         int n2 = bitLength(mag2, length2);
         if (n2 != 17 || mag2[0] != 65537) {
            while (n2 > bnExpModThreshTable[n]) {
               n++;
            }
         }

         int n3 = 1 << n;
         int[][] intsArr = new int[n3][];

         for (int i = 0; i < n3; i++) {
            intsArr[i] = new int[length];
         }

         long l = ((long)mag3[length - 1] & 4294967295L) + (((long)mag3[length - 2] & 4294967295L) << 32);
         long l2 = -MutableBigInt.inverseMod64(l);
         int length3 = ints.length;
         int n4 = length << 5;
         int n5 = length3;
         int[] ints3 = leftShift(ints, n5, n4);
         MutableBigInt mutableBigInt = new MutableBigInt();
         MutableBigInt mutableBigInt2 = new MutableBigInt(ints3);
         MutableBigInt mutableBigInt3 = new MutableBigInt(mag3);
         mutableBigInt3.normalize();
         MutableBigInt mutableBigInt4 = mutableBigInt2.divide2(mutableBigInt3, mutableBigInt);
         intsArr[0] = mutableBigInt4.toIntArray();
         if (intsArr[0].length < length) {
            int n6 = length - intsArr[0].length;
            int[] ints4 = new int[length];
            System.arraycopy(intsArr[0], 0, ints4, n6, intsArr[0].length);
            intsArr[0] = ints4;
         }

         int[] ints5 = intsArr[0];
         Object obj = null;
         int[] ints6 = montgomerySquare(ints5, mag3, length, l2, (int[])obj);
         int[] ints7 = Arrays.copyOf(ints6, length);

         for (int j = 1; j < n3; j++) {
            int[] ints8 = intsArr[j - 1];
            Object obj2 = null;
            int[] ints9 = ints8;
            intsArr[j] = montgomeryMultiply(ints7, ints9, mag3, length, l2, (int[])obj2);
         }

         int n7 = 1 << (n2 - 1 & 31);
         int n8 = 0;
         int length4 = mag2.length;
         int n9 = 0;

         for (int k = 0; k <= n; k++) {
            n8 = n8 << 1 | ((mag2[n9] & n7) != 0 ? 1 : 0);
            n7 >>>= 1;
            if (n7 == 0) {
               n9++;
               n7 = Integer.MIN_VALUE;
               length4--;
            }
         }

         n2--;
         boolean flag = true;

         int n10;
         for (n10 = n2 - n; (n8 & 1) == 0; n10++) {
            n8 >>>= 1;
         }

         int[] ints10 = intsArr[n8 >>> 1];
         n8 = 0;
         if (n10 == n2) {
            flag = false;
         }

         while (true) {
            n2--;
            n8 <<= 1;
            if (length4 != 0) {
               n8 |= (mag2[n9] & n7) != 0 ? 1 : 0;
               n7 >>>= 1;
               if (n7 == 0) {
                  n9++;
                  n7 = Integer.MIN_VALUE;
                  length4--;
               }
            }

            if ((n8 & n3) != 0) {
               for (n10 = n2 - n; (n8 & 1) == 0; n10++) {
                  n8 >>>= 1;
               }

               ints10 = intsArr[n8 >>> 1];
               n8 = 0;
            }

            if (n2 == n10) {
               if (flag) {
                  ints6 = (int[])ints10.clone();
                  flag = false;
               } else {
                  ints3 = montgomeryMultiply(ints6, ints10, mag3, length, l2, ints3);
                  ints3 = ints6;
                  ints6 = ints3;
               }
            }

            if (n2 == 0) {
               int[] ints11 = new int[2 * length];
               System.arraycopy(ints6, 0, ints11, length, length);
               int n11 = (int)l2;
               ints6 = montReduce(ints11, mag3, length, n11);
               ints11 = Arrays.copyOf(ints6, length);
               return new ShadedBigInteger(1, ints11);
            }

            if (!flag) {
               ints3 = montgomerySquare(ints6, mag3, length, l2, ints3);
               ints3 = ints6;
               ints6 = ints3;
            }
         }
      }
   }

   private static int[] add2(int[] ints, int[] ints2) {
      if (ints.length < ints2.length) {
         int[] ints3 = ints;
         ints = ints2;
         ints2 = ints3;
      }

      int length = ints.length;
      int length2 = ints2.length;
      int[] ints4 = new int[length];
      long l = 0L;
      if (length2 == 1) {
         l = ((long)ints[--length] & 4294967295L) + ((long)ints2[0] & 4294967295L);
         ints4[length] = (int)l;
      } else {
         while (length2 > 0) {
            l = ((long)ints[--length] & 4294967295L) + ((long)ints2[--length2] & 4294967295L) + (l >>> 32);
            ints4[length] = (int)l;
         }
      }

      boolean flag;
      for (flag = l >>> 32 != 0L; length > 0 && flag; flag = (ints4[length] = ints[length] + 1) == 0) {
         length--;
      }

      while (length > 0) {
         ints4[length] = ints[--length];
      }

      if (flag) {
         int[] ints5 = new int[ints4.length + 1];
         System.arraycopy(ints4, 0, ints5, 1, ints4.length);
         ints5[0] = 1;
         return ints5;
      } else {
         return ints4;
      }
   }

   public ShadedBigInteger(String str, int n) {
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

   public byte byteValueExact() {
      int[] flowState2 = ShadedNumber.getFlowState();
      if (this.mag.length <= 1 && this.bitLength() <= 31) {
         int n = this.intValue();
         if (n >= -128 && n <= 127) {
            return this.byteValue();
         }
      }

      throw new ArithmeticException("BigInteger out of byte range");
   }

}
