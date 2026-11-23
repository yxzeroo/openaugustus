package net.augustus.vendor.launcher.crypto.math;

public class RationalFraction {
   private ShadedBigInteger denominator;
   public static final RationalFraction ONE;
   public static final RationalFraction TWO;
   private static ShadedBigDecimal DECIMAL_FIVE;
   public static final RationalFraction ZERO;
   private static ShadedMathContext DEFAULT_MATH_CONTEXT;
   private ShadedBigInteger numerator;
   private static ShadedBigDecimal DECIMAL_TWO;

   public RationalFraction(ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2) {
      this.numerator = shadedBigInteger;
      this.denominator = shadedBigInteger2;
      if (shadedBigInteger2.equals(ShadedBigInteger.ZERO)) {
         throw new ArithmeticException("Created impossible fraction");
      }
   }

   public RationalFraction(String str) {
       this((new ShadedBigDecimal(str)));
       ShadedBigDecimal shadedBigDecimal = new ShadedBigDecimal(str);
   }

   public ShadedBigDecimal toBigDecimal(ShadedMathContext shadedMathContext) {
      return new ShadedBigDecimal(this.numerator).divide3(new ShadedBigDecimal(this.denominator), shadedMathContext);
   }

   public RationalFraction simplify() {
      ShadedBigInteger denominator2 = this.denominator;
      ShadedBigInteger shadedBigInteger = this.numerator.gcd(denominator2);
      return shadedBigInteger.equals(ShadedBigInteger.ZERO) ? this : new RationalFraction(this.numerator.divide(shadedBigInteger), this.denominator.divide(shadedBigInteger));
   }

   public static ShadedBigInteger lcm(ShadedBigInteger shadedBigInteger, ShadedBigInteger shadedBigInteger2) {
      return shadedBigInteger.multiply(shadedBigInteger2).divide(shadedBigInteger.gcd(shadedBigInteger2));
   }

   public static RationalFraction parseFraction(String str) {
      if (str.length() == 0) {
         return null;
      } else if (str.contains("/")) {
         String[] strings = str.split("/");
         return new RationalFraction(new ShadedBigInteger(strings[0]), new ShadedBigInteger(strings[1]));
      } else {
         return new RationalFraction(str);
      }
   }

   public RationalFraction reciprocal() {
      return new RationalFraction(this.denominator, this.numerator);
   }

   public int compareTo(RationalFraction other) {
      ShadedBigInteger shadedBigInteger = lcm(this.getDenominator(), other.getDenominator());
      ShadedBigInteger shadedBigInteger2 = shadedBigInteger.divide(this.getDenominator());
      ShadedBigInteger shadedBigInteger3 = shadedBigInteger.divide(other.getDenominator());
      return this.getNumerator().multiply(shadedBigInteger2).compareTo2(other.getNumerator().multiply(shadedBigInteger3));
   }

   public RationalFraction negate() {
      return new RationalFraction(this.numerator.negate(), this.denominator);
   }

   public ShadedBigDecimal toBigDecimal() {
      return this.toBigDecimal(DEFAULT_MATH_CONTEXT);
   }

   public RationalFraction subtract2(RationalFraction rationalFraction) {
      return this.add2(rationalFraction.negate());
   }

   public ShadedBigInteger getNumerator() {
      return this.numerator;
   }

   public RationalFraction add2(RationalFraction rationalFraction) {
      ShadedBigInteger denominator2 = rationalFraction.denominator;
      ShadedBigInteger shadedBigInteger = this.denominator.gcd(denominator2);
      ShadedBigInteger shadedBigInteger2 = rationalFraction.denominator.divide(shadedBigInteger);
      ShadedBigInteger shadedBigInteger3 = this.denominator.divide(shadedBigInteger);
      ShadedBigInteger shadedBigInteger4 = this.denominator.multiply(shadedBigInteger2);
      ShadedBigInteger shadedBigInteger5 = this.numerator.multiply(shadedBigInteger2);
      ShadedBigInteger shadedBigInteger6 = rationalFraction.numerator.multiply(shadedBigInteger3);
      return new RationalFraction(shadedBigInteger5.add(shadedBigInteger6), shadedBigInteger4);
   }

