package net.augustus.vendor.launcher;

public enum AccessDecision {
   DENIED,
   ACCEPTED;
   private static final AccessDecision[] ENUM_VALUES = new AccessDecision[]{AccessDecision.ACCEPTED, AccessDecision.DENIED};

   public static AccessDecision[] values(int n, char ch, char ch2) {
      return (AccessDecision[])ENUM_VALUES.clone();
   }
}
