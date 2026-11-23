package net.augustus.settings;

import net.augustus.ClientManagers;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.utils.math.MathStatisticsUtil;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.utils.random.GeneralizedNormalSampler;
import java.util.Random;
import net.augustus.modules.Module;
import net.minecraft.util.MathHelper;

public class RandomizedRangeSettings extends SettingComposite {
   private RangeSetting outlierIntervalRange;
   private StringValue betaSetting;
   private GeneralizedNormalSampler generalizedNormalSampler;
   private int lastIntValue;
   private final Random random;
   private StringValue centerSetting;
   private BooleanValue notInARow;
   private StringValue maxInARowSetting;
   private BooleanValue outliersEnabled;
   private final DoubleValue parentGroup;
   private int consecutiveSameCount;
   private RangeSetting outlierValueRange;
   private final String baseName;
   private BooleanValue normalDistributionEnabled;
   private int samplesSinceOutlier;
   private int decimalPlaces;
   private RangeSetting rangeSetting;
   private boolean notInARowDisabled;
   private int nextOutlierThreshold;
   private final Module parentModule;
   private double lastDoubleValue;
   private StringValue sigmaSetting;

   public double nextDouble(boolean flag) {
      boolean flag2 = false;
      RangeSetting rangeSetting2 = this.rangeSetting;
      double lastDoubleValue2 = this.lastDoubleValue;
      double d = this.sampleDoubleAvoidingRepeat(lastDoubleValue2, rangeSetting2, flag2, flag);
      if (this.shouldTriggerOutlier()) {
         double d2 = Math.max(0.0, this.rangeSetting.getMinValue() - this.outlierValueRange.getMinValue());
         double maxValue = this.outlierValueRange.getMaxValue();
         double d3 = Math.max(0.0, maxValue - this.rangeSetting.getMaxValue());
         double d4 = d2 + d3;
         if (d4 > 0.0) {
            double d5;
            if (d2 == 0.0) {
               double d6 = this.outlierValueRange.getMaxValue(flag) - Math.max(this.outlierValueRange.getMinValue(flag), this.rangeSetting.getMaxValue(flag));
               RandomNumberGenerator instance = DefaultRandomGenerator.instance;
               double d7 = Math.max(0.0, d6);
               double d8 = d7;
               double d9 = 0.0;
               d5 = instance.nextDouble(d9, d8);
            } else if (d3 == 0.0) {
               double d10 = Math.min(this.outlierValueRange.getMaxValue(flag), this.rangeSetting.getMinValue(flag)) - this.outlierValueRange.getMinValue(flag);
               RandomNumberGenerator instance2 = DefaultRandomGenerator.instance;
               double d11 = Math.max(0.0, d10);
               double d12 = d11;
               double d13 = 0.0;
               d5 = instance2.nextDouble(d13, d12);
            } else {
               double d14 = 0.0;
               d5 = DefaultRandomGenerator.instance.nextDouble(d14, d4);
            }

            if (d5 < d2) {
               d = this.outlierValueRange.getMinValue(flag) + d5;
            } else {
               d = this.outlierValueRange.getMaxValue(flag) - d5;
            }
         }
      }

      this.lastDoubleValue = d;
      return d;
   }

