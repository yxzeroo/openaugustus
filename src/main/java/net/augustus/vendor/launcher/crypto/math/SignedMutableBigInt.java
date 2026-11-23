package net.augustus.vendor.launcher.crypto.math;

public class SignedMutableBigInt extends MutableBigInt {
   public int sign;

   public SignedMutableBigInt(MutableBigInt mutableBigInt) {
       super(mutableBigInt);
      this.sign = (byte)1;
   }

   public void signedAdd(SignedMutableBigInt signedMutableBigInt) {
      if (this.sign == signedMutableBigInt.sign) {
         this.add(signedMutableBigInt);
      } else {
         this.sign = this.sign * this.subtract(signedMutableBigInt);
      }
   }

   @Override
   public String toString() {
      return this.toBigInteger(this.sign).toString();
   }

   public SignedMutableBigInt() {
       super();
      this.sign = (byte)1;
   }

   public SignedMutableBigInt(int n) {
       super(n);
      this.sign = (byte)1;
   }

   public void signedSubtract(SignedMutableBigInt signedMutableBigInt) {
      if (this.sign == signedMutableBigInt.sign) {
         this.sign = this.sign * this.subtract(signedMutableBigInt);
      } else {
         this.add(signedMutableBigInt);
      }
   }

   public void signedSubtract2(MutableBigInt mutableBigInt) {
      if (this.sign == 1) {
         this.sign = this.sign * this.subtract(mutableBigInt);
      } else {
         this.add(mutableBigInt);
      }

      if (this.intLen == 0) {
         this.sign = (byte)1;
      }
   }

   public void signedAdd2(MutableBigInt mutableBigInt) {
      if (this.sign == 1) {
         this.add(mutableBigInt);
      } else {
         this.sign = this.sign * this.subtract(mutableBigInt);
      }
   }
}
