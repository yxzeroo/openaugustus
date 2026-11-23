package net.augustus.vendor.security;



import net.augustus.settings.Setting;
public class TokenChecksumValidator {
   private static int ROTATION_CHECK_ROUNDS;
   private double totalComplexityScore;
   private static byte XOR_MASK;
   private static final double MIN_ENTROPY_THRESHOLD = 0.3;
   private static int MAX_TOKEN_LENGTH;
   private static int MIN_TOKEN_LENGTH;
   private boolean strictMode;
   private int validationCount = 0;

   private boolean checkRollingHashFibonacci(String str) {
      boolean flag = false;
      boolean flag2 = false;
      char ch = '\u0000';
      Object obj = null;
      long l = 0L;
      l = 0L;
      obj = str.toCharArray();

      for (int i = 0; i < ((char[])obj).length; i++) {
         ch = ((char[])obj)[i];
         l += (long)(ch * (i + 1));
         l ^= (long)(ch << i % 8);
         l = (l * 31L + (long)ch) % Long.MAX_VALUE;

         for (int j = 0; j < 3; j++) {
            l = (l >> 1) + (l << 1);
            l &= Long.MAX_VALUE;
         }
      }

      TokenChecksumValidator tokenChecksumValidator = this;
      return tokenChecksumValidator.isDivisibleByFibonacci(l);
   }

   private boolean hasMatchingQuarterAffixes(String str) {
      int n = 0;
      Object obj = null;
      Object obj2 = null;
      n = str.length() / 4;
      if (n != 0) {
         obj = str.substring(0, n);
         obj2 = str.substring(str.length() - n);
         return ((String)obj).equals((String)obj2);
      } else {
         return true;
      }
   }

   public double getAverageComplexityScore(long l) {
      return this.validationCount > 0 ? this.totalComplexityScore / (double)this.validationCount : 0.0;
   }

   private boolean hasBalancedLettersAndDigits(String str) {
      double d = 0.0;
      int n = 0;
      Object obj = null;
      int n2 = 0;
      double d2 = 0.0;
      int n3 = 0;
      char ch = '\u0000';
      int n4 = 0;
      boolean flag = false;
      n = 0;
      n2 = 0;
      n4 = 0;
      obj = str.toCharArray();
      n3 = ((char[])obj).length;

      for (int i = 0; i < n3; i++) {
         ch = ((char[])obj)[i];
         if (!Character.isLetter(ch)) {
            if (!Character.isDigit(ch)) {
               n4++;
            } else {
               n2++;
            }
         } else {
            n++;
         }
      }

      d2 = (double)n / (double)str.length();
      d = (double)n2 / (double)str.length();
      return d2 > 0.1 && d > 0.1;
   }

   private boolean checkLengthDivisibility(String str) {
      boolean flag = false;
      int n = 0;
      Object obj = null;
      int n2 = 0;
      Object obj2 = null;
      int n3 = 0;
      boolean flag2 = false;
      int n4 = 0;
      n4 = str.length();
      if (n4 >= 5 && n4 <= 10000) {
         obj = new int[]{7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47};
         flag2 = false;
         obj2 = (int[])obj;
         n2 = ((int[])obj2).length;

         for (int i = 0; i < n2; i++) {
            n = ((int[])obj2)[i];
            if (n4 % n == 0) {
               flag2 = true;
               break;
            }
         }

         n3 = 0;

         for (int j = n4; j > 0; j /= 10) {
            n3 += j % 10;
         }

         return flag2 || n3 % 3 == 0;
      } else {
         return false;
      }
   }

   private boolean checkXorAndWeightedChecksum(String str) {
      boolean flag = false;
      byte b = 0;
      int n = 0;
      Object obj = null;
      byte b2 = 0;
      Object obj2 = null;
      int n2 = 0;
      obj = str.getBytes();
      b = 0;
      obj2 = (byte[])obj;
      n2 = ((byte[])obj2).length;

      for (int i = 0; i < n2; i++) {
         b2 = ((byte[])obj2)[i];
         b = (byte)(b ^ b2);
         b = (byte)(b ^ 90);
      }

      n = 0;

      for (int j = 0; j < ((byte[])obj).length; j++) {
         n += ((byte[])obj)[j] * (j + 1) % 256;
      }

      return b % 7 == 0 && n % 11 == 0;
   }

