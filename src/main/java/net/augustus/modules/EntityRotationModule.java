package net.augustus.modules;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.events.EventMove;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.settings.RangeSetting;
import net.augustus.utils.rotation.MoveFixMode;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.events.listeners.MoveListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.EventMoveInput;
import net.augustus.Augustus;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public abstract class EntityRotationModule extends Module implements MoveListener, MoveInputListener {
   public final RangeSetting yawSpeedInRotationModule;
   public final RangeSetting pitchSpeedInRotationModule;
   public final BooleansSetting rotationModeInEntityRotationModule;
   public final StringValue hitBoxPercentageVerticalInEntityRotationModule;
   public final StringValue hitBoxPercentageHorizontalInEntityRotationModule;
   public final StringValue hitBoxPercentageVerticalInEntityRotationModule1;
   public final StringValue hitBoxPercentageHorizontalInEntityRotationModule1;
   public final BooleanValue advancedRotsInRotationModule;
   public final BooleanValue interpolationInRotationModule;
   public final BooleansSetting randomTypeInRotationModule;
   public final BooleansSetting randomizeInRotationModule;
   public final BooleanValue reducedRotateInRotationModule;
   public final StringValue reduceRotationPercentageInRotationModule;
   public final BooleanValue smartRotateInRotationModule;
   public final BooleanValue pitchOutliersRotationModule;
   public final RandomizedRangeSettings pitchOutliersAddRotationModule;
   public final RandomizedRangeSettings pitchOutliersDelayRotationModule;
   public final RangeSetting innerRadiusInRotationModule;
   public final StringValue bestHitXMinDistInRotationModule;
   public final StringValue bestHitYMInDistInRotationModule;
   public final StringValue minAbsDistInRotationModule;
   public final StringValue maxXAbsDistInRotationModule;
   public final StringValue maxYAbsDistInRotationModule;
   public final RangeSetting xRelDistInRotationModule;
   public final RangeSetting yRelDistInRotationModule;
   public final StringValue maxRotationAngleInRotationModule;
   public final StringValue randomStrengthInRotationModule;
   public final BooleanValue moveFixInRotationModule;
   public final BooleansSetting silentMoveFix;
   public final BooleanValue silentMoveFixIntaveInRotationModule;
   public boolean rotating;
   public boolean firstRotationTick;
   private final RotationUtil rotationUtil;

   public EntityRotationModule(String str, String str2, Color color2, Categorys categorys2) {
       super(str, str2, color2, categorys2);
      RangeSetting rangeSetting = new RangeSetting(this, "YawSpeedInRotationModule", 30.0, 50.0, 0.0, 180.0, 0);
      String str3 = "MaxYawSpeed";
      String str4 = "MinYawSpeed";
      this.yawSpeedInRotationModule = rangeSetting.withBoundNames(str4, str3);
      RangeSetting rangeSetting2 = new RangeSetting(this, "PitchSpeedInRotationModule", 30.0, 50.0, 0.0, 180.0, 0);
      String str5 = "MaxPitchSpeed";
      String str6 = "MinPitchSpeed";
      this.pitchSpeedInRotationModule = rangeSetting2.withBoundNames(str6, str5);
      BooleansSetting booleansSetting = new BooleansSetting(
         this, "RotationModeInEntityRotationModule", "BestHitVec", new String[]{"Head", "BestHitVec", "NearestHitVec"}
      );
      String str7 = "RotationMode";
      this.rotationModeInEntityRotationModule = (BooleansSetting)booleansSetting.withDisplayName(str7);
      StringValue stringValue = new StringValue(this, "HitBoxPercentageVerticalInEntityRotationModule", 100.0, 1.0, 100.0, 0);
      String str8 = "HitBoxPercentageVertical";
      this.hitBoxPercentageVerticalInEntityRotationModule = stringValue.named(str8);
      StringValue stringValue2 = new StringValue(this, "HitBoxPercentageHorizontalInEntityRotationModule", 100.0, 1.0, 100.0, 0);
      String str9 = "HitBoxPercentageHorizontal";
      this.hitBoxPercentageHorizontalInEntityRotationModule = stringValue2.named(str9);
      StringValue stringValue3 = new StringValue(this, "HitBoxPercentageVerticalInEntityRotationModule1", 100.0, 1.0, 100.0, 0);
      String str10 = "HitBoxPercentageVertical";
      this.hitBoxPercentageVerticalInEntityRotationModule1 = stringValue3.named(str10);
      StringValue stringValue4 = new StringValue(this, "HitBoxPercentageHorizontalInEntityRotationModule1", 100.0, 1.0, 100.0, 0);
      String str11 = "HitBoxPercentageHorizontal";
      this.hitBoxPercentageHorizontalInEntityRotationModule1 = stringValue4.named(str11);
      BooleanValue booleanValue = new BooleanValue(this, "AdvancedRotsInRotationModule", true);
      String str12 = "AdvancedRots";
      this.advancedRotsInRotationModule = (BooleanValue)booleanValue.withDisplayName(str12);
      BooleanValue booleanValue2 = new BooleanValue(this, "InterpolationInRotationModule", false);
      String str13 = "Interpolation";
      this.interpolationInRotationModule = (BooleanValue)booleanValue2.withDisplayName(str13);
      BooleansSetting booleansSetting2 = new BooleansSetting(
         this, "RandomTypeInRotationModule", "SecureRandom", new String[]{"Random", "SecureRandom", "Gaussian", "Intave"}
      );
      String str14 = "RandomType";
      this.randomTypeInRotationModule = (BooleansSetting)booleansSetting2.withDisplayName(str14);
      BooleansSetting booleansSetting3 = new BooleansSetting(
         this, "RandomizeInRotationModule", "OnlyRotation", new String[]{"None", "Basic", "Doubled", "OnlyRotation", "Advanced"}
      );
      String str15 = "Randomize";
      this.randomizeInRotationModule = (BooleansSetting)booleansSetting3.withDisplayName(str15);
      BooleanValue booleanValue3 = new BooleanValue(this, "ReducedRotateInRotationModule", false);
      String str16 = "ReducedRotate";
      BooleanValue booleanValue4 = (BooleanValue)booleanValue3.withDisplayName(str16);
      SettingVisibilityCondition condition = () -> this.isReducedRotateVisible();
      this.reducedRotateInRotationModule = (BooleanValue)booleanValue4.withVisibility(condition);
      StringValue stringValue5 = new StringValue(this, "ReduceRotationPercentageInRotationModule", 100.0, 0.0, 100.0, 0);
      String str17 = "ReduceRotationPercentage";
      StringValue stringValue6 = stringValue5.named(str17);
      SettingVisibilityCondition condition2 = () -> this.isReduceRotationPercentageVisible();
      this.reduceRotationPercentageInRotationModule = stringValue6.visibleWhen(condition2);
      BooleanValue booleanValue5 = new BooleanValue(this, "SmartRotateInRotationModule", false);
      String str18 = "SmartRotate";
      BooleanValue booleanValue6 = (BooleanValue)booleanValue5.withDisplayName(str18);
      SettingVisibilityCondition condition3 = () -> this.isSmartRotateVisible();
      this.smartRotateInRotationModule = (BooleanValue)booleanValue6.withVisibility(condition3);
      BooleanValue booleanValue7 = new BooleanValue(this, "PitchOutliersRotationModule", false);
      String str19 = "PitchOutliers";
      BooleanValue booleanValue8 = (BooleanValue)booleanValue7.withDisplayName(str19);
      SettingVisibilityCondition condition4 = () -> this.isPitchOutliersVisible();
      this.pitchOutliersRotationModule = (BooleanValue)booleanValue8.withVisibility(condition4);
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(this, "PitchOutliersAddRotationModule", 4.0, 10.0, 0.0, 20.0, 2);
      String str20 = "PitchOutliersAdd";
      RandomizedRangeSettings randomizedRangeSettings2 = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str20);
      SettingVisibilityCondition condition5 = () -> this.isPitchOutliersAddVisible();
      this.pitchOutliersAddRotationModule = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition5);
      RandomizedRangeSettings randomizedRangeSettings3 = (new RandomizedRangeSettings(this, "PitchOutliersDelayRotationModule", 15.0, 20.0, 1.0, 40.0, 0)).withoutNotInARow();
      String str21 = "PitchOutliersDelay";
      RandomizedRangeSettings randomizedRangeSettings4 = (RandomizedRangeSettings)randomizedRangeSettings3.withDisplayNamePrefix(str21);
      SettingVisibilityCondition condition6 = () -> this.isPitchOutliersDelayVisible();
      this.pitchOutliersDelayRotationModule = (RandomizedRangeSettings)randomizedRangeSettings4.withVisibility(condition6);
      RangeSetting rangeSetting3 = new RangeSetting(this, "InnerRadiusInRotationModule", "RadiusInRotationModule", 4.0, 9.0, 0.0, 15.0, 2);
      String str22 = "Radius";
      String str23 = "InnerRadius";
      RangeSetting rangeSetting4 = rangeSetting3.withBoundNames(str23, str22);
      SettingVisibilityCondition condition7 = () -> {
         return this.randomizeInRotationModule.getSelectedValue().equals("Advanced");
      };
      this.innerRadiusInRotationModule = rangeSetting4.visibleWhen(condition7);
      StringValue stringValue7 = new StringValue(this, "BestHitXMinDistInRotationModule", 2.0, 0.0, 10.0, 2);
      String str24 = "BestHitXMinDist";
      StringValue stringValue8 = stringValue7.named(str24);
      SettingVisibilityCondition condition8 = () -> this.isBestHitXMinDistVisible();
      this.bestHitXMinDistInRotationModule = stringValue8.visibleWhen(condition8);
      StringValue stringValue9 = new StringValue(this, "BestHitYMInDistInRotationModule", 2.0, 0.0, 10.0, 2);
      String str25 = "BestHitYMInDist";
      StringValue stringValue10 = stringValue9.named(str25);
      SettingVisibilityCondition condition9 = () -> this.isBestHitYMinDistVisible();
      this.bestHitYMInDistInRotationModule = stringValue10.visibleWhen(condition9);
      StringValue stringValue11 = new StringValue(this, "MinAbsDistInRotationModule", 0.5, 0.0, 10.0, 2);
      String str26 = "MinAbsDist";
      StringValue stringValue12 = stringValue11.named(str26);
      SettingVisibilityCondition condition10 = () -> this.isMinAbsDistVisible();
      this.minAbsDistInRotationModule = stringValue12.visibleWhen(condition10);
      StringValue stringValue13 = new StringValue(this, "MaxXAbsDistInRotationModule", 5.0, 0.0, 20.0, 2);
      String str27 = "MaxXAbsDist";
      StringValue stringValue14 = stringValue13.named(str27);
      SettingVisibilityCondition condition11 = () -> this.isMaxXAbsDistVisible();
      this.maxXAbsDistInRotationModule = stringValue14.visibleWhen(condition11);
      StringValue stringValue15 = new StringValue(this, "MaxYAbsDistInRotationModule", 20.0, 0.0, 20.0, 2);
      String str28 = "MaxYAbsDist";
      StringValue stringValue16 = stringValue15.named(str28);
      SettingVisibilityCondition condition12 = () -> this.isMaxYAbsDistVisible();
      this.maxYAbsDistInRotationModule = stringValue16.visibleWhen(condition12);
      RangeSetting rangeSetting5 = new RangeSetting(this, "XRelDistInRotationModule", 0.5, 10.0, 0.0, 20.0, 2);
      String str29 = "MaxXRelDist";
      String str30 = "MinXRelDist";
      RangeSetting rangeSetting6 = rangeSetting5.withBoundNames(str30, str29);
      SettingVisibilityCondition condition13 = () -> this.isXRelDistVisible();
      this.xRelDistInRotationModule = rangeSetting6.visibleWhen(condition13);
      RangeSetting rangeSetting7 = new RangeSetting(this, "YRelDistInRotationModule", 0.5, 10.0, 0.0, 20.0, 2);
      String str31 = "MaxYRelDist";
      String str32 = "MinYRelDist";
      RangeSetting rangeSetting8 = rangeSetting7.withBoundNames(str32, str31);
      SettingVisibilityCondition condition14 = () -> {
         return this.randomizeInRotationModule.getSelectedValue().equals("Advanced");
      };
      this.yRelDistInRotationModule = rangeSetting8.visibleWhen(condition14);
      StringValue stringValue17 = new StringValue(this, "MaxRotationAngleInRotationModule", 90.0, 0.0, 180.0, 0);
      String str33 = "MaxRotationAngle";
      StringValue stringValue18 = stringValue17.named(str33);
      SettingVisibilityCondition condition15 = () -> this.isMaxRotationAngleVisible();
      this.maxRotationAngleInRotationModule = stringValue18.visibleWhen(condition15);
      StringValue stringValue19 = new StringValue(this, "RandomStrengthInRotationModule", 1.25, 0.01, 5.0, 2);
      String str34 = "RandomStrength";
      StringValue stringValue20 = stringValue19.named(str34);
      SettingVisibilityCondition condition16 = () -> this.isRandomStrengthVisible();
      this.randomStrengthInRotationModule = stringValue20.visibleWhen(condition16);
      BooleanValue booleanValue9 = new BooleanValue(this, "MoveFixInRotationModule", true);
      String str35 = "MoveFix";
      this.moveFixInRotationModule = (BooleanValue)booleanValue9.withDisplayName(str35);
      BooleansSetting booleansSetting4 = new BooleansSetting(this, "SilentMoveFix", "Default", new String[]{"None", "Default", "Advanced"});
      String str36 = "SilentMoveFix";
      this.silentMoveFix = (BooleansSetting)booleansSetting4.withDisplayName(str36);
      BooleanValue booleanValue10 = new BooleanValue(this, "SilentMoveFixIntaveInRotationModule", true);
      String str37 = "SilentMoveFixIntave";
      this.silentMoveFixIntaveInRotationModule = (BooleanValue)booleanValue10.withDisplayName(str37);
      this.rotating = false;
      this.firstRotationTick = true;
      this.rotationUtil = new RotationUtil();
   }

   @Override
   public void onEnable() {
      RotationUtil rotationUtil2 = this.rotationUtil;
      float maxValue = (float)this.innerRadiusInRotationModule.getMaxValue();
      short s = 1000;
      float f = maxValue;
      rotationUtil2.generateDeltaCandidates(f, s);
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 28;
      this.toggleEventSubscription(b);
      byte b2 = 21;
      this.toggleEventSubscription(b2);
   }

   public EntityRotationModule(String str, String str2, boolean flag, Color color2, Categorys categorys2) {
       super(str, str2, flag, color2, categorys2);
      RangeSetting rangeSetting = new RangeSetting(this, "YawSpeedInRotationModule", 30.0, 50.0, 0.0, 180.0, 0);
      String str3 = "MaxYawSpeed";
      String str4 = "MinYawSpeed";
      this.yawSpeedInRotationModule = rangeSetting.withBoundNames(str4, str3);
      RangeSetting rangeSetting2 = new RangeSetting(this, "PitchSpeedInRotationModule", 30.0, 50.0, 0.0, 180.0, 0);
      String str5 = "MaxPitchSpeed";
      String str6 = "MinPitchSpeed";
      this.pitchSpeedInRotationModule = rangeSetting2.withBoundNames(str6, str5);
      BooleansSetting booleansSetting = new BooleansSetting(
         this, "RotationModeInEntityRotationModule", "BestHitVec", new String[]{"Head", "BestHitVec", "NearestHitVec"}
      );
      String str7 = "RotationMode";
      this.rotationModeInEntityRotationModule = (BooleansSetting)booleansSetting.withDisplayName(str7);
      Categorys.isControlFlowFlag();
      StringValue stringValue = new StringValue(this, "HitBoxPercentageVerticalInEntityRotationModule", 100.0, 1.0, 100.0, 0);
      String str8 = "HitBoxPercentageVertical";
      this.hitBoxPercentageVerticalInEntityRotationModule = stringValue.named(str8);
      stringValue = new StringValue(this, "HitBoxPercentageHorizontalInEntityRotationModule", 100.0, 1.0, 100.0, 0);
      String str9 = "HitBoxPercentageHorizontal";
      this.hitBoxPercentageHorizontalInEntityRotationModule = stringValue.named(str9);
      stringValue = new StringValue(this, "HitBoxPercentageVerticalInEntityRotationModule1", 100.0, 1.0, 100.0, 0);
      String str10 = "HitBoxPercentageVertical";
      this.hitBoxPercentageVerticalInEntityRotationModule1 = stringValue.named(str10);
      stringValue = new StringValue(this, "HitBoxPercentageHorizontalInEntityRotationModule1", 100.0, 1.0, 100.0, 0);
      String str11 = "HitBoxPercentageHorizontal";
      this.hitBoxPercentageHorizontalInEntityRotationModule1 = stringValue.named(str11);
      BooleanValue booleanValue = new BooleanValue(this, "AdvancedRotsInRotationModule", true);
      String str12 = "AdvancedRots";
      this.advancedRotsInRotationModule = (BooleanValue)booleanValue.withDisplayName(str12);
      BooleanValue booleanValue2 = new BooleanValue(this, "InterpolationInRotationModule", false);
      String str13 = "Interpolation";
      this.interpolationInRotationModule = (BooleanValue)booleanValue2.withDisplayName(str13);
      BooleansSetting booleansSetting2 = new BooleansSetting(
         this, "RandomTypeInRotationModule", "SecureRandom", new String[]{"Random", "SecureRandom", "Gaussian", "Intave"}
      );
      String str14 = "RandomType";
      this.randomTypeInRotationModule = (BooleansSetting)booleansSetting2.withDisplayName(str14);
      BooleansSetting booleansSetting3 = new BooleansSetting(
         this, "RandomizeInRotationModule", "OnlyRotation", new String[]{"None", "Basic", "Doubled", "OnlyRotation", "Advanced"}
      );
      String str15 = "Randomize";
      this.randomizeInRotationModule = (BooleansSetting)booleansSetting3.withDisplayName(str15);
      BooleanValue booleanValue3 = new BooleanValue(this, "ReducedRotateInRotationModule", false);
      String str16 = "ReducedRotate";
      BooleanValue booleanValue4 = (BooleanValue)booleanValue3.withDisplayName(str16);
      SettingVisibilityCondition condition = () -> this.isReducedRotateVisible();
      this.reducedRotateInRotationModule = (BooleanValue)booleanValue4.withVisibility(condition);
      stringValue = new StringValue(this, "ReduceRotationPercentageInRotationModule", 100.0, 0.0, 100.0, 0);
      String str17 = "ReduceRotationPercentage";
      stringValue = stringValue.named(str17);
      SettingVisibilityCondition condition2 = () -> this.isReduceRotationPercentageVisible();
      this.reduceRotationPercentageInRotationModule = stringValue.visibleWhen(condition2);
      BooleanValue booleanValue5 = new BooleanValue(this, "SmartRotateInRotationModule", false);
      String str18 = "SmartRotate";
      BooleanValue booleanValue6 = (BooleanValue)booleanValue5.withDisplayName(str18);
      SettingVisibilityCondition condition3 = () -> this.isSmartRotateVisible();
      this.smartRotateInRotationModule = (BooleanValue)booleanValue6.withVisibility(condition3);
      BooleanValue booleanValue7 = new BooleanValue(this, "PitchOutliersRotationModule", false);
      String str19 = "PitchOutliers";
      BooleanValue booleanValue8 = (BooleanValue)booleanValue7.withDisplayName(str19);
      SettingVisibilityCondition condition4 = () -> this.isPitchOutliersVisible();
      this.pitchOutliersRotationModule = (BooleanValue)booleanValue8.withVisibility(condition4);
      RandomizedRangeSettings randomizedRangeSettings = new RandomizedRangeSettings(this, "PitchOutliersAddRotationModule", 4.0, 10.0, 0.0, 20.0, 2);
      String str20 = "PitchOutliersAdd";
      RandomizedRangeSettings randomizedRangeSettings2 = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str20);
      SettingVisibilityCondition condition5 = () -> this.isPitchOutliersAddVisible();
      this.pitchOutliersAddRotationModule = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition5);
      RandomizedRangeSettings randomizedRangeSettings3 = (new RandomizedRangeSettings(this, "PitchOutliersDelayRotationModule", 15.0, 20.0, 1.0, 40.0, 0)).withoutNotInARow();
      String str21 = "PitchOutliersDelay";
      RandomizedRangeSettings randomizedRangeSettings4 = (RandomizedRangeSettings)randomizedRangeSettings3.withDisplayNamePrefix(str21);
      SettingVisibilityCondition condition6 = () -> this.isPitchOutliersDelayVisible();
      this.pitchOutliersDelayRotationModule = (RandomizedRangeSettings)randomizedRangeSettings4.withVisibility(condition6);
      RangeSetting rangeSetting3 = new RangeSetting(this, "InnerRadiusInRotationModule", "RadiusInRotationModule", 4.0, 9.0, 0.0, 15.0, 2);
      String str22 = "Radius";
      String str23 = "InnerRadius";
      RangeSetting rangeSetting4 = rangeSetting3.withBoundNames(str23, str22);
      SettingVisibilityCondition condition7 = () -> {
         return this.randomizeInRotationModule.getSelectedValue().equals("Advanced");
      };
      this.innerRadiusInRotationModule = rangeSetting4.visibleWhen(condition7);
      StringValue stringValue2 = new StringValue(this, "BestHitXMinDistInRotationModule", 2.0, 0.0, 10.0, 2);
      String str24 = "BestHitXMinDist";
      StringValue stringValue3 = stringValue2.named(str24);
      SettingVisibilityCondition condition8 = () -> this.isBestHitXMinDistVisible();
      this.bestHitXMinDistInRotationModule = stringValue3.visibleWhen(condition8);
      StringValue stringValue4 = new StringValue(this, "BestHitYMInDistInRotationModule", 2.0, 0.0, 10.0, 2);
      String str25 = "BestHitYMInDist";
      StringValue stringValue5 = stringValue4.named(str25);
      SettingVisibilityCondition condition9 = () -> this.isBestHitYMinDistVisible();
      this.bestHitYMInDistInRotationModule = stringValue5.visibleWhen(condition9);
      StringValue stringValue6 = new StringValue(this, "MinAbsDistInRotationModule", 0.5, 0.0, 10.0, 2);
      String str26 = "MinAbsDist";
      StringValue stringValue7 = stringValue6.named(str26);
      SettingVisibilityCondition condition10 = () -> this.isMinAbsDistVisible();
      this.minAbsDistInRotationModule = stringValue7.visibleWhen(condition10);
      StringValue stringValue8 = new StringValue(this, "MaxXAbsDistInRotationModule", 5.0, 0.0, 20.0, 2);
      String str27 = "MaxXAbsDist";
      StringValue stringValue9 = stringValue8.named(str27);
      SettingVisibilityCondition condition11 = () -> this.isMaxXAbsDistVisible();
      this.maxXAbsDistInRotationModule = stringValue9.visibleWhen(condition11);
      StringValue stringValue10 = new StringValue(this, "MaxYAbsDistInRotationModule", 20.0, 0.0, 20.0, 2);
      String str28 = "MaxYAbsDist";
      StringValue stringValue11 = stringValue10.named(str28);
      SettingVisibilityCondition condition12 = () -> this.isMaxYAbsDistVisible();
      this.maxYAbsDistInRotationModule = stringValue11.visibleWhen(condition12);
      RangeSetting rangeSetting5 = new RangeSetting(this, "XRelDistInRotationModule", 0.5, 10.0, 0.0, 20.0, 2);
      String str29 = "MaxXRelDist";
      String str30 = "MinXRelDist";
      RangeSetting rangeSetting6 = rangeSetting5.withBoundNames(str30, str29);
      SettingVisibilityCondition condition13 = () -> this.isXRelDistVisible();
      this.xRelDistInRotationModule = rangeSetting6.visibleWhen(condition13);
      RangeSetting rangeSetting7 = new RangeSetting(this, "YRelDistInRotationModule", 0.5, 10.0, 0.0, 20.0, 2);
      String str31 = "MaxYRelDist";
      String str32 = "MinYRelDist";
      RangeSetting rangeSetting8 = rangeSetting7.withBoundNames(str32, str31);
      SettingVisibilityCondition condition14 = () -> {
         return this.randomizeInRotationModule.getSelectedValue().equals("Advanced");
      };
      this.yRelDistInRotationModule = rangeSetting8.visibleWhen(condition14);
      StringValue stringValue12 = new StringValue(this, "MaxRotationAngleInRotationModule", 90.0, 0.0, 180.0, 0);
      String str33 = "MaxRotationAngle";
      StringValue stringValue13 = stringValue12.named(str33);
      SettingVisibilityCondition condition15 = () -> this.isMaxRotationAngleVisible();
      this.maxRotationAngleInRotationModule = stringValue13.visibleWhen(condition15);
      StringValue stringValue14 = new StringValue(this, "RandomStrengthInRotationModule", 1.25, 0.01, 5.0, 2);
      String str34 = "RandomStrength";
      StringValue stringValue15 = stringValue14.named(str34);
      SettingVisibilityCondition condition16 = () -> this.isRandomStrengthVisible();
      this.randomStrengthInRotationModule = stringValue15.visibleWhen(condition16);
      BooleanValue booleanValue9 = new BooleanValue(this, "MoveFixInRotationModule", true);
      String str35 = "MoveFix";
      this.moveFixInRotationModule = (BooleanValue)booleanValue9.withDisplayName(str35);
      BooleansSetting booleansSetting4 = new BooleansSetting(this, "SilentMoveFix", "Default", new String[]{"None", "Default", "Advanced"});
      String str36 = "SilentMoveFix";
      this.silentMoveFix = (BooleansSetting)booleansSetting4.withDisplayName(str36);
      BooleanValue booleanValue10 = new BooleanValue(this, "SilentMoveFixIntaveInRotationModule", true);
      String str37 = "SilentMoveFixIntave";
      this.silentMoveFixIntaveInRotationModule = (BooleanValue)booleanValue10.withDisplayName(str37);
      this.rotating = false;
      this.firstRotationTick = true;
      this.rotationUtil = new RotationUtil();
   }

   public Rotation calculateRotationToEntity(Entity entity) {
      Vec3 targetHitVec = this.getTargetHitVec(entity);
      if (targetHitVec != null) {
         double zCoord = targetHitVec.zCoord;
         double yCoord = targetHitVec.yCoord;
         double xCoord = targetHitVec.xCoord;
         return this.calculateRotationToPos(xCoord, yCoord, zCoord);
      } else {
         return null;
      }
   }

   public Rotation calculateInterpolatedRotationToEntity(Entity entity, float f) {
      Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
      Vec3 vec = this.getInterpolatedTargetHitVec(entity, f, previousRotation);
      if (vec != null) {
         double zCoord = vec.zCoord;
         double yCoord = vec.yCoord;
         double xCoord = vec.xCoord;
         return this.calculateInterpolatedRotationToPos(xCoord, yCoord, zCoord, f);
      } else {
         return null;
      }
   }

   public Rotation calculateSmoothRotationToEntity(Entity entity, Rotation rotation, float f, float f2, float f3) {
      Vec3 vec = this.getInterpolatedTargetHitVec(entity, f, rotation);
      if (vec != null) {
         double zCoord = vec.zCoord;
         double yCoord = vec.yCoord;
         double xCoord = vec.xCoord;
         return this.calculateSmoothRotationToEntityPos(entity, xCoord, yCoord, zCoord, rotation, f2, f3, f);
      } else {
         return null;
      }
   }

   private Vec3 getTargetHitVec(Entity entity) {
      Vec3 vec = null;
      String selectedValue = this.rotationModeInEntityRotationModule.getSelectedValue();
      Categorys.isControlFlowFlag();
      switch (selectedValue) {
         case "Head":
            vec = new Vec3(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ);
            break;
         case "BestHitVec":
            int value = (int)this.hitBoxPercentageVerticalInEntityRotationModule.getValue();
            int value2 = (int)this.hitBoxPercentageHorizontalInEntityRotationModule.getValue();
            int n = value;
            vec = RotationUtil.getBestHitVec(entity, n, value2);
            break;
         case "NearestHitVec":
            int value3 = (int)this.hitBoxPercentageVerticalInEntityRotationModule1.getValue();
            int value4 = (int)this.hitBoxPercentageHorizontalInEntityRotationModule1.getValue();
            Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
            int n2 = value4;
            int n3 = value3;
            vec = RotationUtil.getNearestLookPointOnEntity(entity, n3, n2, previousRotation);
      }

      return vec;
   }

   private Vec3 getInterpolatedTargetHitVec(Entity entity, float f, Rotation rotation) {
      Vec3 vec = null;
      Categorys.isControlFlowFlagCleared();
      String selectedValue = this.rotationModeInEntityRotationModule.getSelectedValue();
      switch (selectedValue) {
         case "Head":
            vec = new Vec3(
               entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)mc.getTimer().renderPartialTicks,
               entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)mc.getTimer().renderPartialTicks + (double)entity.getEyeHeight(),
               entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)mc.getTimer().renderPartialTicks
            );
            break;
         case "BestHitVec":
            int value = (int)this.hitBoxPercentageVerticalInEntityRotationModule.getValue();
            int value2 = (int)this.hitBoxPercentageHorizontalInEntityRotationModule.getValue();
            int n = value;
            vec = RotationUtil.getInterpolatedBestHitVec(entity, f, n, value2);
            break;
         case "NearestHitVec":
            int value3 = (int)this.hitBoxPercentageVerticalInEntityRotationModule1.getValue();
            int value4 = (int)this.hitBoxPercentageHorizontalInEntityRotationModule1.getValue();
            int n2 = value4;
            int n3 = value3;
            vec = RotationUtil.getInterpolatedNearestLookPoint(entity, f, n3, n2, rotation);
      }

      return vec;
   }

   public Rotation applyRotation(float[] floats) {
      float f = floats[0];
      float f2 = floats[1];
      float f3 = f;
      return this.applyRotation(f3, f2);
   }

   public Rotation applyRotation2(Rotation rotation) {
      float pitch = rotation.pitch;
      float yaw = rotation.yaw;
      return this.applyRotation(yaw, pitch);
   }

   public Rotation calculateRotationToVec(Vec3 vec) {
      double zCoord = vec.zCoord;
      double yCoord = vec.yCoord;
      double xCoord = vec.xCoord;
      return this.calculateRotationToPos(xCoord, yCoord, zCoord);
   }

   public Rotation calculateRotationToPos(double d, double d2, double d3) {
      double d4 = d - mc.thePlayer.posX;
      double d5 = d2 - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
      double d6 = d3 - mc.thePlayer.posZ;
      float f = (float)(MathHelper.atan2(d6, d4) * 180.0 / Math.PI - 90.0);
      float f2 = (float)(-(MathHelper.atan2(d5, (double)MathHelper.sqrt_double(d4 * d4 + d6 * d6)) * 180.0 / Math.PI));
      return this.applyRotation(f, f2);
   }

   public Rotation calculateSmoothRotationToEntityPos(Entity entity, double d, double d2, double d3, Rotation rotation, float f, float f2, float f3) {
      double d4 = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * (double)f3;
      double d5 = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * (double)f3;
      double d6 = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * (double)f3;
      double d7 = d - d4;
      double d8 = d2 - (d5 + (double)mc.thePlayer.getEyeHeight());
      double d9 = d3 - d6;
      float f4 = (float)(MathHelper.atan2(d9, d7) * 180.0 / Math.PI - 90.0);
      float f5 = (float)(-(MathHelper.atan2(d8, (double)MathHelper.sqrt_double(d7 * d7 + d9 * d9)) * 180.0 / Math.PI));
      return this.computeSmoothedRotation(entity, f4, f5, rotation, f, f2);
   }

   public Rotation calculateInterpolatedRotationToPos(double d, double d2, double d3, float f) {
      double d4 = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * (double)f;
      double d5 = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * (double)f;
      double d6 = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * (double)f;
      double d7 = d - d4;
      double d8 = d2 - (d5 + (double)mc.thePlayer.getEyeHeight());
      double d9 = d3 - d6;
      float f2 = (float)(MathHelper.atan2(d9, d7) * 180.0 / Math.PI - 90.0);
      float f3 = (float)(-(MathHelper.atan2(d8, (double)MathHelper.sqrt_double(d7 * d7 + d9 * d9)) * 180.0 / Math.PI));
      return this.applyRotation(f2, f3);
   }

   public Rotation applyRotation(float f, float f2) {
      Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
      Object obj = null;
      return this.applyRotationWithRandomSpeed((EntityLivingBase)obj, f, f2, previousRotation);
   }

   public Rotation applyRotationWithRandomSpeed(EntityLivingBase entity, float f, float f2, Rotation rotation) {
      RandomNumberGenerator instance = DefaultRandomGenerator.instance;
      double minValue = this.yawSpeedInRotationModule.getMinValue();
      double maxValue = this.yawSpeedInRotationModule.getMaxValue();
      double d = minValue;
      float f3 = instance.nextSecureFloat(d, maxValue);
      instance = DefaultRandomGenerator.instance;
      minValue = this.pitchSpeedInRotationModule.getMinValue();
      double maxValue2 = this.pitchSpeedInRotationModule.getMaxValue();
      double d2 = minValue;
      float f4 = instance.nextSecureFloat(d2, maxValue2);
      return this.computeSmoothedRotation(entity, f, f2, rotation, f3, f4);
   }

   public Rotation computeSmoothedRotation(Entity entity, float f, float f2, Rotation rotation, float f3, float f4) {
      boolean flag = Categorys.isControlFlowFlagCleared();
      float f5;
      float f6;
      if (this.interpolationInRotationModule.getValue()) {
         f5 = RotationUtil.interpolateAngleIfLarge(rotation.yaw, f, f3 / 180.0F);
         f6 = RotationUtil.interpolateAngleIfLarge(rotation.pitch, f2, f4 / 180.0F);
      } else {
         f5 = RotationUtil.updateAngle(rotation.yaw, f, f3);
         f6 = RotationUtil.updateAngle(rotation.pitch, f2, f4);
      }

      String selectedValue = this.randomizeInRotationModule.getSelectedValue();
      switch (selectedValue) {
         case "Basic":
            f5 += this.computeRandomOffset(f6);
            f6 += this.computeRandomOffset(f5);
            break;
         case "OnlyRotation":
            double d = (double)MathHelper.wrapAngleTo180_float(f - rotation.yaw);
            double d2 = (double)MathHelper.wrapAngleTo180_float(f2 - rotation.pitch);
            if (!((double)(-f3) <= d) || !(d <= (double)f3) || !((double)(-f4) <= d2) || !(d2 <= (double)f4)) {
               f5 += this.computeRandomOffset(f6);
               f6 += this.computeRandomOffset(f5);
            }
            break;
         case "Doubled":
            float f7 = this.computeRandomOffset(f6);
            float f8 = this.computeRandomOffset(f6);
            float f9 = this.computeRandomOffset(f6);
            float f10 = this.computeRandomOffset(f6);
            float f11 = this.computeRandomOffset(f5);
            float f12 = this.computeRandomOffset(f5);
            float f13 = this.computeRandomOffset(f5);
            float f14 = this.computeRandomOffset(f5);
            RandomNumberGenerator instance = DefaultRandomGenerator.instance;
            double d3 = (double)Math.max(f7, f8);
            double d4 = (double)Math.min(f7, f8);
            f5 += instance.nextSecureFloat(d4, d3);
            instance = DefaultRandomGenerator.instance;
            double d5 = (double)Math.max(f9, f10);
            double d6 = (double)Math.min(f9, f10);
            f5 += instance.nextSecureFloat(d6, d5);
            instance = DefaultRandomGenerator.instance;
            double d7 = (double)Math.max(f11, f12);
            double d8 = (double)Math.min(f11, f12);
            f6 += instance.nextSecureFloat(d8, d7);
            instance = DefaultRandomGenerator.instance;
            double d9 = (double)Math.max(f13, f14);
            double d10 = (double)Math.min(f13, f14);
            f6 += instance.nextSecureFloat(d10, d9);
            break;
         case "Advanced":
            if (entity instanceof EntityLivingBase) {
               RotationUtil rotationUtil2 = this.rotationUtil;
               boolean firstRotationTick2 = this.firstRotationTick;
               EntityLivingBase entity2 = (EntityLivingBase)entity;
               float yaw = rotation.yaw;
               float pitch = rotation.pitch;
               String selectedValue2 = this.randomTypeInRotationModule.getSelectedValue();
               double value = this.reduceRotationPercentageInRotationModule.getValue();
               boolean value2 = this.reducedRotateInRotationModule.getValue();
               boolean value3 = this.pitchOutliersRotationModule.getValue();
               RandomizedRangeSettings randomizedRangeSettings = this.pitchOutliersAddRotationModule;
               RandomizedRangeSettings randomizedRangeSettings2 = this.pitchOutliersDelayRotationModule;
               double d11 = ClientManagers.moduleManager.range.isToggled() ? ClientManagers.moduleManager.range.getCurrentRange() : 3.0;
               boolean value4 = this.smartRotateInRotationModule.getValue();
               float value5 = (float)this.maxXAbsDistInRotationModule.getValue();
               float value6 = (float)this.maxYAbsDistInRotationModule.getValue();
               float value7 = (float)this.minAbsDistInRotationModule.getValue();
               float minValue = (float)this.xRelDistInRotationModule.getMinValue();
               float minValue2 = (float)this.yRelDistInRotationModule.getMinValue();
               float maxValue = (float)this.xRelDistInRotationModule.getMaxValue();
               float maxValue2 = (float)this.yRelDistInRotationModule.getMaxValue();
               float value8 = (float)this.maxRotationAngleInRotationModule.getValue();
               float minValue3 = (float)this.innerRadiusInRotationModule.getMinValue();
               float value9 = (float)this.bestHitXMinDistInRotationModule.getValue();
               float value10 = (float)this.bestHitYMInDistInRotationModule.getValue();
               float f15 = value9;
               float f16 = minValue3;
               float f17 = value8;
               float f18 = maxValue2;
               float f19 = maxValue;
               float f20 = minValue2;
               float f21 = minValue;
               float f22 = value7;
               float f23 = value6;
               float f24 = value5;
               boolean flag2 = value4;
               double d12 = d11;
               RandomizedRangeSettings randomizedRangeSettings3 = randomizedRangeSettings2;
               RandomizedRangeSettings randomizedRangeSettings4 = randomizedRangeSettings;
               boolean flag3 = value3;
               boolean flag4 = value2;
               double d13 = value;
               String str = selectedValue2;
               float f25 = pitch;
               float f26 = yaw;
               EntityLivingBase entity3 = entity2;
               boolean flag5 = firstRotationTick2;
               Rotation rotation2 = rotationUtil2.computeAdvancedRotation(
                  flag5,
                  entity3,
                  f5,
                  f6,
                  f26,
                  f25,
                  str,
                  f,
                  f2,
                  d13,
                  flag4,
                  flag3,
                  randomizedRangeSettings4,
                  randomizedRangeSettings3,
                  d12,
                  flag2,
                  f24,
                  f23,
                  f22,
                  f21,
                  f20,
                  f19,
                  f18,
                  f17,
                  f16,
                  f15,
                  value10
               );
               this.firstRotationTick = false;
               f5 = rotation2.yaw;
               f6 = rotation2.pitch;
            }
      }

      if (this.advancedRotsInRotationModule.getValue()) {
         f6 = (float)((double)f6 + Math.sin(0.06981317007977318 * (double)(RotationUtil.updateAngle(rotation.yaw, f, 180.0F) - f5)) * 8.0);
      }

      return RotationUtil.applyGcdFix(f5, f6, rotation.yaw, rotation.pitch);
   }

   private float computeRandomOffset(float f) {
      double value = this.randomStrengthInRotationModule.getValue();
      String selectedValue = this.randomTypeInRotationModule.getSelectedValue();
      Categorys.isControlFlowFlag();
      switch (selectedValue) {
         case "Random":
            double d = -value;
            return DefaultRandomGenerator.instance.nextFloat2(d, value);
         case "Gaussian":
            return (float)(ThreadLocalRandom.current().nextGaussian() * value);
         case "SecureRandom":
            double d2 = -value;
            return DefaultRandomGenerator.instance.nextSecureFloat(d2, value);
         case "Intave":
            double d3 = 2.0;
            double d4 = 1.0;
            return (float)((double)((float)((double)DefaultRandomGenerator.instance.nextSecureFloat(d4, d3) * value)) * Math.sin((double)f * Math.PI));
         default:
            return 1.0F;
      }
   }

   @Override
   public void onMove(EventMove event) {
      boolean isControlFlowFlag = Categorys.isControlFlowFlag();
      if (this.rotating && !this.moveFixInRotationModule.getValue()) {
         float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
         event.setYaw(realYaw);
      }
   }

   @Override
   public void onMove2(EventMove event) {
      boolean flag = Categorys.isControlFlowFlagCleared();
      if (this.rotating && !this.moveFixInRotationModule.getValue()) {
         float realYaw = Augustus.getInstance().getYawPitchHelper().realYaw;
         event.setYaw(realYaw);
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      boolean flag = Categorys.isControlFlowFlagCleared();
      if (this.rotating && this.moveFixInRotationModule.getValue() && !this.silentMoveFix.getSelectedValue().equals("None") && ClientManagers.rotationController.isOwnedBy(this)) {
         if (this.silentMoveFix.getSelectedValue().equals("Advanced")) {
            MoveFixMode advanced = MoveFixMode.ADVANCED;
            event.setMoveFixMode(advanced);
         } else {
            MoveFixMode moveFixMode = MoveFixMode.DEFAULT;
            event.setMoveFixMode(moveFixMode);
         }

         if (this.silentMoveFixIntaveInRotationModule.getValue()) {
            boolean flag2 = true;
            event.setPreventInstantReverse(flag2);
         }
      }
   }

   public void initVisibilityConditions() {
      StringValue stringValue = this.hitBoxPercentageHorizontalInEntityRotationModule;
      SettingVisibilityCondition condition = () -> this.isBestHitHorizontalVisible();
      stringValue.visibleWhen(condition);
      stringValue = this.hitBoxPercentageVerticalInEntityRotationModule;
      SettingVisibilityCondition condition2 = () -> this.isBestHitVerticalVisible();
      stringValue.visibleWhen(condition2);
      stringValue = this.hitBoxPercentageHorizontalInEntityRotationModule1;
      SettingVisibilityCondition condition3 = () -> this.isNearestHitHorizontalVisible();
      stringValue.visibleWhen(condition3);
      stringValue = this.hitBoxPercentageVerticalInEntityRotationModule1;
      SettingVisibilityCondition condition4 = () -> this.isNearestHitVerticalVisible();
      stringValue.visibleWhen(condition4);
      BooleansSetting silentMoveFix2 = this.silentMoveFix;
      SettingVisibilityCondition condition5 = () -> this.isSilentMoveFixVisible();
      silentMoveFix2.withVisibility(condition5);
      BooleanValue booleanValue = this.silentMoveFixIntaveInRotationModule;
      SettingVisibilityCondition condition6 = () -> {
         boolean flag = Categorys.isControlFlowFlagCleared();
         return this.silentMoveFix.isVisible() && !this.silentMoveFix.getSelectedValue().equals("None");
      };
      booleanValue.withVisibility(condition6);
   }

   private boolean isSilentMoveFixVisible() {
      boolean flag = Categorys.isControlFlowFlagCleared();
      return this.moveFixInRotationModule.getValue() && this.moveFixInRotationModule.isVisible();
   }

   private boolean isNearestHitVerticalVisible() {
      return this.rotationModeInEntityRotationModule.getSelectedValue().equalsIgnoreCase("NearestHitVec");
   }

   private boolean isNearestHitHorizontalVisible() {
      return this.rotationModeInEntityRotationModule.getSelectedValue().equalsIgnoreCase("NearestHitVec");
   }

   private boolean isBestHitVerticalVisible() {
      return this.rotationModeInEntityRotationModule.getSelectedValue().equalsIgnoreCase("BestHitVec");
   }

   private boolean isBestHitHorizontalVisible() {
      return this.rotationModeInEntityRotationModule.getSelectedValue().equalsIgnoreCase("BestHitVec");
   }

   private boolean isRandomStrengthVisible() {
      boolean isControlFlowFlag = Categorys.isControlFlowFlag();
      return !this.randomizeInRotationModule.getSelectedValue().equals("Advanced") && !this.randomizeInRotationModule.getSelectedValue().equals("None");
   }

   private boolean isMaxRotationAngleVisible() {
      return this.randomizeInRotationModule.getSelectedValue().equals("Advanced");
   }

   private boolean isXRelDistVisible() {
      return this.randomizeInRotationModule.getSelectedValue().equals("Advanced");
   }

   private boolean isMaxYAbsDistVisible() {
      return this.randomizeInRotationModule.getSelectedValue().equals("Advanced");
   }

   private boolean isMaxXAbsDistVisible() {
      return this.randomizeInRotationModule.getSelectedValue().equals("Advanced");
   }

   private boolean isMinAbsDistVisible() {
      return this.randomizeInRotationModule.getSelectedValue().equals("Advanced");
   }

   private boolean isBestHitYMinDistVisible() {
      return this.randomizeInRotationModule.getSelectedValue().equals("Advanced");
   }

   private boolean isBestHitXMinDistVisible() {
      return this.randomizeInRotationModule.getSelectedValue().equals("Advanced");
   }

   private boolean isPitchOutliersDelayVisible() {
      boolean flag = Categorys.isControlFlowFlagCleared();
      return this.randomizeInRotationModule.getSelectedValue().equals("Advanced") && this.pitchOutliersRotationModule.getValue();
   }

   private boolean isPitchOutliersAddVisible() {
      boolean flag = Categorys.isControlFlowFlagCleared();
      return this.randomizeInRotationModule.getSelectedValue().equals("Advanced") && this.pitchOutliersRotationModule.getValue();
   }

   private boolean isPitchOutliersVisible() {
      return this.randomizeInRotationModule.getSelectedValue().equals("Advanced");
   }

   private boolean isSmartRotateVisible() {
      return this.randomizeInRotationModule.getSelectedValue().equals("Advanced");
   }

   private boolean isReduceRotationPercentageVisible() {
      boolean isControlFlowFlag = Categorys.isControlFlowFlag();
      return this.randomizeInRotationModule.getSelectedValue().equals("Advanced") && this.reducedRotateInRotationModule.getValue();
   }

   private boolean isReducedRotateVisible() {
      return this.randomizeInRotationModule.getSelectedValue().equals("Advanced");
   }


}
