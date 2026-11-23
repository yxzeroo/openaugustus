package net.augustus.utils.prediction;

import java.util.Arrays;

public class KalmanFilter2D {
   private double[][] processNoise;
   private double[][] covariance;
   private final int measurementDim;
   private final int stateDim;
   private final double[][] measurementNoise;
   private double[] state;

   public void update(double[] doubles) {
      double[][] doublesArr = new double[][]{{1.0, 0.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 0.0, 0.0, 1.0, 0.0, 0.0}};
      double[] doubles2 = new double[]{this.state[0], this.state[3]};
      double[] doubles3 = DoubleMatrixMath.subtractVectors(doubles, doubles2);
      double[][] doublesArr2 = DoubleMatrixMath.transpose(doublesArr);
      double[][] doublesArr3 = DoubleMatrixMath.multiply(this.covariance, doublesArr2);
      double[][] doublesArr4 = DoubleMatrixMath.multiply(doublesArr, doublesArr3);
      double[][] measurementNoise2 = this.measurementNoise;
      double[][] doublesArr5 = DoubleMatrixMath.add(doublesArr4, measurementNoise2);
      double[][] doublesArr6 = DoubleMatrixMath.invert2x2(doublesArr5);
      doublesArr4 = this.covariance;
      double[][] doublesArr7 = DoubleMatrixMath.multiply(doublesArr2, doublesArr6);
      double[][] doublesArr8 = DoubleMatrixMath.multiply(doublesArr4, doublesArr7);
      double[] state2 = this.state;
      double[] doubles4 = DoubleMatrixMath.multiplyMatrixVector(doublesArr8, doubles3);
      this.state = DoubleMatrixMath.addVectors(state2, doubles4);
      double[][] doublesArr9 = DoubleMatrixMath.diagonal(6, 1.0);
      double[][] doublesArr10 = DoubleMatrixMath.multiply(doublesArr8, doublesArr);
      double[][] doublesArr11 = DoubleMatrixMath.subtract(doublesArr9, doublesArr10);
      double[][] covariance2 = this.covariance;
      this.covariance = DoubleMatrixMath.multiply(doublesArr11, covariance2);
   }

   public void setState(double[] doubles) {
      this.state = Arrays.copyOf(doubles, 6);
   }

   public KalmanFilter2D() {
       super();
      this.stateDim = 6;
      this.measurementDim = 2;
      this.state = new double[6];
      this.covariance = DoubleMatrixMath.diagonal(6, 1.0);
      this.processNoise = new double[6][6];
      this.processNoise[0][0] = 0.2;
      this.processNoise[1][1] = 0.05;
      this.processNoise[2][2] = 0.01;
      this.processNoise[3][3] = 0.2;
      this.processNoise[4][4] = 0.05;
      this.processNoise[5][5] = 0.01;
      this.measurementNoise = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
   }

   public double[] predict(double d) {
      double d2 = d * d;
      double[][] doublesArr = new double[][]{{1.0, d, 0.5 * d2}, {0.0, 1.0, d}, {0.0, 0.0, 1.0}};
      double[][] doublesArr2 = new double[6][6];

      for (int i = 0; i < 3; i++) {
         for (int j = 0; j < 3; j++) {
            doublesArr2[i][j] = doublesArr[i][j];
            doublesArr2[i + 3][j + 3] = doublesArr[i][j];
         }
      }

      double[] state2 = this.state;
      this.state = DoubleMatrixMath.multiplyMatrixVector(doublesArr2, state2);
      double[][] doublesArr3 = DoubleMatrixMath.transpose(doublesArr2);
      double[][] doublesArr4 = DoubleMatrixMath.multiply(this.covariance, doublesArr3);
      double[][] doublesArr5 = DoubleMatrixMath.multiply(doublesArr2, doublesArr4);
      double[][] processNoise2 = this.processNoise;
      this.covariance = DoubleMatrixMath.add(doublesArr5, processNoise2);
      return new double[]{this.state[0], this.state[3]};
   }

   public double[] getState() {
      return this.state;
   }
}