   public double nextSecureDouble() {
      boolean flag = false;
      boolean flag2 = true;
      RangeSetting rangeSetting2 = this.rangeSetting;
      double lastDoubleValue2 = this.lastDoubleValue;
      double d = this.sampleDoubleAvoidingRepeat(lastDoubleValue2, rangeSetting2, flag2, flag);
      if (this.shouldTriggerOutlier()) {
         double d2 = Math.max(0.0, this.rangeSetting.getMinValue() - this.outlierValueRange.getMinValue());
         double maxValue = this.outlierValueRange.getMaxValue();
         double d3 = Math.max(0.0, maxValue - this.rangeSetting.getMaxValue());
         double d4 = d2 + d3;
         if (d4 > 0.0) {
            double d5;
            if (d2 == 0.0) {
               double maxValue2 = this.outlierValueRange.getMaxValue();
               maxValue = this.outlierValueRange.getMinValue();
               double d6 = maxValue2 - Math.max(maxValue, this.rangeSetting.getMaxValue());
               RandomNumberGenerator instance = DefaultRandomGenerator.instance;
               double d7 = Math.max(0.0, d6);
               double d8 = 0.0;
               d5 = instance.nextSecureDouble(d8, d7);
            } else if (d3 == 0.0) {
               double d9 = Math.min(this.outlierValueRange.getMaxValue(), this.rangeSetting.getMinValue()) - this.outlierValueRange.getMinValue();
               RandomNumberGenerator instance2 = DefaultRandomGenerator.instance;
               double d10 = Math.max(0.0, d9);
               double d11 = 0.0;
               d5 = instance2.nextSecureDouble(d11, d10);
            } else {
               double d12 = 0.0;
               d5 = DefaultRandomGenerator.instance.nextSecureDouble(d12, d4);
            }

            if (d5 < d2) {
               d = this.outlierValueRange.getMinValue() + d5;
            } else {
               d = this.outlierValueRange.getMaxValue() - d5;
            }
         }
      }

      this.lastDoubleValue = d;
      return d;
   }

   private boolean isBetaSettingVisible() {
      return this.normalDistributionEnabled.getValue();
   }

   public RandomizedRangeSettings withoutNotInARow() {
      if (this.parentGroup != null) {
         this.parentGroup.getChildren().remove(this.notInARow);
      }

      if (this.parentModule != null) {
         ClientManagers.settingManager.getSettings().remove(this.notInARow);
         this.parentModule.getSettings().remove(this.notInARow);
      }

      BooleanValue notInARow2 = this.notInARow;
      SettingVisibilityCondition condition = () -> false;
      notInARow2.withVisibility(condition);
      this.notInARowDisabled = true;
      return this;
   }

   public RandomizedRangeSettings(Module module, String str, double d, double d2, double d3, double d4, int n) {
       super();
      this.outliersEnabled = null;
      this.outlierValueRange = null;
      this.outlierIntervalRange = null;
      this.normalDistributionEnabled = null;
      this.sigmaSetting = null;
      this.lastIntValue = 0;
      this.lastDoubleValue = 0.0;
      this.samplesSinceOutlier = 0;
      this.notInARowDisabled = false;
      this.random = new Random();
      this.consecutiveSameCount = 0;
      RangeSetting rangeSetting2 = new RangeSetting(module, str, d, d2, d3, d4, n);
      SettingChangeListener listener = () -> this.nextValue();
      RangeSetting rangeSetting3 = rangeSetting2.onChange(listener);
      String str2 = "";
      this.rangeSetting = (RangeSetting)this.registerNamedSetting(str2, rangeSetting3);
      BooleanValue booleanValue = new BooleanValue(module, str + "NotInARow", false);
      String str3 = "NotInARow";
      Setting setting = booleanValue.withDisplayName(str3);
      this.notInARow = (BooleanValue)this.registerSetting(setting);
      this.decimalPlaces = n;
      RangeSetting rangeSetting4 = this.rangeSetting;
      Runnable runnable = () -> this.nextValue();
      rangeSetting4.withValueUpdatedCallback(runnable);
      this.parentGroup = null;
      this.parentModule = module;
      this.baseName = str;
      this.nextValue();
   }

   private int sampleIntAvoidingRepeat(int n, RangeSetting rangeSetting2, boolean flag, boolean flag2) {
      int n2 = n;
      int n3 = this.sampleRawInt(rangeSetting2, flag, flag2);
      boolean flag3 = !this.notInARowDisabled && this.notInARow.getValue() && (int)rangeSetting2.getMinValue(flag2) != (int)rangeSetting2.getMaxValue(flag2) && n3 == n;
      if (flag3 && this.maxInARowSetting != null) {
         this.consecutiveSameCount++;
         flag3 = false;
         if ((double)this.consecutiveSameCount >= this.maxInARowSetting.getValue()) {
            flag3 = true;
            this.consecutiveSameCount = 0;
         }
      } else {
         this.consecutiveSameCount = 0;
      }

      for (int i = 0; i < 30 && flag3 && n3 == n2; i++) {
         n3 = this.sampleRawInt(rangeSetting2, flag, flag2);
      }

      return n3;
   }