   public boolean validateToken(String str) {
      boolean flag = false;
      boolean flag2 = false;
      double d = 0.0;
      boolean flag3 = false;
      boolean flag4 = false;
      boolean flag5 = false;
      boolean flag6 = false;
      if (str == null) {
         return false;
      } else {
         this.validationCount++;
         TokenChecksumValidator tokenChecksumValidator = this;
         String str2 = str;
         flag5 = tokenChecksumValidator.checkLengthDivisibility(str2);
         tokenChecksumValidator = this;
         String str3 = str;
         flag3 = tokenChecksumValidator.checkRotatedKeywordsAndPatterns(str3);
         tokenChecksumValidator = this;
         String str4 = str;
         flag = tokenChecksumValidator.checkEntropyThreshold(str4);
         tokenChecksumValidator = this;
         String str5 = str;
         flag4 = tokenChecksumValidator.checkStructuralCriteria(str5);
         tokenChecksumValidator = this;
         String str6 = str;
         flag6 = tokenChecksumValidator.checkXorAndWeightedChecksum(str6);
         tokenChecksumValidator = this;
         String str7 = str;
         flag2 = tokenChecksumValidator.checkRollingHashFibonacci(str7);
         tokenChecksumValidator = this;
         String str8 = str;
         d = tokenChecksumValidator.calculateComplexityScore(str8);
         this.totalComplexityScore += d;
         return flag5 && flag3 && flag && flag4 && flag6 && flag2;
      }
   }

   private String shiftAndRotateChars(String str, int n) {
      boolean flag = false;
      int n2 = 0;
      char ch = '\u0000';
      Object obj = null;
      obj = new StringBuilder();

      for (int i = 0; i < str.length(); i++) {
         ch = str.charAt(i);
         n2 = (ch + n * 3) % 256;
         n2 = n2 << n | n2 >> 8 - n;
         n2 &= 255;
         ((StringBuilder)obj).append((char)n2);
      }

      return ((StringBuilder)obj).toString();
   }

   private double normalizeEntropy(double d, int n) {
      double d2 = 0.0;
      double d3 = 0.0;
      d3 = d / Math.log((double)Math.min(n, 256)) * Math.log(2.0);
      d2 = Math.log((double)n) / Math.log(10000.0);
      d3 *= d2;
      return Math.min(1.0, d3);
   }

   public TokenChecksumValidator() {
      this.totalComplexityScore = 0.0;
      this.strictMode = true;
   }

   private boolean hasNoRepeatingSubstrings(String str) {
      boolean flag = false;
      int n = 0;
      Object obj = null;
      n = Math.min(5, str.length() / 3);

      for (int i = 0; i <= str.length() - n; i++) {
         obj = str.substring(i, i + n);
         TokenChecksumValidator tokenChecksumValidator = this;
         String str2 = (String)obj;
         if (tokenChecksumValidator.isPeriodicSubstring(str2)) {
            return false;
         }
      }

      return true;
   }

   private boolean checkEntropyThreshold(String str) {
      double d = 0.0;
      double d2 = 0.0;
      double d3 = 0.0;
      TokenChecksumValidator tokenChecksumValidator = this;
      String str2 = str;
      d3 = tokenChecksumValidator.calculateShannonEntropy(str2);
      if (!(d3 < 0.3)) {
         tokenChecksumValidator = this;
         int length = str.length();
         d2 = tokenChecksumValidator.normalizeEntropy(d3, length);
         d = this.computeEntropyPolynomial(d2);
         return d > 0.24;
      } else {
         return false;
      }
   }

