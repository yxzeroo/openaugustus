package net.augustus.modules.movement;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.listeners.MotionListener;
import net.augustus.settings.Setting;
import net.augustus.events.EventPacketDelay;
import net.augustus.events.listeners.LoadWorldListener;
import net.augustus.events.EventLoadWorld;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.events.listeners.SoundListener;
import net.augustus.events.EventSound;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.events.EventMotion;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;

public class Timer extends Module implements MotionListener, LoadWorldListener, SoundListener, PacketDelayListener {
   public final StringValue time;
   public final BooleanValue timerAfterVelocity;
   public final StringValue timerAfterVelocityVal;
   public final BooleanValue timerAfterFallDamage;
   public final StringValue timerAfterFallDamageVal;
   public final BooleanValue timerAtBlink;
   public final StringValue timerAtBlinkVal;
   private final TimeHelper timeHelper;
   private boolean lastHurtWasFallDamage;
   private boolean packetDelayed;

   public Timer() {
       super("Timer", "Zeitschaltuhr", new Color(180, 176, 119), Categorys.MOVEMENT);
      this.time = new StringValue(this, "Time", 1.0, 0.1, 10.0, 2);
      this.timerAfterVelocity = new BooleanValue(this, "TimerAfterVelocity", false);
      StringValue stringValue = new StringValue(this, "TimerAfterVelocityVal", 1.0, 0.1, 10.0, 2);
      BooleanValue timerAfterVelocity2 = this.timerAfterVelocity;
      this.timerAfterVelocity.getClass();
      BooleanValue booleanValue = timerAfterVelocity2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.timerAfterVelocityVal = stringValue.visibleWhen(condition);
      this.timerAfterFallDamage = new BooleanValue(this, "TimerAfterFallDamage", false);
      StringValue stringValue2 = new StringValue(this, "TimerAfterFallDamageVal", 1.0, 0.1, 10.0, 2);
      timerAfterVelocity2 = this.timerAfterFallDamage;
      this.timerAfterFallDamage.getClass();
      BooleanValue booleanValue2 = timerAfterVelocity2;
      SettingVisibilityCondition condition2 = () -> booleanValue2.getValue();
      this.timerAfterFallDamageVal = stringValue2.visibleWhen(condition2);
      this.timerAtBlink = new BooleanValue(this, "TimerAtBlink", false);
      StringValue stringValue3 = new StringValue(this, "TimerAtBlinkVal", 1.0, 0.1, 10.0, 2);
      timerAfterVelocity2 = this.timerAtBlink;
      this.timerAtBlink.getClass();
      BooleanValue booleanValue3 = timerAfterVelocity2;
      SettingVisibilityCondition condition3 = () -> booleanValue3.getValue();
      this.timerAtBlinkVal = stringValue3.visibleWhen(condition3);
      this.timeHelper = new TimeHelper();
      this.lastHurtWasFallDamage = false;
      this.packetDelayed = false;
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
      mc.getTimer().timerSpeed = 1.0F;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = -4;
      byte b2 = 18;
      this.toggleEventSubscription(b2, b);
      byte b3 = 17;
      this.toggleEventSubscription(b3);
      byte b4 = 35;
      this.toggleEventSubscription(b4);
      byte b5 = 25;
      this.toggleEventSubscription(b5);
   }

   @Override
   public void onMotion(EventMotion event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      String str = String.valueOf(mc.getTimer().timerSpeed);
      this.setSuffix(str);
      if (mc.theWorld != null) {
         if (mc.thePlayer != null) {
            if (mc.thePlayer.hurtTime > 0 && this.timerAfterVelocity.getValue() && !this.lastHurtWasFallDamage) {
               mc.getTimer().timerSpeed = (float)Math.max(0.1, this.timerAfterVelocityVal.getValue());
            } else {
               if (mc.thePlayer.hurtTime > 0 && this.timerAfterFallDamage.getValue() && this.lastHurtWasFallDamage) {
                  mc.getTimer().timerSpeed = (float)Math.max(0.1, this.timerAfterFallDamageVal.getValue());
               } else if (this.packetDelayed && this.timerAtBlink.getValue()) {
                  mc.getTimer().timerSpeed = (float)Math.max(0.1, this.timerAtBlinkVal.getValue());
                  this.packetDelayed = false;
               } else {
                  mc.getTimer().timerSpeed = (float)Math.max(0.1, this.time.getValue());
               }
            }

            return;
         }
      }

      mc.getTimer().timerSpeed = 1.0F;
   }

   @Override
   public void onLoadWorld(EventLoadWorld event) {
      if (event.getWorld() == null) {
         mc.getTimer().timerSpeed = 1.0F;
      }
   }

   @Override
   public void onSound(EventSound event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      double x = event.getX();
      double d = 0.01;
      double posX = mc.thePlayer.posX;
      double d2 = x;
      if (this.isWithinTolerance(d2, posX, d)) {
         x = event.getY();
         double d3 = 0.01;
         double posY = mc.thePlayer.posY;
         double d4 = x;
         if (this.isWithinTolerance(d4, posY, d3)) {
            x = event.getZ();
            double d5 = 0.01;
            double posZ = mc.thePlayer.posZ;
            double d6 = x;
            if (this.isWithinTolerance(d6, posZ, d5)) {
               if (event.getSoundName().equals("game.player.hurt.fall.small") || event.getSoundName().equals("game.player.hurt.fall.big")) {
                  this.timeHelper.reset();
               }

               if (event.getSoundName().equals("game.player.hurt")) {
                  long l = 100L;
                  this.lastHurtWasFallDamage = !this.timeHelper.reached2(l);
               }
            }
         }
      }
   }

   @Override
   public void onPacketDelay(EventPacketDelay event) {
      if (event.isDelayed()) {
         this.packetDelayed = true;
      }
   }

   @Override
   public void onPacketDelay2(EventPacketDelay event) {
      if (event.isDelayed()) {
         this.packetDelayed = true;
      }
   }

   private boolean isWithinTolerance(double d, double d2, double d3) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      double d4 = Math.abs(d - d2);
      return !(d4 > d3);
   }

}
