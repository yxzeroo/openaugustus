package net.augustus.cosmetics.molang.functions;


import net.augustus.cosmetics.molang.ast.MolangValue;
public class ExpFunction extends MolangFunction {

   public ExpFunction(MolangValue[] molangValues, String str) throws Exception {
       super(molangValues, str);
   }

   @Override
   public int getRequiredArguments() {
      return 1;
   }

   @Override
   public double get() {
      byte b = 0;
      return Math.exp(this.getArg(b));
   }
}
