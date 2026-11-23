package net.augustus.vendor.launcher.crypto.math;

import java.util.Arrays;

public class MutableBigInt {
   public static final MutableBigInt ONE;
   public static final int KNUTH_POW2_THRESH_LEN = 0;
   public static final int KNUTH_POW2_THRESH_ZEROS = 0;
   public int offset;
   public int[] value;
   public int intLen;

   public void addLower(MutableBigInt mutableBigInt, int n) {
      MutableBigInt mutableBigInt2 = new MutableBigInt(mutableBigInt);
      if (mutableBigInt2.offset + mutableBigInt2.intLen >= n) {
         mutableBigInt2.offset = mutableBigInt2.offset + mutableBigInt2.intLen - n;
         mutableBigInt2.intLen = n;
      }

      mutableBigInt2.normalize();
      this.add(mutableBigInt2);
   }

   public int[] toIntArray() {
      int[] ints = new int[this.intLen];

      for (int i = 0; i < this.intLen; i++) {
         ints[i] = this.value[this.offset + i];
      }

      return ints;
   }

   public static long inverseMod64(long l) {
      long l2 = l * (2L - l * l);
      l2 *= 2L - l * l2;
      l2 *= 2L - l * l2;
      l2 *= 2L - l * l2;
      l2 *= 2L - l * l2;

      assert l2 * l == 1L;

      return l2;
   }

   private ShadedBigInteger getLower(int n) {
      if (this.isZero()) {
         return ShadedBigInteger.ZERO;
      } else if (this.intLen < n) {
         return this.toBigInteger((byte)1);
      } else {
         int n2 = n;

         while (n2 > 0 && this.value[this.offset + this.intLen - n2] == 0) {
            n2--;
         }

         int n3 = n2 > 0 ? 1 : 0;
         return new ShadedBigInteger(Arrays.copyOfRange(this.value, this.offset + this.intLen - n2, this.offset + this.intLen), n3);
      }
   }

   public boolean isNormal(long l) {
      if (this.intLen + this.offset > this.value.length) {
         return false;
      } else {
         return this.intLen == 0 ? true : this.value[this.offset] != 0;
      }
   }

   private void ones(int n) {
      if (n > this.value.length) {
         this.value = new int[n];
      }

      Arrays.fill(this.value, -1);
      this.offset = (byte)0;
      this.intLen = n;
   }

   private int difference(MutableBigInt mutableBigInt) {
      MutableBigInt mutableBigInt2 = this;
      int n = this.compare(mutableBigInt);
      if (n == 0) {
         return 0;
      } else {
         if (n < 0) {
            mutableBigInt2 = mutableBigInt;
            mutableBigInt = this;
         }

         long l = 0L;
         int intLen2 = mutableBigInt2.intLen;

         for (int i = mutableBigInt.intLen; i > 0; mutableBigInt2.value[mutableBigInt2.offset + intLen2] = (int)l) {
            intLen2--;
            i--;
            l = ((long)mutableBigInt2.value[mutableBigInt2.offset + intLen2] & 4294967295L) - ((long)mutableBigInt.value[mutableBigInt.offset + i] & 4294967295L) - (long)((int)(-(l >> 32)));
         }

         while (intLen2 > 0) {
            intLen2--;
            l = ((long)mutableBigInt2.value[mutableBigInt2.offset + intLen2] & 4294967295L) - (long)((int)(-(l >> 32)));
            mutableBigInt2.value[mutableBigInt2.offset + intLen2] = (int)l;
         }

         mutableBigInt2.normalize();
         return n;
      }
   }

   public final void normalize() {
      if (this.intLen == 0) {
         this.offset = (byte)0;
      } else {
         int offset2 = this.offset;
         if (this.value[offset2] == 0) {
            int n = offset2 + this.intLen;

            do {
               offset2++;
            } while (offset2 < n && this.value[offset2] == 0);

            int n2 = offset2 - this.offset;
            this.intLen = this.intLen - n2;
            this.offset = this.intLen == 0 ? 0 : this.offset + n2;
         }
      }
   }

   private final int getInt(int n, long l) {
      return this.value[this.offset + n];
   }

   public static MutableBigInt modInverseBP2(MutableBigInt mutableBigInt, int n) {
      return fixup(new MutableBigInt(1), new MutableBigInt(mutableBigInt), n);
   }

   public void mul(int n, MutableBigInt mutableBigInt) {
      if (n == 1) {
         mutableBigInt.copyValue(this);
      } else if (n == 0) {
         mutableBigInt.clear();
      } else {
         long l = (long)n & 4294967295L;
         int[] ints = mutableBigInt.value.length < this.intLen + 1 ? new int[this.intLen + 1] : mutableBigInt.value;
         long l2 = 0L;

         for (int i = this.intLen - 1; i >= 0; i--) {
            long l3 = l * ((long)this.value[i + this.offset] & 4294967295L) + l2;
            ints[i + 1] = (int)l3;
            l2 = l3 >>> 32;
         }

         if (l2 == 0L) {
            mutableBigInt.offset = (byte)1;
            mutableBigInt.intLen = this.intLen;
         } else {
            mutableBigInt.offset = (byte)0;
            mutableBigInt.intLen = this.intLen + 1;
            ints[0] = (int)l2;
         }

         mutableBigInt.value = ints;
      }
   }

   public MutableBigInt(int n) {
      this.offset = (byte)0;
      this.value = new int[1];
      this.intLen = (byte)1;
      this.value[0] = n;
   }

   public static int inverseMod32(int n) {
      int n2 = n * (2 - n * n);
      n2 *= 2 - n * n2;
      n2 *= 2 - n * n2;
      return n2 * (2 - n * n2);
   }

   public MutableBigInt hybridGCD(MutableBigInt mutableBigInt) {
      MutableBigInt mutableBigInt2 = this;
      MutableBigInt mutableBigInt3 = new MutableBigInt();

      while (mutableBigInt.intLen != 0) {
         if (Math.abs(mutableBigInt2.intLen - mutableBigInt.intLen) < 2) {
            return mutableBigInt2.binaryGCD(mutableBigInt);
         }

         MutableBigInt mutableBigInt4 = mutableBigInt2.divide2(mutableBigInt, mutableBigInt3);
         mutableBigInt2 = mutableBigInt;
         mutableBigInt = mutableBigInt4;
      }

      return mutableBigInt2;
   }

   public void clear() {
      byte b;
      byte b2 = b = 0;
      this.intLen = b;
      this.offset = b2;
      int n = 0;

      for (int i = this.value.length; n < i; n++) {
         this.value[n] = 0;
      }
   }

   public void safeLeftShift(int n) {
      if (n > 0) {
         this.leftShift(n);
      }
   }

   public MutableBigInt divide(MutableBigInt mutableBigInt, MutableBigInt mutableBigInt2, boolean flag) {
      return mutableBigInt.intLen >= 80 && this.intLen - mutableBigInt.intLen >= 40 ? this.divideAndRemainderBurnikelZiegler(mutableBigInt, mutableBigInt2) : this.divideKnuth(mutableBigInt, mutableBigInt2, flag);
   }

   private final void primitiveRightShift(int n) {
      int[] value2 = this.value;
      int n2 = 32 - n;
      int n3 = this.offset + this.intLen - 1;

      for (int i = value2[n3]; n3 > this.offset; n3--) {
         int n4 = i;
         i = value2[n3 - 1];
         value2[n3] = i << n2 | n4 >>> n;
      }

      value2[this.offset] = value2[this.offset] >>> n;
   }

   public MutableBigInt(int[] ints) {
      this.offset = (byte)0;
      this.value = ints;
      this.intLen = ints.length;
   }

   public boolean isOdd() {
      return this.isZero() ? false : (this.value[this.offset + this.intLen - 1] & 1) == 1;
   }

