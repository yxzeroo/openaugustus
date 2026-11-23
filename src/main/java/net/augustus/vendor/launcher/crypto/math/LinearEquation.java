package net.augustus.vendor.launcher.crypto.math;

import java.util.ArrayList;

public class LinearEquation {
   private RationalFraction constantTerm;
   private ArrayList<RationalFraction> coefficients;

   public RationalFraction getConstantTerm(long l) {
      return this.constantTerm;
   }

   public ArrayList getCoefficients(long l) {
      return this.coefficients;
   }

   public void addCoefficient(short s, RationalFraction rationalFraction, char ch, int n) {
      this.coefficients.add(rationalFraction);
   }

   public void setConstantTerm(long l, RationalFraction rationalFraction) {
      this.constantTerm = rationalFraction;
   }

   public LinearEquation(RationalFraction rationalFraction, long l) {
      ArrayList list = new ArrayList();
      this.coefficients = list;
      this.constantTerm = rationalFraction;
   }
}
