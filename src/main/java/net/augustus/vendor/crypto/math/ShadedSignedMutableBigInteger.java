package net.augustus.vendor.crypto.math;



import net.augustus.vendor.crypto.CustomBigInteger;
public class ShadedSignedMutableBigInteger extends ShadedMutableBigInteger {
   public int sign = 1;

   public void signedAdd(ShadedSignedMutableBigInteger integer) {
      int[] opaqueStateTag = CustomBigInteger.getOpaqueStateTag();
      if (this.sign != integer.sign) {
         ShadedSignedMutableBigInteger integer2 = this;
         ShadedSignedMutableBigInteger integer3 = this;
         ShadedSignedMutableBigInteger integer4 = integer;
         integer2.sign = this.sign * integer3.subtract(integer4);
      } else {
         ShadedSignedMutableBigInteger integer5 = this;
         ShadedSignedMutableBigInteger integer6 = integer;
         ShadedSignedMutableBigInteger integer7 = integer6;
         integer5.add(integer7);
      }
   }

   @Override
   public String toString() {
      int sign2 = this.sign;
      return this.toBigInteger(sign2).toString();
   }

   public void signedSubtract(ShadedSignedMutableBigInteger integer) {
      int[] opaqueStateTag = CustomBigInteger.getOpaqueStateTag();
      if (this.sign != integer.sign) {
         ShadedSignedMutableBigInteger integer2 = this;
         ShadedSignedMutableBigInteger integer3 = integer;
         ShadedSignedMutableBigInteger integer4 = integer3;
         integer2.add(integer4);
      } else {
         ShadedSignedMutableBigInteger integer5 = this;
         ShadedSignedMutableBigInteger integer6 = this;
         ShadedSignedMutableBigInteger integer7 = integer;
         integer5.sign = this.sign * integer6.subtract(integer7);
      }
   }

   public ShadedSignedMutableBigInteger(int n) {
      super(n);
   }

   public void signedAdd2(ShadedMutableBigInteger shadedMutableBigInteger) {
      int[] opaqueStateTag = CustomBigInteger.getOpaqueStateTag();
      if (this.sign != 1) {
         ShadedSignedMutableBigInteger integer = this;
         ShadedSignedMutableBigInteger integer2 = this;
         ShadedMutableBigInteger shadedMutableBigInteger2 = shadedMutableBigInteger;
         integer.sign = this.sign * integer2.subtract(shadedMutableBigInteger2);
      } else {
         ShadedSignedMutableBigInteger integer3 = this;
         ShadedMutableBigInteger shadedMutableBigInteger3 = shadedMutableBigInteger;
         ShadedMutableBigInteger shadedMutableBigInteger4 = shadedMutableBigInteger3;
         integer3.add(shadedMutableBigInteger4);
      }
   }

   public ShadedSignedMutableBigInteger(ShadedMutableBigInteger shadedMutableBigInteger) {
      super(shadedMutableBigInteger);
   }

   public void signedSubtract2(ShadedMutableBigInteger shadedMutableBigInteger) {
      int[] opaqueStateTag = CustomBigInteger.getOpaqueStateTag();
      if (this.sign != 1) {
         ShadedSignedMutableBigInteger integer = this;
         ShadedMutableBigInteger shadedMutableBigInteger2 = shadedMutableBigInteger;
         ShadedMutableBigInteger shadedMutableBigInteger3 = shadedMutableBigInteger2;
         integer.add(shadedMutableBigInteger3);
      } else {
         ShadedSignedMutableBigInteger integer2 = this;
         ShadedSignedMutableBigInteger integer3 = this;
         ShadedMutableBigInteger shadedMutableBigInteger4 = shadedMutableBigInteger;
         integer2.sign = this.sign * integer3.subtract(shadedMutableBigInteger4);
      }

      if (this.intLen == 0) {
         this.sign = 1;
      }
   }

   public ShadedSignedMutableBigInteger() {
   }

}
