package net.augustus.cosmetics.molang.functions;


import net.augustus.cosmetics.molang.MolangInterpolations;
import net.augustus.cosmetics.molang.ast.MolangValue;
public class LerpRotateFunction extends MolangFunction {

   public LerpRotateFunction(MolangValue[] molangValues, String str) throws Exception {
       super(molangValues, str);
   }

   @Override
   public int getRequiredArguments() {
      return 3;
   }

   public double get() {
      byte b = 0;
      double arg = this.getArg(b);
      byte b2 = 1;
      double arg2 = this.getArg(b2);
      byte b3 = 2;
      return MolangInterpolations.lerpYaw(arg, arg2, this.getArg(b3));
   }
}
