package net.augustus.modules.movement;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.settings.Setting;
import net.augustus.settings.RangeSetting;
import net.augustus.utils.MoveUtil;
import net.augustus.events.listeners.MoveEntityListener;
import net.augustus.utils.world.BlockUtil;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.events.EventMoveEntity;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;

public class Strafe extends Module implements MoveEntityListener, UpdateListener {
   public final BooleansSetting mode;
   public final BooleansSetting event;
   public final StringValue percentage;
   public final BooleanValue strafeOnGround;
   public final BooleansSetting timingOnGround;
   public final RangeSetting hurtTimeStrafeOnGround;
   public final BooleanValue strafeOnAir;
   public final BooleansSetting timinInAir;
   public final RangeSetting hurtTimeStrafeInAir;
   public final BooleanValue backwardsStrafe;

   public Strafe() {
       super("Strafe", "Bestrafung", new Color(50, 168, 82), Categorys.MOVEMENT);
      this.mode = new BooleansSetting(this, "Mode", "Normal", new String[]{"Normal", "Matrix", "Intave"});
      this.event = new BooleansSetting(this, "Event", "Pre", new String[]{"Pre", "Mid", "OtherMid", "Post"});
      StringValue stringValue = new StringValue(this, "Percentage", 100.0, 0.0, 100.0, 0);
      SettingVisibilityCondition condition = () -> this.isPercentageVisible();
      this.percentage = stringValue.visibleWhen(condition);
      this.strafeOnGround = new BooleanValue(this, "StrafeOnGround", false);
      BooleansSetting booleansSetting = new BooleansSetting(this, "TimingOnGround", "Always", new String[]{"Always", "NotWhileKB", "OnlyWhileKB"});
      BooleanValue strafeOnGround2 = this.strafeOnGround;
      this.strafeOnGround.getClass();
      BooleanValue booleanValue = strafeOnGround2;
      SettingVisibilityCondition condition2 = () -> booleanValue.getValue();
      this.timingOnGround = (BooleansSetting)booleansSetting.withVisibility(condition2);
      RangeSetting rangeSetting = new RangeSetting(this, "HurtTimeStrafeOnGround", 0.0, 10.0, 0.0, 10.0, 0);
      SettingVisibilityCondition condition3 = () -> this.isHurtTimeOnGroundVisible();
      this.hurtTimeStrafeOnGround = rangeSetting.visibleWhen(condition3);
      this.strafeOnAir = new BooleanValue(this, "StrafeOnAir", true);
      BooleansSetting booleansSetting2 = new BooleansSetting(this, "TiminInAir", "Always", new String[]{"Always", "NotWhileKB", "OnlyWhileKB"});
      strafeOnGround2 = this.strafeOnAir;
      this.strafeOnAir.getClass();
      BooleanValue booleanValue2 = strafeOnGround2;
      SettingVisibilityCondition condition4 = () -> booleanValue2.getValue();
      this.timinInAir = (BooleansSetting)booleansSetting2.withVisibility(condition4);
      RangeSetting rangeSetting2 = new RangeSetting(this, "HurtTimeStrafeInAir", 0.0, 10.0, 0.0, 10.0, 0);
      SettingVisibilityCondition condition5 = () -> this.isHurtTimeInAirVisible();
      this.hurtTimeStrafeInAir = rangeSetting2.visibleWhen(condition5);
      this.backwardsStrafe = new BooleanValue(this, "BackwardsStrafe", true);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 20;
      this.toggleEventSubscription(b);
      byte b2 = 43;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onUpdate() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.event.getSelectedValue().equals("Post")) {
         this.applyStrafe();
      }
   }

   @Override
   public void onUpdate2() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.event.getSelectedValue().equals("Pre")) {
         this.applyStrafe();
      }
   }

   @Override
   public void onMoveEntity(EventMoveEntity event2) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.canStrafe()) {
         if (!BlockUtil.isScaffoldActiveAndValid()) {
            if (this.event.getSelectedValue().equals("Mid")) {
               boolean flag = true;
               this.applyMidStrafe(event2, flag);
            } else if (this.event.getSelectedValue().equals("OtherMid")) {
               boolean flag2 = false;
               this.applyMidStrafe(event2, flag2);
            }
         }
      }
   }

   private void applyMidStrafe(EventMoveEntity event2, boolean flag) {
      double x = event2.getX();
      Fly.getCachedSettings();
      double z = event2.getZ();
      double d = Math.sqrt(x * x + z * z);
      String selectedValue = this.mode.getSelectedValue();
      switch (selectedValue) {
         case "Normal":
            if (MoveUtil.isMoving()) {
               float f = (float)(this.percentage.getValue() / 100.0);
               double d2 = d * (double)f;
               double d3 = (double)(1.0F - f);
               double d4 = x * d3;
               double d5 = z * d3;
               double direction = MoveUtil.getDirection();
               x = d4 - Math.sin(direction) * d2;
               z = d5 + Math.cos(direction) * d2;
               event2.setX(x);
               event2.setZ(z);
               if (flag) {
                  mc.thePlayer.motionX = x;
                  mc.thePlayer.motionZ = z;
               }
            }
            break;
         case "Intave":
            if (MoveUtil.isMoving()
               && !mc.thePlayer.onGround
               && (mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown())
               && mc.thePlayer.hurtTime == 0
               && !mc.thePlayer.isCollidedVertically
               && !mc.thePlayer.isBurning()) {
               double[] doubles = PlayerUtil.predictPlayerPosition(0, mc.thePlayer);
               if (doubles[9] == 1.0) {
                  float f2 = (float)(this.percentage.getValue() / 100.0);
                  double d6 = d * (double)f2;
                  double d7 = (double)(1.0F - f2);
                  double d8 = x * d7;
                  double d9 = z * d7;
                  double direction2 = MoveUtil.getDirection();
                  x = d8 - Math.sin(direction2) * d6;
                  z = d9 + Math.cos(direction2) * d6;
                  event2.setX(x);
                  event2.setZ(z);
                  if (flag) {
                     mc.thePlayer.motionX = x;
                     mc.thePlayer.motionZ = z;
                  }
               }
            }
            break;
         case "Matrix":
            if (MoveUtil.isMoving()) {
               double matrixStrafeDirection = getMatrixStrafeDirection();
               x = -Math.sin(matrixStrafeDirection) * d;
               z = Math.cos(matrixStrafeDirection) * d;
               event2.setX(x);
               event2.setZ(z);
               if (flag) {
                  mc.thePlayer.motionX = x;
                  mc.thePlayer.motionZ = z;
               }
            }
      }
   }

   private static double getMatrixStrafeDirection() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      float movementYaw = MoveUtil.getMovementYaw();
      if (mc.thePlayer.moveForward < 0.0F) {
         movementYaw += 180.0F;
      } else {
         float f = 1.0F;
         if (mc.thePlayer.moveForward > 0.0F) {
            f = 0.5F;
         }

         if (mc.thePlayer.moveStrafing > 0.0F) {
            movementYaw -= 90.0F * f;
         }

         if (mc.thePlayer.moveStrafing < 0.0F) {
            movementYaw += 90.0F * f;
         }
      }

      return Math.toRadians((double)movementYaw);
   }

   private void applyStrafe() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.canStrafe()) {
         if (!BlockUtil.isScaffoldActiveAndValid()) {
            String selectedValue = this.mode.getSelectedValue();
            switch (selectedValue) {
               case "Normal":
                  MoveUtil.strafe((float)(this.percentage.getValue() / 100.0));
                  break;
               case "Matrix":
                  MoveUtil.strafeCurrentSpeed();
                  break;
               case "Intave":
                  if (!mc.thePlayer.onGround) {
                     double[] doubles = PlayerUtil.predictPlayerPosition(0, mc.thePlayer);
                     if (doubles[9] == 1.0) {
                        MoveUtil.strafe((float)(this.percentage.getValue() / 100.0));
                     }
                  }
            }
         }
      }
   }

   private boolean canStrafe() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (MoveUtil.getCorrectedMoveInputs()[0] < 0 && !this.backwardsStrafe.getValue() && this.isLicenseTimestampValid()) {
         return false;
      } else if (mc.thePlayer.onGround) {
         if (!this.strafeOnGround.getValue()) {
            return false;
         } else if (mc.thePlayer.hurtTime != 0 && this.timingOnGround.getSelectedValue().equals("NotWhileKB")) {
            return false;
         } else if (this.timingOnGround.getSelectedValue().equals("OnlyWhileKB")) {
            if (mc.thePlayer.hurtTime != 0 && (double)mc.thePlayer.hurtTime >= this.hurtTimeStrafeOnGround.getMinValue()) {
               double hurtTime = (double)mc.thePlayer.hurtTime;
               if (hurtTime <= this.hurtTimeStrafeOnGround.getMaxValue()) {
                  return true;
               }
            }

            return false;
         } else if (mc.thePlayer.hurtTime != 0) {
            if ((double)mc.thePlayer.hurtTime >= this.hurtTimeStrafeOnGround.getMinValue()) {
               double hurtTime2 = (double)mc.thePlayer.hurtTime;
               if (hurtTime2 <= this.hurtTimeStrafeOnGround.getMaxValue()) {
                  return true;
               }
            }

            return false;
         } else {
            return true;
         }
      } else if (!this.strafeOnAir.getValue()) {
         return false;
      } else if (mc.thePlayer.hurtTime != 0 && this.timinInAir.getSelectedValue().equals("NotWhileKB")) {
         return false;
      } else if (this.timinInAir.getSelectedValue().equals("OnlyWhileKB")) {
         if (mc.thePlayer.hurtTime != 0 && (double)mc.thePlayer.hurtTime >= this.hurtTimeStrafeInAir.getMinValue()) {
            double hurtTime3 = (double)mc.thePlayer.hurtTime;
            if (hurtTime3 <= this.hurtTimeStrafeInAir.getMaxValue()) {
               return true;
            }
         }

         return false;
      } else if (mc.thePlayer.hurtTime != 0) {
         if ((double)mc.thePlayer.hurtTime >= this.hurtTimeStrafeInAir.getMinValue()) {
            double hurtTime4 = (double)mc.thePlayer.hurtTime;
            if (hurtTime4 <= this.hurtTimeStrafeInAir.getMaxValue()) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private boolean isHurtTimeInAirVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.strafeOnAir.getValue() && !this.timinInAir.getSelectedValue().equals("NotWhileKB");
   }

   private boolean isHurtTimeOnGroundVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.strafeOnGround.getValue() && !this.timingOnGround.getSelectedValue().equals("NotWhileKB");
   }

   private boolean isPercentageVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return !this.mode.getSelectedValue().equals("Matrix");
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
