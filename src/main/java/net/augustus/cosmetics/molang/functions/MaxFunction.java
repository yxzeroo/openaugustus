package net.augustus.cosmetics.molang.functions;


import net.augustus.cosmetics.molang.ast.MolangValue;
public class MaxFunction extends MolangFunction {

   public MaxFunction(MolangValue[] molangValues, String str) throws Exception {
       super(molangValues, str);
   }

   @Override
   public double get() {
      byte b = 0;
      double arg = this.getArg(b);
      byte b2 = 1;
      return Math.max(arg, this.getArg(b2));
   }

   @Override
   public int getRequiredArguments() {
      return 2;
   }
}
