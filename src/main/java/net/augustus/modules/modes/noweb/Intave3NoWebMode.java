package net.augustus.modules.modes.noweb;

import net.augustus.events.EventMoveInput;
import net.augustus.events.EventOrientCamera;
import net.augustus.events.EventWebSlowDown;
import net.augustus.events.listeners.MoveInputListener;
import net.augustus.events.listeners.OrientCameraListener;
import net.augustus.events.listeners.WebSlowDownListener;
import net.augustus.settings.SettingVisibilityCondition;
import net.augustus.modules.movement.NoWeb;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;

public class Intave3NoWebMode extends AbstractNoWebMode implements MoveInputListener, WebSlowDownListener, OrientCameraListener {
   public final BooleanValue intave3HideSneakAnimation;

   public Intave3NoWebMode(NoWeb noWeb, DoubleValue doubleValue) {
       super(noWeb, doubleValue, "Intave3");
      BooleanValue booleanValue = new BooleanValue(this.parentModule, "Intave3HideSneakAnimation", true, this.modeSetting);
      String str = "HideSneakAnimation";
      booleanValue = (BooleanValue)booleanValue.withDisplayName(str);
      SettingVisibilityCondition condition = () -> this.isSelected();
      this.intave3HideSneakAnimation = (BooleanValue)booleanValue.withVisibility(condition);
   }

   @Override
   public void onEnable() {
      super.onEnable();
   }

   @Override
   public void updateEventSubscriptions() {
      super.updateEventSubscriptions();
      byte b = 21;
      this.toggleEventSubscription(b);
      byte b2 = 49;
      this.toggleEventSubscription(b2);
      byte b3 = 24;
      this.toggleEventSubscription(b3);
   }

   @Override
   public void onDisable() {
   }

   @Override
   public void onMoveInput3(EventMoveInput event) {
      if (mc.thePlayer.isInWeb()) {
         boolean flag = true;
         event.setSneak(flag);
      }
   }

   @Override
   public void onWebSlowDown(EventWebSlowDown event) {
      boolean flag = true;
      event.setKeepHorizontalMotion(flag);
      double d = 1.0;
      event.setHorizontalFactor(d);
   }

   @Override
   public void onOrientCamera(EventOrientCamera event) {
      if (mc.thePlayer.isInWeb() && this.intave3HideSneakAnimation.getValue()) {
         float f = 1.62F;
         event.setEyeHeight(f);
      }
   }

}
