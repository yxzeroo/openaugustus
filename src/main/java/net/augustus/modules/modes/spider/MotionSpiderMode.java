package net.augustus.modules.modes.spider;

import net.augustus.events.listeners.TickListener;
import net.augustus.settings.SettingVisibilityCondition;
import java.util.function.BiFunction;
import net.augustus.modules.movement.Spider;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class MotionSpiderMode extends AbstractSpiderMode implements TickListener {
   public final StringValue customMotion;
   public final StringValue tickDelay;

   public MotionSpiderMode(Spider spider, DoubleValue doubleValue) {
       super(spider, doubleValue, "Motion");
      StringValue stringValue = new StringValue(this.parentModule, "CustomMotion", 0.3, 0.0, 2.0, 3, this.modeSetting);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.customMotion = stringValue.visibleWhen(condition);
      StringValue stringValue2 = new StringValue(this.parentModule, "TickDelay", 0.0, 0.0, 20.0, 0, this.modeSetting);
      SettingVisibilityCondition condition2 = () -> this.isSelected();
      this.tickDelay = stringValue2.visibleWhen(condition2);
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

   public static BiFunction factory() {
      return (obj, obj2) -> new MotionSpiderMode((Spider)obj, (DoubleValue)obj2);
   }

   @Override
   public void onTick2() {
      int[] controlFlowState22 = AbstractSpiderMode.getControlFlowState2();
      if (this.canClimbWall()) {
         if ((double)mc.thePlayer.ticksExisted % (this.tickDelay.getValue() + 1.0) == 0.0) {
            mc.thePlayer.motionY = (double)((float)this.customMotion.getValue());
         }
      }
   }

}