   public RandomizedRangeSettings(Module module, String str, double d, double d2, double d3, double d4, int n, DoubleValue doubleValue) {
       super();
      this.outliersEnabled = null;
      this.outlierValueRange = null;
      this.outlierIntervalRange = null;
      this.normalDistributionEnabled = null;
      this.sigmaSetting = null;
      this.lastIntValue = 0;
      this.lastDoubleValue = 0.0;
      this.samplesSinceOutlier = 0;
      this.notInARowDisabled = false;
      this.random = new Random();
      this.consecutiveSameCount = 0;
      RangeSetting rangeSetting2 = new RangeSetting(module, str, d, d2, d3, d4, n, doubleValue);
      SettingChangeListener listener = () -> this.nextValue();
      RangeSetting rangeSetting3 = rangeSetting2.onChange(listener);
      String str2 = "";
      this.rangeSetting = (RangeSetting)this.registerNamedSetting(str2, rangeSetting3);
      BooleanValue booleanValue = new BooleanValue(module, str + "NotInARow", false, doubleValue);
      String str3 = "NotInARow";
      Setting setting = booleanValue.withDisplayName(str3);
      this.notInARow = (BooleanValue)this.registerSetting(setting);
      this.decimalPlaces = n;
      RangeSetting rangeSetting4 = this.rangeSetting;
      Runnable runnable = () -> this.nextValue();
      rangeSetting4.withValueUpdatedCallback(runnable);
      this.parentGroup = doubleValue;
      this.parentModule = module;
      this.baseName = str;
      this.nextValue();
   }

   private boolean shouldTriggerOutlier() {
      boolean flag = this.outliersEnabled != null && this.outliersEnabled.getValue() && this.samplesSinceOutlier > this.nextOutlierThreshold;
      if (flag) {
         this.resetOutlierCountdown();
      }

      this.samplesSinceOutlier++;
      return flag;
   }

   public RandomizedRangeSettings withMaxInARow() {
      StringValue stringValue = new StringValue(this.parentModule, this.baseName + "MaxInARow", 1.0, 0.0, 10.0, 0, this.parentGroup);
      String str = "MaxInARow";
      stringValue = stringValue.named(str);
      SettingVisibilityCondition condition = () -> this.isMaxInARowVisible();
      StringValue stringValue2 = stringValue.visibleWhen(condition);
      this.maxInARowSetting = (StringValue)this.registerSetting(stringValue2);
      return this;
   }

   public void randomize() {
      this.rangeSetting.randomize();
      if (this.sigmaSetting != null) {
         this.sigmaSetting.randomize();
         this.centerSetting.randomize();
         if (this.betaSetting != null) {
            this.betaSetting.randomize();
         }
      }
   }

   public RandomizedRangeSettings withOutliers(double d, double d2, double d3, double d4, double d5, double d6) {
      boolean flag = true;
      return this.withOutliers(d, d2, d3, d4, d5, d6, flag);
   }

   private boolean isMaxInARowVisible() {
      return this.notInARow.getValue() && this.notInARow.isVisible();
   }

   private void resetOutlierCountdown() {
      this.samplesSinceOutlier = 0;
      RandomNumberGenerator instance = DefaultRandomGenerator.instance;
      double minValue = this.outlierIntervalRange.getMinValue();
      double maxValue = this.outlierIntervalRange.getMaxValue();
      double d = maxValue;
      double d2 = minValue;
      this.nextOutlierThreshold = instance.nextInt(d2, d);
   }

