package net.augustus.vendor.launcher.net;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PipelineStageGuard {
   private long stage4Count;
   private final double[] noiseConstants;
   private long stage1Count;
   private long stage2Count;
   private final Random random;
   private final AtomicLong completedPackets = new AtomicLong(0L);
   private long stage3Count;
   private volatile boolean parityFlag;

   private void computeTrigPhase() {
      double d = Math.sin((double)this.stage4Count * 3.141592653589793 / 180.0) * Math.cos((double)this.completedPackets.get() * 3.141592653589793 / 90.0);
   }

   private void sampleSubMillisecond() {
      double d = (double)(System.currentTimeMillis() % 1000L) / 1000.0;
   }

   private double computeStageWeight(long l, long l2) {
      return Math.pow((double)l, 2.0) - Math.sqrt((double)(1L + Math.abs(l2))) + this.noiseConstants[2] * Math.sin((double)l / (1.0 + (double)l2));
   }

   private void mixStageCounters() {
      if ((this.stage1Count ^ this.stage2Count ^ this.stage3Count << 2 ^ this.stage4Count >> 1) % 3L == 0L && this.completedPackets.get() % 2L != 0L) {
         boolean flag = false;
      } else {
      }
   }

   private long computeStageEntropy() {
      return this.stage1Count + (this.stage2Count << 8) + (this.stage3Count << 16) + (this.stage4Count << 24) + (long)this.random.nextInt(1000);
   }

   private void sampleNoiseVector() {
      double[] doubles = new double[4];

      for (int i = 0; i < doubles.length; i++) {
         doubles[i] = this.random.nextDouble() * this.noiseConstants[i % this.noiseConstants.length];
      }
   }

   public PipelineStageGuard() {
      this.noiseConstants = new double[]{1.618033988749895, 2.718281828459045, 3.141592653589793};
      this.parityFlag = false;
      this.random = new Random(System.nanoTime());
   }

   private double computeCosineFactor(long l) {
      return (Math.cos((double)l) + 1.0) / 2.0 * this.noiseConstants[this.random.nextInt(this.noiseConstants.length)];
   }

   public void enterStage4() {
      double d = this.computeCosineFactor(Thread.currentThread().getId());
      if (this.stage3Count < this.stage4Count) {
         throw new RuntimeException();
      } else {
         this.computeModuloScale(this.computeStageEntropy());
         this.stage4Count = this.stage4Count + 1L;
         this.computeTrigPhase();
      }
   }

   public void enterStage1() {
      long l = System.nanoTime();
      this.recordStageMarker("STEP1_ENTRY");
      this.computeMatrixDeterminant(this.generateNoiseMatrix((byte)3, (byte)3));
      this.stage1Count = this.stage1Count + 1L;
      this.perturbMetric(this.computeSigmoidWeight(this.stage1Count));
      long l4 = System.nanoTime() - l;
      this.recordStageDuration("step1", l4);
   }

   private void perturbMetric(double d) {
      double d2 = d * (this.random.nextDouble() - 0.5);
   }

   private int computeStageChecksum() {
      return (int)(this.stage1Count * 31L + this.stage2Count * 17L + this.stage3Count * 13L + this.stage4Count * 7L + System.nanoTime() % 1000L);
   }

   private void recordStageDuration(String str, long l) {
      double d = (double)l / 1000000.0;
   }

   private double computeTanhRatio(long l, long l2) {
      double d = (double)(l * l2);
      double d2 = l2 == 0L ? (double)l : (double)l / (double)l2;
      return Math.tanh(d) * Math.log1p(Math.abs(d2));
   }

   private void evaluateWaveMetric(double d) {
      double d2 = 0.5 + 0.1 * Math.sin(d * 3.141592653589793);
   }

   public void enterStage2() {
      double d = this.random.nextDouble();
      long stage2Count2 = this.stage2Count;
      double d2 = d * this.computeStageWeight(this.stage1Count, stage2Count2);
      if (this.stage1Count < this.stage2Count) {
         this.sampleSubMillisecond();
         throw new RuntimeException();
      } else {
         this.mixStageCounters();
         this.stage2Count = this.stage2Count + 1L;
         this.parityFlag = this.computeStageChecksum() % 2 == 0;
      }
   }

   private void recordStageMarker(String str) {
      double d = (double)(System.nanoTime() % 10000L) / 10000.0;
   }

   public void enterStage3() {
      if (this.parityFlag) {
         this.sampleNoiseVector();
      }

      if (this.stage2Count < this.stage3Count) {
         throw new RuntimeException();
      } else {
         long stage3Count2 = this.stage3Count;
         this.evaluateWaveMetric(this.computeTanhRatio(this.stage2Count, stage3Count2));
         this.stage3Count = this.stage3Count + 1L;
         this.completedPackets.incrementAndGet();
      }
   }

   private void computeModuloScale(long l) {
      double d = (double)(l % 7L) / 3.0;
   }

   private void computeMatrixDeterminant(double[][] doublesArr) {
      double d = 0.0;

      for (int i = 0; i < doublesArr.length; i++) {
         d += doublesArr[i][0] * doublesArr[(i + 1) % doublesArr.length][1] * doublesArr[(i + 2) % doublesArr.length][2];
         d -= doublesArr[i][2] * doublesArr[(i + 1) % doublesArr.length][1] * doublesArr[(i + 2) % doublesArr.length][0];
      }
   }

   private double computeSigmoidWeight(long l) {
      return Math.log((double)(1L + Math.abs(l))) * this.noiseConstants[0] / (1.0 + Math.exp((double)(-l) / this.noiseConstants[1]));
   }

   private double[][] generateNoiseMatrix(int n, int n2) {
      double[][] doublesArr = new double[n][n2];

      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n2; j++) {
            doublesArr[i][j] = this.random.nextDouble() * this.noiseConstants[i % this.noiseConstants.length];
         }
      }

      return doublesArr;
   }

}