   public MutableBigInt divideKnuth(MutableBigInt mutableBigInt, MutableBigInt mutableBigInt2, boolean flag) {
      if (mutableBigInt.intLen == 0) {
         throw new ArithmeticException("BigInteger divide by zero");
      } else if (this.intLen == 0) {
         byte b;
         byte b2 = b = 0;
         mutableBigInt2.offset = b;
         mutableBigInt2.intLen = b2;
         return flag ? new MutableBigInt() : null;
      } else {
         int n = this.compare(mutableBigInt);
         if (n < 0) {
            byte b4;
            byte b5 = b4 = 0;
            mutableBigInt2.offset = b4;
            mutableBigInt2.intLen = b5;
            return flag ? new MutableBigInt(this) : null;
         } else if (n == 0) {
            int[] value2 = mutableBigInt2.value;
            byte b7;
            byte b8 = b7 = 1;
            mutableBigInt2.intLen = b7;
            value2[0] = b8;
            mutableBigInt2.offset = (byte)0;
            return flag ? new MutableBigInt() : null;
         } else {
            mutableBigInt2.clear();
            if (mutableBigInt.intLen == 1) {
               int n3 = this.divideOneWord(mutableBigInt.value[mutableBigInt.offset], mutableBigInt2);
               if (flag) {
                  return n3 == 0 ? new MutableBigInt() : new MutableBigInt(n3);
               } else {
                  return null;
               }
            } else {
               if (this.intLen >= 6) {
                  int n4 = Math.min(this.getLowestSetBit(), mutableBigInt.getLowestSetBit());
                  if (n4 >= 96) {
                     MutableBigInt mutableBigInt3 = new MutableBigInt(this);
                     mutableBigInt = new MutableBigInt(mutableBigInt);
                     mutableBigInt3.rightShift(n4);
                     mutableBigInt.rightShift(n4);
                     MutableBigInt mutableBigInt4 = mutableBigInt3.divideKnuth(mutableBigInt, mutableBigInt2);
                     mutableBigInt4.leftShift(n4);
                     return mutableBigInt4;
                  }
               }

               return this.divideMagnitude(mutableBigInt, mutableBigInt2, flag);
            }
         }
      }
   }

   public long bitLength() {
      return this.intLen == 0 ? 0L : (long)this.intLen * 32L - (long)Integer.numberOfLeadingZeros(this.value[this.offset]);
   }

   @Override
   public String toString() {
      return this.toBigInteger((byte)1).toString();
   }

   private final long getLong(int n, int n2, short s, char ch) {
      return (long)this.value[this.offset + n] & 4294967295L;
   }

   public MutableBigInt(ShadedBigInteger shadedBigInteger) {
      this.offset = (byte)0;
      this.intLen = shadedBigInteger.mag.length;
      this.value = Arrays.copyOf(shadedBigInteger.mag, this.intLen);
   }

   private MutableBigInt modInverse(MutableBigInt mutableBigInt) {
      MutableBigInt mutableBigInt2 = new MutableBigInt(mutableBigInt);
      MutableBigInt mutableBigInt3 = new MutableBigInt(this);
      MutableBigInt mutableBigInt4 = new MutableBigInt(mutableBigInt2);
      SignedMutableBigInt signedMutableBigInt = new SignedMutableBigInt(1);
      SignedMutableBigInt signedMutableBigInt2 = new SignedMutableBigInt();
      Object obj = null;
      Object obj2 = null;
      int n = 0;
      if (mutableBigInt3.isEven()) {
         int lowestSetBit = mutableBigInt3.getLowestSetBit();
         mutableBigInt3.rightShift(lowestSetBit);
         signedMutableBigInt2.leftShift(lowestSetBit);
         n = lowestSetBit;
      }

      while (true) {
         if (mutableBigInt3.isOne()) {
            if (signedMutableBigInt.compare(mutableBigInt2) >= 0) {
               signedMutableBigInt.copyValue(signedMutableBigInt.divide2(mutableBigInt2, new MutableBigInt()));
            }

            if (signedMutableBigInt.sign < 0) {
               signedMutableBigInt.signedAdd2(mutableBigInt2);
            }

            return fixup(signedMutableBigInt, mutableBigInt2, n);
         }

         if (mutableBigInt3.isZero()) {
            throw new ArithmeticException("BigInteger not invertible.");
         }

         if (mutableBigInt3.compare(mutableBigInt4) < 0) {
            obj = mutableBigInt3;
            mutableBigInt3 = mutableBigInt4;
            mutableBigInt4 = (MutableBigInt)obj;
            obj2 = signedMutableBigInt2;
            signedMutableBigInt2 = signedMutableBigInt;
            signedMutableBigInt = (SignedMutableBigInt)obj2;
         }

         if (((mutableBigInt3.value[mutableBigInt3.offset + mutableBigInt3.intLen - 1] ^ mutableBigInt4.value[mutableBigInt4.offset + mutableBigInt4.intLen - 1]) & 3) == 0) {
            mutableBigInt3.subtract(mutableBigInt4);
            signedMutableBigInt.signedSubtract(signedMutableBigInt2);
         } else {
            mutableBigInt3.add(mutableBigInt4);
            signedMutableBigInt.signedAdd(signedMutableBigInt2);
         }

         int lowestSetBit2 = mutableBigInt3.getLowestSetBit();
         mutableBigInt3.rightShift(lowestSetBit2);
         signedMutableBigInt2.leftShift(lowestSetBit2);
         n += lowestSetBit2;
      }
   }

   public void copyValue(int[] ints, long l) {
      int length = ints.length;
      if (this.value.length < length) {
         this.value = new int[length];
      }

      System.arraycopy(ints, 0, this.value, 0, length);
      this.intLen = length;
      this.offset = (byte)0;
   }

   public MutableBigInt divideAndRemainderBurnikelZiegler(MutableBigInt mutableBigInt, MutableBigInt mutableBigInt2) {
      int intLen2 = this.intLen;
      int intLen3 = mutableBigInt.intLen;
      byte b;
      byte b2 = b = 0;
      mutableBigInt2.intLen = b;
      mutableBigInt2.offset = b2;
      if (intLen2 < intLen3) {
         return this;
      } else {
         int n = 1 << 32 - Integer.numberOfLeadingZeros(intLen3 / 80);
         int n3 = ((intLen3 + n - 1) / n) * n;
         long l = 32L * (long)n3;
         int n4 = (int)Math.max(0L, l - mutableBigInt.bitLength());
         MutableBigInt mutableBigInt3 = new MutableBigInt(mutableBigInt);
         mutableBigInt3.safeLeftShift(n4);
         MutableBigInt mutableBigInt4 = new MutableBigInt(this);
         mutableBigInt4.safeLeftShift(n4);
         int n5 = (int)((mutableBigInt4.bitLength() + l) / l);
         if (n5 < 2) {
            n5 = 2;
         }

         MutableBigInt block = mutableBigInt4.getBlock(n5 - 1, n5, n3);
         MutableBigInt block2 = mutableBigInt4.getBlock(n5 - 2, n5, n3);
         block2.addDisjoint(block, n3);
         MutableBigInt mutableBigInt5 = new MutableBigInt();

         for (int i = n5 - 2; i > 0; i--) {
            MutableBigInt mutableBigInt6 = block2.divide2n1n(mutableBigInt3, mutableBigInt5);
            block2 = mutableBigInt4.getBlock(i - 1, n5, n3);
            block2.addDisjoint(mutableBigInt6, n3);
            mutableBigInt2.addShifted(mutableBigInt5, i * n3);
         }

         MutableBigInt mutableBigInt7 = block2.divide2n1n(mutableBigInt3, mutableBigInt5);
         mutableBigInt2.add(mutableBigInt5);
         mutableBigInt7.rightShift(n4);
         return mutableBigInt7;
      }
   }

