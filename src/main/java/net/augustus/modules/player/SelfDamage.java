package net.augustus.modules.player;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.events.EventPacketDelay;
import net.augustus.events.listeners.PacketDelayListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.utils.player.PlayerUtil;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class SelfDamage extends Module implements UpdateListener, PacketListener, PacketDelayListener {
   private final BooleansSetting modes;
   private final StringValue setPositionYAdd;
   private final StringValue motion;
   private final StringValue loops;
   private final StringValue yAddInLoopFirst;
   private final BooleanValue secondPacket;
   private final StringValue yAddInLoopSecond;
   private final BooleanValue finishPacket;
   private final StringValue yAddFinish;
   private final BooleanValue onlyOnGround;
   private final BooleanValue repeat;
   private final StringValue repeatTicks;
   private int ticksSinceDamage;
   private boolean damageTriggered;
   private int jumpCount;

   public SelfDamage() {
       super("SelfDamage", "SelbstSchaden", new Color(235, 64, 52), Categorys.PLAYER);
      this.modes = new BooleansSetting(this, "Modes", "Motion", new String[]{"Motion", "Packet1", "Packet2", "Custom", "Fake", "SetPosition", "3Jump"});
      StringValue stringValue = new StringValue(this, "SetPositionYAdd", 3.1, -4.0, 4.0, 4);
      String str = "YAdd";
      StringValue stringValue2 = stringValue.named(str);
      SettingVisibilityCondition condition = () -> this.isSetPositionYAddVisible();
      this.setPositionYAdd = stringValue2.visibleWhen(condition);
      StringValue stringValue3 = new StringValue(this, "Motion", 5.0, 0.0, 10.0, 2);
      SettingVisibilityCondition condition2 = () -> {
         return this.modes.getSelectedValue().equals("Motion");
      };
      this.motion = stringValue3.visibleWhen(condition2);
      StringValue stringValue4 = new StringValue(this, "Loops", 51.0, 1.0, 100.0, 0);
      SettingVisibilityCondition condition3 = () -> this.isLoopsVisible();
      this.loops = stringValue4.visibleWhen(condition3);
      StringValue stringValue5 = new StringValue(this, "YAddInLoopFirst", 0.02, 0.0, 1.0, 4);
      SettingVisibilityCondition condition4 = () -> this.isFirstPacketYAddVisible();
      this.yAddInLoopFirst = stringValue5.visibleWhen(condition4);
      BooleanValue booleanValue = new BooleanValue(this, "SecondPacket", true);
      SettingVisibilityCondition condition5 = () -> this.isSecondPacketVisible();
      this.secondPacket = (BooleanValue)booleanValue.withVisibility(condition5);
      StringValue stringValue6 = new StringValue(this, "YAddInLoopSecond", 0.02, 0.0, 1.0, 4);
      SettingVisibilityCondition condition6 = () -> this.isSecondPacketYAddVisible();
      this.yAddInLoopSecond = stringValue6.visibleWhen(condition6);
      BooleanValue booleanValue2 = new BooleanValue(this, "FinishPacket", true);
      SettingVisibilityCondition condition7 = () -> this.isFinishPacketVisible();
      this.finishPacket = (BooleanValue)booleanValue2.withVisibility(condition7);
      StringValue stringValue7 = new StringValue(this, "YAddFinish", 0.0, 0.0, 1.0, 4);
      SettingVisibilityCondition condition8 = () -> {
         return this.modes.getSelectedValue().equals("Custom") && this.finishPacket.getValue();
      };
      this.yAddFinish = stringValue7.visibleWhen(condition8);
      BooleanValue booleanValue3 = new BooleanValue(this, "OnlyOnGround", false);
      SettingVisibilityCondition condition9 = () -> this.isOnlyOnGroundVisible();
      this.onlyOnGround = (BooleanValue)booleanValue3.withVisibility(condition9);
      BooleanValue booleanValue4 = new BooleanValue(this, "Repeat", false);
      SettingVisibilityCondition condition10 = () -> this.isRepeatVisible();
      this.repeat = (BooleanValue)booleanValue4.withVisibility(condition10);
      StringValue stringValue8 = new StringValue(this, "RepeatTicks", 140.0, 0.0, 200.0, 0);
      SettingVisibilityCondition condition11 = () -> this.isRepeatTicksVisible();
      this.repeatTicks = stringValue8.visibleWhen(condition11);
      this.ticksSinceDamage = 0;
      this.damageTriggered = false;
      this.jumpCount = 0;
   }

   @Override
   public void onEnable() {
      this.damageTriggered = false;
      this.ticksSinceDamage = 0;
      this.jumpCount = 0;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 43;
      this.toggleEventSubscription(b);
      byte b2 = 26;
      this.toggleEventSubscription(b2);
      byte b3 = 25;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onUpdate() {
      String selectedValue = this.modes.getSelectedValue();
      this.setSuffix(selectedValue);
      if (!this.damageTriggered || this.modes.getSelectedValue().equals("3Jump")) {
         if (mc.thePlayer == null || this.onlyOnGround.getValue() && !mc.thePlayer.onGround && !this.modes.getSelectedValue().equals("3Jump")) {
            return;
         }

         this.applySelfDamage();
         this.damageTriggered = true;
      }

      if (this.damageTriggered && !this.modes.getSelectedValue().equals("3Jump")) {
         if (this.repeat.getValue() && (double)this.ticksSinceDamage > this.repeatTicks.getValue()) {
            this.ticksSinceDamage = 0;
            this.applySelfDamage();
         }

         this.ticksSinceDamage++;
      }
   }

   @Override
   public void onPacketDelay(EventPacketDelay event) {
      if (this.modes.getSelectedValue().equals("3Jump") && this.jumpCount < 4) {
         boolean flag = true;
         event.setDelayed(flag);
      }
   }

   private void applySelfDamage() {
      String selectedValue = this.modes.getSelectedValue();
      switch (selectedValue) {
         case "Motion":
            mc.thePlayer.motionY = (double)((float)this.motion.getValue());
            break;
         case "Packet1":
            PlayerUtil.damageSelfShort();
            break;
         case "Packet2":
            PlayerUtil.damageSelfPacket();
            break;
         case "Fake":
            mc.thePlayer.handleStatusUpdate((byte)2);
            break;
         case "Custom":
            for (int i = 0; (double)i < this.loops.getValue(); i++) {
               mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + this.yAddInLoopFirst.getValue(), mc.thePlayer.posZ, false));
               if (this.secondPacket.getValue()) {
                  mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + this.yAddInLoopSecond.getValue(), mc.thePlayer.posZ, false));
               }
            }

            if (this.finishPacket.getValue()) {
               mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + this.yAddFinish.getValue(), mc.thePlayer.posZ, true));
            }
            break;
         case "SetPosition":
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + this.setPositionYAdd.getValue(), mc.thePlayer.posZ);
            break;
         case "3Jump":
            if (mc.thePlayer.onGround) {
               if (this.jumpCount < 3) {
                  mc.thePlayer.jump();
                  PlayerUtil.triggerJumpStat();
               }

               this.jumpCount++;
            }
      }
   }

   @Override
   public void onPacket2(EventPacket event) {
      if (this.modes.getSelectedValue().equals("3Jump") && this.jumpCount < 4) {
         Packet packet = event.getPacket();
         if (packet instanceof C03PacketPlayer) {
            C03PacketPlayer packet2 = (C03PacketPlayer)packet;
            packet2.setOnGround(false);
         }
      }
   }

   private boolean isRepeatTicksVisible() {
      return this.repeat.getValue() && this.repeat.isVisible();
   }

   private boolean isRepeatVisible() {
      return !this.modes.getSelectedValue().equals("3Jump");
   }

   private boolean isOnlyOnGroundVisible() {
      return !this.modes.getSelectedValue().equals("3Jump");
   }

   private boolean isFinishPacketVisible() {
      return this.modes.getSelectedValue().equals("Custom");
   }

   private boolean isSecondPacketYAddVisible() {
      return this.modes.getSelectedValue().equals("Custom") && this.secondPacket.getValue();
   }

   private boolean isSecondPacketVisible() {
      return this.modes.getSelectedValue().equals("Custom");
   }

   private boolean isFirstPacketYAddVisible() {
      return this.modes.getSelectedValue().equals("Custom");
   }

   private boolean isLoopsVisible() {
      return this.modes.getSelectedValue().equals("Custom");
   }

   private boolean isSetPositionYAddVisible() {
      return this.modes.getSelectedValue().equals("SetPosition");
   }

}
