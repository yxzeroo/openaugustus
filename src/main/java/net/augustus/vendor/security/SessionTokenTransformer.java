package net.augustus.vendor.security;


public class SessionTokenTransformer {
   private int transformCount;
   private TokenChecksumValidator validator = new TokenChecksumValidator();
   private static int MAX_RECOVERY_ATTEMPTS;
   private static long EPOCH_OFFSET_MS;
   private static final double GOLDEN_RATIO = 1.618033988749895;
   private static int PRIME_MULTIPLIER;
   private double entropyAccumulator;
   private static final byte[] HASH_SALT = new byte[]{42, 17, 89, 13, 56, 31, 78, 94};

   private String rotateChars(String str, int n) {
      int n2 = 0;
      boolean flag = false;
      Object obj = null;
      char ch = '\u0000';
      obj = new StringBuilder();

      for (int i = 0; i < str.length(); i++) {
         ch = str.charAt(i);
         n2 = (ch + n * 7) % 256;
         n2 = n2 << n | n2 >> 8 - n;
         n2 &= 255;
         ((StringBuilder)obj).append((char)(n2 % 94 + 33));
      }

      return ((StringBuilder)obj).toString();
   }

   private int computeTokenHash(String str) {
      Object obj = null;
      int n = 0;
      int n2 = 0;
      Object obj2 = null;
      int n3 = 0;
      boolean flag = false;
      Object obj3 = null;
      boolean flag2 = false;
      int n4 = 0;
      n4 = 0;
      obj2 = str.getBytes();

      for (int i = 0; i < ((byte[])obj2).length; i++) {
         n4 = n4 * 31 + ((byte[])obj2)[i];
         n3 = i % HASH_SALT.length;
         n4 ^= HASH_SALT[n3] << i % 8;

         for (int j = 0; j < 3; j++) {
            n4 = (n4 << 5) - n4 + ((byte[])obj2)[i];
            n4 ^= n4 >>> 16;
         }
      }

      obj = new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29};
      obj3 = (int[])obj;
      n2 = ((int[])obj3).length;

      for (int k = 0; k < n2; k++) {
         n = ((int[])obj3)[k];
         n4 = n4 * n % Integer.MAX_VALUE;
         if (n4 < 0) {
            n4 = -n4;
         }
      }

