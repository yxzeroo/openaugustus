package net.augustus.vendor.launcher.crypto.math;

import java.io.Serializable;

public abstract class ShadedNumber implements Serializable {
   private static final long serialVersionUID;
   private static int[] flowState;

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

   public static void setFlowState(int[] ints) {
      flowState = ints;
   }

   public static int[] getFlowState() {
      return flowState;
   }

   static {
      setFlowState(new int[5]);
      long l = -8742448824652078965L;
      serialVersionUID = l;
   }
}
