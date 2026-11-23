package net.augustus.cosmetics.molang.functions;


import net.augustus.cosmetics.molang.ast.MolangValue;
public class SqrtFunction extends MolangFunction {

   @Override
   public double get() {
      byte b = 0;
      return Math.sqrt(this.getArg(b));
   }

   @Override
   public int getRequiredArguments() {
      return 1;
   }

   public SqrtFunction(MolangValue[] molangValues, String str) throws Exception {
       super(molangValues, str);
   }
}