   public void addShifted(MutableBigInt mutableBigInt, int n) {
      if (!mutableBigInt.isZero()) {
         int intLen2 = this.intLen;
         int n2 = mutableBigInt.intLen + n;
         int n3 = this.intLen > n2 ? this.intLen : n2;
         int[] ints;
         if (this.value.length < n3) {
            ints = new int[n3];
         } else {
            ints = this.value;
         }

         int[] ints2 = ints;
         int n4 = ints2.length - 1;
         long l = 0L;

         while (intLen2 > 0 && n2 > 0) {
            intLen2--;
            n2--;
            int n5 = n2 + mutableBigInt.offset < mutableBigInt.value.length ? mutableBigInt.value[n2 + mutableBigInt.offset] : 0;
            long l2 = ((long)this.value[intLen2 + this.offset] & 4294967295L) + ((long)n5 & 4294967295L) + l;
            ints2[n4--] = (int)l2;
            l = l2 >>> 32;
         }

         while (intLen2 > 0) {
            intLen2--;
            if (l == 0L && ints2 == this.value && n4 == intLen2 + this.offset) {
               return;
            }

            long l3 = ((long)this.value[intLen2 + this.offset] & 4294967295L) + l;
            ints2[n4--] = (int)l3;
            l = l3 >>> 32;
         }

         while (n2 > 0) {
            n2--;
            long l4 = ((long)(n2 + mutableBigInt.offset < mutableBigInt.value.length ? mutableBigInt.value[n2 + mutableBigInt.offset] : 0) & 4294967295L) + l;
            ints2[n4--] = (int)l4;
            l = l4 >>> 32;
         }

         if (l > 0L) {
            if (ints2.length < ++n3) {
               int[] ints3 = new int[n3];
               System.arraycopy(ints2, 0, ints3, 1, ints2.length);
               ints3[0] = 1;
               ints2 = ints3;
            } else {
               ints2[n4--] = 1;
            }
         }

         this.value = ints2;
         this.intLen = n3;
         this.offset = ints2.length - n3;
      }
   }

   public int divideOneWord(int n, MutableBigInt mutableBigInt) {
      long l = (long)n & 4294967295L;
      if (this.intLen == 1) {
         long l2 = (long)this.value[this.offset] & 4294967295L;
         int n2 = (int)(l2 / l);
         int n3 = (int)(l2 - (long)n2 * l);
         mutableBigInt.value[0] = n2;
         mutableBigInt.intLen = n2 == 0 ? 0 : 1;
         mutableBigInt.offset = (byte)0;
         return n3;
      } else {
         if (mutableBigInt.value.length < this.intLen) {
            mutableBigInt.value = new int[this.intLen];
         }

         mutableBigInt.offset = (byte)0;
         mutableBigInt.intLen = this.intLen;
         int n5 = Integer.numberOfLeadingZeros(n);
         int n6 = this.value[this.offset];
         long l3 = (long)n6 & 4294967295L;
         if (l3 < l) {
            mutableBigInt.value[0] = 0;
         } else {
            mutableBigInt.value[0] = (int)(l3 / l);
            n6 = (int)(l3 - (long)mutableBigInt.value[0] * l);
            l3 = (long)n6 & 4294967295L;
         }

         for (int i = this.intLen; --i > 0; l3 = (long)n6 & 4294967295L) {
            long l4 = l3 << 32 | (long)this.value[this.offset + this.intLen - i] & 4294967295L;
            int n7;
            if (l4 >= 0L) {
               n7 = (int)(l4 / l);
               n6 = (int)(l4 - (long)n7 * l);
            } else {
               long l5 = divWord(l4, n);
               n7 = (int)(l5 & 4294967295L);
               n6 = (int)(l5 >>> 32);
            }

            mutableBigInt.value[this.intLen - i] = n7;
         }

         mutableBigInt.normalize();
         return n5 > 0 ? n6 % n : n6;
      }
   }

   public MutableBigInt(MutableBigInt mutableBigInt) {
      this.offset = (byte)0;
      this.intLen = mutableBigInt.intLen;
      this.value = Arrays.copyOfRange(mutableBigInt.value, mutableBigInt.offset, mutableBigInt.offset + this.intLen);
   }

   private MutableBigInt divideMagnitude(MutableBigInt mutableBigInt, MutableBigInt mutableBigInt2, boolean flag) {
      int n = Integer.numberOfLeadingZeros(mutableBigInt.value[mutableBigInt.offset]);
      int intLen2 = mutableBigInt.intLen;
      int[] ints;
      MutableBigInt mutableBigInt3;
      if (n > 0) {
         ints = new int[intLen2];
         int offset2 = mutableBigInt.offset;
         copyAndShift(mutableBigInt.value, offset2, intLen2, ints, (byte)0, n);
         if (Integer.numberOfLeadingZeros(this.value[this.offset]) >= n) {
            int[] ints2 = new int[this.intLen + 1];
            mutableBigInt3 = new MutableBigInt(ints2);
            mutableBigInt3.intLen = this.intLen;
            mutableBigInt3.offset = (byte)1;
            int intLen4 = this.intLen;
            int offset3 = this.offset;
            copyAndShift(this.value, offset3, intLen4, ints2, (byte)1, n);
         } else {
            int[] ints3 = new int[this.intLen + 2];
            mutableBigInt3 = new MutableBigInt(ints3);
            mutableBigInt3.intLen = this.intLen + 1;
            mutableBigInt3.offset = (byte)1;
            int offset4 = this.offset;
            int n3 = 0;
            int n4 = 32 - n;

            for (int i = 1; i < this.intLen + 1; offset4++) {
               int n5 = n3;
               n3 = this.value[offset4];
               ints3[i] = n5 << n | n3 >>> n4;
               i++;
            }

            ints3[this.intLen + 1] = n3 << n;
         }
      } else {
         ints = Arrays.copyOfRange(mutableBigInt.value, mutableBigInt.offset, mutableBigInt.offset + mutableBigInt.intLen);
         mutableBigInt3 = new MutableBigInt(new int[this.intLen + 1]);
         System.arraycopy(this.value, this.offset, mutableBigInt3.value, 1, this.intLen);
         mutableBigInt3.intLen = this.intLen;
         mutableBigInt3.offset = (byte)1;
      }

      int intLen6 = mutableBigInt3.intLen;
      int n6 = intLen6 - intLen2 + 1;
      if (mutableBigInt2.value.length < n6) {
         mutableBigInt2.value = new int[n6];
         mutableBigInt2.offset = (byte)0;
      }

      mutableBigInt2.intLen = n6;
      int[] value2 = mutableBigInt2.value;
      if (mutableBigInt3.intLen == intLen6) {
         mutableBigInt3.offset = (byte)0;
         mutableBigInt3.value[0] = 0;
         mutableBigInt3.intLen = mutableBigInt3.intLen + 1;
      }

      int n8 = ints[0];
      long l = (long)n8 & 4294967295L;
      int n9 = ints[1];

      for (int j = 0; j < n6 - 1; j++) {
         int n10 = 0;
         int n11 = 0;
         boolean flag2 = false;
         int n12 = mutableBigInt3.value[j + mutableBigInt3.offset];
         int n13 = n12 + -2147483648;
         int n14 = mutableBigInt3.value[j + 1 + mutableBigInt3.offset];
         if (n12 == n8) {
            n10 = -1;
            n11 = n12 + n14;
            flag2 = n11 + -2147483648 < n13;
         } else {
            long l2 = (long)n12 << 32 | (long)n14 & 4294967295L;
            if (l2 >= 0L) {
               n10 = (int)(l2 / l);
               n11 = (int)(l2 - (long)n10 * l);
            } else {
               long l3 = divWord(l2, n8);
               n10 = (int)(l3 & 4294967295L);
               n11 = (int)(l3 >>> 32);
            }
         }

         if (n10 != 0) {
            if (!flag2) {
               long l4 = (long)mutableBigInt3.value[j + 2 + mutableBigInt3.offset] & 4294967295L;
               long l5 = ((long)n11 & 4294967295L) << 32 | l4;
               long l6 = ((long)n9 & 4294967295L) * ((long)n10 & 4294967295L);
               if (this.unsignedLongCompare(l6, l5)) {
                  n10--;
                  n11 = (int)(((long)n11 & 4294967295L) + l);
                  if (((long)n11 & 4294967295L) >= l) {
                     l6 -= (long)n9 & 4294967295L;
                     l5 = ((long)n11 & 4294967295L) << 32 | l4;
                     if (this.unsignedLongCompare(l6, l5)) {
                        n10--;
                     }
                  }
               }
            }

            mutableBigInt3.value[j + mutableBigInt3.offset] = 0;
            int n15 = j + mutableBigInt3.offset;
            if (this.mulsub(mutableBigInt3.value, ints, n10, intLen2, n15) + -2147483648 > n13) {
               int n17 = j + 1 + mutableBigInt3.offset;
               this.divadd(ints, mutableBigInt3.value, n17);
               n10--;
            }

            value2[j] = n10;
         }
      }

      int n18 = 0;
      int n19 = 0;
      boolean flag3 = false;
      int n20 = mutableBigInt3.value[n6 - 1 + mutableBigInt3.offset];
      int n21 = n20 + -2147483648;
      int n22 = mutableBigInt3.value[n6 + mutableBigInt3.offset];
      if (n20 == n8) {
         n18 = -1;
         n19 = n20 + n22;
         flag3 = n19 + -2147483648 < n21;
      } else {
         long l7 = (long)n20 << 32 | (long)n22 & 4294967295L;
         if (l7 >= 0L) {
            n18 = (int)(l7 / l);
            n19 = (int)(l7 - (long)n18 * l);
         } else {
            long l8 = divWord(l7, n8);
            n18 = (int)(l8 & 4294967295L);
            n19 = (int)(l8 >>> 32);
         }
      }

      if (n18 != 0) {
         if (!flag3) {
            long l9 = (long)mutableBigInt3.value[n6 + 1 + mutableBigInt3.offset] & 4294967295L;
            long l10 = ((long)n19 & 4294967295L) << 32 | l9;
            long l11 = ((long)n9 & 4294967295L) * ((long)n18 & 4294967295L);
            if (this.unsignedLongCompare(l11, l10)) {
               n18--;
               n19 = (int)(((long)n19 & 4294967295L) + l);
               if (((long)n19 & 4294967295L) >= l) {
                  l11 -= (long)n9 & 4294967295L;
                  l10 = ((long)n19 & 4294967295L) << 32 | l9;
                  if (this.unsignedLongCompare(l11, l10)) {
                     n18--;
                  }
               }
            }
         }

         mutableBigInt3.value[n6 - 1 + mutableBigInt3.offset] = 0;
         int n23;
         if (flag) {
            int n24 = n6 - 1 + mutableBigInt3.offset;
            n23 = this.mulsub(mutableBigInt3.value, ints, n18, intLen2, n24);
         } else {
            int n25 = n6 - 1 + mutableBigInt3.offset;
            n23 = this.mulsubBorrow(mutableBigInt3.value, ints, n18, intLen2, n25);
         }

         if (n23 + -2147483648 > n21) {
            if (flag) {
               int n26 = n6 - 1 + 1 + mutableBigInt3.offset;
               this.divadd(ints, mutableBigInt3.value, n26);
            }

            n18--;
         }

         value2[n6 - 1] = n18;
      }

      if (flag) {
         if (n > 0) {
            mutableBigInt3.rightShift(n);
         }

         mutableBigInt3.normalize();
      }

      mutableBigInt2.normalize();
      return flag ? mutableBigInt3 : null;
   }

