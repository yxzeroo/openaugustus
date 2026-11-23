package net.augustus.modules.latency;

import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.settings.SettingVisibilityCondition;
import com.google.common.util.concurrent.AtomicDouble;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import net.augustus.modules.Module;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.util.MathHelper;

public class PingDelayController {
   private final AtomicDouble decelerationStartDelay;
   private final TimeHelper accelerationApplyTimer;
   private final BooleansSetting pingAccelerationMode;
   private final TimeHelper recalculateTimer;
   private final RandomizedRangeSettings pingrecalculatedelayMs;
   private final RandomizedRangeSettings pingAccelerationPerSecond;
   private final TimeHelper decelerationTimer;
   private final AtomicInteger cachedDelayMs;
   private final RandomizedRangeSettings pingDecelerationTime;
   private final RandomizedRangeSettings ping;
   private final RandomizedRangeSettings pingAccelerationRecalculateDelay;
   private final AtomicBoolean accelerating;
   private final AtomicBoolean decelerating;
   private final TimeHelper accelerationRecalculateTimer;
   private final AtomicDouble currentDelayMs;
   private final RandomizedRangeSettings pingAccelerationApplyDelay;

   public int getCurrentDelayMs() {
      int n = (int)this.currentDelayMs.get();
      this.cachedDelayMs.set(n);
      return n;
   }

   public void updateAsync() {
      if (this.decelerating.get()) {
         double d = (double)Math.max(this.pingDecelerationTime.getLastIntValue(), 1);
         double d2 = MathHelper.clamp_double(d - (double)this.decelerationTimer.getTime(), 0.0, d) / d;
         double d3 = this.decelerationStartDelay.get() * d2;
         this.currentDelayMs.set(d3);
      } else if (this.accelerating.get()) {
         TimeHelper timeHelper = this.accelerationRecalculateTimer;
         long lastIntValue = (long)this.pingAccelerationRecalculateDelay.getLastIntValue();
         if (timeHelper.reached2(lastIntValue)) {
            this.accelerationRecalculateTimer.reset();
            this.pingAccelerationRecalculateDelay.nextValue();
            this.pingAccelerationPerSecond.nextValue();
         }

         timeHelper = this.accelerationApplyTimer;
         long lastIntValue2 = (long)this.pingAccelerationApplyDelay.getLastIntValue();
         if (timeHelper.reached2(lastIntValue2)) {
            this.accelerationApplyTimer.reset();
            this.pingAccelerationApplyDelay.nextValue();
            double d4 = Math.max(this.pingAccelerationApplyDelay.getMidpoint(), 1.0);
            double d5 = (double)this.pingAccelerationPerSecond.getLastIntValue() / 1000.0;
            double d6 = d5 * d4;
            this.currentDelayMs.set(this.currentDelayMs.get() + d6);
         }

         if (this.currentDelayMs.get() >= (double)this.ping.getLastIntValue()) {
            this.accelerating.set(false);
            this.recalculateTimer.reset();
         }

         this.decelerationTimer.reset();
      } else {
         TimeHelper recalculateTimer2 = this.recalculateTimer;
         long lastIntValue3 = (long)this.pingrecalculatedelayMs.getLastIntValue();
         if (recalculateTimer2.reached2(lastIntValue3)) {
            this.recalculateTimer.reset();
            this.pingrecalculatedelayMs.nextValue();
            this.ping.nextValue();
            this.currentDelayMs.set((double)this.ping.getLastIntValue());
         }

         this.decelerationTimer.reset();
      }
   }

   private boolean isAccelerationRecalculateDelayVisible() {
      return !this.pingAccelerationMode.getSelectedValue().equals("Instant");
   }

   private boolean isAccelerationApplyDelayVisible() {
      return !this.pingAccelerationMode.getSelectedValue().equals("Instant");
   }

   public void resetAccelerationState() {
      this.accelerating.set(!this.pingAccelerationMode.getSelectedValue().equals("Instant"));
   }

