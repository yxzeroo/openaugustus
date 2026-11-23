package net.augustus.vendor.launcher.crypto.math;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Arrays;

public class ShadedBigDecimal extends Number implements Comparable {
   public static final boolean assertionsDisabled = !ShadedBigDecimal.class.desiredAssertionStatus();
   private static final int BIG_TEN_POWERS_TABLE_INITLEN;
   public static final int ROUND_HALF_EVEN = 6;
   public static final long INFLATED = Long.MIN_VALUE;
   private static final int BIG_TEN_POWERS_TABLE_MAX;
   private static final long serialVersionUID = 6108874887143696463L;
   public static final ShadedBigDecimal ONE;
   public static final int ROUND_HALF_UP = 4;
   private final int scale;
   public static final int ROUND_UP = 0;
   public static final int ROUND_UNNECESSARY = 7;
   private static final long HALF_LONG_MIN_VALUE = -4611686018427387904L;
   private static final double[] double10pow = new double[]{
      1.0,
      10.0,
      100.0,
      1000.0,
      10000.0,
      100000.0,
      1000000.0,
      1.0E7,
      1.0E8,
      1.0E9,
      1.0E10,
      1.0E11,
      1.0E12,
      1.0E13,
      1.0E14,
      1.0E15,
      1.0E16,
      1.0E17,
      1.0E18,
      1.0E19,
      1.0E20,
      1.0E21,
      1.0E22
   };
   private static final long[][] LONGLONG_TEN_POWERS_TABLE = new long[][]{
      {0L, -8446744073709551616L},
      {5L, 7766279631452241920L},
      {54L, 3875820019684212736L},
      {542L, 1864712049423024128L},
      {5421L, 200376420520689664L},
      {54210L, 2003764205206896640L},
      {542101L, 1590897978359414784L},
      {5421010L, -2537764290115403776L},
      {54210108L, -6930898827444486144L},
      {542101086L, 4477988020393345024L},
      {5421010862L, 7886392056514347008L},
      {54210108624L, 5076944270305263616L},
      {542101086242L, -4570789518076018688L},
      {5421010862427L, -8814407033341083648L},
      {54210108624275L, 4089650035136921600L},
      {542101086242752L, 4003012203950112768L},
      {5421010862427522L, 3136633892082024448L},
      {54210108624275221L, -5527149226598858752L},
      {542101086242752217L, 68739955140067328L},
      {5421010862427522170L, 687399551400673280L}
   };
   private static final ShadedBigDecimal[] zeroThroughTen;
   private static final ThreadLocal<BigDecimalStringBuilderHelper> threadLocalStringBuilderHelper = new BigDecimalStringBuilderThreadLocal();
   public static final int ROUND_FLOOR = 3;
   private static final float[] float10pow = new float[]{1.0F, 10.0F, 100.0F, 1000.0F, 10000.0F, 100000.0F, 1000000.0F, 1.0E7F, 1.0E8F, 1.0E9F, 1.0E10F};
   public static final int ROUND_HALF_DOWN = 5;
   public static final ShadedBigDecimal ZERO;
   private transient int precision;
   private static final long[] THRESHOLDS_TABLE = new long[]{
      Long.MAX_VALUE,
      922337203685477580L,
      92233720368547758L,
      9223372036854775L,
      922337203685477L,
      92233720368547L,
      9223372036854L,
      922337203685L,
      92233720368L,
      9223372036L,
      922337203L,
      92233720L,
      9223372L,
      922337L,
      92233L,
      9223L,
      922L,
      92L,
      9L
   };
   private static final ShadedBigDecimal[] ZERO_SCALED_BY;
   private static final ShadedBigInteger INFLATED_BIGINT;
   public static final int ROUND_DOWN = 1;
   private static final long DIV_NUM_BASE = 4294967296L;
   private static volatile ShadedBigInteger[] BIG_TEN_POWERS_TABLE;
   private transient String stringCache;
   private final transient long intCompact;
   private final ShadedBigInteger intVal;
   public static final int ROUND_CEILING = 2;
   public static final ShadedBigDecimal TEN;
   private static final long[] LONG_TEN_POWERS_TABLE = new long[]{
      1L,
      10L,
      100L,
      1000L,
      10000L,
      100000L,
      1000000L,
      10000000L,
      100000000L,
      1000000000L,
      10000000000L,
      100000000000L,
      1000000000000L,
      10000000000000L,
      100000000000000L,
      1000000000000000L,
      10000000000000000L,
      100000000000000000L,
      1000000000000000000L
   };
   private static final int MAX_COMPACT_DIGITS = 18;
   private static final long HALF_LONG_MAX_VALUE = 4611686018427387903L;

   @Override
   public int compareTo(Object other) {
      return this.compareTo2((ShadedBigDecimal)other);
   }

   private static ShadedBigDecimal divide7(ShadedBigInteger shadedBigInteger, int n, ShadedBigInteger shadedBigInteger2, int n2, long l, ShadedMathContext shadedMathContext) {
      if (compareMagnitudeNormalized3(shadedBigInteger, n, shadedBigInteger2, n2) > 0) {
         n2--;
      }

      int precision2 = shadedMathContext.precision;
      int oldMode = shadedMathContext.roundingMode.oldMode;
      int n3 = checkScaleNonZero(l + (long)n2 - (long)n + (long)precision2);
      ShadedBigDecimal shadedBigDecimal;
      if (checkScaleNonZero((long)precision2 + (long)n2 - (long)n) > 0) {
         shadedBigDecimal = divideAndRound3(bigMultiplyPowerTen2(shadedBigInteger, checkScaleNonZero((long)precision2 + (long)n2 - (long)n)), shadedBigInteger2, n3, oldMode, checkScaleNonZero(l));
      } else {
         shadedBigDecimal = divideAndRound3(shadedBigInteger, bigMultiplyPowerTen2(shadedBigInteger2, checkScaleNonZero((long)checkScaleNonZero((long)n - (long)precision2) - (long)n2)), n3, oldMode, checkScaleNonZero(l));
      }

      return doRound(shadedBigDecimal, shadedMathContext);
   }

   public ShadedBigDecimal subtract(ShadedBigDecimal shadedBigDecimal, ShadedMathContext shadedMathContext) {
      if (shadedMathContext.precision == 0) {
         return this.subtract(shadedBigDecimal);
      } else {
         return this.add2(shadedBigDecimal.negate(), shadedMathContext);
      }
   }

   private static ShadedBigDecimal divide(ShadedBigInteger shadedBigInteger, int n, long l, int n2, long l2, ShadedMathContext shadedMathContext) {
      if (-compareMagnitudeNormalized(l, n2, shadedBigInteger, n) > 0) {
         n2--;
      }

      int precision2 = shadedMathContext.precision;
      int oldMode = shadedMathContext.roundingMode.oldMode;
      int n3 = checkScaleNonZero(l2 + (long)n2 - (long)n + (long)precision2);
      ShadedBigDecimal shadedBigDecimal;
      if (checkScaleNonZero((long)precision2 + (long)n2 - (long)n) > 0) {
         shadedBigDecimal = divideAndRound(bigMultiplyPowerTen2(shadedBigInteger, checkScaleNonZero((long)precision2 + (long)n2 - (long)n)), l, n3, oldMode, checkScaleNonZero(l2));
      } else {
         int n7 = checkScaleNonZero((long)n - (long)precision2);
         if (n7 == n2) {
            shadedBigDecimal = divideAndRound(shadedBigInteger, l, n3, oldMode, checkScaleNonZero(l2));
         } else {
            int n10 = checkScaleNonZero((long)n7 - (long)n2);
            long l3;
            if ((l3 = longMultiplyPowerTen(l, n10)) == Long.MIN_VALUE) {
               shadedBigDecimal = divideAndRound3(shadedBigInteger, bigMultiplyPowerTen(l, n10), n3, oldMode, checkScaleNonZero(l2));
            } else {
               shadedBigDecimal = divideAndRound(shadedBigInteger, l3, n3, oldMode, checkScaleNonZero(l2));
            }
         }
      }

      return doRound(shadedBigDecimal, shadedMathContext);
   }

   public ShadedBigInteger unscaledValue() {
      return this.inflated();
   }

   public ShadedBigDecimal(long l, ShadedMathContext shadedMathContext) {
      super();
      int precision2 = shadedMathContext.precision;
      int oldMode = shadedMathContext.roundingMode.oldMode;
      int n = 0;
      int n2 = 0;
      ShadedBigInteger shadedBigInteger = l == Long.MIN_VALUE ? INFLATED_BIGINT : null;
      if (precision2 > 0) {
         if (l == Long.MIN_VALUE) {
            n = 19;

            for (int i = n - precision2; i > 0; i = n - precision2) {
               n2 = checkScaleNonZero((long)n2 - (long)i);
               shadedBigInteger = divideAndRoundByTenPow(shadedBigInteger, i, oldMode);
               l = compactValFor(shadedBigInteger);
               if (l != Long.MIN_VALUE) {
                  break;
               }

               n = bigDigitLength(shadedBigInteger);
            }
         }

         if (l != Long.MIN_VALUE) {
            n = longDigitLength(l);

            for (int j = n - precision2; j > 0; j = n - precision2) {
               n2 = checkScaleNonZero((long)n2 - (long)j);
               l = divideAndRound3(l, LONG_TEN_POWERS_TABLE[j], shadedMathContext.roundingMode.oldMode);
               n = longDigitLength(l);
            }

            shadedBigInteger = null;
         }
      }

      this.intVal = shadedBigInteger;
      this.intCompact = l;
      this.scale = n2;
      this.precision = n;
   }

   private static ShadedBigDecimal multiply2(long l, long l2, int n) {
      long l3 = multiply2(l, l2);
      return l3 != Long.MIN_VALUE ? valueOf(l3, n) : new ShadedBigDecimal(ShadedBigInteger.valueOf(l).multiply2(l2), Long.MIN_VALUE, n, 0);
   }

   private static ShadedBigDecimal add(long l, long l2, int n) {
      long l3 = add(l, l2);
      return l3 != Long.MIN_VALUE ? valueOf(l3, n) : new ShadedBigDecimal(ShadedBigInteger.valueOf(l).add2(l2), n);
   }

   private static ShadedBigDecimal divide2(ShadedBigInteger shadedBigInteger, int n, long l, int n2, int n3, int n4) {
      if (checkScale2(shadedBigInteger, (long)n3 + (long)n2) > n) {
         return divideAndRound(bigMultiplyPowerTen2(shadedBigInteger, (n3 + n2) - n), l, n3, n4, n3);
      } else {
         int n8 = checkScale(l, (long)n - (long)n3) - n2;
         long l4;
         if (n8 < LONG_TEN_POWERS_TABLE.length && (l4 = longMultiplyPowerTen(l, n8)) != Long.MIN_VALUE) {
            return divideAndRound(shadedBigInteger, l4, n3, n4, n3);
         } else {
            return divideAndRound3(shadedBigInteger, bigMultiplyPowerTen(l, n8), n3, n4, n3);
         }
      }
   }

   public byte byteValueExact() {
      long l = this.longValueExact();
      if ((long)((byte)((int)l)) != l) {
         throw new ArithmeticException("Overflow");
      } else {
         return (byte)((int)l);
      }
   }

   public static int longDigitLength(long l) {
      if (!assertionsDisabled && l == Long.MIN_VALUE) {
         throw new AssertionError();
      } else {
         if (l < 0L) {
            l = -l;
         }

         if (l < 10L) {
            return 1;
         } else {
            int n = (64 - Long.numberOfLeadingZeros(l) + 1) * 1233 >>> 12;
            long[] longs = LONG_TEN_POWERS_TABLE;
            return n < longs.length && l >= longs[n] ? n + 1 : n;
         }
      }
   }

   public ShadedBigDecimal subtract(ShadedBigDecimal shadedBigDecimal) {
      if (this.intCompact != Long.MIN_VALUE) {
         if (shadedBigDecimal.intCompact != Long.MIN_VALUE) {
            return add3(this.intCompact, this.scale, -shadedBigDecimal.intCompact, shadedBigDecimal.scale);
         } else {
            return add(this.intCompact, this.scale, shadedBigDecimal.intVal.negate(), shadedBigDecimal.scale);
         }
      } else if (shadedBigDecimal.intCompact != Long.MIN_VALUE) {
         long l = -shadedBigDecimal.intCompact;
         int scale4 = this.scale;
         return add(l, shadedBigDecimal.scale, this.intVal, scale4);
      } else {
         return add2(this.intVal, this.scale, shadedBigDecimal.intVal.negate(), shadedBigDecimal.scale);
      }
   }

   public ShadedBigDecimal movePointLeft(int n) {
      int n2 = this.checkScale((long)this.scale + (long)n);
      ShadedBigDecimal shadedBigDecimal = new ShadedBigDecimal(this.intVal, this.intCompact, n2, 0);
      ShadedBigDecimal shadedBigDecimal2;
      if (shadedBigDecimal.scale < 0) {
         shadedBigDecimal2 = shadedBigDecimal.setScale((byte)0, (byte)7);
      } else {
         shadedBigDecimal2 = shadedBigDecimal;
      }

      return shadedBigDecimal2;
   }

   private static ShadedBigDecimal add2(ShadedBigInteger shadedBigInteger, int n, ShadedBigInteger shadedBigInteger2, int n2) {
      int n3 = n;
      long l = (long)n - (long)n2;
      if (l != 0L) {
         if (l < 0L) {
            int n4 = checkScale2(shadedBigInteger, -l);
            n3 = n2;
            shadedBigInteger = bigMultiplyPowerTen2(shadedBigInteger, n4);
         } else {
            shadedBigInteger2 = bigMultiplyPowerTen2(shadedBigInteger2, checkScale2(shadedBigInteger2, l));
         }
      }

      ShadedBigInteger shadedBigInteger3 = shadedBigInteger.add(shadedBigInteger2);
      ShadedBigDecimal shadedBigDecimal;
      if (shadedBigInteger.signum == shadedBigInteger2.signum) {
         shadedBigDecimal = new ShadedBigDecimal(shadedBigInteger3, Long.MIN_VALUE, n3, 0);
      } else {
         shadedBigDecimal = valueOf(shadedBigInteger3, n3, (byte)0);
      }

      return shadedBigDecimal;
   }

