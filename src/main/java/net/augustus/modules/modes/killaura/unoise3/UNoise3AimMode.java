package net.augustus.modules.modes.killaura.unoise3;

import net.augustus.settings.IntervalNoiseSetting;
import net.augustus.settings.NoiseLayerSetting;
import net.augustus.settings.RangeSetting;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.math.MathStatisticsUtil;
import net.augustus.utils.math.Vec2d;
import net.augustus.utils.rotation.HitboxAngleUtil;
import net.augustus.utils.rotation.RotationUtil;
import java.util.ArrayList;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.interfaces.MC;
import net.minecraft.util.MathHelper;

public abstract class UNoise3AimMode implements MC {
   private BooleanValue restrictToHittableSurface;
   private StringValue minSurfaceSize;
   private BooleanValue limitSurfaceToBoundingBox;
   private StringValue minSurfaceSizeBlocksOnly;
   private DoubleValue settingsGroup;
   private StringValue restrictionStrength;
   private StringValue restrictionStrengthBlocksOnly;
   private UNoise3SettingContext uNoise3SettingContext;
   private boolean vertical = false;
   private boolean supportsSurfaceRestriction;
   private UNoise3AimConstraintType uNoise3AimConstraintType;
   public IntervalNoiseSetting strengthNoise;
   private String name;
   private StringValue unrestrictedRotationStrength;

   private static void notifyStrengthRangeChanged(UNoise3SettingContext uNoise3SettingContext2, boolean flag) {
      uNoise3SettingContext2.getUNoise3PointSelector().recalculateModeStrengthLabels(flag);
   }

   public boolean isSurfaceRestrictionActive() {
      return this.supportsSurfaceRestriction && this.restrictToHittableSurface.getValue();
   }

   private void initSurfaceRestrictionSettings(UNoise3SettingContext uNoise3SettingContext2, UNoise3AimConstraintType uNoise3AimConstraintType2) {
      BooleanValue booleanValue = new BooleanValue(uNoise3SettingContext2.getModule(), uNoise3SettingContext2.getParentGroup().getName() + "_" + this.name + "restrictsurface", false, uNoise3SettingContext2.getParentGroup());
      String str = "Restrict Boundingbox to hittable surface only";
      this.restrictToHittableSurface = (BooleanValue)booleanValue.withDisplayName(str);
      if (uNoise3AimConstraintType2 != UNoise3AimConstraintType.POINT) {
         DoubleValue doubleValue = new DoubleValue(
            uNoise3SettingContext2.getModule(), uNoise3SettingContext2.getParentGroup().getName() + "_" + this.name + "hittable_surface_restriction", uNoise3SettingContext2.getParentGroup()
         );
         String str2 = "Hittable surface restriction";
         doubleValue = (DoubleValue)doubleValue.withDisplayName(str2);
         BooleanValue booleanValue2 = this.restrictToHittableSurface;
         this.restrictToHittableSurface.getClass();
         BooleanValue booleanValue3 = booleanValue2;
         SettingVisibilityCondition condition = () -> booleanValue3.getValue();
         DoubleValue doubleValue2 = doubleValue.visibleWhen(condition);
         StringValue stringValue = new StringValue(uNoise3SettingContext2.getModule(), doubleValue2.getName() + "_unrestrictedstrength", 0.0, 0.0, 100.0, 1, doubleValue2);
         String str3 = "Strength of the unrestricted rotation";
         this.unrestrictedRotationStrength = stringValue.named(str3);
         StringValue stringValue2 = new StringValue(uNoise3SettingContext2.getModule(), doubleValue2.getName() + "_strength", 100.0, 0.0, 100.0, 1, doubleValue2);
         String str4 = "Strength";
         this.restrictionStrength = stringValue2.named(str4);
         StringValue stringValue3 = new StringValue(uNoise3SettingContext2.getModule(), doubleValue2.getName() + "_minsurface", 1.0, 0.0, 30.0, 1, doubleValue2);
         String str5 = "Minimal surface size";
         this.minSurfaceSize = stringValue3.named(str5);
         StringValue stringValue4 = new StringValue(uNoise3SettingContext2.getModule(), doubleValue2.getName() + "_strengthblocks", 100.0, 0.0, 100.0, 1, doubleValue2);
         String str6 = "Strength (Blocks only)";
         this.restrictionStrengthBlocksOnly = stringValue4.named(str6);
         StringValue stringValue5 = new StringValue(uNoise3SettingContext2.getModule(), doubleValue2.getName() + "_minsurfaceblocks", 1.0, 0.0, 30.0, 1, doubleValue2);
         String str7 = "Minimal surface size (Blocks only)";
         this.minSurfaceSizeBlocksOnly = stringValue5.named(str7);
         if (this.vertical) {
            BooleanValue booleanValue4 = new BooleanValue(uNoise3SettingContext2.getModule(), doubleValue2.getName() + "_limitBBSize", false, doubleValue2);
            String str8 = "Limit surface size to actual bounding box size";
            this.limitSurfaceToBoundingBox = (BooleanValue)booleanValue4.withDisplayName(str8);
         }
      }
   }

