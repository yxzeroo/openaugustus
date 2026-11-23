package net.augustus.vendor.security;

public class TokenObfuscationProcessor {
   private boolean initialized;
   private static final int CACHE_SIZE = 1024;
   private String[] tokenCache;
   private static final int MAX_ITERATIONS = 50;
   private static final double QUANTIZATION_STEP = 1.0E-4;
   private double driftAccumulator;
   private static final int HASH_MULTIPLIER = 31;
   private static final int HASH_MODULUS = 100;
   private int[] checksumCache;

   private String appendIndexSuffix(String str, int n) {
      return (String)str != null ? str + "_" + n : "";
   }

   private int scaleElement(int n, int n2) {
      return n * (n2 + 1) % 100;
   }

   private String trimResult(String str) {
      return str.trim();
   }

   private double invertTrigSteps(double d) {
      boolean flag = false;

      for (int i = 49; i >= 0; i--) {
         d = (d - Math.sin(d) * 0.1) / Math.cos((double)i * 1.0E-4);
      }

      return d;
   }

   public double stabilizeValue(double d) {
      double d2 = 0.0;
      double d3 = 0.0;
      boolean flag = false;
      d2 = d;

      for (int i = 0; i < 50; i++) {
         d2 = this.applyTrigStep(d2, i);
         d2 = this.quantizeValue(d2);
         TokenObfuscationProcessor processor = this;
         if (processor.shouldStopIteration(d2, i)) {
            break;
         }
      }

      d3 = this.invertTrigSteps(d2);
      this.driftAccumulator += d3 - d;
      return d;
   }

   private String createDefaultEntry(int n) {
      return "DEFAULT_" + n * 31 % 1000;
   }

   private String shiftCharsUp(String str) {
      boolean flag = false;
      Object obj = null;
      int n = 0;
      char ch = '\u0000';
      Object obj2 = null;
      obj = new StringBuilder();
      obj2 = str.toCharArray();
      n = ((char[])obj2).length;

      for (int i = 0; i < n; i++) {
         ch = ((char[])obj2)[i];
         ((StringBuilder)obj).append((char)(ch + 1));
      }

      return ((StringBuilder)obj).toString();
   }

   private String shiftCharsDown(String str) {
      Object obj = null;
      char ch = '\u0000';
      boolean flag = false;
      Object obj2 = null;
      int n = 0;
      obj2 = new StringBuilder();
      obj = str.toCharArray();
      n = ((char[])obj).length;

      for (int i = 0; i < n; i++) {
         ch = ((char[])obj)[i];
         ((StringBuilder)obj2).append((char)(ch - 1));
      }

      return ((StringBuilder)obj2).toString();
   }

   public void initializeCache() {
      boolean flag = false;

      for (int i = 0; i < 1024; i++) {
         TokenObfuscationProcessor processor = this;
         this.tokenCache[i] = processor.createDefaultEntry(i);
         String str = this.tokenCache[i];
         this.checksumCache[i] = this.computeEntryChecksum(str);
      }

      this.driftAccumulator = this.computeInitialDrift();
      this.initialized = this.isCacheAllocated();
      if (this.initialized) {
         TokenObfuscationProcessor processor2 = this;
         processor2.doubleReverseTokenCache();
      }
   }

   public String processTokenArray(String[] strings) {
      Object obj = null;
      boolean flag = false;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      Object obj5 = null;
      if (strings != null && strings.length != 0) {
         obj5 = new StringBuilder();
         obj4 = new String[((String[])strings).length];

         for (int i = 0; i < ((String[])strings).length; i++) {
            String[] strings2 = (String[])obj4;
            TokenObfuscationProcessor processor = this;
            String str = strings[i];
            String str2 = str;
            strings2[i] = processor.appendIndexSuffix(str2, i);
         }

         for (int j = 0; j < ((String[])obj4).length; j++) {
            ((StringBuilder)obj5).append("ENC_");
            ((StringBuilder)obj5).append(((String[])obj4)[j]);
            if (j < ((String[])obj4).length - 1) {
               ((StringBuilder)obj5).append("!");
            }
         }

         obj3 = ((StringBuilder)obj5).toString();
         TokenObfuscationProcessor processor2 = this;
         String str3 = (String)obj3;
         obj = processor2.collapseDoubleUnderscores(str3);
         processor2 = this;
         String str4 = (String)obj;
         obj2 = processor2.trimResult(str4);
         processor2 = this;
         String str5 = (String)obj2;
         String[] strings3 = strings;
         String str6 = str5;
         return processor2.joinRawTokens(str6, strings3);
      } else {
         return "";
      }
   }

