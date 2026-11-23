package net.augustus.vendor.launcher.crypto.math;

import java.io.Serializable;

public abstract class ShadedNumber implements Serializable {
   private static final long serialVersionUID;

   public byte byteValue() {
      return (byte)this.intValue();
   }

   public abstract int intValue();

   public short shortValue() {
      return (short)this.intValue();
   }

   public abstract float floatValue();

   public abstract long longValue();

   public abstract double doubleValue();



   static {
      serialVersionUID = -8742448824652078965L;
   }
}
