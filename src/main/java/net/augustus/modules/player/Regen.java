package net.augustus.modules.player;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.utils.rotation.RotationController;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.rotation.Rotation;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen extends Module implements UpdateListener, PacketListener, TickListener {
   public final BooleansSetting mode;
   public final StringValue health;
   public final StringValue minHunger;
   public final StringValue packets;
   public final BooleanValue groundCheck;
   public final BooleanValue stopSendPacketsIfHpReached;
   public final StringValue minDelay;
   private final BooleanValue notWhileKillAura;
   private final BooleanValue notWhileScaffold;
   private final TimeHelper timeHelper;

   public Regen() {
       super("Regen", "Regeneration", new Color(224, 111, 49), Categorys.PLAYER);
      this.mode = new BooleansSetting(this, "Mode", "Default", new String[]{"Default", "Legit", "Grim-1.17", "/heal"});
      this.health = new StringValue(this, "Health", 20.0, 0.0, 20.0, 0);
      StringValue stringValue = new StringValue(this, "MinHunger", 5.0, 0.0, 20.0, 0);
      SettingVisibilityCondition condition = () -> this.isMinHungerVisible();
      this.minHunger = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(this, "Packets", 100.0, 0.0, 200.0, 0);
      SettingVisibilityCondition condition2 = () -> this.isPacketsVisible();
      this.packets = stringValue2.visibleWhen(condition2);
      BooleanValue booleanValue = new BooleanValue(this, "GroundCheck", false);
      SettingVisibilityCondition condition3 = () -> this.isGroundCheckVisible();
      this.groundCheck = (BooleanValue)booleanValue.withVisibility(condition3);
      BooleanValue booleanValue2 = new BooleanValue(this, "StopSendPacketsIfHpReached", false);
      SettingVisibilityCondition condition4 = () -> {
         return !this.mode.getSelectedValue().equals("/heal");
      };
      this.stopSendPacketsIfHpReached = (BooleanValue)booleanValue2.withVisibility(condition4);
      StringValue stringValue3 = new StringValue(this, "MinDelay", 30000.0, 0.0, 60000.0, 0);
      SettingVisibilityCondition condition5 = () -> {
         return this.mode.getSelectedValue().equals("/heal");
      };
      this.minDelay = stringValue3.visibleWhen(condition5);
      this.notWhileKillAura = new BooleanValue(this, "NotWhileKillAura", false);
      this.notWhileScaffold = new BooleanValue(this, "NotWhileScaffold", true);
      this.timeHelper = new TimeHelper();
   }

   @Override
   public void onEnable() {
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
      short s = 10000;
      byte b3 = 40;
      this.toggleEventSubscription(b3, s);
   }

   @Override
   public void onUpdate() {
      String selectedValue = this.mode.getSelectedValue();
      this.setSuffix(selectedValue);
      if (this.shouldRegenerate()) {
         for (int i = 0; (double)i < this.packets.getValue() && (this.shouldRegenerate() || !this.stopSendPacketsIfHpReached.getValue()); i++) {
            if (!this.groundCheck.getValue() || mc.thePlayer.onGround) {
               i += this.sendRegenPacket();
            }
         }
      }
   }

   @Override
   public void onTick3() {
      if (mc.thePlayer != null) {
         if (this.mode.getSelectedValue().equals("/heal")) {
            TimeHelper timeHelper2 = this.timeHelper;
            double value = this.minDelay.getValue();
            if (timeHelper2.reached(value)
               && (double)mc.thePlayer.getCurrentHealth() < this.health.getValue()
               && (!ClientManagers.moduleManager.killAura.isToggled() || ClientManagers.moduleManager.killAura.target == null || !this.notWhileKillAura.getValue())
               && (!ClientManagers.moduleManager.scaffold.isToggled() || !this.notWhileScaffold.getValue())) {
               mc.thePlayer.sendChatMessage("/heal");
               this.timeHelper.reset();
               return;
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (this.mode.getSelectedValue().equals("Grim-1.17") && this.shouldRegenerate()) {
         Rotation previousRotation = ClientManagers.rotationController.getPreviousRotation();
         Rotation currentRotation = ClientManagers.rotationController.getCurrentRotation();
         if (previousRotation.equalsRotation(currentRotation)) {
            RotationController rotationController = ClientManagers.rotationController;
            float yaw = ClientManagers.rotationController.getPreviousRotation().yaw;
            float f = ClientManagers.rotationController.getPreviousRotation().pitch > 0.0F ? ClientManagers.rotationController.getPreviousRotation().pitch + 0.2F : ClientManagers.rotationController.getPreviousRotation().pitch - 0.2F;
            float f2;
            float f3 = f2 = ClientManagers.rotationController.getPreviousRotation().pitch;
            float f4 = f;
            float f5 = yaw;
            Rotation rotation = RotationUtil.applyGcdFix(f5, f4, f3, f2);
            byte b = 1;
            Rotation rotation2 = rotation;
            rotationController.requestRotation(rotation2, b, this);
         }
      }
   }

   @Override
   public void onPacket2(EventPacket event) {
      Packet packet = event.getPacket();
      if (mc.thePlayer != null && mc.theWorld != null) {
         if (this.mode.getSelectedValue().equals("Grim-1.17") && this.shouldRegenerate()) {
            if (packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
               for (int i = 0; (double)i < this.packets.getValue() && (this.shouldRegenerate() || !this.stopSendPacketsIfHpReached.getValue()); i++) {
                  if (!this.groundCheck.getValue() || mc.thePlayer.onGround) {
                     mc.thePlayer.sendQueue.addToSendQueueUnchecked(packet);
                  }
               }
            }
         }
      }
   }

   private int sendRegenPacket() {
      String selectedValue = this.mode.getSelectedValue();
      switch (selectedValue) {
         case "Default":
            mc.thePlayer.sendQueue.addToSendQueueUnchecked(new C03PacketPlayer(true));
            break;
         case "Legit":
            mc.thePlayer.onUpdateWalkingPlayer(false);
      }

      return 0;
   }

   private boolean shouldRegenerate() {
      return (double)mc.thePlayer.getFoodStats().getFoodLevel() > this.minHunger.getValue()
         && (double)mc.thePlayer.getCurrentHealth() < this.health.getValue()
         && (!ClientManagers.moduleManager.killAura.isToggled() || ClientManagers.moduleManager.killAura.target == null || !this.notWhileKillAura.getValue())
         && (!ClientManagers.moduleManager.scaffold.isToggled() || !this.notWhileScaffold.getValue());
   }

   private boolean isGroundCheckVisible() {
      return !this.mode.getSelectedValue().equals("/heal");
   }

   private boolean isPacketsVisible() {
      return !this.mode.getSelectedValue().equals("/heal");
   }

   private boolean isMinHungerVisible() {
      return !this.mode.getSelectedValue().equals("/heal");
   }

}
