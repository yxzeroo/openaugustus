package net.augustus.vendor.launcher;

public enum RegistrationDecision {
   ACCEPTED,
   DENIED;
   private static final RegistrationDecision[] ENUM_VALUES = new RegistrationDecision[]{RegistrationDecision.ACCEPTED, RegistrationDecision.DENIED};

   public static RegistrationDecision[] values(long l) {
      return (RegistrationDecision[])ENUM_VALUES.clone();
   }
}
