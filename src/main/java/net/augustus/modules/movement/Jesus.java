package net.augustus.modules.movement;

import java.awt.Color;
import java.util.function.BiFunction;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.events.EventLiquidJump;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.ClientManagers;
import net.augustus.settings.Setting;
import net.augustus.settings.ModeSetting;
import net.augustus.utils.MoveUtil;
import net.augustus.modules.modes.jesus.IntaveNewJesusMode;
import net.augustus.modules.modes.jesus.DolphinJesusMode;
import net.augustus.modules.modes.jesus.SolidJesusMode;
import net.augustus.modules.modes.jesus.IntaveJesusMode;
import net.augustus.modules.modes.jesus.MotionModifierJesusMode;
import net.augustus.modules.modes.jesus.IgnoreJesusMode;
import net.augustus.modules.modes.jesus.Swim113JesusMode;
import net.augustus.modules.modes.jesus.SpeedJesusMode;
import net.augustus.modules.modes.jesus.JumpJesusMode;
import net.augustus.events.listeners.LiquidJumpListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.CustomSubModeFactory;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.minecraft.block.BlockLiquid;

public class Jesus extends Module implements TickListener, LiquidJumpListener {
   public final ModeSetting mode;
   public final BooleanValue disableatsneakJump;
   private final BooleansSetting ignoreFlowingWaterForce;
   public final BooleanValue customWaterUpMotion;
   public final StringValue customWaterUpMotionValue;
   public final BooleanValue customLavaUpMotion;
   public final StringValue customLavaUpMotionValue;

   public Jesus() {
       super("Jesus", "JesusChristus", new Color(39, 103, 146), Categorys.MOVEMENT);
      BiFunction[] biFunctions = new BiFunction[]{
         (obj, obj2) -> new SolidJesusMode((Jesus)obj, (DoubleValue)obj2),
         (obj3, obj4) -> new SpeedJesusMode((Jesus)obj3, (DoubleValue)obj4),
         null,
         null,
         null,
         null,
         null,
         null
      };
      biFunctions[2] = (obj5, obj6) -> new JumpJesusMode((Jesus)obj5, (DoubleValue)obj6);
      biFunctions[3] = (obj7, obj8) -> new Swim113JesusMode((Jesus)obj7, (DoubleValue)obj8);
      biFunctions[4] = (obj9, obj10) -> new DolphinJesusMode((Jesus)obj9, (DoubleValue)obj10);
      biFunctions[5] = (obj11, obj12) -> new IntaveJesusMode((Jesus)obj11, (DoubleValue)obj12);
      biFunctions[6] = (obj13, obj14) -> new IntaveNewJesusMode((Jesus)obj13, (DoubleValue)obj14);
      biFunctions[7] = (obj15, obj16) -> new IgnoreJesusMode((Jesus)obj15, (DoubleValue)obj16);
      BiFunction[] biFunctions2 = biFunctions;
      ModeSetting modeSetting = new ModeSetting(this, "Mode", "Solid", biFunctions2);
      CustomSubModeFactory customSubModeFactory = (obj17, obj18, obj19) -> new MotionModifierJesusMode((Jesus)obj17, (DoubleValue)obj18, (String)obj19);
      this.mode = modeSetting.enableCustomModes(customSubModeFactory);
      this.disableatsneakJump = new BooleanValue(this, "DisableAtSneak/Jump", false);
      this.ignoreFlowingWaterForce = new BooleansSetting(
         this, "IgnoreFlowingWaterForce", "Never", new String[]{"Never", "OnlyAtMovement", "OnlyWhileStanding", "Everytime"}
      );
      BooleanValue booleanValue = new BooleanValue(this, "CustomWaterUpMotion", false);
      SettingVisibilityCondition condition = () -> {
         Setting[] cachedSettings = Fly.getCachedSettings();
         return !this.mode.getSelectedValue().equals("1.13");
      };
      this.customWaterUpMotion = (BooleanValue)booleanValue.withVisibility(condition);
      StringValue stringValue = new StringValue(this, "CustomWaterUpMotionValue", 0.042, 0.03, 0.15, 5);
      SettingVisibilityCondition condition2 = () -> this.isCustomWaterMotionValueVisible();
      this.customWaterUpMotionValue = stringValue.visibleWhen(condition2);
      this.customLavaUpMotion = new BooleanValue(this, "CustomLavaUpMotion", false);
      StringValue stringValue2 = new StringValue(this, "CustomLavaUpMotionValue", 0.042, 0.03, 0.15, 5);
      BooleanValue customLavaUpMotion2 = this.customLavaUpMotion;
      this.customLavaUpMotion.getClass();
      BooleanValue booleanValue2 = customLavaUpMotion2;
      SettingVisibilityCondition condition3 = () -> booleanValue2.getValue();
      this.customLavaUpMotionValue = stringValue2.visibleWhen(condition3);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
      byte b2 = 67;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onTick2() {
      String selectedValue = this.mode.getSelectedValue();
      this.setSuffix(selectedValue);
   }

   public boolean shouldIgnoreWaterFlow() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.isToggled()
         && (
            this.ignoreFlowingWaterForce.getSelectedValue().equalsIgnoreCase("OnlyAtMovement") && MoveUtil.isMoving()
               || this.ignoreFlowingWaterForce.getSelectedValue().equalsIgnoreCase("OnlyWhileStanding") && !MoveUtil.isMoving()
               || this.ignoreFlowingWaterForce.getSelectedValue().equalsIgnoreCase("Everytime")
         );
   }

   public boolean isLiquidSolidEnabled() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.isLicenseTimestampValid()
         && (
            !ClientManagers.moduleManager.jesus.disableatsneakJump.getValue()
               || !mc.gameSettings.keyBindSneak.isKeyDown()
                  && (
                     !mc.gameSettings.keyBindJump.isKeyDown()
                        || !(mc.theWorld.getBlockState(mc.thePlayer.posX, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ).getBlock() instanceof BlockLiquid)
                  )
         );
   }

   @Override
   public void onLiquidJump(EventLiquidJump event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.customWaterUpMotion.getValue()) {
         double value = this.customWaterUpMotionValue.getValue();
         event.setWaterMotionY(value);
      }

      if (this.customLavaUpMotion.getValue()) {
         double value2 = this.customLavaUpMotionValue.getValue();
         event.setLavaMotionY(value2);
      }
   }

   private boolean isCustomWaterMotionValueVisible() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.customWaterUpMotion.getValue() && this.customWaterUpMotion.isVisible();
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