   public IntervalNoiseSetting getStrengthNoise() {
      return this.strengthNoise;
   }

   public UNoise3AimMode(UNoise3SettingContext uNoise3SettingContext2) {
      this.uNoise3SettingContext = uNoise3SettingContext2;
   }

   public void setVertical(boolean flag) {
      this.vertical = flag;
   }

   public double clampToUnoccludedBounds(UNoise3AimContext uNoise3AimContext, double d) {
      double[] unoccludedAngleBounds = uNoise3AimContext.unoccludedAngleBounds;
      if (uNoise3AimContext.vertical) {
         return MathHelper.clamp_double(d, unoccludedAngleBounds[0], unoccludedAngleBounds[1]);
      } else {
         double normalizedYawPosition = HitboxAngleUtil.getNormalizedYawPosition(uNoise3AimContext.expandedAngleBounds[0], uNoise3AimContext.expandedAngleBounds[1], d);
         double normalizedYawPosition2 = HitboxAngleUtil.getNormalizedYawPosition(uNoise3AimContext.expandedAngleBounds[0], uNoise3AimContext.expandedAngleBounds[1], unoccludedAngleBounds[0]);
         double normalizedYawPosition3 = HitboxAngleUtil.getNormalizedYawPosition(uNoise3AimContext.expandedAngleBounds[0], uNoise3AimContext.expandedAngleBounds[1], unoccludedAngleBounds[1]);
         normalizedYawPosition = MathHelper.clamp_double(normalizedYawPosition, normalizedYawPosition2, normalizedYawPosition3);
         return MathHelper.wrapAngleTo180_double(HitboxAngleUtil.lerpRange(uNoise3AimContext.expandedAngleBounds, normalizedYawPosition));
      }
   }

   public UNoise3AimMode(UNoise3SettingContext uNoise3SettingContext2, String str, boolean flag) {
       this(uNoise3SettingContext2, str, UNoise3AimConstraintType.NONE, false, flag);
   }

   private double[] blendRestrictedBounds(double[] doubles, double[] doubles2, double d, double d2, boolean flag) {
      if (flag) {
         double[] doubles3 = new double[]{RotationUtil.lerpPitch(doubles2[0], doubles[0], d2), RotationUtil.lerpPitch(doubles2[1], doubles[1], d2)};
         if (Math.abs(doubles3[0] - doubles3[1]) < d) {
            double d3 = (doubles3[0] + doubles3[1]) / 2.0;
            doubles3[0] = d3 - d;
            doubles3[1] = d3 + d;
            if (this.limitSurfaceToBoundingBox.getValue()) {
               doubles3[0] = Math.max(doubles3[0], doubles2[0]);
               doubles3[1] = Math.min(doubles3[1], doubles2[1]);
            }
         }

         return HitboxAngleUtil.findMinMaxWithIndices(doubles3);
      } else {
         double[] doubles4 = new double[]{RotationUtil.lerpYaw(doubles2[0], doubles[0], d2), RotationUtil.lerpYaw(doubles2[1], doubles[1], d2)};
         doubles4 = HitboxAngleUtil.normalizeYawInterval(doubles4[0], doubles4[1]);
         double d4 = MathHelper.wrapAngleTo180_double(doubles4[0]);
         double d5 = MathHelper.wrapAngleTo180_double(doubles4[1]);
         if (HitboxAngleUtil.getShortestAngularDistance(d4, d5) < d) {
            double d6 = 0.5;
            double d7 = HitboxAngleUtil.lerpRange(doubles4, d6);
            doubles4[0] = d7 - d;
            doubles4[1] = d7 + d;
         }

         return doubles4;
      }
   }

