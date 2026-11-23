package net.augustus.vendor.crypto.math;

import net.augustus.vendor.crypto.CustomBigInteger;
import java.util.Arrays;

public class ShadedMutableBigInteger {
   public int intLen;
   public static int KNUTH_POW2_THRESH_LEN;
   public int offset = 0;
   public int[] value;
   public static int KNUTH_POW2_THRESH_ZEROS;
   public static final ShadedMutableBigInteger ONE = new ShadedMutableBigInteger(1);

   private int difference(ShadedMutableBigInteger shadedMutableBigInteger) {
      int n = 0;
      boolean flag = false;
      long l = 0L;
      Object obj = null;
      int n2 = 0;
      Object obj2 = null;
      obj = this;
      n2 = ((ShadedMutableBigInteger)obj).compare(shadedMutableBigInteger);
      if (n2 == 0) {
         return 0;
      } else {
         if (n2 < 0) {
            obj2 = (ShadedMutableBigInteger)obj;
            obj = shadedMutableBigInteger;
            shadedMutableBigInteger = (ShadedMutableBigInteger)obj2;
         }

         l = 0L;
         n = ((ShadedMutableBigInteger)obj).intLen;

         for (int i = shadedMutableBigInteger.intLen; i > 0; ((ShadedMutableBigInteger)obj).value[((ShadedMutableBigInteger)obj).offset + n] = (int)l) {
            n--;
            i--;
            l = ((long)((ShadedMutableBigInteger)obj).value[((ShadedMutableBigInteger)obj).offset + n] & 4294967295L)
               - ((long)shadedMutableBigInteger.value[shadedMutableBigInteger.offset + i] & 4294967295L)
               - (long)((int)(-(l >> 32)));
         }

         while (n > 0) {
            n--;
            l = ((long)((ShadedMutableBigInteger)obj).value[((ShadedMutableBigInteger)obj).offset + n] & 4294967295L) - (long)((int)(-(l >> 32)));
            ((ShadedMutableBigInteger)obj).value[((ShadedMutableBigInteger)obj).offset + n] = (int)l;
         }

         ((ShadedMutableBigInteger)obj).normalize();
         return n2;
      }
   }

   public final int compareHalf(ShadedMutableBigInteger shadedMutableBigInteger) {
      long l = 0L;
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      long l2 = 0L;
      int n5 = 0;
      Object obj = null;
      int n6 = 0;
      Object obj2 = null;
      int n7 = 0;
      n3 = shadedMutableBigInteger.intLen;
      n6 = this.intLen;
      if (n6 > 0) {
         if (n6 > n3) {
            return 1;
         } else if (n6 < n3 - 1) {
            return -1;
         } else {
            obj = shadedMutableBigInteger.value;
            n = 0;
            n4 = 0;
            if (n6 != n3) {
               if (((int[])obj)[n] != 1) {
                  return -1;
               }

               n++;
               n4 = Integer.MIN_VALUE;
            }

            obj2 = this.value;
            n7 = this.offset;
            n2 = n;

            while (n7 < n6 + this.offset) {
               n5 = ((int[])obj)[n2++];
               l2 = (long)((n5 >>> 1) + n4) & 4294967295L;
               l = (long)((int[])obj2)[n7++] & 4294967295L;
               if (l != l2) {
                  return l < l2 ? -1 : 1;
               }

               n4 = (n5 & 1) << 31;
            }

            return n4 == 0 ? 0 : -1;
         }
      } else {
         return n3 > 0 ? -1 : 0;
      }
   }

   private final int getInt(int n) {
      return this.value[this.offset + n];
   }

   public ShadedMutableBigInteger() {
      this.value = new int[1];
      this.intLen = 0;
   }

   public void leftShift(int n) {
      int n2 = 0;
      boolean flag = false;
      int n3 = 0;
      int n4 = 0;
      Object obj = null;
      int n5 = 0;
      boolean flag2 = false;
      if (this.intLen != 0) {
         n4 = n >>> 5;
         n3 = n & 31;
         n2 = CustomBigInteger.bitLengthForInt(this.value[this.offset]);
         if (n > 32 - n2) {
            n5 = this.intLen + n4 + 1;
            if (n3 <= 32 - n2) {
               n5--;
            }

            if (this.value.length >= n5) {
               if (this.value.length - this.offset < n5) {
                  for (int i = 0; i < this.intLen; i++) {
                     this.value[i] = this.value[this.offset + i];
                  }

                  for (int j = this.intLen; j < n5; j++) {
                     this.value[j] = 0;
                  }

                  this.offset = 0;
               } else {
                  for (int k = 0; k < n5 - this.intLen; k++) {
                     this.value[this.offset + this.intLen + k] = 0;
                  }
               }
            } else {
               obj = new int[n5];

               for (int m = 0; m < this.intLen; m++) {
                  ((int[])obj)[m] = this.value[this.offset + m];
               }

               this.setValue((int[])obj, n5);
            }

            this.intLen = n5;
            if (n3 != 0) {
               if (n3 > 32 - n2) {
                  this.primitiveRightShift(32 - n3);
               } else {
                  this.primitiveLeftShift(n3);
               }
            }
         } else {
            this.primitiveLeftShift(n3);
         }
      }
   }

   public ShadedMutableBigInteger divideKnuth(ShadedMutableBigInteger shadedMutableBigInteger, ShadedMutableBigInteger shadedMutableBigInteger2, boolean flag) {
      int n = 0;
      Object obj = null;
      int n2 = 0;
      Object obj2 = null;
      if (shadedMutableBigInteger.intLen != 0) {
         if (this.intLen != 0) {
            ShadedMutableBigInteger shadedMutableBigInteger3 = this;
            n = shadedMutableBigInteger3.compare(shadedMutableBigInteger);
            if (n < 0) {
               shadedMutableBigInteger2.intLen = shadedMutableBigInteger2.offset = 0;
               return flag ? new ShadedMutableBigInteger(this) : null;
            } else if (n == 0) {
               shadedMutableBigInteger2.value[0] = shadedMutableBigInteger2.intLen = 1;
               shadedMutableBigInteger2.offset = 0;
               return flag ? new ShadedMutableBigInteger() : null;
            } else {
               shadedMutableBigInteger3 = shadedMutableBigInteger2;
               int n3;
               shadedMutableBigInteger3.clear();
               if (shadedMutableBigInteger.intLen == 1) {
                  shadedMutableBigInteger3 = this;
                  n3 = shadedMutableBigInteger.value[shadedMutableBigInteger.offset];
                  n2 = shadedMutableBigInteger3.divideOneWord(n3, shadedMutableBigInteger2);
                  if (flag) {
                     return n2 == 0 ? new ShadedMutableBigInteger() : new ShadedMutableBigInteger(n2);
                  } else {
                     return null;
                  }
               } else {
                  if (this.intLen >= 6) {
                     n2 = Math.min(this.getLowestSetBit(), shadedMutableBigInteger.getLowestSetBit());
                     if (n2 >= 96) {
                        obj2 = new ShadedMutableBigInteger(this);
                        shadedMutableBigInteger = new ShadedMutableBigInteger(shadedMutableBigInteger);
                        ((ShadedMutableBigInteger)obj2).rightShift(n2);
                        shadedMutableBigInteger.rightShift(n2);
                        shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj2;
                        obj = shadedMutableBigInteger3.divideKnuth(shadedMutableBigInteger, shadedMutableBigInteger2);
                        ((ShadedMutableBigInteger)obj).leftShift(n2);
                        return (ShadedMutableBigInteger)obj;
                     }
                  }

                  shadedMutableBigInteger3 = this;
                  return shadedMutableBigInteger3.divideMagnitude(shadedMutableBigInteger, shadedMutableBigInteger2, flag);
               }
            }
         } else {
            shadedMutableBigInteger2.intLen = shadedMutableBigInteger2.offset = 0;
            return !flag ? null : new ShadedMutableBigInteger();
         }
      } else {
         throw new ArithmeticException("BigInteger divide by zero");
      }
   }

   private int mulsubBorrow(int[] ints, int[] ints2, int n, long l, int n2, int n3) {
      long l2 = 0L;
      boolean flag = false;
      long l3 = 0L;
      long l4 = 0L;
      long l5 = 0L;
      l3 = (long)n & 4294967295L;
      l2 = 0L;
      n3 += n2;

      for (int i = n2 - 1; i >= 0; i--) {
         l5 = ((long)ints2[i] & 4294967295L) * l3 + l2;
         l4 = (long)ints[n3--] - l5;
         l2 = (l5 >>> 32) + (long)((l4 & 4294967295L) > ((long)(~((int)l5)) & 4294967295L) ? 1 : 0);
      }

      return (int)l2;
   }

   public boolean isZero() {
      return this.intLen == 0;
   }

   public void addShifted(ShadedMutableBigInteger shadedMutableBigInteger, int n) {
      int n2 = 0;
      Object obj = null;
      long l = 0L;
      long l2 = 0L;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      Object obj2 = null;
      int n6 = 0;
      if (!shadedMutableBigInteger.isZero()) {
         n3 = this.intLen;
         n5 = shadedMutableBigInteger.intLen + n;
         n4 = this.intLen <= n5 ? n5 : this.intLen;
         obj = ((int[])this.value).length < n4 ? new int[n4] : this.value;
         n6 = ((int[])obj).length - 1;
         l2 = 0L;

         while (n3 > 0 && n5 > 0) {
            n3--;
            n5--;
            n2 = n5 + shadedMutableBigInteger.offset < ((int[])shadedMutableBigInteger.value).length ? shadedMutableBigInteger.value[n5 + shadedMutableBigInteger.offset] : 0;
            l = ((long)this.value[n3 + this.offset] & 4294967295L) + ((long)n2 & 4294967295L) + l2;
            ((int[])obj)[n6--] = (int)l;
            l2 = l >>> 32;
         }

         while (n3 > 0) {
            n3--;
            if (l2 == 0L && (int[])obj == this.value && n6 == n3 + this.offset) {
               return;
            }

            l = ((long)this.value[n3 + this.offset] & 4294967295L) + l2;
            ((int[])obj)[n6--] = (int)l;
            l2 = l >>> 32;
         }

         while (n5 > 0) {
            n5--;
            n2 = n5 + shadedMutableBigInteger.offset < ((int[])shadedMutableBigInteger.value).length ? shadedMutableBigInteger.value[n5 + shadedMutableBigInteger.offset] : 0;
            l = ((long)n2 & 4294967295L) + l2;
            ((int[])obj)[n6--] = (int)l;
            l2 = l >>> 32;
         }

         if (l2 > 0L) {
            if (((int[])obj).length < ++n4) {
               obj2 = new int[n4];
               System.arraycopy((int[])obj, 0, (int[])obj2, 1, ((int[])obj).length);
               ((int[])obj2)[0] = 1;
               obj = (int[])obj2;
            } else {
               ((int[])obj)[n6--] = 1;
            }
         }

         this.value = (int[])obj;
         this.intLen = n4;
         this.offset = ((int[])obj).length - n4;
      }
   }

   public boolean isNormal(long l) {
      if (this.intLen + this.offset <= this.value.length) {
         return this.intLen != 0 ? this.value[this.offset] != 0 : true;
      } else {
         return false;
      }
   }

   private final void ensureCapacity(long l, int n) {
      if (this.value.length < n) {
         this.value = new int[n];
         this.offset = 0;
         this.intLen = n;
      }
   }

   private void keepLower(int n) {
      if (this.intLen >= n) {
         this.offset = this.offset + (this.intLen - n);
         this.intLen = n;
      }
   }

