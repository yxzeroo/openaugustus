package net.augustus.modules.movement;

import java.awt.Color;
import java.util.function.BiFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventPacket;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.settings.Setting;
import net.augustus.settings.ModeSetting;
import net.augustus.modules.modes.noweb.Intave3NoWebMode;
import net.augustus.modules.modes.noweb.MotionModifierNoWebMode;
import net.augustus.modules.modes.noweb.IntaveNoWebMode;
import net.augustus.modules.modes.noweb.Intave2NoWebMode;
import net.augustus.modules.modes.noweb.IntaveOldNoWebMode;
import net.augustus.modules.modes.noweb.SolidNoWebMode;
import net.augustus.modules.modes.noweb.Intave1473b2NoWebMode;
import net.augustus.modules.modes.noweb.IgnoreNoWebMode;
import net.augustus.modules.modes.noweb.Intave1477NoWebMode;
import net.augustus.modules.modes.noweb.Polar1NoWebMode;
import net.augustus.events.listeners.MoveEntityListener;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.UpdateListener;
import net.augustus.events.EventMoveInput;
import net.augustus.settings.CustomSubModeFactory;
import net.augustus.events.EventMoveEntity;
import net.augustus.events.listeners.PacketListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.SettingGroup;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class NoWeb extends Module implements MoveEntityListener, MoveInputListener, UpdateListener, PacketListener {
   public final ModeSetting modes;
   public final BooleanValue noDown;
   private final BooleanValue noNoDownWhileVelocity;
   public final BooleanValue upDown;
   private final BooleanValue noHorizontalMoveWhileUpAndDown;
   public final DoubleValue upSpeed;
   public final DoubleValue downSpeed;
   public final TimeHelper blockPlaceTimer;
   private final TimeHelper inWebTimer;

   public NoWeb() {
       super("NoWeb", "KeineSpinnenweben", new Color(179, 252, 255), Categorys.MOVEMENT);
      BiFunction[] biFunctions = new BiFunction[9];
      biFunctions[0] = (obj, obj2) -> new Intave1477NoWebMode((NoWeb)obj, (SettingGroup)obj2);
      biFunctions[1] = (obj3, obj4) -> new Intave1473b2NoWebMode((NoWeb)obj3, (SettingGroup)obj4);
      biFunctions[2] = (obj5, obj6) -> new Intave3NoWebMode((NoWeb)obj5, (SettingGroup)obj6);
      biFunctions[3] = (obj7, obj8) -> new Intave2NoWebMode((NoWeb)obj7, (SettingGroup)obj8);
      biFunctions[4] = (obj9, obj10) -> new IntaveNoWebMode((NoWeb)obj9, (SettingGroup)obj10);
      biFunctions[5] = (obj11, obj12) -> new IntaveOldNoWebMode((NoWeb)obj11, (SettingGroup)obj12);
      biFunctions[6] = (obj13, obj14) -> new IgnoreNoWebMode((NoWeb)obj13, (SettingGroup)obj14);
      biFunctions[7] = (obj15, obj16) -> new SolidNoWebMode((NoWeb)obj15, (SettingGroup)obj16);
      biFunctions[8] = (obj17, obj18) -> new Polar1NoWebMode((NoWeb)obj17, (SettingGroup)obj18);
      this.modes = new ModeSetting(this, "Modes", "Intave", biFunctions).enableCustomModes((obj19, obj20, obj21) -> new MotionModifierNoWebMode((NoWeb)obj19, (SettingGroup)obj20, (String)obj21));
      BooleanValue booleanValue = new BooleanValue(this, "NoDown", false);
      SettingVisibilityCondition condition = () -> {
         return !this.modes.getSelectedValue().equals("Ignore") && !this.modes.getSelectedValue().equals("Solid");
      };
      this.noDown = (BooleanValue)booleanValue.withVisibility(condition);
      this.noNoDownWhileVelocity = (BooleanValue)new BooleanValue(this, "NoNoDownWhileVelocity", false).withVisibility(() -> this.isNoNoDownWhileVelocityVisible());
      this.upDown = (BooleanValue)new BooleanValue(this, "UpDown", false).withVisibility(() -> this.isUpDownVisible());
      BooleanValue booleanValue4 = new BooleanValue(this, "NoHorizontalMoveWhileUpAndDown", false);
      SettingVisibilityCondition condition4 = () -> {
         return this.upDown.getValue() && this.upDown.isVisible();
      };
      this.noHorizontalMoveWhileUpAndDown = (BooleanValue)booleanValue4.withVisibility(condition4);
      this.upSpeed = new DoubleValue(this, "UpSpeed", 2.4, 0.01, 4.0, 2).visibleWhen(() -> this.isUpSpeedVisible());
      DoubleValue stringValue2 = new DoubleValue(this, "DownSpeed", 2.4, 0.01, 4.0, 2);
      SettingVisibilityCondition condition6 = () -> {
         return this.upDown.getValue() && !this.modes.getSelectedValue().equals("Ignore") && !this.modes.getSelectedValue().equals("Solid");
      };
      this.downSpeed = stringValue2.visibleWhen(condition6);
      this.blockPlaceTimer = new TimeHelper();
      this.inWebTimer = new TimeHelper();
   }

   @Override
   public void onEnable() {
      if (mc.thePlayer != null) {
         mc.thePlayer.setSpeedInAir(0.02F);
         mc.thePlayer.setSpeedOnGround(0.1F);
      }
   }

   @Override
   public void onDisable() {
      if (mc.thePlayer != null) {
         mc.thePlayer.setSpeedInAir(0.02F);
         mc.thePlayer.setSpeedOnGround(0.1F);
      }
   }

   @Override
   public void updateEventSubscriptions() {
   }

   @Override
   public void onUpdate2() {
      this.setSuffix(this.modes.getSelectedValue());
      if (!mc.thePlayer.isInWeb()) {
         this.inWebTimer.reset();
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (this.noHorizontalMoveWhileUpAndDown.getValue() && this.upDown.getValue()) {
         if (this.inWebTimer.reached2(200L) && (event.isJump() || event.isSneak())) {
            event.setMoveForward(0.0F);
            event.setMoveStrafe(0.0F);
         }
      }
   }

   @Override
   public void onMoveEntity(EventMoveEntity event) {
      if (mc.thePlayer.isInWeb() && this.noDown.getValue()) {
         if (this.isLicenseTimestampValid()
            && mc.thePlayer.motionY < 0.0
            && (!this.upDown.getValue() || !mc.gameSettings.keyBindSneak.isKeyDown())
            && (mc.thePlayer.hurtTime <= 0 || !this.noNoDownWhileVelocity.getValue())) {
            event.setY(0.0);
         }
      }
   }

   @Override
   public void onPacket2(EventPacket event) {
      Packet packet = event.getPacket();
      if (packet instanceof C08PacketPlayerBlockPlacement) {
         C08PacketPlayerBlockPlacement packet2 = (C08PacketPlayerBlockPlacement)packet;
         if (packet2.getPlacedBlockDirection() < 5 && packet2.getStack() != null && packet2.getStack().getItem() instanceof ItemBlock) {
            this.blockPlaceTimer.reset();
         }
      }
   }

   private boolean isUpSpeedVisible() {
      return this.upDown.getValue() && !this.modes.getSelectedValue().equals("Ignore") && !this.modes.getSelectedValue().equals("Solid");
   }

   private boolean isUpDownVisible() {
      return !this.modes.getSelectedValue().equals("Ignore") && !this.modes.getSelectedValue().equals("Solid");
   }

   private boolean isNoNoDownWhileVelocityVisible() {
      return this.noDown.isVisible() && this.noDown.getValue();
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
