package net.augustus.modules.misc;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.EventMoveInput;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.TimeHelper;

public class AntiAFK extends Module implements MoveInputListener {
   private final RandomizedRangeSettings afkDelay;
   private final BooleanValue move;
   private final RandomizedRangeSettings moveDelay;
   private final RandomizedRangeSettings moveDuration;
   private final BooleanValue inInv;
   private final BooleanValue onlyIfAfk;
   private final DoubleValue delayToStartAntiAfkAfterBeingAFK;
   private final TimeHelper jumpTimer;
   private final TimeHelper moveTimer;
   private final TimeHelper activityTimer;
   private int moveDirectionIndex;
   private boolean isMoving;

   public AntiAFK() {
       super("AntiAFK", "NichtAbwesend", new Color(50, 168, 149), Categorys.MISC);
      this.afkDelay = (RandomizedRangeSettings)(new RandomizedRangeSettings(this, "AFKDelay", 10000.0, 15000.0, 0.0, 20000.0, 0)).withoutNotInARow().markAllVisualOnly();
      this.move = (BooleanValue)(new BooleanValue(this, "Move", true)).markVisualOnly();
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "MoveDelay", 4000.0, 6000.0, 0.0, 20000.0, 0)).withoutNotInARow();
      BooleanValue move2 = this.move;
      this.move.getClass();
      BooleanValue booleanValue = move2;
      this.moveDelay = (RandomizedRangeSettings)((RandomizedRangeSettings)randomizedRangeSettings.withVisibility(() -> booleanValue.getValue())).markAllVisualOnly();
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this, "MoveDuration", 200.0, 400.0, 0.0, 1000.0, 0)).withoutNotInARow();
      move2 = this.move;
      this.move.getClass();
      BooleanValue booleanValue2 = move2;
      this.moveDuration = (RandomizedRangeSettings)((RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(() -> booleanValue2.getValue())).markAllVisualOnly();
      this.inInv = (BooleanValue)(new BooleanValue(this, "InInv", false)).markVisualOnly();
      this.onlyIfAfk = (BooleanValue)(new BooleanValue(this, "OnlyIfAfk", false)).markVisualOnly();
      DoubleValue stringValue = new DoubleValue(this, "DelayToStartAntiAfkAfterBeingAFK", 10000.0, 0.0, 20000.0, 0);
      move2 = this.onlyIfAfk;
      this.onlyIfAfk.getClass();
      BooleanValue booleanValue3 = move2;
      this.delayToStartAntiAfkAfterBeingAFK = (DoubleValue)stringValue.visibleWhen(() -> booleanValue3.getValue()).markVisualOnly();
      this.jumpTimer = new TimeHelper();
      this.moveTimer = new TimeHelper();
      this.activityTimer = new TimeHelper();
      this.moveDirectionIndex = 0;
      this.isMoving = false;
   }

   @Override
   public void onEnable() {
      this.moveDirectionIndex = 0;
      this.isMoving = false;
      this.afkDelay.nextValue();
      this.moveDelay.nextValue();
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      this.toggleEventSubscription((byte)21);
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (!this.isPlayerActive(event)) {
         if (this.inInv.getValue() || mc.currentScreen == null) {
            if (!this.move.getValue() && (event.getMoveForward() != 0.0F || event.getMoveStrafe() != 0.0F)) {
               this.jumpTimer.reset();
            }

            TimeHelper jumpTimer2 = this.jumpTimer;
            if (jumpTimer2.reached2((long)this.afkDelay.getLastIntValue())) {
               event.setJump(true);
               this.jumpTimer.reset();
            }

            jumpTimer2 = this.moveTimer;
            if (jumpTimer2.reached2((long)(this.moveDelay.getLastIntValue() + this.moveDuration.getLastIntValue()))) {
               this.moveDirectionIndex++;
               this.moveDelay.nextValue();
               this.moveDuration.nextValue();
               this.moveTimer.reset();
               this.isMoving = false;
            }

            jumpTimer2 = this.moveTimer;
            if (jumpTimer2.reached2((long)this.moveDelay.getLastIntValue())) {
               this.isMoving = true;
            }

            if (this.isMoving) {
               int n = this.moveDirectionIndex % 4;
               if (n < 2) {
                  event.setMoveForward(n == 0 ? 1.0F : -1.0F);
               } else {
                  event.setMoveStrafe(n == 2 ? 1.0F : -1.0F);
               }
            }
         }
      }
   }

   private boolean isPlayerActive(EventMoveInput event) {
      if (event.getMoveForward() != 0.0F && this.isLicenseTimestampValid()
         || event.getMoveStrafe() != 0.0F
         || event.isJump()
         || event.isSneak()
         || mc.thePlayer.rotationYaw != mc.thePlayer.lastReportedYaw
         || mc.thePlayer.rotationPitch != mc.thePlayer.lastReportedPitch) {
         this.activityTimer.reset();
      }

      return !this.activityTimer.reached(this.delayToStartAntiAfkAfterBeingAFK.getValue());
   }

   private boolean isLicenseTimestampValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
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
