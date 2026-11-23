package net.augustus.modules.modes.jesus;

import net.augustus.events.listeners.TickListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.modules.movement.Jesus;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class SpeedJesusMode extends AbstractJesusMode implements TickListener {
   private final StringValue speedSpeed;
   private final BooleanValue speedModifyMotionY;

   public SpeedJesusMode(Jesus jesus, DoubleValue doubleValue) {
       super(jesus, doubleValue, "Speed");
      StringValue stringValue = new StringValue(this.parentModule, "SpeedSpeed", 1.0, 0.0, 20.0, 2, this.modeSetting);
      String str = "Speed";
      StringValue stringValue2 = stringValue.named(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.speedSpeed = stringValue2.visibleWhen(condition);
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "SpeedModifyMotionY", true, this.modeSetting);
      String str2 = "ModifyMotionY";
      BooleanValue booleanValue2 = (BooleanValue)booleanValue.withDisplayName(str2);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.speedModifyMotionY = (BooleanValue)booleanValue2.withVisibility(condition2);
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
      if (this.shouldApplyJesus()) {
         if (mc.thePlayer.isInWater()) {
            if (!mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown() && this.speedModifyMotionY.getValue()) {
               mc.thePlayer.motionY = 0.0;
            }

            mc.thePlayer.moveFlying(mc.thePlayer.moveStrafing, mc.thePlayer.moveForward, (float)(this.speedSpeed.getValue() / 100.0));
         }
      }
   }

}
