package net.augustus.vendor.crypto.math;

public final class ShadedBigDecimalThreadLocalBuffer extends ThreadLocal {

   @Override
   public Object initialValue() {
      return this.createHelper();
   }

   public ShadedBigDecimalStringHelper createHelper() {
      return new ShadedBigDecimalStringHelper();
   }
}