   public RationalFraction divide(RationalFraction rationalFraction) {
      return this.multiply(new RationalFraction(rationalFraction.denominator, rationalFraction.numerator));
   }

   public boolean isNegative() {
      int flowState = FractionMatrixRow.getFlowState();
      ShadedBigInteger zero = ShadedBigInteger.ZERO;
      int n = this.numerator.compareTo2(zero);
      if (flowState != 0) {
         n = n == -1 ? 1 : 0;
      }

      ShadedBigInteger zero2 = ShadedBigInteger.ZERO;
      int n2 = this.denominator.compareTo2(zero2);
      if (flowState != 0) {
         n2 = n2 == -1 ? 1 : 0;
      }

      return ((n ^ n2) & 1) != 0;
   }

   public RationalFraction(ShadedBigDecimal shadedBigDecimal) {
       super();
      ShadedBigInteger[] shadedBigIntegers = fromBigDecimal(shadedBigDecimal);
      this.numerator = shadedBigIntegers[0];
      this.denominator = shadedBigIntegers[1];
      ShadedBigInteger zero = ShadedBigInteger.ZERO;
      if (this.denominator.equals(zero)) {
         throw new ArithmeticException("Created impossible fraction");
      }
   }

   public RationalFraction add(int n) {
      ShadedBigInteger numerator2 = this.numerator;
      ShadedBigInteger shadedBigInteger2 = this.denominator.multiply(ShadedBigInteger.valueOf((long)n));
      return (new RationalFraction(numerator2.add(shadedBigInteger2), this.denominator)).simplify();
   }

   public RationalFraction(long l, long l2) {
       this(String.valueOf(l), String.valueOf(l2));
   }

   public RationalFraction pow(RationalFraction rationalFraction, ShadedMathContext shadedMathContext) {
      int flowState = FractionMatrixRow.getFlowState();
      RationalFraction rationalFraction2 = rationalFraction;
      if (flowState != 0) {
         if (!rationalFraction.getDenominator().equals(ShadedBigInteger.ONE)) {
            return null;
         }

         rationalFraction2 = this.pow(rationalFraction.getNumerator().intValue());
      }

      return rationalFraction2;
   }

   public boolean equalsFraction(RationalFraction rationalFraction) {
      int flowState = FractionMatrixRow.getFlowState();
      ShadedBigInteger numerator2 = this.numerator;
      boolean flag = numerator2.equals(rationalFraction.getNumerator());
      if (flowState != 0) {
         if (!flag) {
            return false;
         }

         numerator2 = this.denominator;
         flag = numerator2.equals(rationalFraction.getDenominator());
      }

      return flowState == 0 ? flag : flag;
   }

   public RationalFraction subtract(int n) {
      ShadedBigInteger shadedBigInteger2 = this.denominator.multiply(ShadedBigInteger.valueOf((long)n));
      return (new RationalFraction(this.numerator.subtract(shadedBigInteger2), this.denominator)).simplify();
   }

   public String toFractionString(long l) {
      return this.numerator + "/" + this.denominator;
   }

   public RationalFraction pow(int n) {
      int flowState = FractionMatrixRow.getFlowState();
      if (n == 0) {
         return new RationalFraction(1);
      } else {
         RationalFraction rationalFraction = new RationalFraction(this.numerator, this.denominator);
         RationalFraction rationalFraction2 = rationalFraction;
         int n2 = n;
         if (flowState != 0) {
            n2 = n < 0 ? 1 : 0;
         }

         int n3 = n2;
         n = Math.abs(n);
         int n4 = 1;

         while (true) {
            if (n4 < n) {
               rationalFraction = rationalFraction.multiply(rationalFraction2);
               n4++;
               if (flowState == 0) {
                  break;
               }

               if (flowState != 0) {
                  continue;
               }
            }

            if (n3 != 0) {
               rationalFraction = ONE.divide(rationalFraction);
            }
            break;
         }

         return rationalFraction;
      }
   }

   public String toDecimalString(ShadedMathContext shadedMathContext) {
      ShadedBigDecimal shadedBigDecimal = new ShadedBigDecimal(this.numerator);
      shadedBigDecimal = shadedBigDecimal.divide3(new ShadedBigDecimal(this.denominator), shadedMathContext);
      return shadedBigDecimal.toPlainString();
   }

