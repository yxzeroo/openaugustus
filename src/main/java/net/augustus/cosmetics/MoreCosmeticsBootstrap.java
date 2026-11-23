package net.augustus.cosmetics;

public class MoreCosmeticsBootstrap {

   public static MoreCosmeticsCore initialize(int n) {
      return MoreCosmeticsCore.getInstance(n);
   }

   public static void tick() {
      MoreCosmeticsCore instance = MoreCosmeticsCore.getInstance();
      instance.onClientTick();
   }
}
