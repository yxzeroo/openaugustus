package net.augustus.cosmetics.molang.functions;


import net.augustus.cosmetics.molang.ast.MolangValue;
public class PowFunction extends MolangFunction {

   @Override
   public int getRequiredArguments() {
      return 2;
   }

   @Override
   public double get() {
      byte b = 0;
      double arg = this.getArg(b);
      byte b2 = 1;
      return Math.pow(arg, this.getArg(b2));
   }

   public PowFunction(MolangValue[] molangValues, String str) throws Exception {
       super(molangValues, str);
   }
}