   public boolean isEven() {
      return this.intLen == 0 || (this.value[this.offset + this.intLen - 1] & 1) == 0;
   }

   private int mulsub(int[] ints, int[] ints2, int n, int n2, int n3) {
      long l = (long)n & 4294967295L;
      long l2 = 0L;
      n3 += n2;

      for (int i = n2 - 1; i >= 0; i--) {
         long l3 = ((long)ints2[i] & 4294967295L) * l + l2;
         long l4 = (long)ints[n3] - l3;
         ints[n3--] = (int)l4;
         l2 = (l3 >>> 32) + (long)((l4 & 4294967295L) > ((long)(~((int)l3)) & 4294967295L) ? 1 : 0);
      }

      return (int)l2;
   }

   private boolean unsignedLongCompare(long l, long l2) {
      return l + -9223372036854775808L > l2 + -9223372036854775808L;
   }

   private int mulsubLong(int[] ints, int n, int n2, int n3, int n4) {
      long l = (long)n3 & 4294967295L;
      n4 += 2;
      long l2 = ((long)n2 & 4294967295L) * l;
      long l3 = (long)ints[n4] - l2;
      ints[n4--] = (int)l3;
      long l4 = (l2 >>> 32) + (long)((l3 & 4294967295L) > ((long)(~((int)l2)) & 4294967295L) ? 1 : 0);
      l2 = ((long)n & 4294967295L) * l + l4;
      l3 = (long)ints[n4] - l2;
      ints[n4--] = (int)l3;
      l4 = (l2 >>> 32) + (long)((l3 & 4294967295L) > ((long)(~((int)l2)) & 4294967295L) ? 1 : 0);
      return (int)l4;
   }

   public MutableBigInt euclidModInverse(int n) {
      MutableBigInt mutableBigInt = new MutableBigInt(1);
      mutableBigInt.leftShift(n);
      MutableBigInt mutableBigInt2 = new MutableBigInt(mutableBigInt);
      MutableBigInt mutableBigInt3 = new MutableBigInt(this);
      MutableBigInt mutableBigInt4 = new MutableBigInt();
      MutableBigInt mutableBigInt5 = mutableBigInt.divide2(mutableBigInt3, mutableBigInt4);
      mutableBigInt = mutableBigInt5;
      MutableBigInt mutableBigInt6 = new MutableBigInt(mutableBigInt4);
      MutableBigInt mutableBigInt7 = new MutableBigInt(1);
      MutableBigInt mutableBigInt8 = new MutableBigInt();

      while (true) {
         if (mutableBigInt.isOne()) {
            mutableBigInt2.subtract(mutableBigInt6);
            return mutableBigInt2;
         }

         mutableBigInt5 = mutableBigInt3.divide2(mutableBigInt, mutableBigInt4);
         if (mutableBigInt5.intLen == 0) {
            throw new ArithmeticException("BigInteger not invertible.");
         }

         mutableBigInt3 = mutableBigInt5;
         if (mutableBigInt4.intLen == 1) {
            mutableBigInt6.mul(mutableBigInt4.value[mutableBigInt4.offset], mutableBigInt8);
         } else {
            mutableBigInt4.multiply(mutableBigInt6, mutableBigInt8);
         }

         mutableBigInt7.add(mutableBigInt8);
         if (mutableBigInt5.isOne()) {
            return mutableBigInt7;
         }

         mutableBigInt5 = mutableBigInt.divide2(mutableBigInt5, mutableBigInt8);
         if (mutableBigInt5.intLen == 0) {
            throw new ArithmeticException("BigInteger not invertible.");
         }

         mutableBigInt = mutableBigInt5;
         if (mutableBigInt8.intLen == 1) {
            mutableBigInt7.mul(mutableBigInt8.value[mutableBigInt8.offset], mutableBigInt4);
         } else {
            mutableBigInt8.multiply(mutableBigInt7, mutableBigInt4);
         }

         mutableBigInt4 = mutableBigInt4;
         mutableBigInt8 = mutableBigInt8;
         mutableBigInt6.add(mutableBigInt4);
      }
   }

   public void setInt(int n, int n2, long l) {
      this.value[this.offset + n] = n2;
   }

   private final void ensureCapacity(int n, long l) {
      if (this.value.length < n) {
         this.value = new int[n];
         this.offset = (byte)0;
         this.intLen = n;
      }
   }

