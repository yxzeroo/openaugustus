package net.augustus.vendor.launcher.crypto.math;

public final class BigDecimalStringBuilderThreadLocal extends ThreadLocal {

   public BigDecimalStringBuilderHelper createHelper() {
      return new BigDecimalStringBuilderHelper();
   }

   @Override
   public Object initialValue() {
      return this.createHelper();
   }
}
