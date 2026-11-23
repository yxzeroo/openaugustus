package net.augustus.modules.modes.jesus;

import net.augustus.events.EventMoveInput;
import net.augustus.events.EventOrientCamera;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.OrientCameraListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.MoveUtil;
import net.augustus.utils.rotation.RotationUtil;
import net.augustus.modules.movement.Jesus;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;

public class IntaveNewJesusMode extends AbstractJesusMode implements TickListener, MoveInputListener, OrientCameraListener {
   public final BooleanValue hideSneakAnimation;

   public IntaveNewJesusMode(Jesus jesus, DoubleValue doubleValue) {
       super(jesus, doubleValue, "IntaveNew");
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "HideSneakAnimation", true, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.hideSneakAnimation = (BooleanValue)booleanValue.withVisibility(condition);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
      byte b2 = 21;
      this.toggleEventSubscription(b2);
      byte b3 = 24;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onTick2() {
      int[] controlFlowState22 = AbstractJesusMode.getControlFlowState2();
      if (this.shouldApplyJesus()
         && mc.thePlayer.onGround
         && !mc.thePlayer.isCollidedHorizontally
         && mc.thePlayer.isInWater()
         && RotationUtil.isNearCardinalYaw((float)Math.toDegrees(MoveUtil.getDirection(mc.thePlayer.rotationYaw)))
         && MoveUtil.isMoving()
         && mc.thePlayer.hurtTime < 7) {
         mc.thePlayer.moveFlying(mc.thePlayer.moveStrafing, mc.thePlayer.moveForward, 0.0995F);
      }
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      int[] controlFlowState22 = AbstractJesusMode.getControlFlowState2();
      if (this.shouldApplyJesus()
         && mc.thePlayer.isInWater()
         && mc.thePlayer.hurtTime < 7
         && RotationUtil.isNearCardinalYaw((float)Math.toDegrees(MoveUtil.getDirection(mc.thePlayer.rotationYaw)))
         && (event.getMoveForward() != 0.0F || event.getMoveStrafe() != 0.0F)) {
         boolean flag = true;
         event.setSneak(flag);
         double d = 0.6;
         event.setSneakForwardMultiplier(d);
      }
   }

   @Override
   public void onOrientCamera(EventOrientCamera event) {
      int[] controlFlowState22 = AbstractJesusMode.getControlFlowState2();
      if (this.shouldApplyJesus() && mc.thePlayer.isInWater() && this.hideSneakAnimation.getValue()) {
         float f = 1.62F;
         event.setEyeHeight(f);
      }
   }

}
