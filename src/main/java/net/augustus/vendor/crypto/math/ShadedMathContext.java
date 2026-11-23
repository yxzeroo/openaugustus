package net.augustus.vendor.crypto.math;

import net.augustus.vendor.crypto.CustomBigInteger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

public final class ShadedMathContext implements Serializable {
   private static final int DEFAULT_DIGITS = 0;
   public static final ShadedMathContext UNLIMITED;
   public static final ShadedMathContext DECIMAL128;
   public static final ShadedMathContext DECIMAL64;
   public static final ShadedMathContext DECIMAL32;
   private static final int MIN_DIGITS = 0;
   private static final long serialVersionUID = 0L;
   public final int precision;
   public final CustomRoundingMode roundingMode;
   private static final CustomRoundingMode DEFAULT_ROUNDINGMODE = CustomRoundingMode.HALF_UP;

   private void readObject(long l, ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
      String str = null;
      byte b = 6;
      int[] opaqueStateTag = CustomBigInteger.getOpaqueStateTag();

      while (true) {
         outerLoop:
         while (3 < b) {
            if (5 > b) {
               throw new StreamCorruptedException(str);
            }

            if (b <= 5) {
               return;
            }

            if (b <= 6) {
               if (this.precision < 0) {
                  continue;
               }
            } else {
               str = "MathContext: invalid digits in stream";
            }

            while (1 <= b) {
               if (1 >= b) {
                  throw new StreamCorruptedException(str);
               }

               if (this.roundingMode != null) {
                  continue outerLoop;
               }
            }

            str = "MathContext: null roundingMode in stream";
         }

         objectInputStream.defaultReadObject();
      }
   }

   @Override
   public int hashCode() {
      return this.precision + this.roundingMode.hashCode() * 59;
   }

   static {
      UNLIMITED = new ShadedMathContext(0, CustomRoundingMode.HALF_UP);
      DECIMAL32 = new ShadedMathContext(7, CustomRoundingMode.HALF_EVEN);
      DECIMAL64 = new ShadedMathContext(16, CustomRoundingMode.HALF_EVEN);
      DECIMAL128 = new ShadedMathContext(34, CustomRoundingMode.HALF_EVEN);
   }

   @Override
   public String toString() {
      return "precision=" + this.precision + " roundingMode=" + this.roundingMode.toString();
   }

   public ShadedMathContext(int n) {
       this(n, DEFAULT_ROUNDINGMODE);
   }

   @Override
   public boolean equals(Object obj) {
      Object obj2 = null;
      int[] opaqueStateTag = CustomBigInteger.getOpaqueStateTag();
      if (!(obj instanceof ShadedMathContext)) {
         return false;
      } else {
         obj2 = (ShadedMathContext)obj;
         return ((ShadedMathContext)obj2).precision == this.precision && ((ShadedMathContext)obj2).roundingMode == this.roundingMode;
      }
   }

   public ShadedMathContext(int n, CustomRoundingMode customRoundingMode) {
      if (n < 0) {
         throw new IllegalArgumentException("Digits < 0");
      } else if (customRoundingMode == null) {
         throw new NullPointerException("null RoundingMode");
      } else {
         this.precision = n;
         this.roundingMode = customRoundingMode;
      }
   }

   public int getPrecision() {
      return this.precision;
   }

   public CustomRoundingMode getRoundingMode() {
      return this.roundingMode;
   }

   public ShadedMathContext(String str, int n, int n2, short s) {
      int[] opaqueStateTag = CustomBigInteger.getOpaqueStateTag();
      if (str == null) {
         throw new NullPointerException("null String");
      } else {
         int n3;
         try {
            if (!str.startsWith("precision=")) {
               throw new RuntimeException();
            }

            int n4 = str.indexOf(32);
            int n5 = 10;
            n3 = Integer.parseInt(str.substring(10, n4));
            if (!str.startsWith("roundingMode=", n4 + 1)) {
               throw new RuntimeException();
            }

            n5 = n4 + 1 + 13;
            String str2 = str.substring(n5);
            this.roundingMode = CustomRoundingMode.valueOf(str2);
         } catch (RuntimeException e) {
            throw new IllegalArgumentException("bad string format");
         }

         if (n3 < 0) {
            throw new IllegalArgumentException("Digits < 0");
         } else {
            this.precision = n3;
         }
      }
   }
}
