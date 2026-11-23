package net.augustus.vendor.launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class InputCredentialValidator {
   private static final List<Character> SPECIAL_CHARS;
   private static final List<Character> DIGIT_CHARS;
   private static final List<Character> LOWERCASE_CHARS;
   private static final List<Character> UPPERCASE_CHARS;

   public static boolean validatePassword(Text text, String str, int n) {
      List<Character> chars = Arrays.asList(str.chars().mapToObj(InputCredentialValidator::boxPasswordChar).toArray(InputCredentialValidator::newPasswordCharArray));
      boolean hasNum = false;
      for (char c : DIGIT_CHARS) {
         if (chars.contains(c)) {
            hasNum = true;
         }
      }
      boolean hasSpec = false;
      for (char c : SPECIAL_CHARS) {
         if (chars.contains(c)) {
            hasSpec = true;
         }
      }
      boolean hasLow = false;
      for (char c : LOWERCASE_CHARS) {
         if (chars.contains(c)) {
            hasLow = true;
         }
      }
      boolean hasUp = false;
      for (char c : UPPERCASE_CHARS) {
         if (chars.contains(c)) {
            hasUp = true;
         }
      }
      if (text != null) {
         if (!hasNum) {
            text.setText("Numbers required! \n(1, 2, 3, ...)");
         }
         if (!hasSpec) {
            text.setText("Special characters required! \n(#, !, @, ...)");
         }
         if (!hasLow) {
            text.setText("Lower case characters required! \n(a, b, c, ...)");
         }
         if (!hasUp) {
            text.setText("Upper case characters required! \n(A, B, C, ...)");
         }
         if (str.length() < n) {
            text.setText("To short! (length < " + n + ")");
         }
         text.setFill(Paint.valueOf("#f70202"));
      }
      return hasLow && hasUp && hasNum && hasSpec && str.length() >= n;
   }

   public static boolean isAllowedUsernameChar(long l, byte b, char ch) {
      for (char c : LOWERCASE_CHARS) {
         if (c == ch) {
            return true;
         }
      }
      for (char c : UPPERCASE_CHARS) {
         if (c == ch) {
            return true;
         }
      }
      for (char c : DIGIT_CHARS) {
         if (c == ch) {
            return true;
         }
      }
      return ch == '_';
   }

   private static Character boxUsernameChar(int n) {
      return (char)n;
   }

   private static Character boxPasswordChar(int n) {
      return (char)n;
   }

   private static Character[] newUsernameCharArray(int n) {
      return new Character[n];
   }

   private static Character[] newPasswordCharArray(int n) {
      return new Character[n];
   }

   public static boolean validateUsername(Text text, String str, int n) {
      List<Character> chars = Arrays.asList(str.chars().mapToObj(InputCredentialValidator::boxUsernameChar).toArray(InputCredentialValidator::newUsernameCharArray));
      ArrayList<Character> invalid = new ArrayList<>();
      for (char c : chars) {
         if (!DIGIT_CHARS.contains(c) && !LOWERCASE_CHARS.contains(c) && !UPPERCASE_CHARS.contains(c) && c != '_' && !invalid.contains(c)) {
            invalid.add(c);
         }
      }
      if (text != null) {
         if (!invalid.isEmpty()) {
            StringBuilder sb = new StringBuilder("");
            for (Character c : invalid) {
               sb.append(c).append(c == invalid.get(invalid.size() - 1) ? "" : ", ");
            }
            text.setText("Invalid characters: " + sb.toString());
         }
         if (str.length() < n) {
            text.setText("To short! (length < " + n + ")");
         }
         text.setFill(Paint.valueOf("#f70202"));
      }
      return invalid.isEmpty() && str.length() >= n;
   }




   static {
      DIGIT_CHARS = Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9', '0');
      SPECIAL_CHARS = Arrays.asList('!', '&', '*', '?', '=', '#', '^', '@', '%', '$', '#', '(', ')', '-');
      LOWERCASE_CHARS = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
      UPPERCASE_CHARS = Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
   }
}