   public double getLastValue() {
      return this.decimalPlaces == 0 ? (double)this.lastIntValue : this.lastDoubleValue;
   }

   public double getMinValue() {
      return this.rangeSetting.getMinValue();
   }

   public double nextValue(boolean flag) {
      return this.decimalPlaces == 0 ? (double)this.nextInt(flag) : this.nextDouble(flag);
   }

   public double getMaxValue() {
      return this.rangeSetting.getMaxValue();
   }

   public RandomizedRangeSettings withRangeNames(String str, String str2) {
      this.rangeSetting = this.rangeSetting.withBoundNames(str, str2);
      return this;
   }

   public double nextSecureValue() {
      return this.decimalPlaces == 0 ? (double)this.nextSecureInt() : this.nextSecureDouble();
   }

   public RandomizedRangeSettings withOutliers(double d, double d2, double d3, double d4, double d5, double d6, boolean flag) {
      BooleanValue booleanValue = new BooleanValue(this.parentModule, this.baseName + "Outliers", flag, this.parentGroup);
      String str = "Outliers";
      Setting setting = booleanValue.withDisplayName(str);
      this.outliersEnabled = (BooleanValue)this.registerSetting(setting);
      RangeSetting rangeSetting2 = new RangeSetting(this.parentModule, this.baseName + "OutlierValue", d, d2, d3, d4, this.decimalPlaces, this.parentGroup);
      String str2 = "OutlierValue";
      RangeSetting rangeSetting3 = rangeSetting2.named(str2);
      SettingVisibilityCondition condition = () -> {
         return this.outliersEnabled.getValue();
      };
      RangeSetting rangeSetting4 = rangeSetting3.visibleWhen(condition);
      this.outlierValueRange = (RangeSetting)this.registerSetting(rangeSetting4);
      RangeSetting rangeSetting5 = new RangeSetting(this.parentModule, this.baseName + "OutlierDelay", d5, d6, 0.0, 100.0, 0, this.parentGroup);
      String str3 = "MaxOutlierInterval";
      String str4 = "MinOutlierInterval";
      rangeSetting5 = rangeSetting5.withBoundNames(str4, str3);
      SettingVisibilityCondition condition2 = () -> {
         return this.outliersEnabled.getValue();
      };
      RangeSetting rangeSetting6 = rangeSetting5.visibleWhen(condition2);
      this.outlierIntervalRange = (RangeSetting)this.registerSetting(rangeSetting6);
      RangeSetting outlierValueRange2 = this.outlierValueRange;
      Runnable runnable = () -> this.resetOutlierCountdown();
      outlierValueRange2.withValueUpdatedCallback(runnable);
      RangeSetting outlierIntervalRange2 = this.outlierIntervalRange;
      Runnable runnable2 = () -> this.resetOutlierCountdown();
      outlierIntervalRange2.withValueUpdatedCallback(runnable2);
      this.resetOutlierCountdown();
      return this;
   }

   public double getMidpoint() {
      double minValue = this.getMinValue();
      return minValue + (this.getMaxValue() - this.getMinValue()) / 2.0;
   }

   public RandomizedRangeSettings withNormalDistribution(double d, double d2, double d3, boolean flag) {
      boolean flag2 = false;
      double d4 = 2.0;
      return this.withNormalDistribution(d, d2, d3, d4, flag, flag2);
   }

   public int nextInt() {
      boolean flag = false;
      return this.nextInt(flag);
   }

   public double nextValue() {
      double d;
      if (this.decimalPlaces == 0) {
         d = (double)this.nextInt();
      } else {
         d = this.nextDouble();
      }

      return d;
   }