   public void multiply(MutableBigInt mutableBigInt, MutableBigInt mutableBigInt2) {
      int intLen2 = this.intLen;
      int intLen3 = mutableBigInt.intLen;
      int n = intLen2 + intLen3;
      if (mutableBigInt2.value.length < n) {
         mutableBigInt2.value = new int[n];
      }

      mutableBigInt2.offset = (byte)0;
      mutableBigInt2.intLen = n;
      long l = 0L;
      int n2 = intLen3 - 1;

      for (int i = intLen3 + intLen2 - 1; n2 >= 0; i--) {
         long l2 = ((long)mutableBigInt.value[n2 + mutableBigInt.offset] & 4294967295L) * ((long)this.value[intLen2 - 1 + this.offset] & 4294967295L) + l;
         mutableBigInt2.value[i] = (int)l2;
         l = l2 >>> 32;
         n2--;
      }

      mutableBigInt2.value[intLen2 - 1] = (int)l;

      for (int j = intLen2 - 2; j >= 0; j--) {
         l = 0L;
         int n3 = intLen3 - 1;

         for (int k = intLen3 + j; n3 >= 0; k--) {
            long l3 = ((long)mutableBigInt.value[n3 + mutableBigInt.offset] & 4294967295L) * ((long)this.value[j + this.offset] & 4294967295L)
               + ((long)mutableBigInt2.value[k] & 4294967295L)
               + l;
            mutableBigInt2.value[k] = (int)l3;
            l = l3 >>> 32;
            n3--;
         }

         mutableBigInt2.value[j] = (int)l;
      }

      mutableBigInt2.normalize();
   }

   public static MutableBigInt fixup(MutableBigInt mutableBigInt, MutableBigInt mutableBigInt2, int n) {
      MutableBigInt mutableBigInt3 = new MutableBigInt();
      int n2 = -inverseMod32(mutableBigInt2.value[mutableBigInt2.offset + mutableBigInt2.intLen - 1]);
      int n3 = 0;

      for (int i = n >> 5; n3 < i; n3++) {
         mutableBigInt2.mul(n2 * mutableBigInt.value[mutableBigInt.offset + mutableBigInt.intLen - 1], mutableBigInt3);
         mutableBigInt.add(mutableBigInt3);
         mutableBigInt.intLen = mutableBigInt.intLen - 1;
      }

      n3 = n & 31;
      if (n3 != 0) {
         int n6 = n2 * mutableBigInt.value[mutableBigInt.offset + mutableBigInt.intLen - 1];
         n6 &= (1 << n3) - 1;
         mutableBigInt2.mul(n6, mutableBigInt3);
         mutableBigInt.add(mutableBigInt3);
         mutableBigInt.rightShift(n3);
      }

      if (mutableBigInt.compare(mutableBigInt2) >= 0) {
         mutableBigInt = mutableBigInt.divide2(mutableBigInt2, new MutableBigInt());
      }

      return mutableBigInt;
   }

   public ShadedBigDecimal toBigDecimal(int n, int n2) {
      if (this.intLen != 0 && n != 0) {
         int[] magnitudeArray = this.getMagnitudeArray();
         int length = magnitudeArray.length;
         int n3 = magnitudeArray[0];
         if (length <= 2 && (n3 >= 0 || length != 2)) {
            long l = length == 2 ? (long)magnitudeArray[1] & 4294967295L | ((long)n3 & 4294967295L) << 32 : (long)n3 & 4294967295L;
            return ShadedBigDecimal.valueOf(n == -1 ? -l : l, n2);
         } else {
            ShadedBigInteger shadedBigInteger = new ShadedBigInteger(magnitudeArray, n);
            return new ShadedBigDecimal(shadedBigInteger, -9223372036854775808L, n2, (byte)0);
         }
      } else {
         return ShadedBigDecimal.zeroValueOf(n2);
      }
   }

   public void safeRightShift(int n) {
      if (n / 32 >= this.intLen) {
         this.reset();
      } else {
         this.rightShift(n);
      }
   }

   private MutableBigInt divideLongMagnitude(long l, MutableBigInt mutableBigInt) {
      MutableBigInt mutableBigInt2 = new MutableBigInt(new int[this.intLen + 1]);
      System.arraycopy(this.value, this.offset, mutableBigInt2.value, 1, this.intLen);
      mutableBigInt2.intLen = this.intLen;
      mutableBigInt2.offset = (byte)1;
      int intLen3 = mutableBigInt2.intLen;
      int n = intLen3 - 2 + 1;
      if (mutableBigInt.value.length < n) {
         mutableBigInt.value = new int[n];
         mutableBigInt.offset = (byte)0;
      }

      mutableBigInt.intLen = n;
      int[] value2 = mutableBigInt.value;
      long l2 = (long)Long.numberOfLeadingZeros(l);
      if (l2 > 0) {
         l <<= l2;
         mutableBigInt2.leftShift((int)l2);
      }

      if (mutableBigInt2.intLen == intLen3) {
         mutableBigInt2.offset = (byte)0;
         mutableBigInt2.value[0] = 0;
         mutableBigInt2.intLen = mutableBigInt2.intLen + 1;
      }

      int n3 = (int)(l >>> 32);
      long l3 = (long)n3 & 4294967295L;
      int n4 = (int)(l & 4294967295L);

      for (int i = 0; i < n; i++) {
         int n5 = 0;
         int n6 = 0;
         boolean flag = false;
         int n7 = mutableBigInt2.value[i + mutableBigInt2.offset];
         int n8 = n7 + -2147483648;
         int n9 = mutableBigInt2.value[i + 1 + mutableBigInt2.offset];
         if (n7 == n3) {
            n5 = -1;
            n6 = n7 + n9;
            flag = n6 + -2147483648 < n8;
         } else {
            long l4 = (long)n7 << 32 | (long)n9 & 4294967295L;
            if (l4 >= 0L) {
               n5 = (int)(l4 / l3);
               n6 = (int)(l4 - (long)n5 * l3);
            } else {
               long l5 = divWord(l4, n3);
               n5 = (int)(l5 & 4294967295L);
               n6 = (int)(l5 >>> 32);
            }
         }

         if (n5 != 0) {
            if (!flag) {
               long l6 = (long)mutableBigInt2.value[i + 2 + mutableBigInt2.offset] & 4294967295L;
               long l7 = ((long)n6 & 4294967295L) << 32 | l6;
               long l8 = ((long)n4 & 4294967295L) * ((long)n5 & 4294967295L);
               if (this.unsignedLongCompare(l8, l7)) {
                  n5--;
                  n6 = (int)(((long)n6 & 4294967295L) + l3);
                  if (((long)n6 & 4294967295L) >= l3) {
                     l8 -= (long)n4 & 4294967295L;
                     l7 = ((long)n6 & 4294967295L) << 32 | l6;
                     if (this.unsignedLongCompare(l8, l7)) {
                        n5--;
                     }
                  }
               }
            }

            mutableBigInt2.value[i + mutableBigInt2.offset] = 0;
            int n10 = i + mutableBigInt2.offset;
            if (this.mulsubLong(mutableBigInt2.value, n3, n4, n5, n10) + -2147483648 > n8) {
               int n12 = i + 1 + mutableBigInt2.offset;
               this.divaddLong(n3, n4, mutableBigInt2.value, n12);
               n5--;
            }

            value2[i] = n5;
         }
      }

      if (l2 > 0) {
         mutableBigInt2.rightShift((int)l2);
      }

      mutableBigInt.normalize();
      mutableBigInt2.normalize();
      return mutableBigInt2;
   }

   public final int compare(MutableBigInt mutableBigInt) {
      int intLen2 = mutableBigInt.intLen;
      if (this.intLen < intLen2) {
         return -1;
      } else if (this.intLen > intLen2) {
         return 1;
      } else {
         int[] value2 = mutableBigInt.value;
         int offset2 = this.offset;

         for (int i = mutableBigInt.offset; offset2 < this.intLen + this.offset; i++) {
            int n = this.value[offset2] + -2147483648;
            int n2 = value2[i] + -2147483648;
            if (n < n2) {
               return -1;
            }

            if (n > n2) {
               return 1;
            }

            offset2++;
         }

         return 0;
      }
   }

