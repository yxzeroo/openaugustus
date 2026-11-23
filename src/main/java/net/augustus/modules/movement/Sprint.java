package net.augustus.modules.movement;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.input.KeyboardInputUtil;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.events.listeners.SprintListener;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.settings.Setting;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.rotation.MoveFixMode;
import net.augustus.utils.rotation.RotationController;
import net.augustus.events.listeners.MoveEntityListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.EventMoveEntity;
import net.augustus.events.EventSprint;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;

public class Sprint extends Module implements SprintListener, MoveInputListener, TickListener, MoveEntityListener {
   public final BooleansSetting mode;
   public final BooleanValue ignoreCollisions;
   public final BooleanValue hungerSprint;
   public final BooleanValue hungerSprintOnlyOnGround;
   public final BooleanValue jumpSprint;
   public final BooleanValue editMotion;
   public final DoubleValue motionMultiplierAtSprinting;
   public final RandomizedRangeSettings multiplierAirForward;
   public final RandomizedRangeSettings multiplierGroundForward;
   public final RandomizedRangeSettings multiplierAirBackward;
   public final RandomizedRangeSettings multiplierGroundBackward;
   public final RandomizedRangeSettings multiplierAirSideways;
   public final RandomizedRangeSettings multiplierGroundSideways;
   public final RandomizedRangeSettings multiplierAirForwardSideways;
   public final RandomizedRangeSettings multiplierGroundForwardSideways;
   public final RandomizedRangeSettings multiplierAirBackwardSideways;
   public final RandomizedRangeSettings multiplierGroundBackwardSideways;
   private float lastForwardInput;
   private float lastStrafeInput;

