package net.augustus.modules.modes.highjump;

import net.augustus.events.EventMotion;
import net.augustus.events.listeners.MotionListener;
import net.augustus.settings.SettingVisibilityCondition;
import java.util.function.BiFunction;
import net.augustus.modules.movement.HighJump;
import net.augustus.settings.DoubleValue;
import net.augustus.settings.StringValue;

public class VanillaHighJumpMode extends HighJumpMode implements MotionListener {
   public final StringValue vanillaHeight;

   public VanillaHighJumpMode(HighJump highJump, DoubleValue doubleValue) {
       super(highJump, doubleValue, "Vanilla");
      StringValue stringValue = new StringValue(this.parentModule, "VanillaHeight", 1.0, 0.42, 5.0, 2, this.modeSetting);
      String str = "Height";
      StringValue stringValue2 = stringValue.named(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.vanillaHeight = stringValue2.visibleWhen(condition);
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new VanillaHighJumpMode((HighJump)obj, (DoubleValue)obj2);
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
      byte b = 18;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onMotion(EventMotion event) {
      if (this.isActivated() && mc.thePlayer.onGround) {
         mc.thePlayer.motionY = this.vanillaHeight.getValue();
      }
   }

}
