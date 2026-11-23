package net.augustus.vendor.crypto.math;



import net.augustus.vendor.crypto.CustomBigInteger;
public class ShadedSignedMutableBigInteger extends ShadedMutableBigInteger {
   public int sign = 1;

   public void signedAdd(ShadedSignedMutableBigInteger integer) {
      if (this.sign != integer.sign) {
         this.sign = this.sign * this.subtract(integer);
      } else {
         this.add(integer);
      }
   }

   @Override
   public String toString() {
      return this.toBigInteger(this.sign).toString();
   }

   public void signedSubtract(ShadedSignedMutableBigInteger integer) {
      if (this.sign != integer.sign) {
         this.add(integer);
      } else {
         this.sign = this.sign * this.subtract(integer);
      }
   }

   public ShadedSignedMutableBigInteger(int n) {
      super(n);
   }

   public void signedAdd2(ShadedMutableBigInteger shadedMutableBigInteger) {
      if (this.sign != 1) {
         this.sign = this.sign * this.subtract(shadedMutableBigInteger);
      } else {
         this.add(shadedMutableBigInteger);
      }
   }

   public ShadedSignedMutableBigInteger(ShadedMutableBigInteger shadedMutableBigInteger) {
      super(shadedMutableBigInteger);
   }

   public void signedSubtract2(ShadedMutableBigInteger shadedMutableBigInteger) {
      if (this.sign != 1) {
         this.add(shadedMutableBigInteger);
      } else {
         this.sign = this.sign * this.subtract(shadedMutableBigInteger);
      }

      if (this.intLen == 0) {
         this.sign = 1;
      }
   }

   public ShadedSignedMutableBigInteger() {
   }

}
