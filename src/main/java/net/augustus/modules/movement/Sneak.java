package net.augustus.modules.movement;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventOrientCamera;
import net.augustus.ClientManagers;
import net.augustus.events.listeners.MotionListener;
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
import net.augustus.settings.StringValue;
import net.augustus.settings.DoubleValue;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class Sneak extends Module implements OrientCameraListener, MoveInputListener, MotionListener, SafeWalkListener, TickListener {
   public final StringValue sneakTiming;
   public final StringValue mode;
   public final DoubleValue sneakForward;
   public final DoubleValue sneakStrafe;
   public final BooleanValue removeSneakStop;
   public final BooleanValue sneakInInv;
   public final BooleanValue spoofEyeHeight;
   private boolean sneakKeyPressed;

   public Sneak() {
       super("Sneak", "Schleichen", new Color(245, 114, 66), Categorys.MOVEMENT);
      this.sneakTiming = new StringValue(
         this, "SneakTiming", "OnlyWhileSneakKeyPressed", new String[]{"OnlyWhileSneakKeyPressed", "OnlyWhileStandingStill", "Always"}
      );
      this.mode = new StringValue(this, "Mode", "Legit", new String[]{"Legit", "Basic", "PrePostSpam", "PrePostSpamOld"});
      DoubleValue stringValue = new DoubleValue(this, "SneakForward", 1.0, 0.3, 1.0, 2);
      SettingVisibilityCondition condition = () -> {
         return this.mode.getSelectedValue().equals("Basic");
      };
      this.sneakForward = stringValue.visibleWhen(condition);
      DoubleValue stringValue2 = new DoubleValue(this, "SneakStrafe", 1.0, 0.3, 1.0, 2);
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
      if (!this.sneakKeyPressed
         && mc.thePlayer != null
         && mc.thePlayer.isServerSneakState()
         && (this.mode.getSelectedValue().equals("PrePostSpam") || this.mode.getSelectedValue().equals("PrePostSpamOld"))) {
         mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
      }
   }

   @Override
   public void updateEventSubscriptions() {
      this.toggleEventSubscription((byte)24);
      this.toggleEventSubscription((byte)21);
      this.toggleEventSubscription((byte)18);
      this.toggleEventSubscription((byte)33);
      this.toggleEventSubscription((byte)40);
   }

   @Override
   public void onTick2() {
      this.setSuffix(this.mode.getSelectedValue());
   }

   private boolean shouldSneak(boolean flag) {
      return this.matchesSneakTiming(flag)
         && (mc.isNotInGui() || mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof ClickGuiScreen || this.sneakInInv.getValue());
   }

   private boolean matchesSneakTiming(boolean flag) {
      return this.sneakTiming.getSelectedValue().equals("Always")
         || this.sneakTiming.getSelectedValue().equals("OnlyWhileStandingStill") && !MoveUtil.hasHorizontalMotion() && !MoveUtil.isMoving()
         || this.sneakTiming.getSelectedValue().equals("OnlyWhileSneakKeyPressed") && flag;
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      this.sneakKeyPressed = event.isSneak();
      if (!mc.thePlayer.isOnLadder()) {
         if (this.shouldSneak(this.sneakKeyPressed)) {
            switch (this.mode.getSelectedValue()) {
               case "Basic":
                  event.setSneakForwardMultiplier(this.sneakForward.getValue());
                  event.setSneakStrafeMultiplier(this.sneakStrafe.getValue());
                  event.setSneak(true);
                  break;
               case "Legit":
                  if (!ClientManagers.moduleManager.freeCam.isToggled()) {
                     event.setSneak(true);
                  }
            }

            return;
         }
      }
   }

   @Override
   public void onMotion(EventMotion event) {
      boolean flag = this.shouldSneak(this.sneakKeyPressed);
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
      if (this.shouldSneak(this.sneakKeyPressed)) {
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
         event.setEyeHeight(this.getSpoofedEyeHeight());
      }
   }

   @Override
   public void onSafeWalk(EventSafeWalk event) {
      if (this.removeSneakStop.getValue()) {
         if (this.shouldSneak(this.sneakKeyPressed)) {
            event.setDisableSneakSafeWalk(true);
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
