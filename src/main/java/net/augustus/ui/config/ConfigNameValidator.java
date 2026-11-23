package net.augustus.ui.config;

import java.util.HashSet;
import java.util.Set;

public class ConfigNameValidator {
   private static final Set ALLOWED_CHARACTERS = new HashSet();

   static {
      for (char i = 'a'; i <= 'z'; i++) {
         ALLOWED_CHARACTERS.add(i);
      }

      for (char j = 'A'; j <= 'Z'; j++) {
         ALLOWED_CHARACTERS.add(j);
      }

      for (char k = '0'; k <= '9'; k++) {
         ALLOWED_CHARACTERS.add(k);
      }

      char[] chars = new char[]{'ä', 'ö', 'ü', 'Ä', 'Ö', 'Ü'};

      for (char ch : chars) {
         ALLOWED_CHARACTERS.add(ch);
      }

      char[] chars2 = new char[]{'-', '_', ' '};

      for (char ch2 : chars2) {
         ALLOWED_CHARACTERS.add(ch2);
      }
   }

   public static boolean isValidConfigName(String str) {
      boolean flag = false;

      for (int i = 0; i < str.length(); i++) {
         char ch = str.charAt(i);
         if (!ALLOWED_CHARACTERS.contains(ch)) {
            return false;
         }
      }

      return true;
   }
}
