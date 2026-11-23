package net.augustus.cosmetics.molang.functions;


import net.augustus.cosmetics.molang.ast.MolangValue;
public class ModFunction extends MolangFunction {

   @Override
   public int getRequiredArguments() {
      return 2;
   }

   @Override
   public double get() {
      byte b = 0;
      double arg = this.getArg(b);
      byte b2 = 1;
      return arg % this.getArg(b2);
   }

   public ModFunction(MolangValue[] molangValues, String str) throws Exception {
       super(molangValues, str);
   }
}
