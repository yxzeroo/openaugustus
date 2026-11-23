package net.augustus.modules.movement;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.events.listeners.RenderListener;
import net.augustus.settings.Setting;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

public class Clip extends Module implements RenderListener, PacketListener {
   private final BooleansSetting mode;
   private final BooleanValue autoDisable;
   private final StringValue time;
   private final TimeHelper timeHelper;

   public Clip() {
       super("Clip", "Clip", new Color(135, 168, 50), Categorys.MOVEMENT);
      this.mode = new BooleansSetting(this, "Mode", "IntaveInstant", new String[]{"Intave1", "Intave2", "IntaveInstant"});
      this.autoDisable = new BooleanValue(this, "AutoDisable", true);
      StringValue stringValue = new StringValue(this, "Time", 50.0, 0.0, 1000.0, 0);
      BooleanValue autoDisable2 = this.autoDisable;
      this.autoDisable.getClass();
      BooleanValue booleanValue = autoDisable2;
      SettingVisibilityCondition condition = () -> booleanValue.getValue();
      this.time = stringValue.visibleWhen(condition);
      this.timeHelper = new TimeHelper();
   }

   @Override
   public void onEnable() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (mc.thePlayer != null) {
         mc.thePlayer.motionY = 0.9;
         this.timeHelper.reset();
         if (this.mode.getSelectedValue().equals("IntaveInstant")) {
            mc.getTimer().timerSpeed = 100.0F;
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.mode.getSelectedValue().equals("IntaveInstant")) {
         mc.getTimer().timerSpeed = 1.0F;
      }
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 32;
      this.toggleEventSubscription(b);
      byte b2 = 26;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onPacket(EventPacket event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      Packet packet = event.getPacket();
      if (packet instanceof S08PacketPlayerPosLook && this.mode.getSelectedValue().equals("IntaveInstant")) {
         event.cancel();
      }

      if (packet instanceof S08PacketPlayerPosLook && this.mode.getSelectedValue().equals("Intave1")) {
         ((S08PacketPlayerPosLook)packet).setResponseYOffset(1.0F);
      }

      if (packet instanceof S32PacketConfirmTransaction && (this.mode.getSelectedValue().equals("Intave2") || this.mode.getSelectedValue().equals("IntaveInstant"))) {
         event.cancel();
      }
   }

   @Override
   public void onRender(float f) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.autoDisable.getValue()) {
         TimeHelper timeHelper2 = this.timeHelper;
         double value = this.time.getValue();
         if (timeHelper2.reached(value)) {
            this.timeHelper.reset();
            boolean flag = false;
            this.setToggled(flag);
         }
      }
   }

}