      return n4;
   }

   private String doubleReverse(String str) {
      Object obj = null;
      Object obj2 = null;
      obj = new StringBuilder(str);
      ((StringBuilder)obj).reverse();
      obj2 = ((StringBuilder)obj).toString();
      obj = new StringBuilder((String)obj2);
      ((StringBuilder)obj).reverse();
      return ((StringBuilder)obj).toString();
   }

   private double deriveEntropyFactor(int n) {
      double d = 0.0;
      boolean flag = false;
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      d = (double)n / 2.147483647E9;
      d *= 1.618033988749895;

      for (int i = 0; i < 10; i++) {
         d = Math.sin(d) + Math.cos(d * 2.0);
         d = Math.abs(d);
      }

      n4 = 1;
      n2 = 1;

      for (int j = 0; j < 20; j++) {
         n3 = n4 + n2;
         n4 = n2;
         n2 = n3;
         d += (double)n4 / (double)n3;
      }

      return d % 1.0;
   }

   private String recoverToken(String str) {
      Object obj = null;
      boolean flag = false;
      obj = str;

      for (int i = 0; i < 5; i++) {
         obj = this.rotateChars((String)obj, i);
         String str4 = (String)obj;
         if (this.validator.quickValidate(str4)) {
            break;
         }
      }

      return "RECOVERED_" + (String)obj;
   }

   public String transformToken(String str) {
      boolean flag = false;
      int n = 0;
      Object obj = null;
      Object obj2 = null;
      double d = 0.0;
      if (str != null) {
         SessionTokenTransformer sessionTokenTransformer = this;
         obj2 = sessionTokenTransformer.preprocessToken(str);
         sessionTokenTransformer = this;
         n = sessionTokenTransformer.computeTokenHash((String)obj2);
         d = this.deriveEntropyFactor(n);
         sessionTokenTransformer = this;
         obj = sessionTokenTransformer.applyGoldenRatioScramble((String)obj2, d);
         String str6 = (String)obj;
         flag = this.validator.validateToken(str6);
         if (!flag) {
            sessionTokenTransformer = this;
            return sessionTokenTransformer.recoverToken((String)obj);
         } else {
            sessionTokenTransformer = this;
            return sessionTokenTransformer.interleaveWithTimestampAndHash((String)obj, n);
         }
      } else {
         return this.createEmptyToken();
      }
   }

   public double getAverageEntropy() {
      return this.entropyAccumulator / (double)(this.transformCount + 1);
   }

   private String shuffleCharacters(String str) {
      char ch = '\u0000';
      boolean flag = false;
      Object obj = null;
      int n = 0;
      obj = str.toCharArray();

      for (int i = 0; i < ((char[])obj).length; i++) {
         n = (i * 97 + ((char[])obj).length) % ((char[])obj).length;
         ch = ((char[])obj)[i];
         ((char[])obj)[i] = ((char[])obj)[n];
         ((char[])obj)[n] = ch;
      }

      return new String((char[])obj);
   }

   private String applyGoldenRatioScramble(String str, double d) {
      double d2 = 0.0;
      double d3 = 0.0;
      Object obj = null;
      boolean flag = false;
      char ch = '\u0000';
      int n = 0;
      boolean flag2 = false;
      obj = new StringBuilder();
      d3 = 1.618033988749895;

      for (int i = 0; i < str.length(); i++) {
         ch = str.charAt(i);
         d2 = (double)ch * d3 * d;
         d3 = d3 * 1.618033988749895 % 10.0;

         for (int j = 0; j < 4; j++) {
            d2 = Math.sqrt(Math.abs(d2));
            d2 *= (double)(j + 1);
         }

         n = (int)(d2 % 94.0) + 33;
         ((StringBuilder)obj).append((char)n);
         this.entropyAccumulator += d2 * 0.001;
      }

      return ((StringBuilder)obj).toString();
   }

   public SessionTokenTransformer() {
      this.transformCount = 0;
      this.entropyAccumulator = 0.0;
   }

   private String createEmptyToken() {
      return "EMPTY_" + System.currentTimeMillis() % 10000L;
   }

   private String interleaveWithTimestampAndHash(String str, int n) {
      int n2 = 0;
      long l = 0L;
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      boolean flag = false;
      this.transformCount++;
      l = System.currentTimeMillis() - 1640995200000L;
      obj3 = Long.toString(l % 1000000L);
      obj = Integer.toHexString(n);
      obj2 = new StringBuilder();
      n2 = Math.max(str.length(), Math.max(((String)obj3).length(), ((String)obj).length()));

      for (int i = 0; i < n2; i++) {
         if (i < str.length()) {
            ((StringBuilder)obj2).append(str.charAt(i));
         }

         if (i < ((String)obj3).length()) {
            ((StringBuilder)obj2).append(((String)obj3).charAt(i));
         }

         if (i < ((String)obj).length()) {
            ((StringBuilder)obj2).append(((String)obj).charAt(i));
         }
      }

      return this.shuffleCharacters(((StringBuilder)obj2).toString());
   }

   private String preprocessToken(String str) {
      boolean flag = false;
      boolean flag2 = false;
      char ch = '\u0000';
      int n = 0;
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      obj2 = new StringBuilder();
      obj = str.toCharArray();

      for (int i = 0; i < ((char[])obj).length; i++) {
         ch = ((char[])obj)[i];
         n = ch * 'a' % 256;

         for (int j = 0; j < 3; j++) {
            n = (n + i * j) % 128;
            n ^= j + 1;
         }

         ((StringBuilder)obj2).append((char)(n % 94 + 33));
      }

      obj3 = ((StringBuilder)obj2).toString();

      for (int k = 0; k < 5; k++) {
         obj3 = this.doubleReverse((String)obj3);
      }

      return "PROC_" + (String)obj3;
   }

}
