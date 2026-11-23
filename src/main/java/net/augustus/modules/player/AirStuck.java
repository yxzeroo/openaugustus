package net.augustus.modules.player;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.events.EventGameLoop;
import net.augustus.settings.RandomizedRangeSettings;
import net.augustus.events.EventKey;
import net.augustus.events.listeners.KeyListener;
import net.augustus.events.listeners.GameLoopListener;
import net.augustus.events.listeners.MoveEntityListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.events.EventMoveInput;
import net.augustus.utils.TickCounter;
import net.augustus.events.EventUpdate;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.EventMoveEntity;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class AirStuck extends Module implements MoveEntityListener, MoveInputListener, UpdateListener, PacketListener, GameLoopListener, TickListener, KeyListener {
   public final BooleansSetting mode;
   private final BooleanValue motionstopLegacy;
   private final BooleanValue sneak;
   private final BooleanValue delayIncomingPackets;
   private final BooleanValue onlyInAir;
   private final BooleanValue intervals;
   private final RandomizedRangeSettings interval;
   private final RandomizedRangeSettings intervalBreakDuration;
   private final BooleanValue blockKeyAndMouseInputs;
   private final TickCounter tickCounter;
   private boolean shouldMarkDead;

   public AirStuck() {
       super("AirStuck", "LuftStecken", new Color(78, 186, 112), Categorys.PLAYER);
      this.mode = new BooleansSetting(this, "Mode", "NewNoRotate", new String[]{"NewNoRotate", "NoRotate", "CanRotate", "Intave", "Dead"});
      this.motionstopLegacy = new BooleanValue(this, "MotionStop (Legacy)", false);
      BooleanValue booleanValue = new BooleanValue(this, "Sneak", true);
      BooleanValue motionstopLegacy2 = this.motionstopLegacy;
      this.motionstopLegacy.getClass();
      BooleanValue booleanValue2 = motionstopLegacy2;
      SettingVisibilityCondition condition = () -> booleanValue2.getValue();
      this.sneak = (BooleanValue)booleanValue.withVisibility(condition);
      this.delayIncomingPackets = new BooleanValue(this, "DelayIncomingPackets", true);
      booleanValue = new BooleanValue(this, "OnlyInAir", true);
      SettingVisibilityCondition condition2 = () -> this.isOnlyInAirVisible();
      this.onlyInAir = (BooleanValue)booleanValue.withVisibility(condition2);
      booleanValue = new BooleanValue(this, "Intervals", true);
      SettingVisibilityCondition condition3 = () -> this.isIntervalsVisible();
      this.intervals = (BooleanValue)booleanValue.withVisibility(condition3);
      RandomizedRangeSettings randomizedRangeSettings = (new RandomizedRangeSettings(this, "Interval", 15.0, 20.0, 1.0, 100.0, 0)).withoutNotInARow();
      SettingVisibilityCondition condition4 = () -> this.isIntervalVisible();
      this.interval = (RandomizedRangeSettings)randomizedRangeSettings.withVisibility(condition4);
      RandomizedRangeSettings randomizedRangeSettings2 = (new RandomizedRangeSettings(this, "IntervalBreakDuration", 1.0, 1.0, 1.0, 10.0, 0)).withoutNotInARow();
      SettingVisibilityCondition condition5 = () -> this.isIntervalBreakDurationVisible();
      this.intervalBreakDuration = (RandomizedRangeSettings)randomizedRangeSettings2.withVisibility(condition5);
      this.blockKeyAndMouseInputs = new BooleanValue(this, "BlockKeyAndMouseInputs", true);
      this.tickCounter = new TickCounter();
      this.shouldMarkDead = false;
   }

   @Override
   public void onEnable() {
      this.shouldMarkDead = true;
   }

   @Override
   public void onDisable() {
      if (this.mode.getSelectedValue().equals("Dead") && !this.shouldMarkDead) {
         mc.thePlayer.isDead = false;
      }
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 20;
      this.toggleEventSubscription(b);
      byte b2 = 21;
      this.toggleEventSubscription(b2);
      byte b3 = 43;
      this.toggleEventSubscription(b3);
      byte b4 = 26;
      this.toggleEventSubscription(b4);
      byte b5 = 16;
      this.toggleEventSubscription(b5);
      byte b6 = 40;
      this.toggleEventSubscription(b6);
      byte b7 = 14;
      this.toggleEventSubscription(b7);
   }

   @Override
   public void onKey3(EventKey event) {
      if (this.blockKeyAndMouseInputs.getValue() && event.isPressed()) {
         event.cancel();
      }
   }

   @Override
   public void onKey(EventKey event) {
      if (this.blockKeyAndMouseInputs.getValue() && event.isPressed()) {
         event.cancel();
      }
   }

   @Override
   public void onTick() {
      this.tickCounter.increment();
   }

   @Override
   public void onMoveEntity(EventMoveEntity event) {
      if (this.motionstopLegacy.getValue() && this.shouldFreeze()) {
         double d = 0.0;
         event.setX(d);
         double d2 = 0.0;
         event.setY(d2);
         double d3 = 0.0;
         event.setZ(d3);
      }
   }

   @Override
   public void onUpdate(EventUpdate event) {
      String selectedValue = this.mode.getSelectedValue();
      this.setSuffix(selectedValue);
      if (this.shouldFreeze()) {
         if (this.mode.getSelectedValue().equals("Dead") && this.shouldMarkDead) {
            mc.thePlayer.isDead = true;
            this.shouldMarkDead = false;
         }

         if (this.motionstopLegacy.getValue()) {
            mc.thePlayer.motionY = 0.0;
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
         } else {
            event.cancel();
         }
      }
   }

   @Override
   public void onGameLoop(EventGameLoop event) {
      if (this.shouldFreeze() && this.delayIncomingPackets.getValue()) {
         boolean flag = true;
         event.setSkipRender(flag);
         boolean flag2 = true;
         event.setAllowSkipRender(flag2);
      }
   }

   @Override
   public void onUpdate2(EventUpdate event) {
      if (this.shouldFreeze() && this.mode.getSelectedValue().equals("NewNoRotate")) {
         event.cancel();
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (this.sneak.getValue() && this.shouldFreeze() && this.motionstopLegacy.getValue()) {
         boolean flag = true;
         event.setSneak(flag);
      }
   }

   @Override
   public void onPacket2(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof C03PacketPlayer && this.shouldFreeze()) {
         String selectedValue = this.mode.getSelectedValue();
         switch (selectedValue) {
            case "NoRotate":
               event.cancel();
               break;
            case "CanRotate":
               if (!(packet instanceof C03PacketPlayer.C05PacketPlayerLook)) {
                  event.cancel();
               }
         }
      }
   }

   @Override
   public void onPacket(EventPacket event) {
      Packet packet = event.getPacket();
      if (mc.thePlayer == null) {
         boolean flag = false;
         this.setToggled(flag);
      }

      if (this.mode.getSelectedValue().equals("Intave") && packet instanceof S08PacketPlayerPosLook) {
         event.cancel();
      }
   }

   private boolean shouldFreeze() {
      boolean flag = mc.thePlayer != null && (!mc.thePlayer.onGround || !this.onlyInAir.getValue());
      if (flag && this.intervals.getValue()) {
         TickCounter tickCounter2 = this.tickCounter;
         int lastIntValue = this.interval.getLastIntValue();
         if (tickCounter2.hasReached2(lastIntValue)) {
            tickCounter2 = this.tickCounter;
            int n = this.interval.getLastIntValue() + this.intervalBreakDuration.getLastIntValue();
            if (!tickCounter2.hasReached2(n)) {
               flag = false;
            } else {
               this.interval.nextValue();
               this.intervalBreakDuration.nextValue();
               this.tickCounter.reset();
            }
         }
      }

      return flag;
   }

   private boolean isIntervalBreakDurationVisible() {
      return this.intervals.getValue() && this.intervals.isVisible();
   }

   private boolean isIntervalVisible() {
      return this.intervals.getValue() && this.intervals.isVisible();
   }

   private boolean isIntervalsVisible() {
      return !this.mode.getSelectedValue().equals("Intave");
   }

   private boolean isOnlyInAirVisible() {
      return !this.mode.getSelectedValue().equals("Intave");
   }

}