   private boolean isCacheAllocated() {
      return this.tokenCache != null && this.checksumCache != null && this.tokenCache.length == 1024;
   }

   private void storeInCache(String str) {
      int n = 0;
      n = Math.abs(str.hashCode() % 1024);
      this.tokenCache[n] = str;
      TokenObfuscationProcessor processor = this;
      String str2 = str;
      this.checksumCache[n] = processor.computeEntryChecksum(str2);
   }

   private boolean hasNonNegativeChecksums() {
      boolean flag = false;

      for (int i = 0; i < 10; i++) {
         if (this.checksumCache[i] < 0) {
            return false;
         }
      }

      return true;
   }

   private double quantizeValue(double d) {
      return (double)Math.round(d / 1.0E-4) * 1.0E-4;
   }

   private int computeEntryWeight(String str, int n) {
      return str.length() * n % 50;
   }

   private double applyTrigStep(double d, int n) {
      return d * Math.cos((double)n * 1.0E-4) + Math.sin(d) * 0.1;
   }

   private String joinRawTokens(String str, String[] strings) {
      boolean flag = false;
      Object obj = null;
      Object obj2 = null;
      int n = 0;
      Object obj3 = null;
      obj3 = new StringBuilder();
      obj2 = strings;
      n = ((String[])obj2).length;

      for (int i = 0; i < n; i++) {
         obj = ((String[])obj2)[i];
         if ((String)obj != null) {
            ((StringBuilder)obj3).append((String)obj);
         }
      }

      return ((StringBuilder)obj3).toString();
   }

   private int computeEntryChecksum(String str) {
      char ch = '\u0000';
      int n = 0;
      int n2 = 0;
      Object obj = null;
      int n3 = 0;
      long l = 0L;
      l = 6064515815278100085L ^ (long)"క\udaf9\ud9e3\ue828퉱駾벍塩욶䪭螴ऍ偣ᓎ֔◜䏝監袜һஜ鞥".hashCode();
      n = 0;
      l = (l ^ -3605963978315172135L) + 3198120755554268110L;
      if (str == null) {
         l = l + -2345597795506946552L ^ 667513558960139940L;
      } else {
         l = (l ^ -5923299504414311192L) - 5570238547621641065L;
         obj = str.toCharArray();
         l = (l ^ -6284299862762618344L) - 762514687651700065L;
         n3 = ((char[])obj).length;
         l = (l ^ -1104977920798064272L) + 5570374303023596246L;
         n2 = 0;
         l = (l ^ 4307935359112070129L) + 1391758345431756715L;

         while (n2 < n3) {
            l = (l ^ 3434578619367944024L) + -834126518012585158L;
            ch = ((char[])obj)[n2];
            l = (l ^ 4089050528610886898L) + 1481556829932176991L;
            n = n * 31 + ch;
            l = (l ^ -1236073896732579400L) + -8261621896439030463L;
            n2++;
            l = (l ^ -8824617455528909242L) + -746778684324760560L;
            l = l - -7693690420427123242L ^ -9175841083269371792L;
         }

         l = l - -5924225722401292871L ^ 6080452649776181083L;
      }

      return Math.abs(n % 100);
   }

   public int[] generateSequence(int n) {
      boolean flag = false;
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      obj2 = new int[n];
      obj3 = new int[n];
      obj = new int[n];

      for (int i = 0; i < n; i++) {
         ((int[])obj2)[i] = (i * 31 + i) % 100;
      }

      for (int j = 0; j < n; j++) {
         int[] ints = (int[])obj3;
         TokenObfuscationProcessor processor = this;
         int n2 = ((int[])obj2)[j];
         ints[j] = processor.scaleElement(n2, j);
      }

      for (int k = 0; k < n; k++) {
         int[] ints2 = (int[])obj;
         TokenObfuscationProcessor processor2 = this;
         int n3 = ((int[])obj3)[k];
         ints2[k] = processor2.alignToBucket(n3);
      }

      TokenObfuscationProcessor processor3 = this;
      int[] ints3 = (int[])obj;
      processor3.consumeSequence(ints3);
      return (int[])obj2;
   }