   public DoubleValue getSettingsGroup() {
      return this.settingsGroup;
   }

   public double[] getEffectiveAngleBounds(UNoise3AimContext uNoise3AimContext, boolean flag) {
      double[] doubles = flag ? uNoise3AimContext.expandedAngleBounds : uNoise3AimContext.exactAngleBounds;
      if (this.supportsSurfaceRestriction && this.restrictToHittableSurface.getValue()) {
         boolean flag2 = true;
         if (this.unrestrictedRotationStrength.getValue(flag2) == 0.0) {
            boolean flag3 = true;
            if (this.restrictionStrength.getValue(flag3) == 0.0) {
               boolean flag4 = true;
               if (this.restrictionStrengthBlocksOnly.getValue(flag4) == 0.0) {
                  return doubles;
               }
            }
         }

         double[] hittableAngleBounds = uNoise3AimContext.hittableAngleBounds;
         double value = this.minSurfaceSize.getValue();
         boolean flag5 = true;
         double d = this.restrictionStrength.getValue(flag5) / 100.0;
         boolean vertical2 = uNoise3AimContext.vertical;
         double d2 = d;
         double d3 = value;
         double[] doubles2 = hittableAngleBounds;
         double[] doubles3 = this.blendRestrictedBounds(doubles2, doubles, d3, d2, vertical2);
         hittableAngleBounds = uNoise3AimContext.unoccludedAngleBounds;
         value = this.minSurfaceSizeBlocksOnly.getValue();
         boolean flag6 = true;
         d = this.restrictionStrengthBlocksOnly.getValue(flag6) / 100.0;
         boolean vertical3 = uNoise3AimContext.vertical;
         double d4 = d;
         double d5 = value;
         double[] doubles4 = hittableAngleBounds;
         double[] doubles5 = this.blendRestrictedBounds(doubles4, doubles, d5, d4, vertical3);
         ArrayList list = new ArrayList();
         double d6 = doubles[0];
         boolean flag7 = true;
         list.add(new Vec2d(d6, this.unrestrictedRotationStrength.getValue(flag7) / 100.0));
         d6 = doubles3[0];
         boolean flag8 = true;
         list.add(new Vec2d(d6, this.restrictionStrength.getValue(flag8) / 100.0));
         d6 = doubles5[0];
         boolean flag9 = true;
         list.add(new Vec2d(d6, this.restrictionStrengthBlocksOnly.getValue(flag9) / 100.0));
         boolean flag10 = !uNoise3AimContext.vertical;
         double d7 = UNoise3PointSelector.computeWeightedAngleAverage(list, flag10);
         list = new ArrayList();
         d6 = doubles[1];
         boolean flag11 = true;
         list.add(new Vec2d(d6, this.unrestrictedRotationStrength.getValue(flag11) / 100.0));
         d6 = doubles3[1];
         boolean flag12 = true;
         list.add(new Vec2d(d6, this.restrictionStrength.getValue(flag12) / 100.0));
         d6 = doubles5[1];
         boolean flag13 = true;
         list.add(new Vec2d(d6, this.restrictionStrengthBlocksOnly.getValue(flag13) / 100.0));
         boolean flag14 = !uNoise3AimContext.vertical;
         double d8 = UNoise3PointSelector.computeWeightedAngleAverage(list, flag14);
         return uNoise3AimContext.vertical ? HitboxAngleUtil.findMinMaxWithIndices(new double[]{d7, d8}) : HitboxAngleUtil.normalizeYawInterval(d7, d8);
      } else {
         return doubles;
      }
   }

