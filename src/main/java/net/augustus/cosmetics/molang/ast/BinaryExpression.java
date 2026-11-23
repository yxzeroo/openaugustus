package net.augustus.cosmetics.molang.ast;


import net.augustus.cosmetics.molang.MolangOperation;
public class BinaryExpression implements MolangValue {
   public MolangOperation operation;
   public MolangValue right;
   public MolangValue left;

   @Override
   public String toString() {
      return this.left.toString() + " " + this.operation.sign + " " + this.right.toString();
   }

   @Override
   public double get() {
      MolangOperation operation2 = this.operation;
      double d = this.left.get();
      double d2 = this.right.get();
      double d3 = d;
      return operation2.calculate(d3, d2);
   }

   public BinaryExpression(MolangOperation molangOperation, MolangValue molangValue, MolangValue molangValue2) {
      this.operation = molangOperation;
      this.left = molangValue;
      this.right = molangValue2;
   }
}
