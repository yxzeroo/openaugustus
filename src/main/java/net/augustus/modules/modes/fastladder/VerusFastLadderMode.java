package net.augustus.modules.modes.fastladder;

import net.augustus.events.EventLadder;
import net.augustus.events.listeners.LadderListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.modules.movement.FastLadder;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class VerusFastLadderMode extends FastLadderMode implements LadderListener {
   public final StringValue verusMotionY;

   public VerusFastLadderMode(FastLadder fastLadder, DoubleValue doubleValue) {
       super(fastLadder, doubleValue, "Verus");
      StringValue stringValue = new StringValue(this.parentModule, "VerusMotionY", 0.5, 0.2, 2.0, 3, this.modeSetting);
      String str = "MotionY";
      StringValue stringValue2 = stringValue.named(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.verusMotionY = stringValue2.visibleWhen(condition);
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
      if (((FastLadder)this.parentModule).isOnClimbable() && mc.thePlayer.ticksExisted % 3 != 0) {
         double value = this.verusMotionY.getValue();
         event.setMotionY(value);
      }
   }

}
