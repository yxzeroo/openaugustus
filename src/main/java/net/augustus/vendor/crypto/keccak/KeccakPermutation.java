package net.augustus.vendor.crypto.keccak;

public class KeccakPermutation {
   private final KeccakIotaStep iota;
   private final KeccakChi chi;
   private final KeccakRho rho;
   private final KeccakTheta theta = new KeccakTheta();
   private final KeccakPi pi;
   private static final long e = -4777516277683978216L;

   public void permute(long[][] longsArr) {
      boolean flag = false;
      int[] squeezeTrace = KeccakSqueezer.getSqueezeTrace();

      for (int i = 0; i < (int)e; i++) {
         long[][] longsArr2 = longsArr;
         this.theta.applyTheta(longsArr2);
         long[][] longsArr3 = longsArr;
         this.rho.applyRho(longsArr3);
         long[][] longsArr4 = longsArr;
         this.pi.applyPi(longsArr4);
         long[][] longsArr5 = longsArr;
         this.chi.applyChi(longsArr5);
         long[][] longsArr6 = longsArr;
         this.iota.applyIota(longsArr6, i);
      }
   }

   public KeccakPermutation() {
      this.iota = new KeccakIotaStep();
      this.chi = new KeccakChi();
      this.pi = new KeccakPi();
      this.rho = new KeccakRho();
   }

}
