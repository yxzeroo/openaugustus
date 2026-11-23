package net.augustus.cosmetics.molang.functions;

import net.augustus.cosmetics.molang.ast.MolangValue;
import java.util.Random;

public class RandomFunction extends MolangFunction {
   public Random random;

   public double get() {
      double d = 0.0;
      if (this.args.length >= 3) {
         byte b = 2;
         this.random.setSeed((long)this.getArg(b));
         d = this.random.nextDouble();
      } else {
         d = Math.random();
      }

      if (this.args.length >= 2) {
         byte b2 = 0;
         double arg = this.getArg(b2);
         byte b3 = 1;
         double arg2 = this.getArg(b3);
         double d2 = Math.min(arg, arg2);
         double d3 = Math.max(arg, arg2);
         d = d * (d3 - d2) + d2;
      } else if (this.args.length >= 1) {
         byte b4 = 0;
         d *= this.getArg(b4);
      }

      return d;
   }

   public RandomFunction(MolangValue[] molangValues, String str) throws Exception {
       super(molangValues, str);
      this.random = new Random();
   }




}