   public RandomizedRangeSettings withNormalDistribution(double d, double d2, double d3, double d4, boolean flag, boolean flag2) {
      BooleanValue booleanValue = new BooleanValue(this.parentModule, this.baseName + "NormalDistribution", flag, this.parentGroup);
      String str = "Normal Distribution";
      Setting setting = booleanValue.withDisplayName(str);
      this.normalDistributionEnabled = (BooleanValue)this.registerSetting(setting);
      StringValue stringValue = new StringValue(this.parentModule, this.baseName + "Sigma", d, d2, d3, 2, this.parentGroup);
      String str2 = "Sigma";
      stringValue = stringValue.named(str2);
      SettingVisibilityCondition condition = () -> {
         return this.normalDistributionEnabled.getValue();
      };
      StringValue stringValue2 = stringValue.visibleWhen(condition);
      this.sigmaSetting = (StringValue)this.registerSetting(stringValue2);
      StringValue stringValue3 = new StringValue(this.parentModule, this.baseName + "Center", 50.0, 0.0, 100.0, 0, this.parentGroup);
      String str3 = "Center";
      StringValue stringValue4 = stringValue3.named(str3);
      SettingVisibilityCondition condition2 = () -> this.isCenterSettingVisible();
      StringValue stringValue5 = stringValue4.visibleWhen(condition2);
      this.centerSetting = (StringValue)this.registerSetting(stringValue5);
      if (flag2) {
         StringValue stringValue6 = new StringValue(this.parentModule, this.baseName + "Beta", d4, 0.1, 4.0, 2, this.parentGroup);
         String str4 = "Beta";
         StringValue stringValue7 = stringValue6.named(str4);
         SettingVisibilityCondition condition3 = () -> this.isBetaSettingVisible();
         StringValue stringValue8 = stringValue7.visibleWhen(condition3);
         this.betaSetting = (StringValue)this.registerSetting(stringValue8);
         this.generalizedNormalSampler = new GeneralizedNormalSampler();
      }

      return this;
   }

   private boolean isCenterSettingVisible() {
      return this.normalDistributionEnabled.getValue();
   }

   public double nextDouble() {
      boolean flag = false;
      return this.nextDouble(flag);
   }

   public void setLastValue(double d) {
      if (this.decimalPlaces == 0) {
         this.lastIntValue = (int)d;
      } else {
         this.lastDoubleValue = d;
      }
   }

   private double sampleDoubleAvoidingRepeat(double d, RangeSetting rangeSetting2, boolean flag, boolean flag2) {
      double d2 = d;
      double d3 = this.sampleRawDouble(rangeSetting2, flag);
      boolean flag3 = !this.notInARowDisabled && this.notInARow.getValue() && rangeSetting2.getMinValue(flag2) != rangeSetting2.getMaxValue(flag2) && d3 == d;
      if (flag3 && this.maxInARowSetting != null) {
         this.consecutiveSameCount++;
         flag3 = false;
         if ((double)this.consecutiveSameCount >= this.maxInARowSetting.getValue()) {
            flag3 = true;
            this.consecutiveSameCount = 0;
         }
      } else {
         this.consecutiveSameCount = 0;
      }

      for (int i = 0; i < 30 && flag3 && d3 == d2; i++) {
         d3 = this.sampleRawDouble(rangeSetting2, flag);
      }

      return d3;
   }

   private double sampleRawDouble(RangeSetting rangeSetting2, boolean flag) {
      if (this.normalDistributionEnabled != null && this.normalDistributionEnabled.getValue()) {
         double d = this.sampleFromDistribution(rangeSetting2);
         double minValue = rangeSetting2.getMinValue();
         return MathHelper.clamp_double(d, minValue, rangeSetting2.getMaxValue());
      } else {
         double d2;
         if (flag) {
            RandomNumberGenerator instance = DefaultRandomGenerator.instance;
            double minValue2 = rangeSetting2.getMinValue();
            double maxValue = rangeSetting2.getMaxValue();
            double d3 = minValue2;
            d2 = instance.nextSecureDouble(d3, maxValue);
         } else {
            RandomNumberGenerator instance2 = DefaultRandomGenerator.instance;
            double minValue3 = rangeSetting2.getMinValue();
            double maxValue2 = rangeSetting2.getMaxValue();
            double d4 = maxValue2;
            double d5 = minValue3;
            d2 = instance2.nextDouble(d5, d4);
         }

         return d2;
      }
   }

