package net.augustus.modules.movement;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventOrientCamera;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.MotionListener;
import net.augustus.settings.Setting;
import net.augustus.utils.MoveUtil;
import net.augustus.events.EventSafeWalk;
import net.augustus.events.listeners.SafeWalkListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.OrientCameraListener;
import net.augustus.clickgui.ClickGuiScreen;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.events.EventMotion;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Sneak extends Module implements OrientCameraListener, MoveInputListener, MotionListener, SafeWalkListener, TickListener {
   public final BooleansSetting sneakTiming;
   public final BooleansSetting mode;
   public final StringValue sneakForward;
   public final StringValue sneakStrafe;
   public final BooleanValue removeSneakStop;
   public final BooleanValue sneakInInv;
   public final BooleanValue spoofEyeHeight;
   private boolean sneakKeyPressed;

   public Sneak() {
       super("Sneak", "Schleichen", new Color(245, 114, 66), Categorys.MOVEMENT);
      this.sneakTiming = new BooleansSetting(
         this, "SneakTiming", "OnlyWhileSneakKeyPressed", new String[]{"OnlyWhileSneakKeyPressed", "OnlyWhileStandingStill", "Always"}
      );
      this.mode = new BooleansSetting(this, "Mode", "Legit", new String[]{"Legit", "Basic", "PrePostSpam", "PrePostSpamOld"});
      StringValue stringValue = new StringValue(this, "SneakForward", 1.0, 0.3, 1.0, 2);
      SettingVisibilityCondition condition = () -> {
         return this.mode.getSelectedValue().equals("Basic");
      };
      this.sneakForward = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(this, "SneakStrafe", 1.0, 0.3, 1.0, 2);
      SettingVisibilityCondition condition2 = () -> {
         return this.mode.getSelectedValue().equals("Basic");
      };
      this.sneakStrafe = stringValue2.visibleWhen(condition2);
      this.removeSneakStop = new BooleanValue(this, "RemoveSneakStop", true);
      this.sneakInInv = new BooleanValue(this, "SneakInInv", true);
      this.spoofEyeHeight = new BooleanValue(this, "SpoofEyeHeight", true);
      this.sneakKeyPressed = false;
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (!this.sneakKeyPressed
         && mc.thePlayer != null
         && mc.thePlayer.isServerSneakState()
         && (this.mode.getSelectedValue().equals("PrePostSpam") || this.mode.getSelectedValue().equals("PrePostSpamOld"))) {
         mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
      }
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 24;
      this.toggleEventSubscription(b);
      byte b2 = 21;
      this.toggleEventSubscription(b2);
      byte b3 = 18;
      this.toggleEventSubscription(b3);
      byte b4 = 33;
      this.toggleEventSubscription(b4);
      byte b5 = 40;
      this.toggleEventSubscription(b5);
   }

   @Override
   public void onTick2() {
      String selectedValue = this.mode.getSelectedValue();
      this.setSuffix(selectedValue);
   }

   private boolean shouldSneak(boolean flag) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.matchesSneakTiming(flag)
         && (mc.isNotInGui() || mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof ClickGuiScreen || this.sneakInInv.getValue());
   }

   private boolean matchesSneakTiming(boolean flag) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.sneakTiming.getSelectedValue().equals("Always")
         || this.sneakTiming.getSelectedValue().equals("OnlyWhileStandingStill") && !MoveUtil.hasHorizontalMotion() && !MoveUtil.isMoving()
         || this.sneakTiming.getSelectedValue().equals("OnlyWhileSneakKeyPressed") && flag;
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      this.sneakKeyPressed = event.isSneak();
      if (!mc.thePlayer.isOnLadder()) {
         boolean sneakKeyPressed2 = this.sneakKeyPressed;
         if (this.shouldSneak(sneakKeyPressed2)) {
            String selectedValue = this.mode.getSelectedValue();
            switch (selectedValue) {
               case "Basic":
                  double value = this.sneakForward.getValue();
                  event.setSneakForwardMultiplier(value);
                  double value2 = this.sneakStrafe.getValue();
                  event.setSneakStrafeMultiplier(value2);
                  boolean flag = true;
                  event.setSneak(flag);
                  break;
               case "Legit":
                  if (!ClientManagers.moduleManager.freeCam.isToggled()) {
                     boolean flag2 = true;
                     event.setSneak(flag2);
                  }
            }

            return;
         }
      }
   }

   @Override
   public void onMotion(EventMotion event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      boolean sneakKeyPressed2 = this.sneakKeyPressed;
      boolean flag = this.shouldSneak(sneakKeyPressed2);
      if (!flag && mc.thePlayer.isServerSneakState() && !this.sneakKeyPressed && (this.mode.getSelectedValue().equals("PrePostSpam") || this.mode.getSelectedValue().equals("PrePostSpamOld"))) {
         mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
      }

      if (flag) {
         if (this.mode.getSelectedValue().equals("PrePostSpam") && mc.thePlayer.isServerSneakState()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
         }

         if (this.mode.getSelectedValue().equals("PrePostSpamOld") && mc.thePlayer.isServerSneakState()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
         }
      }
   }

   @Override
   public void onMotion() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      boolean sneakKeyPressed2 = this.sneakKeyPressed;
      if (this.shouldSneak(sneakKeyPressed2)) {
         if (this.mode.getSelectedValue().equals("PrePostSpam") && !mc.thePlayer.isServerSneakState()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
         }

         if (this.mode.getSelectedValue().equals("PrePostSpamOld") && !mc.thePlayer.isServerSneakState()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
         }
      }
   }

   @Override
   public void onOrientCamera(EventOrientCamera event) {
      if (this.spoofEyeHeight.getValue()) {
         float spoofedEyeHeight = this.getSpoofedEyeHeight();
         event.setEyeHeight(spoofedEyeHeight);
      }
   }

   @Override
   public void onSafeWalk(EventSafeWalk event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.removeSneakStop.getValue()) {
         boolean sneakKeyPressed2 = this.sneakKeyPressed;
         if (this.shouldSneak(sneakKeyPressed2)) {
            boolean flag = true;
            event.setDisableSneakSafeWalk(flag);
         }
      }
   }

   private float getSpoofedEyeHeight() {
      float f = 1.62F;
      if (mc.thePlayer.isPlayerSleeping()) {
         f = 0.2F;
      }

      return f;
   }

}