   public int divideOneWord(int n, ShadedMutableBigInteger shadedMutableBigInteger) {
      int n2 = 0;
      long l = 0L;
      int n3 = 0;
      long l2 = 0L;
      long l3 = 0L;
      int n4 = 0;
      long l4 = 0L;
      boolean flag = false;
      long l5 = 0L;
      int n5 = 0;
      int n6 = 0;
      l5 = (long)n & 4294967295L;
      if (this.intLen != 1) {
         if (shadedMutableBigInteger.value.length < this.intLen) {
            shadedMutableBigInteger.value = new int[this.intLen];
         }

         shadedMutableBigInteger.offset = 0;
         shadedMutableBigInteger.intLen = this.intLen;
         n4 = Integer.numberOfLeadingZeros(n);
         n3 = this.value[this.offset];
         l4 = (long)n3 & 4294967295L;
         if (l4 < l5) {
            shadedMutableBigInteger.value[0] = 0;
         } else {
            shadedMutableBigInteger.value[0] = (int)(l4 / l5);
            n3 = (int)(l4 - (long)shadedMutableBigInteger.value[0] * l5);
            l4 = (long)n3 & 4294967295L;
         }

         for (int i = this.intLen; --i > 0; l4 = (long)n3 & 4294967295L) {
            l = l4 << 32 | (long)this.value[this.offset + this.intLen - i] & 4294967295L;
            if (l >= 0L) {
               n2 = (int)(l / l5);
               n3 = (int)(l - (long)n2 * l5);
            } else {
               l2 = divWord(l, n);
               n2 = (int)(l2 & 4294967295L);
               n3 = (int)(l2 >>> 32);
            }

            shadedMutableBigInteger.value[this.intLen - i] = n2;
         }

         shadedMutableBigInteger.normalize();
         return n4 > 0 ? n3 % n : n3;
      } else {
         l3 = (long)this.value[this.offset] & 4294967295L;
         n6 = (int)(l3 / l5);
         n5 = (int)(l3 - (long)n6 * l5);
         shadedMutableBigInteger.value[0] = n6;
         shadedMutableBigInteger.intLen = n6 == 0 ? 0 : 1;
         shadedMutableBigInteger.offset = 0;
         return n5;
      }
   }

   private static void copyAndShift(int[] ints, int n, int n2, long l, int[] ints2, int n3, int n4) {
      boolean flag = false;
      int n5 = 0;
      int n6 = 0;
      int n7 = 0;
      n6 = 32 - n4;
      n5 = ints[n];

      for (int i = 0; i < n2 - 1; i++) {
         n7 = n5;
         n5 = ints[++n];
         ints2[n3 + i] = n7 << n4 | n5 >>> n6;
      }

      ints2[n3 + n2 - 1] = n5 << n4;
   }

   public void rightShift(int n) {
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      if (this.intLen != 0) {
         n4 = n >>> 5;
         n3 = n & 31;
         this.intLen -= n4;
         if (n3 != 0) {
            n2 = CustomBigInteger.bitLengthForInt(this.value[this.offset]);
            if (n3 < n2) {
               this.primitiveRightShift(n3);
            } else {
               this.primitiveLeftShift(32 - n3);
               this.intLen--;
            }
         }
      }
   }

   public void addDisjoint(ShadedMutableBigInteger shadedMutableBigInteger, int n) {
      boolean flag = false;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      int n6 = 0;
      Object obj = null;
      if (!shadedMutableBigInteger.isZero()) {
         n4 = this.intLen;
         n5 = shadedMutableBigInteger.intLen + n;
         n6 = this.intLen <= n5 ? n5 : this.intLen;
         if (this.value.length < n6) {
            obj = new int[n6];
         } else {
            obj = this.value;
            Arrays.fill(this.value, this.offset + this.intLen, this.value.length, 0);
         }

         n3 = ((int[])obj).length - 1;
         System.arraycopy(this.value, this.offset, (int[])obj, n3 + 1 - n4, n4);
         n5 -= n4;
         n3 -= n4;
         n2 = Math.min(n5, shadedMutableBigInteger.value.length - shadedMutableBigInteger.offset);
         System.arraycopy(shadedMutableBigInteger.value, shadedMutableBigInteger.offset, (int[])obj, n3 + 1 - n5, n2);

         for (int i = n3 + 1 - n5 + n2; i < n3 + 1; i++) {
            ((int[])obj)[i] = 0;
         }

         this.value = (int[])obj;
         this.intLen = n6;
         this.offset = ((int[])obj).length - n6;
      }
   }

   public static ShadedMutableBigInteger modInverseBP2(ShadedMutableBigInteger shadedMutableBigInteger, int n) {
      return fixup(new ShadedMutableBigInteger(1), new ShadedMutableBigInteger(shadedMutableBigInteger), n);
   }

   public ShadedMutableBigInteger hybridGCD(ShadedMutableBigInteger shadedMutableBigInteger) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      obj3 = this;
      obj = new ShadedMutableBigInteger();

      while (shadedMutableBigInteger.intLen != 0) {
         if (Math.abs(((ShadedMutableBigInteger)obj3).intLen - shadedMutableBigInteger.intLen) < 2) {
            return ((ShadedMutableBigInteger)obj3).binaryGCD(shadedMutableBigInteger);
         }

         obj2 = ((ShadedMutableBigInteger)obj3).divide2(shadedMutableBigInteger, (ShadedMutableBigInteger)obj);
         obj3 = shadedMutableBigInteger;
         shadedMutableBigInteger = (ShadedMutableBigInteger)obj2;
      }

