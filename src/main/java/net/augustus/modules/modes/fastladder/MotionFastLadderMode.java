package net.augustus.modules.modes.fastladder;

import net.augustus.events.EventLadder;
import net.augustus.events.listeners.LadderListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.modules.movement.FastLadder;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class MotionFastLadderMode extends FastLadderMode implements LadderListener {
   public final StringValue motionMotionY;
   public final StringValue motionTickDelay;

   public MotionFastLadderMode(FastLadder fastLadder, DoubleValue doubleValue) {
      super(fastLadder, doubleValue, "Motion");
      FastLadderMode.getControlFlowState2();
      StringValue stringValue = new StringValue(this.parentModule, "MotionMotionY", 0.4, 0.2, 2.0, 3, this.modeSetting);
      String str = "MotionY";
      stringValue = stringValue.named(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.motionMotionY = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(this.parentModule, "MotionTickDelay", 0.0, 0.0, 20.0, 0, this.modeSetting);
      String str2 = "TickDelay";
      StringValue stringValue3 = stringValue2.named(str2);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.motionTickDelay = stringValue3.visibleWhen(condition2);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 15;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onLadder(EventLadder event) {
      int[] controlFlowState22 = FastLadderMode.getControlFlowState2();
      if (((FastLadder)this.parentModule).isOnClimbable() && (double)mc.thePlayer.ticksExisted % (this.motionTickDelay.getValue() + 1.0) == 0.0) {
         double value = this.motionMotionY.getValue();
         event.setMotionY(value);
      }
   }

}
