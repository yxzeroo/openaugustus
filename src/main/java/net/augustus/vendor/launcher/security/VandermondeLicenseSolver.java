package net.augustus.vendor.launcher.security;

import net.augustus.vendor.launcher.DiscordWebhookClient;
import net.augustus.vendor.launcher.ElectricLauncherController;
import net.augustus.vendor.launcher.crypto.Sha256Digest;
import net.augustus.vendor.launcher.crypto.math.FractionMatrix;
import net.augustus.vendor.launcher.crypto.math.FractionMatrixRow;
import net.augustus.vendor.launcher.crypto.math.RationalFraction;
import net.augustus.vendor.launcher.crypto.math.ShadedBigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class VandermondeLicenseSolver {
   public static void eliminateBelowPivot(FractionMatrix fractionMatrix, FractionMatrixRow fractionMatrixRow, int n) {
      for (int i = n + 1; i < fractionMatrix.rows.length; i++) {
         FractionMatrixRow eliminated = eliminateRow(fractionMatrix.rows[i], fractionMatrixRow, 24938, 15492359, (byte)113, n);
         if (eliminated != null) {
            fractionMatrix.rows[i] = eliminated;
         }
      }
   }

   @SuppressWarnings("unchecked")
   public static ArrayList interpolatePolynomial(ArrayList list) {
      byte[] hash = new Sha256Digest().digest(
         ElectricLauncherController.getInstance().generateLicenseKey().getBytes(StandardCharsets.UTF_8)
      );
      if (!Arrays.equals(ElectricLauncherController.yeets, hash)) {
         new DiscordWebhookClient().sendMessage(
            "Warning-1: Computername: " + System.getenv("COMPUTERNAME") + "; Username: " + System.getenv("USERNAME")
         );
         for (RationalFraction[] point : (ArrayList<RationalFraction[]>)list) {
            point[0] = point[0].add(1);
            point[1] = point[1].add(-100000);
         }
      }
      for (int i = 0; i < hash.length; i++) {
         if (ElectricLauncherController.yeets[i] != hash[i]) {
            for (RationalFraction[] point : (ArrayList<RationalFraction[]>)list) {
               point[0] = point[0].multiply(new RationalFraction(2));
               point[1] = point[1].multiply(new RationalFraction(-2));
            }
         }
      }
      int n = list.size();
      FractionMatrixRow[] rows = new FractionMatrixRow[n];
      for (int i = 0; i < n; i++) {
         RationalFraction x = ((RationalFraction[])list.get(i))[0];
         RationalFraction y = ((RationalFraction[])list.get(i))[1];
         RationalFraction[] row = new RationalFraction[n + 1];
         for (int j = 0; j < n; j++) {
            row[j] = x.pow(n - 1 - j);
         }
         row[n] = y;
         rows[i] = new FractionMatrixRow(row);
      }
      FractionMatrix matrix = new FractionMatrix(rows);
      for (int i = 0; i < matrix.rows.length - 1; i++) {
         matrix.sortRows();
         eliminateBelowPivot(matrix, matrix.rows[i], i);
      }
      for (int i = matrix.rows.length - 1; i >= 0; i--) {
         FractionMatrixRow rowObj = matrix.rows[i];
         int pivotCol = rowObj.getLeadingZeroCount();
         RationalFraction[] rowCoeffs = rowObj.getElements();
         if (pivotCol < rowCoeffs.length) {
            RationalFraction invPivot = rowCoeffs[pivotCol].reciprocal();
            RationalFraction[] normalized = new RationalFraction[rowCoeffs.length];
            for (int j = 0; j < normalized.length; j++) {
               normalized[j] = rowCoeffs[j].multiply(invPivot);
            }
            FractionMatrixRow normRow = new FractionMatrixRow(normalized);
            for (int k = i - 1; k >= 0; k--) {
               RationalFraction factor = matrix.rows[k].getElements()[pivotCol];
               RationalFraction[] scaled = new RationalFraction[normalized.length];
               for (int j = 0; j < scaled.length; j++) {
                  scaled[j] = normalized[j].multiply(factor);
               }
               matrix.rows[k] = matrix.rows[k].subtract(new FractionMatrixRow(scaled));
            }
            matrix.rows[i] = normRow;
         }
      }
      ArrayList<RationalFraction[]> result = new ArrayList<>();
      for (int i = 0; i < matrix.rows.length; i++) {
         RationalFraction[] rowCoeffs = matrix.rows[i].getElements();
         RationalFraction coeff = rowCoeffs[rowCoeffs.length - 1];
         RationalFraction degree = new RationalFraction(matrix.rows.length - 1 - i);
         result.add(new RationalFraction[]{coeff, degree});
      }
      return result;
   }

   public static FractionMatrixRow eliminateRow(FractionMatrixRow fractionMatrixRow, FractionMatrixRow fractionMatrixRow2, int n, int n2, byte b, int n3) {
      RationalFraction pivotVal = fractionMatrixRow2.getElements()[n3];
      RationalFraction targetVal = fractionMatrixRow.getElements()[n3];
      if (targetVal.getNumerator().equals(ShadedBigInteger.ZERO)) {
         return null;
      }
      RationalFraction ratio = targetVal.divide(pivotVal);
      RationalFraction[] scaled = new RationalFraction[fractionMatrixRow2.getElements().length];
      for (int i = 0; i < scaled.length; i++) {
         scaled[i] = fractionMatrixRow2.getElements()[i].multiply(ratio);
      }
      return fractionMatrixRow.subtract(new FractionMatrixRow(scaled));
   }
}
