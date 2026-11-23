package net.augustus.vendor.launcher.crypto.keccak;

public class KeccakPermutation {
   private final KeccakChi chi;
   private final KeccakRho rho;
   private final KeccakIota iota;
   private final KeccakPi pi;
   private final KeccakTheta theta = new KeccakTheta();
   private static final long b = 2681178886951141400L;

   public void permute(long[][] longsArr) {

      for (int i = 0; i < (int)b; i++) {
         this.theta.apply(longsArr);
         this.rho.apply(longsArr);
         this.pi.apply(longsArr);
         this.chi.apply(longsArr);
         this.iota.apply(longsArr, i);
      }
   }

   public KeccakPermutation() {
      this.iota = new KeccakIota();
      this.chi = new KeccakChi();
      this.pi = new KeccakPi();
      this.rho = new KeccakRho();
   }

}
