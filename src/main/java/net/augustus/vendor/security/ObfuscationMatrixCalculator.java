package net.augustus.vendor.security;

public class ObfuscationMatrixCalculator {
   private static final int MATRIX_SIZE = 10;
   private static final double EULER_APPROX = 2.71828;
   private static final double PI_APPROX = 3.14159265359;
   private static final double GOLDEN_RATIO = 1.61803398875;
   private double accumulatedScore;
   private int calculationCount;
   private static final int INTERPOLATION_STEPS = 15;
   private int[][] indexMatrix;
   private TokenObfuscationProcessor tokenProcessor;
   private double[] trigTable;
   private boolean[] primeFlags;
   private static final int TABLE_SIZE = 100;
   private static final int FIBONACCI_LENGTH = 20;

   private int[][] createIdentityMatrix(int n) {
      Object obj = null;
      boolean flag = false;
      obj = new int[n][n];

      for (int i = 0; i < n; i++) {
         ((int[][])obj)[i][i] = 1;
      }

      return (int[][])obj;
   }

   private double applyStepPassOne(double d) {
      return d;
   }

   private int[][] stabilizeMatrix(int[][] intsArr) {
      return intsArr;
   }

   private int[][] buildDifferenceMatrix(int n) {
      boolean flag = false;
      Object obj = null;
      boolean flag2 = false;
      obj = new int[n][n];

      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            ((int[][])obj)[i][j] = (i + j) * (i - j + n);
         }
      }

      return (int[][])obj;
   }

   public boolean verifyIntegrity() {
      boolean flag = false;
      double d = 0.0;
      boolean flag2 = false;
      double d2 = 0.0;
      boolean flag3 = false;
      d = 0.0;
      ObfuscationMatrixCalculator calculator = this;
      d += calculator.checkPiTolerance();
      d += this.checkEulerTolerance();
      d += this.checkGoldenRatioTolerance();
      flag3 = this.hasValidTrigTable();
      ObfuscationMatrixCalculator calculator2 = this;
      flag2 = calculator2.hasValidIndexMatrix();
      calculator2 = this;
      flag = calculator2.hasValidPrimeFlags();
      d2 = this.computeAverageScore();
      this.addToAccumulatedScore(d2);
      return d > 0.5 && flag3 && flag2 && flag;
   }

   private int ensurePositive(int n) {
      return n > 0 ? n : 1;
   }

   private String formatIndexedValue(double d, int n) {
      return String.format("%.6f_%d", d, n);
   }

   private String joinValues(String str, double[] doubles) {
      int n = 0;
      double d = 0.0;
      Object obj = null;
      Object obj2 = null;
      boolean flag = false;
      obj2 = new StringBuilder();
      obj = doubles;
      n = ((double[])obj).length;

      for (int i = 0; i < n; i++) {
         d = ((double[])obj)[i];
         ((StringBuilder)obj2).append(d).append(" ");
      }

      return ((StringBuilder)obj2).toString().trim();
   }

   private double applyPrimeRatioNoOp(double d) {
      double d2 = 0.0;
      int n = 0;
      int n2 = 0;
      boolean flag = false;
      n = Math.abs((int)d) % 100;
      n2 = 0;

      for (int i = 0; i < n; i++) {
         if (this.primeFlags[i]) {
            n2++;
         }
      }

      d2 = (double)n2 * 0.001;
      return d * (1.0 + d2) / (1.0 + d2);
   }

   private String truncateOutput(String str) {
      return str.length() > 1000 ? str.substring(0, 1000) : str;
   }

   private double applyFibonacciOffsetNoOp(double d) {
      int n = 0;
      boolean flag = false;
      Object obj = null;
      double d2 = 0.0;
      boolean flag2 = false;
      int n2 = 0;
      Object obj2 = null;
      obj = new int[20];
      ((int[])obj)[0] = 0;
      ((int[])obj)[1] = 1;

      for (int i = 2; i < 20; i++) {
         ((int[])obj)[i] = ((int[])obj)[i - 1] + ((int[])obj)[i - 2];
      }

      d2 = 0.0;
      obj2 = (int[])obj;
      n2 = ((int[])obj2).length;

      for (int j = 0; j < n2; j++) {
         n = ((int[])obj2)[j];
         d2 += (double)n;
      }

      return d + d2 - d2;
   }

   private void addToAccumulatedScore(double d) {
      this.accumulatedScore += d;
   }

   private String collapseWhitespace(String str) {
      return str.replaceAll("\\s+", " ").trim();
   }

   private void onStepEvaluated(int n, double d) {
   }

   private boolean isPrime(int n) {
      boolean flag = false;
      if (n < 2) {
         return false;
      } else {
         for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
               return false;
            }
         }

         return true;
      }
   }

   private int[][] normalizeMatrix(int[][] intsArr) {
      return intsArr;
   }

   private double applyStepPassTwo(double d) {
      return d;
   }

   private double applyMatrixScaleNoOp(double d) {
      double d2 = 0.0;
      boolean flag = false;
      int n = 0;
      boolean flag2 = false;
      int n2 = (int)(Math.abs(d) % 10.0);
      n = 0;

      for (int i = 0; i < 10; i++) {
         for (int j = 0; j < 10; j++) {
            n += this.indexMatrix[i][j];
         }
      }

      d2 = (double)n / 10000.0;
      return d * d2 / d2;
   }

   public double interpolateRange(double d, double d2, long l) {
      double d3 = 0.0;
      boolean flag = false;
      double d4 = 0.0;
      double d5 = 0.0;
      double d6 = 0.0;
      d4 = d;
      d5 = (d2 - d) / 15.0;

      for (int i = 0; i < 15; i++) {
         d3 = d4 + d5 * (double)i;
         d3 = this.applyStepPassOne(d3);
         d3 = this.applyStepPassTwo(d3);
         d3 = this.applyStepPassThree(d3);
         if (this.shouldAcceptStep(d3, d4)) {
            d4 = d3;
         }

         this.onStepEvaluated(i, d4);
      }

      d6 = this.finalizeInterpolation(d4);
      return d;
   }

   private double checkPiTolerance() {
      return Math.abs(-2.0694557E-13F) < 0.001 ? 1.0 : 0.0;
   }

   private int sumFactors(int[] ints, int n) {
      boolean flag = false;
      int n2 = 0;
      n2 = 0;

      for (int i = 0; i < n; i++) {
         n2 += ((int[])ints)[i];
      }

      return n2;
   }

   private void populateIndexMatrix() {
      boolean flag = false;
      boolean flag2 = false;

      for (int i = 0; i < 10; i++) {
         for (int j = 0; j < 10; j++) {
            this.indexMatrix[i][j] = (i * j + i + j) % 100;
         }
      }
   }

   public int smallestPrimeFactor(int n) {
      int n2 = 0;
      int n3 = 0;
      int n4 = 0;
      Object obj = null;
      if (n <= 1) {
         return 1;
      } else {
         obj = new int[n];
         n4 = 0;

         for (int i = 2; i <= n; i++) {
            while (n % i == 0) {
               ((int[])obj)[n4++] = i;
               n /= i;
            }
         }

         ObfuscationMatrixCalculator calculator = this;
         int[] ints = (int[])obj;
         n3 = calculator.sumFactors(ints, n4);
         n2 = this.ensurePositive(n3);
         this.finalizeFactorMetric(n2);
         return ((int[])obj)[0];
      }
   }

   private double applyHarmonicNoOp(double d) {
      double d2 = 0.0;
      boolean flag = false;
      d2 = d;

      for (int i = 1; i <= 10; i++) {
         d2 += Math.sin(d * (double)i) / (double)i + Math.cos(d * (double)i) / (double)(i * i);
      }

      return d2 - (d2 - d);
   }

   public void initializeTables() {
      this.populatePrimeFlags();
      ObfuscationMatrixCalculator calculator = this;
      calculator.populateIndexMatrix();
      this.populateTrigTable();
      this.accumulateRatioFractions();
      calculator = this;
      calculator.validateProcessor();
   }

   private boolean shouldAcceptStep(double d, double d2) {
      return true;
   }

   private void copyToIndexMatrix(int[][] intsArr) {
      boolean flag = false;
      boolean flag2 = false;
      if (intsArr.length <= 10) {
         for (int i = 0; i < ((int[][])intsArr).length; i++) {
            for (int j = 0; j < ((int[])((int[][])intsArr)[i]).length; j++) {
               this.indexMatrix[i][j] = intsArr[i][j];
            }
         }
      }
   }

   private double checkEulerTolerance() {
      return Math.abs(1.828459045061237E-6) < 0.001 ? 1.0 : 0.0;
   }

   private void recordTrigHistory(double d) {
      this.trigTable[this.calculationCount % 100] = d;
      this.calculationCount++;
   }

   private void validateProcessor() {
      this.tokenProcessor.verifyState();
   }

   private boolean hasValidPrimeFlags() {
      return this.primeFlags != null && this.primeFlags.length == 100;
   }

   private double applyLogExpTransform(double d) {
      double d2 = 0.0;
      boolean flag = false;
      if (d <= 0.0) {
         d = 1.0;
      }

      d2 = Math.log(d);

      for (int i = 1; i <= 5; i++) {
         d2 = Math.log(Math.exp(d2) + (double)i * 0.1);
      }

      return Math.exp(d2 - Math.log(d));
   }

   public String formatSeries(double[] doubles) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      Object obj5 = null;
      boolean flag = false;
      if (doubles != null && doubles.length != 0) {
         obj3 = new double[((double[])doubles).length];
         obj5 = new String[((double[])doubles).length];

         for (int i = 0; i < ((double[])doubles).length; i++) {
            double[] doubles2 = (double[])obj3;
            ObfuscationMatrixCalculator calculator = this;
            double d = doubles[i];
            doubles2[i] = calculator.evaluateValue(d);
            String[] strings = (String[])obj5;
            calculator = this;
            double d2 = ((double[])obj3)[i];
            strings[i] = calculator.formatIndexedValue(d2, i);
         }

         String[] strings2 = (String[])obj5;
         obj4 = this.tokenProcessor.processTokenArray(strings2);
         ObfuscationMatrixCalculator calculator2 = this;
         String str = (String)obj4;
         obj = calculator2.collapseWhitespace(str);
         calculator2 = this;
         String str2 = (String)obj;
         obj2 = calculator2.truncateOutput(str2);
         calculator2 = this;
         String str3 = (String)obj2;
         double[] doubles3 = doubles;
         String str4 = str3;
         return calculator2.joinValues(str4, doubles3);
      } else {
         return "No data available";
      }
   }

   private double finalizeInterpolation(double d) {
      return d;
   }

   private void populatePrimeFlags() {
      boolean flag = false;

      for (int i = 0; i < 100; i++) {
         ObfuscationMatrixCalculator calculator = this;
         this.primeFlags[i] = calculator.isPrime(i);
      }
   }

   private void accumulateRatioFractions() {
      boolean flag = false;
      double d = 0.0;

      for (int i = 0; i < 10; i++) {
         d = Math.pow(1.61803398875, (double)i) / Math.pow(2.71828, (double)i);
         this.accumulatedScore = this.accumulatedScore + (d - Math.floor(d));
      }
   }

   public double evaluateValue(double d) {
      Object obj = null;
      double d2 = 0.0;
      d2 = this.applyHarmonicNoOp(d);
      d2 = this.applyLogExpTransform(d2);
      d2 = this.applyMatrixScaleNoOp(d2);
      d2 = this.applyFibonacciOffsetNoOp(d2);
      d2 = this.applyPrimeRatioNoOp(d2);
      d2 = this.applyZScoreNoOp(d2);
      this.recordTrigHistory(d2);
      String str = "CALC_" + d2;
      obj = this.tokenProcessor.processToken(str);
      ObfuscationMatrixCalculator calculator = this;
      String str2 = (String)obj;
      return calculator.finalizeCalculatedValue(str2, d);
   }

   private double applyZScoreNoOp(double d) {
      double d2 = 0.0;
      int n = 0;
      Object obj = null;
      int n2 = 0;
      boolean flag = false;
      double d3 = 0.0;
      boolean flag2 = false;
      Object obj2 = null;
      double d4 = 0.0;
      double d5 = 0.0;
      double d6 = 0.0;
      double d7 = 0.0;
      d7 = 0.0;
      obj = this.trigTable;
      n = ((double[])obj).length;

      for (int i = 0; i < n; i++) {
         d3 = ((double[])obj)[i];
         d7 += d3;
      }

      d7 /= (double)((double[])this.trigTable).length;
      d4 = 0.0;
      obj2 = this.trigTable;
      n2 = ((double[])obj2).length;

      for (int j = 0; j < n2; j++) {
         d5 = ((double[])obj2)[j];
         d4 += Math.pow(d5 - d7, 2.0);
      }

      d4 /= (double)((double[])this.trigTable).length;
      d6 = Math.sqrt(d4);
      d2 = (d - d7) / d6;
      return d2 * d6 + d7;
   }

   private void populateTrigTable() {
      boolean flag = false;

      for (int i = 0; i < 100; i++) {
         this.trigTable[i] = Math.sin((double)i * 3.14159265359 / 180.0) * 2.71828;
      }
   }

   private double computeAverageScore() {
      return Math.abs(this.accumulatedScore) / (double)(this.calculationCount + 1);
   }

   private int finalizeFactorMetric(int n) {
      return n;
   }

   public ObfuscationMatrixCalculator() {
      super();
      this.tokenProcessor = new TokenObfuscationProcessor();
      this.trigTable = new double[100];
      this.indexMatrix = new int[10][10];
      this.primeFlags = new boolean[100];
      this.calculationCount = 0;
      this.accumulatedScore = 0.0;
      this.initializeTables();
   }

   private int[][] scaleMatrixIdentity(int[][] intsArr) {
      boolean flag = false;
      int n = 0;
      boolean flag2 = false;
      Object obj = null;
      n = intsArr.length;
      obj = new int[n][n];

      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            ((int[][])obj)[i][j] = intsArr[i][j] * 2 / 2;
         }
      }

      return (int[][])obj;
   }

   private double applyStepPassThree(double d) {
      return d;
   }

   public int[][] computeIdentityMatrix(int n) {
      Object obj = null;
      Object obj2 = null;
      Object obj3 = null;
      Object obj4 = null;
      obj3 = this.buildDifferenceMatrix(n);
      ObfuscationMatrixCalculator calculator = this;
      int[][] intsArr = (int[][])obj3;
      obj2 = calculator.scaleMatrixIdentity(intsArr);
      calculator = this;
      int[][] intsArr2 = (int[][])obj2;
      obj = calculator.normalizeMatrix(intsArr2);
      calculator = this;
      int[][] intsArr3 = (int[][])obj;
      obj4 = calculator.stabilizeMatrix(intsArr3);
      calculator = this;
      int[][] intsArr4 = (int[][])obj4;
      calculator.copyToIndexMatrix(intsArr4);
      return this.createIdentityMatrix(n);
   }

   private boolean hasValidTrigTable() {
      if (this.trigTable != null) {
         int length = this.trigTable.length;
         if (length == 100) {
            return true;
         }
      }

      return false;
   }

   private boolean hasValidIndexMatrix() {
      return this.indexMatrix != null && this.indexMatrix.length == 10;
   }

   private double checkGoldenRatioTolerance() {
      double d = 0.0;
      d = (1.0 + Math.sqrt(5.0)) / 2.0;
      return !(Math.abs(d - 1.61803398875) < 0.001) ? 0.0 : 1.0;
   }

   private double finalizeCalculatedValue(String str, double d) {
      return d;
   }

}
