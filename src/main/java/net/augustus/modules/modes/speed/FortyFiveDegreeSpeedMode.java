package net.augustus.modules.modes.speed;

import net.augustus.ClientManagers;
import net.augustus.events.EventMotion;
import net.augustus.events.EventMoveEntity;
import net.augustus.events.EventMoveInput;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.input.KeyboardInputUtil;
import net.augustus.utils.rotation.MoveFixMode;
import net.augustus.utils.rotation.RotationController;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.modules.movement.Speed;

public class FortyFiveDegreeSpeedMode extends SpeedMode {
   private boolean yawOffsetApplied = false;

   public FortyFiveDegreeSpeedMode(String str, Speed speed2) {
      super(str, speed2);
   }

   @Override
   public void onEnable() {
      this.yawOffsetApplied = false;
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onPreUpdate() {
   }

   @Override
   public void onPostUpdate() {
   }

   @Override
   public void onMotion(EventMotion event) {
   }

   @Override
   public void onMoveEntity(EventMoveEntity event) {
   }

   @Override
   public void onPreMoveInput(EventMoveInput event) {
      if (this.yawOffsetApplied && this.shouldActivate()) {
         MoveFixMode moveFixMode = MoveFixMode.DEFAULT;
         event.setMoveFixMode(moveFixMode);
         boolean flag = false;
         event.setPreventInstantReverse(flag);
         if (ClientManagers.moduleManager.speed.autoJump.getValue()) {
            boolean flag2 = true;
            event.setJump(flag2);
         }
      }
   }

   @Override
   public void onEarlyTick() {
      this.yawOffsetApplied = false;
      if (this.shouldActivate()) {
         int forwardAxis = KeyboardInputUtil.getForwardAxis();
         int strafeAxis = KeyboardInputUtil.getStrafeAxis();
         boolean isOmnidirectional = this.isOmnidirectional();
         float f;
         if (isOmnidirectional) {
            f = (float)Math.toDegrees(MoveUtil.getDirection((float)forwardAxis, (float)strafeAxis, mc.thePlayer.rotationYaw));
         } else {
            f = mc.thePlayer.rotationYaw;
         }

         if (mc.thePlayer.onGround
            && (
               ClientManagers.moduleManager.speed.autoJump.getValue()
                  || KeyboardInputUtil.isValidKeyCode(mc.gameSettings.keyBindJump.getKeyCode()) && KeyboardInputUtil.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())
            )) {
            RotationController rotationController = ClientManagers.rotationController;
            float f2 = ClientManagers.moduleManager.speed.mouseSensi.getValue() ? RotationUtil.applyYawGcd(f, ClientManagers.rotationController.getPreviousRotation().yaw) : f;
            Speed speed2 = ClientManagers.moduleManager.speed;
            byte b = 10;
            float f3 = f2;
            rotationController.requestYaw(f3, b, speed2);
         } else {
            int n;
            if (isOmnidirectional) {
               n = 1;
            } else {
               n = this.getStrafeSign();
            }

            float f4 = f - (float)(45 * n);
            RotationController rotationController2 = ClientManagers.rotationController;
            float f5 = ClientManagers.moduleManager.speed.mouseSensi.getValue() ? RotationUtil.applyYawGcd(f4, ClientManagers.rotationController.getPreviousRotation().yaw) : f4;
            Speed speed3 = ClientManagers.moduleManager.speed;
            byte b2 = 10;
            float f6 = f5;
            rotationController2.requestYaw(f6, b2, speed3);
         }

         this.yawOffsetApplied = true;
      }
   }

   private boolean shouldActivate() {
      return mc.isNotInGui() && (MoveUtil.isMoving() || !ClientManagers.moduleManager.speed.onlyWhileMove.getValue());
   }

   private int getStrafeSign() {
      int strafeAxis = KeyboardInputUtil.getStrafeAxis();
      return strafeAxis != 0 ? strafeAxis : 1;
   }

   private boolean isOmnidirectional() {
      return ClientManagers.moduleManager.speed.allDirectionSprint.getValue();
   }
}
