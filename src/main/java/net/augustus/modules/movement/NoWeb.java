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
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
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
   public final StringValue upSpeed;
   public final StringValue downSpeed;
   public final TimeHelper blockPlaceTimer;
   private final TimeHelper inWebTimer;

   public NoWeb() {
       super("NoWeb", "KeineSpinnenweben", new Color(179, 252, 255), Categorys.MOVEMENT);
      BiFunction[] biFunctions = new BiFunction[9];
      biFunctions[0] = (obj, obj2) -> new Intave1477NoWebMode((NoWeb)obj, (DoubleValue)obj2);
      biFunctions[1] = (obj3, obj4) -> new Intave1473b2NoWebMode((NoWeb)obj3, (DoubleValue)obj4);
      biFunctions[2] = (obj5, obj6) -> new Intave3NoWebMode((NoWeb)obj5, (DoubleValue)obj6);
      biFunctions[3] = (obj7, obj8) -> new Intave2NoWebMode((NoWeb)obj7, (DoubleValue)obj8);
      biFunctions[4] = (obj9, obj10) -> new IntaveNoWebMode((NoWeb)obj9, (DoubleValue)obj10);
      biFunctions[5] = (obj11, obj12) -> new IntaveOldNoWebMode((NoWeb)obj11, (DoubleValue)obj12);
      biFunctions[6] = (obj13, obj14) -> new IgnoreNoWebMode((NoWeb)obj13, (DoubleValue)obj14);
      biFunctions[7] = (obj15, obj16) -> new SolidNoWebMode((NoWeb)obj15, (DoubleValue)obj16);
      biFunctions[8] = (obj17, obj18) -> new Polar1NoWebMode((NoWeb)obj17, (DoubleValue)obj18);
      BiFunction[] biFunctions2 = biFunctions;
      ModeSetting modeSetting = new ModeSetting(this, "Modes", "Intave", biFunctions2);
      CustomSubModeFactory customSubModeFactory = (obj19, obj20, obj21) -> new MotionModifierNoWebMode((NoWeb)obj19, (DoubleValue)obj20, (String)obj21);
      this.modes = modeSetting.enableCustomModes(customSubModeFactory);
      BooleanValue booleanValue = new BooleanValue(this, "NoDown", false);
      SettingVisibilityCondition condition = () -> {
         Setting[] cachedSettings = Fly.getCachedSettings();
         return !this.modes.getSelectedValue().equals("Ignore") && !this.modes.getSelectedValue().equals("Solid");
      };
      this.noDown = (BooleanValue)booleanValue.withVisibility(condition);
      BooleanValue booleanValue2 = new BooleanValue(this, "NoNoDownWhileVelocity", false);
      SettingVisibilityCondition condition2 = () -> this.isNoNoDownWhileVelocityVisible();
      this.noNoDownWhileVelocity = (BooleanValue)booleanValue2.withVisibility(condition2);
      BooleanValue booleanValue3 = new BooleanValue(this, "UpDown", false);
      SettingVisibilityCondition condition3 = () -> this.isUpDownVisible();
      this.upDown = (BooleanValue)booleanValue3.withVisibility(condition3);
      BooleanValue booleanValue4 = new BooleanValue(this, "NoHorizontalMoveWhileUpAndDown", false);
      SettingVisibilityCondition condition4 = () -> {
         Setting[] cachedSettings2 = Fly.getCachedSettings();
         return this.upDown.getValue() && this.upDown.isVisible();
      };
      this.noHorizontalMoveWhileUpAndDown = (BooleanValue)booleanValue4.withVisibility(condition4);
      StringValue stringValue = new StringValue(this, "UpSpeed", 2.4, 0.01, 4.0, 2);
      SettingVisibilityCondition condition5 = () -> this.isUpSpeedVisible();
      this.upSpeed = stringValue.visibleWhen(condition5);
      StringValue stringValue2 = new StringValue(this, "DownSpeed", 2.4, 0.01, 4.0, 2);
      SettingVisibilityCondition condition6 = () -> {
         Setting[] cachedSettings3 = Fly.getCachedSettings();
         return this.upDown.getValue() && !this.modes.getSelectedValue().equals("Ignore") && !this.modes.getSelectedValue().equals("Solid");
      };
      this.downSpeed = stringValue2.visibleWhen(condition6);
      this.blockPlaceTimer = new TimeHelper();
      this.inWebTimer = new TimeHelper();
   }

   @Override
   public void onEnable() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (mc.thePlayer != null) {
         mc.thePlayer.setSpeedInAir(0.02F);
         mc.thePlayer.setSpeedOnGround(0.1F);
      }
   }

   @Override
   public void onDisable() {
      Setting[] cachedSettings = Fly.getCachedSettings();
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
      Setting[] cachedSettings = Fly.getCachedSettings();
      String selectedValue = this.modes.getSelectedValue();
      this.setSuffix(selectedValue);
      if (!mc.thePlayer.isInWeb()) {
         this.inWebTimer.reset();
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.noHorizontalMoveWhileUpAndDown.getValue() && this.upDown.getValue()) {
         long l = 200L;
         if (this.inWebTimer.reached2(l) && (event.isJump() || event.isSneak())) {
            float f = 0.0F;
            event.setMoveForward(f);
            float f2 = 0.0F;
            event.setMoveStrafe(f2);
         }
      }
   }

   @Override
   public void onMoveEntity(EventMoveEntity event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (mc.thePlayer.isInWeb() && this.noDown.getValue()) {
         if (this.isLicenseTimestampValid()
            && mc.thePlayer.motionY < 0.0
            && (!this.upDown.getValue() || !mc.gameSettings.keyBindSneak.isKeyDown())
            && (mc.thePlayer.hurtTime <= 0 || !this.noNoDownWhileVelocity.getValue())) {
            double d = 0.0;
            event.setY(d);
         }
      }
   }

   @Override
   public void onPacket2(EventPacket event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      Packet packet = event.getPacket();
      if (packet instanceof C08PacketPlayerBlockPlacement) {
         C08PacketPlayerBlockPlacement packet2 = (C08PacketPlayerBlockPlacement)packet;
         if (packet2.getPlacedBlockDirection() < 5 && packet2.getStack() != null && packet2.getStack().getItem() instanceof ItemBlock) {
            this.blockPlaceTimer.reset();
         }
      }
   }

   private boolean isUpSpeedVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.upDown.getValue() && !this.modes.getSelectedValue().equals("Ignore") && !this.modes.getSelectedValue().equals("Solid");
   }

   private boolean isUpDownVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return !this.modes.getSelectedValue().equals("Ignore") && !this.modes.getSelectedValue().equals("Solid");
   }

   private boolean isNoNoDownWhileVelocityVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.noDown.isVisible() && this.noDown.getValue();
   }

   private boolean isLicenseTimestampValid() {
      byte[] timestampBytes = LicenseTimestampStore.timestampBytes;
      Fly.getCachedSettings();
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