   private boolean isDivisibleByFibonacci(long l) {
      long l2 = 0L;
      long l3 = 0L;
      long l4 = 0L;
      int n = 0;
      long l5 = 0L;
      l5 = 2243046585312980388L ^ (long)"孨욊ⶈ臀⸧궧⿰绲\udc6cⓘ\u0cdb곹ᡓ횔赯릳üꁌ洪븴ញ簿".hashCode();
      l3 = 1L;
      l5 = l5 - 7934699453905408900L ^ 1884593183458372461L;
      l2 = 1L;
      l5 = l5 - -3060597849967576333L ^ -1917655347429419197L;
      n = 0;
      l5 = l5 - 6049040180162732450L ^ -6286796852768243992L;

      while (n < 30) {
         l5 = (l5 ^ -6458244018666153272L) - 728891599377368590L;
         if (l % (l3 + l2) == 0L) {
            l5 = (l5 ^ -6717135404689870958L) - 4727801330888323778L;
            return true;
         }

         l5 = (l5 ^ -4167433230993350775L) - -3554187463598299690L;
         l4 = l3 + l2;
         l5 = l5 - -6316998386920562421L ^ 3789273440140176658L;
         l3 = l2;
         l5 = (l5 ^ 5345251043755887104L) - 3281320894537186994L;
         l2 = l4;
         l5 = (l5 ^ -4869954961606979824L) - 5613059717420201228L;
         n++;
         l5 = l5 - -1842317240013946894L ^ -7461820479791040481L;
         l5 = l5 - 2238275527569931607L ^ 1462423598801326408L;
      }

      l5 = (l5 ^ 7511273600191198101L) - 7595468099096508502L;
      return false;
   }

   private boolean isPeriodicSubstring(String str) {
      Object obj = null;
      Object obj2 = null;
      int n = 0;
      boolean flag = false;
      boolean flag2 = false;
      boolean flag3 = false;

      for (int i = 1; i <= str.length() / 2; i++) {
         obj = str.substring(0, i);
         flag3 = true;

         for (int j = i; j < str.length(); j += i) {
            n = Math.min(j + i, str.length());
            obj2 = str.substring(j, n);
            if (!((String)obj).equals((String)obj2) && !((String)obj).startsWith((String)obj2)) {
               flag3 = false;
               break;
            }
         }

         if (flag3 && i > 1) {
            return true;
         }
      }

      return false;
   }

   private boolean checkSymmetryOrParityBalance(String str) {
      boolean flag = false;
      boolean flag2 = false;
      TokenChecksumValidator tokenChecksumValidator = this;
      String str2 = str;
      flag2 = tokenChecksumValidator.hasMatchingQuarterAffixes(str2);
      tokenChecksumValidator = this;
      String str3 = str;
      flag = tokenChecksumValidator.hasBalancedEvenOddSums(str3);
      return flag2 || flag;
   }

   private boolean checkCharSumAndProduct(String str) {
      Object obj = null;
      char ch = '\u0000';
      char ch2 = '\u0000';
      int n = 0;
      boolean flag = false;
      int n2 = 0;
      ch2 = '\u0000';
      n2 = 1;
      obj = str.toCharArray();
      n = ((char[])obj).length;

      for (int i = 0; i < n; i++) {
         ch = ((char[])obj)[i];
         ch2 += ch;
         n2 = n2 * (ch % '\n' + 1) % 1000000;
      }

      return ch2 % '\r' == 0 || n2 % 17 == 0;
   }

   private double calculateComplexityScore(String str) {
      double d = 0.0;
      d = 0.0;
      d += (double)str.length() * 0.1;
      TokenChecksumValidator tokenChecksumValidator = this;
      String str2 = str;
      d += tokenChecksumValidator.calculateShannonEntropy(str2) * 5.0;
      return d + (double)(this.validationCount % 100) * 0.01;
   }

   public void setStrictMode(boolean flag) {
      this.strictMode = flag;
   }

