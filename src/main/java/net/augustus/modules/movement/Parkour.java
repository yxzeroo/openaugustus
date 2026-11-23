package net.augustus.modules.movement;

import java.awt.Color;
import net.augustus.modules.Categorys;
import net.augustus.modules.Module;
import net.augustus.utils.input.KeyboardInputUtil;
import net.augustus.utils.LicenseTimestampStore;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.ClientManagers;
import net.augustus.settings.Setting;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.rotation.MoveFixMode;
import net.augustus.utils.rotation.RotationController;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.EventMoveInput;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.settings.BooleanValue;

public class Parkour extends Module implements MoveInputListener, TickListener {
   private final BooleanValue diagonal;
   private final BooleanValue mouseSensi;

   public Parkour() {
       super("Parkour", "Parkur", new Color(245, 103, 167), Categorys.MOVEMENT);
      this.diagonal = new BooleanValue(this, "Diagonal", false);
      BooleanValue booleanValue = new BooleanValue(this, "MouseSensi", true);
      BooleanValue diagonal2 = this.diagonal;
      this.diagonal.getClass();
      BooleanValue booleanValue2 = diagonal2;
      SettingVisibilityCondition condition = () -> booleanValue2.getValue();
      this.mouseSensi = (BooleanValue)booleanValue.withVisibility(condition);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 21;
      this.toggleEventSubscription(b);
      byte b2 = 40;
      this.toggleEventSubscription(b2);
   }

   @Override
   public void onTick() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.diagonal.getValue() && this.canPerformParkour()) {
         if (this.shouldJumpAtEdge()
            || mc.thePlayer.onGround && KeyboardInputUtil.isValidKeyCode(mc.gameSettings.keyBindJump.getKeyCode()) && KeyboardInputUtil.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
            RotationController rotationController = ClientManagers.rotationController;
            float f = this.mouseSensi.getValue() ? RotationUtil.applyYawGcd(mc.thePlayer.rotationYaw, ClientManagers.rotationController.getPreviousRotation().yaw) : mc.thePlayer.rotationYaw;
            byte b = 11;
            float f2 = f;
            rotationController.requestYaw(f2, b, this);
         } else {
            int strafeDirectionSign = this.getStrafeDirectionSign();
            RotationController rotationController2 = ClientManagers.rotationController;
            float f3 = this.mouseSensi.getValue()
               ? RotationUtil.applyYawGcd(mc.thePlayer.rotationYaw - (float)(45 * strafeDirectionSign), ClientManagers.rotationController.getPreviousRotation().yaw)
               : mc.thePlayer.rotationYaw - (float)(45 * strafeDirectionSign);
            byte b2 = 11;
            float f4 = f3;
            rotationController2.requestYaw(f4, b2, this);
         }
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      Setting[] cachedSettings = Fly.getCachedSettings();
      if (this.shouldJumpAtEdge()) {
         boolean flag = true;
         event.setJump(flag);
      }

      if (this.diagonal.getValue() && this.canPerformParkour()) {
         MoveFixMode moveFixMode = MoveFixMode.DEFAULT;
         event.setMoveFixMode(moveFixMode);
      }
   }

   private boolean canPerformParkour() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return mc.isNotInGui() && MoveUtil.isMoving() && !mc.gameSettings.keyBindSneak.isKeyDown();
   }

   private boolean shouldJumpAtEdge() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      return this.canPerformParkour()
         && mc.thePlayer.onGround
         && this.isLicenseTimestampValid()
         && mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001)).isEmpty();
   }

   private int getStrafeDirectionSign() {
      Setting[] cachedSettings = Fly.getCachedSettings();
      int strafeAxis = KeyboardInputUtil.getStrafeAxis();
      return strafeAxis != 0 ? strafeAxis : 1;
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
