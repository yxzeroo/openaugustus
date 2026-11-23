package net.augustus.modules.legit;

import net.minecraft.util.MovingObjectPosition;
import java.awt.Color;
import java.util.Optional;
import java.util.function.ToDoubleFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.settings.WeaponFilterSettings;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.LookDownDisableSettings;
import net.augustus.events.listeners.MouseMoveListener;
import net.augustus.utils.rotation.HitboxAngleUtil;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.utils.math.RandomNumberGenerator;
import net.augustus.utils.math.DefaultRandomGenerator;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.utils.combat.EntityTargetFilter;
import net.augustus.events.EventLoadWorld;
import net.augustus.utils.rotation.EntityBoundsUtil;
import net.augustus.utils.combat.TargetFilterPresets;
import net.augustus.utils.combat.EntityTargetSelector;
import net.augustus.events.EventMouseMove;
import net.augustus.utils.TickCounter;
import net.augustus.Augustus;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.TargetSelectionSettings;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class AimAssist extends Module implements LoadWorldListener, TickListener, MouseMoveListener {
   private final RandomizedRangeSettings preAimRange;
   private BooleansSetting mode;
   private SettingVisibilityCondition stableModeCondition;
   private SettingVisibilityCondition experimentalModeCondition;
   private StringValue strength;
   private StringValue fasterMouseMovement;
   private final DoubleValue improveYourAimInput;
   private final StringValue ownHorizontalRotationMultiplier;
   private final StringValue ownVerticalRotationMultiplier;
   private final StringValue horizontalAssistMultiplier;
   private final StringValue verticalAssistMultiplier;
   private final DoubleValue autoAim;
   private final RandomizedRangeSettings horizontalSpeed;
   private final RandomizedRangeSettings verticalSpeed;
   private final StringValue horizontalSmoothness;
   private final StringValue verticalSmoothness;
   private final BooleanValue smoothEarly;
   private final BooleansSetting noRotationFovMode;
   private final StringValue noRotationHorizontalFov;
   private final StringValue noRotationVerticalFov;
   private final StringValue noRotationHorizontal;
   private final StringValue noRotationVertical;
   private final StringValue horizontalBoxSize;
   private final StringValue randomizationStrength;
   private final BooleanValue randomizeInNoRotationFovs;
   private final StringValue horizontalFov;
   private final StringValue verticalFov;
   private final BooleanValue click;
   private final BooleanValue diggingCheck;
   private final StringValue aimTickDelayAfterDigging;
   private final TargetSelectionSettings targetSelectionSettings;
   private final BooleanValue selectTargetThroughWalls;
   private final WeaponFilterSettings weaponFilterSettings;
   private final LookDownDisableSettings lookDownDisableSettings;
   private final TickCounter tickCounter;
   private float lastPartialTicks;
   public EntityLivingBase target;
   private final double[] assistDeltaRemainder;
   private final double[] ownDeltaRemainder;
   private int[] previousMouseDelta;
   private int[] targetMouseDelta;
   private double[] accumulatedMouseDelta;
   private int[] appliedMouseDelta;
   private long lastTickTimeMs;
   private int tickDurationMs;
   private long lastFrameTimeMs;

   public AimAssist() {
       super("AimAssist", "Ziehlhilfe", new Color(66, 135, 245), Categorys.LEGIT);
      this.preAimRange = (new RandomizedRangeSettings(this, "Pre aim range", 5.0, 5.0, 0.0, 10.0, 1)).withoutNotInARow();
      this.mode = new BooleansSetting(this, "Mode", "Stable", new String[]{"Stable", "Experimental"});
      this.stableModeCondition = () -> this.isStableMode();
      this.experimentalModeCondition = () -> {
         return this.mode.getSelectedValue().equals("Experimental");
      };
      StringValue stringValue = new StringValue(this, "Strength", 2.0, 1.0, 5.0, 2);
      SettingVisibilityCondition condition = this.experimentalModeCondition;
      this.strength = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(this, "Faster mouse movement", 1.0, 1.0, 5.0, 2);
      SettingVisibilityCondition condition2 = this.experimentalModeCondition;
      this.fasterMouseMovement = stringValue2.visibleWhen(condition2);
      DoubleValue doubleValue = new DoubleValue(this, "Improve your aim input");
      SettingVisibilityCondition stableModeCondition2 = this.stableModeCondition;
      this.improveYourAimInput = doubleValue.visibleWhen(stableModeCondition2);
      this.ownHorizontalRotationMultiplier = new StringValue(this, "Own horizontal rotation multiplier", 1.0, 0.0, 1.0, 2, this.improveYourAimInput);
      this.ownVerticalRotationMultiplier = new StringValue(this, "Own vertical rotation multiplier", 1.0, 0.0, 1.0, 2, this.improveYourAimInput);
      this.horizontalAssistMultiplier = new StringValue(this, "Horizontal assist multiplier", 1.0, 0.0, 1.0, 2, this.improveYourAimInput);
      this.verticalAssistMultiplier = new StringValue(this, "Vertical assist multiplier", 1.0, 0.0, 1.0, 2, this.improveYourAimInput);
      DoubleValue doubleValue2 = new DoubleValue(this, "Auto aim");
      SettingVisibilityCondition stableModeCondition3 = this.stableModeCondition;
      this.autoAim = doubleValue2.visibleWhen(stableModeCondition3);
      this.horizontalSpeed = (new RandomizedRangeSettings(this, " horizontal speed", 4.0, 6.0, 0.0, 15.0, 1, this.autoAim)).withoutNotInARow();
      this.verticalSpeed = (new RandomizedRangeSettings(this, " vertical speed", 4.0, 6.0, 0.0, 15.0, 1, this.autoAim)).withoutNotInARow();
      this.horizontalSmoothness = new StringValue(this, "Horizontal smoothness", 5.0, 1.0, 10.0, 0, this.autoAim);
      this.verticalSmoothness = new StringValue(this, "Vertical smoothness", 5.0, 1.0, 10.0, 0, this.autoAim);
      this.smoothEarly = new BooleanValue(this, "Smooth early", true, this.autoAim);
      this.noRotationFovMode = new BooleansSetting(this, "NoRotationFovMode", "2D", new String[]{"2D", "3D"}, this.autoAim);
      StringValue stringValue3 = new StringValue(this, "No rotation horizontal fov", 5.0, 0.0, 15.0, 0, this.autoAim);
      SettingVisibilityCondition condition3 = () -> this.is2DNoRotationMode();
      this.noRotationHorizontalFov = stringValue3.visibleWhen(condition3);
      StringValue stringValue4 = new StringValue(this, "No rotation vertical fov", 5.0, 0.0, 15.0, 0, this.autoAim);
      SettingVisibilityCondition condition4 = () -> {
         return this.noRotationFovMode.getSelectedValue().equals("2D");
      };
      this.noRotationVerticalFov = stringValue4.visibleWhen(condition4);
      StringValue stringValue5 = new StringValue(this, "No rotation horizontal", 1.75, 0.0, 3.0, 2, this.autoAim);
      SettingVisibilityCondition condition5 = () -> {
         return this.noRotationFovMode.getSelectedValue().equals("3D");
      };
      this.noRotationHorizontal = stringValue5.visibleWhen(condition5);
      StringValue stringValue6 = new StringValue(this, "No rotation vertical", 1.0, 0.0, 3.0, 2, this.autoAim);
      SettingVisibilityCondition condition6 = () -> {
         return this.noRotationFovMode.getSelectedValue().equals("3D");
      };
      this.noRotationVertical = stringValue6.visibleWhen(condition6);
      this.horizontalBoxSize = new StringValue(this, "Horizontal box size", 20.0, 0.0, 100.0, 0, this.autoAim);
      this.randomizationStrength = new StringValue(this, "Randomization strength", 0.0, 0.0, 5.0, 0, this.autoAim);
      BooleanValue booleanValue = new BooleanValue(this, "Randomize in no rotation fovs", true, this.autoAim);
      SettingVisibilityCondition condition7 = () -> this.isRandomizationEnabled();
      this.randomizeInNoRotationFovs = (BooleanValue)booleanValue.withVisibility(condition7);
      this.horizontalFov = (StringValue)(new StringValue(this, "Horizontal fov", 90.0, 0.0, 180.0, 0)).markVisualOnly();
      this.verticalFov = (StringValue)(new StringValue(this, "Vertical fov", 90.0, 0.0, 180.0, 0)).markVisualOnly();
      this.click = (BooleanValue)(new BooleanValue(this, "Click", true)).markVisualOnly();
      this.diggingCheck = (BooleanValue)(new BooleanValue(this, "Digging check", true)).markVisualOnly();
      StringValue stringValue7 = new StringValue(this, "Aim tick delay after digging", 0.0, 0.0, 20.0, 0);
      BooleanValue diggingCheck2 = this.diggingCheck;
      this.diggingCheck.getClass();
      BooleanValue booleanValue2 = diggingCheck2;
      SettingVisibilityCondition condition8 = () -> booleanValue2.getValue();
      this.aimTickDelayAfterDigging = (StringValue)stringValue7.visibleWhen(condition8).markVisualOnly();
      this.targetSelectionSettings = (new TargetSelectionSettings(this, TargetFilterPresets.createCombatFilter())).withSorting();
      this.selectTargetThroughWalls = new BooleanValue(this, "SelectTargetThroughWalls", true);
      this.weaponFilterSettings = new WeaponFilterSettings(this);
      this.lookDownDisableSettings = new LookDownDisableSettings(this);
      this.tickCounter = new TickCounter();
      this.lastPartialTicks = 0.0F;
      this.target = null;
      this.assistDeltaRemainder = new double[2];
      this.ownDeltaRemainder = new double[2];
      this.previousMouseDelta = new int[2];
      this.targetMouseDelta = new int[2];
      this.accumulatedMouseDelta = new double[2];
      this.appliedMouseDelta = new int[2];
      this.lastTickTimeMs = 0L;
      this.tickDurationMs = 0;
      this.lastFrameTimeMs = 0L;
   }

   @Override
   public void onEnable() {
      this.lastFrameTimeMs = System.currentTimeMillis();
      this.lastTickTimeMs = System.currentTimeMillis();
   }

   @Override
   public void onDisable() {
      this.target = null;
      Object obj = null;
      this.targetSelectionSettings.setCurrentTarget((Entity)obj);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
      byte b2 = 19;
      this.toggleEventSubscription(b2);
      byte b3 = 17;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onMouseMove(EventMouseMove event) {
      if (ClientManagers.moduleManager.freelook.isToggled()) {
         byte b = 0;
         event.setDeltaX(b);
         byte b2 = 0;
         event.setDeltaY(b2);
      }

      if (this.canAssist()) {
         if (this.mode.getSelectedValue().equals("Stable")) {
            this.applyStableAimAssist(event);
         } else {
            this.applyExperimentalAimAssist(event);
         }
      }

      this.lastPartialTicks = event.getPartialTicks();
      this.lastFrameTimeMs = System.currentTimeMillis();
   }

   private void applyExperimentalAimAssist(EventMouseMove event) {
      EntityLivingBase target2 = this.target;
      float partialTicks = event.getPartialTicks();
      Vec3 vec = RotationUtil.getInterpolatedNearestPoint(target2, partialTicks);
      double xCoord = vec.xCoord;
      double yCoord = vec.yCoord;
      double zCoord = vec.zCoord;
      Rotation rotation = new Rotation(this.getYaw(), this.getPitch());
      float partialTicks2 = event.getPartialTicks();
      boolean flag = false;
      float f = 180.0F;
      float f2 = 180.0F;
      Rotation rotation2 = rotation;
      Rotation rotation3 = RotationUtil.getInterpolatedRotationsToPos(xCoord, yCoord, zCoord, rotation2, f2, f, flag, partialTicks2);
      double d = EntityBoundsUtil.getShortestSignedAngleDelta(
         (double)MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw), MathHelper.wrapAngleTo180_double((double)rotation3.yaw)
      );
      double d2 = (double)(-(rotation3.pitch - mc.thePlayer.rotationPitch));
      if (mc.gameSettings.invertMouse && !event.isIgnoreInvertMouse()) {
         d2 *= -1.0;
      }

      boolean flag2 = true;
      double value = this.strength.getValue(flag2);
      boolean flag3 = true;
      double value2 = this.fasterMouseMovement.getValue(flag3);
      if ((double)Math.signum((float)event.getDeltaX()) == Math.signum(d)
         && (!((double)Math.abs(event.getDeltaX()) > Math.abs(d)) || !(Math.abs((double)event.getDeltaX() / value - d) < Math.abs(d - (double)event.getDeltaX())))) {
         if ((double)((int)((double)Math.abs(event.getDeltaX()) * value2)) < Math.abs(d)) {
            int n = (int)((double)event.getDeltaX() * value2);
            event.setDeltaX(n);
         }
      } else {
         int n2 = (int)((double)event.getDeltaX() / value);
         event.setDeltaX(n2);
      }

      if ((double)Math.signum((float)event.getDeltaY()) == Math.signum(d2)
         && (!((double)Math.abs(event.getDeltaY()) > Math.abs(d2)) || !(Math.abs((double)event.getDeltaY() / value - d2) < Math.abs(d2 - (double)event.getDeltaY())))) {
         if ((double)((int)((double)Math.abs(event.getDeltaY()) * value2)) < Math.abs(d2)) {
            int n3 = (int)((double)event.getDeltaY() * value2);
            event.setDeltaY(n3);
         }
      } else {
         int n4 = (int)((double)event.getDeltaY() / value);
         event.setDeltaY(n4);
      }
   }

   private void applyStableAimAssist(EventMouseMove event) {
      int n = (int)(System.currentTimeMillis() - this.lastFrameTimeMs);
      EntityLivingBase target2 = this.target;
      float partialTicks = event.getPartialTicks();
      Vec3 vec = RotationUtil.getInterpolatedNearestPoint(target2, partialTicks);
      double xCoord = vec.xCoord;
      double yCoord = vec.yCoord;
      double zCoord = vec.zCoord;
      Rotation rotation = new Rotation(this.getYaw(), this.getPitch());
      float partialTicks2 = event.getPartialTicks();
      boolean flag = false;
      float f = 180.0F;
      float f2 = 180.0F;
      Rotation rotation2 = rotation;
      Rotation rotation3 = RotationUtil.getInterpolatedRotationsToPos(xCoord, yCoord, zCoord, rotation2, f2, f, flag, partialTicks2);
      int[] ints = RotationUtil.computeMouseDeltas(rotation3.yaw, rotation3.pitch, this.getYaw(), this.getPitch());
      xCoord = vec.xCoord;
      yCoord = vec.yCoord;
      zCoord = vec.zCoord;
      rotation = new Rotation(this.getYaw(), this.getPitch());
      float f3 = 4.0F * (event.getPartialTicks() - this.lastPartialTicks);
      partialTicks2 = event.getPartialTicks();
      flag = false;
      f = 4.0F;
      f2 = f3;
      rotation2 = rotation;
      Rotation rotation4 = RotationUtil.getInterpolatedRotationsToPos(xCoord, yCoord, zCoord, rotation2, f2, f, flag, partialTicks2);
      int[] ints2 = RotationUtil.computeMouseDeltas(rotation4.yaw, rotation4.pitch, this.getYaw(), this.getPitch());
      double d = (double)this.targetMouseDelta[0] / (double)this.tickDurationMs * (double)n;
      double d2 = (double)this.targetMouseDelta[1] / (double)this.tickDurationMs * (double)n;
      this.accumulatedMouseDelta[0] = this.accumulatedMouseDelta[0] + d;
      this.accumulatedMouseDelta[1] = this.accumulatedMouseDelta[1] + d2;
      int n2 = (int)Math.round(this.accumulatedMouseDelta[0] - (double)this.appliedMouseDelta[0]);
      int n3 = (int)Math.round(this.accumulatedMouseDelta[1] - (double)this.appliedMouseDelta[1]);
      this.appliedMouseDelta[0] = this.appliedMouseDelta[0] + n2;
      this.appliedMouseDelta[1] = this.appliedMouseDelta[1] + n3;
      if (event.getDeltaX() != 0) {
         int n4 = ints[0];
         if (n4 < 0) {
            if (n4 - event.getDeltaX() <= n4) {
               double d3 = (double)event.getDeltaX() * this.horizontalAssistMultiplier.getValue();
               double d4 = d3 + this.assistDeltaRemainder[0];
               int n5 = (int)Math.round(d4);
               event.setDeltaX(n5);
               this.assistDeltaRemainder[0] = d4 - (double)n5;
            }
         } else if (n4 - event.getDeltaX() >= n4) {
            double d5 = (double)event.getDeltaX() * this.horizontalAssistMultiplier.getValue();
            double d6 = d5 + this.assistDeltaRemainder[0];
            int n6 = (int)Math.round(d6);
            event.setDeltaX(n6);
            this.assistDeltaRemainder[0] = d6 - (double)n6;
         }
      }

      if (event.getDeltaY() != 0) {
         int n7 = ints[1];
         if (n7 < 0) {
            if (n7 - event.getDeltaY() <= n7) {
               double d7 = (double)event.getDeltaY() * this.verticalAssistMultiplier.getValue();
               double d8 = d7 + this.assistDeltaRemainder[1];
               int n8 = (int)Math.round(d8);
               event.setDeltaY(n8);
               this.assistDeltaRemainder[1] = d8 - (double)n8;
            }
         } else if (n7 - event.getDeltaY() >= n7) {
            double d9 = (double)event.getDeltaY() * this.verticalAssistMultiplier.getValue();
            double d10 = d9 + this.assistDeltaRemainder[1];
            int n9 = (int)Math.round(d10);
            event.setDeltaY(n9);
            this.assistDeltaRemainder[1] = d10 - (double)n9;
         }
      }

      double d11 = (double)event.getDeltaX() * this.ownHorizontalRotationMultiplier.getValue();
      double d12 = d11 + this.ownDeltaRemainder[0];
      int n10 = (int)Math.round(d12);
      event.setDeltaX(n10);
      this.ownDeltaRemainder[0] = d12 - (double)n10;
      double d13 = (double)event.getDeltaY() * this.ownVerticalRotationMultiplier.getValue();
      double d14 = d13 + this.ownDeltaRemainder[1];
      int n11 = (int)Math.round(d14);
      event.setDeltaY(n11);
      this.ownDeltaRemainder[1] = d14 - (double)n11;
      int n12 = event.getDeltaX() + n2;
      event.setDeltaX(n12);
      int n13 = event.getDeltaY() + n3;
      event.setDeltaY(n13);
   }

   @Override
   public void onTick3() {
      this.tickDurationMs = (int)Math.abs(this.lastTickTimeMs - System.currentTimeMillis());
      this.lastTickTimeMs = System.currentTimeMillis();
      this.appliedMouseDelta[0] = this.appliedMouseDelta[1] = 0;
      this.accumulatedMouseDelta[0] = this.accumulatedMouseDelta[1] = 0.0;
   }

   @Override
   public void onTick5() {
      this.tickCounter.increment();
      this.lastPartialTicks = 0.0F;
      EntityLivingBase target2 = this.target;
      this.targetSelectionSettings.setCurrentTarget(target2);
      this.target = null;
      if (mc.isNotInGui() && (!this.click.getValue() || mc.gameSettings.keyBindAttack.isKeyDown()) && this.weaponFilterSettings.isHeldItemAllowed() && !this.lookDownDisableSettings.shouldDisable()) {
         this.target = this.findTarget();
         if (this.target != null) {
            EntityLivingBase target3 = this.target;
            int value = (int)this.horizontalBoxSize.getValue();
            byte b = 100;
            Vec3 bestHitVec = RotationUtil.getBestHitVec(target3, b, value);
            target3 = this.target;
            float yaw = this.getYaw();
            float pitch = this.getPitch();
            float f = yaw;
            float[] floats = RotationUtil.getAngleDiffToEntityNearestPoint(target3, f, pitch);
            float f2 = Math.abs(floats[0]);
            float f3 = Math.abs(floats[1]);
            target3 = this.target;
            float yaw2 = this.getYaw();
            float pitch2 = this.getPitch();
            float f4 = yaw2;
            float[] angleDiffToEntityEyes = RotationUtil.getAngleDiffToEntityEyes(target3, f4, pitch2);
            float f5 = Math.abs(angleDiffToEntityEyes[0]);
            float f6 = Math.abs(angleDiffToEntityEyes[1]);
            if (this.smoothEarly.getValue()) {
               if (this.noRotationFovMode.getSelectedValue().equals("2D")) {
                  f5 = Math.max(f5 - (float)this.noRotationHorizontalFov.getValue(), 0.0F);
                  f6 = Math.max(f6 - (float)this.noRotationVerticalFov.getValue(), 0.0F);
               } else {
                  float f7 = (float)(this.noRotationHorizontal.getValue() / 2.0);
                  float f8 = (float)(this.noRotationVertical.getValue() / 2.0);
                  if (f7 > 0.0F && f8 > 0.0F) {
                     Vec3 center = this.target.getEntityBoundingBox().getCenter();
                     AxisAlignedBB box = new AxisAlignedBB(
                        center.xCoord - (double)f7,
                        bestHitVec.yCoord - (double)f8,
                        center.zCoord - (double)f7,
                        center.xCoord + (double)f7,
                        bestHitVec.yCoord + (double)f8,
                        center.zCoord + (double)f7
                     );
                     Vec3 positionEyes = mc.thePlayer.getPositionEyes(1.0F);
                     target3 = this.target;
                     double[] extremeYawAngles = HitboxAngleUtil.getExtremeYawAngles(positionEyes, box);
                     double d = 1.0;
                     float f9 = (float)HitboxAngleUtil.lerpRange(extremeYawAngles, d);
                     double[] pitchRange = HitboxAngleUtil.getPitchRange(positionEyes, box);
                     double d2 = 1.0;
                     float f10 = (float)HitboxAngleUtil.lerpRange(pitchRange, d2);
                     float f11 = f9;
                     float[] angleDiffToEntityEyes2 = RotationUtil.getAngleDiffToEntityEyes(target3, f11, f10);
                     target3 = this.target;
                     double[] extremeYawAngles2 = HitboxAngleUtil.getExtremeYawAngles(positionEyes, box);
                     double d3 = 0.0;
                     float f12 = (float)HitboxAngleUtil.lerpRange(extremeYawAngles2, d3);
                     pitchRange = HitboxAngleUtil.getPitchRange(positionEyes, box);
                     double d4 = 0.0;
                     float f13 = (float)HitboxAngleUtil.lerpRange(pitchRange, d4);
                     float f14 = f12;
                     float[] angleDiffToEntityEyes3 = RotationUtil.getAngleDiffToEntityEyes(target3, f14, f13);
                     if (angleDiffToEntityEyes[0] < 0.0F) {
                        f5 = Math.max(f5 - Math.abs(angleDiffToEntityEyes2[0]), 0.0F);
                     } else {
                        f5 = Math.max(f5 - Math.abs(angleDiffToEntityEyes3[0]), 0.0F);
                     }

                     if (angleDiffToEntityEyes[1] < 0.0F) {
                        f6 = Math.max(f6 - Math.abs(angleDiffToEntityEyes2[1]), 0.0F);
                     } else {
                        f6 = Math.max(f6 - Math.abs(angleDiffToEntityEyes3[1]), 0.0F);
                     }
                  }
               }
            }

            float f15 = MathHelper.clamp_float((float)this.horizontalSpeed.nextValue(), 0.0F, 180.0F);
            float f16 = MathHelper.clamp_float((float)this.verticalSpeed.nextValue(), 0.0F, 180.0F);
            float f17 = MathHelper.clamp_float((float)this.horizontalSmoothness.getValue(), 1.0F, 10.0F);
            float f18 = MathHelper.clamp_float((float)this.verticalSmoothness.getValue(), 1.0F, 10.0F);
            float f19 = f15 == 0.0F ? 0.0F : f15 * (MathHelper.clamp_float(Math.abs(f5), f15, f15 * f17) / (f15 * f17));
            float f20 = f16 == 0.0F ? 0.0F : f16 * (MathHelper.clamp_float(Math.abs(f6), f16, f16 * f18) / (f16 * f18));
            double xCoord = bestHitVec.xCoord;
            double yCoord = bestHitVec.yCoord;
            double zCoord = bestHitVec.zCoord;
            Rotation rotation = new Rotation(this.getYaw(), this.getPitch());
            boolean flag = false;
            Rotation rotationsToPos = RotationUtil.getRotationsToPos(xCoord, yCoord, zCoord, rotation, f19, f20, flag);
            this.targetMouseDelta = RotationUtil.computeMouseDeltas(rotationsToPos.yaw, rotationsToPos.pitch, this.getYaw(), this.getPitch());
            boolean flag2 = false;
            boolean flag3 = false;
            if (this.noRotationFovMode.getSelectedValue().equals("2D")) {
               if ((double)f2 < this.noRotationHorizontalFov.getValue()) {
                  this.targetMouseDelta[0] = 0;
                  flag2 = true;
               }

               if ((double)f3 < this.noRotationVerticalFov.getValue()) {
                  this.targetMouseDelta[1] = 0;
                  flag3 = true;
               }
            } else {
               float f21 = (float)(this.noRotationHorizontal.getValue() / 2.0);
               float f22 = (float)(this.noRotationVertical.getValue() / 2.0);
               if (f21 > 0.0F && f22 > 0.0F) {
                  byte b2 = 100;
                  byte b3 = 100;
                  bestHitVec = RotationUtil.getBestHitVec(this.target, b3, b2);
                  Vec3 center2 = this.target.getEntityBoundingBox().getCenter();
                  AxisAlignedBB box2 = new AxisAlignedBB(
                     center2.xCoord - (double)f21,
                     bestHitVec.yCoord - (double)f22,
                     center2.zCoord - (double)f21,
                     center2.xCoord + (double)f21,
                     bestHitVec.yCoord + (double)f22,
                     center2.zCoord + (double)f21
                  );
                  Vec3 positionEyes2 = mc.thePlayer.getPositionEyes(1.0F);
                  double d5 = (double)(rotationsToPos.yaw % 360.0F);
                  double normalizedYawInBox = HitboxAngleUtil.getNormalizedYawInBox(box2, positionEyes2, d5);
                  double pitch3 = (double)rotationsToPos.pitch;
                  double normalizedPitchInBox = HitboxAngleUtil.getNormalizedPitchInBox(box2, positionEyes2, pitch3);
                  if (normalizedYawInBox > 0.0 && normalizedYawInBox < 1.0) {
                     this.targetMouseDelta[0] = 0;
                     flag2 = true;
                  }

                  if (normalizedPitchInBox > 0.0 && normalizedPitchInBox < 1.0) {
                     this.targetMouseDelta[1] = 0;
                     flag3 = true;
                  }
               }
            }

            if (this.randomizationStrength.getValue() > 0.0) {
               if (this.randomizeInNoRotationFovs.getValue() || !flag2) {
                  int[] targetMouseDelta2 = this.targetMouseDelta;
                  int n = this.targetMouseDelta[0];
                  RandomNumberGenerator instance = DefaultRandomGenerator.instance;
                  double d6 = -this.randomizationStrength.getValue();
                  double value2 = this.randomizationStrength.getValue();
                  double d7 = value2;
                  double d8 = d6;
                  targetMouseDelta2[0] = n + instance.nextInt(d8, d7);
               }

               if (this.randomizeInNoRotationFovs.getValue() || !flag3) {
                  int[] targetMouseDelta3 = this.targetMouseDelta;
                  int n2 = this.targetMouseDelta[1];
                  RandomNumberGenerator instance2 = DefaultRandomGenerator.instance;
                  double d9 = -this.randomizationStrength.getValue();
                  double value3 = this.randomizationStrength.getValue();
                  double d10 = value3;
                  double d11 = d9;
                  targetMouseDelta3[1] = n2 + instance2.nextInt(d11, d10);
               }
            }
         } else {
            this.preAimRange.nextValue();
         }
      }
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      this.target = null;
      Object obj = null;
      this.targetSelectionSettings.setCurrentTarget((Entity)obj);
   }

   private boolean canAssist() {
      if (!mc.thePlayer.isDead && mc.thePlayer.ticksExisted > 20 && this.target != null && mc.isNotInGui() && (!this.click.getValue() || mc.gameSettings.keyBindAttack.isKeyDown())) {
         AutoRod autoRod = ClientManagers.moduleManager.autoRod;
         if (!ClientManagers.rotationController.isOwnedBy(autoRod) && this.passesDiggingCheck()) {
            return true;
         }
      }

      return false;
   }

   private boolean passesDiggingCheck() {
      boolean flag = this.diggingCheck.getValue() && mc.gameSettings.keyBindAttack.isKeyDown() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK;
      if (flag) {
         this.tickCounter.reset();
      }

      TickCounter tickCounter2 = this.tickCounter;
      double value = this.aimTickDelayAfterDigging.getValue();
      return tickCounter2.hasReached(value) && !flag;
   }

   private float getYaw() {
      return Augustus.getInstance().getYawPitchHelper().realYaw;
   }

   private float getPitch() {
      return Augustus.getInstance().getYawPitchHelper().realPitch;
   }

   private EntityLivingBase findTarget() {
      EntityTargetSelector entityTargetSelector = EntityTargetSelector.of(mc.theWorld.loadedEntityList);
      EntityTargetFilter targetFilter = this.targetSelectionSettings.getTargetFilter();
      EntityTargetFilter entityTargetFilter = targetFilter;
      entityTargetSelector = entityTargetSelector.withTargetFilter(entityTargetFilter);
      boolean value = this.selectTargetThroughWalls.getValue();
      entityTargetSelector = entityTargetSelector.throughWalls(value);
      double lastValue = this.preAimRange.getLastValue();
      entityTargetSelector = entityTargetSelector.maxDistance(lastValue);
      double value2 = this.horizontalFov.getValue();
      double value3 = this.verticalFov.getValue();
      double d = value2;
      entityTargetSelector = entityTargetSelector.withinFov(d, value3).filterFocus();
      TargetSelectionSettings targetSelectionSettings2 = this.targetSelectionSettings;
      Rotation rotation = new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
      double d2 = ClientManagers.moduleManager.range.isCombatRangeActive() ? ClientManagers.moduleManager.range.getCurrentRange() : 3.0;
      Rotation rotation2 = rotation;
      ToDoubleFunction toDoubleFunction = targetSelectionSettings2.createTargetComparatorKey(rotation2, d2);
      Optional optional = entityTargetSelector.sortBy(toDoubleFunction).findBest();
      return optional.isPresent() && optional.get() instanceof EntityLivingBase ? (EntityLivingBase)optional.get() : null;
   }

   private boolean isRandomizationEnabled() {
      return this.randomizationStrength.getValue() > 0.0;
   }

   private boolean is2DNoRotationMode() {
      return this.noRotationFovMode.getSelectedValue().equals("2D");
   }

   private boolean isStableMode() {
      return this.mode.getSelectedValue().equals("Stable");
   }

}
