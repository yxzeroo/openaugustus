package net.augustus.ui.account;

public class AccountLoginTypeSwitchMap {
   public static final int[] ORDINAL_MAP = new int[AccountLoginType.values().length];

   static {
      try {
         ORDINAL_MAP[AccountLoginType.MICROSOFT_CREDENTIALS.ordinal()] = 1;
      } catch (NoSuchFieldError e) {
      }

      try {
         ORDINAL_MAP[AccountLoginType.MICROSOFT_DEVICE_CODE.ordinal()] = 2;
      } catch (NoSuchFieldError e2) {
      }

      try {
         ORDINAL_MAP[AccountLoginType.MICROSOFT_EXTERNAL_BROWSER.ordinal()] = 3;
      } catch (NoSuchFieldError e3) {
      }

      try {
         ORDINAL_MAP[AccountLoginType.MICROSOFT_ACCESS_TOKEN.ordinal()] = 4;
      } catch (NoSuchFieldError e4) {
      }

      try {
         ORDINAL_MAP[AccountLoginType.CRACKED.ordinal()] = 5;
      } catch (NoSuchFieldError e5) {
      }

      try {
         ORDINAL_MAP[AccountLoginType.THE_ALTENING.ordinal()] = 6;
      } catch (NoSuchFieldError e6) {
      }
   }
}