   public RationalFraction(int n) {
       this(String.valueOf(n), "1");
   }

   public RationalFraction round() {
      ShadedBigDecimal shadedBigDecimal = this.toBigDecimal(new ShadedMathContext(10, ShadedRoundingMode.HALF_UP));
      ShadedRoundingMode halfup = ShadedRoundingMode.HALF_UP;
      return new RationalFraction(shadedBigDecimal.setScale2((byte)0, halfup));
   }

   public RationalFraction(String str, String str2) {
       super();
      this.numerator = new ShadedBigInteger(str);
      this.denominator = new ShadedBigInteger(str2);
      ShadedBigInteger zero = ShadedBigInteger.ZERO;
      if (this.denominator.equals(zero)) {
         throw new ArithmeticException("Created impossible fraction");
      }
   }

   public ShadedBigInteger getDenominator() {
      return this.denominator;
   }

   public RationalFraction abs() {
      return new RationalFraction(this.numerator.abs(), this.denominator.abs());
   }

   public RationalFraction multiply(RationalFraction rationalFraction) {
      ShadedBigInteger denominator2 = rationalFraction.denominator;
      ShadedBigInteger shadedBigInteger = this.numerator.gcd(denominator2);
      ShadedBigInteger numerator2 = rationalFraction.numerator;
      ShadedBigInteger shadedBigInteger2 = this.denominator.gcd(numerator2);
      ShadedBigInteger shadedBigInteger3 = this.numerator.divide(shadedBigInteger);
      ShadedBigInteger shadedBigInteger4 = rationalFraction.denominator.divide(shadedBigInteger);
      ShadedBigInteger shadedBigInteger5 = rationalFraction.numerator.divide(shadedBigInteger2);
      ShadedBigInteger shadedBigInteger6 = this.denominator.divide(shadedBigInteger2);
      return new RationalFraction(shadedBigInteger3.multiply(shadedBigInteger5), shadedBigInteger4.multiply(shadedBigInteger6));
   }

   @Override
   public String toString() {
      ShadedBigDecimal shadedBigDecimal = new ShadedBigDecimal(this.numerator);
      shadedBigDecimal = shadedBigDecimal.divide3(new ShadedBigDecimal(this.denominator), DEFAULT_MATH_CONTEXT);
      return shadedBigDecimal.toPlainString();
   }

   public static ShadedBigInteger[] fromBigDecimal(ShadedBigDecimal shadedBigDecimal) {
      boolean flag = shadedBigDecimal.compareTo2(ShadedBigDecimal.ZERO) < 0;
      shadedBigDecimal = shadedBigDecimal.abs();
      int scale = shadedBigDecimal.subtract(shadedBigDecimal.setScale2(0, ShadedRoundingMode.DOWN)).getScale();
      ShadedBigDecimal shadedBigDecimal4 = ShadedBigDecimal.TEN.pow(scale);
      ShadedBigDecimal shadedBigDecimal5 = shadedBigDecimal.multiply(shadedBigDecimal4);
      ShadedBigInteger shadedBigInteger3 = shadedBigDecimal5.toBigInteger().gcd(shadedBigDecimal4.toBigInteger());
      ShadedBigDecimal shadedBigDecimal6 = new ShadedBigDecimal(shadedBigInteger3);
      if (flag) {
         shadedBigDecimal5 = shadedBigDecimal5.multiply(new ShadedBigDecimal(-1));
      }

      ShadedBigDecimal[] shadedBigDecimals = new ShadedBigDecimal[]{shadedBigDecimal5.divide(shadedBigDecimal6), shadedBigDecimal4.divide(shadedBigDecimal6)};
      return new ShadedBigInteger[]{shadedBigDecimals[0].toBigInteger(), shadedBigDecimals[1].toBigInteger()};
   }

   static {
      ZERO = new RationalFraction(0);
      ONE = new RationalFraction(1);
      TWO = new RationalFraction(2);
      DECIMAL_TWO = new ShadedBigDecimal("2");
      DECIMAL_FIVE = new ShadedBigDecimal("5");
      DEFAULT_MATH_CONTEXT = new ShadedMathContext(10, ShadedRoundingMode.HALF_UP);
   }
}
