package net.augustus.cosmetics.molang.functions;


import net.augustus.cosmetics.molang.ast.MolangValue;
public class RoundFunction extends MolangFunction {

   public RoundFunction(MolangValue[] molangValues, String str) throws Exception {
       super(molangValues, str);
   }

   @Override
   public int getRequiredArguments() {
      return 1;
   }

   @Override
   public double get() {
      byte b = 0;
      return (double)Math.round(this.getArg(b));
   }
}
