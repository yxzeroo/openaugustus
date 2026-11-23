package net.augustus.modules.modes.jesus;

import net.augustus.events.EventLiquidJump;
import net.augustus.events.EventLiquidPhysics;
import net.augustus.events.listeners.LiquidJumpListener;
import net.augustus.events.listeners.LiquidPhysicsListener;
import net.augustus.events.listeners.TickListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.modules.movement.Jesus;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;
import net.augustus.utils.TimeHelper;

public class IgnoreJesusMode extends AbstractJesusMode implements LiquidPhysicsListener, LiquidJumpListener, TickListener {
   private final BooleanValue ignoreWater;
   private final BooleanValue jumpInWater;
   private final BooleanValue ignoreLava;
   private final BooleanValue jumpInLava;
   private final BooleanValue disableIgnoranceTemporaryIfDoubleJumpKeyPressed;
   private final BooleanValue disableIfYouFallingDown;
   private final StringValue motionYToDisable;
   private final TimeHelper timeHelper;
   private boolean temporarilyDisabled;

   public IgnoreJesusMode(Jesus jesus, DoubleValue doubleValue) {
       super(jesus, doubleValue, "Ignore");
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "Ignore water", true, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.ignoreWater = (BooleanValue)booleanValue.withVisibility(condition);
      AbstractJesusMode.getControlFlowState2();
      BooleanValue booleanValue2 = new BooleanValue(this.parentModule, "Jump in water", true, this.modeSetting);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.jumpInWater = (BooleanValue)booleanValue2.withVisibility(condition2);
      booleanValue2 = new BooleanValue(this.parentModule, "Ignore lava", true, this.modeSetting);
      SettingVisibilityCondition condition3 = () -> this.isSelected();
      this.ignoreLava = (BooleanValue)booleanValue2.withVisibility(condition3);
      booleanValue2 = new BooleanValue(this.parentModule, "Jump in lava", true, this.modeSetting);
      SettingVisibilityCondition condition4 = () -> this.isSelected();
      this.jumpInLava = (BooleanValue)booleanValue2.withVisibility(condition4);
      BooleanValue booleanValue3 = new BooleanValue(this.parentModule, "Disable ignorance temporary if double jump key pressed", false, this.modeSetting);
      SettingVisibilityCondition condition5 = () -> this.isSelected();
      this.disableIgnoranceTemporaryIfDoubleJumpKeyPressed = (BooleanValue)booleanValue3.withVisibility(condition5);
      BooleanValue booleanValue4 = new BooleanValue(this.parentModule, "Disable if you falling down", false, this.modeSetting);
      SettingVisibilityCondition condition6 = () -> this.isSelected();
      this.disableIfYouFallingDown = (BooleanValue)booleanValue4.withVisibility(condition6);
      StringValue stringValue = new StringValue(this.parentModule, "Motion y to disable", -0.7, -2.0, 0.0, 2, this.modeSetting);
      SettingVisibilityCondition condition7 = () -> this.isDisableMotionYVisible();
      this.motionYToDisable = stringValue.visibleWhen(condition7);
      this.timeHelper = new TimeHelper();
      this.temporarilyDisabled = false;
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 66;
      this.toggleEventSubscription(b);
      byte b2 = 67;
      this.toggleEventSubscription(b2);
      byte b3 = 40;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onLiquidPhysics(EventLiquidPhysics event) {
      int[] controlFlowState22 = AbstractJesusMode.getControlFlowState2();
      if (!this.temporarilyDisabled && this.shouldApplyJesus()) {
         if (this.ignoreWater.getValue()) {
            boolean flag = true;
            event.setIgnoreWater(flag);
         }

         if (this.ignoreLava.getValue()) {
            boolean flag2 = true;
            event.setIgnoreLava(flag2);
         }
      }
   }

   @Override
   public void onLiquidJump(EventLiquidJump event) {
      int[] controlFlowState22 = AbstractJesusMode.getControlFlowState2();
      if (!this.temporarilyDisabled && this.shouldApplyJesus()) {
         if (this.jumpInWater.getValue()) {
            boolean flag = true;
            event.setIgnoreWater(flag);
         }

         if (this.jumpInLava.getValue()) {
            boolean flag2 = true;
            event.setIgnoreLava(flag2);
         }
      }
   }

   @Override
   public void onTick2() {
      int[] controlFlowState22 = AbstractJesusMode.getControlFlowState2();
      if (!mc.thePlayer.isInWater() && mc.thePlayer.onGround) {
         this.temporarilyDisabled = false;
      }

      if (this.disableIfYouFallingDown.getValue() && mc.thePlayer.motionY < this.motionYToDisable.getValue() && mc.thePlayer.isInWater()) {
         this.temporarilyDisabled = true;
      }

      if (this.disableIgnoranceTemporaryIfDoubleJumpKeyPressed.getValue()) {
         if (mc.gameSettings.keyBindJump.getPressTime() > 0) {
            long l = 250L;
            if (!this.timeHelper.reached2(l)) {
               this.temporarilyDisabled = !this.temporarilyDisabled;
            }

            this.timeHelper.reset();
         }

         mc.gameSettings.keyBindJump.setPressTime(0);
      }
   }

   private boolean isDisableMotionYVisible() {
      int[] controlFlowState22 = AbstractJesusMode.getControlFlowState2();
      return this.isSelected() && this.disableIfYouFallingDown.getValue();
   }

}