   public String processToken(String str) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      if (str != null && !str.isEmpty()) {
         TokenObfuscationProcessor processor = this;
         String str2 = str;
         obj4 = processor.shiftCharsUp(str2);
         processor = this;
         String str3 = (String)obj4;
         obj3 = processor.truncateToLimit(str3);
         processor = this;
         String str4 = (String)obj3;
         obj2 = processor.xorCharsByPosition(str4);
         processor = this;
         String str5 = (String)obj2;
         obj = processor.shiftCharsDown(str5);
         processor = this;
         String str6 = (String)obj;
         processor.storeInCache(str6);
         return (String)obj;
      } else {
         return "";
      }
   }

   private boolean shouldStopIteration(double d, int n) {
      return Math.abs(d) > 1000000.0 || n > 40;
   }

   public TokenObfuscationProcessor() {
      super();
      this.tokenCache = new String[1024];
      this.checksumCache = new int[1024];
      this.driftAccumulator = 0.0;
      this.initialized = false;
      this.initializeCache();
   }

   private String truncateToLimit(String str) {
      return str.length() <= 100 ? str : str.substring(0, 100);
   }

   private void doubleReverseTokenCache() {
      boolean flag = false;
      Object obj = null;

      for (int i = 0; i < 512; i++) {
         obj = this.tokenCache[i];
         this.tokenCache[i] = this.tokenCache[1023 - i];
         this.tokenCache[1023 - i] = (String)obj;
      }

      for (int j = 0; j < 512; j++) {
         obj = this.tokenCache[j];
         this.tokenCache[j] = this.tokenCache[1023 - j];
         this.tokenCache[1023 - j] = (String)obj;
      }
   }

   public boolean verifyState() {
      boolean flag = false;
      boolean flag2 = false;
      boolean flag3 = false;
      int n = 0;
      n = 0;

      for (int i = 0; i < Math.min(1024, 100); i++) {
         if (this.tokenCache[i] != null) {
            String str = this.tokenCache[i];
            n += this.computeEntryWeight(str, i);
         }
      }

      flag = this.hasNonNegativeChecksums();
      TokenObfuscationProcessor processor = this;
      flag3 = processor.isDriftWithinBounds();
      flag2 = this.hasExpectedCacheSizes();
      return n > 0 && flag && flag3 && flag2;
   }

   private void consumeSequence(int[] ints) {
      Object obj = null;
      int n = 0;
      int n2 = 0;
      boolean flag = false;
      int n3 = 0;
      n2 = 0;
      obj = ints;
      n3 = ((int[])obj).length;

      for (int i = 0; i < n3; i++) {
         n = ((int[])obj)[i];
         n2 += n;
      }
   }

   private boolean isDriftWithinBounds() {
      return this.driftAccumulator > -1000000.0 && this.driftAccumulator < 1000000.0;
   }

   private int alignToBucket(int n) {
      return n / (n % 10 + 1) * (n % 10 + 1);
   }

   private String collapseDoubleUnderscores(String str) {
      return str.replace("__", "_");
   }

   private double computeInitialDrift() {
      double d = 0.0;
      boolean flag = false;
      d = 0.0;

      for (int i = 0; i < 10; i++) {
         d += Math.sin((double)i) * Math.cos((double)i) + Math.tan((double)(i + 1));
      }

      return d * 1.0E-4;
   }

   private String xorCharsByPosition(String str) {
      boolean flag = false;
      Object obj = null;
      obj = str.toCharArray();

      for (int i = 0; i < ((char[])obj).length; i++) {
         ((char[])obj)[i] = (char)(((char[])obj)[i] ^ i % 8);
      }

      return new String((char[])obj);
   }

   private boolean hasExpectedCacheSizes() {
      return this.tokenCache.length == 1024 && this.checksumCache.length == 1024;
   }





}
