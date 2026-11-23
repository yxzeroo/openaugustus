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

      for (int i = 0; i < (int)e; i++) {
         this.theta.applyTheta(longsArr);
         this.rho.applyRho(longsArr);
         this.pi.applyPi(longsArr);
         this.chi.applyChi(longsArr);
         this.iota.applyIota(longsArr, i);
      }
   }

   public KeccakPermutation() {
      this.iota = new KeccakIotaStep();
      this.chi = new KeccakChi();
      this.pi = new KeccakPi();
      this.rho = new KeccakRho();
   }

}
