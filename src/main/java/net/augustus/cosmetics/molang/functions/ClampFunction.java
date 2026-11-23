package net.augustus.cosmetics.molang.functions;


import net.augustus.cosmetics.molang.MolangMathUtil;
import net.augustus.cosmetics.molang.ast.MolangValue;
public class ClampFunction extends MolangFunction {

   public ClampFunction(MolangValue[] molangValues, String str) throws Exception {
       super(molangValues, str);
   }

   public double get() {
      byte b = 0;
      double arg = this.getArg(b);
      byte b2 = 1;
      double arg2 = this.getArg(b2);
      byte b3 = 2;
      return MolangMathUtil.clamp(arg, arg2, this.getArg(b3));
   }

   @Override
   public int getRequiredArguments() {
      return 3;
   }
}
