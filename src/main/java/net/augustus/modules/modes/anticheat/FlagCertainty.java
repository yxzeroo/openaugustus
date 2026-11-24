package net.augustus.modules.modes.anticheat;

import java.util.ArrayList;
import java.util.List;
import net.augustus.modules.ModuleMode;

public enum FlagCertainty {
   LIKELY("likely", "?", 80),
   CERTAIN("certain", "!!", 960),
   MAYBE("maybe", "-", 10),
   ALMOST_CERTAIN("almost certain", "!", 320),
   NONE("none", "-", 0),
   PROBABLE("probable", "??", 40),
   VERY_LIKELY("very likely", "?!", 160);

   private final String label;
   private final String symbol;
   private final int weight;

   public String getLabel() {
      return this.label;
   }

   public static int sumWeights(FlagCertainty[] flagCertaintys, long l) {
      int n = 0;

      for (FlagCertainty flagCertainty : flagCertaintys) {
         n += flagCertainty.getWeight();
      }

      return n;
   }

   private FlagCertainty(String str, String str2, int n) {
      this.label = str;
      this.symbol = str2;
      this.weight = n;
   }

   public static FlagCertainty fromWeight(int n) {
      FlagCertainty flagCertainty = NONE;

      for (FlagCertainty flagCertainty2 : values()) {
         if (flagCertainty2.weight > flagCertainty.weight && flagCertainty2.weight <= n) {
            flagCertainty = flagCertainty2;
         }
      }

      return flagCertainty;
   }

   public static List decomposeWeight(int n) {
      ArrayList list = new ArrayList();

      while (n > MAYBE.getWeight()) {
         FlagCertainty flagCertainty = fromWeight(n);
         list.add(flagCertainty);
         n -= flagCertainty.getWeight();
      }

      return list;
   }

   public boolean isAtLeast(FlagCertainty flagCertainty, int n, int n2, short s) {
      return this.getWeight() >= flagCertainty.getWeight();
   }

   public int getWeight() {
      return this.weight;
   }

   public String getSymbol() {
      return this.symbol;
   }
}