   public long toCompactValue(int n) {
      if (this.intLen != 0 && n != 0) {
         int[] magnitudeArray = this.getMagnitudeArray();
         int length = magnitudeArray.length;
         int n2 = magnitudeArray[0];
         if (length <= 2 && (n2 >= 0 || length != 2)) {
            long l = length == 2 ? (long)magnitudeArray[1] & 4294967295L | ((long)n2 & 4294967295L) << 32 : (long)n2 & 4294967295L;
            return n == -1 ? -l : l;
         } else {
            return -9223372036854775808L;
         }
      } else {
         return 0L;
      }
   }

   private MutableBigInt divide3n2n(MutableBigInt mutableBigInt, MutableBigInt mutableBigInt2) {
      int n = mutableBigInt.intLen / 2;
      MutableBigInt mutableBigInt3 = new MutableBigInt(this);
      mutableBigInt3.safeRightShift(32 * n);
      MutableBigInt mutableBigInt4 = new MutableBigInt(mutableBigInt);
      mutableBigInt4.safeRightShift(n * 32);
      ShadedBigInteger lower = mutableBigInt.getLower(n);
      MutableBigInt mutableBigInt5;
      MutableBigInt mutableBigInt6;
      if (this.compareShifted(mutableBigInt, n) < 0) {
         mutableBigInt5 = mutableBigInt3.divide2n1n(mutableBigInt4, mutableBigInt2);
         mutableBigInt6 = new MutableBigInt(mutableBigInt2.toBigInteger().multiply(lower));
      } else {
         mutableBigInt2.ones(n);
         mutableBigInt3.add(mutableBigInt4);
         int n6 = 32 * n;
         mutableBigInt4.leftShift(n6);
         mutableBigInt3.subtract(mutableBigInt4);
         mutableBigInt5 = mutableBigInt3;
         mutableBigInt6 = new MutableBigInt(lower);
         n6 = 32 * n;
         mutableBigInt6.leftShift(n6);
         mutableBigInt6.subtract(new MutableBigInt(lower));
      }

      mutableBigInt5.leftShift(32 * n);
      mutableBigInt5.addLower(this, n);

      while (true) {
         if (mutableBigInt5.compare(mutableBigInt6) >= 0) {
            mutableBigInt5.subtract(mutableBigInt6);
            return mutableBigInt5;
         }

         mutableBigInt5.add(mutableBigInt);
         mutableBigInt2.subtract(ONE);
      }
   }

   private MutableBigInt getBlock(int n, int n2, int n3) {
      int n4 = n * n3;
      if (n4 >= this.intLen) {
         return new MutableBigInt();
      } else {
         int n5;
         if (n == n2 - 1) {
            n5 = this.intLen;
         } else {
            n5 = (n + 1) * n3;
         }

         if (n5 > this.intLen) {
            return new MutableBigInt();
         } else {
            int[] ints = Arrays.copyOfRange(this.value, this.offset + this.intLen - n5, this.offset + this.intLen - n4);
            return new MutableBigInt(ints);
         }
      }
   }

   public static int binaryGcd(int n, int n2) {
      if (n2 == 0) {
         return n;
      } else if (n == 0) {
         return n2;
      } else {
         int n3 = Integer.numberOfTrailingZeros(n);
         int n4 = Integer.numberOfTrailingZeros(n2);
         n >>>= n3;
         n2 >>>= n4;
         int n5 = n3 < n4 ? n3 : n4;

         while (n != n2) {
            if (n + -2147483648 > n2 + -2147483648) {
               n -= n2;
               n >>>= Integer.numberOfTrailingZeros(n);
            } else {
               n2 -= n;
               n2 >>>= Integer.numberOfTrailingZeros(n2);
            }
         }

         return n << n5;
      }
   }

   private final int getLowestSetBit() {
      if (this.intLen == 0) {
         return -1;
      } else {
         int n = this.intLen - 1;

         while (n > 0 && this.value[n + this.offset] == 0) {
            n--;
         }

         int n2 = this.value[n + this.offset];
         return n2 == 0 ? -1 : (this.intLen - 1 - n << 5) + Integer.numberOfTrailingZeros(n2);
      }
   }

   private int[] getMagnitudeArray() {
      return this.offset <= 0 && this.value.length == this.intLen ? this.value : Arrays.copyOfRange(this.value, this.offset, this.offset + this.intLen);
   }

   public void leftShift(int n) {
      if (this.intLen != 0) {
         int n2 = n >>> 5;
         int n3 = n & 31;
         int n4 = ShadedBigInteger.bitLengthForInt(this.value[this.offset]);
         if (n <= 32 - n4) {
            this.primitiveLeftShift(n3);
         } else {
            int n5 = this.intLen + n2 + 1;
            if (n3 <= 32 - n4) {
               n5--;
            }

            if (this.value.length < n5) {
               int[] ints = new int[n5];

               for (int i = 0; i < this.intLen; i++) {
                  ints[i] = this.value[this.offset + i];
               }

               this.setValue(ints, n5);
            } else if (this.value.length - this.offset >= n5) {
               for (int j = 0; j < n5 - this.intLen; j++) {
                  this.value[this.offset + this.intLen + j] = 0;
               }
            } else {
               for (int k = 0; k < this.intLen; k++) {
                  this.value[k] = this.value[this.offset + k];
               }

               for (int m = this.intLen; m < n5; m++) {
                  this.value[m] = 0;
               }

               this.offset = (byte)0;
            }

            this.intLen = n5;
            if (n3 != 0) {
               if (n3 <= 32 - n4) {
                  this.primitiveLeftShift(n3);
               } else {
                  this.primitiveRightShift(32 - n3);
               }
            }
         }
      }
   }

   public int subtract(MutableBigInt mutableBigInt) {
      MutableBigInt mutableBigInt2 = this;
      int[] value2 = this.value;
      int n = this.compare(mutableBigInt);
      if (n == 0) {
         this.reset();
         return 0;
      } else {
         if (n < 0) {
            mutableBigInt2 = mutableBigInt;
            mutableBigInt = this;
         }

         int intLen2 = mutableBigInt2.intLen;
         if (value2.length < intLen2) {
            value2 = new int[intLen2];
         }

         long l = 0L;
         int intLen3 = mutableBigInt2.intLen;
         int intLen4 = mutableBigInt.intLen;

         int n2;
         for (n2 = value2.length - 1; intLen4 > 0; value2[n2--] = (int)l) {
            intLen3--;
            intLen4--;
            l = ((long)mutableBigInt2.value[intLen3 + mutableBigInt2.offset] & 4294967295L) - ((long)mutableBigInt.value[intLen4 + mutableBigInt.offset] & 4294967295L) - (long)((int)(-(l >> 32)));
         }

         while (intLen3 > 0) {
            intLen3--;
            l = ((long)mutableBigInt2.value[intLen3 + mutableBigInt2.offset] & 4294967295L) - (long)((int)(-(l >> 32)));
            value2[n2--] = (int)l;
         }

         this.value = value2;
         this.intLen = intLen2;
         this.offset = this.value.length - intLen2;
         this.normalize();
         return n;
      }
   }

   private final void primitiveLeftShift(int n) {
      int[] value2 = this.value;
      int n2 = 32 - n;
      int offset2 = this.offset;
      int n3 = value2[offset2];

      for (int i = offset2 + this.intLen - 1; offset2 < i; offset2++) {
         int n4 = n3;
         n3 = value2[offset2 + 1];
         value2[offset2] = n4 << n | n3 >>> n2;
      }

      value2[this.offset + this.intLen - 1] = value2[this.offset + this.intLen - 1] << n;
   }