   private int sampleRawInt(RangeSetting rangeSetting2, boolean flag, boolean flag2) {
      if (this.normalDistributionEnabled != null && this.normalDistributionEnabled.getValue()) {
         int n = (int)Math.round(this.sampleFromDistribution(rangeSetting2));
         int minValue = (int)rangeSetting2.getMinValue();
         return MathHelper.clamp_int(n, minValue, (int)rangeSetting2.getMaxValue());
      } else {
         int n2;
         if (flag) {
            RandomNumberGenerator instance = DefaultRandomGenerator.instance;
            double minValue2 = rangeSetting2.getMinValue(flag2);
            double maxValue = rangeSetting2.getMaxValue(flag2);
            double d = minValue2;
            n2 = (int)instance.nextSecureIntAsDouble(d, maxValue);
         } else {
            RandomNumberGenerator instance2 = DefaultRandomGenerator.instance;
            double minValue3 = rangeSetting2.getMinValue(flag2);
            double maxValue2 = rangeSetting2.getMaxValue(flag2);
            double d2 = maxValue2;
            double d3 = minValue3;
            n2 = instance2.nextInt(d3, d2);
         }

         return n2;
      }
   }

   public int nextInt(boolean flag) {
      boolean flag2 = false;
      RangeSetting rangeSetting2 = this.rangeSetting;
      int lastIntValue2 = this.lastIntValue;
      int n = this.sampleIntAvoidingRepeat(lastIntValue2, rangeSetting2, flag2, flag);
      if (this.shouldTriggerOutlier()) {
         int n2 = Math.max(0, (int)(this.rangeSetting.getMinValue(flag) - this.outlierValueRange.getMinValue(flag)));
         int n3 = Math.max(0, (int)(this.outlierValueRange.getMaxValue(flag) - this.rangeSetting.getMaxValue(flag)));
         int n4 = n2 + n3;
         if (n4 > 0) {
            int n5;
            if (n2 == 0) {
               int n6 = (int)((double)((int)this.outlierValueRange.getMaxValue(flag)) - Math.max(this.outlierValueRange.getMinValue(flag), this.rangeSetting.getMaxValue(flag)));
               RandomNumberGenerator instance = DefaultRandomGenerator.instance;
               int n7 = Math.max(n6, 0);
               byte b = 0;
               n5 = instance.nextInt2(b, n7) + 1;
            } else if (n3 == 0) {
               int n8 = (int)(Math.min(this.outlierValueRange.getMaxValue(flag), this.rangeSetting.getMinValue(flag)) - this.outlierValueRange.getMinValue(flag));
               RandomNumberGenerator instance2 = DefaultRandomGenerator.instance;
               int n9 = Math.max(n8, 0);
               byte b2 = 0;
               n5 = instance2.nextInt2(b2, n9);
            } else {
               int n10 = n4 - 1;
               byte b3 = 0;
               n5 = DefaultRandomGenerator.instance.nextInt2(b3, n10);
            }

            if (n5 < n2) {
               n = (int)(this.outlierValueRange.getMinValue(flag) + (double)n5);
            } else {
               n = (int)(this.outlierValueRange.getMaxValue(flag) - (double)(n5 - 1));
            }
         }
      }

      this.lastIntValue = n;
      return n;
   }

   private int sampleIntAvoidingRepeat(int n, RangeSetting rangeSetting2, boolean flag) {
      boolean flag2 = false;
      return this.sampleIntAvoidingRepeat(n, rangeSetting2, flag, flag2);
   }

   public int getLastIntValue() {
      return this.lastIntValue;
   }