   public int stepDecelerationAndGetDelay() {
      if (this.decelerating.compareAndSet(false, true)) {
         this.pingDecelerationTime.nextValue();
         this.decelerationStartDelay.set((double)this.cachedDelayMs.get());
      }

      if (this.currentDelayMs.get() <= 0.0) {
         this.currentDelayMs.set(0.0);
         this.resetAccelerationState();
         return -1;
      } else {
         return this.getCurrentDelayMs();
      }
   }

   public double getConfiguredMaxDelay() {
      return this.ping.getMaxValue();
   }

   public PingDelayController(Module module, String str, DoubleValue doubleValue) {
       super();
      this.accelerationRecalculateTimer = new TimeHelper();
      this.recalculateTimer = new TimeHelper();
      this.accelerationApplyTimer = new TimeHelper();
      this.decelerationTimer = new TimeHelper();
      this.currentDelayMs = new AtomicDouble(0.0);
      this.decelerationStartDelay = new AtomicDouble(0.0);
      this.accelerating = new AtomicBoolean(false);
      this.decelerating = new AtomicBoolean(false);
      this.cachedDelayMs = new AtomicInteger(0);
      this.ping = (new RandomizedRangeSettings(module, str + "Ping", 100.0, 200.0, 0.0, 2000.0, 0, doubleValue)).withoutNotInARow();
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(module, str + "PingRecalculateDelay (ms)", 400.0, 600.0, 0.0, 2000.0, 0, doubleValue)).withoutNotInARow();
      String str2 = "PingRecalculateDelay (ms)";
      this.pingrecalculatedelayMs = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str2);
      BooleansSetting booleansSetting = new BooleansSetting(module, str + "PingAccelerationMode", "Instant", new String[]{"Instant", "Smooth"}, doubleValue);
      String str3 = "PingAccelerationMode";
      this.pingAccelerationMode = (BooleansSetting)booleansSetting.withDisplayName(str3);
      randomizedRangeSettings = (new RandomizedRangeSettings(module, str + "PingAccelerationPerSecond", 20.0, 200.0, 0.0, 2000.0, 0, doubleValue)).withoutNotInARow();
      String str4 = "PingAccelerationPerSecond";
      randomizedRangeSettings = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str4);
      SettingVisibilityCondition condition = () -> this.isAccelerationRateVisible();
      this.pingAccelerationPerSecond = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition);
      randomizedRangeSettings = (new RandomizedRangeSettings(module, str + "PingAccelerationApplyDelay", 5.0, 10.0, 0.0, 100.0, 0, doubleValue)).withoutNotInARow();
      String str5 = "PingAccelerationApplyDelay";
      randomizedRangeSettings = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str5);
      SettingVisibilityCondition condition2 = () -> this.isAccelerationApplyDelayVisible();
      this.pingAccelerationApplyDelay = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition2);
      randomizedRangeSettings = (new RandomizedRangeSettings(module, str + "PingAccelerationRecalculateDelay", 20.0, 200.0, 0.0, 2000.0, 0, doubleValue)).withoutNotInARow();
      String str6 = "PingAccelerationRecalculateDelay";
      randomizedRangeSettings = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str6);
      SettingVisibilityCondition condition3 = () -> this.isAccelerationRecalculateDelayVisible();
      this.pingAccelerationRecalculateDelay = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition3);
      randomizedRangeSettings = (new RandomizedRangeSettings(module, str + "PingDecelerationTime", 35.0, 45.0, 0.0, 100.0, 0, doubleValue)).withoutNotInARow();
      String str7 = "PingDecelerationTime";
      this.pingDecelerationTime = (RandomizedRangeSettings)randomizedRangeSettings.withDisplayNamePrefix(str7);
   }

   public int resumeAndGetDelay() {
      if (this.decelerating.get()) {
         this.resetAccelerationState();
         this.decelerating.set(false);
      }

      return this.getCurrentDelayMs();
   }

   private boolean isAccelerationRateVisible() {
      return !this.pingAccelerationMode.getSelectedValue().equals("Instant");
   }

}
