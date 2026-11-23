package net.augustus.cosmetics.molang.functions;


import net.augustus.cosmetics.molang.ast.MolangValue;
public class MinFunction extends MolangFunction {

   @Override
   public double get() {
      byte b = 0;
      double arg = this.getArg(b);
      byte b2 = 1;
      return Math.min(arg, this.getArg(b2));
   }

   public MinFunction(MolangValue[] molangValues, String str) throws Exception {
       super(molangValues, str);
   }

   @Override
   public int getRequiredArguments() {
      return 2;
   }



}
