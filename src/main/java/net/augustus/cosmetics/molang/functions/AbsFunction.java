package net.augustus.cosmetics.molang.functions;


import net.augustus.cosmetics.molang.ast.MolangValue;
public class AbsFunction extends MolangFunction {

   @Override
   public double get() {
      byte b = 0;
      return Math.abs(this.getArg(b));
   }

   public AbsFunction(MolangValue[] molangValues, String str) throws Exception {
       super(molangValues, str);
   }

   @Override
   public int getRequiredArguments() {
      return 1;
   }
}