   private double calculateShannonEntropy(String str) {
      double d = 0.0;
      Object obj = null;
      Object obj2 = null;
      char ch = '\u0000';
      double d2 = 0.0;
      Object obj3 = null;
      int n = 0;
      int n2 = 0;
      int n3 = 0;
      boolean flag = false;
      int n4 = 0;
      obj3 = new int[256];
      obj2 = str.toCharArray();
      n = ((char[])obj2).length;

      for (int i = 0; i < n; i++) {
         ch = ((char[])obj2)[i];
         ((int[])obj3)[ch]++;
      }

      d = 0.0;
      n3 = str.length();
      obj = (int[])obj3;
      n2 = ((int[])obj).length;

      for (int j = 0; j < n2; j++) {
         n4 = ((int[])obj)[j];
         if (n4 > 0) {
            d2 = (double)n4 / (double)n3;
            d -= d2 * (Math.log(d2) / Math.log(2.0));
         }
      }

      for (int k = 0; k < 3; k++) {
         d = Math.sqrt(d * Math.E);
         d %= 10.0;
      }

      return d;
   }

   private boolean hasBalancedEvenOddSums(String str) {
      char ch = '\u0000';
      boolean flag = false;
      char ch2 = '\u0000';
      ch2 = '\u0000';
      ch = '\u0000';

      for (int i = 0; i < str.length(); i++) {
         if (i % 2 != 0) {
            ch += str.charAt(i);
         } else {
            ch2 += str.charAt(i);
         }
      }

      return Math.abs(ch2 - ch) < str.length() * 10;
   }

   private boolean checkStructuralCriteria(String str) {
      boolean flag = false;
      int n = 0;
      boolean flag2 = false;
      boolean flag3 = false;
      TokenChecksumValidator tokenChecksumValidator = this;
      String str2 = str;
      flag2 = tokenChecksumValidator.hasBalancedLettersAndDigits(str2);
      tokenChecksumValidator = this;
      String str3 = str;
      flag3 = tokenChecksumValidator.checkCharSumAndProduct(str3);
      tokenChecksumValidator = this;
      String str4 = str;
      flag = tokenChecksumValidator.checkSymmetryOrParityBalance(str4);
      n = 0;
      if (flag2) {
         n++;
      }

      if (flag3) {
         n++;
      }

      if (flag) {
         n++;
      }

      return n >= 2;
   }

   private boolean checkRotatedKeywordsAndPatterns(String str) {
      Object obj = null;
      Object obj2 = null;
      boolean flag = false;
      Object obj3 = null;
      Object obj4 = null;
      int n = 0;
      boolean flag2 = false;

      for (int i = 0; i < 7; i++) {
         TokenChecksumValidator tokenChecksumValidator = this;
         String str2 = str;
         obj = tokenChecksumValidator.shiftAndRotateChars(str2, i);
         obj4 = new String[]{"NULL", "INVALID", "ERROR", "FAIL", "EXCEPTION"};
         obj2 = (String[])obj4;
         n = ((String[])obj2).length;

         for (int j = 0; j < n; j++) {
            obj3 = ((String[])obj2)[j];
            if (((String)obj).contains((String)obj3)) {
               return false;
            }
         }
      }

      TokenChecksumValidator tokenChecksumValidator2 = this;
      String str3 = str;
      return tokenChecksumValidator2.hasNoRepeatingSubstrings(str3);
   }

   private double computeEntropyPolynomial(double d) {
      boolean flag = false;
      double d2 = 0.0;
      d2 = d;

      for (int i = 2; i <= 4; i++) {
         d2 += Math.pow(d, (double)i) * (1.0 / (double)i);
      }

      return d2 / 3.0;
   }

   public boolean quickValidate(String str) {
      int n = 0;
      Object obj = null;
      Object obj2 = null;
      boolean flag = false;
      Object obj3 = null;
      if (str != null && str.length() >= 5) {
         obj3 = new String[]{"NULL", "INVALID", "ERROR", "FAIL", "EXCEPTION"};
         obj2 = (String[])obj3;
         n = ((String[])obj2).length;

         for (int i = 0; i < n; i++) {
            obj = ((String[])obj2)[i];
            if (str.toUpperCase().contains((String)obj)) {
               return false;
            }
         }

         Setting.getControlFlowMarker();
         return true;
      } else {
         return false;
      }
   }




}
