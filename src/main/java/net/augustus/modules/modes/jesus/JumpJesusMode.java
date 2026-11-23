package net.augustus.modules.modes.jesus;

import net.augustus.events.listeners.TickListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.utils.MoveUtil;
import net.augustus.modules.movement.Jesus;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;
import net.minecraft.util.MathHelper;

public class JumpJesusMode extends AbstractJesusMode implements TickListener {
   public final StringValue motion;
   public final BooleanValue onlyMove;
   private final TimeHelper timeHelper;

   public JumpJesusMode(Jesus jesus, DoubleValue doubleValue) {
      super(jesus, doubleValue, "Jump");
      AbstractJesusMode.getControlFlowState2();
      StringValue stringValue = new StringValue(this.parentModule, "Motion", 0.6, 0.01, 2.0, 2, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.motion = stringValue.visibleWhen(condition);
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "OnlyMove", false, this.modeSetting);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.onlyMove = (BooleanValue)booleanValue.withVisibility(condition2);
      this.timeHelper = new TimeHelper();
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 40;
      this.toggleEventSubscription(b);
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
      if (this.shouldApplyJesus() && mc.thePlayer.isInWater()) {
         if (!this.onlyMove.getValue() || MoveUtil.isMoving()) {
            mc.thePlayer.motionY = this.motion.getValue();
            long l = 200L;
            if (this.timeHelper.reached2(l) && mc.thePlayer.isSprinting()) {
               float f = mc.thePlayer.rotationYaw * (float) (Math.PI / 180.0);
               mc.thePlayer.motionX = mc.thePlayer.motionX - (double)(MathHelper.sin(f) * 0.2F);
               mc.thePlayer.motionZ = mc.thePlayer.motionZ + (double)(MathHelper.cos(f) * 0.2F);
               MoveUtil.strafe();
            }
         }

         this.timeHelper.reset();
      }
   }

}
