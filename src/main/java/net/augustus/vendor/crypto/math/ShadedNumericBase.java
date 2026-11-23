package net.augustus.vendor.crypto.math;

import java.io.Serializable;

public abstract class ShadedNumericBase implements Serializable {
   private static final long serialVersionUID;

   public abstract long longValue();

   public short shortValue(long l) {
      return (short)this.intValue();
   }

   public abstract int intValue();

   public abstract double doubleValue();

   public abstract float floatValue();

   public byte byteValue(long l) {
      return (byte)this.intValue();
   }

   static {
      serialVersionUID = -8742448824652078965L;
   }
}