   public ShadedBigDecimal(ShadedBigInteger shadedBigInteger, int n, ShadedMathContext shadedMathContext) {
      super();
      long l = compactValFor(shadedBigInteger);
      int precision2 = shadedMathContext.precision;
      int n2 = 0;
      if (precision2 > 0) {
         int oldMode = shadedMathContext.roundingMode.oldMode;
         if (l == Long.MIN_VALUE) {
            n2 = bigDigitLength(shadedBigInteger);

            for (int i = n2 - precision2; i > 0; i = n2 - precision2) {
               n = checkScaleNonZero((long)n - (long)i);
               shadedBigInteger = divideAndRoundByTenPow(shadedBigInteger, i, oldMode);
               l = compactValFor(shadedBigInteger);
               if (l != Long.MIN_VALUE) {
                  break;
               }

               n2 = bigDigitLength(shadedBigInteger);
            }
         }

         if (l != Long.MIN_VALUE) {
            n2 = longDigitLength(l);

            for (int j = n2 - precision2; j > 0; j = n2 - precision2) {
               n = checkScaleNonZero((long)n - (long)j);
               l = divideAndRound3(l, LONG_TEN_POWERS_TABLE[j], oldMode);
               n2 = longDigitLength(l);
            }

            shadedBigInteger = null;
         }
      }

      this.intVal = shadedBigInteger;
      this.intCompact = l;
      this.scale = n;
      this.precision = n2;
   }