   public abstract double computeTargetAngle(UNoise3AimContext uNoise3AimContext);

   public double sampleWeight(UNoise3AimContext uNoise3AimContext) {
      this.strengthNoise.stepLayers();
      return this.strengthNoise.sample();
   }

   public void updateStrengthSummaryLabel(double d, double d2) {
      if (this.settingsGroup == null) {
         DoubleValue noisegroup = this.strengthNoise.noisegroup;
         String str = this.name + " Strength (min=" + MathStatisticsUtil.roundToDecimalPlaces(d * 100.0, 2) + "%, max=" + MathStatisticsUtil.roundToDecimalPlaces(d2 * 100.0, 2) + "%)";
         noisegroup.updateDisplayName(str);
      } else {
         DoubleValue settingsGroup2 = this.settingsGroup;
         String str2 = this.name + " (min=" + MathStatisticsUtil.roundToDecimalPlaces(d * 100.0, 2) + "%, max=" + MathStatisticsUtil.roundToDecimalPlaces(d2 * 100.0, 2) + "%)";
         settingsGroup2.updateDisplayName(str2);
      }
   }

   public String getName() {
      return this.name;
   }

   public boolean isVertical() {
      return this.vertical;
   }

   public UNoise3AimMode(UNoise3SettingContext uNoise3SettingContext2, String str, UNoise3AimConstraintType uNoise3AimConstraintType2, boolean flag, boolean flag2) {
       super();
      this.vertical = false;
      if (uNoise3AimConstraintType2 != UNoise3AimConstraintType.NONE) {
         flag = true;
      }

      this.name = str;
      this.uNoise3SettingContext = uNoise3SettingContext2;
      this.supportsSurfaceRestriction = uNoise3AimConstraintType2 != UNoise3AimConstraintType.NONE;
      this.uNoise3AimConstraintType = uNoise3AimConstraintType2;
      this.vertical = flag2;
      if (flag) {
         this.settingsGroup = (DoubleValue)(new DoubleValue(uNoise3SettingContext2.getModule(), uNoise3SettingContext2.getParentGroup().getName() + "_" + str, uNoise3SettingContext2.getParentGroup())).withDisplayName(str);
         this.strengthNoise = new IntervalNoiseSetting(uNoise3SettingContext2.getModule(), uNoise3SettingContext2.getParentGroup().getName() + "_" + str, this.settingsGroup, false);
         if (this.supportsSurfaceRestriction) {
            DoubleValue settingsGroup2 = this.settingsGroup;
            UNoise3SettingContext uNoise3SettingContext3 = uNoise3SettingContext2.withParentGroup(settingsGroup2);
            this.initSurfaceRestrictionSettings(uNoise3SettingContext3, uNoise3AimConstraintType2);
         }
      } else {
         this.strengthNoise = new IntervalNoiseSetting(uNoise3SettingContext2.getModule(), uNoise3SettingContext2.getParentGroup().getName() + "_" + str, uNoise3SettingContext2.getParentGroup(), false);
      }

      byte b = 0;
      double d = 0.0;
      double d2 = 0.0;
      double d3 = 100.0;
      double d4 = 0.0;
      this.strengthNoise.withInterval(d4, d3, d2, d, b);
      String str2 = str + " Strength";
      this.strengthNoise.named(str2);
      IntervalNoiseSetting strengthNoise2 = this.strengthNoise;
      NoiseLayerSetting noiseLayerSetting = this.strengthNoise.newLayerBuilder().build();
      strengthNoise2.addLayer(noiseLayerSetting);
      RangeSetting intervalRange = this.strengthNoise.intervalRange;
      Runnable runnable = () -> notifyStrengthRangeChanged(uNoise3SettingContext2, flag2);
      intervalRange.addPropertyChangeCallback(runnable);
   }

   public void stepNoise() {
   }




}
