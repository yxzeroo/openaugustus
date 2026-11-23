package net.augustus.vendor.launcher.crypto.math;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

public final class ShadedMathContext implements Serializable {
   public final int precision;
   private static final ShadedRoundingMode DEFAULT_ROUNDINGMODE = ShadedRoundingMode.HALF_UP;
   public static final ShadedMathContext DECIMAL64;
   public static final ShadedMathContext DECIMAL32;
   private static final int DEFAULT_DIGITS = 0;
   private static final long serialVersionUID;
   public final ShadedRoundingMode roundingMode;
   private static  int MIN_DIGITS;
   public static final ShadedMathContext UNLIMITED;
   public static final ShadedMathContext DECIMAL128;

   public ShadedMathContext(int n) {
      this(n, DEFAULT_ROUNDINGMODE);
   }

   public ShadedMathContext(long l, String str) {
      if (str == null) {
         throw new NullPointerException("null String");
      } else {
         int n;
         try {
            if (!str.startsWith("precision=")) {
               throw new RuntimeException();
            }

            int n2 = str.indexOf(32);
            int n3 = 10;
            n = Integer.parseInt(str.substring(10, n2));
            if (!str.startsWith("roundingMode=", n2 + 1)) {
               throw new RuntimeException();
            }

            n3 = n2 + 1 + 13;
            this.roundingMode = ShadedRoundingMode.valueOf(str.substring(n3, str.length()));
         } catch (RuntimeException e) {
            throw new IllegalArgumentException("bad string format");
         }

         if (n < 0) {
            throw new IllegalArgumentException("Digits < 0");
         } else {
            this.precision = n;
         }
      }
   }

   @Override
   public String toString() {
      return "precision=" + this.precision + " roundingMode=" + this.roundingMode.toString();
   }

   public ShadedRoundingMode getRoundingMode(long l) {
      return this.roundingMode;
   }

   @Override
   public int hashCode() {
      return this.precision + this.roundingMode.hashCode() * 59;
   }

   private void readObject(long l, ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
      objectInputStream.defaultReadObject();
      if (this.precision < 0) {
         String str = "MathContext: invalid digits in stream";
         throw new StreamCorruptedException(str);
      } else if (this.roundingMode == null) {
         String str2 = "MathContext: null roundingMode in stream";
         throw new StreamCorruptedException(str2);
      }
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof ShadedMathContext)) {
         return false;
      } else {
         ShadedMathContext shadedMathContext = (ShadedMathContext)obj;
         return shadedMathContext.precision == this.precision && shadedMathContext.roundingMode == this.roundingMode;
      }
   }

   public int getPrecision(long l) {
      return this.precision;
   }

   static {
      serialVersionUID = 5579720004786848255L;
      UNLIMITED = new ShadedMathContext(0, ShadedRoundingMode.HALF_UP);
      DECIMAL32 = new ShadedMathContext(7, ShadedRoundingMode.HALF_EVEN);
      DECIMAL64 = new ShadedMathContext(16, ShadedRoundingMode.HALF_EVEN);
      DECIMAL128 = new ShadedMathContext(34, ShadedRoundingMode.HALF_EVEN);
   }

   public ShadedMathContext(int n, ShadedRoundingMode shadedRoundingMode) {
      if (n < 0) {
         throw new IllegalArgumentException("Digits < 0");
      } else if (shadedRoundingMode == null) {
         throw new NullPointerException("null RoundingMode");
      } else {
         this.precision = n;
         this.roundingMode = shadedRoundingMode;
      }
   }

}