   private MutableBigInt binaryGCD(MutableBigInt mutableBigInt) {
      MutableBigInt mutableBigInt2 = this;
      MutableBigInt mutableBigInt3 = new MutableBigInt();
      int lowestSetBit = this.getLowestSetBit();
      int lowestSetBit2 = mutableBigInt.getLowestSetBit();
      int n = lowestSetBit < lowestSetBit2 ? lowestSetBit : lowestSetBit2;
      if (n != 0) {
         this.rightShift(n);
         mutableBigInt.rightShift(n);
      }

      boolean flag = n == lowestSetBit;
      MutableBigInt mutableBigInt4 = flag ? mutableBigInt : this;

      int n2;
      for (int i = flag ? -1 : 1; (n2 = mutableBigInt4.getLowestSetBit()) >= 0; mutableBigInt4 = i >= 0 ? mutableBigInt2 : mutableBigInt) {
         mutableBigInt4.rightShift(n2);
         if (i > 0) {
            mutableBigInt2 = mutableBigInt4;
         } else {
            mutableBigInt = mutableBigInt4;
         }

         if (mutableBigInt2.intLen < 2 && mutableBigInt.intLen < 2) {
            int n3 = mutableBigInt2.value[mutableBigInt2.offset];
            n3 = binaryGcd(n3, mutableBigInt.value[mutableBigInt.offset]);
            mutableBigInt3.value[0] = n3;
            mutableBigInt3.intLen = (byte)1;
            mutableBigInt3.offset = (byte)0;
            if (n > 0) {
               mutableBigInt3.leftShift(n);
            }

            return mutableBigInt3;
         }

         if ((i = mutableBigInt2.difference(mutableBigInt)) == 0) {
            break;
         }
      }

      if (n > 0) {
         mutableBigInt2.leftShift(n);
      }

      return mutableBigInt2;
   }

   private long toLong() {
      assert this.intLen <= 2 : "this MutableBigInteger exceeds the range of long";

      if (this.intLen == 0) {
         return 0L;
      } else {
         long l = (long)this.value[this.offset] & 4294967295L;
         return this.intLen == 2 ? l << 32 | (long)this.value[this.offset + 1] & 4294967295L : l;
      }
   }

   private int compareShifted(MutableBigInt mutableBigInt, int n) {
      int intLen2 = mutableBigInt.intLen;
      int n2 = this.intLen - n;
      if (n2 < intLen2) {
         return -1;
      } else if (n2 > intLen2) {
         return 1;
      } else {
         int[] value2 = mutableBigInt.value;
         int offset2 = this.offset;

         for (int i = mutableBigInt.offset; offset2 < n2 + this.offset; i++) {
            int n3 = this.value[offset2] + -2147483648;
            int n4 = value2[i] + -2147483648;
            if (n3 < n4) {
               return -1;
            }

            if (n3 > n4) {
               return 1;
            }

            offset2++;
         }

         return 0;
      }
   }

   public boolean isZero() {
      return this.intLen == 0;
   }

   private void keepLower(int n) {
      if (this.intLen >= n) {
         this.offset = this.offset + (this.intLen - n);
         this.intLen = n;
      }
   }

   public void reset() {
      byte b;
      byte b2 = b = 0;
      this.intLen = b;
      this.offset = b2;
   }

   private int divaddLong(int n, int n2, int[] ints, int n3) {
      long l = 0L;
      long l2 = ((long)n2 & 4294967295L) + ((long)ints[1 + n3] & 4294967295L);
      ints[1 + n3] = (int)l2;
      l2 = ((long)n & 4294967295L) + ((long)ints[n3] & 4294967295L) + l;
      ints[n3] = (int)l2;
      l = l2 >>> 32;
      return (int)l;
   }

   public void addDisjoint(MutableBigInt mutableBigInt, int n) {
      if (!mutableBigInt.isZero()) {
         int intLen2 = this.intLen;
         int n2 = mutableBigInt.intLen + n;
         int n3 = this.intLen > n2 ? this.intLen : n2;
         int[] ints;
         if (this.value.length < n3) {
            ints = new int[n3];
         } else {
            ints = this.value;
            Arrays.fill(this.value, this.offset + this.intLen, this.value.length, 0);
         }

         int n4 = ints.length - 1;
         System.arraycopy(this.value, this.offset, ints, n4 + 1 - intLen2, intLen2);
         n2 -= intLen2;
         n4 -= intLen2;
         int n5 = Math.min(n2, mutableBigInt.value.length - mutableBigInt.offset);
         System.arraycopy(mutableBigInt.value, mutableBigInt.offset, ints, n4 + 1 - n2, n5);

         for (int i = n4 + 1 - n2 + n5; i < n4 + 1; i++) {
            ints[i] = 0;
         }

         this.value = ints;
         this.intLen = n3;
         this.offset = ints.length - n3;
      }
   }

   public ShadedBigInteger toBigInteger(int n) {
      return this.intLen != 0 && n != 0 ? new ShadedBigInteger(this.getMagnitudeArray(), n) : ShadedBigInteger.ZERO;
   }

   public MutableBigInt mutableModInverse(MutableBigInt mutableBigInt) {
      if (mutableBigInt.isOdd()) {
         return this.modInverse(mutableBigInt);
      } else if (this.isEven()) {
         throw new ArithmeticException("BigInteger not invertible.");
      } else {
         int lowestSetBit = mutableBigInt.getLowestSetBit();
         MutableBigInt mutableBigInt2 = new MutableBigInt(mutableBigInt);
         mutableBigInt2.rightShift(lowestSetBit);
         if (mutableBigInt2.isOne()) {
            return this.modInverseMP2(lowestSetBit);
         } else {
            MutableBigInt mutableBigInt3 = this.modInverse(mutableBigInt2);
            MutableBigInt mutableBigInt4 = this.modInverseMP2(lowestSetBit);
            MutableBigInt mutableBigInt5 = modInverseBP2(mutableBigInt2, lowestSetBit);
            MutableBigInt mutableBigInt6 = mutableBigInt2.modInverseMP2(lowestSetBit);
            MutableBigInt mutableBigInt7 = new MutableBigInt();
            MutableBigInt mutableBigInt8 = new MutableBigInt();
            MutableBigInt mutableBigInt9 = new MutableBigInt();
            mutableBigInt3.leftShift(lowestSetBit);
            mutableBigInt3.multiply(mutableBigInt5, mutableBigInt9);
            mutableBigInt4.multiply(mutableBigInt2, mutableBigInt7);
            mutableBigInt7.multiply(mutableBigInt6, mutableBigInt8);
            mutableBigInt9.add(mutableBigInt8);
            return mutableBigInt9.divide2(mutableBigInt, mutableBigInt7);
         }
      }
   }

   public MutableBigInt divideKnuth(MutableBigInt mutableBigInt, MutableBigInt mutableBigInt2) {
      return this.divideKnuth(mutableBigInt, mutableBigInt2, true);
   }

   private static void copyAndShift(int[] ints, int n, int n2, int[] ints2, int n3, int n4) {
      int n5 = 32 - n4;
      int n6 = ints[n];

      for (int i = 0; i < n2 - 1; i++) {
         int n7 = n6;
         n6 = ints[++n];
         ints2[n3 + i] = n7 << n4 | n6 >>> n5;
      }

      ints2[n3 + n2 - 1] = n6 << n4;
   }

   public MutableBigInt() {
      this.offset = (byte)0;
      this.value = new int[1];
      this.intLen = (byte)0;
   }

   public void rightShift(int n) {
      if (this.intLen != 0) {
         int n2 = n >>> 5;
         int n3 = n & 31;
         this.intLen = this.intLen - n2;
         if (n3 != 0) {
            if (n3 >= ShadedBigInteger.bitLengthForInt(this.value[this.offset])) {
               this.primitiveLeftShift(32 - n3);
               this.intLen = this.intLen - 1;
            } else {
               this.primitiveRightShift(n3);
            }
         }
      }
   }

