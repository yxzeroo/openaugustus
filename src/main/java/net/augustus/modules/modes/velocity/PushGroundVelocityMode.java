package net.augustus.modules.modes.velocity;

import net.augustus.events.listeners.UpdateListener;
import net.augustus.settings.RangeSetting;
import net.augustus.settings.SettingVisibilityCondition;
import java.util.function.BiFunction;
import net.augustus.modules.combat.Velocity;
import net.augustus.settings.DoubleValue;

public class PushGroundVelocityMode extends VelocityMode implements UpdateListener {
   private final RangeSetting endOnGroundPush;

   public PushGroundVelocityMode(Velocity velocity, DoubleValue doubleValue) {
       super(velocity, doubleValue, "PushGround");
      RangeSetting rangeSetting = (new RangeSetting(this.parentModule, "EndOnGroundPush", "StartOnGroundPush", 1.0, 10.0, 1.0, 10.0, 0, this.modeSetting)).withMaxFirst();
      String str = "EndHurtTime";
      String str2 = "StartHurtTime";
      RangeSetting rangeSetting2 = rangeSetting.withBoundNames(str2, str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.endOnGroundPush = rangeSetting2.visibleWhen(condition);
   }

   @Override
   public void updateEventSubscriptions() {
      byte b = 43;
      this.toggleEventSubscription(b);
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public static BiFunction factory() {
      return (obj, obj2) -> new PushGroundVelocityMode((Velocity)obj, (DoubleValue)obj2);
   }

   @Override
   public void onUpdate2() {
      if (this.canApplyVelocity()) {
         if ((double)mc.thePlayer.hurtTime >= this.endOnGroundPush.getMinValue()) {
            double hurtTime = (double)mc.thePlayer.hurtTime;
            if (hurtTime <= this.endOnGroundPush.getMaxValue()) {
               mc.thePlayer.onGround = true;
            }
         }
      }
   }

}