   public static ShadedBigDecimal zeroValueOf(int n) {
      return n >= 0 && n < ZERO_SCALED_BY.length ? ZERO_SCALED_BY[n] : new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, n, 1);
   }

   public ShadedBigDecimal(char[] chars, int n, int n2) {
      this(chars, n, n2, ShadedMathContext.UNLIMITED);
   }

   private static int compareMagnitudeNormalized3(ShadedBigInteger shadedBigInteger, int n, ShadedBigInteger shadedBigInteger2, int n2) {
      int n3 = n - n2;
      if (n3 < 0) {
         return bigMultiplyPowerTen2(shadedBigInteger, -n3).compareMagnitude(shadedBigInteger2);
      } else {
         return shadedBigInteger.compareMagnitude(bigMultiplyPowerTen2(shadedBigInteger2, n3));
      }
   }

   public ShadedBigDecimal multiply(ShadedBigDecimal shadedBigDecimal) {
      int n = this.checkScale((long)this.scale + (long)shadedBigDecimal.scale);
      if (this.intCompact != Long.MIN_VALUE) {
         return shadedBigDecimal.intCompact != Long.MIN_VALUE ? multiply2(this.intCompact, shadedBigDecimal.intCompact, n) : multiply(this.intCompact, shadedBigDecimal.intVal, n);
      } else if (shadedBigDecimal.intCompact != Long.MIN_VALUE) {
         return multiply(shadedBigDecimal.intCompact, this.intVal, n);
      } else {
         ShadedBigInteger intVal2 = shadedBigDecimal.intVal;
         return multiply3(this.intVal, intVal2, n);
      }
   }

   public ShadedBigDecimal(String str, ShadedMathContext shadedMathContext) {
      this(str.toCharArray(), 0, str.length(), shadedMathContext);
   }

   static {
      short s = 24530;
      INFLATED_BIGINT = ShadedBigInteger.valueOf(Long.MIN_VALUE);
      zeroThroughTen = new ShadedBigDecimal[]{
         new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, 0, 1),
         new ShadedBigDecimal(ShadedBigInteger.ONE, 1L, 0, 1),
         new ShadedBigDecimal(ShadedBigInteger.valueOf(2L), 2L, 0, 1),
         new ShadedBigDecimal(ShadedBigInteger.valueOf(3L), 3L, 0, 1),
         new ShadedBigDecimal(ShadedBigInteger.valueOf(4L), 4L, 0, 1),
         new ShadedBigDecimal(ShadedBigInteger.valueOf(5L), 5L, 0, 1),
         new ShadedBigDecimal(ShadedBigInteger.valueOf(6L), 6L, 0, 1),
         new ShadedBigDecimal(ShadedBigInteger.valueOf(7L), 7L, 0, 1),
         new ShadedBigDecimal(ShadedBigInteger.valueOf(8L), 8L, 0, 1),
         new ShadedBigDecimal(ShadedBigInteger.valueOf(9L), 9L, 0, 1),
         new ShadedBigDecimal(ShadedBigInteger.TEN, 10L, 0, 2)
      };
      ZERO_SCALED_BY = new ShadedBigDecimal[]{
         zeroThroughTen[0],
         new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, 1, 1),
         new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, 2, 1),
         new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, 3, 1),
         new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, 4, 1),
         new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, 5, 1),
         new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, 6, 1),
         new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, 7, 1),
         new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, 8, 1),
         new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, 9, 1),
         new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, 10, 1),
         new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, 11, 1),
         new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, 12, 1),
         new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, 13, 1),
         new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, 14, 1),
         new ShadedBigDecimal(ShadedBigInteger.ZERO, 0L, 15, 1)
      };
      ZERO = zeroThroughTen[0];
      ONE = zeroThroughTen[1];
      TEN = zeroThroughTen[10];
      BIG_TEN_POWERS_TABLE = new ShadedBigInteger[]{
         ShadedBigInteger.ONE,
         ShadedBigInteger.valueOf(10L),
         ShadedBigInteger.valueOf(100L),
         ShadedBigInteger.valueOf(1000L),
         ShadedBigInteger.valueOf(10000L),
         ShadedBigInteger.valueOf(100000L),
         ShadedBigInteger.valueOf(1000000L),
         ShadedBigInteger.valueOf(10000000L),
         ShadedBigInteger.valueOf(100000000L),
         ShadedBigInteger.valueOf(1000000000L),
         ShadedBigInteger.valueOf(10000000000L),
         ShadedBigInteger.valueOf(100000000000L),
         ShadedBigInteger.valueOf(1000000000000L),
         ShadedBigInteger.valueOf(10000000000000L),
         ShadedBigInteger.valueOf(100000000000000L),
         ShadedBigInteger.valueOf(1000000000000000L),
         ShadedBigInteger.valueOf(10000000000000000L),
         ShadedBigInteger.valueOf(100000000000000000L),
         ShadedBigInteger.valueOf(1000000000000000000L)
      };
      short s2 = 14352;
      BIG_TEN_POWERS_TABLE_INITLEN = BIG_TEN_POWERS_TABLE.length;
      BIG_TEN_POWERS_TABLE_MAX = 16 * BIG_TEN_POWERS_TABLE_INITLEN;
   }

   private static ShadedBigDecimal divideAndRound(ShadedBigInteger shadedBigInteger, long l, int n, int n2, int n3) {
      long l2 = 0L;
      Object obj = null;
      MutableBigInt mutableBigInt = new MutableBigInt(shadedBigInteger.mag);
      obj = new MutableBigInt();
      l2 = mutableBigInt.divide(l, (MutableBigInt)obj);
      boolean flag = l2 == 0L;
      int n4 = l < 0L ? -shadedBigInteger.signum : shadedBigInteger.signum;
      if (!flag) {
         if (needIncrement(l, n2, n4, (MutableBigInt)obj, l2)) {
            MutableBigInt one = MutableBigInt.ONE;
            ((MutableBigInt)obj).add(one);
         }

         return ((MutableBigInt)obj).toBigDecimal(n4, n);
      } else if (n3 != n) {
         long l3 = ((MutableBigInt)obj).toCompactValue(n4);
         if (l3 != Long.MIN_VALUE) {
            return createAndStripZerosToMatchScale(l3, n, (long)n3);
         } else {
            return createAndStripZerosToMatchScale2(((MutableBigInt)obj).toBigInteger(n4), n, (long)n3);
         }
      } else {
         return ((MutableBigInt)obj).toBigDecimal(n4, n);
      }
   }

   private static ShadedBigDecimal divideSmallFastPath(long l, int n, long l2, int n2, long l3, ShadedMathContext shadedMathContext) {
      int precision2 = shadedMathContext.precision;
      int oldMode = shadedMathContext.roundingMode.oldMode;
      if (assertionsDisabled || n <= n2 && n2 < 18 && precision2 < 18) {
         int n3 = n2 - n;
         long l4 = n3 == 0 ? l : longMultiplyPowerTen(l, n3);
         int n4 = longCompareMagnitude(l4, l2);
         ShadedBigDecimal shadedBigDecimal;
         if (n4 > 0) {
            n2--;
            int n5 = checkScaleNonZero(l3 + (long)n2 - (long)n + (long)precision2);
            if (checkScaleNonZero((long)precision2 + (long)n2 - (long)n) > 0) {
               int n6 = checkScaleNonZero((long)precision2 + (long)n2 - (long)n);
               long l5;
               if ((l5 = longMultiplyPowerTen(l, n6)) == Long.MIN_VALUE) {
                  shadedBigDecimal = null;
                  if (precision2 - 1 >= 0 && precision2 - 1 < LONG_TEN_POWERS_TABLE.length) {
                     shadedBigDecimal = multiplyDivideAndRound(LONG_TEN_POWERS_TABLE[precision2 - 1], l4, l2, n5, oldMode, checkScaleNonZero(l3));
                  }

                  if (shadedBigDecimal == null) {
                     shadedBigDecimal = divideAndRound(bigMultiplyPowerTen(l4, precision2 - 1), l2, n5, oldMode, checkScaleNonZero(l3));
                  }
               } else {
                  shadedBigDecimal = divideAndRound2(l5, l2, n5, oldMode, checkScaleNonZero(l3));
               }
            } else {
               int n9 = checkScaleNonZero((long)n - (long)precision2);
               if (n9 == n2) {
                  shadedBigDecimal = divideAndRound2(l, l2, n5, oldMode, checkScaleNonZero(l3));
               } else {
                  int n10 = checkScaleNonZero((long)n9 - (long)n2);
                  long l6;
                  if ((l6 = longMultiplyPowerTen(l2, n10)) == Long.MIN_VALUE) {
                     ShadedBigInteger shadedBigInteger2 = bigMultiplyPowerTen(l2, n10);
                     shadedBigDecimal = divideAndRound3(ShadedBigInteger.valueOf(l), shadedBigInteger2, n5, oldMode, checkScaleNonZero(l3));
                  } else {
                     shadedBigDecimal = divideAndRound2(l, l6, n5, oldMode, checkScaleNonZero(l3));
                  }
               }
            }
         } else {
            int n12 = checkScaleNonZero(l3 + (long)n2 - (long)n + (long)precision2);
            if (n4 == 0) {
               shadedBigDecimal = roundedTenPower(l4 < 0L == l2 < 0L ? 1 : -1, precision2, n12, checkScaleNonZero(l3));
            } else {
               long l7;
               if ((l7 = longMultiplyPowerTen(l4, precision2)) == Long.MIN_VALUE) {
                  shadedBigDecimal = null;
                  if (precision2 < LONG_TEN_POWERS_TABLE.length) {
                     shadedBigDecimal = multiplyDivideAndRound(LONG_TEN_POWERS_TABLE[precision2], l4, l2, n12, oldMode, checkScaleNonZero(l3));
                  }

                  if (shadedBigDecimal == null) {
                     shadedBigDecimal = divideAndRound(bigMultiplyPowerTen(l4, precision2), l2, n12, oldMode, checkScaleNonZero(l3));
                  }
               } else {
                  shadedBigDecimal = divideAndRound2(l7, l2, n12, oldMode, checkScaleNonZero(l3));
               }
            }
         }

         return doRound(shadedBigDecimal, shadedMathContext);
      } else {
         throw new AssertionError();
      }
   }

   private static ShadedBigDecimal add3(long l, int n, long l2, int n2) {
      long l3 = (long)n - (long)n2;
      if (l3 == 0L) {
         return add(l, l2, n);
      } else if (l3 < 0L) {
         int n3 = checkScale(l, -l3);
         long l4 = longMultiplyPowerTen(l, n3);
         if (l4 != Long.MIN_VALUE) {
            return add(l4, l2, n2);
         } else {
            ShadedBigInteger shadedBigInteger = bigMultiplyPowerTen(l, n3).add2(l2);
            ShadedBigDecimal shadedBigDecimal;
            if ((l ^ l2) >= 0L) {
               shadedBigDecimal = new ShadedBigDecimal(shadedBigInteger, Long.MIN_VALUE, n2, 0);
            } else {
               shadedBigDecimal = valueOf(shadedBigInteger, n2, (byte)0);
            }

            return shadedBigDecimal;
         }
      } else {
         int n4 = checkScale(l2, l3);
         long l5 = longMultiplyPowerTen(l2, n4);
         if (l5 != Long.MIN_VALUE) {
            return add(l, l5, n);
         } else {
            ShadedBigInteger shadedBigInteger2 = bigMultiplyPowerTen(l2, n4).add2(l);
            ShadedBigDecimal shadedBigDecimal2;
            if ((l ^ l2) >= 0L) {
               shadedBigDecimal2 = new ShadedBigDecimal(shadedBigInteger2, Long.MIN_VALUE, n, 0);
            } else {
               shadedBigDecimal2 = valueOf(shadedBigInteger2, n, (byte)0);
            }

            return shadedBigDecimal2;
         }
      }
   }

   public ShadedBigDecimal abs() {
      ShadedBigDecimal shadedBigDecimal;
      if (this.signum() < 0) {
         shadedBigDecimal = this.negate();
      } else {
         shadedBigDecimal = this;
      }

      return shadedBigDecimal;
   }

   public ShadedBigDecimal movePointRight(int n) {
      int n2 = this.checkScale((long)this.scale - (long)n);
      ShadedBigDecimal shadedBigDecimal = new ShadedBigDecimal(this.intVal, this.intCompact, n2, 0);
      ShadedBigDecimal shadedBigDecimal2;
      if (shadedBigDecimal.scale < 0) {
         shadedBigDecimal2 = shadedBigDecimal.setScale((byte)0, (byte)7);
      } else {
         shadedBigDecimal2 = shadedBigDecimal;
      }

      return shadedBigDecimal2;
   }

   public ShadedBigDecimal(String str) {
      this(str.toCharArray(), 0, str.length());
   }

   private static boolean commonNeedIncrement(int n, int n2, int n3, boolean flag) {
      switch (n) {
         case 0:
            return true;
         case 1:
            return false;
         case 2:
            return n2 > 0;
         case 3:
            return n2 < 0;
         case 4:
         case 5:
         case 6:
         default:
            if (assertionsDisabled || n >= 4 && n <= 6) {
               if (n3 < 0) {
                  return false;
               } else if (n3 > 0) {
                  return true;
               } else if (!assertionsDisabled && n3 != 0) {
                  throw new AssertionError();
               } else {
                  switch (n) {
                     case 4:
                        return true;
                     case 5:
                        return false;
                     case 6:
                        return flag;
                     default:
                        throw new AssertionError("Unexpected rounding mode" + n);
                  }
               }
            } else {
               throw new AssertionError("Unexpected rounding mode" + ShadedRoundingMode.valueOf2(n));
            }
         case 7:
            throw new ArithmeticException("Rounding necessary");
      }
   }

   public ShadedBigDecimal setScale(int n) {
      return this.setScale(n, (byte)7);
   }

   @Override
   public String toString() {
      String stringCache2 = this.stringCache;
      if (stringCache2 == null) {
         this.stringCache = stringCache2 = this.layoutChars(true);
      }

      return stringCache2;
   }

   public ShadedBigDecimal(char[] chars, ShadedMathContext shadedMathContext) {
      this(chars, 0, chars.length, shadedMathContext);
   }

   private static ShadedBigDecimal add(long l, int n, ShadedBigInteger shadedBigInteger, int n2) {
      int n3 = n;
      long l2 = (long)n - (long)n2;
      boolean flag = Long.signum(l) == shadedBigInteger.signum;
      ShadedBigInteger shadedBigInteger2;
      if (l2 < 0L) {
         int n4 = checkScale(l, -l2);
         n3 = n2;
         long l3 = longMultiplyPowerTen(l, n4);
         if (l3 == Long.MIN_VALUE) {
            shadedBigInteger2 = shadedBigInteger.add(bigMultiplyPowerTen(l, n4));
         } else {
            shadedBigInteger2 = shadedBigInteger.add2(l3);
         }
      } else {
         shadedBigInteger = bigMultiplyPowerTen2(shadedBigInteger, checkScale2(shadedBigInteger, l2));
         shadedBigInteger2 = shadedBigInteger.add2(l);
      }

      ShadedBigDecimal shadedBigDecimal;
      if (flag) {
         shadedBigDecimal = new ShadedBigDecimal(shadedBigInteger2, Long.MIN_VALUE, n3, 0);
      } else {
         shadedBigDecimal = valueOf(shadedBigInteger2, n3, (byte)0);
      }

      return shadedBigDecimal;
   }

   private ShadedBigDecimal[] preAlign(ShadedBigDecimal shadedBigDecimal, ShadedBigDecimal shadedBigDecimal2, long l, ShadedMathContext shadedMathContext) {
      if (!assertionsDisabled && l == 0L) {
         throw new AssertionError();
      } else {
         ShadedBigDecimal shadedBigDecimal3;
         ShadedBigDecimal shadedBigDecimal4;
         if (l < 0L) {
            shadedBigDecimal3 = shadedBigDecimal;
            shadedBigDecimal4 = shadedBigDecimal2;
         } else {
            shadedBigDecimal3 = shadedBigDecimal2;
            shadedBigDecimal4 = shadedBigDecimal;
         }

         long l2 = (long)shadedBigDecimal3.scale - (long)shadedBigDecimal3.precision() + (long)shadedMathContext.precision;
         long l3 = (long)shadedBigDecimal4.scale - (long)shadedBigDecimal4.precision() + 1L;
         if (l3 > (long)(shadedBigDecimal3.scale + 2) && l3 > l2 + 2L) {
            shadedBigDecimal4 = valueOf((long)shadedBigDecimal4.signum(), this.checkScale(Math.max((long)shadedBigDecimal3.scale, l2) + 3L));
         }

         return new ShadedBigDecimal[]{shadedBigDecimal3, shadedBigDecimal4};
      }
   }

   private static boolean needIncrement2(MutableBigInt mutableBigInt, int n, int n2, MutableBigInt mutableBigInt2, MutableBigInt mutableBigInt3) {
      if (!assertionsDisabled && mutableBigInt3.isZero()) {
         throw new AssertionError();
      } else {
         return commonNeedIncrement(n, n2, mutableBigInt3.compareHalf(mutableBigInt), mutableBigInt2.isOdd());
      }
   }

   public int signum() {
      int n;
      if (this.intCompact != Long.MIN_VALUE) {
         n = Long.signum(this.intCompact);
      } else {
         n = this.intVal.getSignum();
      }

      return n;
   }

   private static ShadedBigDecimal multiplyDivideAndRound(long l, long l2, long l3, int n, int n2, int n3) {
      int n4 = Long.signum(l) * Long.signum(l2) * Long.signum(l3);
      l = Math.abs(l);
      l2 = Math.abs(l2);
      l3 = Math.abs(l3);
      long l4 = l >>> 32;
      long l5 = l & 4294967295L;
      long l6 = l2 >>> 32;
      long l7 = l2 & 4294967295L;
      long l8 = l5 * l7;
      long l9 = l8 & 4294967295L;
      long l10 = l8 >>> 32;
      l8 = l4 * l7 + l10;
      l10 = l8 & 4294967295L;
      long l11 = l8 >>> 32;
      l8 = l5 * l6 + l10;
      l10 = l8 & 4294967295L;
      l11 += l8 >>> 32;
      long l12 = l11 >>> 32;
      l11 &= 4294967295L;
      l8 = l4 * l6 + l11;
      l11 = l8 & 4294967295L;
      l12 = (l8 >>> 32) + l12 & 4294967295L;
      return divideAndRound128(make64(l12, l11), make64(l10, l9), l3, n4, n, n2, n3);
   }

   private static ShadedBigDecimal divide4(long l, int n, long l2, int n2, long l3, ShadedMathContext shadedMathContext) {
      int precision2 = shadedMathContext.precision;
      if (n <= n2 && n2 < 18 && precision2 < 18) {
         return divideSmallFastPath(l, n, l2, n2, l3, shadedMathContext);
      } else {
         if (compareMagnitudeNormalized2(l, n, l2, n2) > 0) {
            n2--;
         }

         int oldMode = shadedMathContext.roundingMode.oldMode;
         int n3 = checkScaleNonZero(l3 + (long)n2 - (long)n + (long)precision2);
         ShadedBigDecimal shadedBigDecimal;
         if (checkScaleNonZero((long)precision2 + (long)n2 - (long)n) > 0) {
            int n4 = checkScaleNonZero((long)precision2 + (long)n2 - (long)n);
            long l4;
            if ((l4 = longMultiplyPowerTen(l, n4)) == Long.MIN_VALUE) {
               shadedBigDecimal = divideAndRound(bigMultiplyPowerTen(l, n4), l2, n3, oldMode, checkScaleNonZero(l3));
            } else {
               shadedBigDecimal = divideAndRound2(l4, l2, n3, oldMode, checkScaleNonZero(l3));
            }
         } else {
            int n7 = checkScaleNonZero((long)n - (long)precision2);
            if (n7 == n2) {
               shadedBigDecimal = divideAndRound2(l, l2, n3, oldMode, checkScaleNonZero(l3));
            } else {
               int n8 = checkScaleNonZero((long)n7 - (long)n2);
               long l5;
               if ((l5 = longMultiplyPowerTen(l2, n8)) == Long.MIN_VALUE) {
                  ShadedBigInteger shadedBigInteger2 = bigMultiplyPowerTen(l2, n8);
                  shadedBigDecimal = divideAndRound3(ShadedBigInteger.valueOf(l), shadedBigInteger2, n3, oldMode, checkScaleNonZero(l3));
               } else {
                  shadedBigDecimal = divideAndRound2(l, l5, n3, oldMode, checkScaleNonZero(l3));
               }
            }
         }

         return doRound(shadedBigDecimal, shadedMathContext);
      }
   }

   public static ShadedBigDecimal valueOf(double d) {
      return new ShadedBigDecimal(Double.toString(d));
   }

   private static ShadedBigDecimal multiplyAndRound2(long l, ShadedBigInteger shadedBigInteger, int n, ShadedMathContext shadedMathContext) {
      return l == 0L ? zeroValueOf(n) : doRound2(shadedBigInteger.multiply2(l), n, shadedMathContext);
   }

   private ShadedBigDecimal audit() {
      if (this.intCompact == Long.MIN_VALUE) {
         if (this.intVal == null) {
            print("audit", this);
            throw new AssertionError("null intVal");
         }

         if (this.precision > 0 && this.precision != bigDigitLength(this.intVal)) {
            print("audit", this);
            throw new AssertionError("precision mismatch");
         }
      } else {
         if (this.intVal != null) {
            long l = this.intVal.longValue();
            if (l != this.intCompact) {
               print("audit", this);
               throw new AssertionError("Inconsistent state, intCompact=" + this.intCompact + "\t intVal=" + l);
            }
         }

         if (this.precision > 0 && this.precision != longDigitLength(this.intCompact)) {
            short s = 21193;
            print("audit", this);
            throw new AssertionError("precision mismatch");
         }
      }

      return this;
   }

   private static long add(long l, long l2) {
      long l3 = l + l2;
      if (((l3 ^ l) & (l3 ^ l2)) >= 0L) {
         return l3;
      } else {
         return Long.MIN_VALUE;
      }
   }

   public ShadedBigDecimal(ShadedBigInteger shadedBigInteger, int n) {
      super();
      this.intVal = shadedBigInteger;
      this.intCompact = compactValFor(shadedBigInteger);
      this.scale = n;
   }

   @Override
   public int hashCode() {
      if (this.intCompact != Long.MIN_VALUE) {
         long l = this.intCompact < 0L ? -this.intCompact : this.intCompact;
         int n = (int)((long)((int)(l >>> 32) * 31) + (l & 4294967295L));
         return 31 * (this.intCompact < 0L ? -n : n) + this.scale;
      } else {
         return 31 * this.intVal.hashCode() + this.scale;
      }
   }

   private static ShadedBigDecimal multiply3(ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2, int n) {
      return new ShadedBigDecimal(shadedBigInteger.multiply(shadedBigInteger2), Long.MIN_VALUE, n, 0);
   }

   public ShadedBigDecimal[] divideAndRemainder(ShadedBigDecimal shadedBigDecimal, ShadedMathContext shadedMathContext) {
      if (shadedMathContext.precision == 0) {
         return this.divideAndRemainder(shadedBigDecimal);
      } else {
         ShadedBigDecimal[] shadedBigDecimals = new ShadedBigDecimal[]{this.divideToIntegralValue(shadedBigDecimal, shadedMathContext), null};
         shadedBigDecimals[1] = this.subtract(shadedBigDecimals[0].multiply(shadedBigDecimal));
         return shadedBigDecimals;
      }
   }

   public ShadedBigDecimal(double d) {
      this(d, ShadedMathContext.UNLIMITED);
   }

   private static ShadedBigDecimal roundedTenPower(int n, int n2, int n3, int n4) {
      if (n3 > n4) {
         int n5 = n3 - n4;
         return n5 < n2 ? scaledTenPow(n2 - n5, n, n4) : valueOf((long)n, n3 - n2);
      } else {
         return scaledTenPow(n2, n, n3);
      }
   }

   public ShadedBigDecimal stripTrailingZeros() {
      exitBlock:
      if (this.intCompact != 0L) {
         if (this.intVal != null) {
            if (this.intVal.getSignum() == 0) {
               break exitBlock;
            }
         }

         if (this.intCompact != Long.MIN_VALUE) {
            return createAndStripZerosToMatchScale(this.intCompact, this.scale, Long.MIN_VALUE);
         }

         long minvalue = Long.MIN_VALUE;
         int scale2 = this.scale;
         return createAndStripZerosToMatchScale2(this.intVal, scale2, minvalue);
      }

      return ZERO;
   }

   private static ShadedBigInteger divideAndRoundByTenPow(ShadedBigInteger shadedBigInteger, int n, int n2) {
      if (n < LONG_TEN_POWERS_TABLE.length) {
         shadedBigInteger = divideAndRound(shadedBigInteger, LONG_TEN_POWERS_TABLE[n], n2);
      } else {
         shadedBigInteger = divideAndRound2(shadedBigInteger, bigTenToThe(n), n2);
      }

      return shadedBigInteger;
   }

   private static int checkScale2(ShadedBigInteger shadedBigInteger, long l) {
      int n = (int)l;
      if ((long)n != l) {
         n = l > 2147483647L ? Integer.MAX_VALUE : Integer.MIN_VALUE;
         if (shadedBigInteger.getSignum() != 0) {
            throw new ArithmeticException(n > 0 ? "Underflow" : "Overflow");
         }
      }

      return n;
   }

   private static ShadedBigDecimal divide6(long l, int n, long l2, int n2, int n3, int n4) {
      if (checkScale(l, (long)n3 + (long)n2) > n) {
         int n6 = (n3 + n2) - n;
         if (n6 < LONG_TEN_POWERS_TABLE.length) {
            long l3;
            if ((l3 = longMultiplyPowerTen(l, n6)) != Long.MIN_VALUE) {
               return divideAndRound2(l3, l2, n3, n4, n3);
            }

            ShadedBigDecimal shadedBigDecimal = multiplyDivideAndRound(LONG_TEN_POWERS_TABLE[n6], l, l2, n3, n4, n3);
            if (shadedBigDecimal != null) {
               return shadedBigDecimal;
            }
         }

         return divideAndRound(bigMultiplyPowerTen(l, n6), l2, n3, n4, n3);
      } else {
         int n8 = checkScale(l2, (long)n - (long)n3) - n2;
         long l4;
         if (n8 < LONG_TEN_POWERS_TABLE.length && (l4 = longMultiplyPowerTen(l2, n8)) != Long.MIN_VALUE) {
            return divideAndRound2(l, l4, n3, n4, n3);
         } else {
            ShadedBigInteger shadedBigInteger2 = bigMultiplyPowerTen(l2, n8);
            return divideAndRound3(ShadedBigInteger.valueOf(l), shadedBigInteger2, n3, n4, n3);
         }
      }
   }

   private static boolean needIncrement(long l, int n, int n2, MutableBigInt mutableBigInt, long l2) {
      if (!assertionsDisabled && l2 == 0L) {
         throw new AssertionError();
      } else {
         int n3;
         if (l2 > -4611686018427387904L && l2 <= 4611686018427387903L) {
            n3 = longCompareMagnitude(2L * l2, l);
         } else {
            n3 = 1;
         }

         return commonNeedIncrement(n, n2, n3, mutableBigInt.isOdd());
      }
   }

   private static ShadedBigDecimal divideAndRound3(ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2, int n, int n2, int n3) {
      MutableBigInt mutableBigInt = new MutableBigInt(shadedBigInteger.mag);
      MutableBigInt mutableBigInt2 = new MutableBigInt();
      MutableBigInt mutableBigInt3 = new MutableBigInt(shadedBigInteger2.mag);
      MutableBigInt mutableBigInt4 = mutableBigInt.divide2(mutableBigInt3, mutableBigInt2);
      boolean isZero = mutableBigInt4.isZero();
      int n4 = shadedBigInteger.signum != shadedBigInteger2.signum ? -1 : 1;
      if (!isZero) {
         if (needIncrement2(mutableBigInt3, n2, n4, mutableBigInt2, mutableBigInt4)) {
            mutableBigInt2.add(MutableBigInt.ONE);
         }

         return mutableBigInt2.toBigDecimal(n4, n);
      } else if (n3 != n) {
         long l = mutableBigInt2.toCompactValue(n4);
         if (l != Long.MIN_VALUE) {
            return createAndStripZerosToMatchScale(l, n, (long)n3);
         } else {
            return createAndStripZerosToMatchScale2(mutableBigInt2.toBigInteger(n4), n, (long)n3);
         }
      } else {
         return mutableBigInt2.toBigDecimal(n4, n);
      }
   }

   private static int saturateLong(long l) {
      int n = (int)l;
      return l == (long)n ? n : (l < 0L ? Integer.MIN_VALUE : Integer.MAX_VALUE);
   }

   public ShadedBigDecimal divide(ShadedBigDecimal shadedBigDecimal, int n, ShadedRoundingMode shadedRoundingMode) {
      return this.divide2(shadedBigDecimal, n, shadedRoundingMode.oldMode);
   }

   public ShadedBigDecimal abs(ShadedMathContext shadedMathContext) {
      return this.signum() < 0 ? this.negate(shadedMathContext) : this.round(shadedMathContext);
   }

   public ShadedBigInteger toBigIntegerExact() {
      return this.setScale((byte)0, (byte)7).inflated();
   }

   public ShadedBigDecimal setScale(int n, int n2) {
      if (n2 >= 0 && n2 <= 7) {
         int scale2 = this.scale;
         if (n == scale2) {
            return this;
         } else if (this.signum() == 0) {
            return zeroValueOf(n);
         } else if (this.intCompact != Long.MIN_VALUE) {
            long intCompact2 = this.intCompact;
            if (n > scale2) {
               int n3 = this.checkScale((long)n - (long)scale2);
               if ((intCompact2 = longMultiplyPowerTen(intCompact2, n3)) != Long.MIN_VALUE) {
                  return valueOf(intCompact2, n);
               } else {
                  ShadedBigInteger shadedBigInteger = this.bigMultiplyPowerTen(n3);
                  int n4 = this.precision > 0 ? this.precision + n3 : 0;
                  long minvalue = Long.MIN_VALUE;
                  return new ShadedBigDecimal(shadedBigInteger, minvalue, n, n4);
               }
            } else {
               int n5 = this.checkScale((long)scale2 - (long)n);
               if (n5 < LONG_TEN_POWERS_TABLE.length) {
                  return divideAndRound2(intCompact2, LONG_TEN_POWERS_TABLE[n5], n, n2, n);
               } else {
                  return divideAndRound3(this.inflated(), bigTenToThe(n5), n, n2, n);
               }
            }
         } else if (n > scale2) {
            int n6 = this.checkScale((long)n - (long)scale2);
            ShadedBigInteger shadedBigInteger4 = bigMultiplyPowerTen2(this.intVal, n6);
            int n7 = this.precision > 0 ? this.precision + n6 : 0;
            long minvalue2 = Long.MIN_VALUE;
            return new ShadedBigDecimal(shadedBigInteger4, minvalue2, n, n7);
         } else {
            int n8 = this.checkScale((long)scale2 - (long)n);
            if (n8 < LONG_TEN_POWERS_TABLE.length) {
               long l6 = LONG_TEN_POWERS_TABLE[n8];
               return divideAndRound(this.intVal, l6, n, n2, n);
            } else {
               return divideAndRound3(this.intVal, bigTenToThe(n8), n, n2, n);
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid rounding mode");
      }
   }

   private static long[] divRemNegativeLong(long l, long l2) {
      if (!assertionsDisabled && l >= 0L) {
         throw new AssertionError("Non-negative numerator " + l);
      } else if (!assertionsDisabled && l2 == 1L) {
         throw new AssertionError("Unity denominator");
      } else {
         long l3 = (l >>> 1) / (l2 >>> 1);

         long l4;
         for (l4 = l - l3 * l2; l4 < 0L; l3--) {
            l4 += l2;
         }

         while (l4 >= l2) {
            l4 -= l2;
            l3++;
         }

         return new long[]{l4, l3};
      }
   }

   private static int longCompareMagnitude(long l, long l2) {
      if (l < 0L) {
         l = -l;
      }

      if (l2 < 0L) {
         l2 = -l2;
      }

      return l < l2 ? -1 : (l == l2 ? 0 : 1);
   }

   @Override
   public int intValue() {
      return this.intCompact != Long.MIN_VALUE && this.scale == 0 ? (int)this.intCompact : (int)this.longValue();
   }

   private static int checkScale(long l, long l2) {
      int n = (int)l2;
      if ((long)n != l2) {
         n = l2 > 2147483647L ? Integer.MAX_VALUE : Integer.MIN_VALUE;
         if (l != 0L) {
            throw new ArithmeticException(n > 0 ? "Underflow" : "Overflow");
         }
      }

      return n;
   }

   private static ShadedBigInteger divideAndRound2(ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2, int n) {
      MutableBigInt mutableBigInt = new MutableBigInt(shadedBigInteger.mag);
      MutableBigInt mutableBigInt2 = new MutableBigInt();
      MutableBigInt mutableBigInt3 = new MutableBigInt(shadedBigInteger2.mag);
      MutableBigInt mutableBigInt4 = mutableBigInt.divide2(mutableBigInt3, mutableBigInt2);
      boolean isZero = mutableBigInt4.isZero();
      int n2 = shadedBigInteger.signum != shadedBigInteger2.signum ? -1 : 1;
      if (!isZero && needIncrement2(mutableBigInt3, n, n2, mutableBigInt2, mutableBigInt4)) {
         mutableBigInt2.add(MutableBigInt.ONE);
      }

      return mutableBigInt2.toBigInteger(n2);
   }

   public ShadedBigDecimal(char[] chars, int n, int n2, ShadedMathContext shadedMathContext) {
      super();
      if ((chars.length | n2 | n) >= 0 && n2 <= chars.length - n) {
         int n3 = 0;
         int n4 = 0;
         long l = 0L;
         ShadedBigInteger shadedBigInteger = null;

         try {
            boolean flag = false;
            if (chars[n] == '-') {
               flag = true;
               n++;
               n2--;
            } else if (chars[n] == '+') {
               n++;
               n2--;
            }

            boolean flag2 = false;
            long l2 = 0L;
            boolean flag3 = n2 <= 18;
            int n5 = 0;
            if (!flag3) {
               char[] chars2;
               for (chars2 = new char[n2]; n2 > 0; n2--) {
                  char ch = chars[n];
                  if ((ch < '0' || ch > '9') && !Character.isDigit(ch)) {
                     if (ch != '.') {
                        if (ch != 'e' && ch != 'E') {
                           throw new NumberFormatException();
                        }

                        l2 = parseExp(chars, n, n2);
                        if ((long)((int)l2) != l2) {
                           throw new NumberFormatException();
                        }
                        break;
                     }

                     if (flag2) {
                        throw new NumberFormatException();
                     }

                     flag2 = true;
                  } else {
                     if (ch != '0' && Character.digit(ch, 10) != 0) {
                        if (n3 != 1 || n5 != 0) {
                           n3++;
                        }

                        chars2[n5++] = ch;
                     } else if (n3 == 0) {
                        chars2[n5] = ch;
                        n3 = 1;
                     } else if (n5 != 0) {
                        chars2[n5++] = ch;
                        n3++;
                     }

                     if (flag2) {
                        n4++;
                     }
                  }

                  n++;
               }

               if (n3 == 0) {
                  throw new NumberFormatException();
               }

               if (l2 != 0L) {
                  n4 = this.adjustScale(n4, l2);
               }

               shadedBigInteger = new ShadedBigInteger(chars2, flag ? -1 : 1, n3);
               l = compactValFor(shadedBigInteger);
               int precision2 = shadedMathContext.precision;
               if (precision2 > 0 && n3 > precision2) {
                  if (l == Long.MIN_VALUE) {
                     for (int i = n3 - precision2; i > 0; i = n3 - precision2) {
                        n4 = checkScaleNonZero((long)n4 - (long)i);
                        shadedBigInteger = divideAndRoundByTenPow(shadedBigInteger, i, shadedMathContext.roundingMode.oldMode);
                        l = compactValFor(shadedBigInteger);
                        if (l != Long.MIN_VALUE) {
                           n3 = longDigitLength(l);
                           break;
                        }

                        n3 = bigDigitLength(shadedBigInteger);
                     }
                  }

                  if (l != Long.MIN_VALUE) {
                     for (int j = n3 - precision2; j > 0; j = n3 - precision2) {
                        n4 = checkScaleNonZero((long)n4 - (long)j);
                        l = divideAndRound3(l, LONG_TEN_POWERS_TABLE[j], shadedMathContext.roundingMode.oldMode);
                        n3 = longDigitLength(l);
                     }

                     shadedBigInteger = null;
                  }
               }
            } else {
               while (n2 > 0) {
                  char ch2 = chars[n];
                  if (ch2 == '0') {
                     if (n3 == 0) {
                        n3 = 1;
                     } else if (l != 0L) {
                        l *= 10L;
                        n3++;
                     }

                     if (flag2) {
                        n4++;
                     }
                  } else if (ch2 >= '1' && ch2 <= '9') {
                     int n6 = ch2 - '0';
                     if (n3 != 1 || l != 0L) {
                        n3++;
                     }

                     l = l * 10L + (long)n6;
                     if (flag2) {
                        n4++;
                     }
                  } else if (ch2 == '.') {
                     if (flag2) {
                        throw new NumberFormatException();
                     }

                     flag2 = true;
                  } else {
                     if (!Character.isDigit(ch2)) {
                        if (ch2 != 'e' && ch2 != 'E') {
                           throw new NumberFormatException();
                        }

                        l2 = parseExp(chars, n, n2);
                        if ((long)((int)l2) != l2) {
                           throw new NumberFormatException();
                        }
                        break;
                     }

                     int n7 = Character.digit(ch2, 10);
                     if (n7 == 0) {
                        if (n3 == 0) {
                           n3 = 1;
                        } else if (l != 0L) {
                           l *= 10L;
                           n3++;
                        }
                     } else {
                        if (n3 != 1 || l != 0L) {
                           n3++;
                        }

                        l = l * 10L + (long)n7;
                     }

                     if (flag2) {
                        n4++;
                     }
                  }

                  n++;
                  n2--;
               }

               if (n3 == 0) {
                  throw new NumberFormatException();
               }

               if (l2 != 0L) {
                  n4 = this.adjustScale(n4, l2);
               }

               l = flag ? -l : l;
               int precision3 = shadedMathContext.precision;
               int n8 = n3 - precision3;
               if (precision3 > 0 && n8 > 0) {
                  while (n8 > 0) {
                     n4 = checkScaleNonZero((long)n4 - (long)n8);
                     l = divideAndRound3(l, LONG_TEN_POWERS_TABLE[n8], shadedMathContext.roundingMode.oldMode);
                     n3 = longDigitLength(l);
                     n8 = n3 - precision3;
                  }
               }
            }
         } catch (ArrayIndexOutOfBoundsException e) {
            throw new NumberFormatException();
         } catch (NegativeArraySizeException e2) {
            throw new NumberFormatException();
         }

         this.scale = n4;
         this.precision = n3;
         this.intCompact = l;
         this.intVal = shadedBigInteger;
      } else {
         throw new NumberFormatException("Bad offset or len arguments for char[] input.");
      }
   }

   public ShadedBigDecimal min(ShadedBigDecimal shadedBigDecimal) {
      return this.compareTo2(shadedBigDecimal) <= 0 ? this : shadedBigDecimal;
   }

   private static ShadedBigDecimal divide3(long l, int n, ShadedBigInteger shadedBigInteger, int n2, long l2, ShadedMathContext shadedMathContext) {
      if (compareMagnitudeNormalized(l, n, shadedBigInteger, n2) > 0) {
         n2--;
      }

      int precision2 = shadedMathContext.precision;
      int oldMode = shadedMathContext.roundingMode.oldMode;
      int n3 = checkScaleNonZero(l2 + (long)n2 - (long)n + (long)precision2);
      ShadedBigDecimal shadedBigDecimal;
      if (checkScaleNonZero((long)precision2 + (long)n2 - (long)n) > 0) {
         shadedBigDecimal = divideAndRound3(bigMultiplyPowerTen(l, checkScaleNonZero((long)precision2 + (long)n2 - (long)n)), shadedBigInteger, n3, oldMode, checkScaleNonZero(l2));
      } else {
         ShadedBigInteger shadedBigInteger3 = bigMultiplyPowerTen2(shadedBigInteger, checkScaleNonZero((long)checkScaleNonZero((long)n - (long)precision2) - (long)n2));
         shadedBigDecimal = divideAndRound3(ShadedBigInteger.valueOf(l), shadedBigInteger3, n3, oldMode, checkScaleNonZero(l2));
      }

      return doRound(shadedBigDecimal, shadedMathContext);
   }

   public int compareTo2(ShadedBigDecimal shadedBigDecimal) {
      if (this.scale == shadedBigDecimal.scale) {
         long intCompact2 = this.intCompact;
         long intCompact3 = shadedBigDecimal.intCompact;
         if (intCompact2 != Long.MIN_VALUE && intCompact3 != Long.MIN_VALUE) {
            return intCompact2 != intCompact3 ? (intCompact2 > intCompact3 ? 1 : -1) : 0;
         }
      }

      int n = this.signum();
      int n2 = shadedBigDecimal.signum();
      if (n != n2) {
         return n > n2 ? 1 : -1;
      } else if (n == 0) {
         return 0;
      } else {
         int n3 = this.compareMagnitude(shadedBigDecimal);
         return n > 0 ? n3 : -n3;
      }
   }

   private static long mulsub(long l, long l2, long l3, long l4, long l5) {
      long l6 = l2 - l5 * l4;
      return make64(l + (l6 >>> 32) - l5 * l3, l6 & 4294967295L);
   }

   @Override
   public double doubleValue() {
      if (this.intCompact != Long.MIN_VALUE) {
         if (this.scale == 0) {
            return (double)this.intCompact;
         }

         if (Math.abs(this.intCompact) < 4503599627370496L) {
            if (this.scale > 0 && this.scale < double10pow.length) {
               return (double)this.intCompact / double10pow[this.scale];
            }

            if (this.scale < 0 && this.scale > -double10pow.length) {
               return (double)this.intCompact * double10pow[-this.scale];
            }
         }
      }

      return Double.parseDouble(this.toString());
   }

   private int checkScale(long l) {
      int n = (int)l;
      if ((long)n != l) {
         n = l > 2147483647L ? Integer.MAX_VALUE : Integer.MIN_VALUE;
         if (this.intCompact != 0L) {
            ShadedBigInteger intVal2 = this.intVal;
            if (this.intVal != null) {
               if (intVal2.getSignum() == 0) {
                  return n;
               }
            }

            throw new ArithmeticException(n > 0 ? "Underflow" : "Overflow");
         }
      }

      return n;
   }

   public ShadedBigDecimal negate() {
      return this.intCompact == Long.MIN_VALUE
         ? new ShadedBigDecimal(this.intVal.negate(), Long.MIN_VALUE, this.scale, this.precision)
         : valueOf2(-this.intCompact, this.scale, this.precision);
   }

   public ShadedBigDecimal pow(int n, ShadedMathContext shadedMathContext) {
      if (shadedMathContext.precision == 0) {
         return this.pow(n);
      } else if (n >= -999999999 && n <= 999999999) {
         if (n == 0) {
            return ONE;
         } else {
            ShadedMathContext shadedMathContext2 = shadedMathContext;
            int n2 = Math.abs(n);
            if (shadedMathContext.precision > 0) {
               int n3 = longDigitLength((long)n2);
               if (n3 > shadedMathContext.precision) {
                  throw new ArithmeticException("Invalid operation");
               }

               shadedMathContext2 = new ShadedMathContext(shadedMathContext.precision + n3 + 1, shadedMathContext.roundingMode);
            }

            ShadedBigDecimal shadedBigDecimal2 = ONE;
            boolean flag = false;
            int n4 = 1;

            while (true) {
               n2 += n2;
               if (n2 < 0) {
                  flag = true;
                  shadedBigDecimal2 = shadedBigDecimal2.multiply(this, shadedMathContext2);
               }

               if (n4 == 31) {
                  if (n < 0) {
                     shadedBigDecimal2 = ONE.divide3(shadedBigDecimal2, shadedMathContext2);
                  }

                  return doRound(shadedBigDecimal2, shadedMathContext);
               }

               if (flag) {
                  shadedBigDecimal2 = shadedBigDecimal2.multiply(shadedBigDecimal2, shadedMathContext2);
               }

               n4++;
            }
         }
      } else {
         throw new ArithmeticException("Invalid operation");
      }
   }

   private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
      if (this.intVal == null) {
         BigDecimalUnsafeHolder.setIntValVolatile(this, ShadedBigInteger.valueOf(this.intCompact));
      }

      objectOutputStream.defaultWriteObject();
   }

   private static ShadedBigDecimal createAndStripZerosToMatchScale2(ShadedBigInteger shadedBigInteger, int n, long l) {

      while (true) {
         if (shadedBigInteger.compareMagnitude(ShadedBigInteger.TEN) < 0 || (long)n <= l) {
            break;
         }

         if (shadedBigInteger.testBit((byte)0)) {
            break;
         }

         ShadedBigInteger[] shadedBigIntegers = shadedBigInteger.divideAndRemainder(ShadedBigInteger.TEN);
         if (shadedBigIntegers[1].getSignum() != 0) {
            break;
         }

         shadedBigInteger = shadedBigIntegers[0];
         n = checkScale2(shadedBigInteger, (long)n - 1L);
      }

      return valueOf(shadedBigInteger, n, (byte)0);
   }

   private static ShadedBigDecimal multiply(long l, ShadedBigInteger shadedBigInteger, int n) {
      return l == 0L ? zeroValueOf(n) : new ShadedBigDecimal(shadedBigInteger.multiply2(l), Long.MIN_VALUE, n, 0);
   }

   private int compareMagnitude(ShadedBigDecimal shadedBigDecimal) {
      long intCompact2 = shadedBigDecimal.intCompact;
      long intCompact3 = this.intCompact;
      if (intCompact3 == 0L) {
         return intCompact2 == 0L ? 0 : -1;
      } else if (intCompact2 == 0L) {
         return 1;
      } else {
         long l = (long)this.scale - (long)shadedBigDecimal.scale;
         if (l != 0L) {
            long l2 = (long)this.precision() - (long)this.scale;
            long l3 = (long)shadedBigDecimal.precision() - (long)shadedBigDecimal.scale;
            if (l2 < l3) {
               return -1;
            }

            if (l2 > l3) {
               return 1;
            }

            Object obj = null;
            if (l < 0L) {
               if (l > -2147483648L && (intCompact3 == Long.MIN_VALUE || (intCompact3 = longMultiplyPowerTen(intCompact3, (int)(-l))) == Long.MIN_VALUE) && intCompact2 == Long.MIN_VALUE
                  )
                {
                  obj = this.bigMultiplyPowerTen((int)(-l));
                  ShadedBigInteger intVal2 = shadedBigDecimal.intVal;
                  return ((ShadedBigInteger)obj).compareMagnitude(intVal2);
               }
            } else if (l <= 2147483647L
               && (intCompact2 == Long.MIN_VALUE || (intCompact2 = longMultiplyPowerTen(intCompact2, (int)l)) == Long.MIN_VALUE)
               && intCompact3 == Long.MIN_VALUE) {
               obj = shadedBigDecimal.bigMultiplyPowerTen((int)l);
               return this.intVal.compareMagnitude((ShadedBigInteger)obj);
            }
         }

         if (intCompact3 != Long.MIN_VALUE) {
            return intCompact2 != Long.MIN_VALUE ? longCompareMagnitude(intCompact3, intCompact2) : -1;
         } else if (intCompact2 != Long.MIN_VALUE) {
            return 1;
         } else {
            ShadedBigInteger intVal3 = shadedBigDecimal.intVal;
            return this.intVal.compareMagnitude(intVal3);
         }
      }
   }

   private static ShadedBigDecimal divideAndRound2(long l, long l2, int n, int n2, int n3) {
      long l3 = l / l2;
      if (n2 == 1 && n == n3) {
         return valueOf(l3, n);
      } else {
         long l4 = l % l2;
         int n4 = l < 0L == l2 < 0L ? 1 : -1;
         if (l4 != 0L) {
            return valueOf(needIncrement3(l2, n2, n4, l3, l4) ? l3 + (long)n4 : l3, n);
         } else {
            return n3 != n ? createAndStripZerosToMatchScale(l3, n, (long)n3) : valueOf(l3, n);
         }
      }
   }

   public ShadedBigDecimal divide(ShadedBigDecimal shadedBigDecimal, ShadedRoundingMode shadedRoundingMode) {
      int oldMode = shadedRoundingMode.oldMode;
      return this.divide2(shadedBigDecimal, this.scale, oldMode);
   }

   private static ShadedBigInteger bigMultiplyPowerTen(long l, int n) {
      return n <= 0 ? ShadedBigInteger.valueOf(l) : bigTenToThe(n).multiply2(l);
   }

   private static void matchScale(ShadedBigDecimal[] shadedBigDecimals) {
      if (shadedBigDecimals[0].scale != shadedBigDecimals[1].scale) {
         if (shadedBigDecimals[0].scale < shadedBigDecimals[1].scale) {
            shadedBigDecimals[0] = shadedBigDecimals[0].setScale(shadedBigDecimals[1].scale, (byte)7);
         } else if (shadedBigDecimals[1].scale < shadedBigDecimals[0].scale) {
            shadedBigDecimals[1] = shadedBigDecimals[1].setScale(shadedBigDecimals[0].scale, (byte)7);
         }
      }
   }

   private static int precision(long l, long l2) {
      if (l == 0L) {
         if (l2 >= 0L) {
            return longDigitLength(l2);
         } else {
            return unsignedLongCompareEq(l2, LONGLONG_TEN_POWERS_TABLE[0][1]) ? 20 : 19;
         }
      } else {
         int n = (128 - Long.numberOfLeadingZeros(l) + 1) * 1233 >>> 12;
         int n2 = n - 19;
         return n2 < LONGLONG_TEN_POWERS_TABLE.length && !longLongCompareMagnitude(l, l2, LONGLONG_TEN_POWERS_TABLE[n2][0], LONGLONG_TEN_POWERS_TABLE[n2][1]) ? n + 1 : n;
      }
   }

   private String getValueString(int n, String str, int n2) {
      int n3 = str.length() - n2;
      if (n3 == 0) {
         StringBuilder sb = new StringBuilder();
         String str2;
         if (n < 0) {
            str2 = "-0.";
         } else {
            short s = 1911;
            str2 = "0.";
         }

         return sb.append(str2).append(str).toString();
      } else {
         StringBuilder sb2;
         if (n3 > 0) {
            sb2 = new StringBuilder(str);
            sb2.insert(n3, '.');
            if (n < 0) {
               sb2.insert(0, '-');
            }
         } else {
            sb2 = new StringBuilder(3 - n3 + str.length());
            String str3;
            if (n < 0) {
               str3 = "-0.";
            } else {
               short s2 = 2064;
               str3 = "0.";
            }

            sb2.append(str3);

            for (int i = 0; i < -n3; i++) {
               sb2.append('0');
            }

            sb2.append(str);
         }

         return sb2.toString();
      }
   }

   private static ShadedBigInteger bigMultiplyPowerTen2(ShadedBigInteger shadedBigInteger, int n) {
      if (n <= 0) {
         return shadedBigInteger;
      } else if (n < LONG_TEN_POWERS_TABLE.length) {
         return shadedBigInteger.multiply2(LONG_TEN_POWERS_TABLE[n]);
      } else {
         return shadedBigInteger.multiply(bigTenToThe(n));
      }
   }

   public ShadedBigDecimal divide3(ShadedBigDecimal shadedBigDecimal, ShadedMathContext shadedMathContext) {
      if (shadedMathContext.precision == 0) {
         return this.divide(shadedBigDecimal);
      } else {
         long l = (long)this.scale - (long)shadedBigDecimal.scale;
         if (shadedBigDecimal.signum() == 0) {
            if (this.signum() == 0) {
               throw new ArithmeticException("Division undefined");
            } else {
               throw new ArithmeticException("Division by zero");
            }
         } else if (this.signum() == 0) {
            return zeroValueOf(saturateLong(l));
         } else {
            int n = this.precision();
            int n2 = shadedBigDecimal.precision();
            if (this.intCompact != Long.MIN_VALUE) {
               return shadedBigDecimal.intCompact != Long.MIN_VALUE
                  ? divide4(this.intCompact, n, shadedBigDecimal.intCompact, n2, l, shadedMathContext)
                  : divide3(this.intCompact, n, shadedBigDecimal.intVal, n2, l, shadedMathContext);
            } else if (shadedBigDecimal.intCompact != Long.MIN_VALUE) {
               long intCompact2 = shadedBigDecimal.intCompact;
               return divide(this.intVal, n, intCompact2, n2, l, shadedMathContext);
            } else {
               ShadedBigInteger intVal2 = shadedBigDecimal.intVal;
               return divide7(this.intVal, n, intVal2, n2, l, shadedMathContext);
            }
         }
      }
   }

   private String layoutChars(boolean flag) {
      long l;
      if (this.scale == 0) {
         return this.intCompact != Long.MIN_VALUE ? Long.toString(this.intCompact) : this.intVal.toString();
      } else if (this.scale == 2 && this.intCompact >= 0L && this.intCompact < 2147483647L) {
         int n = (int)this.intCompact % 100;
         return Integer.toString((int)this.intCompact / 100) + '.' + BigDecimalStringBuilderHelper.DIGIT_TENS[n] + BigDecimalStringBuilderHelper.DIGIT_ONES[n];
      } else {
         BigDecimalStringBuilderHelper helper = threadLocalStringBuilderHelper.get();
         char[] chars;
         int n3;
         if (this.intCompact != Long.MIN_VALUE) {
            l = Math.abs(this.intCompact);
            n3 = helper.putIntCompact(l);
            chars = helper.getCmpCharArray();
         } else {
            n3 = 0;
            chars = this.intVal.abs().toString().toCharArray();
         }

         StringBuilder stringBuilder = helper.getStringBuilder();
         if (this.signum() < 0) {
            stringBuilder.append('-');
         }

         int n4 = chars.length - n3;
         long l3 = -((long)this.scale) + (long)(n4 - 1);
         if (this.scale >= 0 && l3 >= -6L) {
            int n5 = this.scale - n4;
            if (n5 < 0) {
               stringBuilder.append(chars, n3, -n5);
               stringBuilder.append('.');
               stringBuilder.append(chars, -n5 + n3, this.scale);
            } else {
               stringBuilder.append('0');
               stringBuilder.append('.');

               while (n5 > 0) {
                  stringBuilder.append('0');
                  n5--;
               }

               stringBuilder.append(chars, n3, n4);
            }
         } else {
            if (flag) {
               stringBuilder.append(chars[n3]);
               if (n4 > 1) {
                  stringBuilder.append('.');
                  stringBuilder.append(chars, n3 + 1, n4 - 1);
               }
            } else {
               int n6 = (int)(l3 % 3L);
               if (n6 < 0) {
                  n6 += 3;
               }

               l3 -= (long)n6;
               n6++;
               if (this.signum() == 0) {
                  switch (n6) {
                     case 1:
                        stringBuilder.append('0');
                        break;
                     case 2:
                        stringBuilder.append("0.00");
                        l3 += 3L;
                        break;
                     case 3:
                        stringBuilder.append("0.0");
                        l3 += 3L;
                        break;
                     default:
                        throw new AssertionError("Unexpected sig value " + n6);
                  }
               } else if (n6 >= n4) {
                  stringBuilder.append(chars, n3, n4);

                  for (int i = n6 - n4; i > 0; i--) {
                     stringBuilder.append('0');
                  }
               } else {
                  stringBuilder.append(chars, n3, n6);
                  stringBuilder.append('.');
                  stringBuilder.append(chars, n3 + n6, n4 - n6);
               }
            }

            if (l3 != 0L) {
               stringBuilder.append('E');
               if (l3 > 0L) {
                  stringBuilder.append('+');
               }

               stringBuilder.append(l3);
            }
         }

         return stringBuilder.toString();
      }
   }

   private static ShadedBigInteger expandBigIntegerTenPowers(int n) {
      synchronized (ShadedBigDecimal.class) {
         ShadedBigInteger[] shadedBigIntegers = BIG_TEN_POWERS_TABLE;
         int length = shadedBigIntegers.length;
         if (length <= n) {
            int n2 = length << 1;

            while (n2 <= n) {
               n2 <<= 1;
            }

            shadedBigIntegers = Arrays.copyOf(shadedBigIntegers, n2);

            for (int i = length; i < n2; i++) {
               shadedBigIntegers[i] = shadedBigIntegers[i - 1].multiply(ShadedBigInteger.TEN);
            }

            BIG_TEN_POWERS_TABLE = shadedBigIntegers;
         }

         return shadedBigIntegers[n];
      }
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof ShadedBigDecimal)) {
         return false;
      } else {
         ShadedBigDecimal shadedBigDecimal = (ShadedBigDecimal)obj;
         if (obj == this) {
            return true;
         } else if (this.scale != shadedBigDecimal.scale) {
            return false;
         } else {
            long intCompact2 = this.intCompact;
            long intCompact3 = shadedBigDecimal.intCompact;
            if (intCompact2 != Long.MIN_VALUE) {
               if (intCompact3 == Long.MIN_VALUE) {
                  intCompact3 = compactValFor(shadedBigDecimal.intVal);
               }

               return intCompact3 == intCompact2;
            } else if (intCompact3 != Long.MIN_VALUE) {
               return intCompact3 == compactValFor(this.intVal);
            } else {
               return this.inflated().equals(shadedBigDecimal.inflated());
            }
         }
      }
   }

   public ShadedBigDecimal(ShadedBigInteger shadedBigInteger) {
      super();
      this.scale = 0;
      this.intVal = shadedBigInteger;
      this.intCompact = compactValFor(shadedBigInteger);
   }

   private static ShadedBigInteger bigTenToThe(int n) {
      if (n < 0) {
         return ShadedBigInteger.ZERO;
      } else if (n < BIG_TEN_POWERS_TABLE_MAX) {
         ShadedBigInteger[] shadedBigIntegers = BIG_TEN_POWERS_TABLE;
         return n < shadedBigIntegers.length ? shadedBigIntegers[n] : expandBigIntegerTenPowers(n);
      } else {
         return ShadedBigInteger.TEN.pow(n);
      }
   }

   private static ShadedBigDecimal doRound(long l, int n, ShadedMathContext shadedMathContext) {
      int precision2 = shadedMathContext.precision;
      if (precision2 > 0 && precision2 < 19) {
         int n2 = longDigitLength(l);

         for (int i = n2 - precision2; i > 0; i = n2 - precision2) {
            n = checkScaleNonZero((long)n - (long)i);
            l = divideAndRound3(l, LONG_TEN_POWERS_TABLE[i], shadedMathContext.roundingMode.oldMode);
            n2 = longDigitLength(l);
         }

         return valueOf2(l, n, n2);
      } else {
         return valueOf(l, n);
      }
   }

   public ShadedBigDecimal divideToIntegralValue(ShadedBigDecimal shadedBigDecimal, ShadedMathContext shadedMathContext) {
      if (shadedMathContext.precision != 0 && this.compareMagnitude(shadedBigDecimal) >= 0) {
         int n = saturateLong((long)this.scale - (long)shadedBigDecimal.scale);
         ShadedBigDecimal shadedBigDecimal2 = this.divide3(shadedBigDecimal, new ShadedMathContext(shadedMathContext.precision, ShadedRoundingMode.DOWN));
         if (shadedBigDecimal2.getScale() < 0) {
            if (this.subtract(shadedBigDecimal2.multiply(shadedBigDecimal)).compareMagnitude(shadedBigDecimal) >= 0) {
               throw new ArithmeticException("Division impossible");
            }
         } else if (shadedBigDecimal2.getScale() > 0) {
            shadedBigDecimal2 = shadedBigDecimal2.setScale2((byte)0, ShadedRoundingMode.DOWN);
         }

         int n2;
         if (n > shadedBigDecimal2.getScale() && (n2 = shadedMathContext.precision - shadedBigDecimal2.precision()) > 0) {
            return shadedBigDecimal2.setScale(shadedBigDecimal2.getScale() + Math.min(n2, n - shadedBigDecimal2.scale));
         } else {
            int scale2 = shadedBigDecimal2.scale;
            long intCompact2 = shadedBigDecimal2.intCompact;
            return stripZerosToMatchScale(shadedBigDecimal2.intVal, intCompact2, scale2, n);
         }
      } else {
         return this.divideToIntegralValue(shadedBigDecimal);
      }
   }

   private ShadedBigInteger inflated() {
      return this.intVal == null ? ShadedBigInteger.valueOf(this.intCompact) : this.intVal;
   }

   public ShadedBigDecimal(long l, long l2) {
      this.intCompact = l;
      this.intVal = l == Long.MIN_VALUE ? INFLATED_BIGINT : null;
      this.scale = 0;
   }

   @Override
   public long longValue() {
      if (this.intCompact != Long.MIN_VALUE && this.scale == 0) {
         return this.intCompact;
      } else {
         return this.signum() != 0 && !this.hasZeroIntegralPart() && this.scale > -64 ? this.toBigInteger().longValue() : 0L;
      }
   }

   public ShadedBigDecimal(double d, ShadedMathContext shadedMathContext) {
      super();
      if (!Double.isInfinite(d) && !Double.isNaN(d)) {
         long l = Double.doubleToLongBits(d);
         int n = l >> 63 == 0L ? 1 : -1;
         int n2 = (int)(l >> 52 & 2047L);
         long l2 = n2 == 0 ? (l & 4503599627370495L) << 1 : l & 4503599627370495L | 4503599627370496L;
         n2 -= 1075;
         if (l2 == 0L) {
            this.intVal = ShadedBigInteger.ZERO;
            this.scale = 0;
            this.intCompact = 0L;
            this.precision = (byte)1;
         } else {
            while ((l2 & 1L) == 0L) {
               l2 >>= 1;
               n2++;
            }

            int n3 = 0;
            long l3 = (long)n * l2;
            ShadedBigInteger shadedBigInteger;
            if (n2 == 0) {
               ShadedBigInteger shadedBigInteger2;
               if (l3 == Long.MIN_VALUE) {
                  shadedBigInteger2 = INFLATED_BIGINT;
               } else {
                  shadedBigInteger2 = null;
               }

               shadedBigInteger = shadedBigInteger2;
            } else {
               if (n2 < 0) {
                  shadedBigInteger = ShadedBigInteger.valueOf(5L).pow(-n2).multiply2(l3);
                  n3 = -n2;
               } else {
                  short s = 206;
                  shadedBigInteger = ShadedBigInteger.valueOf(2L).pow(n2).multiply2(l3);
               }

               l3 = compactValFor(shadedBigInteger);
            }

            int n5 = 0;
            int precision2 = shadedMathContext.precision;
            if (precision2 > 0) {
               int oldMode = shadedMathContext.roundingMode.oldMode;
               if (l3 == Long.MIN_VALUE) {
                  n5 = bigDigitLength(shadedBigInteger);

                  for (int i = n5 - precision2; i > 0; i = n5 - precision2) {
                     n3 = checkScaleNonZero((long)n3 - (long)i);
                     shadedBigInteger = divideAndRoundByTenPow(shadedBigInteger, i, oldMode);
                     l3 = compactValFor(shadedBigInteger);
                     if (l3 != Long.MIN_VALUE) {
                        break;
                     }

                     n5 = bigDigitLength(shadedBigInteger);
                  }
               }

               if (l3 != Long.MIN_VALUE) {
                  n5 = longDigitLength(l3);

                  for (int j = n5 - precision2; j > 0; j = n5 - precision2) {
                     n3 = checkScaleNonZero((long)n3 - (long)j);
                     l3 = divideAndRound3(l3, LONG_TEN_POWERS_TABLE[j], shadedMathContext.roundingMode.oldMode);
                     n5 = longDigitLength(l3);
                  }

                  shadedBigInteger = null;
               }
            }

            this.intVal = shadedBigInteger;
            this.intCompact = l3;
            this.scale = n3;
            this.precision = n5;
         }
      } else {
         throw new NumberFormatException("Infinite or NaN");
      }
   }

   private static long compactValFor(ShadedBigInteger shadedBigInteger) {
      int[] mag = shadedBigInteger.mag;
      int length = mag.length;
      if (length == 0) {
         return 0L;
      } else {
         int n = mag[0];
         if (length <= 2 && (length != 2 || n >= 0)) {
            long l = length == 2 ? ((long)mag[1] & 4294967295L) + ((long)n << 32) : (long)n & 4294967295L;
            return shadedBigInteger.signum < 0 ? -l : l;
         } else {
            return Long.MIN_VALUE;
         }
      }
   }

   private static long parseExp(char[] chars, int n, int n2) {
      long l = 0L;
      char ch = chars[++n];
      n2--;
      boolean flag = ch == '-';
      if (flag || ch == '+') {
         ch = chars[++n];
         n2--;
      }

      if (n2 <= 0) {
         throw new NumberFormatException();
      } else {
         while (n2 > 10 && (ch == '0' || Character.digit(ch, 10) == 0)) {
            ch = chars[++n];
            n2--;
         }

         if (n2 > 10) {
            throw new NumberFormatException();
         } else {
            while (true) {
               int n3;
               if (ch >= '0' && ch <= '9') {
                  n3 = ch - '0';
               } else {
                  n3 = Character.digit(ch, 10);
                  if (n3 < 0) {
                     throw new NumberFormatException();
                  }
               }

               l = l * 10L + (long)n3;
               if (n2 == 1) {
                  if (flag) {
                     l = -l;
                  }

                  return l;
               }

               ch = chars[++n];
               n2--;
            }
         }
      }
   }

   private static ShadedBigDecimal multiplyAndRound(long l, long l2, int n, ShadedMathContext shadedMathContext) {
      long l3 = multiply2(l, l2);
      if (l3 != Long.MIN_VALUE) {
         return doRound(l3, n, shadedMathContext);
      } else {
         int n2 = 1;
         if (l < 0L) {
            l = -l;
            n2 = -1;
         }

         if (l2 < 0L) {
            l2 = -l2;
            n2 *= -1;
         }

         long l4 = l >>> 32;
         long l5 = l & 4294967295L;
         long l6 = l2 >>> 32;
         long l7 = l2 & 4294967295L;
         l3 = l5 * l7;
         long l8 = l3 & 4294967295L;
         long l9 = l3 >>> 32;
         l3 = l4 * l7 + l9;
         l9 = l3 & 4294967295L;
         long l10 = l3 >>> 32;
         l3 = l5 * l6 + l9;
         l9 = l3 & 4294967295L;
         l10 += l3 >>> 32;
         long l11 = l10 >>> 32;
         l10 &= 4294967295L;
         l3 = l4 * l6 + l10;
         l10 = l3 & 4294967295L;
         l11 = (l3 >>> 32) + l11 & 4294967295L;
         ShadedBigDecimal shadedBigDecimal = doRound128(make64(l11, l10), make64(l9, l8), n2, n, shadedMathContext);
         if (shadedBigDecimal != null) {
            return shadedBigDecimal;
         } else {
            ShadedBigInteger shadedBigInteger = ShadedBigInteger.valueOf(l);
            long l14 = l2 * (long)n2;
            shadedBigDecimal = new ShadedBigDecimal(shadedBigInteger.multiply2(l14), Long.MIN_VALUE, n, 0);
            return doRound(shadedBigDecimal, shadedMathContext);
         }
      }
   }

   public int getScale() {
      return this.scale;
   }

   private ShadedBigInteger bigMultiplyPowerTen(int n) {
      if (n <= 0) {
         return this.inflated();
      } else if (this.intCompact != Long.MIN_VALUE) {
         return bigTenToThe(n).multiply2(this.intCompact);
      } else {
         return this.intVal.multiply(bigTenToThe(n));
      }
   }

   public static ShadedBigDecimal valueOf2(long l, int n, int n2) {
      if (n == 0 && l >= 0L && l < (long)zeroThroughTen.length) {
         return zeroThroughTen[(int)l];
      } else if (l == 0L) {
         return zeroValueOf(n);
      } else {
         return new ShadedBigDecimal((ShadedBigInteger)null, l, n, n2);
      }
   }

   private static ShadedBigDecimal divideAndRound128(long l, long l2, long l3, int n, int n2, int n3, int n4) {
      if (l >= l3) {
         return null;
      } else {
         int n5 = Long.numberOfLeadingZeros(l3);
         l3 <<= n5;
         long l4 = l3 >>> 32;
         long l5 = l3 & 4294967295L;
         long l6 = l2 << n5;
         long l7 = l6 >>> 32;
         long l8 = l6 & 4294967295L;
         l6 = l << n5 | l2 >>> 64 - n5;
         long l9 = l6 & 4294967295L;
         long l10;
         long l11;
         if (l4 == 1L) {
            l10 = l6;
            l11 = 0L;
         } else if (l6 >= 0L) {
            l10 = l6 / l4;
            l11 = l6 - l10 * l4;
         } else {
            long[] longs = divRemNegativeLong(l6, l4);
            l10 = longs[1];
            l11 = longs[0];
         }

         while (l10 >= 4294967296L || unsignedLongCompare(l10 * l5, make64(l11, l7))) {
            l10--;
            l11 += l4;
            if (l11 >= 4294967296L) {
               break;
            }
         }

         l6 = mulsub(l9, l7, l4, l5, l10);
         l7 = l6 & 4294967295L;
         long l12;
         if (l4 == 1L) {
            l12 = l6;
            l11 = 0L;
         } else if (l6 >= 0L) {
            l12 = l6 / l4;
            l11 = l6 - l12 * l4;
         } else {
            long[] longs2 = divRemNegativeLong(l6, l4);
            l12 = longs2[1];
            l11 = longs2[0];
         }

         while (l12 >= 4294967296L || unsignedLongCompare(l12 * l5, make64(l11, l8))) {
            l12--;
            l11 += l4;
            if (l11 >= 4294967296L) {
               break;
            }
         }

         if ((int)l10 < 0) {
            MutableBigInt mutableBigInt = new MutableBigInt(new int[]{(int)l10, (int)l12});
            if (n3 == 1 && n2 == n4) {
               return mutableBigInt.toBigDecimal(n, n2);
            } else {
               long l13 = mulsub(l7, l8, l4, l5, l12) >>> n5;
               if (l13 != 0L) {
                  if (needIncrement(l3 >>> n5, n3, n, mutableBigInt, l13)) {
                     mutableBigInt.add(MutableBigInt.ONE);
                  }

                  return mutableBigInt.toBigDecimal(n, n2);
               } else if (n4 != n2) {
                  return createAndStripZerosToMatchScale2(mutableBigInt.toBigInteger(n), n2, (long)n4);
               } else {
                  return mutableBigInt.toBigDecimal(n, n2);
               }
            }
         } else {
            long l15 = make64(l10, l12);
            l15 *= (long)n;
            if (n3 == 1 && n2 == n4) {
               return valueOf(l15, n2);
            } else {
               long l16 = mulsub(l7, l8, l4, l5, l12) >>> n5;
               if (l16 != 0L) {
                  return valueOf(needIncrement3(l3 >>> n5, n3, n, l15, l16) ? l15 + (long)n : l15, n2);
               } else {
                  return n4 != n2 ? createAndStripZerosToMatchScale(l15, n2, (long)n4) : valueOf(l15, n2);
               }
            }
         }
      }
   }

   public ShadedBigDecimal divide2(ShadedBigDecimal shadedBigDecimal, int n) {
      return this.divide2(shadedBigDecimal, this.scale, n);
   }

   private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
      objectInputStream.defaultReadObject();
      if (this.intVal == null) {
         String str = "BigDecimal: null intVal in stream";
         throw new StreamCorruptedException(str);
      } else {
         BigDecimalUnsafeHolder.setIntCompactVolatile(this, compactValFor(this.intVal));
      }
   }

   public ShadedBigDecimal pow(int n) {
      if (n >= 0 && n <= 999999999) {
         int n2 = this.checkScale((long)this.scale * (long)n);
         return new ShadedBigDecimal(this.inflated().pow(n), n2);
      } else {
         throw new ArithmeticException("Invalid operation");
      }
   }

   private static ShadedBigDecimal divide5(ShadedBigInteger shadedBigInteger, int n, ShadedBigInteger shadedBigInteger2, int n2, int n3, int n4) {
      long l = (long)n3 + (long)n2;
      if (checkScale2(shadedBigInteger, l) > n) {
         return divideAndRound3(bigMultiplyPowerTen2(shadedBigInteger, (n3 + n2) - n), shadedBigInteger2, n3, n4, n3);
      } else {
         l = (long)n - (long)n3;
         return divideAndRound3(shadedBigInteger, bigMultiplyPowerTen2(shadedBigInteger2, checkScale2(shadedBigInteger2, l) - n2), n3, n4, n3);
      }
   }

   public static ShadedBigDecimal valueOf2(long l) {
      if (l >= 0L && l < (long)zeroThroughTen.length) {
         return zeroThroughTen[(int)l];
      } else {
         return l != Long.MIN_VALUE ? new ShadedBigDecimal((net.augustus.vendor.launcher.crypto.math.ShadedBigInteger)null, l, 0, 0) : new ShadedBigDecimal(INFLATED_BIGINT, l, 0, 0);
      }
   }

   public ShadedBigDecimal remainder(ShadedBigDecimal shadedBigDecimal, ShadedMathContext shadedMathContext) {
      return this.divideAndRemainder(shadedBigDecimal, shadedMathContext)[1];
   }

   private static int checkScaleNonZero(long l) {
      int n = (int)l;
      if ((long)n != l) {
         throw new ArithmeticException(n > 0 ? "Underflow" : "Overflow");
      } else {
         return n;
      }
   }

   private static ShadedBigDecimal doRound128(long l, long l2, int n, int n2, ShadedMathContext shadedMathContext) {
      int precision2 = shadedMathContext.precision;
      ShadedBigDecimal shadedBigDecimal = null;
      int n3;
      if ((n3 = precision(l, l2) - precision2) > 0 && n3 < LONG_TEN_POWERS_TABLE.length) {
         n2 = checkScaleNonZero((long)n2 - (long)n3);
         shadedBigDecimal = divideAndRound128(l, l2, LONG_TEN_POWERS_TABLE[n3], n, n2, shadedMathContext.roundingMode.oldMode, n2);
      }

      return shadedBigDecimal != null ? doRound(shadedBigDecimal, shadedMathContext) : null;
   }

   private static boolean longLongCompareMagnitude(long l, long l2, long l3, long l4) {
      return l != l3 ? l < l3 : l2 + Long.MIN_VALUE < l4 + Long.MIN_VALUE;
   }

   private static long longMultiplyPowerTen(long l, int n) {
      if (l != 0L && n > 0) {
         long[] longs = LONG_TEN_POWERS_TABLE;
         long[] longs2 = THRESHOLDS_TABLE;
         if (n < longs.length && n < longs2.length) {
            long l2 = longs[n];
            if (l == 1L) {
               return l2;
            }

            if (Math.abs(l) <= longs2[n]) {
               return l * l2;
            }
         }

         return Long.MIN_VALUE;
      } else {
         return l;
      }
   }

   public ShadedBigDecimal plus(ShadedMathContext shadedMathContext) {
      return this.round(shadedMathContext);
   }

   public ShadedBigDecimal max(ShadedBigDecimal shadedBigDecimal) {
      return this.compareTo2(shadedBigDecimal) >= 0 ? this : shadedBigDecimal;
   }

   public int precision() {
      int precision2 = this.precision;
      if (precision2 == 0) {
         long intCompact2 = this.intCompact;
         if (intCompact2 != Long.MIN_VALUE) {
            precision2 = longDigitLength(intCompact2);
         } else {
            precision2 = bigDigitLength(this.intVal);
         }

         this.precision = precision2;
      }

      return precision2;
   }

   @Override
   public float floatValue() {
      if (this.intCompact != Long.MIN_VALUE) {
         if (this.scale == 0) {
            return (float)this.intCompact;
         }

         if (Math.abs(this.intCompact) < 4194304L) {
            if (this.scale > 0 && this.scale < float10pow.length) {
               return (float)this.intCompact / float10pow[this.scale];
            }

            if (this.scale < 0 && this.scale > -float10pow.length) {
               return (float)this.intCompact * float10pow[-this.scale];
            }
         }
      }

      return Float.parseFloat(this.toString());
   }

   public String toPlainString() {
      if (this.scale == 0) {
         return this.intCompact != Long.MIN_VALUE ? Long.toString(this.intCompact) : this.intVal.toString();
      } else if (this.scale >= 0) {
         String str;
         if (this.intCompact != Long.MIN_VALUE) {
            str = Long.toString(Math.abs(this.intCompact));
         } else {
            str = this.intVal.abs().toString();
         }

         return this.getValueString(this.signum(), str, this.scale);
      } else if (this.signum() == 0) {
         return "0";
      } else {
         int n3 = checkScaleNonZero(-((long)this.scale));
         StringBuilder sb;
         if (this.intCompact != Long.MIN_VALUE) {
            sb = new StringBuilder(20 + n3);
            sb.append(this.intCompact);
         } else {
            String str2 = this.intVal.toString();
            sb = new StringBuilder(str2.length() + n3);
            sb.append(str2);
         }

         for (int i = 0; i < n3; i++) {
            sb.append('0');
         }

         return sb.toString();
      }
   }

   private static boolean needIncrement3(long l, int n, int n2, long l2, long l3) {
      if (!assertionsDisabled && l3 == 0L) {
         throw new AssertionError();
      } else {
         int n3;
         if (l3 > -4611686018427387904L && l3 <= 4611686018427387903L) {
            n3 = longCompareMagnitude(2L * l3, l);
         } else {
            n3 = 1;
         }

         return commonNeedIncrement(n, n2, n3, (l2 & 1L) != 0L);
      }
   }

   public ShadedBigDecimal remainder(ShadedBigDecimal shadedBigDecimal) {
      return this.divideAndRemainder(shadedBigDecimal)[1];
   }

   public static ShadedBigDecimal valueOf(long l, int n) {
      if (n == 0) {
         return valueOf2(l);
      } else if (l == 0L) {
         return zeroValueOf(n);
      } else {
         ShadedBigInteger shadedBigInteger = l == Long.MIN_VALUE ? INFLATED_BIGINT : null;
         return new ShadedBigDecimal(shadedBigInteger, l, n, (byte)0);
      }
   }

   private static long multiply2(long l, long l2) {
      long l3 = l * l2;
      if ((Math.abs(l) | Math.abs(l2)) >>> 31 != 0L && l2 != 0L && l3 / l2 != l) {
         return Long.MIN_VALUE;
      } else {
         return l3;
      }
   }

   private int adjustScale(int n, long l) {
      long l2 = (long)n - l;
      if (l2 <= 2147483647L && l2 >= -2147483648L) {
         return (int)l2;
      } else {
         throw new NumberFormatException("Scale out of range.");
      }
   }

   private static ShadedBigDecimal createAndStripZerosToMatchScale(long l, int n, long l2) {

      while (Math.abs(l) >= 10L && (long)n > l2 && (l & 1L) == 0L) {
         if ((l % 10L) != 0L) {
            break;
         }

         l /= 10L;
         n = checkScale(l, (long)n - 1L);
      }

      return valueOf(l, n);
   }

   private static ShadedBigDecimal doRound2(ShadedBigInteger shadedBigInteger, int n, ShadedMathContext shadedMathContext) {
      int precision2 = shadedMathContext.precision;
      int n2 = 0;
      if (precision2 > 0) {
         long l = compactValFor(shadedBigInteger);
         int oldMode = shadedMathContext.roundingMode.oldMode;
         if (l == Long.MIN_VALUE) {
            n2 = bigDigitLength(shadedBigInteger);

            for (int i = n2 - precision2; i > 0; i = n2 - precision2) {
               n = checkScaleNonZero((long)n - (long)i);
               shadedBigInteger = divideAndRoundByTenPow(shadedBigInteger, i, oldMode);
               l = compactValFor(shadedBigInteger);
               if (l != Long.MIN_VALUE) {
                  break;
               }

               n2 = bigDigitLength(shadedBigInteger);
            }
         }

         if (l != Long.MIN_VALUE) {
            n2 = longDigitLength(l);

            for (int j = n2 - precision2; j > 0; j = n2 - precision2) {
               n = checkScaleNonZero((long)n - (long)j);
               l = divideAndRound3(l, LONG_TEN_POWERS_TABLE[j], shadedMathContext.roundingMode.oldMode);
               n2 = longDigitLength(l);
            }

            return valueOf2(l, n, n2);
         }
      }

      return new ShadedBigDecimal(shadedBigInteger, Long.MIN_VALUE, n, n2);
   }

   public int intValueExact() {
      long l = this.longValueExact();
      if ((long)((int)l) != l) {
         throw new ArithmeticException("Overflow");
      } else {
         return (int)l;
      }
   }

   private static ShadedBigDecimal stripZerosToMatchScale(ShadedBigInteger shadedBigInteger, long l, int n, int n2) {
      if (l != Long.MIN_VALUE) {
         return createAndStripZerosToMatchScale(l, n, (long)n2);
      } else {
         return createAndStripZerosToMatchScale2(shadedBigInteger == null ? INFLATED_BIGINT : shadedBigInteger, n, (long)n2);
      }
   }

   private static long make64(long l, long l2) {
      return l << 32 | l2;
   }

   public ShadedBigDecimal divide2(ShadedBigDecimal shadedBigDecimal, int n, int n2) {
      if (n2 >= 0 && n2 <= 7) {
         if (this.intCompact != Long.MIN_VALUE) {
            if (shadedBigDecimal.intCompact != Long.MIN_VALUE) {
               return divide6(this.intCompact, this.scale, shadedBigDecimal.intCompact, shadedBigDecimal.scale, n, n2);
            } else {
               int scale2 = shadedBigDecimal.scale;
               return divide8(this.intCompact, this.scale, shadedBigDecimal.intVal, scale2, n, n2);
            }
         } else if (shadedBigDecimal.intCompact != Long.MIN_VALUE) {
            int scale3 = shadedBigDecimal.scale;
            long intCompact2 = shadedBigDecimal.intCompact;
            int scale4 = this.scale;
            return divide2(this.intVal, scale4, intCompact2, scale3, n, n2);
         } else {
            int scale5 = shadedBigDecimal.scale;
            ShadedBigInteger intVal2 = shadedBigDecimal.intVal;
            int scale6 = this.scale;
            return divide5(this.intVal, scale6, intVal2, scale5, n, n2);
         }
      } else {
         throw new IllegalArgumentException("Invalid rounding mode");
      }
   }

   public ShadedBigInteger toBigInteger() {
      return this.setScale((byte)0, (byte)1).inflated();
   }

   public static ShadedBigInteger getInflatedValue(ShadedBigDecimal shadedBigDecimal) {
      return shadedBigDecimal.inflated();
   }

   public ShadedBigDecimal scaleByPowerOfTen(int n) {
      long l = (long)this.scale - (long)n;
      return new ShadedBigDecimal(this.intVal, this.intCompact, this.checkScale(l), this.precision);
   }

   public ShadedBigDecimal round(ShadedMathContext shadedMathContext) {
      return shadedMathContext.precision == 0 ? this : doRound(this, shadedMathContext);
   }

   public ShadedBigDecimal(int n, ShadedMathContext shadedMathContext) {
      super();
      int precision2 = shadedMathContext.precision;
      long l = (long)n;
      int n2 = 0;
      int n3 = 0;
      if (precision2 > 0) {
         n3 = longDigitLength(l);

         for (int i = n3 - precision2; i > 0; i = n3 - precision2) {
            n2 = checkScaleNonZero((long)n2 - (long)i);
            l = divideAndRound3(l, LONG_TEN_POWERS_TABLE[i], shadedMathContext.roundingMode.oldMode);
            n3 = longDigitLength(l);
         }
      }

      this.intVal = null;
      this.intCompact = l;
      this.scale = n2;
      this.precision = n3;
   }

   private static ShadedBigDecimal doRound(ShadedBigDecimal shadedBigDecimal, ShadedMathContext shadedMathContext) {
      int precision2 = shadedMathContext.precision;
      boolean flag = false;
      if (precision2 <= 0) {
         return shadedBigDecimal;
      } else {
         ShadedBigInteger intVal2 = shadedBigDecimal.intVal;
         long intCompact2 = shadedBigDecimal.intCompact;
         int scale2 = shadedBigDecimal.scale;
         int n = shadedBigDecimal.precision();
         int oldMode = shadedMathContext.roundingMode.oldMode;
         if (intCompact2 == Long.MIN_VALUE) {
            for (int i = n - precision2; i > 0; i = n - precision2) {
               scale2 = checkScaleNonZero((long)scale2 - (long)i);
               intVal2 = divideAndRoundByTenPow(intVal2, i, oldMode);
               flag = true;
               intCompact2 = compactValFor(intVal2);
               if (intCompact2 != Long.MIN_VALUE) {
                  n = longDigitLength(intCompact2);
                  break;
               }

               n = bigDigitLength(intVal2);
            }
         }

         if (intCompact2 != Long.MIN_VALUE) {
            for (int j = n - precision2; j > 0; intVal2 = null) {
               scale2 = checkScaleNonZero((long)scale2 - (long)j);
               intCompact2 = divideAndRound3(intCompact2, LONG_TEN_POWERS_TABLE[j], shadedMathContext.roundingMode.oldMode);
               flag = true;
               n = longDigitLength(intCompact2);
               j = n - precision2;
            }
         }

         return flag ? new ShadedBigDecimal(intVal2, intCompact2, scale2, n) : shadedBigDecimal;
      }
   }

   public ShadedBigDecimal plus() {
      return this;
   }

   public static ShadedBigDecimal scaledTenPow(int n, int n2, int n3) {
      if (n < LONG_TEN_POWERS_TABLE.length) {
         return valueOf((long)n2 * LONG_TEN_POWERS_TABLE[n], n3);
      } else {
         ShadedBigInteger shadedBigInteger = bigTenToThe(n);
         if (n2 == -1) {
            shadedBigInteger = shadedBigInteger.negate();
         }

         return new ShadedBigDecimal(shadedBigInteger, Long.MIN_VALUE, n3, n + 1);
      }
   }

   private static ShadedBigDecimal divide8(long l, int n, ShadedBigInteger shadedBigInteger, int n2, int n3, int n4) {
      if (checkScale(l, (long)n3 + (long)n2) > n) {
         return divideAndRound3(bigMultiplyPowerTen(l, (n3 + n2) - n), shadedBigInteger, n3, n4, n3);
      } else {
         ShadedBigInteger shadedBigInteger3 = bigMultiplyPowerTen2(shadedBigInteger, checkScale2(shadedBigInteger, (long)n - (long)n3) - n2);
         return divideAndRound3(ShadedBigInteger.valueOf(l), shadedBigInteger3, n3, n4, n3);
      }
   }

   public ShadedBigDecimal multiply(ShadedBigDecimal shadedBigDecimal, ShadedMathContext shadedMathContext) {
      if (shadedMathContext.precision == 0) {
         return this.multiply(shadedBigDecimal);
      } else {
         int n = this.checkScale((long)this.scale + (long)shadedBigDecimal.scale);
         if (this.intCompact != Long.MIN_VALUE) {
            return shadedBigDecimal.intCompact != Long.MIN_VALUE ? multiplyAndRound(this.intCompact, shadedBigDecimal.intCompact, n, shadedMathContext) : multiplyAndRound2(this.intCompact, shadedBigDecimal.intVal, n, shadedMathContext);
         } else if (shadedBigDecimal.intCompact != Long.MIN_VALUE) {
            return multiplyAndRound2(shadedBigDecimal.intCompact, this.intVal, n, shadedMathContext);
         } else {
            ShadedBigInteger intVal2 = shadedBigDecimal.intVal;
            return multiplyAndRound3(this.intVal, intVal2, n, shadedMathContext);
         }
      }
   }

   public ShadedBigDecimal(int n) {
      this.intCompact = (long)n;
      this.scale = 0;
      this.intVal = null;
   }

   private static long divideAndRound3(long l, long l2, int n) {
      long l3 = l / l2;
      if (n == 1) {
         return l3;
      } else {
         long l4 = l % l2;
         int n2 = l < 0L == l2 < 0L ? 1 : -1;
         if (l4 != 0L) {
            return needIncrement3(l2, n, n2, l3, l4) ? l3 + (long)n2 : l3;
         } else {
            return l3;
         }
      }
   }

   public static ShadedBigDecimal valueOf(ShadedBigInteger shadedBigInteger, int n, int n2) {
      long l = compactValFor(shadedBigInteger);
      if (l == 0L) {
         return zeroValueOf(n);
      } else {
         return n == 0 && l >= 0L && l < (long)zeroThroughTen.length ? zeroThroughTen[(int)l] : new ShadedBigDecimal(shadedBigInteger, l, n, n2);
      }
   }

   public ShadedBigDecimal setScale2(int n, ShadedRoundingMode shadedRoundingMode) {
      return this.setScale(n, shadedRoundingMode.oldMode);
   }

   public ShadedBigDecimal[] divideAndRemainder(ShadedBigDecimal shadedBigDecimal) {
      ShadedBigDecimal[] shadedBigDecimals = new ShadedBigDecimal[]{this.divideToIntegralValue(shadedBigDecimal), null};
      shadedBigDecimals[1] = this.subtract(shadedBigDecimals[0].multiply(shadedBigDecimal));
      return shadedBigDecimals;
   }

   public ShadedBigDecimal ulp() {
      return valueOf2(1L, this.getScale(), 1);
   }

   private static boolean unsignedLongCompare(long l, long l2) {
      return l + Long.MIN_VALUE > l2 + Long.MIN_VALUE;
   }

   public ShadedBigDecimal(ShadedBigInteger shadedBigInteger, ShadedMathContext shadedMathContext) {
      this(shadedBigInteger, 0, shadedMathContext);
   }

   private static void print(String str, ShadedBigDecimal shadedBigDecimal) {
      System.err.format("%s:\tintCompact %d\tintVal %d\tscale %d\tprecision %d%n", str, shadedBigDecimal.intCompact, shadedBigDecimal.intVal, shadedBigDecimal.scale, shadedBigDecimal.precision);
   }

   private static ShadedBigDecimal multiplyAndRound3(ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2, int n, ShadedMathContext shadedMathContext) {
      return doRound2(shadedBigInteger.multiply(shadedBigInteger2), n, shadedMathContext);
   }

   public short shortValueExact() {
      long l = this.longValueExact();
      if ((long)((short)((int)l)) != l) {
         throw new ArithmeticException("Overflow");
      } else {
         return (short)((int)l);
      }
   }

   public long longValueExact() {
      if (this.intCompact != Long.MIN_VALUE && this.scale == 0) {
         return this.intCompact;
      } else if (this.signum() == 0) {
         return 0L;
      } else if (this.hasZeroIntegralPart()) {
         throw new ArithmeticException("Rounding necessary");
      } else if (this.precision() - this.scale > 19) {
         throw new ArithmeticException("Overflow");
      } else {
         ShadedBigDecimal shadedBigDecimal = this.setScale((byte)0, (byte)7);
         if (shadedBigDecimal.precision() >= 19) {
            BigDecimalLongOverflow.check(shadedBigDecimal);
         }

         return shadedBigDecimal.inflated().longValue();
      }
   }

   public String toEngineeringString() {
      return this.layoutChars(false);
   }

   public ShadedBigDecimal add2(ShadedBigDecimal shadedBigDecimal, ShadedMathContext shadedMathContext) {
      if (shadedMathContext.precision == 0) {
         return this.add(shadedBigDecimal);
      } else {
         ShadedBigDecimal shadedBigDecimal2 = this;
         boolean flag = this.signum() == 0;
         boolean flag2 = shadedBigDecimal.signum() == 0;
         if (!flag && !flag2) {
            long l = (long)this.scale - (long)shadedBigDecimal.scale;
            if (l != 0L) {
               ShadedBigDecimal[] shadedBigDecimals = this.preAlign(this, shadedBigDecimal, l, shadedMathContext);
               matchScale(shadedBigDecimals);
               shadedBigDecimal2 = shadedBigDecimals[0];
               shadedBigDecimal = shadedBigDecimals[1];
            }

            ShadedBigInteger shadedBigInteger = shadedBigDecimal2.inflated();
            shadedBigInteger = shadedBigInteger.add(shadedBigDecimal.inflated());
            return doRound2(shadedBigInteger, shadedBigDecimal2.scale, shadedMathContext);
         } else {
            int n = Math.max(this.getScale(), shadedBigDecimal.getScale());
            if (flag && flag2) {
               return zeroValueOf(n);
            } else {
               ShadedBigDecimal shadedBigDecimal3 = flag ? doRound(shadedBigDecimal, shadedMathContext) : doRound(this, shadedMathContext);
               if (shadedBigDecimal3.getScale() == n) {
                  return shadedBigDecimal3;
               } else if (shadedBigDecimal3.getScale() > n) {
                  int scale3 = shadedBigDecimal3.scale;
                  long intCompact2 = shadedBigDecimal3.intCompact;
                  return stripZerosToMatchScale(shadedBigDecimal3.intVal, intCompact2, scale3, n);
               } else {
                  int n2 = shadedMathContext.precision - shadedBigDecimal3.precision();
                  if (n2 >= (n - shadedBigDecimal3.getScale())) {
                     return shadedBigDecimal3.setScale(n);
                  } else {
                     return shadedBigDecimal3.setScale(shadedBigDecimal3.getScale() + n2);
                  }
               }
            }
         }
      }
   }

   public ShadedBigDecimal add(ShadedBigDecimal shadedBigDecimal) {
      if (this.intCompact != Long.MIN_VALUE) {
         if (shadedBigDecimal.intCompact != Long.MIN_VALUE) {
            return add3(this.intCompact, this.scale, shadedBigDecimal.intCompact, shadedBigDecimal.scale);
         } else {
            int scale2 = shadedBigDecimal.scale;
            return add(this.intCompact, this.scale, shadedBigDecimal.intVal, scale2);
         }
      } else if (shadedBigDecimal.intCompact != Long.MIN_VALUE) {
         int scale3 = this.scale;
         return add(shadedBigDecimal.intCompact, shadedBigDecimal.scale, this.intVal, scale3);
      } else {
         int scale4 = shadedBigDecimal.scale;
         ShadedBigInteger intVal2 = shadedBigDecimal.intVal;
         int scale5 = this.scale;
         return add2(this.intVal, scale5, intVal2, scale4);
      }
   }

   public ShadedBigDecimal divide(ShadedBigDecimal shadedBigDecimal) {
      if (shadedBigDecimal.signum() == 0) {
         if (this.signum() == 0) {
            throw new ArithmeticException("Division undefined");
         } else {
            throw new ArithmeticException("Division by zero");
         }
      } else {
         int n = saturateLong((long)this.scale - (long)shadedBigDecimal.scale);
         if (this.signum() == 0) {
            return zeroValueOf(n);
         } else {
            ShadedMathContext shadedMathContext = new ShadedMathContext(
               (int)Math.min((long)this.precision() + (long)Math.ceil(10.0 * (double)shadedBigDecimal.precision() / 3.0), 2147483647L), ShadedRoundingMode.UNNECESSARY
            );

            ShadedBigDecimal shadedBigDecimal2;
            try {
               shadedBigDecimal2 = this.divide3(shadedBigDecimal, shadedMathContext);
            } catch (ArithmeticException e) {
               throw new ArithmeticException("Non-terminating decimal expansion; no exact representable decimal result.");
            }

            if (n > shadedBigDecimal2.getScale()) {
               return shadedBigDecimal2.setScale(n, (byte)7);
            } else {
               return shadedBigDecimal2;
            }
         }
      }
   }

   public ShadedBigDecimal negate(ShadedMathContext shadedMathContext) {
      return this.negate().round(shadedMathContext);
   }

   private static boolean unsignedLongCompareEq(long l, long l2) {
      return l + Long.MIN_VALUE >= l2 + Long.MIN_VALUE;
   }

   private static int bigDigitLength(ShadedBigInteger shadedBigInteger) {
      if (shadedBigInteger.signum == 0) {
         return 1;
      } else {
         int n = (int)(((long)shadedBigInteger.bitLength() + 1L) * 646456993L >>> 31);
         return shadedBigInteger.compareMagnitude(bigTenToThe(n)) < 0 ? n : n + 1;
      }
   }

   private static int compareMagnitudeNormalized2(long l, int n, long l2, int n2) {
      int n3 = n - n2;
      if (n3 != 0) {
         if (n3 < 0) {
            l = longMultiplyPowerTen(l, -n3);
         } else {
            l2 = longMultiplyPowerTen(l2, n3);
         }
      }

      if (l != Long.MIN_VALUE) {
         return l2 != Long.MIN_VALUE ? longCompareMagnitude(l, l2) : -1;
      } else {
         return 1;
      }
   }

   public ShadedBigDecimal divideToIntegralValue(ShadedBigDecimal shadedBigDecimal) {
      int n = saturateLong((long)this.scale - (long)shadedBigDecimal.scale);
      if (this.compareMagnitude(shadedBigDecimal) < 0) {
         return zeroValueOf(n);
      } else if (this.signum() == 0 && shadedBigDecimal.signum() != 0) {
         return this.setScale(n, (byte)7);
      } else {
         int n2 = (int)Math.min(
            (long)this.precision()
               + (long)Math.ceil(10.0 * (double)shadedBigDecimal.precision() / 3.0)
               + Math.abs((long)this.getScale() - (long)shadedBigDecimal.getScale())
               + 2L,
            2147483647L
         );
         ShadedBigDecimal shadedBigDecimal2 = this.divide3(shadedBigDecimal, new ShadedMathContext(n2, ShadedRoundingMode.DOWN));
         if (shadedBigDecimal2.scale > 0) {
            shadedBigDecimal2 = shadedBigDecimal2.setScale2((byte)0, ShadedRoundingMode.DOWN);
            int scale2 = shadedBigDecimal2.scale;
            long intCompact2 = shadedBigDecimal2.intCompact;
            shadedBigDecimal2 = stripZerosToMatchScale(shadedBigDecimal2.intVal, intCompact2, scale2, n);
         }

         if (shadedBigDecimal2.scale < n) {
            shadedBigDecimal2 = shadedBigDecimal2.setScale(n, (byte)7);
         }

         return shadedBigDecimal2;
      }
   }

   private static int compareMagnitudeNormalized(long l, int n, ShadedBigInteger shadedBigInteger, int n2) {
      if (l == 0L) {
         return -1;
      } else {
         int n3 = n - n2;
         return n3 < 0 && longMultiplyPowerTen(l, -n3) == Long.MIN_VALUE ? bigMultiplyPowerTen(l, -n3).compareMagnitude(shadedBigInteger) : -1;
      }
   }

   public ShadedBigDecimal(char[] chars) {
      this(chars, 0, chars.length);
   }

   private static ShadedBigInteger divideAndRound(ShadedBigInteger shadedBigInteger, long l, int n) {
      long l2 = 0L;
      Object obj = null;
      MutableBigInt mutableBigInt = new MutableBigInt(shadedBigInteger.mag);
      obj = new MutableBigInt();
      l2 = mutableBigInt.divide(l, (MutableBigInt)obj);
      boolean flag = l2 == 0L;
      int n2 = l < 0L ? -shadedBigInteger.signum : shadedBigInteger.signum;
      if (!flag && needIncrement(l, n, n2, (MutableBigInt)obj, l2)) {
         MutableBigInt one = MutableBigInt.ONE;
         ((MutableBigInt)obj).add(one);
      }

      return ((MutableBigInt)obj).toBigInteger(n2);
   }

   public ShadedBigDecimal(ShadedBigInteger shadedBigInteger, long l, int n, int n2) {
      this.scale = n;
      this.precision = n2;
      this.intCompact = l;
      this.intVal = shadedBigInteger;
   }

   private boolean hasZeroIntegralPart() {
      if (!assertionsDisabled && this.signum() == 0) {
         throw new AssertionError();
      } else {
         return this.precision() - this.scale <= 0;
      }
   }

}