   public Sprint() {
       super("Sprint", "Sprinten", new Color(170, 181, 182, 255), Categorys.MOVEMENT);
      this.mode = (BooleansSetting)(new BooleansSetting(
            this, "Mode", "Legit", new String[]{"Legit", "LegitRotationAllDirection", "AllDirection", "AllDirectionMotion"}
         ))
         .markVisualOnly();
      this.ignoreCollisions = (BooleanValue)(new BooleanValue(this, "IgnoreCollisions", true)).markVisualOnly();
      this.hungerSprint = (BooleanValue)(new BooleanValue(this, "HungerSprint", true)).markVisualOnly();
      BooleanValue booleanValue = new BooleanValue(this, "HungerSprintOnlyOnGround", true);
      BooleanValue hungerSprint2 = this.hungerSprint;
      this.hungerSprint.getClass();
      BooleanValue booleanValue2 = hungerSprint2;
      SettingVisibilityCondition condition = () -> booleanValue2.getValue();
      this.hungerSprintOnlyOnGround = (BooleanValue)((BooleanValue)booleanValue.withVisibility(condition)).markVisualOnly();
      this.jumpSprint = (BooleanValue)(new BooleanValue(this, "JumpSprint", false)).markVisualOnly();
      this.editMotion = (BooleanValue)(new BooleanValue(this, "EditMotion", false)).markVisualOnly();
      DoubleValue doubleValue = new DoubleValue(this, "Motion multiplier at sprinting", true);
      hungerSprint2 = this.editMotion;
      this.editMotion.getClass();
      BooleanValue booleanValue3 = hungerSprint2;
      SettingVisibilityCondition condition2 = () -> booleanValue3.getValue();
      this.motionMultiplierAtSprinting = (DoubleValue)doubleValue.visibleWhen(condition2).markVisualOnly();
      this.multiplierAirForward = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, " multiplier air forward", 1.0, 1.0, 0.1, 2.0, 2, this.motionMultiplierAtSprinting)).withoutNotInARow().markAllVisualOnly();
      this.multiplierGroundForward = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, " multiplier ground forward", 1.0, 1.0, 0.1, 2.0, 2, this.motionMultiplierAtSprinting))
         .withoutNotInARow()
         .markAllVisualOnly();
      this.multiplierAirBackward = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, " multiplier air backward", 1.0, 1.0, 0.1, 2.0, 2, this.motionMultiplierAtSprinting)).withoutNotInARow().markAllVisualOnly();
      this.multiplierGroundBackward = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, " multiplier ground backward", 1.0, 1.0, 0.1, 2.0, 2, this.motionMultiplierAtSprinting))
         .withoutNotInARow()
         .markAllVisualOnly();
      this.multiplierAirSideways = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, " multiplier air sideways", 1.0, 1.0, 0.1, 2.0, 2, this.motionMultiplierAtSprinting)).withoutNotInARow().markAllVisualOnly();
      this.multiplierGroundSideways = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, " multiplier ground sideways", 1.0, 1.0, 0.1, 2.0, 2, this.motionMultiplierAtSprinting))
         .withoutNotInARow()
         .markAllVisualOnly();
      this.multiplierAirForwardSideways = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, " multiplier air forward-sideways", 1.0, 1.0, 0.1, 2.0, 2, this.motionMultiplierAtSprinting))
         .withoutNotInARow()
         .markAllVisualOnly();
      this.multiplierGroundForwardSideways = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, " multiplier ground forward-sideways", 1.0, 1.0, 0.1, 2.0, 2, this.motionMultiplierAtSprinting))
         .withoutNotInARow()
         .markAllVisualOnly();
      this.multiplierAirBackwardSideways = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, " multiplier air backward-sideways", 1.0, 1.0, 0.1, 2.0, 2, this.motionMultiplierAtSprinting))
         .withoutNotInARow()
         .markAllVisualOnly();
      this.multiplierGroundBackwardSideways = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, " multiplier ground backward-sideways", 1.0, 1.0, 0.1, 2.0, 2, this.motionMultiplierAtSprinting))
         .withoutNotInARow()
         .markAllVisualOnly();
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = -4;
      byte b2 = 36;
      this.toggleEventSubscription(b2, b);
      byte b3 = 10;
      byte b4 = 21;
      this.toggleEventSubscription(b4, b3);
      byte b5 = 40;
      this.toggleEventSubscription(b5);
      byte b6 = 20;
      this.toggleEventSubscription(b6);
   }

   @Override
   public void onSprint3(EventSprint event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      boolean flag = true;
      event.setSprintKeyDown(flag);
      if (this.mode.getSelectedValue().equals("AllDirection")) {
         boolean flag2 = true;
         event.setForceSprint(flag2);
      }

      if (this.mode.getSelectedValue().equals("Legit") && this.ignoreCollisions.getValue()) {
         boolean flag3 = true;
         event.setKeepSprintOnCollision(flag3);
      }

      if (this.hungerSprint.getValue()) {
         boolean flag4 = true;
         event.setIgnoreHunger(flag4);
      }
   }

   @Override
   public void onSprint(EventSprint event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.jumpSprint.getValue() && MoveUtil.isMoving()) {
         boolean flag = true;
         event.setOmniJumpSprint(flag);
      }

      if (this.hungerSprint.getValue() && (float)mc.thePlayer.getFoodStats().getFoodLevel() <= 6.0F && this.hungerSprintOnlyOnGround.getValue()) {
         boolean flag2 = false;
         event.setApplyJumpBoost(flag2);
      }
   }

   @Override
   public void onSprint2(EventSprint event) {
      if (this.mode.getSelectedValue().equals("AllDirectionMotion")) {
         boolean flag = true;
         event.setMotionSprint(flag);
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (mc.thePlayer.isSprinting() && ClientManagers.moduleManager.scaffold.isToggled() && ClientManagers.moduleManager.scaffold.mode.getSelectedValue().equals("None")) {
         float f = 0.0F;
         event.setMoveForward(f);
      }

      if (this.canUseLegitAllDirection() && ClientManagers.rotationController.isOwnedBy(this)) {
         if (event.getMoveForward() > 0.0F && event.getMoveStrafe() == 0.0F) {
            return;
         }

         MoveFixMode moveFixMode = MoveFixMode.DEFAULT;
         event.setMoveFixMode(moveFixMode);
      }
   }

   @Override
   public void onTick() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.canUseLegitAllDirection()) {
         int forwardAxis = KeyboardInputUtil.getForwardAxis();
         int strafeAxis = KeyboardInputUtil.getStrafeAxis();
         if (forwardAxis > 0 && strafeAxis == 0) {
            return;
         }

         double direction = MoveUtil.getDirection((float)forwardAxis, (float)strafeAxis, mc.thePlayer.rotationYaw);
         RotationController rotationController = ClientManagers.rotationController;
         float f = RotationUtil.applyYawGcd(
            RotationUtil.updateAngle(ClientManagers.rotationController.getPreviousRotation().yaw, (float)Math.toDegrees(direction), 180.0F), ClientManagers.rotationController.getPreviousRotation().yaw
         );
         byte b = 11;
         float f2 = f;
         rotationController.requestYaw(f2, b, this);
      }
   }

   @Override
   public void onMoveInput(EventMoveInput event) {
      this.lastForwardInput = event.getMoveForward();
      this.lastStrafeInput = event.getMoveStrafe();
   }

   @Override
   public void onMoveEntity(EventMoveEntity event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.editMotion.getValue() && (mc.thePlayer.isSprinting() || this.mode.getSelectedValue().equals("AllDirectionMotion"))) {
         if (KeyboardInputUtil.isPureForward(this.lastForwardInput, this.lastStrafeInput)) {
            RandomizedRangeSettings randomizedRangeSettings = this.multiplierGroundForward;
            RandomizedRangeSettings multiplierAirForward2 = this.multiplierAirForward;
            this.applySprintMotionMultiplier(event, multiplierAirForward2, randomizedRangeSettings);
         } else if (KeyboardInputUtil.isPureBackward(this.lastForwardInput, this.lastStrafeInput)) {
            RandomizedRangeSettings randomizedRangeSettings2 = this.multiplierGroundBackward;
            RandomizedRangeSettings multiplierAirBackward2 = this.multiplierAirBackward;
            this.applySprintMotionMultiplier(event, multiplierAirBackward2, randomizedRangeSettings2);
         } else if (KeyboardInputUtil.isForwardDiagonal(this.lastForwardInput, this.lastStrafeInput)) {
            RandomizedRangeSettings randomizedRangeSettings3 = this.multiplierGroundForwardSideways;
            RandomizedRangeSettings randomizedRangeSettings4 = this.multiplierAirForwardSideways;
            this.applySprintMotionMultiplier(event, randomizedRangeSettings4, randomizedRangeSettings3);
         } else if (KeyboardInputUtil.isBackwardDiagonal(this.lastForwardInput, this.lastStrafeInput)) {
            RandomizedRangeSettings randomizedRangeSettings5 = this.multiplierGroundBackwardSideways;
            RandomizedRangeSettings randomizedRangeSettings6 = this.multiplierAirBackwardSideways;
            this.applySprintMotionMultiplier(event, randomizedRangeSettings6, randomizedRangeSettings5);
         } else if (KeyboardInputUtil.isPureStrafe(this.lastForwardInput, this.lastStrafeInput)) {
            RandomizedRangeSettings randomizedRangeSettings7 = this.multiplierGroundSideways;
            RandomizedRangeSettings multiplierAirSideways2 = this.multiplierAirSideways;
            this.applySprintMotionMultiplier(event, multiplierAirSideways2, randomizedRangeSettings7);
         }
      }
   }

   private void applySprintMotionMultiplier(EventMoveEntity event, RandomizedRangeSettings randomizedRangeSettings, RandomizedRangeSettings randomizedRangeSettings2) {
      double d = mc.thePlayer.onGround ? randomizedRangeSettings2.nextValue() : randomizedRangeSettings.nextValue();
      double d2 = event.getX() * d;
      event.setX(d2);
      double d3 = event.getZ() * d;
      event.setZ(d3);
   }

   private boolean canUseLegitAllDirection() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return mc.isNotInGui()
         && this.mode.getSelectedValue().equals("LegitRotationAllDirection")
         && !ClientManagers.moduleManager.scaffold.isToggled()
         && this.isLicenseTimestampValid()
         && (KeyboardInputUtil.getForwardAxis() != 0 || KeyboardInputUtil.getStrafeAxis() != 0)
         && (!ClientManagers.moduleManager.antiBot.isToggled() || ClientManagers.moduleManager.aimBot.target == null)
         && (!ClientManagers.moduleManager.aimAssist.isToggled() || ClientManagers.moduleManager.aimAssist.target == null);
   }

   private boolean isLicenseTimestampValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
      Fly.getCachedSettings();
      boolean flag = false;
      if (timestampBytes != null) {
         long l = 0L;

         for (int i = 0; i < 8; i++) {
            l <<= 8;
            l |= (long)(timestampBytes[i] & 255);
         }

         long l2 = System.currentTimeMillis() - l;
         String str = "518400000";
         long l3 = 0L;
         int n = 0;
         int length = str.length();

         while (n < length) {
            int n2 = Character.digit(str.charAt(n++), 10);
            l3 *= 10L;
            l3 -= (long)n2;
         }

         l3 = -l3;
         flag = l2 <= l3;
      }

      return flag;
   }

}