   public int nextSecureInt() {
      boolean flag = true;
      RangeSetting rangeSetting2 = this.rangeSetting;
      int lastIntValue2 = this.lastIntValue;
      int n = this.sampleIntAvoidingRepeat(lastIntValue2, rangeSetting2, flag);
      if (this.shouldTriggerOutlier()) {
         int n2 = Math.max(0, (int)(this.rangeSetting.getMinValue() - this.outlierValueRange.getMinValue()));
         double maxValue = this.outlierValueRange.getMaxValue();
         int n3 = Math.max(0, (int)(maxValue - this.rangeSetting.getMaxValue()));
         int n4 = n2 + n3;
         if (n4 > 0) {
            int n5;
            if (n2 == 0) {
               double maxValue2 = (double)((int)this.outlierValueRange.getMaxValue());
               double minValue = this.outlierValueRange.getMinValue();
               int n6 = (int)(maxValue2 - Math.max(minValue, this.rangeSetting.getMaxValue()));
               RandomNumberGenerator instance = DefaultRandomGenerator.instance;
               int n7 = Math.max(n6, 0);
               int n8 = n7;
               byte b = 0;
               n5 = instance.nextSecureInt(b, n8) + 1;
            } else if (n3 == 0) {
               int n9 = (int)(Math.min(this.outlierValueRange.getMaxValue(), this.rangeSetting.getMinValue()) - this.outlierValueRange.getMinValue());
               RandomNumberGenerator instance2 = DefaultRandomGenerator.instance;
               int n10 = Math.max(n9, 0);
               int n11 = n10;
               byte b2 = 0;
               n5 = instance2.nextSecureInt(b2, n11);
            } else {
               int n12 = n4 - 1;
               int n13 = n12;
               byte b3 = 0;
               n5 = DefaultRandomGenerator.instance.nextSecureInt(b3, n13);
            }

            if (n5 < n2) {
               n = (int)(this.outlierValueRange.getMinValue() + (double)n5);
            } else {
               n = (int)(this.outlierValueRange.getMaxValue() - (double)(n5 - 1));
            }
         }
      }

      this.lastIntValue = n;
      return n;
   }

   private double sampleFromDistribution(RangeSetting rangeSetting2) {
      if (this.betaSetting != null) {
         GeneralizedNormalSampler generalizedNormalSampler2 = this.generalizedNormalSampler;
         double value = this.sigmaSetting.getValue();
         generalizedNormalSampler2.setStdDev(value);
         generalizedNormalSampler2 = this.generalizedNormalSampler;
         double value2 = this.betaSetting.getValue();
         generalizedNormalSampler2.setBeta(value2);

         for (int i = 0; i < 50; i++) {
            generalizedNormalSampler2 = this.generalizedNormalSampler;
            double minValue = rangeSetting2.getMinValue();
            double d = MathStatisticsUtil.lerp(minValue, rangeSetting2.getMaxValue(), this.centerSetting.getValue() / 100.0);
            double d2 = generalizedNormalSampler2.sample(d);
            double d3 = (double)((int)d2);
            if (d3 <= rangeSetting2.getMaxValue() && d2 >= rangeSetting2.getMinValue()) {
               return d2;
            }
         }

         double minValue2 = rangeSetting2.getMinValue();
         return MathStatisticsUtil.lerp(minValue2, rangeSetting2.getMaxValue(), Math.random());
      } else {
         for (int j = 0; j < 50; j++) {
            double d4 = this.random.nextGaussian() * this.sigmaSetting.getValue();
            double minValue3 = rangeSetting2.getMinValue();
            double d5 = d4 + MathStatisticsUtil.lerp(minValue3, rangeSetting2.getMaxValue(), this.centerSetting.getValue() / 100.0);
            d4 = (double)((int)d5);
            if (d4 <= rangeSetting2.getMaxValue() && d5 >= rangeSetting2.getMinValue()) {
               return d5;
            }
         }

         double minValue4 = rangeSetting2.getMinValue();
         return MathStatisticsUtil.lerp(minValue4, rangeSetting2.getMaxValue(), Math.random());
      }
   }


}
