package net.augustus.vendor.launcher;

public enum LoginDecision {
   ACCEPTED,
   DENIED;

   private static final LoginDecision[] ENUM_VALUES = new LoginDecision[]{ACCEPTED, DENIED};

   public static LoginDecision[] values(long l) {
      return (LoginDecision[])ENUM_VALUES.clone();
   }
}