      return (ShadedMutableBigInteger)obj3;
   }

   private boolean unsignedLongCompare(long l, long l2) {
      return l + Long.MIN_VALUE > l2 + Long.MIN_VALUE;
   }

   public int[] toIntArray() {
      boolean flag = false;
      Object obj = null;
      obj = new int[this.intLen];

      for (int i = 0; i < this.intLen; i++) {
         ((int[])obj)[i] = this.value[this.offset + i];
      }

      return (int[])obj;
   }

   public static int inverseMod32(int n) {
      int n2 = 0;
      n2 = n * (2 - n * n);
      n2 *= 2 - n * n2;
      n2 *= 2 - n * n2;
      return n2 * (2 - n * n2);
   }

   public static int binaryGcd(int n, int n2) {
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      if (n2 != 0) {
         if (n != 0) {
            n5 = Integer.numberOfTrailingZeros(n);
            n3 = Integer.numberOfTrailingZeros(n2);
            n >>>= n5;
            n2 >>>= n3;
            n4 = n5 >= n3 ? n3 : n5;

            while (n != n2) {
               if (n + Integer.MIN_VALUE > n2 + Integer.MIN_VALUE) {
                  n -= n2;
                  n >>>= Integer.numberOfTrailingZeros(n);
               } else {
                  n2 -= n;
                  n2 >>>= Integer.numberOfTrailingZeros(n2);
               }
            }

            return n << n4;
         } else {
            return n2;
         }
      } else {
         return n;
      }
   }

   public long bitLength() {
      return this.intLen != 0 ? (long)this.intLen * 32L - (long)Integer.numberOfLeadingZeros(this.value[this.offset]) : 0L;
   }

   private final void primitiveRightShift(int n) {
      int n2 = 0;
      int n3 = 0;
      Object obj = null;
      int n4 = 0;
      boolean flag = false;
      obj = this.value;
      n2 = 32 - n;
      n3 = this.offset + this.intLen - 1;

      for (int i = ((int[])obj)[n3]; n3 > this.offset; n3--) {
         n4 = i;
         i = ((int[])obj)[n3 - 1];
         ((int[])obj)[n3] = i << n2 | n4 >>> n;
      }

      ((int[])obj)[this.offset] = ((int[])obj)[this.offset] >>> n;
   }

   private ShadedMutableBigInteger divide2n1n(ShadedMutableBigInteger shadedMutableBigInteger, ShadedMutableBigInteger shadedMutableBigInteger2) {
      int n = 0;
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      n = shadedMutableBigInteger.intLen;
      if (n % 2 == 0 && n >= 80) {
         obj2 = new ShadedMutableBigInteger(this);
         ShadedMutableBigInteger shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj2;
         shadedMutableBigInteger3.safeRightShift(32 * (n / 2));
         shadedMutableBigInteger3 = this;
         shadedMutableBigInteger3.keepLower(n / 2);
         obj4 = new ShadedMutableBigInteger();
         shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj2;
         ShadedMutableBigInteger shadedMutableBigInteger4 = shadedMutableBigInteger;
         obj3 = shadedMutableBigInteger3.divide3n2n(shadedMutableBigInteger4, (ShadedMutableBigInteger)obj4);
         shadedMutableBigInteger3 = this;
         shadedMutableBigInteger4 = (ShadedMutableBigInteger)obj3;
         shadedMutableBigInteger3.addDisjoint(shadedMutableBigInteger4, n / 2);
         shadedMutableBigInteger3 = this;
         shadedMutableBigInteger4 = shadedMutableBigInteger;
         obj = shadedMutableBigInteger3.divide3n2n(shadedMutableBigInteger4, shadedMutableBigInteger2);
         shadedMutableBigInteger3 = shadedMutableBigInteger2;
         shadedMutableBigInteger4 = (ShadedMutableBigInteger)obj4;
         shadedMutableBigInteger3.addDisjoint(shadedMutableBigInteger4, n / 2);
         return (ShadedMutableBigInteger)obj;
      } else {
         return this.divideKnuth(shadedMutableBigInteger, shadedMutableBigInteger2);
      }
   }

   public ShadedBigDecimal toBigDecimal(int n, int n2) {
      long l = 0L;
      int n3 = 0;
      int n4 = 0;
      Object obj = null;
      if (this.intLen != 0 && n != 0) {
         obj = this.getMagnitudeArray();
         n3 = ((int[])obj).length;
         n4 = ((int[])obj)[0];
         if (n3 <= 2 && (n4 >= 0 || n3 != 2)) {
            l = n3 != 2 ? (long)n4 & 4294967295L : (long)((int[])obj)[1] & 4294967295L | ((long)n4 & 4294967295L) << 32;
            return ShadedBigDecimal.valueOf(n == -1 ? -l : l, n2);
         } else {
            return new ShadedBigDecimal(new CustomBigInteger((int[])obj, n), Long.MIN_VALUE, n2, 0);
         }
      } else {
         return ShadedBigDecimal.zeroValueOf(n2);
      }
   }

   private final void primitiveLeftShift(int n) {
      int n2 = 0;
      int n3 = 0;
      boolean flag = false;
      int n4 = 0;
      Object obj = null;
      int n5 = 0;
      obj = this.value;
      n4 = 32 - n;
      n5 = this.offset;
      n2 = ((int[])obj)[n5];

      for (int i = n5 + this.intLen - 1; n5 < i; n5++) {
         n3 = n2;
         n2 = ((int[])obj)[n5 + 1];
         ((int[])obj)[n5] = n3 << n | n2 >>> n4;
      }

      ((int[])obj)[this.offset + this.intLen - 1] = ((int[])obj)[this.offset + this.intLen - 1] << n;
   }

   public boolean isOdd() {
      return this.isZero() ? false : (this.value[this.offset + this.intLen - 1] & 1) == 1;
   }

   private ShadedMutableBigInteger divideMagnitude(ShadedMutableBigInteger shadedMutableBigInteger, ShadedMutableBigInteger shadedMutableBigInteger2, boolean flag) {
      ShadedMutableBigInteger shadedMutableBigInteger3 = null;
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      int[] ints = null;
      long l = 0L;
      int n5 = 0;
      int[] ints2 = null;
      int n6 = 0;
      int[] ints3 = null;
      int n7 = 0;
      int n8 = 0;
      int n9 = 0;
      int n10 = 0;
      int n11 = 0;
      int n12 = 0;
      int n13 = 0;
      int n14 = 0;
      int n15 = 0;
      byte b = 0;
      long l2 = 0L;
      long l3 = 0L;
      long l4 = 0L;
      long l5 = 0L;
      long l6 = 0L;
      int n16 = 0;
      long l7 = 0L;
      int n17 = 2;
      long l8 = 4007944927274370238L;
      long l9 = 0L;
      l8 = 6553804638130550443L ^ (long)"쪌ᣪ⎐흉풎㸁ꓽط뜒흧팆끜\uf100傓煠ㄲ撹\ud975\uf287⛚㳭ὂ⣫줙驟".hashCode();
      l9 = 8104625121554923611L ^ (long)"◸歛ẜ磄腋⤹\uea1c촿\ue096௺\uf836뙢㠋⽍ﲩ珃챜\ue1b0ᬔ⫚ᴢ".hashCode();

      while (true) {
         while (true) {
            n17 = (int)(l8 % 61L);
            boolean flag2;
            if (n17 >= 30) {
               if (30 >= n17) {
                  n7 = n5 + 0;
                  l8 = (l8 ^ 0L) - 0L;
                  l9 = (l9 ^ 0L) - 0L;
                  continue;
               }

               if (n17 < 45) {
                  if (37 > n17) {
                     if (33 <= n17) {
                        if (33 >= n17) {
                           n16 = 0;
                           l8 = l8 - 0L ^ 0L;
                           l9 = l9 + 0L ^ 0L;
                        } else if (35 <= n17) {
                           if (35 >= n17) {
                              n9 = n5 + n10;
                              l8 = (l8 ^ 0L) - 0L;
                              l9 = (l9 ^ 0L) + 0L;
                           } else if (Integer.numberOfLeadingZeros(this.value[this.offset]) < n12) {
                              l8 = (l8 ^ 0L) + 0L;
                              l9 = l9 + 0L ^ 0L;
                           } else {
                              l8 = (l8 ^ 0L) + 0L;
                              l9 = l9 - 0L ^ 0L;
                           }
                        } else {
                           n16 = -1;
                           l8 = l8 - 0L ^ 0L;
                           l9 = l9 - 0L ^ 0L;
                        }
                     } else if (31 >= n17) {
                        if (shadedMutableBigInteger3.intLen != n14) {
                           l8 = (l8 ^ 0L) + 0L;
                           l9 = l9 - 0L ^ 0L;
                        } else {
                           l8 = l8 + 0L ^ 0L;
                           l9 = (l9 ^ 0L) - 0L;
                        }
                     } else {
                        n11 = n13;
                        l8 = (l8 ^ 0L) + 0L;
                        l9 = (l9 ^ 0L) + 0L;
                     }
                     continue;
                  }

                  if (n17 <= 37) {
                     n10 = shadedMutableBigInteger3.value[n8 + 1 + shadedMutableBigInteger3.offset];
                     l8 = (l8 ^ 0L) + 0L;
                     l9 = (l9 ^ 0L) + 0L;
                     continue;
                  }

                  if (41 > n17) {
                     if (39 <= n17) {
                        if (n17 <= 39) {
                           shadedMutableBigInteger3.offset = 1;
                           l8 = l8 - 0L ^ 0L;
                           l9 = l9 - 0L ^ 0L;
                        } else {
                           n3 = ints3[1];
                           l8 = (l8 ^ 0L) - 0L;
                           l9 = l9 - 0L ^ 0L;
                        }
                     } else {
                        n13 = this.value[n2];
                        l8 = l8 - 0L ^ 0L;
                        l9 = (l9 ^ 0L) - 0L;
                     }
                     continue;
                  }

                  if (41 >= n17) {
                     n14 = shadedMutableBigInteger3.intLen;
                     l8 = l8 - 0L ^ 0L;
                     l9 = (l9 ^ 0L) - 0L;
                     continue;
                  }

                  if (43 <= n17) {
                     if (n17 <= 43) {
                        n9 = 0;
                        l8 = (l8 ^ 0L) + 0L;
                        l9 = (l9 ^ 0L) + 0L;
                     } else {
                        shadedMutableBigInteger3.intLen++;
                        l8 = l8 + 0L ^ 0L;
                        l9 = l9 + 0L ^ 0L;
                     }
                     continue;
                  }

                  if (n5 == n15) {
                     l8 = (l8 ^ 0L) - 0L;
                     l9 = l9 - 0L ^ 0L;
                     continue;
                  }

                  l8 = l8 - 0L ^ 0L;
                  l9 = l9 + 0L ^ 0L;
                  l5 = (long)n5 << 0 | (long)n10 & 0L;
                  if (l5 >= 0L) {
                     n16 = (int)(l5 / l7);
                     n9 = (int)(l5 - (long)n16 * l7);
                  } else {
                     l3 = 0L;
                     n16 = (int)(l3 & 0L);
                     n9 = (int)(l3 >>> 0);
                  }
                  break;
               }

               if (n17 <= 45) {
                  l8 = (l8 ^ 0L) - 0L;
                  l9 = (l9 ^ 0L) + 0L;
                  continue;
               }

               if (53 <= n17) {
                  if (53 >= n17) {
                     n2 = this.offset;
                     l8 = l8 - 0L ^ 0L;
                     l9 = l9 + 0L ^ 0L;
                  } else if (n17 >= 57) {
                     if (n17 <= 57) {
                        shadedMutableBigInteger3.offset = 0;
                        l8 = (l8 ^ 0L) - 0L;
                        l9 = l9 - 0L ^ 0L;
                     } else if (59 <= n17) {
                        if (n17 <= 59) {
                           ints[n6] = n11 << n12 | n13 >>> n15;
                           l8 = l8 - 0L ^ 0L;
                           l9 = l9 - 0L ^ 0L;
                        } else {
                           ints3 = Arrays.copyOfRange(shadedMutableBigInteger.value, shadedMutableBigInteger.offset, shadedMutableBigInteger.offset + shadedMutableBigInteger.intLen);
                           l8 = (l8 ^ 0L) + 0L;
                           l9 = l9 - 0L ^ 0L;
                        }
                     } else {
                        ints = new int[this.intLen + 1];
                        l8 = l8 - 0L ^ 0L;
                        l9 = (l9 ^ 0L) - 0L;
                     }
                  } else if (55 <= n17) {
                     if (55 >= n17) {
                        n5 = shadedMutableBigInteger3.value[n8 + shadedMutableBigInteger3.offset];
                        l8 = l8 + 0L ^ 0L;
                        l9 = (l9 ^ 0L) + 0L;
                     } else {
                        l8 = (l8 ^ 0L) + 0L;
                        l9 = l9 - 0L ^ 0L;
                     }
                  } else {
                     n15 = ints3[0];
                     l8 = l8 + 0L ^ 0L;
                     l9 = (l9 ^ 0L) - 0L;
                  }
                  continue;
               }

               if (49 <= n17) {
                  if (49 >= n17) {
                     shadedMutableBigInteger3.offset = 1;
                     l8 = (l8 ^ 0L) - 0L;
                     l9 = (l9 ^ 0L) + 0L;
                  } else if (n17 >= 51) {
                     if (51 >= n17) {
                        n2++;
                        l8 = (l8 ^ 0L) + 0L;
                        l9 = (l9 ^ 0L) + 0L;
                     } else {
                        n2 = n14 - n + 1;
                        l8 = l8 + 0L ^ 0L;
                        l9 = (l9 ^ 0L) - 0L;
                     }
                  } else {
                     shadedMutableBigInteger3.value[0] = 0;
                     l8 = (l8 ^ 0L) + 0L;
                     l9 = (l9 ^ 0L) - 0L;
                  }
                  continue;
               }

               if (n17 < 47) {
                  if (n6 >= this.intLen + 1) {
                     l8 = l8 + 0L ^ 0L;
                     l9 = (l9 ^ 0L) - 0L;
                  } else {
                     l8 = l8 + 0L ^ 0L;
                     l9 = l9 - 0L ^ 0L;
                  }
                  continue;
               }

               if (47 >= n17) {
                  if (n12 <= 0) {
                     l8 = (l8 ^ 0L) - 0L;
                     l9 = l9 - 0L ^ 0L;
                  } else {
                     l8 = l8 + 0L ^ 0L;
                     l9 = (l9 ^ 0L) + 0L;
                  }
                  continue;
               }

               if (n9 + 0 < n7) {
                  l8 = (l8 ^ 0L) + 0L;
                  l9 = (l9 ^ 0L) - 0L;
                  continue;
               }

               l8 = (l8 ^ 0L) + 0L;
               l9 = (l9 ^ 0L) - 0L;
               flag2 = false;
            } else {
               if (14 > n17) {
                  if (6 <= n17) {
                     if (n17 <= 6) {
                        shadedMutableBigInteger3.offset = 1;
                        l8 = l8 + 0L ^ 0L;
                        l9 = l9 - 0L ^ 0L;
                     } else if (10 <= n17) {
                        if (10 >= n17) {
                           l8 = l8 - 0L ^ 0L;
                           l9 = (l9 ^ 0L) - 0L;
                        } else if (n17 >= 12) {
                           if (12 >= n17) {
                              if (n8 >= n2 - 1) {
                                 l8 = l8 + 0L ^ 0L;
                                 l9 = l9 + 0L ^ 0L;
                                 int n18 = 0;
                                 int n19 = 0;
                                 boolean flag3 = false;
                                 n4 = shadedMutableBigInteger3.value[n2 - 1 + shadedMutableBigInteger3.offset];
                                 n5 = n4 + 0;
                                 n7 = shadedMutableBigInteger3.value[n2 + shadedMutableBigInteger3.offset];
                                 if (n4 == n15) {
                                    n18 = -1;
                                    n19 = n4 + n7;
                                    flag3 = n19 + 0 < n5;
                                 } else {
                                    l4 = (long)n4 << 0 | (long)n7 & 0L;
                                    if (l4 >= 0L) {
                                       n18 = (int)(l4 / l7);
                                       n19 = (int)(l4 - (long)n18 * l7);
                                    } else {
                                       l6 = 0L;
                                       n18 = (int)(l6 & 0L);
                                       n19 = (int)(l6 >>> 0);
                                    }
                                 }

                                 if (n18 != 0) {
                                    if (!flag3) {
                                       l4 = (long)shadedMutableBigInteger3.value[n2 + 1 + shadedMutableBigInteger3.offset] & 0L;
                                       l6 = ((long)n19 & 0L) << 0 | l4;
                                       l = ((long)n3 & 0L) * ((long)n18 & 0L);
                                       if (false) {
                                          n18--;
                                          n19 = (int)(((long)n19 & 0L) + l7);
                                          if (((long)n19 & 0L) >= l7) {
                                             l -= (long)n3 & 0L;
                                             l6 = ((long)n19 & 0L) << 0 | l4;
                                             if (false) {
                                                n18--;
                                             }
                                          }
                                       }
                                    }

                                    shadedMutableBigInteger3.value[n2 - 1 + shadedMutableBigInteger3.offset] = 0;
                                    byte b2;
                                    if (flag) {
                                       b2 = 0;
                                    } else {
                                       int n20 = n2 - 1;
                                       b2 = 0;
                                    }

                                    if (b2 + 0 > n5) {
                                       if (flag) {
                                          int n21 = n2 - 1 + 1;
                                          flag2 = false;
                                       }

                                       n18--;
                                    }

                                    ints2[n2 - 1] = n18;
                                 }

                                 if (flag && n12 > 0) {
                                    ;
                                 }

                                 return flag ? shadedMutableBigInteger3 : null;
                              }

                              l8 = (l8 ^ 0L) + 0L;
                              l9 = l9 + 0L ^ 0L;
                           } else {
                              shadedMutableBigInteger3.intLen = this.intLen + 1;
                              l8 = l8 + 0L ^ 0L;
                              l9 = (l9 ^ 0L) + 0L;
                           }
                        } else {
                           n15 = 0 - n12;
                           l8 = l8 + 0L ^ 0L;
                           l9 = l9 + 0L ^ 0L;
                        }
                     } else if (n17 >= 8) {
                        if (8 >= n17) {
                           ints3 = new int[n];
                           l8 = l8 + 0L ^ 0L;
                           l9 = l9 + 0L ^ 0L;
                        } else {
                           n = shadedMutableBigInteger.intLen;
                           l8 = l8 - 0L ^ 0L;
                           l9 = l9 + 0L ^ 0L;
                        }
                     } else {
                        n13 = 0;
                        l8 = (l8 ^ 0L) + 0L;
                        l9 = l9 + 0L ^ 0L;
                     }
                  } else if (2 <= n17) {
                     if (n17 <= 2) {
                        if (shadedMutableBigInteger2.value.length >= n2) {
                           l8 = l8 - 0L ^ 0L;
                           l9 = l9 + 0L ^ 0L;
                        } else {
                           l8 = (l8 ^ 0L) + 0L;
                           l9 = l9 - 0L ^ 0L;
                        }
                     } else if (n17 >= 4) {
                        if (4 >= n17) {
                           System.arraycopy(this.value, this.offset, shadedMutableBigInteger3.value, 1, this.intLen);
                           l8 = l8 + 0L ^ 0L;
                           l9 = (l9 ^ 0L) - 0L;
                        } else {
                           n4 = 0;
                           l8 = l8 - 0L ^ 0L;
                           l9 = (l9 ^ 0L) + 0L;
                        }
                     } else {
                        shadedMutableBigInteger3 = new ShadedMutableBigInteger(ints);
                        l8 = (l8 ^ 0L) + 0L;
                        l9 = (l9 ^ 0L) + 0L;
                     }
                  } else if (0 >= n17) {
                     shadedMutableBigInteger2.value = new int[n2];
                     l8 = (l8 ^ 0L) + 0L;
                     l9 = l9 + 0L ^ 0L;
                  } else {
                     n8 = 0;
                     l8 = l8 - 0L ^ 0L;
                     l9 = (l9 ^ 0L) - 0L;
                  }
                  continue;
               }

               if (n17 <= 14) {
                  n12 = Integer.numberOfLeadingZeros(shadedMutableBigInteger.value[shadedMutableBigInteger.offset]);
                  l8 = l8 + 0L ^ 0L;
                  l9 = (l9 ^ 0L) + 0L;
                  continue;
               }

               if (22 <= n17) {
                  if (n17 <= 22) {
                     shadedMutableBigInteger3 = new ShadedMutableBigInteger(ints);
                     l8 = l8 + 0L ^ 0L;
                     l9 = (l9 ^ 0L) - 0L;
                  } else if (26 <= n17) {
                     if (n17 <= 26) {
                        ints[this.intLen + 1] = n13 << n12;
                        l8 = l8 - 0L ^ 0L;
                        l9 = l9 - 0L ^ 0L;
                     } else if (28 <= n17) {
                        if (28 >= n17) {
                           shadedMutableBigInteger3 = new ShadedMutableBigInteger(new int[this.intLen + 1]);
                           l8 = l8 + 0L ^ 0L;
                           l9 = l9 + 0L ^ 0L;
                        } else {
                           n6 = 1;
                           l8 = l8 + 0L ^ 0L;
                           l9 = l9 + 0L ^ 0L;
                        }
                     } else {
                        l8 = l8 - 0L ^ 0L;
                        l9 = (l9 ^ 0L) - 0L;
                     }
                  } else if (n17 >= 24) {
                     if (24 >= n17) {
                        l8 = l8 + 0L ^ 0L;
                        l9 = l9 - 0L ^ 0L;
                     } else {
                        ints2 = shadedMutableBigInteger2.value;
                        l8 = l8 + 0L ^ 0L;
                        l9 = l9 - 0L ^ 0L;
                     }
                  } else {
                     l7 = (long)n15 & 0L;
                     l8 = (l8 ^ 0L) - 0L;
                     l9 = (l9 ^ 0L) + 0L;
                  }
                  continue;
               }

               if (18 <= n17) {
                  if (18 >= n17) {
                     shadedMutableBigInteger3.intLen = this.intLen;
                     l8 = l8 - 0L ^ 0L;
                     l9 = (l9 ^ 0L) + 0L;
                  } else if (n17 >= 20) {
                     if (20 >= n17) {
                        n6++;
                        l8 = l8 - 0L ^ 0L;
                        l9 = l9 + 0L ^ 0L;
                     } else {
                        shadedMutableBigInteger2.intLen = n2;
                        l8 = l8 - 0L ^ 0L;
                        l9 = (l9 ^ 0L) - 0L;
                     }
                  } else {
                     ints = new int[this.intLen + 2];
                     l8 = l8 - 0L ^ 0L;
                     l9 = (l9 ^ 0L) - 0L;
                  }
                  continue;
               }

               if (n17 >= 16) {
                  if (16 >= n17) {
                     shadedMutableBigInteger3.intLen = this.intLen;
                     l8 = (l8 ^ 0L) + 0L;
                     l9 = l9 - 0L ^ 0L;
                  } else {
                     shadedMutableBigInteger2.offset = 0;
                     l8 = (l8 ^ 0L) + 0L;
                     l9 = l9 + 0L ^ 0L;
                  }
                  continue;
               }

               flag2 = true;
            }

            n4 = (flag2) ? 1 : 0;
            break;
         }

         if (n16 != 0) {
            if (n4 == 0) {
               l5 = (long)shadedMutableBigInteger3.value[n8 + 2 + shadedMutableBigInteger3.offset] & 0L;
               l3 = ((long)n9 & 0L) << 0 | l5;
               l2 = ((long)n3 & 0L) * ((long)n16 & 0L);
               if (false) {
                  n16--;
                  n9 = (int)(((long)n9 & 0L) + l7);
                  if (((long)n9 & 0L) >= l7) {
                     l2 -= (long)n3 & 0L;
                     l3 = ((long)n9 & 0L) << 0 | l5;
                     if (false) {
                        n16--;
                     }
                  }
               }
            }

            shadedMutableBigInteger3.value[n8 + shadedMutableBigInteger3.offset] = 0;
            b = 0;
            if (b + 0 > n7) {
               n16--;
            }

            ints2[n8] = n16;
         }

         n8++;
         l8 = l8 + 0L ^ 0L;
         l9 = (l9 ^ 0L) + 0L;
      }
   }

   public ShadedMutableBigInteger(CustomBigInteger customBigInteger) {
      this.intLen = customBigInteger.mag.length;
      this.value = Arrays.copyOf(customBigInteger.mag, this.intLen);
   }

   private int mulsub(int[] ints, int[] ints2, long l, int n, int n2, int n3) {
      boolean flag = false;
      long l2 = 0L;
      long l3 = 0L;
      long l4 = 0L;
      long l5 = 0L;
      l2 = (long)n & 4294967295L;
      l4 = 0L;
      n3 += n2;

      for (int i = n2 - 1; i >= 0; i--) {
         l5 = ((long)ints2[i] & 4294967295L) * l2 + l4;
         l3 = (long)ints[n3] - l5;
         ints[n3--] = (int)l3;
         l4 = (l5 >>> 32) + (long)((l3 & 4294967295L) > ((long)(~((int)l5)) & 4294967295L) ? 1 : 0);
      }

      return (int)l4;
   }

   public ShadedMutableBigInteger(ShadedMutableBigInteger shadedMutableBigInteger) {
      this.intLen = shadedMutableBigInteger.intLen;
      this.value = Arrays.copyOfRange(shadedMutableBigInteger.value, shadedMutableBigInteger.offset, shadedMutableBigInteger.offset + this.intLen);
   }

   public CustomBigInteger toBigInteger() {
      this.normalize();
      return this.toBigInteger(this.isZero() ? 0 : 1);
   }

   private long toLong() {
      long l = 0L;

      assert this.intLen <= 2 : "this MutableBigInteger exceeds the range of long";

      if (this.intLen != 0) {
         l = (long)this.value[this.offset] & 4294967295L;
         return this.intLen != 2 ? l : l << 32 | (long)this.value[this.offset + 1] & 4294967295L;
      } else {
         return 0L;
      }
   }

   public boolean isOne() {
      return this.intLen == 1 && this.value[this.offset] == 1;
   }

   public long toCompactValue(int n) {
      int n2 = 0;
      long l = 0L;
      Object obj = null;
      int n3 = 0;
      if (this.intLen != 0 && n != 0) {
         obj = this.getMagnitudeArray();
         n2 = ((int[])obj).length;
         n3 = ((int[])obj)[0];
         if (n2 <= 2 && (n3 >= 0 || n2 != 2)) {
            l = n2 != 2 ? (long)n3 & 4294967295L : (long)((int[])obj)[1] & 4294967295L | ((long)n3 & 4294967295L) << 32;
            return n == -1 ? -l : l;
         } else {
            return Long.MIN_VALUE;
         }
      } else {
         return 0L;
      }
   }

   private int mulsubLong(int[] ints, int n, int n2, int n3, int n4) {
      long l = 0L;
      long l2 = 0L;
      long l3 = 0L;
      long l4 = 0L;
      long l5 = 0L;
      l5 = 2484664554430144237L ^ (long)"\ue8ff\ue1be凜\ud883讼\ue361命橅\uecf7뷋뛳\ue925㮠ض푀㵮쟩砻\uf706摹濩⠽\ud810鿑\uf7c6\ueeaa\ue81bꪮ".hashCode();
      l = (long)n3 & 4294967295L;
      l5 = l5 - -4035052412970583846L ^ -2460892004960439107L;
      n4 += 2;
      l5 = (l5 ^ 6206445481497164908L) + -2360163040030251449L;
      l4 = ((long)n2 & 4294967295L) * l;
      l5 = l5 + -874881185414545493L ^ -8085989392107538695L;
      l3 = (long)ints[n4] - l4;
      l5 = l5 - 4337160305726854947L ^ -1829716903084529915L;
      ints[n4--] = (int)l3;
      l5 = l5 - -920991134873985993L ^ 4083747171401093674L;
      l2 = (l4 >>> 32) + (long)((l3 & 4294967295L) > ((long)(~((int)l4)) & 4294967295L) ? 1 : 0);
      l4 = ((long)n & 4294967295L) * l + l2;
      l3 = (long)ints[n4] - l4;
      ints[n4--] = (int)l3;
      l2 = (l4 >>> 32) + (long)((l3 & 4294967295L) > ((long)(~((int)l4)) & 4294967295L) ? 1 : 0);
      return (int)l2;
   }

   public ShadedMutableBigInteger divideAndRemainderBurnikelZiegler(ShadedMutableBigInteger shadedMutableBigInteger, ShadedMutableBigInteger shadedMutableBigInteger2) {
      Object obj = null;
      Object obj2 = null;
      int n = 0;
      Object obj3 = null;
      boolean flag = false;
      int n2 = 0;
      int n3 = 0;
      Object obj4 = null;
      int n4 = 0;
      long l = 0L;
      int n5 = 0;
      Object obj5 = null;
      int n6 = 0;
      Object obj6 = null;
      int n7 = 0;
      n = this.intLen;
      n5 = shadedMutableBigInteger.intLen;
      shadedMutableBigInteger2.offset = shadedMutableBigInteger2.intLen = 0;
      if (n < n5) {
         return this;
      } else {
         n7 = 1 << 32 - Integer.numberOfLeadingZeros(n5 / 80);
         n4 = (n5 + n7 - 1) / n7;
         n6 = n4 * n7;
         l = 32L * (long)n6;
         n3 = (int)Math.max(0L, l - shadedMutableBigInteger.bitLength());
         obj4 = new ShadedMutableBigInteger(shadedMutableBigInteger);
         ShadedMutableBigInteger shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj4;
         shadedMutableBigInteger3.safeLeftShift(n3);
         obj2 = new ShadedMutableBigInteger(this);
         shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj2;
         shadedMutableBigInteger3.safeLeftShift(n3);
         n2 = (int)((((ShadedMutableBigInteger)obj2).bitLength() + l) / l);
         if (n2 < 2) {
            n2 = 2;
         }

         shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj2;
         obj5 = shadedMutableBigInteger3.getBlock(n2 - 1, n2, n6);
         shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj2;
         obj = shadedMutableBigInteger3.getBlock(n2 - 2, n2, n6);
         shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj;
         shadedMutableBigInteger3.addDisjoint((ShadedMutableBigInteger)obj5, n6);
         obj6 = new ShadedMutableBigInteger();

         for (int i = n2 - 2; i > 0; i--) {
            shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj;
            ShadedMutableBigInteger shadedMutableBigInteger5 = (ShadedMutableBigInteger)obj4;
            ShadedMutableBigInteger shadedMutableBigInteger6 = (ShadedMutableBigInteger)obj6;
            obj3 = shadedMutableBigInteger3.divide2n1n(shadedMutableBigInteger5, shadedMutableBigInteger6);
            shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj2;
            obj = shadedMutableBigInteger3.getBlock(i - 1, n2, n6);
            shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj;
            shadedMutableBigInteger3.addDisjoint((ShadedMutableBigInteger)obj3, n6);
            shadedMutableBigInteger3 = shadedMutableBigInteger2;
            shadedMutableBigInteger6 = (ShadedMutableBigInteger)obj6;
            shadedMutableBigInteger3.addShifted(shadedMutableBigInteger6, i * n6);
         }

         shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj;
         ShadedMutableBigInteger shadedMutableBigInteger11 = (ShadedMutableBigInteger)obj4;
         ShadedMutableBigInteger shadedMutableBigInteger12 = (ShadedMutableBigInteger)obj6;
         obj3 = shadedMutableBigInteger3.divide2n1n(shadedMutableBigInteger11, shadedMutableBigInteger12);
         shadedMutableBigInteger3 = shadedMutableBigInteger2;
         shadedMutableBigInteger12 = (ShadedMutableBigInteger)obj6;
         shadedMutableBigInteger3.add(shadedMutableBigInteger12);
         ((ShadedMutableBigInteger)obj3).rightShift(n3);
         return (ShadedMutableBigInteger)obj3;
      }
   }

   public void add(ShadedMutableBigInteger shadedMutableBigInteger) {
      int n = 0;
      long l = 0L;
      int n2 = 0;
      Object obj = null;
      int n3 = 0;
      Object obj2 = null;
      long l2 = 0L;
      int n4 = 0;
      n2 = this.intLen;
      n = shadedMutableBigInteger.intLen;
      n4 = this.intLen <= shadedMutableBigInteger.intLen ? shadedMutableBigInteger.intLen : this.intLen;
      obj2 = ((int[])this.value).length < n4 ? new int[n4] : this.value;
      n3 = ((int[])obj2).length - 1;
      l = 0L;

      while (n2 > 0 && n > 0) {
         l2 = ((long)this.value[--n2 + this.offset] & 4294967295L) + ((long)shadedMutableBigInteger.value[--n + shadedMutableBigInteger.offset] & 4294967295L) + l;
         ((int[])obj2)[n3--] = (int)l2;
         l = l2 >>> 32;
      }

      while (n2 > 0) {
         n2--;
         if (l == 0L && (int[])obj2 == this.value && n3 == n2 + this.offset) {
            return;
         }

         l2 = ((long)this.value[n2 + this.offset] & 4294967295L) + l;
         ((int[])obj2)[n3--] = (int)l2;
         l = l2 >>> 32;
      }

      while (n > 0) {
         l2 = ((long)shadedMutableBigInteger.value[--n + shadedMutableBigInteger.offset] & 4294967295L) + l;
         ((int[])obj2)[n3--] = (int)l2;
         l = l2 >>> 32;
      }

      if (l > 0L) {
         if (((int[])obj2).length < ++n4) {
            obj = new int[n4];
            System.arraycopy((int[])obj2, 0, (int[])obj, 1, ((int[])obj2).length);
            ((int[])obj)[0] = 1;
            obj2 = (int[])obj;
         } else {
            ((int[])obj2)[n3--] = 1;
         }
      }

      this.value = (int[])obj2;
      this.intLen = n4;
      this.offset = ((int[])obj2).length - n4;
   }

   public final void normalize() {
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      if (this.intLen == 0) {
         this.offset = 0;
      } else {
         n2 = this.offset;
         if (this.value[n2] == 0) {
            n3 = n2 + this.intLen;

            while (++n2 < n3) {
               if (this.value[n2] != 0) {
                  break;
               }
            }

            n = n2 - this.offset;
            this.intLen -= n;
            this.offset = this.intLen == 0 ? 0 : this.offset + n;
         }
      }
   }

   public ShadedMutableBigInteger divide(ShadedMutableBigInteger shadedMutableBigInteger, ShadedMutableBigInteger shadedMutableBigInteger2, boolean flag) {
      if (shadedMutableBigInteger.intLen >= 80 && this.intLen - shadedMutableBigInteger.intLen >= 40) {
         return this.divideAndRemainderBurnikelZiegler(shadedMutableBigInteger, shadedMutableBigInteger2);
      } else {
         return this.divideKnuth(shadedMutableBigInteger, shadedMutableBigInteger2, flag);
      }
   }

   private ShadedMutableBigInteger modInverse(ShadedMutableBigInteger shadedMutableBigInteger) {
      int n = 0;
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      Object obj5 = null;
      Object obj6 = null;
      Object obj7 = null;
      Object obj8 = null;
      int n2 = 0;
      obj = new ShadedMutableBigInteger(shadedMutableBigInteger);
      obj5 = new ShadedMutableBigInteger(this);
      obj8 = new ShadedMutableBigInteger((ShadedMutableBigInteger)obj);
      obj2 = new ShadedSignedMutableBigInteger(1);
      obj7 = new ShadedSignedMutableBigInteger();
      obj4 = null;
      obj6 = null;
      n = 0;
      if (((ShadedMutableBigInteger)obj5).isEven()) {
         n2 = ((ShadedMutableBigInteger)obj5).getLowestSetBit();
         ((ShadedMutableBigInteger)obj5).rightShift(n2);
         ((ShadedSignedMutableBigInteger)obj7).leftShift(n2);
         n = n2;
      }

      while (!((ShadedMutableBigInteger)obj5).isOne()) {
         if (((ShadedMutableBigInteger)obj5).isZero()) {
            throw new ArithmeticException("BigInteger not invertible.");
         }

         ShadedMutableBigInteger shadedMutableBigInteger2 = (ShadedMutableBigInteger)obj5;
         if (shadedMutableBigInteger2.compare((ShadedMutableBigInteger)obj8) < 0) {
            obj4 = (ShadedMutableBigInteger)obj5;
            obj5 = (ShadedMutableBigInteger)obj8;
            obj8 = (ShadedMutableBigInteger)obj4;
            obj6 = (ShadedSignedMutableBigInteger)obj7;
            obj7 = (ShadedSignedMutableBigInteger)obj2;
            obj2 = (ShadedSignedMutableBigInteger)obj6;
         }

         if ((
               (
                     ((ShadedMutableBigInteger)obj5).value[((ShadedMutableBigInteger)obj5).offset + ((ShadedMutableBigInteger)obj5).intLen - 1]
                        ^ ((ShadedMutableBigInteger)obj8).value[((ShadedMutableBigInteger)obj8).offset + ((ShadedMutableBigInteger)obj8).intLen - 1]
                  )
                  & 3
            )
            != 0) {
            shadedMutableBigInteger2 = (ShadedMutableBigInteger)obj5;
            shadedMutableBigInteger2.add((ShadedMutableBigInteger)obj8);
            ((ShadedSignedMutableBigInteger)obj2).signedAdd((ShadedSignedMutableBigInteger)obj7);
         } else {
            shadedMutableBigInteger2 = (ShadedMutableBigInteger)obj5;
            shadedMutableBigInteger2.subtract((ShadedMutableBigInteger)obj8);
            ((ShadedSignedMutableBigInteger)obj2).signedSubtract((ShadedSignedMutableBigInteger)obj7);
         }

         n2 = ((ShadedMutableBigInteger)obj5).getLowestSetBit();
         ((ShadedMutableBigInteger)obj5).rightShift(n2);
         ((ShadedSignedMutableBigInteger)obj7).leftShift(n2);
         n += n2;
      }

      ShadedSignedMutableBigInteger integer5 = (ShadedSignedMutableBigInteger)obj2;
      if (integer5.compare((ShadedMutableBigInteger)obj) >= 0) {
         integer5 = (ShadedSignedMutableBigInteger)obj2;
         obj3 = integer5.divide2((ShadedMutableBigInteger)obj, new ShadedMutableBigInteger());
         integer5 = (ShadedSignedMutableBigInteger)obj2;
         integer5.copyValue((ShadedMutableBigInteger)obj3);
      }

      if (((ShadedSignedMutableBigInteger)obj2).sign < 0) {
         integer5 = (ShadedSignedMutableBigInteger)obj2;
         integer5.signedAdd2((ShadedMutableBigInteger)obj);
      }

      integer5 = (ShadedSignedMutableBigInteger)obj2;
      return fixup(integer5, (ShadedMutableBigInteger)obj, n);
   }

   public CustomBigInteger toBigInteger(int n) {
      return this.intLen != 0 && n != 0 ? new CustomBigInteger(this.getMagnitudeArray(), n) : CustomBigInteger.ZERO;
   }

   private ShadedMutableBigInteger divide3n2n(ShadedMutableBigInteger shadedMutableBigInteger, ShadedMutableBigInteger shadedMutableBigInteger2) {
      Object obj = null;
      int n = 0;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      Object obj5 = null;
      n = shadedMutableBigInteger.intLen / 2;
      obj4 = new ShadedMutableBigInteger(this);
      ShadedMutableBigInteger shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj4;
      shadedMutableBigInteger3.safeRightShift(32 * n);
      obj5 = new ShadedMutableBigInteger(shadedMutableBigInteger);
      shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj5;
      shadedMutableBigInteger3.safeRightShift(n * 32);
      shadedMutableBigInteger3 = shadedMutableBigInteger;
      obj3 = shadedMutableBigInteger3.getLower(n);
      shadedMutableBigInteger3 = this;
      if (shadedMutableBigInteger3.compareShifted(shadedMutableBigInteger, n) >= 0) {
         shadedMutableBigInteger3 = shadedMutableBigInteger2;
         shadedMutableBigInteger3.ones(n);
         shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj4;
         shadedMutableBigInteger3.add((ShadedMutableBigInteger)obj5);
         shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj5;
         shadedMutableBigInteger3.leftShift(32 * n);
         shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj4;
         shadedMutableBigInteger3.subtract((ShadedMutableBigInteger)obj5);
         obj = (ShadedMutableBigInteger)obj4;
         obj2 = new ShadedMutableBigInteger((CustomBigInteger)obj3);
         shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj2;
         shadedMutableBigInteger3.leftShift(32 * n);
         shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj2;
         shadedMutableBigInteger3.subtract(new ShadedMutableBigInteger((CustomBigInteger)obj3));
      } else {
         shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj4;
         obj = shadedMutableBigInteger3.divide2n1n((ShadedMutableBigInteger)obj5, shadedMutableBigInteger2);
         CustomBigInteger customBigInteger = shadedMutableBigInteger2.toBigInteger();
         CustomBigInteger customBigInteger2 = (CustomBigInteger)obj3;
         obj2 = new ShadedMutableBigInteger(customBigInteger.multiply2(customBigInteger2));
      }

      shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj;
      shadedMutableBigInteger3.leftShift(32 * n);
      shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj;
      shadedMutableBigInteger3.addLower(this, n);

      while (true) {
         shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj;
         if (shadedMutableBigInteger3.compare((ShadedMutableBigInteger)obj2) >= 0) {
            shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj;
            shadedMutableBigInteger3.subtract((ShadedMutableBigInteger)obj2);
            return (ShadedMutableBigInteger)obj;
         }

         shadedMutableBigInteger3 = (ShadedMutableBigInteger)obj;
         shadedMutableBigInteger3.add(shadedMutableBigInteger);
         shadedMutableBigInteger3 = shadedMutableBigInteger2;
         shadedMutableBigInteger3.subtract(ONE);
      }
   }

   public ShadedMutableBigInteger euclidModInverse(int n) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      Object obj5 = null;
      Object obj6 = null;
      Object obj7 = null;
      Object obj8 = null;
      Object obj9 = null;
      obj3 = new ShadedMutableBigInteger(1);
      ((ShadedMutableBigInteger)obj3).leftShift(n);
      obj5 = new ShadedMutableBigInteger((ShadedMutableBigInteger)obj3);
      obj9 = new ShadedMutableBigInteger(this);
      obj8 = new ShadedMutableBigInteger();
      ShadedMutableBigInteger shadedMutableBigInteger = (ShadedMutableBigInteger)obj3;
      obj2 = shadedMutableBigInteger.divide2((ShadedMutableBigInteger)obj9, (ShadedMutableBigInteger)obj8);
      obj = (ShadedMutableBigInteger)obj3;
      obj3 = (ShadedMutableBigInteger)obj2;
      obj2 = (ShadedMutableBigInteger)obj;
      obj4 = new ShadedMutableBigInteger((ShadedMutableBigInteger)obj8);
      obj6 = new ShadedMutableBigInteger(1);
      obj7 = new ShadedMutableBigInteger();

      while (!((ShadedMutableBigInteger)obj3).isOne()) {
         shadedMutableBigInteger = (ShadedMutableBigInteger)obj9;
         obj2 = shadedMutableBigInteger.divide2((ShadedMutableBigInteger)obj3, (ShadedMutableBigInteger)obj8);
         if (((ShadedMutableBigInteger)obj2).intLen == 0) {
            throw new ArithmeticException("BigInteger not invertible.");
         }

         obj = (ShadedMutableBigInteger)obj2;
         obj9 = (ShadedMutableBigInteger)obj;
         if (((ShadedMutableBigInteger)obj8).intLen != 1) {
            shadedMutableBigInteger = (ShadedMutableBigInteger)obj8;
            shadedMutableBigInteger.multiply((ShadedMutableBigInteger)obj4, (ShadedMutableBigInteger)obj7);
         } else {
            shadedMutableBigInteger = (ShadedMutableBigInteger)obj4;
            shadedMutableBigInteger.mul(((ShadedMutableBigInteger)obj8).value[((ShadedMutableBigInteger)obj8).offset], (ShadedMutableBigInteger)obj7);
         }

         obj = (ShadedMutableBigInteger)obj8;
         obj8 = (ShadedMutableBigInteger)obj7;
         obj7 = (ShadedMutableBigInteger)obj;
         shadedMutableBigInteger = (ShadedMutableBigInteger)obj6;
         ShadedMutableBigInteger shadedMutableBigInteger12 = (ShadedMutableBigInteger)obj8;
         shadedMutableBigInteger.add(shadedMutableBigInteger12);
         if (((ShadedMutableBigInteger)obj9).isOne()) {
            return (ShadedMutableBigInteger)obj6;
         }

         shadedMutableBigInteger = (ShadedMutableBigInteger)obj3;
         obj2 = shadedMutableBigInteger.divide2((ShadedMutableBigInteger)obj9, (ShadedMutableBigInteger)obj8);
         if (((ShadedMutableBigInteger)obj2).intLen == 0) {
            throw new ArithmeticException("BigInteger not invertible.");
         }

         obj = (ShadedMutableBigInteger)obj3;
         obj3 = (ShadedMutableBigInteger)obj2;
         if (((ShadedMutableBigInteger)obj8).intLen != 1) {
            shadedMutableBigInteger = (ShadedMutableBigInteger)obj8;
            shadedMutableBigInteger.multiply((ShadedMutableBigInteger)obj6, (ShadedMutableBigInteger)obj7);
         } else {
            shadedMutableBigInteger = (ShadedMutableBigInteger)obj6;
            shadedMutableBigInteger.mul(((ShadedMutableBigInteger)obj8).value[((ShadedMutableBigInteger)obj8).offset], (ShadedMutableBigInteger)obj7);
         }

         obj = (ShadedMutableBigInteger)obj8;
         obj8 = (ShadedMutableBigInteger)obj7;
         obj7 = (ShadedMutableBigInteger)obj;
         shadedMutableBigInteger = (ShadedMutableBigInteger)obj4;
         shadedMutableBigInteger12 = (ShadedMutableBigInteger)obj8;
         shadedMutableBigInteger.add(shadedMutableBigInteger12);
      }

      shadedMutableBigInteger = (ShadedMutableBigInteger)obj5;
      shadedMutableBigInteger.subtract((ShadedMutableBigInteger)obj4);
      return (ShadedMutableBigInteger)obj5;
   }

   public long divide(long l, ShadedMutableBigInteger shadedMutableBigInteger) {
      int n = 0;
      long l2 = 0L;
      l2 = 5023878396925911683L ^ (long)"랮㎸\ue376Ⲵ㕟ꔌ龳ᠾ蘝犂\ud9c8젫㵏댗㻎ⱞ꼛붌ꙁ\uf54b\uebc3\ue81a롩㩁⯰".hashCode();
      if (l != 0L) {
         l2 = l2 - 6852002845522728695L ^ 3876871350506641914L;
         if (this.intLen != 0) {
            l2 = (l2 ^ -4809877876183625973L) - 4961832843839132097L;
            if (l >= 0L) {
               l2 = (l2 ^ -5403040879756184065L) + -8279213109956113686L;
            } else {
               l2 = l2 + 3647372975329484052L ^ -4751102523809350431L;
               l = -l;
               l2 = (l2 ^ -5548794356815925460L) - 2841273355540901428L;
            }

            n = (int)(l >>> 32);
            l2 = (l2 ^ -2425534966903758340L) - 4012733227421979982L;
            ShadedMutableBigInteger shadedMutableBigInteger2 = shadedMutableBigInteger;
            int n2;
            shadedMutableBigInteger2.clear();
            l2 = l2 + 3441605590125119790L ^ 906972266984663037L;
            if (n != 0) {
               l2 = l2 - 6259013886753795060L ^ -5650259026567949064L;
               shadedMutableBigInteger2 = this;
               return shadedMutableBigInteger2.divideLongMagnitude(l, shadedMutableBigInteger).toLong();
            } else {
               l2 = (l2 ^ 3302326221788493892L) - -3994065272474596065L;
               shadedMutableBigInteger2 = this;
               n2 = (int)l;
               return (long)shadedMutableBigInteger2.divideOneWord(n2, shadedMutableBigInteger) & 4294967295L;
            }
         } else {
            l2 = l2 - 5630995321108430766L ^ 7922786344159331308L;
            shadedMutableBigInteger.intLen = shadedMutableBigInteger.offset = 0;
            l2 = l2 - 4451890138658498152L ^ -8832611684016536178L;
            return 0L;
         }
      } else {
         l2 = (l2 ^ -5865554209298685232L) - 2288743068977970953L;
         throw new ArithmeticException("BigInteger divide by zero");
      }
   }

   public void copyValue(int[] ints, long l) {
      int n = 0;
      n = ints.length;
      if (this.value.length < n) {
         this.value = new int[n];
      }

      System.arraycopy(ints, 0, this.value, 0, n);
      this.intLen = n;
      this.offset = 0;
   }

   public ShadedMutableBigInteger(int n) {
      this.value = new int[1];
      this.intLen = 1;
      this.value[0] = n;
   }

   public ShadedMutableBigInteger divide2(ShadedMutableBigInteger shadedMutableBigInteger, ShadedMutableBigInteger shadedMutableBigInteger2) {
      return this.divide(shadedMutableBigInteger, shadedMutableBigInteger2, true);
   }

   public final int compare(ShadedMutableBigInteger shadedMutableBigInteger) {
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      boolean flag = false;
      Object obj = null;
      n = shadedMutableBigInteger.intLen;
      if (this.intLen < n) {
         return -1;
      } else if (this.intLen > n) {
         return 1;
      } else {
         obj = shadedMutableBigInteger.value;
         n4 = this.offset;

         for (int i = shadedMutableBigInteger.offset; n4 < this.intLen + this.offset; i++) {
            n2 = this.value[n4] + Integer.MIN_VALUE;
            n3 = ((int[])obj)[i] + Integer.MIN_VALUE;
            if (n2 < n3) {
               return -1;
            }

            if (n2 > n3) {
               return 1;
            }

            n4++;
         }

         return 0;
      }
   }

   private int[] getMagnitudeArray() {
      return this.offset <= 0 && ((int[])this.value).length == this.intLen ? this.value : Arrays.copyOfRange(this.value, this.offset, this.offset + this.intLen);
   }

   @Override
   public String toString() {
      return this.toBigInteger((byte)1).toString();
   }

   public boolean isEven() {
      return this.intLen == 0 || (this.value[this.offset + this.intLen - 1] & 1) == 0;
   }

   public ShadedMutableBigInteger divideKnuth(ShadedMutableBigInteger shadedMutableBigInteger, ShadedMutableBigInteger shadedMutableBigInteger2) {
      return this.divideKnuth(shadedMutableBigInteger, shadedMutableBigInteger2, true);
   }

   public static long divWord(long l, int n) {
      long l2 = 0L;
      long l3 = 0L;
      long l4 = 0L;
      long l5 = 0L;
      l5 = 2981210311007121467L ^ (long)"⫵蒶☦苴\uf346笓↥磻᫆\ue35d箲㊫쿍Ꭿ㶇\uf152５逼魭ᖳኦ嗟뿚㱠쑒曩䵰\udf9f\udecd".hashCode();
      l3 = (long)n & 4294967295L;
      l5 = l5 + 3728566632450520856L ^ 7573915170357857234L;
      if (l3 == 1L) {
         l5 = (l5 ^ 91411263364480049L) - 4818834505046913908L;
         l2 = (long)((int)l);
         l5 = l5 + -6541190535168580492L ^ -5083629216521834694L;
         l4 = 0L;
         l5 = (l5 ^ -5478720551081469486L) - -5900955677262498909L;
         return l4 << 32 | l2 & 4294967295L;
      } else {
         l5 = (l5 ^ -4603303278418924166L) + -4280696594624815803L;
         l2 = (l >>> 1) / (l3 >>> 1);
         l5 = l5 + -6636659406293245677L ^ 3262770225999402976L;
         l4 = l - l2 * l3;
         l5 = (l5 ^ 6158585324051682295L) + 4928905925557088435L;

         while (l4 < 0L) {
            l5 = (l5 ^ -6970332180757590678L) - -5739875117041552732L;
            l4 += l3;
            l5 = (l5 ^ -2233218954033822632L) + 1376810177976400463L;
            l2--;
            l5 = (l5 ^ 3127015832153721709L) - 4326579265979076540L;
            l5 = (l5 ^ -7635413636931541120L) + -1965999483690822456L;
         }

         l5 = l5 - -7832853149285089342L ^ -7002225833611650417L;

         while (l4 >= l3) {
            l5 = l5 - 3630965821938778044L ^ -8191068648328840047L;
            l4 -= l3;
            l5 = (l5 ^ 2288422392881191461L) + 1580914233415625738L;
            l2++;
            l5 = (l5 ^ -8070017777474865376L) + 2381103214204592468L;
            l5 = l5 + 1550881276500667973L ^ 5669215770461746597L;
         }

         l5 = l5 - 6930667229591269277L ^ 720838732726983077L;
         return l4 << 32 | l2 & 4294967295L;
      }
   }

   public void clear() {
      boolean flag = false;
      int n = 0;
      this.offset = this.intLen = 0;
      n = 0;

      for (int i = this.value.length; n < i; n++) {
         this.value[n] = 0;
      }
   }

   private void ones(int n) {
      if (n > this.value.length) {
         this.value = new int[n];
      }

      Arrays.fill(this.value, -1);
      this.offset = 0;
      this.intLen = n;
   }

   public void addLower(ShadedMutableBigInteger shadedMutableBigInteger, int n) {
      Object obj = null;
      obj = new ShadedMutableBigInteger(shadedMutableBigInteger);
      if (((ShadedMutableBigInteger)obj).offset + ((ShadedMutableBigInteger)obj).intLen >= n) {
         ((ShadedMutableBigInteger)obj).offset = ((ShadedMutableBigInteger)obj).offset + ((ShadedMutableBigInteger)obj).intLen - n;
         ((ShadedMutableBigInteger)obj).intLen = n;
      }

      ((ShadedMutableBigInteger)obj).normalize();
      this.add((ShadedMutableBigInteger)obj);
   }

   public ShadedMutableBigInteger(int[] ints) {
      this.value = ints;
      this.intLen = ints.length;
   }

   public void reset() {
      this.offset = this.intLen = 0;
   }

   public static long inverseMod64(long l) {
      long l2 = 0L;
      long l3 = 0L;
      l3 = 5232722629226796958L ^ (long)"̠锬촛淧ꁩ펳殽휵\u0a0bಆ蚰㪇苰০赃䀎ၾ㑑饶\uda0a䙶뫲⤿댒槅\uee75".hashCode();
      l3 = l3 - -8470237594473095735L ^ -1289222812500204533L;
      l2 = l * (2L - l * l);
      l3 = l3 - 1633834573432265237L ^ 3643817870888731761L;
      l2 *= 2L - l * l2;
      l3 = l3 + -6653471153620458457L ^ 3438418245156070756L;
      l2 *= 2L - l * l2;
      l3 = (l3 ^ -8600455075727507544L) - -6840314207779815395L;
      l2 *= 2L - l * l2;
      l3 = l3 + 6250870269046074472L ^ -2094896819384154993L;
      l2 *= 2L - l * l2;
      l3 = (l3 ^ -3467085583775212319L) - -6752991932599574613L;
      if (!ShadedMutableBigInteger.class.desiredAssertionStatus()) {
         l3 = l3 - 7275904741404328586L ^ -451085180790321504L;
      } else {
         l3 = (l3 ^ 7952400638890282947L) - -7729229829717254039L;
         if (l2 * l != 1L) {
            l3 = l3 - -3542722435090844150L ^ 7452124687819921759L;
            throw new AssertionError();
         }

         l3 = l3 + 8547140067611666527L ^ 638215053954574311L;
      }

      return l2;
   }

   private ShadedMutableBigInteger binaryGCD(ShadedMutableBigInteger shadedMutableBigInteger) {
      int n = 0;
      int n2 = 0;
      Object obj = null;
      int n3 = 0;
      Object obj2 = null;
      boolean flag = false;
      int n4 = 0;
      int n5 = 0;
      Object obj3 = null;
      int n6 = 0;
      boolean flag2 = false;
      obj2 = this;
      obj3 = new ShadedMutableBigInteger();
      n2 = ((ShadedMutableBigInteger)obj2).getLowestSetBit();
      n5 = shadedMutableBigInteger.getLowestSetBit();
      n6 = n2 >= n5 ? n5 : n2;
      if (n6 != 0) {
         ((ShadedMutableBigInteger)obj2).rightShift(n6);
         shadedMutableBigInteger.rightShift(n6);
      }

      flag2 = n6 == n2;
      obj = flag2 ? shadedMutableBigInteger : (ShadedMutableBigInteger)obj2;

      for (int i = flag2 ? -1 : 1; (n = ((ShadedMutableBigInteger)obj).getLowestSetBit()) >= 0; obj = i >= 0 ? (ShadedMutableBigInteger)obj2 : shadedMutableBigInteger) {
         ((ShadedMutableBigInteger)obj).rightShift(n);
         if (i > 0) {
            obj2 = (ShadedMutableBigInteger)obj;
         } else {
            shadedMutableBigInteger = (ShadedMutableBigInteger)obj;
         }

         if (((ShadedMutableBigInteger)obj2).intLen < 2 && shadedMutableBigInteger.intLen < 2) {
            n3 = ((ShadedMutableBigInteger)obj2).value[((ShadedMutableBigInteger)obj2).offset];
            n4 = shadedMutableBigInteger.value[shadedMutableBigInteger.offset];
            n3 = binaryGcd(n3, n4);
            ((ShadedMutableBigInteger)obj3).value[0] = n3;
            ((ShadedMutableBigInteger)obj3).intLen = 1;
            ((ShadedMutableBigInteger)obj3).offset = 0;
            if (n6 > 0) {
               ((ShadedMutableBigInteger)obj3).leftShift(n6);
            }

            return (ShadedMutableBigInteger)obj3;
         }

         if ((i = ((ShadedMutableBigInteger)obj2).difference(shadedMutableBigInteger)) == 0) {
            break;
         }
      }

      if (n6 > 0) {
         ((ShadedMutableBigInteger)obj2).leftShift(n6);
      }

      return (ShadedMutableBigInteger)obj2;
   }

   public void safeRightShift(int n) {
      if (n / 32 < this.intLen) {
         this.rightShift(n);
      } else {
         this.reset();
      }
   }

   public void safeLeftShift(int n) {
      if (n > 0) {
         this.leftShift(n);
      }
   }

   private ShadedMutableBigInteger getBlock(int n, int n2, int n3) {
      Object obj = null;
      int n4 = 0;
      int n5 = 0;
      n4 = n * n3;
      if (n4 < this.intLen) {
         if (n != n2 - 1) {
            n5 = (n + 1) * n3;
         } else {
            n5 = this.intLen;
         }

         if (n5 <= this.intLen) {
            obj = Arrays.copyOfRange(this.value, this.offset + this.intLen - n5, this.offset + this.intLen - n4);
            return new ShadedMutableBigInteger((int[])obj);
         } else {
            return new ShadedMutableBigInteger();
         }
      } else {
         return new ShadedMutableBigInteger();
      }
   }

   private ShadedMutableBigInteger divideLongMagnitude(long l, ShadedMutableBigInteger shadedMutableBigInteger) {
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      Object obj = null;
      boolean flag = false;
      long l2 = 0L;
      int n6 = 0;
      long l3 = 0L;
      long l4 = 0L;
      boolean flag2 = false;
      boolean flag3 = false;
      int n7 = 0;
      long l5 = 0L;
      int n8 = 0;
      Object obj2 = null;
      int n9 = 0;
      int n10 = 0;
      obj2 = new ShadedMutableBigInteger(new int[this.intLen + 1]);
      System.arraycopy(this.value, this.offset, ((ShadedMutableBigInteger)obj2).value, 1, this.intLen);
      ((ShadedMutableBigInteger)obj2).intLen = this.intLen;
      ((ShadedMutableBigInteger)obj2).offset = 1;
      n8 = ((ShadedMutableBigInteger)obj2).intLen;
      n10 = n8 - 2 + 1;
      if (shadedMutableBigInteger.value.length < n10) {
         shadedMutableBigInteger.value = new int[n10];
         shadedMutableBigInteger.offset = 0;
      }

      shadedMutableBigInteger.intLen = n10;
      obj = shadedMutableBigInteger.value;
      long l6 = (long)Long.numberOfLeadingZeros(l);
      if (l6 > 0) {
         l <<= l6;
         ((ShadedMutableBigInteger)obj2).leftShift((int)l6);
      }

      if (((ShadedMutableBigInteger)obj2).intLen == n8) {
         ((ShadedMutableBigInteger)obj2).offset = 0;
         ((ShadedMutableBigInteger)obj2).value[0] = 0;
         ((ShadedMutableBigInteger)obj2).intLen++;
      }

      n9 = (int)(l >>> 32);
      l5 = (long)n9 & 4294967295L;
      n3 = (int)(l & 4294967295L);

      for (int i = 0; i < n10; i++) {
         n5 = 0;
         n4 = 0;
         flag2 = false;
         n6 = ((ShadedMutableBigInteger)obj2).value[i + ((ShadedMutableBigInteger)obj2).offset];
         n7 = n6 + Integer.MIN_VALUE;
         n = ((ShadedMutableBigInteger)obj2).value[i + 1 + ((ShadedMutableBigInteger)obj2).offset];
         if (n6 != n9) {
            l3 = (long)n6 << 32 | (long)n & 4294967295L;
            if (l3 >= 0L) {
               n5 = (int)(l3 / l5);
               n4 = (int)(l3 - (long)n5 * l5);
            } else {
               l4 = divWord(l3, n9);
               n5 = (int)(l4 & 4294967295L);
               n4 = (int)(l4 >>> 32);
            }
         } else {
            n5 = -1;
            n4 = n6 + n;
            flag2 = n4 + Integer.MIN_VALUE < n7;
         }

         if (n5 != 0) {
            if (!flag2) {
               l3 = (long)((ShadedMutableBigInteger)obj2).value[i + 2 + ((ShadedMutableBigInteger)obj2).offset] & 4294967295L;
               l4 = ((long)n4 & 4294967295L) << 32 | l3;
               l2 = ((long)n3 & 4294967295L) * ((long)n5 & 4294967295L);
               if (this.unsignedLongCompare(l2, l4)) {
                  n5--;
                  n4 = (int)(((long)n4 & 4294967295L) + l5);
                  if (((long)n4 & 4294967295L) >= l5) {
                     l2 -= (long)n3 & 4294967295L;
                     l4 = ((long)n4 & 4294967295L) << 32 | l3;
                     if (this.unsignedLongCompare(l2, l4)) {
                        n5--;
                     }
                  }
               }
            }

            ((ShadedMutableBigInteger)obj2).value[i + ((ShadedMutableBigInteger)obj2).offset] = 0;
            ShadedMutableBigInteger shadedMutableBigInteger2 = this;
            int n11 = i + ((ShadedMutableBigInteger)obj2).offset;
            n2 = shadedMutableBigInteger2.mulsubLong(((ShadedMutableBigInteger)obj2).value, n9, n3, n5, n11);
            if (n2 + Integer.MIN_VALUE > n7) {
               shadedMutableBigInteger2 = this;
               int n12 = i + 1 + ((ShadedMutableBigInteger)obj2).offset;
               shadedMutableBigInteger2.divaddLong(n9, n3, ((ShadedMutableBigInteger)obj2).value, n12);
               n5--;
            }

            ((int[])obj)[i] = n5;
         }
      }

      if (l6 > 0) {
         ((ShadedMutableBigInteger)obj2).rightShift((int)l6);
      }

      shadedMutableBigInteger.normalize();
      ((ShadedMutableBigInteger)obj2).normalize();
      return (ShadedMutableBigInteger)obj2;
   }

   private final long getLong(int n, long l) {
      return (long)this.value[this.offset + n] & 0L;
   }

   private int divadd(int[] ints, int[] ints2, int n, long l) {
      long l2 = 0L;
      int n2 = 0;
      long l3 = 0L;
      long l4 = 0L;
      l4 = -5886630035406777291L ^ (long)"揋㥂쇇芞鑯\ue2ba샴￥។\ue829⹖표ꮨ뛪\udf24\udda4삿ừﳣ㏭康ᾬ".hashCode();
      l2 = 0L;
      l4 = (l4 ^ -7998118130129351695L) + 8634744871577686989L;
      n2 = ints.length - 1;
      l4 = (l4 ^ 352914588511019058L) + -642998777224005032L;

      while (n2 >= 0) {
         l4 = (l4 ^ 4820892878351897098L) - 3489743826225945485L;
         l3 = ((long)ints[n2] & 4294967295L) + ((long)ints2[n2 + n] & 4294967295L) + l2;
         l4 = l4 - 126069796989802991L ^ 175524000508780301L;
         ints2[n2 + n] = (int)l3;
         l4 = (l4 ^ 2696287504226523094L) - -7879833640780153497L;
         l2 = l3 >>> 32;
         l4 = l4 - -6532257327089568143L ^ -8569160055668602248L;
         n2--;
         l4 = l4 + 2734698081616438740L ^ -8912457762262899275L;
         l4 = l4 - 8438802206805714201L ^ 6312667547988157381L;
      }

      l4 = l4 + -3109992264448926235L ^ 5160903894060253845L;
      return (int)l2;
   }

   public void mul(int n, ShadedMutableBigInteger shadedMutableBigInteger) {
      boolean flag = false;
      long l = 0L;
      Object obj = null;
      long l2 = 0L;
      long l3 = 0L;
      if (n == 1) {
         shadedMutableBigInteger.copyValue(this);
      } else if (n == 0) {
         shadedMutableBigInteger.clear();
      } else {
         l = (long)n & 4294967295L;
         obj = ((int[])shadedMutableBigInteger.value).length >= this.intLen + 1 ? shadedMutableBigInteger.value : new int[this.intLen + 1];
         l2 = 0L;

         for (int i = this.intLen - 1; i >= 0; i--) {
            l3 = l * ((long)this.value[i + this.offset] & 4294967295L) + l2;
            ((int[])obj)[i + 1] = (int)l3;
            l2 = l3 >>> 32;
         }

         if (l2 == 0L) {
            shadedMutableBigInteger.offset = 1;
            shadedMutableBigInteger.intLen = this.intLen;
         } else {
            shadedMutableBigInteger.offset = 0;
            shadedMutableBigInteger.intLen = this.intLen + 1;
            ((int[])obj)[0] = (int)l2;
         }

         shadedMutableBigInteger.value = (int[])obj;
      }
   }

   public ShadedMutableBigInteger modInverseMP2(int n) {
      long l = 0L;
      long l2 = 0L;
      Object obj = null;
      int n2 = 0;
      if (!this.isEven()) {
         if (n <= 64) {
            n2 = inverseMod32(this.value[this.offset + this.intLen - 1]);
            if (n >= 33) {
               l2 = (long)this.value[this.offset + this.intLen - 1] & 4294967295L;
               if (this.intLen > 1) {
                  l2 |= (long)this.value[this.offset + this.intLen - 2] << 32;
               }

               l = (long)n2 & 4294967295L;
               l *= 2L - l2 * l;
               l = n == 64 ? l : l & (1L << n) - 1L;
               obj = new ShadedMutableBigInteger(new int[2]);
               ((ShadedMutableBigInteger)obj).value[0] = (int)(l >>> 32);
               ((ShadedMutableBigInteger)obj).value[1] = (int)l;
               ((ShadedMutableBigInteger)obj).intLen = 2;
               ((ShadedMutableBigInteger)obj).normalize();
               return (ShadedMutableBigInteger)obj;
            } else {
               n2 = n != 32 ? n2 & (1 << n) - 1 : n2;
               return new ShadedMutableBigInteger(n2);
            }
         } else {
            return this.euclidModInverse(n);
         }
      } else {
         throw new ArithmeticException("Non-invertible. (GCD != 1)");
      }
   }

   public ShadedMutableBigInteger mutableModInverse(ShadedMutableBigInteger shadedMutableBigInteger) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      Object obj5 = null;
      Object obj6 = null;
      Object obj7 = null;
      Object obj8 = null;
      int n = 0;
      if (!shadedMutableBigInteger.isOdd()) {
         if (!this.isEven()) {
            n = shadedMutableBigInteger.getLowestSetBit();
            obj2 = new ShadedMutableBigInteger(shadedMutableBigInteger);
            ((ShadedMutableBigInteger)obj2).rightShift(n);
            if (!((ShadedMutableBigInteger)obj2).isOne()) {
               ShadedMutableBigInteger shadedMutableBigInteger2 = this;
               obj7 = shadedMutableBigInteger2.modInverse((ShadedMutableBigInteger)obj2);
               obj8 = this.modInverseMP2(n);
               obj = modInverseBP2((ShadedMutableBigInteger)obj2, n);
               obj3 = ((ShadedMutableBigInteger)obj2).modInverseMP2(n);
               obj6 = new ShadedMutableBigInteger();
               obj5 = new ShadedMutableBigInteger();
               obj4 = new ShadedMutableBigInteger();
               ((ShadedMutableBigInteger)obj7).leftShift(n);
               shadedMutableBigInteger2 = (ShadedMutableBigInteger)obj7;
               shadedMutableBigInteger2.multiply((ShadedMutableBigInteger)obj, (ShadedMutableBigInteger)obj4);
               shadedMutableBigInteger2 = (ShadedMutableBigInteger)obj8;
               shadedMutableBigInteger2.multiply((ShadedMutableBigInteger)obj2, (ShadedMutableBigInteger)obj6);
               shadedMutableBigInteger2 = (ShadedMutableBigInteger)obj6;
               shadedMutableBigInteger2.multiply((ShadedMutableBigInteger)obj3, (ShadedMutableBigInteger)obj5);
               shadedMutableBigInteger2 = (ShadedMutableBigInteger)obj4;
               shadedMutableBigInteger2.add((ShadedMutableBigInteger)obj5);
               shadedMutableBigInteger2 = (ShadedMutableBigInteger)obj4;
               return shadedMutableBigInteger2.divide2(shadedMutableBigInteger, (ShadedMutableBigInteger)obj6);
            } else {
               return this.modInverseMP2(n);
            }
         } else {
            throw new ArithmeticException("BigInteger not invertible.");
         }
      } else {
         return this.modInverse(shadedMutableBigInteger);
      }
   }

   public int subtract(ShadedMutableBigInteger shadedMutableBigInteger) {
      Object obj = null;
      Object obj2 = null;
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      Object obj3 = null;
      long l = 0L;
      int n4 = 0;
      int n5 = 0;
      obj3 = this;
      obj2 = this.value;
      n4 = ((ShadedMutableBigInteger)obj3).compare(shadedMutableBigInteger);
      if (n4 == 0) {
         this.reset();
         return 0;
      } else {
         if (n4 < 0) {
            obj = (ShadedMutableBigInteger)obj3;
            obj3 = shadedMutableBigInteger;
            shadedMutableBigInteger = (ShadedMutableBigInteger)obj;
         }

         n2 = ((ShadedMutableBigInteger)obj3).intLen;
         if (((int[])obj2).length < n2) {
            obj2 = new int[n2];
         }

         l = 0L;
         n3 = ((ShadedMutableBigInteger)obj3).intLen;
         n = shadedMutableBigInteger.intLen;

         for (n5 = ((int[])obj2).length - 1; n > 0; ((int[])obj2)[n5--] = (int)l) {
            n3--;
            n--;
            l = ((long)((ShadedMutableBigInteger)obj3).value[n3 + ((ShadedMutableBigInteger)obj3).offset] & 4294967295L)
               - ((long)shadedMutableBigInteger.value[n + shadedMutableBigInteger.offset] & 4294967295L)
               - (long)((int)(-(l >> 32)));
         }

         while (n3 > 0) {
            n3--;
            l = ((long)((ShadedMutableBigInteger)obj3).value[n3 + ((ShadedMutableBigInteger)obj3).offset] & 4294967295L) - (long)((int)(-(l >> 32)));
            ((int[])obj2)[n5--] = (int)l;
         }

         this.value = (int[])obj2;
         this.intLen = n2;
         this.offset = this.value.length - n2;
         this.normalize();
         return n4;
      }
   }

   public void setValue(int[] ints, int n) {
      this.value = ints;
      this.intLen = n;
      this.offset = 0;
   }

   private final int getLowestSetBit() {
      int n = 0;
      int n2 = 0;
      if (this.intLen == 0) {
         return -1;
      } else {
         n2 = this.intLen - 1;

         while (n2 > 0 && this.value[n2 + this.offset] == 0) {
            n2--;
         }

         n = this.value[n2 + this.offset];
         return n != 0 ? (this.intLen - 1 - n2 << 5) + Integer.numberOfTrailingZeros(n) : -1;
      }
   }

   private int divaddLong(int n, int n2, int[] ints, int n3) {
      long l = 0L;
      long l2 = 0L;
      long l3 = 0L;
      l3 = -7990517267222312539L ^ (long)"\ue0ba⽂匡ᰛ瓼㛄퍀\uf2e6薗蕉嵩끠矂⊆\u0d53䤹ꕜ⎴ย麰\uaaf9Ð甪詜\ud927\uf8c1콘獍ᇻ".hashCode();
      l2 = 0L;
      l3 = l3 + 3260727163877733308L ^ -5671356675579083003L;
      l = ((long)n2 & 4294967295L) + ((long)ints[1 + n3] & 4294967295L);
      l3 = (l3 ^ -1943265049040865423L) + 5801590154836030934L;
      ints[1 + n3] = (int)l;
      l3 = (l3 ^ -7771171717157594358L) + -992096237484356824L;
      l = ((long)n & 4294967295L) + ((long)ints[n3] & 4294967295L) + l2;
      l3 = l3 + -5386199935804885075L ^ -8901046064371003116L;
      ints[n3] = (int)l;
      l3 = (l3 ^ -6578606087552826163L) + 7037684941641669248L;
      l2 = l >>> 32;
      l3 = (l3 ^ -1321974457172254452L) + -5300566760593004727L;
      return (int)l2;
   }

   public void multiply(ShadedMutableBigInteger shadedMutableBigInteger, ShadedMutableBigInteger shadedMutableBigInteger2) {
      long l = 0L;
      boolean flag = false;
      long l2 = 0L;
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      long l3 = 0L;
      n5 = this.intLen;
      n2 = shadedMutableBigInteger.intLen;
      n = n5 + n2;
      if (shadedMutableBigInteger2.value.length < n) {
         shadedMutableBigInteger2.value = new int[n];
      }

      shadedMutableBigInteger2.offset = 0;
      shadedMutableBigInteger2.intLen = n;
      l = 0L;
      n4 = n2 - 1;

      for (int i = n2 + n5 - 1; n4 >= 0; i--) {
         l2 = ((long)shadedMutableBigInteger.value[n4 + shadedMutableBigInteger.offset] & 4294967295L) * ((long)this.value[n5 - 1 + this.offset] & 4294967295L) + l;
         shadedMutableBigInteger2.value[i] = (int)l2;
         l = l2 >>> 32;
         n4--;
      }

      shadedMutableBigInteger2.value[n5 - 1] = (int)l;

      for (int j = n5 - 2; j >= 0; j--) {
         l = 0L;
         n3 = n2 - 1;

         for (int k = n2 + j; n3 >= 0; k--) {
            l3 = ((long)shadedMutableBigInteger.value[n3 + shadedMutableBigInteger.offset] & 4294967295L) * ((long)this.value[j + this.offset] & 4294967295L)
               + ((long)shadedMutableBigInteger2.value[k] & 4294967295L)
               + l;
            shadedMutableBigInteger2.value[k] = (int)l3;
            l = l3 >>> 32;
            n3--;
         }

         shadedMutableBigInteger2.value[j] = (int)l;
      }

      shadedMutableBigInteger2.normalize();
   }

   public void setInt(int n, int n2) {
      this.value[this.offset + n] = n2;
   }

   private int compareShifted(ShadedMutableBigInteger shadedMutableBigInteger, int n) {
      int n2 = 0;
      int n3 = 0;
      boolean flag = false;
      int n4 = 0;
      int n5 = 0;
      int n6 = 0;
      Object obj = null;
      n4 = shadedMutableBigInteger.intLen;
      n6 = this.intLen - n;
      if (n6 < n4) {
         return -1;
      } else if (n6 > n4) {
         return 1;
      } else {
         obj = shadedMutableBigInteger.value;
         n3 = this.offset;

         for (int i = shadedMutableBigInteger.offset; n3 < n6 + this.offset; i++) {
            n2 = this.value[n3] + Integer.MIN_VALUE;
            n5 = ((int[])obj)[i] + Integer.MIN_VALUE;
            if (n2 < n5) {
               return -1;
            }

            if (n2 > n5) {
               return 1;
            }

            n3++;
         }

         return 0;
      }
   }

   public void copyValue(ShadedMutableBigInteger shadedMutableBigInteger) {
      int n = 0;
      n = shadedMutableBigInteger.intLen;
      if (this.value.length < n) {
         this.value = new int[n];
      }

      System.arraycopy(shadedMutableBigInteger.value, shadedMutableBigInteger.offset, this.value, 0, n);
      this.intLen = n;
      this.offset = 0;
   }

   private CustomBigInteger getLower(int n) {
      int n2 = 0;
      int n3 = 0;
      if (this.isZero()) {
         return CustomBigInteger.ZERO;
      } else if (this.intLen < n) {
         return this.toBigInteger((byte)1);
      } else {
         n3 = n;

         while (n3 > 0 && this.value[this.offset + this.intLen - n3] == 0) {
            n3--;
         }

         n2 = n3 <= 0 ? 0 : 1;
         return new CustomBigInteger(Arrays.copyOfRange(this.value, this.offset + this.intLen - n3, this.offset + this.intLen), n2);
      }
   }

   public static ShadedMutableBigInteger fixup(ShadedMutableBigInteger shadedMutableBigInteger, ShadedMutableBigInteger shadedMutableBigInteger2, int n) {
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      int n5 = 0;
      Object obj = null;
      obj = new ShadedMutableBigInteger();
      n5 = -inverseMod32(shadedMutableBigInteger2.value[shadedMutableBigInteger2.offset + shadedMutableBigInteger2.intLen - 1]);
      n3 = 0;

      for (int i = n >> 5; n3 < i; n3++) {
         n2 = n5 * shadedMutableBigInteger.value[shadedMutableBigInteger.offset + shadedMutableBigInteger.intLen - 1];
         ShadedMutableBigInteger shadedMutableBigInteger3 = shadedMutableBigInteger2;
         shadedMutableBigInteger3.mul(n2, (ShadedMutableBigInteger)obj);
         shadedMutableBigInteger3 = shadedMutableBigInteger;
         shadedMutableBigInteger3.add((ShadedMutableBigInteger)obj);
         shadedMutableBigInteger.intLen--;
      }

      n3 = n & 31;
      if (n3 != 0) {
         n4 = n5 * shadedMutableBigInteger.value[shadedMutableBigInteger.offset + shadedMutableBigInteger.intLen - 1];
         n4 &= (1 << n3) - 1;
         ShadedMutableBigInteger shadedMutableBigInteger7 = shadedMutableBigInteger2;
         shadedMutableBigInteger7.mul(n4, (ShadedMutableBigInteger)obj);
         shadedMutableBigInteger7 = shadedMutableBigInteger;
         shadedMutableBigInteger7.add((ShadedMutableBigInteger)obj);
         shadedMutableBigInteger.rightShift(n3);
      }

      ShadedMutableBigInteger shadedMutableBigInteger11 = shadedMutableBigInteger;
      if (shadedMutableBigInteger11.compare(shadedMutableBigInteger2) >= 0) {
         shadedMutableBigInteger11 = shadedMutableBigInteger;
         shadedMutableBigInteger = shadedMutableBigInteger11.divide2(shadedMutableBigInteger2, new ShadedMutableBigInteger());
      }

      return shadedMutableBigInteger;
   }

}
