package net.augustus.cosmetics.molang.functions;


import net.augustus.cosmetics.molang.ast.MolangValue;
public class SinDegreesFunction extends MolangFunction {

   @Override
   public double get() {
      byte b = 0;
      return Math.sin(this.getArg(b) / 180.0 * Math.PI);
   }

   @Override
   public int getRequiredArguments() {
      return 1;
   }

   public SinDegreesFunction(MolangValue[] molangValues, String str) throws Exception {
       super(molangValues, str);
   }
}
