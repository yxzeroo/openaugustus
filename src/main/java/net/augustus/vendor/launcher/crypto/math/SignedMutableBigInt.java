package net.augustus.vendor.launcher.crypto.math;

public class SignedMutableBigInt extends MutableBigInt {
   public int sign;

   public SignedMutableBigInt(MutableBigInt mutableBigInt) {
       super(mutableBigInt);
      byte b = 1;
      this.sign = b;
   }

   public void signedAdd(SignedMutableBigInt signedMutableBigInt) {
      int[] flowState = ShadedNumber.getFlowState();
      if (this.sign == signedMutableBigInt.sign) {
         this.add(signedMutableBigInt);
      } else {
         int n = this.sign * this.subtract(signedMutableBigInt);
         this.sign = n;
      }
   }

   @Override
   public String toString() {
      int sign2 = this.sign;
      return this.toBigInteger(sign2).toString();
   }

   public SignedMutableBigInt() {
       super();
      byte b = 1;
      this.sign = b;
   }

   public SignedMutableBigInt(int n) {
       super(n);
      byte b = 1;
      this.sign = b;
   }

   public void signedSubtract(SignedMutableBigInt signedMutableBigInt) {
      int[] flowState = ShadedNumber.getFlowState();
      if (this.sign == signedMutableBigInt.sign) {
         int n = this.sign * this.subtract(signedMutableBigInt);
         this.sign = n;
      } else {
         this.add(signedMutableBigInt);
      }
   }

   public void signedSubtract2(MutableBigInt mutableBigInt) {
      int[] flowState = ShadedNumber.getFlowState();
      if (this.sign == 1) {
         int n = this.sign * this.subtract(mutableBigInt);
         this.sign = n;
      } else {
         this.add(mutableBigInt);
      }

      if (this.intLen == 0) {
         byte b = 1;
         this.sign = b;
      }
   }

   public void signedAdd2(MutableBigInt mutableBigInt) {
      int[] flowState = ShadedNumber.getFlowState();
      if (this.sign == 1) {
         this.add(mutableBigInt);
      } else {
         int n = this.sign * this.subtract(mutableBigInt);
         this.sign = n;
      }
   }
}