   private MutableBigInt divide2n1n(MutableBigInt mutableBigInt, MutableBigInt mutableBigInt2) {
      int intLen2 = mutableBigInt.intLen;
      if (intLen2 % 2 == 0 && intLen2 >= 80) {
         MutableBigInt mutableBigInt3 = new MutableBigInt(this);
         mutableBigInt3.safeRightShift(32 * (intLen2 / 2));
         this.keepLower(intLen2 / 2);
         MutableBigInt mutableBigInt4 = new MutableBigInt();
         this.addDisjoint(mutableBigInt3.divide3n2n(mutableBigInt, mutableBigInt4), intLen2 / 2);
         MutableBigInt mutableBigInt6 = this.divide3n2n(mutableBigInt, mutableBigInt2);
         mutableBigInt2.addDisjoint(mutableBigInt4, intLen2 / 2);
         return mutableBigInt6;
      } else {
         return this.divideKnuth(mutableBigInt, mutableBigInt2);
      }
   }

   public static long divWord(long l, int n) {
      long l2 = (long)n & 4294967295L;
      if (l2 == 1L) {
         return 0L << 32 | ((long)((int)l)) & 4294967295L;
      } else {
         long l5 = (l >>> 1) / (l2 >>> 1);

         long l6;
         for (l6 = l - l5 * l2; l6 < 0L; l5--) {
            l6 += l2;
         }

         while (l6 >= l2) {
            l6 -= l2;
            l5++;
         }

         return l6 << 32 | l5 & 4294967295L;
      }
   }

   public void copyValue(MutableBigInt mutableBigInt) {
      int intLen2 = mutableBigInt.intLen;
      if (this.value.length < intLen2) {
         this.value = new int[intLen2];
      }

      System.arraycopy(mutableBigInt.value, mutableBigInt.offset, this.value, 0, intLen2);
      this.intLen = intLen2;
      this.offset = (byte)0;
   }

   public final int compareHalf(MutableBigInt mutableBigInt) {
      int intLen2 = mutableBigInt.intLen;
      int intLen3 = this.intLen;
      if (intLen3 <= 0) {
         return intLen2 <= 0 ? 0 : -1;
      } else if (intLen3 > intLen2) {
         return 1;
      } else if (intLen3 < intLen2 - 1) {
         return -1;
      } else {
         int[] value2 = mutableBigInt.value;
         int n = 0;
         int n2 = 0;
         if (intLen3 != intLen2) {
            if (value2[n] != 1) {
               return -1;
            }

            n++;
            n2 = -2147483648;
         }

         int[] value3 = this.value;
         int offset2 = this.offset;
         int n3 = n;

         while (offset2 < intLen3 + this.offset) {
            int n4 = value2[n3++];
            long l = (long)((n4 >>> 1) + n2) & 4294967295L;
            long l2 = (long)value3[offset2++] & 4294967295L;
            if (l2 != l) {
               return l2 < l ? -1 : 1;
            }

            n2 = (n4 & 1) << 31;
         }

         return n2 == 0 ? 0 : -1;
      }
   }

   public boolean isOne() {
      return this.intLen == 1 && this.value[this.offset] == 1;
   }

   private int divadd(int[] ints, int[] ints2, int n) {
      long l = 0L;

      for (int i = ints.length - 1; i >= 0; i--) {
         long l2 = ((long)ints[i] & 4294967295L) + ((long)ints2[i + n] & 4294967295L) + l;
         ints2[i + n] = (int)l2;
         l = l2 >>> 32;
      }

      return (int)l;
   }

   public ShadedBigInteger toBigInteger() {
      this.normalize();
      return this.toBigInteger(this.isZero() ? 0 : 1);
   }

   public void add(MutableBigInt mutableBigInt) {
      int intLen2 = this.intLen;
      int intLen3 = mutableBigInt.intLen;
      int n = this.intLen > mutableBigInt.intLen ? this.intLen : mutableBigInt.intLen;
      int[] ints;
      if (this.value.length < n) {
         ints = new int[n];
      } else {
         ints = this.value;
      }

      int[] ints2 = ints;
      int n2 = ints2.length - 1;
      long l = 0L;

      while (intLen2 > 0 && intLen3 > 0) {
         long l2 = ((long)this.value[--intLen2 + this.offset] & 4294967295L) + ((long)mutableBigInt.value[--intLen3 + mutableBigInt.offset] & 4294967295L) + l;
         ints2[n2--] = (int)l2;
         l = l2 >>> 32;
      }

      while (intLen2 > 0) {
         intLen2--;
         if (l == 0L && ints2 == this.value && n2 == intLen2 + this.offset) {
            return;
         }

         long l3 = ((long)this.value[intLen2 + this.offset] & 4294967295L) + l;
         ints2[n2--] = (int)l3;
         l = l3 >>> 32;
      }

      while (intLen3 > 0) {
         long l4 = ((long)mutableBigInt.value[--intLen3 + mutableBigInt.offset] & 4294967295L) + l;
         ints2[n2--] = (int)l4;
         l = l4 >>> 32;
      }

      if (l > 0L) {
         if (ints2.length < ++n) {
            int[] ints3 = new int[n];
            System.arraycopy(ints2, 0, ints3, 1, ints2.length);
            ints3[0] = 1;
            ints2 = ints3;
         } else {
            ints2[n2--] = 1;
         }
      }

      this.value = ints2;
      this.intLen = n;
      this.offset = ints2.length - n;
   }

   public long divide(long l, MutableBigInt mutableBigInt) {
      if (l == 0L) {
         throw new ArithmeticException("BigInteger divide by zero");
      } else if (this.intLen == 0) {
         byte b;
         byte b2 = b = 0;
         mutableBigInt.offset = b;
         mutableBigInt.intLen = b2;
         return 0L;
      } else {
         if (l < 0L) {
            l = -l;
         }

         int n = (int)(l >>> 32);
         mutableBigInt.clear();
         if (n == 0) {
            return (long)this.divideOneWord((int)l, mutableBigInt) & 4294967295L;
         } else {
            return this.divideLongMagnitude(l, mutableBigInt).toLong();
         }
      }
   }

   static {
      ONE = new MutableBigInt(1);
   }

   public MutableBigInt modInverseMP2(int n) {
      if (this.isEven()) {
         throw new ArithmeticException("Non-invertible. (GCD != 1)");
      } else if (n > 64) {
         return this.euclidModInverse(n);
      } else {
         int n2 = inverseMod32(this.value[this.offset + this.intLen - 1]);
         if (n < 33) {
            n2 = n == 32 ? n2 : n2 & (1 << n) - 1;
            return new MutableBigInt(n2);
         } else {
            long l = (long)this.value[this.offset + this.intLen - 1] & 4294967295L;
            if (this.intLen > 1) {
               l |= (long)this.value[this.offset + this.intLen - 2] << 32;
            }

            long l2 = (long)n2 & 4294967295L;
            l2 *= 2L - l * l2;
            l2 = n == 64 ? l2 : l2 & (1L << n) - 1L;
            MutableBigInt mutableBigInt = new MutableBigInt(new int[2]);
            mutableBigInt.value[0] = (int)(l2 >>> 32);
            mutableBigInt.value[1] = (int)l2;
            mutableBigInt.intLen = (byte)2;
            mutableBigInt.normalize();
            return mutableBigInt;
         }
      }
   }

   public MutableBigInt divide2(MutableBigInt mutableBigInt, MutableBigInt mutableBigInt2) {
      return this.divide(mutableBigInt, mutableBigInt2, true);
   }

   private int mulsubBorrow(int[] ints, int[] ints2, int n, int n2, int n3) {
      long l = (long)n & 4294967295L;
      long l2 = 0L;
      n3 += n2;

      for (int i = n2 - 1; i >= 0; i--) {
         long l3 = ((long)ints2[i] & 4294967295L) * l + l2;
         l2 = (l3 >>> 32) + (long)((((long)ints[n3--] - l3) & 4294967295L) > ((long)(~((int)l3)) & 4294967295L) ? 1 : 0);
      }

      return (int)l2;
   }

   public void setValue(int[] ints, int n) {
      this.value = ints;
      this.intLen = n;
      this.offset = (byte)0;
   }

}
